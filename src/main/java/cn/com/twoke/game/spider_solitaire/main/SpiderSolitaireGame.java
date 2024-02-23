package cn.com.twoke.game.spider_solitaire.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import cn.com.twoke.game.spider_solitaire.constant.ImageResource;
import cn.com.twoke.game.spider_solitaire.entity.Poker;
import cn.com.twoke.game.spider_solitaire.entity.PokerStack;
import cn.com.twoke.game.spider_solitaire.enums.PokerNoEnum;
import cn.com.twoke.game.spider_solitaire.enums.PokerTypeEnum;
import cn.com.twoke.game.spider_solitaire.framework.core.Game;
import cn.com.twoke.game.spider_solitaire.framework.core.GamePanel;

public class SpiderSolitaireGame extends Game implements MouseListener, MouseMotionListener {

	public SpiderSolitaireGame() {
		super(SpiderSolitaireGame::buildGamePanel);
		initPokers();
		addMouseListener();
	}
	
	private void addMouseListener() {
		this.panel.addMouseListener(this);
		this.panel.addMouseMotionListener(this);
	}

	private Poker[] pokers;
	private PokerStack[] pokerQueues;
	
	private void initPokers() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 13; j++) {
				PokerTypeEnum type = null;
//				if (i == 0 || i == 1) {
					type = PokerTypeEnum.HEI;
//				}
//				if (i == 2 || i == 3) {
//					type = PokerTypeEnum.HONG;
//				}
//				if (i == 4 || i == 5) {
//					type = PokerTypeEnum.MEI;
//				}
//				if (i == 6 || i == 7) {
//					type = PokerTypeEnum.FANG;
//				}
				pokers[i * 13 + j] = new Poker(PokerNoEnum.of(j), type);
			}
		}
		shuffle(pokers);
		pokerDeckSize = pokers.length % 10 == 0 ? pokers.length / 10 : (pokers.length / 10) + 1;
		updatePockerDeck();
	}
	
	private static void shuffle(Poker[] arr) {
        Random mRandom = new Random();
        for (int i = arr.length; i > 0; i--) {
            int rand = mRandom.nextInt(i);
            swap(arr, rand, i - 1);
        }
    }


	private static void swap(Poker[] arr, int j, int i) {
		Poker temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	private static GamePanel buildGamePanel(Game game) {
		return new GamePanel(game, 1024, 512);
	}


	private int pokerDeckSize = 0;
	private static final float OFFSET_X = 28.54f;
	private static final int pokerDeckStartX = (int)(OFFSET_X * 10 + 9 * 71);
	private static final int pokerDeckStartY = 512 - 96 - 20;
	private static final int pokerDeckOffsetX = 5;
	private Rectangle pokerDeck;
	
	@Override
	protected void initialzer() {
		pokers = new Poker[8 * 13];
		pokerDeck = new Rectangle(pokerDeckStartX, pokerDeckStartY, 71, 96);
		pokerQueues = new PokerStack[10];
		for (int i = 0; i < pokerQueues.length; i++) {
			pokerQueues[i] = new PokerStack(this, (int)(OFFSET_X * (i + 1) + i * 71), 20);
		}
		draggedPokers = new ArrayList<Poker>();
	}
	
	boolean firstUpdate = true;
	
	
	
	@Override
	protected void update() {
		if (firstUpdate) {
			int autoFireSize = 6;
			for (int i = 0; i < autoFireSize; i++) {
				doFirePoker(i == autoFireSize - 1);
			}
			firstUpdate = false;
		}
		
	}
	
	

	public List<Poker> getDraggedPokers() {
		return draggedPokers;
	}

	@Override
	protected void doDraw(Graphics g) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 8; y++) {
				g.drawImage(ImageResource.BACKGROUND, x * 64 , y * 64, 64, 64, null);
			}
		}

		for (int i = 0; i < 10; i++) {
		    int x =  (int)(OFFSET_X * (i + 1) + i * 71);
			g.drawImage(ImageResource.POKER_EMPTY, x, 20, 71, 96, null);
		}
		
		// 绘制牌堆
		g.drawImage(ImageResource.POKER_EMPTY, pokerDeckStartX, pokerDeckStartY, 71, 96, null);
		for (int i = 0; i < pokerDeckSize; i++) {
			g.drawImage(ImageResource.POKER_BACK, pokerDeckStartX - i * pokerDeckOffsetX, pokerDeckStartY, 71, 96, null);
			g.drawImage(ImageResource.POKER_MASK, pokerDeckStartX - i * pokerDeckOffsetX, pokerDeckStartY, 71, 96, null);
		}

		// 绘制牌
		for (int i = 0; i < pokerQueues.length; i++) {
			pokerQueues[i].draw(g);
		}
		
		g.setColor(Color.RED);
		g.drawRect(pokerDeck.x, pokerDeck.y, pokerDeck.width, pokerDeck.height);
		
		if (draggedPokers.size() > 0 && Objects.nonNull(draggedPokerHitbox)) {
			int i = 0;
			for (Poker draggedPoker: draggedPokers) {
				draggedPoker.draw(g, draggedPokerHitbox.x, draggedPokerHitbox.y + i * 20, true);
				i++;
			}
		}
		
		if(Objects.nonNull(hitbox)) {
			g.setColor(Color.red);
			g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
			if (Objects.nonNull(draggedPokerHitbox)) {
				g.setColor(Color.YELLOW);
				g.drawRect(draggedPokerHitbox.x, draggedPokerHitbox.y,
						draggedPokerHitbox.width, draggedPokerHitbox.height);
			}
		}
	}

	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	private boolean firePokerPressed = false;
	
	public void updatePockerDeck() {
		pokerDeck.x = pokerDeckSize == 0 ? pokerDeckStartX : pokerDeckStartX - (pokerDeckSize - 1) * pokerDeckOffsetX;
		pokerDeck.y = pokerDeckStartY;
		pokerDeck.width = pokerDeckSize == 0 ? 71 : (pokerDeckSize - 1) * pokerDeckOffsetX + 71;
	}
	
	private List<Poker> draggedPokers;
	private Rectangle draggedPokerHitbox;
	private int draggedPokerOffsetX;
	private int draggedPokerOffsetY;
	private int draggedStackIndex = 0;
	private int draggedStartItemIndex = 0;
	
	private Rectangle hitbox;
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (pokerDeck.contains(e.getX(), e.getY())) {
				firePokerPressed = true;
			} else {
				out:for (int i = 0; i < pokerQueues.length; i++) {
					PokerStack stack = pokerQueues[i];
					if (stack.size() == 0) continue;
					int stackSize  = stack.size();
					inner: for (int j = 0; j < stackSize; j++) {
						Poker poker = stack.get(j);
						if (!poker.isTurnOver()) continue inner;
						Rectangle hitbox = new Rectangle(stack.getLastPokerX(),
								stack.getLastPokerY() - (stackSize - 1 - j) * 20, 71, stackSize - 1 == j ? 96 : 20);
						System.out.println("i: ["+i+"]["+j+"] hitbox"+hitbox);
						System.out.println("i: ["+i+"]["+j+"] x:"+stack.getLastPokerX() + " y:"+(stack.getLastPokerY() - (stackSize - 1 - i) * 20));
						
						if (hitbox.contains(e.getX(), e.getY())) {
							this.hitbox = hitbox;
							for (int k = j; k < stackSize; k++) {
								draggedPokers.add(stack.get(k));
							}
							draggedPokerOffsetX = e.getX() - hitbox.x;
							draggedPokerOffsetY = e.getY() - hitbox.y;
							draggedPokerHitbox = new Rectangle(hitbox.x, hitbox.y,
									71,
									96 + (stackSize - 1 - j) * 20
									);
							draggedStackIndex = i;
							draggedStartItemIndex = j;
							break out;
						}
					}
				}
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (pokerDeck.contains(e.getX(), e.getY()) && firePokerPressed) {
				firePokerPressed = false;
				doFirePoker(true);
			}
			if (draggedPokers.size() > 0) {
				int i = 0;
				for(PokerStack stack: pokerQueues) {
					if (stack.intersects(draggedPokerHitbox)) {
						System.out.println("i:" + i + "  " + draggedStackIndex + "  "+ draggedStartItemIndex);
						for (int j = 0; j < draggedPokers.size(); j++) {
							Poker poker = pokerQueues[draggedStackIndex].remove(draggedStartItemIndex);
							stack.add(poker);
						}
						break;
					}
					i++;
				}
				draggedPokers.clear();
				draggedPokerHitbox = null;
			}
		}
	}

	private int firePokerIndex = 0;
	
	/**
	 * 发牌操作
	 */
	private void doFirePoker(boolean turnOver) {
		if (pokerDeckSize - 1 < 0) return;
		for (int i = 0; i < 10; i++) {
			if (firePokerIndex >= pokers.length) continue;
			pokers[firePokerIndex].setTurnOver(turnOver);
			pokerQueues[i].add(pokers[firePokerIndex]);
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
}
