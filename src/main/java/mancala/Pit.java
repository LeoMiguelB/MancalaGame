package mancala;

import java.io.Serializable;

public class Pit implements Serializable, Countable{
  private static final long serialVersionUID = 8193014678745749031L;
  
  private int pitAmount;

  public Pit() {
    pitAmount = 0;
  }

  /**
   * Get the count of stones in the object.
   *
   * @return The count of stones.
   */
  public int getStoneCount() {
    return pitAmount;    
  }

  /**
   * Add one stone to the object.
   */
  public void addStone() {
    pitAmount++;
  }

  /**
   * Add a specified number of stones to the object.
   *
   * @param numToAdd The number of stones to add.
   */
  public void addStones(int numToAdd) {
    pitAmount += numToAdd;
  }

  /**
   * Remove stones from the object.
   *
   * @return The number of stones removed.
   */
  public int removeStones() {
    int temp = pitAmount;
    pitAmount = 0;
    return temp;
  }

  // simply displays the value of the pitAmount
  @Override
  public String toString() {
    return " " + getStoneCount() + " ";
  }
}