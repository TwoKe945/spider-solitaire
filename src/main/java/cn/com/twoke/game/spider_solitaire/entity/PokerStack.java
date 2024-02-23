package cn.com.twoke.game.spider_solitaire.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.com.twoke.game.spider_solitaire.main.SpiderSolitaireGame;

import static cn.com.twoke.game.spider_solitaire.config.Global.*;

public class PokerStack extends ArrayList<Poker>{

	private static final long serialVersionUID = 4109427192906765312L;
	
	private Rectangle hitbox;
	private SpiderSolitaireGame solitaireGame;
	private List<Poker> notDraggingPokers;
	
	public PokerStack(SpiderSolitaireGame solitaireGame, int x, int y) {
		this.solitaireGame = solitaireGame;
		hitbox = new Rectangle(x, y, POKER_WIDTH, POKER_HEIGHT);
		notDraggingPokers = new ArrayList<Poker>();
	}
	
	@Override
	public boolean add(Poker item) {
		super.add(item);
		hitbox.height = hitbox.height + (size() == 1 ? 0 : !item.isTurnOver() ? NO_TURN_OFFSET : TURN_OFFSET) ;
		return true;
	}
	
	
	
	@Override
	public Poker remove(int index) {
		if (index >= size())return null;
		Poker temPoker = super.remove(index);
		hitbox.height = hitbox.height - (size() == 0 ? 0 : !temPoker.isTurnOver() ? NO_TURN_OFFSET : TURN_OFFSET) ;
		return temPoker;
	}

	public void update() {
		updateNotDraggingPokers();
	}
	
	
	private void updateNotDraggingPokers() {
		notDraggingPokers  = stream().filter(solitaireGame::notDragging).collect(Collectors.toList());		
	}

	public void draw(Graphics g) {
		int y = hitbox.y;
		for (int i = 0; i < notDraggingPokers.size(); i++) {
			Poker temPoker = notDraggingPokers.get(i);
			if (!temPoker.isTurnOver() && i == size() - 1) {
				temPoker.setTurnOver(true);
				this.hitbox.height += NO_TURN_OFFSET;
			}
			temPoker.draw(g, hitbox.x, y , temPoker.isTurnOver());
			y += !temPoker.isTurnOver() ? NO_TURN_OFFSET : TURN_OFFSET; 
		}
		
		debug(() -> {
			Color color = g.getColor();
			g.setColor(Color.BLUE);
			g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height - NO_TURN_OFFSET);
			g.setColor(color);
		});
	}
	
	public boolean intersects(Rectangle rectangle) {
		return hitbox.intersects(rectangle);
	}

	public int getLastPokerY() {
		return this.hitbox.y + this.hitbox.height - POKER_HEIGHT - NO_TURN_OFFSET;
	}
	
	public int getLastPokerX() {
		return this.hitbox.x;
	}
}
