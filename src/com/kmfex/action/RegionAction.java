package com.kmfex.action;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.kmfex.model.Region;
import com.kmfex.service.RegionService;
import com.opensymphony.xwork2.Preparable;
import com.wisdoor.core.page.PageView;
import com.wisdoor.core.page.QueryResult;
import com.wisdoor.struts.BaseAction;
/**
 * 地点action
 * @author eclipse
 *
 */
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class RegionAction extends BaseAction implements Preparable{
	@Resource RegionService regionService;
	private String region_id;
	private String region_parent_id;
	private List<Region> regions;
	private Region region;
	private boolean tag;
	public String list() throws Exception{
		try {
			PageView<Region> pageView = new PageView<Region>(getShowRecord(), getPage());
			QueryResult<Region> result = regionService.getScrollData(pageView.getFirstResult(), pageView.getMaxresult(), " areaparentcode = '0000' order by areacode", null);
			for(Region r : result.getResultlist()){
				List<Region> children = regionService.getScrollDataCommon("from Region where areaparentcode = '"+r.getAreacode()+"'", new String[]{});
				r.setChildren(children);
			}
			pageView.setQueryResult(result);
			ServletActionContext.getRequest().setAttribute("pageView", pageView);
//			regions = regionService.getCommonListData("from Region where areaparentcode = '0000' order by areacode");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "list";
	}
	
	public String getchildren() throws Exception{
		if(region_id!=null){			
			regions = regionService.getScrollDataCommon("from Region where areaparentcode = '"+region_id+"' order by areacode", new String[]{});
			if(!regions.isEmpty()){
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				PrintWriter out = response.getWriter();
				out.write("<option value=''>请选择</option>");
				for(Region r : regions){
					out.write("<option value='"+r.getAreacode()+"'>"+r.getAreaname_s()+"</option>");
				}
				out.flush();
				out.close();
			}
		}
		return null;
	}
	/**
	 * 设置启用状态
	 * @return
	 * @throws Exception
	 */
	public String settag() throws Exception{
		try{
			if(this.region!=null){
				List<Region> children = this.regionService.getScrollDataCommon("from Region where areaparentcode = '"+this.region.getAreacode()+"'", new String[]{});
				for(Region r :children){
					if(this.tag){
						this.region.setEnabled(true);
						r.setEnabled(true);
					}else{
						this.region.setEnabled(false);
						r.setEnabled(false);
					}
					this.regionService.update(r);
				}
				this.regionService.update(this.region);
				
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.getWriter().write("success");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String ui() throws Exception{
		return "ui";
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception{
		if(this.region_id==null || "".equals(this.region_id)){
			if(this.region_parent_id!=null && !"".equals(this.region_parent_id)){
				Region parent = this.regionService.selectById(this.region_parent_id);
				this.region.setAreaparentcode(parent.getAreacode());
			}else{
				this.region.setAreaparentcode("0000");
			}
			this.regionService.insert(this.region);
		}else{
			this.regionService.update(this.region);
		}
		return "edit";
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String del() throws Exception{
		try {
			if(this.region.getId()!=null && !"".equals(this.region.getId())){
				List<Region> children = this.regionService.getScrollDataCommon(" from Region where areaparentcode = '"+this.region.getAreacode()+"'", new String[]{});
				if(!children.isEmpty())
				for(Region r : children){
					this.regionService.delete(r.getId());
				}
				this.regionService.delete(this.region.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "del";
	}

	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	public String getRegion_id() {
		return region_id;
	}


	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}

	public List<Region> getRegions() {
		return regions;
	}

	@Override
	public void prepare() throws Exception {
		if(this.region_id!=null){
			this.region = this.regionService.selectById(this.region_id);
		}else{
			this.region= new Region();
		}
		
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Region getRegion() {
		return region;
	}

	public void setTag(boolean tag) {
		this.tag = tag;
	}

	public boolean isTag() {
		return tag;
	}

	public void setRegion_parent_id(String region_parent_id) {
		this.region_parent_id = region_parent_id;
	}

	public String getRegion_parent_id() {
		return region_parent_id;
	}
	
	
}
