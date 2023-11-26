package mancala;

public class AyoRules extends GameRules {

  public AyoRules() {
    this(4);
  }

  public AyoRules(final int numStonesStart) {
    super(numStonesStart);
  }

  @Override
  /* package */int captureStones(final int stoppingPoint) {
    final int oppositePitIndex = 13 - stoppingPoint;

    // final stone that landed on empty is not captured in ayo rules
    return removePitStones(oppositePitIndex);
  }

  @Override
  /* package */int distributeStones(final int startPit) {
    final boolean skipStartPit = true;

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
        numMovesNthLap = currentSpot.removeStones();

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
  public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {
    if (!isValidMove(startPit, playerNum)) {
      throw new InvalidMoveException("Pick a valid pit!");
    }

    final int saveCurrPlayer = playerNum;

    final int sCurrStoreCount = getPlayerStoreCount(saveCurrPlayer);
    
    distributeStones(startPit); // Start the laps from startPit

    final MancalaDataStructure boardGame = getDataStructure();
    int pitNumAfterMove = getIteratorPosBoard(boardGame);
    
    if (isCapturePossible(pitNumAfterMove, playerNum)) {
      pitNumAfterMove = (pitNumAfterMove <= 6) ? ++pitNumAfterMove : pitNumAfterMove;
      final int capturedStones = captureStones(pitNumAfterMove);
      addStoneStore(playerNum, capturedStones);
    }

    // now for this version (ayo rules) there seems to be no free turn
    setPlayer((playerNum == 1) ? 2 : 1);
    
    return getPlayerStoreCount(saveCurrPlayer) - sCurrStoreCount;
  }

  private int getIteratorPosBoard(final MancalaDataStructure game) {
    return game.getIteratorPos();
  }

  public boolean isCapturePossible(int pitIndex, final int playerNum) {
    // logic for now is that keep pitIndex 0 based as per getItartorPos method, then use that to check
    // if it is store, since short circuiting it's safe to assume isCapture will not need to check for store
    // so inside that method it's safe to put it back to 1 based and ignore stores
    if(isStore(pitIndex)) {
      return false;
    } 
    int index = pitIndex;

    // here we can use the condition above as an advantage since free turns are when players land on stores..

    // now put it to 1 based, in other words undo the pitPos method
    index = index <= 6 ? ++index : index;

    return isCapture(index, playerNum);
  } 
}
