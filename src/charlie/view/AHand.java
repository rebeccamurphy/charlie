
package charlie.view;

import charlie.card.Hid;
import charlie.view.sprite.TurnSprite;
import charlie.util.Constant;
import charlie.util.Point;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Animated hand
 * @author Ron Coleman, Ph.D.
 */
public class AHand {
    public enum Outcome { None, Blackjack, Win, Push, Loose, Charlie, Bust };
    
    // Offsets to stagger the cards
    protected final static int HOME_OFFSET_X = 15;
    protected final static int HOME_OFFSET_Y = 7;
    
    
    protected List<ACard> cards = new ArrayList<>();
    protected int[] values;
    
    protected Font font = new Font("Arial", Font.PLAIN, 18);
    protected Color fontColor = Color.YELLOW; //new Color(247,115,7);
    
    protected Point home;
    protected String name = "NOBODY";
    protected Outcome outcome = Outcome.None; 
    
    protected TurnSprite turnSprite = new TurnSprite();
    protected final Hid hid;

    public AHand(Hid hid) {
        this.hid = hid;
        this.home = new Point(0, 0);
    }

//    public AHand(ACard card) {
//        this();
//
//        add(card);
//    }
//
//    public AHand(Point home) {
//        this.home = home;
//    }
//
//    public AHand(Point home, ACard card) {
//        this(home);
//
//        add(card);
//    }

    public void update() {
        int sz = cards.size();
        for (int i = 0; i < sz; i++) {
            ACard card = cards.get(i);
            card.update();
        }
    }

    public void render(Graphics2D g) {
        if (cards.isEmpty())
            return;

        for (int i = 0; i < cards.size(); i++) {
            ACard card = cards.get(i);
            card.render(g);
        }
        
        String text = this.getText();
        
        renderState(g,text);
    }

    protected void renderState(Graphics2D g,String text) {
        ACard card = cards.get(0);
        
        int wind = turnSprite.getImage().getWidth(null);
        
        int xoff = card.getImage().getWidth(null) / 2 - wind / 2;
        int yoff = -turnSprite.getImage().getHeight(null) - 5;
        
        int x = home.getX()+xoff;
        int y = home.getY()+yoff;
        
        Point pos = new Point(x,y);
        
        turnSprite.setXY(pos);

        turnSprite.render(g);

        x += turnSprite.getWidth();
        y += turnSprite.getHeight() / 2;
        
        g.setColor(fontColor);
        g.setFont(font);

        g.drawString(text, x, y);       
    }
    
    protected String getText() {
        int value = values[Constant.HAND_SOFT_VALUE] <= 21 ?
                values[Constant.HAND_SOFT_VALUE] :
                values[Constant.HAND_VALUE];

        String text = name + ": "+value;
        
        if(outcome == Outcome.Blackjack)
            text += " => WIN Blackjack !";
        else if(outcome == Outcome.Charlie)
            text += " => WIN Charlie !";
        else if(outcome != Outcome.None)
            text += " => "+outcome.toString().toUpperCase()+" !";
        
        return text;
    }
    
    public boolean add(ACard card) {
        int xoff = cards.size() * HOME_OFFSET_X;
        int yoff = cards.size() * HOME_OFFSET_Y;

        Point myhome = new Point(home.getX() + xoff, home.getY() + yoff);

        return cards.add(new ACard(card, myhome));
    }

    public boolean ready() {
        for(int i=0; i < cards.size(); i++) {
            if(!cards.get(i).landed())
                return false;
        }
        
        return true;
    }

    public Hid getHid() {
        return hid;
    }
    
    /**
     * Sets a new home for the hand. This causes all the cards in the hand to
     * move.
     *
     * @param home
     */
    public void setHome(Point home) {
        this.home = home;

        int x = home.getX();
        int y = home.getY();

        // Move all the cards in the hand to their new home
        for (int k = 0; k < cards.size(); k++) {
            int xoff = k * HOME_OFFSET_X;
            int yoff = k * HOME_OFFSET_Y;

            ACard card = cards.get(k);

            card.setHome(new Point(x + xoff, y + yoff));
        }
    }

    public Point getHome() {
        return home;
    }

    public void settle() {
        for (ACard card : cards) {
            card.settle();
        }
    }

    /**
     * Splits the hand which reduces the hand by one and returns that card.
     *
     * @return Animated card
     */
    public ACard split() {
        int lastIndex = cards.size() - 1;

        if (lastIndex == -1) {
            return null;
        }

        ACard card = cards.get(lastIndex);

        cards.remove(lastIndex);

        return card;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    public Integer getPileWidth() {
        if (cards.isEmpty()) {
            return 0;
        }

        return cards.get(0).getWidth() + (cards.size() - 1) * HOME_OFFSET_X;
    }

    public Integer getPileHeight() {
        if (cards.isEmpty()) {
            return 0;
        }

        return cards.get(0).getHeight() + (cards.size() - 1) * HOME_OFFSET_Y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer size() {
        return cards.size();
    }
    
    public ACard get(Integer k) {
        return cards.get(k);
    }
    
    public void enablePlaying(boolean playing) {
        this.turnSprite.show(playing);
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public boolean isBroke() {
        if(values[Constant.HAND_SOFT_VALUE] <= 21 || values[Constant.HAND_VALUE] <= 21)
            return false;
        return true;
    }
    
}
