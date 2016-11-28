<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<link rel="stylesheet" href="/Static/css/member.css"
	type="text/css" />
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>

<script type="text/javascript">
   $(function (){
   $(".table_solid").tableStyleUI(); 
   var b = '${bank}';
   $("option[value='"+b+"']",$("#bank")).attr("selected",true);
   var ss = '${signState}';
   $("option[value='"+ss+"']",$("#signState")).attr("selected",true);
   
   		    $('#provinceCode').change(function(){
			    $.post('/back/region/regionAction!getchildren?region_id='+$(this).val(),null,function(data,status){
			    	$('#cityCode').children().remove();
			    	$('#cityCode').html(data);
				},'text');				
			});
			
			/* $("#startDate").datepicker({
		        //showOn: 'button',
		        buttonImageOnly: false,
		        stepMonths:12,
		        prevText:"上一年",
		        nextText:"下一年",
		        //yearRange:"15:01",
		        changeMonth: true,
		        changeYear: true,
		        numberOfMonths: 1,
		        dateFormat: "yy-mm-dd",
		        maxDate:0,
		        //minDate: -(100*365),
		        beforeShow: function(input, inst) { 
		         setIframeHeight(180);
		        }
		    });
		    $("#endDate").datepicker({
		        //showOn: 'button',
		        buttonImageOnly: false,
		        stepMonths:12,
		        prevText:"上一年",
		        nextText:"下一年",
		        //yearRange:"15:01",
		        changeMonth: true,
		        changeYear: true,
		        numberOfMonths: 1,
		        dateFormat: "yy-mm-dd",
		        maxDate: 0,
		        //minDate: -(100*365),
		        beforeShow: function(input, inst) { 
		         setIframeHeight(180);
		        }
		    });*/
		    $("#startDate").datepicker({
                dateFormat: "yy-mm-dd",
                showOn: "button",
                buttonImage: "/Static/images/calendar.gif",
                buttonImageOnly: true,
                onSelect: function( selectedDate ) {
		        	var date = new Date(selectedDate);
		        	date.setMonth(date.getMonth()+1);
		        	$( "#endDate" ).datepicker( "option", "maxDate", date );
		        	$( "#endDate" ).datepicker( "option", "minDate", selectedDate );
		        	$( "#endDate" ).datepicker( "setDate", date ).datepicker('hide');;
        }
    });
	$("#endDate").datepicker({
        dateFormat: "yy-mm-dd",
        showOn: "button",
        buttonImage: "/Static/images/calendar.gif",
        buttonImageOnly: true
    });
		    
		    $("#ui-datepicker-div").css({'display':'none'});
   });
   
	function toURL(url) {
		window.location.href = url;
	}
	
	/**
	*注销指定id号的会员
	*/
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
                $(this).dialog('close');
            }
        });
        cofirmDialog.open(); 
	}
	
	/**启用指定id号的会员*/
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
                $(this).dialog('close');
            }
        });
        cofirmDialog.open(); 
	}
	
	function queryMembers(){
	  $("#excel").val("false");
	  $("#form1").submit();
	 }
	 
    function exportExcel(){
	   $("#excel").val("true");
	   $("#form1").submit();
	   $("#excel").val("false");
	 }
	 
	function memberTypeChanged(obj){
	  $("#exportExcelButton").hide();
	}
	
	function showInfo(id){
         showModalDialog('/back/member/memberBaseAction!showlog?id=' + id,'','dialogWidth:700px;dialogHeight:500px;center:yes;help:no;status:no'); 
       return false;
}
</script>
<body>
	<form id="form1" action="/back/member/memberBaseAction!list"
		method="post">
		<input type='hidden' class='autoheight' value="auto" />
		<div id="myToolBar" style="height: 70px;"
			class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
			<div style="float: left;">
				<div style="margin: 5px;">
					<input type="hidden" name="page" value="1" />
					<input type="hidden" name="excel" id="excel" value="false" />
					会员/用户：
					<input class="input_box" style="width: 120px;" type="text"
						name="keyword" value="${keyword}" />
					&nbsp;状态：
					<s:select cssClass="input_select" name="memberState"
						list="memberStates" listKey="key" listValue="value"></s:select>
					&nbsp;类型：
					<s:select cssClass="input_select" name="memberTypeId"
						list="memberTypes" listKey="id" listValue="name"
						cssStyle="padding:1px;width:80px;" headerKey="0" headerValue="全部" onchange="memberTypeChanged(this)"></s:select>
					&nbsp;所在区域：
					<s:select cssClass="input_select" list="regions_province"
						id="provinceCode" value="provinceCode" name="provinceCode"
						listKey="areacode" listValue="areaname_s"
						cssStyle="padding:1px;width:80px;" headerKey="0" headerValue="全部"></s:select>
					<s:select cssClass="input_select" list="regions_city" id="cityCode"
						value="cityCode" name="cityCode" listKey="areacode"
						listValue="areaname_s" cssStyle="padding:1px;width:80px;"
						headerKey="0" headerValue="全部"></s:select>
				</div>
				<div style="margin: 5px;">
					<c:if test="${topOrg==true}">开户机构 ： 
					<input
							class="input_box" style="width: 120px;" type="text"
							name="orgName" value="${orgName}" />
					</c:if>
					&nbsp;开户时间：
					从<input readonly="readonly" type="text" name="startDate"
						id="startDate" style="width: 90px"
						value="<s:date name="startDate" format="yyyy-MM-dd" />">
					到<input readonly="readonly" type="text" name="endDate" id="endDate" style="width: 90px"
						value="<s:date name="endDate" format="yyyy-MM-dd" />">
					签约行&nbsp;
					<select name="bank" id="bank">
						<option value="全部">全部</option>
						<option value="华夏银行">华夏银行</option>
						<option value="招商银行">招商银行</option>
					</select>
					签约状态&nbsp;
					<select name="signState" id="signState">
						<option value="全部">全部</option>
						<option value="0">未签约</option>
						<option value="1">签约中</option>
						<option value="2">已签约</option>
						<option value="3">已解约</option>
					</select>
					<input type="button" class="ui-state-default" value="查找" onclick="queryMembers()"/>
					&nbsp;&nbsp;&nbsp;&nbsp;<c:if test="${fn:length(pageView.records)>0 &&  (null != memberTypeId && \"0\" != memberTypeId)}">		
					<input id="exportExcelButton" style="display:<c:out value="${menuMap['memberBaseList_exportExcel']}" />" type="button" class="ui-state-default" value="导出Excel"
						onclick="exportExcel()" /></c:if>
				</div>
			</div>
			<div style="float: right; margin-right: 20px;">
				<button class="ui-state-default"
					onclick="toURL('/back/member/memberBaseAction!edit');return false;"
					style="display:<c:out value="${menuMap['memberBase_edit']}" />">
					会员开户
				</button>
			</div>
		</div>

		<div class="dataList ui-widget">
			<table class="ui-widget ui-widget-content">
				<thead>
					<tr class="ui-widget-header">
						<th rowspan="2">
							会员名称
						</th>
						<th rowspan="2">
							用户名
						</th>
						<th rowspan="2">
							等级
						</th>
						<th rowspan="2">
							可用余额
						</th>
						<th rowspan="2">
                            冻结金额
                        </th>
						<th rowspan="2">
							所在区域
						</th>
						<th rowspan="2">
							类型
						</th>
						<th rowspan="2">
							开户机构
						</th>
						
						<th rowspan="2">
							开户时间
						</th>
						<th rowspan="2">
							状态
						</th>
						<th rowspan="2">
							介绍人
						</th>
						<th colspan="2" style="text-align:center">三方存管</th>
						<th rowspan="2">
							操作
						</th>
					</tr>
					<tr class="ui-widget-header ">
						<th >
							状态
						</th>
						<th >
							子账号
						</th>
					</tr>
				</thead>
				<tbody class="table_solid">
					<c:forEach items="${pageView.records}" var="memberbase">
						<tr>
							<td>
								<c:if test="${memberbase.category==\"1\"}">
							${memberbase.pName}
							</c:if>
								<c:if test="${memberbase.category==\"0\"}">
							${memberbase.eName}
							</c:if>
							</td>
							<td>
								${memberbase.user.username}
							</td>
							<td>
								${memberbase.memberLevel.levelname}
							</td>
							<td>
								<fmt:formatNumber value="${memberbase.user.userAccount.balance}"
									type="currency" currencySymbol=""></fmt:formatNumber>
								&nbsp;
							</td>
							<td>
                                <fmt:formatNumber value="${memberbase.user.userAccount.frozenAmount}"
                                    type="currency" currencySymbol=""></fmt:formatNumber>
                                &nbsp;
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
								<c:if test="${memberbase.state==\"1\"}">
							待审核
							</c:if>
								<c:if test="${memberbase.state==\"2\"}">
							正常
							</c:if>
								<c:if test="${memberbase.state==\"3\"}">
							未通过审核
							</c:if>
								<c:if test="${memberbase.state==\"4\"}">
									<span style="color: red">已停用</span>
								</c:if>
							<c:if test="${memberbase.state==\"5\"}">
                                    <span style="color: red">已注销</span>
                                </c:if>
							</td>
							<td>${memberbase.jingbanren}</td>
							<td>
								<c:if test="${memberbase.user.flag==\"0\"}">
									<span style="color:#4169E1;">未签约</span>
								</c:if>
								<c:if test="${memberbase.user.flag==\"1\"}">
									<span style="color:#e69700;">签约中</span>
								</c:if>
								<c:if test="${memberbase.user.flag==\"2\"}">
									<span style="color:green;">已签约</span>
								</c:if>
								<c:if test="${memberbase.user.flag==\"3\"}">
									<span style="color:#red;">已解约</span>
								</c:if>
							</td>
							<td>
								<c:if test="${memberbase.user.flag==\"0\"||memberbase.user.flag==''||memberbase.user.flag==null}">
									-
								</c:if>
								<c:if test="${memberbase.user.flag==\"1\"}">
									-
								</c:if>
								<c:if test="${memberbase.user.flag==\"2\"}">
									${memberbase.user.accountNo}
								</c:if>
								<c:if test="${memberbase.user.flag==\"3\"}">
									${memberbase.user.accountNo}
								</c:if>
							</td>
							<td>
								<button class="ui-state-default"
									onclick="toURL('/back/member/memberBaseAction!memberDetails?id=${memberbase.id}'); return false;"
									style="display:<c:out value="${menuMap['memberBase_details']}" />">
									详细
								</button>
								<span style="clear: both"></span>
								<button class="ui-state-default"
									onclick="toURL('/back/member/memberBaseAction!memberBankAccounts?id=${memberbase.id}'); return false;"
									style="display:<c:out value="${menuMap['memberBase_bankAccounts']}" />">
									结算信息
								</button>
								<span style="clear: both"></span>
								<c:if
									test="${memberbase.state==\"1\" || memberbase.state==\"3\" }">
									<button class="ui-state-default"
										onclick="toURL('/back/member/memberBaseAction!edit?id=${memberbase.id}'); return false;"
										style="display:<c:out value="${menuMap['memberBase_modify']}" />">
										修改
									</button>
								</c:if>
								<span style="clear: both"></span>
								<c:if test="${memberbase.state==\"2\"}">
									<button class="ui-state-default"
										onclick="disable('${memberbase.id}');return false;"
										style="display:<c:out value="${menuMap['memberBase_disable']}" />">
										停用
									</button>
								</c:if>
								<c:if test="${memberbase.state==\"4\"}">
									<button class="ui-state-default"
										onclick="enable('${memberbase.id}');return false;"
										style="display:<c:out value="${menuMap['memberBase_enable']}" />">
										启用
									</button>
								</c:if>
								
								<button class="ui-state-default" onclick="showInfo('${memberbase.id}');return false;" style="display:<c:out value="${menuMap['memberBase_showlog']}" />" >日志信息</button>  
							</td>
						</tr>
					</c:forEach>
				</tbody>
				<tbody>
					<tr>
						<td colspan="12">
							<jsp:include page="/common/page.jsp"></jsp:include>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
</body>