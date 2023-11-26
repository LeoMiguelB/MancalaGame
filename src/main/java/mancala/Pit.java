package mancala;

import java.io.Serializable;

public class Pit implements Serializable, Countable{
  private static final long serialVersionUID = 8193014678745749031L;
  
  private int pitAmount;

  public Pit() {
    pitAmount = 0;
  }


  @Override
  public int getStoneCount() {
    return pitAmount;    
  }

  
  @Override
  public void addStone() {
    pitAmount++;
  }

  @Override
  public void addStones(final int numToAdd) {
    pitAmount += numToAdd;
  }

  @Override
  public int removeStones() {
    final int temp = pitAmount;
    pitAmount = 0;
    return temp;
  }

  // simply displays the value of the pitAmount
  @Override
  public String toString() {
    return " " + getStoneCount() + " ";
  }
}