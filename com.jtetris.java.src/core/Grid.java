package core;

import java.awt.Color;

import controller.Signal;

public class Grid {

	private final static int WIDTH = 10;
	private final static int HEIGHT = 22;
	
	private Color[][] grid;
	
	private Tetris tetris;
	private int tetrisX;
	private int tetrisY;
	
	private boolean active;
	
	public Grid () {
		this.grid = new Color[HEIGHT][WIDTH];
		this.tetris = null;
		this.tetrisX = this.tetrisY = -1;
		this.active = true;
	}
	
	
	public static int GridWidth() { return Grid.WIDTH; }
	public static int GridHeight() { return Grid.HEIGHT; } 
	
	public Tetris getTetris() { return this.tetris; }
	public int tetrisX() { return this.tetrisX; }
	public int tetrisY() { return this.tetrisY; }

	public Color blockAt(int x, int y) {
		if (this.grid == null) throw new NullPointerException("No grid yet.");
		if (this.grid[y][x] != null) return this.grid[y][x];
		return null;
	}
	
	public void userAction (Signal s){
		
		switch(s){
		case LEFT:
			if (!touchingWall(tetris, tetrisX-1, tetrisY)
					&& !touchingBlock(tetris, tetrisX-1, tetrisY)) tetrisX--;
			break;
		case RIGHT:
			if (!touchingWall(tetris, tetrisX+1, tetrisY)
					&& !touchingBlock(tetris, tetrisX+1, tetrisY)) tetrisX++;
			break;
		case ROTATE_LEFT:
			Tetris tleft = Tetris.RotateLeft(tetris);
			if (!touchingWall(tleft, tetrisX, tetrisY)
				&& !touchingBlock(tleft, tetrisX, tetrisY)
				&& !touchingFloor(tleft, tetrisX, tetrisY)) tetris = tleft;
			break;
		case ROTATE_RIGHT:
			Tetris tright = Tetris.RotateRight(tetris);
			if (!touchingWall(tright, tetrisX, tetrisY)
				&& !touchingBlock(tright, tetrisX, tetrisY)
				&& !touchingFloor(tright, tetrisX, tetrisY)) tetris = tright;
			break;
		case SOFT_FALL:
			if (touchingFloor(tetris, tetrisX, tetrisY+1) || touchingBlock(tetris, tetrisX, tetrisY+1)) {
				newTetris();
			}
			else tetrisY++;
			break;
		case HARD_FALL:
			throw new UnsupportedOperationException("Haven't implemented " + s);
		default:
			throw new RuntimeException("Unknown signal: " + s);
		}
		
	}
	
	public boolean touchingWall (Tetris t, int tx, int ty) {
		for (int y = ty; y < ty + t.getWidth(); y++) {
			for (int x = tx; x < tx + t.getWidth(); x++) {
				if (t.touching(x - tx, y - ty) && (x >= WIDTH || x < 0)) return true;
			}
		}
		return false;
	}
	
	public boolean touchingFloor (Tetris t, int tx, int ty) {
		for (int y = ty; y < ty + t.getWidth(); y++) {
			for (int x = tx; x < tx + t.getWidth(); x++) {
				if (t.touching( x - tx , y - ty) && y >= HEIGHT) return true;
			}
		}
		return false;
	}
	
	public boolean touchingBlock (Tetris t, int tx, int ty) {
		for (int y = ty; y < ty + t.getWidth(); y++) {
			for (int x = tx; x < tx +t.getWidth(); x++) {
				if (t.touching(x - tx, y - ty) && this.grid[y][x] != null) return true;
			}
		}
		return false;
	}
	
	public void newTetris () {
		
		// turn tetris into static blocks
		if (this.tetris != null) {
			for (int y = tetrisY; y < tetrisY + tetris.getWidth(); y++) {
				for (int x = tetrisX; x < tetrisX + tetris.getWidth(); x++) {
					if (y >= HEIGHT) break;
					if (tetris.touching(x - tetrisX, y - tetrisY)) this.grid[y][x] = tetris.getColour();
				}
			}
		}
		
		// generate a new tetris
		this.tetris = Tetris.randomBlock();
		tetrisX = WIDTH/2 - tetris.getWidth()/2;
		tetrisY = 0;
		if (touchingBlock(tetris, tetrisX, tetrisY)) gameOver();
	}
	
	public void step () {
		
		if (tetris == null) {
			newTetris();
			return;
		}
		
		// drop it down one
		if (touchingBlock(tetris, tetrisX, tetrisY+1)) gameOver();
		if (touchingFloor(tetris, tetrisX, tetrisY+1)) newTetris();
		else tetrisY++;
		
	}
	
	
	public void gameOver() {
		throw new UnsupportedOperationException("game over not implemented");
	}
	
}
