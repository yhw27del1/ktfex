package com.wisdoor.core.utils.kindeditor.upload; 
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


/**
 * 实现KindEditor图片上传的Servlet
 * 
 * @author  
 * 
 * @since 2012/03/21 20:20:23
 */
public class ImageUtil {
	
	public static int ImgWidth = -1;
	
	public static int ImgHeight = -1;


	
	/**
	 * 压缩图片
	 * 
	 * @param imgsrc
	 *            源文件
	 * @param imgdist
	 *            目标文件
	 * @param widthdist
	 *            宽
	 * @param heightdist
	 *            高
	 */
	public static void resizeImg(String imgsrc, String imgdist,
			int widthdist, int heightdist) {
		try {
			File srcfile = new File(imgsrc);
			if (!srcfile.exists()) {
				return;
			}
			Image src = ImageIO.read(srcfile);		
			ImgWidth = src.getWidth(null);
			ImgHeight = src.getHeight(null);
			if(ImgWidth < widthdist){
				widthdist = ImgWidth;
			}else{
				ImgWidth = widthdist;
			}
			if(ImgHeight < heightdist){
				heightdist = ImgHeight;
			}else{
				ImgHeight = heightdist;
			}
			BufferedImage tag = new BufferedImage(widthdist, heightdist,BufferedImage.TYPE_INT_RGB);	
			
			tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist,Image.SCALE_SMOOTH), 0, 0, null);
			FileOutputStream out = new FileOutputStream(imgdist);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}