/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.view;

import charlie.view.sprite.Chip;
import charlie.view.sprite.BetAmtSprite;
import charlie.view.sprite.Button;
import charlie.util.Constant;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author roncoleman125
 */
public class BetManager {
    public final static int HOME_X = 210;
    public final static int HOME_Y = 355;
    public final static int PLACE_HOME_X = BetAmtSprite.HOME_X + BetAmtSprite.DIAMETER + 10;
    public final static int PLACE_HOME_Y = BetAmtSprite.HOME_Y + BetAmtSprite.DIAMETER / 4;
    
    protected final int INDEX_5 = 2;
    protected final int INDEX_25 = 1;
    protected final int INDEX_100 = 0;
    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected BasicStroke stroke = new BasicStroke(3);    
    protected Integer[] amounts = { 100, 25, 5 };
    protected Random ran = new Random();
    
    protected final static String[] UP_FILES =
        {"chip-100-1.png","chip-25-1.png","chip-5-1.png"};
    
    protected final static String[] DOWN_FILES =
        {"chip-100-2.png","chip-25-2.png","chip-5-2.png"};
    
    protected List<Button> buttons = new ArrayList<>();
    
    protected BetAmtSprite betAmt = new BetAmtSprite(0);
    protected List<Chip> chips = new ArrayList<>();
    private final int width;
    protected double bankroll;
    protected Integer xDeposit = 0;
    
    public BetManager() {
        ImageIcon icon = new ImageIcon(Constant.DIR_IMGS+UP_FILES[0]);

        Image img = icon.getImage();
        this.width = img.getWidth(null);
//        int height = img.getHeight(null);
        
        int xoff =0;
        for(int i=0; i < amounts.length; i++) {
            Image up = new ImageIcon(Constant.DIR_IMGS+UP_FILES[i]).getImage();
            Image down = new ImageIcon(Constant.DIR_IMGS+DOWN_FILES[i]).getImage();
            buttons.add(new Button(up,down,HOME_X+xoff,HOME_Y));
            xoff += (width + 7);
        }
        
        xDeposit = HOME_X + xoff + 5;
    }
    
    public Integer getAmount() {
        return this.betAmt.getAmt();
    }
    
    public void render(Graphics2D g) {
        for(int i=0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            button.render(g);
        }
        
        for(int i=0; i < chips.size(); i++) {
            Chip chip = chips.get(i);
            chip.render(g);
        }
        
        this.betAmt.render(g);
        
        g.setFont(font);
        String remark = "";
        if(bankroll >= 100)
            g.setColor(Color.WHITE);
        else if(bankroll > 50 && bankroll < 100) {
            g.setColor(Color.YELLOW);
            remark = " !";
        }
        else {
            g.setColor(Color.RED);
            remark = " !!!!!";
        }
            
        g.drawString("Bankroll: "+bankroll+remark, xDeposit, HOME_Y+5);
        
    }
    
    public void click(int x, int y) {
        for(int i=0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            if(button.isReady() && button.isPressed(x,y)) {
                int n = chips.size();
                
                int placeX = PLACE_HOME_X + n * width/3 + ran.nextInt(10)-10;
                
                int placeY = PLACE_HOME_Y + ran.nextInt(5)-5;
                
                Chip chip = new Chip(button.getImage(),placeX,placeY);
                
                chips.add(chip);
                
                betAmt.increase(amounts[i]);
                
                bankroll -= amounts[i];
            }

        }
    }
    
    public void unclick() {
        for(int i=0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            button.release();
        }        
    }

    public void setBankroll(Double bankroll) {
        this.bankroll = bankroll;
    }
    
    
}