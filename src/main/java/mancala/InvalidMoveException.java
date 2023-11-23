package mancala;

public class InvalidMoveException extends Exception {
  /* default */ InvalidMoveException() {
    super("Invalid Move!");
  }

  public InvalidMoveException(final String message) {
    super(message);
  }
}