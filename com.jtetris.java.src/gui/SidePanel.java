package gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import core.Game;

public class SidePanel extends JPanel {

	// constants
	private static final int WIDTH = 80;

	// components
	private TFrame gui;

	private SidePanel() {}

	public static SidePanel MakeScorePanel(TFrame tframe) {
		SidePanel sp = new SidePanel();
		sp.setPreferredSize(new Dimension(80,  tframe.getDimensions().width));
		sp.gui = tframe;
		return sp;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Game game = this.gui.getGame();
		if (game == null) return;
		int score = game.linesCleared();
		g.drawString("Lines", 10, 20);
		g.drawString("" + score, 25, 40 );
	}

}
