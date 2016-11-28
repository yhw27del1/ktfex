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
   .xlsMoney{mso-number-format:"0\.0";}
   .xlsDate{mso-number-format:"yyyy\/mm\/dd hh\:mm\:ss";}
   .xlsDate1{mso-number-format:"yyyy-mm-dd";}
   .xlsText{mso-number-format:"\@";} 
</style>
<body> 
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
				<td align="center" colspan="10">
					<div>
					      (<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"  />:<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"  />)融资项目统计(单位:元)  
					</div>
				</td>
				</tr> 

		  <thead>
		  	<tr class="ui-widget-header ">
		  		<th width="10"></th>
		  		<th>
		  		<s:if test="#request.selectby == '挂单通过'">
		  			挂间时间
		  		</s:if>
		  		<s:else>
		  			${selectby }时间
		  		</s:else>
		  		</th>
		  		<th>项目编号</th>
		  		<th>融资项目名称</th>
		  		<th>融资期限</th>
		  		<th>还款方式</th>
		  		<th>融资额</th>
		  		<th>融资方帐号</th>
		  		<th>融资方姓名</th>
		  		<th></th>
		  	</tr>
		  </thead>
		  <tbody class="table_solid">
	  		<c:forEach items="${resultList}" var="item">
		  		<tr>
		  			<c:set value="${allcount + 1}" var="allcount"/>
		  			<td>${allcount }</td>
		  			<td>
		  				<s:if test="#request.selectby == '撤单'">
		  				${item.modifydate}
		  				</s:if>
		  				<s:else>${item.time}</s:else>
		  				
		  			</td>
		  			<td>${item.financecode}</td>
		  			<td>${item.financename}</td>
		  			<td>${item.term}</td>
		  			<td>${item.returnpattern}</td>
		  			<td>${item.currenyamount}</td>
		  			<c:set value="${allmoney + item.currenyamount}" var="allmoney"/>
		  			
		  			<td>${item.financerusername}</td>
		  			<td>${item.financerrealname}</td>
		  			<td><a href="javascript:void(0)" onclick="window.location.href='/sheets/finance!financedetail?financeid=${item.id}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&selectby=${selectby }&financingcode=${financingcode }'">查看明细</a></td>
		  		</tr>
	  		</c:forEach>
	  		<tr>
	  			<td colspan="2"><h3 style="margin:5px 5px 5px 0;padding:0">总计</h3></td>
	  			<td></td>
	  			<td></td>
	  			<td></td>
	  			<td></td>
	  			<td><h3 style="margin:5px 5px 5px 0;padding:0">${allmoney }</h3></td>
	  			<td></td>
	  			<td></td>
	  			<td></td>
	  		</tr>
	  	</tbody>
	  		  	</table>
	  	</div>
  </body>
</html>
