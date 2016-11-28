<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>融资项目列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style>
		.ui-button-text{
			line-height:30px;
			padding:10px 3px 5px 3px;
		}
		.ui-helper-hidden-accessible{
			display:none;
		}
	</style>
   <%@ include file="/common/import.jsp" %>
   <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
   <script type="text/javascript" src="/back/four.jsp"></script>
   <script>
			$(function(){
				
				$("#seachButton").click(function() {
					$("#form1").submit();
				}); 
				$("#startDate").datepicker({
					numberOfMonths: 2,
			        dateFormat: "yy-mm-dd"
			    });
				$("#endDate").datepicker({
					numberOfMonths: 2,
			        dateFormat: "yy-mm-dd"
			    });
			    $("#ui-datepicker-div").css("display","none");
			    $( "#radio" ).buttonset();
			    $("#radio1").click(function(){
			    	var startDate = $("#startDate").val();
			    	var endDate = $("#endDate").val();
			    	var selectby = $("#selectby").val();
			    	window.location.href="/sheets/finance!financerList?startDate="+startDate+"&endDate="+endDate+"&selectby="+selectby;
			    });
			   
			     $(".table_solid").tableStyleUI(); 
			});
		</script>
  </head>
  <body>
  	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:30px;margin-bottom:10px;">
  	<form action="" id="form1">
  		<div id="radio" style="float:left">
			<input type="radio" id="radio2" name="watchtype" checked="checked"/><label for="radio2" >按融资项目</label>
			<input type="radio" id="radio1" name="watchtype"/><label for="radio1">按融资方</label>
		</div>
		<div style="float:left;margin-left:20px;font-size:15px;">
			按<s:select list="#{'签约':'签约','挂单通过':'挂单','撤单':'撤单'}" name="selectby" id="selectby" value="#request.selectby" cssStyle="padding:3px"></s:select>时间
	  		<input type="text" id="startDate" name="startDate" style="width:80px;padding:3px;" id="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>"/>
	  		&nbsp;到&nbsp;
	  		<input type="text" name="endDate" style="width:80px;padding:3px;" id="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>"/>
	  		项目编号 <input type="text" name="financingcode" value="${financingcode}" style="padding:3px;"/>
	  		 
	  		<button class="ui-state-default" id="seachButton">查找</button>
		  	 <c:if test="${!empty resultList}">
		        &nbsp;&nbsp; <a style="color:red;parding-top:35px;" href="/sheets/finance!financeList?selectby=${selectby}&financingcode=${financingcode}&watchtype=${watchtype}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />&excelFlag=1" title="结果导出EXCEL"><img src="/Static/images/excel.gif" style="height:18px;" ></a>
		    </c:if>	   
  		</div>
  	</form>
  	</div>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
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
		  			<td>
		  				<script>document.write(name("${item.financerrealname}"));</script>
		  			</td>
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
