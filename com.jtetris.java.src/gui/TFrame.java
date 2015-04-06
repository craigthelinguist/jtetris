package gui;

import javax.swing.JFrame;

import controller.TController;
import core.Grid;

public class TFrame extends JFrame {
	
	private Grid grid;
	private TController controller;
	
	public static TFrame Make () {
		TFrame tframe = new TFrame();
		TController controller = new TController(tframe);
		tframe.grid = new Grid();
		controller.setGrid(tframe.grid);
		tframe.addKeyListener(controller);
		tframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tframe.grid.newTetris();
		TPanel tpanel = new TPanel(tframe);
		tframe.add(tpanel);
		tframe.pack();
		tframe.setVisible(true);
		return tframe;
	}

	public Grid getGrid() {
		return this.grid;
	}
	
}
