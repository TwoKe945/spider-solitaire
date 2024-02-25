package cn.com.twoke.game.spider_solitaire.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cn.com.twoke.game.spider_solitaire.constant.ImageResource;
import cn.com.twoke.game.spider_solitaire.enums.PokerNoEnum;
import cn.com.twoke.game.spider_solitaire.enums.PokerTypeEnum;
import cn.com.twoke.game.spider_solitaire.main.SpiderSolitaireGame;

import static cn.com.twoke.game.spider_solitaire.config.Global.*;

public class Poker  {
	private PokerNoEnum no;
	private PokerTypeEnum type;
	private boolean turnOver;
	private boolean firing = false;
	private int aniIndex = 0;
	private int aniCount = 10;
	
	public Poker(PokerNoEnum no, PokerTypeEnum type) {
		this.no = no;
		this.type = type;
	}
	
	public boolean isFiring() {
		return firing;
	}
	
	public void setFiring(boolean firing) {
		this.firing = firing;
	}


	private BufferedImage getFaceImage() {
		switch (type) {
			case HEI:
				return ImageResource.POKER_HEIS[no.getId()] ;
			case HONG:
				return ImageResource.POKER_HONGS[no.getId()];
			case MEI:
				return ImageResource.POKER_MEIS[no.getId()];
			case FANG:
				return ImageResource.POKER_FANGS[no.getId()];
			default:
				return null;
		}
	}
	
	public void update() {
		if (firing) {
			
			if (aniCount <= aniIndex) {
				
				aniIndex = 0;
				firing = false;
			}
			aniIndex++;
		}
	}

	public void draw(Graphics g, int startX, int startY, boolean isFace) {
		BufferedImage image = ImageResource.POKER_BACK;
		if (isFace) {
			image = getFaceImage();
		}
		g.drawImage(image, startX, startY, POKER_WIDTH, POKER_HEIGHT, null);
		g.drawImage(ImageResource.POKER_MASK, startX, startY, POKER_WIDTH, POKER_HEIGHT, null);
	}

	public PokerNoEnum getNo() {
		return no;
	}

	public PokerTypeEnum getType() {
		return type;
	}

	public boolean isTurnOver() {
		return turnOver;
	}

	public void setTurnOver(boolean turnOver) {
		this.turnOver = turnOver;
	}
	
	
	
}
