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
public class Test00_7_A {
    
    private static IAdvisor advisor;
    
    public Test00_7_A() {
        advisor = new MyAdvisor();
    }
    
    @BeforeClass
    public static void setUpClass() {
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
    public void test()
    {
        Hid hid = new Hid(Seat.YOU, 1.0, 1.5);
        Hand hand = new Hand(hid);
        
        hand.hit(new Card(3, Card.Suit.CLUBS));
        hand.hit(new Card(4, Card.Suit.DIAMONDS));        
        
        Play result = advisor.advise(hand, new Card(Card.ACE, Card.Suit.CLUBS));
        Play expectedPlay = Play.HIT;
        
        assertEquals(expectedPlay, result);
    }
}