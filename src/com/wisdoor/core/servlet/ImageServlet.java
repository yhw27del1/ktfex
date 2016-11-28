package com.wisdoor.core.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("image/jpeg");
		HttpSession session = request.getSession();
		int width = 250;
		int height = 43;

		// 设置浏览器不要缓存此图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 创建内存图像并获得其图形上下文
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		// 产生随机验证码
		// 定义验证码的字符表
		String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] rands = new char[5];
		for (int i = 0; i < 5; i++) {
			int rand = (int) (Math.random() * 36);
			rands[i] = chars.charAt(rand);
		}

		// 产生图像
		// 画背景
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, width, height);
		// 随机产生120个干扰点
		for (int i = 0; i < 120; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			int red = (int) (Math.random() * 255);
			int green = (int) (Math.random() * 255);
			int blue = (int) (Math.random() * 255);
			g.setColor(new Color(red, green, blue));
			g.drawOval(x, y, 1, 0);
		}

		
		g.setFont(new Font(null, Font.ROMAN_BASELINE, 30));
		// 在不同的高度上输出验证码的不同字符
		g.setColor(Color.BLACK);
		g.drawString("" + rands[0], 5, 30);
		g.drawString("" + rands[1], 33, 40);
		g.drawString("" + rands[2], 65, 30);
		g.drawString("" + rands[3], 95, 36);
		g.drawString("" + rands[4], 135, 30);

		g.dispose();

		// 将图像输出到客户端
		ServletOutputStream sos = response.getOutputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", baos);
		byte[] buffer = baos.toByteArray();
		response.setContentLength(buffer.length);
		sos.write(buffer);
		baos.close();
		sos.close();

		// 将验证码放到 session 中
		session.setAttribute("checkCode", new String(rands));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}