package mancala;

public class KalahRules extends GameRules {

  private static final int PLAYER_1_STORE = 6;
  private static final int PLAYER_2_STORE = 13;
  private static final int TOTAL_PITS = 12;

  public KalahRules() {
    this(4);
  }

  public KalahRules(int numStonesStart) {
    super(numStonesStart);
  }

  @Override
  int captureStones(int stoppingPoint) {
    int oppPitNumber = (13 - stoppingPoint);

    int captured = removePitStones(oppPitNumber);

    // remove the stone that landed in the empty spot aswell
    captured += removePitStones(stoppingPoint);
    
    return captured;
  }

  @Override
  int distributeStones(int startPit) {
    int numMoves = removePitStones(startPit);
    boolean shouldSkip = false;
    // this variation account for the
    // subtract one from the startPit, so that when next() is called it lands
    // on the startPit
    setIterator(startPit, shouldSkip);

    Countable currentSpot;

    for (int i = 0; i < numMoves; i++) {
      currentSpot = nextCountable();
      currentSpot.addStone();
    }

    return numMoves;
  }

  public boolean isCapturePossible(int pitIndex, int playerNum) {
    return (!isStore(pitIndex) && isCapture(pitIndex, playerNum));
  }

  private boolean isStore(int pitIndex) {
    return pitIndex == PLAYER_1_STORE || pitIndex == PLAYER_2_STORE;
  }


  @Override
  public int moveStones(int startPit, int playerNum) throws InvalidMoveException {
    if (!isValidMove(startPit, playerNum)) {
      throw new InvalidMoveException("Pick a valid pit!");
    }
    
    int numMoves = distributeStones(startPit);

    // in this variation of the game player gets free turn if in store
    // player one is at 6, and player two is at 13
    int playerStore = (playerNum == 1) ? 6 : 13;
    
    int pitNumAfterMove = getNextPitIndex(startPit+numMoves);

    if (isCapturePossible(pitNumAfterMove, playerNum)) {
      int capturedStones = captureStones(pitNumAfterMove);
      addStoneStore(playerNum, capturedStones);
    }
    
    int saveCurrP = playerNum;
    
    // if player lands on their store just dont' switch players to indicate a free
    // turn
    if ((pitNumAfterMove != playerStore)) {
      setPlayer((playerNum == 1) ? 2 : 1);
    }

    return getPlayerStoreCount(saveCurrP);
  }

}