package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import controller.TController;

import core.Grid;
import core.Tetris;

public class TPanel extends JPanel {

	// Constants & class fields.
	// ------------------------------------------------------------	
	protected static final int CELL_WD = 20;
	protected static final Color BG_COLOR = new Color(250, 249, 222);
	protected static final Color GRID_COLOR = new Color(145, 145, 145);
	protected static final Color DARKER_GRID_COLOR = Color.BLACK;
	
	
	
	// Instance fields.
	// ------------------------------------------------------------	
	private TFrame gui;
	
	
	
	// Constructors.
	// ------------------------------------------------------------	
	
	protected TPanel (TFrame parent) {
		this.gui = parent;
		this.setPreferredSize(new Dimension(CELL_WD * grid().GridWidth() + 1, CELL_WD * grid().GridHeight() + 1));
	}

	public Grid grid() {
		return gui.getGrid();
	}

	public static TPanel Make (TFrame parent) {
		return new TPanel(parent);
	}

	
	// Drawing methods.
	// ------------------------------------------------------------	
	
	@Override
	protected void paintComponent (Graphics g) {
		if (g == null) return;

		Grid grid = grid();
		if (grid == null) return;
		
		// drawing constants
		final int HT = Grid.GridHeight();
		final int WD = Grid.GridWidth();

		// draw blocks on the grid
		for (int y = 0; y < HT; y++) {
			for (int x = 0; x < WD; x++) {
				Color col = grid.blockAt(x, y);
				if (col != null) {
					g.setColor(col);
					g.fillRect(x*CELL_WD, y*CELL_WD, CELL_WD, CELL_WD);
				}
				else {
					g.setColor(BG_COLOR);
					g.fillRect(x*CELL_WD, y*CELL_WD, CELL_WD, CELL_WD);
				}
			}
		}

		// draw tetris if it exists
		Tetris t = grid.getTetris();
		if (t != null) {
			int x = grid.tetrisX();
			int y = grid.tetrisY();
			Color tetroidColour = t.getColour();

			// draw ghost tetris
			int ghostY = grid.getGhostTetris();
			TetrisDrawer.drawTetrisAt(g, t, x, ghostY, t.getGhostColour());

			// draw regular tetris
			TetrisDrawer.drawTetrisAt(g, t, x, y, tetroidColour);
		}

		// draw outline of grid
		g.setColor(GRID_COLOR);
		for (int y = 0; y < HT; y++) {
			for (int x = 0; x < WD; x++) {
				g.drawRect(x*CELL_WD, y*CELL_WD, CELL_WD, CELL_WD);
			}
		}

		// draw outline of tetroids
		g.setColor(DARKER_GRID_COLOR);
		for (int y = 0; y < HT; y++) {
			for (int x = 0; x < WD; x++) {
				if (grid.blockAt(x, y) == null) continue;
				g.drawRect(x*CELL_WD, y*CELL_WD, CELL_WD, CELL_WD);
			}
		}
		TetrisDrawer.outlineTetrisAt(g, t, grid.tetrisX(), grid.tetrisY(), DARKER_GRID_COLOR);
				
	}


}
