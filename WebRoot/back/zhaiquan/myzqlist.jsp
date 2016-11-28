<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript"
	src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript">
	$(function() {
		$("#startDate").datepicker( {
			numberOfMonths : 2,
			dateFormat : "yy-mm-dd"
		});
		$("#endDate").datepicker( {
			numberOfMonths : 2,
			dateFormat : "yy-mm-dd"
		});
		$("#ui-datepicker-div").css( {
			'display' : 'none'
		});
		$(".table_solid").tableStyleUI();
		$("#backTo").click(function() {
			window.location.href = "/back/investBaseAction!recordListTotal";
			return false;
		});
		$('.agreement')
				.click(
						function() {
							window
									.showModalDialog(
											"/back/investBaseAction!agreement_ui2?invest_record_id="
													+ $(this).attr("id"), null,
											"dialogWidth:1000px;dialogHeight:auto;status:no;help:yes;resizable:no;");
							$("input.ui-state-default").trigger('click');
						});
	});

	/**
	 *设置投资人选中的债权
	 */
	function changeZhaiQuanCode(zhaiQuanId, zhaiQuanCode, zqzrState,sellingSate,
			haveNum, days) {
		//alert("zqzrState="+zqzrState+",days="+days+",haveNum="+haveNum);
		var price=$("#invest"+zhaiQuanId).html();
		$("#zhaiQuanId").val("");
		$("#zhaiQuanCode").val("");
		$("#price").val("");
		$("#otherPayment").val("");
		$("#actualAmount").val("");
		if ((("1" == zqzrState || "3" == zqzrState) && 200 > haveNum
				&& (5 < days || 0 == days))&& sellingSate !="3") {
			$("#zhaiQuanId").val(zhaiQuanId);
			$("#zhaiQuanCode").val(zhaiQuanCode);
			$("#price").val(forDight(price, 0));
		}		
		$.ajax( {
				url : "/back/zhaiquan/buyingAction!newBuyList",
				data : {
					"recordSum":5,
					"id":zhaiQuanId
				},
				type : "post",
				async:false,
				success : function(data, textStatus) {
					//alert(data);
					$("#newBuyerList").html("");
					if(0==data.length){
					   $("#newBuyerList").append("<tr align=\"center\"><td style=\"color:red\" colspan=\"2\">暂无数据</td></tr>");
					}else{
					for(var i=0;i<data.length;i++){
					$("#newBuyerList").append("<tr align=\"center\"><td>"+data[i].buyingPrice+"</td><td>"+data[i].createDate+"</td></tr>");}
				}
				}
			});
	}

	/**
	 *根椐用戶出价计算收益
	 */
	function computeActualAmount() {
		hideErrorMsg();
		if ("" != $("#zhaiQuanCode").val()) {
			$.ajax( {
				url : "/back/zhaiquan/zhaiQuanInvestAction!jsmoney",
				data : {
					"zhaiQuanId" : $("#zhaiQuanId").val(),
					"money" : $("#price").val()
				},
				type : "post",
				async:false,
				success : function(data, textStatus) {
					var fee = parseFloat(data.db1, 10);
					var taxes = parseFloat(data.db2, 10);
					$("#zqfwf").val(fee);
					$("#zqsf").val(taxes);
					$("#otherPayment").val(fee + taxes);
					$("#actualAmount").val(
							parseFloat($("#price").val(), 10)
									- (parseFloat(fee) + parseFloat(taxes)));
				}
			});
		} else {
			showErrorMsg();
		}
	}

	//对指定的债权下达出让指令
	function sellClaims() {
	   computeActualAmount();
		if ("" != $("#actualAmount").val()) {
			if ((0 >= $("#actualAmount").val() && confirm("您将亏本，是否真要下达出让指令？"))
					|| 0 < $("#actualAmount").val()) {
					showModalDialog('/back/zhaiquan/contractAction!preview_for_sell?ivr_id='+$("#zhaiQuanId").val()+'&price='+$("#price").val()+'&zqfwf='+$("#zqfwf").val()+'&zqsf='+$("#zqsf").val(),'','dialogWidth:620px;center:yes;help:no;resizable:no;status:no'); 
			}
		} else {
			alert("请先计算应得金额！");
		}
	}
    
	function showErrorMsg() {
		$("#errorMsg").show();
		$("#zhaiQuanCode").addClass("empty_border");
	}

	function hideErrorMsg() {
		$("#errorMsg").hide();
		$("#zhaiQuanCode").attr("border", "");
		$("#zhaiQuanCode").attr("border-color", "");
	}

	function checkNumber(obj) {
		obj.value = obj.value.replace(/[^(\d)]/g, '');
	}

	/**转载自http://www.geekso.com
	 *四舍五入到小数点后指定的位数
	 */
	function forDight(Dight, How) {
		Dight = Math.round(Dight * Math.pow(10, How)) / Math.pow(10, How);
		return Dight;
	}
	
	function getBuyPrice(investRecordId){
	   $.ajax({
	     url:"/back/zhaiquan/contractAction!getBuyPrice",
	     data:{"investRecordId":investRecordId},
	     type:"post",
	     success:function(data,textStatus){
	       $("#invest"+investRecordId).html(data);
	     }
	   }); 
	}
</script>
<style type="text/css">
.s_table td {
	background-color: #e3e3e3
}

.agreement {
	cursor: pointer;
}

.agreement:HOVER {
	text-decoration: underline;
}

.empty_border {
	border: 1px;
	border-color: red;
}
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black;overflow: auto;">
	<input type='hidden' class='autoheight' value="auto" />
	<table width="1200px;">
		<tr valign="top">
			<td width="800px;" height="600px;">
				<div class="dataList ui-widget"
					style="float: left; width: 800px; height: 600px;">
					<div style="font-size: 14px;">
					<form action="/back/zhaiquan/zhaiQuanInvestAction!myzqlist">
								<input type="hidden" name="page" value="1" />
								<input type='hidden' class='zqzrState' value="${zqzrState}" />
											<div style="margin: 10px;"><span
												style="color: black; font-size: 16px; font-weight: bold;">按条件查询</span></div>
											<div style="margin: 10px;">债权代码&nbsp;
											<input type="text" value="${querykeyWord}"
												name="querykeyWord" style="width:120px;" />&nbsp;&nbsp;
											开始日期&nbsp;
											<input type="text" name="startDate"
												value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>"
												id="startDate" style="width:100px;"/>&nbsp;&nbsp;
											结束日期&nbsp;
											<input type="text" name="endDate"
												value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>"
												id="endDate" style="width:100px;"/>&nbsp;&nbsp;
											状态
											<s:select name="zqzrState" list="paiList" listKey="string1"
												listValue="string2" headerKey="" headerValue="全部" />&nbsp;&nbsp;
											<input class="ui-state-default" style="cursor: pointer;" type="submit" value="查询"></div>
							</form></div>
					<table width="100%" style="font-size: 14px;"
						class="ui-widget ui-widget-content">
						<thead>
							<tr class="ui-widget-header ">
								<th>
									债权代码
								</th>
								<th>
									购入价(元)
								</th>
								<th>
									年利率(%)
								</th>
								<th>
									到期日期
								</th>
								<th>
									下一还款日
								</th>
								<th>
									出让价格
								</th>
								<th>
									状态
								</th>
							</tr>
						</thead>
						<tbody class="table_solid">
							<c:forEach items="${pageView.records}" var="entry">
								<tr
									onclick="changeZhaiQuanCode('${entry.id}','${entry.zhaiQuanCode}','${entry.zqzrState}','${entry.sellingState}','${entry.haveNum}','${entry.day}')">
									<td>
										<a href="/back/zhaiquan/zhaiQuanInvestAction!detail?zhaiQuanId=${entry.id}">${entry.zhaiQuanCode}</a>
									</td>
									<td>
									    <script type="text/javascript">
									    getBuyPrice('${entry.id}');
									    </script>
										<span id="invest${entry.id}"><fmt:formatNumber value='${entry.investAmount}'
											type="currency" currencySymbol="" /></span>
									</td>
									<td>
                                        ${entry.financingBase.rate}
									</td>
									<td>
										<fmt:formatDate value="${entry.lastDate}" type="date" />
									</td>
									<td>
										<fmt:formatDate value="${entry.xyhkr}" type="date" />
									</td>
									<td>
										<c:choose>
                                            <c:when test="${0>=entry.crpice}">-
                                            </c:when>
                                            <c:when test="${entry.zqzrState=='1' && entry.sellingState=='0'}">-                                            
                                            </c:when>                                            
                                            <c:otherwise>
                                            <fmt:formatNumber value='${entry.crpice}' type="currency"
											currencySymbol="" />                                            
                                            </c:otherwise>
                                        </c:choose>
									</td>
									<td>
										<c:if test="${entry.zqzrState=='0'}">
											<span style="color: #4169E1;">待挂牌</span>
										</c:if>
										<c:if test="${entry.zqzrState=='1'}">
											<c:if test="${entry.sellingState=='0'}"> 
            	                                                                                     挂牌
											</c:if>
											<c:if test="${entry.sellingState=='1'}">
											      出让中 
											</c:if>
											<c:if test="${entry.sellingState=='2'}">
											      求购中
											</c:if>
											<c:if test="${entry.sellingState=='3'}">  
											    交易成功
											</c:if>
											<c:if test="${entry.sellingState=='4'}"> 
											      出让中
											</c:if>
										</c:if>
										<c:if test="${entry.zqzrState=='2'}">
											<span style="color: red;">停牌 </span>
										</c:if>
										<c:if test="${entry.zqzrState=='3'}">
											<c:if test="${entry.sellingState=='0'}"> 
            	                                                                                挂牌
											</c:if>
											<c:if test="${entry.sellingState=='1'}">
											      出让中 
											</c:if>
											<c:if test="${entry.sellingState=='2'}">
											      求购中
											</c:if>
											<c:if test="${entry.sellingState=='3'}">  
											      交易成功
											</c:if>
											<c:if test="${entry.sellingState=='4'}"> 
											      出让中
											</c:if>
										</c:if>
										<c:if test="${entry.zqzrState=='4'}">
											<span style="color: green;">摘牌</span>
										</c:if>
									</td>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="11">
									<jsp:include page="/common/page.jsp"></jsp:include></td>
							</tr>
						</tbody>
					</table>
				</div>
			</td>
			<td width="400px;">
				<table>
					<tr>
						<td>
							<form>
								<table>
									<tr>
										<td colspan="2">
											<div><span
												style="color: black; font-size: 16px; font-weight: bold;">我要理财</span></div>
											<div><p style="color:red;font-size:14px;line-height: 18px;">
									             &nbsp;&nbsp;&nbsp;&nbsp;使用鼠标单击左边列表中的一个债权<br/>
									                                   以选中此债权，在“出让委托价”输入框<br/>
									                                   中输入您的叫价，点击“计算应得金额”<br/>
									                                   按钮，可计算出所选债权的应得金额。点<br/>
									                                   击“卖出”可对选中债权下达出让指令。</p>							
											</div>
										</td>
									</tr>
									<tr>
										<td>
											已选债权&nbsp;
										</td>
										<td>
											<input type="hidden" name="zhaiQuanId" id="zhaiQuanId" />
											<input type="hidden" name="zqfwf" id="zqfwf" />
											<input type="hidden" name="zqsf" id="zqsf" />
											<input type="text" name="zhaiQuanCode" id="zhaiQuanCode"
												readonly="readonly"
												style="width: 120px;" />
											<label style="color: red; display: none;" id="errorMsg">
												请选择债权
											</label>
										</td>
									</tr>
									<tr>
										<td>
											出让委托价&nbsp;
										</td>
										<td>
											<input style="width: 100px;" type="text" name="price" id="price"
												onchange="checkNumber(this)" onkeyup="checkNumber(this)" />
											元
										</td>
									</tr>
									<tr>
										<td>
											其它费用&nbsp;
										</td>
										<td>
											<input style="width: 100px;" type="text" name="otherPayment" id="otherPayment"
												readonly="readonly" />
											元
										</td>
									</tr>
									<tr>
										<td>
											应得金额
										</td>
										<td>
											<input style="width: 100px;" type="text" name="actualAmount" id="actualAmount"
												readonly="readonly" />
											元
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<span style="color: red;">其它费用为服务费与代扣税费合计</span>
										</td>
									</tr>
									<tr>
										<td colspan="1">
											<input class="ui-state-default" style="cursor: pointer;" type="button" value="计算应得金额"
												onclick="computeActualAmount()">
										</td>
										<td colspan="1">
											&nbsp;&nbsp;&nbsp;&nbsp;
											<input class="ui-state-default" style="cursor: pointer;" type="button" value="卖出"
												onclick="sellClaims()" />
										</td>
									</tr>
								</table>
							</form>
						</td>
					</tr>
					<tr>
						<td>
						    <div style="text-align: left;font-weight: bold;">前五名有意向购买者出价</div>
							<table style="font-size: 14px;" width="260px"
								class="ui-widget ui-widget-content">
								<thead>
									<tr align="center" class="ui-widget-header">
									    <th>
											出价(元)
										</th>
										<th>
											受让时间
										</th>
									</tr>
								</thead>
								<tbody class="table_solid" id="newBuyerList"></tbody>
							</table>
				</table>
			</td>
		</tr>
	</table>
</body>
