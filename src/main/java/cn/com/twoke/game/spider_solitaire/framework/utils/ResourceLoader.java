package cn.com.twoke.game.spider_solitaire.framework.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public abstract class ResourceLoader {

	/**
	 * 加载图片资源
	 * @param imagePath
	 * @return
	 */
	public static BufferedImage loadImage(String imagePath) {
		BufferedImage image = null;
        InputStream is = ResourceLoader.class.getResourceAsStream(imagePath);
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
	}
	
	
	
}
