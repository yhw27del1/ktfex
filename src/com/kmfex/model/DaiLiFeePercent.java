package com.kmfex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
 

@Entity 
@Table(name="t_dailifei_percent",schema="KT")
public class DaiLiFeePercent     implements java.io.Serializable{  
 
	private static final long serialVersionUID = -44008554898660275L;

	/**ID**/
	private String id;
	
	/**显示名称**/
	private String showName;
	
	/**签约机构编码**/
	private String orgshowCoding;
	
	/**签约代理费核算类型**/
	private String type;
	
	/**分配比例**/
	private String allocationProportion ;
	
	/**每月核算标准**/
	private String checkStandard;
	
	/**一月期比率**/
	private Double mouthForOne;
	
	/**三月期比率**/	
	private Double mouthForThree;
	
	/**六月期比率**/
	private Double mouthForSix;
	
	/**九月期比率**/
	private Double mouthForNine;
	
	/**十二月期比率**/
	private Double mouthForTwelve;
	
	/**合同起始**/
	private Date startDate;
	
	/**合同到期**/
	private Date endDate;
	
	/**负责人**/
	private String director;
	
	/**区域**/
	private String area;
	
	/**业务类型**/
	private String businessType;
	
	/**渠道负责人**/
	private String channelDirector;
	
	/**席位费标准**/
	private String seatFee;
	
	/**状态**/
	private String State;
	
	/**备注**/
	private String note;
	
	public DaiLiFeePercent() {
		// TODO Auto-generated constructor stub
	}

	
	public DaiLiFeePercent(String id, String orgshowCoding, String type,
			String allocationProportion, String checkStandard, Date startDate,
			Date endDate, String director, String area, String businessType,
			String channelDirector) {
		super();
		this.id = id;
		this.orgshowCoding = orgshowCoding;
		this.type = type;
		this.allocationProportion = allocationProportion;
		this.checkStandard = checkStandard;
		this.startDate = startDate;
		this.endDate = endDate;
		this.director = director;
		this.area = area;
		this.businessType = businessType;
		this.channelDirector = channelDirector;
	}

	

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getOrgshowCoding() {
		return orgshowCoding;
	}
	public void setOrgshowCoding(String orgshowCoding) {
		this.orgshowCoding = orgshowCoding;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAllocationProportion() {
		return allocationProportion;
	}
	public void setAllocationProportion(String allocationProportion) {
		this.allocationProportion = allocationProportion;
	}
	public String getCheckStandard() {
		return checkStandard;
	}
	public void setCheckStandard(String checkStandard) {
		this.checkStandard = checkStandard;
	}
	public Double getMouthForOne() {
		return mouthForOne;
	}
	public void setMouthForOne(Double mouthForOne) {
		this.mouthForOne = mouthForOne;
	}
	public Double getMouthForThree() {
		return mouthForThree;
	}
	public void setMouthForThree(Double mouthForThree) {
		this.mouthForThree = mouthForThree;
	}
	public Double getMouthForSix() {
		return mouthForSix;
	}
	public void setMouthForSix(Double mouthForSix) {
		this.mouthForSix = mouthForSix;
	}
	public Double getMouthForNine() {
		return mouthForNine;
	}
	public void setMouthForNine(Double mouthForNine) {
		this.mouthForNine = mouthForNine;
	}
	public Double getMouthForTwelve() {
		return mouthForTwelve;
	}
	public void setMouthForTwelve(Double mouthForTwelve) {
		this.mouthForTwelve = mouthForTwelve;
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
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getChannelDirector() {
		return channelDirector;
	}
	public void setChannelDirector(String channelDirector) {
		this.channelDirector = channelDirector;
	}
	public String getSeatFee() {
		return seatFee;
	}
	public void setSeatFee(String seatFee) {
		this.seatFee = seatFee;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	
	@Column(name="note_",length=500)
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
 
 
}
