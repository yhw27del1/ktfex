package com.kmfex.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springannotationplugin.Properties;

import com.kmfex.action.cmb.CMBVO;
import com.kmfex.cmb.request.merchant.MerchantRequest6500;
import com.kmfex.hxbank.HxbankParam;
import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.KmfexTradeMarket;
import com.kmfex.model.OpenCloseDeal;
import com.kmfex.service.KmfexTradeMarketService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.cmb.CmbDealService;
import com.kmfex.service.hx.HxbankDealService;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
@Service
public class OpenCloseDealServiceImpl  extends BaseServiceImpl<OpenCloseDeal> implements OpenCloseDealService{
	
	@Resource private KmfexTradeMarketService kmfexTradeMarketService;
	@Resource private HxbankDealService hxbankDealService;
	@Resource private CmbDealService cmbDealService;
	
	@Properties(name="cmb")
	private String cmb;
	
	@Properties(name="hxb")
	private String hxb;
	
	//return 0；情形0：交易规则管理无启用记录
	//return 1；OpenCloseDeal表中最后一条成功记录为“开市”
	//return 2；OpenCloseDeal表中最后一条成功记录为“休市”
	//return 3；OpenCloseDeal表中最后一条成功记录为“清算”
	//return 4；OpenCloseDeal表中最后一条成功记录为“对账”
	//return 5；OpenCloseDeal表中最后一条成功记录为“开夜市”
	//return 6；OpenCloseDeal表中最后一条成功记录为“休夜市”
	//return -1；请联系管理员
	@Override
	@Transactional
	public byte checkState(){
		byte state = 0;//返回0表示交易规则管理无启用记录
		List<KmfexTradeMarket> ls = this.kmfexTradeMarketService.getCommonListData("from KmfexTradeMarket where enabled = true");
		if(null==ls||ls.size()==0||ls.size()>1){//没有交易规则或有超过1条的交易规则
			state = 0;
		}else{
			String all = "select m.memo,m.name,m.success,to_char(m.createdate,'yyyy-MM-dd HH24:mi:ss') as createdate from (select * from t_opan_close oc where oc.success=1 order by oc.createdate desc) m  where rownum=1";
			try {
				ArrayList<LinkedHashMap<String,Object>> first = this.selectListWithJDBC(all);
				if(null!=first&&first.size()==1){
					String name = first.get(0).get("name").toString().trim();
					if(null==name||"".equals(name)){
						state = -1;
					}else{
						if(name.equals("开市")){
							state = 1;
						}else if(name.equals("休市")){
							state = 2;
						}else if(name.equals("清算")){
							state = 3;
						}else if(name.equals("对账")){
							state = 4;
						}else if(name.equals("开夜市")){
							state = 5;
						}else if(name.equals("休夜市")){
							state = 6;
						}else{
							state = -1;
						}
					}
				}else{
					state = -1;
				}
			} catch (Exception e) {
				state = -1;
			}
		}
		return state;
	}
	
	/**  
     * 产生20位的交易流水
     * @return yyMMddHHmmssSSSxxxxx 
     */  
    public String generateNo20(){
		//yyMMddHHmmssSSS：年月日时分秒毫秒，共15位
		//2位年，2位月，2位日，2位时，2位分，2位秒，3位毫秒
		SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmmssSSS");
		Date today = new Date();
		return (f.format(today)+(String.valueOf(Math.random()).substring(2))).substring(0, 20);
	}
	
	@Override
	@Transactional
	public OpenCloseDeal open_close(String name, User u) {
		OpenCloseDeal d = new OpenCloseDeal();
		d.setCreateDate(new Date());
		d.setOperator(u);
		d.setName(name);
		HxbankVO vo = new HxbankVO();
		System.out.println("hxb:"+this.hxb.trim());
		System.out.println("cmb:"+this.cmb.trim());
		boolean sign_hxb = false;//华夏签到或签退成功？
		boolean sign_cmb = false;//招行签到或签退成功？
		CMBVO cmbvo = new CMBVO();
		//华行
		if("on".equals(this.hxb.trim())){
			//华夏正式环境签到
			HxbankParam p = new HxbankParam();
			if("开市".equals(name)){
				try {
					vo = this.hxbankDealService.sign_in(p,u);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if("休市".equals(name)){
				try {
					vo = this.hxbankDealService.sign_off(p,u);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sign_hxb = vo.isFlag();//华夏正式环境签到，成功则true，失败则false
			if(!vo.isFlag()){
				d.setMemo("华夏"+vo.getTip());
			}
		}else{
			sign_hxb = true;//关闭接口或测试环境，则华夏签到默认成功
		}
		
		//招行
		if("on".equals(this.cmb.trim())){
			//招行正式环境签到
			if("开市".equals(name)){
				try {
					String txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
					String txTime = DateUtils.formatDate(new Date(), "HHmmss");
					MerchantRequest6500 request6500 = new MerchantRequest6500();
					request6500.setCoSerial(generateNo20());
					request6500.setCoTime(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
					request6500.setTransDate(DateUtils.formatDate(new Date(), "yyyyMMdd"));
					cmbvo = this.cmbDealService.request6500(request6500, u.getId(), txDate, txTime);
					sign_cmb = cmbvo.isSuccess();//招商正式环境签到，成功则true，失败则false
					if(!cmbvo.isSuccess()){
						d.setMemo(d.getMemo()+"@招商"+cmbvo.getMsg());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if("休市".equals(name)){//招行无休市接口
				sign_cmb = true;
			}
		}else{
			sign_cmb = true;//关闭接口或测试环境，则招行签到默认成功
		}
		
		if(sign_hxb&&sign_cmb){
			d.setSuccess(true);
		}
		
		try {
			this.insert(d);
			return d;
		} catch (EngineException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	@Transactional
	public Date getLatestKaiShi(){
		Date date = null;
		String parten = "yyyy-MM-dd HH:mm:ss";
		String all = "select m.memo,m.name,m.success,to_char(m.createdate,'yyyy-MM-dd HH24:mi:ss') as createdate from (select * from t_opan_close oc where oc.success=1 and oc.name='开市' order by oc.createdate desc) m  where rownum=1";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.selectListWithJDBC(all);
			if(null!=first&&first.size()==1){
				String createdate = first.get(0).get("createdate").toString().trim();
				if(null!=createdate&&!"".equals(createdate)){
					date = DateUtils.parseDate(createdate,parten);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null==date){
			System.out.println("取最近的开市日期出错");
		}
		return date;
	}
	
	@Override
	@Transactional
	public OpenCloseDeal open_close_night(String name, User u) {
		OpenCloseDeal d = new OpenCloseDeal();
		d.setCreateDate(new Date());
		d.setOperator(u);
		d.setName(name);
		d.setSuccess(true);
		d.setMemo("夜市只能投标");
		try {
			this.insert(d);
			return d;
		} catch (EngineException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//OpenCloseDeal表中最后一条成功记录是“对账”:true
	//OpenCloseDeal表中最后一条成功记录不是“对账”:false
	//开夜市时会调用此方法
	@Override
	@Transactional
	public boolean isSuccessDuiZhang(){
		boolean r = false;
		String all = "select m.memo,m.name,m.success,to_char(m.createdate,'yyyy-MM-dd HH24:mi:ss') as createdate from (select * from t_opan_close oc where oc.success=1 order by oc.createdate desc) m  where rownum=1";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.selectListWithJDBC(all);
			if(null!=first&&first.size()==1){
				String name = first.get(0).get("name").toString().trim();
				if(null!=name&&!"".equals(name)&&"对账".equals(name)){
					r = true;
				}else{
					System.out.println("开夜市时：OpenCloseDeal表中最后一条成功记录为："+name);
				}
			}else{
				System.out.println("取OpenCloseDeal表中最后一条成功记录时出错");
			}
		} catch (Exception e) {
			System.out.println("取OpenCloseDeal表中最后一条成功记录时出错");
		}
		return r;
	}
	
	//OpenCloseDeal表中最后一条成功记录是“开夜市”:true
	//OpenCloseDeal表中最后一条成功记录不是“开夜市”:false
	//休夜市时会调用此方法
	@Override
	@Transactional
	public boolean isSuccessOpenNight(){
		boolean r = false;
		String all = "select m.memo,m.name,m.success,to_char(m.createdate,'yyyy-MM-dd HH24:mi:ss') as createdate from (select * from t_opan_close oc where oc.success=1 order by oc.createdate desc) m  where rownum=1";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.selectListWithJDBC(all);
			if(null!=first&&first.size()==1){
				String name = first.get(0).get("name").toString().trim();
				if(null!=name&&!"".equals(name)&&"开夜市".equals(name)){
					r = true;
				}else{
					System.out.println("休夜市时：OpenCloseDeal表中最后一条成功记录为："+name);
				}
			}else{
				System.out.println("取OpenCloseDeal表中最后一条成功记录时出错");
			}
		} catch (Exception e) {
			System.out.println("取OpenCloseDeal表中最后一条成功记录时出错");
		}
		return r;
	}

	public String getCmb() {
		return cmb;
	}

	public void setCmb(String cmb) {
		this.cmb = cmb;
	}

	public String getHxb() {
		return hxb;
	}

	public void setHxb(String hxb) {
		this.hxb = hxb;
	}
}
