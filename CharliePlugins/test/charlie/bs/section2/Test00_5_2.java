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
public class Test00_5_2 {
    
    private static IAdvisor advisor;
    
    public Test00_5_2() {
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
        //Tests row 5-8, collumn 2
        Hid hid = new Hid(Seat.YOU, 1.0, 1.5);
        Hand hand = new Hand(hid);
        
        hand.hit(new Card(3, Card.Suit.CLUBS));
        hand.hit(new Card(2, Card.Suit.DIAMONDS));        
        
        Play result = advisor.advise(hand, new Card(2, Card.Suit.CLUBS));
        Play expectedPlay = Play.HIT;
        
        assertEquals(expectedPlay, result);
    }
}