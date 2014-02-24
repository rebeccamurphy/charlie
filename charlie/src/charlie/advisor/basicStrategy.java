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


/**
 *
 * @author Rebecca
 */
public class basicStrategy {
    
     static char[][] strategyKey= new char[][] {
       // 2,   3,   4,   5,   6,   7,   8,   9,   10,  A 
        {'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S' }, //17+  |0
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //16   |1
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //15   |2
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //14   |3
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //13   |4
        {'H', 'H', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //12   |5
        
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H' }, //11   |6
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H', 'H' }, //10   |7
        {'H', 'D', 'D', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // 9   |8
        {'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H' }, // 5-8 |9
        
        {'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S' }, // A, 8-10 |10
        {'S', 'D', 'D', 'D', 'D', 'S', 'S', 'H', 'H', 'H' }, // A, 7    |11
        {'H', 'D', 'D', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 6    |12
        {'H', 'H', 'D', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 5    |13
        {'H', 'H', 'D', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 4    |14
        {'H', 'H', 'H', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 3    |15
        {'H', 'H', 'H', 'D', 'D', 'H', 'H', 'H', 'H', 'H' }, // A, 2    |16
        
        {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P' }, // A, A,8,8    |17
        {'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S' }, // 10, 10      |18
        {'P', 'P', 'P', 'P', 'P', 'S', 'P', 'P', 'S', 'S' }, // 9,  9       |19
        {'P', 'P', 'P', 'P', 'P', 'P', 'H', 'H', 'H', 'H' }, // 7,  7       |20
        {'P', 'P', 'P', 'P', 'P', 'H', 'H', 'H', 'H', 'H' }, // 6,  6       |21
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H', 'H' }, // 5,  5       |22
        {'H', 'H', 'H', 'P', 'P', 'H', 'H', 'H', 'H', 'H' }, // 4,  4       |23
        {'P', 'P', 'P', 'P', 'P', 'P', 'H', 'H', 'H', 'H' }, // 3,  3       |24
        {'P', 'P', 'P', 'P', 'P', 'P', 'H', 'H', 'H', 'H' }, // 2,  2       |25
        
         
    };
     
    //Hashtable <Integer, Integer> playerHandPairs = new Hashtable<Integer,Integer>();
    //playerHandPairs.put(new Integer 8, new Integer 17);
  static Hashtable<Integer, Integer > playerHandValueLoc = new Hashtable<Integer, Integer>(){{
        put(5,9);
        put(6,9);
        put(7,9);
        put(8,9);
        
        for (int i =17; i<22; i++ )
            put(i, 0);
        
        int j = 1;
        for (int i=16;i>8  ; i--)
        {
            put(i, j);
            j++;
        }        
    }};
     
     
     static Hashtable<Integer, Integer > playerHandAceLoc = new Hashtable<Integer, Integer>(){{
        put(8,10);
        put(9,10);
        put(10,10);
        
        int j = 11;
        for (int i=7;i>1  ; i--)
        {
            put(i, j);
            j++;
        }        
    }};
     
     static Hashtable<Integer, Integer > playerHandPairsLoc = new Hashtable<Integer, Integer>(){{
        put(1,17);
        put(8,17);
        put(10,18);
        put(9,19);
        
        int j = 20;
        for (int i=7;i>1  ; i--)
        {
            put(i, j);
            j++;
        }        
    }};
  
     static Hashtable<Integer, Integer > dealerUpCardLocs = new Hashtable<Integer, Integer>(){{
        put(1,9);
        
        int j = 0;
        for (int i=2;i<11  ; i++)
        {
            put(i, j);
            j++;
        }        
    }};
  
    public basicStrategy()
    {}
    public static Play charToEnum(char t)
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
    
   
    
    public static char  getAdvice( Hand myHand, Card upCard)
    {
        int yourHandLoc;
        int dealerUpCardLoc;
        
        if (myHand.isPair())//pair check
         yourHandLoc = playerHandPairsLoc.get(myHand.getCard(0).value());
      
        else if (myHand.getCard(0).isAce()) //Ace might be more than one
            yourHandLoc = playerHandAceLoc.get(myHand.getCard(1).value());
        else if (myHand.getCard(1).isAce()) //another ace check
            yourHandLoc = playerHandAceLoc.get(myHand.getCard(0).value());
        else 
            yourHandLoc = playerHandValueLoc.get(myHand.getValue());
            
        dealerUpCardLoc = dealerUpCardLocs.get(upCard.value());
        System.out.println(yourHandLoc +" " + dealerUpCardLoc);
        return strategyKey[yourHandLoc][dealerUpCardLoc];
    }
    public static Play getPlay (Hand myHand, Card upCard)
    {
        //NONE, HIT, STAY, DOUBLE_DOWN, SPLIT
        char advice = getAdvice(myHand, upCard);
        return charToEnum(advice);
        
    }
    
}
