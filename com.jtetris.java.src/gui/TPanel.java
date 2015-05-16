package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import controller.TController;

import core.Grid;
import core.Tetris;

public class TPanel extends JPanel {

	private TFrame gui;
	private static final int CELL_WD = 20;

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

	/**
	 * Return the same colour but several shades lighter.
	 * @param col
	 * @return a lighter Color.
	 */
	private Color darker (Color col) {
		return col.darker().darker();
	}

	/**
	 * Draw specified tetris at given position.
	 * @param g: graphics object to draw onto.
	 * @param t: tetris to be drawn.
	 * @param x: x position.
	 * @param y: y position.
	 * @param col: colour to draw the tetris.
	 */
	private void drawTetrisAt (Graphics g, Tetris t, int x, int y, Color col) {
		final int WD = t.getWidth();
		g.setColor(col);
		for (int j = y; j < y + WD; j++) {
			for (int i = x; i < x + WD; i++) {
				if (t.touching(i-x, j-y)) g.fillRect(i*CELL_WD, j*CELL_WD, CELL_WD, CELL_WD);
			}
		}


	}

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
			drawTetrisAt(g, t, x, ghostY, darker(tetroidColour));

			// draw regular tetris
			drawTetrisAt(g, t, x, y, tetroidColour);
		}

		// draw outline of grid
		g.setColor(Color.BLACK);
		for (int y = 0; y < HT; y++) {
			for (int x = 0; x < WD; x++) {
				g.drawRect(x*CELL_WD, y*CELL_WD, CELL_WD, CELL_WD);
			}
		}

	}


}
