//<![CDATA[
    	/*
			2013年9月12日 11:29:28
		*/		
		$(function(){
			
			$("#checkedAll").click(function() {
				if ($(this).attr("checked") == true) { // 全选
		   			$("input[name='chkEachs']").each(function() {
		   				$(this).attr("checked", true);
		   				$('tbody > tr', $('#sort-table')).addClass('selected');
		  			});
				} else { // 取消全选
		   			$("input[name='chkEachs']").each(function() {
		   				$(this).attr("checked", false);
		   				$('tbody > tr', $('#sort-table')).removeClass('selected');
		  			});
				}
			});
			
			//交替显示行				
			$('tbody > tr:odd', $('#sort-table')).toggleClass('alternation');		
			
			//选择行
			//为表格行添加选择事件处理
			$('tbody > tr', $('#sort-table')).click(function(){
				 var hasselected = $(this).hasClass("selected");
         		$(this)[hasselected?"removeClass":"addClass"]("selected").find(":checkbox").attr("checked",!hasselected);
			}).hover(		//注意这里的链式调用
				function(){
					$(this).addClass('mouseOver');
				},
				function(){
					$(this).removeClass('mouseOver');
				}
			);
				
			//获取表格中已选择的复选框的值集合
			$('#getSelected').click(function(){
				var sequence = [];
				$('#tbody_records input[name=chkEachs]:checked').each(function(){
					sequence.push(this.value);
				});
				alert(sequence.join(','));
			});			
		});
		
	//]]>