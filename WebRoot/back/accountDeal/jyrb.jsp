<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	function toURL(url) {
		window.location.href = url;
	}
</script>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="/back/four.jsp"></script>
<script type="text/javascript">
$(function(){
	$(".table_solid").tableStyleUI();
	$("#startDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
	$("#endDate").datepicker({
		numberOfMonths: 2,
        dateFormat: "yy-mm-dd"
    });
    $("#ui-datepicker-div").css({'display':'none'});
    $("#extend").click(function(){
    	$.dialog({
    		title:'按机构统计',
    		width:'980px',
    		height:'500px',
    		content:'url:/back/accountDealAction!jyrb_extend',
    		lock:true,
    		min:false
    	}).max();
    });
});
</script>
<body>
<form action="">
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
  <div style="float: left;">
  日期&nbsp;<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date' />" id="startDate"/>
   <input type="submit" class="ui-state-default" value="查询" />&nbsp;
   <a href="/back/accountDealAction!jyrb_extend" target="_black">按机构统计</a>
  </div>
</div>
	<br />截至<fmt:formatDate value="${today}" type="date" pattern="yyyy年MM月dd日" />已挂单未满标(<s:property value="#request.list23.size()"/>个)
	<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header"> 
				<th align="center">项目编号</th>
				<th align="center">项目简称</th> 
				<th align="center">融资方</th>
				<th align="center">总融资额/可融资额</th>
				<th align="center">已融资额</th>
				<th align="center">投标人数</th> 
				<th align="center">投标截止</th>
				<th align="center">挂单时间</th>
				<th align="center">状态</th>
				<th align="center">进度</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${list23}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td>
					<a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}');return false;"  class="tooltip" title="${entry.shortname}">
						<c:choose>
							<c:when test="${fn:length(entry.shortname) > 20}">
								<c:out value="${fn:substring(entry.shortname,0,16)}..." />
							</c:when>
							<c:otherwise>
								<c:out value="${entry.shortname}" />
							</c:otherwise>
						</c:choose>
					</a>
				</td>
				<td>
				<c:if test="${(entry.ename)!= null}">
					<script>document.write(name("${entry.ename}"));</script>
				</c:if>	 
				<c:if test="${(entry.ename)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxamount}'   type="currency" currencySymbol=""/>/<fmt:formatNumber value='${entry.curcaninvest}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.currenyamount}'   type="currency" currencySymbol=""/></td>
				<td>${entry.haveinvestnum}</td>
				<td>${entry.enddate}</td>
				<td>${entry.guadan}</td>
				<td> 
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中</span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已交易确认</span></c:if>	
					<c:if test="${entry.state=='6'}"><span style="color:green;">已费用确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约</span></c:if>	
					<c:if test="${entry.state=='8'}"><span style="color:red;">已撤单</span></c:if>	
				</td> 
				<td> 
					  <span style="color:#4169E1;"><fmt:formatNumber value='${entry.jd}' type="currency" currencySymbol=""/>%</span>
				</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table> 
	</div>
	
	
	<br />截至<fmt:formatDate value="${today}" type="date" pattern="yyyy年MM月dd日" />已满标未交易确认(<s:property value="#request.list4.size()"/>个)
	<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header"> 
				<th align="center">项目编号</th>
				<th align="center">项目简称</th> 
				<th align="center">融资方</th>
				<th align="center">总融资额/可融资额</th>
				<th align="center">已融资额</th>
				<th align="center">投标人数</th> 
				<th align="center">投标截止</th>
				<th align="center">满标时间</th>
				<th align="center">状态</th>
				<th align="center">进度</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${list4}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!list');return false;"  class="tooltip" title="${entry.shortname}">
				<c:choose>
					<c:when test="${fn:length(entry.shortname) > 10}">
						<c:out value="${fn:substring(entry.shortname,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortname}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				 <td>
				<c:if test="${(entry.ename)!= null}">
					<script>document.write(name("${entry.ename}"));</script>
				</c:if>	 
				<c:if test="${(entry.ename)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxamount}'   type="currency" currencySymbol=""/>/<fmt:formatNumber value='${entry.curcaninvest}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.currenyamount}'   type="currency" currencySymbol=""/></td>
				<td>${entry.haveinvestnum}</td>
				<td>${entry.enddate}</td>
				<td>${entry.mane}</td>
				<td> 
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中</span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已交易确认</span></c:if>	
					<c:if test="${entry.state=='6'}"><span style="color:green;">已费用确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约</span></c:if>	
					<c:if test="${entry.state=='8'}"><span style="color:red;">已撤单</span></c:if>	
				</td> 
				<td> 
					  <span style="color:#4169E1;"><fmt:formatNumber value='${entry.jd}'   type="currency" currencySymbol=""/>%</span>
				</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table> 
	</div>
	
	
	<br />截至<fmt:formatDate value="${today}" type="date" pattern="yyyy年MM月dd日" />已交易确认未费用确认(<s:property value="#request.list5.size()"/>个)
	<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header"> 
				<th align="center">项目编号</th>
				<th align="center">项目简称</th> 
				<th align="center">融资方</th>
				<th align="center">总融资额/可融资额</th>
				<th align="center">已融资额</th>
				<th align="center">投标人数</th> 
				<th align="center">投标截止</th>
				<th align="center">交易确认</th>
				<th align="center">状态</th>
				<th align="center">进度</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${list5}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!list');return false;"  class="tooltip" title="${entry.shortname}">
				<c:choose>
					<c:when test="${fn:length(entry.shortname) > 20}">
						<c:out value="${fn:substring(entry.shortname,0,16)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortname}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				 <td>
				<c:if test="${(entry.ename)!= null}">
					<script>document.write(name("${entry.ename}"));</script>
				</c:if>	 
				<c:if test="${(entry.ename)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxamount}'   type="currency" currencySymbol=""/>/<fmt:formatNumber value='${entry.curcaninvest}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.currenyamount}'   type="currency" currencySymbol=""/></td>
				<td>${entry.haveinvestnum}</td>
				<td>${entry.enddate}</td>
				<td>${entry.jyqr}</td>
				<td> 
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中</span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已交易确认</span></c:if>
					<c:if test="${entry.state=='6'}"><span style="color:green;">已费用确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约</span></c:if>
					<c:if test="${entry.state=='8'}"><span style="color:red;">已撤单</span></c:if>
				</td> 
				<td> 
					  <span style="color:#4169E1;"><fmt:formatNumber value='${entry.jd}'   type="currency" currencySymbol=""/>%</span>
				</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table> 
	</div>
	
	
	<br />截至<fmt:formatDate value="${today}" type="date" pattern="yyyy年MM月dd日" />已费用确认未签约(<s:property value="#request.list6.size()"/>个)
	<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header"> 
				<th align="center">项目编号</th>
				<th align="center">项目简称</th> 
				<th align="center">融资方</th>
				<th align="center">总融资额/可融资额</th>
				<th align="center">已融资额</th>
				<th align="center">投标人数</th> 
				<th align="center">投标截止</th>
				<th align="center">费用确认</th>
				<th align="center">状态</th>
				<th align="center">进度</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${list6}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!list');return false;"  class="tooltip" title="${entry.shortname}">
				<c:choose>
					<c:when test="${fn:length(entry.shortname) > 20}">
						<c:out value="${fn:substring(entry.shortname,0,16)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortname}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				 <td>
				<c:if test="${(entry.ename)!= null}">
					<script>document.write(name("${entry.ename}"));</script>
				</c:if>	 
				<c:if test="${(entry.ename)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxamount}'   type="currency" currencySymbol=""/>/<fmt:formatNumber value='${entry.curcaninvest}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.currenyamount}'   type="currency" currencySymbol=""/></td>
				<td>${entry.haveinvestnum}</td>
				<td>${entry.enddate}</td>
				<td>${entry.fyqr}</td>
				<td> 
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待发布</span></c:if>
					<c:if test="${entry.state=='1.5'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中</span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已交易确认</span></c:if>
					<c:if test="${entry.state=='6'}"><span style="color:green;">已费用确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约</span></c:if>
					<c:if test="${entry.state=='8'}"><span style="color:red;">已撤单</span></c:if>
				</td> 
				<td> 
					  <span style="color:#4169E1;"><fmt:formatNumber value='${entry.jd}'   type="currency" currencySymbol=""/>%</span>
				</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table> 
	</div>
	
	
	<br /><fmt:formatDate value="${startDate}" type="date" pattern="yyyy年MM月dd日" />已签约(<s:property value="#request.list7.size()"/>个)
	<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header"> 
				<th align="center">项目编号</th>
				<th align="center">项目简称</th> 
				<th align="center">融资方</th>
				<th align="center">总融资额/可融资额</th>
				<th align="center">已融资额</th>
				<th align="center">投标人数</th> 
				<th align="center">签约日期</th>
				<th align="center">状态</th>
				<th align="center">进度</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${list7}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!list');return false;"  class="tooltip" title="${entry.shortname}">
				<c:choose>
					<c:when test="${fn:length(entry.shortname) > 20}">
						<c:out value="${fn:substring(entry.shortname,0,16)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortname}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				 <td>
				<c:if test="${(entry.ename)!= null}">
					<script>document.write(name("${entry.ename}"));</script>
				</c:if>	 
				<c:if test="${(entry.ename)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxamount}'   type="currency" currencySymbol=""/>/<fmt:formatNumber value='${entry.curcaninvest}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.currenyamount}'   type="currency" currencySymbol=""/></td>
				<td>${entry.haveinvestnum}</td>
				<td>${entry.qianyuedate}</td>
				<td> 
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中</span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已交易确认</span></c:if>
					<c:if test="${entry.state=='6'}"><span style="color:green;">已费用确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约</span></c:if>
					<c:if test="${entry.state=='8'}"><span style="color:red;">已撤单</span></c:if>
				</td> 
				<td> 
					<span style="color:#4169E1;"><fmt:formatNumber value='${entry.jd}'   type="currency" currencySymbol=""/>%</span>
				</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table> 
	</div>
	
	
	<br /><fmt:formatDate value="${startDate}" type="date" pattern="yyyy年MM月dd日" />已撤单(<s:property value="#request.list8.size()"/>个)
	<div class="dataList ui-widget">
	<table class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header"> 
				<th align="center">项目编号</th>
				<th align="center">项目简称</th> 
				<th align="center">融资方</th>
				<th align="center">总融资额/可融资额</th>
				<th align="center">已融资额</th>
				<th align="center">投标人数</th> 
				<th align="center">撤单日期</th>
				<th align="center">状态</th>
				<th align="center">进度</th>
			</tr>
		</thead>
		<tbody class="table_solid">
		<c:forEach items="${list8}" var="entry">
			<tr> 
				<td>${entry.code}</td>
				<td><a href="#" onclick="toURL('/back/financingBaseAction!detail?id=${entry.id}&directUrl=/back/financingBaseAction!list');return false;"  class="tooltip" title="${entry.shortname}">
				<c:choose>
					<c:when test="${fn:length(entry.shortname) > 10}">
						<c:out value="${fn:substring(entry.shortname,0,10)}..." />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.shortname}" />
					</c:otherwise>
				</c:choose>
				</a></td>
				 <td>
				<c:if test="${(entry.ename)!= null}">
					<script>document.write(name("${entry.ename}"));</script>
				</c:if>	 
				<c:if test="${(entry.ename)== null}">
					暂无
				</c:if>
				</td>
				<td><fmt:formatNumber value='${entry.maxamount}'   type="currency" currencySymbol=""/>/<fmt:formatNumber value='${entry.curcaninvest}' type="currency" currencySymbol=""/></td>
				<td><fmt:formatNumber value='${entry.currenyamount}'   type="currency" currencySymbol=""/></td>
				<td>${entry.haveinvestnum}</td>
				<td>${entry.modifydate}</td>
				<td> 
			  	    <c:if test="${entry.state=='0'}"><span style="color:#4169E1;">待审核</span></c:if>
					<c:if test="${entry.state=='1'}"><span style="color:green;">待挂单</span></c:if>
					<c:if test="${entry.state=='2'}"><span style="color:red;">投标中 </span></c:if>
					<c:if test="${entry.state=='3'}"><span style="color:#4169E1;">部分投标</span></c:if>
					<c:if test="${entry.state=='4'}"><span style="color:green;">已满标</span></c:if>
					<c:if test="${entry.state=='5'}"><span style="color:red;">已交易确认</span></c:if>
					<c:if test="${entry.state=='6'}"><span style="color:green;">已费用确认</span></c:if>
					<c:if test="${entry.state=='7'}"><span style="color:red;">已签约</span></c:if>
					<c:if test="${entry.state=='8'}"><span style="color:red;">已撤单</span></c:if>
				</td> 
				<td> 
					<span style="color:#4169E1;"><fmt:formatNumber value='${entry.jd}'   type="currency" currencySymbol=""/>%</span>
				</td> 
			</tr>
		</c:forEach>
		</tbody>
	</table> 
	</div>
</form>
</body>
