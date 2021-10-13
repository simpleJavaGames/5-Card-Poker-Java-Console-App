import java.util.Stack;
import java.util.Collections;
public class Deck{
    protected Stack<Card> deckOfCards = new Stack<Card>();
    private Stack <Card> cardsOut = new Stack<Card>();

    // create the deck and fill it with all the cards.
    public Deck(){
        for(byte i=0;i<4;i++){
            for(byte j=2;j<15;j++) deckOfCards.add(new Card(i,j));
        }
        shuffleDeck();
    }

    //Shuffle Deck
    public void  shuffleDeck(){
        Collections.shuffle(deckOfCards);
    }

    //Deals the top card out and adds it to the cards out
    public Card dealOneCard(){
        cardsOut.push(deckOfCards.pop()); // This takes the top card of the deck, and puts it to the top of cards out.
        return cardsOut.peek();
    }

    //This deals a hand of 5 cards all at once and returns it as an array.
    public Card[] dealFiveCards(){
        Card []hand = new Card[5];
        for (int i=0;i<5;i++){
            hand[i] = dealOneCard();
        }
        return hand;
    }

    public Card[] deal5SetCardsTesting(){
        Card []hand = new Card[5];
        hand[0] = new Card((byte)0,(byte)2);
        hand[1] = new Card((byte)1,(byte)2);
        hand[2] = new Card((byte)2,(byte)3);
        hand[3] = new Card((byte)3,(byte)4);
        hand[4] = new Card((byte)2,(byte)14);
        return hand;
    }


    //This returns the deck's current size.
    public int deckOfCardsLength(){
        return deckOfCards.size();
    }

    //This returns the number of cards currently out
    public int numCardsOut(){
        return cardsOut.size();
    }

    //This will reset the deck by adding all the cards out back into the deck.
    public void resetDeck(){
        deckOfCards.addAll(cardsOut);
        cardsOut.clear();
        shuffleDeck();
    }
}