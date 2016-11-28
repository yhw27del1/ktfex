/**
 * 自定义的MyCommonJs对象 
 * 功能：常用的JS
 * 作者： 
 * 时间: 2012-02-20  
 */
var MyCommonJs = function() {
};

/** 定义一些便捷的变量 */
MyCommonJs.prototype.randomNumber = new Date().getTime(); // 随机数
MyCommonJs.prototype.version = "MyCommonJs1.0 Beta"; // 当前MyCommonJs的版本 
/**
 * 根据指定的url打开一个页面
 * @param url 要打开的页面地址
 */
MyCommonJs.prototype.goPage = function(url) {
	try {
		if (!this.isNull(url)) {
			window.location.href = url;
		}
	} catch (e) {
	}
} 
/**
 * 输出您好
 */
MyCommonJs.prototype.goHello = function() {
	var now = new Date(); 
	hour = now.getHours(); 
	if(hour < 6){ return "凌晨好！"} 
	else if (hour < 9){ return "早上好！"}  
	else if (hour < 12){return "上午好！"}  
	else if (hour < 14){return "中午好！"}  
	else if (hour < 17){return "下午好！"}  
	else if (hour < 19){return "傍晚好！"}  
	else if (hour < 22){return "晚上好！"}  
	else {return "夜里好！"}   
} 


/** 实例化JS对象,用于调用的页面用 */
var MyCommonJs = new MyCommonJs();  