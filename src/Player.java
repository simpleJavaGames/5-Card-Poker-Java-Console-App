import java.util.Arrays;

public class Player {
    private Card[] playerHand = new Card[5];
    private byte handvalue;
    private byte highestCard;
    private byte rankingMatch;

    Player() {
    }

    //Receive initial hand.
    public void updateHand(Card[] initialHand) {
        playerHand = initialHand;
    }

    //Return current hand
    public Card[] getHand() {
        return playerHand;
    }

    //Use bubble sort to sort the player hand.
            public void sortHand() {
                for (int i = 0; i < playerHand.length; i++) {
                    for (int j = 0; j < playerHand.length - 1 - i; j++) {
                        if (playerHand[j].getCardRank() > playerHand[j + 1].getCardRank()
                        ) {
                            Card temp = playerHand[j];
                            playerHand[j] = playerHand[j + 1];
                            playerHand[j + 1] = temp;
                        }
                    }
        }
    }

    public void setHandValue(byte handvalue){
        this.handvalue = handvalue;
    }
    public byte getHandValue(){
        return handvalue;
    }

    public void setrankingMatch(byte rankingMatch){
        this.rankingMatch = rankingMatch;
    }

    public void setCurrentHighestCard(byte highestCard){
        this.highestCard = highestCard;
    }

    public byte getRankingMatch (){
        return rankingMatch;
    }

    public byte getHighestCard(){
        return highestCard;
    }

    //This will make every index of the array into an empty slot as well as reset handvalue, highestcard, ranking match.
    public void resetHand(){
        Arrays.fill(playerHand,null);
        handvalue = 0;
        highestCard = 0;
        rankingMatch = 0;
    }
}
