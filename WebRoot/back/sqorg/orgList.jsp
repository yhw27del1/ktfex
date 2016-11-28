
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">   
$(document).ready(function(){
    $(".table_solid").tableStyleUI();  
});
function toURL(url){ 
   window.location.href = url; 
} 
/**
*注销授权服务中心
*/
function cancel(url,orgName){
        var dlgHelper = new dialogHelper();
        dlgHelper.set_Title("您是否真要注销“"+orgName+"”？");
        dlgHelper.set_Msg("这个操作将注销“"+orgName+"”及其下属机构，您确定要这么做吗？");
        dlgHelper.set_Height("180");
        dlgHelper.set_Width("650");
        dlgHelper.set_Buttons({
            '确定': function(ev) {
                    toURL(url);
                    $(this).dialog('close'); 
            },
            '取消': function() {
                //这里可以调用其他公共方法。
                $(this).dialog('close');
            }
        });
        dlgHelper.open(); 
}
</script>
<body>
    <form id="form1" action="/back/sqOrgAction!sqList" method="post">
	<input type='hidden' class='autoheight' value="auto" />
	<div id="myToolBar"  class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		
			<div style="float: left;">
					<input type="hidden" name="page" value="1" />
					名称：
					<input type="text" name="keyWord" value="${keyWord}">
					&nbsp;&nbsp;编码：
					<input type="text" name="showCoding" value="${showCoding}">
					&nbsp;&nbsp;
					<input class="ui-state-default" type="submit" value="查询">
			</div>
			<div style="float: right; margin-right: 20px;">
				<button class="ui-state-default"
					onclick="toURL('/back/sqOrgAction!ui');return false;" 
                    style="display:<c:out value="${menuMap['org_sqAdd']}" />">
					新增机构
				</button>
			</div>
		
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header">
					<th width="20%">
						机构名称
					</th>
					<th width="7%">
						机构编码
					</th>
					<th width="8%">
						简称
					</th>
					<th width="10%">
						机构类型
					</th>
					<th width="8%">
						联系人
					</th>
					<th width="8%">
						联系手机
					</th>
					
					<!--  <th width="13%">
						地址
					</th>-->
	
					<th width="8%">
						创建人
					</th>
					<th width="8%">
						创建日期
					</th>
					<th width="5%">
						状态
					</th>
					<th width="25%">
						操作
					</th>
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="org">
					<tr>
						<td>
							<script>document.write(name("${org.name}"));</script>
						</td>
						<td>
							${org.showCoding}
						</td>
						<td>
							${org.shortName}
						</td>
						<td><c:if test="${org.type == 0}">担保机构</c:if>
				            <c:if test="${org.type == 1}">授权中心</c:if>
				            <c:if test="${org.type == 2}">担保+授权中心</c:if>
				        </td>
						<td>
							${org.orgContact.linkMan}
						</td>
						<td>
							<script>document.write(phone("${org.orgContact.mobile}"));</script>
						</td>
						<!--  <td>
							${org.orgContact.phone}
						</td>
						<td>
							${org.orgContact.address}
						</td>-->
						<!--  <td>
							${org.orgContact.postalcode}
						</td>-->
						<td>
							${org.createBy.realname}
						</td>
						<td>
							<fmt:formatDate value="${org.createDateBy}" pattern="yyyy-MM-dd" />
						</td>
						<td>
							<c:if test="${org.state==0}">正常</c:if>
							<c:if test="${org.state==1}"><span style="color: red;">已注销</span></c:if>
						</td>
						<td>
							<c:if test="${org.state==0}"><button class="ui-state-default"
								onclick="toURL('/back/sqOrgAction!ui?id=${org.id}');return false;"
								style="display:<c:out value="${menuMap['org_sqEdit']}" />" />
								修改
							</button>
							&nbsp;
							</c:if>
							<c:if test="${org.state==0}">
								<button class="ui-state-default"
									onclick="cancel('/back/sqOrgAction!cancel?id=${org.id}','${org.name}');return false;"
									style="display:<c:out value="${menuMap['org_sqCancel']}" />" title="注销此授权服务中心" />
									注销
								</button>
							</c:if>
							 &nbsp;
							<c:if test="${org.state==0}"><c:if test="${org.type == 1}"><button class="ui-state-default"
                                onclick="toURL('/back/sqOrgAction!newOrgUserUI?orgId=${org.id}');return false;"
                                style="display:<c:out value="${menuMap['org_sqNew']}" />" />
                                机构操作员
                            </button>
                           </c:if></c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tbody>
				<tr>
					<td colspan="11">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	</form>
</body>