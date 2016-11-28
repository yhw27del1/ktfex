<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>子账户签约</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
	<style type="text/css">
		.red{color:red;}
		.green{color:green;}
		.tip{color:red; font-size: 10px;}
	</style>
	<script type="text/javascript">
		$(function(){
			$(".table_solid").tableStyleUI();
			$("#startDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
			$("#endDate").datepicker({
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
		   			alert(data.tip);
		   			location.reload();
		   		},
		   		error:function(data){
		   			alert(data.tip);
		   			location.reload();
		   		}
		   	});
		}
	</script>
  </head>
<body>
<form id="dataForm">
<table border="0">
	<tr>
		<td align="right">
			<span style="color: red">*</span>会员编号：
		</td>
		<td>
			<input name="param.merAccountNo" maxlength="20" type="text"/>
		</td>
		<td align="right">
			<span style="color: red">*</span>交易市场当前总余额：
		</td>
		<td>
			<input name="param.amt" value="0" type="text"/>
		</td>
		<td align="right">
			办公电话：
		</td>
		<td>
			<input name="param.phone" placeholder="企业必须输入" type="text"/>
		</td>
	</tr>
	<tr>
		<td align="right">
			<span style="color: red">*</span>会员性质：
		</td>
		<td>
			<select name="param.prop">
				<option value="1">个人</option>
				<option value="0">企业</option>
			</select>
		</td>
		<td align="right">
			<span style="color: red">*</span>交易市场当前可用余额：
		</td>
		<td>
			<input name="param.amtUse" value="0" type="text"/>
		</td>
		<td align="right">
			移动电话：
		</td>
		<td>
			<input name="param.mobile" placeholder="企业必须输入" type="text"/>
		</td>
	</tr> 
	<tr>
		<td align="right">
			<span style="color: red">*</span>会员名称：
		</td>
		<td>
			<input name="param.accountName" type="text" />
		</td>
		<td align="right">
			联系人：
		</td>
		<td>
			<input name="param.linkMan" placeholder="企业必须输入" type="text" />
		</td>
		<td align="right">
			地址：
		</td>
		<td>
			<input name="param.address" placeholder="企业必须输入" type="text"/>
		</td>
	</tr>
	<tr>
		<td align="right">
			<span style="color: red">*</span>证件类型：
		</td>
		<td>
			<select name="param.lawType">
				<option value="1">身份证</option>
			</select>
		</td>
		<td align="right">
			<span style="color: red">*</span>证件号码：
		</td>
		<td>
			<input name="param.lawNo" type="text" />
		</td>
		<td align="right">
			复核标识：
		</td>
		<td>
			<input name="param.checkFlag" maxlength="1" placeholder="企业必须输入" type="text"/>
		</td>
	</tr>
	<tr>
		<td align="right">
			<span style="color: red">*</span>关联账户(卡号)：
		</td>
		<td>
			<input name="param.relaAcct" type="text" />
		</td>
		<td align="right">
			<span style="color: red">*</span>关联账户名：
		</td>
		<td>
			<input name="param.relaAcctName" type="text" />
		</td>
		<td align="right">
			<span style="color: red">*</span>关联账户开户行：
		</td>
		<td>
			<input name="param.relaAcctBank" type="text"/>
		</td>
	</tr>
	<tr>
		<td align="right">
			<span style="color: red">*</span>关联账户开户行地址：
		</td>
		<td>
			<input name="param.relaAcctBankAddr" type="text" />
		</td>
		<td align="right">
			<span style="color: red">*</span>关联账户开户行支付系统行号：
		</td>
		<td>
			<input name="param.relaAcctBankCode" type="text" />
		</td>
		<td align="right">
			法人姓名：
		</td>
		<td>
			<input name="param.lawName" placeholder="企业必须输入" type="text" />
		</td>
	</tr>
	<tr>
		<td align="right">
			<span style="color: red">*</span>短信通知：
		</td>
		<td>
			<select name="param.noteFlag">
				<option value="1">需要</option>
				<option value="0">不需要</option>
		</td>
		<td align="right">
			<span style="color: red">*</span>通知号码：
		</td>
		<td>
			<input name="param.notePhone" placeholder="需要短信通知必须输入" type="text" />
		</td>
		<td align="right">
			<span style="color: red">*</span>eMail：
		</td>
		<td>
			<input name="param.email" type="text" />
		</td>
	</tr>
	<tr>
		<td align="right">
			&nbsp;
		</td>
		<td>
			<button onclick="toURL('/back/hxbank/hxbankAction!subAccountSigned');return false;" class="ui-state-default" >子账户签约</button>
		</td>
		<td align="right">
			邮编：
		</td>
		<td>
			<input name="param.zipCode" placeholder="企业必须输入" maxlength="6" type="text" />
		</td>
		<td align="right" colspan="3">
			&nbsp;
		</td>
	</tr>
</table>
</form>
<br />
<form action="">
<div id="myToolBar"
		class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<input type="hidden" name="page" value="1" />
				签约日期&nbsp;从&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />" id="endDate"/>
				<input type="submit" class="ui-state-default" value="查询" />
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th>
						日期
					</th>
					<th>
						市场流水
					</th>
					<th>
						银行流水
					</th>
					<th>
						交易名称<br/>交易码
					</th>
					<th>
						会员编号
					</th>
					<th>
						子账号
					</th>
					<th>
						名称
					</th>
					<th>
						性质
					</th>
					<th>
						状态
					</th>
					<th>
						操作者
					</th>
					<th>
						备注
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
			<c:forEach items="${pageView.records}" var="entry">
					<tr>
						<td>
							<fmt:formatDate value="${entry.createDate}" type="date" />
						</td>
						<td>
							${entry.merchantTrnxNo}
						</td>
						<td>
							${entry.bankTxSerNo}
						</td>
						<td>
							${entry.name}<br/>${entry.trnxCode}
						</td>
						<td>
							${entry.merAccountNo}
						</td>
						<td>
							${entry.accountNo}
						</td>
						<td>
							${entry.accountName}
						</td>
						<td>
							<c:if test="${entry.prop=='1'}">个人</c:if>
							<c:if test="${entry.prop=='0'}">企业</c:if>
						</td>
						<td>
							<c:if test="${entry.success}"><span class="green">成功</span></c:if>
							<c:if test="${!entry.success}"><span class="red">失败</span></c:if>
						</td>
						<td>
							${entry.operator.realname}
						</td>
						<td>
							${entry.message}
						</td>
					</tr>
			</c:forEach>
			<tr>
				<td colspan="12">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>
</html>
