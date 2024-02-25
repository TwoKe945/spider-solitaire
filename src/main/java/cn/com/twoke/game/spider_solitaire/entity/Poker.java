package cn.com.twoke.game.spider_solitaire.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cn.com.twoke.game.spider_solitaire.animation.Animation;
import cn.com.twoke.game.spider_solitaire.constant.ImageResource;
import cn.com.twoke.game.spider_solitaire.enums.PokerNoEnum;
import cn.com.twoke.game.spider_solitaire.enums.PokerTypeEnum;

import static cn.com.twoke.game.spider_solitaire.config.Global.*;

public class Poker  {
	private PokerNoEnum no;
	private PokerTypeEnum type;
	private boolean turnOver;
	private Animation fireAnimation;
	private Animation completedAnimation;
	
	public Poker(PokerNoEnum no, PokerTypeEnum type) {
		this.no = no;
		this.type = type;
		fireAnimation = new Animation(15);
		completedAnimation = new Animation();
	}
	
	public boolean isFiring() {
		return fireAnimation.isRuning();
	}
	
	public boolean isCompleted() {
		return completedAnimation.isRuning();
	}
	
	public void fire() {
		fireAnimation.startAnimation();
	}
	
	public void completed() {
		completedAnimation.startAnimation();
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
		fireAnimation.update();
		completedAnimation.update();
	}

	public void draw(Graphics g, int startX, int startY, boolean isFace) {
		BufferedImage image = ImageResource.POKER_BACK;
		if (isFace && !fireAnimation.isRuning()) {
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
