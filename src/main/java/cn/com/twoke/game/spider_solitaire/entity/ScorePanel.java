package cn.com.twoke.game.spider_solitaire.entity;

import static cn.com.twoke.game.spider_solitaire.config.Global.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import cn.com.twoke.game.spider_solitaire.audio.AudioPlayer;
import cn.com.twoke.game.spider_solitaire.main.SpiderSolitaireGame;

public class ScorePanel extends MouseAdapter {

	public static final int SCORE_VALUE_MAX = 500;
	private Rectangle hitbox;
	private boolean pressedScorePanel;
	private int score = SCORE_VALUE_MAX;
	private int numberOfOperations = 0;
	private SpiderSolitaireGame game;
	
	public ScorePanel(int w, int h, SpiderSolitaireGame game) {
		this.hitbox = new Rectangle((WIDTH -  w ) / 2  , HEIGHT - h - POKER_MARGIN_VALUE , w, h);
		this.game = game;
	}
	
	private boolean in(int x, int y) {
		return this.hitbox.contains(x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && in(e.getX(), e.getY())) {
			pressedScorePanel = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && pressedScorePanel) {
			tip(); // 提示
			pressedScorePanel = false;
		}
	}

	
	private void tip() {
		game.tip();
	}
	
	
	public void update() {
		
	}
	
	public void addScore(int value) {
		this.score += value;
	}
	
	public void lostScore(int value) {
		if(value > 0) {
			this.score -= value;
			this.numberOfOperations++;
		}
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(0x00, 0x7f, 0x00));
		g.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		
		g.setColor(Color.black);
		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		
		g.setColor(Color.WHITE);
		g.setFont(FONT);
		g.drawString("分数：" + score, hitbox.x + hitbox.width / 2 - 30 , hitbox.y + hitbox.height / 2 );
		g.drawString("操作：" + numberOfOperations, hitbox.x + hitbox.width / 2 - 30 , hitbox.y + hitbox.height / 2 + 20);
	}
	
	
}
