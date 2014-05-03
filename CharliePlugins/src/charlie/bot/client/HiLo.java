/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package charlie.bot.client;

import charlie.card.Card;
import charlie.util.Constant;
import java.util.HashMap;

/**
 *
 * @author Rebecca
 */
public class HiLo {
public int trueCount;
public double runningCount;
public int decksRemaining;
private int betAmount;
private final HashMap<String, Integer> cardVals;

public HiLo(){
    //go off of card.getName()
    cardVals = new HashMap<>();
    cardVals.put("2", 1);
    cardVals.put("3", 1);
    cardVals.put("4", 1);
    cardVals.put("5", 1);
    cardVals.put("6", 1);
    cardVals.put("7", 0);
    cardVals.put("8", 0);
    cardVals.put("9", 0);
    cardVals.put("10", -1);
    cardVals.put("J", -1);
    cardVals.put("Q", -1);
    cardVals.put("K", -1);
    cardVals.put("A", -1);
    
    trueCount =0;
    runningCount=0;
}

public void update(Card card){
    runningCount += cardVals.get(card.getName());

}
public void reset(){
    trueCount =0;
    runningCount =0;
    
}
public void setDecksRemaining(int shoeSizes){
    this.decksRemaining = shoeSizes;
}

public int getBetAmount(){
    //rounds the trueCount to the closest int
    trueCount = (int) Math.round(runningCount /decksRemaining);
    if (trueCount >=2)
        betAmount = trueCount * Constant.MIN_BET;
    else
        betAmount = Constant.MIN_BET;
    return betAmount;
}

}
