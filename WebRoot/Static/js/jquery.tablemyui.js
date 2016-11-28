/*
 * tableMyUI 0.1
 * Copyright (c) 2012  
 * Date: 2012-04-11
 * 使用tableMyUI可以方便地将表格提示使用体验。先提供的功能有奇偶行颜色交替，鼠标移上高亮显示
 */
(function($){ 
	$.fn.tableStyleUI = function(options){ 
		var defaults = {
			evenRowStyle:{"background-color":"#F5FAFA"},
			oddRowStyle: {"background-color":"#fff"},
			activeRowStyle:{"background-color":"#f0f0f0"}
		}
		var options = $.extend(defaults, options);
		this.each(function(){
			var thisTable=$(this);
			//添加奇偶行颜色
			$(thisTable).children("tr:visible:even").css(options.evenRowStyle);
			$(thisTable).children("tr:visible:odd").css(options.oddRowStyle);
			//添加活动行颜色
			$(thisTable).children("tr:visible").bind("mouseover",function(){
				$(this).css(options.activeRowStyle);
			});
			$(thisTable).children("tr:visible").bind("mouseout",function(){
				$(thisTable).children("tr:visible:even").css(options.evenRowStyle);
				$(thisTable).children("tr:visible:odd").css(options.oddRowStyle);
			});
		});
	};
})(jQuery);