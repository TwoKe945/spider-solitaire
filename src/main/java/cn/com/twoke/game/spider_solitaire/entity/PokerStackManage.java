package cn.com.twoke.game.spider_solitaire.entity;

import java.awt.Color;
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
import cn.com.twoke.game.spider_solitaire.enums.PokerNoEnum;
import cn.com.twoke.game.spider_solitaire.enums.PokerTypeEnum;
import cn.com.twoke.game.spider_solitaire.main.SpiderSolitaireGame;
import cn.com.twoke.game.spider_solitaire.utils.PokerUtils; 

import static cn.com.twoke.game.spider_solitaire.config.Global.*;

public class PokerStackManage implements MouseListener, MouseMotionListener {
	

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
	
	private Poker[] pokers;
	private PokerStack[] pokerStacks;
	private Rectangle pressedHitbox;
	private SpiderSolitaireGame game;
	private List<TipPokerGroup> tipPokerGroups;
	private int tipPokerIndex = 0;
	private boolean tip;
	
	public PokerStackManage(SpiderSolitaireGame game) {
		this.game = game;
		initClass();
		initPokers(pokers);
		PokerUtils.shuffle(pokers);
		game.loadPokers(pokers);
	}
	
	private void initClass() {
		pokers = new Poker[8 * 13];
		pokerStacks = new PokerStack[10];
		for (int i = 0; i < pokerStacks.length; i++) {
			pokerStacks[i] = new PokerStack(game, (int)(SpiderSolitaireGame.OFFSET_X * (i + 1) + i * POKER_WIDTH), POKER_MARGIN_VALUE);
		}
		draggedPokers = new ArrayList<Poker>();
		tipPokerGroups = new ArrayList<TipPokerGroup>();
	}

	private void initPokers(Poker[] pokers) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 13; j++) {
				PokerTypeEnum type = PokerTypeEnum.HEI;
				pokers[i * 13 + j] = new Poker(PokerNoEnum.of(j), type);
			}
		}
	}
	
	int aniIndex= 0;
	
	public void draw(Graphics g) {
		drawPlayingPokerDeck(g);
		drawDraggedPokers(g);
		if (tip) {
			TipPokerGroup group = tipPokerGroups.get(tipPokerIndex);
			PokerStack start = pokerStacks[group.getStartStackIndex()];
			PokerStack target = pokerStacks[group.getTargetStackIndex()];
			start.draw(g, group.getStartStackIndex(), group.getStartStackPokerIndex());
			if (aniIndex > 50) {
				aniIndex = 0;
				tip = false;
			}
			if (aniIndex > 25) {
				target.draw(g, group.getTargetStackIndex(), target.size() - 1);
			}
			aniIndex++;
		}
	}
	
	public void update() {
		for (PokerStack poker : pokerStacks) {
			poker.update();
		}
		updateTipPokerGroups();
	}
	
	private void updateTipPokerGroups() {
		tipPokerGroups.clear();
		for (int i = 0; i < pokerStacks.length; i++) {
			PokerStack poker = pokerStacks[i];
			if (poker.size() == 0) continue;
			int j = poker.size() - 1;
			for (; j > 0; j--) {
				if (poker.get(j - 1).getNo().getId() - poker.get(j).getNo().getId() != 1 ||
						!poker.get(j - 1).isTurnOver()
						) {
					break;
				}
			}
			for (int k = 0; k < pokerStacks.length; k++) {
				if (pokerStacks[k].size() == 0) continue;
				if (pokerStacks[k].get(pokerStacks[k].size() - 1).getNo().getId()  -
						poker.get(j).getNo().getId() == 1
						) {
					tipPokerGroups.add(new TipPokerGroup(i, k, j));
					break;
				}
			}
		}
	}

	/**
	 * 绘制游戏牌堆
	 * @param g
	 */
	private void drawPlayingPokerDeck(Graphics g) {
		for (int i = 0; i < pokerStacks.length; i++) {
			int x =  (int)(SpiderSolitaireGame.OFFSET_X * (i + 1) + i * POKER_WIDTH);
			g.drawImage(ImageResource.POKER_EMPTY, x, POKER_MARGIN_VALUE, POKER_WIDTH, POKER_HEIGHT, null);
			pokerStacks[i].draw(g, i);
			final int sizeIndex = i;
			debug(() -> {
				g.setColor(Color.WHITE);
				g.drawString("size:" + pokerStacks[sizeIndex].size(), x, pokerStacks[sizeIndex].getLastPokerY() + TURN_OFFSET + POKER_HEIGHT );
			});
		}
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

	public PokerStack[] getPokerStacks() {
		return pokerStacks;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (draggedPokers.size() > 0) {
			draggedPokerHitbox.x = e.getX() - draggedPokerOffsetX;
			draggedPokerHitbox.y = e.getY() - draggedPokerOffsetY;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			checkAndPickUpPokers(e);
		}
	}
	

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (draggedPokers.size() > 0) {
				doPlacePokers();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	public Poker[] getPokers() {
		return pokers;
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
			if(!stack.get(i).isTurnOver()) continue;
			Rectangle pressedHitbox = new Rectangle(stack.getLastPokerX(),
					stack.getLastPokerY() - (stackSize - 1 - i) * TURN_OFFSET, POKER_WIDTH, stackSize - 1 == i ? POKER_HEIGHT : TURN_OFFSET);
			if (pressedHitbox.contains(mouseX, mouseY) && canPickUp(stack, i, stackSize)) {
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
				game.getAudioPlayer().playEffect(AudioPlayer.PICKUP);
				return true;
			}
		}
		return false;
	}

	private boolean canPickUp(PokerStack stack, int startIndex, int stackSize) {
		for (int i = stackSize - 1; i > startIndex ; i--) {
			if (stack.get(i).getNo().getId() - stack.get(i - 1).getNo().getId() != -1) {
				return false;
			}
		}
		return true;
	}

	private void doPlacePokers() {
		for (int i = 0; i < pokerStacks.length; i++) {
			PokerStack stack =  pokerStacks[i];
			if (stack.intersects(draggedPokerHitbox) && canPlace(stack, draggedPokers)) { // 判断是否移动到另外的牌堆
				for (int j = 0; j < draggedPokers.size(); j++) { // 循环放置扑克到另外的牌堆
					Poker poker = pokerStacks[draggedStackIndex].remove(draggedStartItemIndex);
					stack.add(poker);
				}
				game.getScorePanel().lostScore(1);
				break;
			}
		}
		draggedPokers.clear();
		draggedPokerHitbox = null;
		pressedHitbox = null;
		game.getAudioPlayer().playEffect(AudioPlayer.PUTDOWN);
	}

	
	private boolean canPlace(PokerStack targetStack, List<Poker> draggedStack) {
		if (targetStack.size() == 0) return true;
		Poker draggedFirstPoker = draggedStack.get(0);
		Poker targetLastPoker =   targetStack.get(targetStack.size() - 1);
		return targetLastPoker.getNo().getId() - draggedFirstPoker.getNo().getId() == 1;
	}
	
	public boolean isDragging(Poker poker) {
		return draggedPokers.contains(poker);
	}
	
	public boolean notDragging(Poker poker) {
		return !draggedPokers.contains(poker);
	}

	public void tip() {
		if (tipPokerGroups.size() > 0) {
			tip = true;
			tipPokerIndex++;
			if (tipPokerIndex + 1>= tipPokerGroups.size()) {
				tipPokerIndex = 0;
			}
			game.getAudioPlayer().playEffect(AudioPlayer.TIP);
		} else {
			game.getAudioPlayer().playEffect(AudioPlayer.NO_TIP);
		}
	}
	
}
