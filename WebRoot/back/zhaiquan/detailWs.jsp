<%@ page language="java" import="java.util.*,com.kmfex.model.*" pageEncoding="utf-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>

<html>
  <head>   
 		<style>
<!--
body{
 color:#FBF800;
 background-color:#000;
}
-->
</style>     
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
	    		<span  style="color:red">债权${ir.zhaiQuanCode}的转让信息</span>
	    		<table style="margin:20px 0;">
	    			<tr>
	    				<td><span style="color:#48E1DF;" align="center">转让价格：</span><span>${con.price}</span>元</td>
	    				<td><span style="color:#48E1DF;" align="center">转让日期：</span><span>${con.buyerDate}</span></td>
	    			</tr>
	    			<tr>
	    				<td colspan="2"><span style="color:#48E1DF;" align="center">转让手续费：</span><span>${con.buying.zqfwf}</span></td>
	    			</tr>
	    		</table>
	    		
	    	</div>
	    	<div  style="color:red;margin:10px 0;">债权回收计划</div>
	    	<table class="tt">
	    		<thead>
	    			<tr>
		    			<th><span style="color:#48E1DF;" align="center">还款期数</span></th>
		    			<th><span style="color:#48E1DF;" align="center">还款日期</span></th>
		    			<th><span style="color:#48E1DF;" align="center">应收本金</span></th>
		    			<th><span style="color:#48E1DF;" align="center">应收利息</span></th>
	    			</tr>
	    		</thead>
	    		<tbody>
	    			<s:iterator value="#request.prs" var="pr">
	    				<s:if test="#pr.predict_repayment_date >= #request.con.fbrq">
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
		    				</tr>
	    				</s:if>
	    			</s:iterator>
	    		</tbody>
	    	</table>
	    </div>
    </s:if>
    
    
    <div style="margin:0 5px 0 10px;">
    	
    	<div  style="color:red">债权详细信息</div>
    	<table>
    		<tr>
   				<td><span style="color:#48E1DF;" align="center">债权编号：</span><span>${ir.zhaiQuanCode}</span></td>
   				<td><span style="color:#48E1DF;" align="center">项目编号：</span><span>${ir.financingBase.code}</span></td>
   			</tr>
   			<tr>
   				<td><span style="color:#48E1DF;" align="center">债权本金：</span><span>${ir.investAmount}</span></td>
   				<td><span style="color:#48E1DF;" align="center">借款年利率：</span><span>${ir.financingBase.rate}%</span></td>
   			</tr>
   			<tr>
   				<td><span style="color:#48E1DF;" align="center">借款期限：</span>
				<c:if test="${(ir.financingBase.interestDay)!= 0}">按日计息(${ir.financingBase.interestDay}天)</c:if>
			    <c:if test="${(ir.financingBase.interestDay)== 0}">${ir.financingBase.businessType.term}个月 </c:if>      				
   	 			</td>
   				<td><span style="color:#48E1DF;" align="center">还款方式：</span><span>${ir.financingBase.businessType.returnPattern}</span></td>
   			</tr>
   			<tr>
   				<td><span style="color:#48E1DF;" align="center">起息时间：</span>
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
   				<td><span style="color:#48E1DF;" align="center">还款结束时间：</span>
   					<span>
   						<fmt:formatDate value="${ir.lastDate}" pattern="yyyy-MM-dd"/>
   					</span>
   				</td>
   			</tr>
    	</table>
    	<div  style="color:red">还款情况表</div>
    	<table class="tt">
    		<thead> 
    			<tr>
	    			<th><span style="color:#48E1DF;" align="center">期数</span></th>
	    			<th><span style="color:#48E1DF;" align="center">还款日期</span></th>
	    			<th><span style="color:#48E1DF;" align="center">应还本金</span></th>
	    			<th><span style="color:#48E1DF;" align="center">应还利息</span></th>
	    			<th><span style="color:#48E1DF;" align="center">还款情况</span></th>
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
    						<s:if test="#pr.state==0">未还款</s:if>
    						<s:elseif test="#pr.state==1">正常还款</s:elseif>
    						<s:elseif test="#pr.state==2">提前还款</s:elseif>
    						<s:elseif test="#pr.state==3">逾期还款</s:elseif>
    					</td>
    				</tr>
    			</s:iterator>
    		</tbody>
    	</table>
    
    </div>
    
  </body>
</html>
