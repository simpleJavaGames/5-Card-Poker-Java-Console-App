import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class main {
    public static void main(String[] args) {
        Deck deck = new Deck();
        Scanner sc = new Scanner(System.in);
        Player opponent = new Player();
        Player player = new Player();

        boolean playing = true;

        //TODO add something telling what hand the player has.

        do {
            //Dealing the initial hand for both player and opponent.
            opponent.updateHand(deck.dealFiveCards());
            player.updateHand(deck.dealFiveCards());
            //TODO remove this rigged hand dealt.
            //opponent.updateHand(deck.deal5SetCardsTesting());

            player.setHandValue(assignHandValue(player));
            showHand(player);

            boolean swapYN = playerSwapCardsYN(sc);

            //This is where the player chooses which cards he would like to swap (if any)
            if (swapYN == true) {
                System.out.print("\nHow many card(s) would you like to swap? ");
                swapCards(cardsToSwapArray(sc, howManyCardsToSwap(sc)), player, deck);
                showHand(player);
            } else {
                System.out.println("\nKeeping hand.");
            }

            waitASec();
            showOpponentHand(opponent);

            //This will set the initial hand value for the opponent so he can make his decisions.
            opponent.setHandValue(assignHandValue(opponent));

            //This will see first if the opponent's hand is even worth swapping out, and if it is, than it will use the swapping algorithm to swap cards.
            if (opponent.getHandValue() <= 4) {
                swapCards(opponentWhichCardsToDiscard(opponent), opponent, deck);
                opponent.setHandValue(assignHandValue(opponent));
            }

            System.out.println("\n==== Showdown ====");

            //This will show your hand again
            waitASec();
            showHand(player);

            //This will display the opponent's hand.
            waitASec();
            showOpponentHand(opponent);

            //This will decide the winner.
            decideWinner(player, opponent);

            //This will ask the player if they want to play again.
            playing = playAgainYN(sc);

            //This will reset the game by resetting all handvalues, the deck and whatever cards the players had.
            if(playing == true){
                resetGame(player,opponent,deck);
                System.out.println("\n ==================== \n");
            }
        }while (playing == true);


    }

    // This method returns which cards to send back in a byte array.
    public static byte[] cardsToSwapArray(Scanner sc, byte howManyCardsToSwap){
        sc.nextLine();
        boolean checkPassed = false;
        byte[] cardsToSwap = new byte[howManyCardsToSwap];
        Set<String> store = new HashSet<>();

        //This makes sure that the userInput can only have commas and numbers, to prevent program from crashing.
        do {
            //Get the user input
            System.out.print("Please enter the cards you want to swap,from lowest to highest, separated by a comma (1,2,3) etc. ");
            String userInput = sc.nextLine().trim();
            store.clear();

            //Check 1, is it in commas and numbers??
            if (userInput.matches("^[0-4\\,]+$")) {
                //Check 2, is it the proper number of cards to discard?
                if(userInput.length() != howManyCardsToSwap + howManyCardsToSwap-1){
                    System.out.print("Please enter the correct number of cards to discard. ");
                    continue;
                }
                //Check 3, is there duplicate cards being discarded?
                boolean dupeFound = false;
                    String[] cardsToSwapString = userInput.split(",");
                    for(String dupeChecker : cardsToSwapString){
                        if(store.add(dupeChecker) == false){
                            System.out.println("You can't discard the same card twice.");
                            dupeFound = true;
                            break;
                        }
                    }
                    if(dupeFound == false){
                        //if everything passes, than break out of do-while and return the cards to swap as an array.
                        for (int i = 0; i < cardsToSwap.length; i++) {
                            cardsToSwap[i] = Byte.parseByte(cardsToSwapString[i]);
                        }
                        checkPassed = true;
                    }
            } else {
                System.out.print("Please enter a valid input. ");
                userInput = sc.nextLine().trim();
            }
        }while(!checkPassed);


        return cardsToSwap;
    }

    public static byte howManyCardsToSwap(Scanner sc){
        boolean checkPassed = false;
        do {
            if (sc.hasNextByte()) {
                byte userInput = sc.nextByte();
                if(userInput > 0 && userInput <=5 ){
                    return userInput;
                }else{
                    sc.nextLine();
                    System.out.print("Please enter a number between 1 - 5: ");
                }
            }else{
                sc.nextLine();
                System.out.print("Please enter a valid input. ");
            }
        }while(!checkPassed);
        return -1; // something went wrong, -1 was returned (should not happen).
    }

    //Showing the player their hand.
    public static void showHand(Player player){
        Card[] playerHand = player.getHand();
        player.sortHand();
        System.out.print("\nYour hand is: \n ");
        for (int i=0;i<5;i++){
            System.out.println(i+": "+playerHand[i].toCardString());
            System.out.print(" ");
        }

        System.out.println();
        switch (player.getHandValue()) {
            case 10:
                System.out.println("You currently have a Royal Straight Flush!");
                break;
            case 9:
                System.out.println("You currently have a Straight flush! ");
                break;
            case 8:
                System.out.println("You currently have a 4-of-a-kind with " + player.getRankingMatch());
                break;
            case 7:
                System.out.println("You currently have a full house with "+ player.getRankingMatch() +" as the 3 of a kind!");
                break;
            case 6:
                System.out.println("You currently have a flush!");
                break;
            case 5:
                System.out.println("You currently have a Straight!");
                break;
            case 4:
                System.out.println("You currently have 3-of-a-kind with "+player.getRankingMatch()+"!");
                break;
            case 3:
                System.out.println("You currently have two pairs!");
                break;
            case 2:
                System.out.println("You currently have one pair of "+player.getRankingMatch()+".");
                break;
            case 1:
                System.out.println("You currently have a high Card of "+player.getHighestCard());
                break;
        }

    }

    public static boolean playerSwapCardsYN(Scanner sc){
        //Check if the player wants to swap cards.
        System.out.print("\n\bWould you like to swap cards? (Y/N) ");
        boolean passedTest = false;
        do{
            char playerChoice = sc.next().toUpperCase().charAt(0);
            if (playerChoice == 'Y') {
                return true;
            } else if (playerChoice == 'N') {
                return false;
            } else {
                System.out.print("Please enter a valid input: ");
            }
        }while(!passedTest);
        return false; // if somehow the loop gets broken, it will auto default send no.
    }

    public static void swapCards(byte[] cardsToSwap,Player player,Deck deck){
        Card[] playerHand = player.getHand();
        for(int i=0;i<cardsToSwap.length;i++){
            playerHand[cardsToSwap[i]] = deck.dealOneCard();
        }
        player.sortHand();
        player.setHandValue(assignHandValue(player));
    }

    //This assigns a hand value to the hand it is given.
    public static byte assignHandValue(Player player){
        /*Hand value goes as follows:
        10:Royal flush
        9:Straight flush
        8:4 of a kind
        7:Full house
        6:Flush
        5:Straight
        4:3 of a kind
        3:2 pair
        2:1 pair
        1:High Card
         */
        Card[] playerHand = player.getHand();
        player.sortHand();
        //Checking for flushes and straights.
        boolean isAFlush = false;
        for (int j=0;j<playerHand.length-1;j++){
            if(playerHand[j].getCardSuit() != playerHand[j+1].getCardSuit()){
                //it isn't a flush
                isAFlush = false;
                break;
            }else if(j == playerHand.length-2){
                isAFlush=true;//it's a flush
            }
        }
        if(playerHand[0].getCardRank() == playerHand[1].getCardRank()-1 && playerHand[2].getCardRank()-2 == playerHand[3].getCardRank()-3 && playerHand[4].getCardRank()-4 == playerHand[0].getCardRank()){
            if (playerHand[0].getCardRank() == 10) {
                if (playerHand[1].getCardRank() == 11 && playerHand[2].getCardRank() == 12 && playerHand[3].getCardRank() == 13 && playerHand[4].getCardRank() == 14) {
                    if(isAFlush){
                        return 10;//it's a royal straight flush
                    }else{
                        return 5;//it's a straight
                    }
                }
            }else{
                if(isAFlush) {
                    return 9;//it's a straight flush
                }else{
                    return 5;//it's a straight
                }
            }
        }else if(playerHand[0].getCardRank() == playerHand[1].getCardRank()-1 && playerHand[2].getCardRank()-2 == playerHand[3].getCardRank()-3 && playerHand[4].getCardRank()-12 == playerHand[0].getCardRank()){
            if(isAFlush){
                return 9;//it's a straight flush
            }else{
                return 5; //straight using ace. 1,2,3,4,5
            }
        } else if(isAFlush){
            return 6;
        }
        Set<Byte> firstMatch = new HashSet<>();
        Set<Byte> secondMatch = new HashSet<>();

        //TODO currently, the kicker card is absolutely useless for deciding ties except for high card ties.
        byte firstCounter = 1;
        boolean firstAlreadyHasAMatch = false;
        byte secondCounter = 1;
        byte secondMatchValue = 0;
        byte firstMatchValue = 0;
        byte currentHigestCard =0;
        for(int i=0;i<playerHand.length;i++){
            if(playerHand[i].getCardRank() > currentHigestCard)currentHigestCard = playerHand[i].getCardRank();
            if(firstAlreadyHasAMatch == false || playerHand[i].getCardRank() == firstMatchValue){
                if(firstMatch.add(playerHand[i].getCardRank()) == false){
                    firstAlreadyHasAMatch = true;
                    firstCounter++;
                    firstMatchValue = playerHand[i].getCardRank();
                }
            }else{
                if(secondMatch.add(playerHand[i].getCardRank()) == false){
                    secondCounter++;
                    secondMatchValue = playerHand[i].getCardRank();
                }
            }
        }

        if(firstCounter == 2 && secondCounter == 2 && firstMatchValue == secondMatchValue){
            //We have a 4 of a kind.
            if(firstCounter == 4){
                player.setrankingMatch(firstMatchValue);
                return 8;
            }else{
                player.setrankingMatch(secondMatchValue);
                return 8;
            }
        }else if (firstCounter == 3 && secondCounter == 2 || secondCounter == 3 && firstCounter == 2){
            //We have a full house.
                if(firstCounter == 3){
                    player.setrankingMatch(firstMatchValue);
                    return 7;
                }else{
                    player.setrankingMatch(secondMatchValue);
                    return 7;
                }
        }else if (firstCounter == 3 || secondCounter == 3){
            //We have 3 of a kind
            if(firstCounter == 3){
                player.setrankingMatch(firstMatchValue);
                return 4;
            }else{
                player.setrankingMatch(secondMatchValue);
                return 4;
            }
        }else if(firstCounter == 2 && secondCounter == 2){
            //We have 2 pairs.
            if(firstMatchValue > secondMatchValue){
                player.setrankingMatch(firstMatchValue);
                return 3;
            }else{
                player.setrankingMatch(secondMatchValue);
                return 3;
            }
        }else if(firstCounter == 2 || secondCounter == 2){
            //We have 1 pair.
            if (firstCounter == 2){
                player.setrankingMatch(firstMatchValue);
                return 2;
            }else{
                player.setrankingMatch(secondMatchValue);
                return 2;
            }
        }else{
            //We have 1 high card.
            player.setCurrentHighestCard(currentHigestCard);
            return 1;
        }

    }

    //This takes a hand in and uses a algorithm to decide which cards to discard.
    public static byte[] opponentWhichCardsToDiscard(Player opponent){
        opponent.sortHand();
        Card[] opponentHand = opponent.getHand();

        switch (opponent.getHandValue()){
            case 4: //Three of a kind.
                byte[] ThreeOfaKind = new byte[2];
                byte rankingTrio = opponent.getRankingMatch();
                int counter3 =0;
                for(byte i=0;i<opponentHand.length;i++){
                    if(opponentHand[i].getCardRank() != rankingTrio){
                        ThreeOfaKind[counter3] = i;
                        counter3++;
                    }
                }
                return ThreeOfaKind;
            case 3: //Two pairs
                byte[] twoPair = new byte[1];
                    if(opponentHand[0].getCardRank() != opponentHand[1].getCardRank()){
                        twoPair[0] = 0;
                        return twoPair;
                    }else if(opponentHand[3].getCardRank() != opponentHand[4].getCardRank()){
                        twoPair[0] = 4;
                        return twoPair;
                    }else{
                        twoPair[0] = 2;
                        return twoPair;
                    }
            case 2: //One pair
                byte[] onePair = new byte[3];
                byte rankingPair = opponent.getRankingMatch();
                int counter =0;
                for(byte i=0;i<opponentHand.length;i++){
                    if(opponentHand[i].getCardRank() != rankingPair){
                        onePair[counter] = i;
                        counter++;
                    }
                }
                return onePair;
            case 1: //HighCard
                byte[] highCard = new byte[4];
                byte counterHighCard = 0;
                byte highestCard = opponent.getHighestCard();
                for(byte i=0;i<opponentHand.length;i++){
                    if(opponentHand[i].getCardRank() == highestCard){
                     //do nothing.
                    }else{
                        highCard[counterHighCard] = i;
                        counterHighCard++;
                    }
                }
            return highCard;
        }
        return null;
    }

    //This shows the opponent's hand.
    public static void showOpponentHand(Player opponent){
        Card[] playerHand = opponent.getHand();
        opponent.sortHand();
        System.out.print("\nOpponent's hand is: \n ");
        for (int i=0;i<5;i++){
            System.out.println(i+": "+playerHand[i].toCardString());
            System.out.print(" ");
        }

        System.out.println();

        switch (opponent.getHandValue()) {
            case 10:
                System.out.println("Your Opponent currently has a Royal Straight Flush!");
                break;
            case 9:
                System.out.println("Your Opponent currently has a Straight flush! ");
                break;
            case 8:
                System.out.println("Your Opponent currently has a 4-of-a-kind with " + opponent.getRankingMatch());
                break;
            case 7:
                System.out.println("Your Opponent currently has a full house with "+ opponent.getRankingMatch() +" as the 3 of a kind!");
                break;
            case 6:
                System.out.println("Your Opponent currently has a flush!");
                break;
            case 5:
                System.out.println("Your Opponent currently has a Straight!");
                break;
            case 4:
                System.out.println("Your Opponent currently has 3-of-a-kind with "+opponent.getRankingMatch()+"!");
                break;
            case 3:
                System.out.println("Your Opponent currently has two pairs!");
                break;
            case 2:
                System.out.println("Your Opponent currently has one pair of "+opponent.getRankingMatch()+".");
                break;
            case 1:
                System.out.println("Your Opponent currently has a high Card of "+opponent.getHighestCard());
                break;
        }
    }

    public static void decideWinner(Player player, Player opponent){

        waitASec();

        System.out.println();

        //if both players get high card.
        if(player.getHandValue() == 1 && opponent.getHandValue() == 1){
            if(player.getHighestCard() > opponent.getHighestCard()){
                //player wins
                System.out.println("You win!");
            }else if(player.getHighestCard() < opponent.getHighestCard()){
                //opponent wins
                System.out.println("Opponent wins.");
            }else{
                //Draw
                System.out.println("Draw.");
            }
            return;
        }

        if(player.getHandValue() == opponent.getHandValue()){
            //They have the same kinds of card,but have at least a pair, so check who has the higher pair.
            if(player.getRankingMatch() > opponent.getRankingMatch()){
                //player wins
                System.out.println("You win!");
            }else if (player.getRankingMatch() < opponent.getRankingMatch()){
                //opponent wins
                System.out.println("Opponent wins.");
            }else{
                //Draw.
                System.out.println("Draw.");
            }
        }else if(player.getHandValue()  > opponent.getHandValue()){
            //player wins
            System.out.println("You win!");
        }else{
            //opponent wins.
            System.out.println("Opponent wins.");
        }
    }

    //This will reset the game.

    public static void resetGame(Player player,Player opponent, Deck deck){
        deck.resetDeck();
        player.resetHand();
        opponent.resetHand();
    }

    public static boolean playAgainYN(Scanner sc) {
        boolean checkPassed = false;
        System.out.print("\nDo you want to play again? (Y/N) ");
        while (!checkPassed) {
            char playerChoice = sc.next().toUpperCase().charAt(0);
            if (playerChoice == 'Y') {
                return true;
            } else if (playerChoice == 'N') {
                return false;
            } else {
                System.out.print("Please enter a valid input. ");
            }
        }

        return false; // if somehow the loops gets broken, it will default send false.
    }

    public static void waitASec(){
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

}
