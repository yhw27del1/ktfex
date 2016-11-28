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
            url:'/back/autoInvestAction!pre_data',
            method:'get',
            toolbar:'#tb',
            onLoadSuccess:loadSuccess
            ">
        <thead>
			<tr>
				<th data-options="field:'username',width:80">交易帐号</th>
				<th data-options="field:'balance',width:100">账户余额</th> 
				<th data-options="field:'param8',width:100">可用余额不低于</th>   
				<th data-options="field:'param9',width:100">单笔投标最大金额</th>
				<th data-options="field:'last',width:100,formatter:date_formatter">生效时间</th>
				<th data-options="field:'param1',width:70"><span id="param1" style="color: green;text-decoration: underline;">风险评级</span></th>
				<th data-options="field:'param2',width:70"><span id="param2" style="color: green;text-decoration: underline;">还款方式</span></th>
				<th data-options="field:'param5',width:70"><span id="param5" style="color: green;text-decoration: underline;">担保方式</span></th>
				
				<th data-options="field:'dayMin',width:50">最小天</th>
				<th data-options="field:'dayMax',width:50">最大天</th>
				<th data-options="field:'param7',width:80">按天年利率</th>
				
				<th data-options="field:'monthMin',width:50">最小月</th>
				<th data-options="field:'monthMax',width:50">最大月</th>
				<th data-options="field:'param6',width:80">按月年利率</th>
				<th data-options="field:'levelScore',width:50">优先级</th>
			</tr>
		</thead>
    </table>
    <div id="tb" style="padding:5px;height:auto">
        <div>
        	<span style="font-size: 16px;font-weight: 20px;">&nbsp;&nbsp;&nbsp;&nbsp;风险评级：</span>
        	<span><input type="radio" name="pre.param1" value="1" class="combo" />优质项目</span>
        	<span><input type="radio" name="pre.param1" value="2" class="combo" />中等项目</span>
        	<span><input type="radio" name="pre.param1" value="3" class="combo" />合格项目</span>
        	<span><input type="radio" name="pre.param1" value="4" class="combo" />高风险项目</span>
        	<span>&nbsp;&nbsp;</span>
        	
        	<br />
        	
        	<span style="font-size: 16px;font-weight: 20px;">&nbsp;&nbsp;&nbsp;&nbsp;还款方式：</span>
        	<span><input type="radio" name="pre.param2" value="1" class="combo" />按月等额本息</span>
        	<span><input type="radio" name="pre.param2" value="2" class="combo" />按月等本等息</span>
        	<span><input type="radio" name="pre.param2" value="3" class="combo" />按月等额还息,到期一次还本</span>
        	<span><input type="radio" name="pre.param2" value="4" class="combo" />到期一次还本付息</span>
        	<span>&nbsp;&nbsp;</span>
        	
        	<br />
        	
        	<span style="font-size: 16px;font-weight: 20px;">&nbsp;&nbsp;&nbsp;&nbsp;担保方式：</span>
        	<span><input type="radio" name="pre.param5" value="1" class="combo" />本金担保</span>
        	<span><input type="radio" name="pre.param5" value="2" class="combo" />本息担保</span>
        	<span><input type="radio" name="pre.param5" value="3" class="combo" />无担保</span>
        	<span>&nbsp;&nbsp;</span>
        	<br /> 
        	<span style="font-size: 16px;font-weight: 20px;">&nbsp;&nbsp;&nbsp;&nbsp;期限类型:</span><span>&nbsp;&nbsp;<select name="qxType" id="qxType">
        	   <option value='month'>按月</option>
        	   <option value='day'>按日计息</option>
        	</select></span>
        	<span id='divqx1' style="display:none;"> 
        	<span style="font-size: 16px;font-weight: 20px;">按天期限：</span>
        	<span><input type="text" id="day1" name="pre.dayMin" size="5" class="easyui-numberbox" data-options="min:0,max:100" />&nbsp;&nbsp;天~<input type="text" id="day2" name="pre.dayMax" size="5" class="easyui-numberbox" data-options="min:0,max:100" />&nbsp;&nbsp;天，年利率：<input type="text" id="rate1" name="pre.param7" size="5" class="easyui-numberbox" data-options="min:0,precision:2,max:100" /></span>
        	<span>&nbsp;&nbsp;</span>
        	</span>
        	<span  id='divqx2'> 
        	<span style="font-size: 16px;font-weight: 20px;">按月期限：</span>
        	<span><input type="text" id="month1" name="pre.monthMin" size="5" class="easyui-numberbox" data-options="min:0,max:12" />个月~<input type="text" id="month2" name="pre.monthMax" size="5" class="easyui-numberbox" data-options="min:0,max:12" />个月，年利率：<input type="text" id="rate2" name="pre.param6" size="5" class="easyui-numberbox" data-options="min:0,precision:2,max:100" /></span>
        	<span>&nbsp;&nbsp;</span>
        	</span>
        	<br />
        	<span style="font-size: 16px;font-weight: 20px;">
        	&nbsp;&nbsp;&nbsp;&nbsp;账户余额：&nbsp;<input type="text"  placeholder="余额大于(整千)"   id="balance" name="pre.balance" size="20" class="easyui-numberbox" data-options="min:0,precision:2" />&nbsp;&nbsp;&nbsp;&nbsp;关键字：<input type="text" id="keyword" name="pre.keyword" size="30"  placeholder="输入用户名或真实姓名检索"  class="easyui-numberbox"/></span>
        	<span>&nbsp;&nbsp;</span>
        	<br />
        	<span style="font-size: 16px;font-weight: 20px;">
        	&nbsp;&nbsp;&nbsp;&nbsp;可用余额不低于：&nbsp;<input type="text"    placeholder="每次自动投标成功后 ,剩余的可用余额"    id="param8" name="pre.param8" size="40" class="easyui-numberbox" data-options="min:0,precision:2" />&nbsp;&nbsp;&nbsp;&nbsp;满足单笔投标最大金额：<input type="text" id="param9" name="pre.param8" size="30"  class="easyui-numberbox"/></span>
        	<span>&nbsp;&nbsp;</span>
            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
            <div>
            	<span id="n" style="font-size: 16px;"></span>
            	<span id="w" style="font-size: 16px;"></span>
            	<span id="sum_balance" style="font-size: 16px;"></span>
            	<span id="sum_balance_zq" style="font-size: 16px;"></span>
            </div>
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
		    str += '<a href="javascript:;" onclick="showLogs(\'/back/autoInvestAction!whos?id='+row.ID+'\',\'符合条件的人\')">符合条件的人</a><br />';
		    str += '<a href="javascript:;" onclick="showLogs(\'/back/autoInvestAction!draws?id='+row.ID+'\',\'中奖名单\')">抽签了</a>';
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
            if(val==null) return null;
            return val.substr(0,2)+"-"+row.CITYNAME.substr(0,2);
        }
		function hy_formatter(val,row,index){
            if(val==null) return null;
            return val.substr(0,2)+"-"+row.QYTYPE.substr(0,2);
        }
		
		function financingbase_name_formatter(val,row,index){
		    if(val==null) return null;
		    return '<a href="javascript:;" onclick="showdialog(\'/back/financingBaseAction!detail?id='+row.ID+'\')">' + val + '</a>';
		}
		function financer_name_formatter(val,row,index){
		    if(val==null) return null;
		    val = name(val);
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
		
		function showLogs(url_,tt){
		    $('#win').dialog({
		        title:tt,
		        width:900,
		        height:500,
		        modal:true,
		        maximizable:true,
		        resizable:true,
		        href:url_
		    });
		        
		}
		
		function loadSuccess(data){
			console.info(data);
			if(data){
				$("#n").html("委托人数:"+data.n);
				$("#w").html("委托金额:"+data.w);
				$("#sum_balance").html("可用余额汇总:"+data.sum_balance);
				$("#sum_balance_zq").html("可用余额(整千)汇总:"+data.sum_balance_zq);
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
		    
		    $("#qxType").change(function(){  
		      if('day'==$(this).val()){
		         $("#divqx1").css({'display':''});     
		         $("#divqx2").css({'display':'none'});
			      $("#month1").val('');
			      $("#month2").val('');
			      $("#rate2").val('');
		      }else{
		      	 $("#divqx1").css({'display':'none'});
		         $("#divqx2").css({'display':''});
		         $("#day1").val('');
		    	 $("#day2").val('');
		    	 $("#rate1").val('');
		      }
		    });
		    $("#search").click(function(){
		    	var chk_value_1 = [];//风险评估 
		    	
		    	var chk_value_2 = [];//还款方式 
		    	
		    	var chk_value_5 = [];//担保方式 
		    	
		    	$('input[name="pre.param1"]:checked').each(function(){
   					chk_value_1.push($(this).val());
  				}); 
  				
  				$('input[name="pre.param2"]:checked').each(function(){
   					chk_value_2.push($(this).val());
  				}); 
  				
  				$('input[name="pre.param5"]:checked').each(function(){
   					chk_value_5.push($(this).val());
  				});
  		 
		    	
		    	var day1 = $("#day1").val();
		    	var day2 = $("#day2").val();
		    	var rate1 = $("#rate1").val();
		     
		    	
		    	var month1 = $("#month1").val();
		    	var month2 = $("#month2").val();
		    	var rate2 = $("#rate2").val();
		    	var balance = $("#balance").val();
		    	var qxtype = $("#qxType").val();
		    	var keyword = $("#keyword").val();
		    	var param8 = $("#param8").val();
		    	var param9 = $("#param9").val();
		    	if($("#param8").length=0){
				   param8 = 0;
				}
		    	if($("#param9").length=0){
				   param9 = 0;
				} 	    	  
 				var data = "?_="+new Date().getTime()+"&pre.param1="+chk_value_1+"&pre.param2="+chk_value_2+"&pre.param5="+chk_value_5+"&pre.dayMin="+day1+"&pre.dayMax="+day2+"&pre.param7="+rate1+"&pre.monthMin="+month1+"&pre.monthMax="+month2+"&pre.param6="+rate2+"&pre.balance="+balance+"&pre.qxtype="+qxtype+"&pre.keyword="+keyword+"&pre.param8="+param8+"&pre.param9="+param9  
 				var source_url = '/back/autoInvestAction!pre_data';
	        	var url_ = source_url+data;
	        	$("#dg").datagrid({
		            url: url_
		        });
		        doToolTip();
		    });
		    
		    doToolTip();
		    $(window).resize(function(){
		        $("#dg").datagrid('resize');
		    });
		});
</script>