/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.bs.section2;

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
public class Test00_5_7 {
    
    private static IAdvisor advisor;
    
    public Test00_5_7() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        advisor = new MyAdvisor();
    }
    
    @After
    public void tearDown() {
    }
    
    
    @Test
    public void test()
    {
        //Tests row 10, collumn 7
        Hid hid = new Hid(Seat.YOU, 1.0, 1.5);
        Hand hand = new Hand(hid);
        
        hand.hit(new Card(8, Card.Suit.CLUBS));
        hand.hit(new Card(2, Card.Suit.SPADES));        
        
        Play result = advisor.advise(hand, new Card(7, Card.Suit.HEARTS));
        Play expectedPlay = Play.DOUBLE_DOWN;
        
        assertEquals(expectedPlay, result);
    }
}