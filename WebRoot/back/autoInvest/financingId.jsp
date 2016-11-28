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
            url:'/back/autoInvestAction!financing_data',
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            pageList:[15,30,50,100]
            
            ">
        
        <thead data-options="frozen:true">
                <tr>
                    <th data-options="field:'FINANCECODE',width:60" rowspan="2">编号</th>
                    <th data-options="field:'FINANCENAME',width:100,formatter:financingbase_name_formatter" rowspan="2">项目简称</th>
                    <th data-options="field:'FINANCERREALNAME',width:80,formatter:financer_name_formatter" rowspan="2">融资方</th>
                    <th data-options="field:'CREATEORG_SHORT',width:90,formatter:creater_formatter" rowspan="2">担保方</th>
                </tr>
            </thead>
            <thead>
                <tr>
                    <th colspan="3">融资额</th>
                    <th colspan="3">融资类型</th>
                    <th data-options="field:'STATE_ZH',width:50,formatter:state_formatter" rowspan="2">状态</th>
                    <th data-options="field:'CREATERUSERNAME',width:50,formatter:curstate_formatter" rowspan="2">进度</th>
                    <th data-options="field:'PROVINCENAME',width:60,formatter:area_formatter" rowspan="2">所属地域</th>
                    <th data-options="field:'HYTYPE',width:60,formatter:hy_formatter" rowspan="2">行业</th>
                    <th data-options="field:'showok',width:100,formatter:operator_formatter" rowspan="2">操作</th> 
                </tr>
                <tr>
                    <th data-options="field:'MAXAMOUNT',width:80,sortable:true">总融资额</th>
                    <th data-options="field:'CURCANINVEST',width:80,sortable:true">可融资额</th>
                    <th data-options="field:'CURRENYAMOUNT',width:80,sortable:true">已融资额</th>
                    <th data-options="field:'RATE',width:50,formatter:rate_formatter">年利率</th>
                    <th data-options="field:'TERM',width:50,formatter:term_formatter">期限</th>
                    <th data-options="field:'RETURNPATTERN',width:80">还款方式</th>
                </tr>
            </thead>
    </table>
    <div id="tb" style="padding:5px;height:auto">
        <div>
        	<input id="keyWord" type="text" name="keyWord" size="25" class="combo" title="项目编号"  placeholder="项目编号" />
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
		function curstate_formatter(val,row,index){
			if(val==null) return null;
			var str = '';
			var curamount = row.CURRENYAMOUNT;
			var maxamount = row.MAXAMOUNT;
            if(curamount == "0"){
            	str += "<span style='color:#2D85D8'>0.00%</span>";
            }else{
                str +="<span style='color:#2D85D8'>"+ ((curamount / maxamount)*100).toFixed(2)+"%</span>";
            }
            return str;
        }
		function operator_formatter(val,row,index){
		    var str = '';
		    if(row.ID==null) return null;
		    str += '<br/><a href="javascript:;" onclick="showLogs(\'/back/autoInvestAction!whos?id='+row.ID+'\',\'符合条件的人\')">筛选人</a>';
		    str += '<br/><br/><a href="javascript:;" onclick="showLogs(\'/back/autoInvestAction!draws?id='+row.ID+'\',\'中奖名单\')">抽签</a>'; 
		    <c:if test="${menuMap['-4'] == 'inline' }">
		       str += '<br/><br/><a href="javascript:;" onclick="autoInvestOpen(\'/back/autoInvestAction!autoInvestOpen?id='+row.ID+'\')">对外公布</a>';
		    </c:if>	 
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
		
	 function autoInvestOpen(url){
		$.messager.confirm('委托的包对外公布', '你确定要把委托自动投标的这个融资项目对外开放吗?', function(r){
            if (r){
            	$.messager.progress({text:'拼命处理中...',interval:700}); 
                $.post(url,{},function(data,state){
                	$.messager.progress('close');
	   				$.messager.alert('操作结果',data.message+'!','info');
	   				window.location.href = "/back/autoInvestAction!listForFinancingId";
	   			},'json');
            }
        });
	}
		
		
		function creater_formatter(val,row,index){
		    if(val=="合计"){
			    return "合计";
			   }
		    if(val==null) return row.CREATEORG;
		    return val+"/"+row.CREATEORG;
		}
		function area_formatter(val,row,index){
            if(val==null) return null;
            return val.substr(0,2)+"-"+row.CITYNAME.substr(0,2);
        }
		function hy_formatter(val,row,index){
            if(val==null) return null;
            return val.substr(0,2)+"-"+row.QYTYPE.substr(0,2);
        }
		
		function financingbase_name_formatter(val,row,index){
		    if(val==null) return null;
		    return '<a href="javascript:;" onclick="showdialog(\'/back/financingBaseAction!detail?id='+row.ID+'\',800)">' + val + '</a>';
		}
		function financer_name_formatter(val,row,index){
		    if(val==null) return null;
		    val = name(val);
		    return '<a href="javascript:;" onclick="showdialog(\'/back/member/memberBaseAction!memberDetails?id='+row.FINANCERUSERID+'\',800)">' + val + '</a>';
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
		
		function autoInvests(financingId){
			$.messager.confirm('投资人委托成交确认', '确认把抽中的人成交吗？', function(r){
	            if (r){
	            	$.messager.progress({text:'拼命加载中...',interval:700}); 
	                $.post('/back/autoInvestAction!autoInvests?id='+financingId,{},function(data,state){
	                	$.messager.progress('close');
		   				$.messager.alert('操作结果',data.message,'info');
		   			},'json');
	            }
        	});
		}
		
		function showdialog(url,width){
		    showModalDialog(url,'','dialogWidth:'+width+'px;dialogHeight:500px;center:yes;help:no;status:no'); 
		}
		
		function showLogs(url_,tt){
		    $('#win').dialog({
		        title:tt,
		        width:1000,
		        height:500,
		        modal:true,
		        href:url_,
		        maximized:true,
		        onLoad:doToolTip
		    });
		}
		
		function loadSuccess(data){
			if(data){
				if(data.total==0){
					$("#cj").hide();
				}
				$("#info").html(data.footer[0].info);
			}
		}
		
		function doToolTip(){
			$('#param1').tooltip({
				position: 'top',
			    content: '<div style="color:#fff">1:优质项目<br />2:中等项目<br />3:合格项目<br />4:高风险项目</div>',
			    onShow: function(){
			        $(this).tooltip('tip').css({
			            backgroundColor: '#666',
			            borderColor: '#666'
			        });
			    }
			});
			
			$('#param2').tooltip({
				position: 'top',
			    content: '<div style="color:#fff">1:按月等额本息<br />2:按月等本等息<br />3:按月等额还息,到期一次还本<br />4:到期一次还本付息</div>',
			    onShow: function(){
			        $(this).tooltip('tip').css({
			            backgroundColor: '#666',
			            borderColor: '#666'
			        });
			    }
			});
			
			$('#param5').tooltip({
				position: 'top',
			    content: '<div style="color:#fff">1:本金担保<br />2:本息担保<br />3:无担保</div>',
			    onShow: function(){
			        $(this).tooltip('tip').css({
			            backgroundColor: '#666',
			            borderColor: '#666'
			        });
			    }
			});
		}
		
		$(function(){
		    var height_ = document.body.clientHeight ;
		    $("#dg").datagrid({
		        height: height_
		    });
		    $("#search").click(function(){
		    	var keyWord = $("[name='keyWord']").val();
		        var source_url = '/back/autoInvestAction!financing_data';
		        var url_ = source_url+"?keyWord="+keyWord;
		        $("#dg").datagrid({
		            url: url_
		        });
		    });
		    $(window).resize(function(){
		        $("#dg").datagrid('resize');
		        $("#win").dialog('resize');
		    });
		});

</script>