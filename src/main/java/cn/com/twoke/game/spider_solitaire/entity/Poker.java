package cn.com.twoke.game.spider_solitaire.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import cn.com.twoke.game.spider_solitaire.constant.ImageResource;
import cn.com.twoke.game.spider_solitaire.enums.PokerNoEnum;
import cn.com.twoke.game.spider_solitaire.enums.PokerTypeEnum;

public class Poker  {
	private PokerNoEnum no;
	private PokerTypeEnum type;
	private boolean turnOver;
	
	public Poker(PokerNoEnum no, PokerTypeEnum type) {
		this.no = no;
		this.type = type;
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

	public void draw(Graphics g, int startX, int startY, boolean isFace) {
		BufferedImage image = ImageResource.POKER_BACK;
		if (isFace) {
			image = getFaceImage();
		}
		g.drawImage(image, startX, startY, 71, 96, null);
		g.drawImage(ImageResource.POKER_MASK, startX, startY, 71, 96, null);

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
