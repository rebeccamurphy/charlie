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

/**
 *
 * @author Rebecca
 */
public class basicStrategy {
    
     char[][] strategyKey= new char[][] {
       // 2,   3,   4,   5,   6,   7,   8,   9,   10,  A 
        {'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S' }, //17+  |0
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //16   |1
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //15   |2
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //14   |3
        {'S', 'S', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //13   |4
        {'H', 'H', 'S', 'S', 'S', 'H', 'H', 'H', 'H', 'H' }, //12   |5
        
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H' }, //11   |6
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H', 'H' }, //10   |7
        {'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'H', 'H' }, // 9   |8
        {'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S' }, // 5-8 |9
        
        {'S', 'D', 'D', 'D', 'D', 'S', 'S', 'H', 'H', 'H' }, // A, 8-10 |10
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
     
    public Play charToEnum(char t)
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
    
    public Play advise(Hand myHand, Card upCard)
    {
        
    }
    
    public int getmyHandLoc( Hand myHand, Card upCard)
    {
        int yourHand;
        int dealerUpCard;
        
        if (myHand.isPair())
        {
           switch (myHand.getCard(0).value())
           {
               case 1:
               case 8:
                   yourHand = 17;
                   return yourHand;
               case 10:
                   yourHand = 18;
                   return yourHand;
               case 9:
                   yourHand = 19;
                   return yourHand;
               case 7:
                   yourHand = 20;
                   return yourHand;
               case 6:
                   yourHand = 21;
                   return yourHand;
               case 5:
                   yourHand = 22;
                   return yourHand;
               case 4:
                   yourHand = 23;
                   return yourHand;
               case 3:
                   yourHand = 24;
                   return yourHand;
               case 2:
                   yourHand = 25;
                   return yourHand;
                   
           }
        }
    }
    public Play getPlay (Hand myHand, Card upCard)
    {
        //NONE, HIT, STAY, DOUBLE_DOWN, SPLIT
        return NONE;
    }
    
}
