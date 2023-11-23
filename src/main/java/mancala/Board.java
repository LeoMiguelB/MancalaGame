package mancala;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
  private static final long serialVersionUID = 2395882136664685248L;
  private ArrayList<Store> store;
  private ArrayList<Pit> pits;
  private boolean freeTurn;

  public Board() {
    store = new ArrayList<>(2);
    pits =  new ArrayList<>(12);
    setUpPits();
    setUpStores();
    initializeBoard();
    freeTurn = false;
  }

  /*
   * required methods of this class best to be package private; the MancalaGame is the agregator; 
   * that is accessible to ui and not this class
   */

  //12 empty pits
  void setUpPits() {
    for(int i = 0; i < 12; i++) {
      pits.add(i,new Pit());
    }
  }

  ArrayList<Pit> getPits() {
    return pits;
  }

  ArrayList<Store> getStores() {
    return store;
  }

  void setUpStores() {
    for(int i = 0; i < 2; i++) {
      store.add(new Store());
    }
  }

  // distributing 4 stones to each pit
  void initializeBoard() {
    for (Pit pit : getPits()) {
      pit.addStones(4);
    }
  }

  void resetBoard() {
    for (Pit pit : getPits()) {
      pit.removeStones();
      pit.addStones(4);
    }
    for(Store currStore: getStores()) {
      currStore.removeStones();
    }
  }

  void registerPlayers(Player one, Player two) {
    getStores().get(0).setOwner(one);
    getStores().get(1).setOwner(two);
    one.setStore(getStores().get(0));
    two.setStore(getStores().get(1));
  }

  int moveStones(int startPit, Player player) throws InvalidMoveException {
    // comparison below is not indexed (since pits are from 1 to 12)
    // the move in mancala game already handles checking if the pit is empty
    boolean isValidMove = isPlayerOne(player) ? (startPit > 0 && startPit < 7)
        : (startPit > 6 && startPit <= 12);
    int startingPointIndexed = (startPit-1);
    if (!isValidMove) {
      throw new InvalidMoveException();
    }
    try {
      int totalNumOfStones = distributeStones(startPit);
      // calculate how many stones would have passed the players store
      int distanceReference = isPlayerOne(player) ? 7 : 13;
      int distanceFromStart = (distanceReference - startPit);
      int numOfPasses = 0;
      if ((totalNumOfStones >= distanceFromStart)) {
        // account for the first pass, and then start counting from the store again
        int beginCountAgain = (totalNumOfStones - distanceFromStart);
        numOfPasses = 1;
        // must go through 12 pits and plus another stone to reach the store
        numOfPasses += (beginCountAgain/13);
        // now add to the respective store
        int storeToAdd = isPlayerOne(player) ? 0 : 1;
        addStonesStore(getStore(storeToAdd),numOfPasses);
        // if beginCountAgain is zero that means player landed in there store
        if(beginCountAgain == 0) {
          setFreeTurn(true);
          return player.getStoreCount();
        }
      }
      int startPitIndexed = (startPit-1);
      int stopPit = ((totalNumOfStones + (startPitIndexed)) > 11) ? ((totalNumOfStones + startPitIndexed) - 11)
          : (totalNumOfStones + startPitIndexed);
      // must be on there side (stop pit is indexed)
      boolean allowedToCapture = isPlayerOne(player) ? (stopPit < 6 && stopPit >= 0) : (stopPit < 12 && stopPit >= 6);  
      // checks done, now actually put the stones in there place; subtract number of passed to the store
      putStones(startPitIndexed, (totalNumOfStones-numOfPasses), player);
      // check to see if player has landed on a empty pit; make sure to exclude the stone that just added
      if((getNumStones((stopPit+1))-1) == 0 && allowedToCapture) {
        int numCaptured = captureStones(stopPit+1);
        // add these capture stones to the players store
        addStonesStore(player.getStore(), numCaptured);
      }
    } catch (PitNotFoundException e) {
      System.out.println(e);
    }
    return player.getStoreCount();
  }

  // wrapper for adding stones ot a store
  private void addStonesStore(Store store, int amount) {
    store.addStones(amount);
  }

  // assuming this is not indexed
  int distributeStones(final int startingPoint) throws PitNotFoundException {
    if (startingPoint > 12 || startingPoint < 1) {
      throw new PitNotFoundException();
    }
    int startPIndexed = startingPoint-1;
    // essentially total number of stones added to pits and stores is number of moves
    return removeStonesPit(getPit(startPIndexed));
  }

  int captureStones(final int stoppingPoint) throws PitNotFoundException {

    if (stoppingPoint > 12 || stoppingPoint < 1) {
      throw new PitNotFoundException();
    }

    // change to indexed
    int stopPIndexed = (stoppingPoint-1); 

    int stonesRemoved = 0;

    // checks have been made in moveStones
    // if stopping pit is between 6 - 11 then it player 2 that will capture, otherwise it's player one
    boolean isP2Turn = (stopPIndexed > 5 && stopPIndexed < 12);

    // Store storeToAdd = isPlayerTwoCapturing ? getStores().get(1) : getStores().get(0);
    
    int distance = isP2Turn ? (11 - stopPIndexed) : (5 - stopPIndexed);
    int oppositePit = isP2Turn ? (distance) : (6 + distance);
    // should just be one stone in this pit    
    stonesRemoved += removeStonesPit(getPit(stopPIndexed));
    stonesRemoved += removeStonesPit(getPit(oppositePit));

    return stonesRemoved;
  }

  // wrapper for removing stones of a specific pit
  private int removeStonesPit(final Pit pit) {
    return pit.removeStones();
  }

  int getNumStones(final int pitNum) throws PitNotFoundException {
    
    if (pitNum > 12 || pitNum < 1) {
      throw new PitNotFoundException();
    }
    // indexed the pitNum
    return getPitStoneCount(getPit(pitNum-1));
  }

  // wrapper for getting the stone count of a pit
  private int getPitStoneCount(Pit pit) {
    return pit.getStoneCount();
  }
  

  boolean isSideEmpty(final int pitNum) throws PitNotFoundException {
    if (pitNum > 12 || pitNum < 1) {
      throw new PitNotFoundException();
    }

    final boolean isPlayerOne = (pitNum < 7 && pitNum > 0);

    final int min = (isPlayerOne) ? 0 : 6;

    final int max = (isPlayerOne) ? 6 : 12;

    int totalForSide = 0;

    for (int i = min; i < max; i++) {
      try {
        // get num stones is not indexed -> must add one
        totalForSide += getNumStones(i+1);
      } catch(PitNotFoundException e) {
        System.out.println(e);
      }
    }

    return (totalForSide == 0);
  }

  /*
   ****************************************************************************
   ****************************************************************************
   Wrappers and helpers section
  */

  // since text ui needs this
  public void setFreeTurn(final boolean status) {
    freeTurn = status;
  }

  public boolean getFreeTurn() {
    return freeTurn;
  }

  Pit getPit(int whichPit) {
    return getPits().get(whichPit);
  }

  private Store getStore(int whichStore) {
    return getStores().get(whichStore);
  } 


  private Player getStoreOneOwner() {
    Store storeOne = getStore(0); 
    return storeOne.getOwner();
  }


  private boolean isPlayerOne(final Player checkPlayer) {
    // return (getStoreOneOwner().getName().equals(checkPlayer.getName()));
  
    return (isNamesEqual(getPlayerName(getStoreOneOwner()), getPlayerName(checkPlayer)));
  }

  // helper for checking if anmes are equals
  private boolean isNamesEqual(final String nameOne, final String nameTwo) {
    return nameOne.equals(nameTwo);
  }

  // wrapper for getting player's names
  private String getPlayerName(final Player player) {
    return player.getName();
  }


  // defined by me; allowed to be indexed
  void putStones(final int startingPit, final int numOfMoves, final Player currPlayer) {
    // as in where is the store located
    // final int whichStop = isPlayerOne(currPlayer) ? 5 : 11;
    // ignores stores; already calculated by totalNumOfStores-numOfPasses
    // indexed; + 1 so that it starts on the next pit
    int chaser = startingPit == 11 ? 0 : (startingPit+1);
    int whenToResetChaser = 11;
    for (int i = numOfMoves; i > 0; i--) {
      if (chaser > whenToResetChaser) {
        chaser = 0;
        addPitStone(chaser);
        chaser++;
      }else {
        addPitStone(chaser);
        chaser++;
      }
    }
  }

  // package private since should be able to be acceessed within the MancalaGame class
  void addPitStone(final int startingPit) {
    final Pit pit = getPitStone(getPits(), startingPit);
    pit.addStone();
  }

  void addStone(final Pit pit, final int stoneCount) {
    if(stoneCount == -1) {
      pit.addStone();
      return;  
    }
    pit.addStones(stoneCount);
  }

  void addPitStone(final int startingPit, final int amount) {
    final Pit pit = getPitStone(getPits(), startingPit);
    pit.addStones(amount);
  }

  // wrapper for getting a specific pit
  Pit getPitStone(final List<Pit> pits, final int startingPit){
    return pits.get(startingPit);
  }

  /*
   ****************************************************************************
   string builder section for toString
  */

  private String printBoard() {
    final StringBuilder board = new StringBuilder();

    board.append(buildRowPlayerTwo());
    board.append(printStores());
    board.append(buildRowPlayerOne());
    
    return board.toString();
  }

  private String buildRowPlayerOne() {
    final StringBuilder rowPlayerOne = new StringBuilder();
    // now print the pits
    rowPlayerOne.append("\t");
    for(int j = 0; j < 6; j++) {
      if(j == 0) {
        rowPlayerOne.append("|");
      }
      rowPlayerOne.append(" " + getPit(j) + " |");
    }
    rowPlayerOne.append("\n");
    return rowPlayerOne.toString();
  }

  private String buildRowPlayerTwo() {
    final StringBuilder rowPlayerTwo = new StringBuilder();
    
    // now print the pits
    rowPlayerTwo.append("\t");
    for(int j = 11; j > 5; j--) {
      if(j == 11) {
        rowPlayerTwo.append("|");
      }
      rowPlayerTwo.append(" " + getPit(j) + " |");
    }
    rowPlayerTwo.append("\n");

    return rowPlayerTwo.toString();
  }

  // player ones store the first store -> will be placed to the right
  // player two's store is always the second store -> will be placed to left
  private String printStores() {
    final StringBuilder stores = new StringBuilder();
    stores.append("|"+getStore(1)+"|" + "*" +"\t\t\t\t\t\t  " + "*" + "|"+ getStore(0)+"|" + "\n");
    return stores.toString();
  }

  @Override
  public String toString() {
    return printBoard();
  }

}