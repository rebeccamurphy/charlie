/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package charlie.advisor;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IAdvisor;
import charlie.util.Play;

/**
 *
 * @author Rebecca
 */
public class myAdvisor implements IAdvisor {
//  Hit =0, Stand =1, DD= 2, SP =3
    @Override
    public Play advise(Hand myHand, Card upCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    int[][] basicStrategy= new int[][] 
    {
        {1, 1, 1, 1, 1 , 1, 1, 1, 1, 1 },
        {1, 1, 1, 1, 1 , 1, 1, 1, 1, 1 }
    };
    
            }
    
}
