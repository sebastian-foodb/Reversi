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

import java.util.LinkedList;

//computer opponent that maximizes the number of flipped discs every turn
public class OpponentAI2 extends OpponentGeneric
{
	//CONSTANTS
	private final int BOARD_LENGTH = 8;
	private final int EMPTY = 0;
	private final int BLACK = 1;
	private final int WHITE = 2;
	private final int BOTH = 3;

	public OpponentAI2()
	{
		//initializations
		legal = new int[BOARD_LENGTH][BOARD_LENGTH];
		decision = new int[2];

		//ai goes second
		setPlayerTurn(false);
	}

	@Override
	public void behavior()
	{
		placesC = new LinkedList<Integer>();
		placesR = new LinkedList<Integer>();
		
		//linked list for if there's a multiway tie between positions
		LinkedList<Integer> placesTie = new LinkedList<Integer>();

		//go through the legal positions and see where
		for(int i = 0; i < BOARD_LENGTH; i++)
		{
			for(int j = 0; j < BOARD_LENGTH; j++)
			{
				if((legal[i][j] == WHITE)||(legal[i][j] == BOTH))
				{
					placesC.add(i);
					placesR.add(j);
				}
			}
		}

		//counters for determining the position that'll yield the most captures
		int chosenIndex = 0, mostFlips = 0;

		//counters for individual directions
		boolean lineEnd;
		int place;
		int flipsThisPosition = 0;
		int[][] flips = new int[placesC.size()][BOARD_LENGTH];

		//count the spaces between
		for(int i = 0; i < placesC.size(); i++)
		{
			//legal[placesC.get(i)][placesR.get(i)]
			//board[placesC.get(i)][placesR.get(i)]

			//initialize counters
			lineEnd = false;
			place = 1;
			flipsThisPosition = 0;

			//check N positions
			try
			{	
				while(lineEnd == false)
				{
					if(board[placesC.get(i)][placesR.get(i)-place] == EMPTY)
					{
						lineEnd = true;
						flips[i][0] = 0;
					}
					else if(board[placesC.get(i)][placesR.get(i)-place] == BLACK)
					{
						flips[i][0]++;
					}
					else if(board[placesC.get(i)][placesR.get(i)-place] == WHITE)
					{
						lineEnd = true;
					}
					
					//increment counter
					place++;
				}
			}
			catch(Exception n) {flips[i][0] = 0;}
			
			//reset counters
			lineEnd = false;
			place = 1;

			//check NE positions
			try
			{
				while(lineEnd == false)
				{
					if(board[placesC.get(i)+place][placesR.get(i)-place] == EMPTY)
					{
						lineEnd = true;
						flips[i][1] = 0;
					}
					else if(board[placesC.get(i)+place][placesR.get(i)-place] == BLACK)
					{
						flips[i][1]++;
					}
					else if(board[placesC.get(i)+place][placesR.get(i)-place] == WHITE)
					{
						lineEnd = true;
					}

					//increment counter
					place++;
				}
			}
			catch(Exception ne) {flips[i][1] = 0;}
			
			//reset counters
			lineEnd = false;
			place = 1;
			
			//check E positions
			try
			{
				while(lineEnd == false)
				{
					if(board[placesC.get(i)+place][placesR.get(i)] == EMPTY)
					{
						lineEnd = true;
						flips[i][2] = 0;
					}
					else if(board[placesC.get(i)+place][placesR.get(i)] == BLACK)
					{
						flips[i][2]++;
					}
					else if(board[placesC.get(i)+place][placesR.get(i)] == WHITE)
					{
						//establish a line
						lineEnd = true;
					}

					//increment counter
					place++;
				}
			}
			catch(Exception e) {flips[i][2] = 0;}
			
			//reset counters
			lineEnd = false;
			place = 1;
			
			//check SE positions
			try
			{
				while(lineEnd == false)
				{
					if(board[placesC.get(i)+place][placesR.get(i)+place] == EMPTY)
					{
						lineEnd = true;
						flips[i][3] = 0;
					}
					else if(board[placesC.get(i)+place][placesR.get(i)+place] == BLACK)
					{
						flips[i][3]++;
					}
					else if(board[placesC.get(i)+place][placesR.get(i)+place] == WHITE)
					{
						lineEnd = true;
					}

					//increment counter
					place++;
				}
			}
			catch(Exception se) {flips[i][3] = 0;}
			
			//reset counters
			lineEnd = false;
			place = 1;
			
			//check S positions
			try
			{
				while(lineEnd == false)
				{
					if(board[placesC.get(i)][placesR.get(i)+place] == EMPTY)
					{
						lineEnd = true;
						flips[i][4] = 0;
					}
					else if(board[placesC.get(i)][placesR.get(i)+place] == BLACK)
					{
						flips[i][4]++;
					}
					else if(board[placesC.get(i)][placesR.get(i)+place] == WHITE)
					{
						lineEnd = true;
					}

					//increment counter
					place++;
				}
			}
			catch(Exception s) {flips[i][4] = 0;}
			
			//reset counters
			lineEnd = false;
			place = 1;
			
			//check SW positions
			try
			{
				while(lineEnd == false)
				{
					if(board[placesC.get(i)-place][placesR.get(i)+place] == EMPTY)
					{
						lineEnd = true;
						flips[i][5] = 0;
					}
					else if(board[placesC.get(i)-place][placesR.get(i)+place] == BLACK)
					{
						flips[i][5]++;
					}
					else if(board[placesC.get(i)-place][placesR.get(i)+place] == WHITE)
					{
						lineEnd = true;
					}

					//increment counter
					place++;
				}
			}
			catch(Exception sw) {flips[i][5] = 0;}
			
			//reset counters
			lineEnd = false;
			place = 1;
			
			//check W positions
			try
			{
				while(lineEnd == false)
				{
					if(board[placesC.get(i)-place][placesR.get(i)] == EMPTY)
					{
						lineEnd = true;
						flips[i][6] = 0;
					}
					else if(board[placesC.get(i)-place][placesR.get(i)] == BLACK)
					{
						flips[i][6]++;
					}
					else if(board[placesC.get(i)-place][placesR.get(i)] == WHITE)
					{
						lineEnd = true;
					}

					//increment counter
					place++;
				}
			}
			catch(Exception w) {flips[i][6] = 0;}
			
			//check NW positions
			try
			{
				while(lineEnd == false)
				{
					if(board[placesC.get(i)-place][placesR.get(i)-place] == EMPTY)
					{
						lineEnd = true;
						flips[i][7] = 0;
					}
					else if(board[placesC.get(i)-place][placesR.get(i)-place] == BLACK)
					{
						flips[i][7]++;
					}
					else if(board[placesC.get(i)-place][placesR.get(i)-place] == WHITE)
					{
						lineEnd = true;
					}

					//increment counter
					place++;
				}
			}
			catch(Exception nw) {flips[i][7] = 0;}
			
			//count up the total flips from this position
			for(int j = 0; j < BOARD_LENGTH; j++)
			{
				flipsThisPosition += flips[i][j];
			}
			
			//determine if this position is where the most flips will occur
			//initialize using the first position checked
			if(i == 0)
			{
				mostFlips = flipsThisPosition;
				chosenIndex = i;
			}
			//there is a better position
			else if((i > 0)&&(flipsThisPosition > mostFlips))
			{
				mostFlips = flipsThisPosition;
				chosenIndex = i;
			}
			//multiple positions with the same potential amount of flips
			else if((i > 0)&&(flipsThisPosition == mostFlips))
			{
				if(placesTie.size() == 0)
				{
					placesTie.add(chosenIndex);
					placesTie.add(chosenIndex);
					placesTie.add(i);
					placesTie.add(i);
				}
				else
				{
					placesTie.add(i);
					placesTie.add(i);
				}
			}
		}
		
		//if there's a tie, randomly choose one of the positions
		if(placesTie.size() > 0)
		{
			chosenIndex = (int) Math.random()*placesTie.size();
		}
		
		setDecidedPosition(chosenIndex);
	}
}