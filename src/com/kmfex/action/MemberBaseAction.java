package com.kmfex.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.Constant;
import com.kmfex.MemberTotalVO;
import com.kmfex.hxbank.HxbankParam;
import com.kmfex.hxbank.HxbankVO;
import com.kmfex.model.BankLibrary;
import com.kmfex.model.CompanyProperty;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.Industry;
import com.kmfex.model.MemberAudit;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberGuarantee;
import com.kmfex.model.MemberLevel;
import com.kmfex.model.MemberType;
import com.kmfex.model.ModifyLog;
import com.kmfex.model.Region;
import com.kmfex.model.VersionRestrain;
import com.kmfex.model.hx.HxbankDeal;
import com.kmfex.service.BankLibraryService;
import com.kmfex.service.CompanyPropertyService;
import com.kmfex.service.CreditRulesService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.IndustryService;
import com.kmfex.service.MemberAuditService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberGuaranteeService;
import com.kmfex.service.MemberLevelService;
import com.kmfex.service.MemberTypeService;
import com.kmfex.service.ModifyLogService;
import com.kmfex.service.RegionService;
import com.kmfex.service.VersionRestrainService;
import com.kmfex.service.hx.HxbankDealService;
import com.kmfex.webservice.client.WsInterfaceService;
import com.kmfex.webservice.vo.MessageTip;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Contact;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.Role;
import com.wisdoor.core.model.UploadFile;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.ContactService;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UploadFileService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.utils.CloseSoftUtils;
import com.wisdoor.core.utils.CurrencyOperator;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.IpAddrUtil;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.utils.UploadFileUtils;
import com.wisdoor.struts.BaseAction;

/**
 * 会员基本信息Action
 * 
 * @author
 * @version 
 * 
 *          <pre>
 * 修改记录 
 *   save方法里除去发短信功能,审核通过时才发短信
 *   修改exportFinancier()和exportInvestors()方法，增加
 *             "账户余额(元)"的显示
 *    修改enable(),disable(),save() 增加对User类中userState状态的更新
 *   新增assignmentsStart(),assignmentsStop()转让成员的启用和停用
 * </pre>  
 * */
@Controller
@Scope("prototype")
public class MemberBaseAction extends BaseAction implements Preparable {

	
	/**
	 *    
	 */
	private static final long serialVersionUID = -5772539768859423514L;

	/**
	 * 标记：会员列表
	 * */
	private static final String LIST = "list";

	/**
	 * 标记：未通过审核的会员列表
	 * */
	private static final String NOT_PASS_AUDIT_LIST = "notPassAuditedList";

	/**
	 * 标记：修改会员级别(投资会员列表)
	 * */
	private static final String TLIST = "tList";

	/**
	 * 标记：会员信息编辑(修改，新增)
	 * */
	private static final String EDIT = "edit";

	private static final String CHECK_IDCARD = "checkIdCard";

	private static final String CHECK_BUSCODE = "checkBusCode";

	/**
	 * 某个机构融资方的融资进度列表
	 * */
	private static final String FINANCIER_FINANCING_PROGRESS = "financingProgressList";

	/**
	 * 标记：会员开户成功页面
	 * */
	private static final String CREATE_MEMBER_SUCCESS = "createMemberSuccess";

	/**
	 * 标记：未审核会员列表
	 * */
	private static final String NOT_AUDITED_MEMBER_LIST = "notAuditedList";

	/**
	 * 标记：会员详细信息
	 * */
	private static final String MEMBER_DETAILS = "memberDetails";

	/**
	 * 标记：会员银行账号
	 * */
	private static final String MEMBER_BANKACCOUNTS = "memberBankAccounts";

	/**
	 * 标记：会员资料变更
	 * */
	private static final String CHANGE = "change";

	private Map<Integer, String> sexes = new HashMap<Integer, String>();
	/**
	 * 省份列表
	 * */
	private List<Region> regions_province = new ArrayList<Region>();

	/**
	 * 城市列表
	 * */
	private List<Region> regions_city = new ArrayList<Region>();

	private String password = null;

	/**
	 * 会员类型列表
	 * */
	private Map<Integer, String> categories = new LinkedHashMap<Integer, String>();

	/**
	 * 会员状态列表
	 * */
	private Map<Byte, String> memberStates = new HashMap<Byte, String>();

	/**
	 * 标记是否为顶级机构：true，是;false，不是。
	 * */
	private boolean topOrg = false;

	/**
	 * 查询参数：会员状态
	 * */
	private String memberState;
	/**
	 * 查询参数：开户机构名
	 * */
	private String orgName;

	/**
	 * 查询参数：省份代码
	 * */
	private String provinceCode;
	/**
	 * 查询参数：城市代码
	 * */
	private String cityCode;

	private List<String> fileIds;
	private List<UploadFile> files;

	private String banklibrary_id;

	@Resource
	private IndustryService industryService;

	@Resource
	private CompanyPropertyService companyPropertyService;

	@Resource
	WsInterfaceService wsInterfaceService;

	@Resource
	private MemberGuaranteeService memberGuaranteeService = null;

	@Resource
	private FinancingBaseService financingBaseService = null;

	@Resource
	BankLibraryService bankLibraryService;
	@Resource
	CreditRulesService creditRulesService;
	@Resource
	ModifyLogService modifyLogService;

	/**
	 * 企业所属行业列表
	 * */
	private List<Industry> industries;

	/**
	 * 企业所属行业id号
	 * */
	private String industryId;

	/**
	 * 企业性质id号
	 * */
	private String companyPropertyId = "0";

	/**
	 * 企业性质列表
	 * */
	private List<CompanyProperty> companyProperties;
	/**
	 * 查询参数：关键字
	 * */
	private String keyword;
	/**
	 * 查询参数：会员类型
	 */
	private String membertype;
	/**
	 * 会员id号
	 * */
	private String id;

	/**
	 * 用户类别id
	 * */
	private String memberTypeId;

	/** 用户类别代码 */
	private String category = MemberBase.CATEGORY_PERSON;

	private MemberBase memberBase;

	/** 
	 * 会员生日
	 * */
	private Date birthday;
	/**
	 * 会员级别id号
	 * */
	private String levelId;

	/**
	 * 会员类型列表
	 * */
	private List<MemberType> memberTypes;

	/**
	 * 会员级别列表
	 * */
	private List<MemberLevel> memberLevels;

	private List<BankLibrary> banklibrary;

	/**
	 * 标记是否为变更会员资料:true,变更会员资料；false则不是。
	 * */
	private boolean change = false;

	@Resource
	private MemberTypeService memberTypeService;
	@Resource
	private UserService userService;

	@Resource
	private ContactService contactService;

	@Resource
	private MemberBaseService memberBaseService;
	@Resource
	private MemberAuditService memberAuditService;
	@Resource
	private RegionService regionService;
	@Resource
	private UploadFileService uploadFileService;
	@Resource
	private MemberLevelService memberLevelService;
	@Resource
	private OrgService orgService;

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

	private String idCard = null;

	private String busCode = null;

	/**
	 * 个人会员身份证号码及企业会员营业执照代码检验结果：true, 表示此号码已经开过会员；false，表示此号码尚未开过会员
	 * */
	private boolean checkResult = false;

	/**
	 * 需要验证大小的文件
	 * */
	private File imgFile;

	/**
	 * 需要检查是否存在的文件名
	 * */
	private String fileName;

	/**
	 * 标记为是否要导出Excel：true，导出所有会员信息为Excel；false:页面上分页查询
	 * */
	private boolean excel;

	/**
	 * 统计会员的日期
	 * */
	private Date statisticalDate;

	/**
	 * 查询时间段之起始时间
	 * */
	private Date startDate;

	/**
	 * 查询时间段之结束时间
	 * */
	private Date endDate;

	private User logUser;

	private String orgCode = "";// 530101的机构编码，让输入介绍人
	private String jingbanren = "";
	private String usernames;
	
	public String getUsernames() {
		return usernames;
	}
	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}
	
	
	/**
	 * 债权转让会员启用
	 * @return
	 * @throws Exception 
	 */
	public String assignmentsStart() throws Exception {
		JSONObject result = new JSONObject(); 
		result.element("code", "1"); 
		result.element("tip", "操作提示：会员启用成功"); 
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
			result.element("code", "0"); 
			result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
			result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==usernames){
			result.element("code", "0"); 
			result.element("tip", "操作提示：参数非法!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		try {
			String[] users = usernames.split(",");
			for(String usernametemp : users){
				try {
					VersionRestrain vr=versionRestrainService.selectById(usernametemp);
					if(null!=vr){ 
						vr.setUseFlag(1);
						versionRestrainService.update(vr);
					} 
				} catch (Exception e) { 
					result.element("code", "0"); 
					result.element("tip", "操作提示：保存数据异常，稍后重试!");
				}
			} 
		} catch (Exception e1) {  
			result.element("code", "0"); 
			result.element("tip", "操作提示：操作异常，稍后重试!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return null; 
	}
	/**
	 * 债权转让会员停用
	 * @return
	 * @throws Exception 
	 */
	public String assignmentsStop() throws Exception {
		JSONObject result = new JSONObject(); 
		result.element("code", "1"); 
		result.element("tip", "操作提示：会员停用成功"); 
		User loginUser = null;
		try {
			loginUser =(User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		} catch (Exception e1) {  
			result.element("code", "0"); 
			result.element("tip", "操作提示：用户登陆过期，请重新登陆!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==loginUser){
			result.element("code", "0"); 
			result.element("tip", "操作提示：非法用户!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		if(null==usernames){
			result.element("code", "0"); 
			result.element("tip", "操作提示：参数非法!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		try {
			String[] users = usernames.split(",");
			for(String usernametemp : users){
				try {
					VersionRestrain vr=versionRestrainService.selectById(usernametemp);
					if(null!=vr){ 
						vr.setUseFlag(0);
						versionRestrainService.update(vr);
					} 
				} catch (Exception e) { 
					result.element("code", "0"); 
					result.element("tip", "操作提示：保存数据异常，稍后重试!");
				}
			} 
		} catch (Exception e1) {  
			result.element("code", "0"); 
			result.element("tip", "操作提示：操作异常，稍后重试!");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
			return null;
		} 
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),result.toString());
		return null;
	}	
	/**
	 * 修改或新增页面
	 * */
	public String edit() throws Exception {
		
 
		
		try {
			User loginUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			this.orgCode = loginUser.getOrg().getShowCoding();//操作人机构
			this.setMemberTypes(memberTypeService.getList(loginUser));//操作人可查看类型
			this.setMemberLevels(memberLevelService.listAll());//等级
			this.setIndustries(industryService.getScrollData().getResultlist());//行业
			this.setCompanyProperties(companyPropertyService.getScrollData()
					.getResultlist());
			this.banklibrary = this.bankLibraryService.getScrollData(
					" 1=1 order by order asc").getResultlist();
			
			if (null != memberBase.industry) {
				this.setIndustryId(String.valueOf(memberBase.industry.getId()));
			}
			if (null != memberBase.getCompanyProperty()) {
				this.setCompanyPropertyId(String.valueOf(memberBase
						.getCompanyProperty().getId()));
			}
			regions_province = regionService
					.getScrollDataCommon(
							"from Region where areaparentcode = '0000' order by areacode",
							new String[] {});
			regions_city = regionService
					.getScrollDataCommon(
							"from Region where areaparentcode = '0001' order by areacode",
							new String[] {});
			if (isModify()) {
				// 修改会员  列出会员信息
				this.setMemberTypeId(memberBase.getMemberType().getId());
				category = memberBase.category;
				files = uploadFileService
						.getCommonListData("from UploadFile c where c.entityFrom = 'MemberBase' and c.entityId='"
								+ id + "' ");
				for (UploadFile file : files) {
					file.setFrontId(file.getId().replaceAll("\\.", ""));
				}
				if (memberBase.getProvince() != null
						&& !"".equals(this.memberBase.getProvince())) {
					regions_city = regionService.getScrollDataCommon(
							"from Region where areaparentcode = '"
									+ this.memberBase.getProvince()
									+ "' order by areacode", new String[] {});
				}
				
				if (null == memberBase.getJingbanren()
						|| "".equals(memberBase.getJingbanren())) {
					this.jingbanren = loginUser.getOrg().getName();
				} else {
					this.jingbanren = memberBase.getJingbanren();
				}
			} else {
				// 新增会员
				memberBase.category = category;
				this.jingbanren = loginUser.getOrg().getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EDIT;
	}

	/**
	 * 修改或新增页面
	 * */
	public String edit2() throws Exception {
 
		
		try {
			User loginUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			this.orgCode = loginUser.getOrg().getShowCoding();//操作人机构
			this.setMemberTypes(memberTypeService.getList(loginUser));//操作人可查看类型
			this.setMemberLevels(memberLevelService.listAll());//等级
			this.setIndustries(industryService.getScrollData().getResultlist());//行业
			this.setCompanyProperties(companyPropertyService.getScrollData()
					.getResultlist());
			this.banklibrary = this.bankLibraryService.getScrollData(
					" 1=1 order by order asc").getResultlist();
			
			if (null != memberBase.industry) {
				this.setIndustryId(String.valueOf(memberBase.industry.getId()));
			}
			if (null != memberBase.getCompanyProperty()) {
				this.setCompanyPropertyId(String.valueOf(memberBase
						.getCompanyProperty().getId()));
			}
			regions_province = regionService
					.getScrollDataCommon(
							"from Region where areaparentcode = '0000' order by areacode",
							new String[] {});
			regions_city = regionService
					.getScrollDataCommon(
							"from Region where areaparentcode = '0001' order by areacode",
							new String[] {});
			if (isModify()) {
				// 修改会员  列出会员信息
				this.setMemberTypeId(memberBase.getMemberType().getId());
				category = memberBase.category;
				files = uploadFileService
						.getCommonListData("from UploadFile c where c.entityFrom = 'MemberBase' and c.entityId='"
								+ id + "' ");
				for (UploadFile file : files) {
					file.setFrontId(file.getId().replaceAll("\\.", ""));
				}
				if (memberBase.getProvince() != null
						&& !"".equals(this.memberBase.getProvince())) {
					regions_city = regionService.getScrollDataCommon(
							"from Region where areaparentcode = '"
									+ this.memberBase.getProvince()
									+ "' order by areacode", new String[] {});
				}
				/*
				 * if
				 * (memberBase.getBanklib()!=null&&!"".equals(this.memberBase.
				 * getBanklib())){
				 * this.banklibrary_id=memberBase.banklib.getId(); }
				 */
				if (null == memberBase.getJingbanren()
						|| "".equals(memberBase.getJingbanren())) {
					this.jingbanren = loginUser.getOrg().getName();
				} else {
					this.jingbanren = memberBase.getJingbanren();
				}
			} else {
				// 新增会员
				memberBase.category = category;
				this.jingbanren = loginUser.getOrg().getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EDIT;
	}
	
	/**
	 * 会员资料变更列表页面
	 * */
	public String change() throws Exception {
		try {
			initListData();
			User loginUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String orgNo = loginUser.getOrg().getCoding();
			logUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			ServletActionContext.getRequest().setAttribute(
					"pageView",
					memberBaseService.listMembersForChange(keyword, membertype,
							orgNo, getShowRecord(), getPage()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return CHANGE;
	}

	public String resetPasswrodUi() {
		this.memberBase = null;
		return "resetPasswordUi";
	}

	private String name;
	private String userName;
	private String idCardNo;

	private String errorStr;
	private boolean canReset = false;

	public String resetPasswrodQuery() {
		String hql = "from MemberBase o where o.user.username = '"
				+ userName.trim() + "'";
		MemberBase m = this.memberBaseService.selectByHql(hql);
		if (null != m) {
			if (m.getState().equals(MemberBase.STATE_NOT_AUDIT)) {
				errorStr = "用户名为:" + userName.trim() + "的会员，还未审核通过。";
			} else {
				String mU = "";
				if (m.getCategory().equals("1")) {// 个人
					mU = m.getpName();
				} else {// 企业
					mU = m.geteName();
				}
				if (!name.trim().equals(mU)) {
					errorStr = "用户名为:" + userName.trim() + "的会员，其会员名称为:“" + mU
							+ "”，与您输入的会员名称:“" + name + "” 不符";
				} else {
					if (!idCardNo.trim().equals(m.getIdCardNo())) {
						errorStr = "用户名为:" + userName.trim() + "的会员，其身份证号为:“"
								+ m.getIdCardNo() + "”，与您输入的身份证号:“" + idCardNo
								+ "” 不符";
					} else {
						canReset = true;
						this.memberBase = m;
					}
				}
			}
		} else {
			errorStr = "用户名为:" + userName.trim() + "的会员，不存在，请重新输入。";
		}
		return "resetPasswordUi";
	}

	
	private String firstBankAccount;
	private String firstEmobile;
	private String firstPmobile;
	private String firstIdCardNo;
	
	/**
	 * 保存修改或新增的会员
	 * */
	public String save() {
				
		try {
			
			User loginUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String orgNo = loginUser.getOrg().getShowCoding();

			memberBase.memberType = memberTypeService.selectById(memberTypeId);
			memberBase.banklib = bankLibraryService.selectById(banklibrary_id);

			if (memberBase.category.equals(MemberBase.CATEGORY_ORG)) {
				memberBase.setIndustry(industryService.selectById(Long
						.valueOf(industryId)));
				memberBase.setCompanyProperty(companyPropertyService
						.selectById(Long.valueOf(companyPropertyId)));
				if (busCodeExists(memberBase.eBusCode) && !isModify()) {
					addActionError("营业执照已使用！保存失败，请重试");
					return edit();
				}
			} else if (memberBase.category.equals(MemberBase.CATEGORY_PERSON)) {

				if (idCardExists(memberBase.idCardNo,memberBase.memberType.id) && !isModify()) {
					addActionError("身份证已经被使用！保存失败，请重试");
					return edit();
				}
				// 如此需求：会员类型为个人时硬要设置其企业相关属性
				try {
					if (MemberType.CODE_FINANCING.equals(memberBase
							.getMemberType().getCode()))// 融资方
					{
						memberBase.seteName(memberBase.pName);
						memberBase.seteContact(memberBase.pName);
						memberBase.seteContactMobile(memberBase.pMobile);
						memberBase.seteContactPhone(memberBase.pPhone);
						memberBase.seteMobile(memberBase.pMobile);
						memberBase.setePhone(memberBase.pPhone);
						memberBase.seteAddress(memberBase.pAddress);

						// 企业性质
						memberBase
								.setIndustry(industryService
										.selectByHql("from Industry where name like '%其他%'"));
						// 企业行业
						memberBase
								.setCompanyProperty(companyPropertyService
										.selectByHql("from CompanyProperty where name = '个人'"));

					}
				} catch (RuntimeException e) {
					e.printStackTrace();
					addActionError("保存失败，请重试");
					return edit();
				}
			}

			try {
				memberBase.setProvinceName(regionService.selectByAreacode(
						memberBase.province).getAreaname_l());
				memberBase.setCityName(regionService.selectByAreacode(
						memberBase.city).getAreaname_l());
				memberBase.setpBirthday(birthday);
			} catch (RuntimeException e1) {
				e1.printStackTrace();
				return edit();
			}
			// 开户行设置
			if (this.banklibrary_id != null && !"".equals(this.banklibrary_id)) {
				BankLibrary bl = this.bankLibraryService
						.selectById(this.banklibrary_id);
				if (bl != null) {
					memberBase.setBanklib(bl);
					// memberBase.setBank(bl.getCaption());
				}
			}

			saveAttachment();
			
			// 添加日志 ：身份证、银行卡和电话
			if (isModify()) {
				// 修改会员
				User user = memberBase.getUser();
			
				if (MemberType.CODE_FINANCING.equals(memberBase.getMemberType()
						.getCode()))// 融资方
				{
					user.getRoles().clear();
					user.getRoles().add(new Role(Constant.RZROLEID));
				}
				if (MemberType.CODE_INVESTORS.equals(memberBase.getMemberType()
						.getCode()))// 投资人
				{
					user.getRoles().clear();
					user.getRoles().add(new Role(Constant.TZROLEID));
				}
				user.setUserType(memberBase.getMemberType().getCode());
				Contact ct = user.getUserContact();

				saveUserProperties(user, ct);

				contactService.update(ct);

				memberBase.setUpdateDate(new Date());
				//log
				
				HttpServletRequest request = ServletActionContext. getRequest(); 
				String ip = getIpAddr(request);
				String firstPhone = MemberBase.CATEGORY_ORG.equals(memberBase.getCategory())?this.getFirstEmobile().trim():this.getFirstPmobile().trim();
				String endPhone = ct.getMobile();
			
				if( !isEqual(firstPhone, endPhone)){
					ModifyLog ml = new ModifyLog();
					ml.setChanger(loginUser);
					ml.setDatatype("member");
					ml.setDataname("save");
					ml.setModifyDate(new Date());
					ml.setModifyIP(ip);
					ml.setModifyModel(id);
					ml.setContent("更改会员手机号码");
					ml.setFirstData(firstPhone);
					ml.setEndData(endPhone);
					modifyLogService.insert(ml);
				}
				if(!this.getFirstBankAccount().trim().equals(memberBase.getBankAccount().trim())){
					ModifyLog ml2 = new ModifyLog();
					ml2.setChanger(loginUser);
					ml2.setDatatype("member");
					ml2.setDataname("save");
					ml2.setModifyDate(new Date());
					ml2.setModifyIP(ip);
					ml2.setModifyModel(id);
					ml2.setContent("更改会员银行卡号");
					ml2.setFirstData(this.getFirstBankAccount().trim());
					ml2.setEndData(memberBase.getBankAccount().trim());
					modifyLogService.insert(ml2);
				}
				if(!this.getFirstIdCardNo().trim().equals(memberBase.getIdCardNo().trim())){
					ModifyLog ml3 = new ModifyLog();
					ml3.setChanger(loginUser);
					ml3.setDatatype("member");
					ml3.setDataname("save");
					ml3.setModifyDate(new Date());
					ml3.setModifyIP(ip);
					ml3.setContent("更改会员身份证号");
					ml3.setModifyModel(id);
					ml3.setFirstData(this.getFirstIdCardNo().trim());
					ml3.setEndData(memberBase.getIdCardNo().trim());
					modifyLogService.insert(ml3);
				}
				memberBase.setChanger(loginUser);
				userService.update(user);
				memberBaseService.update(memberBase);
			} else {
				// 新会员开户
				// 生成一个默认用户：用户名为开户机构编号(6位)+ 5位流水号，
				String userName;
				try {
					userName = memberBaseService.getMemberAccountNo(orgNo);
				} catch (Exception e) {
					e.printStackTrace();
					addActionError("保存失败，请重试");
					return edit();
				}
				// 密码的长度
				int passwordLength = 6;

				User memberUser;
				try {
					int beginIndex = 0;
					if (MemberBase.CATEGORY_ORG.equals(memberBase.category)) {
						// 机构会员密码为营业执照的最后passwordLength位
						if (passwordLength < memberBase.eBusCode.length()) {
							beginIndex = memberBase.eBusCode.length()
									- passwordLength;
						}
						password = memberBase.eBusCode.substring(beginIndex);
					} else if (MemberBase.CATEGORY_PERSON
							.equals(memberBase.category)) {
						// 个人会员密码为身份证的最后passwordLength位。
						if (passwordLength < memberBase.idCardNo.length()) {
							beginIndex = memberBase.idCardNo.length()
									- passwordLength;
						}
						password = memberBase.idCardNo.substring(beginIndex);
					}

					memberUser = new User(userName, password, "1");
					memberUser.setEnabled(false);
					//投资会员，开户时选择开户行为：工商银行，则专户设定为工行专户
					/*if(MemberType.CODE_INVESTORS.equals(memberBase.getMemberType().getCode())){
						if(null!=memberBase.getBanklib()&&null!=memberBase.getBanklib().getCaption()&&"工商银行".equals(memberBase.getBanklib().getCaption())){
							memberUser.setChannel(2);
							System.out.println("投资人:工行专户");
						}
					}*/
					// edit by  2013.05.24 添加用户状态 1未审核
					memberUser.setUserState(User.STATE_NOT_AUDIT);
					// edit end;
					memberUser.setOrg(loginUser.getOrg());
					Contact ct = new Contact();
					memberBase.setMemberLevel(memberLevelService
							.getCommonLevel());
					saveUserProperties(memberUser, ct);
					contactService.insert(ct);
					memberUser.setUserContact(ct);
					memberUser.setUserType(memberBase.memberType.getCode());
					userService.insertUser(memberUser);
					memberBase.setUser(memberUser);
					memberUser.getRoles().add(new Role(Constant.INITROLEID));
					memberBase.setOrgNo(orgNo);
					memberBase.setCreator(loginUser);

					memberBaseService.insert(memberBase);
					if (MemberType.CODE_SECURED.equals(memberBase
							.getMemberType().getCode())) {
						MemberGuarantee mg = new MemberGuarantee();
						mg.setMemberBase(memberBase);
						mg.setCreateUser(loginUser);
						memberGuaranteeService.insert(mg);
					}
					/*
					 * 开户时赠送积分
					 */
					this.creditRulesService.open_an_account(memberUser
							.getUserAccount());
				} catch (RuntimeException e) {
					e.printStackTrace();
					addActionError("保存失败，请重试");
					return edit();
				}

			}
			if (fileIds != null && fileIds.size() > 0) {
				for (String fileId : fileIds) {
					UploadFile uploadFile = uploadFileService
							.selectById(fileId);
					uploadFile.setEntityFrom("MemberBase");
					uploadFile.setEntityId(this.memberBase.getId());
					uploadFile.setUseFlag("1");// 使用中
					uploadFileService.update(uploadFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			try {
				response.getWriter().write(e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		if (!isModify()) {
			return CREATE_MEMBER_SUCCESS;
		}
		return list();
	}

	private boolean isNull(String str){
		if( str == null || str.equals("") )
			return true;
		else
			return false;
	}
	
	private boolean isEqual(String str1,String str2){
		boolean flag = false;
		if(isNull(str1) && isNull(str2)){
			flag = true;
		}else if (!isNull(str1) && !isNull(str2)){
			if(str1.trim().equals(str2.trim()))
				flag = true;
			else flag = false;
		}else 
			flag = false;
		return flag;
	}
	
	private void saveUserProperties(User user, Contact ct) {

		if (MemberBase.CATEGORY_ORG.equals(memberBase.category)) {
			user.setRealname(memberBase.eName);
			ct.setAddress(memberBase.geteAddress());
			ct.setMobile(memberBase.geteMobile());
			ct.setPhone(memberBase.getePhone());
			ct.setPostalcode(memberBase.getePostcode());
		}

		if (MemberBase.CATEGORY_PERSON.equals(memberBase.category)) {
			user.setRealname(memberBase.pName);
			ct.setAddress(memberBase.getpAddress());
			ct.setMobile(memberBase.getpMobile());
			ct.setPhone(memberBase.getpPhone());
		}
	}

	/**
	 * 会员开户时验证个人会员的身份证号是否已开过会员
	 * */
	public String checkIdCard() {
		String statusCode = "0";// 身份证号码未被使用
		if (idCardExists(idCard) && !isModify()) {
			statusCode = "0";// 身份证号码已被该类型会员使用
		}
		if (idCardExists(idCard,memberTypeId) && !isModify()) {
			statusCode = "1";// 身份证号码已被该类型会员使用
		}
		try {
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					statusCode);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						statusCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		return null;

	}

	/**
	 * 会员开户时检查身份证号码是否已开过户
	 * 
	 * @param idCard
	 *            省份证号
	 * @return true，指定的身份证号已开户；false，指定的身份证号未开户。
	 * */
	private boolean idCardExists(String idCard) {
		StringBuilder hql = new StringBuilder("from MemberBase where 1=1 ");
		if (isModify()) {
			hql.append("and id=' " + id + "' ");
		}
		hql.append("and category = '" + MemberBase.CATEGORY_PERSON
				+ "' and idCardNo = '" + idCard + "'  and state != '5'");
		MemberBase mb = memberBaseService.selectByHql(hql.toString());
//		System.out.println("idCardExists returns "+ (null != mb));
		return null != mb;
	}
	
	/**
	 * 会员开户时检查身份证号码是否已开过户
	 * 
	 * @param idCard
	 *            省份证号
	 * @return true，指定的身份证号已开户；false，指定的身份证号未开户。
	 * */
	private boolean idCardExists(String idCard,String membertype_id) {
		StringBuilder hql = new StringBuilder("from MemberBase where 1=1 ");
		if (isModify()) {
			hql.append("and id=' " + id + "' ");
		}
		hql.append("and category = '" + MemberBase.CATEGORY_PERSON
				+ "' and idCardNo = '" + idCard + "' and membertype_id = '"+membertype_id+"' and state != '5'");
		MemberBase mb = memberBaseService.selectByHql(hql.toString());
		return null != mb;
	}

	/**
	 * 验证营业执照是否未使用
	 * */
	public String checkBusCode() {
		String statusCode = "0";// 营业执照号码未使用

		if (busCodeExists(busCode) && !isModify()) {
			statusCode = "1";// 营业执照号码已使用
		}
		try {
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					statusCode);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						statusCode);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
		return null;
	}

	/**
	 * 检查营业执照是否在使用
	 * */
	public boolean busCodeExists(String busCode) {
		StringBuilder hql = new StringBuilder("from MemberBase where 1=1 ");
		if (isModify()) {
			hql.append("and id = '" + id + "' ");
		}
		hql.append("and category = '" + MemberBase.CATEGORY_ORG
				+ "' and eBusCode = '" + busCode + "' ");

		MemberBase mb = memberBaseService.selectByHql(hql.toString());
		return null != mb;
	}

	/**
	 * 检验文件大小：单个文件最多只允许1M
	 * */
	public String checkImageSize() {
		String statusCode = "1";

		if (imgFile.length() > 1024 * 1024) {
			statusCode = "0";
		}
		try {
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),
					statusCode);
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 检查指定文件是否存在
	 * */
	/*
	 * public String checkFile() { // 指定的文件不存在 String statusCode = "0"; try {
	 * String request = ServletActionContext.getRequest().getRealPath(
	 * "Static"); File dir = new File(request);
	 * 
	 * File secondDir = new File(dir, "userfiles");
	 * 
	 * File file = new File(secondDir, fileName);
	 * 
	 * if (file.exists()) { // 指定的文件存在 statusCode = "1"; } } catch (Exception
	 * e1) { e1.printStackTrace(); return null; } finally { try {
	 * DoResultUtil.doStringResult(ServletActionContext.getResponse(),
	 * statusCode); } catch (Exception e) { // 
	 * e.printStackTrace(); } } return null; }
	 */

	/**
	 * 保存机构法人身份证，组织机构证，营业执照，税务登记证， (个人)身份证，银行卡正面和开户申请书等扫描图
	 * */
	private void saveAttachment() throws Exception {
		if (idCardFrontImg != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(idCardFrontImg, idCardFrontImgFileName,
					idCardFrontImgContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setIdCardFrontImg(file);
		}

		if (idCardBackImg != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(idCardBackImg, idCardBackImgFileName,
					idCardBackImgContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setIdCardBackImg(file);
		}

		if (bankCardFrontImg != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(bankCardFrontImg,
					bankCardFrontImgFileName, bankCardFrontImgContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setBankCardFrontImg(file);
		}

		if (accountApplicationImg != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(accountApplicationImg,
					accountApplicationImgFileName,
					accountApplicationImgContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setAccountApplicationImg(file);
		}

		if (accountApplicationImg1 != null) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(accountApplicationImg1,
					accountApplicationImg1FileName,
					accountApplicationImg1ContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setAccountApplicationImg1(file);
		}

		if (null != busLicense) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(busLicense, busLicenseFileName,
					busLicenseContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setBusLicense(file);
		}

		if (null != orgCertificate) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(orgCertificate, orgCertificateFileName,
					orgCertificateContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setOrgCertificate(file);
		}

		if (null != legalPersonIdCard) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(legalPersonIdCard,
					legalPersonIdCardFileName, legalPersonIdCardContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setLegalPersonIdCard(file);
		}

		if (null != taxRegCertificate) {
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(taxRegCertificate,
					taxRegCertificateFileName, taxRegCertificateContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			uploadFileService.insert(file);
			memberBase.setTaxRegCertificate(file);
		}
	}

	/**
	 * 重新启用指定的会员
	 * */
	public String enable() {
		this.memberBase = memberBaseService.selectById(id);
		if(this.memberBase.getState().equalsIgnoreCase(MemberBase.STATE_STOPPED))
		{
			return list();
		}
		memberBase.setState(MemberBase.STATE_NOT_AUDIT);
		try {
			memberBaseService.update(memberBase);
			User user = memberBase.getUser();
			if (user != null) {
				user.setLoginCount(0);
				// edit by sxs 2013年5月27日 修改bug 将USER的可登录状态在审核中决定
				// user.setEnabled(true);
				user.setUserState(User.STATE_NOT_AUDIT);
				// edit ends;
				userService.update(user);
				
				//log
				User loginUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
				HttpServletRequest request = ServletActionContext. getRequest(); 
				String ip = getIpAddr(request);
				ModifyLog ml = new ModifyLog();
				ml.setChanger(loginUser);
				ml.setDatatype("member");
				ml.setDataname("enable");
				ml.setModifyDate(new Date());
				ml.setModifyIP(ip);
				ml.setContent("重新启用会员");
				ml.setModifyModel(memberBase.id);
				ml.setFirstData("");
				ml.setEndData("");
				modifyLogService.insert(ml);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return newlist();
		}
		return newlist();
	}

	/**
	 * 删除指定会员
	 * */
	public String delete() {
		try {
			memberBaseService.delete(id);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return newlist();
	}

	/**
	 * 是否可以停用
	 * @return
	 */
	public String canStop(){
		return null;
	}
	
	/**
	 * 注销(停用)指定会员
	 * 
	 * @update by sxs 更新添加用户状态设置为已停用
	 * */
	public String disable() {
		
		this.memberBase = memberBaseService.selectById(id);
		memberBase.setState(MemberBase.STATE_DISABLED);
		try {
			User loginUser = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
			HttpServletRequest request = ServletActionContext. getRequest(); 
			String ip = getIpAddr(request);
			ModifyLog ml = new ModifyLog();
			ml.setChanger(loginUser);
			ml.setDatatype("member");
			ml.setDataname("disable");
			ml.setModifyDate(new Date());
			ml.setModifyIP(ip);
			ml.setContent("停用会员");
			ml.setModifyModel(id);
			ml.setFirstData("");
			ml.setEndData("");
			modifyLogService.insert(ml);
			
			memberBaseService.update(memberBase);
			User user = memberBase.getUser();
			user.setLoginCount(5);// 在后台用户由3次改为5次之后，这里由3改为5
			user.setEnabled(false);
			user.setUserState(User.STATE_DISABLED);
			userService.update(user);
		} catch (Exception e) {
			e.printStackTrace();
			return newlist();
		}
		return newlist();
	}

	public String getIpAddr(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = request.getRemoteAddr();
	    }
	    return ip;
	} 
	
	/**
	 * 指定类型的会员列表：顶级机构可以看见所有开户机构的会员； 非顶级机构只能看见自己的会员
	 * */
	private String bank = "全部";// 默认为全部
	private String channel = "全部";// 默认为全部
	
	private String signState;

	public String getSignState() {
		return signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public void getAllMemberBaseList(){
		try {
			//TODO 
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			logUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String orgName = null;// loginUser.getOrg().getName();
			
			if (Org.TOP_ORG_CODEING.equals(logUser.getOrg().getCoding()) || Org.TOP_ORG_CODEING2.equals(logUser.getOrg().getCoding())) {
				this.setTopOrg(true);
				if(!(this.orgName == null || this.orgName.trim().equals(""))){
					String strPattern="^\\d+$";
					Pattern p = Pattern.compile(strPattern);
					Matcher m = p.matcher(this.orgName);
					boolean flag =  m.find();
					if(flag)
						orgName = this.orgService.findOrg(this.orgName).getName();
					else
						orgName = this.orgName;
				}else
					orgName = this.orgName;
			}
			// 页面上分页查询满足条件的会员信息
			List<Map<String, Object>> result =
					memberBaseService.listMembersByCondition2(keyword,jingbanren,
							memberTypeId, memberState, logUser.getOrg()
									.getCoding(), orgName, provinceCode,
							cityCode,rows,getPage(),
							startDate, endDate, bank,signState,channel);
			
			// 页面上分页查询满足条件的会员信息
			List<Map<String, Object>> result2 =
					memberBaseService.listMembersByCondition2(keyword,jingbanren,
							memberTypeId, memberState, logUser.getOrg()
									.getCoding(), orgName, provinceCode,
							cityCode,9999999,1,
							startDate, endDate, bank,signState,channel);
					
			int total = result2 == null? 0 : result2.size() ;
			
			if(total > 0){
				for (Map<String, Object> obj : result) {
					if ("0".equals(obj.get("category"))){
						obj.put("PNAME", obj.get("eName"));
					}
					obj.put("showok", true);
				}
			}
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String newlist() {
		logUser = (User) SecurityContextHolder.getContext()
		.getAuthentication().getPrincipal();
		if (Org.TOP_ORG_CODEING.equals(logUser.getOrg().getCoding()) || Org.TOP_ORG_CODEING2.equals(logUser.getOrg().getCoding())) {
			this.setTopOrg(true);
			return "newlist_topOrg";
		}
		return "newlist";
	}
	/**
	 * 会员列表
	 * 
	 * @return
	 */
	public String list() {
		initListData();
		try {
			logUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String orgName = null;// loginUser.getOrg().getName();
			
			if (Org.TOP_ORG_CODEING.equals(logUser.getOrg().getCoding()) || Org.TOP_ORG_CODEING2.equals(logUser.getOrg().getCoding())) {
				this.setTopOrg(true);
				if(!(this.orgName == null  || this.orgName.trim().equals(""))){
					String strPattern="^\\d+$";
					Pattern p = Pattern.compile(strPattern);
					Matcher m = p.matcher(this.orgName);
					boolean flag =  m.find();
					if(flag)
						orgName = this.orgService.findOrg(this.orgName).getName();
					else
						orgName = this.orgName;
				}else
					orgName = this.orgName;
			}
			if (excel) {
				// 导出所有满足条件的会员信息为Excel
				if (null == memberTypeId) {
					memberTypeId = memberTypeService.selectByName("投资方")
							.getId();
				}
				membertype = memberTypeService.selectById(memberTypeId)
						.getCode();
				return exportInvestors();
				
			} else {
				// 页面上分页查询满足条件的会员信息
				ServletActionContext.getRequest().setAttribute(
						"pageView",
						memberBaseService.listMembersByCondition(keyword,
								memberTypeId, memberState, logUser.getOrg()
										.getCoding(), orgName, provinceCode,
								cityCode, getShowRecord(), getPage(),
								startDate, endDate, bank,signState));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return LIST;
	}
	
	/**
	 * 会员列表
	 * 
	 * @return
	 */
	public String listForChoose() {
		initListData();
		try {
			logUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String orgName = null;// loginUser.getOrg().getName();
			if (Org.TOP_ORG_CODEING.equals(logUser.getOrg().getCoding())) {
				this.setTopOrg(true);
				orgName = this.orgName;
			}
				// 页面上分页查询满足条件的会员信息
			ServletActionContext.getRequest().setAttribute(
						"pageView",
						memberBaseService.listMembersByCondition(keyword,
								memberTypeId, memberState, logUser.getOrg()
										.getCoding(), orgName, provinceCode,
								cityCode, getShowRecord(), getPage(),
								startDate, endDate, bank,signState));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "listForChoose";
	}

	/**
	 * 导出投资会员为Excel表格
	 * */
	public String exportInvestors() throws Exception {
		try {
			//TODO 
			logUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String orgName = null;// loginUser.getOrg().getName();
			
			if (Org.TOP_ORG_CODEING.equals(logUser.getOrg().getCoding()) || Org.TOP_ORG_CODEING2.equals(logUser.getOrg().getCoding())) {
				this.setTopOrg(true);
				if(!(this.orgName == null || this.orgName.trim().equals(""))){
					String strPattern="^\\d+$";
					Pattern p = Pattern.compile(strPattern);
					Matcher m = p.matcher(this.orgName);
					boolean flag =  m.find();
					if(flag)
						orgName = this.orgService.findOrg(this.orgName).getName();
					else
						orgName = this.orgName;
				}else
					orgName = this.orgName;
			}
			// 页面上分页查询满足条件的会员信息
			List<Map<String, Object>> result =
					memberBaseService.listMembersByCondition2(keyword,jingbanren,
							memberTypeId, memberState, logUser.getOrg()
									.getCoding(), orgName, provinceCode,
							cityCode, 9999999, 1,
							startDate, endDate, bank,signState,channel);
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("list", result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "exportInvestorsEx";
	}

	/**
	 * 导出融资方等类型的会员为Excel表格
	 * */
	private void exportFinancier(OutputStream os) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		WritableWorkbook book = Workbook.createWorkbook(os);
		WritableSheet sheet = book.createSheet("sheet1", 0);
		String fontName = "楷体_GB2312";
		WritableFont font2 = new WritableFont(
				WritableFont.createFont(fontName), 10, WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font2);
		format.setAlignment(jxl.format.Alignment.CENTRE);// 设置为居中

		WritableFont font1 = new WritableFont(WritableFont.TIMES, 14,
				WritableFont.BOLD);
		WritableCellFormat format1 = new WritableCellFormat(font1);
		format1.setAlignment(jxl.format.Alignment.CENTRE);// 设置为居中

		sheet.mergeCells(0, 0, 20, 0);

		SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String start = "无限制";
		String end = "无限制";

		if (null != startDate) {
			start = sdfDateFormat.format(startDate);
		}
		if (null != endDate) {
			end = sdfDateFormat.format(endDate);
		}
		// 在Label对象的构造方法中指明单元格位置是第一列第一行(0,0)
		Label label = new Label(0, 0, "昆投互联网金融交易"
				+ memberTypeService.selectById(memberTypeId).getName()
				+ "会员信息(开户时间从" + start + "到" + end + ","
				+ sdf.format(new Date()) + "导出)", format1);
		sheet.addCell(label);

		format1.setFont(font1);

		label = new Label(0, 1, "开户时间", format);
		sheet.addCell(label);

		label = new Label(1, 1, "会员编号", format);
		sheet.addCell(label);
		label = new Label(2, 1, "企业名称", format);
		sheet.addCell(label);
		label = new Label(3, 1, "法人姓名", format);
		sheet.addCell(label);
		label = new Label(4, 1, "法人身份证号码", format);
		sheet.addCell(label);
		label = new Label(5, 1, "个人会员手机", format);
		sheet.addCell(label);
		label = new Label(6, 1, "授权代理人姓名", format);
		sheet.addCell(label);
		label = new Label(7, 1, "企业会员联系人手机", format);
		sheet.addCell(label);
		label = new Label(8, 1, "企业会员手机", format);
		sheet.addCell(label);
		label = new Label(9, 1, "固定电话", format);
		sheet.addCell(label);
		label = new Label(10, 1, "传真", format);
		sheet.addCell(label);
		label = new Label(11, 1, "开户机构", format);
		sheet.addCell(label);
		label = new Label(12, 1, "账户余额(元)", format);
		sheet.addCell(label);
		label = new Label(13, 1, "银行账号", format);
		sheet.addCell(label);
		label = new Label(14, 1, "详细地址", format);
		sheet.addCell(label);
		label = new Label(15, 1, "企业性质", format);
		sheet.addCell(label);
		label = new Label(16, 1, "所属行业", format);
		sheet.addCell(label);
		label = new Label(17, 1, "地区", format);
		sheet.addCell(label);
		label = new Label(18, 1, "开户审核日期", format);
		sheet.addCell(label);
		label = new Label(19, 1, "介绍人", format);
		sheet.addCell(label);
		label = new Label(20, 1, "三方存管签约状态", format);
		sheet.addCell(label);
		label = new Label(21, 1, "三方存管签约时间", format);
		sheet.addCell(label);

		int pageNo = 1;
		int pageSize = getShowRecord();
		// 已经显示的数据数量
		int offset = (pageNo - 1) * pageSize;

		PageView<MemberBase> pageView = memberBaseService
				.listMembersByCondition(keyword, memberTypeId, memberState,
						logUser.getOrg().getCoding(), orgName, provinceCode,
						cityCode, pageSize, pageNo, startDate, endDate, bank,signState);

		long pages = pageView.getTotalpage();

		for (int page = 0; page < pages; page++) {
			List<MemberBase> members = pageView.getRecords();
			int rows = members.size();
			for (int i = 0; i < rows; i++) {
				MemberBase mb = (MemberBase) members.get(0);
				int index = i + 2 + offset;
				// 开户时间
				label = new Label(0, index, sdf.format(mb.getCreateDate()));
				sheet.addCell(label);

				label = new Label(1, index, mb.getUser().getUsername());
				sheet.addCell(label);

				label = new Label(2, index, mb.geteName());
				sheet.addCell(label);

				label = new Label(3, index, mb.geteLegalPerson());

				sheet.addCell(label);

				label = new Label(4, index, mb.getIdCardNo());
				sheet.addCell(label);

				label = new Label(5, index, mb.getpMobile());
				sheet.addCell(label);

				font2 = new WritableFont(WritableFont.createFont(fontName));
				format = new WritableCellFormat(font2);

				label = new Label(6, index, mb.geteContact());
				label.setCellFormat(format);
				sheet.addCell(label);

				label = new Label(7, index, mb.geteContactMobile());
				sheet.addCell(label);
				
				label = new Label(8, index, mb.geteMobile());
				sheet.addCell(label);

				label = new Label(9, index, mb.geteContactPhone());
				sheet.addCell(label);

				label = new Label(10, index, mb.geteContactFax());
				sheet.addCell(label);

				label = new Label(11, index, mb.getUser().getOrg().getName());
				sheet.addCell(label);

				label = new Label(12, index, CurrencyOperator.addComma(mb
						.getUser().getUserAccount().getBalance()));
				sheet.addCell(label);

				label = new Label(13, index, mb.getBankAccount());
				sheet.addCell(label);

				label = new Label(14, index, mb.geteAddress());
				sheet.addCell(label);

				String companyProperty = "";

				String industry = "";

				if (MemberBase.CATEGORY_ORG.equals(mb.getCategory())) {
					companyProperty = mb.getCompanyProperty().getName();
					industry = mb.getIndustry().getName();
				}

				label = new Label(15, index, companyProperty);
				sheet.addCell(label);

				label = new Label(16, index, industry);
				sheet.addCell(label);

				label = new Label(17, index, mb.getProvinceName()
						+ mb.getCityName());
				sheet.addCell(label);

				String d = "无审核时间";
				MemberAudit audit = this.memberAuditService
						.findByMemberBaseId(mb.getId());
				if (null != audit && null != audit.getAdditDate()) {
					d = sdf.format(audit.getAdditDate());
				}
				label = new Label(18, index, d);
				sheet.addCell(label);

				label = new Label(19, index, mb.getJingbanren());
				sheet.addCell(label);
				
				String isFlag = mb.getUser().getFlag();//
				String flag = "";
				if(isFlag == null || isFlag.equals("") || isFlag.equals("0")){
					isFlag = "0";
					flag = "未签约";
				}else if(isFlag.equals("1")){
					flag = "签约中";
				}else if(isFlag.equals("2")){
					flag = "已签约";
				}else if(isFlag.equals("3")){
					flag = "已解约";
				}
				label = new Label(20, index, flag);
				sheet.addCell(label);
				
				String signDate = null;
				if(isFlag.equals("0") || isFlag.equals("1")){
					signDate = "-";
				}else
					signDate = sdf.format(mb.getUser().getSignDate());
				label = new Label(21, index, signDate);
				sheet.addCell(label);
				
				label = null;
				members.remove(0);

			}
			pageNo++;
			pageView = memberBaseService.listMembersByCondition(keyword,
					memberTypeId, memberState, logUser.getOrg().getCoding(),
					orgName, provinceCode, cityCode, pageSize, pageNo,
					startDate, endDate, bank,signState);
			offset = (pageNo - 1) * pageSize;
		}
		book.write();
		book.close();
		os.flush();
	}

	/**
	 * 审核不通过的会员列表
	 * */
	public String listByNotPassedAudited() {

		initListData();
		memberState = MemberBase.STATE_NOT_PASS_AUDIT;
		User loginUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String orgName = null;// loginUser.getOrg().getName();
		if (Org.TOP_ORG_CODEING.equals(loginUser.getOrg().getShowCoding())) {
			// 只有顶级机构才能按开户机构查询，其它机构职能查询自己的会员
			this.setTopOrg(true);
			orgName = this.orgName;
		}

		ServletActionContext.getRequest().setAttribute(
				"pageView",
				memberBaseService.listMembersByCondition(keyword, memberTypeId,
						memberState, loginUser.getOrg().getCoding(), orgName,
						provinceCode, cityCode, getShowRecord(), getPage(),
						startDate, endDate, bank,signState));

		return NOT_PASS_AUDIT_LIST;
	}

	private void initListData() {
		User loginUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		this.setMemberTypes(memberTypeService.getList(loginUser));
		regions_province = regionService.getScrollDataCommon(
				"from Region where areaparentcode = '0000' order by areacode",
				new String[] {});
		if (memberBase.province != null && !"".equals(this.memberBase.province)) {
			regions_city = regionService.getScrollDataCommon(
					"from Region where areaparentcode = '"
							+ this.memberBase.province + "' order by areacode",
					new String[] {});
		}

	}
	
	public void state_json_for_select(){
		try {
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			JSONArray array = new JSONArray();
			JSONObject temp = new JSONObject();
			temp.element("value", "0");
			temp.element("text", "全部");
			temp.element("selected", true);
			array.add(temp);
			temp = new JSONObject();
			temp.element("value", MemberBase.STATE_NOT_AUDIT);
			temp.element("text", "待审核");
			array.add(temp);
			temp = new JSONObject();
			temp.element("value", MemberBase.STATE_PASSED_AUDIT);
			temp.element("text", "正常");
			array.add(temp);
			temp = new JSONObject();
			temp.element("value", MemberBase.STATE_NOT_PASS_AUDIT);
			temp.element("text", "未通过审核");
			array.add(temp);
			temp = new JSONObject();
			temp.element("value", MemberBase.STATE_DISABLED);
			temp.element("text", "已停用");
			array.add(temp);
			out.print(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void type_json_for_select(){
		try {
			User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			memberTypes = memberTypeService.getList(loginUser);
			JSONArray array = new JSONArray();
			JSONObject temp = new JSONObject();
			temp.element("value", "0");
			temp.element("text", "全部");
			temp.element("selected", true);
			array.add(temp);
			for(int i=0;i<memberTypes.size();i++){
				MemberType mt = memberTypes.get(i);
				temp.element("value", mt.getId());
				temp.element("text", mt.getName());
				temp.element("selected", false);
				array.add(temp);
			}
			out.print(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void province_json_for_select(){
		try {
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			regions_province = regionService.getScrollDataCommon(
					"from Region where areaparentcode = '0000' order by areacode",
					new String[] {});
			JSONArray array = new JSONArray();
			JSONObject temp = new JSONObject();
			temp.element("value", "0");
			temp.element("text", "全部");
			temp.element("selected", true);
			array.add(temp);
			for(int i=0;i<regions_province.size();i++){
				Region r = regions_province.get(i);
				temp.element("value", r.getAreacode());
				temp.element("text", r.getAreaname_s());
				temp.element("selected", false);
				array.add(temp);
			}
			out.print(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void city_json_for_select(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String province = request.getParameter("province");
			PrintWriter out = ServletActionContext.getResponse().getWriter();
			JSONArray array = new JSONArray();
			if(null == province || "".equals(province.trim())){
				out.print(array);
				return;
			}
			JSONObject temp = new JSONObject();
			temp.element("value", "0");
			temp.element("text", "全部");
			temp.element("selected", true);
			array.add(temp);
			regions_city = regionService.getScrollDataCommon(
					"from Region where areaparentcode = '" + province+"' order by areacode");
			for(int i=0;i<regions_city.size();i++){
				Region r = regions_city.get(i);
				temp.element("value", r.getAreacode());
				temp.element("text", r.getAreaname_s());
				temp.element("selected", false);
				array.add(temp);
			}
			out.print(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 投资方会员列表(修改投资方会员级别)： 顶级机构可以看见所有开户机构的会员； 非顶级机构只能看见自己的会员
	 * */
	public String tList() {
		User loginUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String orgName = null;// loginUser.getOrg().getName();
		if (Org.TOP_ORG_CODEING.equals(loginUser.getOrg().getCoding())) {
			this.setTopOrg(true);
			orgName = this.orgName;
		}
		this.setMemberLevels(memberLevelService.listTAll());
		ServletActionContext.getRequest().setAttribute(
				"pageView",
				memberBaseService.listMembersByCondition(
						keyword,
						memberTypeService.selectByHql(
								"from MemberType where code ='"
										+ MemberType.CODE_INVESTORS + "'")
								.getId(), memberState, loginUser.getOrg()
								.getCoding(), orgName, null, null,
						getShowRecord(), getPage(), startDate, endDate, bank,signState));
		return TLIST;
	}

	/**
	 * 保存指定会员的级别
	 * */
	public String saveLevel() {
		logUser = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
		this.memberBase = this.memberBaseService.selectById(id);
		MemberLevel newLv = memberLevelService.selectById(this.levelId);
		MemberBase mb = memberBaseService.selectById(id);//旧信息
		HttpServletRequest request = ServletActionContext. getRequest(); 
		String ip = getIpAddr(request);
		if(!this.memberBase.getMemberLevel().getId().equals(newLv.getId()) ){
			ModifyLog ml = new ModifyLog();
			ml.setChanger(logUser);
			ml.setDatatype("member");
			ml.setDataname("saveLevel");
			ml.setModifyDate(new Date());
			ml.setModifyIP(ip);
			ml.setModifyModel(id);
			ml.setContent("更改会员级别");
			ml.setFirstData(mb.getMemberLevel().getLevelname());
			ml.setEndData(newLv.getLevelname());
			try {
				modifyLogService.insert(ml);
			} catch (EngineException e) {
				e.printStackTrace();
			}
		}
		this.memberBase.setMemberLevel(newLv);
		try {
			this.memberBaseService.update(this.memberBase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tList();
	}

	/**
	 * 未审核的会员列表
	 * */
	public String notAuditedMemberlist() {
		try {
			User loginUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String orgNo = loginUser.getOrg().getCoding();
			ServletActionContext.getRequest().setAttribute(
					"pageView",
					memberBaseService.listNotAuditedMembers(keyword,
							membertype, orgNo, getShowRecord(), getPage()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NOT_AUDITED_MEMBER_LIST;
	}

	/**
	 * 会员详细信息
	 * */
	public String memberDetails() {
		maybeDetails();
		return MEMBER_DETAILS;
	}

	private void maybeDetails() {
		if (null != id && !"".equals(id)) {
			try {
				this.memberBase = memberBaseService.selectById(id);
				files = uploadFileService
						.getCommonListData("from UploadFile c where c.entityFrom = 'MemberBase' and c.entityId='"
								+ id + "' ");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 显示会员的银行账号信息
	 * */
	public String memberBankAccounts() {
		maybeDetails();
		return MEMBER_BANKACCOUNTS;
	}

	/**
	 * 某个机构的所有融资方的融资进度列表
	 * */
	public String financingProgress() {
		try {
			logUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			String orgName = null;
			if (Org.TOP_ORG_CODEING.equals(logUser.getOrg().getCoding())) {
				this.setTopOrg(true);
				orgName = this.orgName;
			}

			PageView<FinancingBase> pageView = new PageView<FinancingBase>(
					getShowRecord(), getPage());
			ServletActionContext.getRequest()
					.setAttribute("pageView", pageView);

			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("createDate", "desc");
			StringBuilder sb = new StringBuilder(" 1=1    ");

			// 数据控制(本级和下级机构的数据)
			// User u =
			// (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Org org=null;
			// String orgCode="";
			// if(null!=u) org=u.getOrg();
			// if(null!=org){
			// orgCode=org.getCoding();
			// }
			// if(null!=orgCode&&!"".equals(orgCode)){
			//
			// sb.append(" and o.createBy.org.coding like '"+orgCode+"%' ");
			// }else{
			// sb.append(" and o.createBy.org.coding like '@@@@@@@%' ");
			// }
			//
			// List<String> params = new ArrayList<String>();
			// if(null!=keyWord&&!"".equals(keyWord.trim())){
			// keyWord = keyWord.trim();
			// sb.append(" and  o.shortName like ?");
			// params.add("%"+keyWord+"%");
			// }
			// if(null!=queryCode&&!"".equals(queryCode.trim())){
			// queryCode = queryCode.trim();
			// sb.append(" and  o.code like '%"+queryCode+"'");
			// }
			// pageView.setQueryResult(financingBaseService.getScrollData(pageView.getFirstResult(),
			// pageView.getMaxresult(),sb.toString(), params,orderby));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return FINANCIER_FINANCING_PROGRESS;
	}

	@Resource
	private HxbankDealService hxbankDealService;
	private HxbankParam param;

	//华夏存管本行预签
	public String hxthirdsign() {
		User u = (User) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Org org = null;
		String orgCode = "";
		if (null != u)
			org = u.getOrg();
		if (null != org) {
			orgCode = org.getCoding();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		if (null == this.startDate) {
			this.startDate = today;
		}
		if (null == this.endDate) {
			this.endDate = today;
		}
		Date endDateNext = null;
		endDateNext = DateUtils.getAfter(this.endDate, 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(
				getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();

		sb.append(" and o.operator.org.coding like '%" + orgCode + "%' ");

		sb.append(" and ");
		sb.append(" o.trnxCode= 'DZ010' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.startDate)
				+ "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)
				+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(
					pageView.getFirstResult(), pageView.getMaxresult(),
					sb.toString(), params, orderby));
			ServletActionContext.getRequest()
					.setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "hxthirdsign";
	}

	//执行预签操作
	public String hxthirdsign_do() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Org org = null;
		String orgCode = "";
		if (null != u)
			org = u.getOrg();
		if (null != org) {
			orgCode = org.getCoding();
		}
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		User owner = null;
		if (null != this.param && null != this.param.getMerAccountNo()) {
			// 华夏限制：1 必须为投资人，2 必须是未签约成功的，3 会员编号和会员名称必须对应
			String queryString = "from User c where c.flag != '2' and c.userType = 'T' and c.realname= '"
					+ this.param.getAccountName()
					+ "' and c.username = '"
					+ this.param.getMerAccountNo()
					+ "' and c.org.coding like '%" + orgCode + "%' ";
			owner = this.userService.selectByHql(queryString);
		}
		if (null != owner) {
			if (null != owner.getUserAccount()
					&& (owner.getUserAccount().getBalance() + owner
							.getUserAccount().getFrozenAmount()) == 0) {
				boolean go = true;
				char[] s = owner.getRealname().toCharArray();
				for(int i=0;i<s.length;i++){
					String aim = s[i]+"";
					String iso_gb2312 = new String(aim.getBytes("gb2312"),"ISO-8859-1");
					String gb2312 = new String(iso_gb2312.getBytes("ISO-8859-1"),"gb2312");
					if(gb2312.contains("?")){
						go = false;
						vo.setFlag(false);
						vo.setTip("“"+aim+"”字银行可能无法识别，会影响“"+owner.getRealname()+"”签约，暂时不能预签");
						break;
					}
				}
				if(go){
					//子账户同步(本行)
					vo = this.hxbankDealService.subAccountSync(p, getLoginUser(),owner);
				}
			} else {
				vo.setFlag(false);
				vo.setTip("投资方账户可用金额与冻结金额必须为0");
			}
		} else {
			vo.setFlag(false);
			vo.setTip("投资方用户名不能为空或不存在或已经签约成功");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}
	
	//华夏存管他行签约
	public String hxthirdsigned(){
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Org org = null;
		String orgCode = "";
		if (null != u)
			org = u.getOrg();
		if (null != org) {
			orgCode = org.getCoding();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		if (null == this.startDate) {
			this.startDate = today;
		}
		if (null == this.endDate) {
			this.endDate = today;
		}
		Date endDateNext = null;
		endDateNext = DateUtils.getAfter(this.endDate, 1);
		PageView<HxbankDeal> pageView = new PageView<HxbankDeal>(
				getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("createDate", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();
		
		sb.append(" and o.operator.org.coding like '%" + orgCode + "%' ");
		
		sb.append(" and ");
		sb.append(" o.trnxCode= 'DZ020' ");
		sb.append(" and ");
		sb.append(" o.createDate >= to_date('" + format.format(this.startDate)
				+ "','yyyy-MM-dd')");
		sb.append(" and ");
		sb.append(" o.createDate <= to_date('" + format.format(endDateNext)
				+ "','yyyy-MM-dd')");
		try {
			pageView.setQueryResult(this.hxbankDealService.getScrollData(
					pageView.getFirstResult(), pageView.getMaxresult(),
					sb.toString(), params, orderby));
			ServletActionContext.getRequest()
					.setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("signed");
		return "hxthirdsigned";
	}
	
	//执行签约操作
	public String hxthirdsigned_do() throws Exception{
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Org org = null;
		String orgCode = "";
		if (null != u)
			org = u.getOrg();
		if (null != org) {
			orgCode = org.getCoding();
		}
		HxbankVO vo = new HxbankVO();
		HxbankParam p = this.param;
		User owner = null;
		if (null != this.param && null != this.param.getMerAccountNo()) {
			// 华夏限制：1 必须为投资人，2 必须是未签约成功的，3 会员编号和会员名称必须对应
			String queryString = "from User c where c.flag != '2' and c.userType = 'T' and c.realname= '"
					+ this.param.getAccountName()
					+ "' and c.username = '"
					+ this.param.getMerAccountNo()
					+ "' and c.org.coding like '%" + orgCode + "%' ";
			owner = this.userService.selectByHql(queryString);
		}
		if (null != owner) {
			if (null != owner.getUserAccount()
					&& (owner.getUserAccount().getBalance() + owner
							.getUserAccount().getFrozenAmount()) == 0) {
				boolean go = true;
				char[] s = owner.getRealname().toCharArray();
				for(int i=0;i<s.length;i++){
					String aim = s[i]+"";
					String iso_gb2312 = new String(aim.getBytes("gb2312"),"ISO-8859-1");
					String gb2312 = new String(iso_gb2312.getBytes("ISO-8859-1"),"gb2312");
					if(gb2312.contains("?")){
						go = false;
						vo.setFlag(false);
						vo.setTip("“"+aim+"”字银行可能无法识别，会影响“"+owner.getRealname()+"”签约，暂时不能签约");
						break;
					}
				}
				if(go){
					//子账户签约(他行)
					vo = this.hxbankDealService.subAccountSigned(p, getLoginUser(),owner);
				}
			} else {
				vo.setFlag(false);
				vo.setTip("投资方账户可用金额与冻结金额必须为0");
			}
		} else {
			vo.setFlag(false);
			vo.setTip("投资方用户名不能为空或不存在或已经签约成功");
		}
		getOut().write(vo.getJSON(vo));
		return null;
	}

	// 会员统计，当前登录者的机构及其下属机构开户的会员统计
	private List<MemberTotalVO> totalMembers = new ArrayList<MemberTotalVO>();
	private int sum_total = 0;
	private int sum_total_T = 0;
	private int sum_total_R = 0;
	private int sum_total_D = 0;
	private int sum_total_Y = 0;
	private int sum_total_Q = 0;

	public String totalMembers() {
		User loginUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String login_orgCode = loginUser.getOrg().getShowCoding();
		String login_orgName = loginUser.getOrg().getName();
		String orgs = "";
		if (!"530100".equals(login_orgCode)) {// 过滤掉顶层的机构530100
			orgs = login_orgCode + "@" + login_orgName;
		}
		List<Org> os = this.orgService.getOrgChildren(login_orgCode);
		for (int i = 0; i < os.size(); i++) {
			if (!"".equals(orgs)) {
				orgs += ",";
			}
			orgs += os.get(i).getShowCoding() + "@" + os.get(i).getName();
		}
		Date today = new Date();
		if (null == this.startDate) {
			this.startDate = today;
		}
		if (null == this.endDate) {
			this.endDate = today;
		}
		
		System.out.println(orgs);
		
		this.totalMembers = this.memberBaseService.totalMembers(this.startDate,
				this.endDate, orgs);
		for (MemberTotalVO v : this.totalMembers) {
			this.sum_total += v.getTotal();
			this.sum_total_T += v.getTotal_T();
			this.sum_total_R += v.getTotal_R();
			this.sum_total_D += v.getTotal_D();
			this.sum_total_Y += v.getTotal_Y();
			this.sum_total_Q += v.getTotal_Q();
		}
		return "totalMembers";
	}

	/**
	 * 跳转到授权会员 债权转让 页面
	 * 
	 * @return
	 */
	public String toAssignmentPage() {
		return "toAssignmentPage";
	}

	@Resource
	public VersionRestrainService versionRestrainService;
	

	/**
	 * 搜索债权转让会员
	 * 
	 * @return
	 */
	public String assignment() {
		System.out.println(this.isoverdate + " xiaoxie ");
		User u = (User) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Org org = null;
		String orgCode = "";
		if (null != u)
			org = u.getOrg();
		if (null != org) {
			orgCode = org.getCoding();
		}

		PageView<VersionRestrain> pageView = new PageView<VersionRestrain>(
				getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();

		StringBuilder sb = new StringBuilder(" 1=1 ");
		List<String> params = new ArrayList<String>();

		if (this.getKeyWord() != null) {
			sb.append(" and username like '%" + this.getKeyWord() + "%' ");
		}
		if (this.isoverdate) {
			System.out.println("only in date member is listed");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String now = sdf.format(new Date());
			long nows = Long.parseLong(now);
			sb.append(" and endDate > "+nows + " ");
		}
		sb.append(" ");

		try {
			pageView.setQueryResult(this.versionRestrainService.getScrollData(
					pageView.getFirstResult(), pageView.getMaxresult(),
					sb.toString(), params, orderby));
			ServletActionContext.getRequest()
					.setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "assignment";
	}

	/**
	 * 新授权 用户债权转让
	 * 
	 * @return
	 * @throws Exception
	 */
	public String swb() throws Exception {
		if (this.getNewAuthorizerUsername() == null) {
			return null;
		}
		if (this.getNewAuthorizerEndDate() == null) {
			return null;
		}
		vr = this.versionRestrainService
				.selectById(newAuthorizerUsername);
		JSONObject resultOfNew = new JSONObject(); 
        resultOfNew.element("code", "20"); 
        VersionRestrain newAuthorizer = new VersionRestrain();
		newAuthorizer.setUsername(newAuthorizerUsername);
		newAuthorizer.setEndDate(Long.parseLong(newAuthorizerEndDate));
		if(vr!=null){
			vr.setEndDate(Long.parseLong(newAuthorizerEndDate));
			try{
			this.versionRestrainService.update(vr);
			resultOfNew.element("newresult", "1");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),resultOfNew.toString());
			}catch(EngineException e){
				e.printStackTrace();
				resultOfNew.element("newresult", "0");
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),resultOfNew.toString());
			}
		}else{
			this.versionRestrainService.insert(newAuthorizer);
			resultOfNew.element("newresult", "1");
			DoResultUtil.doStringResult(ServletActionContext.getResponse(),resultOfNew.toString());
		}
		return null;
	}

	private VersionRestrain vr;
	
	/**
	 * 验证是否存在授权用户
	 * 
	 * @return json
	 * @throws Exception
	 */
	public String validateNewAuthorizer() throws Exception {
		JSONObject result = new JSONObject();
//		result.element("code", "1");
		if (newAuthorizerUsername == null) {
			System.out.println("{\"resul\"':\"错误！未接受到授权信息！\"}");
		} else {
			vr = this.versionRestrainService
					.selectById(newAuthorizerUsername);
			User user = this.userService.findUser(newAuthorizerUsername);
			if (user != null && vr != null) {
//				System.out.println("账户已经被授权过！");
				result.element("result", "2");
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						result.toString());
			} else if (user != null) {
				result.element("result", "1");
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						result.toString());
			} else {
				result.element("result", "0");
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),
						result.toString());
			}
		}
		return null;
	}

	public String showlog() throws Exception{
		MemberBase mb = memberBaseService.selectById(id);
		PageView<ModifyLog> pageView = new PageView<ModifyLog>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Org org = null;
		String orgCode = "";
		if (null != u) org = u.getOrg();
		if (null != org) {
			orgCode = org.getCoding();
		}
		if(!orgCode.equalsIgnoreCase("1")){
			if (null != orgCode && !"".equals(orgCode)) {
				sb.append(" and o.changer.org.coding like '" + orgCode + "%' ");
			} else {
				sb.append(" and o.changer.org.coding like '@@@@@@@%' ");
			}
		}
		
		List<String> params = new ArrayList<String>();
		
		if(mb!=null){
			if(mb.getUser().getUsername()!=null){
				sb.append(" and (o.modifyModel = '"+mb.getId()+"'  or o.modifyModel = '"+mb.getUser().getUsername()+"' )");
			}else
				sb.append(" and o.modifyModel = '"+mb.getId()+"' ");
		}
		QueryResult<ModifyLog> qr = modifyLogService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
		pageView.setQueryResult(qr);
		
		
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		
		return "showlog";
	}
	
	public String showalllog() throws Exception{
		PageView<ModifyLog> pageView = new PageView<ModifyLog>(getShowRecord(), getPage());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 ");
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Org org = null;
		String orgCode = "";
		if (null != u) org = u.getOrg();
		if (null != org) {
			orgCode = org.getCoding();
		}
		if(!orgCode.equalsIgnoreCase("1")){
			if (null != orgCode && !"".equals(orgCode)) {
				sb.append(" and o.changer.org.coding like '" + orgCode + "%' ");
			} else {
				sb.append(" and o.changer.org.coding like '@@@@@@@%' ");
			}
		}
		
		Date today = new Date();
		if(null==this.getStartDate()){
			startDate = today;
		}
		if(null==this.getEndDate()){
			endDate = today;
		}
		
		List<String> params = new ArrayList<String>();
		
		if(this.getStartDate()!=null & this.getEndDate()!=null){
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			if(DateUtils.getBetween(this.getStartDate(),this.getEndDate())<0)
				endDateNext = DateUtils.getAfter(this.getStartDate(), 1);
			sb.append(" and o.modifyDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd')"+ " and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')" );
		}
		if(this.getType()!=null && !this.getType().equals("0")){
			sb.append(" and o.dataname = '"+this.getType()+"'");
		}
		
		QueryResult<ModifyLog> qr = modifyLogService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
		pageView.setQueryResult(qr);
		
		
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		
		return "showalllog";
	}
	
	
	
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	private String newAuthorizerUsername;

	private String newAuthorizerEndDate;

	private List<VersionRestrain> allowedUsers;

	public List<VersionRestrain> getAllowedUsers() {
		return allowedUsers;
	}

	public void setAllowedUsers(List<VersionRestrain> allowedUsers) {
		this.allowedUsers = allowedUsers;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberBase getMemberBase() {
		return memberBase;
	}

	public void setMemberBase(MemberBase memberBase) {
		this.memberBase = memberBase;
	}

	public List<MemberType> getMemberTypes() {
		return memberTypes;
	}

	public void setMemberTypes(List<MemberType> memberTypes) {
		this.memberTypes = memberTypes;
	}

	/**
	 * 返回是否为修改会员
	 * 
	 * @return true,修改会员；false,新增会员
	 * */
	private boolean isModify() {// 402881ed375968b40137596c81680001
		boolean ret = null != id && !"".equals(id);
		this.setChange(ret);
		return ret;
	}

	public Map<Integer, String> getSexes() {
		sexes.put(0, "男");
		sexes.put(1, "女");
		return sexes;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public MemberTypeService getMemberTypeService() {
		return memberTypeService;
	}

	public void setMemberTypeService(MemberTypeService memberTypeService) {
		this.memberTypeService = memberTypeService;
	}

	public MemberBaseService getMemberBaseService() {
		return memberBaseService;
	}

	public void setMemberBaseService(MemberBaseService memberBaseService) {
		this.memberBaseService = memberBaseService;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setSexes(Map<Integer, String> sexes) {
		this.sexes = sexes;
	}

	public Map<Integer, String> getCategories() {	
		categories.put(1, "个人");
		categories.put(0, "机构");
		return categories;
	}

	public void setCategories(Map<Integer, String> categories) {
		this.categories = categories;
	}

	public List<Region> getRegions_province() {
		return regions_province;
	}

	public void setRegions_province(List<Region> regionsProvince) {
		regions_province = regionsProvince;
	}

	public List<Region> getRegions_city() {
		return regions_city;
	}

	public void setRegions_city(List<Region> regionsCity) {
		regions_city = regionsCity;
	}

	public File getOrgCertificate() {
		return orgCertificate;
	}

	public void setOrgCertificate(File orgCertificate) {
		this.orgCertificate = orgCertificate;
	}

	public File getBusLicense() {
		return busLicense;
	}

	public void setBusLicense(File busLicense) {
		this.busLicense = busLicense;
	}

	public File geteLegalPersonIdCard() {
		return legalPersonIdCard;
	}

	public void seteLegalPersonIdCard(File eLegalPersonIdCard) {
		this.legalPersonIdCard = eLegalPersonIdCard;
	}

	public File getTaxRegCertificate() {
		return taxRegCertificate;
	}

	public void setTaxRegCertificate(File taxRegCertificate) {
		this.taxRegCertificate = taxRegCertificate;
	}

	public List<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(List<String> fileIds) {
		this.fileIds = fileIds;
	}

	public List<UploadFile> getFiles() {
		return files;
	}

	public void setFiles(List<UploadFile> files) {
		this.files = files;
	}

	@Override
	public void prepare() throws Exception {
		if (isModify()) {
			memberBase = this.memberBaseService.selectById(id);
		} else {
			memberBase = new MemberBase();
		}
	}

	public String getMemberTypeId() {
		return memberTypeId;
	}

	public void setMemberTypeId(String memberTypeId) {
		this.memberTypeId = memberTypeId;
	}

	public List<MemberLevel> getMemberLevels() {
		return memberLevels;
	}

	public void setMemberLevels(List<MemberLevel> memberLevels) {
		this.memberLevels = memberLevels;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getMembertype() {
		return membertype;
	}

	public void setMembertype(String membertype) {
		this.membertype = membertype;
	}

	public List<Industry> getIndustries() {
		return industries;
	}

	public void setIndustries(List<Industry> industries) {
		this.industries = industries;
	}

	public List<CompanyProperty> getCompanyProperties() {
		return companyProperties;
	}

	public void setCompanyProperties(List<CompanyProperty> companyProperties) {
		this.companyProperties = companyProperties;
	}

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getCompanyPropertyId() {
		return companyPropertyId;
	}

	public void setCompanyPropertyId(String companyPropertyId) {
		if (null == companyPropertyId || "".equals(companyPropertyId)) {
			companyPropertyId = "0";
		}
		this.companyPropertyId = companyPropertyId;
	}

	public Map<Byte, String> getMemberStates() {
		memberStates.clear();
		memberStates.put((byte) 0, "全部");
		memberStates.put(Byte.valueOf(MemberBase.STATE_NOT_AUDIT), "待审核");
		memberStates.put(Byte.valueOf(MemberBase.STATE_PASSED_AUDIT), "正常");
		memberStates
				.put(Byte.valueOf(MemberBase.STATE_NOT_PASS_AUDIT), "未通过审核");
		memberStates.put(Byte.valueOf(MemberBase.STATE_DISABLED), "已停用");
		return memberStates;
	}

	public String getMemberState() {
		return memberState;
	}

	public void setMemberState(String memberState) {
		this.memberState = memberState;
	}

	public UploadFileService getUploadFileService() {
		return uploadFileService;
	}

	public void setUploadFileService(UploadFileService uploadFileService) {
		this.uploadFileService = uploadFileService;
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

	public void setLegalPersonIdCardContentType(
			String legalPersonIdCardContentType) {
		this.legalPersonIdCardContentType = legalPersonIdCardContentType;
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

	public void setTaxRegCertificateContentType(
			String taxRegCertificateContentType) {
		this.taxRegCertificateContentType = taxRegCertificateContentType;
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

	public void setBankCardFrontImgContentType(
			String bankCardFrontImgContentType) {
		this.bankCardFrontImgContentType = bankCardFrontImgContentType;
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

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public boolean isTopOrg() {
		return topOrg;
	}

	public void setTopOrg(boolean topOrg) {
		this.topOrg = topOrg;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public boolean isCheckResult() {
		return checkResult;
	}

	public void setCheckResult(boolean checkResult) {
		this.checkResult = checkResult;
	}

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public boolean isExcel() {
		return excel;
	}

	public void setExcel(boolean excel) {
		this.excel = excel;
	}

	public Date getStatisticalDate() {
		return statisticalDate;
	}

	public void setStatisticalDate(Date statisticalDate) {
		this.statisticalDate = statisticalDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}

	public String getErrorStr() {
		return errorStr;
	}

	public void setCanReset(boolean canReset) {
		this.canReset = canReset;
	}

	public boolean isCanReset() {
		return canReset;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setJingbanren(String jingbanren) {
		this.jingbanren = jingbanren;
	}

	public String getJingbanren() {
		return jingbanren;
	}

	public void setTotalMembers(List<MemberTotalVO> totalMembers) {
		this.totalMembers = totalMembers;
	}

	public List<MemberTotalVO> getTotalMembers() {
		return totalMembers;
	}

	public void setSum_total(int sum_total) {
		this.sum_total = sum_total;
	}

	public int getSum_total() {
		return sum_total;
	}

	public void setSum_total_T(int sum_total_T) {
		this.sum_total_T = sum_total_T;
	}

	public int getSum_total_T() {
		return sum_total_T;
	}

	public void setSum_total_D(int sum_total_D) {
		this.sum_total_D = sum_total_D;
	}

	public int getSum_total_D() {
		return sum_total_D;
	}

	public void setSum_total_Y(int sum_total_Y) {
		this.sum_total_Y = sum_total_Y;
	}

	public int getSum_total_Y() {
		return sum_total_Y;
	}

	public void setSum_total_Q(int sum_total_Q) {
		this.sum_total_Q = sum_total_Q;
	}

	public int getSum_total_Q() {
		return sum_total_Q;
	}

	public void setSum_total_R(int sum_total_R) {
		this.sum_total_R = sum_total_R;
	}

	public int getSum_total_R() {
		return sum_total_R;
	}

	public List<BankLibrary> getBanklibrary() {
		return banklibrary;
	}

	public void setBanklibrary(List<BankLibrary> banklibrary) {
		this.banklibrary = banklibrary;
	}

	public String getBanklibrary_id() {
		return banklibrary_id;
	}

	public void setBanklibrary_id(String banklibrary_id) {
		this.banklibrary_id = banklibrary_id;
	}

	public HxbankParam getParam() {
		return param;
	}

	public void setParam(HxbankParam param) {
		this.param = param;
	}

	public String getNewAuthorizerUsername() {
		return newAuthorizerUsername;
	}

	public void setNewAuthorizerUsername(String newAuthorizerUsername) {
		this.newAuthorizerUsername = newAuthorizerUsername;
	}

	public String getNewAuthorizerEndDate() {
		return newAuthorizerEndDate;
	}

	public void setNewAuthorizerEndDate(String newAuthorizerEndDate) {
		this.newAuthorizerEndDate = newAuthorizerEndDate;
	}

	
	
	private boolean isoverdate;

	public boolean isIsoverdate() {
		return isoverdate;
	}

	public void setIsoverdate(boolean isoverdate) {
		this.isoverdate = isoverdate;
	}

	public String getFirstBankAccount() {
		return firstBankAccount;
	}

	public void setFirstBankAccount(String firstBankAccount) {
		this.firstBankAccount = firstBankAccount;
	}

	public String getFirstEmobile() {
		return firstEmobile;
	}

	public void setFirstEmobile(String firstEmobile) {
		this.firstEmobile = firstEmobile;
	}

	public String getFirstPmobile() {
		return firstPmobile;
	}

	public void setFirstPmobile(String firstPmobile) {
		this.firstPmobile = firstPmobile;
	}

	public String getFirstIdCardNo() {
		return firstIdCardNo;
	}

	public void setFirstIdCardNo(String firstIdCardNo) {
		this.firstIdCardNo = firstIdCardNo;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	
}
