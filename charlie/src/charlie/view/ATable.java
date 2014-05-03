/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package charlie.view;

import charlie.plugin.IUi;
import charlie.view.sprite.TurnIndicator;
import charlie.GameFrame;
import charlie.actor.Courier;
import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.card.Card;
import charlie.card.HoleCard;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.JPanel;
import charlie.util.Point;
import charlie.dealer.Seat;
import charlie.plugin.IGerty;
import charlie.plugin.ISideBetView;
import charlie.util.Constant;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the main table panel.
 *
 * @author Ron Coleman
 */
public final class ATable extends JPanel implements Runnable, IUi, MouseListener {
    private final Logger LOG = LoggerFactory.getLogger(ATable.class);
    protected Random ran = new Random();
    protected String[] b9s = {"Apollo", "Zeus", "Talos"};
    protected String[] n6s = {"Hera", "Athena", "Hecate"};
    protected AHandsManager you = new AHandsManager("You", new Point(225, 225));
    protected AHandsManager dealer = new AHandsManager("Dealer", new Point(225, 0));
    protected AHandsManager b9 = new AHandsManager(b9s[ran.nextInt(b9s.length)], new Point(450, 150));
    protected AHandsManager n6 = new AHandsManager(n6s[ran.nextInt(n6s.length)], new Point(25, 150));
    protected AHandsManager[] handsManager = {you, dealer, b9, n6};
    protected TurnIndicator turnSprite = new TurnIndicator();
    protected AHand turn = null;
    protected HashMap<Seat, AHandsManager> seats = new HashMap<Seat, AHandsManager>() {
        {
            put(Seat.YOU, you);
            put(Seat.RIGHT, b9);
            put(Seat.LEFT, n6);
            put(Seat.DEALER, dealer);
        }
    };
    private final HashMap<Seat, AMoneyManager> monies = new HashMap<Seat, AMoneyManager>() {
        {
            put(Seat.YOU, new AMoneyManager());
            put(Seat.RIGHT, new ABotMoneyManager());
            put(Seat.LEFT, new ABotMoneyManager());
        }
    };
    
    protected HashMap<Hid, AHand> manos = new HashMap<>();
    protected Thread gameLoop;
    protected static Color COLOR_FELT = new Color(0, 153, 100);
    protected final int DELAY = 50;
    protected final GameFrame frame;
    protected boolean bettable = false;
    protected boolean gameOver = true;
    protected int shoeSize;
    protected Image instrImg;
    protected Image shoeImg;
    protected Image trayImg;
    protected ABurnCard burnCard = new ABurnCard();
    protected int numHands;
    protected int loserCount;
    protected int pushCount;
    protected int winnerCount;
    protected ISideBetView sideBetView;
    protected Properties props; 
    protected IGerty gerty;
    private Card holeCard;
    private int[] holeValues;
    protected Courier courier;

    /**
     * Constructor
     *
     * @param frame Main game frame
     * @param parent Parent panel containing this one.
     */
    public ATable(GameFrame frame, JPanel parent) {
        this.frame = frame;

        setSize(parent.getWidth(), parent.getHeight());

        init();
    }

    /**
     * Initializes custom table components.
     */
    public void init() {
        setBackground(COLOR_FELT);

        setDoubleBuffered(true);

        this.addMouseListener(this);

        this.addNotify();

        this.instrImg = new ImageIcon(Constant.DIR_IMGS + "dealer-stands-0.png").getImage();
        this.shoeImg = new ImageIcon(Constant.DIR_IMGS + "shoe-0.png").getImage();
        this.trayImg = new ImageIcon(Constant.DIR_IMGS + "tray-0.png").getImage();
        
        this.loadConfig();
    }
    
    /**
     * Clears table of old bets, etc.
     */
    public void clear() {
        winnerCount = loserCount = pushCount = 0;
        
        for (AHandsManager animator : seats.values()) {
            animator.clear();
        }
        
        for (Hid hid : manos.keySet()) {
            AMoneyManager money = monies.get(hid.getSeat());

            // Skip dealer since it doesn't have a money manager
            if (money == null)
                continue;

            money.undubble();
        }
        
        if(sideBetView != null)
            sideBetView.starting();     
        
        holeCard = null;
        
        holeValues = null;
    }

    /**
     * Gets the main upBet amount on the table.<br>
 This should only be requested when making a upBet but before the table
 has been clearBeted
     * @return Bet amount
     */
    public Integer getBetAmt() {
        AMoneyManager money = this.monies.get(Seat.YOU);
        
        Integer amt = money.getWager();
        
        return amt;
    }
    
    /**
     * Gets the side upBet amount on the table.
     * @return Side upBet amount
     */
    public Integer getSideAmt() {
        int amt = 0;
        
        if(this.sideBetView != null)
            amt = this.sideBetView.getAmt();
        
        LOG.info("side bet = "+amt);
        
        return amt;
    }

    /**
     * Makes the paint method get invoked.
     */
    @Override
    public void addNotify() {
        super.addNotify();

        gameLoop = new Thread(this);

        gameLoop.start();
    }

    /**
     * Paints the display some time after repainted invoked.
     *
     * @param g_
     */
    @Override
    public synchronized void paint(Graphics g_) {
        super.paint(g_);

        Graphics2D g = (Graphics2D) g_;

        // Render the paraphenelia
        g.drawImage(this.instrImg, 140, 208, this);
        g.drawImage(this.shoeImg, 540, 5, this);
        g.drawImage(this.trayImg, 430, 5, this);

        // Render the upBet on the table
        this.monies.get(Seat.YOU).render(g);

        // Render the hands
        for (int i = 0; i < handsManager.length; i++) {
            handsManager[i].render(g);
        }
        
        // Render the side upBet
        if(sideBetView != null)
            sideBetView.render(g);

        // Render the burn card
        if(burnCard.isVisible())
            burnCard.render(g);
        
        if(gerty != null)
            gerty.render(g);
        
        // Java tool related stuff
        Toolkit.getDefaultToolkit().sync();

        g_.dispose();
    }

    /**
     * Updates the table.
     */
    public synchronized void update() {
        // Update every hand at the table
        for (int i = 0; i < handsManager.length; i++) {
            handsManager[i].update();
        }
        
        // Update the side upBet
        if(sideBetView != null)
            sideBetView.update(); 
        
        // If it's my turn, I didn't break, and my cards have landed,
        // then enable enable to play
        if (turn != null
                && turn.hid.getSeat() == Seat.YOU
                && !turn.isBroke()
                && you.isReady()
                && dealer.isReady()
                && !gameOver) {
            // "trucking" => I'm running
            frame.enableTrucking(true);

            // Enable the buttons
            frame.enablePlay(true);
        }
        
        burnCard.update();
        
        if(gerty != null)
            gerty.update();
       
    }

    /**
     * Repaints the display
     */
    public void render() {
        repaint();
    }

    /**
     * Runs the game loop
     */
    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {
            update();

            render();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    /**
     * Sets the amt.
     *
     * @param amt Bankroll
     */
    public void setBankroll(Double amt) {
        this.monies.get(Seat.YOU).setBankroll(amt);
    }

    /**
     * Enables upBetting (i.e., the keys work)
     *
     * @param betting True or false
     */
    public void enableBetting(boolean betting) {
        bettable = betting;
    }

    /**
     * Double upBet on the table.
     *
     * @param hid Hand id
     */
    public void dubble(Hid hid) {
        AMoneyManager money = this.monies.get(hid.getSeat());

        money.dubble();
    }

    /**
     * Sets the turn for a hand.
     *
     * @param hid Hand id
     */
    @Override
    public void turn(final Hid hid) {
        AHand hand = manos.get(hid);

        if (hid.getSeat() == Seat.DEALER) {
            // Reveal dealer's hole card
            hand.get(0).flip();
            
            // Inform gety since we bypassed sending this to gerty during the deal
            // This is really only important at this stage for counting cards.
            if(gerty != null)
                gerty.deal(hid, holeCard, holeValues);
            
            // Disable the "turn" signal
            // Note: "turn" will be null on dealer blackjack in which case
            // nobody has played.
            if (turn != null)
                turn.enablePlaying(false);
            
            // Disable player input
            this.frame.enableTrucking(false);
            this.frame.enablePlay(false);
        } else {           
            // Disable old hand
            if (turn != null)
                turn.enablePlaying(false);

            // Enable new hand
            turn = hand;

            turn.enablePlaying(true);

            // If turn is NOT my hand, disable my hand
            boolean enable = true;

            if (hid.getSeat() != Seat.YOU) {
                enable = false;
            }

            if (gerty == null) {
                this.frame.enableTrucking(enable);
                this.frame.enablePlay(enable);
            }
            else {
                new Thread(new Runnable() { 
                    @Override
                    public void run() {
                        gerty.play(hid);
                    }
                }).start();

            }
            
            SoundFactory.play(Effect.TURN);
        }
    }

    /**
     * Receives a hit for a hand.
     *
     * @param hid Hand id
     * @param card Card hitting the hand
     * @param handValues Hand values
     */
    @Override
    public synchronized void deal(final Hid hid, final Card card, final int[] handValues) {
        // Get the burn card off the table
        burnCard.clear();
        
        SoundFactory.play(Effect.DEAL);

        AHand hand = manos.get(hid);

        hand.setValues(handValues);

        // If card is null, this is not a "real" hit but only
        // updating the respective hand value.
        if (card == null)
            return;

        // Convert card to an animated card and hit the hand
        ACard acard = ACard.animate(card);

        hand.hit(acard);
        
        // Let the advisor, if it exists, know what's going on
        frame.deal(hid, card, handValues);
        
        // Let Gerty, if it exists, know what's going on except for the hole card
        // which we'll send to Gerty when it's the dealer's turn.
        if(card instanceof HoleCard) {
            this.holeValues = handValues;
            this.holeCard = card;
        }
        
        if (gerty != null && !(card instanceof HoleCard)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    gerty.deal(hid, card, handValues);
                }
            }).start();
        }
            
    }

    /**
     * Busts a hand.
     *
     * @param hid Hand id
     */
    @Override
    public void bust(Hid hid) {
        LOG.info("BUST for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Bust);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.decrease(hid.getAmt());

        if (hid.getSeat() != Seat.DEALER) {
            SoundFactory.play(Effect.BUST);
            loserCount++;
        }
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(gerty != null)
            gerty.bust(hid);
    }

    /**
     * Updates hand with winning outcome.
     *
     * @param hid Hand id
     */
    @Override
    public void win(Hid hid) {
        LOG.info("WIN for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Win);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.increase(hid.getAmt());

        winnerCount++;
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(gerty != null)
            gerty.win(hid);
    }

    /**
     * Updates hand with loosing outcome.
     *
     * @param hid Hand id
     */
    @Override
    public void lose(Hid hid) {
        LOG.info("LOOSE for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Lose);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.decrease(hid.getAmt());

        loserCount++;
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(gerty != null)
            gerty.lose(hid);
    }

    /**
     * Updates hand with push outcome.
     *
     * @param hid Hand id
     */
    @Override
    public void push(Hid hid) {
        LOG.info("PUSH for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Push);

        AMoneyManager money = this.monies.get(hid.getSeat());
        
        money.increase(hid.getSideAmt());
        
        ++pushCount;
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(gerty != null)
            gerty.push(hid);
    }

    /**
     * Updates hand with Blackjack outcome.
     *
     * @param hid Hand id
     */
    @Override
    public void blackjack(Hid hid) {
        LOG.info("BJ for hid = "+hid+" amt = "+hid.getAmt());
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Blackjack);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.increase(hid.getAmt());

        if (hid.getSeat() != Seat.DEALER) {
            SoundFactory.play(Effect.BJ);

            winnerCount++;
        }
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(gerty != null)
            gerty.blackjack(hid);
    }

    /**
     * Updates hand with Charlie outcome.
     *
     * @param hid Hand id
     */
    @Override
    public void charlie(Hid hid) {
        LOG.info("CHARLIE for hid = "+hid);
        
        AHand hand = manos.get(hid);

        hand.setOutcome(AHand.Outcome.Charlie);

        AMoneyManager money = this.monies.get(hid.getSeat());

        money.increase(hid.getAmt());

        SoundFactory.play(Effect.CHARLIE);

        winnerCount++;
        
        if(sideBetView != null)
            sideBetView.ending(hid);
        
        if(gerty != null)
            gerty.charlie(hid);
    }

    /**
     * Starts a game. Note: we received the initial player bankroll during login
     * which is handled by GameFrame.
     *
     * @param shoeSize Shoe size
     * @param hids Hand ids
     */
    @Override
    public void starting(List<Hid> hids, int shoeSize) {
        clear();
        
        numHands = hids.size();
//        
//        for (AHandsManager animator : seats.values()) {
//            animator.clear();
//        }

        this.shoeSize = shoeSize;

        // It's nobdy's turn...yet
        turn = null;

        // Game definitely not over
        gameOver = false;

        // Create corresponding (animated) hands
        for (Hid hid : hids) {
            AHand hand =
                    hid.getSeat() == Seat.DEALER ? new ADealerHand(hid) : new AHand(hid);

            // Assign hand to it's manager
            AHandsManager animator = seats.get(hid.getSeat());

            animator.add(hand);

            // Put the hand in its mano cache for quick look up later
            manos.put(hid, hand);
        }
        
        if(gerty != null)
            gerty.startGame(hids, shoeSize);
    }

    /**
     * Signals end of a game.
     *
     * @param shoeSize Shoe size
     */
    @Override
    public void ending(final int shoeSize) {
        LOG.info("num hands = "+numHands);
        LOG.info("winner count = "+winnerCount);
        LOG.info("looser count = "+loserCount);
        LOG.info("push count = "+pushCount);
        
        // Game now over
        gameOver = true;

        // Update the shoe size
        this.shoeSize = shoeSize;   
        
        if (gerty == null) {
            // Enable betting and dealing again
            frame.enableDeal(true);
            this.bettable = true;

            // Disable play -- we must wait for player to upBet and request deal
            frame.enablePlay(false);
        }
        else {
            // Run gerty in worker thread in event there's a need for
            // endGame to wait between games.
            new Thread(new Runnable() { 
                @Override
                public void run() {
                    try {
                        gerty.endGame(shoeSize);
                        
                        Thread.sleep(2000);
                        
                        gerty.go();
                    } catch (InterruptedException ex) {
                        
                    }
                }
            }).start();

        }

        if (winnerCount == numHands - 1) {
            SoundFactory.play(Effect.NICE);
        } else if (loserCount == numHands - 1) {
            SoundFactory.play(Effect.TOUGH);
        } else if (pushCount == numHands - 1) {
            SoundFactory.play(Effect.PUSH);
        }
    }

    /**
     * Handles shuffling from dealer.
     */
    @Override
    public void shuffling() {
        burnCard.launch();
        
        SoundFactory.play(Effect.SHUFFLING);
        
        if(gerty != null)
            gerty.shuffling();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Register upBets.
     *
     * @param e Event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(gerty != null)
            return;
        
        if (!bettable)
            return;

        // Get the coordinates of the mouse and let upBet manager
        // determine whether this is a upBet and how much.
        int x = e.getX();
        int y = e.getY();

        // Place main upBet on left-click
        if(SwingUtilities.isLeftMouseButton(e))
            monies.get(Seat.YOU).click(x, y);
        
        // Ditto for the side upBet system on right-click
        if(sideBetView != null && SwingUtilities.isRightMouseButton(e))
            sideBetView.click(x, y);
    }

    /**
     * Toggles the button image from pressed to up.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(gerty != null)
            return;
        
        monies.get(Seat.YOU).unclick();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
    /**
     * Sets the courier.
     * @param courier Courier
     */
    @Override
    public void setCourier(Courier courier) {
        this.courier = courier;
    }
    
    /**
     * Tests whether the gerty has been installed.
     * @return 
     */
    public boolean autopilotEngaged() {
        return gerty != null;
    }
    
    /**
     * Starts the gerty.
     */
    public void startAutopilot() {
        this.gerty.setMoneyManager(this.monies.get(Seat.YOU));
        this.gerty.setCourier(courier);       
        if (gerty != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    gerty.go();
                }
            }).start();
        }
    }
    
    /**
     * Loads the plug-ins.
     */
    protected void loadConfig() { 
        try {
            // Open the configuration file
            props = new Properties();
            props.load(new FileInputStream("charlie.props"));

        } catch (IOException ex) {
            LOG.error("failed to open charlie.props: "+ex);
            return;
        }
        
        loadSideBetSystem();
        loadAutoPilot();
    } 
    
    /**
     * Loads the side upBet system based on the property file setting.
     */
    protected void loadSideBetSystem() {
        try {
            String className = props.getProperty(Constant.PROPERTY_SIDE_BET_VIEW);
            
            if (className == null)
                return;
            
            Class<?> clazz;
            
            clazz = Class.forName(className);
            
            this.sideBetView = (ISideBetView) clazz.newInstance();
            
            this.sideBetView.setMoneyManager(this.monies.get(Seat.YOU)); 
            
            LOG.info("successfully loaded side bet view");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.error("failed to load side bet view: "+ex);
        }
    }
    
    /**
     * Loads the gerty based on the property file setting.
     */
    protected void loadAutoPilot() {
        try {
            String className = props.getProperty(Constant.PROPERTY_GERTY);
            
            if (className == null)
                return;
            
            Class<?> clazz;
            
            clazz = Class.forName(className);
            
            this.gerty = (IGerty) clazz.newInstance();
                        
            LOG.info("successfully loaded autopilot");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.error("failed to load autopilog: "+ex);
        }
    }    
}
