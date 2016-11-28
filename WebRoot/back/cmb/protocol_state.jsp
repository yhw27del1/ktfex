<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>招行协议状态对账</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
	<script type="text/javascript" src="/back/four.jsp"></script>
	<style type="text/css">
		.red{color:red;}
		.green{color:green;}
	</style>
	<script type="text/javascript">
		$(function(){
			$(".table_solid").tableStyleUI();
			$("#startDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
		    $("#ui-datepicker-div").css({'display':'none'});
		});
		function toURL(url){
			var d = $("#dataForm").serialize()+"&time="+new Date().getTime();
		   	$.ajax({
		   		type:"post",
		   		url:url,
		   		data:d,
		   		dataType:"json",
		   		success:function(data){
		   		console.log(data);
		   			alert(data.msg);
		   			location.reload();
		   		},
		   		error:function(data){
		   		console.log(data);
		   			alert(data.msg);
		   			location.reload();
		   		}
		   	});
		}
	</script>
  </head>
<body>
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				对账日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						
					</th>
					<th>
						银行编号
					</th>
					<th>
						合作方编号
					</th>
					<th>
						合作方机构号
					</th>
					<th>
						交易日期
					</th>
					<th>
						银行账号
					</th>
					<th>
						交易账号
					</th>
					<th>
						姓名
					</th>
					<th>
						协议状态
					</th>
					<!-- 
					<th>
						货币代码
					</th>
					 -->
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:if test="${data!=null}">
				<c:forEach items="${data}" var="entry" varStatus="jj">
						<tr>
							<td>
								${jj.count}
							</td>
							<td>
								${entry.BANKID}
							</td>
							<td>
								${entry.DEALID}
							</td>
							<td>
								${entry.COBRN}
							</td>
							<td>
								<c:out value="${fn:substring(entry.TXDATE,0,4)}-" /><c:out value="${fn:substring(entry.TXDATE,4,6)}-" /><c:out value="${fn:substring(entry.TXDATE,6,8)}" />
							</td>
							<td>
								<script>document.write(bankcard("${entry.BANKACC}"));</script>
							</td>
							<td>
								${entry.FUNDACC}
							</td>
							<td>
								<script>document.write(name("${entry.CUSTNAME}"));</script>
							</td>
							<td>
								<c:if test="${entry.STATUS=='1'}">
									销户
								</c:if>
								<c:if test="${entry.STATUS=='0'}">
									正常
								</c:if>
							</td>
							<!-- 
							<td>
								${entry.CURCODE}
							</td>
							 -->
						</tr>
				</c:forEach>
			</c:if>
			<c:if test="${data==null}">
				<tr>
					<td></td>
					<td colspan="10">${msg}</td>
				</tr>
			</c:if>
			</tbody>
		</table>
	</div>
	</form>
</body>
</html>