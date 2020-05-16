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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ColorScheme
{
	//CONSTANTS
	//indices of board elements with adjustable colors
	private final int BACKG = 0;
	private final int LABEL = 1;
	private final int BOARD = 2;
	private final int BLACK = 3;
	private final int WHITE = 4;
	//hex codes for colors users can choose from
	private final String[][] hexColorList =
		{
				{"#000000","Black"},
				{"#ffffff","White"},
				{"#ff0000","Red"},
				{"#ff8000","Orange"},
				{"#ffff00","Yellow"},
				{"#008000","Green"},
				{"#0000ff","Blue"},
				{"#800080","Purple"},
				{"#00ff00","Lime Green"},
				{"#008080","Teal"},
				{"#ff1493","Pink"},
				{"#996633","Brown"},
				{"#664422","Dark Brown"},
				{"#daa520","Goldenrod"},
				{"#420420","Nice"}
		};

	//user-defined colors
	private String[] colors;

	//Save file for colors
	File scheme;

	//constructor
	public ColorScheme()
	{
		scheme = new File("colorscheme.txt");
		
		colors = new String[5];

		//load the color scheme
		loadScheme();
	}

	//save a color scheme
	public void saveScheme()
	{
		try
		{
			PrintWriter pw = new PrintWriter(scheme);

			//write each element and its color to colorscheme.txt
			pw.write("backg "+colors[BACKG]+"\n"
					+"label "+colors[LABEL]+"\n"
					+"board "+colors[BOARD]+"\n"
					+"black "+colors[BLACK]+"\n"
					+"white "+colors[WHITE]);

			//close the writer
			pw.close();
		}
		catch(FileNotFoundException ColorBlind)
		{
			scheme = new File("colorscheme.txt");
			saveScheme();
		}
	}
	
	//load the saved color scheme on startup
	public void loadScheme()
	{
		try
		{
			FileReader fr = new FileReader(scheme);
			BufferedReader br = new BufferedReader(fr);
			String line;
			String[] ls;

			//read each value and store its corresponding color
			while((line = br.readLine()) != null)
			{
				ls = line.split(" ");
				if(ls[0].contentEquals("backg"))
				{
					colors[BACKG] = ls[1];
				}
				else if(ls[0].contentEquals("label"))
				{
					colors[LABEL] = ls[1];
				}
				else if(ls[0].contentEquals("board"))
				{
					colors[BOARD] = ls[1];
				}
				else if(ls[0].contentEquals("black"))
				{
					colors[BLACK] = ls[1];
				}
				else if(ls[0].contentEquals("white"))
				{
					colors[WHITE] = ls[1];
				}
			}
			fr.close();
			br.close();
		}
		//write the default color scheme if an existing one cannot be found
		catch(IOException ColorBlind)
		{
			writeDefaultScheme();
		}
	}
	
	//create the generic
	public void writeDefaultScheme()
	{
		//default color scheme
		colors = new String[]
				{
						hexColorList[11][0],
						hexColorList[8][0],
						hexColorList[5][0],
						hexColorList[0][0],
						hexColorList[1][0]
				};

		//save the default scheme to colorscheme.txt
		saveScheme();
	}

	//
	//setters and getters
	//

	//get the hex color list
	public String[][] getHexColorList()
	{
		return hexColorList;
	}

	//set and get the list of colors
	public void setOneColor(int index, String set)
	{
		colors[index] = set;
	}

	public String[] getColors()
	{
		return colors;
	}
}