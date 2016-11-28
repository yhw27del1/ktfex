package com.wisdoor.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户(个人、企业、交易中心管理员)
 * @author
 * 添加字段userState
 * 添加字段 Agreement
 */ 
@Entity 
@Table(name="sys_User",schema="KT")
public class User     implements java.io.Serializable,UserDetails{ 
	private static final long serialVersionUID = -6732569191310529603L;
	private long id;
	/**用户名**/
	private String username;
	//摊位号，username
	//2012-12-10新增字段
	private String accountNo;//华夏，子账号
	private String dealerOperNo;//华夏，登录银行系统操作员代码
	private String flag = "0";//0表示初始状态；1表示交易系统已经同步,但银行未同步(无子账号)；2表示交易系统已经同步,银行已经同步(有子账号)；3表示交易商解约了
	private Date signDate;//子账户签约时间
	private int signBank = 0;//三方存管签约行:未签约三方存管=0,签约华夏三方存管=1,签约招商三方存管=2
	private int signType = 0;//三方存管签约类型:未签约三方存管=0,签约本行=1,签约他行=2
	private Date synDate_market;//交易市场发起的子账户同步时间
	private Date synDate_bank;//银行发起的子账户同步时间
	private Date surrenderDate;//解约日期
	/**登录密码**/
	private String password;
	/**姓名 **/
	private String realname;  
	/**照片**/
	private String imageName;  
	/**是否启用 **/
	private Boolean visible=true;
	/**注册时间 **/
	private Date regTime=new Date(); 
	/**联系信息 **/
	private Contact userContact; 
	/**公司信息**/
	private Org org;  
	/** 帐号信息 **/
	@Column
	private Account userAccount;
	/**
	 * 编码规则：机构编码+此表Id   
	 * 唯一,最关键字段 ,体现层次 
	 */
	private String coding;  
	/**用户类型**/  
	private String userType;  
	/**1前台2后台用户**/  
	private String typeFlag;  
	/**登录次数**/
	private int loginCount=0;  
	/**最近一次登录时间 **/
	private Date lastDate=new Date();  
	/**当前登录时间 **/
	private Date currentDate=new Date(); 
	private boolean enabled=true; //true使用false停用
	

	/* 拥有的角色 */
	private Set<Role> roles = new HashSet<Role>();	
	private User createBy;	//创建人
	private Date createDateBy; //创建日期
	

	private boolean agreement = false; //是否签署过补充协议    
	
	/**
	 * 用户的默认密码
	 * */
    public static final String defaultPassword="654321"; 

   
    /** 当前用户状态     **/
    private String userState = "2";
    
    /**
	 * 用户状态：未审核，即此账户刚开通,还没有通过审核
	 * */
	public final static String STATE_NOT_AUDIT = "1";
	/**
	 * 用户状态：正常，即已审核通过，并在使用中
	 * */
	public final static String STATE_PASSED_AUDIT = "2";
	/**
	 * 用户状态：关联未通过审核
	 * */
	public final static String STATE_NOT_PASS_AUDIT = "3";

	/**
	 * 用户状态：已停用
	 * */
	public final static String STATE_DISABLED = "4";
	
	/**
	 * 用户状态：休眠---暂时无切换到休眠状态的方法
	 * */
	public final static String STATE_SLEEPED = "5";
	
	/**
	 * 用户状态：多次登录错误被锁定
	 * */
	public final static String STATE_LOCKED = "6";
	
	/**
	 * 用户状态：异常情况：前台用户无member关联
	 * */
	public final static String STATE_ERROR_USER = "7";
	
	private int channel = 1;//主要标注：手工方式的专户渠道
	//0：无专户渠道,签约三方存管将此字段设置为0；解约三方存管将此字段设置为1
	//1：银行手工专户渠道
	//
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SYS_USER_SE")  
    @SequenceGenerator(name="SYS_USER_SE",sequenceName="SYS_USER_SEQUENCE",allocationSize=1)
	@Column(length=50)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public User(){}
	
	public User(String username) {
		this.username = username;
	}
 
	
	public User(String username, String password, String typeFlag) { 
		this.username = username;
		this.password = password;
		this.typeFlag = typeFlag;
	}
 

	/**
	 * 返回一个已指定名称为用户名，指定默认密码的用户
	 * */
	public static User createUser(String username){
		return new User(username,defaultPassword,"1");
	}
	
	@Column(length=50,unique=true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Column(length=100)
	public String getPassword() {   
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	@Column(length=300)
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
 
  
	@Column(length=255)
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	@Column(nullable=false)
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="org_id")
	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}
	@Column(length=255)
	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}
 
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	@Column(name="userType_")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="contactid")
	public Contact getUserContact() {
		return userContact;
	}

	public void setUserContact(Contact userContact) {
		this.userContact = userContact;
	}
	@OneToOne
    public Account getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(Account userAccount) {
		this.userAccount = userAccount;
	}

	
	@ManyToMany(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER)
	@JoinTable(name="sys_user_role",schema="KT",joinColumns=@JoinColumn(name="userid"),
			inverseJoinColumns=@JoinColumn(name="roleid"))
 	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	/**
	 * 添加权限角色
	 * @param group
	 */
	public void addRole(Role role){
		this.roles.add(role);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	 


	public int getLoginCount() {
		return loginCount;
	}

	
	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

 
	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}


	public String getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(String typeFlag) {
		this.typeFlag = typeFlag;
	}

	@Transient
	public Collection<GrantedAuthority> getAuthorities() { 
		Collection<GrantedAuthority> grantedAuthoritis = new ArrayList<GrantedAuthority>(roles.size());
		for(Role role : roles){
			grantedAuthoritis.add(new GrantedAuthorityImpl("ROLE_"+role.getId()));
		}
		return grantedAuthoritis;
	}
	 
	@Transient
	public boolean isAccountNonExpired() { 
		return true;
	} 
	@Transient
	public boolean isAccountNonLocked() { 
		return true;
	} 
	@Transient
	public boolean isCredentialsNonExpired() { 
		return true;
	} 
	//当前用户是否处于激活状态
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	//返回逗号分割形式的的所有角色
	@Transient
	public String getRoleString(){
		List<String> roleList = new ArrayList<String>();
		for(Role role : roles){
			roleList.add(role.getId()+"");
		}
		return StringUtils.join(roleList,",");
	}
	@ManyToOne 
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDateBy() {
		return createDateBy;
	}

	public void setCreateDateBy(Date createDateBy) {
		this.createDateBy = createDateBy;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setDealerOperNo(String dealerOperNo) {
		this.dealerOperNo = dealerOperNo;
	}

	public String getDealerOperNo() {
		return dealerOperNo;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(columnDefinition="varchar2(10) default '0'")
	public String getFlag() {
		return flag;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSynDate_market(Date synDate_market) {
		this.synDate_market = synDate_market;
	}

	public Date getSynDate_market() {
		return synDate_market;
	}

	public void setSynDate_bank(Date synDate_bank) {
		this.synDate_bank = synDate_bank;
	}

	public Date getSynDate_bank() {
		return synDate_bank;
	}

	public void setSurrenderDate(Date surrenderDate) {
		this.surrenderDate = surrenderDate;
	}

	public Date getSurrenderDate() {
		return surrenderDate;
	}
 

	@Column(columnDefinition="number(1) default 0")
	public boolean isAgreement() {
		return agreement;
	}

	public void setAgreement(boolean agreement) {
		this.agreement = agreement;
	}

	@Column(columnDefinition="number(10) default 0")
	public int getSignBank() {
		return signBank;
	}

	public void setSignBank(int signBank) {
		this.signBank = signBank;
	}

	@Column(columnDefinition="number(10) default 0")
	public int getSignType() {
		return signType;
	}

	public void setSignType(int signType) {
		this.signType = signType;
	}

	@Column(columnDefinition="number(10) default 1")
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}
	
}
