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
    
    // final stone that landed on empty is not captured in ayo rules
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


  @Override
  public int moveStones(int startPit, int playerNum) throws InvalidMoveException {
    if (!isValidMove(startPit, playerNum)) {
      throw new InvalidMoveException("Pick a valid pit!");
    }

    int saveCurrPlayer = playerNum;

    int sCurrStoreCount = getPlayerStoreCount(saveCurrPlayer);
    
    int numMoves = distributeStones(startPit); // Start the laps from startPit

    MancalaDataStructure boardGame = getDataStructure();
    int pitNumAfterMove = boardGame.getIteratorPos();
    
    if (isCapturePossible(pitNumAfterMove, playerNum)) {
      pitNumAfterMove = (pitNumAfterMove <= 6) ? ++pitNumAfterMove : pitNumAfterMove;
      int capturedStones = (captureStones(pitNumAfterMove));
      addStoneStore(playerNum, capturedStones);
    }

    // now for this version (ayo rules) there seems to be no free turn
    setPlayer((playerNum == 1) ? 2 : 1);
    
    return (getPlayerStoreCount(saveCurrPlayer) - sCurrStoreCount);
  }

  public boolean isCapturePossible(int pitIndex, int playerNum) {
    // logic for now is that keep pitIndex 0 based as per getItartorPos method, then use that to check
    // if it is store, since short circuiting it's safe to assume isCapture will not need to check for store
    // so inside that method it's safe to put it back to 1 based and ignore stores
    if(isStore(pitIndex)) {
      return false;
    } 
    
    // here we can use the condition above as an advantage since free turns are when players land on stores..

    // now put it to 1 based, in other words undo the pitPos method
    pitIndex = (pitIndex <= 6) ? ++pitIndex : pitIndex;

    return (isCapture(pitIndex, playerNum));
  } 
}
