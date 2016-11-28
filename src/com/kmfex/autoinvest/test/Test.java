package com.kmfex.autoinvest.test;


public class Test {
	/**
	* @param args
	*/
	public static void main(String[] args) {
		   
		int [] arr4 = {1,2,3,4}; //产生0-(arr.length-1)的整数值,也是数组的索引
		int index=(int)(Math.random()*arr4.length); 
		//int rand = arr4[index];
	    //System.out.println("index="+index+",rand="+rand); 
	    String str="";
	    for(int i=0;i<index+1;i++){
	    	str+=arr4[i]+",";
	    } 
	    System.out.println("str4="+str); 
	    
	    int [] arr31 = {1,2,3}; //产生0-(arr.length-1)的整数值,也是数组的索引
	    int index31=(int)(Math.random()*arr31.length); 
	    //System.out.println("index31="+index31+",rand31="+rand31); 
	    String str31="";
	    for(int i=0;i<index31+1;i++){
	    	str31+=arr31[i]+",";
	    } 
	    System.out.println("str31="+str31); 
	    
	    int [] arr41 = {1,2,3}; //产生0-(arr.length-1)的整数值,也是数组的索引
	    int index41=(int)(Math.random()*arr41.length); 
	    //System.out.println("index41="+index41+",rand31="+rand41); 
	    String str41="";
	    for(int i=0;i<index41+1;i++){
	    	str41+=arr41[i]+",";
	    } 
	    System.out.println("str41="+str41); 
	    
	    
	
	}

}
