/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package charlie.bot.client;

import charlie.actor.Courier;
import charlie.advisor.BasicStrategy;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IGerty;
import charlie.util.Constant;
import charlie.util.Play;
import charlie.view.AMoneyManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

/**
 *
 * @author Rebecca
 */
public class Gerty implements IGerty{
    
    private final Logger LOG = LoggerFactory.getLogger(Gerty.class);
    protected Courier courier;
    protected AMoneyManager moneyManager;
    protected Hid myHid;
    protected Hid dealerHid;
    protected Hand myHand;
    protected Hand dealerHand;
    protected Card dealerUpCard;
    protected Play advice;
    protected BasicStrategy bs;
    protected Random random;
    protected final int DELAY =2000;
    private int currentBet=0;
    protected int betSum =0;
    private int meanBet =0;
    private int maxBet=0;
    protected HiLo betStrategy = new HiLo();
    
    
    protected int gamesPlayed =0;
    private int blackjacksCount = 0;
    private int charliesCount = 0;
    private int winsCount = 0;
    private int breaksCount = 0;
    private int losesCount = 0;
    private int pushesCount = 0;
    private long startingTime = 0;
    
    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected Font statisticsFont = new Font("Tahoma", Font.BOLD, 12);

    @Override
    public void go() {
       if (gamesPlayed==0)
           startingTime = System.currentTimeMillis();
    
     
     if (gamesPlayed==0){
         //bets minimum on first turn
         moneyManager.clearBet();
         try{
            Thread.sleep(500);
         }
         catch (InterruptedException ex){
              java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
         }
         moneyManager.upBet(Constant.MIN_BET);
         currentBet = Constant.MIN_BET;
         
     }
     else {
         //bets true count * minimum 
         moneyManager.clearBet();
         try{
            Thread.sleep(500);
         }
         catch (InterruptedException ex){
         }
        
         int numChips = (betStrategy.getBetAmount() / Constant.MIN_BET);
         maxBet = (betStrategy.getBetAmount()>maxBet)? betStrategy.getBetAmount(): maxBet;
          
             
         //plays all min chips for bet amount
         for (int i=0; i< numChips; i++){
             moneyManager.upBet(Constant.MIN_BET);
             try{
                Thread.sleep(1000);
             }
             catch (InterruptedException ex){
                  java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
             }    
         }
         currentBet = betStrategy.getBetAmount();
         
     }
     LOG.info("getting bet amount " + betStrategy.getBetAmount());
     LOG.info("getting true count " + betStrategy.trueCount);
     //will this add extra to bet?
     myHid = courier.bet(currentBet, 0);
     betSum +=currentBet;
     myHand = new Hand(myHid);
     
    }
    public void halt() {
        
    }

    @Override
    public void setCourier(Courier courier) {
       this.courier = courier;
    }

    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
    }

    @Override
    public void update() {
       
    }

    @Override
    public void render(Graphics2D g) {
        g.setFont(this.statisticsFont);
        g.setColor(Color.BLACK);
        int statisticsTableX = 5;
        int statisticsTableY = 205;
        int lineHeight = 20;
        
        g.drawString("Counting System: Hi-Lo", statisticsTableX, statisticsTableY);
        
        // TODO: Wrong format parameter.
        g.drawString("Shoe Size:" + betStrategy.decksRemaining, statisticsTableX, statisticsTableY += lineHeight);
        //g.drawString(String.format("Shoe Size: %.2f", this.blackjacksCount), statisticsTableX, statisticsTableY += lineHeight);
        g.drawString("Running Count: " + betStrategy.runningCount, statisticsTableX, statisticsTableY += lineHeight);
        g.drawString("True Count: " + betStrategy.trueCount, statisticsTableX, statisticsTableY += lineHeight);
        
        g.drawString(gamesPlayed + " Games within "+ this.getElapsedTimeAsString(), statisticsTableX, statisticsTableY += lineHeight);
        g.drawString("Mean Bet: $" + meanBet, statisticsTableX, statisticsTableY += lineHeight);
        g.drawString("Max Bet: $"+maxBet , statisticsTableX, statisticsTableY += lineHeight);
     
        g.drawString("Mean Bet Amount Per Game: ", statisticsTableX, statisticsTableY += lineHeight);
        g.drawString("Blackjacks "+this.blackjacksCount+" | Charlies "+this.charliesCount+" "
                + "| Wins "+this.winsCount+" | Breaks "+this.breaksCount+" | Loses "+this.losesCount+" "
                + "| Pushes "+this.pushesCount, statisticsTableX, statisticsTableY += lineHeight);
    }

     private String getElapsedTimeAsString(){
        int elapsedTimeInMills = 0;
        if (this.startingTime != 0)
        {
            elapsedTimeInMills = (int) (System.currentTimeMillis()-this.startingTime);
        }
        
        if (elapsedTimeInMills >= 60000)
            return String.format("%d Minutes %d Seconds", (elapsedTimeInMills/60000), (elapsedTimeInMills/1000));
        else
            return (elapsedTimeInMills/1000)+" Seconds";
    }
     
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        if (shoeSize <52)
            betStrategy.setDecksRemaining(1);
        else 
            betStrategy.setDecksRemaining((int) Math.round(shoeSize/52.0));
        LOG.info("game started");
        //myHand = new Hand(myHid);
    }

    @Override
    public void endGame(int shoeSize) {
        LOG.info("end game");
        gamesPlayed++;
        dealerHid=null;
        meanBet = betSum/ gamesPlayed;
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            
        }
    }

    @Override
    public void deal(Hid hid, Card card, int[] values) {
        //betStrategy.update(card, charlies);
        //decks remaining = 1 because reasons
        betStrategy.update(card);
        if(hid.getSeat() ==Seat.DEALER){
            this.dealerHid = hid;
            this.dealerUpCard = card;
            //LOG.info("dealer upcard set " + card);
        }
      
        if (hid.getSeat().equals(Seat.YOU))
            myHand.hit(card);
          
       if (hid.getSeat().equals(Seat.YOU) && myHand.size()>2 && !(myHand.isBroke()))
        respond();
    }

    @Override
    public void insure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   @Override
    public void bust(Hid hid) {
        // TODO: Is Break and bust the same?
        this.breaksCount++;
    }

    @Override
    public void win(Hid hid) {
        this.winsCount++;
    }

    @Override
    public void blackjack(Hid hid) {
        this.blackjacksCount++;
    }

    @Override
    public void charlie(Hid hid) {
        this.charliesCount++;
    }

    @Override
    public void lose(Hid hid) {
        this.losesCount++;
    }

    @Override
    public void push(Hid hid) {
        this.pushesCount++;
    }

    @Override
    public void shuffling() {
        betStrategy.reset();
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            
        }
        
    }

    @Override
    public void play(Hid hid) {
       if (hid.getSeat().equals(Seat.YOU))
           respond();
    }
    
    protected void respond(){
     LOG.info("performing turn");
     Random random = new Random();
     final int DELAY = random.nextInt(3001-1000) +1000;
     
            try {
                Thread.sleep(DELAY);
            }
            catch (InterruptedException ex) {
              //LOG.info("Thread Error");
              }
        //sends play to dealer
      LOG.info("myHand value" +myHand.getValue() +" " + dealerUpCard.value());
        advice = BasicStrategy.getPlay(myHand, dealerUpCard);
                
                if (advice == Play.DOUBLE_DOWN && myHand.size() == 2)
                    courier.dubble(myHid);
                else if (advice == Play.STAY)
                    courier.stay(myHid);
                else
                    //if double down is advised on more than 2 cards 
                    //or split is advised, or hit is advised
                    courier.hit(myHid);
              
    }
}
