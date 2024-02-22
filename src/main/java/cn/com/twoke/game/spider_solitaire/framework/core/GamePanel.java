package cn.com.twoke.game.spider_solitaire.framework.core;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 游戏面板
 */
public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = 6443606981792577132L;
	
	private final Game game;
	private Dimension size;
	
	
	
	public GamePanel(Game game, int width, int height) {
		this.game = game;
		this.size = new Dimension(width, height);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
	}
	
	public Game getGame() {
		return this.game;
	}

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.draw(g);
    }

	public Dimension size() {
		return size;
	}
	
}
