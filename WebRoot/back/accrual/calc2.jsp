<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<c:set var="now" value="<%=new java.util.Date()%>" />
<html>
	<head>
		<title>会员利息计算</title>
		<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css" />
		<script type="text/javascript" src="/Static/js/lhgdialog/lhgdialog/lhgdialog.min.js"></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<script>
		$(function(){
			$("#sum_text_input").val($("#sum_text").text());
			$("#lx_text_input").val($("#lx_text").text());
			$("#count_text_input").val($("#count_text").text());
			
			$("input.lxlx").blur(function(){
				//input修改完利息值后，主区间中的利息汇总要改变
				var lx = 0;
				$.each($("input.lxlx"),function(k,v){
					lx  += Number($(v).val());
				});
				var aim = $("input.yflx[username='"+$(this).attr('username')+"']");
				var value = $("input.lxlx[username='"+$(this).attr('username')+"']");
				var sub_lx = 0;
				$.each(value,function(k,v){
					sub_lx  += Number($(v).val());
				});
				aim.val(sub_lx.toFixed(2));
				$("#lx_text").text(lx.toFixed(2));
				$("#lx_text_input").val(lx.toFixed(2));
			});
		});
		function doprint(){
			$("#toolbar").hide();
			$(".noClassPint").hide();
			$("input.lxlx").addClass("noborder");
			print();  
			$("#toolbar").show();
			$(".noClassPint").show();
			$("input.lxlx").removeClass("noborder");
		}
		function dosubmit(){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要预发利息吗？",
				ok:function(){
					$("body").showLoading();
					$("#form").submit();
				},
				cancelVal:'关闭',
				cancel:true
			});
		}
		function doexport(){
			$.dialog({title:'提示信息',lock:true,min:false,max:false,width:'400px',content:"你确定要导出excel吗？",
				ok:function(){
					var m = $("#memo").val();
					var s = $("#start").val();
					var e = $("#end").val();
					var i = $("#interval").val();
					var ut = $("#userType").val();
					var st = $("#signType").val();
					var s1 = $("#start1").val();
					var e1 = $("#end1").val();
					var r1 = $("#rate1").val();
					var s2 = $("#start2").val();
					var e2 = $("#end2").val();
					var r2 = $("#rate2").val();
					var cl = $("#channel").val();
					window.location.href = "/back/accrual/accrualAction!calc_ex?time="+new Date().getTime()+"&main.memo="+encodeURI(m)+"&main.main_start="+s+"&main.main_end="+e+"&main.main_rate=0"+"&main.interval="+i+"&userType="+ut+"&signType="+st+"&main.sub_start_1="+s1+"&main.sub_end_1="+e1+"&main.sub_rate_1="+r1+"&main.sub_start_2="+s2+"&main.sub_end_2="+e2+"&main.sub_rate_2="+r2+"&channel="+cl;
				},
				cancelVal:'关闭',
				cancel:true
			});
		}
		</script>
<style>
body {
	font-size: 13px;
	padding: 5px;
	margin: 0;
}

.l1 {
	text-align: center;
}

.space {
	padding-left: 50px;
}

.title {
	font-size: 13.5px;
	font-weight: bold
}

.padding {
	padding: 6px;
}

.tr_on_selected {
	background: #f7f7f7;
	font-weight: bold;
}

#toolbar{
	position: fixed;
	top: 0px;
	left: 0px;
	width: 100%;
	background-color: #F2F2F2;
	border-bottom: 1px solid #DCDCDC;
	padding-bottom:2px;
	height:30px;
	line-height:30px;
	z-index:90;
}

.float_div_1{
	border:0;float:right;padding:0 10px 0 10px;
}
.noborder{
	border:none;
	background:none;
}
.lxlx{
	size:10px;
}
</style>

	</head>
	<body>
		<div style="" id="toolbar">
			<div class="float_div_1">
			<a href="javascript:;" onclick="doprint();">打印列表</a>
			<a href="javascript:;" onclick="window.location.reload();">刷新</a>
			<!-- <a href="javascript:;" onclick="dosubmit();">预发利息</a> -->
			<a href="javascript:;" onclick="doexport();">导出excel</a>
			</div>
		</div>
		<div style="margin: 50px auto; font-weight: bold;">
			<p style="text-align: center; margin: 20px auto;">
			<div style="float: right">
				<img width="80" height="80" style="position: relative; right: 30px;" src="/Static/images/logo.png">
			</div>
			<span style="text-align: center;"><h2 align="center">
					昆投互联网金融交易
				</h2>
				<h1 align="center">
					会员利息计算
				</h1>
			</span>
			</p>
			<p style="line-height: 60px;">
			<div style="float: left; margin: 0px;">
				会计日期：
				<span id="create_time"><fmt:formatDate value="${now}" pattern="yyyy/MM/dd"/></span>
			</div>
			<br />
			</p>
			<p style="line-height: 60px;">
			<form id="form" action="/back/accrual/accrualAction!add2" method="post">
			<div>
				<span class="title">主期间：</span><span class="value"><fmt:formatDate value="${main.main_start}" pattern="yyyy-MM-dd"/>至<fmt:formatDate value="${main.main_end}" pattern="yyyy-MM-dd"/></span>
				<span class="space"></span>
				<span class="title">年利率：</span><span class="value">${main.main_rate}% </span>
				<span class="space"></span>
				<span class="title">年利率数：</span><span class="value">${main.interval} </span>
				<span class="space"></span>
				<span class="title">余额汇总：</span><span class="value" id="sum_text">${sums}</span>
				<span class="space"></span>
				<span class="title">利息汇总：</span><span class="value" id="lx_text">${lxs}</span>
				<span class="space"></span>
				<span class="title">人数汇总：</span><span class="value" id="count_text">${counts}</span>
				
				<input id="memo" name="vo2.memo" value="${main.memo}" style="display:none;" />
				<input id="userType" value="${userType}" style="display:none;" />
				<input id="signType" value="${signType}" style="display:none;" />
				<input id="channel" value="${channel}" style="display:none;" />
				<input id="interval" name="vo2.interval" value="${main.interval}" style="display:none;" />
				<input name="vo2.sum_all" id="sum_text_input" style="display:none;" />
				<input name="vo2.sum_lx" id="lx_text_input" style="display:none;" />
				<input name="vo2.count" id="count_text_input" style="display:none;" />
				<input id="start" name="vo2.start" value="<fmt:formatDate value='${main.main_start}' pattern='yyyy-MM-dd'/>" style="display:none;" />
				<input id="end" name="vo2.end" value="<fmt:formatDate value='${main.main_end}' pattern='yyyy-MM-dd'/>" style="display:none;" />
				
				<input id="start1" name="vo2.start1" value="<fmt:formatDate value='${main.sub_start_1}' pattern='yyyy-MM-dd'/>" style="display:none;" />
				<input id="end1" name="vo2.end1" value="<fmt:formatDate value='${main.sub_end_1}' pattern='yyyy-MM-dd'/>" style="display:none;" />
				<input id="rate1" name="vo2.rate1" value="${main.sub_rate_1}" style="display:none;" />
				
				<input id="start2" name="vo2.start2" value="<fmt:formatDate value='${main.sub_start_2}' pattern='yyyy-MM-dd'/>" style="display:none;" />
				<input id="end2" name="vo2.end2" value="<fmt:formatDate value='${main.sub_end_2}' pattern='yyyy-MM-dd'/>" style="display:none;" />
				<input id="rate2" name="vo2.rate2" value="${main.sub_rate_2}" style="display:none;" />
			</div>
			<div>
				<span class="title">子期间1：</span><span class="value"><fmt:formatDate value="${main.sub_start_1}" pattern="yyyy-MM-dd"/>至<fmt:formatDate value="${main.sub_end_1}" pattern="yyyy-MM-dd"/></span>
				<span class="space"></span>
				<span class="title">子期间2：</span><span class="value"><fmt:formatDate value="${main.sub_start_2}" pattern="yyyy-MM-dd"/>至<fmt:formatDate value="${main.sub_end_2}" pattern="yyyy-MM-dd"/></span>
			</div>
			<table style="font-size: 13px; font-weight: bold">
				<thead>
					<tr>
						<th rowspan="3">
							序号
						</th>
						<th rowspan="3">
							会员名称
						</th>
						<th rowspan="3">
							会员编号
						</th>
						<th colspan="3" style="text-align:center;">子期间1</th>
						<th colspan="3" style="text-align:center;">子期间2</th>
						<th rowspan="3">
							积数
						</th>
						<th rowspan="3">
							应发利息<span style="cursor:hand;" title="应发利息 = (年利率/365) X 总额">&nbsp;?&nbsp;</span>
						</th>
					</tr>
					<tr>
						<th style="text-align: right;">
							积数1
						</th>
						<th style="text-align: right;">
							年利率1
						</th>
						<th style="text-align: right;">
							利息1
						</th>
						<th style="text-align: right;">
							积数2
						</th>
						<th style="text-align: right;">
							年利率2
						</th>
						<th style="text-align: right;">
							利息2
						</th>
					</tr>
				</thead>
				<c:forEach items="${detail}" var="dt" varStatus="mm">
				<tbody>
					<tr>
						<td>
							${mm.count}
						</td>
						<td>
							<script>document.write(name("${dt.realname}"));</script>
						</td>
						<td>
							${dt.username}
							<!-- <input name="vo2.username" value="${dt.username}" style="display:none;" /> -->
						</td>
						
						<!-- 子期间1 -->
						<td>
							${dt.sum1}
							<!-- <input name="vo2.sum1" value="${dt.sum1}" style="display:none;" /> -->
						</td>
						<td>
							${dt.r1}%
						</td>
						<td>
							<!-- <input name="vo2.lx1" username="${dt.username}" class="lxlx" size="10" value="${dt.lx1}" /> -->
							${dt.lx1}
						</td>
						<!-- 子期间1 -->
						
						<!-- 子期间2 -->
						<td>
							${dt.sum2}
							<!-- <input name="vo2.sum2" value="${dt.sum2}" style="display:none;" /> -->
							<!-- <input name="vo2.sum3" value="0" style="display:none;" /> -->
						</td>
						<td>
							${dt.r2}%
						</td>
						<td>
							${dt.lx2}
							<!-- <input name="vo2.lx2" username="${dt.username}" class="lxlx" size="10" value="${dt.lx2}" /> -->
							<!-- <input name="vo2.lx3" username="${dt.username}" class="lxlx" size="10" value="0" style="display:none;" /> -->
						</td>
						<!-- 子期间2 -->
						
						<td>
							${dt.sum}
							<!-- <input name="vo2.sum" value="${dt.sum}" style="display:none;" /> -->
						</td>
						<td>
							<!-- <input username="${dt.username}" name="vo2.lx" class="noborder yflx" readonly="readonly" size="10" value="${dt.lx}" /> -->
							${dt.lx}
						</td>
					</tr>
				</tbody>
				</c:forEach>
			</table>
			</form>
			 <div style="float:right">
			 	<div style="float:left;width:140px">操作员:${session.LOGININFO.realname}</div><div style="margin-left:20px;float:left;width:140px">复核员:</div><div style="float:left;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			 </div>
		</div>
	</body>
</html>