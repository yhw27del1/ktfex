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
   .xlsDate1{mso-number-format:"yyyy-mm-dd";}
   .xlsText{mso-number-format:"\@";} 
</style>
<body>
 
	</head>
	<body style="text-align: center;"> 
		<table align="center" border="1" style="width:600px;">
			 <tr style="width: 100%;">
				<td align="center" colspan="10">
					<div>
					      (<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />:<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"  />)待还款记录查询(单位:元)  
					</div>
				</td>
				</tr> 
			<thead>
				<tr>
				<th>项目编号</th>
				<th>年利率</th> 
				<th>还款方式</th>  
				<th>融资方帐号</th>
				<th>融资方银行帐号</th>
				<th>融资方</th>
				<th>投标方帐号</th>
				<th>投标方</th>
				<th style="text-align: right;">投标金额</th>
				<th>应还总额</th>
				<th>应还日期</th>
				</tr>
			</thead>
			<tbody>
		<c:forEach items="${pageView.records}" var="iter">
			<tr> 
				<td>${iter.investRecord.financingBase.code}</td>
				<td >${iter.investRecord.financingBase.rate}%</td> 
				<td >
				<c:if test="${(iter.investRecord.financingBase.interestDay)!= 0}">按日计息(${iter.investRecord.financingBase.interestDay}天)</c:if>
			    <c:if test="${(iter.investRecord.financingBase.interestDay)== 0}">${iter.investRecord.financingBase.businessType.term}个月 </c:if>    						
                </td>
			 	<td>${iter.investRecord.financingBase.financier.user.username}</td>
				<td class="xlsText">
					<c:if test="${menuMap['bankcard']=='inline'}">
						${iter.investRecord.financingBase.financier.bankAccount}
					</c:if>
					<c:if test="${menuMap['bankcard']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(iter.investRecord.financingBase.financier.bankAccount) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(iter.investRecord.financingBase.financier.bankAccount) > 4}">
								<c:out value="****${fn:substring(iter.investRecord.financingBase.financier.bankAccount,fn:length(iter.investRecord.financingBase.financier.bankAccount)-4,-1)}" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
				</td> 
				<td>
				<c:if test="${iter.investRecord.financingBase.financier.category=='1'}">
					<c:if test="${menuMap['name']=='inline'}">
						${iter.investRecord.financingBase.financier.pName}
					</c:if>
					<c:if test="${menuMap['name']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(iter.investRecord.financingBase.financier.pName) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(iter.investRecord.financingBase.financier.pName) > 1}">
								<c:out value="${fn:substring(iter.investRecord.financingBase.financier.pName,0,1)}****" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:if>
				<c:if test="${iter.investRecord.financingBase.financier.category!='1'}">
					<c:if test="${menuMap['name']=='inline'}">
						${iter.investRecord.financingBase.financier.eName}
					</c:if>
					<c:if test="${menuMap['name']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(iter.investRecord.financingBase.financier.eName) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(iter.investRecord.financingBase.financier.eName) > 1}">
								<c:out value="${fn:substring(iter.investRecord.financingBase.financier.eName,0,1)}****" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:if>
				</td>
				<td>
					${iter.investRecord.investor.user.username}
				</td>
				<td>
				<c:if test="${iter.investRecord.investor.category=='0'}">
					<c:if test="${menuMap['name']=='inline'}">
						${iter.investRecord.investor.eName}
					</c:if>
					<c:if test="${menuMap['name']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(iter.investRecord.investor.eName) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(iter.investRecord.investor.eName) > 1}">
								<c:out value="${fn:substring(iter.investRecord.investor.eName,0,1)}****" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:if>
				<c:if test="${iter.investRecord.investor.category!='0'}">
					<c:if test="${menuMap['name']=='inline'}">
						${iter.investRecord.investor.pName}
					</c:if>
					<c:if test="${menuMap['name']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(iter.investRecord.investor.pName) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(iter.investRecord.investor.pName) > 1}">
								<c:out value="${fn:substring(iter.investRecord.investor.pName,0,1)}****" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:if>
				</td>
				<td style="text-align: right;"><fmt:formatNumber value="${iter.investRecord.investAmount}" pattern="0.00"/></td>
				<td id="should_debt_source${iter.id}"><fmt:formatNumber value="${iter.xybj+iter.xylx}" pattern="0.00"/>元(本金:<fmt:formatNumber value="${iter.xybj}" pattern="0.00"/>元，利息:<fmt:formatNumber value="${iter.xylx}" pattern="0.00"/>元)</td>
				<td class="xlsText"><fmt:formatDate value="${iter.predict_repayment_date}" pattern="yyyy-MM-dd"/></td>
 			</tr> 
		</c:forEach>
 			<tr> 
				<td>合计</td>
				<td></td>
				<td></td> 
				<td></td> 
				<td></td>  
				<td></td>
				<td></td>
				<td style="text-align: right;"><fmt:formatNumber value="${count1}" pattern="0.00"/></td>
				<td ><fmt:formatNumber value="${count2}" pattern="0.00"/>元(本金:<fmt:formatNumber value="${count3}" pattern="0.00"/>元，利息:<fmt:formatNumber value="${count4}" pattern="0.00"/>元)</td>
				<td ></td>
 			</tr> 
			</tbody>

		</table>
	</body>
</html>
