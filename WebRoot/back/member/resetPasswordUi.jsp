<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript">
$(function(){
	$("#query").click(function(){
		$("#chargeAmount").val("");
		var name = $("#name").val();
		var username = $("#username").val();
		var idCardNo = $("#idCardNo").val();
		if(username == ""){
			alert("请输入用户名");
			$("#username").focus();
			return false;
		}else if(name == ""){
	        alert("请输入会员名称");
			$("#name").focus();
			return false;
		}else if(idCardNo == ""){
	        alert("请输入身份证");
			$("#idCardNo").focus();
			return false;
		}else{
			$("#queryform").submit();
		}
	});
	
});

</script>
<script type="text/javascript">
<!--
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
			           window.location.href="/back/member/memberBaseAction!resetPasswrodUi";
			        }else{
			           alert("重置密码失败，稍后再试!");  
			           window.location.href="/back/member/memberBaseAction!resetPasswrodUi";
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
-->
</script>
<body>
	<form id="queryform" action="/back/member/memberBaseAction!resetPasswrodQuery">
		<table style="padding:0 0;">
			<tbody>
				<tr>
					<td>
						用户名 ：
						<input size="20" type="text" name="userName" onkeyup="this.value=this.value.replace(/\s+/g,'')" value="${userName}" id="username" />
						&nbsp;
					</td>
					<td>
						会员名称：
						<input size="20" type="text" name="name" onkeyup="this.value=this.value.replace(/\s+/g,'')" value="${name}" id="name" />
						&nbsp;
					</td>
					<td>
						身份证：
						<input size="20" type="text" name="idCardNo" onkeyup="this.value=this.value.replace(/\s+/g,'')" value="${idCardNo}" id="idCardNo" />
						&nbsp;
					</td>
					<td>
						<input id="query" class="ui-state-default" type="button" value="查询">
					</td>
				</tr>
			</tbody>
		</table>
	</form>
	<div class="dataList ui-widget" style="border:0;">
		<table class="ui-widget ui-widget-content">
			<thead class="ui-widget-header ">
				<tr>
					<th width="20%" align="left">
						会员名称
					</th>
					<th width="20%" align="left">
						用户名
					</th>
					<th width="20%" align="left">
						余额
					</th>
					<th width="20%" align="left">
						身份证
					</th>
					<th width="20%" align="left">
						操作
					</th>
				</tr>
			</thead>
			<tbody id="my_list">
			<s:if test="canReset==true">
				<tr>
					<td width="20%">
						<c:if test="${memberBase.category==\"1\"}">
							${memberBase.pName}
							</c:if>
						<c:if test="${memberBase.category==\"0\"}">
							${memberBase.eName}
							</c:if>
					</td>
					<td width="20%">
						${memberBase.user.username}
					</td>
					<td width="20%">
						<fmt:formatNumber value='${memberBase.user.userAccount.balance}' type="currency" currencySymbol=""/>
					</td>
					<td width="20%">
						${memberBase.idCardNo}
					</td>
					<td width="20%">
						<button  class="ui-state-default"  onclick="reSetPassWord('${memberBase.user.id}');return false;" >重置密码</button>
					</td>
				</tr>
			</s:if>
			<s:else>
				<tr><td colspan="5">${errorStr}</td></tr>
			</s:else>
			</tbody>
		</table>
	</div>
</body>