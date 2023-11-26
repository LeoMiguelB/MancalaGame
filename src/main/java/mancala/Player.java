package mancala;
import java.io.Serializable;

public class Player implements Serializable {
  private static final long serialVersionUID = 488352369362822555L;
  private Store store;
  private String name;
  private UserProfile user;

  public Player() {
  }

  public Player(final UserProfile userP) {
    name = userP.getName();
  }

  public String getName() {
    return name;
  }

  public void setName(final String playerName) {
    name = playerName;
  }

  // below are package private since they relate to the game not the player...; ui can't access these
  /* default */ Store getStore() {
    return store;
  }

  /* default */ int getStoreCount() {
    return store.getStoneCount();
  }

  /* default */ void setStore(final Store storeSet) {
    store = storeSet;
  }

  // getter for the user profile
  public UserProfile getUserProfile() {
    return user;
  }

  @Override
  public String toString() {
    return name + "-> " + getStoreCount();
  }
}