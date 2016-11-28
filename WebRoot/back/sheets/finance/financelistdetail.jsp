<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>融资项目信息</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@ include file="/common/import.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script>
			$(function(){
				$(".table_solid").tableStyleUI(); 
				
			});
		</script>
		<style>
		#myToolBar {
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 30px;
			background: #000;
			filter: alpha(opacity = 60);
			-moz-opacity: 0.6;
			-khtml-opacity: 0.6;
			opacity: 0.6;
			line-height:30px;
			
		}
		#myToolBar a{
			color:#fff;
			font-weight:bold;
			text-decoration: none;
			margin-left:10px;
		}
		#myToolBar a:hover{
			
			text-decoration: underline;
		}
		</style>
	</head>
	<body>
		<div id="myToolBar" id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<a href="/sheets/finance!financerList?startDate=<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>&selectby=${selectby }&searchusername=${searchusername }"><<返回</a>
		</div>
		<div class="dataList ui-widget">
		<c:forEach items="${results}" var="result">
		<table style="margin-top:40px; " class="ui-widget ui-widget-content">
			<tr><td>融资项目编码</td><td>${result.code }</td><td>融资项目名称</td><td>${result.shortName}</td></tr>
			<tr><td>还款方式</td><td>${result.businessType.returnPattern }</td><td>借款期限</td><td>${result.businessType.term}</td></tr>
			<tr><td>还款次数</td><td>${result.businessType.returnTimes }</td><td>已融资额</td><td>${result.currenyAmount}</td></tr>
			<tr><td>年利率</td><td>${result.rate}%</td><td>投标人数</td><td>${result.haveInvestNum}</td></tr>
			<tr><td>投标开始日期</td><td><fmt:formatDate value="${result.startDate}" pattern="yyyy-MM-dd"/></td><td>投标截至日期</td><td><fmt:formatDate value="${result.endDate}" pattern="yyyy-MM-dd"/></td></tr>
			<tr><td>融资方签约时间</td><td><fmt:formatDate value="${result.qianyueDate}" pattern="yyyy-MM-dd"/></td><td></td><td></td></tr>
			<tr><td>融资会员号</td><td>${result.financier.user.username }</td><td>融资会员姓名</td><td><script>document.write(name("${result.financier.user.realname}"));</script></td></tr>
			<tr><td>融资会员类别</td><td><s:if test="#request.result.financier.category='0'">企业</s:if><s:else>个人</s:else></td><td>融资会员开户机构</td><td>${result.financier.orgNo }</td></tr>
			<tr><td>融资会员所在省市</td><td>${result.financier.provinceName }-${result.financier.cityName}</td><td>融资会员手机号</td><td><script>document.write(phone("${result.financier.mobile}"));</script></td></tr>
			<tr><td>担保方会员号</td><td>${result.guarantee.user.username }</td><td>担保方名称</td><td><script>document.write(name("${result.guarantee.user.realname}"));</script></td></tr>
		</table>
		<table style="margin-top:40px;" class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header "><th>时间</th><th>操作人</th><th>操作结果</th></tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${result.logs}" var="log" varStatus="sta">
					<tr>
						<td><fmt:formatDate value="${log.time}" pattern="yyyy-MM-dd HH:mm:00"/></td><td>${log.realname}</td><td>${log.operate }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</c:forEach>
		</div>
	</body>
</html>
