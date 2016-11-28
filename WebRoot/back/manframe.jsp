<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="/Static/css/newtemplate/common.css" type="text/css" />
		<title>管理区域</title>
		<link rel="stylesheet" href="/Static/js/jquery.chromatable-1.3.0/css/style.css" type="text/css"/>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery-ui-1.7.2.custom.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery.chromatable-1.3.0/jquery.chromatable.js"></script>
		<script>
			$(function(){
				$("#tt").chromatable({
					width: "100%",
					height: "auto",
					scrolling: "yes"
				});
			});
		</script>
		<style>
			body{
				margin-top:0px;
				
			}
			
			#man_zone{
				margin-top:0;
				padding:0;
				background-color: #f7f7f7;
			}
			.dqlb{
				padding:0px;
				
			}
			.dqlb li{
				float:left;
				list-style-type: none;
				margin:20px 0 0 20px;
				border:1px solid #d7d7d7;
				padding:10px;
				height:180px;
				position: relative;
				background-color: #fff;
				-moz-border-radius: 3px;      /* Gecko browsers */
			    -webkit-border-radius: 3px;   /* Webkit browsers */
			    border-radius:3px;            /* W3C syntax */
			    -moz-box-shadow:1px 1px 3px #a7a7a7; 
			    -webkit-box-shadow:1px 1px 3px #a7a7a7; 
			    box-shadow:1px 1px 3px #a7a7a7;
			}
			
			.tag{
				position: absolute;
				right:-10px;
				top:3px;
				padding:5px 10px !important;
				color:#fff;
			}
			.tag.dqlb_yuqi{
				background-color: #C32929;
			}
			.tag.dqlb_daoqi{
				background-color: #249A1B;
			}
		</style>
	</head>

<body>
<div id="man_zone">
	<ul class="dqlb">
		<c:forEach items="${dqlb_yuqi}" var="item">
			<li class="dqlb_yuqi">
				<div class="tag dqlb_yuqi">逾期</div>
				<div>项目编号:${item.financecode}</div>
				<div>项目简称:${item.financename}</div>
				<div>融资金额:<fmt:formatNumber value="${item.currenyamount}" pattern="#,##0.00"/></div>
				<div>融资利率:${item.rate}%</div>
				<div>借款期限:${item.term}</div>
				<div>还款方式:${item.returnpattern}</div>
				<div>担保公司:${item.guaranteerealname}</div>
				<div>下次还款时间:<fmt:formatDate value="${item.xyhkr}" pattern="yyyy-MM-dd"/></div>
			</li>
		</c:forEach>
		<c:forEach items="${dqlb_daoqi}" var="item">
			<li class="dqlb_daoqi">
				<div class="tag dqlb_daoqi">即将到期</div>
				<div>项目编号:${item.financecode}</div>
				<div>项目简称:${item.financename}</div>
				<div>融资金额:<fmt:formatNumber value="${item.currenyamount}" pattern="#,##0.00"/></div>
				<div>融资利率:${item.rate}%</div>
				<div>借款期限:${item.term}</div>
				<div>还款方式:${item.returnpattern}</div>
				<div>担保公司:${item.guaranteerealname}</div>
				<div>下次还款时间:<fmt:formatDate value="${item.xyhkr}" pattern="yyyy-MM-dd"/></div>
			</li>
		</c:forEach>
	</ul>
	
</div>
</body>
</html>
