package com.kmfex.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kmfex.model.BusinessType;
import com.kmfex.model.ContractTemplate;
import com.kmfex.model.MemberBase;
import com.kmfex.model.MemberGuarantee;
import com.kmfex.model.PreFinancingBase;
import com.kmfex.service.BusinessTypeService;
import com.kmfex.service.ContractTemplateService;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.service.MemberGuaranteeService;
import com.kmfex.service.PreFinancingBaseService;
import com.kmfex.webservice.vo.MessageTip;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Logs;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.model.UploadFile;
import com.wisdoor.core.model.User;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.DictionaryService;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.service.UploadFileService;
import com.wisdoor.core.service.UserService;
import com.wisdoor.core.symbol.UserTags;
import com.wisdoor.core.utils.BaseTool;
import com.wisdoor.core.utils.CloseSoftUtils;
import com.wisdoor.core.utils.Constant;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.vo.CommonVo;
import com.wisdoor.struts.BaseAction;

/**
 * 融资项目历史管理
 * 
 * @author
 * 
 *    申请中的资包 数据显示根据融资项目的融资方的开户人所属机构层次过滤
 *    利息方式 businessTypes列表中加入权限过滤
 *    修改驳回方法xybohui(),控制并发操作
 *     融资项目中“融资期限”选择加入排序功能,修改了c_ui(),xyUi(),ui()方法。
 *    修改了xyFinish(),xybohui()方法,控制并发,修改edit()方法控制只保留一条融资项目信息。
 *     修改了edit()发包增加贷款类型。
 *     修改了edit()融资方无法修改的问题
 *    修改了edit()融资项目按日计息生成编号的问题
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class PreFinancingBaseAction extends BaseAction implements Preparable {

	@Resource
	PreFinancingBaseService preFinancingBaseService;
	@Resource
	FinancingBaseService financingBaseService;
	@Resource
	BusinessTypeService businessTypeService;
	@Resource
	MemberBaseService memberBaseService;
	@Resource
	DictionaryService dictionaryService;
	@Resource
	UserService userService;
	@Resource
	UploadFileService uploadFileService;
	@Resource
	MemberGuaranteeService memberGuaranteeService;
	@Resource
	LogsService logsService;
	@Resource
	ContractTemplateService contractTemplateService;
	private String id;
	private PreFinancingBase preFinancingBase;
	private String keyWord = "";
	private String queryCode = "";
	private String queryOrg = "";
	private String queryState = "1";
	private String queryFinancier = "";
	private List<String> fileIds;
	private String financierId;
	private String guaranteeId;
	private String businessTypeId;
	private MemberGuarantee memberGuarantee = new MemberGuarantee();
	private List<UploadFile> files;
	private List<BusinessType> businessTypes;
	private List<CommonVo> fxbzStateList; //风险保障
	private String disabledStr="false";//担保方式是否可以修改
	
	private List<CommonVo> dkTypes;// 贷款分类
	private List<CommonVo> hyTypes;
	//企业类型(中型企业、小型企业、微型企业)
	private List<CommonVo> qyTypes=new ArrayList<CommonVo>();	
	
	private List<ContractTemplate> cTemplates;
	private String conTemplateId;	
	@SuppressWarnings("unchecked")
	public String ui() throws Exception {

		Map<String,String> menuMap =(Map<String,String>)ServletActionContext.getRequest().getSession().getAttribute(UserTags.MENUMAP); 
        String sqlDayLx="";
        if("none".equals(menuMap.get("preFinancingBase_DayLx"))){
        	sqlDayLx="  and c.id!='day' ";
        } 
		businessTypes = businessTypeService.getCommonListData("from BusinessType c where c.code = 'R'  "+sqlDayLx+"  order by c.order desc ");
		ServletActionContext.getRequest().setAttribute("listMapBts", initBusinessTypes(businessTypes));
		dkTypes=dictionaryService.getListByUrlKey("002");
 		hyTypes=dictionaryService.getListByUrlKey("001");	
 
		return "ui";
	}
	public Map<String,String>  initBusinessTypes(List<BusinessType> businessTypes) throws Exception {
		Map<String,String> listMap=new HashMap<String,String>();  
		for(BusinessType bt:businessTypes){
			if("day".equals(bt.getId())){
				listMap.put("按日计息", bt.getId()+"_"+bt.getReturnPattern()+"_按日计息");
			}else{
				String key=bt.getTerm()+"个月";
				boolean contains = listMap.containsKey(key); 
				if (contains) {//如果条件成立  
					listMap.put(key,listMap.get(key)+"@"+bt.getId()+"_"+bt.getReturnPattern()+"_"+key);
				}else {
					listMap.put(key, bt.getId()+"_"+bt.getReturnPattern()+"_"+key);
				}  
			} 
		}
 		return listMap;
	}
	
	@SuppressWarnings("unchecked")
	public String xyUi() throws Exception { 
		businessTypes = businessTypeService.getCommonListData("from BusinessType c where c.code = 'R'  order by c.order desc ");
		ServletActionContext.getRequest().setAttribute("listMapBts", initBusinessTypes(businessTypes));
		
		hyTypes=dictionaryService.getListByUrlKey("001");	 	
		/*if (null != this.preFinancingBase.getHyType() && !"".equals(this.preFinancingBase.getHyType())) {   
			String qyTypeTemp=this.preFinancingBase.getHyType().substring(this.preFinancingBase.getHyType().indexOf("(")+1, this.preFinancingBase.getHyType().length()-1);
	    	//this.qyTypes=dictionaryService.getListByUrlKey2(qyTypeTemp);  	
		}else{ 
			if(null!=hyTypes&&hyTypes.size()>0){
		    	//this.qyTypes=dictionaryService.getListByUrlKey2(hyTypes.get(0).getString1());  	
		    }	
		} */ 
		String codestr = this.preFinancingBase.getCode().substring(0, 1);
		
		
		if("day".equals(this.preFinancingBase.getBusinessType().getId())){//指定默认的模板
			codestr = "D";
		}else{
			codestr = "M";
		}
		
		
	
		cTemplates=this.contractTemplateService.getCommonListData("from ContractTemplate o  where 1=1 and o.code like '%"+codestr +"%' and not o.isOverDate='Y' " );  
		   
	    conTemplateId=this.preFinancingBase.getContractTemplate().getId();
		
		return "xyUi";
	}
	@SuppressWarnings("unchecked")
	public String c_ui() throws Exception {
		Map<String,String> menuMap =(Map<String,String>)ServletActionContext.getRequest().getSession().getAttribute(UserTags.MENUMAP); 
        String sqlDayLx="";
        if("none".equals(menuMap.get("preFinancingBase_DayLx"))){
        	sqlDayLx="  and c.id!='day' ";  
        } 
		businessTypes = businessTypeService.getCommonListData("from BusinessType c where c.code = 'R'   "+sqlDayLx +"  order by c.order desc ");
		ServletActionContext.getRequest().setAttribute("listMapBts", initBusinessTypes(businessTypes));
		dkTypes=dictionaryService.getListByUrlKey("002");
		hyTypes=dictionaryService.getListByUrlKey("001");	
		
		return "c_ui";
	}

	/**
	 * 信用确认
	 * 
	 * @return
	 * @throws Exception
	 */
	public String xyFinish() throws Exception {
		try {
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(null==this.preFinancingBase||null==financierId){ 
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控确认失败,参数错误");
				return "xyFinish";
			}
			if(null==this.preFinancingBase.getCode()||"".equals(this.preFinancingBase.getCode())){ 
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控确认失败,参数错误");
				return "xyFinish";
			}
			if(2==this.preFinancingBase.getState()){
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控确认失败,已经确认过");
				return "xyFinish";
			}
			if(3==this.preFinancingBase.getState()){
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控确认失败,已经有人驳回");
				return "xyFinish";
			} 
			if(5==this.preFinancingBase.getState()){
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控确认失败,融资项目已经废弃了");
				return "xyFinish";
			}
			BusinessType businessType =new BusinessType(businessTypeId);// this.businessTypeService.selectById(businessTypeId);
			this.preFinancingBase.setBusinessType(businessType);
			
			if (null != this.conTemplateId && !"".equals(conTemplateId.trim())) {
				this.preFinancingBase.setContractTemplate(contractTemplateService.selectById(conTemplateId));
			}  
			this.financingBaseService.xyFinish(u, this.preFinancingBase, financierId, guaranteeId);
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控确认成功，进入待审核");
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控确认失败");
		}
		return "xyFinish";
	}

	/**
	 * 信用驳回
	 * 
	 * @return
	 * @throws Exception
	 */
	public String xybohui() throws Exception {
		try {
 
			if(2==this.preFinancingBase.getState()){
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控驳回失败,风控已经确认通过");
				return "xybohui";
			}
			if(3==this.preFinancingBase.getState()){
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控驳回失败,已经驳回");
				return "xybohui";
			} 
			if(5==this.preFinancingBase.getState()){
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控驳回失败,融资项目已经废弃了");
				return "xybohui";
			}
			
			this.preFinancingBase.setState(3);
			this.preFinancingBaseService.update(this.preFinancingBase);
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控驳回成功");
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "风控驳回失败");
		}
		return "xybohui";
	}

	/**
	 * 融资废弃
	 * 
	 * @return
	 * @throws Exception
	 */
	public String stop() throws Exception {
		try {
			 
			this.preFinancingBase.setState(5);
			this.preFinancingBaseService.update(this.preFinancingBase);
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资废弃成功");
		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资废弃失败");
		}
		return "stop";
	}

	/**
	 * 新增
	 * 
	 * @return
	 * @throws Exception
	 */

	public String edit() throws Exception {   
		
		if (null != this.preFinancingBase && null != this.preFinancingBase.getNote() && this.preFinancingBase.getNote().contains("color:black;")) {
			this.preFinancingBase.setNote(this.preFinancingBase.getNote().replaceAll("color:black;", " "));
		}
		try {
			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			MemberBase financier = null;
			MemberBase guarantee = null;
			if (BaseTool.isNotNull(financierId)) {
				financier = new MemberBase(financierId);//this.memberBaseService.selectById(financierId);
			}
		//	if (BaseTool.isNotNull(guaranteeId)) {
		//		guarantee = new MemberBase(guaranteeId);//this.memberBaseService.selectById(guaranteeId);
		//	}
			
			if ("15".equals(this.preFinancingBase.getFxbzState())) {//无担保  
				this.preFinancingBase.setGuarantee(null); 
			}else{   
				if(null != guarantee){
					this.preFinancingBase.setGuarantee(guarantee);
					this.preFinancingBase.setMemberGuarantee(memberGuaranteeService.getLastByMemberGuarantee(guarantee.getId()));
					}  	
			}
			
			if (this.id == null || "".equals(this.id)) {
				ContractTemplate ct = null;
				
				
				
				//if ("1".equals(this.preFinancingBase.getFlag())) {
				//	this.preFinancingBase.setCode(this.preFinancingBaseService.buildPreFinancingCode("D")); 
                //    ct = contractTemplateService.selectByHql("from ContractTemplate where code = 'CA' order by version desc");
					
				//} else {
					
						if("10".equals(this.preFinancingBase.getFxbzState())) {//本金
							this.preFinancingBase.setCode(this.preFinancingBaseService.buildPreFinancingCode("A"));
						}
						if ("12".equals(this.preFinancingBase.getFxbzState())){//本息
							this.preFinancingBase.setCode(this.preFinancingBaseService.buildPreFinancingCode("B"));
						}  
						if ("15".equals(this.preFinancingBase.getFxbzState())) {//无担保
							this.preFinancingBase.setCode(this.preFinancingBaseService.buildPreFinancingCode("C"));
						} 	
						if("day".equals(businessTypeId)){//指定默认的模板
							ct = contractTemplateService.selectByHql("from ContractTemplate where code = 'D' order by version desc");
						}
				//}
				
				
 
				if (ct == null) {
					ct = contractTemplateService.selectByHql("from ContractTemplate where code = 'M' order by version desc");
				}
				this.preFinancingBase.setContractTemplate(ct);
				BusinessType businessType =new BusinessType(businessTypeId);// this.businessTypeService.selectById(businessTypeId);
				this.preFinancingBase.setBusinessType(businessType);

				this.preFinancingBase.setCreateBy(u);
				this.preFinancingBase.setCreateDate(new Date());
				if (null == this.preFinancingBase.getStartDate()) {
					this.preFinancingBase.setStartDate(new Date());
				}
				if (null == this.preFinancingBase.getEndDate()) {
					this.preFinancingBase.setEndDate(DateUtils.getAfter(new Date(), 1));
				}
				if (null != financier) this.preFinancingBase.setFinancier(financier);
 


				

				this.preFinancingBaseService.insert(this.preFinancingBase);
				
				Logs log = this.logsService.log("创建融资项目");// 日志
				log.setEntityId(this.preFinancingBase.getId());
				log.setEntityFrom("PreFinancingBase");
				this.logsService.insert(log);
				
				
				/*if (fileIds != null && fileIds.size() > 0) {
					for (String fileId : fileIds) {
						UploadFile uploadFile = uploadFileService.selectById(fileId);
						uploadFile.setEntityFrom("PreFinancingBase");
						uploadFile.setEntityId(this.preFinancingBase.getCode());
						uploadFile.setUseFlag("1");// 使用中
						uploadFileService.update(uploadFile);
					}
				}*/
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资项目申请成功");
			} else {  
				
				if(2==this.preFinancingBase.getState()){
					ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "修改操作失败,信用已经确认!");
					return "edit"; 
				}  
				 
				if(5==this.preFinancingBase.getState()){
					ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "修改操作失败,融资项目已经废弃!");
					return "edit"; 
				}
				
				
				/*
				PreFinancingBase newPreFinancingBase = new PreFinancingBase();
				newPreFinancingBase.setBusinessType(this.preFinancingBase.getBusinessType());
				newPreFinancingBase.setFxbzState(this.preFinancingBase.getFxbzState());

				if (null != financier) newPreFinancingBase.setFinancier(financier);
				
				if ("10".equals(this.preFinancingBase.getFxbzState())) {  
					newPreFinancingBase.setGuarantee(null); 
				}else{   
					if(null != guarantee){
						newPreFinancingBase.setGuarantee(guarantee);
						newPreFinancingBase.setMemberGuarantee(memberGuaranteeService.getLastByMemberGuarantee(guarantee.getId()));
 					}  	
				}
				 
				newPreFinancingBase.setContractTemplate(this.preFinancingBase.getContractTemplate());
				
				newPreFinancingBase.setFinancier(this.preFinancingBase.getFinancier());
				newPreFinancingBase.setShortName(this.preFinancingBase.getShortName());
				newPreFinancingBase.setStartDate(this.preFinancingBase.getStartDate());
				newPreFinancingBase.setEndDate(this.preFinancingBase.getEndDate());
				newPreFinancingBase.setCreateDate(new Date());
				newPreFinancingBase.setYongtu(this.preFinancingBase.getYongtu());
				BusinessType businessType = new BusinessType(businessTypeId);//this.businessTypeService.selectById(businessTypeId);
				newPreFinancingBase.setBusinessType(businessType);

				newPreFinancingBase.setFxbzState(this.preFinancingBase.getFxbzState());
				newPreFinancingBase.setNote(this.preFinancingBase.getNote());

				newPreFinancingBase.setCode(this.preFinancingBase.getCode());
 
				if("day".equals(businessTypeId)){ 
					newPreFinancingBase.setInterestDay(this.preFinancingBase.getInterestDay());//利息天数 
				}else{
					newPreFinancingBase.setInterestDay(0);//利息天数 
				}
				
				 
				
				newPreFinancingBase.setMaxAmount(this.preFinancingBase.getMaxAmount());
				newPreFinancingBase.setRate(this.preFinancingBase.getRate());
				newPreFinancingBase.setGuaranteeNote(this.preFinancingBase.getGuaranteeNote());
				newPreFinancingBase.setCreateBy(u);
				newPreFinancingBase.setCreateDate(new Date()); 
				this.preFinancingBaseService.insert(newPreFinancingBase);

				// 新增加的附件
				if (fileIds != null && fileIds.size() > 0) {
					for (String fileId : fileIds) {
						UploadFile uploadFile = uploadFileService.selectById(fileId);
						uploadFile.setEntityFrom("PreFinancingBase");
						uploadFile.setEntityId(newPreFinancingBase.getCode());
						uploadFile.setUseFlag("1");// 使用中
						uploadFileService.update(uploadFile);
					}
				}  
*/
				BusinessType businessType =new BusinessType(businessTypeId);
				this.preFinancingBase.setBusinessType(businessType);
				if (null != financier) this.preFinancingBase.setFinancier(financier); 
				this.preFinancingBase.setState(1);
				this.preFinancingBaseService.update(this.preFinancingBase);
				ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "融资项目修改成功");

			}

		} catch (Exception e) {
			e.printStackTrace();
			ActionContext.getContext().getSession().put(Constant.MESSAGETIP, "操作失败");
		}
		return "edit";
	}

	/**
	 * detail详细
	 * 
	 * @return
	 * @throws Exception
	 */
	public String detail() throws Exception {

		return "detail";
	}

	/**
	 * 融资项目申请历史列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception {
		try {
			PageView<PreFinancingBase> pageView = new PageView<PreFinancingBase>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("startDate", "desc"); 
			StringBuilder sb = new StringBuilder(" 1=1    and  o.state>0   "); // 最后一次的融资项目记录

			User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Org org = null;
			String orgCode = "";
			if (null != u) org = u.getOrg();
			if (null != org) {
				orgCode = org.getCoding();
			} else {
				return null;
			}

			/*
			 * if(com.kmfex.Constant.XYD_ORG_CODE.equals(org.getShowCoding())){//兴易贷
			 * 按融资方过滤 sb.append(" and
			 * o.financier.user.org.showCoding='"+com.kmfex.Constant.XYD_ORG_CODE+"'
			 * "); }else{
			 */
			// 其他按照创建者过滤
			
			List<String> params = new ArrayList<String>();
			
			if (null != orgCode && !"".equals(orgCode)) {
				sb.append(" and o.financier.user.org.coding like ? ");
				params.add("%" + orgCode + "%");
			} else {
				sb.append(" and o.financier.user.org.coding like '@@@@@@@%' ");
			}
			// }
			
			if (null != keyWord && !"".equals(keyWord.trim())) {
				keyWord = keyWord.trim();
				sb.append(" and  o.shortName like ?");
				params.add("%" + keyWord + "%");
			}
			if (null != queryCode && !"".equals(queryCode.trim())) {
				queryCode = queryCode.trim();
				sb.append(" and  o.code like ? ");
				params.add("%" + queryCode + "%");
			}
			if (null != queryOrg && !"".equals(queryOrg.trim())) {
				queryOrg = queryOrg.trim();
				sb.append(" and  ( ");
				sb.append("  o.createBy.org.shortName like  ? ");
				sb.append("  or o.createBy.org.showCoding like ? ");
				sb.append(" )");
				
				
				params.add("%" + queryOrg + "%");
				params.add("%" + queryOrg + "%");
				
			}
			if (null != queryFinancier && !"".equals(queryFinancier.trim())) {
				queryFinancier = queryFinancier.trim();
				sb.append(" and  ( ");
				sb.append("  o.financier.pName like ? ");
				sb.append("  or o.financier.eName like ? ");
				sb.append("  or o.financier.user.username like ? ");
				sb.append(" )");
				
				
				params.add("%" + queryFinancier + "%");
				params.add("%" + queryFinancier + "%");
				params.add("%" + queryFinancier + "%");
				
			} 
			if (!"99".equals(queryState.trim())) {
				queryState = queryState.trim();
				sb.append(" and  o.state = ? "); 
				params.add(queryState);
			} 
			pageView.setQueryResult(preFinancingBaseService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), sb.toString(), params, orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}

	public String getQueryCode() {
		return queryCode;
	}

	public void setQueryCode(String queryCode) {
		this.queryCode = queryCode;
	}

	public List<CommonVo> getFxbzStateList() {
		if (fxbzStateList != null) return fxbzStateList;
		fxbzStateList = new ArrayList<CommonVo>();		
		fxbzStateList.add(new CommonVo("10", "本息担保"));
		fxbzStateList.add(new CommonVo("12", "本金担保"));
		fxbzStateList.add(new CommonVo("15", "无担保"));
		return fxbzStateList;
	}

	public void setFxbzStateList(List<CommonVo> fxbzStateList) {
		this.fxbzStateList = fxbzStateList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public PreFinancingBase getPreFinancingBase() {
		return preFinancingBase;
	}

	public void setPreFinancingBase(PreFinancingBase preFinancingBase) {
		this.preFinancingBase = preFinancingBase;
	}

	public String getFinancierId() {
		return financierId;
	}

	public void setFinancierId(String financierId) {
		this.financierId = financierId;
	}

	public String getGuaranteeId() {
		return guaranteeId;
	}

	public void setGuaranteeId(String guaranteeId) {
		this.guaranteeId = guaranteeId;
	}

	@Override
	public void prepare() throws Exception {
		try {
			if (this.id == null || "".equals(this.id)) {
				this.preFinancingBase = new PreFinancingBase();
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Org org=user.getOrg();
				if(null!=org){//担保方式初始化
					if("0".equals(org.getType())){//保荐机构
						/*if(null!=org.getGuarantee()){
							this.preFinancingBase.setGuarantee(org.getGuarantee());
							this.preFinancingBase.setFxbzState("10");	 
						}else{
							this.preFinancingBase.setFxbzState("15");
						}*/
						disabledStr="true";
					}
				}
			} else {
				this.preFinancingBase = this.preFinancingBaseService.selectById(this.id);
				files = uploadFileService.getCommonListData("from UploadFile c where c.entityFrom = 'PreFinancingBase' and c.entityId='" + this.preFinancingBase.getCode() + "' ");
				for (UploadFile file : files) {
					file.setFrontId(file.getId().replaceAll("\\.", ""));
				}
				businessTypeId = this.preFinancingBase.getBusinessType().getId();
				if (null != this.preFinancingBase.getMemberGuarantee()) {
					memberGuarantee = this.preFinancingBase.getMemberGuarantee();
				}

				if (null == memberGuarantee) {
					memberGuarantee = new MemberGuarantee();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public MemberGuarantee getMemberGuarantee() {
		return memberGuarantee;
	}

	public void setMemberGuarantee(MemberGuarantee memberGuarantee) {
		this.memberGuarantee = memberGuarantee;
	}

	public String getDisabledStr() {
		return disabledStr;
	}

	public void setDisabledStr(String disabledStr) {
		this.disabledStr = disabledStr;
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
	public String getQueryOrg() {
		return queryOrg;
	}
	public void setQueryOrg(String queryOrg) {
		this.queryOrg = queryOrg;
	}
	public String getQueryState() {
		return queryState;
	}
	public void setQueryState(String queryState) {
		this.queryState = queryState;
	}
	public String getQueryFinancier() {
		return queryFinancier;
	}
	public void setQueryFinancier(String queryFinancier) {
		this.queryFinancier = queryFinancier;
	}
	public List<CommonVo> getQyTypes() {
		return qyTypes;
	}
	public void setQyTypes(List<CommonVo> qyTypes) {
		this.qyTypes = qyTypes;
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

	
	
}
