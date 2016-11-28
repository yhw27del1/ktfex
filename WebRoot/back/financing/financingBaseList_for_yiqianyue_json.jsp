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
			.mytable{font-size:13px;border-collapse:collapse;border:solid 1px #000;width:100%;}
			.mytable td,.mytable th{border:solid 1px #000;}
			.opencontract_a{color: -webkit-link;
text-decoration: underline;
cursor: pointer;}
			.opencontract_a:HOVER{text-decoration: underline;}
		</style>
		<script>
        /**
         * 扩展两个方法
         */
        $.extend($.fn.datagrid.methods, {
            /**
             * 开打提示功能
             * @param {} jq
             * @param {} params 提示消息框的样式
             * @return {}
             */
            doCellTip: function(jq, params){
                function showTip(data, td, e){
                    if ($(td).text() == "") 
                        return;
                    data.tooltip.text($(td).text()).css({
                        top: (e.pageY + 10) + 'px',
                        left: (e.pageX + 20) + 'px',
                        'z-index': $.fn.window.defaults.zIndex,
                        display: 'block'
                    });
                };
                return jq.each(function(){
                    var grid = $(this);
                    var options = $(this).data('datagrid');
                    if (!options.tooltip) {
                        var panel = grid.datagrid('getPanel').panel('panel');
                        var defaultCls = {
                            'border': '1px solid #333',
                            'padding': '2px',
                            'color': '#333',
                            'background': '#f7f5d1',
                            'position': 'absolute',
                            'max-width': '200px',
                            'border-radius' : '4px',
                            '-moz-border-radius' : '4px',
                            '-webkit-border-radius' : '4px',
                            'display': 'none'
                        }
                        var tooltip = $("<div id='celltip'></div>").appendTo('body');
                        tooltip.css($.extend({}, defaultCls, params.cls));
                        options.tooltip = tooltip;
                        panel.find('.datagrid-body').each(function(){
                            var delegateEle = $(this).find('> div.datagrid-body-inner').length ? $(this).find('> div.datagrid-body-inner')[0] : this;
                            $(delegateEle).undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove').delegate('td', {
                                'mouseover': function(e){
                                    if (params.delay) {
                                        if (options.tipDelayTime) 
                                            clearTimeout(options.tipDelayTime);
                                        var that = this;
                                        options.tipDelayTime = setTimeout(function(){
                                            showTip(options, that, e);
                                        }, params.delay);
                                    }
                                    else {
                                        showTip(options, this, e);
                                    }
                                    
                                },
                                'mouseout': function(e){
                                    if (options.tipDelayTime) 
                                        clearTimeout(options.tipDelayTime);
                                    options.tooltip.css({
                                        'display': 'none'
                                    });
                                },
                                'mousemove': function(e){
                                    var that = this;
                                    if (options.tipDelayTime) 
                                        clearTimeout(options.tipDelayTime);
                                    //showTip(options, this, e);
                                    options.tipDelayTime = setTimeout(function(){
                                            showTip(options, that, e);
                                        }, params.delay);
                                }
                            });
                        });
                        
                    }
                    
                });
            },
            /**
             * 关闭消息提示功能
             *
             * @param {}
             *            jq
             * @return {}
             */
            cancelCellTip: function(jq){
                return jq.each(function(){
                    var data = $(this).data('datagrid');
                    if (data.tooltip) {
                        data.tooltip.remove();
                        data.tooltip = null;
                        var panel = $(this).datagrid('getPanel').panel('panel');
                        panel.find('.datagrid-body').undelegate('td', 'mouseover').undelegate('td', 'mouseout').undelegate('td', 'mousemove')
                    }
                    if (data.tipDelayTime) {
                        clearTimeout(data.tipDelayTime);
                        data.tipDelayTime = null;
                    }
                });
            }
        });

       
        function doCellTip(){
            $('#dg').datagrid('doCellTip',{'max-width':'100px'});
        }
        function cancelCellTip(){
            $('#test').datagrid('cancelCellTip');
        } 
        
    </script>
	</head>
	<body>
		
	<table id="dg" class="easyui-datagrid"
            data-options="rownumbers:true,
            singleSelect:true,
            url:'/back/financingBaseAction!getFinancingBaseListForYiqianyue',
            method:'get',
            toolbar:'#tb',
            pagination:true,
            showFooter: true,
            sortName:'QIANYUEDATE',
            sortOrder:'desc',
            pageList:[15,30,50,100]
            ">
        
        <thead data-options="frozen:true">
                <tr>
                    <th data-options="field:'FINANCECODE',width:80,sortable:true" rowspan="2">项目编号</th>
                    <th data-options="field:'FINANCENAME',width:100" rowspan="2">项目简称</th>
                    <th data-options="field:'FINANCERREALNAME',width:80,formatter:financer_name_formatter" rowspan="2">融资方</th>
                    <th data-options="field:'CREATEORG_SHORT',width:90,formatter:creater_formatter" rowspan="2">担保方</th>
                </tr>
            </thead>
            <thead>
                <tr>
                       
                    <th colspan="3">融资额</th>   
                    <th colspan="3">融资类型</th>
                    <th data-options="field:'HAVEINVESTNUM',width:30" rowspan="2">投标<br/>人数</th>
                    <th data-options="field:'QIANYUEDATE',width:65,formatter:date_formatter,sortable:true" rowspan="2">签约日期</th>
                    <th data-options="field:'STATE_ZH',width:50,formatter:state_formatter" rowspan="2">状态</th>
                    <th data-options="field:'PROVINCENAME',width:60,formatter:area_formatter" rowspan="2">所属地域</th>
                    <th data-options="field:'HYTYPE',width:60,formatter:hy_formatter" rowspan="2">行业</th>
                    <th data-options="field:'GROUPSTR',width:60" rowspan="2">优先投标</th>
                    <th data-options="field:'showok',width:100,formatter:operator_formatter" rowspan="2">操作</th>
                </tr>
                <tr>
                    <th data-options="field:'MAXAMOUNT',width:65,sortable:true">总融资额</th>
                    <th data-options="field:'CURCANINVEST',width:65,sortable:true">可融资额</th>
                    <th data-options="field:'CURRENYAMOUNT',width:65,sortable:true">已融资额</th>
                    <th data-options="field:'RATE',width:50,formatter:rate_formatter">年利率</th>
                    <th data-options="field:'TERM',width:50,formatter:term_formatter">期限</th>
                    <th data-options="field:'RETURNPATTERN',width:65">还款方式</th>
                </tr>
            </thead>
    </table>
    <div id="tb" style="padding:5px;height:auto">
        <div>
            项目编号
            
        	<input type="text" name="queryCode" id="queryCode" class="combo" style="width:100px"  placeholder="项目编号" title="如果多个值,中间用半角逗号隔开,如：A,C,D"/>
         <input type="text" name="queryName" id="queryName" class="combo" style="width:100px"  placeholder="项目简称" title="融资项目名"/>
         
            签约时间区间: 
            <input class="easyui-datebox" name="startDate" style="width:100px" placeholder="开始日期">
            <input class="easyui-datebox" name="endDate" style="width:100px" placeholder="结束日期">
            <input id="qkeyWord" type="text" name="qkeyWord" class="combo" style="width:100px"  title="项目编号、融资方名称,融资方用户名" placeholder="关键字" />
            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search" title="先查询到结果再打开文字泡提示查看过长内容">查询</a>
            </div>
    </div>
    	<div id="win">
    		 <iframe frameborder="0" id="iframe" height="100%" width="100%"></iframe>
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
		    if(row.ID==null) return null;
		    str += '<a href="javascript:;" onclick="showinvestlist(\'/back/financingBaseAction!showInvest?fid='+row.ID+'\')">投标信息</a>';
            <c:if test="${menuMap['qianyue_dbhtFile'] == 'inline' }">
           
                str += '&nbsp;<a href="javascript:;" onclick="showdialog(\'/back/financingBaseAction!dbhtFileList?id=' + row.ID + '\',1000)">上传担保合同</a>';
            
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
            if(val==null || row.HYTYPE == null) return null;
           // return val.substr(0,2)+"-"+row.HYTYPE.substr(0,2);
           return val;
        }
		
		function financingbase_name_formatter(val,row,index){
		    if(val==null) return null;
		    return '<a href="javascript:;" onclick="showdialog(\'/back/financingBaseAction!detail?id='+row.ID+'\',1000)">' + val + '</a>';
		}
		function financer_name_formatter(val,row,index){
		    if(val==null) return null;
		    return '<a href="javascript:;" onclick="upload(\'/back/member/memberBaseAction!memberDetails?id='+row.FINANCERUSERID+'\',700)">' + name(val) + '</a>';
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
		
		function showinvestlist(url_){
			$("#iframe").attr("src",url_);
			$('#win').dialog({
		        title:'投标信息',
		        width:950,
		        height:500,
		        modal:true
		    });
		}

		function upload(url_){ 
			$("#iframe").attr("src",url_);
            $('#win').dialog({
                title:'上传担保合同',
                width:700,
                height:500,
                modal:true
            });
                
        }
        
		function opencontract(url){
			window.open(url);
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
		        var source_url = '/back/financingBaseAction!getFinancingBaseListForYiqianyue';
		        var startDate = $("[name='startDate']").val();
		        var endDate = $("[name='endDate']").val();
		        var qkeyWord = $("[name='qkeyWord']").val();
		        var queryCode = $("[name='queryCode']").val();
		        var queryName = $("[name='queryName']").val();
		        var containstr = $("[name='containstr']").val();
		        var url_ = source_url+
		            "?startDate=" + startDate + 
		            "&queryCode=" + queryCode + 
		            "&queryName=" + queryName +
		            "&containstr=1" + 
		            "&endDate=" + endDate +
		            "&qkeyWord=" + qkeyWord;
		        $("#dg").datagrid({
		            url: url_
		        });
		    });

		    $("#search1").click(function(){
                doCellTip();
            });

		    $("#search2").click(function(){
		    	location.reload(true);
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