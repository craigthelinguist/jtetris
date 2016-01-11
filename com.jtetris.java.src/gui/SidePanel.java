package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.Game;
import core.Grid;
import core.Tetris;

public class SidePanel extends JPanel {

	// constants
	private static final int WIDTH = 80;

	// components
	private TFrame gui;
	private JLabel labelLines;
	private TetrisDisplay displayStorage;
	private TetrisDisplay displayNext;

	private SidePanel(TFrame tframe) {
		this.gui = tframe;
		this.makeAndLayoutComponents();
	}

	public static SidePanel MakeScorePanel(TFrame tframe) {
		return new SidePanel(tframe);
	}

	private void makeAndLayoutComponents () {

		// create layout manager
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		GroupLayout.SequentialGroup horizontal = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vertical = layout.createSequentialGroup();
		layout.setHorizontalGroup(horizontal);
		layout.setVerticalGroup(vertical);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		// new game button
		JButton button = new JButton("New Game");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Game g = gui.getGame();
				if (g != null) g.restartGame();
			}
		});

		// lines-cleared label
		this.labelLines = new JLabel("Lines Cleared: 0");
		
		// stored tetris label
		JLabel storedLabel = new JLabel("Stored Tetris");
		
		// storage display
		this.displayStorage = TetrisDisplay.make();
		
		// next tetris label
		JLabel nextTetrisLabel = new JLabel("Next Tetris");
		
		// next tetris display
		this.displayNext = TetrisDisplay.make();
		
		// add components in one column
		horizontal.addGroup(
			layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(button)
				.addComponent(labelLines)
				.addComponent(storedLabel)
				.addComponent(displayStorage)
				.addComponent(nextTetrisLabel)
				.addComponent(displayNext)
			);
		
		// add one row per component
		vertical.addComponent(button);
		vertical.addComponent(labelLines);
		vertical.addComponent(storedLabel);
		vertical.addComponent(displayStorage);
		vertical.addComponent(nextTetrisLabel);
		vertical.addComponent(displayNext);
		
		// set size of side panel
		final int WIDTH = (int) Math.max(labelLines.getPreferredSize().width * 1.2,
										  button.getPreferredSize().width     * 1.2);
		this.setPreferredSize(new Dimension(WIDTH, this.gui.getDimensions().width));
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Game game = this.gui.getGame();
		if (game == null) return;
	
		// update components in the gui.
		
		// update number of lines cleared.
		int score = game.linesCleared();
		this.labelLines.setText("Lines Cleared: " + score);
		
		// upgrade the block in storage.
		Tetris stored = this.gui.getStorageTetris();
		this.displayStorage.setTetris(stored);
		
		// update the next block
		Tetris next = this.gui.getNextTetris();
		this.displayNext.setTetris(next);
		
	}

}
