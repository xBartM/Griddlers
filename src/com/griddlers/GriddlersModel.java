package com.griddlers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class responsible for managing data
 * <p>
 * Created by bmielcza on 29.05.17.
 * </p>
 */
class GriddlersModel
{
	
	/**
	 * Number of all puzzles
	 */
	private int puzzleCount;
	/**
	 * Number of all done puzzles
	 */
	private int doneCount;
	/**
	 * Amount of all time wasted
	 */
	private String overallTime;
	
	/**
	 * Array of names of puzzles[ID]
	 */
	private String[] puzzleName;
	/**
	 * Array of widths of puzzles[ID]
	 */
	private int[] puzzleWidth;
	/**
	 * Array of heights of puzzles[ID]
	 */
	private int[] puzzleHeight;
	/**
	 * Array of times of puzzles[ID]
	 */
	private String[] puzzleTime;
	/**
	 * Array of arrays of puzzles "maps"[ID]
	 */
	private int[][][] puzzleArray;
	
	
	/**
	 * Basic constructor for GriddlersModel
	 * <p>
	 * This constructor is used to initialize arrays.
	 * It also loads data from file to memory.
	 * </p>
	 */
	GriddlersModel()
	{
		puzzleName = new String[100];
		puzzleWidth = new int[100];
		puzzleHeight = new int[100];
		puzzleTime = new String[100];
		puzzleArray = new int[100][9][9];
		
		try
		{
			updateVars();
		} catch (IOException ex)
		{
			System.err.println("Couldn't find data.txt");
		}
	}
	
	
	/**
	 * Moves data from memory to file (data.txt)
	 *
	 * @throws IOException
	 */
	void updateData() throws IOException    // update data.txt
	{
		FileWriter out = null;
		
		try
		{
			out = new FileWriter("data.txt");
			
			if (puzzleCount < 10)
			{
				out.write("0" + puzzleCount);
			} else
			{
				out.write(puzzleCount / 10 + '0');
				out.write(puzzleCount % 10 + '0');
			}
			out.write('\n');
			
			if (doneCount < 10)
			{
				out.write("0" + doneCount);
			} else
			{
				out.write(doneCount / 10 + '0');
				out.write(doneCount % 10 + '0');
			}
			out.write('\n');
			
			out.write(overallTime.toCharArray());
			out.write('\n');
			
			for (int id = 0; id < puzzleCount; id++)
			{
				for (int i = 0; i < 31; i++)
					if (puzzleName[id].toCharArray()[i] != '\n')
						out.write(puzzleName[id].toCharArray()[i]);
					else
						break;
				out.write('\n');
				
				out.write(puzzleWidth[id] + '0');
				out.write('x');
				out.write(puzzleHeight[id] + '0');
				out.write('\n');
				
				out.write(puzzleTime[id].toCharArray());
				out.write('\n');
				
				for (int j = 0; j < 9; j++)
				{
					for (int k = 0; k < 9; k++)
					{
						out.write(puzzleArray[id][j][k] + '0');
					}
					out.write('\n');
				}
			}
		} finally
		{
			if (out != null)
			{
				out.close();
			}
		}
	}
	
	/**
	 * Moves data from file (data.txt) to memory
	 *
	 * @throws IOException
	 */
	void updateVars() throws IOException    // update variables
	{
		FileReader in = null;
		
		char[] bufferTime = new char[5];    // buffer for time String
		char[] bufferName = new char[32];    // buffer for name Strings
		
		try
		{
			in = new FileReader("data.txt");
			
			puzzleCount = in.read() - '0';
			puzzleCount = 10 * puzzleCount + in.read() - '0';
			while (in.read() != '\n') ;    // go to the new line
			
			doneCount = in.read() - '0';
			doneCount = 10 * doneCount + in.read() - '0';
			while (in.read() != '\n') ;    // go to the new line
			
			in.read(bufferTime);
			overallTime = new String(bufferTime);
			while (in.read() != '\n') ;    // go to the new line
			
			for (int id = 0; id < puzzleCount; id++)
			{
				for (int i = 0; (bufferName[i] = (char) in.read()) != '\n'; i++)
				{
					if (i == 31)
						break;
				}
				for (int i = 0; i < 32; i++)
				{
					if (bufferName[i] == '\n')
						bufferName[i] = 0;
				}
				puzzleName[id] = new String(bufferName);
				
				puzzleWidth[id] = in.read() - '0';
				in.read();    // skip 'x'
				puzzleHeight[id] = in.read() - '0';
				while (in.read() != '\n') ;    // go to the new line
				
				in.read(bufferTime);
				puzzleTime[id] = new String(bufferTime);
				while (in.read() != '\n') ;    // go to the new line
				
				for (int j = 0; j < 9; j++)
				{
					for (int k = 0; k < 9; k++)
					{
						puzzleArray[id][j][k] = in.read() - '0';
					}
					while (in.read() != '\n') ;    // go to the new line
				}
			}
		} finally
		{
			if (in != null)
			{
				in.close();
			}
		}
	}
	
	/**
	 * Simple getter of {@link #puzzleCount}
	 *
	 * @return value of {@link #puzzleCount}
	 */
	int getPuzzleCount()            // get number of puzzles
	{
		return puzzleCount;
	}
	
	/**
	 * Increments {@link #puzzleCount} by 1
	 */
	void addPuzzleCount()            // increment number of puzzles
	{
		puzzleCount++;
	}
	
	/**
	 * Simple getter of {@link #doneCount}
	 *
	 * @return value of {@link #doneCount}
	 */
	int getDoneCount()                // get number of done puzzles
	{
		return doneCount;
	}
	
	/**
	 * Increments {@link #doneCount} by 1
	 */
	void addDoneCount()                // increment number of done puzzles
	{
		doneCount++;
	}
	
	/**
	 * Simple getter of {@link #overallTime}
	 *
	 * @return value of {@link #overallTime}
	 */
	String getOverallTime()            // get amount of all time wasted
	{
		return overallTime;
	}
	
	/**
	 * Adds amount of time to {@link #overallTime}
	 *
	 * @param time amount of time to add
	 */
	void addOverallTime(String time)// add to overall time wasted
	{
		char[] chr1 = time.toCharArray();
		char[] chr2 = overallTime.toCharArray();
		char[] result = new char[5];
		
		for (int i = 4; i > -1; i--)// now they are in int format
		{
			chr1[i] -= '0';            // we don't care about ':' getting changed
			chr2[i] -= '0';
			result[i] = (char) (chr1[i] + chr2[i]);    // rough sum of 2 (int)chars
		}
		result[2] >>= 1;            // div former ':' by 2 back to (char)10
		
		if (result[4] > 9)            // take care of "carry"
		{
			result[3] = (char) (result[3] + 1);
			result[4] = (char) (result[4] - 10);
		}
		
		if (result[3] > 5)            // take care of "carry"
		{
			result[1] = (char) (result[1] + 1);
			result[3] = (char) (result[3] - 6);
		}
		
		if (result[1] > 9)            // take care of "carry"
		{
			result[0] = (char) (result[0] + 1);
			result[1] = (char) (result[1] - 10);
		}
		
		for (int i = 0; i < 5; i++)    // format to ascii numbers again
			result[i] += '0';
		
		overallTime = new String(result);
	}
	
	/**
	 * Simple getter of {@link #puzzleName}
	 *
	 * @return value of {@link #puzzleName}
	 */
	String getPuzzleName(int ID)    // get name of puzzle[ID]
	{
		return puzzleName[ID];
	}
	
	/**
	 * Sets {@link #puzzleName}[ID] to name
	 *
	 * @param ID   ID of a puzzle to change
	 * @param name name to change to
	 */
	void setPuzzleName(int ID, String name)    // set name of puzzle[ID] to 'name'
	{
		puzzleName[ID] = name;
	}
	
	/**
	 * Simple getter of {@link #puzzleWidth}
	 *
	 * @return value of {@link #puzzleWidth}
	 */
	int getPuzzleWidth(int ID)        // get width of puzzle[ID]
	{
		return puzzleWidth[ID];
	}
	
	/**
	 * Sets {@link #puzzleWidth}[ID] to width
	 *
	 * @param ID    ID of a puzzle to change
	 * @param width width to change to
	 */
	void setPuzzleWidth(int ID, int width)    // set width of puzzle[ID]
	{
		puzzleWidth[ID] = width;
	}
	
	/**
	 * Simple getter of {@link #puzzleHeight}
	 *
	 * @return value of {@link #puzzleHeight}
	 */
	int getPuzzleHeight(int ID)        // get height of puzzle[ID]
	{
		return puzzleHeight[ID];
	}
	
	/**
	 * Sets {@link #puzzleHeight}[ID] to height
	 *
	 * @param ID     ID of a puzzle to change
	 * @param height height to change to
	 */
	void setPuzzleHeight(int ID, int height)    // set height of puzzle[ID]
	{
		puzzleHeight[ID] = height;
	}
	
	/**
	 * Simple getter of {@link #puzzleTime}
	 *
	 * @return value of {@link #puzzleTime}
	 */
	String getPuzzleTime(int ID)    // get time of puzzle[ID]
	{
		return puzzleTime[ID];
	}
	
	/**
	 * Sets {@link #puzzleTime} to time
	 *
	 * @param ID   ID of a puzzle to change
	 * @param time time to change to
	 */
	void setPuzzleTime(int ID, String time)    // set time of puzzle[ID]
	{
		puzzleTime[ID] = time;
	}
	
	/**
	 * Simple getter of {@link #puzzleArray}
	 *
	 * @return value of {@link #puzzleArray}
	 */
	int[][] getPuzzleArray(int ID)    // get puzzle array[ID] and put into array[][]
	{
		int[][] array = new int[9][9];
		for (int i = 8; i > -1; i--)
		{
			for (int j = 8; j > -1; j--)
			{
				array[i][j] = puzzleArray[ID][i][j];
			}
		}
		return array;
	}
	
	/**
	 * Sets {@link #puzzleArray} to array[][]
	 *
	 * @param ID    ID of a puzzle to change
	 * @param array array to change to
	 */
	void setPuzzleArray(int ID, int[][] array)
	{
		for (int i = array.length - 1; i > -1; i--)
		{
			for (int j = array[0].length - 1; j > -1; j--)
			{
				puzzleArray[ID][i][j] = array[i][j];
			}
		}
	}
	
	/**
	 * Compares two arrays
	 *
	 * @param ID    ID of a puzzle to compare
	 * @param array array to compare to
	 * @return true if arrays are equal, otherwise false
	 */
	boolean compareArrays(int ID, int[][] array)
	{
		for (int x = 0; x < 9; x++)
			for (int y = 0; y < 9; y++)
				if (puzzleArray[ID][x][y] != array[x][y])
					return false;
		return true;
	}
}
