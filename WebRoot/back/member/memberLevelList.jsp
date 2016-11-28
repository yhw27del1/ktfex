<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<html>
	<head>
		<script type="text/javascript" src="/Static/js/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/metro/easyui.css">
    	<link rel="stylesheet" type="text/css" href="/Static/js/jquery-easyui-1.3.4/themes/icon.css">
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/Static/js/jquery-easyui-1.3.4/easyui-lang-zh_CN.js"/></script>
		<style>
			body{padding:0;margin:0;}
			.combo{height:22px;background: #fff;}
		</style>
	</head>
<body>
	
	
	
	
	
		<table id="dg" 
			class="easyui-datagrid"
	        data-options="rownumbers:true,
	            singleSelect:true,
	            url:'/back/member/memberLevelAction!list',
	            method:'get',
	            toolbar:'#tb',
	            pagination:true,
	            showFooter: true,
	            pageList:[15,30,50,100]
	            ">
	        <thead>
	            <tr>
	                <th data-options="field:'LEVELNAME',width:80">会员级别</th>
	                <th data-options="field:'TYPENAME',width:80">会员类型</th>
	                <th data-options="field:'TYPECODE',width:60">类型标识</th>
	                <th data-options="field:'USERCOUNT',align:'right',width:60">用户数</th>
	                <th data-options="field:'showok',width:100,formatter:operator_formatter">操作</th>
	            </tr>
	        </thead>
	    </table>
	    <div id="tb" style="padding:5px;height:auto">
	        <div>
	            <input type="text" name="keyWord" placeholder="会员级别关键字" class="combo" id="keyWord"/>
	            <a href="#" id="search" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <c:if test="${menuMap['memberLevel_ui']=='inline'}">
	            <a href="#" id="create" class="easyui-linkbutton" iconCls="icon-add" onclick="showdialog('/back/member/memberLevelAction!ui')">新建</a>
	            </c:if>
	        </div>
	    </div>
		<div id="prepare-dialog" style="display:none"><iframe frameborder="0" id="iframe" width="100%"></iframe></div>
		
		<div id="deldlg" class="easyui-dialog" title="删除会员级别分组" style="width:400px;height:200px;padding:10px;"
            data-options="
                iconCls: 'icon-remove',
                modal:true,
                closed:true,
                buttons: [{
                    text:'确认',
                    iconCls:'icon-ok',
                    handler:deldlg_click_ok
                },{
                    text:'取消',
                    handler:deldlg_click_cancel
                }]
            ">
	        该分组下还有用户<span id="confirm_del_span"></span>人，请指定这些用户的新分组。
	        <div style="color:#CE0E0E;font-size:15px;">该操作会删除关联的"投资约束规则"，请谨慎使用。</div>
	        
	        <input class="easyui-combobox" id="newlevel" name="newlevel" data-options="valueField:'ID',textField:'TEXT',editable:false">
	        <span id="confirm_del_span_error"></span>
	        <span id="confirm_del_span_oldid" style="display:none"></span>
    	</div>
    
</body>
</html>



<script>
	$(function(){
		var height_ = document.body.clientHeight;
    	$("#dg").datagrid({
		    height: height_
		});
		$("#search").click(function(){
			var source_url = '/back/member/memberLevelAction!list';
			var keyWord = $("[name='keyWord']").val();
			var url_ = source_url+
		   		"?keyWord="+keyWord;
			$("#dg").datagrid({
		   		url: url_
			});
		});
	});
	function operator_formatter(val,row,index){
		var str = '';
		<c:if test="${menuMap['memberLevel_edit'] == 'inline'}">
			str += '<a href="javascript:;" onclick="showdialog(\'/back/member/memberLevelAction!ui?id='+row.ID+'\')">修改</a>';
			str += '&nbsp;|&nbsp;<a href="javascript:;" onclick="deldialog(\''+row.ID+'\')">删除</a>';
		</c:if>
		return str;
	}
	
	<c:if test="${menuMap['memberLevel_edit'] == 'inline'}">
	function showdialog(url_){
		$("#iframe").attr("src",url_); 
		$('#prepare-dialog').show();
		$('#prepare-dialog').dialog({
	        title:'会员级别维护',
	        width:600,
	        height:300,
	        modal:true,
	        onClose:function(){
				$("#dg").datagrid('reload');
			}
	    });
	}
	function deldialog(id_){
		$.post('/back/member/memberLevelAction!hasitem',{'id':id_},function(data,state){
			if(data.count > 0){
				$("#confirm_del_span_oldid").html(id_);
				$('#newlevel').combobox('clear');
				$("#confirm_del_span").html(data.count);
				$('#newlevel').combobox('reload', '/back/member/memberLevelAction!exceptteam?id='+id_);
				$("#deldlg").dialog('open');
			}else{
				$.messager.confirm('删除会员级别分组', '操作不可回滚(<span style="color:#CE0E0E">该操作会删除关联的"投资约束规则"，请谨慎使用。</span>)，确认?', function(r){
	                if (r){
	                    $.post('/back/member/memberLevelAction!del',{'id':id_},function(data,state){
    						if(data.code == 1){
    							 $.messager.alert('处理结果','操作成功!','info');
    							 $("#dg").datagrid('reload');
    						}else{
    							$.messager.alert('处理结果','操作失败!code:'+data.code,'info');
    						}
    						
    					},'json');
	                }
	            });
			}
		},'json');
	}
	
	function deldlg_click_ok(){
		$("#confirm_del_span_error").html('');
		var old_id = $("#confirm_del_span_oldid").html();
    	var new_level_id = $('[name=\"newlevel\"]').val();
    	if(new_level_id != ''){
    		$.post('/back/member/memberLevelAction!del',{'id':old_id,'newid':new_level_id},function(data,state){
    			if(data.code == 1){
					 $.messager.alert('处理结果','操作成功!','info');
					 $("#dg").datagrid('reload');
				}else{
					$.messager.alert('处理结果','操作失败!code:'+data.code,'info');
				}
    		},'json');
    	}else{
    		$("#confirm_del_span_error").html('请选择一个级别');
    	}
    }
    
    function deldlg_click_cancel(){
    	$("#confirm_del_span_error").html('');
    	$('#deldlg').dialog('close');
    }
    
    
	</c:if>

</script>
