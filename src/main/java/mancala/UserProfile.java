package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable {
  private String name;
  private int gamesPlayedK;
  private int gameWonK;
  private int gamesPlayedA;
  private int gameWonA;

  public UserProfile() {
    
  }

  public UserProfile(String name) {
    this(name,0,0,0,0);
  }

  public UserProfile(String name, int gamesPlayedK, int gameWonK, int gamesPlayedA, int gameWonA) {
    this.name = name;
    this.gamesPlayedK = gamesPlayedK;
    this.gameWonK = gameWonK;
    this.gamesPlayedA = gamesPlayedA;
    this.gameWonA = gameWonA;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getGamesPlayedK() {
    return gamesPlayedK;
  }

  public void setGamesPlayedK(int gamesPlayedK) {
    this.gamesPlayedK = gamesPlayedK;
  }

  public int getGameWonK() {
    return gameWonK;
  }

  public void setGameWonK(int gameWonK) {
    this.gameWonK = gameWonK;
  }

  public int getGamesPlayedA() {
    return gamesPlayedA;
  }

  public void setGamesPlayedA(int gamesPlayedA) {
    this.gamesPlayedA = gamesPlayedA;
  }

  public int getGameWonA() {
    return gameWonA;
  }

  public void setGameWonA(int gameWonA) {
    this.gameWonA = gameWonA;
  }

  // true is ayo rules otherwise it's kalah
  public void addGamePlayed(boolean rule){
    if(rule) {
      this.gamesPlayedA++;
    } else {
      this.gamesPlayedK++;
    }
  }

}