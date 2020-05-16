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

//generic data each opponent type needs
public class OpponentGeneric
{
	//CONSTANTS
	private final int BOARD_LENGTH = 8;
	private final int EMPTY = 0;
	private final int BLACK = 1;
	private final int WHITE = 2;
	private final int BOTH = 3;

	//turn order [true black,false white]
	private boolean playerTurn;

	//board positions that ai2 will use to make decisions
	int[][] board;

	//legal positions that ai1 will use to make decisions
	int[][] legal;

	//position that the opponent chose to play
	int[] decision;

	//list of legal columns and rows to place discs
	LinkedList<Integer> placesC;
	LinkedList<Integer> placesR;

	//change turns from black to white or vice versa
	public void changeTurn()
	{
		playerTurn = !playerTurn;
		
		//if it's player 2's turn, let them carry out their behavior
		if(!playerTurn)
		{
			behavior();
		}
	}
	
	//dummy method for the opponent's behavior
	public void behavior() {/**/}
	
	//
	//setters and getters
	//
	
	//set and get player turn
	public void setPlayerTurn(boolean tf)
	{
		playerTurn = tf;
	}
	
	public boolean getPlayerTurn()
	{
		return playerTurn;
	}
	
	//set board positions from GridSystem
	public void setBoardPosition(int[][] board)
	{
		this.board = board;
	}
	
	//set legal positions from GridSystem
	public void setLegalPosition(int[][] legal)
	{
		this.legal = legal;
	}
	
	//set and get the decided position
	public void setDecidedPosition(int chosenIndex)
	{
		try
		{
			decision[0] = placesC.get(chosenIndex);
			decision[1] = placesR.get(chosenIndex);
		}
		catch(IndexOutOfBoundsException ThrownDuringCustomSetup) {/*purposefully do nothing*/}
	}
	
	public int[] getDecidedPosition()
	{
		return decision;
	}
}