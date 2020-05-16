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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HighScore
{
	//CONSTANTS
	//high score list will only track the 10 highest scores
	private final int RANKS = 10;
	//default data if there is no highscores.txt
	private final String[][] DEFAULT_LIST =
		{
				{"51","Rikki"},
				{"49","Fagen"},
				{"47","Becker"},
				{"45","Dias"},
				{"43","Baxter"},
				{"41","Hodder"},
				{"39","Palmer"},
				{"37","Jones"},
				{"35","McDonald"},
				{"33","Porcaro"}
		};
	
	//high score array
	private String[][] highScoreList;
	
	//high score file
	private File scores;
	
	//file scanner
	private Scanner hsreader;
	
	//file writer
	private PrintWriter hswriter;
	
	//constructor
	public HighScore()
	{
		//define external high score file
		scores = new File("highscores.txt");
		
		//define internal high score list
		highScoreList = new String[RANKS][2];
		
		loadHighScores();
	}
	
	//create the default high score list if one does not exist
	public void writeDefaultHighScores()
	{
		try
		{
			hswriter = new PrintWriter(scores);
			for(int i = 0; i < RANKS; i++)
			{
				//write DEFAULT_LIST to highscores.txt
				hswriter.write(DEFAULT_LIST[i][0]+" "+DEFAULT_LIST[i][1]+"\n");
				
				//save DEFAULT_LIST to highScoreList
				highScoreList[i][0] = DEFAULT_LIST[i][0];
				highScoreList[i][1] = DEFAULT_LIST[i][1];
			}	
			hswriter.close();
			
			saveHighScores();
		}
		catch (FileNotFoundException MasterOfNothing)
		{
			scores = new File("highscores.txt");
			writeDefaultHighScores();
		}
	}
	
	//take a player's score and see where they place
	public int checkForNewHighScore(int score)
	{
		//counters
		boolean keepChecking = true;
		int place = 0;
		
		//compare the argument to every score on the list
		while(keepChecking)
		{
			if((place == RANKS)
					||(score > Integer.parseInt(highScoreList[place][0])))
			{
				keepChecking = false;
			}
			else
			{
				place++;
			}
		}
		
		//return the index of the place the new high score is at
		return place;
	}

	//when there's a new high score, edit it into the high score variable
	public void updateHighScores(int place, int score, String name)
	{		
		//move the appropriate amount of places downward
		for(int i = RANKS-1; i > place; i--)
		{
			highScoreList[i][0] = highScoreList[i-1][0];
			highScoreList[i][1] = highScoreList[i-1][1];
		}
		
		//put the new score in its place
		highScoreList[place][0] = String.valueOf(score);
		highScoreList[place][1] = name;
		
		//update the high score file
		saveHighScores();
	}
	
	//save high scores from variable to file
	public void saveHighScores()
	{
		try
		{
			hswriter = new PrintWriter(scores);
			for(int i = 0; i < RANKS; i++)
			{
				hswriter.write(highScoreList[i][0]+" "+highScoreList[i][1]+"\n");
			}
			hswriter.close();
		}
		catch (FileNotFoundException e)
		{
			scores = new File("highscores.txt");
			saveHighScores();
		}
	}

	//load high scores from file to variable
	public void loadHighScores()
	{
		try
		{
			hsreader = new Scanner(scores);
			for(int i = 0; i < RANKS; i++)
			{
				for(int j = 0; j < 2; j++)
				{
					highScoreList[i][j] = hsreader.next();
				}
			}
			hsreader.close();
		}
		catch(FileNotFoundException e)
		{
			scores = new File("highscores.txt");
			writeDefaultHighScores();
			loadHighScores();
		}
	}

	//
	//setters and getters
	//

	//get high score list
	public String[][] getHighScoreList()
	{
		return highScoreList;
	}
}