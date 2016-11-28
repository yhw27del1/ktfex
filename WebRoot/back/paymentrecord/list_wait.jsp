
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<style>
		.ui-datepicker{
			font-size:12px;
		}
		
		.error {
			float: left;
		}
		
		.tab_div{
			width:100%;
			min-height:300px;
			background-color: #fff;
		}
		.tab_div div{
			border:1px solid #ddd;
			border-top: none;
		}
		
		#startDate,#endDate,#fbcode{width:100px !important;}
		.toolbar{margin-top:5px;background-color:#d7d7d7;float:right;padding:5px 5px;margin-right:10px;}
		.nav{padding:0 !important;margin:0 !important;}
		.query{
			height:25px !important;
			vertical-align: middle;
			line-height:normal;
		}
		</style>
		<link rel="stylesheet" href="/Static/css/metro-bootstrap.css" type="text/css" />
		<script>
		$(function(){
			$("#startDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
			$("#endDate").datepicker({
				numberOfMonths: 2,
		        dateFormat: "yy-mm-dd"
		    });
		    $("#ui-datepicker-div").css({'display':'none'});
		    
		    $("#tab-ul li a").click(function(){
		    	var index = $(this).parent().index();
		    	$(this).parent().addClass("active").siblings().removeClass("active");
		    	var div = $(".tab_div div:eq("+index+")");
		    	var iframe = div.children("iframe");
		    	div.show().siblings().hide();
		    	var elements = $(this).attr("elements");
		    	
		    	
		    	
		    	if(typeof(elements) == "undefined"){
		    		$(".toolbar").hide();
		    		var src = iframe.attr("source_src");
		    		iframe.attr("src",src);
		    	}else{
		    		$(".toolbar").show();
		    		$(".toolbar input:text").hide();
		    		var obj = eval(elements);
		    		$.each(obj,function(key,val){
		    			var ele = $("#"+val.key).show();
		    			ele.attr("placeholder",val.value);
		    		})
		    	}
		    	
		    	var fcode = iframe.attr("fcode");
		    	var startDate = iframe.attr("startDate");
		    	var endDate = iframe.attr("endDate");
		    	var org_code = iframe.attr("org_code");
		    	var batch_no = iframe.attr("batch_no");
		    	
		    	$(".toolbar input#fbcode").val((typeof(fcode)!="undefined")?fcode:"");
		    	$(".toolbar input#startDate").val((typeof(startDate)!="undefined")?startDate:"");
		    	$(".toolbar input#endDate").val((typeof(endDate)!="undefined")?endDate:"");
		    	$(".toolbar input#org_code").val((typeof(org_code)!="undefined")?org_code:"");
		    	$(".toolbar input#batch_no").val((typeof(batch_no)!="undefined")?batch_no:"");
		    	
		    });
		    
		    $(".query").click(function(){
		    	var index = $("#tab-ul li.active").index();
		    	var iframe = $(".tab_div div:eq("+index+") iframe");
		    	var fcode = $(".toolbar input#fbcode").val();
		    	var startDate = $(".toolbar input#startDate").val();
		    	var endDate = $(".toolbar input#endDate").val();
		    	var org_code = $(".toolbar input#org_code").val();
		    	var batch_no = $(".toolbar input#batch_no").val();
		    	var src = iframe.attr("source_src");
		    	
		    	
		    	iframe.attr("fcode",fcode);
		    	iframe.attr("startDate",startDate);
		    	iframe.attr("endDate",endDate);
		    	iframe.attr("org_code",org_code);
		    	iframe.attr("batch_no",batch_no);
		    	
		    	
		    	iframe.attr("src",src+"?fbcode="+fcode+"&startDate="+startDate+"&endDate="+endDate+"&org_code="+org_code+"&batch_no="+batch_no);
		    });
		    
		    $(window).load(function(){
		    	var height = window.parent.frames['manFrame'].document.documentElement.clientHeight;
		    	height -= 40;
		    	$(".tab_div,iframe").css({"height":(height-4)+"px"});
		    	
		    });
		    
		});
		
		</script>
	</head>
	<body>
		<ul class="nav nav-tabs" id="tab-ul">
			<li class="active"><a href="javascript:;" elements="[{key:'fbcode',value:'项目编号'},{key:'startDate',value:'签约日期:起'},{key:'endDate',value:'签约日期:止'}]" >单记录还款</a></li>
			<c:if test="${menuMap['paymentrecord_list_standby']=='inline'}">
				<li><a href="javascript:;" elements="[{key:'fbcode',value:'项目编号'},{key:'startDate',value:'应还日期:起'},{key:'endDate',value:'应还日期:止'}]">批量预还款</a></li>
			</c:if>
			<c:if test="${menuMap['paymentrecord_do_audit']=='inline'}">
				<li><a href="javascript:;">批量还款审核</a></li>
			</c:if>
			<c:if test="${menuMap['paymentrecord_list_query_print']=='inline'}">
				<li><a href="javascript:;" elements="[{key:'org_code',value:'担保机构'},{key:'startDate',value:'实还日期:起'},{key:'endDate',value:'实还日期:止'},{key:'batch_no',value:'批处理号'}]">还款汇总打印</a></li>
			</c:if>
			<div class="toolbar">
					<input type="text" id="fbcode" name="fbcode" value="${fbcode}" style="width: 120px;" placeholder="项目编号"/>
					<input type="text" id="batch_no" name="batch_no" value="${batch_no}" style="width: 150px;display:none;" placeholder="批处理号"/>
					<input type="text" id="org_code" name="org_code" value="${org_code}" style="width: 120px;display:none;" placeholder="担保机构"/>
					<input type="text" id="startDate" name="startDate" placeholder="应还日期:起" value="<fmt:formatDate value='${startDate}' pattern="yyyy-MM-dd" />"/>
					<input type="text" id="endDate" name="endDate" placeholder="应还日期:止" value="<fmt:formatDate value='${endDate}' pattern="yyyy-MM-dd" />"/>
					<input type="button" class="ui-state-default query" value="查询">
			</div>
		</ul>
		<div class="tab_div">
			<div><iframe align="top" frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" width="100%" source_src="/back/paymentRecord/paymentRecordAction!paymentrecord_list_single"></iframe></div>
			<c:if test="${menuMap['paymentrecord_list_standby']=='inline'}">
				<div style="display:none"><iframe align="top" frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" width="100%" source_src="/back/paymentRecord/paymentRecordAction!paymentrecord_list_standby"></iframe></div>
			</c:if>
			<c:if test="${menuMap['paymentrecord_do_audit']=='inline'}">
				<div style="display:none"><iframe align="top" frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" width="100%" source_src="/back/paymentRecord/paymentRecordAction!paymentrecord_list_preaudit"></iframe></div>
			</c:if>
			<c:if test="${menuMap['paymentrecord_list_query_print']=='inline'}">
				<div style="display:none"><iframe align="top" frameborder="0" scrolling="auto" marginheight="0" marginwidth="0" width="100%" source_src="/back/paymentRecord/paymentRecordAction!paymentrecord_list_query_print"></iframe></div>
			</c:if>
		</div>
	</body>
</html>
