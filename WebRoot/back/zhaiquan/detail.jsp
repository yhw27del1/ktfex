<%@ page language="java" import="java.util.*,com.kmfex.model.*" pageEncoding="utf-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>

<html>   
  <head>
    
    <title>债权详细信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css"/>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery-ui-1.7.2.custom.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery.chromatable.js"></script>
		<script>
			$(function(){
				$(".tt").chromatable({
					width: "100%",
					height: "auto",
					scrolling: "yes"
				});
			});
		</script>
  </head>
  
  <body style="margin:0;padding:0;overflow-x:hidden">
  	<s:if test="#request.isfrombuy == true">
	    <div style="margin:0 5px 30px 10px;">
	    	<div>
	    		<span>债权${ir.zhaiQuanCode}的转让信息</span>
	    		<table style="margin:20px 0;">
	    			<tr>
	    				<td>转让价格：<span>${con.price}</span>元</td>
	    				<td>转让日期：<span>${con.buyerDate}</span></td>
	    			</tr>
	    			<tr>
	    				<td colspan="2">转让手续费：<span>${con.buying.zqfwf}</span></td>
	    			</tr>
	    		</table>
	    		
	    	</div>
	    	<div style="margin:10px 0;">债权回收计划</div>
	    	<table class="tt">
	    		<thead>
	    			<tr>
		    			<th>还款期数</th>
		    			<th>还款日期</th>
		    			<th>应收本金</th>
		    			<th>应收利息</th>
		    			<th>罚金</th>
	    			</tr>
	    		</thead>
	    		<tbody>
	    			<s:iterator value="#request.prs" var="pr">
	    				<s:if test="#pr.state == 0">
		    				<tr>
		    					<td>${pr.succession}</td>
		    					<td>
		    						<s:if test="#pr.extension_period==null">
		    							<fmt:formatDate value="${pr.predict_repayment_date}" pattern="yyyy-MM-dd"/>
		    						</s:if>
		    						<s:else>
		    							<fmt:formatDate value="${pr.extension_period}" pattern="yyyy-MM-dd"/>
		    						</s:else>
		    					</td>
		    					<td>
		    						${pr.xybj}
		    					</td>
		    					<td>
		    						${pr.xylx}
		    					</td>
		    					<td>
		    						${pr.penal}
		    					</td>
		    				</tr>
	    				</s:if>
	    			</s:iterator>
	    		</tbody>
	    	</table>
	    </div>
    </s:if>
    
    
    <div style="margin:0 5px 0 10px;">
    	
    	<div>债权详细信息</div>
    	<table>
    		<tr>
   				<td>债权编号：<span>${ir.zhaiQuanCode}</span></td>
   				<td>项目编号：<span>${ir.financingBase.code}</span></td>
   			</tr>
   			<tr>
   				<td>债权本金：<span>${ir.investAmount}</span></td>
   				<td>借款年利率：<span>${ir.financingBase.rate}%</span></td>
   			</tr>
   			<tr>
   				<td>借款期限：
				<c:if test="${(ir.financingBase.interestDay)!= 0}">按日计息(${ir.financingBase.interestDay}天)</c:if>
			    <c:if test="${(ir.financingBase.interestDay)== 0}">${ir.financingBase.businessType.term}个月 </c:if>      				
 
   				<td>还款方式：<span>${ir.financingBase.businessType.returnPattern}</span></td>
   			</tr>
   			<tr>
   				<td>起息时间：
   					<%
   					SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
   					InvestRecord ir = (InvestRecord)request.getAttribute("ir");
   					Date time = ir.getContract().getInvestor_make_sure();
   					Calendar cal = Calendar.getInstance();
   					cal.setTime(time);
   					cal.add(Calendar.MONTH,1);
   					out.print(sdf.format(cal.getTime()));
   					%>
   				</td>
   				<td>还款结束时间：
   					<span>
   						<fmt:formatDate value="${ir.lastDate}" pattern="yyyy-MM-dd"/>
   					</span>
   				</td>
   			</tr>
    	</table>
    	<table class="tt">
    		<thead>
    			<tr><th colspan="6">还款情况表</th></tr>
    			<tr>
	    			<th>期数</th>
	    			<th>还款日期</th>
	    			<th>应还本金</th>
	    			<th>应还利息</th>
	    			<th>罚金</th>
	    			<th>还款情况</th>
    			</tr>
    		</thead>
    		<tbody>
    			<s:iterator value="#request.prs" var="pr">
    				<tr>
    					<td>${pr.succession}</td>
    					<td>
    						<s:if test="#pr.extension_period==null"><fmt:formatDate value="${pr.predict_repayment_date}" pattern="yyyy-MM-dd"/></s:if>
    						<s:else><fmt:formatDate value="${pr.extension_period}" pattern="yyyy-MM-dd"/></s:else>
    					</td>
    					<td>
    						${pr.xybj}
    					</td>
    					<td>
    						${pr.xylx}
    					</td>
    					<td>
    						${pr.penal}
    					</td>
    					<td>
    						<s:if test="#pr.state==0">未还款</s:if>
    						<s:elseif test="#pr.state==1">正常还款</s:elseif>
    						<s:elseif test="#pr.state==2">提前还款</s:elseif>
    						<s:elseif test="#pr.state==3">逾期还款</s:elseif>
    						<s:elseif test="#pr.state==4">担保代偿</s:elseif>
    					</td>
    				</tr>
    			</s:iterator>
    		</tbody>
    	</table>
    
    </div>
    
  </body>
</html>
