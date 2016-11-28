<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp" %>
<script type="text/javascript">
$(function(){
	$("#modify").click(function(){
		if(!$("#oldPassword").val()){
			alert("请输入原始密码.");
			$("#oldPassword").focus();
			return false;
		}
		if(!$("#newPassword").val()){
			alert("请输入新密码."); 
			$("#newPassword").focus();
			return false;
		}
		if(!$("#newPassword2").val()){
			alert("请再次输入新密码.");  
			$("#newPassword2").focus();
			return false;
		}
		if($("#newPassword").val()!=$("#newPassword2").val()){
			alert("两次密码输入不正确");  
			$("#newPassword").focus();
			return false;
		}
		$.getJSON("/user/userManager!modifyPass?time="+new Date().getTime()+"&idStr="+$("#adminId").val()+"&userPassword="+$("#oldPassword").val()+"&newPassowrd="+$("#newPassword").val(),function(data){
			if(data=="0"){
				alert("修改失败，原始密码输入错误。"); 
				$("#oldPassword").focus();
			}else{
				alert("修改密码成功，请牢记您修改后的密码。");
			}
		});
		return false;
	});
	$("#reset").click(function(){
		$("#oldPassword").val("");
		$("#newPassword").val("");
		$("#newPassword2").val("");
		$("#oldPassword").focus();
		return false;
	});
});
</script>
<%String adminId = request.getParameter("id"); %>
<div class="ui-widget">
	<div style="margin: 0 0; padding: 0 .7em;" class="ui-state-highlight ui-corner-all">
		<p>
			<form id="modifyPassword" method="get">
				<input name='idStr' id="adminId"  value="<%=adminId %>" type="hidden" />
				<fieldset>
					<table>
						<tr>
							<td align="right">
								原始密码:
							</td>
							<td>
								<input type="password" name="userPassword" id="oldPassword" />
							</td>
							<td></td>
						</tr>
						<tr>
							<td align="right">
								新密码:
							</td>
							<td>
								<input type="password" name="user.password" id="newPassword" value="" />
							</td>
							<td></td>
						</tr>
						<tr>
							<td align="right">
								重复新密码:
							</td>
							<td>
								<input type="password" name="user.password2" id="newPassword2" />
							</td>
							<td></td>
						</tr>
						<tr>
						<td></td>
						<td><button id="modify" class="ui-state-default">修改</button>&nbsp;
						<button id="reset" class="ui-state-default">重置</button>
						</td></tr>
					</table>
				</fieldset>
			</form>
		</p>
	</div>
</div>
