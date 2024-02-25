package cn.com.twoke.game.spider_solitaire.entity;

public class TipPokerGroup {
	/**
	 * 开始移动的扑克堆索引
	 */
	private int startStackIndex;
	/**
	 * 目标扑克堆索引
	 */
	private int targetStackIndex;
	/**
	 * 开始移动时扑克堆的提示索引
	 */
	private int startStackPokerIndex;
	
	
	public TipPokerGroup(int startStackIndex, int targetStackIndex, int startStackPokerIndex) {
		super();
		this.startStackIndex = startStackIndex;
		this.targetStackIndex = targetStackIndex;
		this.startStackPokerIndex = startStackPokerIndex;
	}

	public int getStartStackIndex() {
		return startStackIndex;
	}
	
	public void setStartStackIndex(int startStackIndex) {
		this.startStackIndex = startStackIndex;
	}
	
	public int getTargetStackIndex() {
		return targetStackIndex;
	}
	
	public void setTargetStackIndex(int targetStackIndex) {
		this.targetStackIndex = targetStackIndex;
	}
	
	public int getStartStackPokerIndex() {
		return startStackPokerIndex;
	}
	
	public void setStartStackPokerIndex(int startStackPokerIndex) {
		this.startStackPokerIndex = startStackPokerIndex;
	}
	
	
	
}
