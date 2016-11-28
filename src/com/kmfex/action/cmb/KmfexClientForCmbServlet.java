package com.kmfex.action.cmb;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.cmb.CmbDealService;

//招商银行cmb服务器调用此servlet
public class KmfexClientForCmbServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7882549789140208902L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ApplicationContext cxt = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()); 
		CmbDealService cmbDealService = (CmbDealService)cxt.getBean("cmbDealImpl");
		OpenCloseDealService openCloseDealService = (OpenCloseDealService)cxt.getBean("openCloseDealServiceImpl");
		String TxCode = req.getParameter("TxCode");
		String json = req.getParameter("json");
		String msg = "";
		if(null!=TxCode&&!"".equals(TxCode)){
			if(null!=json&&!"".equals(json)){
				System.out.println("TxCode:"+TxCode);
				System.out.println("json:"+json);
				//state=1;开市
				//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
				//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
				//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
				//state=5;开夜市：只能投标
				//state=6;休夜市：一切业务停止
				byte state = 0;
				if("5600".equals(TxCode)||"5500".equals(TxCode)){//5600,5500交易不受交易时间的限制
					state = 1;//招商银行发起5600或5500交易，则系统按开市处理
				}else{//招商银行发起其他的交易，则取系统当前的实时开市状态
					state = openCloseDealService.checkState();
				}
				if (state!=1) {
					msg = "非交易时间";
				}else{
					if("5600".equals(TxCode)){//密钥更换通知
						msg = cmbDealService.request5600(json);
					}else if("5500".equals(TxCode)){//对账就绪通知
						msg = cmbDealService.request5500(json);
					}else if("5400".equals(TxCode)){//查询资金账户余额，在交易所的账户余额信息
						msg = cmbDealService.request5400(json);
					}else if("5205".equals(TxCode)){//冲正交易所转银行，冲正出金，需要验证密码
						msg = cmbDealService.request5205(json);
					}else if("5202".equals(TxCode)){//重发银行转交易所，重发入金
						msg = cmbDealService.request5202(json);
					}else if("5201".equals(TxCode)){//交易所转银行，出金(商转银)，需要验证密码
						msg = cmbDealService.request5201(json);
					}else if("5200".equals(TxCode)){//银行转交易所，入金(银转商)
						msg = cmbDealService.request5200(json);
					}else if("5101".equals(TxCode)){//关闭银商转账服务，解约，需要验证密码
						msg = cmbDealService.request5101(json);
					}else if("5104".equals(TxCode)){//激活银商转账服务，该业务与6100配套，暂时未开放给交易所，需要验证密码
						msg = cmbDealService.request5104(json);
					}else if("5100".equals(TxCode)){//一步式签约，需要验证密码
						msg = cmbDealService.request5100(json);
					}else{
						msg = "交易代码错误";
					}
				}
			}else{
				msg = "交易报文错误";
			}
		}else{
			msg = "交易代码错误";
		}
		PrintWriter writer = null;
        try {
            writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(),"UTF-8"));
            writer.write(msg);
            writer.flush();
            writer.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
	}
}