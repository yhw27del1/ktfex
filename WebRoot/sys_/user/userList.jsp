<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">   
function toURL(url){ 
   window.location.href = url; 
}
function del(id){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("确认要删除现有用户吗？");
        dlgHelper.set_Msg("执行这个操作，此用户将被删除，你确认要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                    window.location.href = "/sys_/userAction!del?id="+id;
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 

}
function userStop(id){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("确认要停用用户吗？");
        dlgHelper.set_Msg("执行这个操作，此用户将被停用，你确认要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                    window.location.href = "/sys_/userAction!stop?id="+id;
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 

}
function reSetPassWord(userId){   
     var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("确认要重置用户密码吗？");
        dlgHelper.set_Msg("执行这个操作,此用户的密码重新设置为123456，你确认要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) { 
			    $.post("/sysCommon/sysUserAction!reSetPassWord", {id:userId}, function(data){
			      if(1 == data){
			           alert("重置密码成功，密码为:123456,请及时修改!");
			        }else{
			           alert("重置密码失败，稍后再试!");  
			      }
			   }); 
			   $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。  
                $(this).dialog('close');
            }
        });
        dlgHelper.open();   
}
function userStart(id){ 
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("确认要启用现有用户吗？");
        dlgHelper.set_Msg("执行这个操作，此用户将被启用，你确认要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                    window.location.href = "/sys_/userAction!start?id="+id;
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 

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
<form id="form1" action="/sys_/userAction!list" method="post"> 
<input type='hidden' class='autoheight' value="auto" /> 
<input type="hidden" name="page" value="1"/> 
<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
 	<button class="ui-state-default" onclick="toURL('/sys_/userAction!ui');return false;"  style="display:<c:out value="${menuMap['user_add']}" />">新增</button> 
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
				<th width="16%">操作</th>
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
				<td><button onclick="toURL('/sys_/userAction!ui?id=${user.id}');return false;" class="ui-state-default" style="display:<c:out value="${menuMap['user_edit']}" />">修改</button>&nbsp;
				<button  class="ui-state-default"  onclick="del('${user.id}');return false;"  style="display:<c:out value="${menuMap['user_del']}" />">删除</button>
				<c:if test="${user.enabled}">
						<button  class="ui-state-default"  onclick="userStop('${user.id}');return false;"  style="display:<c:out value="${menuMap['user_stop']}" />">停用</button>
 				</c:if>
				<c:if test="${!user.enabled}">
				    <button  class="ui-state-default"  onclick="userStart('${user.id}');return false;"  style="display:<c:out value="${menuMap['user_start']}" />">启用</button>
				</c:if>	
				<button  class="ui-state-default"  onclick="reSetPassWord('${user.id}');return false;" >重置密码</button>		
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