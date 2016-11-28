<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
	<head>
		<title></title>
		<%@ include file="/common/taglib.jsp"%>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<script type="text/javascript" src="/back/four.jsp"></script>
		<style>
			body{padding:0;margin:0;}
		</style>
	</head>
	<body>
		
	<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            url:'/back/financingBaseAction!getFinancingBaseListForSearch',
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            ">
        
        <thead data-options="frozen:true">
                <tr>
                    <th data-options="field:'FINANCECODE',width:80" rowspan="2">项目编号</th>
                    <th data-options="field:'FINANCENAME',width:100,formatter:financingbase_name_formatter" rowspan="2">项目简称</th>
                    <th data-options="field:'FINANCERREALNAME',width:80,formatter:name_formatter" rowspan="2">融资方</th>
                    <th data-options="field:'FINANCERUSERNAME',width:80" rowspan="2">融资方<br/>交易账户</th>
                    <th data-options="field:'CREATEORG_SHORT',width:90,formatter:creater_formatter" rowspan="2">担保方</th>
                </tr>
            </thead>
            <thead>
                <tr>
                    
                    <th colspan="3">融资额</th>
                    <th colspan="3">融资类型</th>
                    <th data-options="field:'HAVEINVESTNUM',width:30" rowspan="2">投标<br/>人数</th>
                    <th data-options="field:'ENDDATE',width:65,formatter:date_formatter" rowspan="2">投标截止</th>
                    <th data-options="field:'STATE_ZH',width:50,formatter:state_formatter" rowspan="2">状态</th>
                    <th data-options="field:'PROVINCENAME',width:60,formatter:area_formatter" rowspan="2">所属地域</th>
                    <th data-options="field:'HYTYPE',width:60,formatter:hy_formatter" rowspan="2">行业</th>
                </tr>
                <tr>
                    <th data-options="field:'MAXAMOUNT',width:65,sortable:true">总融资额</th>
                    <th data-options="field:'CURCANINVEST',width:65,sortable:true">可融资额</th>
                    <th data-options="field:'CURRENYAMOUNT',width:65,sortable:true">已融资额</th>
                    <th data-options="field:'RATE',width:50,formatter:rate_formatter">年利率</th>
                    <th data-options="field:'TERM',width:50,formatter:term_formatter">期限</th>
                    <th data-options="field:'RETURNPATTERN',width:50">还款方式</th>
                </tr>
            </thead>
    </table>
    <div id="tb" style="padding:5px;height:auto">
        <div>
            项目编号
            <select class="easyui-combobox" name="containstr" id="containstr">
        		<option value="1">包含</option>
        		<option value="2">不包含</option>
        	</select>
        	<input type="text" name="queryCode" id="queryCode" class="combo" style="width:100px"  placeholder="项目编号" title="如果多个值,中间用半角逗号隔开,如：A,C,D"/>
            <select class="easyui-combobox" name="states" id="states">
                <option value="">项目状态</option>
                <option value="2">投标中</option>
                <option value="4">已满标</option>
                <option value="5">已确认</option>
                <option value="7">已签约</option>
                <option value="8">已撤单</option>
            </select>
            挂单时间区间: 
            <input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期">
            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期">
            <input type="text" name="queryByOrgCode" id="queryByOrgCode" style="width:100px"  placeholder="担保机构编码" class="combo"  />
            <input id="qkeyWord" type="text" name="qkeyWord" class="combo" style="width:100px"  title="项目简称、项目编号、融资方名称,融资方用户名" placeholder="关键字" />
            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </div>
    </div>
    	<div id="win">
    		 
    	</div>
				
		<%@ include file="/common/messageTip.jsp"%>
	</body>
</html>
<script>
		function rate_formatter(val,row,index){
		    if(val == null ) return null;
		    return val+"%";
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
		    if(val==null){
		        return null;
		    }else{
		        return (val/60).toFixed(2);
		    }
		}
		function operator_formatter(val,row,index){
		    var str = '';
		   
		    return str;
		}
		
		
		function state_formatter(val,row,index){
		    if(val=="投标中"){
		        return "<span style='color:#D72323'>投标中</span>";
		    }else if(val=="已满标"){
		        return "<span style='color:#43C71F'>已满标</span>";
		    }else if(val=="签约完成"){
		        return "<span style='color:#2D85D8'>已签约</span>";
		    }else{
		        return val;
		    }
		}
		function creater_formatter(val,row,index){
		    if(val=="合计"){
			    return "合计";
			   }
		    if(val==null) return row.CREATEORG;
		    return val+"/"+row.CREATEORG;
		}
		function area_formatter(val,row,index){
            if(val==null || row.CITYNAME == null) return null;
            return val.substr(0,2)+"-"+row.CITYNAME.substr(0,2);
        }
		function hy_formatter(val,row,index){
            if(val==null || row.QYTYPE == null) return null;
            return val.substr(0,2)+"-"+row.QYTYPE.substr(0,2);
        }
		
		function financingbase_name_formatter(val,row,index){
		    if(val==null) return null;
		    return '<a href="javascript:;" onclick="showdialog(\'/back/financingBaseAction!detail?id='+row.ID+'\',1000)">' + val + '</a>';
		}
		function financer_name_formatter(val,row,index){
		    if(val==null) return null;
		    return '<a href="javascript:;" onclick="showdialog(\'/back/member/memberBaseAction!memberDetails?id='+row.FINANCERUSERID+'\',700)">' + val + '</a>';
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
		
		function cancel(id){
		    $.messager.confirm('确认操作', '确认撤销该融资项目(该操作不能回滚)?', function(r){
		        if (r){
		            $.post("/back/financingBaseAction!cancel",{"id":id},function(data,state){
		                $.messager.alert('操作结果',data.message,'info');
		                $("#dg").datagrid('reload');
		            },'json');
		        }
		    });
		}
		
		
		$(function(){
		    var height_ = document.body.clientHeight ;
		    $("#dg").datagrid({
		        height: height_
		    });
		    $("#search").click(function(){
		        var source_url = '/back/financingBaseAction!getFinancingBaseListForSearch';
		        var states = $("[name='states']").val();
		        var startDate = $("[name='startDate']").val();
		        var endDate = $("[name='endDate']").val();
		        var queryByOrgCode = $("[name='queryByOrgCode']").val();
		        var qkeyWord = $("[name='qkeyWord']").val();
		        var queryCode = $("[name='queryCode']").val();
		        var containstr = $("[name='containstr']").val();
		        var url_ = source_url+
		            "?states=" + states +
		            "&startDate=" + startDate + 
		            "&queryCode=" + queryCode + 
		            "&containstr=" + containstr + 
		            "&endDate=" + endDate +
		            "&queryByOrgCode=" + queryByOrgCode +
		            "&qkeyWord=" + qkeyWord;
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
		        $(this).attr("href","/back/financingBaseAction!EmpList"+parameters);
		    });
		    $(window).resize(function(){
		        $("#dg").datagrid('resize');
		    });
		});

</script>