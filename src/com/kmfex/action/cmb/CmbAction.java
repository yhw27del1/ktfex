package com.kmfex.action.cmb;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.cmb.request.merchant.MerChantRequest6200;
import com.kmfex.cmb.request.merchant.MerChantRequest6201;
import com.kmfex.cmb.request.merchant.MerChantRequest6203;
import com.kmfex.cmb.request.merchant.MerChantRequest6204;
import com.kmfex.cmb.request.merchant.MerchantRequest6100;
import com.kmfex.cmb.request.merchant.MerchantRequest6400;
import com.kmfex.cmb.request.merchant.MerchantRequest6410;
import com.kmfex.cmb.request.merchant.MerchantRequest6500;
import com.kmfex.cmb.request.merchant.MerchantRequest6600;
import com.kmfex.model.AccountDeal;
import com.kmfex.model.cmb.B01;
import com.kmfex.model.cmb.B02;
import com.kmfex.model.cmb.B03;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.SignHistoryService;
import com.kmfex.service.cmb.CmbDealService;
import com.linuxense.javadbf.DBFReader;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.struts.BaseAction;
/**
 * @author linuxp
 * */
@Controller
@Scope("prototype")
public class CmbAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Resource
	UserService userService;
	
	@Resource
	OpenCloseDealService openCloseDealService;
	
	@Resource
	private CmbDealService cmbDealService;
	
	@Resource
	private AccountDealService acccountDealService;
	
	@Resource
	private SignHistoryService signHistoryService;
	
	private String id;
	private String name;
	private double amount = 0d;
	private String txDate;
	private String txTime;
	
	private MerchantRequest6600 request6600;
	private MerchantRequest6500 request6500;
	private MerchantRequest6410 request6410;
	private MerchantRequest6400 request6400;
	private MerChantRequest6204 request6204;
	private MerChantRequest6203 request6203;
	private MerChantRequest6201 request6201;
	private MerChantRequest6200 request6200;
	private MerchantRequest6100 request6100;
	
    public static PrintWriter getOut() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		return out;
	}
	
	//申请密钥
	public String request6600(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.memo from cmb_deal cd,sys_user u where cd.txCode='6600' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6600";
	}
	
	public String request6600_do(){
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo = new CMBVO();
		this.request6600 = new MerchantRequest6600();
		this.request6600.setCoSerial(DateUtils.generateNo20());
		try {
			vo = this.cmbDealService.request6600(this.request6600, this.getLoginUser().getId(),this.txDate,this.txTime);
			getOut().write(vo.getJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//日初签到
	public String request6500(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.coTime,cd.transDate,cd.memo from cmb_deal cd,sys_user u where cd.txCode='6500' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6500";
	}
	
	public String request6500_do(){
		//签到前先执行更新密钥操作
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo_gx = new CMBVO();
		this.request6600 = new MerchantRequest6600();
		this.request6600.setCoSerial(DateUtils.generateNo20());
		try {
			vo_gx = this.cmbDealService.request6600(this.request6600, this.getLoginUser().getId(),this.txDate,this.txTime);
			if(vo_gx.isSuccess()){//更新密钥成功
				CMBVO vo = new CMBVO();
				this.request6500 = new MerchantRequest6500();
				this.request6500.setCoSerial(DateUtils.generateNo20());
				this.request6500.setCoTime(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
				this.request6500.setTransDate(DateUtils.formatDate(new Date(), "yyyyMMdd"));
				
				try {
					vo = this.cmbDealService.request6500(this.request6500, this.getLoginUser().getId(),this.txDate,this.txTime);
					getOut().write(vo.getJSON());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				getOut().write(vo_gx.getJSON());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//查询交易所结算账户余额
	public String request6410(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.coTime,cd.transDate,cd.memo,cd.bankacc,cd.fundbal_,cd.funduse_ from cmb_deal cd,sys_user u where cd.txCode='6410' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6410";
	}
	
	public String request6410_do(){
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo = new CMBVO();
		this.request6410.setCoSerial(DateUtils.generateNo20());
		this.request6410.setCurCode("CNY");
		try {
			vo = this.cmbDealService.request6410(this.request6410, this.getLoginUser().getId(),this.txDate,this.txTime);
			getOut().write(vo.getJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//查询银行账户及余额
	public String request6400(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.coTime,cd.transDate,cd.memo,cd.fundacc,cd.fundbal_,cd.funduse_ from cmb_deal cd,sys_user u where cd.txCode='6400' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6400";
	}
	
	public String request6400_do(){
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo = new CMBVO();
		this.request6400.setCoSerial(DateUtils.generateNo20());
		this.request6400.setCurCode("CNY");
		try {
			vo = this.cmbDealService.request6400(this.request6400, this.getLoginUser().getId(),this.txDate,this.txTime);
			getOut().write(vo.getJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//冲正银行转交易所(入金)
	public String request6204(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.memo,cd.fundAcc,cd.bankAcc,cd.idno,cd.custname,cd.amount_ from cmb_deal cd,sys_user u where cd.txCode='6204' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6204";
	}
	
	public String request6204_do(){
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo = new CMBVO();
		if(this.amount>0){
			this.request6204.setCoSerial(DateUtils.generateNo20());
			this.request6204.setCurCode("CNY");
			this.request6204.setCurFlag("1");
			this.request6204.setCountry("CHN");
			String formatAmount = DoubleUtils.formatDouble(this.amount);
			this.request6204.setAmount(formatAmount);
			try {
				vo = this.cmbDealService.request6204(this.request6204, this.getLoginUser().getId(),this.txDate,this.txTime);
				getOut().write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				vo.setMsg("金额必须大于0元");
				getOut().write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//重发交易所转银行(出金)
	public String request6203(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.memo,cd.fundAcc,cd.bankAcc,cd.idno,cd.custname,cd.amount_ from cmb_deal cd,sys_user u where cd.txCode='6203' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6203";
	}
	
	public String request6203_do(){
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo = new CMBVO();
		if(this.amount>0){
			this.request6203.setCoSerial(DateUtils.generateNo20());
			this.request6203.setCurCode("CNY");
			this.request6203.setCurFlag("1");
			this.request6203.setCountry("CHN");
			String formatAmount = DoubleUtils.formatDouble(this.amount);
			this.request6203.setAmount(formatAmount);
			try {
				vo = this.cmbDealService.request6203(this.request6203, this.getLoginUser().getId(),this.txDate,this.txTime);
				getOut().write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				vo.setMsg("金额必须大于0元");
				getOut().write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//交易所转银行(出金)
	public String request6201(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.memo,cd.fundAcc,cd.bankAcc,cd.idno,cd.custname,cd.amount_ from cmb_deal cd,sys_user u where cd.txCode='6201' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6201";
	}
	
	//出金申请，审核通过时才调用6201接口
	public String request6201_do(){
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo = new CMBVO();
		if(this.amount>0){
			try {
				User u = userService.findUser(this.request6201.getFundAcc());
				boolean f = this.acccountDealService.in_out_merchant(u.getUserAccount(), this.amount, AccountDeal.ZQ2BANK, "25",null,DateUtils.generateNo20());
				if(f){
					vo.setSuccess(true);
					vo.setMsg("出金申请成功,等待审核");
				}else{
					vo.setSuccess(false);
					vo.setMsg("出金申请失败,余额不足");
				}
				getOut().write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				vo.setMsg("金额必须大于0元");
				getOut().write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//银行转交易所(入金)
	public String request6200(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.memo,cd.fundAcc,cd.bankAcc,cd.idno,cd.custname,cd.amount_ from cmb_deal cd,sys_user u where cd.txCode='6200' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6200";
	}
	
	//入金
	public String request6200_do(){
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo = new CMBVO();
		if(this.amount>0){
			this.request6200.setCoSerial(DateUtils.generateNo20());
			this.request6200.setCurCode("CNY");
			this.request6200.setCurFlag("1");
			this.request6200.setCountry("CHN");
			String formatAmount = DoubleUtils.formatDouble(this.amount);
			this.request6200.setAmount(formatAmount);
			try {
				vo = this.cmbDealService.request6200(this.request6200, this.getLoginUser().getId(),this.txDate,this.txTime);
				if(vo.isSuccess()){
					User u = userService.findUser(this.request6200.getFundAcc());
					this.acccountDealService.in_out_merchant(u.getUserAccount(), this.amount, AccountDeal.BANK2ZQ, "24",null,this.request6200.getCoSerial());
				}
				getOut().write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				vo.setMsg("金额必须大于0元");
				getOut().write(vo.getJSON());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//指定银商转账银行(预签约)
	public String request6100(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select to_char(cd.createDate,'yyyy-MM-dd HH24:mi:ss') as createDate,cd.success,cd.name,cd.txCode,cd.coSerial,cd.bkSerial,u.username,u.realname,cd.memo,cd.fundAcc,cd.bankAcc,cd.idno,cd.custname,cd.busiType,cd.accType from cmb_deal cd,sys_user u where cd.txCode='6100' and cd.operator=u.id and (cd.createDate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) order by cd.createDate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.cmbDealService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "request6100";
	}
	
	public String request6100_do(){
		this.txDate = DateUtils.formatDate(new Date(), "yyyyMMdd");
		this.txTime = DateUtils.formatDate(new Date(), "HHmmss");
		CMBVO vo = new CMBVO();
		this.request6100.setCoSerial(DateUtils.generateNo20());
		this.request6100.setCurCode("CNY");
		this.request6100.setCountry("CHN");
		try {
			vo = this.cmbDealService.request6100(this.request6100, this.getLoginUser().getId(),this.txDate,this.txTime);
			getOut().write(vo.getJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String file = File.separator + "home" + File.separator + "chinacmb" + File.separator;
	
	//转账交易对账明细，B01
	public String transfer(){
		this.checkDate();
		String b01 = this.file+"B01"+DateUtils.formatDate(this.getStartDate(), "yyyyMMdd")+".dbf";
		try {
			List<B01> list = new ArrayList<B01>();
			File f1 = new File(b01);
			if(f1.exists()){
				FileInputStream fin = new FileInputStream(f1);
				DBFReader r = new DBFReader(fin);
				r.setCharactersetName("GB18030");
				int lie = r.getFieldCount();//字段列
				int hang = r.getRecordCount();//记录行
				for(int i=0;i<hang;i++){//记录行的遍历
					B01 b = new B01();
					Class clazz = Class.forName(B01.class.getName());
					Object[] record = r.nextRecord();//记录行
					//System.out.println("++++++++++第"+(i+1)+"行++++++++++");
					if(null!=record){
						for(int field=0;field<lie;field++){//每行的字段列遍历
							Field f = clazz.getField(r.getField(field).getName());
							f.set(b, record[field].toString().trim());
							//System.out.println(r.getField(field).getName()+":"+record[field].toString().trim());
						}
						String amount = DoubleUtils.formatString(DoubleUtils.doubleCheck2(Double.parseDouble(b.getAMOUNT()), 0), 15, true, true);
						b.setAMOUNT(amount);
						b.setAMOUNT_(DoubleUtils.toDouble(b.getAMOUNT()));
						list.add(b);
					}
				}
				if(null!=list&&list.size()>0){
					ServletActionContext.getRequest().setAttribute("data", list);
				}else{
					ServletActionContext.getRequest().setAttribute("data", null);
					ServletActionContext.getRequest().setAttribute("msg", "改交易日无转账类交易数据");
				}
				f1 = null;
				r = null;
				if(null!=fin){
					fin.close();
				}
			}else{
				ServletActionContext.getRequest().setAttribute("data", null);
				ServletActionContext.getRequest().setAttribute("msg", "文件\""+b01+"\"不存在，招行对账文件未就绪，请联系管理员");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ServletActionContext.getRequest().setAttribute("data", null);
			ServletActionContext.getRequest().setAttribute("msg", "出现异常，请联系管理员");
		}
		return "transfer";
	}
	
	//客户协议状态(增量)对账文件，B02
	public String protocol_state(){
		this.checkDate();
		String b02 = this.file+"B02"+DateUtils.formatDate(this.getStartDate(), "yyyyMMdd")+".dbf";
		try {
			List<B02> list = new ArrayList<B02>();
			File f1 = new File(b02);
			if(f1.exists()){
				FileInputStream fin = new FileInputStream(f1);
				DBFReader r = new DBFReader(fin);
				r.setCharactersetName("GB18030");
				int lie = r.getFieldCount();//字段列
				int hang = r.getRecordCount();//记录行
				for(int i=0;i<hang;i++){//记录行的遍历
					B02 b = new B02();
					Class clazz = Class.forName(B02.class.getName());
					Object[] record = r.nextRecord();//记录行
					//System.out.println("++++++++++第"+(i+1)+"行++++++++++");
					if(null!=record){
						for(int field=0;field<lie;field++){//每行的字段列遍历
							Field f = clazz.getField(r.getField(field).getName());
							f.set(b, record[field].toString().trim());
							//System.out.println(r.getField(field).getName()+":"+record[field].toString().trim());
						}
						list.add(b);
					}
				}
				if(null!=list&&list.size()>0){
					ServletActionContext.getRequest().setAttribute("data", list);
				}else{
					ServletActionContext.getRequest().setAttribute("data", null);
					ServletActionContext.getRequest().setAttribute("msg", "改交易日无协议状态数据");
				}
				f1 = null;
				r = null;
				if(null!=fin){
					fin.close();
				}
			}else{
				ServletActionContext.getRequest().setAttribute("data", null);
				ServletActionContext.getRequest().setAttribute("msg", "文件\""+b02+"\"不存在，招行对账文件未就绪，请联系管理员");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ServletActionContext.getRequest().setAttribute("data", null);
			ServletActionContext.getRequest().setAttribute("msg", "出现异常，请联系管理员");
		}
		return "protocol_state";
	}
	
	//账户类交易对账明细文件，B03
	public String protocol_process(){
		this.checkDate();
		String b03 = this.file+"B03"+DateUtils.formatDate(this.getStartDate(), "yyyyMMdd")+".dbf";
		try {
			List<B03> list = new ArrayList<B03>();
			File f1 = new File(b03);
			if(f1.exists()){
				FileInputStream fin = new FileInputStream(f1);
				DBFReader r = new DBFReader(fin);
				r.setCharactersetName("GB18030");
				int lie = r.getFieldCount();//字段列
				int hang = r.getRecordCount();//记录行
				for(int i=0;i<hang;i++){//记录行的遍历
					B03 b = new B03();
					Class clazz = Class.forName(B03.class.getName());
					Object[] record = r.nextRecord();//记录行
					//System.out.println("++++++++++第"+(i+1)+"行++++++++++");
					if(null!=record){
						for(int field=0;field<lie;field++){//每行的字段列遍历
							Field f = clazz.getField(r.getField(field).getName());
							f.set(b, record[field].toString().trim());
							//System.out.println(r.getField(field).getName()+":"+record[field].toString().trim());
						}
						list.add(b);
					}
				}
				if(null!=list&&list.size()>0){
					ServletActionContext.getRequest().setAttribute("data", list);
				}else{
					ServletActionContext.getRequest().setAttribute("data", null);
					ServletActionContext.getRequest().setAttribute("msg", "改交易日无协议类交易数据");
				}
				f1 = null;
				r = null;
				if(null!=fin){
					fin.close();
				}
			}else{
				ServletActionContext.getRequest().setAttribute("data", null);
				ServletActionContext.getRequest().setAttribute("msg", "文件\""+b03+"\"不存在，招行对账文件未就绪，请联系管理员");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ServletActionContext.getRequest().setAttribute("data", null);
			ServletActionContext.getRequest().setAttribute("msg", "出现异常，请联系管理员");
		}
		return "protocol_process";
	}
	
	public String history(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.checkDate();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		String s = format.format(this.getStartDate());
		String e = format.format(endDateNext);
		String sql = "select u.username,u.realname,u.accountno,s.name,s.signbank,s.signtype,to_char(s.syndate_market,'yyyy-MM-dd HH24:mi:ss') as syndate_market,to_char(s.signdate,'yyyy-MM-dd HH24:mi:ss') as signdate,to_char(s.surrenderdate,'yyyy-MM-dd HH24:mi:ss') as surrenderdate,s.memo from t_signhistory s,sys_user u where s.owner=u.id and s.success=1 and (s.createdate between to_date('"+s+"','yyyy-MM-dd') and to_date('"+e+"','yyyy-MM-dd')) ";
		if (null != this.getKeyWord() && !"".equals(this.getKeyWord().trim())) {
			this.setKeyWord(this.getKeyWord().trim());
			sql += " and ( u.username like '%"+this.getKeyWord()+"%' ";
			sql += " or u.realname like '%"+this.getKeyWord()+"%' ";
			sql += " ) ";
		}
		sql += " order by s.createdate desc ";
		try {
			ArrayList<LinkedHashMap<String,Object>> first = this.signHistoryService.selectListWithJDBC(sql);
			ServletActionContext.getRequest().setAttribute("data", first);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "history";
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public MerchantRequest6600 getRequest6600() {
		return request6600;
	}

	public void setRequest6600(MerchantRequest6600 request6600) {
		this.request6600 = request6600;
	}

	public MerchantRequest6500 getRequest6500() {
		return request6500;
	}

	public void setRequest6500(MerchantRequest6500 request6500) {
		this.request6500 = request6500;
	}

	public MerchantRequest6410 getRequest6410() {
		return request6410;
	}

	public void setRequest6410(MerchantRequest6410 request6410) {
		this.request6410 = request6410;
	}

	public MerchantRequest6400 getRequest6400() {
		return request6400;
	}

	public void setRequest6400(MerchantRequest6400 request6400) {
		this.request6400 = request6400;
	}

	public MerChantRequest6204 getRequest6204() {
		return request6204;
	}

	public void setRequest6204(MerChantRequest6204 request6204) {
		this.request6204 = request6204;
	}

	public MerChantRequest6203 getRequest6203() {
		return request6203;
	}

	public void setRequest6203(MerChantRequest6203 request6203) {
		this.request6203 = request6203;
	}

	public MerChantRequest6201 getRequest6201() {
		return request6201;
	}

	public void setRequest6201(MerChantRequest6201 request6201) {
		this.request6201 = request6201;
	}

	public MerChantRequest6200 getRequest6200() {
		return request6200;
	}

	public void setRequest6200(MerChantRequest6200 request6200) {
		this.request6200 = request6200;
	}

	public MerchantRequest6100 getRequest6100() {
		return request6100;
	}

	public void setRequest6100(MerchantRequest6100 request6100) {
		this.request6100 = request6100;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getTxDate() {
		return txDate;
	}

	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}

	public String getTxTime() {
		return txTime;
	}

	public void setTxTime(String txTime) {
		this.txTime = txTime;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}