<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title></title>
		<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
		body{
			overflow-x:scroll;
		}
		table {
			font-size: 13px;
		}
		td,th{white-space: nowrap;}
		.pagefoot{
			padding-right:20px;
		}
		.pagefoot span{
			margin:0 0 0 5px;
		}
		.pagefoot span:hover{
			border-bottom:1px solid #000;
		}
		.pagefoot a{
			text-decoration: none;
		}
		.pagefoot a.current{
			color:red;
			margin-left:5px;
		}
		</style>
		<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
$(document).ready(function(){
    $(".table_solid").tableStyleUI();  
    $("#newwindow").css({"padding":"3px","cursor":"pointer"}).button().click(function(){
    	window.open(window.location.href);
    });
    if(self==top){
    	$("#newwindow").hide();
    }
    $("#startDate").datepicker({
        dateFormat: "yy-mm-dd",
        showOn: "button",
        buttonImage: "/Static/images/calendar.gif",
        buttonImageOnly: true,
        onSelect: function( selectedDate ) {
        	var date = new Date(selectedDate);
        	date.setMonth(date.getMonth()+1);
        	$( "#endDate" ).datepicker( "option", "maxDate", date );
        	$( "#endDate" ).datepicker( "option", "minDate", selectedDate );
        	$( "#endDate" ).datepicker( "setDate", date ).datepicker('hide');;
        }
    });
	$("#endDate").datepicker({
        dateFormat: "yy-mm-dd",
        showOn: "button",
        buttonImage: "/Static/images/calendar.gif",
        buttonImageOnly: true
    });
    $("#ui-datepicker-div").css({'display':'none'});
    $(".pagefoot a[class!='current']").click(function(){
    	var page = $(this).attr("obj");
    	$("#page").val(page);
    	$("#myform").submit();
    });
    
});
</script>
	</head>
	<body>
		<form action="" id="myform" method="post">
			<input type="hidden" name="page" id="page" value="1"/>
			<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" style="overflow: visible;">
				<div style="float: left;">
					<select name="selectby">
						<option value="0" <c:if test="${selectby==0}">selected="selected"</c:if>>
							投标日期
						</option>
						<option value="1" <c:if test="${selectby==1}">selected="selected"</c:if>>
							签约日期
						</option>
					</select>
					从
					<input value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>" name="startDate" id="startDate" style="width: 80px" placeholder="起始日期" readonly="readonly" />
					至
					<input value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" name="endDate" style="width: 80px" id="endDate" placeholder="结束日期" readonly="readonly" />
					&nbsp;
					<input type="text" name="fcode" value="${fcode}" placeholder="项目编号" style="width: 100px;" />
					<input type="text" name="iorusername" value="${iorusername}" placeholder="投标人帐号" style="width: 100px;" />
					<button class="ui-state-default">
						查找
					</button>
			      <c:if test="${!empty result}">
			        &nbsp;&nbsp; <a style="color:red;"  href="/back/investBaseAction!contractDetail?fcode=${fcode}&iorusername=${iorusername}&selectby=${selectby}&startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />&excelFlag=1"  title="结果导出EXCEL"><img src="/Static/images/excel.gif"></a>
			    </c:if>	 
				</div>
				<div style="float: right; padding-top: 5px;">
					<span id="newwindow">新窗口打开</span>
				</div>
			</div>
		</form>

		<div class="dataList ui-widget" style="width:100%">
		统计期间，投标总额为<fmt:formatNumber value="${amount }" pattern="#,###,###,##0.00"/> 共${recordcount}条
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
						<th>
							项目编号
						</th>
						<th>
							签约日期
						</th>
						<th>
							项目简称
						</th>
						<th>期限</th>  
						<th>
							融资方
						</th>
						<th>
							融资方户名
						</th>  
						<th>
							投标方
						</th>
						<th>
							投标方户名
						</th>
						<th>
							投标金额
						</th>
						<th>
							投标日期
						</th>
						<th>
							合同编号
						</th>
						<th>
							状态
						</th>
						<th>
							月收本金
						</th>
						<th>
							月收利息
						</th>
						<th>
							月收本息
						</th>
						<th>
							本息余额
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${result}" var="c" varStatus="sta">
						<tr>
							<td>
								${c.financbasecode}
							</td>
							<td>
								<fmt:formatDate value="${c.financier_make_sure_date}" pattern="yy-MM-dd" />
							</td>
							<td>
								<a title="${c.fshortname}">${fn:substring(c.fshortname,0,6)}</a>
							</td>
							<td >${c.businesstype}期(${c.returnpattern})</td>
							<td>
								<script>document.write(name("${c.frealname}"));</script>
							</td>
							<td>
								${c.financiername}
							</td>
							
							<td>
							    <script>document.write(name("${c.investorrealname}"));</script>
							</td>
							<td>
								${c.investorname}
							</td>
							<td>
								${c.investamount}
								<c:set value="${investamount+c.investamount}" var="investamount" />								
							</td>
							<td>
								<fmt:formatDate value="${c.investdate}" pattern="yy-MM-dd" />
							</td>
							
							<td>
								${c.contract_number}
							</td>
							<td>
								<c:choose>
									<c:when test="${c.state==1}">未签</c:when>
									<c:when test="${c.state==2}">已签</c:when>
									<c:when test="${c.state==3}">取消</c:when>
								</c:choose>
							</td>
							<td>
								<c:if test="${c.payment_method==1}">
									<fmt:formatNumber value="${c.investamount}" pattern="0.00" />
								</c:if>
								<c:if test="${c.payment_method==2}">
									<fmt:formatNumber value="${c.principal_allah_monthly}" pattern="0.00" />
								</c:if>
							</td>
							<td>
								<c:if test="${c.payment_method==1}">
									<fmt:formatNumber value="${c.interest_allah}" pattern="0.00" />
								</c:if>
								<c:if test="${c.payment_method==2}">
									<fmt:formatNumber value="${c.interest_allah_monthly}" pattern="0.00" />
								</c:if>
							</td>
							<td>
								<c:if test="${c.payment_method==1}">
									<fmt:formatNumber value="${c.investamount}" pattern="0.00" var="investamount" />
									<fmt:formatNumber value="${c.interest_allah}" pattern="0.00" var="interest_allah" />
							${investamount+interest_allah}
						</c:if>
								<c:if test="${c.payment_method==2}">
									<fmt:formatNumber value="${c.repayment_amount_monthly_allah}" pattern="0.00" />
								</c:if>
							</td>
							<td>
								<fmt:formatNumber value="${c.bjye}" pattern="0.00" var="bjye" />
								<fmt:formatNumber value="${c.lxye}" pattern="0.00" var="lxye" />
								<fmt:formatNumber value="${bjye+lxye}" pattern="0.00" />
							</td>
						</tr>
					</c:forEach>
				</tbody>
				
			</table>
		</div>
	</body>
</html>