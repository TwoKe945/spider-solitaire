package cn.com.twoke.game.spider_solitaire.config;

public class Global {

	public static final boolean DEBUG = false;
	
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
	
}
