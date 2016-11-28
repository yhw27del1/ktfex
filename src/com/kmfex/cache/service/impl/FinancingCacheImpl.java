package com.kmfex.cache.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.cache.service.FinancingCacheService;
import com.kmfex.cache.utils.CacheManagerUtils;
import com.kmfex.cache.vo.Cache;
import com.kmfex.model.FinancingBase;
import com.kmfex.service.FinancingBaseService;
import com.kmfex.service.FinancingRestrainService;
import com.kmfex.service.UserGroupService;
import com.kmfex.util.ReadsStaticConstantPropertiesUtil;
import com.kmfex.webservice.vo.FinancingBaseVo;
import com.wisdoor.core.service.LogsService;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DateUtils;
import com.wisdoor.core.utils.DoubleUtils;

/**
 * 
 *  
 * 缓存融资项目信息
 * 2014年04月29日13:59   缓存修改了doingInvests方法 ,初始化时，只初始化未委托投标的
 */
@Service
@Transactional
public class FinancingCacheImpl extends BaseServiceImpl<FinancingBase> implements FinancingCacheService {
	
	@Resource FinancingBaseService financingBaseService;
	@Resource FinancingRestrainService financingRestrainService;
	@Resource UserGroupService userGroupService;
	@Resource LogsService logsService;	 
	public static final String  fields="select  id, code, shortname, maxamount, currenyamount, curcaninvest, rate, term, returnpattern, haveinvestnum, fxbzstate, fxbzstatename, startdate, enddate, qyzs, fddbzs, czzs, dbzs, zhzs, zhzsstar, qyzsnote, fddbzsnote, czzsnote, dbzsnote, zhzsnote, yongtu, address, industry, companyproperty, state_, statename, financierid, financiercode, financiername, guaranteeid, guaranteecode, guaranteename, logoname, guaranteenote, terminal, to_char(modifydate,'yyyy-MM-dd HH24:mi:ss') as modifydate, createdate, preinvest, interestday, businesstypeid ";
	 //缓存---待发布的、投标中的融资项目
	public  void doingInvests() throws Exception{
	   ArrayList<LinkedHashMap<String, Object>> dataList = financingBaseService.selectListWithJDBC(fields+ "  from v_finance_investlist o where o.state_ in ('1.5','2','3') and o.modifydate is not null and o.autoinvest='0'  order by o.modifydate desc  "); 
       List<FinancingBaseVo> invests = swithInvestList(dataList);
     
       Set<String> financingBaseCodes = new HashSet<String>();
       for(FinancingBaseVo fbv:invests){ 
/*       	if("1".equals(fbv.getPreInvest())){//开启了优先投标的融资项目
       		fbv.setUsers(getPreInvestUsers(fbv.getCode()));
       	}*/ 
       	//投标中的融资项目id 
       	financingBaseCodes.add(fbv.getCode()); 
       	//每个融资项目缓存
       	CacheManagerUtils.clearOnly("doing_"+fbv.getCode()); 
   		CacheManagerUtils.putCache("doing_"+fbv.getCode(), new Cache("doing_"+fbv.getCode(),fbv,Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()))));
       }  
       
   	  CacheManagerUtils.putCache("financingBaseCodes", new Cache("financingBaseCodes",financingBaseCodes,Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()))));
    
	} 
	 
	 
	//得到目前投标中的融资项目ids列表
	@SuppressWarnings("unchecked")
	public  Set<String> getFinancingBaseCodes() throws Exception{  
		 try {
			Object obj=CacheManagerUtils.getCacheInfo("financingBaseCodes").getValue();
			return (Set<String>)obj;  
		} catch (Exception e) {
			return null;
		} 
	
	}  
	
	//更新投标中的融资项目ids中的某个id
	public void updateFinancingBaseCode(String  financingBasecode) throws Exception {
		Set<String> financingBaseCodes=getFinancingBaseCodes();
		financingBaseCodes.add(financingBasecode);  
		CacheManagerUtils.clearOnly("financingBaseCodes"); 
   		CacheManagerUtils.putCache("financingBaseCodes", new Cache("financingBaseCodes",financingBaseCodes,Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()))));
    }	
	
	
	//删除的融资项目列表ids中的某个id
	public void deleteFinancingBaseCode(String  financingBasecode) throws Exception {
		Set<String> financingBaseCodes=getFinancingBaseCodes();
		financingBaseCodes.remove(financingBasecode);  
		CacheManagerUtils.clearOnly("financingBaseCodes"); 
   		CacheManagerUtils.putCache("financingBaseCodes", new Cache("financingBaseCodes",financingBaseCodes,Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()))));
    }	
	
	
	public void updateFinancingCache(FinancingBaseVo vo) throws Exception {
		CacheManagerUtils.clearOnly("doing_"+vo.getCode());  
   		CacheManagerUtils.putCache("doing_"+vo.getCode(), new Cache("doing_"+vo.getCode(),vo,Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()))));
    }	
	
	
	public void deleteFinancingCache(String financingBaseCode) throws Exception { 
		CacheManagerUtils.clearOnly("doing_"+financingBaseCode); 
	}	
	
	
	//初步只缓存最近20天的结束的融资项目信息
    //缓存投标结束的包(4已满标   5融资确认已完成  6费用核算完成  7签约完成 8已撤单)
	public  void stopInvests() throws Exception{  
		List<FinancingBaseVo> stopInvests=new ArrayList<FinancingBaseVo>();
		try {
			Date date =DateUtils.getAfter(new Date(), -10);
			ArrayList<LinkedHashMap<String, Object>> dataList = financingBaseService.selectListWithJDBC(fields+ "   from v_finance_investlist o where o.state_ in ('4','5','6','7','8')   and  o.modifydate > = to_date('"+new SimpleDateFormat("yyyy-MM-dd").format(date)+"','yyyy-MM-dd') and o.modifydate is not null  order by o.modifydate desc ");   
			stopInvests = swithInvestList(dataList);
		} catch (Exception e) { 
			e.printStackTrace();  
		} 
		CacheManagerUtils.clearOnly("stopInvests");  
	    CacheManagerUtils.putCache("stopInvests", new Cache("stopInvests",stopInvests,Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()))));
   } 
	
	//组成员变动，更新投标中的融资项目缓存
	public  void updateDoingFinancingGroupByGroupId(String groupId,String userName){   
		try {
			String codes=financingRestrainService.getFinancingCodesByGroup(groupId);
			if(null!=codes&&!"".equals(codes)){
				String str[]=stringAnalytical(codes,",");
				for(String s:str){ 
					//更新融资项目缓存  
					FinancingBase fb=this.financingBaseService.selectByHql("from FinancingBase f where f.code='" + s + "'"); 
					try { ReadsStaticConstantPropertiesUtil.updateServiceCache(fb.getId()); } catch (Exception e) {} 
				} 
			}
		} catch (Exception e) { }
	} 
	
	
	// 返回字符串数组   
    private static String[] stringAnalytical(String str, String divisionChar) {   
        String string[];   
        int i = 0;  
        if(null==str) return null;
        StringTokenizer tokenizer = new StringTokenizer(str, divisionChar);   
        string = new String[tokenizer.countTokens()];// 动态的决定数组的长度   
         while (tokenizer.hasMoreTokens()) {   
            string[i] = new String();   
            string[i] = tokenizer.nextToken();   
            i++;   
        }   
        return string;
    } 
	//融资项目下的用户
	public  Set<String> getPreInvestUsers(String code) throws Exception{ 
		   Set<String> users = new HashSet<String>();
			StringBuilder sb = new StringBuilder(); 
			sb.append(" tg.groupid_=tr.groupid_ and su.id=tg.user_id "); 
			sb.append(" and tr.financingcode='"+code+"'");
			sb.append(" group by su.username "); 
			try {
				List<Map<String,Object>> list = this.queryForList("su.username as username", 
						" T_USERGROUP tg,t_FinancingRestrain tr,Sys_user su ", sb.toString()); 
				for(Map<String,Object>  map:list)
				   if(map.get("username") != null){
					users.add(map.get("username").toString()); 
				}
			} catch (Exception e) {  
				
			}
/*		 List<FinancingRestrain> frs=financingRestrainService.getCommonListData("from FinancingRestrain o where o.financingCode='"+code+"' ");  
	     for(FinancingRestrain restrain:frs){ 
			 List<UserGroup> groups=userGroupService.getCommonListData("from UserGroup o where o.groupId='"+restrain.getGroupId()+"'"); 
			 for(UserGroup g:groups) {
				users.add(g.getUser().getUsername());  
			 }	  
		 }  */
	     return users;
	
	}  
	 
	private  List<FinancingBaseVo> swithInvestList(ArrayList<LinkedHashMap<String, Object>> dataList) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<FinancingBaseVo> financingBaseVoS = new ArrayList<FinancingBaseVo>();
		for (int i = 0; i < dataList.size(); i++) {
			FinancingBaseVo vo = new FinancingBaseVo();
			vo.setCode(dataList.get(i).get("code").toString());
			vo.setId(dataList.get(i).get("id").toString());
			vo.setShortName(dataList.get(i).get("shortname").toString());
			vo.setRate(Double.valueOf(dataList.get(i).get("rate").toString()));
			vo.setHaveInvestNum(Integer.parseInt(dataList.get(i).get("haveinvestnum").toString()));
			if (null != dataList.get(i).get("address")){
				vo.setAddress(dataList.get(i).get("address").toString());
			} 
			vo.setFxbzState(dataList.get(i).get("fxbzstate").toString());
			
			if (null == dataList.get(i).get("companyproperty") || "null".equals(dataList.get(i).get("companyproperty")) || "".equals(dataList.get(i).get("companyproperty"))) {
				vo.setCompanyProperty("-");
			} else {
				vo.setCompanyProperty(dataList.get(i).get("companyproperty").toString());
			}
			if (null == dataList.get(i).get("industry") || "null".equals(dataList.get(i).get("industry")) || "".equals(dataList.get(i).get("industry"))) {
				vo.setIndustry("-");
			} else {
				vo.setIndustry(dataList.get(i).get("industry").toString());
			}
			if (null == dataList.get(i).get("logoname") || "null".equals(dataList.get(i).get("logoname")) || "".equals(dataList.get(i).get("logoname"))) {
				vo.setLogoName("-");
			} else {
				if("10".equals(vo.getFxbzState())){//无担保
					vo.setLogoName("-");
				}else{
					vo.setLogoName(dataList.get(i).get("logoname").toString());
				}
				
			}

			if (null == dataList.get(i).get("guaranteenote") || "null".equals(dataList.get(i).get("guaranteenote")) || "".equals(dataList.get(i).get("guaranteenote"))) {
				vo.setGuaranteeNote("-");
			} else {
				vo.setGuaranteeNote(dataList.get(i).get("guaranteenote").toString());
			}

			if (null != dataList.get(i).get("startdate")) vo.setStartDate(format.parse(dataList.get(i).get("startdate").toString()));
			if (null != dataList.get(i).get("enddate")) vo.setEndDate(format.parse(dataList.get(i).get("enddate").toString()));
			if (null != dataList.get(i).get("modifydate")){
				try {
					vo.setModifyDate(format_time.parse(dataList.get(i).get("modifydate").toString()));
				} catch (RuntimeException e) {  
					e.printStackTrace();
					System.out.println(dataList.get(i).get("modifydate")+"--FinancingCache----code="+vo.getCode());  
 					vo.setModifyDate(DateUtils.getAfter(new Date(), 1));
				}
			} 
			if (null != dataList.get(i).get("preinvest")) vo.setPreInvest(dataList.get(i).get("preinvest").toString());
			
			 
			
			vo.setState(dataList.get(i).get("state_").toString());
			Double currenyamount = Double.valueOf(dataList.get(i).get("currenyamount").toString());
			Double maxamount = Double.valueOf(dataList.get(i).get("maxamount").toString());
			Double curcaninvest = Double.valueOf(dataList.get(i).get("curcaninvest").toString());
			if ("5".equals(vo.getState()) || "6".equals(vo.getState()) || "7".equals(vo.getState())) {
				vo.setProGress("100%");
			} else {
				vo.setProGress((DoubleUtils.doubleCheck((currenyamount / maxamount) * 100, 2)) + "%");
			}
			if (null != dataList.get(i).get("financiercode")){
				vo.setFinancierCode(dataList.get(i).get("financiercode").toString());
			} 
			if (null != dataList.get(i).get("financierid")){
				vo.setFinancierId(dataList.get(i).get("financierid").toString());
			} 
			if (null != dataList.get(i).get("financiername")){
				vo.setFinancierName(dataList.get(i).get("financiername").toString());
			} 
			 
			if (null != dataList.get(i).get("yongtu")) vo.setYongtu(dataList.get(i).get("yongtu").toString());

			
			vo.setFxbzStateName(dataList.get(i).get("fxbzstatename").toString());
		  
			String businessTypeId=dataList.get(i).get("businesstypeid").toString();
            if("day".equals(businessTypeId)){    
            	vo.setTerm(1);  
            	vo.setInterestDay(Integer.parseInt(dataList.get(i).get("interestday").toString()));
            	vo.setTermStr(vo.getInterestDay()+"天");
            	vo.setReturnPattern(dataList.get(i).get("returnpattern").toString());
            }else{
            	vo.setTerm(Integer.parseInt(dataList.get(i).get("term").toString()));
            	vo.setTermStr(vo.getTerm()+"个月");
    			vo.setReturnPattern(dataList.get(i).get("returnpattern").toString());
            }
			
			
			vo.setMaxAmount(maxamount);

			if (null != dataList.get(i).get("qyzs")) vo.setQyzs(dataList.get(i).get("qyzs").toString());
			if (null != dataList.get(i).get("qyzsnote")) vo.setQyzsNote(dataList.get(i).get("qyzsnote").toString());
			if (null != dataList.get(i).get("fddbzs")) vo.setFddbzs(dataList.get(i).get("fddbzs").toString());
			if (null != dataList.get(i).get("fddbzsnote")) vo.setFddbzsNote(dataList.get(i).get("fddbzsnote").toString());
			if (null != dataList.get(i).get("czzs")) vo.setCzzs(dataList.get(i).get("czzs").toString());
			if (null != dataList.get(i).get("czzsnote")) vo.setCzzsNote(dataList.get(i).get("czzsnote").toString());
			if (null != dataList.get(i).get("dbzs")) vo.setDbzs(dataList.get(i).get("dbzs").toString());
			if (null != dataList.get(i).get("dbzsnote")) vo.setDbzsNote(dataList.get(i).get("dbzsnote").toString());
			if (null != dataList.get(i).get("zhzs")) vo.setZhzs(dataList.get(i).get("zhzs").toString());
			if (null != dataList.get(i).get("zhzsnote")) vo.setZhzsNote(dataList.get(i).get("zhzsnote").toString());

			if (null != dataList.get(i).get("guaranteecode")) vo.setGuaranteeCode(dataList.get(i).get("guaranteecode").toString());
			if (null != dataList.get(i).get("guaranteeid")) vo.setGuaranteeId(dataList.get(i).get("guaranteeid").toString());
			if (null != dataList.get(i).get("guaranteename")) vo.setGuaranteeName(dataList.get(i).get("guaranteename").toString());

			vo.setState(dataList.get(i).get("state_").toString());
			vo.setStateName(dataList.get(i).get("statename").toString());// 状态显示名称
			vo.setCurCanInvest(curcaninvest);
			vo.setCurrenyAmount(currenyamount);

			vo.setZhzsStar(dataList.get(i).get("zhzsstar").toString());
			
			
	       	if("1".equals(vo.getPreInvest())){//开启了优先投标的融资项目
	       		vo.setUsers(getPreInvestUsers(vo.getCode()));
	       	}
	       	
			financingBaseVoS.add(vo);
			
			

		}
		return financingBaseVoS;

	}
	
	public  FinancingBaseVo swithInvestList(String financingBaseId) throws Exception { 
		ArrayList<LinkedHashMap<String, Object>> dataList = financingBaseService.selectListWithJDBC(fields+ "   from v_finance_investlist o where o.id='"+financingBaseId+"' and o.modifydate is not null  "); 
        List<FinancingBaseVo> yxzInvests = swithInvestList(dataList);
        if(null!=yxzInvests&&yxzInvests.size()>0){
        	return yxzInvests.get(0);
        }else{
        	return null;
        }
		
	}

	/**
	 * 查数据库更新缓存方法-
	 * @param financingBaseId
	 */
	public void updateOneFinancingCache(String financingBaseId){
		/**投标记录成功,更新缓存开始***/  
		try {
			FinancingBaseVo baseVo=this.swithInvestList(financingBaseId); 
			Double state=Double.valueOf(baseVo.getState());
			if (state>=4) {// 当前投标额等于融资的剩余融资额，说明当前投标为最后一次投标 
				 //更新结束的融资项目缓存
				this.deleteFinancingCache(baseVo.getCode());
				try {
					this.deleteFinancingBaseCode(baseVo.getCode());
				} catch (Exception e) {}
				this.stopInvests(); 
			}else{
				//更新融资项目缓存 
				Set<String> financingBaseCodes = getFinancingBaseCodes();
				financingBaseCodes.add(baseVo.getCode());
				CacheManagerUtils.putCache("financingBaseCodes", new Cache("financingBaseCodes",financingBaseCodes,Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()))));
			 
				if("1".equals(baseVo.getPreInvest())){
					Set<String> users=getPreInvestUsers(baseVo.getCode());
					baseVo.setUsers(users); 
				}   
				this.updateFinancingCache(baseVo);     
			}
		} catch (Exception e) { 
			//e.printStackTrace();
		}
		/**更新缓存结束***/ 
	}


}
