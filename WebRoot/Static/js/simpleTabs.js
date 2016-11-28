$(function() {
	var tabTitle = ".tab dl dt a";
	var tabContent = ".tab dl ul";
	$(tabTitle + ":first").addClass("on");
	$(tabContent).not(":first").hide();
	$(tabTitle).unbind("click").bind(
			"click",
			function() {
				$(this).siblings("a").removeClass("on").end()
						.addClass("on");
				var index = $(tabTitle).index($(this));
				$(tabContent).eq(index).siblings(tabContent).hide().end()
						.fadeIn("slow");
			});
});