/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.advisor;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IAdvisor;
import charlie.util.Play;
import java.util.HashMap;

/**
 *
 * @author Wallance Miranda
 */
public class MyAdvisor implements IAdvisor {
    
    //private static HashMap<String, Play> playReference;
    
    //  Hit =H, Stand =A, DD= D, SP =P
    public MyAdvisor()
    {
        //playReference = new HashMap<>();
        
    }
    
    @Override
    public Play advise(Hand myHand, Card upCard) {
        return BasicStrategy.getPlay(myHand, upCard);
    }
    
}
