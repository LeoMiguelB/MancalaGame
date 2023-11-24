package mancala;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class SaverTest {

  @Disabled
  @Test
  public void testSaveObject() throws IOException{
    // should be able to save the object
    MancalaGame game = new MancalaGame();
    // simple state where the players are set...
    game.setPlayers(new Player(new UserProfile("leo", 0, 0, 0, 0)), new Player(new UserProfile("leoThe2nd", 0, 0, 0, 0)));
    Saver.saveObject(game, "someGame.serialized");
    // check fi the file is there
    File f = new File("./assets/someGame.serialized");
    assertEquals(true, f.exists());
  }

}
