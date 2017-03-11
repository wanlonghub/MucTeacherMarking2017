<%@ page import="com.google.common.base.Strings" %>
<%@ page import="edu.muc.marking.dao.TokenDao" %>
<%@ page import="edu.muc.marking.dao.bean.Token" %>
<%@ page import="edu.muc.marking.bean.UserInfo" %>
<%@ page import="edu.muc.marking.db.DBUtil" %>
<%@ page import="edu.muc.marking.dao.UserInfoDao" %>
<%@ page import="edu.muc.marking.dao.bean.Student" %><%--
  Created by IntelliJ IDEA.
  User: wanlong
  Date: 17-3-4
  Time: 下午4:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // 查询token
    String token = request.getParameter("token");

%>

<!DOCTYPE html>
<!-- saved from url=(0029)http://cn.mikecrm.com/Na6maSC -->
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>中央民族大学辅导员打分平台</title>

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <meta name="msapplication-tap-highlight" content="no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="robots" content="noindex">
    <meta name="robots" content="noarchive">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="renderer" content="webkit">

    <link rel="apple-touch-icon" sizes="144x144" href="files/img/logo.jpg">
    <link rel="icon" type="image/png" href="files/img/logo.jpg" sizes="32x32">
    <link rel="icon" type="image/png" href="files/img/logo.jpg" sizes="96x96">
    <meta name="msapplication-TileColor" content="#2d89ef">
    <meta name="msapplication-TileImage" content="files/img/logo.jpg">
    <meta name="theme-color" content="#267DC5">

    <link rel="stylesheet" type="text/css" href="./done_files/reset.css">
    <link rel="stylesheet" type="text/css" href="./done_files/error.css">
    <link rel="stylesheet" type="text/css" href="./done_files/formGlobal.css">
    <!-- <link rel="stylesheet" id="formStyleSheet" type="text/css" href="form/css/form.css"/> -->
    <link rel="stylesheet" type="text/css" href="./done_files/cpCalendar.css">
    <style>
        .my_buttom {
            background-color: rgba(255, 255, 255, 0);
            color: #FFFFFF;
            margin-top: 80px;
            height: 40px;
            padding-left: 10px;
            padding-right: 10px;
            margin-bottom: 10px;
            line-height: 20px;
            padding-bottom: 24px;
            min-width: 43%;
            display: inline-block;
            min-width: 100%;
            display: block;
            box-sizing: border-box;
            line-height: 19px !important;
            display: block !important;
            height: 36px !important;
            margin-right: 4px;
            width: 100%;
            background-color: rgba(255, 255, 255, 0.4);
            font-size: 12px;
            color: #3D3D3D;
            border: solid 1px #cacaca;
            display: inline-block;
            padding: 10px 20px;
            border-radius: 5px;
            background-color: rgba(255, 255, 255, 0);
            color: #FFFFFF;
            margin-top: 60px;
            border-radius: 5px;
        }
    </style>

    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/public.js"></script>

</head>
<!--[if IE 8]>
<body class="ie8 cn win sld_cn"> <![endif]-->
<!--[if IE 9]>
<body class="ie9 cn win sld_cn"> <![endif]-->
<!--[if gt IE 9]>
<body class="cn win sld_cn"> <![endif]-->
<!--[if !IE]>-->
<body class="cn win sld_cn"
      style="background-image:url(files/img/bg7.jpg); background-position: center 0%; background-repeat: no-repeat; background-size:cover; ">
<!--<![endif]-->
<form class="handleForm" method="post" enctype="multipart/form-data">
    <div class="wrapper">
        <div class="main" style="width: 80%;">

            <div class="" style="height: auto; min-height: 120px;color: #ffffff;">
                <div class="fb_headerInfo" style="padding-top:0px">
                    <div class="h_headline" style="text-align:center;font-size:16px;line-height: 120px;font-weight: 100;"><p>辅导员打分平台 | 信息确认</p></div>
                </div>
            </div>

            <div class="body" style=" background-color:#000; opacity:0.5;filter:alpha(opacity=50);">
                <div class="fb_secondaryStatus" style="padding-left: 50px;padding-right: 50px;">

                        <%
                            // token缺失
                            if(Strings.isNullOrEmpty(token)){
                                out.println("<div class=\"fb_ssItem text\">\n" +
                                        "<div class=\"fb_ssSubTitle\" style=\"color:#ffffff;\">token缺失</div>\n" +
                                        "</div>");
                                return;
                            }

                            // token有效性
                            if(TokenDao.isTokenValid(token) == Token.TOKEN_STATUS_INVALID){
                                out.println("<div class=\"fb_ssItem text\">\n" +
                                        "<div class=\"fb_ssSubTitle\" style=\"color:#ffffff;\">token失效</div>\n" +
                                        "</div>");
                                return;
                            }

                            // 获取个人信息
                            UserInfo userInfo = UserInfoDao.queryUserInfoByToken(token);

                            out.println("<div class=\"fb_ssItem text\">\n" +
                                    "<div class=\"fb_ssSubTitle\" style=\"color:#ffffff;\">请确认个人信息</div>\n" +
                                    "</div>\n" +
                                    "<br>\n" +
                                    "<div style=\"text-align: left\">\n" +
                                    "<div class=\"fb_ssSubTitle\" style=\"color:#ffffff;line-height: 23px;font-size: 14px;\">姓名：" + userInfo.getName() +
                                    "</div>\n" +
                                    "<div class=\"fb_ssSubTitle\" style=\"color:#ffffff;line-height: 23px;font-size: 14px;\">学号：" + userInfo.getAccount() +
                                            "</div>\n" +
                                    "<div class=\"fb_ssSubTitle\" style=\"color:#ffffff;line-height: 23px;font-size: 14px;\">学院：" + userInfo.getAcademyName() +
                                                    "</div>\n" +
                                    "<div class=\"fb_ssSubTitle\" style=\"color:#ffffff;line-height: 23px;font-size: 14px;\">班级：" + userInfo.getClassName() +
                                    "</div>\n" +
                                    "<div class=\"fb_ssSubTitle\" style=\"color:#ffffff;font-size:17px;line-height: 50px;\"><span style=\"font-size: 14px;\">辅导员：</span>" + userInfo.getTeacherName() +
                                    "</div>\n" +
                                    "</div>");
                        %>

                </div>
            </div>

            <%
                if(userInfo.getStatus() == Student.MARKING_STATUS_ALLOWED){
                    out.println("<input type=\"button\" class=\"my_buttom\" id=\"btn_sure\" value=\"确    认\">");
                }else{
                    out.println("<input type=\"button\" class=\"my_buttom\"  value=\"您已打分，请关闭页面\">");
                }
            %>


            <div class="right-text" style="text-align: right;">
                <a id="login_reg" class="button" style="color:#00C5CD; "  href="help.html">遇到问题</a>
            </div>

        </div>
    </div>
    <div class="copyright" style="position: absolute;bottom: 15px;width: 100%;">
        <p class="center-text" style="color: #CCCCCC; font-size: 12px;line-height: 23px;">
            中央民族大学 Copyright 2017 <br>技术支持：<a id="power" style="color: #CCCCCC; font-size: 12px;"><u>wanlong.ma</u></a>
        </p>
    </div>
    </div>
</form>

<div class="popwin" id="popwin"></div>

<div style="display: none;">
    <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1261407847'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1261407847%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script>
</div>

</body>

<script type="text/javascript">

    $(document).ready(function() {

        $("#btn_sure").click(function() {

            var account = $("#login_account").attr("value");
            var password = $("#login_password").attr("value");

            // checkLogin(username,password);

            window.location.href="marking.html" + "?token=<%out.print(token);%>";

        });

    });

</script>

<script type="text/javascript">
    $(document).ready(function() {
        $("#power").click(function() {
            var token = getUrlParam("token");
            var data = {
                token:token
            };
            $.ajax( {
                url:'tech.do',
                data:data,
                type:'post',
                cache:false,
                dataType:'json',
                success:function(data) {
                    window.location.href = "http://www.jianshu.com/u/48fc914f90d8";
                },
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    window.location.href = "http://www.jianshu.com/u/48fc914f90d8";
                }
            });
        });
    });
</script>

</html>