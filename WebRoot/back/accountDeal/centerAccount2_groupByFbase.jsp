<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript">
	$(function() {
		$(".table_solid").tableStyleUI();
	});
</script>
<script type="text/javascript">
	function openCenterOutDialog(action,showcoding,code){
		$.dialog({
    		title:'中心账户出账',
    		width:'940px',
    		height:'480px',
    		content:'url:/back/accountDealAction!center_out?action='+action+'&showcoding='+showcoding+'&fcode='+code,
    		lock:true
    	});
	}
</script>

<body>

	<input type='hidden' class='autoheight' value="auto" />




	<div class="dataList ui-widget" style="border: none">


		<span style="font-size: 13px; line-height: 30px; float: left;">
			<c:choose>

				<c:when test="${date!=null}">
				<a href="javascript:history.go(-1);">
					<button class="ui-state-default" id="backTo">返回</button></a>
					<fmt:formatDate value="${date}" pattern="yyyy-MM-dd" /> 的清单&nbsp;
				</c:when>
				<c:otherwise>
				<a href="javascript:history.go(-1);"><button class="ui-state-default" id="backTo">返回</button></a>
     				从<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd" />到<fmt:formatDate
						value="${endDate}" pattern="yyyy-MM-dd" /> 的清单&nbsp;
				</c:otherwise>

			</c:choose>


		</span>



		<!-- 此DIV为默认展示 -->
		<form action="">
			<input type="hidden" name="page" value="1" /> <input type="hidden"
				name="date" id="date"
				value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>" />
			<!-- 为了返回使用 -->
			<input type="hidden" name="startDate"
				value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>" />
			<input type="hidden" name="endDate"
				value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>" />
			关键字：<input type="text" name="keyWord" value="${keyWord }"
				 /> 融资项目号：<input type="text" name="fcode"
				value="${fcode }"/> <input type="submit"
				name="submit" class="ui-state-default" value="搜索" />
			<table class="ui-widget ui-widget-content" style="margin: 0;">
				<thead>
					<tr class="ui-widget-header ">

						<th>类型</th>
						<th>融资方</th>
						<th>发生对象</th>
						<th>项目简称</th>
						<th>融资项目ID</th>
						<th>(收)发生额</th>
						<th>(付)发生额</th>
						<th>发生笔数</th>
						<th>操作者</th>
						<th>核算状态</th>
						<th>担保机构</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:set value="0" var="allmoney" />
					<c:set value="0" var="allmoney2" />
					<c:forEach items="${pageView.result}" var="calr">
						<tr>

							<td><c:choose>
									<c:when test="${calr.action==1}">
									
									融资款交割-划出
								</c:when>
									<c:when test="${calr.action==2}">
									投资款划入中心账户-划入
								</c:when>
									<c:when test="${calr.action==3}">
									三方结算-划入
								</c:when>
									<c:when test="${calr.action==4}">
									债权买入-划入
								</c:when>
									<c:when test="${calr.action==5}">
									债权卖出-划入
								</c:when>
									<c:when test="${calr.action==6}">
									融资服务费+罚金
								</c:when>
									<c:when test="${calr.action==7}">
									担保费+罚金
								</c:when>
									<c:when test="${calr.action==9}">
									交易手续费
								</c:when>
									<c:when test="${calr.action==21}">
									风险管理费-兴
								</c:when>
									<c:when test="${calr.action==22}">
									融资服务费-兴
								</c:when>
									<c:when test="${calr.action==23}">
									担保费-兴
								</c:when>
									<c:when test="${calr.action==31}">
									担保费
								</c:when>
									<c:when test="${calr.action==32}">
									风险管理费
								</c:when>
									<c:when test="${calr.action==33}">
									融资服务费
								</c:when>
									<c:when test="${calr.action==34}">
									保证金
								</c:when>
									<c:when test="${calr.action==36}">
									席位费
								</c:when>
									<c:when test="${calr.action==41}">
									内部转帐
								</c:when>
									<c:otherwise>${calr.action}</c:otherwise>
								</c:choose> </td>
							<td>${calr.realname }</td>
							<td>${calr.username }</td>
							<td><a
								href="/back/financingBaseAction!detail?id=${calr.fbase_id}"
								class="tooltip" title="${calr.shortname}"> <c:choose>
										<c:when test="${fn:length(calr.shortname) > 10}">
											<c:out value="${fn:substring(calr.shortname,0,10)}..." />
										</c:when>
										<c:otherwise>
											<c:out value="${calr.shortname}" />
										</c:otherwise>
									</c:choose>
							</a></td>
							<td>${calr.code}</td>
							<td>
								<!-- 此处本来取的是绝对值 --> 
								<c:if test="${calr.total_value>=0}">
									<fmt:formatNumber value='${calr.total_abs_value}' pattern="#,##0.00" />
									<c:set value="${allmoney + calr.total_abs_value}" var="allmoney" />
								</c:if> 
								<c:if test="${calr.total_value<0}">0</c:if>

							</td>
							<td>
								<c:if test="${calr.total_value < 0}">
									<!-- 此处取的是绝对值 -->
									<fmt:formatNumber value='${calr.total_abs_value}' pattern="#,##0.00" />
									<c:set value="${allmoney2 + calr.total_abs_value}" var="allmoney2" />
								</c:if> 
								<c:if test="${calr.total_value >= 0 }">0</c:if>
							</td>
							<td>
								${calr.count}
								<c:set value="${allcount + calr.count}" var="allcount" />
							</td>
							<td>${calr.operater}</td>
							<td>${calr.calculated}</td>
							<td>${calr.orgname}
							<%--
							 <button class="ui-state-default" onclick="openCenterOutDialog('${calr.action}','${calr.showcoding}','${calr.code}');return false;">出账到对应机构</button>
							  --%>
							 </td>
						</tr>
					</c:forEach>
					<tr>
						<td>本页总计</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>

						<td><fmt:formatNumber value="${allmoney}" pattern="#,##0.00" /></td>
						<td><fmt:formatNumber value="${allmoney2}" pattern="#,##0.00" /></td>
						<td>${allcount}笔</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="7"><jsp:include page="/common/page.jsp"></jsp:include>
						</th>
					</tr>
				</tfoot>
			</table>
		</form>
	</div>





</body>
<script>
	setIframeHeight(100);
</script>