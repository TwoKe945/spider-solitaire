package cn.com.twoke.game.spider_solitaire.framework.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.function.Function;

/**
 * 游戏启动
 */
public abstract class Game  implements Runnable  {
	   /**
     * FPS
     */
    public static final int FPS_SET = 120;
    /**
     * UPS
     */
    public static final int UPS_SET = 200;

    protected final GamePanel panel;
    protected final GameWindow window;
    private Thread drawThread;
    
    
    public Game(Function<Game, GamePanel> panelGetter) {
        initialzer();
    	this.panel = panelGetter.apply(this);
        this.window = new GameWindow(panel);
        this.panel.requestFocus();
        startDrawGameLoop();
    }
    
    
    
	protected void startDrawGameLoop() {
		drawThread = new Thread(this);
        drawThread.start();
	}

	@Override
	public void run() {
		double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        long lastCheck = System.currentTimeMillis();;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;

        double deltaU = 0;
        double deltaF = 0;
        while (true) { // loop
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
//            	panel.requestFocus();
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                panel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000 ) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
	}
	

	public void windowLostFocus() {}


	protected void initialzer() {}
	
	public  void draw(Graphics drawGraphics) {
		int height = this.panel.size().height;
		int width = this.panel.size().width;
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.getGraphics();
		doDraw(g);
		drawGraphics.drawImage(bufferedImage, 0, 0, width, height, null);
	}
	
	protected abstract void doDraw(Graphics g);



	protected void update() {}

}
