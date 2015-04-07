package core;

import java.awt.Color;

public class Tetris {

	// Class fields.
	// ------------------------------------------------------------
	
	private static final char[] types = new char[]{'I', 'J', 'L', 'O', 'S', 'T', 'Z'};
	private static final Color[] colors = new Color[]{
		new Color(95, 149, 207),
		new Color(242, 163, 15),
		new Color(196, 106, 196),
		new Color(227, 221, 48),
		new Color(92, 199, 54),
		new Color(80, 204, 181),
		new Color(180, 255, 250)
	};
	
	// Instance fields.
	// ------------------------------------------------------------
	
	private boolean[][] grid;
	private final Color color;
	
	// Constructors.
	// ------------------------------------------------------------
	
	private Tetris (Color col, boolean[][] grid){
		this.color = col;
		this.grid = grid;
	}

	/**
	 * Create and return a random tetroid.
	 * @return Tetris
	 */
	public static Tetris randomBlock () {
		return new Tetris();
	}
	
	private Tetris () {
		int i = (int)(Math.random()*types.length);
		this.color = colors[i];
	
		switch(types[i]){
		case 'I':
			this.grid = new boolean[][]{
				new boolean[]{ true, true, true, true },
				new boolean[]{ false, false, false, false },
				new boolean[]{ false, false, false, false },
				new boolean[]{ false, false, false, false }
			};
			return;
		case 'J':
			this.grid = new boolean[][]{
				new boolean[]{ true, true, true },
				new boolean[]{ false, false, true },
				new boolean[]{ false, false, false },
			};
			return;
		case 'L':
			this.grid = new boolean[][]{
				new boolean[]{ true, true, true },
				new boolean[]{ true, false, false },
				new boolean[]{ false, false, false },
			};
			return;
		case 'O':
			this.grid = new boolean[][]{
				new boolean[]{ true, true },
				new boolean[]{ true, true },
			};
			return;
		case 'S':
			this.grid = new boolean[][]{
				new boolean[]{ false, true, true },
				new boolean[]{ true, true, false },
				new boolean[]{ false, false, false },
			};
			return;
		case 'T':
			this.grid = new boolean[][]{
				new boolean[]{ false, true, false },
				new boolean[]{ true, true, true },
				new boolean[]{ false, false, false },
			};
			return;
		case 'Z':
			this.grid = new boolean[][]{
				new boolean[]{ true, true, false },
				new boolean[]{ false, true, true },
				new boolean[]{ false, false, false },
			};
			return;
		default:
			throw new RuntimeException("Unknown tetris type: " + i);
		}
	}
	
	// Class methods.
	// ------------------------------------------------------------
	
	/**
	 * Return the rotation of a tetris to the left.
	 * @param t: tetris to rotate.
	 * @return: a new tetris, rotated left.
	 */
	public static Tetris RotateRight (Tetris t) {
		boolean[][] grid = t.grid;
		boolean[][] grid2 = new boolean[grid.length][grid[0].length];
		// grid[x][y] -> grid2[ht-y-1][x]
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				grid2[grid.length-y-1][x] = grid[x][y];
			}
		}
		return new Tetris(t.getColour(), grid2);
	}
	
	/**
	 * Return the rotation of a tetris to the right.
	 * @param t: tetris to rotate.
	 * @return: a new tetris, rotated right.
	 */
	public static Tetris RotateLeft (Tetris t) {
		boolean[][] grid = t.grid;
		boolean[][] grid2 = new boolean[grid.length][grid[0].length];
		// grid[x][y] -> grid2[y][ht-x-1]
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				grid2[y][grid.length-x-1] = grid[x][y];
			}
		}
		return new Tetris(t.getColour(), grid2);
	}
	

	// Instance methods.
	// ------------------------------------------------------------

	/**
	 * Return true if the specified point in the bounding box around
	 * the tetroid is part of the tetroid..
	 * @param x: x position
	 * @param y: y position
	 * @return boolean
	 */
	public boolean touching (int x, int y) {
		return this.grid[y][x];
	}
	
	// Getters/setters.
	// ------------------------------------------------------------
	
	/**
	 * Return width of the bounding box around this tetroid.
	 * @return int
	 */
	public int getWidth() {
		return this.grid.length;
	}

	/**
	 * Return the colour of this tetroid.
	 * @return Color
	 */
	public Color getColour() {
		return this.color;
	}
	
}
