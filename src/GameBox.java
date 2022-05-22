import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.PrintWriter;
import java.util.Scanner;

public class GameBox extends Application {
    // GameBox class is the main class of our project. It has 2 scenes, one is menu and the other is the game scene.

    // We declared a window here to change the scene in any method.
    private Stage window;
    private Label levelLabel, restartLabel, currentScoreLabel, highScoreLabel, clickInfo, nextLevel;
    private Button saveGameButton, mainMenuButton,newGameButton, loadGameButton, musicButton;
    private Scene gameScene, menuScene;
    private GridPane gameArea;

    public static void main(String[] args) {
        launch(args);
    }

    public void prepareTop(HBox top) {
        levelLabel = new Label("Level #1");
        levelLabel.setPadding(new Insets(0,100,0,50));
        currentScoreLabel = new Label("0");
        currentScoreLabel.setPadding(new Insets(0,100,0,0));
        highScoreLabel = new Label("High Score: 0");
        restartLabel = new Label("Restart");
        restartLabel.setPadding(new Insets(0,100,0,0));
        top.getChildren().addAll(levelLabel, restartLabel, currentScoreLabel, highScoreLabel);
    }

    public void prepareGameArea(GridPane gameArea) {
        gameArea.setStyle("-fx-background-color: #7f7f7f");
        gameArea.setHgap(3);
        gameArea.setVgap(3);
        gameArea.setPadding(new Insets(2, 2, 2, 120));
    }

    public void prepareBottoms(HBox bottom1, HBox bottom2) {
        clickInfo = new Label("--Text--");
        nextLevel = new Label("Next Level>>");
        nextLevel.setPadding(new Insets(0, 0, 0, 200));
        bottom1.getChildren().addAll(clickInfo, nextLevel);
        nextLevel.setVisible(false);

        saveGameButton = new Button("Save Game");
        mainMenuButton = new Button("Main Menu");
        bottom2.getChildren().addAll(saveGameButton, mainMenuButton);
        bottom2.setAlignment(Pos.CENTER);
    }

    public void prepareMainMenu(VBox mainMenu) {
        newGameButton = new Button("New Game");
        loadGameButton = new Button("Load Game");
        musicButton = new Button("Music On/Off");
        mainMenu.getChildren().addAll(newGameButton, loadGameButton,musicButton);
        mainMenu.setAlignment(Pos.CENTER);
    }

    public void prepareButtonActions(Box[][] boxList) {
        newGameButton.setOnAction(e -> {
            // Changes the scene first
            window.setScene(gameScene);
            try {
                // New Game button prepare the game screen for level 1 and reorganize the nextLevel label.
                prepareGameScreen(boxList, 1, gameArea, levelLabel, currentScoreLabel, highScoreLabel, clickInfo);
                nextLevel.setText("Next Level >>");
                nextLevel.setVisible(false);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

        loadGameButton.setOnAction(e -> {
            try {
                // Load Game button gets the last saved level from the save.txt file if it has, then prepare the game screen for the level saved.
                File save = new File("save.txt");
                Scanner levelScanner = new Scanner(save);
                if (levelScanner.hasNext()) {
                    int level = levelScanner.nextInt();
                    // Changes the scene if the file has a saved level information.
                    window.setScene(gameScene);
                    prepareGameScreen(boxList, level, gameArea, levelLabel, currentScoreLabel, highScoreLabel, clickInfo);
                    nextLevel.setText("Next Level >>");
                    nextLevel.setVisible(false);
                }
                levelScanner.close();
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }

        });

        // Music button to on/off the background music.
        musicButton.setOnAction(e -> {
            if(background.isAutoPlay()){
                background.pause();
                background.setAutoPlay(false);
            }
            else if(!background.isAutoPlay()) {
                background.setAutoPlay(true);
            }
        });



        // GameScene Click Events and Button Actions

        // Restart Label gets the level value from the label and re-prepare the same level and reorganize the nextLevel label.
        restartLabel.setOnMouseClicked(e -> {
            int level = Character.getNumericValue(levelLabel.getText().charAt(7));
            try {
                prepareGameScreen(boxList, level, gameArea, levelLabel, currentScoreLabel, highScoreLabel, clickInfo);
                nextLevel.setVisible(false);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

        // Save game button saves the current level info from the levelLabel and store it in save.txt file
        saveGameButton.setOnAction(e -> {
            File save = new File("save.txt");
            try (PrintWriter saveWriter = new PrintWriter(save)) {
                int level = Character.getNumericValue(levelLabel.getText().charAt(7));
                saveWriter.println(level);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        });

        // Main Menu button basically sets the scene to the menuScene
        mainMenuButton.setOnAction( e -> window.setScene(menuScene));
    }

    public void boxClickEvent(Box[][] boxList) {
        gameArea.setOnMouseClicked(e -> {
            // Gets the current score
            int scoreBefore = Integer.parseInt(currentScoreLabel.getText());

            // Calculates the destroys and the score according to the count of box destroyed.
            int scoreNow = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    // After a click made in the gameArea, every Clickable boxes will be checked
                    // When a click made a box, its isClicked value will be true. This way we can find the box that clicked.
                    if (boxList[j][i] instanceof Clickable && ((Clickable) boxList[j][i]).isClicked()) {
                        clickInfo.setText("Box: " + boxList[j][i].getCurrentX() + "-" + boxList[j][i].getCurrentY());
                        // Calculates the count of the boxes destroyed
                        int count = calculateCount(boxList, i, j, clickInfo);
                        // Changes the isClicked value of the clicked box to the false
                        ((Clickable) boxList[j][i]).setClicked(false);
                        // Calculates the new score
                        scoreNow = getScoreNow(currentScoreLabel, scoreBefore, scoreNow, count, clickInfo);
                    }
                }
            }
            // Whenever a click made, we need to check the gameArea to find is the game finished
            if (isDone(boxList)) {
                // If it is then

                int level = Character.getNumericValue(levelLabel.getText().charAt(7));
                try {
                    // We update the highscore label first
                    updateHighScore(currentScoreLabel, highScoreLabel, levelLabel);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                if (level == 5) {
                    // Since there is no more level we change its text to Main Menu
                    nextLevel.setText("Main Menu >>");
                }
                nextLevel.setVisible(true);

                // If the level is done, the nextLevel label appears and if we click it we go to the next level if there is a next level or to the main menu.
                nextLevel.setOnMouseClicked(event -> {
                    try {
                        if (level == 5) {
                            window.setScene(menuScene);
                        }
                        else {
                            prepareGameScreen(boxList, level + 1, gameArea, levelLabel, currentScoreLabel, highScoreLabel, clickInfo);
                        }
                        nextLevel.setVisible(false);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {
        // Width and Height values of our gameScene
        int width = 620, height = 420;

        // This is the boxList that will shown in gameArea
        Box[][] boxList = new Box[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                boxList[i][j] = new WallBox(j, i);
            }
        }

        window = primaryStage;

        // Our gameScreen is a BorderPane to set it's top, center and bottom places with another panes.
        BorderPane mainScreen = new BorderPane();


        // Top pane is a HBox, it has 4 Labels. Level, Restart, Score and HighScore labels.
        HBox top = new HBox();
        prepareTop(top);

        // Center is a GridPane, It has the boxes in the boxList
        gameArea = new GridPane();
        prepareGameArea(gameArea);


        // Bottom pane is VBox containing 2 HBox
        // First HBox contains 2 label, click info and next level labels.
        HBox bottom1 = new HBox();
        // Second HBox contains 2 buttons, save game and main menu button
        HBox bottom2 = new HBox(50);

        prepareBottoms(bottom1, bottom2);

        // Here is the our bottom pane which is VBox
        VBox bottom = new VBox();
        bottom.getChildren().addAll(bottom1, bottom2);

        // Setting the panes
        mainScreen.setBottom(bottom);
        mainScreen.setTop(top);
        mainScreen.setCenter(gameArea);

        // Setting the scene
        gameScene = new Scene(mainScreen, width, height);

        // Main Menu is created with a VBox, it has 3 buttons. 
        VBox mainMenu = new VBox(15);
        prepareMainMenu(mainMenu);

        // Setting the scene
        menuScene = new Scene(mainMenu, 200, 200);
        // Main Menu Buttons Actions

        prepareButtonActions(boxList);

        // Box Click event

        boxClickEvent(boxList);

        // Setting the first scene as a menuScene
        window.setScene(menuScene);
        window.setTitle("Game BOX");
        window.show();
        //music();
    }

    // It prepares everything in the gameArea according to the level value.
    public void prepareGameScreen(Box[][] boxList, int level, GridPane gameArea, Label levelLabel, Label currentScoreLabel, Label highScoreLabel, Label clickInfo) throws FileNotFoundException {
        // We delete every box on the gameArea so we can add the new ones
        clearGameArea(boxList, gameArea);


        // Choosing the file according to the level
        File file = null;
        switch (level) {
            case 1:
                file = new File("levels/level1.txt");
                break;
            case 2:
                file = new File("levels/level2.txt");
                break;
            case 3:
                file = new File("levels/level3.txt");
                break;
            case 4:
                file = new File("levels/level4.txt");
                break;
            default:
                file = new File("levels/level5.txt");
                break;
        }

        // Updating boxList
        try {
            // First we put WallBox to every index
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    boxList[i][j] = new WallBox(j, i);
                }
            }

            // Then we scan the level file. Since the input will be "WoodBox,1,5" we need to convert it to more useable form.
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {

                String s1 = fileScanner.nextLine();
                // We split it first with , sign
                String[] s1List = s1.split(",");

                String type = s1List[0];
                int rowIndex = Integer.parseInt(s1List[1]);
                int columnIndex = Integer.parseInt(s1List[2]);

                // Then we put the other boxes
                switch (type) {
                    case "Empty":
                        boxList[rowIndex][columnIndex] = new EmptyBox(columnIndex, rowIndex);
                        break;
                    case "Mirror":
                        boxList[rowIndex][columnIndex] = new MirrorBox(columnIndex, rowIndex);
                        break;
                    default:
                        boxList[rowIndex][columnIndex] = new WoodBox(columnIndex, rowIndex);
                        break;
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File is missing");
        }

        // Then we add the every item in boxList to the gameArea
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                gameArea.add(boxList[i][j], j, i);
            }
        }

        // Prepare the labels
        levelLabel.setText("Level #" + level);
        currentScoreLabel.setText("0");
        clickInfo.setText("--Text--");
        // Since this method also updates the highScoreLabel we call it
        getHighScoreFromFile(level, highScoreLabel);
    }


    // We calculate the count of the destoryed boxes
    public int calculateCount(Box[][] boxList, int i, int j, Label clickInfo) {
        // Whenever a click made on a box this method will be executed, so we call the clickSound method here
        // clickSound();

        // Instead of using if else statement to check if the top, bottom, left and right indexes out of the bound 
        // or using throw decleration on the method, we used to try-catch for every case to make it easier
        int count = 1;


        // Bottom
        try {
            // We check the if the box is clickable and has durability more than 0, then execute its click method and update the clickInfo label.
            if (boxList[j-1][i] instanceof Clickable && boxList[j-1][i].getDurability() > 0) {
                count++;
                ((Clickable)boxList[j-1][i]).click();
                clickInfo.setText(clickInfo.getText() + " - Hit: " + boxList[j-1][i].getCurrentX() + "," + boxList[j-1][i].getCurrentY());
            }
        } catch (IndexOutOfBoundsException ignored){}

        // Top
        try {
            if (boxList[j+1][i] instanceof Clickable && boxList[j+1][i].getDurability() > 0) {
                count++;
                ((Clickable)boxList[j+1][i]).click();
                clickInfo.setText(clickInfo.getText() + " - Hit: " + boxList[j+1][i].getCurrentX() + "," + boxList[j+1][i].getCurrentY());
            }
        } catch (IndexOutOfBoundsException ignored){}

        // Left 
        try {
            if (boxList[j][i-1] instanceof Clickable && (boxList[j][i-1].getDurability() > 0)) {
                count++;
                ((Clickable)boxList[j][i-1]).click();
                clickInfo.setText(clickInfo.getText() + " - Hit: " + boxList[j][i-1].getCurrentX() + "," + boxList[j][i-1].getCurrentY());
            }
        } catch (IndexOutOfBoundsException ignored){}

        // Right
        try {
            if (boxList[j][i+1] instanceof Clickable && boxList[j][i+1].getDurability() > 0) {
                count++;
                ((Clickable)boxList[j][i+1]).click();
                clickInfo.setText(clickInfo.getText() + " - Hit: " + boxList[j][i+1].getCurrentX() + "," + boxList[j][i+1].getCurrentY());
            }
        } catch (IndexOutOfBoundsException ignored){}

        return count;
    }

    // It calculates the score will be gained according to count and updates the clickInfo and currentScoreLabel
    private int getScoreNow(Label currentScoreLabel, int scoreBefore, int scoreNow, int count, Label clickInfo) {
        switch (count) {
            case 1: {
                scoreNow = scoreBefore - 3;
                clickInfo.setText(clickInfo.getText() + " (-3 Points)");
                break;
            }
            case 2: {
                scoreNow = scoreBefore - 1;
                clickInfo.setText(clickInfo.getText() + " (-1 Points)");
                break;
            }
            case 3: {
                scoreNow = scoreBefore + 1;
                clickInfo.setText(clickInfo.getText() + " (+1 Points)");
                break;
            }
            case 4: {
                scoreNow = scoreBefore + 2;
                clickInfo.setText(clickInfo.getText() + " (+2 Points)");
                break;
            }
            default:
                scoreNow = scoreBefore + 4;
                clickInfo.setText(clickInfo.getText() + " (+4 Points)");
                break;
        }

        // Updating the current score label
        currentScoreLabel.setText(String.valueOf(scoreNow));
        return scoreNow;
    }

    // Gets high score values from the file and also updates the highScoreLabel
    public int getHighScoreFromFile(int level, Label highScoreLabel) throws FileNotFoundException {
        File highScores = new File("highscores.txt");
        Scanner scoreReader = new Scanner(highScores);
        int highScore = 0;
        // It skips every line, since the i is equal to level.
        for(int i = 1; i < 6; i++) {
            if(i == level) {
                // The highScore.txt has lines like "1 3" which means "Level: 1 High Score: 3" so we only need the 2nd value. We skip the first and get the 2nd.
                scoreReader.nextInt();
                highScore = scoreReader.nextInt();
            }
            else {
                // To skip lines since i == level
                scoreReader.nextLine();
            }
        }
        scoreReader.close();

        // Updates the highScoreLabel
        if (highScore == -99) {
            highScoreLabel.setText("High Score: 0");
        }
        else {
            highScoreLabel.setText("High Score: " + highScore);
        }


        return highScore;
    }


    // It updates the highScore.txt file
    public void updateHighScore(Label currentScoreLabel, Label highScoreLabel, Label levelLabel) throws FileNotFoundException {
        int currentScore = Integer.parseInt(currentScoreLabel.getText());
        int level = Character.getNumericValue(levelLabel.getText().charAt(7));
        int highScore = getHighScoreFromFile(level, highScoreLabel);
        
        /* 	We had some trouble in here. We could not find a proper way to update a value in a line.
            So, first we check the currentScore and highScore. If the currentScore is bigger, then the execution starts
         */
        if (currentScore > highScore) {
            // Instead of updating the highscores.txt, we created a new .txt file and copied the content to the new one except the current level's line.
            File highScoresOld = new File("highscores.txt");
            Scanner scoreScanner = new Scanner(highScoresOld);

            File highScoresNew = new File("highscores2.txt");
            try (PrintWriter highScoreEditor = new PrintWriter(highScoresNew)){
                for (int i = 1; i < 6; i++) {
                    // If the line is equal to level, we update the old high score with the new one by this
                    if (i == level) {
                        highScoreEditor.println(i + " " + currentScore);
                        // Skips this line
                        scoreScanner.nextLine();
                    }
                    else {
                        highScoreEditor.println(scoreScanner.nextLine());
                    }
                }
                scoreScanner.close();
                // Then we delete the highscores.txt
                highScoresOld.delete();
            }
            // Then we rename the highscores2.txt to highscores.txt 
            highScoresNew.renameTo(highScoresOld);

            // Since this method updates the highScoreLabel we call it
            getHighScoreFromFile(level, highScoreLabel);
        }
    }

    // It returns true if every box has 0 durability
    public boolean isDone(Box[][] boxList) {
        boolean isDone = true;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (boxList[i][j].getDurability() > 0) {
                    isDone = false;
                    break;
                }
            }
            if (!isDone) break;
        }
        return isDone;
    }

    // It removes the every item in the gameArea
    public void clearGameArea(Box[][] boxList, GridPane gameArea) {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                gameArea.getChildren().remove(boxList[j][i]);
            }
        }
    }

    // Playing music and click effect

    MediaPlayer background;
    public void music() {
        String s = "fon.mp3";
        Media h = new Media(new File(s).toURI().toString());
        background = new MediaPlayer(h);
        background.setAutoPlay(true);

    }
    MediaPlayer clickSound;
    public boolean clickSound() {
        String s = "click.mp3";
        Media h = new Media(new File(s).toURI().toString());
        clickSound = new MediaPlayer(h);
        clickSound.play();
        return false;
    }
}