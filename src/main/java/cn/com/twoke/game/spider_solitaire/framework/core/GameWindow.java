package cn.com.twoke.game.spider_solitaire.framework.core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

/**
 * 游戏窗口
 */
public class GameWindow {
	
	private final JFrame frame;
	
	public GameWindow(GamePanel panel) {
		this.frame = new JFrame();
		frame.setTitle("蜘蛛纸牌 V1.0");
//		设置默认关闭操作
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
//		不允许变动窗口大小
		frame.setResizable(false);
		frame.pack();
//      设置显示位置到屏幕中间,必须在pack后面设置
		frame.setLocationRelativeTo(null);
//		设置窗口显示
		frame.setVisible(true);
//		设置窗口聚焦监听处理
		frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {

            }

            @Override
            public void windowLostFocus(WindowEvent e) {
            	panel.getGame().windowLostFocus();
            }
        });
	}
	
	
	
}
