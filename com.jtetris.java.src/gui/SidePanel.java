package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import core.Game;

public class SidePanel extends JPanel {

	// constants
	private static final int WIDTH = 80;

	// components
	private TFrame gui;

	private SidePanel() {}

	public static SidePanel MakeScorePanel(TFrame tframe) {
		final SidePanel sp = new SidePanel();
		sp.setPreferredSize(new Dimension(80,  tframe.getDimensions().width));

		JButton button = new JButton("New Game");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Game g = sp.gui.getGame();
				if (g != null) {
					g.stopGame();
				}
			}

		});

		sp.add(button, BorderLayout.NORTH);

		sp.gui = tframe;
		return sp;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Game game = this.gui.getGame();
		if (game == null) return;
		int score = game.linesCleared();
		g.drawString("Lines", 10, 50);
		g.drawString("" + score, 25, 65 );
	}

}
