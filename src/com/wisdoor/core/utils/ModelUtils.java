package com.wisdoor.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体类工具
 * 
 * @author 
 * @version 2012-05-22
 * */
public final class ModelUtils {

	private final static Map<String, String> modelNames = new HashMap<String, String>();

	static {
		modelNames.put("com.kmfex.model.AccountDeal", "用户帐户明细表");
		modelNames.put("com.kmfex.model.Announcement", "公告信息表");
		modelNames.put("com.kmfex.model.BusinessType", "业务类型表");
		modelNames.put("com.kmfex.model.CompanyProperty", "公司(企业)性质表");
		modelNames.put("com.kmfex.model.ContractKeyData", "合同关键数据表");
		modelNames.put("com.kmfex.model.CostBase", "收费项目表");
		modelNames.put("com.kmfex.model.CostItem", "收费标准明细表");
		modelNames.put("com.kmfex.model.CreditLevel", "信用等级表");
		modelNames.put("com.kmfex.model.FinancingBase", "确认后的融资项目基本信息表");
		modelNames.put("com.kmfex.model.FinancingContract", "融资项目总合同表");
		modelNames.put("com.kmfex.model.FinancingCost", "融资费用表");
		modelNames.put("com.kmfex.model.Industry", "公司行业表");
		modelNames.put("com.kmfex.model.InvestCondition", "投资投标约束表");
		modelNames.put("com.kmfex.model.InvestRecord", "投资投标记录表");
		modelNames.put("com.kmfex.model.InvestRecordCost", "投资投标费用明细表");
		modelNames.put("com.kmfex.model.MemberAudit", "会员审核记录表");
		modelNames.put("com.kmfex.model.MemberBase", "会员基本信息实体类表");
		modelNames.put("com.kmfex.model.MemberCredit", "会员信用表");
		modelNames.put("com.kmfex.model.MemberGuarantee", "担保公司会员担保情况表");
		modelNames.put("com.kmfex.model.MemberLevel", "会员级别表");
		modelNames.put("com.kmfex.model.MemberType", "会员类型实体类表");
		modelNames.put("com.kmfex.model.PaymentRecord", "还款记录表");
		modelNames.put("com.kmfex.model.PreFinancingBase", "融资项目申请历史表");
		modelNames.put("com.kmfex.model.Region", "地域表");
		modelNames.put("com.kmfex.model.TradeTime", "交易时间表");
		modelNames.put("com.kmfex.model.TreatyBase", "协议实体表");

		modelNames.put("com.wisdoor.core.model.Account", "用户帐户表");
		modelNames.put("com.wisdoor.core.model.Contact", "用户联系方式表");
		modelNames.put("com.wisdoor.core.model.Menu", "菜单表");
		modelNames.put("com.wisdoor.core.model.Org", "机构表");
		modelNames.put("com.wisdoor.core.model.Role", "角色表");
		modelNames.put("com.wisdoor.core.model.RoleMenu", "角色资源表");
		modelNames.put("com.wisdoor.core.model.Transaction", "事务信息表");
		modelNames.put("com.wisdoor.core.model.UploadFile", "文件(附件)信息表");
		modelNames.put("com.wisdoor.core.model.User", "用户表");
	}

	/**
	 * 返回指定类名的实体类的描述
	 * @return className 完整的类名
	 * */
	public final static String getTableNameByClassName(String className) {
		String tableName = null;

		if (null != className) {
			return modelNames.get(className);
		}

		return tableName;

	}
	
	/**
	 * 返回声明的类名
	 * */
	public final static String[] getDeclaredClassesName(){
		
		return (String[])modelNames.keySet().toArray();
	}

	public static void main(String[] args) {

		System.out
				.println(getTableNameByClassName("com.wisdoor.core.model.User"));

		// File dir=new
		// File("F:\\敖汝安\\myeclipse workspace\\kmfex\\src\\com\\wisdoor\\core\\model");
		//		
		// File[] modelFiles=dir.listFiles(new FilenameFilter() {
		//			
		// @Override
		// public boolean accept(File dir, String name) {
		//				
		// return name.endsWith(".java");
		//				
		// }
		// });
		//		
		// System.out.println("modelFiles.length= "+modelFiles.length);
		//		
		// for(File f:modelFiles){
		// System.out.println("modelNames.put(\"com.wisdoor.core.model."+f.getName()+"\", \"用户帐户明细表\");");
		// }

	}
}
