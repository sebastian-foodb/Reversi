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

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ReversiGUI extends Application
{
	//get the pane from BoardPieces
	private BoardPieces bp;
	
	//constructor
	public ReversiGUI() throws Exception
	{
		//instantiate board pane
		bp = new BoardPieces();
	}
	
	@Override
	public void start(Stage reversiStage)
	{
		//fix dimensions
		reversiStage.setResizable(false);
		
		Scene reversiScene = new Scene(bp.getReversiWindow(),
				bp.getDimensions()[0],bp.getDimensions()[1]);
		reversiStage.setScene(reversiScene);
		reversiStage.setTitle("Reversi (not to be confused with any other trademarked products)");
		reversiStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}