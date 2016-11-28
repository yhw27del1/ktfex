package com.kmfex.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.driver.OracleTypes;

import org.apache.struts2.ServletActionContext;
import org.apache.velocity.VelocityContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.MemberBaseVo;
import com.kmfex.MoneyFormat;
import com.kmfex.cache.service.FinancingCacheService;
import com.kmfex.model.BusinessType;
import com.kmfex.model.ContractKeyData;
import com.kmfex.model.ContractTemplate;
import com.kmfex.model.CostItem;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.FinancingContract;
import com.kmfex.model.FinancingCost;
import com.kmfex.model.FinancingRestrain;
import com.kmfex.model.Group;
import com.kmfex.model.InvestRecord;
import com.kmfex.model.MemberAudit;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberGuarantee;
import com.kmfex.service.AccountDealService;
import com.kmfex.service.BusinessTypeService;
import com.kmfex.service.ChargingStandardService;
import com.kmfex.service.ContractTemplateService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingContractService;
import com.kmfex.service.FinancingCostService;
import com.kmfex.service.FinancingRestrainService;
import com.kmfex.service.GroupService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberAuditService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberGuaranteeService;
import com.kmfex.service.OpenCloseDealService;
import com.kmfex.service.PaymentRecordService;
import com.kmfex.service.PreFinancingBaseService;
import com.kmfex.util.ReadsStaticConstantPropertiesUtil;
import com.kmfex.util.SMSNewUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.model.Account;
import com.wisdoor.core.model.Logs;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.UploadFile;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageString;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.core.service.DictionaryService;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.service.UploadFileService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.service.BaseService.OutParameterModel;
import com.wisdoor.core.utils.Constant;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.utils.DoubleUtils;
import com.wisdoor.core.utils.IpAddrUtil;
import com.wisdoor.core.utils.StringUtils;
import com.wisdoor.core.utils.UploadFileUtils;
import com.wisdoor.core.utils.VelocityUtils;
import com.wisdoor.core.vo.CommonVo;
import com.wisdoor.struts.BaseAction;

/**
 * 融资项目管理
 * 
 * @author 
 * 
 * <pre> 
 * 修改记录 2013.09.09	对合同模版进行筛选 删选掉版本陈旧和非对应的合同模版
 * 修改记录 2012-07-30  增加融资方、担保方选择过滤 2012-07-31 1、融资项目查询，
 * 数据显示根据融资项目的融资方的开户人所属机构层次过滤 
 * 2012-08-14  新增cancel()方法，实现取消签约之前的融资项目的功能。
 * 2012-08-27  修改prjournaling()方法，调用PaymentRecordService的
 * selectByFanacingBase(String finacingBaseId)方法以只返回还款记录的第一期记录
 * 2013年07月26日11:14  新增方法fabu(),fabuUI(),修改方法guadanUI()发布信息状态的控制
 * 2013年07月31日15:15  修改方法guadanUI(),fabuUI()过滤已经停用的组
 * 2013年08月09日15:15  修改方法guadan(),check()控制并发
 * 2013年08月16日11:15 修改方法listQuery()优化查询
 * 2013年08月28日13:36  修改方法list()优化查询
 * 2013年08月30日15:01  修改方法guadanUI(),guadan()挂单时，可以修改合同模版
 * 2013年09月05日13:01  修改方法saveDbhtFile(),dbhtFileList()融资签约列表上传担保合同
 * 2013年09月10日16:01 修改方法financiers()融资方选择加入融资方编号,同名会选择错误问题
 * 2013年09月17日13:01 新增方法checkUI(),check()行业分分类
 * 2013年10月18日13:01 融资项目信息加入导出excel功能
 * 2013年11月13日14:39 缓存修改了fabu()、guadan()、depaly()、myQuanyue()方法  
 * </pre>
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class FinancingBaseAction extends BaseAction implements Preparable {

	@Resource
	PreFinancingBaseService preFinancingBaseService;
	@Resource
	FinancingBaseService financingBaseService;
	@Resource
	FinancingCostService financingCostService;
	@Resource
	BusinessTypeService businessTypeService;
	@Resource
	MemberBaseService memberBaseService;
	@Resource
	UserService userService;
	@Resource
	UploadFileService uploadFileService;
	@Resource
	InvestRecordService investRecordService;
	@Resource
	PaymentRecordService paymentRecordService;
	@Resource
	FinancingContractService financingContractService;
	@Resource
	MemberGuaranteeService memberGuaranteeService;
	@Resource
	ChargingStandardService chargingStandardService;
	@Resource GroupService groupService; 
	@Resource
	FinancingRestrainService financingRestrainService;
	@Resource
	LogsService logsService;
	@Resource
	DictionaryService dictionaryService;
	@Resource
	AccountDealService accountDealService;
	@Resource
	OpenCloseDealService openCloseDealService;
	@Resource
	OrgService orgService;
	
	@Resource
	ContractTemplateService contractTemplateService;
	
	@Resource
	private MemberAuditService memberAuditService;
	@Resource
	FinancingCacheService financingCacheService;
	
	private String contractTemplateId;
	private String id;
	private FinancingBase financingBase;
	private String keyWord = "";
	private String qkeyWord = "";
	private String containstr = "";
	private String queryCode = "";
	private String queryName="";
	private List<String> fileIds;
	private List<UploadFile> files;
	private List<ContractTemplate> cTemplates;
	private String conTemplateId;
	private String opeNote;
	private String showMessage;
	private String states;// 融资项目状态查询
	private Date startDate;// 融资项目开始投标日期查询
	private Date endDate;// 融资项目结束投标日期查询

	private String actionUrl = "list";

	private MemberGuarantee memberGuarantee = new MemberGuarantee();
	private List<BusinessType> businessTypes;

	private String user_type_flag;
	/**
	 * 投标记录
	 */
	private List<InvestRecord> investRecords;

	// 风险保障
	private List<CommonVo> fxbzStateList;

	// 融资项目选择3个月时，这三项金额为0(一次性还款，非按月还款)
	private double principal_allah_monthly = 0d;// 按月等额本息还款(本金_阿拉伯数字 )
	private double interest_allah_monthly = 0d;// 按月等额本息还款(利息_阿拉伯数字)
	private double repayment_amount_monthly_allah = 0d;// 每月应还金额(阿拉伯数字)

	private double interest_allah = 0d;// 利息总额(阿拉伯数字)
	private double principal_allah = 0d;// 本金总额

	private int loan_term = 0;// 贷款期限

	private List<ContractKeyData> contractList = new ArrayList<ContractKeyData>();
	private List<FinancingContract> contracts = new ArrayList<FinancingContract>();

	private FinancingCost financingCost;
	private MemberBase financier;
	
	private String feeFlag = "0";
	private String creditFlag = "0";
	
	private String autoinvest="0";
	/**
	 * 担保合同图片
	 * */
	private File image;
	private String imageFileName;
	private String imageContentType;
	private String dbhtFileId;
	
	
	private String groupStr="";
	
	private String qyType="00101";
	/**
	 * 融资项目信息，融资项目查询，两个页面，用来传指定的机构编码
	 */
	private String queryByOrgCode;
 

	// 贷款分类
	private List<CommonVo> dkTypes=new ArrayList<CommonVo>();
 
	//行业 
	private List<CommonVo> hyTypes=new ArrayList<CommonVo>();
	
	//企业类型(中型企业、小型企业、微型企业)
	private List<CommonVo> qyTypes=new ArrayList<CommonVo>();
	
	private String firstFlag="1";//第一次加载为0
	
	
	/**
	 * UI
	 * 
	 * @return
	 * @throws Exception
	 */

	public String ui() throws Exception {
		businessTypes = businessTypeService.getCommonListData("from BusinessType c where c.code = 'R'");
		dkTypes=dictionaryService.getListByUrlKey("002"); 
		 return "ui";
	}

	/**
	 * detail详细
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detail() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.user_type_flag = u.getTypeFlag();
		for (UploadFile file : files) {
			file.setFrontId(file.getId().replaceAll("\\.", ""));
		}
		return "detail";
	}

	/**
	 * 挂单页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String guadanUI() throws Exception {
		if(!"1.5".equals(this.financingBase.getState())){
			this.actionUrl = "today_for_order";
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "先发布信息，才能挂单!");
			return "guadanNotUI";
		}
		try {
			 List<FinancingRestrain> frs=financingRestrainService.getCommonListData("from FinancingRestrain o where o.financingCode='"+financingBase.getCode()+"'");
			 List<Group> selectGroups=new ArrayList<Group>();
			 for(FinancingRestrain re:frs){
				 selectGroups.add(new Group(re.getGroupId()));
			 } 
			 ServletActionContext.getRequest().setAttribute("selectGroups", selectGroups);//选中
			 
			 
		    List<Group> groups=groupService.getCommonListData("from Group o  where o.state!='0' "); 
		    ServletActionContext.getRequest().setAttribute("groups", groups);//分组信息	
		    
		   String codestr = this.financingBase.getCode().substring(0, 1);
 
		 
			if("day".equals(this.financingBase.getBusinessType().getId())){//指定默认的模板
				codestr = "D";
			}else{
				codestr = "M";
			}
		   cTemplates=this.contractTemplateService.getCommonListData("from ContractTemplate o  where 1=1 and o.code like '%"+codestr +"%' and not o.isOverDate='Y' " );  
		   
		   if(null!=this.financingBase.getContractTemplate()){
			   conTemplateId=this.financingBase.getContractTemplate().getId();
		   }
		   
			
			for (UploadFile file : files) {
				file.setFrontId(file.getId().replaceAll("\\.", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "guadanUI";
	}
	/**
	 * 发布信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String fabuUI() throws Exception {
		if("1.5".equals(this.financingBase.getState())){
			this.actionUrl = "today_for_order";
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "已经发布过信息!");
			return "guadanNotUI";
		}
		try {
			
			 List<FinancingRestrain> frs=financingRestrainService.getCommonListData("from FinancingRestrain o where o.financingCode='"+financingBase.getCode()+"'");
			 List<Group> selectGroups=new ArrayList<Group>();
			 for(FinancingRestrain re:frs){ 
				 selectGroups.add(this.groupService.selectById(re.getGroupId()));
			 } 
			 ServletActionContext.getRequest().setAttribute("selectGroups", selectGroups);//选中
			  
			for (UploadFile file : files) {
				file.setFrontId(file.getId().replaceAll("\\.", ""));
			}
			String codestr = this.financingBase.getCode().substring(0, 1);		
			
			
			if("day".equals(this.financingBase.getBusinessType().getId())){//指定默认的模板
				codestr = "D";
			}else{
				codestr = "M";
			}
		
			cTemplates=this.contractTemplateService.getCommonListData("from ContractTemplate o  where 1=1 and o.code like '%"+codestr +"%' and not o.isOverDate='Y' " );  
			   
		    conTemplateId=this.financingBase.getContractTemplate().getId();
			   
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fabuUI";
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String fabu() throws Exception {
		try {
			if(null!=this.financingBase&&"1".equals(this.financingBase.getState())){
				this.financingBase.setState("1.5"); 
				this.financingBase.setAuditDate(new Date());
				
				//加入委托投标日志
				this.financingBase.setAutoinvest(autoinvest);
				if("1".equals(autoinvest)){
					Logs log0 = logsService.log("发布融资信息时,开启委托投标");// 日志
					//this.financingBase.getLogs().add(log);// 日志
					log0.setEntityId(this.financingBase.getId());
					log0.setEntityFrom("FinancingBase");
					this.logsService.insert(log0);
				} 
				
				Logs log = logsService.log("发布融资信息成功");// 日志
				//this.financingBase.getLogs().add(log);// 日志
				log.setEntityId(this.financingBase.getId());
				log.setEntityFrom("FinancingBase");
				this.logsService.insert(log);
				this.financingBase.setModifyDate(new Date()); 
				 
				if (null != this.conTemplateId && !"".equals(conTemplateId.trim())) {
					financingBase.setContractTemplate(contractTemplateService.selectById(conTemplateId));
				}  
				
				this.financingBaseService.update(this.financingBase);
				
				if("0".equals(this.financingBase.getAutoinvest())){//如果是委托投标，不更新缓存 
					//更新未结束的融资项目缓存  
					try { ReadsStaticConstantPropertiesUtil.updateServiceCache(this.financingBase.getId()); } catch (Exception e) {}
				}
				
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "发布融资项目信息成功"); 
			}else{
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "发布融资项目信息失败");  
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "发布融资项目信息失败");
		}
		return "fabu";
	}

	/**
	 * 审核驳回
	 * 
	 * @return
	 * @throws Exception
	 */
	public String fabuBohui() throws Exception {
		try { 
			if(null!=this.financingBase&&"1".equals(this.financingBase.getState())){
			   this.financingBaseService.fabuBohui(id);
			   ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "驳回到待审核状态成功");
			}else{
			   ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "驳回失败");  
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "驳回失败");
		}
		return "fabuBohui";
	}

	public String qyTypesAj() throws Exception {
		String res=""; 
		try {     
			if(qyType.contains("(")){
				qyType=qyType.substring(qyType.indexOf("(")+1, qyType.length()-1);
			}
			this.qyTypes=dictionaryService.getListByUrlKey2(qyType); 
			
			for(int i=0;i<this.qyTypes.size();i++){  
				res+="{\"string1\":\""+this.qyTypes.get(i).getString1()
				+"\",\"string2\":\""+this.qyTypes.get(i).getString2()
				+"\",\"string3\":\""+this.qyTypes.get(i).getString3()
				+"\",\"string4\":\""+this.qyTypes.get(i).getString4()
				+"\",\"string5\":\""+this.qyTypes.get(i).getString5()+"\"},";
			}    
			if(!"".equals(res)){
				res=res.substring(0, res.length()-1);
			}
			res="{\"code\":\"0\",\"tip\":\"成功加载\",\"list\":["+res+"]}"; 
		} catch (final Exception e) {  
			res="{\"code\":\"1\",\"tip\":\"加载列表失败\",\"list\":[]}";
		}  
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8"); 
		ServletActionContext.getResponse().setHeader("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate");    
		ServletActionContext.getResponse().addHeader("Cache-Control", "post-check=0, pre-check=0");   
		ServletActionContext.getResponse().setHeader("Pragma", "no-cache"); 
		ServletActionContext.getResponse().getWriter().write(res);
		ServletActionContext.getResponse().getWriter().flush();
		ServletActionContext.getResponse().getWriter().close();
		return null;
	}
	
	
	/**
	 * 审核页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkUI() throws Exception {
		try {
			for (UploadFile file : files) {
				file.setFrontId(file.getId().replaceAll("\\.", ""));
			} 
			hyTypes=dictionaryService.getListByUrlKey("001");	 	
			if (null != this.financingBase.getHyType() && !"".equals(this.financingBase.getHyType())) {   
				String qyTypeTemp=this.financingBase.getHyType().substring(this.financingBase.getHyType().indexOf("(")+1, this.financingBase.getHyType().length()-1);
		    	//this.qyTypes=DictionaryUtils.getListByUrlKey2(BaseTool.getProjectUrl(ServletActionContext.getRequest())+ "sys_/dictionary/dic_001_2.jsp","list"+qyTypeTemp);  	
				this.qyTypes=dictionaryService.getListByUrlKey2(qyTypeTemp);  	
			}else{ 
				if(null!=hyTypes&&hyTypes.size()>0){
			    	this.qyTypes=dictionaryService.getListByUrlKey2(hyTypes.get(0).getString1());  	
			    }	
			}  
			 List<FinancingRestrain> frs=financingRestrainService.getCommonListData("from FinancingRestrain o where o.financingCode='"+financingBase.getCode()+"'");
			 List<Group> selectGroups=new ArrayList<Group>();
			 for(FinancingRestrain re:frs){
				 selectGroups.add(new Group(re.getGroupId()));
			 } 
			 ServletActionContext.getRequest().setAttribute("selectGroups", selectGroups);//选中
			 
			List<Group> groups=groupService.getCommonListData("from Group o where o.state!='0' "); 
			ServletActionContext.getRequest().setAttribute("groups", groups);//分组信息	
			
		} catch (Exception e) {   
			e.printStackTrace();
		}
		return "checkUI";
	}
	
 
	public String delayList() throws Exception { 
		SimpleDateFormat startDateFormat = new SimpleDateFormat( "yyyy-MM-dd 00:00:00");
        SimpleDateFormat endDateFormat = new SimpleDateFormat( "yyyy-MM-dd 23:59:59"); 
        StringBuilder sb = new StringBuilder("from MemberBase where 1=1 "); 
        if (null != startDate && null != endDate) { 
			sb.append(" and (createDate >= to_date('"
					+ startDateFormat.format(startDate)
					+ "','yyyy-mm-dd hh24:mi:ss') and createDate <= to_date('"
					+ endDateFormat.format(endDate)
					+ "','yyyy-mm-dd hh24:mi:ss'))");
		} else if (null != startDate && null == endDate) {
			sb.append(" and (createDate >= to_date('"
					+ startDateFormat.format(startDate)
					+ "','yyyy-mm-dd hh24:mi:ss') and createDate <= to_date('"
					+ endDateFormat.format(new Date())
					+ "','yyyy-mm-dd hh24:mi:ss'))");
		} else if (null == startDate && null != endDate) {
			sb.append(" and createDate <= to_date('"
					+ endDateFormat.format(endDate)
					+ "','yyyy-mm-dd hh24:mi:ss')");
		} 
		sb.append(" order by createdate desc");
		List<MemberBase>  ms=this.memberBaseService.getCommonListData(sb.toString());  
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),ms.toString()); 
		return null;      
 }
	
	
	/**
	 * 审核通过
	 * 
	 * @return
	 * @throws Exception
	 */
	public String check() throws Exception {
		try { 
			if(null!=this.financingBase&&"0".equals(this.financingBase.getState())){
				this.financingBase.setState("1");
				User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				this.financingBase.setAuditBy(u);
				this.financingBase.setAuditDate(new Date());
				//加入委托投标日志
				this.financingBase.setAutoinvest(autoinvest);
				if("1".equals(autoinvest)){
					Logs log0 = logsService.log("审核时,开启委托投标");// 日志
					//this.financingBase.getLogs().add(log);// 日志
					log0.setEntityId(this.financingBase.getId());
					log0.setEntityFrom("FinancingBase");
					this.logsService.insert(log0);
				} 
				
				
				Logs log = logsService.log("审核");// 日志
				//this.financingBase.getLogs().add(log);// 日志
				log.setEntityId(this.financingBase.getId());
				log.setEntityFrom("FinancingBase");
				this.logsService.insert(log);
				 List<FinancingRestrain> frs=financingRestrainService.getCommonListData("from FinancingRestrain o where o.financingCode='"+financingBase.getCode()+"'");
				 for(FinancingRestrain re:frs){
					 financingRestrainService.delete(re.getId());     
				 } 
				if (null != groupIds && !"".equals(groupIds.trim())) {
					groupIds = groupIds.trim();  
					 //重新加入
					 for (StringTokenizer tokenizer = new StringTokenizer(groupIds, ","); tokenizer.hasMoreTokens();) {
						 String groupId = tokenizer.nextToken();
	                     this.financingRestrainService.insert(new FinancingRestrain(groupId, financingBase.getCode()));
					 } 
					 this.financingBase.setPreInvest(1);
				}
				 
				this.financingBaseService.update(this.financingBase);
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "审核成功,等待发布");
			}else{
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "审核失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "审核失败");
		}
		return "check";
	}

	/**
	 * 审核驳回
	 * 
	 * @return
	 * @throws Exception
	 */
	public String noCheck() throws Exception {
		try {
			// 历史记录修改状态为已经驳回
			// PreFinancingBase
			// tempPreFinancingBase=this.preFinancingBaseService.selectByHql("
			// from PreFinancingBase o where o.state=2 and
			// o.code='"+this.financingBase.getCode()+"'");
			// tempPreFinancingBase.setState(4);
			// tempPreFinancingBase.setOpeNote(tempPreFinancingBase.getOpeNote()+opeNote);
			// this.preFinancingBaseService.update(tempPreFinancingBase);
			// 删除待审核的记录
			// this.delete(id);
			if(null!=this.financingBase&&"0".equals(this.financingBase.getState())){
			   this.financingBaseService.noCheck(id, this.financingBase.getCode(), opeNote);
			   ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "驳回成功");
			}else{
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "驳回失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "驳回失败");
		}
		return "noCheck";
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String guadan() throws Exception {
		try {
			if(null!=this.financingBase&&"1.5".equals(this.financingBase.getState())){
				this.financingBase.setState("2");
				this.financingBase.setAuditDate(new Date());
				Logs log = logsService.log("挂单通过");// 日志
				//this.financingBase.getLogs().add(log);// 日志
				log.setEntityId(this.financingBase.getId());
				log.setEntityFrom("FinancingBase");
				this.logsService.insert(log);
				this.financingBase.setModifyDate(new Date());
				
				if (null != this.contractTemplateId && !"".equals(contractTemplateId.trim())) {
					this.financingBase.setContractTemplate(this.contractTemplateService.selectById(contractTemplateId));
				}
				
				
				 List<FinancingRestrain> frs=financingRestrainService.getCommonListData("from FinancingRestrain o where o.financingCode='"+financingBase.getCode()+"'");
				 for(FinancingRestrain re:frs){
					 financingRestrainService.delete(re.getId());     
				 } 
				 
				if (null != groupIds && !"".equals(groupIds.trim())) {
					groupIds = groupIds.trim();  
					 //重新加入
					 for (StringTokenizer tokenizer = new StringTokenizer(groupIds, ","); tokenizer.hasMoreTokens();) {
						 String groupId = tokenizer.nextToken();
	                     this.financingRestrainService.insert(new FinancingRestrain(groupId, financingBase.getCode()));
					 } 
				}else{
					this.financingBase.setPreInvest(0);
				}
				financingBase.setModifyDate(new Date());
				if (null != groupIds && !"".equals(groupIds.trim())) {
					groupIds = groupIds.trim();  
					 //重新加入
					 for (StringTokenizer tokenizer = new StringTokenizer(groupIds, ","); tokenizer.hasMoreTokens();) {
						 String groupId = tokenizer.nextToken();
	                     this.financingRestrainService.insert(new FinancingRestrain(groupId, financingBase.getCode()));
					 } 
				}
				if (null != conTemplateId && !"".equals(conTemplateId.trim())) {
			    	financingBase.setContractTemplate(contractTemplateService.selectById(conTemplateId));
				}
				this.financingBaseService.update(this.financingBase);
				 
				//更新融资项目缓存  
				if("0".equals(this.financingBase.getAutoinvest())){ //如果是委托投标，不更新缓存 
				 try { ReadsStaticConstantPropertiesUtil.updateServiceCache(this.financingBase.getId()); } catch (Exception e) {}
				}
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "挂单成功");

				// 项目发布投資人短信
				try {
					VelocityContext context = new VelocityContext();
					SimpleDateFormat format = new SimpleDateFormat("yy年MM月dd日");
					context.put("createDate", format.format(new Date())); // xx年x月x日

					/*
					 * String content = VelocityUtils.getVelocityString(context,
					 * "tzr_fabu.html"); format = new
					 * SimpleDateFormat("yyyyMMddHHmmss"); HashMap<String, String>
					 * params = new HashMap<String, String>();
					 * params.put("content", content.trim()); List<MemberBase>
					 * members = this.memberBaseService.getScrollData( "
					 * o.memberType.code='T' and o.state = '2' ") .getResultlist();
					 * for (MemberBase m : members) { if (null != m.getMobile() &&
					 * !"".equals(m.getMobile())) { //
					 * System.out.println("PHO-----"+m.getMobile());
					 * params.put("mobile", m.getMobile()); //
					 * System.out.println("url="+BaseTool.getProjectUrl(ServletActionContext.getRequest())+ //
					 * "sms/SmsService");
					 * SendUrlRequestUtils.send_Url("http://localhost:8001/sms/SmsService",
					 * "POST", params); } }
					 */
					// 项目发布融資人短信
					String content = VelocityUtils.getVelocityString(context, "rzr_fabu.html");
					SimpleDateFormat formats = new SimpleDateFormat("yyyyMMddHHmmss");
					SMSNewUtil.sms(this.financingBase.getFinancier().getMobile(), content.trim(), formats.format(new Date()), "","1");
					//老的短信接口不再使用2014-06-30
					//SMSUtil.sms(this.financingBase.getFinancier().getMobile(), content.trim());
				} catch (Exception e) {
					e.printStackTrace();
				}

				// params.put("mobile",
				// this.financingBase.getFinancier().getMobile());
				// params.put("content", content.trim());
				// SendUrlRequestUtils.send_Url("http://localhost:8001/sms/SmsService",
				// "POST", params);
				/*
				 * SendUrlRequestUtils.send_Url(BaseTool
				 * .getProjectUrl(ServletActionContext.getRequest()) +
				 * "sms/SmsService", "POST", params);
				 */
			}else{
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "挂单失败,不要重复挂单");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "挂单失败");
		}
		return "guadan";
	}
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 费用核算移至“融资项目确认”步骤
	 * 
	 * 
	 * 
	 * @return
	 * @throws Exception
	 */
	// 融资确认并核算
	public String hesuanUI() throws Exception {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			this.financingCost = this.financingCostService.getCostByFinancingBase(id);
			if(this.financingCost == null){
				this.financingCost = financingCostService.insertFinancingCost(id);
			}
			//ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资确认成功");
			
			
			this.financier = financingCost.getFinancier();
            /**年费开始**/
			if (null == this.financier.getYearFeeStartDate()) {// 针对历史数据赋值
				MemberAudit memberA = memberAuditService.selectByHql(" from MemberAudit where memberBase.id = '" + financier.getId() + "' order by additDate desc");
				if (null != memberA.getAdditDate()) {
					this.financier.setYearFeeStartDate(memberA.getAdditDate());
					Calendar c = Calendar.getInstance();
					c.setTime(this.financier.getYearFeeStartDate());
					c.add(c.YEAR, 1);
					this.financier.setYearFeeEndDate(c.getTime());
					memberBaseService.update(this.financier);
				}
			}

			if (null != this.financier.getYearFeeDate()) { 
				if (this.financier.getYearFeeDate().getTime()<=new Date().getTime()&&new Date().getTime()<=this.financier.getYearFeeStartDate().getTime()) { 
					feeFlag = "1"; // 已经缴费
				} else {
					CostItem fee7Item = chargingStandardService.findCostItem("xwf", "R");
					if (null != fee7Item) {
						financingCost.setFee7(fee7Item.getMoney());
					}
				}
			} else {
				CostItem fee7Item = chargingStandardService.findCostItem("xwf", "R");
				if (null != fee7Item) {
					financingCost.setFee7(fee7Item.getMoney());
				}
			}
			/**年费结束**/
			
            /**信用管理费开始**/
			if (null == this.financier.getCreditFeeStartDate()) {// 针对历史数据赋值
				MemberAudit memberA = memberAuditService.selectByHql(" from MemberAudit where memberBase.id = '" + this.financier.getId() + "' order by additDate desc");
				if (null != memberA.getAdditDate()) {
					this.financier.setCreditFeeStartDate(memberA.getAdditDate());
					Calendar c = Calendar.getInstance();
					c.setTime(this.financier.getCreditFeeStartDate());
					c.add(c.YEAR, 1);
					this.financier.setCreditFeeEndDate(c.getTime());
					memberBaseService.update(this.financier);
				}
			}

			if (null != this.financier.getCreditFeeDate()) { 
				//SimpleDateFormat format = new SimpleDateFormat("yyyy");
				//if (format.format(new Date()).equals(format.format(financier.getCreditFeeDate()))) {
				if (this.financier.getCreditFeeDate().getTime()<=new Date().getTime()&&new Date().getTime()<=this.financier.getCreditFeeStartDate().getTime()) {  
					creditFlag = "1"; // 已经缴费
				} else {
					CostItem fee10Item = chargingStandardService.findCostItem("xyglf", "R");
					if (null != fee10Item) {
						financingCost.setFee10(fee10Item.getMoney());
					}
				}
			} else {
				CostItem fee10Item = chargingStandardService.findCostItem("xyglf", "R");
				if (null != fee10Item) {
					financingCost.setFee10(fee10Item.getMoney());
				}
			}
			/**信用管理费结束**/  
			
				
				

		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资确认失败");
		}
		return "hesuanUI";
	}

	// 融资项目签约UI
	public String qianyueUI() throws Exception {
		try {

			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			/**
			 * 如果融资方与当前用户为同一人，则显示签约按钮
			 */
			if (this.financingBase.getFinancier().getUser().getId() == u.getId()) {
				this.creditor = true;
			} else {
				this.creditor = false;
			}

			showMessage = "";
			if (3 == this.financingBase.getBusinessType().getTerm()) {
				if (null != this.financingBase.getFinishtime()) {
					int month = DateUtils.getMouth(this.financingBase.getFinishtime());
					showMessage = "还款分" + this.financingBase.getBusinessType().getReturnTimes() + "期,还款日期是：" + (month + 3) + "月";
				}
			} else {
				showMessage = "还款分" + this.financingBase.getBusinessType().getReturnTimes() + "期,还款日期是：每月";
			}
			investRecords = this.investRecordService.getScrollDataCommon("from InvestRecord where financingBase.id = ?", new String[] { this.financingBase.getId() });
			this.contractList = this.financingContractService.getContractList(this.financingBase.getId());
			if (null != contractList && contractList.size() > 0) {
				for (ContractKeyData c : contractList) {
					this.loan_term = c.getLoan_term();
					this.interest_allah += c.getInterest_allah();
					this.principal_allah += c.getLoan_allah();
					if (this.financingBase.getBusinessType().getReturnTimes() > 1) {
						this.principal_allah_monthly += c.getPrincipal_allah_monthly();
						this.interest_allah_monthly += c.getInterest_allah_monthly();
						this.repayment_amount_monthly_allah += c.getRepayment_amount_monthly_allah();
					}
				}
				// System.out.println("利息总额："+this.interest_allah);
				// System.out.println("本金总额："+this.principal_allah);
				// System.out.println("按月等额本息还款(本金)："+this.principal_allah_monthly);
				// System.out.println("按月等额本息还款(利息)："+this.interest_allah_monthly);
				// System.out.println("每月应还金额："+this.repayment_amount_monthly_allah);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "qianyueUI";
	}

	// 融资项目签约(生成还款记录)
	public String qianyue() throws Exception {
		try {

			this.financingBaseService.sign(id);
			
			//更新未结束的融资项目缓存  
			try { ReadsStaticConstantPropertiesUtil.updateServiceCache(id); } catch (Exception e) {}
			
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资签约成功");
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资签约失败");
		}
		return "qianyue";
	}

	/**
	 * 我的融资签约UI
	 * 
	 * @return String
	 * @exception
	 */

	private boolean creditor;

	public String myQianyueUI() throws Exception {

		try {
			StringBuilder message_temp = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			/**
			 * 如果融资方与当前用户为同一人，则显示签约按钮
			 */
			if (this.financingBase.getFinancier().getUser().getId() == u.getId()) {
				this.creditor = true;
			} else {
				this.creditor = false;
			}

			message_temp.append("还款分").append(this.financingBase.getBusinessType().getReturnTimes()).append("期,还款日期是：");
			
			if (1 == this.financingBase.getBusinessType().getBranch()) {
				if(this.financingBase.getInterestDay() != 0 ){
					cal.add(Calendar.DAY_OF_MONTH, this.financingBase.getInterestDay());
				}else{
					cal.add(Calendar.DAY_OF_MONTH, 1);
					cal.add(Calendar.MONTH, this.financingBase.getBusinessType().getTerm());
				}
				message_temp.append(sdf.format(cal.getTime()));
			} else {
				cal.add(Calendar.DAY_OF_MONTH, 1);
				now = cal.getTime();
				for(int x = 1; x <= this.financingBase.getBusinessType().getTerm(); x++){
					cal.add(Calendar.MONTH, x); 
					if(x != 1){
						message_temp.append("、");
					}
					message_temp.append(sdf.format(cal.getTime()));
					cal.setTime(now);
				}
			}
			showMessage = message_temp.toString();
			investRecords = this.investRecordService.getScrollDataCommon("from InvestRecord where financingBase.id = ?", new String[] { this.financingBase.getId() });
			this.contractList = this.financingContractService.getContractList(this.financingBase.getId());
			if (null != contractList && contractList.size() > 0) {
				for (ContractKeyData c : contractList) {
					this.loan_term = c.getLoan_term();
					this.interest_allah += c.getInterest_allah();
					this.principal_allah += c.getLoan_allah();
					if (this.financingBase.getBusinessType().getReturnTimes() > 1) {
						this.principal_allah_monthly += c.getPrincipal_allah_monthly();
						this.interest_allah_monthly += c.getInterest_allah_monthly();
						this.repayment_amount_monthly_allah += c.getRepayment_amount_monthly_allah();
					}
				}
				// System.out.println("利息总额："+this.interest_allah);
				// System.out.println("本金总额："+this.principal_allah);
				// System.out.println("按月等额本息还款(本金)："+this.principal_allah_monthly);
				// System.out.println("按月等额本息还款(利息)："+this.interest_allah_monthly);
				// System.out.println("每月应还金额："+this.repayment_amount_monthly_allah);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "myQianyueUI";
	}

	public String financingContract() throws Exception {
		investRecords = this.investRecordService.getScrollDataCommon("from InvestRecord where financingBase.id = ?", new String[] { this.financingBase.getId() });
		this.contractList = this.financingContractService.getContractList(this.financingBase.getId());
		if (null != contractList && contractList.size() > 0) {
			for (ContractKeyData c : contractList) {
				this.loan_term = c.getLoan_term();
				this.interest_allah += c.getInterest_allah();
				this.principal_allah += c.getLoan_allah();
				if (this.financingBase.getBusinessType().getReturnTimes() > 1) {
					this.principal_allah_monthly += c.getPrincipal_allah_monthly();
					this.interest_allah_monthly += c.getInterest_allah_monthly();
					this.repayment_amount_monthly_allah += c.getRepayment_amount_monthly_allah();
				}
			}
			// System.out.println("利息总额："+this.interest_allah);
			// System.out.println("本金总额："+this.principal_allah);
			// System.out.println("按月等额本息还款(本金)："+this.principal_allah_monthly);
			// System.out.println("按月等额本息还款(利息)："+this.interest_allah_monthly);
			// System.out.println("每月应还金额："+this.repayment_amount_monthly_allah);
		}
		return "financingContract";
	}

	public String myContracts() throws Exception {
		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MemberBase mb = this.memberBaseService.selectById(" from MemberBase where user.id = ?", new Object[] { u.getId() });
		this.contracts = this.financingContractService.listByFinancinger(mb.getId());
		return "myContracts";
	}

	// 我的融资签约(生成还款记录)
	public String myQianyue() throws Exception {
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5 ||state==6) {
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "交易市场未开市");
		} else if (state == 2) {
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
		} else {
			try {
				this.financingBaseService.sign(id);
				
				//更新未结束的融资项目缓存  
				try { ReadsStaticConstantPropertiesUtil.updateServiceCache(id); } catch (Exception e) {}
				
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资签约成功");
				
			} catch (Exception e) {
				e.printStackTrace();
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资签约失败");
			}
		}
		return "myQianyue";
	}

	/**
	 * 融资单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String receipts() throws Exception {
		try {
			investRecords = this.investRecordService.getScrollDataCommon("from InvestRecord where financingBase.id = ?", new String[] { this.financingBase.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "receipts";
	}

	/**
	 * 融资方合同
	 * 
	 * @return
	 * @throws Exception
	 */
	public String agreement_ui() throws Exception {
		try {
			investRecords = this.investRecordService.getScrollDataCommon("from InvestRecord where financingBase.id = ?", new String[] { this.financingBase.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "agreement_ui";
	}

	/**
	 * 签定合同
	 * 
	 * @return
	 * @throws Exception
	 */
	public String agreement() throws Exception {
		if (this.id != null && !"".equals(this.id)) {
			investRecords = this.investRecordService.getScrollDataCommon("from InvestRecord where financingBase.id = ?", new String[] { this.financingBase.getId() });
		}
		for (InvestRecord ir : this.investRecords) {
			ir.getContract().setFinancier_make_sure(new Date());
			this.investRecordService.update(ir);
		}
		return null;
	}

	/**
	 * 保存
	 * 
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		try {
			if (this.id == null || "".equals(this.id)) {
				User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				this.financingBase.setCreateBy(u);
				this.financingBase.setCreateDate(new Date());
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				this.financingBase.setCode(format.format(new Date()));
				if (null == this.financingBase.getStartDate()) {
					this.financingBase.setStartDate(new Date());
				}
				if (null == this.financingBase.getEndDate()) {
					this.financingBase.setEndDate(new Date());
				}
				if ("".equals(this.financingBase.getGuarantee().getId())) this.financingBase.setGuarantee(null);
				if ("".equals(this.financingBase.getFinancier().getId())) {
					this.financingBase.setFinancier(null);
					this.financingBase.setMemberGuarantee(null);
				}
				if (null != this.financingBase.getGuarantee()) {
					this.financingBase.setMemberGuarantee(memberGuaranteeService.getLastByMemberGuarantee(this.financingBase.getGuarantee().getId()));
				}

				this.financingBaseService.insert(this.financingBase);

				if (fileIds != null && fileIds.size() > 0) {
					for (String fileId : fileIds) {
						UploadFile uploadFile = uploadFileService.selectById(fileId);
						uploadFile.setEntityFrom("FinancingBase");
						uploadFile.setEntityId(this.financingBase.getId());
						uploadFile.setUseFlag("1");// 使用中
						uploadFileService.update(uploadFile);
					}
				}
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "新增成功");

			} else {
				
				if("2".equals(this.financingBase.getState())){
					ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "修改操作失败,信用已经确认!");
					return "edit"; 
				}
				 
				if("5".equals(this.financingBase.getState())){
					ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "修改操作失败,融资项目已经废弃!");
					return "edit"; 
				}  
				
				
				this.financingBase.setState("0");
				this.financingBase.setAuditBy(null);
				this.financingBase.setHaveInvestNum(0);
				this.financingBase.setCurrenyAmount(0d);// 当前融资额(已融资额) 初始为0
				if (null != this.financingBase.getGuarantee()) {
					this.financingBase.setMemberGuarantee(memberGuaranteeService.getLastByMemberGuarantee(this.financingBase.getGuarantee().getId()));
				}

				Logs log = this.logsService.log("修改融资项目");// 日志
				//this.financingBase.getLogs().add(log);// 日志
				log.setEntityId(this.financingBase.getId());
				log.setEntityFrom("FinancingBase");
				this.logsService.insert(log);
				
				this.financingBaseService.update(this.financingBase);
				if (fileIds != null && fileIds.size() > 0) {
					for (String fileTempId : fileIds) {
						UploadFile uploadFile = uploadFileService.selectById(fileTempId);
						uploadFile.setEntityFrom("FinancingBase");
						uploadFile.setEntityId(this.financingBase.getId());
						uploadFile.setUseFlag("1");// 使用中
						uploadFileService.update(uploadFile);
					}
				}
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "重发成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "操作失败");
		}
		return "edit";
	}

	/**
	 * 融资项目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() {
		return "list";
	}
	/**
	 * 导出数据
	 * @return
	 * @throws Exception
	 */
	public String EmpList() throws Exception{
		SimpleDateFormat format22 = new SimpleDateFormat("yyyyMMddHHmmss");
		ServletActionContext.getRequest().setAttribute("msg", format22.format(new Date()));
		
		HttpServletRequest request = ServletActionContext.getRequest();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

		StringBuilder subsql = new StringBuilder(" 1 = 1");
		
		List<Object> args = new LinkedList<Object>();

		User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Org org = u.getOrg();
		if (null != org && StringUtils.isNotBlank(this.queryByOrgCode)) {
			if (("5301".equals(org.getShowCoding()) || "530100".equals(org.getShowCoding())) && StringUtils.isNotBlank(this.queryByOrgCode)) {
				subsql.append(" and createorg = ? ");
				args.add(this.queryByOrgCode);
			} else {
				subsql.append(" and createorg = ? ");
				args.add(org.getShowCoding());
			}
		}

		if (null == this.startDate ) {
			this.startDate = this.endDate = new Date();
		}
		if(null == this.endDate){
			this.endDate = new Date();
		}
		subsql.append(" and to_char(startdate,'yyyymmdd') between ? and ? ");
		args.add(format.format(this.getStartDate()));
		args.add(format.format(this.getEndDate()));
		
		
		
		if (null != this.states && !"".equals(this.states)) {
			if (this.states.equals("5")) {
				subsql.append(" and state in (?,?) ");
				args.add("5");
				args.add("6");
			} else {
				subsql.append(" and state = ?");
				args.add(this.states);
			}
		}

		if (null != qkeyWord && !"".equals(qkeyWord.trim())) {
			qkeyWord = qkeyWord.trim();
			subsql.append(" and (");
			subsql.append("     financerusername like ?");
			args.add("%" + qkeyWord + "%");
			
			
			subsql.append("     or ");
			subsql.append("     financerrealname like ?");
			args.add("%" + qkeyWord + "%");
			
			
			subsql.append("     or ");
			subsql.append("     financecode like ?");
			args.add("%" + qkeyWord + "%");
			
			subsql.append("     or ");
			subsql.append("     financename like ?");
			args.add("%" + qkeyWord + "%");
			subsql.append(" )");
		}

		List<Map<String, Object>> result = this.financingBaseService.queryForList("*","v_finance",subsql.toString(),args.toArray());
		long[] totalData = financingBaseService.queryForExpiredListCount(subsql.toString(),args.toArray());
		
		request.setAttribute("list", result);
		request.setAttribute("totalData", totalData);
		
		return "list_excel";
	}

	
	public void getAllFinancingBaseList(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

			StringBuilder subsql = new StringBuilder(" state != '0' ");

			ArrayList<Object> args_list = new ArrayList<Object>();
			
			if(StringUtils.isBlank(this.states) && StringUtils.isBlank(this.qkeyWord) && StringUtils.isBlank(this.queryByOrgCode) ){
				if (null == this.startDate ) {
					this.startDate = this.endDate = new Date();
				}
				if(null == this.endDate){
					this.endDate = new Date();
				}
			}
			
			if( null != this.startDate && null != this.endDate){
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getEndDate());
				cal.add(Calendar.DAY_OF_MONTH, 1);
				subsql.append(" and to_char(startdate,'yyyymmdd') between ? and ? ");
				args_list.add(format.format(this.getStartDate()));
				args_list.add(format.format(cal.getTime()));
			}
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = u.getOrg();
			if (("5301".equals(org.getShowCoding()) || "530100".equals(org.getShowCoding()))) {
				if(StringUtils.isNotBlank(this.queryByOrgCode)){
					subsql.append(" and createorg = ? ");
					args_list.add(this.queryByOrgCode);
				}
			} else {
				subsql.append(" and createorg = ? ");
				args_list.add(org.getShowCoding());
			}

			if (StringUtils.isNotBlank(this.states)) {
				if (this.states.equals("5")) {
					subsql.append(" and state in (?,?)");
					args_list.add("5");
					args_list.add("6");
				} else {
					subsql.append(" and state = ?");
					args_list.add(this.states);
				}
			}

			if (StringUtils.isNotBlank(this.qkeyWord)) {
				qkeyWord = qkeyWord.trim();
				subsql.append(" and (");
				subsql.append("     financerusername like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financerrealname like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financecode like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financename like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append(" )");
			}

			
			Object [] args = args_list.toArray();
			
			List<Map<String, Object>> result = this.financingBaseService.queryForList("*","v_finance",subsql.toString(),args, getPage(), rows);
			int total = this.financingBaseService.queryForListTotal("id","v_finance",subsql.toString(),args);

			for (Map<String, Object> obj : result) {
				if (null != obj.get("enddate")) {
					if (((Date) obj.get("enddate")).compareTo(new Date()) <= 0){// 融资到期
						if ((!"5".equals(obj.get("state"))) && (!"6".equals(obj.get("state"))) && (!"7".equals(obj.get("state")))){// 5融资确认已完成
						// 6费用核算完成 7签约完成
							obj.put("showok", true);
						}
					} else {
						if ("4".equals(obj.get("state"))){// 已满标或者融资到期可以融资确认
							obj.put("showok", true);
						}
					}
				}

			}
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			long[] totalData = financingBaseService.queryForExpiredListCount(subsql.toString(),args);
			JSONArray footer = new JSONArray();
			JSONObject _totalDate = new JSONObject();
			_totalDate.element("MAXAMOUNT", totalData[0]);
			_totalDate.element("CURCANINVEST", totalData[1]);
			_totalDate.element("CURRENYAMOUNT", totalData[2]);
			_totalDate.element("CREATEORG_SHORT", "合计");
			footer.add(_totalDate);
			o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	/**
	 * 融资项目查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listQuery() throws Exception {
		return "list_for_search_json";
	}
  
    public static String stringAnalytical(String str, String divisionChar,String like) {   
        String strs[];   
        int i = 0;  
        if(null==str) return null;
        StringTokenizer tokenizer = new StringTokenizer(str, divisionChar);   
        strs = new String[tokenizer.countTokens()];// 动态的决定数组的长度   
         while (tokenizer.hasMoreTokens()) {   
        	 strs[i] = tokenizer.nextToken();   
             i++;   
        }   
        if(strs.length==1){
        	return " and  o.code "+like+" '%" +strs[0]+ "%'";
        }else{
        	String temps="";
        	if("like".equals(like)){
            	for(int j=0;j<strs.length;j++){
            		if(j==0){
            			temps+="and ( o.code "+like+" '%" +strs[j]+ "%'  ";
            		}else if(j==strs.length-1){
            			temps+="or  o.code "+like+" '%" +strs[j]+ "%' )";
            		}else{
            			temps+="or  o.code "+like+" '%" +strs[j]+ "%'  ";
            		}
            	}     		
        	}else{
            	for(int j=0;j<strs.length;j++){
            		 temps+="and o.code "+like+" '%" +strs[j]+ "%'  "; 
            	}         		
        	} 
        	return temps;
        }
       
         
    } 
    
    public static String stringAnalytical2(String str, String divisionChar,String like) {   
        String strs[];   
        int i = 0;  
        if(null==str) return null;
        StringTokenizer tokenizer = new StringTokenizer(str, divisionChar);   
        strs = new String[tokenizer.countTokens()];// 动态的决定数组的长度   
         while (tokenizer.hasMoreTokens()) {   
        	 strs[i] = tokenizer.nextToken();   
             i++;   
        }   
        if(strs.length==1){
        	return " and  code "+like+" '%" +strs[0]+ "%'";
        }else{
        	String temps="";
        	if("like".equals(like)){
            	for(int j=0;j<strs.length;j++){
            		if(j==0){
            			temps+="and ( code "+like+" '%" +strs[j]+ "%'  ";
            		}else if(j==strs.length-1){
            			temps+="or  code "+like+" '%" +strs[j]+ "%' )";
            		}else{
            			temps+="or  code "+like+" '%" +strs[j]+ "%'  ";
            		}
            	}     		
        	}else{
            	for(int j=0;j<strs.length;j++){
            		 temps+="and code "+like+" '%" +strs[j]+ "%'  "; 
            	}         		
        	} 
        	return temps;
        }
    } 
    
	/**
	 * 融资项目查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listRzQuery() throws Exception {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			this.checkDate();
			Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("createDate", "desc");
			StringBuilder sb = new StringBuilder(" 1=1    ");

			sb.append(" and o.state in ('4','5','6','7')");
			sb.append(" and o.createDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(endDateNext) + "','yyyy-MM-dd')");

			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  o.shortName like ?");
				params.add("%" + keyWord + "%");
			}
			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				sb.append(" and  o.code like '%" + queryCode + "'");
			}
			pageView.setQueryResult(financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "listRzQuery";
	}

	/**
	 * 融资项目签约
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listQianyue() throws Exception {
		try {
			if("0".equals(this.firstFlag)){
				return "listQianyue";
			}
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("createDate", "desc");
			StringBuilder sb = new StringBuilder(" 1=1 ");
			sb.append(" and o.state in ('6','7')");
			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			if (null != u) org = u.getOrg();
			if (null != org) {
				orgCode = org.getCoding();
			}
			if (null != orgCode && !"".equals(orgCode)) {
				sb.append(" and o.createBy.org.coding like '" + orgCode + "%' ");
			} else {
				sb.append(" and o.createBy.org.coding like '@@@@@@@%' ");
			}

			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  o.shortName like ?");
				params.add("%" + keyWord + "%");
			}
			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				sb.append(" and  o.code like '%" + queryCode + "'");
			}
			pageView.setQueryResult(financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "listQianyue";
	}
	/**
	 * 今日待挂单的融资项目
	 * 
	 * @return
	 * @throws Exception
	 */
	public String today_for_order() throws Exception {
		this.actionUrl = "today_for_order";
		try {
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			List<String> params = new ArrayList<String>();
			orderby.put("createDate", "desc");
			StringBuilder sb = new StringBuilder(" o.state in ('1','1.5') ");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			// sb.append(" and o.startDate >=
			// to_date('"+format.format(today)+"','yyyy-MM-dd')-1");
			sb.append(" and o.startDate <= to_date('" + format.format(today) + "','yyyy-MM-dd')+1");
			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			if (null != u) org = u.getOrg();
			if (null != org) {
				orgCode = org.getCoding();
			}
			if (null != orgCode && !"".equals(orgCode)) {

				sb.append(" and o.createBy.org.coding like '" + orgCode + "%' ");
			} else {
				sb.append(" and o.createBy.org.coding like '@@@@@@@%' ");
			}
			pageView.setQueryResult(financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "listtoday";
	}

	/**
	 * 待挂单的融资项目
	 * 
	 * @return
	 * @throws Exception
	 */
	public String for_order() throws Exception {
		this.actionUrl = "for_order";
		try {
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			List<String> params = new ArrayList<String>();

			orderby.put("startDate", "desc");
			StringBuilder sb = new StringBuilder(" o.state='1.5' ");

			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			if (null != u) org = u.getOrg();
			if (null != org) {
				orgCode = org.getCoding();
			}
			if (null != orgCode && !"".equals(orgCode)) {
				sb.append(" and o.createBy.org.coding like '" + orgCode + "%' ");
			} else {
				sb.append(" and o.createBy.org.coding like '@@@@@@@%' ");
			} 
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  o.shortName like ?");
				params.add("%" + keyWord + "%");
			}
			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				sb.append(" and  o.code like '%" + queryCode + "'");
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			if (null != this.startDate && null != this.endDate) {
				sb.append(" and o.startDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd')");
			}
			pageView.setQueryResult(financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "listorder";
	}

	/**
	 * 待发布的融资项目
	 * @return
	 * @throws Exception
	 */
	public String list_for_nofabu() throws Exception {
		this.actionUrl = "list_for_nofabu";
		try {
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			List<String> params = new ArrayList<String>();
			orderby.put("startDate", "desc");
			StringBuilder sb = new StringBuilder(" o.state='1' ");

			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			if (null != u) org = u.getOrg();
			if (null != org) {
				orgCode = org.getCoding();
			}
			if (null != orgCode && !"".equals(orgCode)) {

				sb.append(" and o.createBy.org.coding like '" + orgCode + "%' ");
			} else {
				sb.append(" and o.createBy.org.coding like '@@@@@@@%' ");
			}
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  o.shortName like ?");
				params.add("%" + keyWord + "%");
			}
			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				sb.append(" and  o.code like '%" + queryCode + "'");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			if (null != this.startDate && null != this.endDate) {
				sb.append(" and o.startDate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(this.getEndDate()) + "','yyyy-MM-dd')");
			}
			pageView.setQueryResult(financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "list_for_nofabu";
	}
	 
	

	/**
	 * 投标中的融资项目
	 * 
	 * @return
	 * @throws Exception
	 */
	public String for_subscribe() throws Exception { 
		return "list_for_subscribe_json";
	}

	/**
	 * 投标中融资项目
	 * 输出JSON
	 */
	public void getFinancingBaseListForSubscribe(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

			ArrayList<Object> args_list = new ArrayList<Object>();
			
			StringBuilder subsql = new StringBuilder(" state in ('2') ");

			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				if (null != containstr && !"".equals(containstr.trim())) {
					containstr=containstr.trim();
					if("1".equals(containstr)){
						if(queryCode.contains(",")){ 
							subsql.append(stringAnalytical2(queryCode.toUpperCase(), ",","like"));	  
						}else{
							subsql.append(" and  financecode like ? ");
							args_list.add("%" + queryCode.toUpperCase() + "%");
						}
					}else if("2".equals(containstr)){ 
						if(queryCode.contains(",")){ 
							subsql.append(stringAnalytical2(queryCode.toUpperCase(), ",","not like"));	
						}else{
							subsql.append(" and  financecode not like ?");	
							args_list.add("%" + queryCode.toUpperCase() + "%");
						} 
					}else{
						subsql.append(" and  financecode like ?");	
						args_list.add("%" + queryCode.toUpperCase() + "%");
					}
				}else{
					subsql.append(" and  financecode like ?");	
					args_list.add("%" + queryCode.toUpperCase() + "%");
				}
			}
			
			/*if( null != this.startDate && null != this.endDate){
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getEndDate());
				cal.add(Calendar.DAY_OF_MONTH, 1);
				subsql.append(" and to_char(startdate,'yyyymmdd') between ? and ? ");
				args_list.add(format.format(this.getStartDate()));
				args_list.add(format.format(cal.getTime()));
			}*/
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = u.getOrg();
			if (("5301".equals(org.getShowCoding()) || "530100".equals(org.getShowCoding()))) {
				if(StringUtils.isNotBlank(this.queryByOrgCode)){
					subsql.append(" and createorg = ? ");
					args_list.add(this.queryByOrgCode);
				}
			} else {
				subsql.append(" and createorg = ? ");
				args_list.add(org.getShowCoding());
			}

			/*if (StringUtils.isNotBlank(this.states)) {
				if (this.states.equals("5")) {
					subsql.append(" and state in (?,?)");
					args_list.add("5");
					args_list.add("6");
				} else {
					subsql.append(" and state = ? ");
					args_list.add(this.states);
				}
			}*/

			if (StringUtils.isNotBlank(this.qkeyWord)) {
				qkeyWord = qkeyWord.trim();
				subsql.append(" and (");
				subsql.append("     financerusername like ");
				subsql.append("'%" + qkeyWord + "%'");
				subsql.append("     or ");
				subsql.append("     financerrealname like ");
				subsql.append("'%" + qkeyWord + "%'");
				subsql.append("     or ");
				subsql.append("     financecode like ");
				subsql.append("'%" + qkeyWord + "%'");
				subsql.append("     or ");
				subsql.append("     financename like ");
				subsql.append("'%" + qkeyWord + "%'");
				subsql.append(" )");
			}

			Object [] args = args_list.toArray();
			
			List<Map<String, Object>> result = this.financingBaseService.queryForList("*","v_finance",subsql.toString(),args, getPage(), rows);
			int total = this.financingBaseService.queryForListTotal("id","v_finance",subsql.toString(),args);

			for (Map<String, Object> obj : result) {	
					obj.put("showok", true);
			}
			
			
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			long[] totalData = financingBaseService.queryForExpiredListCount(subsql.toString(),args);
			JSONArray footer = new JSONArray();
			JSONObject _totalDate = new JSONObject();
			_totalDate.element("MAXAMOUNT", totalData[0]);
			_totalDate.element("CURCANINVEST", totalData[1]);
			_totalDate.element("CURRENYAMOUNT", totalData[2]);
			_totalDate.element("CREATEORG_SHORT", "合计");
			footer.add(_totalDate);
			o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getFinancingBaseListForSearch(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

			ArrayList<Object> args_list = new ArrayList<Object>();
			
			StringBuilder subsql = new StringBuilder(" state !='10' ");

			if( null == this.startDate  ){
				this.setStartDate(new Date());
			}
			
			if(null == this.endDate){
				this.setEndDate(new Date());
			}
			
			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				if (null != containstr && !"".equals(containstr.trim())) {
					containstr=containstr.trim();
					if("1".equals(containstr)){
						if(queryCode.contains(",")){ 
							subsql.append(stringAnalytical2(queryCode.toUpperCase(), ",","like"));	  
						}else{
							subsql.append(" and  financecode like ? ");
							args_list.add("%" + queryCode.toUpperCase() + "%");
						}
					}else if("2".equals(containstr)){ 
						if(queryCode.contains(",")){ 
							subsql.append(stringAnalytical2(queryCode.toUpperCase(), ",","not like"));	
						}else{
							subsql.append(" and  financecode not like ? ");
							args_list.add("%" + queryCode.toUpperCase() + "%");
						} 
					}else{
						subsql.append(" and  financecode like ? ");	
						args_list.add("%" + queryCode.toUpperCase() + "%");
					}
				}else{
					subsql.append(" and  financecode like ? ");	
					args_list.add("%" + queryCode.toUpperCase() + "%");
				}
			}
			
			if( null != this.startDate && null != this.endDate){
				subsql.append(" and to_char(startdate,'yyyymmdd') between ? and ? ");
				args_list.add(format.format(this.getStartDate()));
				args_list.add(format.format(this.getEndDate()));
			}
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = u.getOrg();
			if (("5301".equals(org.getShowCoding()) || "530100".equals(org.getShowCoding()))) {
				if(StringUtils.isNotBlank(this.queryByOrgCode)){
					subsql.append(" and createorg = ? ");
					args_list.add(this.queryByOrgCode);
				}
			} else {
				subsql.append(" and createorg = ? ");
				args_list.add(org.getShowCoding());
			}

			if (StringUtils.isNotBlank(this.states)) {
				if (this.states.equals("5")) {
					subsql.append(" and state in (?,?)");
					args_list.add("5");
					args_list.add("6");
				} else {
					subsql.append(" and state = ? ");
					args_list.add(this.states);
				}
			}   

			if (StringUtils.isNotBlank(this.qkeyWord)) {
				qkeyWord = qkeyWord.trim();
				subsql.append(" and (");
				subsql.append("     financerusername like ? ");
				args_list.add("%" + qkeyWord + "%");
				
				subsql.append("     or ");
				subsql.append("     financerrealname like ? ");
				args_list.add("%" + qkeyWord + "%");
				
				subsql.append("     or ");
				subsql.append("     financecode like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financename like ? ");
				args_list.add("%" + qkeyWord + "%");
				
				subsql.append(" )");
			}

			Object [] args = args_list.toArray();
			
			List<Map<String, Object>> result = this.financingBaseService.queryForList("*","v_finance",subsql.toString(),args, getPage(), rows);
			int total = this.financingBaseService.queryForListTotal("id","v_finance",subsql.toString(),args);

			
			
			for (Map<String, Object> obj : result) {
				if (null != obj.get("enddate")) {
					if (((Date) obj.get("enddate")).compareTo(new Date()) <= 0){// 融资到期
						if ((!"5".equals(obj.get("state"))) && (!"6".equals(obj.get("state"))) && (!"7".equals(obj.get("state")))){// 5融资确认已完成
						// 6费用核算完成 7签约完成
							obj.put("showok", true);
						}
					} else {
						if ("4".equals(obj.get("state"))){// 已满标或者融资到期可以融资确认
							obj.put("showok", true);
						}
					}
				}

			}
			
			
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			
			long[] totalData = financingBaseService.queryForExpiredListCount(subsql.toString(),args);
			JSONArray footer = new JSONArray();
			JSONObject _totalDate = new JSONObject();
			_totalDate.element("MAXAMOUNT", totalData[0]);
			_totalDate.element("CURCANINVEST", totalData[1]);
			_totalDate.element("CURRENYAMOUNT", totalData[2]);
			_totalDate.element("CREATEORG_SHORT", "合计");
			footer.add(_totalDate);
			o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 已签约融资项目
	 * 输出JSON
	 */
	public void getFinancingBaseListForYiqianyue(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

			ArrayList<Object> args_list = new ArrayList<Object>();
			
			StringBuilder subsql = new StringBuilder(" state = '7' ");

			
			
			if (StringUtils.isNotBlank(this.queryName)) {
				this.queryName = this.queryName.trim().toUpperCase();
				subsql.append(" and  financename like ? ");
				args_list.add("%" + this.queryName+ "%");
			}
			
			if (StringUtils.isNotBlank(this.queryCode)) {
				this.queryCode = this.queryCode.trim().toUpperCase();
				if (StringUtils.isNotBlank(this.containstr)) {
					this.containstr = this.containstr.trim();
					if("1".equals(this.containstr)){
						if(this.queryCode.contains(",")){ 
							subsql.append(stringAnalytical2(this.queryCode, ",","like"));	  
						}else{
							subsql.append(" and  financecode like ? ");
							args_list.add("%" + this.queryCode + "%");
						}
					}else if("2".equals(this.containstr)){ 
						if(this.queryCode.contains(",")){ 
							subsql.append(stringAnalytical2(this.queryCode, ",","not like"));	
						}else{
							subsql.append(" and  financecode not like ? ");	
							args_list.add("%" + this.queryCode + "%");
						} 
					}else{
						subsql.append(" and  financecode like ? ");		
						args_list.add("%" + this.queryCode + "%");
					}
				}else{
					subsql.append(" and  financecode like ? ");	
					args_list.add("%" + this.queryCode + "%");
				}
			}
			
			if( null != this.startDate && null != this.endDate){
				subsql.append(" and to_char(qianyuedate,'yyyymmdd') between ? and ? ");
				args_list.add(format.format(this.getStartDate()));
				args_list.add(format.format(this.getEndDate()));
			}
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = u.getOrg();
			if (("5301".equals(org.getShowCoding()) || "530100".equals(org.getShowCoding()))) {
				
			} else {
				subsql.append(" and createorg = ? ");
				args_list.add(org.getShowCoding());
			}

			if (StringUtils.isNotBlank(this.states)) {
				if (this.states.equals("5")) {
					subsql.append(" and state in (?,?)");
					args_list.add("5");
					args_list.add("6");
				} else {
					subsql.append(" and state = ? ");
					args_list.add(this.states);
				}
			}

			if (StringUtils.isNotBlank(this.qkeyWord)) {
				qkeyWord = qkeyWord.trim();
				subsql.append(" and (");
				subsql.append("     financerusername like ? ");
				args_list.add("%" + this.qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financerrealname like ? ");
				args_list.add("%" + this.qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financecode like ? ");
				args_list.add("%" + this.qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financename like ? ");
				args_list.add("%" + this.qkeyWord + "%");
				subsql.append(" )");
			}

			subsql.append(" order by " + sort + " "+ order);
			
			Object [] args = args_list.toArray();
			
			List<Map<String, Object>> result = this.financingBaseService.queryForList("*","v_finance",subsql.toString(),args, getPage(), rows);
			int total = this.financingBaseService.queryForListTotal("id","v_finance",subsql.toString(),args);

			for (Map<String, Object> obj : result) {
				if (null != obj.get("enddate")) {
					if(1==Integer.parseInt(obj.get("PreInvest").toString())){//1开启优先投标
						this.groupStr="";
						StringBuilder sbGroup = new StringBuilder("select ");
						String sqlGroup = " o.groupId";
						sbGroup.append(sqlGroup); 
						sbGroup.append("  ,sum(0) ");
						sbGroup.append("  from FinancingRestrain o");
						sbGroup.append("   where o.financingCode='"+obj.get("FINANCECODE").toString()+"'");   
						sbGroup.append(" group by ").append(sqlGroup);       
						
						QueryResult<Object> qrgroup = financingBaseService.groupHqlQuery(sbGroup.toString());
						if (null !=qrgroup.getResultlist() && qrgroup.getResultlist().size() > 0) {
							for (Object arr : qrgroup.getResultlist()) {
								Object[] o = (Object[]) arr;
								if (null != o[1]) { 
									Group g=this.groupService.selectById(o[0].toString());
							    	this.groupStr+=g.getName()+",";
								}
							}
							if (null!=this.groupStr&&!"".equals(this.groupStr)) {
						    	this.groupStr=this.groupStr.substring(0, this.groupStr.length()-1);
						    }	
							
						}   
						obj.put("GROUPSTR", this.groupStr);
						//fb.setGroupStr(this.groupStr);
					}
					if (((Date) obj.get("enddate")).compareTo(new Date()) <= 0){// 融资到期
						if ((!"5".equals(obj.get("state"))) && (!"6".equals(obj.get("state"))) && (!"7".equals(obj.get("state")))){// 5融资确认已完成
						// 6费用核算完成 7签约完成
							obj.put("showok", true);
						}
					} else {
						if ("4".equals(obj.get("state"))){// 已满标或者融资到期可以融资确认
							obj.put("showok", true);
						}
					}
				}

			}
			
			
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			long[] totalData = financingBaseService.queryForExpiredListCount(subsql.toString(),args);
			JSONArray footer = new JSONArray();
			JSONObject _totalDate = new JSONObject();
			_totalDate.element("MAXAMOUNT", totalData[0]);
			_totalDate.element("CURCANINVEST", totalData[1]);
			_totalDate.element("CURRENYAMOUNT", totalData[2]);
			_totalDate.element("CREATEORG_SHORT", "合计");
			footer.add(_totalDate);
			o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	/**
	 * 已满标/已到期融资项目
	 * 
	 * @return
	 * @throws Exception
	 */
	public String for_ending() throws Exception { 
		return "listok";
	}
	
	/**
	 * 已满标已到期融资项目
	 * 输出JSON
	 */
	public void getFinishedFinancingBaseList(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			int rows = Integer.parseInt(request.getParameter("rows"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			ArrayList<Object> args_list = new ArrayList<Object>();
			
			StringBuilder subsql = new StringBuilder(" (state = '4' or ( endDate <= to_date('" + format.format(new Date()) + "','yyyy-MM-dd') and ( state = '3' or state = '2' ) ) )");
			
			
			
			if( null != this.startDate && null != this.endDate){
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getEndDate());
				cal.add(Calendar.DAY_OF_MONTH, 1);
				subsql.append(" and startdate between to_date('" + format.format(this.getStartDate()) + "','yyyy-MM-dd') and to_date('" + format.format(cal.getTime()) + "','yyyy-MM-dd')");
			}
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = u.getOrg();
			if (("5301".equals(org.getShowCoding()) || "530100".equals(org.getShowCoding()))) {
				if(StringUtils.isNotBlank(this.queryByOrgCode)){
					subsql.append(" and createorg = ? ");
					args_list.add(this.queryByOrgCode);
				}
			} else {
				subsql.append(" and createorg = ? ");
				args_list.add(org.getShowCoding());
			}

			if (StringUtils.isNotBlank(this.states)) {
				if (this.states.equals("5")) {
					subsql.append(" and state in ('5','6')");
				} else {
					subsql.append(" and state = ? ");
					args_list.add(this.states);
				}
			}

			if (StringUtils.isNotBlank(qkeyWord)) {;
				qkeyWord = qkeyWord.trim();
				subsql.append(" and (");
				subsql.append("     financerusername like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financerrealname like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financecode like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append("     or ");
				subsql.append("     financename like ? ");
				args_list.add("%" + qkeyWord + "%");
				subsql.append(" )");
			}
			
			Object [] args = args_list.toArray();

			List<Map<String, Object>> result = this.financingBaseService.queryForList("*","v_finance",subsql.toString(),args, getPage(), rows);
			int total = this.financingBaseService.queryForListTotal("id","v_finance",subsql.toString(),args);

			for (Map<String, Object> obj : result) {
				if (null != obj.get("enddate")) {
					if (((Date) obj.get("enddate")).compareTo(new Date()) <= 0){// 融资到期
						if ((!"5".equals(obj.get("state"))) && (!"6".equals(obj.get("state"))) && (!"7".equals(obj.get("state")))){// 5融资确认已完成
						// 6费用核算完成 7签约完成
							obj.put("showok", true);
						}
					} else {
						if ("4".equals(obj.get("state"))){// 已满标或者融资到期可以融资确认
							obj.put("showok", true);
						}
					}
				}

			}
			JSONArray object = JSONArray.fromObject(result);
			JSONObject o = new JSONObject();
			o.element("total", total);
			o.element("rows", object);
			long[] totalData = financingBaseService.queryForExpiredListCount(subsql.toString(),args);
			JSONArray footer = new JSONArray();
			JSONObject _totalDate = new JSONObject();
			_totalDate.element("MAXAMOUNT", totalData[0]);
			_totalDate.element("CURCANINVEST", totalData[1]);
			_totalDate.element("CURRENYAMOUNT", totalData[2]);
			_totalDate.element("CREATEORG_SHORT", "合计");
			footer.add(_totalDate);
			o.element("footer", footer);
			ServletActionContext.getResponse().getWriter().write(o.toString());
			ServletActionContext.getRequest().setAttribute("totalData", totalData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	

	/**
	 * 我的融资签约
	 * 
	 * @return
	 * @throws Exception
	 */
	public String myListQianyue() throws Exception {
		try {
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("createDate", "desc");
			StringBuilder sb = new StringBuilder(" 1=1    ");

			sb.append(" and ");
			sb.append(" o.state in ('6','7')");
			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			MemberBase mb = memberBaseService.getMemByUser(u);
			sb.append(" and o.financier.id='" + mb.getId() + "' ");

			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  o.shortName like ?");
				params.add("%" + keyWord + "%");
			}
			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				sb.append(" and  o.code like '%" + queryCode + "'");
			}
			pageView.setQueryResult(financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			for (FinancingBase fb : pageView.getRecords()) {
				fb.setFc(this.financingCostService.getCostByFinancingBase(fb.getId()));
			}
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "myListQianyue";
	}

	/**
	 * 融资项目审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkList() throws Exception {
		try {
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("code", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  and o.state='0'  ");

			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			if (null != u) org = u.getOrg();
			if (null != org) {
				orgCode = org.getCoding();
			}
			if (null != orgCode && !"".equals(orgCode)) {

				sb.append(" and o.createBy.org.coding like '" + orgCode + "%' ");
			} else {
				sb.append(" and o.createBy.org.coding like '@@@@@@@%' ");
			}

			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  o.shortName like ?");
				params.add("%" + keyWord + "%");
			}
			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				sb.append(" and  o.code like '%" + queryCode + "'");
			}
			QueryResult<FinancingBase> qr = financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<FinancingBase> fbs = qr.getResultlist();
			List<FinancingBase> tempfbs = new ArrayList<FinancingBase>();
			for (FinancingBase fb : fbs) {
				if (null != fb.getEndDate()) {
					int day = DateUtils.getBetween(fb.getEndDate(), new Date());
					if (day > 0 || "4".equals(fb.getState()))// 已满标或者融资到期可以融资确认
					{
						if (!"5".equals(fb.getState())) {
							fb.setShowOk(true);
						}
						tempfbs.add(fb);
					} else {
						tempfbs.add(fb);
					}
				} else {
					tempfbs.add(fb);
				}

			}
			qr.setResultlist(tempfbs);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "checkList";
	}

	/**
	 * 我的融资项目
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listForPerson() throws Exception {
		try {
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("code", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");

			// 数据控制(本级和下级机构的数据)
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			MemberBase mb = memberBaseService.getMemByUser(u);
			sb.append(" and o.financier.id='" + mb.getId() + "' ");

			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and (");
				sb.append(" o.code like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.shortName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" )");
			}

			QueryResult<FinancingBase> qr = financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<FinancingBase> fbs = qr.getResultlist();
			List<FinancingBase> tempfbs = new ArrayList<FinancingBase>();
			for (FinancingBase fb : fbs) {
				FinancingCost fc = this.financingCostService.selectById(" from FinancingCost where financingBase.id = ? ", new Object[] { fb.getId() });
				fb.setFc(fc);
				tempfbs.add(fb);
			}
			qr.setResultlist(tempfbs);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "listForPerson";
	}

	/**
	 * 到期列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String dqlb() throws Exception {
		try {
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (null == u || null == u.getUserType() || !"r".equals(u.getUserType().toLowerCase())) return null;
			HttpServletRequest request = ServletActionContext.getRequest();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 10);
			///////////////////////////////////////////////////////////////////////////////////////////////
			HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();
			inParamList.put(1, u.getUsername());
			inParamList.put(2, new java.sql.Date(new Date().getTime()));
			inParamList.put(3, new java.sql.Date(cal.getTimeInMillis()));
			OutParameterModel outParameter = new OutParameterModel(4, OracleTypes.CURSOR); 
			
			ArrayList<LinkedHashMap<String, Object>> dqlb_yuqi = this.financingBaseService.callProcedureForList("P_FINANCER_FINANCE.dqlb_yuqi", inParamList, outParameter);
			ArrayList<LinkedHashMap<String, Object>> dqlb_daoqi = this.financingBaseService.callProcedureForList("P_FINANCER_FINANCE.dqlb_daoqi", inParamList, outParameter);
			
			request.setAttribute("dqlb_yuqi", dqlb_yuqi);
			request.setAttribute("dqlb_daoqi", dqlb_daoqi);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "dqlb";
	}

	/**
	 * 融资项目列表(可以投标的)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String investList() throws Exception {
		try {
			Date today = DateUtils.getAfter(new Date(), Integer.valueOf(0));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String d = format.format(today);
			PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("code", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  ");
			List<String> params = new ArrayList<String>();
			sb.append(" and ");
			sb.append(" o.state in ('2','3','4')");

			sb.append(" and ");
			sb.append(" o.curCanInvest >=0");

			// startDate投标开始日期;endDate投标截止日期

			// oracle
			sb.append(" and ");
			sb.append(" o.startDate <= to_date('" + d + "','yyyy-MM-dd')");
			sb.append(" and ");
			sb.append(" o.endDate >= to_date('" + d + "','yyyy-MM-dd')");

			// mysql
			// sb.append(" and ");
			// sb.append(" o.startDate <= '"+d+"'");
			// sb.append(" and ");
			// sb.append(" o.endDate >= '"+d+"'");

			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and (");
				sb.append(" o.code like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.shortName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" )");
			}
			QueryResult<FinancingBase> qr = financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby);
			List<FinancingBase> lis = qr.getResultlist();
			List<FinancingBase> tempfbs = new ArrayList<FinancingBase>();
			for (FinancingBase f : lis) {
				f.setMaxAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getMaxAmount(), 3), false));
				f.setCurCanInvest_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getCurCanInvest(), 3), false));
				f.setCurrenyAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(f.getCurrenyAmount(), 3), false));
				tempfbs.add(f);
			}
			qr.setResultlist(tempfbs);
			pageView.setQueryResult(qr);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "investlist";
	}

	/**
	 * 融资方列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String financiers() throws Exception {
		try {
			PageView<MemberBase> pageView = new PageView<MemberBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  and o.memberType.code='R' and o.state='2' ");
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (null != u) {
				//sb.append(" and o.creator.org.coding like '" + u.getOrg().getCoding() + "%' ");
				sb.append(" and  o.user.org.coding like '" + u.getOrg().getCoding() + "%' ");
			} else {
				//sb.append(" and o.creator.org.coding like '@@@@@@@%' ");
				sb.append(" and  o.user.org.coding like '@@@@@@@%' ");   
			}  

			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {

				keyWord = keyWord.trim();
				sb.append(" and (");
				sb.append(" o.pName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.eName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.pPhone like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.ePhone like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.pMobile like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.eMobile like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.user.username like ?");
				params.add("%" + keyWord + "%");

				sb.append(" )");
			}
			pageView.setQueryResult(memberBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));

			List<MemberBase> memberBaseTempTs = pageView.getRecords();
			List<MemberBaseVo> memberBaseTemps = new ArrayList<MemberBaseVo>();
			for (MemberBase mb : memberBaseTempTs) {
				MemberBaseVo mbVo = new MemberBaseVo();
				mbVo.setId(mb.getId());
				if ("1".equals(mb.getCategory())) {
					if (null != mb.getpName() && !"".equals(mb.getpName())) mbVo.setShowName(mb.getpName()+"("+mb.getUser().getUsername()+")");
					else mbVo.setShowName("暂无");
					if (null != mb.getpMobile() && !"".equals(mb.getpMobile())) mbVo.setShowMobile(mb.getpMobile());
					else mbVo.setShowMobile("暂无");
					if (null != mb.getpPhone() && !"".equals(mb.getpPhone())) mbVo.setShowPhone(mb.getpPhone());
					else mbVo.setShowPhone("暂无");
				} else {
					if (null != mb.geteName() && !"".equals(mb.geteName())) mbVo.setShowName(mb.geteName()+"("+mb.getUser().getUsername()+")");
					else mbVo.setShowName("暂无");
					if (null != mb.geteMobile() && !"".equals(mb.geteMobile())) mbVo.setShowMobile(mb.geteMobile());
					else mbVo.setShowMobile("暂无");
					if (null != mb.getePhone() && !"".equals(mb.getePhone())) mbVo.setShowPhone(mb.getePhone());
					else mbVo.setShowPhone("暂无");
				}
				if (null != mb.getCompanyProperty()) {
					mbVo.setCompanyPropertyName(mb.getCompanyProperty().getName());
				} else {
					mbVo.setCompanyPropertyName("暂无");
				}
				mbVo.setProvinceName(mb.getProvinceName());
				mbVo.setCityName(mb.getCityName());
				if (null != mb.getIndustry()) {
					mbVo.setIndustryName(mb.getIndustry().getName());
				} else {
					mbVo.setIndustryName("暂无");
				}
				memberBaseTemps.add(mbVo);
			}
			HashMap<String, Object> objectResult = new HashMap<String, Object>();
			objectResult.put("listData", memberBaseTemps);
			objectResult.put("pageData", PageString.getPage(pageView.getFirstResult(), pageView.getMaxresult(), pageView.getTotalrecord()));
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), objectResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 担保方列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String guarantees() throws Exception {
		try {
			PageView<MemberBase> pageView = new PageView<MemberBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "desc");
			StringBuilder sb = new StringBuilder(" 1=1  and o.memberType.code='D' and o.state='2'  ");

			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (null != u) {
				sb.append(" and o.creator.org.coding like '" + u.getOrg().getCoding() + "%' ");
				//sb.append(" and  o.user.org.coding like '" + u.getOrg().getCoding() + "%' ");
			} else {
				sb.append(" and o.creator.org.coding like '@@@@@@@%' ");
				//sb.append(" and  o.user.org.coding like '@@@@@@@%' ");
			}  
		 
			List<String> params = new ArrayList<String>();
			if (null != keyWord && !"".equals(keyWord.trim())) {

				keyWord = keyWord.trim();
				sb.append(" and (");
				sb.append(" o.pName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.eName like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.pPhone like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.ePhone like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.pMobile like ?");
				params.add("%" + keyWord + "%");
				sb.append(" or ");
				sb.append(" o.eMobile like ?");
				params.add("%" + keyWord + "%");
				sb.append(" )");
			}

			pageView.setQueryResult(memberBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			List<MemberBase> memberBaseTempTs = pageView.getRecords();
			List<MemberBaseVo> memberBaseTemps = new ArrayList<MemberBaseVo>();
			for (MemberBase mb : memberBaseTempTs) {
				MemberBaseVo mbVo = new MemberBaseVo();
				mbVo.setId(mb.getId());
				if ("1".equals(mb.getCategory())) {
					if (null != mb.getpName() && !"".equals(mb.getpName())) mbVo.setShowName(mb.getpName()+"("+mb.getUser().getUsername()+")");
					else mbVo.setShowName("暂无");
					if (null != mb.getpMobile() && !"".equals(mb.getpMobile())) mbVo.setShowMobile(mb.getpMobile());
					else mbVo.setShowMobile("暂无");
					if (null != mb.getpPhone() && !"".equals(mb.getpPhone())) mbVo.setShowPhone(mb.getpPhone());
					else mbVo.setShowPhone("暂无");
				} else {
					if (null != mb.geteName() && !"".equals(mb.geteName())) mbVo.setShowName(mb.geteName()+"("+mb.getUser().getUsername()+")");
					else mbVo.setShowName("暂无");
					if (null != mb.geteMobile() && !"".equals(mb.geteMobile())) mbVo.setShowMobile(mb.geteMobile());
					else mbVo.setShowMobile("暂无");
					if (null != mb.getePhone() && !"".equals(mb.getePhone())) mbVo.setShowPhone(mb.getePhone());
					else mbVo.setShowPhone("暂无");
				}
				if (null != mb.getCompanyProperty()) {
					mbVo.setCompanyPropertyName(mb.getCompanyProperty().getName());
				} else {
					mbVo.setCompanyPropertyName("暂无");
				}
				mbVo.setProvinceName(mb.getProvinceName());
				mbVo.setCityName(mb.getCityName());
				if (null != mb.getIndustry()) {
					mbVo.setIndustryName(mb.getIndustry().getName());
				} else {
					mbVo.setIndustryName("暂无");
				}
				memberBaseTemps.add(mbVo);
			}
			HashMap<String, Object> objectResult = new HashMap<String, Object>();
			objectResult.put("listData", memberBaseTemps);
			objectResult.put("pageData", PageString.getPage(pageView.getFirstResult(), pageView.getMaxresult(), pageView.getTotalrecord()));
			DoResultUtil.doObjectResult(ServletActionContext.getResponse(), objectResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String print_finance_voucher() {
		try {
			this.financingBase.setFc(this.financingCostService.getCostByFinancingBase(this.financingBase.getId()));
			long investor_with_530105 = this.investRecordService.getScrollDataCount("from InvestRecord o where o.financingBase.id = ? and o.investor.user.org.parentCoding = ?", this.financingBase.getId(), "530105");
			long investor_with_530101 = this.investRecordService.getScrollDataCount("from InvestRecord o where o.financingBase.id = ? and o.investor.user.org.parentCoding != ?", this.financingBase.getId(), "530105");
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("investor_with_530101", investor_with_530101);
			request.setAttribute("investor_with_530105", investor_with_530105);
			double lx_all = 0d;
			Map<String,Object> lx_map = this.paymentRecordService.queryForObject("sum(p.xylx) lx_all", " t_payment_record p ", "exists (select i.id from t_invest_record i where i.financingbase_id = '"+this.financingBase.getId()+"' and p.investrecord_id = i.id )", null);
			if(lx_map.get("lx_all") != null){
				lx_all = Double.parseDouble(lx_map.get("lx_all").toString());
			}
			String lx_all_chinese = MoneyFormat.format(Double.toString(lx_all), true);
			request.setAttribute("lx_all", lx_all);
			request.setAttribute("lx_all_chinese", lx_all_chinese);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "print_finance_voucher";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FinancingBase getFinancingBase() {
		return financingBase;
	}

	public void setFinancingBase(FinancingBase financingBase) {
		this.financingBase = financingBase;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public List<String> getFileIds() {
		return fileIds;
	}

	public List<BusinessType> getBusinessTypes() {
		return businessTypes;
	}

	public void setBusinessTypes(List<BusinessType> businessTypes) {
		this.businessTypes = businessTypes;
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
		try {
			if (this.id == null || "".equals(this.id)) {
				this.financingBase = new FinancingBase();
			} else {
				this.financingBase = this.financingBaseService.selectById(this.id);
				this.financingBase.setMaxAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(this.financingBase.getMaxAmount(), 3), true));
				this.financingBase.setCurCanInvest_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(this.financingBase.getCurCanInvest(), 3), true));
				this.financingBase.setCurrenyAmount_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(this.financingBase.getCurrenyAmount(), 3), true));
				files = uploadFileService.getCommonListData("from UploadFile c where c.entityFrom = 'PreFinancingBase' and c.entityId='" + this.financingBase.getCode() + "' ");
				for (UploadFile file : files) {
					file.setFrontId(file.getId().replaceAll("\\.", ""));
				}
				if (null != this.financingBase.getMemberGuarantee()) {
					memberGuarantee = this.financingBase.getMemberGuarantee();
				}

				if (null == memberGuarantee) {
					memberGuarantee = new MemberGuarantee();
				}
				if (this.financingBase.getState().equals("7")) {// 已签约融资项目，取出它的所有投标记录
					// this.investRecords =
					// this.investRecordService.getInvestRecordListByFinancingId(this.financingBase.getId());
					this.contractList = this.financingContractService.getContractList(this.financingBase.getId());
				}
				if(1==this.financingBase.getPreInvest()){//1让用户优先投标
					StringBuilder sb = new StringBuilder("select ");
					String sqlGroup = " o.groupId";
					sb.append(sqlGroup); 
					sb.append("  ,sum(0) ");
					sb.append("  from FinancingRestrain o");
					sb.append("   where o.financingCode='"+this.financingBase.getCode()+"'");   
					sb.append(" group by ").append(sqlGroup);       
					
					QueryResult<Object> qr = paymentRecordService.groupHqlQuery(sb.toString());
					if (null != qr.getResultlist() && qr.getResultlist().size() > 0) {
						for (Object arr : qr.getResultlist()) {
							Object[] o = (Object[]) arr;
							if (null != o[1]) { 
								Group g=this.groupService.selectById(o[0].toString());
						    	this.groupStr+=g.getName()+",";
							}
						}
						if (null!=this.groupStr&&!"".equals(this.groupStr)) {
					    	this.groupStr=this.groupStr.substring(0, this.groupStr.length()-1);
					    }	
					} 
				    
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 电子合同汇总(投标记录)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String prjournaling() throws Exception {
		try {
			if (this.financingBase == null) {
				return null;
			}
			this.investRecords = this.investRecordService.getScrollDataCommon(" from InvestRecord i where i.financingBase.id = ? order by i.investor.user.username ", new String[] { this.financingBase.getId() });
			FinancingCost fc = this.financingCostService.selectById(" from FinancingCost where financingBase.id = ? ", new Object[] { this.financingBase.getId() });
			this.financingBase.setFc(fc);
			for(InvestRecord ir : this.investRecords){
				if(ir.getContracts()!=null && ir.getContracts().size() > 0 && ir.getContract()!=null && ir.getContract().getFirst_id()!=null){
					MemberBase mb = this.memberBaseService.findByIdCard(ir.getContract().getFirst_id());
					if(mb != null){
						ir.setInvestor(mb);
					}
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
			return "prjournaling";

	}

	private String excel;
		
	
	public String getExcel() {
		return excel;
	}

	public void setExcel(String excel) {
		this.excel = excel;
	}

	// 已签约融资项目列表
	public String list_qianyue() throws Exception {
		return "list_for_yiqianyue_json";
	}

	// 某融资项目下的所有投标记录
	public String showInvest() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		ArrayList<Object> args_list = new ArrayList<Object>();
		String fid = request.getParameter("fid");
		if(StringUtils.isBlank(fid)) return null;
		args_list.add(fid);
		Object [] args = args_list.toArray();
		try {
			Map<String,Object> financing = this.financingBaseService.queryForObject("fb.code,fb.shortname,u.username", "t_financing_base fb,t_member_base m,sys_user u", "fb.id = ? and m.id = fb.financier_id and m.user_id = u.id ",args);
			List<Map<String, Object>> list = this.financingBaseService.queryForList("u.username,u.realname,m.idcardno,i.investamount,i.createdate,i.id,c.contract_numbers,c.investor_make_sure", "t_invest_record i,t_member_base m,sys_user u ,t_contract_key_data c ", "i.financingbase_id = ? and m.id = i.investor_id and m.user_id = u.id and c.inverstrecord_id = i.id order by u.username ",args);
			ServletActionContext.getRequest().setAttribute("records", list);
			ServletActionContext.getRequest().setAttribute("financing", financing);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "showInvest";
	}

	/**
	 * 取消指定的融资项目。只有签约之前的融资项目才能取消。
	 * @throws IOException 
	 */
	public void cancel() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		byte state = this.openCloseDealService.checkState();
		//state=1;开市
		//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
		//state=5;开夜市：只能投标
		//state=6;休夜市：一切业务停止
		if (state == 3 || state == 4 ||state == 5||state==6) {
			json.element("code", -2);
			json.element("message", "交易市场未开市");
		} else if (state == 2) {
			json.element("code", -2);
			json.element("message", "现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)");
		} else {
			try {
				boolean canceled = false;
				canceled = financingBaseService.cancel(id);

				if(canceled){
					json.element("code", 1);
					json.element("message", "操作成功"); 
					//更新结束的融资项目
					try { ReadsStaticConstantPropertiesUtil.updateServiceCache(id); } catch (Exception e) {}
				}else{
					json.element("code", -1);
					json.element("message", "操作失败,原因未知");
				} 
			} catch (Exception e) {
				e.printStackTrace();
				json.element("code", -1);
				json.element("message", "操作失败:错误信息:"+e.getMessage());
			}
		}
		out.print(json);
		
	}

	/**
	 * 已结束融资项目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String end_list() throws Exception {
		PageView<FinancingBase> pageView = new PageView<FinancingBase>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("code", "desc");
		StringBuilder sb = new StringBuilder(" terminal = true ");
		if(this.getStartDate()!=null && this.getEndDate()!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date endDateNext = DateUtils.getAfter(this.getEndDate(),1);
			sb.append(" and o.terminal_date between to_date('" + sdf.format(this.getStartDate()) + "','yyyy-MM-dd') and  to_date('" + sdf.format(endDateNext) + "','yyyy-MM-dd')");
		}
		 
		List<String> params = new ArrayList<String>();
		if (StringUtils.isNotBlank(this.keyWord)) {
			keyWord = keyWord.trim();
			sb.append(" and (");
			sb.append(" o.shortName like ?");
			params.add("%" + keyWord + "%");
			sb.append("or o.code like ?");
			params.add("%" + keyWord + "%");
			sb.append(" )");
		}
		if(StringUtils.isNotBlank(this.org_code)){
			sb.append(" and o.createBy.org.showCoding = ? ");
			//sb.append(" and o.financier.org = ? ");
			params.add(this.org_code);
		}
		
		
		
		sb.append(" order by o.terminal_date desc ");
		if((this.getStartDate()!=null && this.getEndDate()!=null) ||StringUtils.isNotBlank(this.keyWord)){
			pageView.setQueryResult(financingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
			
		}
		return "end_list";
	}
	/**
	 * 已结束融资项目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String print_end_list() throws Exception {
		if(StringUtils.isBlank(this.org_code)){
			return "print_end_list";
		}
		
		
		this.checkDate();
		HttpServletRequest request = ServletActionContext.getRequest();
		ArrayList<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);

		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();

		inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		inParamList.put(2, new java.sql.Date(endDateNext.getTime()));
		inParamList.put(3, this.org_code);
		inParamList.put(4, this.keyWord);

		OutParameterModel outParameter = new OutParameterModel(5, OracleTypes.CURSOR);

		resultList = this.financingBaseService.callProcedureForList("P_FINANCER_FINANCE.end_list", inParamList, outParameter);

		Org org = this.orgService.findOrg(this.org_code);
		request.setAttribute("org", org);
		request.setAttribute("list", resultList);
		
		
		
		return "print_end_list";
	}
	/**
	 * 导出已结束融资项目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String export_end_list() throws Exception {
		if(StringUtils.isBlank(this.org_code)){
			return "export_end_list";
		}
		
		this.checkDate();
		HttpServletRequest request = ServletActionContext.getRequest();
		ArrayList<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();
		
		inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		inParamList.put(2, new java.sql.Date(endDateNext.getTime()));
		inParamList.put(3, this.org_code);
		inParamList.put(4, this.keyWord);
		
		OutParameterModel outParameter = new OutParameterModel(5, OracleTypes.CURSOR);
		
		resultList = this.financingBaseService.callProcedureForList("P_FINANCER_FINANCE.end_list", inParamList, outParameter);
		
		Org org = this.orgService.findOrg(this.org_code);
		request.setAttribute("org", org);
		request.setAttribute("list", resultList);
		return "export_end_list";
	}

	public String delayUI() {
		 List<FinancingRestrain> frs=financingRestrainService.getCommonListData("from FinancingRestrain o where o.financingCode='"+financingBase.getCode()+"'");
		 List<Group> selectGroups=new ArrayList<Group>();
		 for(FinancingRestrain re:frs){
			 selectGroups.add(new Group(re.getGroupId()));
		 } 
		 ServletActionContext.getRequest().setAttribute("selectGroups", selectGroups);//选中
		 
		 List<Group> groups=groupService.getCommonListData("from Group o  where o.state!='0' "); 
		 ServletActionContext.getRequest().setAttribute("groups", groups);//分组信息
		 
		 conTemplateId=this.financingBase.getContractTemplate().getId();
 
		  String codestr = this.financingBase.getCode().substring(0, 1);
		   /*if("H".equals(codestr)){//指定默认的模板
				codestr = "A";
			}*/
		   
			if("day".equals(this.financingBase.getBusinessType().getId())){//指定默认的模板
				codestr = "D";
			}
		  
		    cTemplates=this.contractTemplateService.getCommonListData("from ContractTemplate o  where 1=1 and o.code like '%"+codestr +"%' and not o.isOverDate='Y' " );  
		
		 return "delayUI";
	}
	private String groupIds;
	
	public void delay() throws IOException { 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		try {
			 List<FinancingRestrain> frs=financingRestrainService.getCommonListData("from FinancingRestrain o where o.financingCode='"+financingBase.getCode()+"'");
			 for(FinancingRestrain re:frs){
				 financingRestrainService.delete(re.getId());     
			 } 
			 
			if (null != groupIds && !"".equals(groupIds.trim())) {
				groupIds = groupIds.trim();  
				 //重新加入
				 for (StringTokenizer tokenizer = new StringTokenizer(groupIds, ","); tokenizer.hasMoreTokens();) {
					 String groupId = tokenizer.nextToken();
                     this.financingRestrainService.insert(new FinancingRestrain(groupId, financingBase.getCode()));
				 } 
			}
			 
			
			if (null != this.conTemplateId && !"".equals(conTemplateId.trim())) {
				financingBase.setContractTemplate(contractTemplateService.selectById(conTemplateId));
			}  
			
			financingBase.setModifyDate(new Date());
			financingBaseService.update(financingBase);
	  
			//更新融资项目缓存  
			if("0".equals(this.financingBase.getAutoinvest())){ //如果是委托投标，不更新缓存 
		    	try { ReadsStaticConstantPropertiesUtil.updateServiceCache(this.financingBase.getId()); } catch (Exception e) {}
			}
			
			
			
		} catch (EngineException e) {
			e.printStackTrace();
			out.println("操作失败！错误信息:"+e.getMessage());
			return;
		}
		out.println("操作成功！");
	}
	/**
	 * 融资提现申请 1、判断是否有待审核的提现申请 2、判断是否该融资项目已经完成提现申请(txed字段)
	 * 
	 * @return
	 */
	//2014-1-8修改:提现申请时,可用减少,冻结增加
	public String txrequest() {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			Object user_object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = null;
			if (user_object != null && user_object instanceof User) {
				user = (User) user_object;
			} else {
				return null;
			}
			
			
			byte state = this.openCloseDealService.checkState();
			//state=1;开市
			//state=2;休市：投资人业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
			//state=3;清算：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
			//state=4;对账：投资人业务提示“交易市场未开市”；融资方业务提示“现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)”
			//state=5;开夜市：只能投标
			//state=6;休夜市：一切业务停止
			
			if (state == 3 || state == 4 ||state == 5||state==6) {//清算，融资方不允许提现
				out.write("{'info':'现在是休市时间,请在开市时间进行操作(开市时间为:周一至周五9:30-16:30)'}");
				return null;
			}

			// 没找到包，ID不对，你懂的
			if (this.financingBase == null) {
				out.write("{'info':'系统错误，请刷新后重试，如果问题依旧，请联系商工作人员'}");
				return null;
			}

			// 看看融资方和登录用户是不是同一个人
			if (user.getId() != financingBase.getFinancier().getUser().getId()) {
				out.write("{'info':'请融资方本人来'}");
				return null;
			}

			// txed 是否已经提现。
			if (this.financingBase.isTxed()) {
				out.write("{'info':'该融资项目已经完成提现申请'}");
				return null;
			}

			// 查询有该融资项目是否有待审核的提现申请，如果有，请求不予处理
			long count = this.accountDealService.getScrollDataCount("from AccountDeal a where a.financing.id = ? and ( a.checkFlag = ? or a.checkFlag = ? )", this.financingBase.getId(), "2.9","3");
			if (count > 0) {
				out.write("{'info':'您已经提交过请求，请等待工作人员处理，如有特殊情况，请联系商工作人员'}");
				return null;
			}

			FinancingCost cost = financingCostService.getCostByFinancingBase(financingBase.getId());
			Account account = this.financingBase.getFinancier().getUser().getUserAccount();

			double realamount = ((int)(cost.getRealAmount()*100))/100;
			double balance = ((int)(account.getBalance()*100))/100;
			// 帐户可提现金额不足
			if ( realamount > balance ) {
				out.write("{'info':'帐户可用金额不足:帐户余额:"+balance+",提现额:"+realamount+"'}");
				return null;
			}

			// 前面的条件都通过了，可以进行提现了。
			// 生成一条提现申请
			this.accountDealService.financeCash(account.getId(), cost.getRealAmount(), financingBase);
			out.write("{'info':'申请已经提交，请等待结果处理'}");
			account = null;
			cost = null;
			user_object = null;
			user = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * tag terminal 以下代码，改变融资项目是否结束的状态 通过查询该融资项目下的还款记录中，是否有存在状态为未还款的记录
	 * 如果有，则什么也不做，如果没有 ，则表示该融资项目的所有还款已经结束，则改变该融资项目的terminal字段为true
	 */
	public String terminal() {
		try {
			if (this.financingBase == null || this.financingBase.isTerminal()) return null;
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			ServletOutputStream sos = response.getOutputStream();
			boolean result = this.financingBaseService.terminal(this.financingBase.getId());
			if(result) sos.print("{\"result\":\"success\"}");
			else sos.print("{\"result\":\"faild\"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String org_code;

	/**
	 * 打印成交报表
	 * 
	 * @return
	 */
	public String print_transactions_report() {
		
		
		this.checkDate();
		HttpServletRequest request = ServletActionContext.getRequest();
		ArrayList<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		
		String org_code2_str = request.getParameter("org_code2");
		String [] org_code2_arr = null;
		if(org_code2_str != null && !"".equals(org_code2_str) ){
			org_code2_str = org_code2_str.replaceAll(" ", "");
			if(org_code2_str.contains(",")){
				org_code2_arr = org_code2_str.split(",");
			}else{
				org_code2_arr = new String[]{org_code2_str};
			}
		}
		
		Org org = this.orgService.findOrg(this.org_code);
		
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();

		inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		inParamList.put(2, new java.sql.Date(endDateNext.getTime()));
		inParamList.put(3, this.org_code);
		inParamList.put(4, this.queryCode.toUpperCase());
		inParamList.put(5, this.keyWord);
		inParamList.put(6, org_code2_str);

		OutParameterModel outParameter = new OutParameterModel(7, OracleTypes.CURSOR);

		resultList = this.financingBaseService.callProcedureForList("P_FINANCER_FINANCE.transactions_report", inParamList, outParameter);

		List<Org> orglist = null;
		try {
			if(org_code2_arr != null && org_code2_arr.length > 0){
				StringBuffer sql_ = new StringBuffer();
				for(int x = 0; x< org_code2_arr.length ; x++){
					if(x!=0){sql_.append(",");}
					sql_.append("'"+org_code2_arr[x]+"'");
				}
				orglist = this.orgService.getScrollDataCommon("from Org o where o.showCoding in ("+sql_.toString()+")", new String[]{});
			}else{
				orglist = this.orgService.getScrollData().getResultlist();
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		
		
		request.setAttribute("org", org);
		request.setAttribute("org_code2", org_code2_str);
		request.setAttribute("orglist", orglist);
		request.setAttribute("list", resultList);

		return "print_transactions_report";
	}
	
	
	
	/**
	 * 导出excel成交报表
	 * 
	 * @return
	 */
	public String export_transactions_report() {
		
		
		this.checkDate();
		HttpServletRequest request = ServletActionContext.getRequest();
		ArrayList<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
		Date endDateNext = DateUtils.getAfter(this.getEndDate(), 1);
		
		String org_code2_str = request.getParameter("org_code2");
		String [] org_code2_arr = null;
		if(org_code2_str != null && !"".equals(org_code2_str) ){
			org_code2_str = org_code2_str.replaceAll(" ", "");
			if(org_code2_str.contains(",")){
				org_code2_arr = org_code2_str.split(",");
			}else{
				org_code2_arr = new String[]{org_code2_str};
			}
		}
		
		
		HashMap<Integer, Object> inParamList = new HashMap<Integer, Object>();

		inParamList.put(1, new java.sql.Date(this.getStartDate().getTime()));
		inParamList.put(2, new java.sql.Date(endDateNext.getTime()));
		inParamList.put(3, this.org_code);
		inParamList.put(4, this.queryCode.toUpperCase());
		inParamList.put(5, this.keyWord);
		inParamList.put(6, org_code2_str);

		OutParameterModel outParameter = new OutParameterModel(7, OracleTypes.CURSOR);

		resultList = this.financingBaseService.callProcedureForList("P_FINANCER_FINANCE.transactions_report", inParamList, outParameter);

		List<Org> orglist = null;
		try {
			if(org_code2_arr != null && org_code2_arr.length > 0){
				StringBuffer sql_ = new StringBuffer();
				for(int x = 0; x< org_code2_arr.length ; x++){
					if(x!=0){sql_.append(",");}
					sql_.append("'"+org_code2_arr[x]+"'");
				}
				orglist = this.orgService.getScrollDataCommon("from Org o where o.showCoding in ("+sql_.toString()+")", new String[]{});
			}else{
				orglist = this.orgService.getScrollData().getResultlist();
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		
		
		request.setAttribute("org_code2", org_code2_str);
		request.setAttribute("orglist", orglist);
		request.setAttribute("list", resultList);
		return "export_transactions_report";

	}

	/**
	 * 上传担保合同
	 * */
	public String  saveDbhtFile() throws Exception {
		if (image != null) { 
			UploadFile file = null;
			UploadFileUtils fileUtils = new UploadFileUtils(
					ServletActionContext.getServletContext());
			file = fileUtils.saveFile(image, imageFileName,
					imageContentType,
					IpAddrUtil.getIpAddr(ServletActionContext.getRequest()));
			file.setUploadtime(new Date());
			file.setEntityFrom("FinancingBase_dbht");
			file.setEntityId(this.financingBase.getCode());
			uploadFileService.insert(file); 
		}
		return "saveDbhtFile";
	}
	public String  delDbhtFile() throws Exception { 
			uploadFileService.delete(dbhtFileId); 
			return "delDbhtFile";
	}
	/**
	 * 某个融资项目的担保合同列表
	 * */
	public String  dbhtFileList() throws Exception {
		PageView<UploadFile> pageView = new PageView<UploadFile>(getShowRecord(), getPage());
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		StringBuilder sb = new StringBuilder(" 1=1 "); 
		List<String> params = new ArrayList<String>(); 
		sb.append("and o.entityFrom = 'FinancingBase_dbht' and o.entityId='" + this.financingBase.getCode() + "' ");
		if(null!=keyWord&&!"".equals(keyWord.trim())){

			keyWord = keyWord.trim(); 
			sb.append(" and (");
			sb.append(" o.name like ?");
			params.add("%"+keyWord+"%");
			sb.append(" or ");
			sb.append(" o.desc like ?"); 
			params.add("%"+keyWord+"%");
			sb.append(" )");
		} 
		pageView.setQueryResult(uploadFileService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(),sb.toString(), params,orderby));
		
		ServletActionContext.getRequest().setAttribute("pageView", pageView);
		return "dbhtFileList";
	}
	public String logs() throws Exception {
		List<Map<String, Object>> list = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		Object object = request.getParameter("_id");
		if(object==null || StringUtils.isBlank(object.toString())){
			return null;
		}
		String id = object.toString();
		list = this.financingBaseService.queryLogs(id);
		ServletActionContext.getRequest().setAttribute("list", list);
		return "logs";
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public void setInvestRecords(List<InvestRecord> investRecords) {
		this.investRecords = investRecords;
	}

	public List<InvestRecord> getInvestRecords() {
		return investRecords;
	}

	public List<CommonVo> getFxbzStateList() {
		if (fxbzStateList != null) return fxbzStateList;
		fxbzStateList = new ArrayList<CommonVo>();
		/*
		 * fxbzStateList.add(new CommonVo("1", "担保代偿")); fxbzStateList.add(new
		 * CommonVo("0", "风险补偿")); fxbzStateList.add(new CommonVo("2",
		 * "兴易贷保荐")); fxbzStateList.add(new CommonVo("5", "融宜通保荐"));
		 */
		fxbzStateList.add(new CommonVo("10", "无担保"));
		fxbzStateList.add(new CommonVo("15", "本息担保"));
		fxbzStateList.add(new CommonVo("12", "本金担保"));
		return fxbzStateList;
	}

	public void setFxbzStateList(List<CommonVo> fxbzStateList) {
		this.fxbzStateList = fxbzStateList;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public void setUser_type_flag(String user_type_flag) {
		this.user_type_flag = user_type_flag;
	}

	public String getUser_type_flag() {
		return user_type_flag;
	}

	public double getPrincipal_allah_monthly() {
		return principal_allah_monthly;
	}

	public double getInterest_allah_monthly() {
		return interest_allah_monthly;
	}

	public void setPrincipal_allah_monthly(double principalAllahMonthly) {
		principal_allah_monthly = principalAllahMonthly;
	}

	public void setInterest_allah_monthly(double interestAllahMonthly) {
		interest_allah_monthly = interestAllahMonthly;
	}

	public void setInterest_allah(double interest_allah) {
		this.interest_allah = interest_allah;
	}

	public double getInterest_allah() {
		return interest_allah;
	}

	public void setRepayment_amount_monthly_allah(double repayment_amount_monthly_allah) {
		this.repayment_amount_monthly_allah = repayment_amount_monthly_allah;
	}

	public double getRepayment_amount_monthly_allah() {
		return repayment_amount_monthly_allah;
	}

	public void setPrincipal_allah(double principal_allah) {
		this.principal_allah = principal_allah;
	}

	public double getPrincipal_allah() {
		return principal_allah;
	}

	public void setLoan_term(int loan_term) {
		this.loan_term = loan_term;
	}

	public int getLoan_term() {
		return loan_term;
	}

	public void setContractList(List<ContractKeyData> contractList) {
		this.contractList = contractList;
	}

	public List<ContractKeyData> getContractList() {
		return contractList;
	}

	public String getOpeNote() {
		return opeNote;
	}

	public void setOpeNote(String opeNote) {
		this.opeNote = opeNote;
	}

	public MemberGuarantee getMemberGuarantee() {
		return memberGuarantee;
	}

	public void setMemberGuarantee(MemberGuarantee memberGuarantee) {
		this.memberGuarantee = memberGuarantee;
	}

	public List<FinancingContract> getContracts() {
		return contracts;
	}

	public void setContracts(List<FinancingContract> contracts) {
		this.contracts = contracts;
	}

	public boolean isCreditor() {
		return creditor;
	}

	public void setCreditor(boolean creditor) {
		this.creditor = creditor;
	}


	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
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

	public String getQkeyWord() {
		return qkeyWord;
	}

	public void setQkeyWord(String qkeyWord) {
		this.qkeyWord = qkeyWord;
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getFeeFlag() {
		return feeFlag;
	}

	public void setFeeFlag(String feeFlag) {
		this.feeFlag = feeFlag;
	}

	public String getCreditFlag() {
		return creditFlag;
	}

	public void setCreditFlag(String creditFlag) {
		this.creditFlag = creditFlag;
	}

	public FinancingCost getFinancingCost() {
		return financingCost;
	}

	public void setFinancingCost(FinancingCost financingCost) {
		this.financingCost = financingCost;
	}

	public MemberBase getFinancier() {
		return financier;
	}

	public void setFinancier(MemberBase financier) {
		this.financier = financier;
	}
	

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getContainstr() {
		return containstr;
	}

	public void setContainstr(String containstr) {
		this.containstr = containstr;
	}

	public String getContractTemplateId() {
		return contractTemplateId;
	}

	public void setContractTemplateId(String contractTemplateId) {
		this.contractTemplateId = contractTemplateId;
	}

	public List<ContractTemplate> getCTemplates() {
		return cTemplates;
	}

	public void setCTemplates(List<ContractTemplate> templates) {
		cTemplates = templates;
	}

	public String getConTemplateId() {
		return conTemplateId;
	}

	public void setConTemplateId(String conTemplateId) {
		this.conTemplateId = conTemplateId;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	public String getDbhtFileId() {
		return dbhtFileId;
	}

	public void setDbhtFileId(String dbhtFileId) {
		this.dbhtFileId = dbhtFileId;
	}

	public String getGroupStr() {
		return groupStr;
	}

	public void setGroupStr(String groupStr) {
		this.groupStr = groupStr;
	}

	public List<CommonVo> getDkTypes() {
		return dkTypes;
	}

	public void setDkTypes(List<CommonVo> dkTypes) {
		this.dkTypes = dkTypes;
	}

	public List<CommonVo> getHyTypes() {
		return hyTypes;
	}

	public void setHyTypes(List<CommonVo> hyTypes) {
		this.hyTypes = hyTypes;
	}

	public List<CommonVo> getQyTypes() {
		return qyTypes;
	}

	public void setQyTypes(List<CommonVo> qyTypes) {
		this.qyTypes = qyTypes;
	}

	public String getQyType() {
		return qyType;
	}

	public void setQyType(String qyType) {
		this.qyType = qyType;
	}

	public String getQueryByOrgCode() {
		return queryByOrgCode;
	}

	public void setQueryByOrgCode(String queryByOrgCode) {
		this.queryByOrgCode = queryByOrgCode;
	}

	public String getFirstFlag() {
		return firstFlag;
	}

	public void setFirstFlag(String firstFlag) {
		this.firstFlag = firstFlag;
	}

	public String getAutoinvest() {
		return autoinvest;
	}

	public void setAutoinvest(String autoinvest) {
		this.autoinvest = autoinvest;
	} 
	

}
