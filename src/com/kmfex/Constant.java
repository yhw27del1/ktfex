package com.kmfex;
/*
 * 融资项目常量
* @author  
 */
public class Constant {
    /**
     * 机构根为空时显示名称
     */
	public static final String ORGROOTNAME = "昆明商企业金融";  
    /**
     * 机构根ID
     */
	public static final long ORGID = 1; 
	/***
	 *  初始角色
	 */
	public static final long INITROLEID = 7; 
	/***
	 *  融资方默认角色
	 */
	public static final long RZROLEID = 8; 
	/***
	 *  投资人默认角色
	 */
	public static final long TZROLEID = 9; 
	
	/***
	 *  担保公司默认角色
	 */
	public static final long DBROLEID = 14; 
	
	
	/***
	 *  一年按几天计算
	 */
	public static final long YEAR_DAY = 360; 
	
	/***
	 *  当前投标使用的是那种标准
	 *  F表示最大约束来自融资项目金额
	 *  A表示最大约束根据总资产：约束的最大融资额（当前用户已经投标的金额+帐号余额）
	 */
	public static final String MAX_INVEST="F";
	
	/**
	 * 服务中心编码
	 **/
	public static final  String FWZX_ORG_CODE = "530100"; 
	
 
}
