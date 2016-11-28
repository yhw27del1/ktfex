<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>会员投资统计明细</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
   <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
   <script type="text/javascript" src="/back/four.jsp"></script>
   <script>
   			$(function(){
   				$("#type").val("${type}"); 
   				
				$(".table_solid").tableStyleUI(); 
				
				$("#startDate").datepicker({
                    numberOfMonths: 2,
                    dateFormat: "yy-mm-dd"
                });
                $("#endDate").datepicker({
                    numberOfMonths: 2,
                    dateFormat: "yy-mm-dd"
                });
                $("#ui-datepicker-div").css("display","none");
				
				$("#excelImg2").click(function(){
                    var startDate = $("#startDate").val();
                    var stDate = new Date(Date.parse(startDate));
                   var endDate = $("#endDate").val();
                    var edDate = new Date(Date.parse(endDate));
                    if(edDate < stDate){
                        alert("起止时间范围必须在1个月以内");
                        return false;
                    }
                    var type = $("#type").val();
                    var financingcode = $("#financingcode").val();
                    stDate.setMonth(stDate.getMonth()+1);
                    var days = (new Date(endDate)-new Date(startDate))/1000/60/60/24+1;
                    if(edDate <= stDate){
                        //alert("success:"+type);
                        if (confirm("统计共计"+days+"天的数据，是否确定下载？")) {
                        	window.location.href="/sheets/investor!investorDetailListEx?startDate="+startDate+"&endDate="+endDate+"&type="+type+"&financingcode="+financingcode+"&username=<%=request.getParameter("username")!=null?request.getParameter("username"):""%>";
                        }
                    }else{
                        alert("起止时间范围必须在1个月以内");
                        return false;
                    }
                });
				
				$("#seachButton").click(function() {
					 var startDate = $("#startDate").val();
	                    var stDate = new Date(Date.parse(startDate));
	                   var endDate = $("#endDate").val();
	                   var type = $("#type").val();
	                    var edDate = new Date(Date.parse(endDate));
	                    if(edDate < stDate){
	                        alert("查询失败！开始日期不能小于结束日期！");
	                        return false;
	                    }
	                    stDate.setMonth(stDate.getMonth()+1);
	                    var days = (new Date(endDate)-new Date(startDate))/1000/60/60/24+1;
	                    if(edDate <= stDate){
	                            $("#form").submit();
	                    }else{
	                        alert("查询失败！起止时间范围必须不超过1个月");
	                        return false;
	                    }
				}); 
			});
   </script>
  </head>
  <body>
    <form action="" id="form">
    <input type="hidden" name="page" value="1" />
  	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  		
	  		
			关键字:<input type="text" value="${username}" name="username" /> 
				<select type="select" name="type" id="type" value = "1">
                <option value="1" >签约日期：</option>
                <option value="0" >投标日期：</option>
               <input type="text" id="startDate" name="startDate"
				style="width:80px;" id="startDate"
				value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" />&nbsp;到&nbsp;
				<input type="text" name="endDate" style="width:80px;" id="endDate"
				value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" />

				<input type="hidden" id="excel" name="excel" value="0" />
				 
			&nbsp;&nbsp;&nbsp;&nbsp;项目编号<input type="text" id="financingcode" name="financingcode" value="${financingcode }" />
			<button class="ui-state-default" id="seachButton">查找</button>
	  		<div style="float:right;margin-right:30px;">
	  			<c:if test="${type=='1'}">签约</c:if><c:if test="${type=='0'}">投标</c:if>日期：<span style="color:#E91313"><fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" /></span>&nbsp;到&nbsp;<span style="color:#E91313"><fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/></span>
	  			<a style="color:red;" id="excelImg2" name="excelImg2"
                    href="javascript:;" title="明细导出EXCEL"><img
                        src="/Static/images/excel.gif">
                </a>
	  		</div>
  		
  	</div>
  	<div class="dataList ui-widget">
	  <table  class="ui-widget ui-widget-content">
		  <thead>
		  	<tr class="ui-widget-header ">
		  		<th>会员帐号</th>
		  		<th>会员名称</th>
		  		<th>投标额</th>
		  		<th>投标日期</th>
		  		<th>签约日期</th>
		  		<th>融资项目名</th>
		  		<th>项目编号</th>
		  		<th>融资项目期次</th>
		  		<th>投标合同</th>
		  		<th>状态</th>
		  	</tr>
		  </thead>
		  <tbody class="table_solid">
	  		
	  		<c:forEach items="${pageView.result}" var="item" varStatus="sta">
		  		<tr>
		  			<td>${item.investorname}</td>
		  			<td>
		  				<script>document.write(name("${item.investorrealname}"));</script>
		  			</td>
		  			<td>${item.investamount}</td>
		  			<td>${item.createdate}</td>
		  			<td>${item.qianyuedate}</td>
		  			<td>${item.fshortname}</td>
		  			<td>${item.financbasecode} </td>
		  			<td>${item.businesstype} </td>
		  			<td><a href="javascript:void(0)" onclick="window.showModalDialog('/back/investBaseAction!agreement_ui3?contract.id=${item.contractid }', null, 'dialogWidth:1000px;dialogHeight:800px;status:no;help:yes;resizable:no;')">${item.contract_number }</a></td>
		  			<td>
		  				<c:if test="${item.state=='1'}">待签约</c:if>
		  				<c:if test="${item.state=='2'}">已签约</c:if>
		  				<c:if test="${item.state=='3'}">已撤单</c:if>
		  			</td>
		  		</tr>
		  		</c:forEach>
		  		<tr>
                        <td colspan="9">
                            <jsp:include page="/common/page.jsp"></jsp:include></td>
                </tr>
	  		
	  	</tbody>
	  	</table>
	  	</div>
	  	</form>
  </body>
</html>
