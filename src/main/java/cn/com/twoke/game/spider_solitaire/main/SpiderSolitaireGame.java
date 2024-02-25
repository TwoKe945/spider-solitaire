package cn.com.twoke.game.spider_solitaire.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.com.twoke.game.spider_solitaire.audio.AudioPlayer;
import cn.com.twoke.game.spider_solitaire.constant.ImageResource;
import cn.com.twoke.game.spider_solitaire.entity.Poker;
import cn.com.twoke.game.spider_solitaire.entity.PokerStack;
import cn.com.twoke.game.spider_solitaire.entity.PokerStackManage;
import cn.com.twoke.game.spider_solitaire.entity.ScorePanel;
import cn.com.twoke.game.spider_solitaire.enums.PokerNoEnum;
import cn.com.twoke.game.spider_solitaire.enums.PokerTypeEnum;
import cn.com.twoke.game.spider_solitaire.framework.core.Game;
import cn.com.twoke.game.spider_solitaire.framework.core.GamePanel;
import cn.com.twoke.game.spider_solitaire.utils.PokerUtils;
import static cn.com.twoke.game.spider_solitaire.config.Global.*;


public class SpiderSolitaireGame extends Game implements MouseListener, MouseMotionListener {
	public static final float OFFSET_X = (WIDTH - POKER_WIDTH * 10) / 11;
	public static final int pokerDeckStartX = (int)(OFFSET_X * 10 + 9 * POKER_WIDTH);
	public static final int pokerDeckStartY = HEIGHT - POKER_HEIGHT - POKER_MARGIN_VALUE;
	public static final int pokerDeckOffsetX = 5;
	private int pokerDeckSize = 0;
	private Rectangle pokerDeck;
	
	private boolean firstUpdate = true;
	private boolean gameCompleted = false;
	private PokerStackManage pokerStackManage;
	private Poker[] pokers;
	/**
	 * 发牌计数器
	 */
	private int firePokerIndex = 0;
	/**
	 * 是否按下发牌堆
	 */
	private boolean firePokerPressed = false;
	
	private List<Poker> completedPokers;
	private ScorePanel scorePanel;
	private AudioPlayer audioPlayer;
	
	public SpiderSolitaireGame() {
		super(SpiderSolitaireGame::buildGamePanel);
		initClass();
		addMouseListener();
		
	}
	
	private void initClass() {
		pokerDeck = new Rectangle(pokerDeckStartX, pokerDeckStartY, POKER_WIDTH, POKER_HEIGHT);
		pokerStackManage = new PokerStackManage(this);
		scorePanel = new ScorePanel(200, 100, this);
		audioPlayer = new AudioPlayer();
		
	}

	public void loadPokers(Poker[] pokers) {
		this.pokers = pokers;
		pokerDeckSize = pokers.length % 10 == 0 ? pokers.length / 10 : (pokers.length / 10) + 1;
		updatePockerDeck();
	}
	
	
	private static GamePanel buildGamePanel(Game game) {
		return new GamePanel(game, WIDTH, HEIGHT);
	}
	
	private void addMouseListener() {
		this.panel.addMouseListener(this);
		this.panel.addMouseMotionListener(this);
	}
	
	@Override
	protected void initialzer() {
		completedPokers = new ArrayList<Poker>();
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}

	public void setAudioPlayer(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	@Override
	protected void update() {
		if (firstUpdate) {
			int autoFireSize = AUTO_FIRE_SIZE;
			for (int i = 0; i < autoFireSize; i++) {
				doFirePoker(i == autoFireSize - 1, true);
			}
			firstUpdate = false;
		}
		pokerStackManage.update();
		updateGameState();
		if (firing) {
			if (fireAudioCount >= 100) {
				audioPlayer.stopSong();
				fireAudioCount = 0;
			}
			fireAudioCount ++;
		}
	}
	
	private boolean playgameCompletedAudio = true;
	
	private void updateGameState() {
		if (completedPokers.size() == 8) {
			gameCompleted = true;
			if (playgameCompletedAudio) {
				audioPlayer.playEffect(AudioPlayer.SUCCESS);
				playgameCompletedAudio = false;
			}
		}
	}

	@Override
	protected void doDraw(Graphics g) {
		g.setFont(FONT);
		drawBackground(g);
		drawFirePokerDeck(g);
		pokerStackManage.draw(g);
		drawCompletedPokers(g);
		scorePanel.draw(g);
		
		if (gameCompleted) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("仿宋",Font.BOLD,100));
			g.drawString("恭喜您!", (WIDTH - 300) / 2 , HEIGHT / 2);
		}
		
//		g.drawImage(ImageUtils.inverse(ImageResource.POKER_HEIS[12]), 0, 0, POKER_WIDTH, POKER_HEIGHT, null);
	}

	private void drawCompletedPokers(Graphics g) {
		for (int i = 0; i < completedPokers.size(); i++) {
			completedPokers.get(i).draw(g, (int)(OFFSET_X + i * 10), pokerDeckStartY, true);
		}
	}

	/**
	 *绘制发牌堆
	 * @param g
	 */
	private void drawFirePokerDeck(Graphics g) {
		g.drawImage(ImageResource.POKER_EMPTY, pokerDeckStartX, pokerDeckStartY, POKER_WIDTH, POKER_HEIGHT, null);
		for (int i = 0; i < pokerDeckSize; i++) {
			g.drawImage(ImageResource.POKER_BACK, pokerDeckStartX - i * pokerDeckOffsetX, pokerDeckStartY, POKER_WIDTH, POKER_HEIGHT, null);
			g.drawImage(ImageResource.POKER_MASK, pokerDeckStartX - i * pokerDeckOffsetX, pokerDeckStartY, POKER_WIDTH, POKER_HEIGHT, null);
		}
		debug(() -> {
			g.setColor(Color.RED);
			g.drawRect(pokerDeck.x, pokerDeck.y, pokerDeck.width, pokerDeck.height);
		});
	}

	/**
	 * 绘制背景
	 * @param g
	 */
	private void drawBackground(Graphics g) {
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 12; y++) {
				g.drawImage(ImageResource.BACKGROUND, x * 64 , y * 64, 64, 64, null);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (gameCompleted) return;
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (pokerDeck.contains(e.getX(), e.getY())) {
				firePokerPressed = true;
			} else {
				pokerStackManage.mousePressed(e);
			}
		}
		scorePanel.mousePressed(e);
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameCompleted) return;
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (pokerDeck.contains(e.getX(), e.getY()) && firePokerPressed) {
				firePokerPressed = false;
				doFirePoker(true, false);
			} else {
				pokerStackManage.mouseReleased(e);
			}
		}
		scorePanel.mouseReleased(e);
	}


	private boolean firing;
	private int fireAudioCount = 0;

	
	public void updatePockerDeck() {
		pokerDeck.x = pokerDeckSize == 0 ? pokerDeckStartX : pokerDeckStartX - (pokerDeckSize - 1) * pokerDeckOffsetX;
		pokerDeck.y = pokerDeckStartY;
		pokerDeck.width = pokerDeckSize == 0 ? POKER_WIDTH : (pokerDeckSize - 1) * pokerDeckOffsetX + POKER_WIDTH;
	}
	
	/**
	 * 发牌操作
	 */
	private void doFirePoker(boolean turnOver, boolean isForceFire) {
		if (!canFirePoker(isForceFire)) return;
		for (int i = 0; i < 10; i++) {
			if (firePokerIndex >= pokers.length) continue;
			pokers[firePokerIndex].setTurnOver(turnOver);
			pokers[firePokerIndex].fire();
			pokerStackManage.getPokerStacks()[i].add(pokers[firePokerIndex]);
			firePokerIndex++;
		}
		pokerDeckSize--;
		updatePockerDeck();
		if (!firstUpdate) {
			scorePanel.lostScore(1);
			audioPlayer.playSong(AudioPlayer.FIRE);
			firing = true;
			fireAudioCount = 0;
		}
	}

	private boolean canFirePoker(boolean isForceFire) {
		if (isForceFire) return true;
		if (pokerDeckSize - 1 < 0 ) return false;
		for (int i = 0; i < pokerStackManage.getPokerStacks().length; i++) {
			if (pokerStackManage.getPokerStacks()[i].size() == 0) return false;
		}
		return true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (gameCompleted) return;
		pokerStackManage.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
	
	public boolean isDragging(Poker poker) {
		return pokerStackManage.isDragging(poker);
	}
	
	public boolean notDragging(Poker poker) {
		return pokerStackManage.notDragging(poker);
	}

	public void completed(PokerTypeEnum type) {
		completedPokers.add(new Poker(PokerNoEnum.POKER_K, type));
		if (!firstUpdate) {
			scorePanel.addScore(100);
		}
	}

	public ScorePanel getScorePanel() {
		return scorePanel;
	}

	public void tip() {
		pokerStackManage.tip();
	}
	
}
