package gui;

import javax.swing.JFrame;

import controller.TController;
import core.Grid;

public class TFrame extends JFrame {
	
	private Grid grid;
	
	public static TFrame Make () {
		TFrame tframe = new TFrame();
		tframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TPanel tpanel = new TPanel(tframe);
		tframe.add(tpanel);
		tframe.pack();
		tframe.setVisible(true);
		return tframe;
	}
	
	public void setController (TController c) {
		this.addKeyListener(c);
	}

	public void setGrid (Grid g) {
		this.grid = g;
	}
	
	public Grid getGrid() {
		return this.grid;
	}
	
}
