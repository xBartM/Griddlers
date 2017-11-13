package com.griddlers;

public class Main
{
	
	public static void main(String[] args)
	{
		GriddlersView theView = new GriddlersView();
		GriddlersModel theModel = new GriddlersModel();
		
		GriddlersController theController = new GriddlersController(theView, theModel);
	}
}
