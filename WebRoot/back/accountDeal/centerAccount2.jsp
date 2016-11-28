<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>

<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	$("#backTo").click(function(){
		window.location.href="/back/accountDeal/accountDealAction!centerAccount?startDate=<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>";
		return false;
	});
	$("#mytabs dt a").click(function(){
		$(this).addClass("on").siblings().removeClass("on");
		var index = $("#mytabs dt a").index(this);
		
		$("#mytabs dd > div").siblings().removeClass("on");
		$($("#mytabs dd > div").get(index)).addClass("on");
		
	});
});


function dyniframesize(down) { 
	var pTar = null; 
	if (document.getElementById){ 
		pTar = document.getElementById(down); 
	} else{ 
		eval('pTar = ' + down + ';'); 
	} 
	if (pTar && !window.opera){ 
		//begin resizing iframe 
		pTar.style.display="block" 
		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){ 
			//ns6 syntax 
			pTar.height = pTar.contentDocument.getElementById("table_solid").offsetHeight +20; 
			console.log(pTar.contentDocument.getElementById("table_solid").offsetHeight)
		} else if (pTar.Document && pTar.Document.body.scrollHeight){ 
			//ie5+ syntax 
			pTar.height = pTar.Document.getElementById("table_solid").scrollHeight; 
		} 
	} 
} 
</script>
<style>
<!--
#mytabs {
	padding: 0;
	margin: 0;
}

#mytabs dt {
	clear: both;
	height: 31px;
	border-bottom: 1px solid #cccccc;
}

#mytabs dt a {
	border-left: 1px solid #cccccc;
	border-top: 1px solid #cccccc;
	padding: 0 30px 0 30px;
	margin: 0;
	float: right;
	cursor: pointer;
	font-size:13px;
	line-height:30px;
	
}

#mytabs dt a.on {
	background: #fff;
	border-bottom: 1px solid #fff;
}

#mytabs dd {
	margin: 0;
	clear: both;
}

#mytabs dd div.containor {
	background: #fff;
	display: none;
	border:1px solid #cccccc;
	border-top:none;
}


#mytabs dd div.on {
	display: block;
	background: #fff;
}
-->
</style>
<body>

	<input type='hidden' class='autoheight' value="auto" />
	



	<div class="dataList ui-widget" style="border:none">

		<dl id="mytabs">
			<dt>
				<span style="font-size:13px;line-height:30px;float:left;">
					<fmt:formatDate value="${date}" pattern="yyyy-MM-dd" />
					的清单&nbsp;
					<button class="ui-state-default" id="backTo">
						返回
					</button>
				</span>
				<a class="on">合计</a>
				<a>一次性收费</a>
				<a>按月收费</a>
				<a>其它</a>
				
			</dt>
			<dd>
				<!-- 此DIV为默认展示 -->
				<div class="on containor">
					<form action="">
		<input type="hidden" name="page" value="1" />
		<input type="hidden" name="date" value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>"/>
		<input type="hidden" name="startDate" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>"/>
		<input type="hidden" name="endDate" value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>"/>
					<table class="ui-widget ui-widget-content" style="margin:0;">
						<thead>
							<tr class="ui-widget-header ">
									<th>发生时间</th>
									<th>类型</th>
									<th>融资方</th>
									<th>发生对象</th>
									<th>项目简称</th>
									<th>融资项目ID</th>
									<th>(收)发生额</th>
									<th>(付)发生额</th>
									<th>操作者</th>
									<th>核算状态</th>
								</tr>
						</thead>
						<tbody class="table_solid">
							<c:forEach items="${pageView.records}" var="calr">
								<tr>
								<td>
										<fmt:formatDate value="${calr.createtime}" pattern="yyyy-MM-dd hh:mm:ss" />
										<c:set value="${allcount + 1}" var="allcount"/>
									</td>
									<td>
										<c:choose>
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
								<c:when test="${calr.action==8}">
									风险管理费+罚金
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
								<c:when test="${calr.action==35}">
                                    中心账户出账-划出
                                </c:when>
								<c:when test="${calr.action==36}">
									席位费
								</c:when>
								<c:when test="${calr.action==37}">
									信用管理费
								</c:when>
								<c:when test="${calr.action==41}">
									内部转帐
								</c:when>
											<c:otherwise>${calr.action}</c:otherwise>
										</c:choose>
									</td>
									<td>
										<script>document.write(name("${calr.object_.user.realname}"));</script>
									</td>
									<td>
										${calr.object_.user.username }
									</td>
									<td>
										<a href="/back/financingBaseAction!detail?id=${calr.fbase.id}');" class="tooltip" title="${calr.fbase.shortName}">
										 <c:choose>
												<c:when test="${fn:length(calr.fbase.shortName) > 10}">
													<c:out value="${fn:substring(calr.fbase.shortName,0,10)}..." />
												</c:when>
												<c:otherwise>
													<c:out value="${calr.fbase.shortName}" />
												</c:otherwise>
											</c:choose> </a>
									</td>
									<td>
										${calr.fbase.code}
									</td>
									<td>
										<!-- 此处本来取的是绝对值 -->
										<c:if test="${calr.value>=0}">
											<fmt:formatNumber value='${calr.abs_value}' pattern="#,##0.00" />
											<c:set value="${allmoney + calr.abs_value}" var="allmoney"/>
										</c:if>
										<c:if test="${calr.value<0}">
										0
										</c:if>
										
									</td>
									<td>
										<c:if test="${calr.value<0}">
										<!-- 此处取的是绝对值 -->
											<fmt:formatNumber value='${calr.abs_value}' pattern="#,##0.00" />
											<c:set value="${allmoney2 + calr.abs_value}" var="allmoney2"/>
										</c:if>
										<c:if test="${calr.value>=0}">
										0
										</c:if>
									</td>
									
									
									<td>
										${calr.operater.username}
									</td>
									<td>
										${calr.calculated}
									</td>
								</tr>
							</c:forEach>
								<tr>
									<td>本页总计</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td>${allmoney}</td>
									<td>${allmoney2}</td>
									<td>${allcount}笔</td>
									<td></td>
								</tr>
							</tbody>
						<tfoot>
					<tr>
						<th colspan="7">
							<jsp:include page="/common/page.jsp"></jsp:include>
						</th>
					</tr>
				</tfoot>
					</table>
					</form>
				</div>
				
				<div class="containor">
					<iframe src="/back/accountDealAction!centerAccount2_a?startDate=<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm" name="ifm" onload="javascript:dyniframesize('ifm');" width="100%"></iframe>
				</div>
				<div class="containor">
					<iframe src="/back/accountDealAction!centerAccount2_b?startDate=<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm2" name="ifm2" onload="javascript:dyniframesize('ifm2');" width="100%"></iframe>
				</div>
				<div class="containor">
					<iframe src="/back/accountDealAction!centerAccount2_c?startDate=<fmt:formatDate value="${date}" pattern="yyyy-MM-dd"/>" frameborder="0" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" id="ifm3" name="ifm3" onload="javascript:dyniframesize('ifm3');" width="100%"></iframe>
				</div>
			</dd>
		</dl>

	</div>

</body>
<script>
setIframeHeight(100);
</script>