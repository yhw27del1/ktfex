<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript">   
function toAddUser(groupId,groupName,groupRestrainId){  
   var url='/back/userGroupAction!groupSelectUserList?id='+groupId;    
   if('UserGroupRestrain1'==groupRestrainId){   
      url='/back/userGroupAction!groupSelectUser1List?id='+groupId;  
   }
   toURL(url);
}
$(document).ready(function(){ 
    $(".table_solid").tableStyleUI();  
});

function toURL(url){ 
   window.location.href = url; 
}  

 
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script> 
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>

<body>
<form id="form1" action="/back/userGroupAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/> 
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
     关键字&nbsp;<input type="text" name="keyWord" value="${keyWord}"/>  
	<button class="ui-state-default"  id="seachButton">查询</button>
  </div>
  <div style="float: right; margin-right: 20px;">
     <button class="ui-state-default"  onclick="toURL('/back/userGroupAction!ui');return false;"   style="display:<c:out value="${menuMap['userGroup_edit']}" />" >新增分组</button> 
  </div>		
</div>
<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header "> 
				<th>分组名称</th>
				<th>备注</th> 
				<th>状态</th> 
				<th>约束</th> 
				<th></th>
			</tr>
		</thead>
		<tbody  class="table_solid">
		<c:forEach items="${pageView.records}" var="entry">
			<tr>  
				<td>${entry.name}</td>  
				<td>${entry.note}</td>  	 
				<td>
				    <c:if test="${entry.state==0}"><span style="color:red;">停用</span></c:if>
					<c:if test="${entry.state!=0}"><span style="color:green;">使用中</span></c:if>	 
                </td>  	 
				<td>
				    ${entry.groupRestrainName} 
                </td>  	 
				<td>
				 <button class="ui-state-default" onclick="toAddUser('${entry.id}','${entry.name}','${entry.groupRestrainId}');return false;">会员</button>  
				 <button class="ui-state-default" onclick="toURL('/back/userGroupAction!ui?id=${entry.id}');return false;"  style="display:<c:out value="${menuMap['userGroup_edit']}" />"/>修改组</button>
				 <c:if test="${entry.state==0}">
				    <button class="ui-state-default" onclick="toURL('/back/userGroupAction!start?id=${entry.id}');return false;"  />启用</button>
				 </c:if>
				 <c:if test="${entry.state!=0}">
				   <button class="ui-state-default" onclick="toURL('/back/userGroupAction!stop?id=${entry.id}');return false;"  />停用</button>
				 </c:if> 
				 
 				</td>
			</tr>
		</c:forEach>
		</tbody>
		<tbody>
			<tr>
				<td colspan="4">  
					<jsp:include page="/common/page.jsp"></jsp:include></td>
			</tr>
		</tbody>
	</table> 
</div>
</form>
<%@ include file="/common/messageTip.jsp" %>
</body> 