<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<script type="text/javascript" src="/Static/js/autoheight.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-selected.js"></script>
		<script type="text/javascript"
			src="/Static/js/jquery.tablemyui.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
        <script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
		<style type="text/css">
			.selected1 {
			    background-color: #f0f0f0 !important;
			}
        </style>
		<script type="text/javascript">
	$(function() {
		 $(".table_solid").tableStyleUI(); 
		   var b = '${bank}';
		   $("option[value='"+b+"']",$("#bank")).attr("selected",true);
		   var ss = '${signState}';
		   $("option[value='"+ss+"']",$("#signState")).attr("selected",true);

		   $("#startDate").datepicker({
		        numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
		    $("#endDate").datepicker({
		        numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
           
		            
		    $("#ui-datepicker-div").css({'display':'none'});
		  
		    $("#checkedAll").click(function() {
	            if ($(this).attr("checked") == "checked") { // 全选
	                    $("input[name='chkGoods']").each(function(index,element) {
	                        $(this).attr("checked", true);
	                        $('tbody > tr', $('#table_solid')).addClass('selected1');
	                    });
	                } else { // 取消全选
	                    $("input[name='chkGoods']").each(function(index) {
	                        $(this).attr("checked", false);
	                        $('tbody > tr', $('#table_solid')).removeClass('selected1');
	                    });
	                }
	            });

		    //选择行
            //为表格行添加选择事件处理
            $('tbody > tr', $('#table_solid')).click(function(){
                 var hasselected = $(this).hasClass("selected1");
                $(this)[hasselected?"removeClass":"addClass"]("selected1").find(":checkbox").attr("checked",!hasselected);
            }).hover(       //注意这里的链式调用
                function(){
                    $(this).addClass('mouseOver');
                },
                function(){
                    $(this).removeClass('mouseOver');
                }
            );

		    $("#addToUrl").click(function() {
		    	var categories = $('input[name="chkGoods"]:checked').map(function() {  
                    return $(this).val();  
                 }).get();  
                var tarUrl = '${tarUrl}';
                if(categories == null || categories ==""){
                    alert("请至少选择一项！");
                    return false;
                }
                alert (categories);
                if(tarUrl == null || tarUrl ==""){
                    alert("目标action地址为空！ ");
                    return false;
                }
                window.location.href = tarUrl+"?usernames="+categories;
		    });
	});
</script>

</head>
	<body>
		<form id="form1" action=""
			method="post">
			<input type='hidden' class='autoheight' value="auto" />
            <input type="hidden" name="id" value="${id}" id="id"/>  
			
			<div id="myToolBar" style="height: 70px;"
				class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
				<div style="float: left;">
					<div style="margin: 5px;">
						<input type="hidden" name="page" value="1" />
						会员/用户：
						<input class="input_box" style="width: 120px;" type="text"
							name="keyword" value="${keyword}" />
						&nbsp;状态：
						<s:select cssClass="input_select" name="memberState"
							list="memberStates" listKey="key" listValue="value"></s:select>
						&nbsp;类型：
						<s:select cssClass="input_select" name="memberTypeId"
							list="memberTypes" listKey="id" listValue="name"
							cssStyle="padding:1px;width:80px;" headerKey="0" headerValue="全部"
							onchange="memberTypeChanged(this)"></s:select>
						&nbsp;所在区域：
						<s:select cssClass="input_select" list="regions_province"
							id="provinceCode" value="provinceCode" name="provinceCode"
							listKey="areacode" listValue="areaname_s"
							cssStyle="padding:1px;width:80px;" headerKey="0" headerValue="全部"></s:select>
						<s:select cssClass="input_select" list="regions_city"
							id="cityCode" value="cityCode" name="cityCode" listKey="areacode"
							listValue="areaname_s" cssStyle="padding:1px;width:80px;"
							headerKey="0" headerValue="全部"></s:select>
					</div>
					<div style="margin: 5px;">
						<c:if test="${topOrg==true}">开户机构 ： 
                    <input class="input_box" style="width: 120px;"
								type="text" name="orgName" value="${orgName}" />
						</c:if>
						&nbsp;开户时间： 从
						<input type="text" name="startDate" value="<fmt:formatDate value='${startDate}' type='date'/>"  id="startDate"/>
						&nbsp;到&nbsp;<input type="text" name="endDate" value="<fmt:formatDate value='${endDate}' type='date' />"  id="endDate"/>
						签约行&nbsp;
						<select name="bank" id="bank">
							<option value="全部">
								全部
							</option>
							<option value="华夏银行">
								华夏银行
							</option>
							<option value="招商银行">
								招商银行
							</option>
						</select>
						签约状态&nbsp;
						<select name="signState" id="signState">
							<option value="全部">
								全部
							</option>
							<option value="0">
								未签约
							</option>
							<option value="1">
								签约中
							</option>
							<option value="2">
								已签约
							</option>
							<option value="3">
								已解约
							</option>
						</select>
						<input id="tarUrl" type="text" name="tarUrl" value="${tarUrl}"/>
						<input id="btnName" type="text" name="btnName" value="${btnName}"/>
						<input type="submit" class="ui-state-default" value="查找" />
						<c:if test="${!empty tarUrl && !empty btnName}">
                            <input type="button" class="ui-state-default" id="addToUrl" value="${btnName}" >
                        </c:if>
                        
					</div>
				</div>

			</div>

			<div class="dataList ui-widget">
				<table class="ui-widget ui-widget-content" id="table_solid">
					<thead>
						<tr class="ui-widget-header">
							<th rowspan="2">
								<label for="qx">
									<input type="checkbox" id="checkedAll" />
									全选
								</label>
							</th>
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
								开户银行
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
							<th colspan="2" style="text-align: center">
								三方存管
							</th>
							<th rowspan="2">
								操作
							</th>
						</tr>
						<tr class="ui-widget-header ">
							<th>
								状态
							</th>
							<th>
								子账号
							</th>
						</tr>
					</thead>
					<tbody class="table_solid">
						<c:forEach items="${pageView.records}" var="memberbase">
							<tr >
								<td height="20">
									<input name="chkGoods" type="checkbox"
										value="${memberbase.user.username }" />
								</td>
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
									<fmt:formatNumber
										value="${memberbase.user.userAccount.balance}" type="currency"
										currencySymbol=""></fmt:formatNumber>
									&nbsp;
								</td>
								<td>
									<fmt:formatNumber
										value="${memberbase.user.userAccount.frozenAmount}"
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
									${memberbase.banklib.caption}
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
								</td>
								<td>
									${memberbase.jingbanren}
								</td>
								<td>
									<c:if test="${memberbase.user.flag==\"0\"}">
										<span style="color: #4169E1;">未签约</span>
									</c:if>
									<c:if test="${memberbase.user.flag==\"1\"}">
										<span style="color: #e69700;">签约中</span>
									</c:if>
									<c:if test="${memberbase.user.flag==\"2\"}">
										<span style="color: green;">已签约</span>
									</c:if>
									<c:if test="${memberbase.user.flag==\"3\"}">
										<span style="color: #red;">已解约</span>
									</c:if>
								</td>
								<td>

								</td>
								<td>

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