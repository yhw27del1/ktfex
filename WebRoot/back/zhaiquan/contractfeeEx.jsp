<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
  response.setHeader("Content-Disposition", "inline;filename="+ request.getAttribute("msg")+".xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style>
   .xlsMoney{mso-number-format:"0\.00";}
   .xlsDate{mso-number-format:"yyyy\/mm\/dd";}
   .xlsText{mso-number-format:"\@";} 
</style>
<body>
<form action="">
 

	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
					 <tr style="width: 100%;">
				<td align="center" colspan="11">
					<div>
					   昆投互联网金融交易-债权转让费用明细表
					</div>
				</td>
				</tr>  
		</table>
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					    <th rowspan="3">
							序号
						</th>
 						 <th rowspan="3">
							成交日期
						 </th> 
						<th rowspan="3">
							成交价
						</th> 
						<th rowspan="3">
							费率(%)
						</th>  
						<th style="text-align:center" colspan="2">手续费</th>
					
					 <th style="text-align:center" colspan="4">会员信息</th>
					 
						<th rowspan="3">
							协议编号
						</th> 
				     <th rowspan="3">
				         备注
					 </th>  
				</tr>
				<tr class="ui-widget-header ">
						<th style="text-align:center" rowspan="2">出让方</th>
						<th style="text-align:center" rowspan="2">受让方</th>
						
						 <th style="text-align:center" colspan="2">出让方</th> 
					     <th style="text-align:center" colspan="2">受让方</th> 
						
					</tr>
				 <tr class="ui-widget-header ">
				    <th style="text-align:center">交易账户</th>
			  		<th style="text-align:center">户名</th> 
				    <th style="text-align:center">交易账户</th>
			  		<th style="text-align:center">户名</th> 
			  	</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry" varStatus="index">
						<tr>
							<td>
								 ${index.count} 
							</td> 
					       <td class='xlsDate'>
								<fmt:formatDate value='${entry.buyerDate}' pattern="yyyy-MM-dd" />
							</td> 
							<td>
								<fmt:formatNumber value='${entry.price}'  pattern='##.##' minFractionDigits='2'  /> 
							</td> 
					       <td><fmt:formatNumber value='${entry.percentSell}'  pattern='##.##' minFractionDigits='2'  /></td>  
					        <td><fmt:formatNumber value='${entry.selling.zqfwf}'  pattern='##.##' minFractionDigits='2'  /></td>
					        <td><fmt:formatNumber value='${entry.buying.zqfwf}'  pattern='##.##' minFractionDigits='2'  /></td>
					        <td>${entry.seller.username}</td> 
					        <td>${entry.seller.realname}</td>  
					        <td>${entry.buyer.username}</td> 
					        <td>${entry.buyer.realname}</td>  
                            <td>${entry.xieyiCode}</td> 
							
                            <td></td> 
						</tr>
					</c:forEach>
					     <tr>
							<td>合计</td>
							<td></td> 
							<td>
								<fmt:formatNumber value='${price}' type="currency" currencySymbol="" />
							</td> 
					        <td> </td> 
					        <td><fmt:formatNumber value='${zqfwf_s}' type="currency" currencySymbol="" /></td> 
					        <td><fmt:formatNumber value='${zqfwf_b}' type="currency" currencySymbol="" /></td>    

							<td></td> 
							<td></td>
							<td></td> 
							<td></td> 
							<td></td> 
							<td></td>   
				</tr>
		</table>		
	</div>
	</form>
</body> 