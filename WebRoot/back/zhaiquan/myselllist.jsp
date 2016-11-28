<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/dialogHelper.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
			<link href="/Static/js/lhgdialog/_doc/common.css" type="text/css" rel="stylesheet"/> 
	    <script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript">
	$(function() {
		$("#startDate").datepicker( {
			numberOfMonths : 2,
			dateFormat : "yy-mm-dd"
		});
		$("#endDate").datepicker( {
			numberOfMonths : 2,
			dateFormat : "yy-mm-dd"
		});
		$("#ui-datepicker-div").css( {
			'display' : 'none'
		});
		$(".table_solid").tableStyleUI(); 
	});
function oper(id,title){ 
		$.dialog({
		    title: '提示信息',
			lock:  true,
			min:  false,
			max:  false,
			width: '400px',
		    content: '您要把此条出让记录'+title+'吗?',
		    ok: function(){ 
		            $("#form1").attr("action","/back/zhaiquan/sellingAction!upState");
                    $("#sellId").val(id); 
                    $("#form1").submit();  
		        return false;
		    },
		    cancelVal: '关闭',
		    cancel: true /*为true等价于function(){}*/
		});
         
}
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3
}

.agreement {
	cursor: pointer;
}

.agreement:HOVER {
	text-decoration: underline;
}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
<form action="/back/zhaiquan/sellingAction!myselllist" id="form1"> 
	<input type='hidden' class='autoheight' value="auto" />
		<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		查询条件:&nbsp;关键字&nbsp;<input type="text" value="${querykeyWord}" name="querykeyWord">&nbsp;
				日期段：&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>" id="startDate"/>到<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>" id="endDate"/>
                   状态: <s:select name="queryState" list="stateList" listKey="string1" listValue="string2" headerKey="" headerValue="全部"/>
				<input class="ui-state-default" type="submit" value="查询">
	</div>
	<input type="hidden" name="page" value="1" /> 
	<input type="hidden" name="id" id="sellId"/> 
	<div class="dataList ui-widget" style="float: left; width:100%; margin-top:15px;">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						债权代码
					</th>
					<!-- 
					<th>
						借款合同编号
					</th>  -->
					<th>
						债权到期日期
					</th> 
					<th>
						出让时间
					</th>
					<th>
						出让价格
					</th> 
					<th>
						状态
					</th>
					<th> 
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
							${entry.zhaiQuanCode}
						</td>
						<!-- <td>
							${entry.contract_numbers}
						</td>  -->
					    <td>
					       <fmt:formatDate value='${entry.investRecord.lastDate}' pattern="yyyy-MM-dd"/>
 						</td>
						<td>
							${entry.createDate}
						</td>
						<td>
							<fmt:formatNumber value='${entry.sellingPrice}' type="currency"
								currencySymbol="" />
						</td>
						<td>
							<c:if test="${entry.state=='0'}">
								<span style="color: #4169E1;">出让中</span>
							</c:if>
							<c:if test="${entry.state=='1'}">
								<span style="color: green;">成功 </span>
							</c:if>
							<c:if test="${entry.state=='2'}">
								<span style="color: red;">失败 </span>
							</c:if> 
							 <c:if test="${entry.state=='3'}">
								<span style="color: red;">撤单 </span>
							</c:if> 
							 <c:if test="${entry.state=='4'}">
								<span style="color: red;">已过期 </span>
							</c:if> 
						</td>
						<td>
							 <c:if test="${entry.state=='1'}">
								<a href="/back/zhaiquan/contractAction!view?id=${entry.contract.id}" target="_blank">查看合同</a>
							</c:if>  
							<c:if test="${entry.state=='0'}">
								  <button onclick="oper('${entry.id}','撤单');return false;" class="ui-state-default" >撤单</button>&nbsp;&nbsp;&nbsp;
 							</c:if> 
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="6">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div> 
	</form>
	<%@ include file="/common/messageTip.jsp" %>
</body>
