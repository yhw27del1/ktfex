<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"  %>
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
<table class="ui-widget ui-widget-content">
  			 <tr style="width: 100%;">
				<td align="center" colspan="10">
					<div>
					      (<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />:<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"  />)融资项目信息(单位:元)  
					</div>
				</td>
				</tr> 
</table>
		<div class="dataList ui-widget" style="width:100%"> 
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
					    <th align="center">项目编号</th>
						<th align="center">项目简称</th> 
		                <th align="center">担保方</th>
						<th align="center">融资方</th>
						<th align="center">总融资额</th>
						<th align="center">可融资额</th>
						<th align="center">已融资额</th>
					    <th  align="center">期限(还款方式)</th> 
					    <th  align="center">年利率</th>
					    <th  align="center">投标人数</th>
						<th align="center">投标截止</th>
						<th align="center">状态</th>  
						<th align="center">进度</th>  
						<th align="center">所属地域</th>  
						<th align="center">行业</th>  
						<th align="center">交易时长(分钟)</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${list}" var="entry">
			       <tr> 
				   <td>${entry.financecode}</td>
				   <td>${entry.FINANCENAME}</td>
	               <td>${entry.CREATEORG_SHORT}(${entry.CREATEORG})</td>  
				   <td> 
					<c:if test="${menuMap['name']=='inline'}">
						${entry.FINANCERREALNAME}
					</c:if>
					<c:if test="${menuMap['name']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(entry.FINANCERREALNAME) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(entry.FINANCERREALNAME) > 1}">
								<c:out value="${fn:substring(entry.FINANCERREALNAME,0,1)}****" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
					</td> 
				   <td><fmt:formatNumber value='${entry.MAXAMOUNT}' pattern="0.00"/></td>
			       <td><fmt:formatNumber value='${entry.CURCANINVEST}' pattern="0.00"/></td> 
			       <td><fmt:formatNumber value='${entry.CURRENYAMOUNT}' pattern="0.00"/></td>
				   <td title='${entry.RETURNPATTERN}'>
						<c:choose>
						    <c:when test="${entry.INTERESTDAY>0}">
								${entry.INTERESTDAY}天<br/>
							</c:when>
							<c:otherwise>
								${entry.TERM}个月<br/>
							</c:otherwise>
						</c:choose>
				    	${entry.RETURNPATTERN}
					</td>
				   <td>${entry.RATE}%</td>
			       <td>${entry.HAVEINVESTNUM}</td> 
				   <td><fmt:formatDate value="${entry.ENDDATE}" pattern="yyyy-MM-dd"/></td>
				   <td>${entry.STATE_ZH}</td>
			       <td>
			       		<fmt:formatNumber value="${entry.CURRENYAMOUNT}" var="CURRENYAMOUNT" pattern="0.00"/>
			       		<fmt:formatNumber value="${entry.MAXAMOUNT}" var="MAXAMOUNT" pattern="0.00"/>
			       		<fmt:formatNumber value='${(CURRENYAMOUNT/MAXAMOUNT)*100}' pattern="0.00"/>%
			       </td> 
			       <td>${entry.provinceName}${entry.cityName} </td> 
			       <td >${entry.HYTYPE}</td>  
				   <td>
				   		<fmt:formatNumber value="${entry.jysc}" var="jysc" pattern="0.00"/>
				   		<fmt:formatNumber value="${jysc/60}" pattern="0.00"/>
				   </td> 
			</tr>
		</c:forEach>
				</tbody>
				<tr > 
				    <td align="center">小计</td>  
				    <td align="center"></td> 
				    <td align="center"></td>
				    <td align="center"></td>	
			        <td align="center"><fmt:formatNumber value='${totalData[0]}' pattern="#,##0.00"/></td>  
				    <td align="center"><fmt:formatNumber value='${totalData[2]}' pattern="#,##0.00"/></td>  
				    <td align="center"><fmt:formatNumber value='${totalData[1]}' pattern="#,##0.00"/></td>
			        <td align="center"></td>
			        <td align="center"></td>
			        <td align="center"></td>  
					<td align="center"></td>
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"></td> 
					<td align="center"></td> 
			</table>
		</div>
	</body>
</html>