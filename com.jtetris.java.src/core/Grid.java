package core;

import java.awt.Color;

import controller.Signal;

public class Grid {

	// Class fields.
	// ------------------------------------------------------------	
	private final static int WIDTH = 10;
	private final static int HEIGHT = 22;
	

	// Instance fields.
	// ------------------------------------------------------------
	private Game game;
	
 	private Color[][] grid;
	private Tetris tetris;
	private int tetrisX;
	private int tetrisY;


	// Constructors & configuration.
	// ------------------------------------------------------------
	public Grid () {
		this.grid = new Color[HEIGHT][WIDTH];
		this.tetris = null;
		this.tetrisX = this.tetrisY = -1;
	}
	
	public void attachTo (Game g) {
		this.game = g;
	}

	
	// Class methods.
	// ------------------------------------------------------------
	public static int GridWidth() { return Grid.WIDTH; }
	public static int GridHeight() { return Grid.HEIGHT; } 
	

	// Instance methods.
	// ------------------------------------------------------------
	
	/**
	 * Return true if there is a block at the specified position on the grid.
	 * @param x: x position
	 * @param y: y position
	 * @return boolean.
	 */
	public Color blockAt(int x, int y) {
		if (this.grid == null) throw new NullPointerException("No grid yet.");
		if (this.grid[y][x] != null) return this.grid[y][x];
		return null;
	}
	
	/**
	 * Perform some action on the grid as the result of a user input.
	 * @param s: the user input.
	 */
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
				dropTetris();
			}
			else tetrisY++;
			break;
		case HARD_FALL:
			int ghostY = getGhostTetris();
			this.tetrisY = ghostY;
			dropTetris();
			break;
		default:
			throw new RuntimeException("Unknown signal: " + s);
		}
		
	}

	/**
	 * Drop the tetris, update number of rows cleared.
	 * @return
	 */
	private void dropTetris () {
		int rowsCleared = newTetris();
		this.game.updateLines(rowsCleared);
	}
	
	/**
	 * Return the y position of the tetris if you were to place it as close to the floor
	 * as possible.
	 * @return; int, the y coordinate to the tetris after hard-dropping it.
	 */
	public int getGhostTetris() {
		
		// get current tetris and the position
		Tetris t = this.getTetris();
		int tx = tetrisX();
		int ty = tetrisY();
		
		// move it down by one and check to see if it's colliding
		// keep doing this while it's not colliding
		int i = 0;
		while (ty + i < GridHeight()
				&& !touchingFloor(t, tx, ty+i)
				&& !touchingBlock(t, tx, ty+i)) {
			i++;
		}
		return ty + i - 1;
		
	}
	
	/**
	 * Return true if the specified tetris located at position (tx, ty) is touching the
	 * left or right wall.
	 * @param t; tetris
	 * @param tx: x-position of tetris
	 * @param ty: y-position of tetris
	 * @return boolean
	 */
	public boolean touchingWall (Tetris t, int tx, int ty) {
		for (int y = ty; y < ty + t.getWidth(); y++) {
			for (int x = tx; x < tx + t.getWidth(); x++) {
				if (t.touching(x - tx, y - ty) && (x >= WIDTH || x < 0)) return true;
			}
		}
		return false;
	}

	/**
	 * Return true if the specified tetris located at position (tx, ty) is touching the
	 * floor.
	 * @param t: tetris
	 * @param tx: x-position of tetris
	 * @param ty: y-position of tetris
	 * @return boolean
	 */
	public boolean touchingFloor (Tetris t, int tx, int ty) {
		for (int y = ty; y < ty + t.getWidth(); y++) {
			for (int x = tx; x < tx + t.getWidth(); x++) {
				if (t.touching( x - tx , y - ty) && y >= HEIGHT) return true;
			}
		}
		return false;
	}
	
	/**
	 * Return true if the specified tetris located at position (tx,ty) is touching a
	 * block in the grid.
	 * @param t: tetris
	 * @param tx: x-position of tetris
	 * @param ty: y-position of tetris
	 * @return: boolean
	 */
	public boolean touchingBlock (Tetris t, int tx, int ty) {
		for (int y = ty; y < ty + t.getWidth(); y++) {
			for (int x = tx; x < tx +t.getWidth(); x++) {
				if (t.touching(x - tx, y - ty) && this.grid[y][x] != null) return true;
			}
		}
		return false;
	}
	
	/**
	 * Turn the old tetris into regular blocks in the grid. Create a new tetris at the
	 * top of the grid.
	 * @return int: number of rows cleared
	 */
	public int newTetris () {
		
		// turn tetris into static blocks
		if (this.tetris != null) {
			for (int y = tetrisY; y < tetrisY + tetris.getWidth(); y++) {
				for (int x = tetrisX; x < tetrisX + tetris.getWidth(); x++) {
					if (y >= HEIGHT) break;
					if (tetris.touching(x - tetrisX, y - tetrisY)) this.grid[y][x] = tetris.getColour();
				}
			}
		}
		
		// keep clearing rows while there are rows to clear.
		int rowsCleared = 0;
		while (clearRow()) rowsCleared++;
		
		// generate a new tetris
		this.tetris = Tetris.randomBlock();
		tetrisX = WIDTH/2 - tetris.getWidth()/2;
		tetrisY = 0;
		if (touchingBlock(tetris, tetrisX, tetrisY)) gameOver();
		
		return rowsCleared;
	}

	/**
	 * Starting from the bottom of the grid, move up looking for any complete rows. if you find a complete
	 * row, clear it, shift everything down, and return true.
	 * @return boolean: true if any rows were cleared
	 */
	private boolean clearRow () {
		
		outer: for (int row = HEIGHT-1; row >= 0; row--) {
			
			// check to see if this row is complete
			for (int col = 0; col < WIDTH; col++) {
				if (grid[row][col] == null) continue outer;
			}
			
			// row is complete, so delete all the blocks in them
			for (int col = 0; col < WIDTH; col++) {
				grid[row][col] = null;
			}
			
			// now apply gravity
			for (int r = row - 1; r >= 0; r--) {
				for (int col = 0; col < WIDTH; col++) {
					if (grid[r+1][col] == null) {
						grid[r+1][col] = grid[r][col];
						grid[r][col] = null;
					}
				}
			}
			return true;
			
		}
		
		return false;
	
	}
	
	/**
	 * Freeze the grid so it does not respond to user actions.
	 */
	public void gameOver() {
		throw new UnsupportedOperationException("game over not implemented");
	}
	

	// Getters/setters.
	// ------------------------------------------------------------
	public Tetris getTetris() { return this.tetris; }
	public int tetrisX() { return this.tetrisX; }
	public int tetrisY() { return this.tetrisY; }


	
}
