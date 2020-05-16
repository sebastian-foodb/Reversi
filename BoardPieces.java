// ***************************************************************
// Name: Sebastian
// Date: April 24, 2019
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.
//
// ***************************************************************

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class BoardPieces
{
	//CONSTANTS
	//grid text labels
	private final String[] LETTER = {"A","B","C","D","E","F","G","H"};
	private final String[] NUMBER = {"1","2","3","4","5","6","7","8"};
	//shape dimensions
	private final double SQUARE_LENGTH = 50;
	private final double DISC_RADIUS = 20;
	//opponent type identifiers
	private final int OPPONENT_HUMAN = 0;
	private final int OPPONENT_AI1 = 1;
	private final int OPPONENT_AI2 = 2;
	//color indices
	private final int COLOR_BACKG = 0;
	private final int COLOR_LABEL = 1;
	private final int COLOR_BOARD = 2;
	private final int COLOR_BLACK = 3;
	private final int COLOR_WHITE = 4;
	//players in turn feed
	private final String P1 = "Player 1";
	private final String P2 = "Player 2";
	//maximum length of a high score name
	private final int MAX_NAME_LENGTH = 20;

	//classes to call
	private GridSystem gs;
	private ColorScheme cs;

	//pane for the board and discs
	private Pane reversiWindow;

	//board squares
	private Rectangle[][] boardSquare;

	//board grid labels rows and columns respectively
	private Text[][] boardLabel;

	//discs
	private Circle[][] disc;

	//scores
	private Text scoreTextBlack;
	private Text scoreTextWhite;

	//turn feed
	private ScrollPane turnFeed;
	private Text feedText;

	//buttons
	//new game
	private Button buttonNew;
	//load game
	private Button buttonLoad;
	//high scores
	private Button buttonHighScores;
	//options menu
	private Button buttonOptions;
	//back to startup after a game
	private Button backToStartup;
	//submit new high score
	private Button buttonSubmit;
	
	//dropdown menus
	//opponent selection
	private ComboBox<String> oppBox;
	//board setup selection
	private ComboBox<String> setupBox;
	//board color selections
	private ComboBox<String>[] selectColor;

	//user-defined colors (except background)
	private Color[] colorElement;
	
	//menus
	private Group menuStartup;
	private Group menuSetup;
	private Group menuOptions;
	private Group menuHighScores;
	
	//high score list elements
	//user input box for new high scores
	private TextField nameField;
	private Text congrats;
	private Label highScores;
	
	//placement of new high score
	private int placement;

	//row and column of a placed disc
	private int[] place;

	//constructor
	public BoardPieces()
	{		
		//call classes
		gs = new GridSystem();
		cs = new ColorScheme();

		//get board colors
		colorElement = new Color[]
				{
						Color.web(cs.getColors()[COLOR_BACKG]),
						Color.web(cs.getColors()[COLOR_LABEL]),
						Color.web(cs.getColors()[COLOR_BOARD]),
						Color.web(cs.getColors()[COLOR_BLACK]),
						Color.web(cs.getColors()[COLOR_WHITE])
				};

		//initialize board pane
		reversiWindow = new Pane();
		//set background color
		reversiWindow.setStyle("-fx-background-color: "+cs.getColors()[COLOR_BACKG]+";"
				+"-fx-border-color: black;"
				+"-fx-border-width: 10;");

		//initialize board as an 8*8 grid
		boardSquare = new Rectangle[gs.BOARD_LENGTH][gs.BOARD_LENGTH];

		//initialize grid labels
		boardLabel = new Text[gs.BOARD_LENGTH][2];

		//initialize discs as an array corresponding to the board grid
		disc = new Circle[gs.BOARD_LENGTH][gs.BOARD_LENGTH];

		//initialize score text
		scoreTextBlack = new Text();
		scoreTextWhite = new Text();

		//initialize turn feed
		turnFeed = new ScrollPane();
		
		//initialize and define the visual properties of the menus
		defineMenuHighScores();
		defineMenuOptions();
		defineMenuSetup();
		defineMenuStartup();
	}

	//define how the grid labels should look
	public void defineGridLabels()
	{
		//shared properties
		for(int i = 0; i < boardLabel.length; i++)
		{
			for(int j = 0; j < boardLabel[0].length; j++)
			{
				//define every border label as a text object
				boardLabel[i][j] = new Text();
				//font style
				boardLabel[i][j].setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,36));
				boardLabel[i][j].setStroke(Color.BLACK);
				boardLabel[i][j].setTextAlignment(TextAlignment.CENTER);
				//font color changes with argument
				boardLabel[i][j].setFill(colorElement[COLOR_LABEL]);

				//add to the pane
				reversiWindow.getChildren().add(boardLabel[i][j]);
			}

			//define row coordinates with numbers
			boardLabel[i][0].setText(NUMBER[i]);
			boardLabel[i][0].setX(15);
			boardLabel[i][0].setY((SQUARE_LENGTH*1.75)+(SQUARE_LENGTH*i));

			//define column coordinates with letters
			boardLabel[i][1].setText(LETTER[i]);
			boardLabel[i][1].setX((SQUARE_LENGTH*1.25)+(SQUARE_LENGTH*i));
			boardLabel[i][1].setY(40);
		}
	}

	//define how the board should look
	public void defineBoardSquares()
	{
		//shared properties
		for(int i = 0; i < boardSquare.length; i++)
		{
			for(int j = 0; j < boardSquare.length; j++)
			{
				boardSquare[i][j] = new Rectangle();
				//dimensions
				boardSquare[i][j].setHeight(SQUARE_LENGTH);
				boardSquare[i][j].setWidth(SQUARE_LENGTH);
				//stroke color
				boardSquare[i][j].setStroke(Color.BLACK);
				//position in pane
				boardSquare[i][j].setLayoutX(SQUARE_LENGTH+(SQUARE_LENGTH*i));
				boardSquare[i][j].setLayoutY(SQUARE_LENGTH+(SQUARE_LENGTH*j));
				//action on mouse click
				boardSquare[i][j].setOnMouseClicked(new placeDiscClick());
				//color changes with argument
				boardSquare[i][j].setFill(colorElement[COLOR_BOARD]);

				//add to the pane
				reversiWindow.getChildren().add(boardSquare[i][j]);
			}
		}
	}

	//define the discs
	public void defineDiscs()
	{
		//shared properties
		for(int i = 0; i < disc.length; i++)
		{
			for(int j = 0; j < disc.length; j++)
			{
				//define every disc as a circle object
				disc[i][j] = new Circle();
				//dimensions
				disc[i][j].setRadius(DISC_RADIUS);
				//stroke fill
				disc[i][j].setStroke(Color.BLACK);
				//position in pane
				disc[i][j].setLayoutX((SQUARE_LENGTH*1.5)+(SQUARE_LENGTH*i));
				disc[i][j].setLayoutY((SQUARE_LENGTH*1.5)+(SQUARE_LENGTH*j));
			}
		}
	}

	//define the score text
	public void defineScores()
	{
		//black score text
		scoreTextBlack.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,72));
		scoreTextBlack.setFill(colorElement[COLOR_BLACK]);
		scoreTextBlack.setStroke(Color.BLACK);
		scoreTextBlack.setText(String.valueOf(gs.getScoreBlack()));
		scoreTextBlack.setTextAlignment(TextAlignment.CENTER);
		scoreTextBlack.setX(500);
		scoreTextBlack.setY(150);

		//white score text
		scoreTextWhite.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,72));
		scoreTextWhite.setFill(colorElement[COLOR_WHITE]);
		scoreTextWhite.setStroke(Color.BLACK);
		scoreTextWhite.setText(String.valueOf(gs.getScoreWhite()));
		scoreTextWhite.setTextAlignment(TextAlignment.CENTER);
		scoreTextWhite.setX(500);
		scoreTextWhite.setY(350);

		//add them to the pane
		reversiWindow.getChildren().addAll(scoreTextBlack,scoreTextWhite);
	}

	//define the turn feed
	public void defineTurnFeed()
	{
		//define the feed itself
		turnFeed.setLayoutX(600);
		turnFeed.setLayoutY(25);
		turnFeed.setPrefSize(270,450);

		//define the text inside of it
		feedText = new Text();
		feedText.setFont(Font.font("Courier",14));
		feedText.setWrappingWidth(245);
		turnFeed.setBackground(new Background(new BackgroundFill(colorElement[COLOR_BOARD],null,null)));

		//add text to turn feed
		turnFeed.setContent(feedText);

		//add turnFeed to the pane
		reversiWindow.getChildren().add(turnFeed);
	}
	
	//define the startup screen
	public void defineMenuStartup()
	{
		menuStartup = new Group();
		menuStartup.toFront();
		
		//background
		Rectangle bg = new Rectangle();
		bg.setFill(colorElement[COLOR_BACKG]);
		bg.setLayoutX(10);
		bg.setLayoutY(10);
		
		//new game button
		buttonNew = new Button("New Game");
		buttonNew.setTextFill(colorElement[COLOR_BLACK]);
		buttonNew.setTextAlignment(TextAlignment.CENTER);
		buttonNew.setStyle("-fx-background-color: "+cs.getColors()[COLOR_WHITE]+";");
		buttonNew.setLayoutX(10);
		buttonNew.setLayoutY(10);
		buttonNew.setOnAction(new newGame());

		//load game button
		buttonLoad = new Button("Load Game");
		buttonLoad.setTextFill(colorElement[COLOR_BLACK]);
		buttonLoad.setTextAlignment(TextAlignment.CENTER);
		buttonLoad.setStyle("-fx-background-color: "+cs.getColors()[COLOR_WHITE]+";");
		buttonLoad.setLayoutX(10);
		buttonLoad.setLayoutY(60);
		buttonLoad.setOnAction(new loadGame());
		
		//options button
		buttonOptions = new Button("Options");
		buttonOptions.setTextFill(colorElement[COLOR_BLACK]);
		buttonOptions.setTextAlignment(TextAlignment.CENTER);
		buttonOptions.setStyle("-fx-background-color: "+cs.getColors()[COLOR_WHITE]+";");
		buttonOptions.setLayoutX(10);
		buttonOptions.setLayoutY(110);
		buttonOptions.setOnAction(gameOptions ->
		{
			menuStartup.setVisible(false);
			menuOptions.setVisible(true);
		});
		
		//button to go back to the startup menu
		Button scoresToStartup = new Button("Back");
		scoresToStartup.setTextFill(Color.BLACK);
		scoresToStartup.setTextAlignment(TextAlignment.CENTER);
		scoresToStartup.setStyle("-fx-background-color: white;");
		scoresToStartup.setLayoutX(10);
		scoresToStartup.setLayoutY(500);
		scoresToStartup.setOnAction(goback ->
		{
			menuStartup.setVisible(true);
			menuHighScores.setVisible(false);
			scoresToStartup.setVisible(false);
		});
		scoresToStartup.setVisible(false);
		menuHighScores.getChildren().add(scoresToStartup);
		
		
		//high scores button
		buttonHighScores = new Button("View High Scores");
		buttonHighScores.setTextFill(colorElement[COLOR_BLACK]);
		buttonHighScores.setTextAlignment(TextAlignment.CENTER);
		buttonHighScores.setStyle("-fx-background-color: "+cs.getColors()[COLOR_WHITE]+";");
		buttonHighScores.setLayoutX(10);
		buttonHighScores.setLayoutY(160);
		buttonHighScores.setOnAction(viewHighScores ->
		{
			menuStartup.setVisible(false);
			menuHighScores.setVisible(true);
			scoresToStartup.setVisible(true);
		});
		
		//add all of the nodes to the group
		menuStartup.getChildren().addAll(buttonNew,buttonLoad,buttonOptions,buttonHighScores,bg);
		
		//add menu to the pane
		reversiWindow.getChildren().add(menuStartup);
	}
	
	//define the setup screen
	public void defineMenuSetup()
	{
		menuSetup = new Group();
		
		//opponent selection dropdown
		oppBox = new ComboBox<String>();
		oppBox.getItems().addAll("Human","Computer 1","Computer 2");
		oppBox.setValue("Human");
		oppBox.setStyle("-fx-background-color: "+cs.getColors()[COLOR_WHITE]+";");
		oppBox.setLayoutX(10);
		oppBox.setLayoutY(10);
		Text textOppBox = new Text("Opponent Type");
		textOppBox.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,18));
		textOppBox.setFill(colorElement[COLOR_LABEL]);
		textOppBox.setStroke(Color.BLACK);
		textOppBox.setStrokeWidth(0.625);
		textOppBox.setLayoutX(210);
		textOppBox.setLayoutY(30);
		
		//board setup dropdown
		setupBox = new ComboBox<String>();
		setupBox.getItems().addAll("Standard Setup","Custom Setup");
		setupBox.setValue("Standard Setup");
		setupBox.setStyle("-fx-background-color: "+cs.getColors()[COLOR_WHITE]+";");
		setupBox.setLayoutX(10);
		setupBox.setLayoutY(60);
		Text textSetupBox = new Text("Setup Type");
		textSetupBox.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,18));
		textSetupBox.setFill(colorElement[COLOR_LABEL]);
		textSetupBox.setStroke(Color.BLACK);
		textSetupBox.setStrokeWidth(0.625);
		textSetupBox.setLayoutX(210);
		textSetupBox.setLayoutY(80);
		
		//start button
		Button buttonStart = new Button("Start!");
		buttonStart.setTextFill(colorElement[COLOR_BLACK]);
		buttonStart.setTextAlignment(TextAlignment.CENTER);
		buttonStart.setStyle("-fx-background-color: "+cs.getColors()[COLOR_WHITE]+";");
		buttonStart.setLayoutX(10);
		buttonStart.setLayoutY(110);
		buttonStart.setOnAction(new gameStart());
		
		//button to go back to startup menu
		Button setupToStartup = new Button("Back");
		setupToStartup.setTextFill(Color.BLACK);
		setupToStartup.setTextAlignment(TextAlignment.CENTER);
		setupToStartup.setStyle("-fx-background-color: white;");
		setupToStartup.setLayoutX(10);
		setupToStartup.setLayoutY(500);
		setupToStartup.setOnAction(goback ->
		{
			menuSetup.setVisible(false);
			menuStartup.setVisible(true);
		});
		
		//add all of the nodes to the group
		menuSetup.getChildren().addAll(oppBox,textOppBox,setupBox,textSetupBox,buttonStart,setupToStartup);
		
		//start hidden by default
		menuSetup.setVisible(false);
		
		//add menu to the pane
		reversiWindow.getChildren().add(menuSetup);
	}
	
	//define the options menu
	public void defineMenuOptions()
	{
		menuOptions = new Group();
		
		//dropdown menus [0 background,1 labels,2 board,3 black discs,4 white discs]
		selectColor = new ComboBox[5];
		for(int i = 0; i < selectColor.length; i++)
		{
			selectColor[i] = new ComboBox<String>();
			selectColor[i].setLayoutX(20);
			selectColor[i].setLayoutY(50*(1+i));
			selectColor[i].setStyle("-fx-background-color: white;");
			
			//add each name of the colors in hexColorList to the menu
			for(int j = 0; j < cs.getHexColorList().length; j++)
			{
				selectColor[i].getItems().add(cs.getHexColorList()[j][1]);
				
				//determine the default initial value in the dropdown
				if(cs.getHexColorList()[j][0].equals(cs.getColors()[i]))
				{
					selectColor[i].setValue(cs.getHexColorList()[j][1]);
				}
			}
		}
		
		//label background color dropdown
		Text textBackg = new Text("Background");
		textBackg.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,18));
		textBackg.setFill(colorElement[COLOR_BACKG]);
		textBackg.setStroke(Color.BLACK);
		textBackg.setStrokeWidth(0.625);
		textBackg.setLayoutX(160);
		textBackg.setLayoutY(70);
		
		//label grid label color dropdown
		Text textLabel = new Text("Grid Labels");
		textLabel.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,18));
		textLabel.setFill(colorElement[COLOR_LABEL]);
		textLabel.setStroke(Color.BLACK);
		textLabel.setStrokeWidth(0.625);
		textLabel.setLayoutX(160);
		textLabel.setLayoutY(120);
		
		
		//label board square dropdown
		Text textBoard = new Text("Board");
		textBoard.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,18));
		textBoard.setFill(colorElement[COLOR_BOARD]);
		textBoard.setStroke(Color.BLACK);
		textBoard.setStrokeWidth(0.625);
		textBoard.setLayoutX(160);
		textBoard.setLayoutY(170);
		
		//label black disc dropdown
		Text textBlack = new Text("Black Discs");
		textBlack.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,18));
		textBlack.setFill(colorElement[COLOR_BLACK]);
		textBlack.setStroke(Color.BLACK);
		textBlack.setStrokeWidth(0.625);
		textBlack.setLayoutX(160);
		textBlack.setLayoutY(220);
		
		//label white disc dropdown
		Text textWhite = new Text("White Discs");
		textWhite.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,18));
		textWhite.setFill(colorElement[COLOR_WHITE]);
		textWhite.setStroke(Color.BLACK);
		textWhite.setStrokeWidth(0.625);
		textWhite.setLayoutX(160);
		textWhite.setLayoutY(270);
		
		//add the text labels to the menu
		menuOptions.getChildren().addAll(textBackg,textLabel,textBoard,textBlack,textWhite);
		
		//button to apply color changes
		Button buttonApplyColors = new Button("Apply");
		buttonApplyColors.setTextFill(Color.BLACK);
		buttonApplyColors.setTextAlignment(TextAlignment.CENTER);
		buttonApplyColors.setStyle("-fx-background-color: white;");
		buttonApplyColors.setLayoutX(10);
		buttonApplyColors.setLayoutY(500);
		buttonApplyColors.setOnAction(new applyColors());
		
		//button to go back to startup menu
		Button optionsToStartup = new Button("Back");
		optionsToStartup.setTextFill(Color.BLACK);
		optionsToStartup.setTextAlignment(TextAlignment.CENTER);
		optionsToStartup.setStyle("-fx-background-color: white;");
		optionsToStartup.setLayoutX(110);
		optionsToStartup.setLayoutY(500);
		optionsToStartup.setOnAction(goback ->
		{
			menuOptions.setVisible(false);
			menuStartup.setVisible(true);
		});
		
		//add all of the nodes to the group
		menuOptions.getChildren().addAll(selectColor);
		menuOptions.getChildren().addAll(buttonApplyColors,optionsToStartup);
		
		//start hidden by default
		menuOptions.setVisible(false);
		
		//add menu to the pane
		reversiWindow.getChildren().add(menuOptions);
	}
	
	//define the high scores menu
	public void defineMenuHighScores()
	{
		menuHighScores = new Group();

		//background rectangle
		Rectangle r = new Rectangle();
		r.setX(0);
		r.setY(0);
		r.setWidth(getDimensions()[0]);
		r.setHeight(getDimensions()[1]);
		r.setFill(colorElement[COLOR_BACKG]);
		r.setOpacity(0.7);
		
		//name entry box
		nameField = new TextField();
		nameField.setLayoutX(300);
		nameField.setLayoutY(300);
		nameField.setVisible(false);
		
		//submit name button
		buttonSubmit = new Button("Submit");
		buttonSubmit.setLayoutX(300);
		buttonSubmit.setLayoutY(350);
		buttonSubmit.setStyle("-fx-background-color: "+cs.getColors()[COLOR_WHITE]+";");
		buttonSubmit.setTextFill(colorElement[COLOR_BLACK]);
		buttonSubmit.setVisible(false);
		buttonSubmit.setOnAction(new submitHighScore());
		
		//high score list
		highScores = new Label();
		highScores.setLayoutX(10);
		highScores.setLayoutY(1+getDimensions()[1]/4);
		highScores.setBackground(new Background(
				new BackgroundFill(colorElement[COLOR_BACKG],null,null)));
		highScores.setFont(Font.font("Helvetica",FontWeight.BOLD,24));
		highScores.setTextFill(colorElement[COLOR_LABEL]);
		highScores.setTextAlignment(TextAlignment.RIGHT);
		for(int i = 0; i < gs.hs.getHighScoreList().length; i++)
		{
				highScores.setText(highScores.getText()
						+gs.hs.getHighScoreList()[i][1]
						+"        "+gs.hs.getHighScoreList()[i][0]+"\n");
		}
		
		//new high score notification
		congrats = new Text("Congratulations!\nYou set a new record!");
		congrats.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,20));
		congrats.setFill(colorElement[COLOR_LABEL]);
		congrats.setStroke(Color.BLACK);
		congrats.setStrokeWidth(0.625);
		congrats.setVisible(false);
		congrats.setLayoutX(10);
		congrats.setLayoutY(highScores.getLayoutY()-49);
		
		//back to startup menu button
		backToStartup = new Button("Back to Main Menu");
		backToStartup.setVisible(false);
		backToStartup.setLayoutX(10);
		backToStartup.setLayoutY(500);
		backToStartup.setOnAction(backtomain ->
		{
			backToMainMenu();
			menuHighScores.setVisible(false);
			backToStartup.setVisible(false);
		});
		
		//add all of the nodes to the group
		menuHighScores.getChildren().addAll(r,nameField,buttonSubmit,highScores,congrats,backToStartup);
		
		//start hidden by default
		menuHighScores.setVisible(false);
		
		//add menu to the pane
		reversiWindow.getChildren().add(menuHighScores);
	}

	//set up the board according to the standard starting setup
	public void setupStandard()
	{
		//place black discs at E4 and D5 then disable those squares
		//E4 [4][3]
		disc[4][3].setFill(colorElement[COLOR_BLACK]);
		gs.setOneBoardPosition(4,3,gs.BLACK);
		reversiWindow.getChildren().add(disc[4][3]);
		boardSquare[4][3].setOnMouseClicked(null);
		//D5 [3][4]
		disc[3][4].setFill(colorElement[COLOR_BLACK]);
		gs.setOneBoardPosition(3,4,gs.BLACK);
		reversiWindow.getChildren().add(disc[3][4]);
		boardSquare[3][4].setOnMouseClicked(null);

		//place white discs at D4 and E5 then disable those squares
		//D4 [3][3]
		disc[3][3].setFill(colorElement[COLOR_WHITE]);
		gs.setOneBoardPosition(3,3,gs.WHITE);
		reversiWindow.getChildren().add(disc[3][3]);
		boardSquare[3][3].setOnMouseClicked(null);
		//E5 [4][4]
		disc[4][4].setFill(colorElement[COLOR_WHITE]);
		gs.setOneBoardPosition(4,4,gs.WHITE);
		reversiWindow.getChildren().add(disc[4][4]);
		boardSquare[4][4].setOnMouseClicked(null);

		//set each player's score to 2
		gs.setScoreBlack(2);
		gs.setScoreWhite(2);

		//define visual properties of the score text
		defineScores();

		//save the setup
		gs.as.saveToFile(gs.getBoardPosition());

		//record what moves are legal for each color
		gs.checkForLegalPosition();
	}

	//set up the board where players choose their starting discs
	public void setupCustom()
	{
		for(int i = 0; i < gs.BOARD_LENGTH; i++)
		{
			for(int j = 0; j < gs.BOARD_LENGTH; j++)
			{
				//only allow the players to place discs on the center 4 squares
				if((i==4)&&(j==3)
						||(i==3)&&(j==4)
						||(i==3)&&(j==3)
						||(i==4)&&(j==4))
				{
					boardSquare[i][j].setOnMouseClicked(new customPlaceDisc());
				}
				//hide the rest
				else
				{
					boardSquare[i][j].setOpacity(0.3);
					boardSquare[i][j].setOnMouseClicked(null);
				}
			}
		}
	}

	//timeline animation that play when disc(s) get flipped
	public void flipAnimation(int column, int row, boolean turn)
	{
		Timeline tl = new Timeline();
		
		//first half of the flip
		for(int i = 0; i < 10; i++)
		{
			KeyFrame kf1 = new KeyFrame(Duration.millis(i*(175/10)), action ->
			{
				disc[column][row].setScaleX(disc[column][row].getScaleX() - 0.1);
			});
			tl.getKeyFrames().add(kf1);
		}

		//change the color
		KeyFrame kf2 = new KeyFrame(Duration.millis(175), action ->
		{
			disc[column][row].setFill(turn ? colorElement[COLOR_BLACK] : colorElement[COLOR_WHITE]);
		});
		tl.getKeyFrames().add(kf2);

		//second half of the flip
		for(int i = 0; i < 10; i++)
		{
			KeyFrame kf3 = new KeyFrame(Duration.millis(175+175*i/10), action ->
			{
				disc[column][row].setScaleX(disc[column][row].getScaleX() + 0.1);
			});
			tl.getKeyFrames().add(kf3);
		}

		tl.play();
	}
	
	//processes for when a game is finished
	public void endOfGame()
	{
		//determine winner
		feedText.setText(feedText.getText()+"\n\tGAME OVER\n");
		
		//bring up the high score menu
		menuHighScores.setVisible(true);
		backToStartup.setVisible(true);
		menuHighScores.toFront();

		//announce who won in the turn feed
		//black won
		if(gs.determineWinner() == gs.BLACK)
		{
			feedText.setText(feedText.getText()
					+"\t"+P1.toUpperCase()+" WINS\n\t"
					+String.valueOf(gs.getScoreBlack())+" - "
					+String.valueOf(gs.getScoreWhite())+"\n\n");
			turnFeed.setVvalue(1);

			placement = gs.hs.checkForNewHighScore(gs.getScoreBlack());
			if(placement != 10)
			{
				congrats.setVisible(true);
				nameField.setVisible(true);
				buttonSubmit.setVisible(true);
			}
		}
		//white won
		else if(gs.determineWinner() == gs.WHITE)
		{
			feedText.setText(feedText.getText()
					+"\t"+P2.toUpperCase()+" WINS\n\t"
					+String.valueOf(gs.getScoreWhite())+" - "
					+String.valueOf(gs.getScoreBlack())+"\n\n");
			turnFeed.setVvalue(1);

			//white must be a human player to submit a high score
			placement = gs.hs.checkForNewHighScore(gs.getScoreWhite());;
			if((placement != 10)&&(gs.as.oppFromSave() == OPPONENT_HUMAN))
			{
				congrats.setVisible(true);
				nameField.setVisible(true);
				buttonSubmit.setVisible(true);
			}
		}
		//tie
		else
		{
			feedText.setText(feedText.getText()
					+"\tUNPRECEDENTED\n\tTHE PLAYERS TIED\n\n");
			turnFeed.setVvalue(1);
		}

		//delete game-related files
		gs.as.deleteSaves();
	}
	
	//clear all the board elements and go back to the startup menu
	public void backToMainMenu()
	{
		//remove turn feed and scores
		reversiWindow.getChildren().removeAll(turnFeed,scoreTextBlack,scoreTextWhite);
		
		for(int i = 0; i < gs.BOARD_LENGTH; i++)
		{
			for(int j = 0; j < gs.BOARD_LENGTH; j++)
			{
				//remove discs
				reversiWindow.getChildren().remove(disc[i][j]);
				
				//return functionality to squares and then remove them
				reversiWindow.getChildren().remove(boardSquare[i][j]);
			}
			
			//remove grid labels
			reversiWindow.getChildren().removeAll(boardLabel[i][0],boardLabel[i][1]);
		}
		
		//make the startup menu visible
		menuStartup.setVisible(true);
	}
	
	//place a disc from where a square was clicked (or selected by an ai opponent)
	public void placeDisc()
	{
		//shorten names of called objects
		boolean player = gs.opp.getPlayerTurn();
		int clickedSquare = gs.getOneLegalPosition(place[0],place[1]);

		//check for legal moves
		gs.checkForLegalPosition();

		//if the player cannot make any legal moves this turn, they have to skip their turn
		if(gs.checkForAnyLegalMoves(gs.opp.getPlayerTurn()) == 0)
		{
			feedText.setText(feedText.getText()
					+(gs.opp.getPlayerTurn() ? P1 : P2)
					+" has to skip their turn.\n\n");
			turnFeed.setVvalue(1);
			gs.opp.changeTurn();
			gs.as.savePlayerTurn(gs.opp.getPlayerTurn());
		}
		//if a legal turn can be made, carry it out
		else if(((player == true)&&(clickedSquare == gs.BLACK))
				||((player == false)&&(clickedSquare == gs.WHITE))
				||(clickedSquare == gs.BOTH))
		{
			//get the board positions from the save state
			int[][] before = gs.as.boardPositionFromSave();

			//get the board from the play
			int[][] after = gs.getBoardPosition();

			//make the legal play
			gs.makeLegalMove(place[0],place[1]);

			//remove functionality from the now nonempty square
			boardSquare[place[0]][place[1]].setOnMouseClicked(null);

			//make the placed disc the correct color
			disc[place[0]][place[1]].setFill(player ?
					colorElement[COLOR_BLACK] : colorElement[COLOR_WHITE]);

			//add a disc to that position
			reversiWindow.getChildren().add(disc[place[0]][place[1]]);

			//start visually flipping discs
			//compare the color of every disc before with every disc after
			for(int i = 0; i < gs.BOARD_LENGTH; i++)
			{
				for(int j = 0; j < gs.BOARD_LENGTH; j++)
				{
					//a disc already on the board gets flipped and the placed disc gets ignored
					if((before[i][j] != after[i][j])&&(before[i][j] != gs.EMPTY))
					{
						flipAnimation(i,j,player);
					}
				}
			}

			//update turn feed
			feedText.setText(feedText.getText()
					+(gs.opp.getPlayerTurn() ? P1 : P2)
					+" placed a disc at "
					+LETTER[place[0]]+NUMBER[place[1]]
							+" and flipped "
							+String.valueOf(gs.getFlipsThisPosition())
							+" disc"+(gs.getFlipsThisPosition() > 1 ? "s" : "")+".\n\n");
			turnFeed.setVvalue(1);

			//update the scores
			gs.tallyScores();
			scoreTextBlack.setText(String.valueOf(gs.getScoreBlack()));
			scoreTextWhite.setText(String.valueOf(gs.getScoreWhite()));

			//save the game
			gs.as.saveToFile(after);
			gs.as.saveLegalPosition(gs.getLegalPosition());

			//update legal moves
			gs.checkForLegalPosition();

			//if neither player can make legal moves anymore, the game is over
			if(gs.checkForAnyLegalMoves(gs.opp.getPlayerTurn()) == gs.GAMEOVER)
			{
				endOfGame();
			}
			//otherwise, change turns
			else
			{
				gs.opp.setLegalPosition(gs.getLegalPosition());
				gs.opp.setBoardPosition(gs.getBoardPosition());
				gs.opp.changeTurn();
				gs.as.savePlayerTurn(gs.opp.getPlayerTurn());

				//if it's now player 2's turn, let their behavior automatically make a move
				if((!gs.opp.getPlayerTurn())&&(gs.as.oppFromSave()!=OPPONENT_HUMAN))
				{
					place[0] = gs.opp.getDecidedPosition()[0];
					place[1] = gs.opp.getDecidedPosition()[1];
					placeDisc();
				}
			}
		}
	}

	//
	//event handlers
	//
	
	//new game button function
	private class newGame implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent start)
		{
			//hide the startup menu and show the setup menu
			menuStartup.setVisible(false);
			menuSetup.setVisible(true);
		}
	}

	//load game button function
	private class loadGame implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent load)
		{
			//dont load if the last save state is a completed game
			if(gs.as.canLoad())
			{
				//hide the startup menu
				menuStartup.setVisible(false);

				//set the board position from file
				gs.setBoardPosition(gs.as.boardPositionFromSave());

				//set the legal positions from file
				gs.setLegalPosition(gs.as.legalPositionFromSave());

				//set opponent type
				gs.setOpponentType(gs.as.oppFromSave());

				//visually set the board
				place = new int[2];
				defineBoardSquares();
				defineGridLabels();
				defineTurnFeed();
				defineDiscs();

				//add discs to pane accordingly
				for(int i = 0; i < gs.BOARD_LENGTH; i++)
				{
					for(int j = 0; j < gs.BOARD_LENGTH; j++)
					{
						if(gs.getBoardPosition()[i][j] != gs.EMPTY)
						{
							disc[i][j].setVisible(true);
							disc[i][j].setFill(gs.getBoardPosition()[i][j] == gs.BLACK ?
									colorElement[COLOR_BLACK] : colorElement[COLOR_WHITE]);
							reversiWindow.getChildren().add(disc[i][j]);
						}
					}
				}

				//set the player turn from file
				gs.opp.setPlayerTurn(gs.as.turnFromSave());

				//announce whose turn it was when the game was closed
				feedText.setText(
						(gs.opp.getPlayerTurn() ? P1 : P2)
						+" continues the game.\n\n");

				//update the scores
				defineScores();
				gs.tallyScores();
				scoreTextBlack.setText(String.valueOf(gs.getScoreBlack()));
				scoreTextWhite.setText(String.valueOf(gs.getScoreWhite()));
			}
			//inform the user that there's no file to load
			else
			{
				//briefly show a text prompt saying there is no game file
				Text noFile = new Text("No file to load.");
				noFile.setFont(Font.font("Helvetica",FontWeight.EXTRA_BOLD,20));
				noFile.setFill(colorElement[COLOR_LABEL]);
				noFile.setStroke(Color.BLACK);
				noFile.setStrokeWidth(0.625);
				noFile.setX(10);
				noFile.setY(210);
				Timeline tl = new Timeline();
				KeyFrame kf1 = new KeyFrame(Duration.millis(0), action ->
				{
					reversiWindow.getChildren().add(noFile);
					buttonLoad.setOnAction(null);
				});
				KeyFrame kf2 = new KeyFrame(Duration.millis(1250), action ->
				{
					reversiWindow.getChildren().remove(noFile);
					buttonLoad.setOnAction(new loadGame());
				});
				tl.getKeyFrames().addAll(kf1,kf2);
				tl.play();
			}
		}
	}

	//apply colors button function
	private class applyColors implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent apply)
		{
			//set the user-defined colors
			for(int i = 0; i < colorElement.length; i++)
			{
				for(int j = 0; j < cs.getHexColorList().length; j++)
				{
					//correlate a name to a hex code
					if(selectColor[i].getValue()==(cs.getHexColorList()[j][1]))
					{
						cs.setOneColor(i,cs.getHexColorList()[j][0]);
						colorElement[i] = Color.web(cs.getHexColorList()[j][0]);
					}
					
				}
			}
			
			//for the background, change the color of the pane itself
			reversiWindow.setStyle("-fx-background-color: "+cs.getColors()[COLOR_BACKG]+";"
					+"-fx-border-color: black;"
					+"-fx-border-width: 10;");
			
			//save the changes to colorscheme.txt
			cs.saveScheme();
		}
	}
	
	//begin a new game 
	private class gameStart implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent begin)
		{
			gs = new GridSystem();
			
			//determine the opponent type
			//human
			if(oppBox.getValue().contentEquals("Human"))
			{
				gs.setOpponentType(OPPONENT_HUMAN);
			}
			//ai 1
			else if(oppBox.getValue().contentEquals("Computer 1"))
			{
				gs.setOpponentType(OPPONENT_AI1);
			}
			//ai 2
			else if(oppBox.getValue().contentEquals("Computer 2"))
			{
				gs.setOpponentType(OPPONENT_AI2);
			}
			
			//black goes first
			gs.opp.setPlayerTurn(true);
			
			//define visual properties
			place = new int[2];
			defineBoardSquares();
			defineGridLabels();
			defineDiscs();
			defineTurnFeed();
			
			//determine the board setup
			if(setupBox.getValue().contentEquals("Standard Setup"))
			{
				setupStandard();
			}
			else
			{
				setupCustom();
			}
			gs.as.saveToFile(gs.getBoardPosition());
			
			//when the opponent and board are set up, hide the menu
			menuSetup.setVisible(false);
		}
	}
	
	//for the custom game setup, let players choose the opening arrangement independent of the flip rules
	private class customPlaceDisc implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent gambit)
		{
			//check which square started this event
			for(int i = 0; i < gs.BOARD_LENGTH; i++)
			{
				for(int j = 0; j < gs.BOARD_LENGTH; j++)
				{
					if((Rectangle) gambit.getSource() == boardSquare[i][j])
					{
						//set the new disc position equal to the clicked square
						place[0] = i;
						place[1] = j;
					}
				}
			}
			
			//make the legal play
			gs.makeLegalMove(place[0],place[1]);
			
			//remove functionality from the now nonempty square
			boardSquare[place[0]][place[1]].setOnMouseClicked(null);
			
			//make the placed disc the correct color
			disc[place[0]][place[1]].setFill(gs.opp.getPlayerTurn() ?
					colorElement[COLOR_BLACK] : colorElement[COLOR_WHITE]);
			
			//add a disc to that position
			reversiWindow.getChildren().add(disc[place[0]][place[1]]);
			
			//update boardPosition
			gs.setOneBoardPosition(place[0],place[1],(gs.opp.getPlayerTurn() ? gs.BLACK : gs.WHITE));
			
			//save the setup
			gs.as.saveToFile(gs.getBoardPosition());
			
			//the game can properly start once both players have two discs on the board
			if((gs.checkNumberOfEach()[0] == 2)&&(gs.checkNumberOfEach()[1] == 2))
			{
				//restore functionality to all the other board squares
				for(int i = 0; i < gs.BOARD_LENGTH; i++)
				{
					for(int j = 0; j < gs.BOARD_LENGTH; j++)
					{
						boardSquare[i][j].setOpacity(1);
						boardSquare[i][j].setOnMouseClicked(new placeDiscClick());
					}
				}
				boardSquare[4][3].setOnMouseClicked(null);
				boardSquare[3][4].setOnMouseClicked(null);
				boardSquare[3][3].setOnMouseClicked(null);
				boardSquare[4][4].setOnMouseClicked(null);
				
				//set the scores
				gs.setScoreBlack(2);
				gs.setScoreWhite(2);
				defineScores();
				
				//save the setup
				gs.as.saveToFile(gs.getBoardPosition());

				//record what moves are legal for each color
				gs.checkForLegalPosition();
				gs.as.saveLegalPosition(gs.getLegalPosition());
			}

			//change turns
			gs.opp.changeTurn();
			gs.as.savePlayerTurn(gs.opp.getPlayerTurn());
		}
	}

	//use the clicked square's indices to see if a disc can be placed on it
	private class placeDiscClick implements EventHandler<MouseEvent>
	{
		@Override
		public void handle(MouseEvent squeak)
		{
			//check which square started this event
			for(int i = 0; i < gs.BOARD_LENGTH; i++)
			{
				for(int j = 0; j < gs.BOARD_LENGTH; j++)
				{
					if((Rectangle) squeak.getSource() == boardSquare[i][j])
					{
						//set the new disc position equal to the clicked square
						place[0] = i;
						place[1] = j;
					}
				}
			}
			
			placeDisc();
		}
	}

	//update the high score list when a new record is made
	private class submitHighScore implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent enter)
		{
			//make sure name isnt empty
			if(!nameField.getText().contentEquals(""))
			{
				//make sure name isnt too long
				if(nameField.getText().trim().length() <= MAX_NAME_LENGTH)
				{
					if(gs.getScoreBlack() > gs.getScoreWhite())
					{
						gs.hs.updateHighScores(placement,gs.getScoreBlack(),nameField.getText().trim());
					}
					else if(gs.getScoreWhite() > gs.getScoreBlack())
					{
						gs.hs.updateHighScores(placement,gs.getScoreWhite(),nameField.getText().trim());
					}

					//update the high score list
					highScores.setText("");
					for(int i = 0; i < gs.hs.getHighScoreList().length; i++)
					{
						highScores.setText(highScores.getText()
								+gs.hs.getHighScoreList()[i][1]
										+"        "+gs.hs.getHighScoreList()[i][0]+"\n");
					}

					//dont allow the user to submit the same high score again
					congrats.setVisible(false);
					nameField.setVisible(false);
					buttonSubmit.setVisible(false);
				}
				//inform user name is too long
				else
				{
					Timeline tl = new Timeline();
					KeyFrame kf1 = new KeyFrame(Duration.millis(0), action ->
					{
						congrats.setText("\nName is too long.");
						buttonSubmit.setOnAction(null);
					});
					KeyFrame kf2 = new KeyFrame(Duration.millis(1250), action ->
					{
						congrats.setText("Congratulations!\nYou set a new record!");
						buttonSubmit.setOnAction(new submitHighScore());
					});
					tl.getKeyFrames().addAll(kf1,kf2);
					tl.play();
				}
			}
			//inform user name cannot be empty
			else
			{
				Timeline tl = new Timeline();
				KeyFrame kf1 = new KeyFrame(Duration.millis(0), action ->
				{
					congrats.setText("\nName cannot be empty.");
					buttonSubmit.setOnAction(null);
				});
				KeyFrame kf2 = new KeyFrame(Duration.millis(1250), action ->
				{
					congrats.setText("Congratulations!\nYou set a new record!");
					buttonSubmit.setOnAction(new submitHighScore());
				});
				tl.getKeyFrames().addAll(kf1,kf2);
				tl.play();
			}
		}
	}

	//
	//setters and getters
	//

	//get the pane when ReversiGUI declares a scene
	public Pane getReversiWindow()
	{
		return reversiWindow;
	}
	
	//get the dimensions of the pane
	public double[] getDimensions()
	{
		//define the dimensions of the pane
		//width, height
		double[] d = {900,600};
		return d;
	}
}