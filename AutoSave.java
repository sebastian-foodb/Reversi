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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class AutoSave
{
	//files
	//board save file
	private File saveFile;
	//legal positions file
	private File legalFile;
	//turn file
	private File turnFile;
	//opponent file
	private File oppFile;
	
	//multiuse writer
	private PrintWriter pw;
	
	//multiuse scanner
	private Scanner scan;
	
	//size of board from GridSystem
	private int bl;
	
	//constructor
	public AutoSave(int bl)
	{
		//define board length based on the input argument from GridSystem
		this.bl = bl;
				
		//define external save file
		saveFile = new File("savestate.txt");

		//define external file for legalPosition[][] in GridSystem
		legalFile = new File("legalpositions.txt");
		
		//define external file for playerTurn in OpponentGeneric
		turnFile = new File("turn.txt");
		
		//define external file for setOpponentType in GridSystem
		oppFile = new File("opp.txt");
	}

	//write the board state to the save file
	public void saveToFile(int[][] board)
	{
		try
		{
			pw = new PrintWriter(saveFile);
			
			//write each row of the board to a file
			for(int i = 0; i < bl; i++)
			{
				for(int j = 0; j < bl; j++)
				{
					//0 empty,1 black,2 white
					pw.write(board[j][i]+" ");
				}
				//start a new line after a row is written
				pw.write("\n");
			}
			
			//close the writer
			pw.close();
		}
		catch(IOException UhOhOthello)
		{
			saveFile = new File("board.txt");
			saveToFile(board);
		}
	}
	
	//write the legal positions for each color to a file
	public void saveLegalPosition(int[][] legalMoves)
	{
		try
		{
			pw = new PrintWriter(legalFile);

			//write each row of legalPosition[][] to its file
			for(int i = 0; i < bl; i++)
			{
				for(int j = 0; j < bl; j++)
				{
					//0 empty,1 black,2 white
					pw.write(legalMoves[j][i]+" ");
				}
				//start a new line after a row is written
				pw.write("\n");
			}

			//close the writer
			pw.close();
		}
		catch(IOException UhOhOthello)
		{
			legalFile = new File("legalpositions.txt");
			saveLegalPosition(legalMoves);
		}
	}
	
	//write the current turn to a file
	public void savePlayerTurn(boolean turn)
	{
		try
		{
			pw = new PrintWriter(turnFile);
			pw.write(String.valueOf(turn));
			pw.close();
		}
		catch(IOException UhOhOthello)
		{
			turnFile = new File("turn.txt");
			savePlayerTurn(turn);
		}
	}
	
	//write the opponent type to a file
	public void saveOpponentType(int type)
	{
		try
		{
			pw = new PrintWriter(oppFile);
			//pw.write(type);
			pw.write(String.valueOf(type));
			pw.close();
		}
		catch(IOException UhOhOthello)
		{
			oppFile = new File("opp.txt");
			saveOpponentType(type);
		}
	}
	
	//get board positions from board.txt
	public int[][] boardPositionFromSave()
	{
		int[][] board = new int[bl][bl];

		try
		{
			scan = new Scanner(saveFile);

			//read each value at each position
			for(int i = 0; i < bl; i++)
			{
				for(int j = 0; j < bl; j++)
				{
					board[j][i] = scan.nextInt();
				}
			}

			//close the scanner
			scan.close();
		}
		catch(FileNotFoundException UhOhOthello) {/**/}

		//return the board positions read from savestate.txt
		return board;
	}
	
	//get legal positions from legalpositions.txt
	public int[][] legalPositionFromSave()
	{
		int[][] legal = new int[bl][bl];
		
		try
		{
			scan = new Scanner(legalFile);
			
			//read each value at each position
			for(int i = 0; i < bl; i++)
			{
				for(int j = 0; j < bl; j++)
				{
					legal[j][i] = scan.nextInt();
				}
			}
			
			scan.close();
		}
		catch(FileNotFoundException UhOhOthello) {/**/}
		
		return legal;
	}
	
	//get player turn from turn.txt
	public boolean turnFromSave()
	{
		boolean turn = true;
		
		//define the player turn from whats written in turn.txt
		try
		{
			scan = new Scanner(turnFile);
			turn = Boolean.valueOf(scan.next());
			scan.close();
		}
		catch(FileNotFoundException UhOhOthello) {/**/}
		
		return turn;
	}
	
	//get opponent type from opponent.txt
	public int oppFromSave()
	{
		int type = 0;
		
		//define the opponent type from whats written in opponent.txt
		try
		{
			scan = new Scanner(oppFile);
			type = scan.nextInt();
			scan.close();
		}
		catch(FileNotFoundException UhOhOthello) {/**/}
		
		return type;
	}
	
	//see if there is even a file to load
	public boolean canLoad()
	{
		boolean possible = legalFile.exists() ? true : false;
		return possible;
	}
	
	//delete saved files
	public void deleteSaves()
	{
		saveFile.delete();
		legalFile.delete();
		turnFile.delete();
		oppFile.delete();
	}
}