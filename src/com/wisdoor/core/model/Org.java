package com.wisdoor.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.kmfex.model.MemberBase;

/*
 * 公司、机构表   
 * @author
 */
@Entity
@Table(name = "sys_Org", schema = "KT")
public class Org implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3762424299913330858L;
	/**
	 * 顶级机构内部编码
	 * */
	public static final String TOP_ORG_CODEING = "1";
	
	/**
	 * 顶级机构内部编码
	 * */
	public static final String TOP_ORG_CODEING2 = "1m1196";

	/**
	 * 状态标记：正常
	 * */
	public static final byte STATE_NORMAL = 0;
	/**
	 * 状态标记：注销
	 * */
	public static final byte STATE_CANCELED = 1;
	

	private long id;
	private String name;
	/**
	 * 简称、logo、品牌 
	 */
	private String shortName;
	
	/**
	 * 担保方简称
	 */
	private String shortName2;
	
	private Set<User> users = new HashSet<User>();
	private boolean leaf;
	private Org parent;
	private Set<Org> children;
	private long parentId; // 辅助变量不参与映射数据库
	/**
	 * 编码规则： 一级 1 二级 1m2 三级 1m2m3 依此类推 唯一,最关键字段 ,体现层次
	 */
	private String coding;

	/**
	 * 显示编码 (人工维护)
	 */
	private String showCoding;
	
	private String parentCoding;

	private long sortAss = 0;
	private String link;
	private Integer order = 0;
	/** 联系信息 **/
	private Contact orgContact;

	/** 帐号信息 **/
	private Account orgAccount;

	private User createBy; // 创建人
	private Date createDateBy; // 创建日期

	/**
	 * 状态
	 * */
	private byte state = STATE_NORMAL;
	
	
	private String type;   /* 0: 保荐机构   1:授权服务中心  2:保证+授权中心 */

	/**
	 * 注销日期
	 * */
	private Date canceledDate;
	
	/**
	 * 担保方
	 */
	private MemberBase guarantee;
	
	private int version=0;
	
	/**最大用户号---生成用户时用**/
	private String maxUsername;
	
	public Org() {
	}

	public Org(long id) {
		this.id = id;
	}

	public Org(String name, Org parent, String coding, boolean leaf) {
		this.name = name;
		this.leaf = leaf;
		this.parent = parent;
		this.coding = coding;
	}

	@OneToOne
	public Account getOrgAccount() {
		return orgAccount;
	}

	public void setOrgAccount(Account orgAccount) {
		this.orgAccount = orgAccount;
	}

	public Org(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "org", cascade = CascadeType.REFRESH)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SYS_ORG_SE")  
    @SequenceGenerator(name="SYS_ORG_SE",sequenceName="SYS_ORG_SEQUENCE",allocationSize=1)
	@Column(length = 50)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "name_", length = 128, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.REMOVE }, mappedBy = "parent")
	public Set<Org> getChildren() {
		return children;
	}

	public void setChildren(Set<Org> children) {
		this.children = children;
	}

	@Column(name = "leaf_")
	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "parentid_")
	public Org getParent() {
		return parent;
	}

	public void setParent(Org parent) {
		this.parent = parent;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contactid_")
	public Contact getOrgContact() {
		return orgContact;
	}

	public void setOrgContact(Contact orgContact) {
		this.orgContact = orgContact;
	}

	@Transient
	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	@Column(name = "order_")
	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public long getSortAss() {
		return sortAss;
	}

	@Column(name = "sortAss_", columnDefinition = "int default 0")
	public void setSortAss(long sortAss) {
		this.sortAss = sortAss;
	}

	@Column(name = "link_")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getShowCoding() {
		return showCoding;
	}

	public void setShowCoding(String showCoding) {
		this.showCoding = showCoding;
	}
	

	public String getParentCoding() {
		return parentCoding;
	}

	public void setParentCoding(String parentCoding) {
		this.parentCoding = parentCoding;
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

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public Date getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(Date canceledDate) {
		this.canceledDate = canceledDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name="guarantee_id")
	public MemberBase getGuarantee() {
		return guarantee;
	}

	public void setGuarantee(MemberBase guarantee) {
		this.guarantee = guarantee;
	}
	
	
	@Version
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}

	public String getMaxUsername() {
		return maxUsername;
	}

	public void setMaxUsername(String maxUsername) {
		this.maxUsername = maxUsername;
	}
	@Column(length=30)
	public String getShortName() {
		return shortName;  
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	@Column(length=50)
	public String getShortName2() {
		return shortName2;
	}

	public void setShortName2(String shortName2) {
		this.shortName2 = shortName2;
	}

}
