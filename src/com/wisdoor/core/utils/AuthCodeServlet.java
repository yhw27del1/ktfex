package com.wisdoor.core.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/****
 * 通用验证码
 * @author 
 */

public class AuthCodeServlet extends HttpServlet
{
  private static final long serialVersionUID = 20060808L;
  private static final String CONTENT_TYPE = "image/jpeg";
  private static final int DEFAULT_WIDTH = 100;
  private static final int DEFAULT_HEIGHT = 35;
  private static final int DEFAULT_LENGTH = 5;
  public static final String DEFAULT_CODETYPE = "2";
  private String CodeType;
  private String AuthKey;
  private int Width;
  private int Height;
  private int Length;
  private OutputStream out;
  private Random rand = new Random(System.currentTimeMillis());
  private String seed;
  private BufferedImage image;
  //static char[] arr = "23456789qwertyuipasdfghjkzxcvbnm".toCharArray();
  static char[] arr = "2345678901".toCharArray();

  public void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    try
    {
      this.CodeType = request.getParameter("CodeType");
      this.AuthKey = request.getParameter("AuthKEY");
      String tWidth = request.getParameter("Width");
      String tHeight = request.getParameter("Height");
      String tLength = request.getParameter("Length");
      if ((this.CodeType == null) || (this.CodeType.equals(""))) {
        this.CodeType =DEFAULT_CODETYPE;
      }
      if ((this.AuthKey == null) || (this.AuthKey.equals(""))) {
        this.AuthKey = "_TXPT_AUTHKEY";
      }
      if ((tWidth == null) || (tWidth.equals(""))) {
        this.Width = DEFAULT_WIDTH;
      }
      if ((tHeight == null) || (tHeight.equals(""))) {
        this.Height = DEFAULT_HEIGHT;
      }
      if ((tLength == null) || (tLength.equals("")))
        this.Length = DEFAULT_LENGTH;
      try
      {
        this.Width = Integer.parseInt(tWidth);
      } catch (Exception ex) {
        this.Width = 100;
      }
      try {
        this.Height = Integer.parseInt(tHeight);
      } catch (Exception ex) {
        this.Height = 35;
      }
      try {
        this.Length = Integer.parseInt(tLength);
      } catch (Exception ex) {
        this.Length = 5;
      }
      response.setContentType(CONTENT_TYPE);
      response.setHeader("Pragma", "No-cache");
      response.setHeader("Cache-Control", "no-cache");
      response.setDateHeader("Expires", 0L);
      this.out = response.getOutputStream();
      this.seed = getSeed();
      request.getSession().setAttribute(this.AuthKey,this.seed);
      if (this.CodeType.equals("1"))
        code1(request, response);
      else if (this.CodeType.equals("2"))
        code2(request, response);
      else if (this.CodeType.equals("3"))
        code3(request, response);
      try
      {
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(this.out);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(this.image);
        param.setQuality(1.0F, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(this.image);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      this.out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private BufferedImage code1(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    this.image = new BufferedImage(this.Width, this.Height, 1);
    Graphics g = this.image.getGraphics();
    g.setColor(new Color(245, 245, 245));
    g.fillRect(0, 0, this.Width, this.Height);
    g.setColor(Color.DARK_GRAY);
    g.setFont(new Font("Arial", 0, 11));
    g.drawString(this.seed, 3, 11);
    g.dispose();
    return this.image;
  }

  private BufferedImage code2(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
{
	image = new BufferedImage(Width, Height, 1);
	Graphics g = image.getGraphics();
	g.setColor(getRandColor(200, 250));
	g.fillRect(0, 0, Width, Height);
	g.setFont(new Font("Arial", 0, 28));
	g.setColor(getRandColor(160, 200));
	// 随机产生120个干扰点
	for (int i = 0; i < 120; i++) {
		int x = (int) (Math.random() * Width);
		int y = (int) (Math.random() * Height);
		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);
		g.setColor(new Color(red, green, blue));
		g.drawOval(x, y, 1, 0);
	}
	for (int i = 0; i < Length; i++)
	{
		String c = seed.substring(i, i + 1);
		g.setColor(new Color(20 + rand.nextInt(110), 20 + rand.nextInt(110), 20 + rand.nextInt(110)));
		g.drawString(c, 18 * i + 3, 28);
	}

	g.dispose();
	return image;
}

  private BufferedImage code3(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    this.image = new BufferedImage(this.Width, this.Height, 1);
    Graphics2D g = (Graphics2D)this.image.getGraphics();
    g.setColor(getRandColor(200, 250));
    g.fillRect(0, 0, this.Width, this.Height);
    g.setFont(new Font("Arial", 0, 28));

    g.setColor(getRandColor(160, 200));
    for (int i = 0; i < 180; ++i) {
      int x = this.rand.nextInt(this.Width);
      int y = this.rand.nextInt(this.Height);
      int xl = this.rand.nextInt(12);
      int yl = this.rand.nextInt(12);
      g.drawLine(x, y, x + xl, y + yl);
    }

    AffineTransform fontAT = new AffineTransform();
    for (int i = 0; i < this.Length; ++i) {
      String c = this.seed.substring(i, i + 1);
      g.setColor(new Color(20 + this.rand.nextInt(110), 20 + this.rand.nextInt(110), 20 + this.rand.nextInt(110)));
      fontAT.shear(this.rand.nextFloat() * 0.6D - 0.3D, 0.0D);
      FontRenderContext frc = g.getFontRenderContext();
      Font theDerivedFont = g.getFont().deriveFont(fontAT);
      TextLayout tstring2 = new TextLayout(c, theDerivedFont, frc);
      tstring2.draw(g, 18 * i + 3, 28.0F);
    }
    g.dispose();
    return this.image;
  }

  private String getSeed()
  {
    StringBuffer sb = new StringBuffer(this.Length);
    for (int i = 0; i < this.Length; ++i) {
      sb.append(arr[this.rand.nextInt(arr.length)]);
    }
    return sb.toString();
  }

  private Color getRandColor(int fc, int bc) {
    if (fc > 255)
      fc = 255;
    if (bc > 255)
      bc = 255;
    int r = fc + this.rand.nextInt(bc - fc);
    int g = fc + this.rand.nextInt(bc - fc);
    int b = fc + this.rand.nextInt(bc - fc);

    return new Color(r, g, b);
  }
}
