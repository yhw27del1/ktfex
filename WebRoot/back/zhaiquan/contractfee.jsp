<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
$(function(){
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
	$(".table_solid").tableStyleUI();
	
	      $("#printButton").click(function(){    	
		    var params = "";
			if(window.location.href.indexOf("?")>0){
			    params = window.location.href.substring(window.location.href.indexOf("?"),window.location.href.length);
			}
			window.open("/back/zhaiquan/contractAction!feePrint"+params);
			return false;
		  });  
	      $("#exButton").click(function(){    	
		    var params = "";
			if(window.location.href.indexOf("?")>0){
			    params = window.location.href.substring(window.location.href.indexOf("?"),window.location.href.length);
			}
			window.open("/back/zhaiquan/contractAction!feePrint"+params+"&excelFlag=1");  
			return false;
		  });  
		  
		  $("#queryButton").click(function(){
		    $("#form1").attr("action","/back/zhaiquan/contractAction!fee");  
		    $("#form1").submit(); 
		  });
});
function show(url){
	window.showModalDialog(url, null, "dialogWidth:800px;dialogHeight:auto;status:no;help:yes;resizable:no;");
} 
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3     
}
.agreement{cursor:pointer;}
.agreement:HOVER{text-decoration: underline;}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
	<input type='hidden' class='autoheight' value="auto" />
	<form action="" id='form1'> 
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="height:30px;">
		
		<div style="float:left">
			
			<div style="background-color:#fff;height:24px;float:left;border:1px solid #d3d3d3">

				<s:select list="#{'d':'出让人关键字','e':'受让人关键字','a':'出让人或受让人关键字','b':'债权代码','c':'主合同编码'}" name="searchtype" cssStyle="padding:3px"></s:select>
				<input type="text" value="${keyword}" name="keyword" style="padding:3px 0 3px 0;margin:0;border:none;" >
			</div>
		</div>
		<div style="float:left;margin-left:10px;">
			开始日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate" style="width:80px;padding:3px;"/>&nbsp;
			结束日期&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" id="endDate" style="width:80px;padding:3px;"/>
			<input class="ui-state-default" style="height:28px;cursor:pointer" type="submit" id='queryButton'  value="查询">
			 <c:if test="${!empty pageView.records}">
			&nbsp;&nbsp;<input class="ui-state-default" style="height:28px;cursor:pointer" id='printButton' type="submit" value="打印">
		    &nbsp;&nbsp;<input class="ui-state-default" style="height:28px;cursor:pointer" id='exButton' type="submit" value="导出">
		    </c:if>
		</div>
	</div>
	<input type="hidden" name="page" value="1" /> 
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					    <th rowspan="3">
							序号
						</th>
						
 						 <th rowspan="3">
							成交日期
						 </th> 
						<th rowspan="3">
							成交价
						</th> 
						<th rowspan="3">
							费率(%)
						</th>  
						<th style="text-align:center" colspan="2">手续费</th>
					
					 <th colspan="6" style="text-align:center">会员信息</th>
					 
						 
						<th rowspan="3">
							协议编号
						</th> 
				     <th rowspan="3">
				         备注
					 </th>   
				
				</tr>
				<tr class="ui-widget-header ">
						<th style="text-align:center" rowspan="3">出让方</th>
						<th style="text-align:center" rowspan="3">受让方</th>
						
						 <th style="text-align:center" colspan="3">出让方</th> 
					     <th style="text-align:center" colspan="3">受让方</th> 
						
					</tr>
				 <tr class="ui-widget-header ">
				    <th style="text-align:center">交易账户</th>
			  		<th style="text-align:center">户名</th> 
			  		<th style="text-align:center">三方存管</th> 
				    <th style="text-align:center">交易账户</th>
			  		<th style="text-align:center">户名</th> 
			  		<th style="text-align:center">三方存管</th> 
			  	</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry" varStatus="index">
						<tr>
							<td  align='center'>
								 ${index.count} 
							</td> 
					        <td  align='center'>
								<fmt:formatDate value='${entry.buyerDate}' pattern="yyyy-MM-dd" />
							</td> 
							<td  align='center'>
								<fmt:formatNumber value='${entry.price}'  pattern='##.##' minFractionDigits='2'  />
							</td> 
					        <td  align='center'><fmt:formatNumber value='${entry.percentSell}'  pattern='##.##' minFractionDigits='2'  /></td> 
					        <td  align='center'><fmt:formatNumber value='${entry.selling.zqfwf}'  pattern='##.##' minFractionDigits='2'  /></td>  
					        <td  align='center'><fmt:formatNumber value='${entry.buying.zqfwf}'  pattern='##.##' minFractionDigits='2'  /></td>
					        <td  align='center'>${entry.seller.username}</td> 
					        <td  align='center'>${entry.seller.realname}</td>  
					        <td  align='center'>${entry.seller.accountNo}</td>  
					        <td  align='center'>${entry.buyer.username}</td> 
					        <td  align='center'>${entry.buyer.realname}</td>  
					        <td  align='center'>${entry.buyer.accountNo}</td>  
                            <td  align='center'><a href="javascript:void(0)" onclick="show('/back/zhaiquan/zhaiQuanInvestAction!detail?zhaiQuanId=${entry.investRecord.id}&contract_id=${entry.id}')">${entry.xieyiCode}</a></td> 
							
                            <td></td> 
						</tr>
					</c:forEach>
					     <tr>
							<td  align='center'>
								合计
							</td>
							<td>-</td> 
							<td  align='center'>
								<fmt:formatNumber value='${price}' type="currency" currencySymbol="" />
							</td> 
					        <td> </td> 
					        <td  align='center'><fmt:formatNumber value='${zqfwf_s}' type="currency" currencySymbol="" /></td> 
					        <td  align='center'><fmt:formatNumber value='${zqfwf_b}' type="currency" currencySymbol="" /></td>    

							<td>-</td> 
							<td>-</td>
							<td>-</td> 
							<td>-</td> 
							<td>-</td> 
							<td>-</td> 
							<td>-</td> 
							<td>-</td>  
				</tr>
				<tr>
					<td colspan="14">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
