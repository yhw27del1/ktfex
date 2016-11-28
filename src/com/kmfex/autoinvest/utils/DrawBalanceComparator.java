package com.kmfex.autoinvest.utils;

import java.util.Comparator;

import com.kmfex.autoinvest.vo.Draw;

public class DrawBalanceComparator implements Comparator<Draw> {

	public int compare(Draw o1, Draw o2) { 

		if (o1.getBalance() > o2.getBalance()) {  
			return 1;
		} else if (o1.getBalance() == o2.getBalance()) { 
			if (o1.getLevelScore() < o2.getLevelScore()) { 
				return 1;
			} 
			else if (o1.getLevelScore() == o2.getLevelScore()) { 
				return 0;
			} 
			else if (o1.getLevelScore() > o2.getLevelScore()) { 
				return -1; 
			}
		}else if (o1.getBalance() > o2.getBalance()) { 
			return -1;

		} 
		return -1;
	}

}