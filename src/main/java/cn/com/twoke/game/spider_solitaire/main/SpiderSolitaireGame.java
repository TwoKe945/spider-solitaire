package cn.com.twoke.game.spider_solitaire.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.com.twoke.game.spider_solitaire.constant.ImageResource;
import cn.com.twoke.game.spider_solitaire.entity.Poker;
import cn.com.twoke.game.spider_solitaire.entity.PokerStack;
import cn.com.twoke.game.spider_solitaire.enums.PokerNoEnum;
import cn.com.twoke.game.spider_solitaire.enums.PokerTypeEnum;
import cn.com.twoke.game.spider_solitaire.framework.core.Game;
import cn.com.twoke.game.spider_solitaire.framework.core.GamePanel;
import cn.com.twoke.game.spider_solitaire.utils.PokerUtils;
import static cn.com.twoke.game.spider_solitaire.config.Global.*;

public class SpiderSolitaireGame extends Game implements MouseListener, MouseMotionListener {

	private Poker[] pokers;
	private PokerStack[] pokerStacks;

	private int pokerDeckSize = 0;
	private static final float OFFSET_X = 28.54f;
	private static final int pokerDeckStartX = (int)(OFFSET_X * 10 + 9 * POKER_WIDTH);
	private static final int pokerDeckStartY = 512 - POKER_HEIGHT - POKER_MARGIN_VALUE;
	private static final int pokerDeckOffsetX = 5;
	private Rectangle pokerDeck;
	private boolean firstUpdate = true;
	private Rectangle pressedHitbox;
	
	/**
	 * 发牌计数器
	 */
	private int firePokerIndex = 0;
	/**
	 * 是否按下发牌堆
	 */
	private boolean firePokerPressed = false;
	/**
	 * 被拖动的扑克牌
	 */
	private List<Poker> draggedPokers;
	/**
	 * 扑克牌拖动时的碰撞盒子
	 */
	private Rectangle draggedPokerHitbox;
	/**
	 * 拖动开始时鼠标与扑克牌X轴的偏移值
	 */
	private int draggedPokerOffsetX;
	/**
	 * 拖动开始时鼠标与扑克牌Y轴的偏移值
	 */
	private int draggedPokerOffsetY;
	/**
	 * 扑克牌拖动的牌堆索引
	 */
	private int draggedStackIndex = 0;
	/**
	 * 拖动扑克牌开始的位置
	 */
	private int draggedStartItemIndex = 0;
	
	public SpiderSolitaireGame() {
		super(SpiderSolitaireGame::buildGamePanel);
		initFirePockerDeck();
		addMouseListener();
	}
	
	/**
	 * 初始化发牌堆
	 */
	private void initFirePockerDeck() {
		initPokers(pokers);
		PokerUtils.shuffle(pokers);
		pokerDeckSize = pokers.length % 10 == 0 ? pokers.length / 10 : (pokers.length / 10) + 1;
		updatePockerDeck();
	}
	
	private void initPokers(Poker[] pokers) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 13; j++) {
				PokerTypeEnum type = PokerTypeEnum.HEI;
				pokers[i * 13 + j] = new Poker(PokerNoEnum.of(j), type);
			}
		}
	}
	
	private static GamePanel buildGamePanel(Game game) {
		return new GamePanel(game, 1024, 512);
	}
	
	private void addMouseListener() {
		this.panel.addMouseListener(this);
		this.panel.addMouseMotionListener(this);
	}
	
	@Override
	protected void initialzer() {
		pokers = new Poker[8 * 13];
		pokerDeck = new Rectangle(pokerDeckStartX, pokerDeckStartY, POKER_WIDTH, POKER_HEIGHT);
		pokerStacks = new PokerStack[10];
		for (int i = 0; i < pokerStacks.length; i++) {
			pokerStacks[i] = new PokerStack(this, (int)(OFFSET_X * (i + 1) + i * POKER_WIDTH), POKER_MARGIN_VALUE);
		}
		draggedPokers = new ArrayList<Poker>();
	}
	
	@Override
	protected void update() {
		if (firstUpdate) {
			int autoFireSize = AUTO_FIRE_SIZE;
			for (int i = 0; i < autoFireSize; i++) {
				doFirePoker(i == autoFireSize - 1);
			}
			firstUpdate = false;
		}
		
		for (PokerStack poker : pokerStacks) {
			poker.update();
		}
	}
	
	@Override
	protected void doDraw(Graphics g) {
		drawBackground(g);
		drawPlayingPokerDeck(g);
		drawFirePokerDeck(g);
		drawDraggedPokers(g);
	}
	
	/**
	 * 绘制拖动牌堆
	 * @param g
	 */
	private void drawDraggedPokers(Graphics g) {
		if (draggedPokers.size() > 0 && Objects.nonNull(draggedPokerHitbox)) {
			int i = 0;
			for (Poker draggedPoker: draggedPokers) {
				draggedPoker.draw(g, draggedPokerHitbox.x, draggedPokerHitbox.y + i * TURN_OFFSET, true);
				i++;
			}
		}
		debug(() -> {
			if (Objects.nonNull(pressedHitbox)) {
				g.setColor(Color.GREEN);
				g.drawRect(pressedHitbox.x, pressedHitbox.y, pressedHitbox.width, pressedHitbox.height);
			}
			if ( Objects.nonNull(draggedPokerHitbox)) {
				g.setColor(Color.YELLOW);
				g.drawRect(draggedPokerHitbox.x, draggedPokerHitbox.y,
						draggedPokerHitbox.width, draggedPokerHitbox.height);
			}
		});
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
	 * 绘制游戏牌堆
	 * @param g
	 */
	private void drawPlayingPokerDeck(Graphics g) {
		for (int i = 0; i < pokerStacks.length; i++) {
			int x =  (int)(OFFSET_X * (i + 1) + i * POKER_WIDTH);
			g.drawImage(ImageResource.POKER_EMPTY, x, POKER_MARGIN_VALUE, POKER_WIDTH, POKER_HEIGHT, null);
			pokerStacks[i].draw(g);
			final int sizeIndex = i;
			debug(() -> {
				g.drawString("size:" + pokerStacks[sizeIndex].size(), x, pokerStacks[sizeIndex].getLastPokerY() + TURN_OFFSET + POKER_HEIGHT );
			});
		}
	}

	/**
	 * 绘制背景
	 * @param g
	 */
	private void drawBackground(Graphics g) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 8; y++) {
				g.drawImage(ImageResource.BACKGROUND, x * 64 , y * 64, 64, 64, null);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	public void updatePockerDeck() {
		pokerDeck.x = pokerDeckSize == 0 ? pokerDeckStartX : pokerDeckStartX - (pokerDeckSize - 1) * pokerDeckOffsetX;
		pokerDeck.y = pokerDeckStartY;
		pokerDeck.width = pokerDeckSize == 0 ? POKER_WIDTH : (pokerDeckSize - 1) * pokerDeckOffsetX + POKER_WIDTH;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (pokerDeck.contains(e.getX(), e.getY())) {
				firePokerPressed = true;
			} else {
				checkAndPickUpPokers(e);
			}
		}
		
	}

	/**
	 * 检查并拾起扑克牌
	 * @param e
	 */
	private void checkAndPickUpPokers(MouseEvent e) {
		for (int i = 0; i < pokerStacks.length; i++) {
			PokerStack stack = pokerStacks[i];
			if (stack.size() == 0) continue;
			if (doCheckAndPickUpPoker(i, e.getX(), e.getY(), stack)) break;
		}
	}

	private boolean doCheckAndPickUpPoker(int stackIndex, int mouseX, int mouseY, PokerStack stack) {
		int stackSize  = stack.size();
		for (int i = stackSize - 1; i >= 0; i--) {
			Rectangle pressedHitbox = new Rectangle(stack.getLastPokerX(),
					stack.getLastPokerY() - (stackSize - 1 - i) * TURN_OFFSET, POKER_WIDTH, POKER_HEIGHT);
			if (pressedHitbox.contains(mouseX, mouseY)) {
				this.pressedHitbox = pressedHitbox;
				for (int k = i; k < stackSize; k++) {
					draggedPokers.add(stack.get(k));
				}
				draggedPokerOffsetX = mouseX - pressedHitbox.x;
				draggedPokerOffsetY = mouseY - pressedHitbox.y;
				draggedPokerHitbox = new Rectangle(pressedHitbox.x, pressedHitbox.y,
						POKER_WIDTH,
						POKER_HEIGHT + (stackSize - 1 - i) * TURN_OFFSET
						);
				draggedStackIndex = stackIndex;
				draggedStartItemIndex = i;
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (pokerDeck.contains(e.getX(), e.getY()) && firePokerPressed) {
				firePokerPressed = false;
				doFirePoker(true);
			}
			if (draggedPokers.size() > 0) {
				doPlacePokers();
			}
		}
	}

	private void doPlacePokers() {
		for (int i = 0; i < pokerStacks.length; i++) {
			PokerStack stack =  pokerStacks[i];
			if (stack.intersects(draggedPokerHitbox)) { // 判断是否移动到另外的牌堆
				for (int j = 0; j < draggedPokers.size(); j++) { // 循环放置扑克到另外的牌堆
					Poker poker = pokerStacks[draggedStackIndex].remove(draggedStartItemIndex);
					stack.add(poker);
				}
				break;
			}
		}
		draggedPokers.clear();
		draggedPokerHitbox = null;
		pressedHitbox = null;
	}

	
	/**
	 * 发牌操作
	 */
	private void doFirePoker(boolean turnOver) {
		if (pokerDeckSize - 1 < 0) return;
		for (int i = 0; i < 10; i++) {
			if (firePokerIndex >= pokers.length) continue;
			pokers[firePokerIndex].setTurnOver(turnOver);
			pokerStacks[i].add(pokers[firePokerIndex]);
			firePokerIndex++;
		}
		pokerDeckSize--;
		updatePockerDeck();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (draggedPokers.size() > 0) {
			draggedPokerHitbox.x = e.getX() - draggedPokerOffsetX;
			draggedPokerHitbox.y = e.getY() - draggedPokerOffsetY;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
	
	public boolean isDragging(Poker poker) {
		return draggedPokers.contains(poker);
	}
	
	public boolean notDragging(Poker poker) {
		return !draggedPokers.contains(poker);
	}
	
}
