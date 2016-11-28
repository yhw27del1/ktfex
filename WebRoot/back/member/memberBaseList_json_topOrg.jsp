<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	Date now = new Date();
%>
<html>
	<head>
		<title></title>
		<%@ include file="/common/taglib.jsp"%>
		<%@ include file="/common/import.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			<!--
				body{
				 overflow:auto !important;padding;0;margin:0;
				}
			-->
		</style>
 	</head>
	<body>
		<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            url:'/back/memberBaseAction!getAllMemberBaseList',
            singleSelect:true,
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100],
            onLoadSuccess:hideorshow
            ">
	        <thead data-options="frozen:true">
	        	<tr>
		        	<th data-options="field:'PNAME',width:80,formatter:name_formatter" rowspan="2">会员名称</th>
		            <th data-options="field:'USERNAME',width:100" rowspan="2">用户名</th>
		            <th data-options="field:'LEVELNAME',width:50" rowspan="2">等级</th>
		        </tr>
	        </thead>
	        <thead>
	            <tr>
	                <th data-options="field:'BALANCE_',width:80" rowspan="2">可用余额</th>
                    <th data-options="field:'FROZENAMOUNT',width:80" rowspan="2">冻结金额</th>
                    <th data-options="field:'PROVINCENAME',width:100,formatter:area_formatter" rowspan="2">所在区域</th>
                    <th data-options="field:'CATEGORY',width:100,formatter:member_type_formatter" rowspan="2">类型</th>
                    <th data-options="field:'ORGNAME',width:100" rowspan="2">开户机构</th>
                    <th data-options="field:'CREATEDATE',width:100,formatter:date_formatter" rowspan="2">开户时间</th>
                    <th data-options="field:'STATE',width:100,formatter:state_formatter" rowspan="2">状态</th>
                    <th data-options="field:'JINGBANREN',width:100" rowspan="2">介绍人</th>
                    <th data-options="field:'CHANNEL',width:50,formatter:zjc_formatter" rowspan="2">专户</th>
	                <th data-options="field:'FLAG',width:120,sortable:true,formatter:sanfangstate_formatter" rowspan="2">三方支付</th>
	                <th data-options="field:'showok',width:180,formatter:operator_formatter" rowspan="2">操作</th>
	            </tr>
	            <tr>
	            	
	            </tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	           &nbsp;&nbsp;会员名：<input id="keyword" type="text" name="keyword" class="combo" />
	           &nbsp;状态：<select class="easyui-combobox" data-options="width:80,editable:false,
                    url:'/back/member/memberBaseAction!state_json_for_select?time=<%=new Date() %>'" 
                name="memberState" id="memberState">
            </select>
	           &nbsp;类型：<select class="easyui-combobox" data-options="width:80,editable:false,
                    url:'/back/member/memberBaseAction!type_json_for_select?time=<%=new Date() %>'" 
                name="memberTypeId" id="memberTypeId"></select>
	           &nbsp;所在区域：<select class="easyui-combobox" data-options="
                onSelect:province_changed,
                onLoadSuccess:initCity,
                width:80,
                editable:false,
                url:'/back/member/memberBaseAction!province_json_for_select?time=<%=new Date() %>' "
                name="provinceCode" id="provinceCode">
            </select>
            <select class="easyui-combobox" data-options="
                width:80,
                editable:false,
                valueField:'value',
                textField:'text'" name="cityCode" id="cityCode">
            </select>
            &nbsp;介绍人：<input id="jingbanren" type="text" name="jingbanren" class="combo" />
	           <br/>
	           开户机构：<input id="orgName" type="text" name="orgName" class="combo" />
	           &nbsp;开户时间：<input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期" />
	            到<input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期"  />
	            &nbsp;专户
                <select class="easyui-combobox" name="channel" id="channel">
                    <option value="">全部</option>
                    <!--  <option value="1">招行</option>
                    <option value="2">工行</option>-->
                </select>
	           <!--  &nbsp;签约行
	            <select class="easyui-combobox" name="bank" id="bank">
	        		<option value="">全部</option>
	        		<option value="1">华夏银行</option>
	        		<option value="2">招商银行</option>
	        	</select>-->
	        	&nbsp;签约状态
	        	<select class="easyui-combobox" name="signState" id="signState">
                    <option value="">全部</option>
                    <option value="0">未签约</option>
                    <option value="1">签约中</option>
                    <option value="2">已签约</option>
                    <option value="3">已解约</option>
                </select>
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <c:if test="${menuMap['memberBaseList_exportExcel'] == 'inline' }">
                <a href="#" id="empExcel" class="easyui-linkbutton" iconCls="icon-save"  style="display:none" />导出EXCEL</a>
	           </c:if>
	        </div>
	    </div>
	    <div id="win"></div>
		<%@ include file="/common/messageTip.jsp" %>
	</body> 
</html>



<script>
	function province_changed(record){
	    $("#cityCode").combobox('reload','/back/member/memberBaseAction!city_json_for_select?province='+record['value']);
	}
	function initCity(){
	    var year_str = $("[name='provinceCode_select']").val();
	    $("#cityCode").combobox({url:'/back/member/memberBaseAction!city_json_for_select?province='+year_str});
	}

	   function hideorshow(data){
		   if(data.total > 0) {
			   $("#empExcel").show();
		   }else{
			   $("#empExcel").hide();
		   }
	    }

	function toURL(url) {
        window.location.href = url;
    }

	function showLogs(url_){ 
        $('#win').dialog({
            title:'日志',
            width:700,
            height:500,
            modal:true,
            href:url_
        });
            
    }
    
    function showdialog(url,width){
        showModalDialog(url,'','dialogWidth:'+width+'px;dialogHeight:500px;center:yes;help:no;status:no'); 
    }
    /**
    *注销指定id号的会员
    */
    function disable(id){
    	if (confirm("确认要停用此会员吗？执行这个操作，此会员将被停用，停用后此会员将无法登录，你确认要这么做吗？")) {
    		window.location.href = "/back/member/memberBaseAction!disable?id="+id;
        }
    }
    
    /**启用指定id号的会员*/
    function enable(id){
    	if (confirm("确认要启用此会员吗？执行这个操作，将会启用此会员，你确认要这么做吗？")) {
    		window.location.href = "/back/member/memberBaseAction!enable?id="+id;
        }
    }

	function rate_formatter(val,row,index){
		if(val == null ) return null;
		return val+"%";
	}

	function area_formatter(val,row,index){
        if(val == null ) return null;
        return val+" " +row.CITYNAME;
    }

	function member_type_formatter(val,row,index){
        if(val == null ) return null;
        var str = "";
        if(val == '0'){
            str = "机构 ";
        }else{
            str = "个人 ";
        }
        return str+" " +row.TYNAME;
    }

	function state_formatter(val,row,index){
        if(val=="1"){
            return "<span style='color:#D72323'>待审核</span>";
        }else if(val=="2"){
            return "<span style='color:#43C71F'>正常</span>";
        }else if(val=="3"){
            return "<span style='color:#2D85D8'>未通过审核</span>";
        }else if(val=="4"){
            return "<span style='color:#2D85D8'>已停用</span>";
        }else if(val=="5"){
            return "<span style='color:#2D85D8'>已注销</span>";
        }else{
            return val;
        }
    }
    
    function zjc_formatter(val,row,index){
        var str = "";
        if(val=="0"){
            str += "<span style='color:#D72323'></span>";
        }else if(val=="1"){
            str += "<span style='color:#D72323'>银行专户</span>";
        }else{
            str += "";
        }
        return str;
    }

	function sanfangstate_formatter(val,row,index){
		var str = "";
		if(val=="0"){
			str += "<span style='color:#D72323'></span>";
        }else if(val=="1"){
        	str += "<span style='color:#D72323'>签约中</span>";
        }else if(val=="2"){
        	str += "<span style='color:#43C71F'>已签约</span>";
        	if(row.SIGNBANK == "1"){
        		str += "<span style='color:#2D85D8'>华夏</span>";
            }else if(row.SIGNBANK == "2"){
                str += "<span style='color:#2D85D8'>招行</span>";
            }else{
                str += "&nbsp;-";
                }
        	if(row.SIGNTYPE == "1"){
                str += "<span style='color:#2D85D8'>本行</span>";
            }else if(row.SIGNTYPE == "2"){
                str += "<span style='color:#2D85D8'>他行</span>";
            }else{
                str += "&nbsp;-";
            }
        }else if(val=="3"){
        	str += "<span style='color:#2D85D8'>已解约</span>";
        }else{
        	str += "";
        }
		return str;
    }
	
	function term_formatter(val,row){
		if(val == null) return null;
		if(row.INTERESTDAY != 0){
			return row.INTERESTDAY+"天";
		}else{
			return val+"个月";
		}
	}
	function date_formatter(val,row,index){
		if(val==null){
			return null;
		}else{
			return new Date(val.time).format('yyyy-MM-dd');
		}
	}
	function jysc_formatter(val,row,index){
		if(val==null ){
			return null;
		}else if(val == 0){
			return 0;
		}else{
			return (val/60).toFixed(2);
		}
	}
	function operator_formatter(val,row,index){
		var str = '';
		if(row.USERNAME==null) return null;
		<c:if test="${menuMap['memberBase_details'] == 'inline' }">
			str += '&nbsp;<a href="javascript:;" onclick="toURL(\'/back/member/memberBaseAction!memberDetails?id='+row.ID+'\'); return false;">详细</a>';
		</c:if>
		
		<c:if test="${menuMap['memberBase_bankAccounts'] == 'inline' }">
            str += '&nbsp;<a href="javascript:;" onclick="toURL(\'/back/member/memberBaseAction!memberBankAccounts?id='+row.ID+'\'); return false;">结算信息</a>';
        </c:if>
        
        <c:if test="${menuMap['memberBase_modify'] == 'inline' }">
        if(row.STATE == '1' || row.STATE == '3' ){
            str += '&nbsp;<a href="javascript:;" onclick="toURL(\'/back/member/memberBaseAction!edit?id='+row.ID+'\'); return false; ">修改</a>';
        }
        </c:if>
        
        <c:if test="${menuMap['memberBase_disable'] == 'inline' }">
        if(row.STATE == '2'){
            str += '&nbsp;<a href="javascript:;" onclick="disable(\''+row.ID+'\');return false;">停用</a>';
        }
        </c:if>
        <c:if test="${menuMap['memberBase_enable'] == 'inline' }">
        if(row.STATE == '4' ){
            str += '&nbsp;<a href="javascript:;" onclick="enable(\''+row.ID+'\');return false;">启用</a>';
        }
        </c:if>
        <c:if test="${menuMap['memberBase_showlog'] == 'inline' }">
            str += '&nbsp;<a href="javascript:;" onclick="showLogs(\'/back/member/memberBaseAction!showlog?id=' + row.ID + '\')">变更日志</a>';
        </c:if>
		
		return str;
	}
	
	
	
	function creater_formatter(val,row,index){
		if(val=="合计") return "合计";
		return val+"/"+row.CREATEORG;
	}
	
	
	
	Date.prototype.format =function(format){
		var o = {
			"M+" : this.getMonth()+1, //month
			"d+" : this.getDate(), //day
			"h+" : this.getHours(), //hour
			"m+" : this.getMinutes(), //minute
			"s+" : this.getSeconds(), //second
			"q+" : Math.floor((this.getMonth()+3)/3), //quarter
			"S" : this.getMilliseconds() //millisecond
		}
		if(/(y+)/.test(format)){
			format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4- RegExp.$1.length));
		}
		for(var k in o)if(new RegExp("("+ k +")").test(format)){
			format = format.replace(RegExp.$1, RegExp.$1.length==1? o[k] :("00"+ o[k]).substr((""+ o[k]).length));
		}
		return format;
	}
	
	
	
	
	
	
	
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$("#search").click(function(){
			$("#empExcel").hide();
			var source_url = '/back/memberBaseAction!getAllMemberBaseList';
			var keyword = $("[name='keyword']").val();
			var memberState = $("[name='memberState']").val();
            var memberTypeId = $("[name='memberTypeId']").val();
            var provinceCode = $("[name='provinceCode']").val();
            var cityCode = $("[name='cityCode']").val();
            var orgName = $("[name='orgName']").val();
            var startDate = $("[name='startDate']").val();
			var endDate = $("[name='endDate']").val();
			var bank = $("[name='bank']").val();
			var channel = $("[name='channel']").val();
            var signState = $("[name='signState']").val();
			var jingbanren = $("[name='jingbanren']").val();
			var url_ = source_url+
		   		"?keyword=" + keyword +
		   		"&memberState=" + memberState +
		   		"&memberTypeId=" + memberTypeId +
		   		"&provinceCode=" + provinceCode +
		   		"&cityCode=" + cityCode +
		   		"&orgName=" + orgName +
		   		"&startDate=" + startDate + 
		   		"&endDate=" + endDate +
		   		"&bank=" + bank +
		   		"&channel=" + channel +
		   		"&signState=" + signState +
		   		"&jingbanren=" + jingbanren;
			
			$("#dg").datagrid({
		   		url: url_
			});
		});
		
		
		$("#empExcel").click(function(){
			var options = $("#dg").datagrid('options');
			var url = options.url;
			var index = url.indexOf("?");
			var parameters = "";
			if(index != -1){
				parameters = url.substring(index);
			}
			$(this).attr("href","/back/memberBaseAction!exportInvestors"+parameters);
		});
		$(window).resize(function(){
			$("#dg").datagrid('resize');
		});
	});
		
</script>