<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/import.jsp"%>
<script type="text/javascript" src="/Static/js/autoheight.js"></script>
<script type="text/javascript" src="/Static/js/jquery.tablemyui.js"></script>
<script type="text/javascript" src="/Static/js/jquery.form.js"></script>
<style>
.a_button {
	cursor: pointer;
}

.a_button:HOVER {
	text-decoration: underline;
}
.enable_radio{
	font-size:13px;
	padding:0 5px 0 5px;
}
.ui-helper-hidden-accessible{
	display:none;
}
#type_radio{
	line-height:25px;
}
.button{
	font-size:13px;
	padding:3px;
	cursor:pointer;
}
#imp_div{
	font-size:13px;
}
#example{
	cursor:pointer;
	float:right;
	color:#3991D0;
	text-decoration: underline;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".table_solid").tableStyleUI();
		$( ".enable_radio" ).buttonset();
		$(".button").button();
		$("#imp").click(function(){
			$( "#imp_div" ).dialog("open");
		});
		$("#clear").click(function(){
			if(confirm("该操作不可恢复，确定要清除全部帐户的积分？")){
				$.post("/back/credit/creditRulesAction!clear",function(d,s){
					alert(d.result);
				},'json')
			}
		});
		
		$( "#imp_div" ).dialog({
            autoOpen: false,
            height: 400,
            width: 550,
            modal: true,
            resizable:false,
            buttons: {
                "导入": function() {
                	var datacount = $("#result_table > tbody > tr").length;
                	if( datacount == 0 ){
                		alert("没有数据可导入");
                		return;
                	}
                	$.post("/back/credit/creditRulesAction!imp_do",function(d,s){
                		$.each(d.resultlist,function(index,obj){
                			$("#result_table > tbody > tr").each(function(){
                				var username = $(this).children("td:first").html();
                				if(username == obj['username']){
                					var result = obj['result']=="成功"?"<span style='color:green'>成功</span>":"<span style='color:red'>成功</span>";
                					$(this).children("td:eq(2)").html(result);
                				}
                			})
                		});
                	},'json');
                }
            },
            close: function() {
            	$("#result_table > tbody > tr").remove();
            }
        });
        
        $("#example_div").dialog({
        	autoOpen: false,
        	resizable:false,
        	modal: false,
            width: 500,
            position:['right','middle']
        });
        $("#example").click(function(){
        	$( "#example_div" ).dialog("open");
        });
        
        $('#exceluploadform').ajaxForm({dataType:"json",success:result});
	});
	function setenable(id,value){
		$.post("/back/credit/creditRulesAction!setEnable",{"id":id,"value":value},function(data,status,xml){
			$("#form1").submit();
		});
	}
	function FileUpload_onselect(input){
		var value = input.value;
		var exs = value.split(".");
		var ex = exs[exs.length-1];
		var files = value.split("\\");
		if(ex=="xls"){
			$("#result_table > tbody > tr").remove();
			$("#exceluploadform").submit();
		}else{
			input.value="";
			alert("只能选择EXCEL 2000~2003文件");
		}
	}
	function result(d,s){
		if(s=="success"){
		    $.each(d.list,function(index,obj){
		    	$("<tr><td>"+obj['username']+"</td><td>"+obj['credit']+"</td><td>等待</td></tr>").appendTo($("#result_table > tbody"));
		    });
		}else{
			alert("服务器未响应，请稍后重试");
		}
    }
    function open_upload(){
    	$("#excelupload").trigger("click");
    }
</script>
<body>
	<input type='hidden' class='autoheight' value="auto" />
	<div id="myToolBar" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all">
		<div style="float: left;line-height:25px;">
			<span class="button" id="imp">批量导入</span>
			<span class="button" id="clear">批量清空</span>
			<form id="form1" action="/back/credit/creditRulesAction!rules_list" method="post">
				<input type="hidden" name="page" value="1" />
			</form>
		</div>
		<div style="float: right;">
			<button class="ui-state-default reflash">
				刷新
			</button>
			<button class="ui-state-default"
				onclick="javascript:window.location.href = '/back/credit/creditRulesAction!rule';return false;">
				新增
			</button>
		</div>
	</div>
	<div class="dataList ui-widget">
		<table class="ui-widget ui-widget-content">
			<thead>
				<tr class="ui-widget-header ">
					<th rowspan="2">
						标题
					</th>
					<th rowspan="2">
						建立时间
					</th>
					<th colspan="2" style="text-align:center">
						作用时间
					</th>
					<th rowspan="2">
						开户获赠</br>投资基金
					</th>
					<th colspan="2" style="text-align:center">
						投标抵扣
					</th>
					
					<th style="width:100px;" rowspan="2">
						启用状态
					</th>
				</tr>
				<tr class="ui-widget-header ">
					<th >
						生效时间
					</th>
					<th >
						失效时间
					</th>
					
					<th>
						阀值
					</th>
					<th>
						抵扣比例
					</th>
					
				</tr>
			</thead>
			<tbody class="table_solid">
				<c:forEach items="${pageView.records}" var="iter">
					<tr <c:if test="${iter.enable}"> style="color:red;font-weight:bold"</c:if>>
						<td>
							${iter.title}
						</td>
						<td>
							<fmt:formatDate value="${iter.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.effecttime}" pattern="yyyy-MM-dd"/>
						</td>
						<td>
							<fmt:formatDate value="${iter.expiretime}" pattern="yyyy-MM-dd"/>
						</td>
						<td>${iter.khjf }</td>
						<td>
							${iter.relation_value}
						</td>
						<td>
							${iter.value}
						</td>
						
						<td>
							<div class="enable_radio">
						        <input type="radio" id="radio_${iter.id}_x" name="radio_${iter.id}" <c:if test="${iter.enable}">checked="checked"</c:if>/><label for="radio_${iter.id}_x" class="enable_radio"  onclick="setenable('${iter.id}',true)">启用</label>
						        <input type="radio" id="radio_${iter.id}_y" name="radio_${iter.id}" <c:if test="${!iter.enable}">checked="checked"</c:if> /><label for="radio_${iter.id}_y" class="enable_radio" onclick="setenable('${iter.id}',false)">禁用</label>
						    </div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tbody>
				<tr>
					<td colspan="5">
						<jsp:include page="/common/page.jsp"></jsp:include></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div id="imp_div" title="批量导入积分" >
			<form action="/back/credit/creditRulesAction!imp_json" enctype="multipart/form-data" id="exceluploadform" method="post">
				<input id="excelupload" type="file" name="excel" style="outline:none;display:none;" onchange="FileUpload_onselect(this)" accept="application/vnd.ms-excel"/>
				<a href="javascript:;" onclick="open_upload()">选择文件</a><span id="example">excel文件格式</span>
			</form>
			
			<table id="result_table">
				<thead>
					<tr><th>用户名</th><th>积分</th><th>结果</th></tr>
				</thead>
				<tbody>
				</tbody>
			</table>
	</div>
	<div id="example_div" title="Excel文件格式" >
		<ul>
			<li>版本为excel 2000~2003 (2007及以上暂不支持)。</li>
			<li>无表头，首列为会员交易帐号，次列为要充入的积分值。</li>
			<li><img src="/Static/images/newtemplate/20121217093949.png"/></li>
		</ul>
	</div>
</body>