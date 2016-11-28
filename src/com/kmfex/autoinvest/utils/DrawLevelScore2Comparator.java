package com.kmfex.autoinvest.utils;

import java.util.Comparator;

import com.kmfex.autoinvest.vo.Draw;

public class DrawLevelScore2Comparator implements Comparator<Draw>{
     /**
      * 小金额者先成交
      */
	public int compare(Draw o1, Draw o2) {
		if(o1.getLevelScore() > o2.getLevelScore()){
			return 1;
		}else if(o1.getLevelScore() == o2.getLevelScore()){
			if (o1.getPrePrice()>o2.getPrePrice()) { 
				return 1;
			} 
			else if (o1.getPrePrice() == o2.getPrePrice()) { 
				return 0;
			} 
			else if (o1.getPrePrice() < o2.getPrePrice()) { 
				return -1; 
			}
			return 0;
		}else{
			return -1;
		}
		
	}
	
}