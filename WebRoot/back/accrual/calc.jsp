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
			var sum_ = new Number($("#sum").val());
			$("#sum_text").text(sum_.toFixed(2));
			var lx_ = new Number($("#lx").val());
			$("#lx_text").text(lx_.toFixed(2));
			$("#count_text").text($("#count").val());
			
			$("#sum_text_input").val($("#sum").val());
			$("#lx_text_input").val($("#lx").val());
			$("#count_text_input").val($("#count").val());
			
			$("input.lxlx").blur(function(){
				//input修改完利息值后，主区间中的利息汇总要改变
				var lx = 0;
				$.each($("input.lxlx"),function(k,v){
					lx  += Number($(v).val());
				});
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
					var r = $("#rate").val();
					var i = $("#interval").val();
					var ut = $("#userType").val();
					var st = $("#signType").val();
					var cl = $("#channel").val();
					window.location.href = "/back/accrual/accrualAction!calc_ex?time="+new Date().getTime()+"&main.memo="+encodeURI(m)+"&main.main_start="+s+"&main.main_end="+e+"&main.main_rate="+r+"&main.interval="+i+"&userType="+ut+"&signType="+st+"&channel="+cl;
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
			<form id="form" action="/back/accrual/accrualAction!add" method="post">
			<div>
				<span class="title">主期间：</span><span class="value"><fmt:formatDate value="${main.main_start}" pattern="yyyy-MM-dd"/>至<fmt:formatDate value="${main.main_end}" pattern="yyyy-MM-dd"/></span>
				<span class="space"></span>
				<span class="title">年利率：</span><span class="value">${main.main_rate}% </span>
				<span class="space"></span>
				<span class="title">年利率数：</span><span class="value">${main.interval} </span>
				<span class="space"></span>
				<span class="title">余额汇总：</span><span class="value" id="sum_text"></span>
				<span class="space"></span>
				<span class="title">利息汇总：</span><span class="value" id="lx_text"></span>
				<span class="space"></span>
				<span class="title">人数汇总：</span><span class="value" id="count_text"></span>
				
				<input id="memo" name="vo.memo" value="${main.memo}" style="display:none;" />
				<input id="userType" value="${userType}" style="display:none;" />
				<input id="signType" value="${signType}" style="display:none;" />
				<input id="channel" value="${channel}" style="display:none;" />
				<input id="interval" name="vo.interval" value="${main.interval}" style="display:none;" />
				<input name="vo.sum_all" id="sum_text_input" style="display:none;" />
				<input name="vo.sum_lx" id="lx_text_input" style="display:none;" />
				<input name="vo.count" id="count_text_input" style="display:none;" />
				<input id="rate" name="vo.rate" value="${main.main_rate}" style="display:none;" />
				<input id="start" name="vo.start" value="<fmt:formatDate value='${main.main_start}' pattern='yyyy-MM-dd'/>" style="display:none;" />
				<input id="end" name="vo.end" value="<fmt:formatDate value='${main.main_end}' pattern='yyyy-MM-dd'/>" style="display:none;" />
			</div>
			<table style="font-size: 13px; font-weight: bold">
				<thead>
					<tr>
						<th>
							序号
						</th>
						<th>
							会员名称
						</th>
						<th>
							会员编号
						</th>
						<th>
							积数
						</th>
						<th>
							年利率
						</th>
						<th>
							应发利息<span style="cursor:hand;" title="应发利息 = (年利率/365) X 总额">&nbsp;?&nbsp;</span>
						</th>
					</tr>
				</thead>
				<c:set var="sum" value="0"></c:set>
				<c:set var="lx" value="0"></c:set>
				<c:set var="count" value="0"></c:set>
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
							<input name="vo.username" value="${dt.username}" style="display:none;" />
						</td>
						<td>
							${dt.sum}
							<input name="vo.sum" value="${dt.sum}" style="display:none;" />
						</td>
						<td>
							${main.main_rate}%
						</td>
						<td>
							<!-- <input name="vo.lx" class="lxlx yflx" size="10" value="${dt.lx}" /> -->
							${dt.lx}
							<c:set var="lx" value="${lx+dt.lx}"></c:set>
							<c:set var="sum" value="${sum+dt.sum}"></c:set>
							<c:set var="count" value="${mm.count}"></c:set>
						</td>
					</tr>
				</tbody>
				</c:forEach>
			</table>
			</form>
			<input type="text" id="lx" value="${lx}" style="display: none;" />
			<input type="text" id="sum" value="${sum}" style="display: none;" />
			<input type="text" id="count" value="${count}" style="display: none;" />
			 <div style="float:right">
			 	<div style="float:left;width:140px">操作员:${session.LOGININFO.realname}</div><div style="margin-left:20px;float:left;width:140px">复核员:</div><div style="float:left;">打印时间:<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			 </div>
		</div>
	</body>
</html>