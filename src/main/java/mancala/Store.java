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

  /* package */ Player getOwner() {
    return owner;
  }

  @Override
  public void addStone() {
    storeHole++;
  }

  @Override
  public void addStones(final int numToAdd) {
    storeHole += numToAdd;
  }


  @Override
  public int getStoneCount() {
    return storeHole;
  }

  @Override
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