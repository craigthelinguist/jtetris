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
	
	@Override
	protected void paintComponent (Graphics g) {
		if (g == null) return;

		System.out.println("woah");
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
			Color col = t.getColour();
			g.setColor(col);
			for (int j = y; j < y + t.getWidth(); j++) {
				for (int i = x; i < x + t.getWidth(); i++) {
					if (t.touching(i - x, j - y)) {
						g.fillRect(i * CELL_WD, j * CELL_WD, CELL_WD, CELL_WD);
					}
				}
			}
		}
	
		// draw outline
		g.setColor(Color.BLACK);
		for (int y = 0; y < Grid.GridHeight(); y++) {
			for (int x = 0; x < Grid.GridWidth(); x++) {
				g.drawRect(x*CELL_WD, y*CELL_WD, CELL_WD, CELL_WD);
			}
		}
		
	}
	
	
}
