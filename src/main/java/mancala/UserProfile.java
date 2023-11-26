package mancala;

import java.io.Serializable;

public class UserProfile implements Serializable {
  private static final long serialVersionUID = 6355532757989608423L;
  private String name;
  private int gamesPlayedK;
  private int gameWonK;
  private int gamesPlayedA;
  private int gameWonA;

  public UserProfile() {
  }

  public UserProfile(final String name) {
    this(name,0,0,0,0);
  }

  public UserProfile(final String name, final int gamesPlayedK, final int gameWonK, final int gamesPlayedA, final int gameWonA) {
    this.name = name;
    this.gamesPlayedK = gamesPlayedK;
    this.gameWonK = gameWonK;
    this.gamesPlayedA = gamesPlayedA;
    this.gameWonA = gameWonA;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getGamesPlayedK() {
    return gamesPlayedK;
  }

  public void setGamesPlayedK(final int gamesPlayedK) {
    this.gamesPlayedK = gamesPlayedK;
  }

  public int getGameWonK() {
    return gameWonK;
  }

  public void setGameWonK(final int gameWonK) {
    this.gameWonK = gameWonK;
  }

  public int getGamesPlayedA() {
    return gamesPlayedA;
  }

  public void setGamesPlayedA(final int gamesPlayedA) {
    this.gamesPlayedA = gamesPlayedA;
  }

  public int getGameWonA() {
    return gameWonA;
  }

  public void setGameWonA(final int gameWonA) {
    this.gameWonA = gameWonA;
  }

  // true is ayo rules otherwise it's kalah
  public void addGamePlayed(final boolean rule){
    if(rule) {
      this.gamesPlayedA++;
    } else {
      this.gamesPlayedK++;
    }
  }

}