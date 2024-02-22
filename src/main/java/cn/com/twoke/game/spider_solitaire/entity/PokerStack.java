package cn.com.twoke.game.spider_solitaire.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import cn.com.twoke.game.spider_solitaire.main.SpiderSolitaireGame;

public class PokerStack extends ArrayList<Poker>{

	private static final long serialVersionUID = 4109427192906765312L;
	
	private Rectangle hitbox;
	private SpiderSolitaireGame solitaireGame;
	
	public PokerStack(SpiderSolitaireGame solitaireGame, int x, int y) {
		this.solitaireGame = solitaireGame;
		hitbox = new Rectangle(x, y, 71, 96);
	}
	
	@Override
	public boolean add(Poker item) {
		super.add(item);
		hitbox.height = hitbox.height + (size() == 1 ? 0 : !item.isTurnOver() ? 10 : 20) ;
		return true;
	}
	
	
	
	@Override
	public Poker remove(int index) {
		if (index >= size())return null;
		Poker temPoker = super.remove(index);
		hitbox.height = hitbox.height - (size() == 0 ? 0 : !temPoker.isTurnOver() ? 10 : 20) ;
		return temPoker;
	}

	public int getLastPokerY() {
		return this.hitbox.y + this.hitbox.height - 96 - 10;
	}
	
	public int getLastPokerX() {
		return this.hitbox.x;
	}

	public void draw(Graphics g) {
		int y = hitbox.y;
		int i = 0; 
		
		g.drawString("size:" + size(), hitbox.x, hitbox.y + hitbox.height + 10);
		
		for(Poker temPoker : this.stream().filter(item -> item != solitaireGame.getDraggedPoker()).collect(Collectors.toList())) {
			if (!temPoker.isTurnOver() && i == size() - 1) {
				temPoker.setTurnOver(true);
				this.hitbox.height += 10;
			}
			
			temPoker.draw(g, hitbox.x, y , temPoker.isTurnOver());
			y += !temPoker.isTurnOver() ? 10 : 20; 
			i++;
		}
		Color color = g.getColor();
		g.setColor(color.BLUE);
		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height - 10);
		g.setColor(color);
	}
	
	public boolean intersects(Rectangle rectangle) {
		return hitbox.intersects(rectangle);
	}
	
}
