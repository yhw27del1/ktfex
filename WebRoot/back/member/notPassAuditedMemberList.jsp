<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<link rel="stylesheet" href="/Static/css/member.css"
			type="text/css" />
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
   $(function (){
   $(".table_solid").tableStyleUI(); 
   		    $('#provinceCode').change(function(){
			    $.post('/back/region/regionAction!getchildren?region_id='+$(this).val(),null,function(data,status){
			    	$('#cityCode').children().remove();
			    	$('#cityCode').html(data);
				},'text');				
			});
   });
	function toURL(url) {
		window.location.href = url;
	}
	
	function disable(id){
		var cofirmDialog = new dialogHelper();
        cofirmDialog.set_Title("确认要注销此会员吗？");
        cofirmDialog.set_Msg("执行这个操作，此会员将被注销，注销后此会员将无法登录，你确认要这么做吗？");
        cofirmDialog.set_Height("180");
        cofirmDialog.set_Width("650");
        cofirmDialog.set_Buttons({
            '确定': function(ev) {
                    window.location.href = "/back/member/memberBaseAction!disable?id="+id;
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        cofirmDialog.open(); 
	}
	
	/**启用会员*/
	function enable(id){
	  	var cofirmDialog = new dialogHelper();
        cofirmDialog.set_Title("确认要启用此会员吗？");
        cofirmDialog.set_Msg("执行这个操作，将会启用此会员，你确认要这么做吗？");
        cofirmDialog.set_Height("180");
        cofirmDialog.set_Width("650");
        cofirmDialog.set_Buttons({
            '确定': function(ev) {
                    window.location.href = "/back/member/memberBaseAction!enable?id="+id;
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        cofirmDialog.open(); 
	}
	
	function del(id) {
	    var cofirmDialog = new dialogHelper();
        cofirmDialog.set_Title("确认要删除此会员吗？");
        cofirmDialog.set_Msg("执行这个操作，此会员将被删除，你确认要这么做吗？");
        cofirmDialog.set_Height("180");
        cofirmDialog.set_Width("650");
        cofirmDialog.set_Buttons({
            '确定': function(ev) {
                    window.location.href = "/back/member/memberBaseAction!delete?id="+id;
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        cofirmDialog.open(); 
		//window.location.href = "/back/member/memberBaseAction!delete?id=" + id;
	}
	
	function getNotPassAuditReason(memberId){
	
	  if(""!=memberId){
	    $.ajax({
	    url:"/back/member/memberAuditAction!getNotPassAuditedReason?memberId=" + memberId,
	    type:"post",
	    data:{"memberId":memberId},
	    success:function(data,textStatus){
	      alert(data);
	      $("#"+memberId).html(data);
	    },
	    error:function(textStatus,xmlHttpRequest,errorThrow){
	      alert(xmlHttpRequest);
	    }
	    });
	  }	
	}
</script>
<body>
	<form id="form1" action="/back/member/memberBaseAction!listByNotPassedAudited"
		method="post">
		<input type='hidden' class='autoheight' value="auto" />
		<div id="myToolBar" style="height:70px;"
			class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<div style="float: left;" >
			    <div style="margin: 5px;">
					<input type="hidden" name="page" value="1"/>
					会员名/用户名&nbsp;
					<input class="input_box" style="width: 120px;" type="text" name="keyword" value="${keyword}" />
					类型&nbsp;
					<s:select cssClass="input_select" name="memberTypeId"
						list="memberTypes" listKey="id" listValue="name"
						cssStyle="padding:1px;width:120px;" headerKey="0" headerValue="全部"></s:select>
					</div><div style="margin: 10px;">	
					所在区域&nbsp;
					<s:select cssClass="input_select" list="regions_province"
						id="provinceCode" value="provinceCode"
						name="provinceCode" listKey="areacode"
						listValue="areaname_l" cssStyle="padding:1px;width:120px;"
						headerKey="0" headerValue="全部"></s:select>
					<s:select cssClass="input_select" list="regions_city"
						id="cityCode" value="cityCode"
						name="cityCode" listKey="areacode" listValue="areaname_l"
						cssStyle="padding:1px;width:120px;" headerKey="0"
						headerValue="全部"></s:select>
					<c:if test="${topOrg==true}">开户机构&nbsp;<input class="input_box" style="width: 120px;" type="text"
							name="orgName" value="${orgName}" />
					</c:if>
					<input type="submit" class="ui-state-default" value="查找" />
					</div>
			</div>
			<!--  <div style="float: right; margin-right: 20px;">
				<button class="ui-state-default"
					onclick="toURL('/back/member/memberBaseAction!edit');	return false;"
                    style="display:<c:out value="${menuMap['memberBase_edit']}" />">
					会员开户
				</button>
			</div>-->
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
							未通过审核的原因
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
								<c:if test="${memberbase.category==\"1\"}">
							<script>document.write(name("${memberbase.pName}"));</script>
							</c:if>
								<c:if test="${memberbase.category==\"0\"}">
							<script>document.write(name("${memberbase.eName}"));</script>
							</c:if>
							</td>
							<td>
								${memberbase.user.username}
							</td>
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
							<span id="${memberbase.id}"></span>
							<script type="text/javascript">
							  $.ajax({
	    url:"/back/member/memberAuditAction!getNotPassAuditedReason",
	    type:"post",
	    dataType:"text",
	    data:{"memberId":"${memberbase.id}"},
	    success:function(data,textStatus){
	      //alert(data);
	      $("#${memberbase.id}").html(data);
	    },
	    error:function(xmlHttpRequest,textStatus,errorThrow){
	      //alert(textStatus);
	    }
	    });
							</script>
							</td>
							<td>
								<button class="ui-state-default"
									onclick="toURL('/back/member/memberBaseAction!memberDetails?id=${memberbase.id}'); return false;"
									style="display:<c:out value="${menuMap['memberBase_details']}" />">
									详细
								</button>
								<span style="clear: both"></span>
								<c:if
									test="${memberbase.state==\"1\" || memberbase.state==\"3\" }">
									<button class="ui-state-default"
										onclick="toURL('/back/member/memberBaseAction!edit?id=${memberbase.id}'); return false;"
										style="display:<c:out value="${menuMap['memberBase_edit']}" />">
										修改
									</button>
								</c:if>
								<span style="clear: both"></span>
								<c:if test="${memberbase.state==\"2\"}">
									<button class="ui-state-default"
										onclick="disable('${memberbase.id}');return false;"
										style="display:<c:out value="${menuMap['memberBase_disable']}" />">
										注销
									</button>
								</c:if>
								<c:if test="${memberbase.state==\"4\"}">
									<button class="ui-state-default"
										onclick="enable('${memberbase.id}');return false;"
										style="display:<c:out value="${menuMap['memberBase_enable']}" />">
										启用
									</button>
								</c:if>
								<%--<span style="clear: both"></span>
								<button class="ui-state-default"
									onclick="del('${memberbase.id}');">
									删除
								</button>--%>
							</td>
						</tr>
					</c:forEach>
						</tbody>
					<tbody>
					<tr>
						<td colspan="9">
							<jsp:include page="/common/page.jsp"></jsp:include></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</body>