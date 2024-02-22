package cn.com.twoke.game.spider_solitaire.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Main {
	
	public static void main(String[] args) throws URISyntaxException {
		new SpiderSolitaireGame();
		
//		URL url = Main.class.getResource("/image/cardmask.bmp");
//		
//		convertImage(new File(url.toURI()));
	}
	
	public static void convertImage(File imageFile) {
        if (imageFile == null || imageFile.isDirectory()) {
            return;
        }
        //获取image buffered
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert image != null;
        //获取图片高度
        int height = image.getHeight();
        //获取图片宽度
        int width = image.getWidth();
        //生成ImageIcon对象
        ImageIcon imageIcon = new ImageIcon(image);
        //载入原图片数据
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        //创建画笔对象
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        //绘制Image图片
        graphics2D.drawImage(imageIcon.getImage(), 0, 0, null);
        //图片透明度
        int alpha = 0;
        //遍历图片y坐标
        for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
            //遍历图片x坐标
            for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                alpha = includeColor(rgb) ? 0 : 255;
                rgb = (alpha << 24) | (rgb & 0X00FFFFFF);
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        //将处理后的色块buffered对象写入缓冲区
        graphics2D.drawImage(bufferedImage, 0, 0, null);
        //创建输出路径
        File outFile = new File(imageFile.getParentFile().getPath() + "/temp/");
        //检测输出路径是否存在
        boolean exists = outFile.exists() || outFile.mkdir();
        if (exists) {
            //获取文件名不含后缀
            String fileName = imageFile.getName().substring(0, imageFile.getName().lastIndexOf("."));
            //创建输出图片文件对象
            File outImageFile = new File(outFile.getPath() + "/" + fileName + ".png");
            try {
                //输出图片
                ImageIO.write(bufferedImage, "png", outImageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //输出
            System.out.println(outImageFile.getPath() + " 转换完成!");
        }
    }

	/**
     * 判断当前色块是否属于设定值的范围
     * @param color 当前颜色
     * @return false|true
     */
    public static boolean includeColor(int color) {
        int red = (color & 0xFF0000) >> 16;
        int green = (color & 0x00FF00) >> 8;
        int blue = (color & 0x0000FF);
        int color_range = 215;
        return (red >= color_range && green >= color_range && blue >= color_range);
    }
}
