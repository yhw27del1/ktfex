package com.kmfex.autoinvest.utils;

import java.util.Comparator;

import com.kmfex.autoinvest.vo.Draw;

public class DrawLevelScoreComparator implements Comparator<Draw>{

	public int compare(Draw o1, Draw o2) {
		if(o1.getLevelScore() > o2.getLevelScore()){
			return 1;
		}else if(o1.getLevelScore() == o2.getLevelScore()){
			if (o1.getBalance()< o2.getBalance()) { 
				return 1;
			} 
			else if (o1.getBalance() == o2.getBalance()) { 
				return 0;
			} 
			else if (o1.getBalance() > o2.getBalance()) { 
				return -1; 
			}
			return 0;
		}else{
			return -1;
		}
		
	}
	
}