<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
 
$(document).ready(function(){   
        $(".table_solid").tableStyleUI();
		//setTitle2("管理员管理"); //重新设置切换tab的标题  
			$("#seachButton").click(function() {
				$("#form1").submit();
			}); 
		});
</script> 
<body>
<form id="form1" action="/back/sqUserAction!sqList" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/> 
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">  
 	关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>
	<button class="ui-state-default"  id="seachButton">搜索</button>
</div>

<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>用户名</th>
				<th>真实名</th>
				<th>地址</th>
				<th>邮编</th>
				<th>手机</th>
				<th>座机</th>
				<th>状态</th> 
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="user">
			<tr> 
				<td>${user.username}</td>
				<td>${user.realname}</td>
				<td>${user.userContact.address}</td>
				<td>${user.userContact.postalcode}</td>
				<td>${user.userContact.mobile}</td>
				<td>${user.userContact.phone}</td>
				<td>
				<c:if test="${user.enabled}">
	                <span style="color:#4169E1;">使用中</span> 
 				</c:if>
				<c:if test="${!user.enabled}">
				    <span style="color:red;">停用</span>
				</c:if>		
				</td> 
			</tr>
		</c:forEach>
		</tbody>
		<tbody>
			<tr>
				<td colspan="8">
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
</body>