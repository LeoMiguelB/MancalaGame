package mancala;

import java.io.Serializable;

/**
 * Abstract class representing the rules of a Mancala game.
 * KalahRules and AyoRules will subclass this class.
 */
public abstract class GameRules implements Serializable{
    private MancalaDataStructure gameBoard;
    private int currentPlayer = 1; // Player number (1 or 2)
    private static final int TOTAL_PITS = 14;
    private static final long serialVersionUID = -2829534344787151722L;

    /**
     * Constructor to initialize the game board.
     */
    public GameRules() {
        // default to 4
        this(4);
    }

    public GameRules(int numStonesEach) {
        gameBoard = new MancalaDataStructure(numStonesEach);
        gameBoard.setUpPits();

    }

    /**
     * Get the number of stones in a pit.
     *
     * @param pitNum The number of the pit.
     * @return The number of stones in the pit.
     */
    public int getNumStones(int pitNum) {
        return gameBoard.getNumStones(pitNum);
    }

    /**
     * Get the game data structure.
     *
     * @return The MancalaDataStructure.
     */
    MancalaDataStructure getDataStructure() {
        return gameBoard;
    }

    /**
     * Check if a side (player's pits) is empty.
     *
     * @param pitNum The number of a pit in the side.
     * @return True if the side is empty, false otherwise.
     */
    boolean isSideEmpty(int pitNum) {
        // This method can be implemented in the abstract class.


        // set max for the player traversal
        int max = (pitNum > 0 && pitNum < 7) ? 7 : 13;
        int min = (pitNum > 0 && pitNum < 7) ? 1 : 7;


        // should terminate if atleast one pit has a stone
        // should be up to 6 since each side has 6 pits
        for (int i = min; i < max; i++) {
            System.out.println(i);
            System.out.println((getNumStones(i) >= 1));
            if(getNumStones(i) >= 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Set the current player.
     *
     * @param playerNum The player number (1 or 2).
     */
    public void setPlayer(int playerNum) {
        currentPlayer = playerNum;
    }

    /**
     * Perform a move and return the number of stones added to the player's store.
     *
     * @param startPit  The starting pit for the move.
     * @param playerNum The player making the move.
     * @return The number of stones added to the player's store.
     * @throws InvalidMoveException If the move is invalid.
     */
    public abstract int moveStones(int startPit, int playerNum) throws InvalidMoveException;

    /**
     * Distribute stones from a pit and return the number distributed.
     *
     * @param startPit The starting pit for distribution.
     * @return The number of stones distributed.
     */
    abstract int distributeStones(int startPit);

    /**
     * Capture stones from the opponent's pit and return the number captured.
     *
     * @param stoppingPoint The stopping point for capturing stones.
     * @return The number of stones captured.
     */
    abstract int captureStones(int stoppingPoint);

    /**
     * Register two players and set their stores on the board.
     *
     * @param one The first player.
     * @param two The second player.
     */
    public void registerPlayers(Player one, Player two) {
        // this method can be implemented in the abstract class.

        /*
         * make a new store in this method, set the owner
         * then use the setStore(store,playerNum) method of the data structure
         */

        // create two new stores for these players
        Store storeOne = new Store();
        Store storeTwo = new Store();
        storeOne.setOwner(one);
        storeTwo.setOwner(two);
        gameBoard.setStore(storeOne, 1);
        gameBoard.setStore(storeTwo, 2);
        one.setStore(storeOne);
        two.setStore(storeTwo);
    }

    /**
     * Reset the game board by setting up pits and emptying stores.
     */
    public void resetBoard() {
        // should remove stones and then setup pits will put the right stones back
        for (int i = 1; i <= 12; i++) {
            gameBoard.removeStones(i);
        }
        gameBoard.setUpPits();
        gameBoard.emptyStores();
    }

    // -----------------------------------------------------------------------
    // wrappers and helpers
    // -----------------------------------------------------------------------

    public boolean isValidMove(int pitIndex, int playerNum) {
        int min = (playerNum == 1) ? 1 : 7;
        int max = (playerNum == 1) ? 6 : 12;
        return (pitIndex >= min && pitIndex <= max) && getNumStones(pitIndex) != 0;
    }

    public int removePitStones(int pitNum) {
        // System.out.println("inside remove stones (GameRules) " + pitNum);
        // the remove stones built in already indexes automatically
        return gameBoard.removeStones(pitNum);
    }

    public int getPlayerStoreCount(int p) {
        return gameBoard.getStoreCount(p);
    }

    public Store getPlayersStore(Player p) {
        return p.getStore();
    }

    public void addStoneStore(int playerNum, int amount) {
        gameBoard.addToStore(playerNum, amount);
    }

    public void addStone(int pitNum) {
        // for now simply increment by 1
        gameBoard.addStones(pitNum, 1);
    }

    public void setIterator(int startPit, boolean skipStartingPit) {
        // for ayo should skip the current pit hence third param is true
        gameBoard.setIterator(startPit, currentPlayer, skipStartingPit);
    }

    public Countable nextCountable() {
        return gameBoard.next();
    }

    public void setGameStores(Store store, Store storeT) {
        gameBoard.setStore(store, 1);
        gameBoard.setStore(storeT, 2);
    }

    // helper to check if capture can be made
    public boolean isCapture(int stopPitNum, int playerNum) {
        // no need to index
        int max = (playerNum == 1) ? 6 : 12;
        int min = (playerNum == 1) ? 1 : 7;

        return ((getNumStones(stopPitNum) == 1) && (stopPitNum <= max && stopPitNum >= min));
    }

    public int getNextPitIndex(int currentValue) {
        System.out.println("inside getNextPitIndex method: " + ((currentValue % TOTAL_PITS)) + " current pit index "
                + currentValue);

        int nextIndex = (currentValue % TOTAL_PITS);
        // if modulus results in 0 then it must be that it landed on 1
        nextIndex = (nextIndex == 0) ? 1 : nextIndex;

        return nextIndex;
    }

    @Override
    public String toString() {
        // Implement toString() method logic here.

        return "";
    }
}
