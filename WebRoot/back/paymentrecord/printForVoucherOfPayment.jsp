<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*,com.kmfex.*"%>
<%@ include file="/common/taglib.jsp" %>
<%
	Date now = new Date();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>融资还款凭证</title>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			body{padding;0;margin:0;}
			.padd20{padding-top:20px;}
			.title{text-align:center;}
			.mLeft{float:left;}
			.mRight{float:right;}
			.clear{clear: both;}
			table{border-collapse:collapse;border:none;}
			td{border:solid #000 1px;padding:5px;text-align: center;}
			.toolbar,.toolbar-background{width:100%; position: fixed;top:0;left:0;height:26px;padding:3px 3px;}
			.toolbar-background{background-color: #000;filter:alpha(opacity=60); -moz-opacity:0.6; opacity: 0.6; }
			.toolbar{border-bottom:1px solid #000;/*-webkit-box-shadow:0 0 10px black;-moz-box-shadow:0 0 10px black;*/}
			.footer{margin-bottom:80px;}
			.footer div{ float:left;margin-right:100px;}
			.togglebutton{
				float:right;
			}
		</style>
		<script>
			$(function(){
				$("#print-button").click(function(){
					$(".toolbar,.toolbar-background").hide();
					$("body").removeClass("padd20");
					print();
					$("body").addClass("padd20");
					$(".toolbar,.toolbar-background").show();
				});
				$(".cfield").click(function(){
					var options = $(this).linkbutton("options")
					if(options['selected']){
						$("tr."+$(this).attr("tr")).show();
					}else{
						$("tr."+$(this).attr("tr")).hide();
					}
				});
			});
		</script>
	</head>
	<body class="padd20">
		<div class="toolbar-background"></div>
		<div class="toolbar">
			<a href="javascript:;" class="easyui-linkbutton" iconCls="icon-print" id="print-button">打印</a>
			<div class="togglebutton">
				<a href="#" class="easyui-linkbutton cfield" data-options="toggle:true,selected:true" tr="benjin">本金</a>
	        	<a href="#" class="easyui-linkbutton cfield" data-options="toggle:true,selected:true" tr="lixi">利息</a>
	        	<a href="#" class="easyui-linkbutton cfield" data-options="toggle:true,selected:true" tr="faxi">罚息</a>
	        	<a href="#" class="easyui-linkbutton cfield" data-options="toggle:true,selected:true" tr="rongzifuwufei">融资服务费</a>
	        	<a href="#" class="easyui-linkbutton cfield" data-options="toggle:true,selected:true" tr="fuwufeifaxi">服务费罚息</a>
	        	<a href="#" class="easyui-linkbutton cfield" data-options="toggle:true,selected:true" tr="danbaofei">担保费</a>
	        	<a href="#" class="easyui-linkbutton cfield" data-options="toggle:true,selected:true" tr="danbaofeifaxi">担保费罚息</a>
        	</div>
		</div>
		<c:forEach items="${list}" var="item">
			<h3 class="title">昆投互联网金融交易-结算部融资还款凭证</h3>
			<div class="info">
				<div class="mLeft"><label>担保机构:</label><span>${item['forgname']}-${item['forgno'] }</span></div>
				<div class="mRight"><label>日期:</label><span><fmt:formatDate value="${item['shdate']}" pattern="yyyy年MM月dd日"/></span></div>
			</div>
			<div class="clear"></div>
			<table width="100%">
				<tbody>
					<tr>
						<td rowspan="3" class="">收款人</td>
						<td>户名</td>
						<td>各投资人</td>
						<td rowspan="3">付款人</td>
						<td>融资方帐号及项目编号</td>
						<td><script>document.write(name("${item['frealname']}"));</script>&nbsp;${item['financbasecode'] }</td>
					</tr>
					<tr>
						<td rowspan="2">投资人数</td>
						<td rowspan="2">${item['times'] }人</td>
						<td>交易帐号</td>
						<td>${item['financiername'] }</td>
					</tr>
					<tr>
						<td>银行帐号</td>
						<td>${item['fbankaccount'] }</td>
					</tr>
					<tr class="benjin">
						<td colspan="2">本金</td>
						<td colspan="4" style="text-align: left">
							<c:set value="${item['shbj']}" var="shbj" scope="request"/>
							<%
								if(request.getAttribute("shbj")!=null){
									String shbj = request.getAttribute("shbj").toString();
									out.println(MoneyFormat.format(shbj,true));
								}
							%>
							<fmt:formatNumber value="${item['shbj']}" pattern="￥#,##0.00"/>
						</td>
					</tr>
					<tr class="lixi">
						<td colspan="2">利息</td>
						<td colspan="4" style="text-align: left">
							<c:set value="${item['shlx']}" var="shlx" scope="request"/>
							<%
								if(request.getAttribute("shlx")!=null){
									String shlx = request.getAttribute("shlx").toString();
									out.println(MoneyFormat.format(shlx,true));
								}
							%>
							<fmt:formatNumber value="${item['shlx']}" pattern="￥#,##0.00"/>
						</td>
					</tr>
					<tr class="faxi">
						<td colspan="2">罚息</td>
						<td colspan="4" style="text-align: left">
							<c:set value="${item['shfj']}" var="shfj" scope="request"/>
							<%
								if(request.getAttribute("shfj")!=null){
									String shfj = request.getAttribute("shfj").toString();
									out.println(MoneyFormat.format(shfj,true));
								}
							%>
							<fmt:formatNumber value="${item['shfj']}" pattern="￥#,##0.00"/>
						</td>
					</tr>
					<tr class="rongzifuwufei">
						<td colspan="2">融资服务费</td>
						<td colspan="4" style="text-align: left">
							<c:set value="${item['rongzifuwufei']}" var="rongzifuwufei" scope="request"/>
							<%
								if(request.getAttribute("rongzifuwufei")!=null){
									String rongzifuwufei = request.getAttribute("rongzifuwufei").toString();
									out.println(MoneyFormat.format(rongzifuwufei,true));
								}
							%>
							<fmt:formatNumber value="${item['rongzifuwufei']}" pattern="￥#,##0.00"/>
						</td>
					</tr>
					<tr class="fuwufeifaxi">
						<td colspan="2">服务费罚息</td>
						<td colspan="4" style="text-align: left">
							<c:set value="${item['fuwufeifajin']}" var="fuwufeifajin" scope="request"/>
							<%
								if(request.getAttribute("fuwufeifajin")!=null){
									String fuwufeifajin = request.getAttribute("fuwufeifajin").toString();
									out.println(MoneyFormat.format(fuwufeifajin,true));
								}
							%>
							<fmt:formatNumber value="${item['fuwufeifajin']}" pattern="￥#,##0.00"/>
						</td>
					</tr>
					<tr class="danbaofei">
						<td colspan="2">担保费</td>
						<td colspan="4" style="text-align: left">
							<c:set value="${item['danbaofei']}" var="danbaofei" scope="request"/>
							<%
								if(request.getAttribute("danbaofei")!=null){
									String danbaofei = request.getAttribute("danbaofei").toString();
									out.println(MoneyFormat.format(danbaofei,true));
								}
							%>
							<fmt:formatNumber value="${item['danbaofei']}" pattern="￥#,##0.00"/>
						</td>
					</tr>
					<tr class="danbaofeifaxi">
						<td colspan="2">担保费罚息</td>
						<td colspan="4" style="text-align: left">
							<c:set value="${item['danbaofeifajin']}" var="danbaofeifajin" scope="request"/>
							<%
								if(request.getAttribute("danbaofeifajin")!=null){
									String danbaofeifajin = request.getAttribute("danbaofeifajin").toString();
									out.println(MoneyFormat.format(danbaofeifajin,true));
								}
							%>
							<fmt:formatNumber value="${item['danbaofeifajin']}" pattern="￥#,##0.00"/>
						</td>
					</tr>
					<tr>
						<td colspan="6" style="text-align: left;" valign="top" height="50">
						摘要：
						${item['RETURNTIMES']}-${item['qs']}
						<c:choose>
							<c:when test="${item['state']==1}">正常还款</c:when>
							<c:when test="${item['state']==2}">提前还款</c:when>
							<c:when test="${item['state']==3}">逾期还款 逾期天数:${item['overdue_days'] }</c:when>
							<c:when test="${item['state']==4}">担保代偿</c:when>
						</c:choose>  
						<c:if test="${item['group_'] > 0}">[第${item['group_']}组]</c:if>
						&nbsp;
						${item['REMARK'] }
						
						</td>
					</tr>
				</tbody>
			</table>
			<div class="footer">
				<div><label>打印时间:</label><span><fmt:formatDate value="<%=now %>" pattern="yyyy-MM-dd hh:mm:ss"/></div>
				<div><label>经办:</label></div>
				<div><label>复核:</label></div>
			</div>
		</c:forEach>
	</body>
</html>