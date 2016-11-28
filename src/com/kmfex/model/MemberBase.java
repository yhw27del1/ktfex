package com.kmfex.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.UploadFile;
import com.wisdoor.core.model.User;

/**
 * 会员基本信息实体类
 * 
 * @author 敖汝安
 * @version 2012-04-01
 * 
 * 修改记录
 *   2012-07-30 增加辅助字段showName解决pName、eName问题
 *   2012-08-24 增加字段yearFeeDate（缴费时间）、yearFeeStartDate (开始时间)、yearFeeEndDate（结束时间）、
        yearFeeNote（核算日志）
 *   2013-05-17 增加字段creditFeeDate（缴费时间）、creditFeeStartDate (信用管理费开始时间)、creditFeeEndDate（信用管理费结束时间）、
        creditFeeNote（信用管理费核算日志）
 * */
@Entity
@Table(name = "t_member_base", schema = "KT")
public class MemberBase implements Serializable {
	/**
	 *    
	 */
	private static final long serialVersionUID = 4683014439862315854L;
	/**
	 * 会员状态：未审核，即此会员刚开通
	 * */
	public final static String STATE_NOT_AUDIT = "1";
	/**
	 * 会员状态：正常，即已审核通过，并在使用中
	 * */
	public final static String STATE_PASSED_AUDIT = "2";
	/**
	 * 会员状态：未通过审核
	 * */
	public final static String STATE_NOT_PASS_AUDIT = "3";

	/**
	 * 会员状态：已停用
	 * */
	public final static String STATE_DISABLED = "4";
	
	/**
	 * 会员状态：已注销
	 * */
	public final static String STATE_STOPPED = "5";

	/**
	 * 会员类别：机构
	 * */
	public final static String CATEGORY_ORG = "0";

	/**
	 * 会员类别：个人会员
	 * */
	public final static String CATEGORY_PERSON = "1";

	/** 会员的id号 */
	public String id;

	/**
	 * 此会员的用户
	 * */
	public User user;

	/**
	 * 开户人：为此会员开户的User对象
	 * */
	public User creator;

	/**
	 * 最经一次资料变更人：最近一次为此会员变更资料的User对象
	 * */
	public User changer;

	/** 开户机构编码(服务中心) */
	private String orgNo;

	/**
	 * 会员类别：0，企业；1，个人
	 * */
	public String category;

	/** 会员类型 **/
	public MemberType memberType;

	/** 所在省 */
	public String province;

	/**
	 * 所在省名
	 * */
	public String provinceName;

	/** 所在市 */
	public String city;

	/** 所在市 */
	public String cityName;

	/** 状态 */
	public String state = STATE_NOT_AUDIT;

	/** 姓名 */
	public String pName;
	
	
	/**公司简称 品牌**/
	public String logoName;
	
	/**商标 logo 图片地址
	public String logoImg;
	**/

	/**
	 * 性别：0，男；1，女
	 * */
	public String pSex;

	/**
	 * 住址
	 * */
	public String pAddress;
	/**
	 * 座机
	 * */
	public String pPhone;

	/** 移动电话 */
	public String pMobile;

	/** 出生年月 */
	public Date pBirthday;

	/** (法人)身份证号 */
	public String idCardNo;

	/**
	 * (法人)身份证正面扫描图片
	 * */
	public UploadFile idCardFrontImg;

	/**
	 * (法人)身份证反面扫描图
	 */
	public UploadFile idCardBackImg;

	/** 资金开户行全称 */
	public String bank;
	/** 资金账号 */
	public String bankAccount;
	/**
	 * 开户行 替换原开户行  原来开户行改为全称
	 */
	public BankLibrary banklib;
	

	/** 注册日期 */
	public Date createDate = new Date();

	/**
	 * 修改(变更日期)
	 * */
	public Date updateDate = null;

	/** 企业名称 */
	public String eName;
	/** 企业详细地址 */
	public String eAddress;
	/** 邮编 */
	public String ePostcode;
	/**
	 * 组织机构代码
	 * */
	public String eOrgCode;

	/**
	 * 组织机构复印件(图片)
	 * */
	private UploadFile orgCertificate = null;

	/** 营业执照代码 */
	public String eBusCode;

	/**
	 * 营业执照复印件(图片)
	 * */
	private UploadFile busLicense = null;

	/** 税务登记号码 */
	public String eTaxCode;

	/**
	 * 税务登记证复印件(图片)
	 * */
	private UploadFile taxRegCertificate = null;

	/** 法人代表 */
	public String eLegalPerson;

	/**
	 * 法人代表身份证复印件(图片)
	 * */
	private UploadFile legalPersonIdCard = null;

	/** 企业性质 */
	public String eNature;

	/**
	 * 企业性质
	 * */
	public CompanyProperty companyProperty;

	public String companyPropertyName;

	/**
	 * 企业所属行业
	 * */
	public Industry industry;

	/**
	 * 固定电话
	 * */
	public String ePhone;
	/**
	 * 手机
	 * */
	public String eMobile;
	/**
	 * 传真
	 * */
	public String eFax;
	/** 企业经营状况 */

	public String eState;
	/** 联系人 */
	public String eContact;
	/** 联系人固话 */
	public String eContactPhone;
	/** 联系人手机 */
	public String eContactMobile;
	/** 联系人传真 */
	public String eContactFax;

	public String showPhone;

	public String showMobile;

	public String showName;
	/**
	 * 手机(辅助字段)
	 * */
	public String mobile;
	/**
	 * 会员所属的级别
	 * */
	public MemberLevel memberLevel;

	/**
	 * 开户申请书第一扫描图片
	 * */
	public UploadFile accountApplicationImg;

	/**
	 * 开户申请书第二页扫描图片
	 * */
	public UploadFile accountApplicationImg1;

	/**
	 * 银行卡正面扫描图
	 * */
	public UploadFile bankCardFrontImg;
	
	/**
	 * 缴费时间
	 **/
	public Date yearFeeDate;
	/**
	 * 席位费开始时间 
	 * 默认是审核通过的时间  计时开始
	 **/
	public Date yearFeeStartDate;
	/**
	 * 席位费缴费记录明细
	 * {username:操作员,date:操作日期,finanCode:融资项目,fee:席位费金额},每次缴席位费操作都追加明细
	 **/
	public String yearFeeNote;
	
	/**
	 * 席位费结束时间(自动生成)
	 * 开始时间自动往后推一年为结束时间
	 **/
	public Date yearFeeEndDate;
	/**
	 * 缴费时间
	 **/
	public Date creditFeeDate;
	/**
	 * 信用管理费开始时间 
	 * 默认是审核通过的时间  计时开始
	 **/
	public Date creditFeeStartDate;
	/**
	 * 信用管理费费记录明细
	 * {username:操作员,date:操作日期,finanCode:融资项目,fee:席位费金额},每次缴席位费操作都追加明细
	 **/
	public String creditFeeNote;
	
	/**
	 * 信用管理费结束时间(自动生成)
	 * 开始时间自动往后推一年为结束时间
	 **/
	public Date creditFeeEndDate;
	
	private String jingbanren;//介绍人
	
	private String relaAcctBankAddr;//关联账户开户行地址
	private String relaAcctBankCode;//关联账户开户行支付系统行号
	private String relaAcctBank;//关联账户开户行
	
	private String qq;
	private String email;
	
	private Date firstAuditDate;
	private Date recentAuditDate;
	
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}

	public MemberBase() {
	}

	public MemberBase(String id) { 
		this.id = id;
	}
	
	public MemberBase(String id,String pName) { 
		this.id = id;
		this.pName = pName;
	}

	public MemberBase(String id, String category, String pName, String pPhone,
			String pMobile, String eName, String ePhone, String eMobile,
			MemberType memberType) {
		this.id = id;
		this.category = category;
		this.pName = pName;
		this.pPhone = pPhone;
		this.pMobile = pMobile;
		this.eName = eName;
		this.ePhone = ePhone;
		this.eMobile = eMobile;
		this.memberType = memberType;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToOne
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToOne
	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpSex() {
		return pSex;
	}

	public void setpSex(String pSex) {
		this.pSex = pSex;
	}

	public String getpAddress() {
		return pAddress;
	}

	public void setpAddress(String pAddress) {
		this.pAddress = pAddress;
	}

	public String getpPhone() {
		return pPhone;
	}

	public void setpPhone(String pPhone) {
		this.pPhone = pPhone;
	}

	public String getpMobile() {
		return pMobile;
	}

	public void setpMobile(String pMobile) {
		this.pMobile = pMobile;
	}
	
	@Transient
	public String getMobile() {
		if((MemberBase.CATEGORY_PERSON).equals(this.category))
		    return this.getpMobile();
		else{
			return this.geteMobile()==null?this.geteContactMobile():this.geteMobile();
		}
	}  
	public Date getpBirthday() {
		return pBirthday;
	}

	public void setpBirthday(Date pBirthday) {
		this.pBirthday = pBirthday;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	/**
	 * @deprecated 
	 */
	public String getBank() {
		return bank;
	}
	
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String geteAddress() {
		return eAddress;
	}

	public void seteAddress(String eAddress) {
		this.eAddress = eAddress;
	}

	public String getePostcode() {
		return ePostcode;
	}

	public void setePostcode(String ePostcode) {
		this.ePostcode = ePostcode;
	}

	public String geteOrgCode() {
		return eOrgCode;
	}

	public void seteOrgCode(String eOrgCode) {
		this.eOrgCode = eOrgCode;
	}

	public String geteBusCode() {
		return eBusCode;
	}

	public void seteBusCode(String eBusCode) {
		this.eBusCode = eBusCode;
	}

	public String geteTaxCode() {
		return eTaxCode;
	}

	public void seteTaxCode(String eTaxCode) {
		this.eTaxCode = eTaxCode;
	}

	public String geteLegalPerson() {
		return eLegalPerson;
	}

	public void seteLegalPerson(String eLegalPerson) {
		this.eLegalPerson = eLegalPerson;
	}

	public String geteNature() {
		return eNature;
	}

	public void seteNature(String eNature) {
		this.eNature = eNature;
	}

	public String getePhone() {
		return ePhone;
	}

	public void setePhone(String ePhone) {
		this.ePhone = ePhone;
	}

	public String geteMobile() {
		return eMobile;
	}

	public void seteMobile(String eMobile) {
		this.eMobile = eMobile;
	}

	public String geteFax() {
		return eFax;
	}

	public void seteFax(String eFax) {
		this.eFax = eFax;
	}

	@Column(length = 2048)
	public String geteState() {
		return eState;
	}

	public void seteState(String eState) {
		this.eState = eState;
	}

	public String geteContact() {
		return eContact;
	}

	public void seteContact(String eContact) {
		this.eContact = eContact;
	}

	public String geteContactPhone() {
		return eContactPhone;
	}

	public void seteContactPhone(String eContactPhone) {
		this.eContactPhone = eContactPhone;
	}

	public String geteContactMobile() {
		return eContactMobile;
	}

	public void seteContactMobile(String eContactMobile) {
		this.eContactMobile = eContactMobile;
	}

	public String geteContactFax() {
		return eContactFax;
	}

	public void seteContactFax(String eContactFax) {
		this.eContactFax = eContactFax;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Transient
	public String getShowPhone() {
		return showPhone;
	}

	@Transient
	public String getShowMobile() {
		return showMobile;
	}

	@Transient 
	public String getShowName() {
		if((MemberBase.CATEGORY_PERSON).equals(this.category))
		    return this.getpName();
		else{
			return this.geteName();
		} 
	}

	public void setShowPhone(String showPhone) {
		this.showPhone = showPhone;
	}

	public void setShowMobile(String showMobile) {
		this.showMobile = showMobile;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName());
		if (MemberBase.CATEGORY_ORG.equals(category)) {
			sb.append("[name:" + this.eName);
			sb.append(",companyProperty:" + this.companyProperty.getName());
			sb.append(",industry:" + this.industry.getName());
			sb.append(",phone:" + this.ePhone + ",mobile:" + this.eMobile
					+ ",fax:" + this.eFax + ",bank:" + this.bank);
			sb.append(",bankAccount:" + this.bankAccount);
			sb.append(",legalPerson:" + this.eLegalPerson);
			sb.append(",idCardNo:" + this.idCardNo);
			sb.append(",nature:" + this.eNature);
			sb.append(",orgCode:" + this.eOrgCode);
			sb.append(",busCode:" + this.eBusCode);
			sb.append(",taxCode:" + this.eTaxCode);
			sb.append(",contact:" + this.eContact);
			sb.append(",contactPhone:" + this.eContactPhone);
			sb.append(",contactMobile:" + this.eContactMobile);
			sb.append(",contactFax:" + this.eContactFax);
			sb.append(",postCode:" + this.ePostcode);
			sb.append(",state:" + this.eState);
			sb.append(",province:" + this.provinceName);
			sb.append(",city:" + this.cityName);
			sb.append(",address:" + this.eAddress + "]");

		} else if (MemberBase.CATEGORY_PERSON.equals(category)) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			sb.append("[name:" + this.pName + ",phone:" + this.pPhone
					+ ",mobile:" + this.pMobile + ",bank:" + this.bank);
			sb.append(",bankAccount:" + this.bankAccount);
			sb.append(",sex:" + this.pSex);
			if (null != pBirthday) {
				sb.append(",birthday:" + df.format(this.pBirthday));
			}
			sb.append(",province:" + this.provinceName);
			sb.append(",city:" + this.cityName);
			sb.append(",address:" + this.pAddress + "]");
		}
		return sb.toString();
	}

	@OneToOne
	public MemberLevel getMemberLevel() {
		return memberLevel;
	}

	public void setMemberLevel(MemberLevel memberLevel) {
		this.memberLevel = memberLevel;
	}

	@OneToOne
	public CompanyProperty getCompanyProperty() {
		return companyProperty;
	}

	public void setCompanyProperty(CompanyProperty companyProperty) {
		this.companyProperty = companyProperty;
	}

	@OneToOne
	public Industry getIndustry() {
		return industry;
	}

	public void setIndustry(Industry industry) {
		this.industry = industry;
	}

	@OneToOne
	public UploadFile getOrgCertificate() {
		return orgCertificate;
	}

	public void setOrgCertificate(UploadFile orgCertificate) {
		this.orgCertificate = orgCertificate;
	}

	@Transient
	public String getCompanyPropertyName() {
		return companyPropertyName;
	}

	public void setCompanyPropertyName(String companyPropertyName) {
		this.companyPropertyName = companyPropertyName;
	}

	@OneToOne
	public UploadFile getBusLicense() {
		return busLicense;
	}

	public void setBusLicense(UploadFile busLicense) {
		this.busLicense = busLicense;
	}

	@OneToOne
	public UploadFile getTaxRegCertificate() {
		return taxRegCertificate;
	}

	public void setTaxRegCertificate(UploadFile taxRegCertificate) {
		this.taxRegCertificate = taxRegCertificate;
	}

	@OneToOne
	public UploadFile getLegalPersonIdCard() {
		return legalPersonIdCard;
	}

	public void setLegalPersonIdCard(UploadFile legalPersonIdCard) {
		this.legalPersonIdCard = legalPersonIdCard;
	}

	@OneToOne
	public UploadFile getIdCardFrontImg() {
		return idCardFrontImg;
	}

	public void setIdCardFrontImg(UploadFile idCardFrontImg) {
		this.idCardFrontImg = idCardFrontImg;
	}

	@OneToOne
	public UploadFile getIdCardBackImg() {
		return idCardBackImg;
	}

	public void setIdCardBackImg(UploadFile idCardBackImg) {
		this.idCardBackImg = idCardBackImg;
	}

	@OneToOne
	public UploadFile getBankCardFrontImg() {
		return bankCardFrontImg;
	}

	public void setBankCardFrontImg(UploadFile bankCardFrontImg) {
		this.bankCardFrontImg = bankCardFrontImg;
	}

	@OneToOne
	public UploadFile getAccountApplicationImg() {
		return accountApplicationImg;
	}

	public void setAccountApplicationImg(UploadFile accountApplicationImg) {
		this.accountApplicationImg = accountApplicationImg;
	}

	@OneToOne
	public UploadFile getAccountApplicationImg1() {
		return accountApplicationImg1;
	}

	public void setAccountApplicationImg1(UploadFile accountApplicationImg1) {
		this.accountApplicationImg1 = accountApplicationImg1;
	}

	@OneToOne
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@OneToOne
	public User getChanger() {
		return changer;
	}

	public void setChanger(User changer) {
		this.changer = changer;
	} 
 
	public Date getYearFeeDate() {
		return yearFeeDate;
	}

	public void setYearFeeDate(Date yearFeeDate) {
		this.yearFeeDate = yearFeeDate;
	}

	public Date getYearFeeStartDate() {
		return yearFeeStartDate;
	}

	public void setYearFeeStartDate(Date yearFeeStartDate) {
		this.yearFeeStartDate = yearFeeStartDate;
	} 
    @Column(length=2000)
	public String getYearFeeNote() {
		return yearFeeNote;
	}

	public void setYearFeeNote(String yearFeeNote) {
		this.yearFeeNote = yearFeeNote;
	}

	public Date getYearFeeEndDate() {
		return yearFeeEndDate;
	}

	public void setYearFeeEndDate(Date yearFeeEndDate) {
		this.yearFeeEndDate = yearFeeEndDate;
	}

	 
	
	public Date getCreditFeeDate() {
		return creditFeeDate;
	}

	public void setCreditFeeDate(Date creditFeeDate) {
		this.creditFeeDate = creditFeeDate;
	}

	public Date getCreditFeeStartDate() {
		return creditFeeStartDate;
	}

	public void setCreditFeeStartDate(Date creditFeeStartDate) {
		this.creditFeeStartDate = creditFeeStartDate;
	}
	@Column(length=2000)
	public String getCreditFeeNote() {
		return creditFeeNote;
	}

	public void setCreditFeeNote(String creditFeeNote) {
		this.creditFeeNote = creditFeeNote;
	}

	public Date getCreditFeeEndDate() {
		return creditFeeEndDate;
	}

	public void setCreditFeeEndDate(Date creditFeeEndDate) {
		this.creditFeeEndDate = creditFeeEndDate;
	}

	public void setJingbanren(String jingbanren) {
		this.jingbanren = jingbanren;
	}

	public String getJingbanren() {
		return jingbanren;
	}
	@ManyToOne
	@JoinColumn(name="bank_id")
	public BankLibrary getBanklib() {
		return banklib;
	}

	public void setBanklib(BankLibrary banklib) {
		this.banklib = banklib;
	}

	public void setRelaAcctBankAddr(String relaAcctBankAddr) {
		this.relaAcctBankAddr = relaAcctBankAddr;
	}

	public String getRelaAcctBankAddr() {
		return relaAcctBankAddr;
	}

	public void setRelaAcctBankCode(String relaAcctBankCode) {
		this.relaAcctBankCode = relaAcctBankCode;
	}

	public String getRelaAcctBankCode() {
		return relaAcctBankCode;
	}

	public void setRelaAcctBank(String relaAcctBank) {
		this.relaAcctBank = relaAcctBank;
	}

	public String getRelaAcctBank() {
		return relaAcctBank;
	}
	
	@Column(length=50)
	public String getLogoName() {
		return logoName;
	}

	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}
 
	public Date getFirstAuditDate() {
		return firstAuditDate;
	}

	public void setFirstAuditDate(Date firstAuditDate) {
		this.firstAuditDate = firstAuditDate;
	}

	public Date getRecentAuditDate() {
		return recentAuditDate;
	}

	public void setRecentAuditDate(Date recentAuditDate) {
		this.recentAuditDate = recentAuditDate;
	}

	public MemberBase clone(){
		MemberBase obj = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			bis = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bis);
			obj = (MemberBase) ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				bos.close();
				oos.close();
				bis.close();
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
}
