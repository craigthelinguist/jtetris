package controller;

import gui.TFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import core.Grid;

public class TController implements KeyListener {

	private Grid grid;
	private TFrame gui;	
	
	private Integer keyPressed;
	
	private static int[] keyCodes = new int[]{
		KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_SPACE,
		KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_UP, KeyEvent.VK_DOWN
	};
	
	public Map<Integer, Integer> inputDelay;
	public Map<Integer, Long> lastInput;
		
	public TController () {
		this.keyPressed = null;
		this.inputDelay = new HashMap<>();
		
		int DELAY_MOVEMENT = 60;
		int DELAY_SOFT_DROP = 40;
		int DELAY_HARD_DROP = 250;
		int DELAY_ROTATE = 120;
		
		inputDelay.put(KeyEvent.VK_RIGHT, DELAY_MOVEMENT);
		inputDelay.put(KeyEvent.VK_LEFT, DELAY_MOVEMENT);
		inputDelay.put(KeyEvent.VK_SPACE, DELAY_HARD_DROP);
		inputDelay.put(KeyEvent.VK_Z, DELAY_ROTATE);
		inputDelay.put(KeyEvent.VK_X, DELAY_ROTATE);
		inputDelay.put(KeyEvent.VK_UP, DELAY_ROTATE);
		inputDelay.put(KeyEvent.VK_DOWN, DELAY_SOFT_DROP);
		
		this.lastInput = new HashMap<>();
		for (int code : keyCodes) lastInput.put(code, 0l);
		
	}
	
	public void setGui (TFrame gui) {
		this.gui = gui;
	}
	
	public void setGrid (Grid g) {
		this.grid = g;
	}
	
	/**
	 * Return the time to wait between registering inputs for the specified
	 * key code.
	 * @param keyCode: KeyEvent.VK_ of what has been pushed.
	 * @return long: minimum amount of time in ms between key events
	 */
	public int getInputDelay (Integer keyCode) {
		return inputDelay.get(keyCode);
	}
	
	/**
	 * Return the last time the key was pushed.
	 * @param keyCode: KeyEvent.VK_ of the key that was pushed.
	 * @return long: the last time the key was pushed in ms
	 */
	public long getLastInput (Integer keyCode) {
		return lastInput.get(keyCode);
	}
	
	public Integer getKeyPressed() {
		return this.keyPressed;
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
		if (this.grid == null || this.gui == null) return;
		int code = ke.getKeyCode();
		if (validKey(code)) this.keyPressed = code;
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (this.grid == null || this.gui == null) return;
		int code = ke.getKeyCode();
		if (keyPressed != null && keyPressed == code) keyPressed = null;
	}

	@Override
	public void keyTyped(KeyEvent ke) {}

	private boolean validKey (int keyCode) {
		for (int i = 0; i < TController.keyCodes.length; i++) {
			if (keyCode == TController.keyCodes[i]) return true;
		}
		return false;
	}

	/**
	 * Update the last time that the specified keycode was input.
	 * @param code: key code of key that was registered.
	 * @param currentTimeMillis: time that it was registered.
	 */
	public void updateLastInput(Integer code, long currentTimeMillis) {
		this.lastInput.put(code, currentTimeMillis);
	}
	
}
