package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import controller.TController;
import core.Game;
import core.Grid;
import core.Tetris;

public class TFrame extends JFrame {

	private Grid grid;
	private TController controller;
	private Game game;

	// components
	private TPanel tpanel;
	private SidePanel scorePanel;

	public static TFrame Make () {
		TFrame tframe = new TFrame();
		tframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tframe.tpanel = new TPanel(tframe);
		tframe.scorePanel = SidePanel.MakeScorePanel(tframe);

		tframe.add(tframe.tpanel, BorderLayout.WEST);
		tframe.add(tframe.scorePanel, BorderLayout.EAST);
		tframe.pack();
		tframe.setVisible(true);
		
		// This is needed to correctly focus the panel.
		tframe.setEnabled(true);
		
		return tframe;
	}

	public void setGame (Game g) {
		this.game = g;
	}

	public Game getGame () {
		return this.game;
	}

	protected Dimension getDimensions () {
		return this.tpanel.getPreferredSize();
	}

	protected Tetris getStorageTetris () {
		return this.grid.getStoredTetris();
	}
	
	public void setController (TController c) {
		this.controller = c;
		this.tpanel.addKeyListener(c);
	}

	public void setGrid (Grid g) {
		this.grid = g;
	}

	public Grid getGrid() {
		return this.grid;
	}

	public Tetris getNextTetris() {
		return grid.getNextTetris();
	}
	
	@Override
	public void setEnabled (boolean toEnable) {
		super.setEnabled(toEnable);
		if (toEnable) {
			this.tpanel.grabFocus();
			this.tpanel.requestFocus();
		}
	}

}
