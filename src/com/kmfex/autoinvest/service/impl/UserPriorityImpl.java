package com.kmfex.autoinvest.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kmfex.Constant;
import com.kmfex.InvestVO;
import com.kmfex.MoneyFormat;
import com.kmfex.autoinvest.model.UserParameter;
import com.kmfex.autoinvest.model.UserPriority;
import com.kmfex.autoinvest.service.UserPriorityService;
import com.kmfex.autoinvest.utils.AutoInvestUtils;
import com.kmfex.autoinvest.utils.DrawBalanceComparator;
import com.kmfex.autoinvest.utils.DrawLevelScore2Comparator;
import com.kmfex.autoinvest.utils.DrawLevelScoreComparator;
import com.kmfex.autoinvest.vo.Draw;
import com.kmfex.model.BusinessType;
import com.kmfex.model.FinancingBase;
import com.kmfex.model.MemberBase;
import com.kmfex.service.BusinessTypeService;
import com.kmfex.service.InvestRecordService;
import com.kmfex.service.MemberBaseService;
import com.kmfex.webservice.vo.MessageTip;
import com.wisdoor.core.exception.EngineException;
import com.wisdoor.core.service.impl.BaseServiceImpl;
import com.wisdoor.core.utils.DoubleUtils;

/**
 * 
 *   
 */
@Service
public class UserPriorityImpl extends BaseServiceImpl<UserPriority> implements UserPriorityService { 
	@Resource
	transient  InvestRecordService investRecordService; 
	@Resource
	transient  BusinessTypeService businessTypeService; 
	@Resource
	transient  MemberBaseService memberBaseService;
	 
	@Override
	public Boolean firstAutoInvest(String username) {
		try {
			UserPriority userPriority=this.selectById(username); 
			if(null==userPriority){ 
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	} 
	@SuppressWarnings("unchecked")
	@Override
	public List<Draw> draw1(Map<String, Object> map,Double finacingMoney) {
		Long n=(Long)map.get("n");  
		Double w=Double.valueOf(map.get("w").toString()); //委托金额
		List<Draw> draws=(ArrayList<Draw>)map.get("draws"); 
		//System.out.println("n="+n+",w="+w+",x="+finacingMoney);
		if(n>=200){//投标人数
			if(w>=finacingMoney){
				return drawsForMdayuX11(draws,finacingMoney);//抽签
			}
		}else{//投标人数<200 
			if(w==finacingMoney||w<finacingMoney){
				Collections.sort(draws, new DrawLevelScoreComparator());
				return draws;
			}else if(w>finacingMoney){
				return drawsForMdayuX22(draws,finacingMoney,w);//抽签
			}else{} 
		} 
		return null;
	} 

	/**
	 * 当    N>=200(先优先级后小金额)  
	 * @param draws  符合满足条件的人
	 * @param finacingMoney 融资金额
	 * @return
	 */
	private List<Draw> drawsForMdayuX11(List<Draw> draws,double finacingMoney){  
		//临时对象
		List<Draw> tempDraws=new ArrayList<Draw>(); //存在200个的中签对象
		Draw tempDraw=null;//单个临时对象
		Double tempWtotal=0d;//临时对象金额汇总
		Collections.sort(draws, new DrawLevelScoreComparator());
		int size=draws.size();
		Draw repDraw=null;//准备替换的人
		for(int i=0;i<199;i++){
			tempDraw=draws.get(i);
			if(size>=200&i==198){ 
				for(int j=198;j>-1;j--){
					repDraw=draws.get(j); 
					if(repDraw.getMax()>2*tempDraw.getMin()){
						repDraw.setPrePrice(2*tempDraw.getMin()); 
						break; //调出循环 
					}
			  } 
			}else{
				tempDraw.setPrePrice(tempDraw.getMin()); 
			}
			tempDraws.add(tempDraw);
			tempWtotal+=tempDraw.getMax();
			//System.out.println("先优先级后小金额的第"+(i+1)+"人,username="+tempDraw.getUsername()+","+tempDraw.getBalance());
		}
		
		//避免均等分，替换掉某个人
		if(null!=repDraw){
			for(Draw dw:tempDraws){
				if(dw.getUsername()==repDraw.getUsername()){
					dw=repDraw;
					break; //调出循环 
				}
			}
		}
		
		if(tempWtotal==finacingMoney){
			return tempDraws;
		}else{//W>X
			List<Draw> newDraws=new ArrayList<Draw>();  
			for(Draw dw:tempDraws){
				if(dw.getPrePrice()>finacingMoney){
					dw.setPrePrice(finacingMoney);
					newDraws.add(dw);
					break; //投标满调出循环 
				}else{
					finacingMoney-=dw.getPrePrice();
					newDraws.add(dw);
				} 
				if(finacingMoney<=0){break;} //投标满调出循环 
			}
			Collections.sort(newDraws, new DrawLevelScoreComparator());
			return newDraws;
		} 
	}

	/**
	 * 当    N<200(先优先级，优先级相同取金额小的)  
	 * @param draws  符合满足条件的人
	 * @param finacingMoney 融资金额
	 * @param w 委托金额
	 * @return
	 */
	private List<Draw> drawsForMdayuX22(List<Draw> draws,double finacingMoney,double w){
		List<Draw> newDraws=new ArrayList<Draw>(); 
		if(w==finacingMoney||w<finacingMoney){
			return draws;
		}else{//W>X 
			//调用排序类     
			Collections.sort(draws, new DrawLevelScore2Comparator());
			for(Draw dw:draws){  
			/*	if("53030100171".equals(dw.getUsername()))
				    System.out.println(finacingMoney+"11111111111111---->"+dw.getUsername()+"--->min="+dw.getMin()+"--->max="+dw.getMax()+"--->当个包上限param9="+dw.getParam9()+"--->可用余额不低于param8="+dw.getParam8()+"--->prePrice="+dw.getPrePrice()+"--->balance="+dw.getBalance());
				 */
				if(dw.getPrePrice()>finacingMoney){
					dw.setPrePrice(finacingMoney);
					newDraws.add(dw);
					break; //投标满调出循环 
				}else{
					finacingMoney-=dw.getPrePrice();
					dw.setPrePrice(dw.getPrePrice());
					newDraws.add(dw);
				} 
				if(finacingMoney<=0){break;} //投标满调出循环 
				
			} 
			//Collections.sort(newDraws, new DrawLevelScore2Comparator());
			return newDraws;
		}
		
	}
	@Transactional
	public void autoInvest(List<Draw> draws, FinancingBase financingBase){
		MemberBase member=null;
		SimpleDateFormat sFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		financingBase.setFromApp("autoinvest");
	 
		for(Draw dw:draws){ 
			member=memberBaseService.selectById(dw.getMemberid());

			MessageTip stip2=this.investRecordService.investNew3(member, dw.getPrePrice(), financingBase, 0);
			if (stip2.isSuccess()){  
				//自动投标执行成功，变动优先级UserPriority.levelScore，和更新UserPriority.lastTime
				UserPriority up=this.selectById(member.getUser().getUsername());
				up.setLastTime(Long.parseLong(sFormat.format(new Date())));
				if(dw.getMax()==dw.getPrePrice()){
					up.setLevelScore(up.getLevelScore()+10);
				}else{
					up.setLevelScore(up.getLevelScore()+DoubleUtils.doubleCheck((dw.getPrePrice()/dw.getMax())*10, 2));
				}
				try {
					//扣减委托金额 
					UserParameter para=up.getUserParameter();
					Double chaDouble=para.getParam8()-dw.getPrePrice();
					if(chaDouble<=0)chaDouble=0d;
					para.setParam8(chaDouble);
					//更新优先级
					this.update(up);
				} catch (EngineException e) {  
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public InvestVO getMaxMin(String in_userName, String in_financingBaseId,String in_usertype) {

		InvestVO invest = new InvestVO(); 
		Map st=this.investRecordService.callProcedureForDocheck(in_userName, in_financingBaseId, in_usertype); 
		BigDecimal out_frozenamount=new BigDecimal(0);
		BigDecimal out_MAXAMOUNT=new BigDecimal(0);
		BigDecimal out_HIGHPERCENT=new BigDecimal(0);
		BigDecimal out_LOWESTMONEY=new BigDecimal(0);
		BigDecimal out_HaveInvestNum=new BigDecimal(0);
		BigDecimal out_BJYE=new BigDecimal(0);
		BigDecimal out_INVESTAMOUNT=new BigDecimal(0);
		BigDecimal out_balance = (BigDecimal) st.get("3");
		out_frozenamount = (BigDecimal) st.get("4");
		BigDecimal out_CURCANINVEST = (BigDecimal) st.get("5");
		BigDecimal out_CURRENYAMOUNT = (BigDecimal) st.get("6");
		out_MAXAMOUNT = (BigDecimal) st.get("7");
		out_HIGHPERCENT = (BigDecimal) st.get("8");
		out_LOWESTMONEY = (BigDecimal) st.get("9");
		out_HaveInvestNum = (BigDecimal) st.get("10");
		out_BJYE = (BigDecimal) st.get("11");
		out_INVESTAMOUNT = (BigDecimal) st.get("12"); 
		
		String out_fbCode= (String) st.get("20"); 
		 
        if(out_CURCANINVEST==null){out_CURCANINVEST=new BigDecimal(0);}
        if(out_HaveInvestNum==null){out_HaveInvestNum=new BigDecimal(0);}
		double can = Double.parseDouble(out_CURCANINVEST.toString());// financingBase.getCurCanInvest();

		/**
		 * 最小融资额=剩余融资额/(200-投标人数)
		 */
		double minFinancing = Double.parseDouble(out_CURCANINVEST.toString()) / (200 - Integer.parseInt(out_HaveInvestNum.toString()));// financingBase.getCurCanInvest()
																																		// /(200-financingBase.getHaveInvestNum());
		if(out_LOWESTMONEY==null){out_LOWESTMONEY=new BigDecimal(0);}
		double minCondition = Double.parseDouble(out_LOWESTMONEY.toString());// condition.getLowestMoney();//
																				// 约束的最小融资额

		// (融资额%200)与约束的最小融资额比较取大者；2012-6-11改成取小者 2012-6-21改成取大者
		double min = minFinancing < minCondition ? minCondition : minFinancing;
		if (min < 1000) {
			min = 1000.00;
		}

		double b1 = Double.parseDouble(out_balance.toString()) + Double.parseDouble(out_frozenamount.toString()) + Double.parseDouble(out_BJYE.toString());// account.getTotalAmount();
		double b2 = Double.parseDouble(out_INVESTAMOUNT.toString());// this.investRecordService.investHistory2(m,
																	// financingBase);//
																	// 当前用户对当前融资项目已经投标的金额

		double max = 0d;
		double highPercent = Double.parseDouble(out_HIGHPERCENT.toString());

		if (Constant.MAX_INVEST.equals("F")) {// 根据融资项目可融资额*会员级别的比例
			max = (Double.parseDouble(out_MAXAMOUNT.toString())) * (highPercent / 100);
			max = max - b2;
		} else {
			// 约束的最大融资额（当前用户已经投标的金额+帐号余额）*会员级别的比例
			max = b1 * (highPercent / 100);
		}
		
		if(out_fbCode.startsWith("H")){
			max = 5000;
			max = max - b2;
			min = 1000;
		}
		
		// 保留千位
		min = DoubleUtils.doubleToQian(min);
		max = DoubleUtils.doubleToQian(max);

		if (max > can) {
			max = can;
		}
		if (min > can) {
			min = can;
		}
		/**新加代码**/
		double balance = Double.parseDouble(out_balance.toString());
		if(max > balance){
			max = AutoInvestUtils.doubleToQian(balance);
		}
		
		if (max <= min) {
			min = max;
		}
		if (max <= 0) {
			min = 0;
			max = 0;
		}
		if(min <1000d){
			min = 1000d;
		}

		invest.setMinMoney(min);
		invest.setMinMoney_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(min, 3), false));
		invest.setMaxMoney(max);
		invest.setMaxMoney_daxie(MoneyFormat.format(DoubleUtils.doubleCheck2(max, 3), false));

		return invest;
	}
    //转换Param2为查询参数  
    public  String[] ConverterParam2(BusinessType bt) { 
    	String[] arr={"0","0"};
    	if("day".equals(bt.getId())){//到期一次还本付息
    		arr[1]="4,";
    		arr[0]="day";
    		return arr;
    	}
    	if(null!=bt){
    		String nameStr=bt.getReturnPattern();
    		if("到期一次还本付息".equals(nameStr)){
    			arr[1]="4,";
        		arr[0]=bt.getTerm()+"";
        		return arr;
    		}else if("按月等额本息".equals(nameStr)){
    			arr[1]="1,";
        		arr[0]=bt.getTerm()+"";
        		return arr;
    		}else if("按月等额还息,到期一次还本".equals(nameStr)){
    			arr[1]="3,";
        		arr[0]=bt.getTerm()+"";
        		return arr;
    		}else if("按月等本等息".equals(nameStr)){
    			arr[1]="2,";
        		arr[0]=bt.getTerm()+"";
        		return arr;
    		}
    	}
    	return arr; 
     
    }
    
	/**
	 * 当    N>=200(先随机再可用余额后优先级)  
	 * @param draws  符合满足条件的人
	 * @param finacingMoney 融资金额
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Draw> drawsForMdayuX1(List<Draw> draws,double finacingMoney){ 
		int size=draws.size();
		
		if(size>250){//减少抽签范围，让优先级高的抽中机率更大
			size=250;
		}
		
		//1、随机抽出200
		int[] ran200s=AutoInvestUtils.randomArray(0, size-1, 198);
		//临时对象
		List<Draw> tempDraws=new ArrayList<Draw>(); //存在200个的中签对象
		Draw tempDraw=null;//单个临时对象
		Double tempWtotal=0d;//临时对象金额汇总
		
		for(int i:ran200s){
			tempDraw=draws.get(i);
			tempDraw.setPrePrice(tempDraw.getMax());
			tempDraws.add(tempDraw);
			tempWtotal+=tempDraw.getMax();
			System.out.println("随机的第"+(i+1)+"人,username="+tempDraw.getUsername()+","+tempDraw.getBalance());
		}
	
		if(tempWtotal==finacingMoney){
			return tempDraws;
		}else{//W>X
			List<Draw> newDraws=new ArrayList<Draw>(); 
			//调用排序类    
			Collections.sort(tempDraws, new DrawBalanceComparator());
			for(Draw dw:tempDraws){
				if(dw.getMax()>finacingMoney){
					dw.setPrePrice(finacingMoney);
					newDraws.add(dw);
					break; //投标满调出循环 
				}else{
					finacingMoney-=dw.getMax();
					newDraws.add(dw);
				} 
				if(finacingMoney<=0){break;} //投标满调出循环 
			}
			return newDraws;
		} 
	}
	/**
	 * 当    N<200(大金额优先，金额相同取优先级大的)    
	 * @param draws  符合满足条件的人
	 * @param finacingMoney 融资金额
	 * @param w 委托金额
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Draw> drawsForMdayuX2(List<Draw> draws,double finacingMoney,double w){
		List<Draw> newDraws=new ArrayList<Draw>(); 
		if(w==finacingMoney||w<finacingMoney){
			return draws;
		}else{//W>X 
			//调用排序类     
			Collections.sort(draws, new DrawBalanceComparator());
			for(Draw dw:draws){  
				if(dw.getMax()>finacingMoney){
					dw.setPrePrice(finacingMoney);
					newDraws.add(dw);
					break; //投标满调出循环 
				}else{
					finacingMoney-=dw.getMax();
					newDraws.add(dw);
				} 
				if(finacingMoney<=0){break;} //投标满调出循环 
				
			} 
			return newDraws;
		}
		 
	}
}
