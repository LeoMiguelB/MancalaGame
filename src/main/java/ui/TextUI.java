package ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import mancala.AyoRules;
import mancala.GameNotOverException;
import mancala.InvalidMoveException;
import mancala.KalahRules;
import mancala.MancalaGame;
import mancala.NoSuchPlayerException;
import mancala.PitNotFoundException;
import mancala.Saver;
import mancala.UserProfile;
import mancala.Player;
import mancala.GameRules;

public class TextUI extends JFrame {
  private JPanel mancalaContainer;
  private JPanel mainMenuContainer;
  private JPanel userSelectorContainer;
  private JPanel sceneContainer;
  private JLabel storeOne;
  private JLabel storeTwo;
  private JLabel currentPlayer;
  private JPanel p1ProfileContainer;
  private JPanel p2ProfileContainer;
  private JButton p1Selector;
  private JButton p2Selector;
  private CardLayout cardContainer;
  private PositionAwareButton[][] pitButtons;
  private JMenuBar menuBar;
  private MancalaGame game;
  private UserProfile p1 = null;
  private UserProfile p2 = null;
  private JLabel userInfoP1;
  private JLabel userInfoP2;

  // helper variable for determining which selector is in use
  // 1 is for player 1 and 2 is for player 2
  private int currSelector = 0;

  // toggle between kalah rules and Ayo rules
  // should default to kalah
  private boolean toggleRules = false;

  /*
   * CONSTANTS
   */
  private static final String PROFILE_EXTENSION = ".profile";
  private static final String MANCALA_EXTENSION = ".mancala";
  private static final String PLAYER_INDICATOR = "Player turn: ";
  private static final int GAME_HEIGHT = 2;
  private static final int GAME_WIDTH = 6;


  public TextUI() {
    super();
    initializeGame();
    setupUI();
  }

  private void initializeGame() {
    setLayout(new BorderLayout());
    createComponents();
    createContainers();
  }

  public void switchPanel(String panelName) {
    cardContainer.show(sceneContainer, panelName);
  }

  private void setupUI() {
    setupMainMenu();
    setupMenuBar();
    setJMenuBar(menuBar);
    pack();
  }

  private void createPlayers() {
    Player playerOne = new Player(p1);
    Player playerTwo = new Player(p2);
    game = new MancalaGame();
    // ayo rules is one and kalah rules is two
    // this reduces the coupling between classes
    game.changeRules((toggleRules) ? 1 : 2);
    game.setPlayers(playerOne, playerTwo);
  }

  private void createContainers() {
    mainMenuContainer = new JPanel();
    cardContainer = new CardLayout();
    userSelectorContainer = new JPanel();
    sceneContainer = new JPanel(cardContainer);
    sceneContainer.add(mainMenuContainer, "MainMenu");
    sceneContainer.add(userSelectorContainer, "UserSelector");
    sceneContainer.add(makeCreateLoadPage(), "CreateLoad");
    sceneContainer.add(createProfilePage(), "CreateProfile");


    // default should show main menu
        
    cardContainer.show(sceneContainer, "MainMenu");
    add(sceneContainer, BorderLayout.CENTER);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private void createComponents() {
    p1Selector = new JButton();
    p2Selector = new JButton();
  }

  private void createMancalaGame() {
    // initially must increment games played for
    // players
    addGamesPlayed();
    currentPlayer = new JLabel(PLAYER_INDICATOR + game.getCurrentPlayerName());
    createStores();
    mancalaContainer = new JPanel();
    mancalaContainer.setLayout(new BorderLayout());
    mancalaContainer.add(createUserCard(p1, userInfoP1), BorderLayout.EAST);
    mancalaContainer.add(createUserCard(p2, userInfoP1), BorderLayout.WEST);
    mancalaContainer.add(createMancala(), BorderLayout.CENTER);
    sceneContainer.add(mancalaContainer, "MancalaGame");
    pack();
  }

  private JPanel createMancala() {
    JPanel mancala = new JPanel(new BorderLayout());
    mancala.add(createMancalaBoard(), BorderLayout.CENTER);
    mancala.add(createPlayerIndicator(currentPlayer), BorderLayout.NORTH);
    return mancala;
  }

  private JPanel createUserCard(UserProfile profile, JLabel user) {
    JPanel userProfile = new JPanel();
    userProfile.setLayout(new BoxLayout(userProfile, BoxLayout.Y_AXIS));

    String content = "<html>" + "Name:" + profile.getName() + "<br/>" + "Game Won (Kalah):" + profile.getGameWonK() + "<br/>" + "Game Won (Ayo):" + profile.getGameWonA() + "<br/>" +  "Games Played (Kalah):" + profile.getGamesPlayedK() + "<br/>" + "Game Played (Ayo):" + profile.getGamesPlayedA() + "<br/>" + "</html>";

    user = new JLabel(content);

    userProfile.add(user);

    return userProfile;
  }

  private void createStores() {
    try {
      storeOne = new JLabel(Integer.toString(game.getStoreCount(game.getPlayerOne())));
      storeTwo = new JLabel(Integer.toString(game.getStoreCount(game.getPlayerTwo())));
    } catch (NoSuchPlayerException e) {
      storeOne = new JLabel("0");
      storeTwo = new JLabel("0");
      System.out.println("Error retrieving store count: " + e.getMessage());
    }
  }

  private JButton createResetButton() {
    JButton resetBtn = new JButton("reset game");
    resetBtn.addActionListener(e -> {
      addGamesPlayed();
      newGame(); 
    });
    return resetBtn;
  }

  private JPanel createMancalaBoard() {
    JPanel mancalaBoard = new JPanel(new BorderLayout());
    mancalaBoard.add(createMancalaGrid(), BorderLayout.CENTER);
    mancalaBoard.add(createPlayerStores(storeOne), BorderLayout.EAST);
    mancalaBoard.add(createPlayerStores(storeTwo), BorderLayout.WEST);
    mancalaBoard.add(createResetButton(), BorderLayout.NORTH);
    return mancalaBoard;
  }

  private JPanel createPlayerStores(JLabel storeLabel) {
    JPanel storePanel = new JPanel(new BorderLayout());
    storePanel.setBackground(Color.LIGHT_GRAY);
    storePanel.add(storeLabel, BorderLayout.CENTER);
    return storePanel;
  }

  private JPanel createPlayerIndicator(JLabel indicator) {
    JPanel playerIndicator = new JPanel();
    playerIndicator.add(indicator);
    return playerIndicator;
  }


  private void setupMainMenu() {
    mainMenuContainer.setLayout(new BorderLayout());
    mainMenuContainer.add(makeMainMenuOptions(), BorderLayout.CENTER);
  }

  private void setupMenuBar() {
    menuBar = new JMenuBar();
    JMenu dropDown = new JMenu("Options");
    dropDown.add(createMenuItem("Load", (e) -> loadData(MANCALA_EXTENSION)));
    dropDown.add(createMenuItem("Save",(e) -> saveData(MANCALA_EXTENSION)));
    dropDown.add(createMenuItem("Quit", (e) -> quitGame()));
    menuBar.add(dropDown);
    // since at main menu don't want the jmenu bar showing
    // should set visibility to true once it start the game
    menuBar.setVisible(false);
    setJMenuBar(menuBar);
  }

  private JMenuItem createMenuItem(String label, ActionListener listener) {
    JMenuItem item = new JMenuItem(label);
    item.addActionListener(listener);
    return item;
  }

  public void setupMancalGameContainer() {
    mancalaContainer.setLayout(new BorderLayout());
    mancalaContainer.add(createMancalaGrid(), BorderLayout.CENTER);
    mancalaContainer.add(makePlayerStores(storeOne), BorderLayout.EAST);
    mancalaContainer.add(makePlayerStores(storeTwo), BorderLayout.WEST);
    mancalaContainer.add(makePlayerIndicator(currentPlayer), BorderLayout.NORTH);
  }

  public JPanel makePlayerIndicator(JLabel indicator) {
    JPanel playerIndicator = new JPanel();
    playerIndicator.add(indicator);
    return playerIndicator;
  }

  public void setupMainMenuContainer() {
    mainMenuContainer.setLayout(new BorderLayout());
    mainMenuContainer.add(makeMainMenuOptions(), BorderLayout.CENTER);
  }

  private JPanel makeMainMenuOptions() {
    JPanel options = new JPanel();
    options.setLayout(new BoxLayout(options, BoxLayout.PAGE_AXIS));
    
    options.add(makeStartGameButton());
    // these will indicate whether the players have been set
    options.add(createPlayerBtn("P1: ", p1, p1Selector, 1));
    options.add(createPlayerBtn("P2: ", p2, p2Selector, 2));
    options.add(createToggleRules());

    
    return options;
  }

  private JButton createToggleRules() {
    JButton toggleRulesBtn = new JButton("Toggle Rules: Kalah");
    toggleRulesBtn.addActionListener(e -> {
      toggleRules = !toggleRules;
      toggleRulesBtn.setText("Toggle Rules: " + ((toggleRules) ? "Ayo" : "Kalah"));
    });

    return toggleRulesBtn;
  } 

  private JButton makeStartGameButton() {
    JButton startGame = new JButton("Start Game");
    startGame.addActionListener(e -> {
      // neither of the profiles should be null
      if(p1 == null || p2 == null) {
        JOptionPane.showMessageDialog(startGame, "Should set both players!");
      } else {
        // initiate the creationg of players
        createPlayers();
        createMancalaGame();
        // should now create the Mancala Game 
        menuBar.setVisible(true);

        switchPanel("MancalaGame");
      }
    });
    return startGame;
  }

  private JButton makeLoadProfileButton() {
    JButton loadProfile = new JButton("Load Profile");
    loadProfile.addActionListener(e -> loadData(PROFILE_EXTENSION));
    return loadProfile;
  }



  private JPanel makePlayerStores(JLabel storeLabel) {
    JPanel storePanel = new JPanel(new BorderLayout());
    storePanel.setBackground(Color.LIGHT_GRAY);
    storePanel.add(storeLabel, BorderLayout.CENTER);
    return storePanel;
  }


  private JPanel createMancalaGrid() {
    JPanel panel = new JPanel();
    pitButtons = new PositionAwareButton[GAME_HEIGHT][GAME_WIDTH];
    panel.setLayout(new GridLayout(GAME_HEIGHT, GAME_WIDTH));

    for (int x = (GAME_HEIGHT - 1); x >= 0; x--) {
      for (int y = 0; y < GAME_WIDTH; y++) {
        pitButtons[x][y] = new PositionAwareButton();
        pitButtons[x][y].setAcross(x + 1);
        pitButtons[x][y].setDown(y + 1);
        // this effectively follows the perpective of mancala game 1 through 6 at the
        // bottom and 12 to 7 at the top
        int pitNum = (x > 0) ? ((GAME_WIDTH * GAME_HEIGHT) - y) : (x * GAME_WIDTH + y + 1);
        pitButtons[x][y].addActionListener(e -> {
          move(pitNum);
          updateView();
        });
        try {
          String pitValue = Integer.toString(game.getNumStones(pitNum));
          pitButtons[x][y].setText(pitValue);
        } catch (PitNotFoundException e) {
          JOptionPane.showMessageDialog(panel, "Something went wrong with setting up pits!");
        }

        panel.add(pitButtons[x][y]);
      }
    }

    return panel;
  }

  private void newGame() {
    game.startNewGame();
    updateView();
  }

  private void move(int startPit) {
    try {
      game.move(startPit);
      // check to if the game is over
      try {
        Player winner = game.getWinner();
        updateView();

        JOptionPane.showMessageDialog(mancalaContainer, "Congrats! " + winner.getName() + " you are the winner");

        playAgainModal();
        
        // now update the winner stats
        if (winner.getName().equals(p1.getName())) {
          if ((toggleRules)) {
            p1.setGameWonA(p1.getGameWonA() + 1);
          } else {
            p1.setGameWonK(p1.getGameWonK() + 1);
          }
        } else if (winner.getName().equals(p2.getName())) {
          if ((toggleRules)) {
            p2.setGameWonA(p2.getGameWonA() + 1);
          } else {
            p2.setGameWonK(p2.getGameWonK() + 1);
          }
        }
      } catch (GameNotOverException e) {
        // should do not nothing if there is no winner
      } catch (NoSuchPlayerException e) {

      }

      updateView();
    
    } catch (InvalidMoveException e) {
      JOptionPane.showMessageDialog(rootPane, e.getMessage());
    } 
  }

  private void playAgainModal() {
    String[] options = {
      "Return to Menu",
      "Play Again"
    };
    int choice = JOptionPane.showOptionDialog(
      null, 
      "Choose", 
      "Menu or Play Again", 
      JOptionPane.YES_NO_OPTION, 
      JOptionPane.QUESTION_MESSAGE,
      null, 
      options, 
      options[0]);

    if(choice == JOptionPane.YES_OPTION) {
      addGamesPlayed();
      saveProfileModal();
      quitGame();
    } else if (choice == JOptionPane.NO_OPTION) {
      newGame();
    }
  }

  private void saveProfileModal() {
    String[] options = {
      "Save Profile 1",
      "Save Profile 2",
      "Save Both"
    };
    System.out.println("inside of save Profile modal");
    int choice = JOptionPane.showOptionDialog(rootPane, "Select an option to save profiles:", "Profile Saving Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

    try {
          if(choice == 0) {
            Saver.saveObject(p1, PROFILE_EXTENSION);
          } else if (choice == 1) {
            Saver.saveObject(p2, PROFILE_EXTENSION);
          } else if (choice == 2) {
            Saver.saveObject(p1, PROFILE_EXTENSION);
            Saver.saveObject(p2, PROFILE_EXTENSION);
          }
    } catch (IOException e) {
      JOptionPane.showMessageDialog(rootPane, e.getMessage());
    }
  }

  private void addGamesPlayed() {
    p1.addGamePlayed(toggleRules);
    p2.addGamePlayed(toggleRules);
  }

  private void quitGame() {
    // should also effectively reset board
    game.startNewGame();

    menuBar.setVisible(false);
    // quit would return to the main menu panel
    switchPanel("MainMenu");
    
  }

  private void saveData(String fileExt) {
    String fileName = JOptionPane.showInputDialog(mancalaContainer, "filename: ", "Choose File Name",
        JOptionPane.PLAIN_MESSAGE);

    String fileNameWithExt = fileName + fileExt;
    try {
      Saver.saveObject(game, fileNameWithExt);
    } catch (IOException e){
      JOptionPane.showMessageDialog(rootPane, e.getMessage());
    }
  }

  private void loadData(String fileExt) {
    String fileName = fileLoader(fileExt);

    try {
      if(fileExt.equals(PROFILE_EXTENSION)) {
        if(currSelector == 1) {
          p1 = (UserProfile) Saver.loadObject(fileName);
        } else if (currSelector == 2) {
          p2 = (UserProfile) Saver.loadObject(fileName);
        }
        updatePlayerBtns();
        updatePlayerInfo();
      } else if (fileExt.equals(MANCALA_EXTENSION)) {
        game = (MancalaGame) Saver.loadObject(fileName);
      }
    } catch (IOException | ClassNotFoundException e) {
      JOptionPane.showMessageDialog(rootPane, e.getMessage());
    }

    updateView();
  }

  private void setGame(MancalaGame gameToSet) {
    game = gameToSet;
  }

  private String fileLoader(String fileExt) {
    JFileChooser chooser = new JFileChooser();
    int validation = chooser.showOpenDialog(mancalaContainer);
    if (validation == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile().getName();
    }

    return "";
  }
  
  /*/
  **************************************************************
  ------------------updates UI---------------
  **************************************************************
  */

  protected void updateView() {
    updateGrid();
    updateStores();
  }

  private void updateGrid() {
    for (int x = (GAME_HEIGHT - 1); x >= 0; x--) {
      for (int y = 0; y < GAME_WIDTH; y++) {
        int pitNum = (x > 0) ? ((GAME_WIDTH * GAME_HEIGHT) - y) : (x * GAME_WIDTH + y + 1);

        try {
          pitButtons[x][y].setText(Integer.toString(game.getNumStones(pitNum)));
        } catch (PitNotFoundException e) {

        }

      }
    }
    // simply update the text
    currentPlayer.setText(PLAYER_INDICATOR + game.getCurrentPlayerName());
  }

  private void updateStores() {
    // now update the stores...
    try {
      storeOne.setText(Integer.toString(game.getStoreCount(game.getPlayerOne())));
      storeTwo.setText(Integer.toString(game.getStoreCount(game.getPlayerTwo())));
    } catch (NoSuchPlayerException e) {
      System.out.println(e.getMessage());
    }
  }

  private void updatePlayerBtns() {
    p1Selector.setText("P1: " + ((p1 == null) ? "not set" : p1.getName()));
    p2Selector.setText("P2: " + ((p2 == null) ? "not set" : p2.getName()));
  }

  private void updatePlayerInfo() {

    String content1 = "<html>" + "Name:" + p1.getName() + "<br/>" + "Game Won (Kalah):" + p1.getGameWonK() + "<br/>" + "Game Won (Ayo):" + p1.getGameWonA() + "<br/>" +  "Games Played (Kalah):" + p1.getGamesPlayedK() + "<br/>" + "Game Played (Ayo):" + p1.getGamesPlayedA() + "<br/>" + "</html>";
    String content2 = "<html>" + "Name:" + p2.getName() + "<br/>" + "Game Won (Kalah):" + p2.getGameWonK() + "<br/>" + "Game Won (Ayo):" + p2.getGameWonA() + "<br/>" +  "Games Played (Kalah):" + p2.getGamesPlayedK() + "<br/>" + "Game Played (Ayo):" + p2.getGamesPlayedA() + "<br/>" + "</html>";

    userInfoP1.setText(content1);
    userInfoP2.setText(content2);
  }

  /*/
  **************************************************************
  ------------------UI that deals with the profile---------------
  **************************************************************
  */

  private JButton createPlayerBtn(String player, UserProfile uProf, JButton selector, int playerNum) {
    // player is either P1:  or P2: 
    String pName = (uProf == null) ? "not set" : uProf.getName();
 
    selector.setText(player + pName);

    selector.addActionListener(e -> {
      // if((uProf == null)) {
      //   switchPanel("CreateLoad");
      // }
      switchPanel("CreateLoad");

      // update the currSelector...
      currSelector = playerNum;
    });

    return selector;
  } 

  private JPanel makeCreateLoadPage() {
    JPanel page = new JPanel(new BorderLayout());
    page.add(makeCreateProfileBtn(), BorderLayout.NORTH);
    page.add(makeLoadProfileBtn(), BorderLayout.SOUTH);
    return page;
  }

  private JButton makeCreateProfileBtn() {
    JButton profileBtn = new JButton("create");
    profileBtn.addActionListener(e -> {
      switchPanel("CreateProfile");
    });
    return profileBtn;
  } 

  private JPanel createProfilePage() {
    JPanel createProfile = new JPanel(new BorderLayout());
    JTextField nameInput = new JTextField(1);
    createProfile.add(nameInput, BorderLayout.NORTH);
    createProfile.add(makeSubmitBtn(nameInput), BorderLayout.SOUTH);
    return createProfile;
  }

  private JButton makeLoadProfileBtn() {
    JButton load =  new JButton("Load Profile");
    load.addActionListener(e -> {
      loadData(PROFILE_EXTENSION);
      switchPanel("MainMenu");
    });
    return load;
  }

  private JButton makeSubmitBtn(JTextField nameInput) {
    JButton submit = new JButton("submit");
    submit.addActionListener(e -> {
      if (nameInput.getText().isEmpty()) {
        JOptionPane.showMessageDialog(submit, "name can't be empty");
      }
      else if ((currSelector == 2) && (p1 != null) && (p1.getName().equals(nameInput.getText()))) 
      {
        // names must b unqiue
        JOptionPane.showMessageDialog(submit, "can't have the same name as player 1");
        
      }
      else if((currSelector == 1) && (p2 != null) && (p2.getName().equals(nameInput.getText())))
      {
        JOptionPane.showMessageDialog(submit, "can't have the same name as player 2");
      }
      else {
        // now instantiate the UserProfi
        if(currSelector == 1) {
          p1 = new UserProfile(nameInput.getText());
        } else if (currSelector == 2) {
          p2 = new UserProfile(nameInput.getText());
        }
        //reset currSelector
        currSelector = 0;
        // reset the text field
        nameInput.setText("");
        updatePlayerBtns();
        switchPanel("MainMenu");
      }
    });

    return submit;
  }


    /*/
  **************************************************************
  ------------------the main for this game----------------------
  **************************************************************
  */

  public static void main(String[] args) {
    TextUI mUI = new TextUI();
    mUI.setVisible(true);
  }
}