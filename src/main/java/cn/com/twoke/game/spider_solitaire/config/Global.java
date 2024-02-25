package cn.com.twoke.game.spider_solitaire.config;

import java.awt.Font;

public class Global {

	public static final boolean DEBUG = false;

	public static final Font FONT = new Font("宋体", Font.PLAIN, 15);
	public static void debug(Runnable runnable) {
		if (DEBUG) {
			runnable.run();
		}
	}
	public static final int AUTO_FIRE_SIZE = 6;
	public static final int POKER_WIDTH = 71;
	public static final int POKER_HEIGHT = 96;
	public static final int POKER_DECK_OFFSET = 5;
	public static final int TURN_OFFSET = 20;
	public static final int NO_TURN_OFFSET = 10;
	public static final int POKER_MARGIN_VALUE = 20;
	
	public static final int WIDTH = 64 * 20;
	public static final int HEIGHT = 64 * 12;
	
}
