package com.kmfex.util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.lang.reflect.Method;

import com.kmfex.model.MemberBase;
import com.wisdoor.core.model.User;

public class ImportExcelUtil {
	
	public static final int FontSize = 14;
	public static final int FontSize_Body = 10;
	
	
	
	/**
	 * 
	 * @param title 标题
	 * @param os 目标输出流
	 * @param head 表头
	 * @param paramOrder 参数顺序
	 * @param theData 数据集合
	 * @param objName 数据类名（含包名）
	 * @throws Exception
	 */
	public static void exportInvestors(String title,OutputStream os,List<String> head,List<String> paramOrder,List theData,String objName) throws Exception {
		System.out.println("搜索到的信息长度为"+theData.size());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		WritableWorkbook book = Workbook.createWorkbook(os);
		WritableSheet sheet = book.createSheet("sheet1", 0);
		String fontName = "宋体";
		
		//format1 为标题及表头字体格式
		//format为正文格式
		WritableFont font2 = new WritableFont(
				WritableFont.createFont(fontName), FontSize_Body, WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font2);
		
		format.setAlignment(jxl.format.Alignment.CENTRE);// 设置为居中

		WritableFont font1 = new WritableFont(WritableFont.TIMES, 14,
				WritableFont.BOLD);
		WritableCellFormat format1 = new WritableCellFormat(font1);
		format1.setAlignment(jxl.format.Alignment.CENTRE);// 设置为居中

		//将0行0列的9个单元格合并
		sheet.mergeCells(0, 0, 9, 0);

		// 在Label对象的构造方法中指明单元格位置是第一列第一行(0,0)
		//第一行添加表头
		
		Label label = null;
		if(title == null){
			label = new Label(0, 0, "XXXX导出", format1);
			sheet.addCell(label);
		}else{
			label = new Label(0, 0, title, format1);
			sheet.addCell(label);
		}
		

		format1.setFont(font1);
		//第二行 添加表头
		for(int j=0;j<head.size();j++){
			label = new Label(j, 1, head.get(j), format);
			sheet.addCell(label);
		}
		
		//从第三行起  添加数据
		for(int i=0;i<theData.size();i++){
			Class<?> demo = null;
//			System.out.println(objName);
			if("HashMap".equals(objName)){
				if(theData.get(i)!=null){
					HashMap map = (HashMap) theData.get(i);
					//毎完成一次for循环，数据添加一行
					for(int j=0;j<paramOrder.size();j++){
						String currentParam = paramOrder.get(j);
//						System.out.println("容易出错标记之参数列表："+currentParam);
						Object currentObj = map.get(currentParam);
						String str = currentObj==null?"":currentObj.toString();
//						System.out.println("第"+(i+2)+"行，第"+j+"列，输出数据为"+str);
						label = new Label(j,i+2,str, format);
						sheet.addCell(label);
					}
				}
			}else {
				if(!objName.contains(".")){
					objName = getFullName(objName);
//					System.out.println(objName);
				} 
				demo = Class.forName(objName);
				Object obj = null;
				obj = demo.newInstance();
				// 如果取到的数据第i个不为空，则取其内容
				if (theData.get(i) != null) {
					obj = theData.get(i);
					// 毎完成一次for循环，数据添加一行
					for (int j = 0; j < paramOrder.size(); j++) {
						String str = (String) getFinalString(theData.get(i),
								paramOrder.get(j));
//						System.out.println("第" + (i + 2) + "行，第" + j
//								+ "列，输出数据为" + str);
						label = new Label(j, i + 2, str, format);

						sheet.addCell(label);
					}
				}
			}
		}		
		book.write();
		book.close();
		os.flush();
	}
	
	/**
	 * 由于sql中可以用decode来进行对结果修正，所以不需要decode，这里是为了以后修改方法不动原来功能
	 * @param title
	 * @param os
	 * @param head
	 * @param paramOrder
	 * @param theData
	 * @param objName
	 * @throws Exception
	 */
	public static void exportExcelWithDecode(String title,OutputStream os,List<String> head,List<String> paramOrder,List theData,String objName,Map<String,String> expressions) throws Exception {
		System.out.println("搜索到的信息长度为"+theData.size());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		WritableWorkbook book = Workbook.createWorkbook(os);
		WritableSheet sheet = book.createSheet("sheet1", 0);
		String fontName = "宋体";
		
		//format1 为标题及表头字体格式
		//format为正文格式
		WritableFont font2 = new WritableFont(
				WritableFont.createFont(fontName), FontSize_Body, WritableFont.NO_BOLD);
		WritableCellFormat format = new WritableCellFormat(font2);
		
		format.setAlignment(jxl.format.Alignment.CENTRE);// 设置为居中

		WritableFont font1 = new WritableFont(WritableFont.TIMES, 14,
				WritableFont.BOLD);
		WritableCellFormat format1 = new WritableCellFormat(font1);
		format1.setAlignment(jxl.format.Alignment.CENTRE);// 设置为居中

		//将0行0列的9个单元格合并
		sheet.mergeCells(0, 0, 9, 0);

		// 在Label对象的构造方法中指明单元格位置是第一列第一行(0,0)
		//第一行添加表头
		
		Label label = null;
		if(title == null){
			label = new Label(0, 0, "XXXX导出", format1);
			sheet.addCell(label);
		}else{
			label = new Label(0, 0, title, format1);
			sheet.addCell(label);
		}
		

		format1.setFont(font1);
		//第二行 添加表头
		for(int j=0;j<head.size();j++){
			label = new Label(j, 1, head.get(j), format);
			sheet.addCell(label);
		}
		
		//从第三行起  添加数据
		for(int i=0;i<theData.size();i++){
			Class<?> demo = null;
			//hashMap类型结构内容的读取导入excel
			if("HashMap".equals(objName)){
				if(theData.get(i)!=null){
					HashMap map = (HashMap) theData.get(i);
					//毎完成一次for循环，数据添加一行
					for(int j=0;j<paramOrder.size();j++){
						String currentParam = paramOrder.get(j);
						Object currentObj = map.get(currentParam);
						String str = currentObj==null?"":currentObj.toString();
						label = new Label(j,i+2,str, format);
						sheet.addCell(label);
					}
				}
			}else {//类类型结构内容的读取导入excel
				if(!objName.contains(".")){
					objName = getFullName(objName);
				} 
				demo = Class.forName(objName);
				Object obj = null;
				obj = demo.newInstance();
				// 如果取到的数据第i个不为空，则取其内容
				if (theData.get(i) != null) {
					obj = theData.get(i);
					// 毎完成一次for循环，数据添加一行
					for (int j = 0; j < paramOrder.size(); j++) {
						String str = (String) getFinalString(theData.get(i),
								paramOrder.get(j));
						label = new Label(j, i + 2, str, format);
						sheet.addCell(label);
					}
				}
			}//end else
		}//end for
		if(!expressions.isEmpty()){
			//解析expressions公式中的各个位置参数及内容
			Set <String> sets = expressions.keySet();
			Iterator<String> iterator=sets.iterator();
			while(iterator.hasNext())
			{
				String side = iterator.next();
				String value = expressions.get(side);
				int i = Integer.parseInt(side.substring(0, 2));
				int j = Integer.parseInt(side.substring(2, 4));
				label = new Label(j, i, value, format);
				sheet.addCell(label);
			}
		}
		book.write();
		book.close();
		os.flush();
	}
	
	/**
	 * 方法重载，title为空
	 * @param os 目标输出流
	 * @param head 表头
	 * @param paramOrder 参数顺序
	 * @param theData 数据集合
	 * @param objName 数据类名（含包名）
	 * @throws Exception 
	 */
	public static void exportInvestors(OutputStream os,List<String> head,List<String> paramOrder,List theData,String objName) throws Exception{
		exportInvestors(null,os,head,paramOrder,theData,objName);
	}
  
    /** 
     * @param obj 
     *            操作的对象 
     * @param att 
     *            操作的属性 
     * @return 
     * */
    public static Object getter(Object obj, String att) { 
        try { 
            Method method = obj.getClass().getMethod("get" + att); 
            //System.out.println(method.invoke(obj)); 
            return method.invoke(obj);
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return null;
    } 
    
    
    /**
     * 从  memberbase.user.username 中获取最终的内容
     * 递归调用
     * @param obj 给到的实体类
     * @param att 给出的字符窜	
     * @return
     */
    public static String getFinalString(Object obj, String att) { 
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Method method = null;
        try { 
        	if(att!=null){
	        	if(!att.contains(".")){
	        		method = obj.getClass().getMethod("get" + att); 
	        		Object objr = method.invoke(obj);
		            //System.out.println(objr); 
		            if(objr instanceof String)
		            	return (String)objr;
		            else if(objr instanceof Date){
		            	return sdf.format((Date)objr);
		            }else{
//		            	System.out.println("解析到最后仍然发现该字段不为字符串，使用其toString方法转换"+objr); 
		            	return objr.toString();
		            }	
	        	}else{
	        		String[] methodNames = att.split("\\.", 2);
	        		Class<?> demo2 = null;
	        		
	        		Object obj2=null;
	        		demo2 = Class.forName(getFullName(methodNames[0]));
	        		obj2=demo2.newInstance();
	        		obj2 = getter(obj,methodNames[0]);
	        		return getFinalString(obj2,methodNames[1]);
	        	}
        	}
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return null;
    } 
    
    
  
    /** 
     * @param obj 
     *            操作的对象 
     * @param att 
     *            操作的属性 
     * @param value 
     *            设置的值 
     * @param type 
     *            参数的属性 
     * */
    public static void setter(Object obj, String att, Object value, 
            Class<?> type) { 
        try { 
            Method method = obj.getClass().getMethod("set" + att, type); 
            method.invoke(obj, value); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
    
    public static String getFullName(String str){
    	if(str.equalsIgnoreCase("User")){
    		return "com.wisdoor.core.model.User";
    	}else if(str.equalsIgnoreCase("MemberBase")){
    		return "com.kmfex.model.MemberBase";
    	}else if(str.equalsIgnoreCase("CoreAccountLiveRecord")){
    		return "com.kmfex.model.CoreAccountLiveRecord";
    	}else if(str.equalsIgnoreCase("FinancingBase")){
    		return "com.kmfex.model.FinancingBase";
    	}else if(str.equalsIgnoreCase("FinancingCost")){
    		return "com.kmfex.model.FinancingCost";
    	}else if(str.equalsIgnoreCase("MemberGuarantee")){
    		return "com.kmfex.model.MemberGuarantee";
    	}else if(str.equalsIgnoreCase("BusinessType")){
    		return "com.kmfex.model.BusinessType";
    	}else if(str.equalsIgnoreCase("financier")){
    		return "com.kmfex.model.MemberBase";
    	}else if(str.equalsIgnoreCase("CreateBy")){
    		return "com.wisdoor.core.model.User";
    	}else if(str.equalsIgnoreCase("org")){
    		return "com.wisdoor.core.model.Org";
    	}else 
    		return "HashMap";
    }
    
    public static void main(String[] args) throws Exception { 	
    	System.out.println("1234".substring(0, 2));
    	System.out.println("1234".substring(2, 4));
    }

}
