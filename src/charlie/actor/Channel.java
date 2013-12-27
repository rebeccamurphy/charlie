/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.actor;

import charlie.card.Card;
import charlie.card.HoleCard;
import charlie.message.view.to.Ready;
import charlie.view.IUi;
import charlie.card.Hid;
import charlie.message.view.from.Bet;
import charlie.message.view.from.Hit;
import charlie.message.view.from.Stay;
import charlie.message.view.to.Blackjack;
import charlie.message.view.to.Bust;
import charlie.message.view.to.Charlie;
import charlie.message.view.to.Deal;
import charlie.message.view.to.Ending;
import charlie.message.view.to.Loose;
import charlie.message.view.to.Outcome;
import charlie.message.view.to.Play;
import charlie.message.view.to.Push;
import charlie.message.view.to.Starting;
import charlie.message.view.to.Win;
import charlie.util.Constant;
import com.googlecode.actorom.Actor;
import com.googlecode.actorom.Address;
import com.googlecode.actorom.annotation.OnMessage;
import com.googlecode.actorom.remote.ClientTopology;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author roncoleman125
 */
public class Channel {  
    private final Logger LOG = LoggerFactory.getLogger(Channel.class);

    private final IUi ui;
    protected Actor player;
    protected ClientTopology topology;
    protected Address myAddress;
    protected HoleCard holeCard;
    
    public Channel(IUi ui) {
        this.ui = ui;
    }
    
    public void stay(Hid hid) {
        player.send(new Stay(hid));
    }
    
    public void hit(Hid hid) {
        player.send(new Hit(hid));
    }
    
    public Hid bet(Integer amt) {
        Hid hid = new Hid();
        
        player.send(new Bet(hid,amt));
        
        return hid;
    }
    
    @OnMessage(type = Outcome.class)
    public void onReceive(Outcome outcome) {
        LOG.info("received outcome = "+outcome);
        
        Hid hid = outcome.getHid();
        
        if(outcome instanceof Blackjack)
            ui.blackjack(hid);
        else if(outcome instanceof Charlie)
            ui.charlie(hid);
        else if(outcome instanceof Win)
            ui.win(hid);
        else if(outcome instanceof Push)
            ui.push(hid);
        else if(outcome instanceof Loose)
            ui.loose(hid);
        else if(outcome instanceof Bust)
            ui.bust(hid);
        else
            LOG.error("outcome unknown");
    }
    
    /**
     * Receives a connected message sent by the house
     * @param msg 
     */
    @OnMessage(type = Ready.class)
    public void onReceive(Ready msg) {
        Address addr = msg.getAddress();
        LOG.info("received "+msg+" from "+addr);
        
        this.topology =
                new ClientTopology(addr.getHost(), addr.getPort(), 5, TimeUnit.SECONDS, 3, TimeUnit.SECONDS);
        
        this.player = topology.getActor(msg.getAddress());
        
        if(!player.isActive())
            return;

        synchronized(ui) {
            ui.notify();
        }
    }
    
    @OnMessage(type = Starting.class)
    public void onReceive(Starting starting) {      
        for(Hid hid: starting.getHids())
            LOG.info("starting hand: "+hid);
        
        ui.starting(starting.getHids());
    }

    @OnMessage(type = Deal.class)
    public void onReceive(Deal deal) {      
        Hid hid = deal.getHid();
        
        Card card = deal.getCard();
        
        if(card instanceof HoleCard)
            holeCard = (HoleCard)card;
        
        int[] values = deal.getHandValues();
        
        LOG.info("received card = "+card+" values = "+values[Constant.HAND_VALUE]+"/"+values[Constant.HAND_SOFT_VALUE]+" hid = "+hid);
        
        ui.deal(hid, card, values);
    }
    
    @OnMessage(type = Play.class)
    public void onReceive(Play turn) {
        LOG.info("got trun = "+turn.getHid());
        
        ui.turn(turn.getHid());
    }
    
    @OnMessage(type = Ending.class)
    public void onReceive(Ending ending) {
        LOG.info("received ending bankrool = "+ending.getBankroll());
        ui.ending(ending.getBankroll());
    }
    
    @OnMessage(type = String.class)
    public void onReceive(String s) {
        System.out.println(s);
    }
    
    public void setMyAddress(Address mine) {
        this.myAddress = mine;
    }
}
