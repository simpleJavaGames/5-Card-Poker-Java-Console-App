public class Card {
    private final byte cardSuit;
    private final byte cardRankByte;
    private String cardRankFinal;

    Card(byte cardSuit, byte cardRankbyte){
        this.cardSuit = cardSuit; //0 = Spades, 1 = Hearts, 2 = Diamonds , 3 = Clubs
        this.cardRankByte = cardRankbyte;
        switch (cardRankbyte){
            case 14 -> cardRankFinal = "Ace";
            case 11 -> cardRankFinal = "J";                         // Assign suit and rank to each card
            case 12 -> cardRankFinal = "Q";
            case 13 -> cardRankFinal = "K";
            default -> cardRankFinal = ""+cardRankbyte;
        }
    }

    public String toCardString(){
        switch(cardSuit){
            case 0:
                return cardRankFinal + " of Spades";                 // return each card as a string
            case 1:
                return cardRankFinal + " of Hearts";
            case 2:
                return cardRankFinal + " of Diamonds";
            case 3:
                return cardRankFinal + " of Clubs";
        }
        return null;
    }
    public byte getCardSuit(){
        return cardSuit;
    }
    public byte getCardRank(){
        return cardRankByte;
    }

}