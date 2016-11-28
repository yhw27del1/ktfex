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

	});

	/**
	 *设置投资人选中的债权
	 */
	function changeZhaiQuanCode(zhaiQuanId, zhaiQuanCode, zqzrState,sellingSate,
			buyingPrice, haveNum, days, currentUserId, investUserId) {
		$("#zhaiQuanId").val("");
		$("#zhaiQuanCode").val("");
		$("#buyingPrice").val("");
		$("#otherPayment").val("");
		$("#income").val("");
		//只有状态为挂牌和复牌并且债权的买入或卖出天数大于5并且债权累计持有人小于200并且非自己的债权才能受让
		if (("1" == zqzrState || "3" == zqzrState || ((0 == days || 5 < days) && 200 < haveNum))
				&& (currentUserId != investUserId)) {
			$("#zhaiQuanId").val(zhaiQuanId);
			$("#zhaiQuanCode").val(zhaiQuanCode);
			$("#buyingPrice").val(forDight(buyingPrice, 0));
		}
	}

	/**
	 *根椐用戶出价计算收益
	 */
	function computeIncome() {
		hideErrorMsg();
		if ("" != $("#zhaiQuanCode").val()) {
			$.ajax( {
				url : "/back/zhaiquan/zhaiQuanInvestAction!jsmoney",
				data : {
					"zhaiQuanId" : $("#zhaiQuanId").val(),
					"money" : $("#buyingPrice").val()
				},
				type : "post",
				async : false,
				success : function(data, textStatus) {
					var fee = parseFloat(data.db1, 10);
					var taxes = parseFloat(data.db2, 10);
					$("#zqfwf").val(fee);
					$("#zqsf").val(taxes);
					$("#otherPayment").val(fee + taxes);
					$("#income").val(data.db3);
				}
			});
		} else {
			showErrorMsg();
		}
	}

	//对指定的债权下达受让指令
	function buyClaims(userId) {
		computeIncome();
		var balance = queryAccountBalance(userId);
		//alert("balance="+parseFloat(balance,10)+",pay="+(parseFloat($("#otherPayment").val(),10)+parseFloat($("#buyingPrice").val(),10)))
		//判断用户余额
		if (parseFloat(balance, 10) >= (parseFloat($("#otherPayment").val(), 10) + parseFloat(
				$("#buyingPrice").val(), 10))) {

			if ((0 >= $("#income").val() && confirm("你将保本或亏本，是否真要下达受让指令？"))
					|| (0 < $("#income").val())) {
				showModalDialog(
						'/back/zhaiquan/contractAction!preview_for_buy?ivr_id='
								+ $("#zhaiQuanId").val() + '&price='
								+ $("#buyingPrice").val()+'&zqfwf='+$("#zqfwf").val()+'&zqsf='+$("#zqsf").val(), '',
						'dialogWidth:620px;center:yes;help:no;resizable:no;status:no');
			}
		} else {
			//余额不足
			alert("您的账户余额不足！不能对此债权下达受让指令。");
		}
	}

	/**
	 *根据指定的用户查询其账户的余额
	 */
	function queryAccountBalance(userId) {
		var balance = 0.0;
		$.ajax( {
			"url" : "/back/zhaiquan/accountDealAction!queryAccountBalance",
			"data" : {
				"userId" : userId
			},
			"type" : "post",
			"async" : false,
			success : function(data, textStatus) {
				//alert(data);
			balance = data;
		},
		error : function(textStatus, xmlHttpRequest, errorThrow) {
			//alert("查询账户余额时发生了错误！不能查询您的账户余额。");
			throw new Exception("查询账户余额时发生了错误！不能查询您的账户余额。");
		}
		});
		return balance;
	}

	function showErrorMsg() {
		$("#errorMsg").show();
		$("#zhaiQuanCode").css("boder", "1px");
		$("#zhaiQuanCode").css("boder-color", "red");
	}

	function hideErrorMsg() {
		$("#errorMsg").hide();
		$("#zhaiQuanCode").css("border", "");
		$("#zhaiQuanCode").css("border-color", "");
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
	function show(url){
	    window.showModalDialog(url, null, "dialogWidth:800px;dialogHeight:auto;status:no;help:yes;resizable:no;");
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
</style>
<body class="ui-widget-header" style="font-size: 8pt; color: black">
	<div style="overflow: auto;">
		<input type='hidden' class='autoheight' value="auto" />
		<table width="1200px" height="600px;">
			<tr valign="top">
				<td>
					<div class="dataList ui-widget"
						style="width: 800px; height: 600px;">
						<div style="font-size: 14px;">
							<form action="/back/zhaiquan/zhaiQuanInvestAction!flist">
								<input type="hidden" name="page" value="1" />
								<input type='hidden' class='zqzrState' value="${zqzrState}" />
								<div style="margin: 10px;">
									<span style="color: black; font-size: 16px; font-weight: bold;">按条件查询</span>
								</div>
								<div style="margin: 10px;">
									债权代码&nbsp;
									<input style="width:120px;" type="text" value="${querykeyWord}" name="querykeyWord">
									开始日期&nbsp;
									<input style="width:100px;" type="text" name="startDate"
										value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd"/>"
										id="startDate" />
									结束日期&nbsp;
									<input style="width:100px;" type="text" name="endDate"
										value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd"/>"
										id="endDate" />
									状态
									<s:select name="zqzrState" list="paiList" listKey="string1"
										listValue="string2" headerKey="" headerValue="全部" />&nbsp;&nbsp;
									<input class="ui-state-default" style="cursor: pointer;" type="submit" value="查询">
								</div>
							</form>
						</div>
						<table align="center" class="" style="text-align: center;">
							<thead>
								<tr class="ui-widget-header ">
									<th align="center">
										债权代码
									</th>
									<th align="center">
										成本价(元)/期末值(元)
									</th>
									<th align="center">
										参考收益率(%)
									</th>
									<th align="center">
										到期日期
									</th>
									<th align="center">
										出让价格(元)
									</th>
									<th align="center">
										状态
									</th>
								</tr>
							</thead>
							<tbody class="table_solid">
								<c:forEach items="${pageView.records}" var="entry">
									<tr
										onclick="changeZhaiQuanCode('${entry.id}','${entry.zhaiQuanCode}','${entry.zqzrState}','${entry.sellingState}','${entry.qmz}','${entry.haveNum}','${entry.day}','${loginUserId}','${entry.investor.user.id}')">
										<td align="center"> 
										  <a href="javascript:void(0)" onclick="show('/back/zhaiquan/zhaiQuanInvestAction!detail?zhaiQuanId=${entry.investRecord.id}')">${entry.zhaiQuanCode}</a>
										</td>
										<td align="center">
											<fmt:formatNumber value='${entry.cbj}' type="currency"
												currencySymbol="" />
											/
											<fmt:formatNumber value='${entry.qmz}' type="currency"
												currencySymbol="" />
										</td>
										<td align="center">
											<c:choose>
												<c:when test="${ 0 >= entry.cbj}">-
                                            </c:when>
												<c:otherwise>
													<fmt:formatNumber value='${entry.syl}' type="currency"
														currencySymbol="" />
												</c:otherwise>
											</c:choose>
										</td>
										<td align="center">
											<fmt:formatDate value='${entry.lastDate}'
												pattern="yyyy-MM-dd" />
										</td>
										<td align="center">
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
										<td align="center">
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
											          转让成功
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
            	                                                                                     复牌
											</c:if>
												<c:if test="${entry.sellingState=='1'}">
											      出让中 
											</c:if>
												<c:if test="${entry.sellingState=='2'}">
											      求购中
											</c:if>
												<c:if test="${entry.sellingState=='3'}">  
											      转让成功
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
									<td colspan="9">
										<jsp:include page="/common/page.jsp"></jsp:include></td>
								</tr>
							</tbody>
						</table>
					</div>
				</td>
				<td width="400px;">
					<form>
									<table>
										<tr>
											<td colspan="2">
												<div>
													<span
														style="color: black; font-size: 16px; font-weight: bold;">我要理财</span>
												</div>
												<div>
													<p style="color:red;font-size:14px;line-height: 18px;">
														&nbsp;&nbsp;&nbsp;&nbsp;使用鼠标单击左边列表中的一个债权
														<br />
														以选中此债权，在“受让委托价”输入框
														<br />
														中输入您的出价，点击“计算收益”按钮
														<br />
														，可计算出所选债权的期末收益。点击“
														<br />
														买入”按钮可对选中债 权下达受让指令。
													</p>
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
													readonly="readonly" style="width: 120px;" />
												<label style="color: red; display: none;" id="errorMsg">
													请选择债权
												</label>
											</td>
										</tr>
										<tr>
											<td>
												受让委托价&nbsp;
											</td>
											<td>
												<input style="width: 100px;" type="text" name="buyingPrice"
													id="buyingPrice" onchange="checkNumber(this)"
													onkeyup="checkNumber(this)"/>
												元
											</td>
										</tr>
										<tr>
											<td>
												其它费用&nbsp;
											</td>
											<td>
												<input style="width: 100px;" type="text" name="otherPayment"
													id="otherPayment" readonly="readonly" />
												元
											</td>
										</tr>
										<tr>
											<td>
												期末收益
											</td>
											<td>
												<input style="width: 100px;" type="text" name="income"
													id="income" readonly="readonly" />
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
												<input class="ui-state-default" style="cursor: pointer;" type="button" value="计算收益"
													onclick="computeIncome()" />
											</td>
											<td colspan="1">
												&nbsp;&nbsp;&nbsp;&nbsp;
												<input class="ui-state-default" style="cursor: pointer;" type="button" value="买入"
													onclick="buyClaims('${loginUserId}');">
											</td>
										</tr>
									</table>
								</form>
				</td>
			</tr>
		</table>
	</div>
</body>
