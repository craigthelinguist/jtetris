package main;

import core.Game;
import gui.TFrame;

public class Main {

	public static void main (String[] args) {
		TFrame frame = TFrame.Make();
		Game game = Game.newGame(frame);
		try {
			game.startGame();
		} catch (InterruptedException e) {
			throw new IllegalStateException("Game was interrupted.");
		}
	}
	
}
