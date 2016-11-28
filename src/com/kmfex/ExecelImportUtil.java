package com.kmfex;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
public class ExecelImportUtil {
	static Logger log = Logger.getLogger(ExecelImportUtil.class.getName());

	private File excelFile;

	private int sheetNums;

	private HSSFWorkbook wb;

	public ExecelImportUtil(InputStream inputStream) {
		try {
			// 创建Excel工作书
			wb = new HSSFWorkbook(inputStream);
			// 设置表单数
			this.setSheetnums(wb.getNumberOfSheets());
		} catch (Exception e) {
			log.info("构造导入程序出错:" + e.getMessage());
			this.sheetNums = 0;
		}
	}

	public ExecelImportUtil(File excelfile) {
		this.excelFile = excelfile;
		try {
			// 创建Excel工作书
			wb = new HSSFWorkbook(new FileInputStream(excelfile));
			// 设置表单数
			this.setSheetnums(wb.getNumberOfSheets());
		} catch (Exception e) {
			log.info("构造导入程序出错:" + e.getMessage());
			e.printStackTrace();
			this.sheetNums = 0;
		}
	}

	public ExecelImportUtil(String filename) {
		this.excelFile = new File(filename);

		try {
			// 创建Excel工作书
			wb = new HSSFWorkbook(new FileInputStream(excelFile));
			// 设置表单数
			this.setSheetnums(wb.getNumberOfSheets());
		} catch (Exception e) {
			log.info("构造导入程序出错:" + e.getMessage());
			this.sheetNums = 0;
		}
	}

	private void setSheetnums(int num) {
		this.sheetNums = num;
	}

	public int getSheetnums() {
		return this.sheetNums;
	}

	/**
	 * 会员现金充值EXCEL存入
	 */
	public List<ChargeImport> readChargeImportFromSheet(int beginrow) throws Exception {
		ArrayList<ChargeImport> filecontent = new ArrayList<ChargeImport>();
		try {
			int sheetindex = 0;
			// 获得Excel文件的表单名称
			if (this.sheetNums >= 0)
				sheetindex = 0;
			String tableName = wb.getSheetName(sheetindex);
			// 获得表单
			HSSFSheet sheet = wb.getSheet(tableName);
			// 获得行数
			int rowCount = sheet.getPhysicalNumberOfRows();
			// 按照列的顺序,获得文件的列名行的每列名称
			for (int i = beginrow; i < rowCount; i++) {
				DecimalFormat df = new DecimalFormat("#");
				HSSFRow row = sheet.getRow(i);
				ChargeImport obj = new ChargeImport();
				String name = getCellValue(row, (short) 0).trim();
				String username = "";
				if (row.getCell((short)1).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
					username = df.format(row.getCell((short)1).getNumericCellValue());
				}else{
					username = row.getCell((short)1).getStringCellValue();
				}
				String money = getCellValue(row, (short) 2).trim();
				if(null!=username&&!"".equals(username)&&null!=money&&!"".equals(money)){
					obj.setName(name);
					if(username.contains("E")){
						System.out.println("导入充值时会员交易帐号为非文本");
						return null;
					}
					obj.setUsername(username);
					try {
						obj.setMoney(Double.parseDouble(money));
					} catch (Exception e) {
						System.out.println("导入充值时金额格式化错误");
						return null;
					}
					obj.setMemo(getCellValue(row, (short) 3).trim());
					filecontent.add(obj);
				}
			}

		} catch (Exception e) {
			log.info("A POI error has occured.");
			e.printStackTrace();
			return null;
		}
		return filecontent;
	}
	
	/**
	 * 会员现金充值EXCEL存入
	 */
	public List<ChargeImport> readLXImportFromSheet(int beginrow) throws Exception {
		ArrayList<ChargeImport> filecontent = new ArrayList<ChargeImport>();
		try {
			int sheetindex = 0;
			// 获得Excel文件的表单名称
			if (this.sheetNums >= 0)
				sheetindex = 0;
			String tableName = wb.getSheetName(sheetindex);
			// 获得表单
			HSSFSheet sheet = wb.getSheet(tableName);
			// 获得行数
			int rowCount = sheet.getPhysicalNumberOfRows();
			// 按照列的顺序,获得文件的列名行的每列名称
			for (int i = beginrow; i < rowCount; i++) {
				DecimalFormat df = new DecimalFormat("#");
				HSSFRow row = sheet.getRow(i);
				ChargeImport obj = new ChargeImport();
				String name = getCellValue(row,(short)1).trim();
				String username = "";
				if (row.getCell((short)2).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
					username = df.format(row.getCell((short)2).getNumericCellValue());
				}else{
					username = row.getCell((short)2).getStringCellValue();
				}
				String money = getCellValue(row,(short)3).trim();
				if(null!=username&&!"".equals(username)&&null!=money&&!"".equals(money)){
					obj.setName(name);
					if(username.contains("E")){
						System.out.println("导入充值时会员交易帐号为非文本");
						return null;
					}
					obj.setUsername(username);
					try {
						obj.setMoney(Double.parseDouble(money));
					} catch (Exception e) {
						System.out.println("导入充值时金额格式化错误");
						return null;
					}
					obj.setMemo(getCellValue(row,(short)4).trim());
					filecontent.add(obj);
				}
			}

		} catch (Exception e) {
			log.info("A POI error has occured.");
			e.printStackTrace();
			return null;
		}
		return filecontent;
	}
	
	/**
	 * 兴易贷批量划拨
	 */
	public List<ChargeImport> readXYDImportFromSheet(int beginrow) throws Exception {
		ArrayList<ChargeImport> filecontent = new ArrayList<ChargeImport>();
		try {
			int sheetindex = 0;
			// 获得Excel文件的表单名称
			if (this.sheetNums >= 0)
				sheetindex = 0;
			String tableName = wb.getSheetName(sheetindex);
			// 获得表单
			HSSFSheet sheet = wb.getSheet(tableName);
			// 获得行数
			int rowCount = sheet.getPhysicalNumberOfRows();
			// 按照列的顺序,获得文件的列名行的每列名称
			for (int i = beginrow; i < rowCount; i++) {
				DecimalFormat df = new DecimalFormat("#");
				HSSFRow row = sheet.getRow(i);
				ChargeImport obj = new ChargeImport();
				String name = getCellValue(row,(short)2).trim();
				String username = "";
				if (row.getCell((short)1).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
					username = df.format(row.getCell((short)1).getNumericCellValue());
				}else{
					username = row.getCell((short)1).getStringCellValue();
				}
				String money = getCellValue(row,(short)3).trim();
				if(null!=username&&!"".equals(username)&&null!=money&&!"".equals(money)){
					obj.setName(name);
					if(username.contains("E")){
						System.out.println("导入充值时会员交易帐号为非文本");
						return null;
					}
					obj.setUsername(username);
					try {
						obj.setMoney(Double.parseDouble(money));
					} catch (Exception e) {
						System.out.println("导入充值时金额格式化错误");
						return null;
					}
					obj.setMemo(getCellValue(row,(short)4).trim());
					filecontent.add(obj);
				}
			}

		} catch (Exception e) {
			log.info("A POI error has occured.");
			e.printStackTrace();
			return null;
		}
		return filecontent;
	}
	
	public double getRound(double dSource) {
		double iRound;
		// BigDecimal的构造函数参数类型是double
		BigDecimal deSource = new BigDecimal(dSource);
		// deSource.setScale(0,BigDecimal.ROUND_HALF_UP) 返回值类型 BigDecimal
		// intValue() 方法将BigDecimal转化为int
		iRound = deSource.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return iRound;
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	private String getCellValue(HSSFRow row, short column) {
		String tempvalue = "";
		if (row.getCell(column) != null) {
			if (row.getCell(column).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
				tempvalue = String.valueOf(row.getCell(column).getNumericCellValue());
			else
				tempvalue = row.getCell(column).getStringCellValue();

		}
		return tempvalue;
	}
}
