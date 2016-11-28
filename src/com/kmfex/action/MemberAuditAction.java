package com.kmfex.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.MemberAudit;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberType;
import com.kmfex.service.MemberAuditService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.hx.HxbankDealService;
import com.kmfex.util.ReadsStaticConstantPropertiesUtil;
import com.kmfex.util.SMSNewUtil;
import com.kmfex.util.SMSUtil;
import com.kmfex.webservice.client.WsInterfaceService;
import com.opensymphony.xwork2.ActionContext;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.UploadFile;
import com.wisdoor.core.model.User;
import com.wisdoor.core.service.AccountService;
import com.wisdoor.core.service.UploadFileService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.Constant;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.IpAddrUtil;
import com.wisdoor.core.utils.UploadFileUtils;
import com.wisdoor.core.utils.VelocityUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 会员审核的Action
 * 
 * @author
 * @version 
 * 
 *  修改记录 
 *  2012-08-01  audit方法里审核通过 时，增加 发短信功能
 *  2012-08-20  audit方法里审核通过 时，给融资方席位费相关字段赋初始值
 *  2013-05-28  audit方法里审核时，如果是第二次审核或更多，则不发送短信
 * */
@Controller
@Scope("prototype")
public class MemberAuditAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5700537697752131258L;

	private static final String MEMBER_AUDIT = "memberAudit";

	private static final String GET_BY_MEMBER_BASE = "getByMemberBase";

	private static final String AUDIT = "audit";

	private String memberId;

	private MemberBase memberBase;

	private List<UploadFile> files;

	private MemberAudit memberAudit;

	private String membertype;
	
	private String reason;

	private String state;
	@Resource
	private MemberBaseService memberBaseService;
	@Resource
	private MemberAuditService memberAuditService;
	@Resource
	private AccountService accountService;
	@Resource
	private UploadFileService uploadFileService;
	
	@Resource
	private WsInterfaceService wsInterfaceService; 
	
	@Resource
	private HxbankDealService hxbankDealService;
	
	@Resource
	private UserService userService;

	/**
	 * 营业执照扫描图
	 * */
	private File busLicense;
	private String busLicenseFileName;
	private String busLicenseContentType;

	/** 法人代表身份证 */
	private File legalPersonIdCard;
	private String legalPersonIdCardFileName;
	private String legalPersonIdCardContentType;

	/** 税务登记证扫描图 */
	private File taxRegCertificate;
	private String taxRegCertificateFileName;
	private String taxRegCertificateContentType;

	/**
	 * (法人或个人)身份证正面扫描图
	 * */
	private File idCardFrontImg;
	private String idCardFrontImgFileName;
	private String idCardFrontImgContentType;

	/**
	 * (法人法人或个人)身份证反面扫描图
	 * */
	private File idCardBackImg;
	private String idCardBackImgFileName;
	private String idCardBackImgContentType;

	/**
	 * 银行卡正面扫描图
	 * */
	private File bankCardFrontImg;
	private String bankCardFrontImgFileName;
	private String bankCardFrontImgContentType;

	/**
	 * 组织机构证扫描图
	 * */
	private File orgCertificate;
	private String orgCertificateFileName;
	private String orgCertificateContentType;

	/**
	 * 开户申请书第一页扫描图
	 * */
	private File accountApplicationImg;
	private String accountApplicationImgFileName;
	private String accountApplicationImgContentType;
	/**
	 * 开户申请书第二页扫描图
	 * */
	private File accountApplicationImg1;
	private String accountApplicationImg1FileName;
	private String accountApplicationImg1ContentType;
	/**
	 *会员审核页面
	 * */
	public String memberAudit() {
		if (null != memberId && !"".equals(memberId)) {
			try {
				this.memberBase = memberBaseService.selectById(memberId);
				files = uploadFileService
						.getCommonListData("from UploadFile c where c.entityFrom = 'MemberBase' and c.entityId='"
								+ memberId + "' ");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return MEMBER_AUDIT;
	}

	/**
	 * 会员审核操作
	 * */
	public String audit() {
		if (null != memberId && !"".equals(memberId)) {
			MemberAudit ma = new MemberAudit();
			MemberBase mb = memberBaseService.selectById(memberId);
			
			//下面这行记录为了检查此用户是否存在审核历史记录  2013年5月27日
			MemberAudit memberA = memberAuditService.selectByHql(" from MemberAudit where state = '2' and memberBase.id = '"
										+ memberId + "'");

			this.setMembertype(mb.getMemberType().getCode());
			
			ma.setAdditDate(new Date());
			ma.setAuditor((User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal());
			ma.setMemberBase(mb);
			ma.setState(state);
			ma.setReasion(reason);
			try {
				memberAuditService.insert(ma);
				if (MemberBase.STATE_PASSED_AUDIT.equals(state)) {// 会员审核通过，其账户也随之开通
					
					//给融资方席位费/年费赋初始值
					if (MemberType.CODE_FINANCING.equals(mb.getMemberType().getCode())){ 
						mb.setYearFeeStartDate(new Date()); 
						Calendar c=Calendar.getInstance();
						c.setTime(mb.getYearFeeStartDate());
						c.add(c.YEAR, 1);
						mb.setYearFeeEndDate(c.getTime()); 
					}
					
					mb.setState(MemberBase.STATE_PASSED_AUDIT);
					mb.getUser().getUserAccount().setState(Account.STATE_OPEN);
					mb.getUser().getUserAccount().setAccountId(mb.getUser().getUsername());
					mb.getUser().setEnabled(true);
					mb.getUser().setLoginCount(0);
					// 2013.05.24 
					mb.getUser().setUserState(User.STATE_PASSED_AUDIT);
					//edit end;
					mb.setRecentAuditDate(new Date());
					
					ActionContext.getContext().getSession().put(
							Constant.MESSAGETIP, "审核通过");
					
					saveAttachment(memberId);//保存图片
					
					// 审核通过发短信
					try {
						if (memberA == null) {
							mb.setFirstAuditDate(new Date());
							if(mb.getUser().getId()!=0){
								System.out.println("新增的用户ID为"+mb.getUser().getId()+"的会员发送短信开始！");}
							else{
								System.out.println("发送短信给会员："+mb.getId());
							}
							VelocityContext context = new VelocityContext(); 
							context.put("userName", mb.getUser().getUsername());
							if(MemberBase.CATEGORY_ORG.equals(mb.getCategory()))
							   {context.put("password", "营业执照代码后六位");}
							if(MemberBase.CATEGORY_PERSON.equals(mb.getCategory()))
							   {context.put("password", "身 份 证号码后六位");}					
			 					String content = "";
							if (MemberType.CODE_FINANCING.equals(mb.getMemberType()
									.getCode()))
								content = VelocityUtils.getVelocityString(context,
										"rzr_reg.html");
							if (MemberType.CODE_INVESTORS.equals(mb.getMemberType()
									.getCode()))
								content = VelocityUtils.getVelocityString(context,"tzr_reg.html");
							
							SimpleDateFormat format1 =new SimpleDateFormat("yyyyMMddHHmmss");  
							//2013.07.22 更改为调用servlet方式发送短信
							//String path = "http://localhost:8080/kmfex/sms?phones="+mb.getMobile()+"&content="+content.trim()+"&time="+format1.format(new Date())+"&decode=&action=1";
							String usercheckpass = ReadsStaticConstantPropertiesUtil.readValue("usercheckpass");
							System.out.println("usercheckpass:"+usercheckpass);
							if(null!=usercheckpass&&"on".equals(usercheckpass)){
								System.out.println("invoke new sms");
								SMSNewUtil.sms(mb.getMobile(), content, format1.format(new Date()), "","1");
							}else{
								System.out.println("invoke old sms");
								SMSUtil.sms(mb.getMobile(), content, format1.format(new Date()), "","1");
							}
							//StringUtils.getHtml(path);
							
							//wsInterfaceService.getChargeinterfacePort().sendsms1(mb.getMobile(), content.trim(),format1.format(new Date()));
							//wsInterfaceService.getChargeinterfacePort().sendsms1(mb.getMobile(), content.trim(),format1.format(new Date()));
							}else{
								//如果数据库中有此账户的审核记录说明非第一次审核
								System.out.println("检查此用户非第一次审核，未发送短信！");
							}
								
				    
						} catch (Exception e) { 
						e.printStackTrace();
					}
					/*		
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("content", content.trim());
					params.put("mobile", mb.getMobile());
					
					 
		          	SendUrlRequestUtils.send_Url(BaseTool
							.getProjectUrl(ServletActionContext.getRequest())
							+ "sms/SmsService", "POST", params);	*/	
					
					//审核通过时发起子账户同步(华夏本行)
					/**
					if(null!=mb.getBanklib()){
						if("hxb".equals(mb.getBanklib().getCode())){//用户开户选择华夏银行
							HxbankParam p = new HxbankParam();
							String name = "";
							if("0".equals(mb.getCategory())){
								name = mb.geteName();
							}else if("1".equals(mb.getCategory())){
								name = mb.getpName();
							}
							p.setProp(mb.getCategory());
							p.setAccountName(name);
							p.setMerAccountNo(mb.getUser().getUsername());
							HxbankVO vo = this.hxbankDealService.subAccountSync(p, getLoginUser(),mb.getUser());
		  					if(vo.isFlag()){
		  						User u = mb.getUser();
		  						u.setSynDate_market(new Date());
		  						u.setFlag("1");//交易市场已经同步
		  						this.userService.update(u);
		  					}
						}else{//用户开户选择非华夏银行
							
						}
					}*/
				} else {
					// 会员审核不通过
					mb.setState(MemberBase.STATE_NOT_PASS_AUDIT);
					
					//edit by 2013.05.24 更新添加审核不通过的状态
					mb.getUser().setUserState(User.STATE_NOT_PASS_AUDIT);
					//edit end;
					
					ActionContext.getContext().getSession().put(
							Constant.MESSAGETIP, "审核不通过");
				}
				memberBaseService.update(mb);
				userService.update(mb.getUser());
				accountService.update(mb.getUser().getUserAccount());
			} catch (Exception e) {
				e.printStackTrace();
				ActionContext.getContext().getSession().put(
						Constant.MESSAGETIP, "审核失败");
				return memberAudit();
			}
		}
		return AUDIT;
	}

	public String getByMemberBase() {

		return GET_BY_MEMBER_BASE;
	}

	public String getNotPassAuditedReason() {
		this.reason = "";
		this.memberAudit = this.memberAuditService.findByMemberBaseId(memberId);
		if (null != memberAudit) {
			this.reason = memberAudit.getReasion();
		}
		try {
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					this.reason);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 保存机构法人身份证，组织机构证，营业执照，税务登记证， (个人)身份证，银行卡正面和开户申请书等扫描图
	 * */
	private void saveAttachment(String memberId) throws Exception {
		MemberBase memberBase1 = memberBaseService.selectById(memberId);
		if (idCardFrontImg != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(idCardFrontImg, idCardFrontImgFileName,
					idCardFrontImgContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);  
			memberBase1.setIdCardFrontImg(file);
		}

		if (idCardBackImg != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(idCardBackImg, idCardBackImgFileName,
					idCardBackImgContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase1.setIdCardBackImg(file);
		}

		if (bankCardFrontImg != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(bankCardFrontImg,
					bankCardFrontImgFileName, bankCardFrontImgContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase1.setBankCardFrontImg(file);
		}

		if (accountApplicationImg != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(accountApplicationImg,
					accountApplicationImgFileName,
					accountApplicationImgContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase1.setAccountApplicationImg(file);
		}

		if (accountApplicationImg1 != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(accountApplicationImg1,
					accountApplicationImg1FileName,
					accountApplicationImg1ContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase1.setAccountApplicationImg1(file);
		}

		if (null != busLicense) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(busLicense, busLicenseFileName,
					busLicenseContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase1.setBusLicense(file);
		}

		if (null != orgCertificate) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(orgCertificate, orgCertificateFileName,
					orgCertificateContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase1.setOrgCertificate(file);
		}

		if (null != legalPersonIdCard) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(legalPersonIdCard,
					legalPersonIdCardFileName, legalPersonIdCardContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase1.setLegalPersonIdCard(file);
		}

		if (null != taxRegCertificate) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(taxRegCertificate,
					taxRegCertificateFileName, taxRegCertificateContentType,IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase1.setTaxRegCertificate(file);
		}
		memberBaseService.update(memberBase1);
	}
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public MemberAudit getMemberAudit() {
		return memberAudit;
	}

	public void setMemberAudit(MemberAudit memberAudit) {
		this.memberAudit = memberAudit;
	}

	public MemberBase getMemberBase() {
		return memberBase;
	}

	public void setMemberBase(MemberBase memberBase) {
		this.memberBase = memberBase;
	}

	public List<UploadFile> getFiles() {
		return files;
	}

	public void setFiles(List<UploadFile> files) {
		this.files = files;
	}

	public String getMembertype() {
		return membertype;
	}

	public void setMembertype(String membertype) {
		this.membertype = membertype;
	}

	public File getBusLicense() {
		return busLicense;
	}

	public void setBusLicense(File busLicense) {
		this.busLicense = busLicense;
	}

	public String getBusLicenseFileName() {
		return busLicenseFileName;
	}

	public void setBusLicenseFileName(String busLicenseFileName) {
		this.busLicenseFileName = busLicenseFileName;
	}

	public String getBusLicenseContentType() {
		return busLicenseContentType;
	}

	public void setBusLicenseContentType(String busLicenseContentType) {
		this.busLicenseContentType = busLicenseContentType;
	}

	public File getLegalPersonIdCard() {
		return legalPersonIdCard;
	}

	public void setLegalPersonIdCard(File legalPersonIdCard) {
		this.legalPersonIdCard = legalPersonIdCard;
	}

	public String getLegalPersonIdCardFileName() {
		return legalPersonIdCardFileName;
	}

	public void setLegalPersonIdCardFileName(String legalPersonIdCardFileName) {
		this.legalPersonIdCardFileName = legalPersonIdCardFileName;
	}

	public String getLegalPersonIdCardContentType() {
		return legalPersonIdCardContentType;
	}

	public void setLegalPersonIdCardContentType(String legalPersonIdCardContentType) {
		this.legalPersonIdCardContentType = legalPersonIdCardContentType;
	}

	public File getTaxRegCertificate() {
		return taxRegCertificate;
	}

	public void setTaxRegCertificate(File taxRegCertificate) {
		this.taxRegCertificate = taxRegCertificate;
	}

	public String getTaxRegCertificateFileName() {
		return taxRegCertificateFileName;
	}

	public void setTaxRegCertificateFileName(String taxRegCertificateFileName) {
		this.taxRegCertificateFileName = taxRegCertificateFileName;
	}

	public String getTaxRegCertificateContentType() {
		return taxRegCertificateContentType;
	}

	public void setTaxRegCertificateContentType(String taxRegCertificateContentType) {
		this.taxRegCertificateContentType = taxRegCertificateContentType;
	}

	public File getIdCardFrontImg() {
		return idCardFrontImg;
	}

	public void setIdCardFrontImg(File idCardFrontImg) {
		this.idCardFrontImg = idCardFrontImg;
	}

	public String getIdCardFrontImgFileName() {
		return idCardFrontImgFileName;
	}

	public void setIdCardFrontImgFileName(String idCardFrontImgFileName) {
		this.idCardFrontImgFileName = idCardFrontImgFileName;
	}

	public String getIdCardFrontImgContentType() {
		return idCardFrontImgContentType;
	}

	public void setIdCardFrontImgContentType(String idCardFrontImgContentType) {
		this.idCardFrontImgContentType = idCardFrontImgContentType;
	}

	public File getIdCardBackImg() {
		return idCardBackImg;
	}

	public void setIdCardBackImg(File idCardBackImg) {
		this.idCardBackImg = idCardBackImg;
	}

	public String getIdCardBackImgFileName() {
		return idCardBackImgFileName;
	}

	public void setIdCardBackImgFileName(String idCardBackImgFileName) {
		this.idCardBackImgFileName = idCardBackImgFileName;
	}

	public String getIdCardBackImgContentType() {
		return idCardBackImgContentType;
	}

	public void setIdCardBackImgContentType(String idCardBackImgContentType) {
		this.idCardBackImgContentType = idCardBackImgContentType;
	}

	public File getBankCardFrontImg() {
		return bankCardFrontImg;
	}

	public void setBankCardFrontImg(File bankCardFrontImg) {
		this.bankCardFrontImg = bankCardFrontImg;
	}

	public String getBankCardFrontImgFileName() {
		return bankCardFrontImgFileName;
	}

	public void setBankCardFrontImgFileName(String bankCardFrontImgFileName) {
		this.bankCardFrontImgFileName = bankCardFrontImgFileName;
	}

	public String getBankCardFrontImgContentType() {
		return bankCardFrontImgContentType;
	}

	public void setBankCardFrontImgContentType(String bankCardFrontImgContentType) {
		this.bankCardFrontImgContentType = bankCardFrontImgContentType;
	}

	public File getOrgCertificate() {
		return orgCertificate;
	}

	public void setOrgCertificate(File orgCertificate) {
		this.orgCertificate = orgCertificate;
	}

	public String getOrgCertificateFileName() {
		return orgCertificateFileName;
	}

	public void setOrgCertificateFileName(String orgCertificateFileName) {
		this.orgCertificateFileName = orgCertificateFileName;
	}

	public String getOrgCertificateContentType() {
		return orgCertificateContentType;
	}

	public void setOrgCertificateContentType(String orgCertificateContentType) {
		this.orgCertificateContentType = orgCertificateContentType;
	}

	public File getAccountApplicationImg() {
		return accountApplicationImg;
	}

	public void setAccountApplicationImg(File accountApplicationImg) {
		this.accountApplicationImg = accountApplicationImg;
	}

	public String getAccountApplicationImgFileName() {
		return accountApplicationImgFileName;
	}

	public void setAccountApplicationImgFileName(
			String accountApplicationImgFileName) {
		this.accountApplicationImgFileName = accountApplicationImgFileName;
	}

	public String getAccountApplicationImgContentType() {
		return accountApplicationImgContentType;
	}

	public void setAccountApplicationImgContentType(
			String accountApplicationImgContentType) {
		this.accountApplicationImgContentType = accountApplicationImgContentType;
	}

	public File getAccountApplicationImg1() {
		return accountApplicationImg1;
	}

	public void setAccountApplicationImg1(File accountApplicationImg1) {
		this.accountApplicationImg1 = accountApplicationImg1;
	}

	public String getAccountApplicationImg1FileName() {
		return accountApplicationImg1FileName;
	}

	public void setAccountApplicationImg1FileName(
			String accountApplicationImg1FileName) {
		this.accountApplicationImg1FileName = accountApplicationImg1FileName;
	}

	public String getAccountApplicationImg1ContentType() {
		return accountApplicationImg1ContentType;
	}

	public void setAccountApplicationImg1ContentType(
			String accountApplicationImg1ContentType) {
		this.accountApplicationImg1ContentType = accountApplicationImg1ContentType;
	}

}
