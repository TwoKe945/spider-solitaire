package cn.com.twoke.game.spider_solitaire.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Animation {

	public int aniIndex = 0;
	public int aniCount;
	public List<Consumer<Integer>> listeners;
	public boolean aniFlag = false;
	
	
	public Animation() {
		this(10);
	}
	
	public Animation(int aniCount) {
		this.aniCount = aniCount;
		this.listeners = new ArrayList<Consumer<Integer>>();
	}
	
	public void startAnimation() {
		this.aniFlag = true;
	}
	
	public void endAnimation() {
		this.aniFlag = false;
	}
	
	public boolean isRuning() {
		return aniFlag;
	}
	
	public void addListener(Consumer<Integer> listener) {
		this.listeners.add(listener);
	}
	
	public void update() {
		if (aniFlag) {
			if (aniIndex > aniCount) {
				aniIndex = 0;
				endAnimation();
			}
			listeners.forEach(listener -> listener.accept(aniIndex));
			aniIndex++;
		}
	}
	
	
	
}
