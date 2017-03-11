//---------用户信息-----------
var isLoginStr;
var isLogin = false;
var user_id;
var user_name;

jQuery(document).ready(function() {

	isLoginStr = getCookie("isLogin");
	user_id = getCookie("user_id");
	if(isLoginStr == "true"){
		isLogin = true;
    }

});

/**
 * 注销
 */
function logout(){
	setCookie("isLogin", false, 1);
	setCookie("user_id", -1, 1);
}

function setCookie(c_name, value, expiredays) {
	var exdate = new Date()
	exdate.setDate(exdate.getDate() + expiredays)
	document.cookie = c_name + "=" + escape(value)
			+ ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString())
}

function getCookie(c_name) {
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=")
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1
			c_end = document.cookie.indexOf(";", c_start)
			if (c_end == -1)
				c_end = document.cookie.length
			return unescape(document.cookie.substring(c_start, c_end))
		}
	}
	return "";
}

/**
 * 获取URL参数
 * @param name
 * @returns
 */
function getUrlParam(name){  
    //构造一个含有目标参数的正则表达式对象  
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
    //匹配目标参数  
    var r = window.location.search.substr(1).match(reg);  
    //返回参数值  
    if (r!=null) return unescape(r[2]);  
    return null;  
} 

/**
 * 携带参数的post页面跳转
 * 
 * @param url
 * @param args
 */
function StandardPost(url, args) {
	var form = jQuery("<form method='post'></form>");
	form.attr({
		"action" : url
	});
	for (arg in args) {
		var input = jQuery("<input type='hidden'>");
		input.attr({
			"name" : arg
		});
		input.val(args[arg]);
		form.append(input);
	}
	form.submit();
}

/**
 * 判断字符串str中是否包含字符串sub
 * @param str
 * @param sub
 */
function stringContains(str,sub){
	
	//返回大于等于0的整数值，若不包含"Text"则返回"-1。
	return str.indexOf(sub) >= 0 ? true : false;
	
}

/**
 * 设置本地存储
 * @param key
 * @param value
 */
function setLocalStorage(key,value){
	if (window.localStorage) {
	    localStorage.setItem(key, value);	
	} else {
	    Cookie.write(key, value);	
	}
}

/**
 * 获取本地存储
 * @param key
 * @returns
 */
function getLocalStorage(key){
	return window.localStorage? localStorage.getItem(key): Cookie.read(key);
}

/**
 * 自动获取焦点
 * @param obj
 */
function setFocus(obj) {

	if (obj.setSelectionRange) {

		setTimeout(function() {

			obj.setSelectionRange(0, 0);

			obj.focus();

		}, 100);

	} else {

		if (obj.createTextRange) {

			var range = obj.createTextRange();

			range.collapse(true);

			range.moveEnd("character", 0);

			range.moveStart("character", 0);

			range.select();

		}

		try {
			obj.focus();
		} catch (e) {
		}

	}

}

/**
 * 返回页面顶部
 * @param min_height
 */
function gotoTop(min_height) {
	// 预定义返回顶部的html代码，它的css样式默认为不显示
	var gotoTop_html = '<div id="gotoTop">返回顶部</div>';
	// 将返回顶部的html代码插入页面上id为page的元素的末尾
	$("#page").append(gotoTop_html);
	$("#gotoTop").click(// 定义返回顶部点击向上滚动的动画
	function() {
		$('html,body').animate({
			scrollTop : 0
		}, 700);
	}).hover(// 为返回顶部增加鼠标进入的反馈效果，用添加删除css类实现
	function() {
		$(this).addClass("hover");
	}, function() {
		$(this).removeClass("hover");
	});
	// 获取页面的最小高度，无传入值则默认为600像素
	min_height ? min_height = min_height : min_height = 600;
	// 为窗口的scroll事件绑定处理函数
	$(window).scroll(function() {
		// 获取窗口的滚动条的垂直位置
		var s = $(window).scrollTop();
		// 当窗口的滚动条的垂直位置大于页面的最小高度时，让返回顶部元素渐现，否则渐隐
		if (s > min_height) {
			$("#gotoTop").fadeIn(100);
		} else {
			$("#gotoTop").fadeOut(200);
		}
		;
	});
};