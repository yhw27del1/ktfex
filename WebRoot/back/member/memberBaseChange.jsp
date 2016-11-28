<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
<!--
   $(function (){
   $(".table_solid").tableStyleUI(); 
   });
   
   function toURL(url) {
		window.location.href = url;
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
-->
</script>
<body>
	<form id="form1" action="/back/member/memberBaseAction!change"
		method="post">
		<input type='hidden' class='autoheight' value="auto" />
		<div id="myToolBar" 
			class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<div style="float: left;" >
				<input type="hidden" name="page" />
					会员名/用户名&nbsp;
					<input class="input_box" style="width: 120px;" type="text" name="keyword" value="${keyword}" />
					<input type="submit" class="ui-state-default" value="查找" />
			</div>
		</div>
		<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header">
						<th>
							会员名称
						</th>
						<th>
						用户名
						</th>
						<%--<th>
							余额
						</th>--%>
						<th>
							所在区域
						</th>
						<th>
							类型
						</th>
						<th>
							开户机构
						</th>
						<th>
							开户时间
						</th>
						<th>
							最近变更人
						</th>
						<th>
						           最近变更时间
						</th>
						<th> 
						           操作
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.records}" var="memberbase">
						<tr>
							<td><%--<a href="/back/member/memberBaseAction!edit?id=${memberbase.id}">--%>
								<c:if test="${memberbase.category==\"1\"}">
							<script>document.write(name("${memberbase.pName}"));</script>
							</c:if>
							<c:if test="${memberbase.category==\"0\"}">
							<script>document.write(name("${memberbase.eName}"));</script>
							</c:if><%--</a>--%>
							</td>
							<td>
							${memberbase.user.username}
							</td>
							<%--<td>
								${memberbase.user.userAccount.balance}
							</td>--%>
							<td>
								${memberbase.provinceName}&nbsp;${memberbase.cityName}
							</td>
							<td>
								<c:if test="${memberbase.category==\"1\"}">
							个人&nbsp;${memberbase.memberType.name}
							</c:if>
								<c:if test="${memberbase.category==\"0\"}">
							机构&nbsp;${memberbase.memberType.name}
							</c:if>
							</td>
							<td>
								${memberbase.user.org.name}
							</td>
							<td>
								<fmt:formatDate value="${memberbase.createDate}" type="date" />
							</td>
							<td>
								${memberbase.changer.realname}
							</td>
							<td> 
								<fmt:formatDate value="${memberbase.updateDate}" type="date" />
							</td>
							<td> 
							<button class="ui-state-default"
										onclick="toURL('/back/member/memberBaseAction!edit2?id=${memberbase.id}'); return false;"
										style="display:<c:out value="${menuMap['member_change']}" />">
										变更
									</button>
				              <!-- <button  class="ui-state-default"  onclick="reSetPassWord('${memberbase.user.id}');return false;" >重置密码</button> -->		
				            </td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="9">
							<jsp:include page="/common/page.jsp"></jsp:include></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</body>