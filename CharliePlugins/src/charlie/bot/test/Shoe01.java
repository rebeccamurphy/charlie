/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package charlie.bot.test;

import charlie.card.Shoe;
import java.util.Random;

/**
 *
 * @author Wallance Miranda
 */
public class Shoe01 extends Shoe {
    public void init() {
        // See: http://stackoverflow.com/questions/8572147/optimal-java-random-seed
        super.ran = new Random(Double.doubleToLongBits(Math.random()));
        
        super.numDecks = 2;
        
        super.load();
        
        super.shuffle();
    }
}
