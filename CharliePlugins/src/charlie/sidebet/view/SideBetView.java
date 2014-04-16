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

package charlie.sidebet.view;

import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.plugin.ISideBetView;
import charlie.view.sprite.Chip;
import charlie.view.AMoneyManager;

import charlie.view.sprite.ChipButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the side bet view
 * @author Ron Coleman, Ph.D.
 */
public class SideBetView implements ISideBetView {
    private final Logger LOG = LoggerFactory.getLogger(SideBetView.class);
    
    private static Random ran;
    private static int sideBetAreaX;
    private static int sideBetAreaY;
    
    public final static int X = 400;
    public final static int Y = 200;
    public final static int DIAMETER = 50;
    
    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected Font stakesFont = new Font("Tahoma", Font.BOLD, 14);
    protected BasicStroke stroke = new BasicStroke(3);
    
    // See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
    protected float dash1[] = {10.0f};
    protected BasicStroke dashed
            = new BasicStroke(3.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);   

    protected List<ChipButton> buttons;
    protected int amt = 0;
    protected AMoneyManager moneyManager;
    
    protected List<Chip> sideBetChips;
    
    private SideBetOutcome outcome;
    protected enum SideBetOutcome { WIN, LOSE, NONE};

    public SideBetView() {
        this.sideBetChips = new ArrayList<>();
        ran = new Random();
        sideBetAreaX = X-DIAMETER/2;
        sideBetAreaY = Y-DIAMETER/2;
        LOG.info("side bet view constructed");
        outcome = SideBetOutcome.NONE;
    }
    
    /**
     * Sets the money manager.
     * @param moneyManager 
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
        this.buttons = moneyManager.getButtons();
    }
    
    /**
     * Registers a click for the side bet.
     * @param x X coordinate
     * @param y Y coordinate
     */
    @Override
    public void click(int x, int y) {
        int oldAmt = amt;
        
        // Test if any chip button has been pressed.
        for(ChipButton button: buttons) {
            if(button.isPressed(x, y)) {
                amt += button.getAmt();
                int chipX = sideBetAreaX + DIAMETER + ( (this.sideBetChips.size() * 10) + ran.nextInt(15));
                
                // To simulate the chips being placed on the table, like a human,
                // which doesn't space them evenly out, use randomness to adjust
                // the X and Y coordinates.
                int randomY = ran.nextInt(10);
                
                int chipY = sideBetAreaY + ( (ran.nextBoolean()) ? randomY : -randomY );
                
                this.sideBetChips.add(new Chip(button.getImage(), chipX, chipY, amt));
                
                LOG.info("A. side bet amount "+button.getAmt()+" updated new amt = "+amt);
                
                SoundFactory.play(Effect.CHIPS_IN);
            } 
        }
        
        if(oldAmt == amt) {
            amt = 0;
            this.sideBetChips.clear();
            SoundFactory.play(Effect.CHIPS_OUT);
            LOG.info("B. side bet amount cleared");
        }
    }

    /**
     * Informs view the game is over and it's time to update the bankroll for the hand.
     * @param hid Hand id
     */
    @Override
    public void ending(Hid hid) {
        double bet = hid.getSideAmt();
        
        if(bet == 0)
            return;

        LOG.info("side bet outcome = "+bet);
        this.
        
        // Update the bankroll
        moneyManager.increase(bet);
        
        // Determines if the bet is positive, negative or 0.
        double signim = Math.signum(bet);
        
        if (signim > 0)
        {
            // Bet was positive.
            this.outcome = SideBetOutcome.WIN;
        }
        else if (signim < 0)
        {
            // Bet was negative.
            this.outcome = SideBetOutcome.LOSE;
        }
        else
        {
            // Bet was 0.
            this.outcome = SideBetOutcome.NONE;
        }
        
        LOG.info("Player " + hid.getSeat().toString() + " - Side Bet Outcome: " + this.outcome.toString()+ ".");
        
        LOG.info("new bankroll = "+moneyManager.getBankroll());
    }

    /**
     * Informs view the game is starting
     */
    @Override
    public void starting() {
        // Resets the outcome when the player presses the "Deal" button.
        this.outcome = SideBetOutcome.NONE;
    }

    /**
     * Gets the side bet amount.
     * @return Bet amount
     */
    @Override
    public Integer getAmt() {
        return amt;
    }

    /**
     * Updates the view
     */
    @Override
    public void update() {
        // Note: Called continously.
        
        //LOG.info("THE UPDATE METHOD HAS BEEN CALLED!");
    }

    /**
     * Renders the view
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        // Draw the at-stake place on the table
        g.setColor(Color.RED); 
        g.setStroke(dashed);
        int ovalX = X-DIAMETER/2;
        int ovalY = Y-DIAMETER/2;
        g.drawOval(ovalX, ovalY, DIAMETER, DIAMETER);        
        
        // Draw the at-stake amount
        FontMetrics fontMetrics = g.getFontMetrics(font);
        String sideBetStakeAmountText = "" + amt;
        int sideBetTextWidth = fontMetrics.charsWidth(sideBetStakeAmountText.toCharArray(), 0, sideBetStakeAmountText.length());
        g.setFont(font);
        g.setColor(Color.WHITE);
        int sideBetXCoordinate = ovalX + (DIAMETER/2) -(sideBetTextWidth/2);
        g.drawString(sideBetStakeAmountText, sideBetXCoordinate, Y+5);
        
        // Draw the stake payouts on the table
        g.setFont(this.stakesFont);
        g.setColor(Color.BLACK);
        int payoutStringX = X + 55;
        int payoutStringY = Y + 100;
        g.drawString("SUPER 7 pays 3:1", payoutStringX, payoutStringY);
        g.drawString("ROYAL MATCH pays 25:1", payoutStringX, payoutStringY += 20);
        g.drawString("EXACTLY 13 pays 1:1", payoutStringX, payoutStringY += 20);
        
        // Draw the chips on the table
        ovalX += DIAMETER + 5;
        for(int i=0; i < this.sideBetChips.size(); i++)
        {
            Chip chip = this.sideBetChips.get(i);
            chip.render(g);
        }
        
        // Draw WIN or LOSE label.
        if (this.outcome != SideBetOutcome.NONE)
                {
            g.setFont(this.font);
            String labelText = "";

            if (this.outcome == SideBetOutcome.WIN)
            {
                labelText = "WIN !";
                g.setColor(Color.GREEN);
            }
            else if (this.outcome == SideBetOutcome.LOSE)
            {
                labelText = "LOSE !";
                g.setColor(Color.RED);
            }
            
            /// Figure out the dimensions of the text based on the font.
            FontMetrics fm = g.getFontMetrics(font);
            int textWidth = fm.charsWidth(labelText.toCharArray(), 0, labelText.length());
            int textHeight = fm.getHeight();

            int winLoseLabelX = X + 60;
            int winLoseLabelY = Y - 10;

            // Render the colored background for the text.
            g.fillRoundRect(winLoseLabelX-5, winLoseLabelY-textHeight+3, textWidth+8, textHeight, 5, 5);

            if (this.outcome == SideBetOutcome.LOSE)
                g.setColor(Color.WHITE);
            else
                g.setColor(Color.BLACK);

            // Render the outcome text.
            g.drawString(labelText, winLoseLabelX, winLoseLabelY);
        }
    }
}
