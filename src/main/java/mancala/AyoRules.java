package mancala;

public class AyoRules extends GameRules {

  private static final int PLAYER_1_STORE = 6;
  private static final int PLAYER_2_STORE = 13;
  private static final int TOTAL_PITS = 12;

  public AyoRules() {
    this(4);
  }

  public AyoRules(int numStonesStart) {
    super(numStonesStart);
  }

  @Override
  int captureStones(int stoppingPoint) {
    int oppositePitIndex = (13 - stoppingPoint);

    int capturedStones = removePitStones(oppositePitIndex);
    
    return capturedStones;
  }

  @Override
  int distributeStones(int startPit) {
    boolean skipStartPit = true;
    setIterator(startPit, skipStartPit);
    int numMoves = removePitStones(startPit);
    int numMovesNthLap = numMoves;
    boolean laps = true;
    Countable currentSpot = null;

    while(laps) {
      for(int i = 0; i < numMovesNthLap; i++) {
        //... do n distributes
        currentSpot = nextCountable();
        currentSpot.addStone();
      }
      // must not be an empty pit nor can't be any stores
      if(!(currentSpot instanceof Store) && (currentSpot.getStoneCount() > 1)) {
        //... should set new num moves
        // should subtract 1 since need to add the current stone
        numMovesNthLap = (currentSpot.removeStones());

        // add to the current pit
        // update the total moves
        numMoves += numMovesNthLap;
      } else {
        //... should terminate loop
        laps = false;
      }
    }

    return numMoves;
  }

  
  private boolean isStore(int pitIndex) {
    return pitIndex == PLAYER_1_STORE || pitIndex == PLAYER_2_STORE;
  }

  private boolean isCapturePossible(int pitIndex, int playerNum) {
    return !isStore(pitIndex) && isCapture(pitIndex, playerNum);
  }


  @Override
  public int moveStones(int startPit, int playerNum) throws InvalidMoveException {
    if (!isValidMove(startPit, playerNum)) {
      throw new InvalidMoveException("Pick a valid pit!");
    }
    
    int numMoves = distributeStones(startPit); // Start the laps from startPit
    
    int pitNumAfterMove = getNextPitIndex(startPit+numMoves);
    
    if (isCapturePossible(pitNumAfterMove, playerNum)) {
      // subtract one since this version does not take the last pit the landed on empty
      int capturedStones = (captureStones(pitNumAfterMove)-1);
      // add back to the pit to keep the last stone
      addStone(pitNumAfterMove);
      addStoneStore(playerNum, capturedStones);
    }
    
    int currentPlayer = playerNum;

    setPlayer((playerNum == 1) ? 2 : 1);

    return getPlayerStoreCount(currentPlayer);
  }
}
