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
	<body style="text-align: center;"> 
		<table align="center" border="1" style="width:600px;">
			 <tr style="width: 100%;">
				<td align="center" colspan="11">
					<div>
					      融资项目还款统计
					</div>
				</td>
				</tr>  
		<thead>
			<tr > 
				<th>序号</th>
				<th>项目编号</th>
				<th>项目简称</th>
				<th>融资额(元)</th>
				<th>融资方帐号</th> 
				<th>融资方</th>  
				<th>融资方手机</th>  
				<th>担保方</th>  
				<th>应还日期</th>  
				<th>实还日期</th>  
				<th>期次</th>  
			</tr>
		</thead>
		<tbody class="table_solid">
		    <%int i=1; %> 
		    <c:set value="0" var="total_obj"/>
			<c:forEach items="${dataList}" var="data">
				<tr> 
					<td><%=i%><%i++;%></td> 
					<td>${data.financbasecode}</td> 
					<td>${data.fshortname}</td> 
					<td><fmt:formatNumber value="${data.currenyamount}" pattern="#0.00"/><c:set value="${data.currenyamount+total_obj}" var="total_obj"/></td> 
					<td>${data.financiername}</td> 
					<td>
					<c:if test="${menuMap['name']=='inline'}">
						${data.frealname}
					</c:if>
					<c:if test="${menuMap['name']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(data.frealname) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(data.frealname) > 1}">
								<c:out value="${fn:substring(data.frealname,0,1)}****" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
					</td>
					<td>
					<c:if test="${menuMap['bankcard']=='inline'}">
						${data.emobile}
					</c:if>
					<c:if test="${menuMap['bankcard']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(data.emobile) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(data.emobile) > 4}">
								<c:out value="****${fn:substring(data.emobile,fn:length(data.emobile)-4,-1)}" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
					</td>
					<td>
					<c:if test="${menuMap['name']=='inline'}">
						${data.dbhsname}
					</c:if>
					<c:if test="${menuMap['name']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(data.dbhsname) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(data.dbhsname) > 1}">
								<c:out value="${fn:substring(data.dbhsname,0,1)}****" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
					</td>
					<td><fmt:formatDate value='${data.yhdate}' pattern="yyyy-MM-dd"/></td>  
					<td><fmt:formatDate value='${data.shdatestr}' pattern="yyyy-MM-dd"/></td> 
					<td  class='xlsText'>${data.qs}/${data.returntimes}</td>
		    	</tr> 
			</c:forEach>
			<tr> 
				<td>合计</td>
				<td></td>
				<td></td> 
				<td><fmt:formatNumber  value="${total_obj}" pattern="#0.00"/></td> 
				<td></td> 
				<td></td>  
				<td></td>   
				<td></td>    
				<td></td>    
				<td></td>    
				<td></td>    
 			</tr>   
		</tbody> 
	</table> 
</body>
 

