package mancala;

import java.io.Serializable;

public class Store implements Serializable,Countable{
  private static final long serialVersionUID = 5140630299911343199L;
  private Player owner;
  private int storeHole;

  public Store() {
    storeHole = 0;
  }

  // methods below are package private; text ui should not have access to these
  // no need for protected; no subclasses being created or used
  /* default */ void setOwner(final Player player) {
    owner = player;
  }

  /* default */ Player getOwner() {
    return owner;
  }

  public void addStone() {
    storeHole++;
  }

  public void addStones(int numToAdd) {
    storeHole+= numToAdd;
  }

  /**
   * Get the count of stones in the object.
   *
   * @return The count of stones.
   */
  public int getStoneCount() {
    return storeHole;
  }

  public int removeStones() {
    final int saveVal = storeHole;
    storeHole = 0;
    return saveVal;
  }


  // simply the value of the store
  @Override
  public String toString() {
    return " " + getStoneCount() + " ";
  }
}