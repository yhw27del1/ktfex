package com.kmfex.hxbank;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class HXBankServletLoad extends HttpServlet{
/**
	 * 
	 */
	private static final long serialVersionUID = 417284250685638711L;

	//private HttpclientHelper clientHelper;
	
	//private ConfigManager cfg;
	
	@Override
	public void init() throws ServletException {
		//cfg=new ConfigManager();
		//clientHelper=new HttpclientHelper(cfg.HOST,cfg.PORT);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		HxbankParam p = new HxbankParam();
		p.setCode("DZ015");
		p.setMerchantTrnxNo(HXBankUtil.generateNo20());
		String json = p.getJSON(p);
		NameValuePair one = new NameValuePair("json",json);
		NameValuePair two = new NameValuePair("trnxCode",p.getCode());
		String result = clientHelper.executeMethod(cfg.URL, new NameValuePair[]{one,two});
		//ResponseDZ015 o = new GsonBuilder().create().fromJson(result, ResponseDZ015.class);
		PrintWriter writer = null;
		try {
            writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(),"utf-8"));
            writer.write(result);
            writer.flush();
            writer.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        */
	}
}
