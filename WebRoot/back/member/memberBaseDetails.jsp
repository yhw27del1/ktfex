<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<title>会员详细信息</title>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
	</head>
	<body>
		<div class="outer">
			<div class="box">
				<div class="inner">
					<input type='hidden' class='autoheight' value="auto" />
				    <div>
                      <%@ include file="memberInfor.jsp" %>
					</div> 
					<div>
						<button class="ui-state-default"
							onclick="javascript: history.go(-1);">
							返回
						</button>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
