<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<title>会员级别</title>
		<link rel="stylesheet" type="text/css" href="/Static/js/tree/dhtmlXTree/dhtmlxtree.css">
		<link rel="stylesheet" href="/Static/css/member.css" type="text/css" />
		<link rel="stylesheet" href="/Static/js/validate/validateself-skin1.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/tree/dhtmlXTree/dhtmlxcommon.js"></script>
		<script type="text/javascript" src="/Static/js/tree/dhtmlXTree/dhtmlxtree.js"></script>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		<script type="text/javascript" src="/Static/js/validate/jquery.validate.js"></script>
		<script type="text/javascript">
	$(function() {
		$("#setMemberLevelForm").validate( {
			rules : {
				"memberLevel.levelname" : {
					required : true
				}
			},
			messages : {
				"memberLevel.levelname" : {
					required : "会员级别不能为空"
				}
			}
		});
				
	});
</script>
	</head>
	<body>
		<form action="/back/member/memberLevelAction!edit" method="post" id="setMemberLevelForm">
			<s:actionerror />
			<table>
				<tr>
					<td align="right">
						<span style="color: red">*</span><span class="title">会员类型：</span>
					</td>
					<td>
						<s:select cssClass="input_select" list="list" name="memberTypeid" listKey="id" listValue="name" theme="simple"></s:select>
					</td>
				</tr>
				<tr>
					<td align="right">
						<span style="color: red">*</span><span class="title">会员级别：</span>
					</td>
					<td>
						<input class="level_input_box" name="memberLevel.levelname" id="levelname" type="text" value="${memberLevel.levelname }" />
						<input type="hidden" name="id" value="${id}" />
					</td>
				</tr>

				<tr>
					<td colspan="2" align="right">
						<input type="submit" class="ui-state-default" id="submitBtn" value="保存" style="cursor: pointer" />
					</td>
				</tr>
				</form>
	</body>
</html>
