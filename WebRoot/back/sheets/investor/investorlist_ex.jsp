<%@ page language="java" import="java.util.*" contentType="application/msexcel" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<%
  response.setHeader("Content-Disposition", "inline;filename=investorlist.xls");
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
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
				<td align="center" colspan="6">
					<div>
					      (<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />:<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"  />)融资项目统计(单位:元)  
					</div>
				</td>
				</tr> 

		  <thead>
		  	<tr class="ui-widget-header ">
                <th>会员帐号</th>
                <th>会员名称</th>
                <th>所属机构</th>
                <th>总投标额</th>
                <th>投资次数</th>
                <th>会员介绍人</th>
            </tr>
		  </thead>
		  <tbody class="table_solid">
	  		<c:forEach items="${resultList}" var="item">
                <tr>
                    <td>${item.investorname}</td>
                    <td>
                    <c:if test="${menuMap['name']=='inline'}">
						${item.investorrealname}
					</c:if>
					<c:if test="${menuMap['name']!='inline'}">
						<c:choose>
							<c:when test="${fn:length(item.investorrealname) == 0}">
								无
							</c:when>
							<c:when test="${fn:length(item.investorrealname) > 1}">
								<c:out value="${fn:substring(item.investorrealname,0,1)}****" />
							</c:when>
							<c:otherwise>
								****
							</c:otherwise>
						</c:choose>
					</c:if>
					</td>
                    <td>${item.investor_orgno}</td>
                    <td class="xlsMoney">${item.money}</td>
                    <c:set value="${allmoney + item.money}" var="allmoney"/>
                    <td>${item.count}</td>
                    <c:set value="${allcount + item.count}" var="allcount"/>
                    <td class="xlsText">${item.jingbanren}</td>
                     </tr>
            </c:forEach>
	  		<tr>
                <td>总计</td>
                <td></td>
                <td></td>
                <td class="xlsMoney">${allmoney }</td>
                <td>${allcount}</td>
                <td></td>
            </tr>
	  	</tbody>
	  		  	</table>
	  	</div>
  </body>
</html>
