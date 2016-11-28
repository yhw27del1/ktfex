package com.kmfex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.wisdoor.core.model.User;
/**
 * @author linuxp
 * */
@Entity
@Table(name = "t_DayCut",schema="KT")
public class DayCut {
	private String id;
	private Date createDate = new Date();
	private Date modifyDate;
	private User user;
	private int year = 2013;
	private int month = 1;
	
	private double balance_1 = 0d;
	private double frozen_1 = 0d;
	
	private double balance_2 = 0d;
	private double frozen_2 = 0d;
	
	private double balance_3 = 0d;
	private double frozen_3 = 0d;
	
	private double balance_4 = 0d;
	private double frozen_4 = 0d;
	
	private double balance_5 = 0d;
	private double frozen_5 = 0d;
	
	private double balance_6 = 0d;
	private double frozen_6 = 0d;
	
	private double balance_7 = 0d;
	private double frozen_7 = 0d;
	
	private double balance_8 = 0d;
	private double frozen_8 = 0d;
	
	private double balance_9 = 0d;
	private double frozen_9 = 0d;
	
	private double balance_10 = 0d;
	private double frozen_10 = 0d;
	
	private double balance_11 = 0d;
	private double frozen_11 = 0d;
	
	private double balance_12 = 0d;
	private double frozen_12 = 0d;
	
	private double balance_13 = 0d;
	private double frozen_13 = 0d;
	
	private double balance_14 = 0d;
	private double frozen_14 = 0d;
	
	private double balance_15 = 0d;
	private double frozen_15 = 0d;
	
	private double balance_16 = 0d;
	private double frozen_16 = 0d;
	
	private double balance_17 = 0d;
	private double frozen_17 = 0d;
	
	private double balance_18 = 0d;
	private double frozen_18 = 0d;
	
	private double balance_19 = 0d;
	private double frozen_19 = 0d;
	
	private double balance_20 = 0d;
	private double frozen_20 = 0d;
	
	private double balance_21 = 0d;
	private double frozen_21 = 0d;
	
	private double balance_22 = 0d;
	private double frozen_22 = 0d;
	
	private double balance_23 = 0d;
	private double frozen_23 = 0d;
	
	private double balance_24 = 0d;
	private double frozen_24 = 0d;
	
	private double balance_25 = 0d;
	private double frozen_25 = 0d;
	
	private double balance_26 = 0d;
	private double frozen_26 = 0d;
	
	private double balance_27 = 0d;
	private double frozen_27 = 0d;
	
	private double balance_28 = 0d;
	private double frozen_28 = 0d;
	
	private double balance_29 = 0d;
	private double frozen_29 = 0d;
	
	private double balance_30 = 0d;
	private double frozen_30 = 0d;
	
	private double balance_31 = 0d;
	private double frozen_31 = 0d;
	
	private int z_1 = 0;
	private int z_2 = 0;
	private int z_3 = 0;
	private int z_4 = 0;
	private int z_5 = 0;
	private int z_6 = 0;
	private int z_7 = 0;
	private int z_8 = 0;
	private int z_9 = 0;
	private int z_10 = 0;
	private int z_11 = 0;
	private int z_12 = 0;
	private int z_13 = 0;
	private int z_14 = 0;
	private int z_15 = 0;
	private int z_16 = 0;
	private int z_17 = 0;
	private int z_18 = 0;
	private int z_19 = 0;
	private int z_20 = 0;
	private int z_21 = 0;
	private int z_22 = 0;
	private int z_23 = 0;
	private int z_24 = 0;
	private int z_25 = 0;
	private int z_26 = 0;
	private int z_27 = 0;
	private int z_28 = 0;
	private int z_29 = 0;
	private int z_30 = 0;
	private int z_31 = 0;
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@ManyToOne
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public double getBalance_1() {
		return balance_1;
	}
	public void setBalance_1(double balance_1) {
		this.balance_1 = balance_1;
	}
	public double getFrozen_1() {
		return frozen_1;
	}
	public void setFrozen_1(double frozen_1) {
		this.frozen_1 = frozen_1;
	}
	public double getBalance_2() {
		return balance_2;
	}
	public void setBalance_2(double balance_2) {
		this.balance_2 = balance_2;
	}
	public double getFrozen_2() {
		return frozen_2;
	}
	public void setFrozen_2(double frozen_2) {
		this.frozen_2 = frozen_2;
	}
	public double getBalance_3() {
		return balance_3;
	}
	public void setBalance_3(double balance_3) {
		this.balance_3 = balance_3;
	}
	public double getFrozen_3() {
		return frozen_3;
	}
	public void setFrozen_3(double frozen_3) {
		this.frozen_3 = frozen_3;
	}
	public double getBalance_4() {
		return balance_4;
	}
	public void setBalance_4(double balance_4) {
		this.balance_4 = balance_4;
	}
	public double getFrozen_4() {
		return frozen_4;
	}
	public void setFrozen_4(double frozen_4) {
		this.frozen_4 = frozen_4;
	}
	public double getBalance_5() {
		return balance_5;
	}
	public void setBalance_5(double balance_5) {
		this.balance_5 = balance_5;
	}
	public double getFrozen_5() {
		return frozen_5;
	}
	public void setFrozen_5(double frozen_5) {
		this.frozen_5 = frozen_5;
	}
	public double getBalance_6() {
		return balance_6;
	}
	public void setBalance_6(double balance_6) {
		this.balance_6 = balance_6;
	}
	public double getFrozen_6() {
		return frozen_6;
	}
	public void setFrozen_6(double frozen_6) {
		this.frozen_6 = frozen_6;
	}
	public double getBalance_7() {
		return balance_7;
	}
	public void setBalance_7(double balance_7) {
		this.balance_7 = balance_7;
	}
	public double getFrozen_7() {
		return frozen_7;
	}
	public void setFrozen_7(double frozen_7) {
		this.frozen_7 = frozen_7;
	}
	public double getBalance_8() {
		return balance_8;
	}
	public void setBalance_8(double balance_8) {
		this.balance_8 = balance_8;
	}
	public double getFrozen_8() {
		return frozen_8;
	}
	public void setFrozen_8(double frozen_8) {
		this.frozen_8 = frozen_8;
	}
	public double getBalance_9() {
		return balance_9;
	}
	public void setBalance_9(double balance_9) {
		this.balance_9 = balance_9;
	}
	public double getFrozen_9() {
		return frozen_9;
	}
	public void setFrozen_9(double frozen_9) {
		this.frozen_9 = frozen_9;
	}
	public double getBalance_10() {
		return balance_10;
	}
	public void setBalance_10(double balance_10) {
		this.balance_10 = balance_10;
	}
	public double getFrozen_10() {
		return frozen_10;
	}
	public void setFrozen_10(double frozen_10) {
		this.frozen_10 = frozen_10;
	}
	public double getBalance_11() {
		return balance_11;
	}
	public void setBalance_11(double balance_11) {
		this.balance_11 = balance_11;
	}
	public double getFrozen_11() {
		return frozen_11;
	}
	public void setFrozen_11(double frozen_11) {
		this.frozen_11 = frozen_11;
	}
	public double getBalance_12() {
		return balance_12;
	}
	public void setBalance_12(double balance_12) {
		this.balance_12 = balance_12;
	}
	public double getFrozen_12() {
		return frozen_12;
	}
	public void setFrozen_12(double frozen_12) {
		this.frozen_12 = frozen_12;
	}
	public double getBalance_13() {
		return balance_13;
	}
	public void setBalance_13(double balance_13) {
		this.balance_13 = balance_13;
	}
	public double getFrozen_13() {
		return frozen_13;
	}
	public void setFrozen_13(double frozen_13) {
		this.frozen_13 = frozen_13;
	}
	public double getBalance_14() {
		return balance_14;
	}
	public void setBalance_14(double balance_14) {
		this.balance_14 = balance_14;
	}
	public double getFrozen_14() {
		return frozen_14;
	}
	public void setFrozen_14(double frozen_14) {
		this.frozen_14 = frozen_14;
	}
	public double getBalance_15() {
		return balance_15;
	}
	public void setBalance_15(double balance_15) {
		this.balance_15 = balance_15;
	}
	public double getFrozen_15() {
		return frozen_15;
	}
	public void setFrozen_15(double frozen_15) {
		this.frozen_15 = frozen_15;
	}
	public double getBalance_16() {
		return balance_16;
	}
	public void setBalance_16(double balance_16) {
		this.balance_16 = balance_16;
	}
	public double getFrozen_16() {
		return frozen_16;
	}
	public void setFrozen_16(double frozen_16) {
		this.frozen_16 = frozen_16;
	}
	public double getBalance_17() {
		return balance_17;
	}
	public void setBalance_17(double balance_17) {
		this.balance_17 = balance_17;
	}
	public double getFrozen_17() {
		return frozen_17;
	}
	public void setFrozen_17(double frozen_17) {
		this.frozen_17 = frozen_17;
	}
	public double getBalance_18() {
		return balance_18;
	}
	public void setBalance_18(double balance_18) {
		this.balance_18 = balance_18;
	}
	public double getFrozen_18() {
		return frozen_18;
	}
	public void setFrozen_18(double frozen_18) {
		this.frozen_18 = frozen_18;
	}
	public double getBalance_19() {
		return balance_19;
	}
	public void setBalance_19(double balance_19) {
		this.balance_19 = balance_19;
	}
	public double getFrozen_19() {
		return frozen_19;
	}
	public void setFrozen_19(double frozen_19) {
		this.frozen_19 = frozen_19;
	}
	public double getBalance_20() {
		return balance_20;
	}
	public void setBalance_20(double balance_20) {
		this.balance_20 = balance_20;
	}
	public double getFrozen_20() {
		return frozen_20;
	}
	public void setFrozen_20(double frozen_20) {
		this.frozen_20 = frozen_20;
	}
	public double getBalance_21() {
		return balance_21;
	}
	public void setBalance_21(double balance_21) {
		this.balance_21 = balance_21;
	}
	public double getFrozen_21() {
		return frozen_21;
	}
	public void setFrozen_21(double frozen_21) {
		this.frozen_21 = frozen_21;
	}
	public double getBalance_22() {
		return balance_22;
	}
	public void setBalance_22(double balance_22) {
		this.balance_22 = balance_22;
	}
	public double getFrozen_22() {
		return frozen_22;
	}
	public void setFrozen_22(double frozen_22) {
		this.frozen_22 = frozen_22;
	}
	public double getBalance_23() {
		return balance_23;
	}
	public void setBalance_23(double balance_23) {
		this.balance_23 = balance_23;
	}
	public double getFrozen_23() {
		return frozen_23;
	}
	public void setFrozen_23(double frozen_23) {
		this.frozen_23 = frozen_23;
	}
	public double getBalance_24() {
		return balance_24;
	}
	public void setBalance_24(double balance_24) {
		this.balance_24 = balance_24;
	}
	public double getFrozen_24() {
		return frozen_24;
	}
	public void setFrozen_24(double frozen_24) {
		this.frozen_24 = frozen_24;
	}
	public double getBalance_25() {
		return balance_25;
	}
	public void setBalance_25(double balance_25) {
		this.balance_25 = balance_25;
	}
	public double getFrozen_25() {
		return frozen_25;
	}
	public void setFrozen_25(double frozen_25) {
		this.frozen_25 = frozen_25;
	}
	public double getBalance_26() {
		return balance_26;
	}
	public void setBalance_26(double balance_26) {
		this.balance_26 = balance_26;
	}
	public double getFrozen_26() {
		return frozen_26;
	}
	public void setFrozen_26(double frozen_26) {
		this.frozen_26 = frozen_26;
	}
	public double getBalance_27() {
		return balance_27;
	}
	public void setBalance_27(double balance_27) {
		this.balance_27 = balance_27;
	}
	public double getFrozen_27() {
		return frozen_27;
	}
	public void setFrozen_27(double frozen_27) {
		this.frozen_27 = frozen_27;
	}
	public double getBalance_28() {
		return balance_28;
	}
	public void setBalance_28(double balance_28) {
		this.balance_28 = balance_28;
	}
	public double getFrozen_28() {
		return frozen_28;
	}
	public void setFrozen_28(double frozen_28) {
		this.frozen_28 = frozen_28;
	}
	public double getBalance_29() {
		return balance_29;
	}
	public void setBalance_29(double balance_29) {
		this.balance_29 = balance_29;
	}
	public double getFrozen_29() {
		return frozen_29;
	}
	public void setFrozen_29(double frozen_29) {
		this.frozen_29 = frozen_29;
	}
	public double getBalance_30() {
		return balance_30;
	}
	public void setBalance_30(double balance_30) {
		this.balance_30 = balance_30;
	}
	public double getFrozen_30() {
		return frozen_30;
	}
	public void setFrozen_30(double frozen_30) {
		this.frozen_30 = frozen_30;
	}
	public double getBalance_31() {
		return balance_31;
	}
	public void setBalance_31(double balance_31) {
		this.balance_31 = balance_31;
	}
	public double getFrozen_31() {
		return frozen_31;
	}
	public void setFrozen_31(double frozen_31) {
		this.frozen_31 = frozen_31;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_1() {
		return z_1;
	}
	public void setZ_1(int z_1) {
		this.z_1 = z_1;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_2() {
		return z_2;
	}
	public void setZ_2(int z_2) {
		this.z_2 = z_2;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_3() {
		return z_3;
	}
	public void setZ_3(int z_3) {
		this.z_3 = z_3;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_4() {
		return z_4;
	}
	public void setZ_4(int z_4) {
		this.z_4 = z_4;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_5() {
		return z_5;
	}
	public void setZ_5(int z_5) {
		this.z_5 = z_5;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_6() {
		return z_6;
	}
	public void setZ_6(int z_6) {
		this.z_6 = z_6;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_7() {
		return z_7;
	}
	public void setZ_7(int z_7) {
		this.z_7 = z_7;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_8() {
		return z_8;
	}
	public void setZ_8(int z_8) {
		this.z_8 = z_8;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_9() {
		return z_9;
	}
	public void setZ_9(int z_9) {
		this.z_9 = z_9;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_10() {
		return z_10;
	}
	public void setZ_10(int z_10) {
		this.z_10 = z_10;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_11() {
		return z_11;
	}
	public void setZ_11(int z_11) {
		this.z_11 = z_11;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_12() {
		return z_12;
	}
	public void setZ_12(int z_12) {
		this.z_12 = z_12;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_13() {
		return z_13;
	}
	public void setZ_13(int z_13) {
		this.z_13 = z_13;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_14() {
		return z_14;
	}
	public void setZ_14(int z_14) {
		this.z_14 = z_14;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_15() {
		return z_15;
	}
	public void setZ_15(int z_15) {
		this.z_15 = z_15;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_16() {
		return z_16;
	}
	public void setZ_16(int z_16) {
		this.z_16 = z_16;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_17() {
		return z_17;
	}
	public void setZ_17(int z_17) {
		this.z_17 = z_17;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_18() {
		return z_18;
	}
	public void setZ_18(int z_18) {
		this.z_18 = z_18;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_19() {
		return z_19;
	}
	public void setZ_19(int z_19) {
		this.z_19 = z_19;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_20() {
		return z_20;
	}
	public void setZ_20(int z_20) {
		this.z_20 = z_20;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_21() {
		return z_21;
	}
	public void setZ_21(int z_21) {
		this.z_21 = z_21;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_22() {
		return z_22;
	}
	public void setZ_22(int z_22) {
		this.z_22 = z_22;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_23() {
		return z_23;
	}
	public void setZ_23(int z_23) {
		this.z_23 = z_23;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_24() {
		return z_24;
	}
	public void setZ_24(int z_24) {
		this.z_24 = z_24;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_25() {
		return z_25;
	}
	public void setZ_25(int z_25) {
		this.z_25 = z_25;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_26() {
		return z_26;
	}
	public void setZ_26(int z_26) {
		this.z_26 = z_26;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_27() {
		return z_27;
	}
	public void setZ_27(int z_27) {
		this.z_27 = z_27;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_28() {
		return z_28;
	}
	public void setZ_28(int z_28) {
		this.z_28 = z_28;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_29() {
		return z_29;
	}
	public void setZ_29(int z_29) {
		this.z_29 = z_29;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_30() {
		return z_30;
	}
	public void setZ_30(int z_30) {
		this.z_30 = z_30;
	}
	@Column(columnDefinition="number(10) default 1")
	public int getZ_31() {
		return z_31;
	}
	public void setZ_31(int z_31) {
		this.z_31 = z_31;
	}
}
