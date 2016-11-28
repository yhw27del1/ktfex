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
<script type="text/javascript" src="/back/four.jsp"></script>
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
					      (<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />:<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"  />)金额(单位:元)  
					</div>
				</td>
				</tr> 
</table>
		<div class="dataList ui-widget" style="width:100%"> 
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
					    <th align="center">会员名称</th>
						<th align="center">交易账号</th> 
						<th align="center">会员类型</th>
						<th align="center">交易类型</th>
						<th align="center">充值额</th>
						<th align="center">提现额</th>
					    <th align="center">状态</th> 
					    <th align="center">申请日期</th>
					    <th align="center">审核日期</th>
						<th align="center">划款日期</th>
						<th align="center">备注</th>  
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${list}" var="entry">
				       <tr> 
						   <td>
						   		<c:if test="${menuMap['name']=='inline'}">
									${entry.TARGET_ACCOUNT_USER_REALNAME}
								</c:if>
								<c:if test="${menuMap['name']!='inline'}">
									<c:choose>
										<c:when test="${fn:length(entry.TARGET_ACCOUNT_USER_REALNAME) == 0}">
											无
										</c:when>
										<c:when test="${fn:length(entry.TARGET_ACCOUNT_USER_REALNAME) > 1}">
											<c:out value="${fn:substring(entry.TARGET_ACCOUNT_USER_REALNAME,0,1)}****" />
										</c:when>
										<c:otherwise>
											****
										</c:otherwise>
									</c:choose>
								</c:if>	
						   </td>
						   <td>${entry.TARGET_ACCOUNT_USER_USERNAME}</td>
						   <td>
						   <c:if test="${entry.TARGET_ACCOUNT_USER_USERTYPE=='T'}">投资人</c:if>
						   <c:if test="${entry.TARGET_ACCOUNT_USER_USERTYPE=='R'}">融资方</c:if>
						   <c:if test="${entry.TARGET_ACCOUNT_USER_USERTYPE=='D'}">担保方</c:if>
						   </td> 
						   <td>${entry.TYPE}</td>
					       <td><fmt:formatNumber value='${entry.MONEY_ADD}' pattern="#,##0.00"/></td>
					       <td><fmt:formatNumber value='${entry.MONEY_SUBTRACT}' pattern="#,##0.00"/></td>
					       <td>
					       <c:if test="${entry.CHECKFLAG=='0'||entry.CHECKFLAG=='3'||entry.CHECKFLAG=='25'||entry.CHECKFLAG=='36'||entry.CHECKFLAG=='2.4'||entry.CHECKFLAG=='2.5'||entry.CHECKFLAG=='2.9'}">
					       	待审核
					       </c:if>
					       <c:if test="${entry.CHECKFLAG=='1'||entry.CHECKFLAG=='24'||entry.CHECKFLAG=='26'||entry.CHECKFLAG=='37'}">
					       	已审核
					       </c:if>
					       <c:if test="${entry.CHECKFLAG=='2'||entry.CHECKFLAG=='5'||entry.CHECKFLAG=='27'||entry.CHECKFLAG=='38'}">
					       	已驳回
					       </c:if>
					       <c:if test="${entry.CHECKFLAG=='4'&&entry.CHECKFLAG2=='0'}">
					       	待划款
					       </c:if>
					       <c:if test="${entry.CHECKFLAG=='4'&&entry.CHECKFLAG2=='1'}">
					       	已划款
					       </c:if>
					       <c:if test="${entry.CHECKFLAG=='4'&&entry.CHECKFLAG2=='2'}">
					       	转账异常
					       </c:if>
					       <c:if test="${entry.CHECKFLAG=='4'&&entry.CHECKFLAG2=='3'}">
					       	提现错误
					       </c:if>
					       <c:if test="${entry.CHECKFLAG=='4'&&entry.CHECKFLAG2=='4'}">
					       	提现冲正
					       </c:if>
					       </td>
						   <td><fmt:formatDate value="${entry.CREATEDATE}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						   <td><fmt:formatDate value="${entry.CHECKDATE}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						   <td><fmt:formatDate value="${entry.HKDATE}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						   <td>${entry.MEMO}</td>
						</tr>
					</c:forEach>
				</tbody>
				<tr > 
				    <td align="center">小计</td>  
				    <td align="center"></td> 
				    <td align="center"></td>
				    <td align="center"></td>	
			        <td align="center"><fmt:formatNumber value='${totalData[0]}' pattern="#,##0.00"/></td>  
				    <td align="center"><fmt:formatNumber value='${totalData[1]}' pattern="#,##0.00"/></td>  
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