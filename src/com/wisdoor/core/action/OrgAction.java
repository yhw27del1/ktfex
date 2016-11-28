package com.wisdoor.core.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.Constant;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.model.Org;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.service.OrgService;
import com.wisdoor.core.utils.DoResultUtil;
import com.wisdoor.core.vo.CommonVo;
import com.wisdoor.struts.BaseAction;
/*** 
 * 中心机构类
 * @author   

 * 修改记录
 * list方法 显示所有机构
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class OrgAction extends BaseAction implements Preparable {
	@Resource OrgService orgService;  
	private long id; 
	private long parentId=1l; 
	private long parent_id;
	private Org org; 
	private String keyWord = "";  
	private String showCoding="";
	
	//机构类型
	private List<CommonVo> orgtypeList;
	
	public void prepare() throws Exception {
      if(0!=id) {
    	   org=orgService.selectById(id);
       }else{
    	   org=new Org();
       }
	}    
	
	/**跳转页面**/
	public String ui() throws Exception { 
		try {
 		} catch (Exception e) { 
			e.printStackTrace();
		}
		return "ui"; 
	} 
	public String list() throws Exception {
		try {
			PageView<Org> pageView = new PageView<Org>(getShowRecord(), getPage());
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put("id", "asc");
			StringBuilder sb = new StringBuilder(" 1=1 and o.parent is not null "); 
			List<String> params = new ArrayList<String>(); 
			if(null!=keyWord&&!"".equals(keyWord.trim())){ 
				keyWord = keyWord.trim(); 
				sb.append(" and (");
				sb.append(" o.name like ?");
				params.add("%"+keyWord+"%"); 
				sb.append(" )");
	 		} 
			pageView.setQueryResult(orgService.getScrollData(sb.toString(), params,orderby));
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
        return "list";
	}
	/**新增修改**/
	public String edit() throws Exception {
		try { 
			if(0!=parent_id) { 
				  org.setParent(orgService.selectById(parent_id));//设置父机构
			} 
			if(0!=id) { 
				if(null!=org.getParent())
				{
				    org.setCoding(org.getParent().getCoding()+"m"+id);//编码
				}
			    orgService.update(org); 
			}else{  
				orgService.insert(org);
				if(null!=org.getParent())
				{
					org.setCoding(org.getParent().getCoding()+"m"+org.getId());//编码
				    orgService.update(org); 
				}
			}
			 
		} catch (Exception e) { 
			e.printStackTrace(); 
 		}   
		return "edit"; 
	} 
	
	
	/**删除**/
	public String del() throws Exception {
		try {  
			List<Org> orgList = orgService.getCommonListData(" from Org o where o.coding like '"+org.getCoding()+"%'");
			for(Org o:orgList)
			{
			  orgService.delete(o.getId()); 
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		return "del";
	}
	 
	public String selectTree() throws Exception{ 
		List<Org> orgList = orgService.getScrollData(" o.parent is not null ").getResultlist(); 
		Map<String, Object> mapNode = new LinkedHashMap<String, Object>();
		Map<String, Object> mapAttr = new LinkedHashMap<String, Object>();
		Map<String, Object> mapData = new LinkedHashMap<String, Object>(); 
		mapAttr.put("id", parentId);
		Org orgRoot=orgService.selectByHql("from Org o where o.parent is  null ");
		if(null!=orgRoot)
		     { mapData.put("title", orgRoot.getName()); }
		else{
			 { mapData.put("title", Constant.ORGROOTNAME); }
		}
		mapNode.put("attributes", mapAttr);
		mapNode.put("data", mapData);
		mapNode.put("state", "open"); 
		List<Map<String, Object>> subNodes = getSubNodes(parentId, orgList);
		mapNode.put("children", subNodes);
		List<Map<String, Object>> root = new ArrayList<Map<String, Object>>();
		root.add(mapNode);
		JSONArray jsonArray = JSONArray.fromObject(root);
		String content = jsonArray.toString(); 
		DoResultUtil.doStringResult(ServletActionContext.getResponse(),content);
		return null;  
	} 
	
	public List<Map<String, Object>> getSubNodes(long id,List<Org> orgList) {
		List<Map<String, Object>> subNodes = new ArrayList<Map<String, Object>>();
		for (Org org : orgList) {
			if (org.getParent().getId() == id) { 
				Map<String, Object> mapNode = new LinkedHashMap<String, Object>();
				Map<String, Object> mapAttr = new LinkedHashMap<String, Object>();
				Map<String, Object> mapData = new LinkedHashMap<String, Object>();
				mapAttr.put("id", org.getId());
				mapData.put("title", org.getName());
				mapNode.put("attributes", mapAttr);
				mapNode.put("data", mapData);
				mapNode.put("state", "open"); 
				mapNode.put("children", getSubNodes(org.getId(), orgList));
				subNodes.add(mapNode);  
			}
		}
		return subNodes;

	}
	/**验证机构编码是否存在**/
	public String validateOrg() throws Exception {
		try {  
			Org orgTemp=orgService.findOrg(showCoding); 
			if(orgTemp == null){ 
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),"0"); 
			}else{ 
				DoResultUtil.doStringResult(ServletActionContext.getResponse(),"1"); 
			} 
		} catch (Exception e) {  
			e.printStackTrace(); 
		} 
		return null;
	}
	  
	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
 
	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parentId) {
		parent_id = parentId;
	}

	public String getShowCoding() {
		return showCoding;
	}

	public void setShowCoding(String showCoding) {
		this.showCoding = showCoding;
	}

	public List<CommonVo> getOrgtypeList() {
		if (orgtypeList != null)
			return orgtypeList;
		orgtypeList = new ArrayList<CommonVo>();
		orgtypeList.add(new CommonVo("0", "担保机构"));
		orgtypeList.add(new CommonVo("1", "投资服务中心"));
		orgtypeList.add(new CommonVo("2", "担保+投资服务中心"));
		return orgtypeList;
	}

	public void setOrgtypeList(List<CommonVo> orgtypeList) {
		this.orgtypeList = orgtypeList;
	}
  
	
}
