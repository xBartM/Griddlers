package com.griddlers;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class responsible for managing windows
 * <p>
 * Created by bmielcza on 29.05.17.
 * </p>
 */
class GriddlersView
{
	
	/**
	 * Main frame of view
	 */
	private JFrame mainFrame;
	/**
	 * Everything will be written here
	 */
	private JPanel controlPanel;
	/**
	 * Matrix of buttons
	 */
	private JButton[][] javaButton;
	/**
	 * Array of preloaded icons
	 */
	private ImageIcon[] icon;
	/**
	 * Timer used to count puzzleTime
	 */
	private Timer timer;
	/**
	 * Label used to show time on screen
	 */
	private JLabel timeOnScreen;
	/**
	 * Field storing ID of current puzzle
	 */
	private int presentID;
	
	/**
	 * Constructor preparing essential fields
	 * <p>
	 * In here {@link #icon} gets loaded, {@link #javaButton} prepared,
	 * {@link #timer} set and GUI style defined
	 * </p>
	 */
	GriddlersView()
	{
		prepareIcons();
		prepareButtons();
		prepareTimer();
		prepareGUI();
	}
	
	/**
	 * Loads textures to {@link #icon}
	 */
	private void prepareIcons()
	{
		icon = new ImageIcon[13];
		
		for (int i = 1; i < 10; i++)
			icon[i - 1] = createImageIcon("/resources/" + i + "black32.png", String.valueOf(i));
		
		icon[Icons.WHITE.ordinal()] = createImageIcon("/resources/white32.png", "White");
		icon[Icons.CROSS.ordinal()] = createImageIcon("/resources/cross32.png", "Cross");
		icon[Icons.BLACK.ordinal()] = createImageIcon("/resources/black32.png", "Black");
		icon[Icons.PWHITE.ordinal()] = createImageIcon("/resources/plainWhite32.png", "PWhite");
	}
	
	/**
	 * Creates and sets essential parameters of buttons
	 */
	private void prepareButtons()
	{
		javaButton = new JButton[14][14];
		
		for (int x = 0; x < 14; x++)
			for (int y = 0; y < 14; y++)
			{
				javaButton[x][y] = new JButton(icon[Icons.PWHITE.ordinal()]);
				javaButton[x][y].setMargin(new Insets(0, 0, 0, 0));
				javaButton[x][y].setBorder(null);
			}
	}
	
	/**
	 * Prepares {@link #timer} and adds {@link #timeOnScreen} to {@link #controlPanel}
	 */
	private void prepareTimer()
	{
		timer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				char[] chr = timeOnScreen.getText().toCharArray();
				char[] result = new char[5];
				
				for (int i = 4; i > -1; i--)// now they are in int format
				{
					chr[i] -= '0';            // we don't care about ':' getting changed
					result[i] = chr[i];
				}
				
				result[4] = (char) (chr[4] + 1);
				
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
				
				timeOnScreen.setText(new String(result));
			}
		});
	}
	
	/**
	 * Sets size, layout, visible etc. to {@link #mainFrame} and {@link #controlPanel}
	 */
	private void prepareGUI()
	{
		mainFrame = new JFrame("Griddlers :D");
		
		mainFrame.setSize(512, 512);
		mainFrame.setLayout(new GridLayout(1, 0));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		controlPanel.setBackground(Color.WHITE);
		
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
	}
	
	/**
	 * Draws a game window
	 *
	 * @param ID           ID of puzzle to draw
	 * @param puzzleName   name of puzzle to draw
	 * @param puzzleWidth  width of puzzle to draw
	 * @param puzzleHeight height of puzzle to draw
	 * @param puzzleTime   best time achieved in puzzle to draw
	 * @param puzzleArray  array of 0 and 1 of puzzle to draw
	 */
	void drawGame(int ID, String puzzleName, int puzzleWidth, int puzzleHeight, String puzzleTime, int[][] puzzleArray)
	{
		mainFrame.setTitle("Griddlers :D - " + puzzleName + " " + puzzleWidth + "x" + puzzleHeight + " Best time: " + puzzleTime);
		
		controlPanel.removeAll(); // remove everything so we can draw new stuff
		
		presentID = ID;
		setupButtons(puzzleWidth, puzzleHeight);
		setupInstructions(puzzleWidth, puzzleHeight, puzzleArray);
		setupTimer();
	}
	
	/**
	 * Draws (not yet) clickable {@link #javaButton}s
	 *
	 * @param width  width of a puzzle
	 * @param height height of a puzzle
	 */
	private void setupButtons(int width, int height)
	{
		GridBagConstraints constr = new GridBagConstraints();
		
		for (int x = width + 4; x > 4; x--)
		{
			for (int y = height + 4; y > 4; y--)
			{
				javaButton[x][y].setIcon(icon[Icons.WHITE.ordinal()]);
				constr.gridx = x;
				constr.gridy = y;
				controlPanel.add(javaButton[x][y], constr);
			}
		}
	}
	
	/**
	 * Sets up border instructions
	 *
	 * @param width  width of a puzzle
	 * @param height height of a puzzle
	 * @param array  array of a puzzle
	 */
	private void setupInstructions(int width, int height, int[][] array)
	{
		GridBagConstraints constr = new GridBagConstraints();
		int[] instructions = new int[5];
		int pointer = 0;
		
		// vertically
		for (int x = width - 1; x > -1; x--)
		{
			for (int y = height - 1; y > -1; y--)
			{
				if (array[x][y] == 1)
					instructions[pointer]++;
				else if (instructions[pointer] != 0)
					pointer++;
			}
			
			for (int z = 4; z > -1; z--)
			{
				if (instructions[4 - z] != 0)
				{
					javaButton[x + 5][z].setIcon(icon[instructions[4 - z] - 1]);
					constr.gridx = x + 5;
					constr.gridy = z;
					controlPanel.add(javaButton[x + 5][z], constr);
				} else
					break;
			}
			instructions = new int[5];
			pointer = 0;
		}
		
		// horizontally
		for (int y = height - 1; y > -1; y--)
		{
			for (int x = width - 1; x > -1; x--)
			{
				if (array[x][y] == 1)
					instructions[pointer]++;
				else if (instructions[pointer] != 0)
					pointer++;
			}
			
			for (int z = 4; z > -1; z--)
			{
				if (instructions[4 - z] != 0)
				{
					javaButton[z][y + 5].setIcon(icon[instructions[4 - z] - 1]);
					constr.gridx = z;
					constr.gridy = y + 5;
					controlPanel.add(javaButton[z][y + 5], constr);
				} else
					break;
			}
			instructions = new int[5];
			pointer = 0;
		}
	}
	
	/**
	 * Draws a {@link #timeOnScreen} and starts {@link #timer}
	 */
	private void setupTimer()
	{
		GridBagConstraints constr = new GridBagConstraints();
		
		timeOnScreen = new JLabel("00:00");
		timeOnScreen.setFont(new Font("Serif", Font.PLAIN, 24));
		constr.gridx = 1;
		constr.gridy = 2;
		controlPanel.add(timeOnScreen, constr);
		
		timer.start();
	}
	
	/**
	 * Adds action listener to all of possible game buttons
	 *
	 * @param listenForButton ActionListener defined in GriddlersController
	 */
	void addButtonListeners(ActionListener listenForButton)
	{
		for (int x = 5; x < 14; x++)
		{
			for (int y = 5; y < 14; y++)
			{
				javaButton[x][y].addActionListener(listenForButton);
			}
		}
	}
	
	/**
	 * Simple getter of present puzzle array on screen
	 *
	 * @return array visible on screen
	 */
	int[][] getArray()
	{
		int[][] arr = new int[9][9];
		
		for (int x = 0; x < 9; x++)
			for (int y = 0; y < 9; y++)
				if (javaButton[x + 5][y + 5].getIcon() == icon[Icons.BLACK.ordinal()])
					arr[x][y] = 1;
		
		return arr;
	}
	
	/**
	 * Simple getter of text in {@link #timeOnScreen}
	 *
	 * @return text in {@link #timeOnScreen}
	 */
	String getTime()
	{
		return timeOnScreen.getText();
	}
	
	/**
	 * Simple getter of {@link #icon}
	 *
	 * @return {@link #icon}
	 */
	Icon[] getIconArray()
	{
		return icon;
	}
	
	/**
	 * Simple getter of {@link #presentID}
	 *
	 * @return {@link #presentID}
	 */
	int getPresentID()
	{
		return presentID;
	}
	
	/**
	 * Checks whether the {@link #timer} is running
	 *
	 * @return true if {@link #timer} is running, otherwise false
	 */
	boolean isTimerRunning()
	{
		return timer.isRunning();
	}
	
	/**
	 * Procedure that stops a {@link #timer}
	 */
	void stopActivity() // when puzzles are finished
	{
		timer.stop();
	}
	
	/**
	 * Gets resource
	 *
	 * @param path        path to the resource
	 * @param description short name for a resource
	 * @return usable icon
	 */
	@Nullable
	private static ImageIcon createImageIcon(String path, String description)
	{
		java.net.URL imgURL = GriddlersView.class.getResource(path);
		
		if (imgURL != null)
		{
			return new ImageIcon(imgURL, description);
		} else
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
}

/**
 * Enum type for "easier" navigation through icons (mostly useless)
 */
enum Icons
{
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
	SEVEN,
	EIGHT,
	NINE,
	WHITE,
	CROSS,
	BLACK,
	PWHITE
}
