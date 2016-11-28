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
   .xlsMoney{mso-number-format:"0\.00";}
   .xlsText{mso-number-format:"\@";} 
</style>
<body> 
		<div class="dataList ui-widget" style="width:100%"> 
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
					    <th align="center">姓名</th>
						<th align="center">卡号</th> 
						<th align="center">应处理金额</th>
						<th align="center">备注</th>
						<th align="center">实处理金额</th>
						<th align="center">处理标识</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${list}" var="entry">
				       <tr> 
						   <td>${entry.account.user.realname}</td>
						   <td class="xlsText">${entry.bankAccount}</td>
						   <td class="xlsMoney">${entry.money}</td>
						   <td>${entry.memo}</td>
						   <td></td>
						   <td></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</body>
</html>