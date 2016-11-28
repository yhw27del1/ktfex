package com.wisdoor.core.vo;

/****
 * 简单通用对象
 * @author  
 *
 */
public class CommonVo{
	private String id; 
	private String string1;
	private String string2; 
	private String string3;
	private String string4;
	private String string5;
	private int int1=0;
	private int int2=0;
	private int int3=0;
	private int int4=0;
	private Double db1=0.0;
	private Double db2=0.0;
	private Double db3=0.0;
	private Double db4=0.0;
	private Long lon1=0L;
	private Long lon2=0L; 
	private Long lon3=0L;
	private Long lon4=0L; 	
	public CommonVo() {  
	}
	
	public CommonVo(String string1) { 
		this.string1 = string1;
	}

	public CommonVo(String string1, String string2) { 
		this.string1 = string1;
		this.string2 = string2;
	}

	public CommonVo(String string1, String string2, String string3,
			String string4,String string5) { 
		this.string1 = string1;
		this.string2 = string2;
		this.string3 = string3;
		this.string4 = string4;
		this.string5 = string5;
	}

	public String getString1() {
		return string1;
	}
	public void setString1(String string1) {
		this.string1 = string1;
	}
	public String getString2() {
		return string2;
	}
	public void setString2(String string2) {
		this.string2 = string2;
	}
	public String getString3() {
		return string3;
	}
	public void setString3(String string3) {
		this.string3 = string3;
	}
	public String getString4() {
		return string4;
	}
	public void setString4(String string4) {
		this.string4 = string4;
	}
	public String getString5() {
		return string5;
	}
	public void setString5(String string5) {
		this.string5 = string5;
	}
	public int getInt1() {
		return int1;
	}
	public void setInt1(int int1) {
		this.int1 = int1;
	}
	public int getInt2() {
		return int2;
	}
	public void setInt2(int int2) {
		this.int2 = int2;
	}
	public int getInt3() {
		return int3;
	}
	public void setInt3(int int3) {
		this.int3 = int3;
	}
	public int getInt4() {
		return int4;
	}
	public void setInt4(int int4) {
		this.int4 = int4;
	}
	public Double getDb1() {
		return db1;
	}
	public void setDb1(Double db1) {
		this.db1 = db1;
	}
	public Double getDb2() {
		return db2;
	}
	public void setDb2(Double db2) {
		this.db2 = db2;
	}
	public Double getDb3() {
		return db3;
	}
	public void setDb3(Double db3) {
		this.db3 = db3;
	}
	public Double getDb4() {
		return db4;
	}
	public void setDb4(Double db4) {
		this.db4 = db4;
	}
	 

	public Long getLon1() {
		return lon1;
	}

	public void setLon1(Long lon1) {
		this.lon1 = lon1;
	}

	public Long getLon2() {
		return lon2;
	}

	public void setLon2(Long lon2) {
		this.lon2 = lon2;
	}

	public Long getLon3() {
		return lon3;
	}

	public void setLon3(Long lon3) {
		this.lon3 = lon3;
	}

	public Long getLon4() {
		return lon4;
	}

	public void setLon4(Long lon4) {
		this.lon4 = lon4;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
 
}
