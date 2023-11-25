package mancala;
import java.io.Serializable;

public class MancalaGame implements Serializable{
  private static final long serialVersionUID = 4705517013251752724L;
  private GameRules board;
  private Player currentPlayer;
  private Player playerOne;
  private Player playerTwo;
  
  public MancalaGame() {
    // default is kalah rules
    setGameRules(new KalahRules());
  }  

  public MancalaGame(GameRules whichRule) {
    setGameRules(whichRule);
  }
  
  // private as per UML diagram provided
  private GameRules getBoard() {
    return board;
  }  
  
  public Player getCurrentPlayer() {
    return currentPlayer;
  }  

  
  public int getNumStones(final int pitNum) throws PitNotFoundException {
    // not indexed since pits are num from 1 to 12
    if (pitNum < 1 || pitNum > 12) {
      throw new PitNotFoundException();
    }

    return board.getNumStones(pitNum);
  }
  
  public int getStoreCount(final Player player) throws NoSuchPlayerException {
    // assuming player names are unique
    if(!containsPlayer(player)) {
      throw new NoSuchPlayerException();
    }
    
    // check if its' player one; to do this storeOne always holds player one
    boolean isPlayerOne = (getPlayerName(player).equals(getPlayerName(getPlayerOne())));
    int playerNum = isPlayerOne ? 1 : 2;

    return board.getPlayerStoreCount(playerNum);
  }

  public Player getWinner() throws GameNotOverException, NoSuchPlayerException {
    
    Player winner;
    if(isGameOver()) {
      // need to know whose side is not empty
      int playerNotEmpty = (isSideEmpty(1)) ? 2 : 1;

      endGameEmptySides(playerNotEmpty);

      if(getStoreCount(getPlayerOne()) == getStoreCount(getPlayerTwo())) {
        // indicates a tie
        winner = null;
      } else if(getStoreCount(getPlayerOne()) > getStoreCount(getPlayerTwo())) {
        winner =  getPlayerOne();
      } else {
        winner = getPlayerTwo();
      }
    } else {
    
      throw new GameNotOverException();
    }

    return winner;
    
  }
  
  public boolean isGameOver() {
    boolean sideOne = (isSideEmpty(1));
    boolean sideTwo = (isSideEmpty(7));
    return (sideOne || sideTwo);
  }
  
  // wrapper for checking if one side is empty
  private boolean isSideEmpty(final int pitNum){
    return board.isSideEmpty(pitNum);
  }
  
  // return the total number of stones remaining the players side pits
  // players 1's pit starts at pit 1 to 6, same pattern applies to player 2
  // should check if the pit is empty (shoudl not have to check within board again)
  public int move(final int startPit) throws 
  InvalidMoveException {
    // player 1 can only choose from pits 1 to 6, opposite for player 2
    final boolean isPlayerOne = (getPlayerName(currentPlayer).equals(getPlayerName(getPlayerOne())));
    
    // the other checks or done inside the GameRules implmentations...
    
    if(isPitEmpty(startPit)) {
      throw new InvalidMoveException("Empty Pit");
    }
    
    board.moveStones(startPit, isPlayerOne ? 1 : 2);
    
    // now should swithc current player (note: free turn is handled in KalahRules not here...)
    setCurrentPlayer(isPlayerOne ? getPlayerTwo() : getPlayerOne());
    
    return getSidePits(getCurrentPlayer());
    
  }
  
  public void setBoard(final GameRules theBoard) {
    board = theBoard;
  }  
  
  public void setCurrentPlayer(final Player player) {
    currentPlayer = player;
  }  
  
  public void setPlayers(final Player onePlayer, final Player twoPlayer) {
    // now set up the board accordingly
    board.registerPlayers(onePlayer, twoPlayer);
    setPlayerOne(onePlayer);
    setPlayerTwo(twoPlayer);
    setCurrentPlayer(onePlayer);    
  }
  
  
  public void startNewGame() {
    board.resetBoard();
    // should also put player one as current player again
    setCurrentPlayer(getPlayerOne());
  }
  
  // setter for the game rules
  private void setGameRules(GameRules whichRule) {
    board = whichRule;
  }
  
  // factory method for the setGameRules
  // decouples the textui from the GameRules class
  public void changeRules(int choice) {
    if(choice == 1) {
      setGameRules(new AyoRules());
    } else if (choice == 2) {
      setGameRules(new KalahRules());
    }
  }
  
  
  
  /*
  *****************************************************************************
  wrappers and helpers
  */
  
  private void endGameEmptySides(int playerNum) {
    int max = (playerNum == 1) ? 6 : 12;
    int min = (playerNum == 1) ? 1 : 7;
    
    for(int i = min; i <= max; i++) {
      int stonesToAdd = board.removePitStones(i);
      board.addStoneStore(playerNum, stonesToAdd);
    }
  }

  private void setPlayerOne(Player pOne) {
    playerOne = pOne;
  }
  
  private void setPlayerTwo(Player pTwo) {
    playerTwo = pTwo;
  }
  
  // helper for get getting players name
  private String getPlayerName(Player p) {
    return p.getName();
  }
  
  // helper if this player exists in the game
  private boolean containsPlayer(Player p) {
    String pOneName = getPlayerName(getPlayerOne());
    String pTwoName = getPlayerName(getPlayerTwo());
    return (getPlayerName(p).equals(pOneName) || getPlayerName(p).equals(pTwoName));
  }
  
  // helper method to add up the the specified pits
  private int addAllPits(final int min, final int max) {
    int total = 0;
    for (int i = min; i < max; i++) {
      try {
        // getNumStones is not indexed so add one
        total += getNumStones(i+1);
      } catch (PitNotFoundException e) {
        System.out.println(e);
      }
    }
    
    return total;
  }
  

  // helper for checking if pit is empty
  private boolean isPitEmpty(final int whichPit) {
    boolean isEmpty = false;
    try {
      isEmpty = (getNumStones(whichPit) == 0);
    } catch (PitNotFoundException e) {
      System.out.println(e);
    }
    return isEmpty;
  }

  // helper method for the the move method
  // gets the number of rocks in each pit on the players side
  private int getSidePits(final Player player) {
    // return ((getPlayerName(player).equals(getPlayerName(getPlayerOne()))) ? addAllPits(0, 6) : addAllPits(6, 12));
    return (isNamesEqual(getPlayerName(player), getPlayerName(getPlayerOne()))) ? addAllPits(0, 6) : addAllPits(6, 12);
  }

  private boolean isNamesEqual(final String nameOne, final String nameTwo) {
    return nameOne.equals(nameTwo);
  }

  public String getCurrentPlayerName() {
    return getName(getCurrentPlayer());
  }

  // my method
  public Player getPlayerOne() {
    return playerOne;
  }

  // also my method
  public Player getPlayerTwo() {
    return playerTwo;
  }

  private String getName(final Player player) {
    return player.getName();
  }
  

  /*
   *****************************************************************************
   now building the string for the text representation of the game
  */
  private String manacalaGameTextUI() {
    final StringBuilder mancalaGameText = new StringBuilder();
    mancalaGameText.append("\t\t" + getCurrentPlayerName() + " turn\n\n");
    mancalaGameText.append(getBoard());
    return mancalaGameText.toString();
  }

  @Override
  public String toString() {
    return manacalaGameTextUI();
  }
}