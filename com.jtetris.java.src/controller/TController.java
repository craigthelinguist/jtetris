package controller;

import gui.TFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import core.Grid;

public class TController implements KeyListener {

	private Grid grid;
	private TFrame gui;	
	
	public TController (TFrame g) {
		this.gui = g;
	}
	
	public void setGrid (Grid g) {
		this.grid = g;
	}
	
	public void setGui (TFrame g) {
		this.gui = g;
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (this.grid == null) return;
		int code = ke.getKeyCode();
		if (code == KeyEvent.VK_RIGHT) 		grid.userAction(Signal.RIGHT);
		else if (code == KeyEvent.VK_LEFT)	grid.userAction(Signal.LEFT);
		else if (code == KeyEvent.VK_DOWN)	grid.userAction(Signal.SOFT_FALL);
		else if (code == KeyEvent.VK_SPACE)	grid.userAction(Signal.HARD_FALL);
		else if (code == KeyEvent.VK_X)		grid.userAction(Signal.ROTATE_RIGHT);
		else if (code == KeyEvent.VK_UP)	grid.userAction(Signal.ROTATE_RIGHT);
		else if (code == KeyEvent.VK_Z)		grid.userAction(Signal.ROTATE_LEFT);
		this.gui.repaint();
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		// TODO Auto-generated method stub
		
	}

	
}
