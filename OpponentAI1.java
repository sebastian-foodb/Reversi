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

//computer opponent that makes random legal moves every turn
public class OpponentAI1 extends OpponentGeneric
{
	//CONSTANTS
	private final int BOARD_LENGTH = 8;
	private final int EMPTY = 0;
	private final int BLACK = 1;
	private final int WHITE = 2;
	private final int BOTH = 3;
	
	//constructor
	public OpponentAI1()
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
		
		//randomly decide which position to place the disc
		int chosenIndex = (int) Math.random()*placesC.size();
		setDecidedPosition(chosenIndex);
	}
}