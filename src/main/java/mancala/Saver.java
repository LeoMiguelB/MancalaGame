package mancala;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
  side note...
  example for generating UID's:
 serialver -classpath build/classes/java/main/mancala  mancala.Man
calaGame
 */

public class Saver {

  public static void saveObject(final Serializable toSave, final String filename) {
    String filePathFull = "./assets/" + filename;

    try (ObjectOutputStream objectOut =  new ObjectOutputStream(new FileOutputStream(filePathFull))) {

      objectOut.writeObject(toSave);
    
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static Serializable loadObject(final String filename) {
    MancalaGame gameLoaded = null;

    String filePathFull = "./assets/" + filename;
    
    try(ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(filePathFull))) {

      gameLoaded = (MancalaGame) objectIn.readObject();

    } catch (IOException | ClassNotFoundException e) {

      System.out.println(e);

    }
    return gameLoaded;

  }

}
