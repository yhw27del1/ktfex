$(function(){
	$("button").css({"padding":"1px","margin":"1px","font-size":"12px","font-family":"宋体","cursor":"pointer"});
	
	//按钮的hover效果等 
	$("button").hover(function(){
		$(this).addClass("ui-state-hover"); 
	},function(){
		$(this).removeClass("ui-state-hover"); 
	}).mousedown(function(){
		$(this).addClass("ui-state-active"); 
	}).mouseup(function(){
		$(this).removeClass("ui-state-active");
	});
	
	//刷新操作
	$('button.reflash').click(function(){
		window.location.href = window.location.href;
	});
});
