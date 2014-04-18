/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package charlie.sidebet.test;

import charlie.card.Card;

/**
 *
 * @author Marist User
 */
public class Shoe extends charlie.card.Shoe {
    @Override
    public void init() {
        cards.clear();
        
        //Test case #1
        cards.add(new Card(7,Card.Suit.HEARTS));            //player
        cards.add(new Card(Card.KING,Card.Suit.DIAMONDS));  //dealer
        
        cards.add(new Card(9,Card.Suit.SPADES));        //player
        cards.add(new Card(9,Card.Suit.HEARTS));        //dealer
        
        cards.add(new Card(3,Card.Suit.CLUBS));         //player
        
        // Test case #2
        cards.add(new Card(7,Card.Suit.HEARTS));        //player
        cards.add(new Card(Card.KING,Card.Suit.CLUBS)); //dealer
        
        cards.add(new Card(9,Card.Suit.SPADES));        //player
        cards.add(new Card(8,Card.Suit.DIAMONDS));      //dealer
        
        cards.add(new Card(3,Card.Suit.CLUBS));         //player
        
        // Test case #3
        cards.add(new Card(9,Card.Suit.HEARTS));        //player
        cards.add(new Card(Card.KING,Card.Suit.CLUBS)); //dealer
        
        cards.add(new Card(7,Card.Suit.SPADES));        //player
        cards.add(new Card(8,Card.Suit.DIAMONDS));      //dealer
        
        cards.add(new Card(3,Card.Suit.CLUBS));         //player
        
        //Test case 4
        
        cards.add(new Card(7,Card.Suit.CLUBS));          //player
        cards.add(new Card(Card.KING,Card.Suit.SPADES)); //dealer
        
        cards.add(new Card(9,Card.Suit.SPADES));         //player
        cards.add(new Card(10,Card.Suit.DIAMONDS));      //dealer
        
        cards.add(new Card(3,Card.Suit.CLUBS));         //player
        
        
        //Test case 5
        
        cards.add(new Card(9,Card.Suit.CLUBS));          //player
        cards.add(new Card(Card.KING,Card.Suit.SPADES)); //dealer
        
        cards.add(new Card(7,Card.Suit.SPADES));         //player
        cards.add(new Card(10,Card.Suit.DIAMONDS));      //dealer
        
        cards.add(new Card(3,Card.Suit.CLUBS));         //player
        
        //Test case 6
        
        cards.add(new Card(Card.KING,Card.Suit.CLUBS));         //player
        cards.add(new Card(Card.KING,Card.Suit.SPADES));        //dealer
        
        cards.add(new Card(Card.QUEEN,Card.Suit.CLUBS));       //player
        cards.add(new Card(8,Card.Suit.DIAMONDS));             //dealer
        
        
       
        //Test case 7
        
        cards.add(new Card(Card.KING,Card.Suit.CLUBS));         //player
        cards.add(new Card(Card.KING,Card.Suit.SPADES));        //dealer
        
        cards.add(new Card(Card.QUEEN,Card.Suit.SPADES));       //player
        cards.add(new Card(8,Card.Suit.DIAMONDS));             //dealer
        
     
        //Test case 8
        
        cards.add(new Card(8,Card.Suit.CLUBS));                 //player
        cards.add(new Card(Card.KING,Card.Suit.SPADES));        //dealer
        
        cards.add(new Card(5,Card.Suit.SPADES));               //player
        cards.add(new Card(6,Card.Suit.DIAMONDS));             //dealer
        
        cards.add(new Card(Card.KING,Card.Suit.DIAMONDS));     //dealer
        
        //Test case 9
        
        cards.add(new Card(7,Card.Suit.CLUBS));                 //player
        cards.add(new Card(Card.KING,Card.Suit.SPADES));        //dealer
        
        cards.add(new Card(6,Card.Suit.SPADES));               //player
        cards.add(new Card(6,Card.Suit.DIAMONDS));             //dealer
        
        cards.add(new Card(Card.KING,Card.Suit.DIAMONDS));     //dealer
        
        //Test case 10
        cards.add(new Card(6,Card.Suit.CLUBS));                 //player
        cards.add(new Card(Card.KING,Card.Suit.SPADES));        //dealer
        
        cards.add(new Card(8,Card.Suit.SPADES));               //player
        cards.add(new Card(6,Card.Suit.DIAMONDS));             //dealer
        
        cards.add(new Card(Card.KING,Card.Suit.DIAMONDS));     //dealer
    }
    
    @Override
    public boolean shuffleNeeded() {
        return false;
    }
}