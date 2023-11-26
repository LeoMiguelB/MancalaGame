package mancala;

public class KalahRules extends GameRules {

  public KalahRules() {
    this(4);
  }

  public KalahRules(final int numStonesStart) {
    super(numStonesStart);
  }

  @Override
  int captureStones(final int stoppingPoint) {
    final int oppPitNumber = 13 - stoppingPoint;

    int captured = removePitStones(oppPitNumber);

    // remove the stone that landed in the empty spot aswell
    captured += removePitStones(stoppingPoint);
    
    return captured;
  }

  @Override
  int distributeStones(final int startPit) {
    final int numMoves = removePitStones(startPit);
    final boolean shouldSkip = false;
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


  @Override
  public int moveStones(final int startPit, final int playerNum) throws InvalidMoveException {
    if (!isValidMove(startPit, playerNum)) {
      throw new InvalidMoveException("Pick a valid pit!");
    }

    // since swapping of players might occur, need to save the curr playerNum
    final int saveCurrPlayer = playerNum;

    // in order to calculate the number of stones added
    // to the players store must save this to calc difference
    final int sCurrStoreCount = getPlayerStoreCount(saveCurrPlayer);
    
    int numMoves = distributeStones(startPit);

    // in this variation of the game player gets free turn if in store
    // player one is at 6, and player two is at 13
    int playerStore = (playerNum == 1) ? 6 : 13;

    // simply get the current position of iterator
    MancalaDataStructure boardGame = getDataStructure();
    int pitNumAfterMove = boardGame.getIteratorPos();

    if (isCapturePossible(pitNumAfterMove, playerNum)) {
      // if it passed the store test it means we are not dealing with a store hence we can go back to 1 based
      pitNumAfterMove = (pitNumAfterMove <= 6) ? ++pitNumAfterMove : pitNumAfterMove;
      int capturedStones = captureStones(pitNumAfterMove);
      addStoneStore(playerNum, capturedStones);
    } 

    // effectively calcs the difference
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
    setPlayer((playerNum == 1) ? 2 : 1);

    // now put it to 1 based, in other words undo the pitPos method
    pitIndex = (pitIndex <= 6) ? ++pitIndex : pitIndex;

    return (isCapture(pitIndex, playerNum));
  } 

}