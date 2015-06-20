package gui;

import java.awt.Color;
import java.awt.Graphics;

import core.Tetris;

/**
 * This class is for the purpose of describing how to drawer a tetris on a given grid.
 * @author craig
 */
public final class TetrisDrawer {
	
	private TetrisDrawer() {}
	
	private static int CELL_WD() { return TPanel.CELL_WD; }
	
	/**
	 * Draw specified tetris at given position.
	 * @param g: graphics object to draw onto.
	 * @param t: tetris to be drawn.
	 * @param x: x position.
	 * @param y: y position.
	 * @param col: colour to draw the tetris.
	 */
	protected static void drawTetrisAt (Graphics g, Tetris t, int x, int y, Color col) {
		final int WD = t.getWidth();
		g.setColor(col);
		for (int j = y; j < y + WD; j++) {
			for (int i = x; i < x + WD; i++) {
				if (t.touching(i-x, j-y)) g.fillRect(i*CELL_WD(), j*CELL_WD(), CELL_WD(), CELL_WD());
			}
		}
	}
	
	/**
	 * Draw an outline of the specified tetris at the given position.
	 * @param g: graphics object to draw onto.
	 * @param t: tetris to be drawn.
	 * @param x: x position.
	 * @param y: y position.
	 * @param col: colour to draw the outline.
	 */
	protected static void outlineTetrisAt (Graphics g, Tetris t, int x, int y, Color col) {
		final int WD = t.getWidth();
		g.setColor(col);
		for (int j = y; j < y + WD; j++) {
			for (int i = x; i < x + WD; i++) {
				if (t.touching(i-x, j-y)) g.drawRect(i*CELL_WD(), j*CELL_WD(), CELL_WD(), CELL_WD());
			}
		}
	}

}
