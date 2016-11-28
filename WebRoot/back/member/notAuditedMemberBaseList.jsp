<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
    $(function(){
    $(".table_solid").tableStyleUI(); 
    });
	function toURL(url) {
		window.location.href = url;
	}
	function del(id) {
		var cofirmDialog = new dialogHelper();
		cofirmDialog.set_Title("确认要删除此会员吗？");
		cofirmDialog.set_Msg("执行这个操作，此会员将被删除，你确认要这么做吗？");
		cofirmDialog.set_Height("180");
		cofirmDialog.set_Width("650");
		cofirmDialog
				.set_Buttons( {
					'确定' : function(ev) {
						window.location.href = "/back/member/memberBaseAction!delete?id="
								+ id;
						$(this).dialog('close');
					},
					'取消' : function() {
						//这里可以调用其他公共方法。
					$(this).dialog('close');
				}
				});
		cofirmDialog.open();
		//window.location.href = "/back/member/memberBaseAction!delete?id=" + id;
	}
	$(document).ready(function() {
		//setTitle2("角色管理"); //重新设置切换tab的标题 
		});
</script>
<body><form id="form1" action="/back/member/memberBaseAction!notAuditedMemberlist" method="post">
		<input type='hidden' class='autoheight' value="auto" />
		<div id="myToolBar"
			class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<div style="float: left;">					
				<input type="hidden" name="page" value="1" />
				<input type="hidden" name="membertype"  value="${membertype}"/>
				会员名/用户名&nbsp;
				<input class="input_box" style="width: 120px;"  type="text" name="keyword" value="${keyword}" />
				<input type="submit" class="ui-state-default" value="查找" />			
			</div>
		</div>
		<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header ">
						<th>
							会员名称
						</th>
						<th>
							用户名
						</th>
						<th>
							所在区域
						</th>
						<%--<th>
							所在市
						</th>--%>
						<%--<th>
							类别
						</th>--%>
						<th>
							类型
						</th>
						<%--
						<th>
							地址
						</th>
						<th>
							固定电话
						</th>
						<th>
							移动电话
						</th>
						--%>
						<th>
							开户机构
						</th>
						<th>
							开户人
						</th>
						<th>
							开户时间
						</th>
						<th>
							操作
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.records}" var="memberbase">
						<tr>
							<td>
								<A
									href="/back/member/memberBaseAction!memberDetails?id=${memberbase.id}"
									title="点击查看详细信息"> <c:if
										test="${memberbase.category==\"1\"}">
							<script>document.write(name("${memberbase.pName}"));</script>
							</c:if> <c:if test="${memberbase.category==\"0\"}">
							<script>document.write(name("${memberbase.eName}"));</script>
							</c:if> </A>
							</td>
							<td>
								${memberbase.user.username}
							</td>
							<td>
								${memberbase.provinceName}&nbsp;${memberbase.cityName}
							</td>
							<%--<td>
								${memberbase.cityName}
							</td>--%>
							<td>
								<c:if test="${memberbase.category==\"1\"}">
							个人&nbsp;${memberbase.memberType.name}
							</c:if>
								<c:if test="${memberbase.category==\"0\"}">
							机构&nbsp;${memberbase.memberType.name}
							</c:if>
							</td>
							<%--<td>
								${memberbase.memberType.name}
							</td>--%>
							<%--
							<td>
								<c:if test="${memberbase.category==\"1\"}">
							${memberbase.pAddress}
							</c:if>
								<c:if test="${memberbase.category==\"0\"}">
							${memberbase.eAddress}
							</c:if>
							</td>
							<td>
								<c:if test="${memberbase.category==\"1\"}">
							${memberbase.pPhone}
							</c:if>
								<c:if test="${memberbase.category==\"0\"}">
							${memberbase.ePhone}
							</c:if>
							</td>
							<td>
								<c:if test="${memberbase.category==\"1\"}">
							${memberbase.pMobile}
							</c:if>
								<c:if test="${memberbase.category==\"0\"}">
							${memberbase.eMobile}
							</c:if>
							</td>
							--%>
							<td>
								${memberbase.user.org.name}
							</td>
							<td>
							    ${memberbase.creator.realname}
							</td>
							<td>
								<fmt:formatDate value="${memberbase.createDate}" type="date" />
							</td>
							<td>
								<%--<button class="ui-state-default"
									onclick="toURL('/back/member/memberBaseAction!memberDetails?id=${memberbase.id}');return false;">
									详细
								</button>--%>
								<span style="clear: both"></span>
								<button class="ui-state-default"
									onclick="toURL('/back/member/memberAuditAction!memberAudit?memberId=${memberbase.id}');return false;"
									style="display:<c:out value="${menuMap['memberBase_member_audit']}" />">
									审核
								</button>
							</td>
						</tr>
					</c:forEach>
					</tbody>
					<tbody>
					<tr> 
						<td colspan="7">
							<jsp:include page="/common/page.jsp"></jsp:include></td>
					</tr>
				</tbody>
			</table>
		</div>
		</form>
		<jsp:include page="/common/messageTip.jsp"></jsp:include>
</body>