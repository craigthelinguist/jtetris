package core;

import java.awt.event.KeyEvent;
import java.util.Collection;

import controller.Signal;
import controller.TController;
import gui.TFrame;

public class Game {

	private Thread threadGame;
	private Thread threadDrop;

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
	 * Return the number of lines cleared so far in this game.
	 * @return int
	 */
	public int linesCleared () {
		return this.linesCleared;
	}

	/**
	 * Return the amount of time to wait before dropping the tetris. The formula
	 * depends on number of lines cleared. More lines cleared => tetris drops faster.
	 * @return: amount of time to wait in milliseconds.
	 */
	private int fallDelay() {
		
		return Math.max(25, 1000-25*linesCleared);
	}

	/**
	 * Start the new game running.
	 */
	public void startGame() {

		// set up game parameters, tell grid to drop a new tetris.
		if (running) throw new IllegalStateException("Starting a game that's already running");
		this.running = true;
		this.linesCleared = 0;
		this.grid.newTetris();

		final int DRAW_UPDATE = 50;
	
		long lastFall = 0;
		long lastUpdate = 0;
		
		while (running) {

			long time = System.currentTimeMillis();
			
			if (time - lastFall > fallDelay()) {
				grid.userAction(Signal.SOFT_FALL);
				lastFall = time;
			}
			
			if (time - lastUpdate > DRAW_UPDATE) {
				handleInput();
				frame.repaint();
				lastUpdate = time;
			}
			
			
		}

	}


	/**
	 * Handle user input.
	 * @return true if there was any user input to do.
	 */
	private void handleInput() {

		// poll current key being pressed
		Integer code = controller.getKeyPressed();
		if (code == null) return;

		// check input delay on that key
		long delay = controller.getInputDelay(code);
		long lastTimePushed = controller.getLastInput(code);
		if (System.currentTimeMillis() - lastTimePushed < delay) return;

		// map that key to the appropriate action
		if (code == KeyEvent.VK_RIGHT) grid.userAction(Signal.RIGHT);
		else if (code == KeyEvent.VK_LEFT) grid.userAction(Signal.LEFT);
		else if (code == KeyEvent.VK_DOWN) grid.userAction(Signal.SOFT_FALL);
		else if (code == KeyEvent.VK_SPACE) grid.userAction(Signal.HARD_FALL);
		else if (code == KeyEvent.VK_X) grid.userAction(Signal.ROTATE_RIGHT);
		else if (code == KeyEvent.VK_UP) grid.userAction(Signal.ROTATE_RIGHT);
		else if (code == KeyEvent.VK_Z) grid.userAction(Signal.ROTATE_LEFT);

		// update when that key was last registered
		controller.updateLastInput(code, System.currentTimeMillis());
		return;
		
	}


	public void stopGame() {
		this.threadDrop.interrupt();
		this.threadGame.interrupt();
		System.out.println("Game over, man.");
	}

}
