/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package charlie.advisor;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;
import static charlie.util.Play.*;
import java.util.Hashtable;
import sun.rmi.runtime.Log;


/**
 *
 * @author Rebecca
 */
public class BasicStrategy {
     //Multidimensional Array containing the strategy table
     final static char[][] strategyKey= new char[][] {
       // 0    1    1    3    4    5    6    7    8    9 | Second position
         
       // 2,   3,   4,   5,   6,   7,   8,   9,   10,  A     //First Position
        {'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S' }, //17+  |0
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //16   |1
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //15   |2
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //14   |3
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //13   |4
        {'H', 'H', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //12   |5
        //section 1
        
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H' }, //11   |6
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H', 'H' }, //10   |7
        {'H', 'D', 'D', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // 9   |8
        {'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H' }, // 5-8 |9
        //section 2
        
        {'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S' }, // A, 8-10 |10
        {'S', 'D', 'D', 'D', 'D', 'S', 'S', 'H', 'H', 'H' }, // A, 7    |11
        {'H', 'D', 'D', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 6    |12
        {'H', 'H', 'D', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 5    |13
        {'H', 'H', 'D', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 4    |14
        {'H', 'H', 'H', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 3    |15
        {'H', 'H', 'H', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 2    |16
        //section 3
        
        {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P' }, // A, A,8,8    |17
        {'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S' }, // 10, 10      |18
        {'P', 'P', 'P', 'P', 'P', 'S', 'P', 'P', 'S', 'S' }, // 9,  9       |19
        {'P', 'P', 'P', 'P', 'P', 'P', 'H', 'H', 'H', 'H' }, // 7,  7       |20
        {'P', 'P', 'P', 'P', 'P', 'H', 'H', 'H', 'H', 'H' }, // 6,  6       |21
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H', 'H' }, // 5,  5       |22
        {'H', 'H', 'H', 'P', 'P', 'H', 'H', 'H', 'H', 'H' }, // 4,  4       |23
        {'P', 'P', 'P', 'P', 'P', 'P', 'H', 'H', 'H', 'H' }, // 3,  3       |24
        {'P', 'P', 'P', 'P', 'P', 'P', 'H', 'H', 'H', 'H' }, // 2,  2       |25
        //section 4
        
         
    };
     
    
  static Hashtable<Integer, Integer > playerHandValueLocs = new Hashtable<Integer, Integer>(){{
      //Hash table representing the Locations of section 1 and 2 rows of the strategy table, hands values 5-17+
      //put(Card.value(), corresponding strategyKey table row)
        // 5-8
        put(5,9); 
        put(6,9);
        put(7,9);
        put(8,9);
        
        int j = 1;
        // 12 - 16
        for (int i=16;i>8  ; i--)
        {
            put(i, j);
            j++;
        } 
        //17+
        for (int i =17; i<22; i++ )
            put(i, 0);
         
    }};
     
     
     static Hashtable<Integer, Integer > playerHandAceLocs = new Hashtable<Integer, Integer>(){{
      //Hash table representing the Locations of section 3 rows of the strategy table, Hands containing an Ace
      //put(Card.value(), corresponding strategyKey table row)
        put(10,10);//A, 10 
        put(9,10); //A, 9 
        put(8,10); //A, 8
     
        int j = 11;
        // A, 7 - A, 2 
        for (int i=7;i>1  ; i--)
        {
            put(i, j);
            j++;
        }        
    }};
     
     static Hashtable<Integer, Integer > playerHandPairsLocs = new Hashtable<Integer, Integer>(){{
      //Hash table representing the Locations of section 4 rows of the strategy table, the pairs
      //put(Card.value(), corresponding strategyKey table row)
        put(1,17);  //A, A
        put(11, 17); //A, A
        put(8,17);  //8, 8
        put(10,18); //10, 10
        put(9,19);  //9, 9
        
        int j = 20; 
        // 7,7 - 2,2
        for (int i=7;i>1  ; i--)
        {
            put(i, j);
            j++;
        }        
    }};
  
     static Hashtable<Integer, Integer > dealerUpCardLocs = new Hashtable<Integer, Integer>(){{
      //Hash table representing the Locations of Dealers up card collumns, 2 - A
      //put(Card.value(), corresponding strategyKey table collumn)

        int j = 0;
        // 2 - 10
        for (int i=2;i<11  ; i++)
        {
            put(i, j);
            j++;
        }
        put(1,9); // Ace
    }};
  /**
     * This function checks if there is an Ace in the players hand. 
     * @param myHand
     * @return true if Ace, false otherwise
     */
    
    private static boolean containsAce (Hand myHand)
    {
        return myHand.getCard(1).isAce() || myHand.getCard(0).isAce();
               
    }
    
    /***
     * This function converts a character from the strategyKey to an enumerated Play type.
     * @param char t
     * @return enum HIT, STAY, DOUBLE_DOWN, SPLIT or NONE
     */
    private static Play charToEnum(char t)
    {
        
        switch (t){
            case 'H':
               return HIT;
            case 'S':
                return STAY;
            case 'D':
                return DOUBLE_DOWN;
            case 'P':
                return SPLIT;
            default: 
                return NONE;
           }
        
    }
    /**
     * This function returns the proper advice for the player.
     * @param myHand
     * @param upCard
     * @return Play
     */
    private static Play getAdvice( Hand myHand, Card upCard)
    {
        int yourHandLoc ;
        int dealerUpCardLoc;
        
        //if-else sets yourHandLoc to correct row location, by retriving it from the correct Hashtable
        if (myHand.isPair())//Checks for pair, section 4
            yourHandLoc = playerHandPairsLocs.get(myHand.getCard(0).value());  
        
        else if (myHand.size()==2 && containsAce(myHand)) //Checks for Ace, section 3 
            yourHandLoc = playerHandAceLocs.get(myHand.getValue() -11);
        
        else //Uses Hand value, sections 1 and 2 
            System.out.println(myHand.getValue());
            yourHandLoc = playerHandValueLocs.get(myHand.getValue());
        
        // sets dealersUpCardLoc to the correct location from the hashtable
        dealerUpCardLoc = dealerUpCardLocs.get(upCard.value());
        
        //retrieves the advice from stragetyKey, corresponding to the proper locations, and converts to Play type
        return charToEnum(strategyKey[yourHandLoc][dealerUpCardLoc]);
    }
   /**
    * This function gets the recommended Play, for use of MyAdivsor.
    * @param myHand
    * @param upCard
    * @return Play 
    */ 
   
    public static Play getPlay (Hand myHand, Card upCard)
    {
        //NONE, HIT, STAY, DOUBLE_DOWN, SPLIT
        return getAdvice(myHand, upCard);
        
    }
    
    
}
