package mancala;

public class NoSuchPlayerException extends Exception {
  public NoSuchPlayerException() {
    super("There is No Such Player...");
  }

  public NoSuchPlayerException(final String message) {
    super(message);
  }
}