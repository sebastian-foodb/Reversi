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

public class GridSystem
{
	//CONSTANTS
	public final int BOARD_LENGTH = 8;
	public final int EMPTY = 0;
	public final int BLACK = 1;
	public final int WHITE = 2;
	public final int BOTH = 3;
	public final int ILLEGAL = 0;
	public final int LEGAL = 1;
	public final int GAMEOVER = -1;
	
	//classes to call
	public OpponentGeneric opp;
	public AutoSave as;
	public HighScore hs;
	
	//board positions [0 empty,1 black,2 white]
	private int[][] boardPosition;
	
	//board directions where opponent discs will be flipped
	private int flipsThisPosition;
	
	//see if discs can legally get flipped in one of eight directions
	private int[][] legalPosition;
	
	//player turn [true black,false white]
	private boolean playerTurn;
	
	//score for each player
	private int scoreBlack;
	private int scoreWhite;
	
	//constructor
	public GridSystem()
	{
		//define size of the board
		boardPosition = new int[BOARD_LENGTH][BOARD_LENGTH];
		legalPosition = new int[BOARD_LENGTH][BOARD_LENGTH];

		//define save file with the length of one side of the board
		as = new AutoSave(BOARD_LENGTH);
		
		//instantiate high score
		hs = new HighScore();
	}
	
	//set the type of opponent to play against
	public void setOpponentType(int opponentType)
	{
		//define the type of opponent based on the input argument from BoardPieces
		//human
		if(opponentType == 0)
		{
			opp = new OpponentHuman();
		}
		//ai 1
		else if(opponentType == 1)
		{
			opp = new OpponentAI1();
		}
		//ai 2
		else if(opponentType == 2)
		{
			opp = new OpponentAI2();
		}
		
		//save the selection to a file
		as.saveOpponentType(opponentType);
	}
	
	public int[] checkNumberOfEach()
	{
		int[] number = new int[2];
		
		for(int i = 0; i < BOARD_LENGTH; i++)
		{
			for(int j = 0; j < BOARD_LENGTH; j++)
			{
				if(boardPosition[i][j]==BLACK)
				{
					number[0]++;
				}
				else if(boardPosition[i][j]==WHITE)
				{
					number[1]++;
				}
			}
		}
		
		return number;
	}
	
	//identify which squares a disc can be placed on
	public void checkForLegalPosition()
	{
		legalPosition = new int[BOARD_LENGTH][BOARD_LENGTH];
		
		//players and position information
		int player, enemy;
		int column, row;
		for(int i = 0; i < 2; i++)
		{
			//see if black or white is the player
			if(i == 0)
			{
				player = BLACK;
				enemy = WHITE;
			}
			else
			{
				player = WHITE;
				enemy = BLACK;
			}

			//counters
			boolean lineEnd;
			int place, flips, legalDirections;

			//check every board position to see a disc can be placed
			for(column = 0; column < BOARD_LENGTH; column++)
			{
				for(row = 0; row < BOARD_LENGTH; row++)
				{
					//if the space is already filled, a disc cannot be placed there
					if(boardPosition[column][row] != EMPTY)
					{
						legalPosition[column][row] = ILLEGAL;
					}
					//if the position is empty, check every direction for potential flips
					else
					{
						//initialize counters
						lineEnd = false;
						place = 1;
						flips = 0;
						legalDirections = 0;

						//check N positions
						try
						{
							while(lineEnd == false)
							{
								//the next position is empty
								if(boardPosition[column][row-place] == EMPTY)
								{
									lineEnd = true;
								}
								//the next disc is the enemy's color
								else if(boardPosition[column][row-place] == enemy)
								{
									flips++;
								}
								//the next is disc is the player's color
								else if(boardPosition[column][row-place] == player)
								{
									//establish a line
									lineEnd = true;

									//this direction is only legal if any flips can be made
									if(flips > 0)
									{
										legalDirections++;
									}
								}

								//increment counter
								place++;
							}
						}
						catch(Exception n) {/*a disc cannot be placed here*/}

						//reset counters
						lineEnd = false;
						flips = 0;
						place = 1;

						//check NE positions
						try
						{
							while(lineEnd == false)
							{
								//the next position is empty
								if(boardPosition[column+place][row-place] == EMPTY)
								{
									lineEnd = true;
								}
								//the next disc is the enemy's color
								else if(boardPosition[column+place][row-place] == enemy)
								{
									flips++;
								}
								//the next is disc is the player's color
								else if(boardPosition[column+place][row-place] == player)
								{
									//establish a line
									lineEnd = true;

									//this direction is only legal if any flips can be made
									if(flips > 0)
									{
										legalDirections++;
									}
								}

								//increment counter
								place++;
							}
						}
						catch(Exception ne) {/*a disc cannot be placed here*/}

						//reset counters
						lineEnd = false;
						flips = 0;
						place = 1;

						//check E positions
						try
						{
							while(lineEnd == false)
							{
								//the next position is empty
								if(boardPosition[column+place][row] == EMPTY)
								{
									lineEnd = true;
								}
								//the next disc is the enemy's color
								else if(boardPosition[column+place][row] == enemy)
								{
									flips++;
								}
								//the next is disc is the player's color
								else if(boardPosition[column+place][row] == player)
								{
									//establish a line
									lineEnd = true;

									//this direction is only legal if any flips can be made
									if(flips > 0)
									{
										legalDirections++;
									}
								}

								//increment counter
								place++;
							}
						}
						catch(Exception e) {/*a disc cannot be placed here*/}

						//reset counters
						lineEnd = false;
						flips = 0;
						place = 1;

						//check SE positions
						try
						{
							while(lineEnd == false)
							{
								//the next position is empty
								if(boardPosition[column+place][row+place] == EMPTY)
								{
									lineEnd = true;
								}
								//the next disc is the enemy's color
								else if(boardPosition[column+place][row+place] == enemy)
								{
									flips++;
								}
								//the next is disc is the player's color
								else if(boardPosition[column+place][row+place] == player)
								{
									//establish a line
									lineEnd = true;

									//this direction is only legal if any flips can be made
									if(flips > 0)
									{
										legalDirections++;
									}
								}

								//increment counter
								place++;
							}
						}
						catch(Exception se) {/*a disc cannot be placed here*/}

						//reset counters
						lineEnd = false;
						flips = 0;
						place = 1;

						//check S positions
						try
						{
							while(lineEnd == false)
							{
								//the next position is empty
								if(boardPosition[column][row+place] == EMPTY)
								{
									lineEnd = true;
								}
								//the next disc is the enemy's color
								else if(boardPosition[column][row+place] == enemy)
								{
									flips++;
								}
								//the next is disc is the player's color
								else if(boardPosition[column][row+place] == player)
								{
									//establish a line
									lineEnd = true;

									//this direction is only legal if any flips can be made
									if(flips > 0)
									{
										legalDirections++;
									}
								}

								//increment counter
								place++;
							}
						}
						catch(Exception s) {/*a disc cannot be placed here*/}

						//reset counters
						lineEnd = false;
						flips = 0;
						place = 1;

						//check SW positions
						try
						{
							while(lineEnd == false)
							{
								//the next position is empty
								if(boardPosition[column-place][row+place] == EMPTY)
								{
									lineEnd = true;
								}
								//the next disc is the enemy's color
								else if(boardPosition[column-place][row+place] == enemy)
								{
									flips++;
								}
								//the next is disc is the player's color
								else if(boardPosition[column-place][row+place] == player)
								{
									//establish a line
									lineEnd = true;

									//this direction is only legal if any flips can be made
									if(flips > 0)
									{
										legalDirections++;
									}
								}

								//increment counter
								place++;
							}
						}
						catch(Exception sw) {/*a disc cannot be placed here*/}

						//reset counters
						lineEnd = false;
						flips = 0;
						place = 1;

						//check W positions
						try
						{
							while(lineEnd == false)
							{
								//the next position is empty
								if(boardPosition[column-place][row] == EMPTY)
								{
									lineEnd = true;
								}
								//the next disc is the enemy's color
								else if(boardPosition[column-place][row] == enemy)
								{
									flips++;
								}
								//the next is disc is the player's color
								else if(boardPosition[column-place][row] == player)
								{
									//establish a line
									lineEnd = true;

									//this direction is only legal if any flips can be made
									if(flips > 0)
									{
										legalDirections++;
									}
								}

								//increment counter
								place++;
							}
						}
						catch(Exception w) {/*a disc cannot be placed here*/}

						//reset counters
						lineEnd = false;
						flips = 0;
						place = 1;

						//check NW positions
						try
						{
							while(lineEnd == false)
							{
								//the next position is empty
								if(boardPosition[column-place][row-place] == EMPTY)
								{
									lineEnd = true;
								}
								//the next disc is the enemy's color
								else if(boardPosition[column-place][row-place] == enemy)
								{
									flips++;
								}
								//the next is disc is the player's color
								else if(boardPosition[column-place][row-place] == player)
								{
									//establish a line
									lineEnd = true;

									//this direction is only legal if any flips can be made
									if(flips > 0)
									{
										legalDirections++;
									}
								}

								//increment counter
								place++;
							}
						}
						catch(Exception nw) {/*a disc cannot be placed here*/}

						//once every direction is checked, verify that this position is legal
						if((legalDirections > 0)&&(legalPosition[column][row] == EMPTY))
						{
							legalPosition[column][row] = player;
						}
						//legal for both
						else if((legalDirections > 0)&&(legalPosition[column][row] == enemy))
						{
							legalPosition[column][row] = BOTH;
						}
						//legal for neither
						else if((legalDirections == 0)&&(legalPosition[column][row] != enemy))
						{
							legalPosition[column][row] = ILLEGAL;
						}
					}
				}
			}
		}
		
		//save the array to a file
		as.saveLegalPosition(legalPosition);
	}
	
	//check if a player can make any legal moves at all this turn
	public int checkForAnyLegalMoves(boolean turn)
	{
		int plays = 0;
		int zeroes = 0;

		for(int i = 0; i < BOARD_LENGTH; i++)
		{
			for(int j = 0; j < BOARD_LENGTH; j++)
			{
				//if the value from legalPosition matches the current player turn, there's a possible move
				if(
						((turn == true)&&(legalPosition[i][j] == BLACK))
						||((turn == false)&&(legalPosition[i][j] == WHITE))
						||(legalPosition[i][j] == BOTH)
						)
				{
					plays++;
				}
				else if(legalPosition[i][j] == ILLEGAL)
				{
					zeroes++;
				}
			}
		}
		
		//if there are no possible turns for any player, the game ends
		if(zeroes == BOARD_LENGTH*BOARD_LENGTH)
		{
			plays = GAMEOVER;
		}

		return plays;
	}
	
	//this method is always ran after checkForLegalPosition okays the column and row
	public void makeLegalMove(int column, int row)
	{
		//player information
		int player, enemy;

		if(opp.getPlayerTurn())
		{
			player = BLACK;
			enemy = WHITE;
		}
		else
		{
			player = WHITE;
			enemy = BLACK;
		}
		
		//the placed disc now contributes to the player's territory
		boardPosition[column][row] = player;
		
		//counters
		boolean lineEnd = false;
		int flips = 0;
		int place = 1;
		flipsThisPosition = 0;

		//check N positions
		try
		{
			while(lineEnd == false)
			{
				//the next position is empty
				if(boardPosition[column][row-place] == EMPTY)
				{
					//immediately stop checking this direction
					lineEnd = true;
				}
				//the next disc is the enemy's color
				else if(boardPosition[column][row-place] == enemy)
				{
					//increment the potential number of flips
					flips++;
				}
				//the next is disc is the player's color
				else if(boardPosition[column][row-place] == player)
				{
					//stop checking this direction
					lineEnd = true;

					//this direction is only legal if any flips can be made
					if(flips > 0)
					{
						for(int i = 1; i < place; i++)
						{
							boardPosition[column][row-i] = player;
						}
						flipsThisPosition += flips;
					}
				}

				//increment counter
				place++;
			}
		}
		//a disc cannot be placed here
		catch(Exception n) {/*a disc cannot be placed here*/}

		//reset counters
		lineEnd = false;
		flips = 0;
		place = 1;

		//check NE positions
		try
		{
			while(lineEnd == false)
			{
				//the next position is empty
				if(boardPosition[column+place][row-place] == EMPTY)
				{
					//immediately stop checking this direction
					lineEnd = true;
				}
				//the next disc is the enemy's color
				else if(boardPosition[column+place][row-place] == enemy)
				{
					//increment the potential number of flips
					flips++;
				}
				//the next is disc is the player's color
				else if(boardPosition[column+place][row-place] == player)
				{
					//stop checking this direction
					lineEnd = true;

					//this direction is only legal if any flips can be made
					if(flips > 0)
					{
						for(int i = 1; i < place; i++)
						{
							boardPosition[column+i][row-i] = player;
						}
						flipsThisPosition += flips;
					}
				}

				//increment counter
				place++;
			}
		}
		catch(Exception ne) {/*a disc cannot be placed here*/}

		//reset counters
		lineEnd = false;
		flips = 0;
		place = 1;

		//check E positions
		try
		{
			while(lineEnd == false)
			{
				//the next position is empty
				if(boardPosition[column+place][row] == EMPTY)
				{
					//immediately stop checking this direction
					lineEnd = true;
				}
				//the next disc is the enemy's color
				else if(boardPosition[column+place][row] == enemy)
				{
					//increment the potential number of flips
					flips++;
				}
				//the next is disc is the player's color
				else if(boardPosition[column+place][row] == player)
				{
					//stop checking this direction
					lineEnd = true;

					//this direction is only legal if any flips can be made
					if(flips > 0)
					{
						for(int i = 1; i < place; i++)
						{
							boardPosition[column+i][row] = player;
						}
						flipsThisPosition += flips;
					}
				}

				//increment counter
				place++;
			}
		}
		catch(Exception e) {/*a disc cannot be placed here*/}

		//reset counters
		lineEnd = false;
		flips = 0;
		place = 1;

		//check SE positions
		try
		{
			while(lineEnd == false)
			{
				//the next position is empty
				if(boardPosition[column+place][row+place] == EMPTY)
				{
					//immediately stop checking this direction
					lineEnd = true;
				}
				//the next disc is the enemy's color
				else if(boardPosition[column+place][row+place] == enemy)
				{
					//increment the potential number of flips
					flips++;
				}
				//the next is disc is the player's color
				else if(boardPosition[column+place][row+place] == player)
				{
					//stop checking this direction
					lineEnd = true;

					//this direction is only legal if any flips can be made
					if(flips > 0)
					{
						for(int i = 1; i < place; i++)
						{
							boardPosition[column+i][row+i] = player;
						}
						flipsThisPosition += flips;
					}
				}

				//increment counter
				place++;
			}
		}
		catch(Exception se) {/*a disc cannot be placed here*/}

		//reset counters
		lineEnd = false;
		flips = 0;
		place = 1;

		//check S positions
		try
		{
			while(lineEnd == false)
			{
				//the next position is empty
				if(boardPosition[column][row+place] == EMPTY)
				{
					//immediately stop checking this direction
					lineEnd = true;
				}
				//the next disc is the enemy's color
				else if(boardPosition[column][row+place] == enemy)
				{
					//increment the potential number of flips
					flips++;
				}
				//the next is disc is the player's color
				else if(boardPosition[column][row+place] == player)
				{
					//stop checking this direction
					lineEnd = true;

					//this direction is only legal if any flips can be made
					if(flips > 0)
					{
						for(int i = 1; i < place; i++)
						{
							boardPosition[column][row+i] = player;
						}
						flipsThisPosition += flips;
					}
				}

				//increment counter
				place++;
			}
		}
		catch(Exception s) {/*a disc cannot be placed here*/}

		//reset counters
		lineEnd = false;
		flips = 0;
		place = 1;

		//check SW positions
		try
		{
			while(lineEnd == false)
			{
				//the next position is empty
				if(boardPosition[column-place][row+place] == EMPTY)
				{
					//immediately stop checking this direction
					lineEnd = true;
				}
				//the next disc is the enemy's color
				else if(boardPosition[column-place][row+place] == enemy)
				{
					//increment the potential number of flips
					flips++;
				}
				//the next is disc is the player's color
				else if(boardPosition[column-place][row+place] == player)
				{
					//stop checking this direction
					lineEnd = true;

					//this direction is only legal if any flips can be made
					if(flips > 0)
					{
						for(int i = 1; i < place; i++)
						{
							boardPosition[column-i][row+i] = player;
						}
						flipsThisPosition += flips;
					}
				}

				//increment counter
				place++;
			}
		}
		catch(Exception sw) {/*a disc cannot be placed here*/}

		//reset counters
		lineEnd = false;
		flips = 0;
		place = 1;

		//check W positions
		try
		{
			while(lineEnd == false)
			{
				//the next position is empty
				if(boardPosition[column-place][row] == EMPTY)
				{
					//immediately stop checking this direction
					lineEnd = true;
				}
				//the next disc is the enemy's color
				else if(boardPosition[column-place][row] == enemy)
				{
					//increment the potential number of flips
					flips++;
				}
				//the next is disc is the player's color
				else if(boardPosition[column-place][row] == player)
				{
					//stop checking this direction
					lineEnd = true;

					//this direction is only legal if any flips can be made
					if(flips > 0)
					{
						for(int i = 1; i < place; i++)
						{
							boardPosition[column-i][row] = player;
						}
						flipsThisPosition += flips;
					}
				}

				//increment counter
				place++;
			}
		}
		catch(Exception e) {/*a disc cannot be placed here*/}

		//reset counters
		lineEnd = false;
		flips = 0;
		place = 1;

		//check NW positions
		try
		{
			while(lineEnd == false)
			{
				//the next position is empty
				if(boardPosition[column-place][row-place] == EMPTY)
				{
					//immediately stop checking this direction
					lineEnd = true;
				}
				//the next disc is the enemy's color
				else if(boardPosition[column-place][row-place] == enemy)
				{
					//increment the potential number of flips
					flips++;
				}
				//the next is disc is the player's color
				else if(boardPosition[column-place][row-place] == player)
				{
					//stop checking this direction
					lineEnd = true;

					//this direction is only legal if any flips can be made
					if(flips > 0)
					{
						for(int i = 1; i < place; i++)
						{
							boardPosition[column-i][row-i] = player;
						}
						flipsThisPosition += flips;
					}
				}

				//increment counter
				place++;
			}
		}
		catch(Exception e) {/*a disc cannot be placed here*/}
	}
	
	//tally the scores by reading the entire board
	public void tallyScores()
	{
		scoreBlack = 0;
		scoreWhite = 0;
		for(int i = 0; i < BOARD_LENGTH; i++)
		{
			for(int j = 0; j < BOARD_LENGTH; j++)
			{
				//ignore empty squares
				if(boardPosition[i][j] == BLACK)
				{
					scoreBlack++;
				}
				else if(boardPosition[i][j] == WHITE)
				{
					scoreWhite++;
				}
			}
		}
	}
	
	//see who won
	public int determineWinner()
	{
		int winner = EMPTY;
		
		//black won
		if(scoreBlack > scoreWhite)
		{
			winner = BLACK;
		}
		//white won
		else if(scoreWhite > scoreBlack)
		{
			winner = WHITE;
		}
		
		return winner;
	}
	
	//
	//setters and getters
	//
	
	//set and get board positions
	public void setBoardPosition(int[][] input)
	{
		boardPosition = input;
	}
	
	public int[][] getBoardPosition()
	{
		return boardPosition;
	}
	
	//set the value inside any one board position (used by BoardPieces)
	public void setOneBoardPosition(int column, int row, int value)
	{
		boardPosition[column][row] = value;
	}
	
	//set and get legal positions
	public void setLegalPosition(int[][] input)
	{
		legalPosition = input;
	}
	
	public int[][] getLegalPosition()
	{
		return legalPosition;
	}
	
	public int getOneLegalPosition(int column, int row)
	{
		return legalPosition[column][row];
	}
	
	//use flipsThisPosition to have BoardPieces's event handlers start comparing discs
	public int getFlipsThisPosition()
	{
		return flipsThisPosition;
	}
	
	//get the current turn
	public boolean getPlayerTurn()
	{
		return playerTurn;
	}
	
	//set and get black score
	public void setScoreBlack(int b)
	{
		scoreBlack = b;
	}

	//get black score
	public int getScoreBlack()
	{
		return scoreBlack;
	}
	
	//set and get white score
	public void setScoreWhite(int w)
	{
		scoreWhite = w;
	}
	
	public int getScoreWhite()
	{
		return scoreWhite;
	}
}