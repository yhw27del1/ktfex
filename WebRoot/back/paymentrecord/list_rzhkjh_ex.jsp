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
	<body style="text-align: center;"> 
		<table align="center" border="1" style="width:600px;">
			 <tr style="width: 100%;">
				<td align="center" colspan="11">
					<div>
					      我的融资还款统计
					</div>
				</td>
				</tr>  
		<thead>
			<tr > 
<th>序号</th>
				<th>项目编号</th>
				<th>项目简称</th>				
				<th>融资方</th> 
				<th>交易账号</th>  
				<th>年利率</th>  
				<th>融资额</th>  
				<th>签约额</th> 
				<th>担保方</th> 
				<th>签约时间</th> 
				<th>到期时间</th> 
				<th>实还时间</th> 
				<th>期数</th> 
				<th>逾期天数</th> 
				<th>应还本金</th> 
				<th>应还利息</th> 
				<th>风险管理费</th> 
				<th>融资服务费</th> 
                <th>担保费</th>  
				<th>保证金</th> 
				<th>应还合计</th> 
			</tr>
		</thead>
		<tbody class="table_solid">
		    <%int i=1; %>  
		    <c:set value="0" var="total_1"/>
		    <c:set value="0" var="total_2"/>
		    <c:set value="0" var="total_3"/>
		    <c:set value="0" var="total_4"/>
		    <c:set value="0" var="total_5"/>
		    <c:set value="0" var="total_6"/>
		    <c:set value="0" var="total_7"/>
			<c:forEach items="${dataList}" var="data">  
				<tr> 
					<td><%=i%><%i++;%></td> 
					<td>${data.financbasecode}</td> 
					<td>${data.fshortname}</td> 					
					<td>${data.frealname}</td> 					
					<td>${data.financiername}</td>  
					<td>${data.rate}%</td>  
					<td>${data.maxamount}</td>  
					<td>${data.currenyamount}</td>  
					<td>${data.dbhsname}</td>  
					<td><fmt:formatDate value='${data.qianyuedate}' pattern="yyyy-MM-dd"/></td>  
					<td><fmt:formatDate value='${data.yhdate}' pattern="yyyy-MM-dd"/></td>  
					<td><fmt:formatDate value='${data.shdate}' pattern="yyyy-MM-dd"/></td>  
					<td class='xlsText'>${data.qs}/${data.returntimes}</td>
					<td>${data.overdue_days}</td>
					<td><fmt:formatNumber value="${data.totalbj}" pattern="#0.00"/>
					   <c:set value="${total_1+data.totalbj}" var="total_1"/>  
					</td>  
					<td><fmt:formatNumber value="${data.totallx}" pattern="#0.00"/>
					   <c:set value="${total_2+data.totallx}" var="total_2"/>  
					</td>  
					<td><fmt:formatNumber value="${data.totalfxglf}" pattern="#0.00"/>
					   <c:set value="${total_3+data.totalfxglf}" var="total_3"/>
					</td>  
					<td><fmt:formatNumber value="${data.totalrzfwf}" pattern="#0.00"/>
					   <c:set value="${total_4+data.totalrzfwf}" var="total_4"/>
					</td>  
					<td><fmt:formatNumber value="${data.totaldbf}" pattern="#0.00"/>
					   <c:set value="${total_5+data.totaldbf}" var="total_5"/>
					</td>  
					<td><fmt:formatNumber value="${data.totalbzj}" pattern="#0.00"/>
					   <c:set value="${total_6+data.totalbzj}" var="total_6"/>
					</td>   
                    <td><fmt:formatNumber value="${data.total}" pattern="#0.00"/>
                      <c:set value="${total_7+data.total}" var="total_7"/>
                    </td> 
		    	</tr> 
			</c:forEach> 
			<tr> 
				<td>总计</td>
				<td></td>
				<td></td>
				<td></td>    
				<td></td>     
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td></td>   
				<td><fmt:formatNumber  value="${total_1}" pattern="#0.00"/></td>   
				<td><fmt:formatNumber  value="${total_2}" pattern="#0.00"/></td>   
				<td><fmt:formatNumber  value="${total_3}" pattern="#0.00"/></td>    
                <td><fmt:formatNumber  value="${total_4}" pattern="#0.00"/></td>
                <td><fmt:formatNumber  value="${total_5}" pattern="#0.00"/></td>
                <td><fmt:formatNumber  value="${total_6}" pattern="#0.00"/></td>
                <td><fmt:formatNumber  value="${total_7}" pattern="#0.00"/></td>   
 			</tr>   
		</tbody>   
	</table> 
</body>
 

