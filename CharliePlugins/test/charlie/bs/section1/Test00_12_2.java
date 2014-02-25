/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.bs.section1;

import charlie.advisor.MyAdvisor;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IAdvisor;
import charlie.util.Play;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wallance Miranda
 */
public class Test00_12_2 {
    
    private static IAdvisor advisor;
    
    public Test00_12_2() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        advisor = new MyAdvisor();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void testRow17Plus()
    {
        // Test row "15" and collumn 4
        Hid hid = new Hid(Seat.YOU, 1.0, 1.5);
        Hand hand = new Hand(hid);
        
        hand.hit(new Card(10, Card.Suit.CLUBS));
        hand.hit(new Card(5, Card.Suit.DIAMONDS));        
        
        Play result = advisor.advise(hand, new Card(4, Card.Suit.CLUBS));
        Play expectedPlay = Play.STAY;
        
        assertEquals(expectedPlay, result);
        
    }
}