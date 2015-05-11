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

	private volatile boolean running = false;
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
		return Math.max(25, 1000-10*linesCleared);
	}

	/**
	 * Start the new game running.
	 * @throws InterruptedException
	 */
	public void startGame()
	throws InterruptedException {

		// set up game parameters, tell grid to drop a new tetris.
		if (running) throw new IllegalStateException("Starting a game that's already running");
		this.running = true;
		this.linesCleared = 0;
		this.grid.newTetris();

		// this thread will tell the controller to keep soft-dropping
		// at an interval inversely proportional to the number of lines cleared.
		this.threadDrop = new Thread() {
			@Override
			public void run() {
				while (running) {
					try {
						System.out.println(linesCleared);
						grid.userAction(Signal.SOFT_FALL);
						frame.repaint();
						Thread.sleep(fallDelay());
					}
					catch (InterruptedException e) {
						return;
					}
				}
			}

		};

		// this thread is the main game loop.
		this.threadGame = new Thread() {
			@Override
			public void run() {
				running = true;
				while (running) {
					handleInput();
					frame.repaint();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						running = false;
					}
				}
			}


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

			}


		};

		this.threadGame.start();
		this.threadDrop.start();

	}

	public synchronized void stopGame() {
		this.threadDrop.interrupt();
		this.threadGame.interrupt();
		System.out.println("Game over, man.");
	}

}
