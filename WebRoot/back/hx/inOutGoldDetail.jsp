<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="now" value="<%=new java.util.Date()%>" />
<html>
	<head>
		<title>华夏银行出入金明细核对</title>
		<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script>
		
		function doprint(){
			$("#toolbar").hide();
			$(".noClassPint").hide();
			var now = new Date();
			var now_time = now.getFullYear()+"-"
				+((now.getMonth()+1)>9?(now.getMonth()+1):"0"+(now.getMonth()+1))+"-"
				+(now.getDate()>9?now.getDate():"0"+now.getDate())+" "
				+(now.getHours()>9?now.getHours():"0"+now.getHours())+":"
				+(now.getMinutes()>9?now.getMinutes():"0"+now.getMinutes())+":"
				+(now.getSeconds()>9?now.getSeconds():"0"+now.getSeconds());
			$("#create_time").html(now_time);
			now_time = now = null;
			print();  
			$("#toolbar").show();
			$(".noClassPint").show();
		}
		</script>

		<style>
body {
	font-size: 13px;
	padding: 0;
	margin: 0;
}

.l1 {
	text-align: center;
}

.space {
	padding-left: 50px;
}

.title {
	font-size: 13.5px;
	font-weight: bold
}

.padding {
	padding: 6px;
}

.tr_on_selected {
	background: #f7f7f7;
	font-weight: bold;
}

#toolbar{
	position: fixed;
	top: 0px;
	left: 0px;
	width: 100%;
	background-color: #F2F2F2;
	border-bottom: 1px solid #DCDCDC;
	padding-bottom:2px;
}


</style>

	</head>
	<body>
		<div style="" id="toolbar">
			<input type="button" value="打印列表" onclick="doprint()" style="padding: 6px;" />
			<input type="button" value="刷新" onclick="window.location.reload()" style="padding: 6px;" />
		</div>
		<div style="margin: 40px auto; font-weight: bold;">
			<p style="text-align: center; margin: 20px auto;">
			<div style="float: right">
				<img width="80" height="80" style="position: relative; right: 30px;" src="/Static/images/logo.png">
			</div>
			<span style="text-align: center;"><h2 align="center">
					昆投互联网金融交易
				</h2>
				<h1 align="center">
					华夏银行出入金汇总与明细
				</h1>
			</span>
			</p>
			<p style="line-height: 60px;">
			<div style="float: left; margin: 0px;">
				会计日期：
				<span id="create_time"><fmt:formatDate value="${now}" pattern="yyyy/MM/dd"/></span>
			</div>
			<br />
			</p>
			华夏银行出入金汇总
			<table style="font-size: 13px; font-weight: bold">
				<thead>
					<tr>
						<th>
							发生日期
						</th>
						<th>
							入金总额
						</th>
						<th>
							入金总笔数
						</th>
						<th>
							出金总额
						</th>
						<th>
							出金总笔数
						</th>
						<th>
							状态
						</th>
						<th>
							处理日期
						</th>
						<th>
							处理者
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							${deal.workDay}
						</td>
						<td>
							<fmt:formatNumber value='${deal.amtIn}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${deal.countIn}
						</td>
						<td>
							<fmt:formatNumber value='${deal.amtOut}' type="currency" currencySymbol=""/>
						</td>
						<td>
							${deal.countOut}
						</td>
						<td>
							<c:if test="${deal.success}"><span class="green">成功</span></c:if>
							<c:if test="${!deal.success}"><span class="red">失败</span></c:if>
						</td>
						<td>
							<fmt:formatDate value="${deal.createDate}" type="date" />
						</td>
						<td>
							${deal.operator.realname}
						</td>
					</tr>
				</tbody>
			</table>
			<br />华夏银行出入金明细<br />
			<table>
				<thead>
					<tr>
						<th>
							发生日期
						</th>
						<th>
							子账号
						</th>
						 <th>
							用户名
						</th>   
						 <th>
							子账户名称
						</th>
						<th>
						           出入金类型
						</th>
						<th>
						          金额
						</th>  
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${deal.subs}" var="sub">
						<tr>
							<td>
								<fmt:formatDate value="${sub.checkDate}" type="date" />
							</td>
							<td>
								${sub.accountNo}
							</td>
							 <td>
								${sub.merAccountNo}
							</td>   
							 <td>
								${sub.accountName}
							</td>
							<td>
							    ${sub.trnxType_show}
							</td>
							<td>
							    <fmt:formatNumber value='${sub.amt}' type="currency" currencySymbol="" />
							</th>  
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>				
			<div style="float:right">
				<div style="float:left;width:140px">操作员:${session.LOGININFO.realname}</div><div style="margin-left:20px;float:left;width:140px">复核员:</div><div style="float:left;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			</div>
		</div>
	</body>
</html>
