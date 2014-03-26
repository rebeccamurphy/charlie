/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import java.util.List;
import java.util.Random;

/**
 *
 * @author rebecca murphy
 */
public class ServerBot implements IBot {
    protected Hid hid;
    protected Hand playHand;
    protected Dealer dealer;
    protected Seat seat;
    protected IAdvisor advisor;
    protected Hid dealerHid;
    protected Card dealerUpCard;
    protected BasicStrategy bs;
    
    public ServerBot () {
    bs = new BasicStrategy();
    }
    
    @Override
    public Hand getHand() {
        return playHand;
    }

    @Override
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    @Override
    public void sit(Seat seat) {
       this.seat = seat;
       hid = new Hid(this.seat, 0,0);
       playHand = new Hand(hid);
    }
    

    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        //add log of starting game? 
    }

    @Override
    public void endGame(int shoeSize) {
        //add log of in end game
    }

    @Override
    public void deal(Hid hid, Card card, int[] values) {
        if(this.dealerHid == null && hid.getSeat() ==Seat.DEALER){
            this.dealerHid = hid;
            this.dealerUpCard = card;
        }
        if (playHand.getHid() == hid && playHand.size() >2 && !(playHand.isBroke())){
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
        //add alert for win 
    }

    @Override
    public void blackjack(Hid hid) {
       //add alert for blackjack win
    }

    @Override
    public void charlie(Hid hid) {
       //add alert for charlie win
    }

    @Override
    public void lose(Hid hid) {
        //add alert for lose 
    }

    @Override
    public void push(Hid hid) {
        //add alert for push 
    }

    @Override
    public void shuffling() {
        //add alert for shuffling 
    }

    @Override
    public void play(Hid hid) {
        Random random = new Random();
        final IPlayer bot = this;
        final Hid botHid = hid;
        final int DELAY = random.nextInt(3001-1000) +1000;
        final int randomPlay = random.nextInt(4);
        final int ignoreBs = DELAY % 5;
        final Play[] plays = {Play.DOUBLE_DOWN, Play.HIT, Play.SPLIT, Play.STAY};
        
        Runnable thread = new Runnable() {
        @Override 
        public void run() {
            Play advice;
            int ignoreBs = DELAY % 5;
            try {
                Thread.sleep(DELAY);
            }
            catch (InterruptedException ex) {
              //log error  
              }
            if (botHid == playHand.getHid()){
                advice = bs.getPlay(playHand, dealerUpCard);
                
                if (advice == Play.DOUBLE_DOWN && playHand.size() == 2)
                    dealer.doubleDown(bot, botHid);
                else if (advice == Play.SPLIT)
                    dealer.hit(bot, botHid);
                else if (advice == Play.STAY)
                    dealer.stay(bot, botHid);
                else
                    dealer.hit(bot, botHid);
                }
            }
                
        };
        new Thread(thread).start();
    
    }
    
    
}
