package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import core.Tetris;

/**
 * A TetrisDisplay is capable of drawing a tetris on a small grid. It is used to display the
 * tetris in storage and the next tetris that will be generated.
 * 
 * @author craig
 */
public class TetrisDisplay extends JPanel {

	private final int WIDTH = 4;
	
	private Tetris tetris;
	
	private TetrisDisplay() {}
	
	public static TetrisDisplay make () {
		return new TetrisDisplay();
	}
	
	public void setTetris (Tetris t) {
		this.tetris = t;
		final int CELL_WD = TPanel.CELL_WD;
		this.setPreferredSize(new Dimension(WIDTH*CELL_WD, WIDTH*CELL_WD));
	}
	
	private boolean tetrisCheck (int col, int row) {
		return tetris != null && tetris.withinBounds(col, row) && tetris.touching(col, row);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		final int CELL_WD = TPanel.CELL_WD;
		final Color BG_COLOR = TPanel.BG_COLOR;
		final Color GRID_COLOR = TPanel.GRID_COLOR;
		final Color DARKER_GRID_COLOR = TPanel.DARKER_GRID_COLOR;
		
		// fill in the colour of each block.
		for (int row = 0; row < WIDTH; row++) {
			for (int col = 0; col < WIDTH; col++) {
				if (tetrisCheck(col,row)) g.setColor(tetris.getColour());
				else g.setColor(BG_COLOR);
				g.fillRect(col*CELL_WD, row*CELL_WD, CELL_WD, CELL_WD);
			}
		}
		
		// draw outline of grid
		g.setColor(GRID_COLOR);
		for (int row = 0; row < WIDTH; row++) {
			for (int col = 0; col < WIDTH; col++) {
				g.drawRect(col*CELL_WD, row*CELL_WD, CELL_WD, CELL_WD);
			}
		}
		
		// draw outline of tetroid
		if (tetris == null) return;
		g.setColor(DARKER_GRID_COLOR);
		for (int row = 0; row < WIDTH; row++) {
			for (int col = 0; col < WIDTH; col++) {
				if (tetrisCheck(col, row)) g.drawRect(col*CELL_WD, row*CELL_WD, CELL_WD, CELL_WD);
			}
		}
		
	}
	
}
