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
		this.setVisible(true);
	}
	
	public Grid grid() {
		return gui.getGrid();
	}
	
	public static TPanel Make (TFrame parent) {
		return new TPanel(parent);
	}
	
	private int getGhostTetris(Tetris t) {
		
		Grid g = grid();
		int tx = g.tetrisX();
		int ty = g.tetrisY();
		
		int i = 0;
		
		while (ty + i < g.GridHeight()
				&& !g.touchingFloor(t, tx, ty+i)
				&& !g.touchingBlock(t, tx, ty+i)) {
			
			if (i == 19) {
				System.out.println("break");
			}
			i++;
		
		}
		return ty + i - 1;
		
	}
	
	private Color lighter (Color col) {
		return col.brighter().brighter();
	}
	
	@Override
	protected void paintComponent (Graphics g) {
		if (g == null) return;

		// draw blocks on the grid
		for (int y = 0; y < Grid.GridHeight(); y++) {
			for (int x = 0; x < Grid.GridWidth(); x++) {
				Color col = grid().blockAt(x, y);
				if (col != null) {
					g.setColor(col);
					g.fillRect(x*CELL_WD, y*CELL_WD, CELL_WD, CELL_WD);
				}
			}
		}
		
		// draw tetris if it exists
		Tetris t = grid().getTetris();
		if (t != null) {
			
			int x = grid().tetrisX();
			int y = grid().tetrisY();
			Color tetroidColour = t.getColour();
			
			// draw ghost tetris
			int ghostY = getGhostTetris(t);
			g.setColor(lighter(tetroidColour));
			for (int j = ghostY; j < ghostY + t.getWidth(); j++){ 
				for (int i = x; i < x + t.getWidth(); i++) {
					if (t.touching(i-x, j-ghostY)) g.fillRect(i*CELL_WD, j*CELL_WD, CELL_WD, CELL_WD);
				}
			}
			
			// draw regular tetris
			g.setColor(tetroidColour);
			for (int j = y; j < y + t.getWidth(); j++) {
				for (int i = x; i < x + t.getWidth(); i++) {
					if (t.touching(i - x, j - y)) {
						g.fillRect(i * CELL_WD, j * CELL_WD, CELL_WD, CELL_WD);
					}
				}
			}

			
		}
	
		// draw outline of grid
		g.setColor(Color.BLACK);
		for (int y = 0; y < Grid.GridHeight(); y++) {
			for (int x = 0; x < Grid.GridWidth(); x++) {
				g.drawRect(x*CELL_WD, y*CELL_WD, CELL_WD, CELL_WD);
			}
		}
		
	}
	
	
}
