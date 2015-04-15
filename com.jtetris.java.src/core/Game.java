package core;

import controller.Signal;
import controller.TController;
import gui.TFrame;

public class Game {

	private TFrame frame;
	private Grid grid;
	private TController controller;
	
	private boolean running = false;
	private int linesCleared = 0;
	
	private Game(TFrame frame, Grid grid, TController controller) {
		this.frame = frame;
		this.grid = grid;
		this.controller = controller;
	}
	
	/**
	 * Make a new Game object. The controller and grid needed for the game will
	 * be created in this method.
	 * @param frame: gui on which the game should be displayed.
	 * @return Game object
	 */
	public static Game newGame (TFrame frame) {
		Grid grid = new Grid();
		TController controller = new TController();
		frame.setGrid(grid);
		controller.setGrid(grid);
		controller.setGui(frame);
		frame.setController(controller);
		Game g = new Game(frame, grid, controller);
		grid.attachTo(g);
		return g;
	}
	
	/**
	 * Update the number of lines cleared in the name so far.
	 * @param lines: amount to add onto the lines cleared so far.
	 */
	public void updateLines(int lines) {
		this.linesCleared += lines;
	}
	
	/**
	 * Return the amount of time to wait before dropping the tetris. The formula
	 * depends on number of lines cleared. More lines cleared => tetris drops faster.
	 * @return: amount of time to wait in milliseconds.
	 */
	private int fallDelay() {
		return Math.max(25, 1000-100*linesCleared);
	}
	
	/**
	 * Start the new game running.
	 */
	public void startGame () {
		
		// set up game parameters, tell grid to drop a new tetris.
		if (running) throw new IllegalStateException("Starting a game that's already running");
		this.running = true;
		this.linesCleared = 0;
		this.grid.newTetris();
		
		// this thread will tell the controller to keep soft-dropping
		// at an interval inversely proportional to the number of lines cleared.
		Thread t = new Thread() {
			@Override
			public void run() {
				while (Game.this.running) {
					try {
						System.out.println(linesCleared);
						grid.userAction(Signal.SOFT_FALL);
						frame.repaint();
						Thread.sleep(fallDelay());
					}
					catch (InterruptedException e) { 
						throw new IllegalStateException("Game thread was interrupted");
					}
				} 
			}
			
		};
		
		t.start();
		
	}
	
}
