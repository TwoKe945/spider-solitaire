package cn.com.twoke.game.spider_solitaire.constant;

import java.awt.image.BufferedImage;

import cn.com.twoke.game.spider_solitaire.framework.utils.ResourceLoader;

public class ImageResource {
	/**
	 * 黑桃 A-K
	 */
	public static final BufferedImage[] POKER_HEIS;
	/**
	 * 红桃 A-K
	 */
	public static final BufferedImage[] POKER_HONGS;
	/**
	 * 梅花 A-K
	 */
	public static final BufferedImage[] POKER_MEIS;
	/**
	 * 方块 A-K
	 */
	public static final BufferedImage[] POKER_FANGS;
	
	public static final BufferedImage POKER_MASK;
	public static final BufferedImage POKER_BACK;
	public static final BufferedImage POKER_EMPTY;
	public static final BufferedImage BACKGROUND;
	static {
		POKER_HEIS = loadPokerHeis();
		POKER_HONGS = loadPokerHongs();
		POKER_MEIS = loadPokerMeis();
		POKER_FANGS = loadPokerFangs();
		POKER_BACK = ResourceLoader.loadImage("/image/cardback.bmp");
		POKER_MASK = ResourceLoader.loadImage("/image/cardmask.png");
		POKER_EMPTY = ResourceLoader.loadImage("/image/cardempty.bmp");
		BACKGROUND = ResourceLoader.loadImage("/image/background.bmp");
	}
	
	private static BufferedImage[] loadPokerHeis() {
		BufferedImage[] cards = new  BufferedImage[13];
		for (int i = 0; i < cards.length; i++) {
			cards[i] = ResourceLoader.loadImage("/image/card/CARD"+(40+i)+".bmp");
		}
		return cards;
	}
	
	private static BufferedImage[] loadPokerHongs() {
		BufferedImage[] cards = new  BufferedImage[13];
		for (int i = 0; i < cards.length; i++) {
			cards[i] = ResourceLoader.loadImage("/image/card/CARD"+(27+i)+".bmp");
		}
		return cards;
	}
	
	private static BufferedImage[] loadPokerMeis() {
		BufferedImage[] cards = new  BufferedImage[13];
		for (int i = 0; i < cards.length; i++) {
			cards[i] = ResourceLoader.loadImage("/image/card/CARD"+(1+i)+".bmp");
		}
		return cards;
	}
	
	private static BufferedImage[] loadPokerFangs() {
		BufferedImage[] cards = new  BufferedImage[13];
		for (int i = 0; i < cards.length; i++) {
			cards[i] = ResourceLoader.loadImage("/image/card/CARD"+(14+i)+".bmp");
		}
		return cards;
	}
}
