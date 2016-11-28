<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>简单面板</title>
		<script type="text/javascript"
			src="/Static/js/jquery-1.7.1.min.js"></script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<style type="text/css">
			ul,li,dl,dt,dd {
				padding: 0;
				margin: 0;
			}
			
			.tab {
				text-align: left;
				width: 100%;
				border: #ccc 1px solid;
				margin: 0 0;
			}
			
			.tab dt {
				border-bottom: #ccc 1px solid;
				height: 25px;
				background: #f1f1f1;
				margin-bottom: -1px;
				height: 25px;
				line-height: 25px;
			}
			
			.tab dt strong {
				padding: 0 15px;
				color: #444;
			}
			
			.tab dt a {
				display: inline-block;
				cursor: pointer;
				padding: 0 10px;
				text-align: center;
				background: #f1f1f1;
				color: #000;
			}
			
			.tab dt a.on {
				background: #fff;
				color: #333;
				font-weight: bold;
				border-bottom: 1px solid #fff;
				border-right: 1px solid #ccc;
				border-left: 1px solid #ccc;
			}
			
			.tab dd {
				padding: 10px;
				height: 200px;
				clear: both;
			}
		</style>
	</head>

	<body>
		<script type="text/javascript">
	$(function() {
		var tabTitle = ".tab dl dt a";
		var tabContent = ".tab dl ul";
		$(tabTitle + ":first").addClass("on");
		$(tabContent).not(":first").hide();
		$(tabTitle).unbind("click").bind(
				"click",
				function() {
					$(this).siblings("a").removeClass("on").end()
							.addClass("on");
					var index = $(tabTitle).index($(this));
					$(tabContent).eq(index).siblings(tabContent).hide().end()
							.fadeIn("slow");
				});
	});
</script>
		<div class="tab">
			<dl>
				<dt>
					<strong>发布融资项目</strong><a>第一栏</a><a>第二栏</a><a>第三栏</a><a>第四栏</a>
				</dt>
				<dd>
					<ul>
						1111111111111111111111
					</ul>
					<ul>
						2222222222222222222222
					</ul>
					<ul>
						3333333333333333333333
					</ul>
					<ul>
						4444444444444444444444
					</ul>
				</dd>
			</dl>
		</div>
	</body>
</html>
