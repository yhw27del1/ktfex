<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %> 
<link rel="stylesheet" type="text/css" href="/Static/css/page/pagination.css" />
<script type="text/javascript" src="/Static/css/page/jquery.pagination.js"></script>
<script type="text/javascript">
var totalPage=${pageView.totalpage};//总页数
var totalRecord=${pageView.totalrecord};//总记录数

var links=5;//连续链接数
var start_end=2;//首部和尾部的链接数，页码多时有效果
var showRecord=${showRecord};//每页显示多少条
var page=${page};//当前页
$(function(){
	$("option[value='"+showRecord+"']").attr("selected",true);
	$("#pagination").pagination(totalRecord, {
		'callback':pageselectCallback,
		'items_per_page':showRecord,//每页显示记录数
		'num_display_entries':links,//连续分页主体部分分页条目数或链接数
		'num_edge_entries':start_end,//两侧首尾分页条目数
		'prev_text':"<<",
		'next_text':">>",
		'current_page':page-1 //当前页索引，从0开始
	});
	$("#go").click(function(){
		pageselectCallback(0);
		return false;
	});
});
function pageselectCallback(page_index){
	var page = page_index+1;
	var form = document.forms[0];
	//解决一个页面有多个form的问题
	if(form.page){//第一个form下有分页参数吗？
		form.page.value=page;
		form.submit();
	}else{
		form = document.forms[1];
		form.page.value=page;
		form.submit();
	}
}
</script>
<div id="pagination"></div>
<input type="hidden" name="nowtime"/>
共:${pageView.totalrecord}条记录|共:${pageView.totalpage}页|每页显示:
<select name="showRecord" id="showRecord"> 
    <option value="10" >10</option>
    <option value="15" selected >15</option>  
    <option value="20">20</option>
    <option value="30">30</option>
    <option value="50">50</option>
</select>条 <a id="go" href="javascript:void(0);return false;">go</a>
<!-- 
<s:select name="showRecord" list="showRecordList" listKey="string1" listValue="string2"/>
-->
