package com.griddlers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Class responsible for controlling GriddlersView and GriddlersModel
 * <p>
 * Created by bmielcza on 29.05.17.
 * </p>
 */
class GriddlersController
{
	
	/**
	 * View field
	 */
	private GriddlersView theView;
	/**
	 * Model field
	 */
	private GriddlersModel theModel;
	
	/**
	 * Constructor initializing {@link #GriddlersController}
	 *
	 * @param passedView  GriddlersView to write to {@link #theView}
	 * @param passedModel GriddlersModel to write to {@link #theModel}
	 */
	GriddlersController(GriddlersView passedView, GriddlersModel passedModel)
	{
		theView = passedView;
		theModel = passedModel;
		
		theView.addButtonListeners(new ButtonListener());
		
		//drawGame(0);
		drawGame(1);
	}
	
	/**
	 * Method made to ease a process of calling drawGame from GriddlersView
	 *
	 * @param ID ID of a game You want do draw
	 */
	private void drawGame(int ID)
	{
		theView.drawGame(
				ID,
				theModel.getPuzzleName(ID),
				theModel.getPuzzleWidth(ID),
				theModel.getPuzzleHeight(ID),
				theModel.getPuzzleTime(ID),
				theModel.getPuzzleArray(ID));
	}
	
	/**
	 * An ActionListener class that is also responsible for some of the decision making
	 */
	class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (theView.isTimerRunning())
			{
				if (((JButton) e.getSource()).getIcon() == theView.getIconArray()[Icons.WHITE.ordinal()])
					((JButton) e.getSource()).setIcon(theView.getIconArray()[Icons.BLACK.ordinal()]);
				else if (((JButton) e.getSource()).getIcon() == theView.getIconArray()[Icons.BLACK.ordinal()])
					((JButton) e.getSource()).setIcon(theView.getIconArray()[Icons.CROSS.ordinal()]);
				else
					((JButton) e.getSource()).setIcon(theView.getIconArray()[Icons.WHITE.ordinal()]);
				
				if (theModel.compareArrays(theView.getPresentID(), theView.getArray()))
				{
					theView.stopActivity();
					if (theModel.getPuzzleTime(theView.getPresentID()).compareTo("00:00") == 0)
					{
						theModel.addDoneCount();
						theModel.setPuzzleTime(theView.getPresentID(), theView.getTime());
					}
					if (theModel.getPuzzleTime(theView.getPresentID()).compareTo(theView.getTime()) > 0)
						theModel.setPuzzleTime(theView.getPresentID(), theView.getTime());
					theModel.addOverallTime(theView.getTime());
					
					try
					{
						theModel.updateData();
					} catch (IOException ex)
					{
						System.err.println("Couldn't find data.txt");
					}
				}
			}
		}
	}
}
