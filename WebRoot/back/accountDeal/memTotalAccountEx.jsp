<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
  response.setHeader("Content-Disposition", "inline;filename="+ request.getAttribute("msg")+".xls");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style>
   .xlsMoney{mso-number-format:"0\.0";}
   .xlsDate{mso-number-format:"yyyy\/mm\/dd hh\:mm\:ss";}
   .xlsText{mso-number-format:"\@";} 
</style>
<body>
 
	</head>
	<body style="text-align: center;"> 
		<table align="center" border="1" style="width:600px;">
			 <tr style="width: 100%;">
				<td align="center" colspan="6">
					<div>
					      会员资产情况(${msg})单位:元   
					</div>
				</td>
				</tr> 
			<thead>
				<tr>
					<th>
						序号
					</th>
                    <th>
						交易账户
					</th>
					<th>
						用户名
					</th>
					<th>
					    总资产
					</th>
					<th>
						可用余额
					</th>
					<th>
						冻结金额
					</th>
					<th>
						持有债权
					</th>
					<th>
					    借出笔数 
					</th>
					<th>
						借出总额
					</th>
					<th>
						已回收总笔数
					</th>
					<th>
						已回收总额
					</th>
				</tr>
			</thead>
			<tbody>
				
				<c:forEach items="${acs}" var="account" varStatus="xh">  
					<tr>
						<td>
							${xh.count}
						</td>
						<td>
							${account.string5}
						</td>
						<td>
						    <c:if test="${menuMap['name']=='inline'}">
								${${account.string6}}
							</c:if>
							<c:if test="${menuMap['name']!='inline'}">
								<c:choose>
									<c:when test="${fn:length(${account.string6}) == 0}">
										无
									</c:when>
									<c:when test="${fn:length(${account.string6}) > 1}">
										<c:out value="${fn:substring(${account.string6},0,1)}****" />
									</c:when>
									<c:otherwise>
										****
									</c:otherwise>
								</c:choose>
							</c:if>
						</td>  
	                   <td><fmt:formatNumber value='${account.totalAmount}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${account.balance}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${account.frozenAmount}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${account.cyzq}' type="currency" currencySymbol=""/></td> 
	                   	<td> ${account.string1} </td>  
	                   	<td><fmt:formatNumber value='${account.string2}' type="currency" currencySymbol=""/></td>  
	                   	<td> ${account.string3} </td>  
	                   	<td><fmt:formatNumber value='${account.string4}' type="currency" currencySymbol=""/></td>  
					</tr>
				</c:forEach>
					 <tr>
						<td align="center">   
						 合计:
						</td>  
						<td align="center"> 
						</td>  
						<td align="center">  
						</td> 
	                   <td><fmt:formatNumber value='${totalAmountSum}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${balanceSum}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${frozenAmountSum}' type="currency" currencySymbol=""/></td>
	                   <td><fmt:formatNumber value='${cyzqSum}' type="currency" currencySymbol=""/></td> 
	                   <td><fmt:formatNumber value='${count1}' type="currency" currencySymbol=""/></td> 
	                   <td><fmt:formatNumber value='${count2}' type="currency" currencySymbol=""/></td> 
	                   <td><fmt:formatNumber value='${count3}' type="currency" currencySymbol=""/></td> 
	                   <td><fmt:formatNumber value='${count4}' type="currency" currencySymbol=""/></td> 
					</tr>
			</tbody>

		</table>
	</body>
</html>
