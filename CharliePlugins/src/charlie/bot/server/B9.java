package charlie.bot.server;

import charlie.advisor.BasicStrategy;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IAdvisor;
import charlie.plugin.IBot;
import charlie.plugin.IPlayer;
import charlie.util.Play;
import java.util.Random;

import charlie.util.Constant;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author rebecca murphy
 */
public class B9 implements IBot {
    protected Hid hid;
    private Hand myHand;
    protected Dealer dealer;
    protected Seat seat;
    protected IAdvisor advisor;
    protected Hid dealerHid;
    protected Card dealerUpCard;
    protected boolean myTurn = false;
    
    protected HashMap<Hid,Hand> hands = new HashMap<>();
    private final Logger LOG = LoggerFactory.getLogger(B9.class);
    
     /**
     * Constructor
     */
    
    public B9 () {
    }
    
     /**
     * Gets the bots hand.
     * @return Hand
     */
    
    @Override
    public Hand getHand() {
        return myHand;
    }
    
     /**
     * Sets the dealer to which the bot responds.
     * @param dealer 
     */
    
    @Override
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }
    
     /**
     * Sits the bot at a given seat.
     * @param seat Seat
     */

    @Override
    public void sit(Seat seat) {
       this.seat = seat;
       this.hid = new Hid(seat, Constant.BOT_MIN_BET, 0);   
       this.myHand = new Hand(this.hid);
    }
    
     /**
     * Starts a game.
     * @param hids Hand ids
     * @param shoeSize Shoe size at game start before cards dealt.
     */

    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        for(Hid hid_: hids) {
            hands.put(hid_,new Hand(hid_));
            
            if(hid_.getSeat() == Seat.DEALER)
                this.dealerHid = hid_;
        }     
    }
    
    /**
     * Ends a game.
     * @param shoeSize Shoe size at game end after cards dealt.
     */

    @Override
    public void endGame(int shoeSize) {
         LOG.info("received endGame shoeSize = "+shoeSize);
    }

     /**
     * Deals a card.
     * @param hid Target hand id
     * @param card Card
     * @param values Hard and soft values of a hand.
     */
    
    @Override
    public void deal(Hid hid, Card card, int[] values) {
         LOG.info("got card = "+card+" hid = "+hid);
        
        // If its the dealer's turn 
            if(hid.getSeat() ==Seat.DEALER){
            this.dealerHid = hid;
            this.dealerUpCard = card;
            LOG.info("dealer upcard set " + card);
        }
        if (myHand.getHid() == hid && myHand.size() >2 && !(myHand.isBroke())){
            myTurn = true;
            play(hid);
        }

            
    }

    @Override
    public void insure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bust(Hid hid) {
        //add alert to tell player hand is broken 
    }

    @Override
    public void win(Hid hid) {
        LOG.info("WIN " + hid);
    }

    @Override
    public void blackjack(Hid hid) {
       LOG.info("BLACKJACK " + hid);
    }

    @Override
    public void charlie(Hid hid) {
       LOG.info("CHARLIE " + hid);
    }

    @Override
    public void lose(Hid hid) {
        LOG.info("LOSE " + hid);
    }

    @Override
    public void push(Hid hid) {
        LOG.info("PUSH " + hid);
    }

    @Override
    public void shuffling() {
        LOG.info("SHUFFLING");
    }

    @Override
    public void play(Hid hid) {
        LOG.info("turn hid = "+hid); 
        Random random = new Random();
        final IPlayer bot = this;
        final Hid botHid = hid;
        final int DELAY = random.nextInt(3001-1000) +1000;
        
        Runnable thread = new Runnable() {
        @Override 
        public void run() {
            Play advice;
            try {
                Thread.sleep(DELAY);
            }
            catch (InterruptedException ex) {
              LOG.info("Thread Error");
              }
            //double checks to make sure it is my turn
            if (botHid == myHand.getHid()){
                
                advice = BasicStrategy.getPlay(myHand, dealerUpCard);
                
                if (advice == Play.DOUBLE_DOWN && myHand.size() == 2)
                    dealer.doubleDown(bot, botHid);
                else if (advice == Play.STAY)
                    dealer.stay(bot, botHid);
                else
                    //if double down is advised on more than 2 cards 
                    //or split is advised, or hit is advised
                    dealer.hit(bot, botHid);
                }
            
            }
                
        };
        new Thread(thread).start();

    myTurn = false;
    }
    
    
}
