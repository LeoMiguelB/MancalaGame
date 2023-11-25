package mancala;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class Saver {

  private static final String FOLDER_NAME = "assets";

  public static void saveObject(final Serializable toSave, final String filename) throws IOException {

    Path currentDir = Paths.get(System.getProperty("user.dir"));
    Path folderPath = currentDir.resolve(FOLDER_NAME);
    boolean folderExists = Files.exists(folderPath);

    // simply add contents in the existing folder
    if (!folderExists) {
      try {
        // Create the folder
        Files.createDirectory(folderPath);
      } catch (Exception e) {
      }
    }
    
    Path filePathFull = folderPath.resolve(filename);

    try (ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(filePathFull.toFile()))) {
      System.out.println("currently saving object");
      objectOut.writeObject(toSave);

    } catch (IOException e) {
      throw e;
    }

  }

  public static Serializable loadObject(final String filename) throws IOException, ClassNotFoundException {
    Serializable gameLoaded = null;

    Path currentDir = Paths.get(System.getProperty("user.dir"));
    Path folderPath = currentDir.resolve(FOLDER_NAME);
    boolean folderExists = Files.exists(folderPath);

    if(!folderExists) {
      try {
        Files.createDirectory(folderPath);
      } catch (Exception e) {

      }
    }

    Path filePathFull = folderPath.resolve(filename);

    try (ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(filePathFull.toFile()))) {

      gameLoaded = (Serializable) objectIn.readObject();

    } catch (IOException | ClassNotFoundException e) {
      throw e;
    }
    return gameLoaded;
  }

}
