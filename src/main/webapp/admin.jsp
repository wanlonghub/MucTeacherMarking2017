<%@ page import="com.google.common.base.Strings" %>
<%@ page import="edu.muc.marking.dao.TokenDao" %>
<%@ page import="edu.muc.marking.bean.UserInfo" %>
<%@ page import="edu.muc.marking.dao.UserInfoDao" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.common.collect.Lists" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="edu.muc.marking.db.DBUtil" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="edu.muc.marking.dao.bean.*" %>
<%@ page import="edu.muc.marking.dao.AdminDao" %><%--
  Created by IntelliJ IDEA.
  User: wanlong
  Date: 17-3-5
  Time: 上午3:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // 验证token
    String token = request.getParameter("token");
    if(Strings.isNullOrEmpty(token) || TokenDao.isTokenValid(token) == Token.TOKEN_STATUS_MISSED){
        return;
    }

    // 获取id
    Admin admin = AdminDao.queryAdminByaccount(TokenDao.queryTokenByTokenString(token).getUser_account());
    int academy_id = admin.getAcademy_id();

    // 获取学院名称
    String academyName = null;
    if(admin.getAdmin_id() > 1000){
        academyName = admin.getAdmin_name() + "：超级管理员";
    }else{
        Academy academy = UserInfoDao.queryAcademyById(academy_id);
        System.out.println(academy);
        academyName = "学院：" + academy.getAcademy_name();
    }

    // 报表内容
    Connection connection = DBUtil.openConnection();
    String sql = null;
    PreparedStatement preStmt = null;
    ResultSet resultSet = null;
    StringBuilder reportContent = new StringBuilder();

    java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00000");

    // 超级管理员：学院打分 & 辅导员得分
    if(admin.getAdmin_id() > 1000) {
        reportContent.append("<div style='text-align:center;'>各学院辅导员得分和班级打分情况报表</div><br>");

        sql = "select * from t_academy";
        List<Academy> academyList = DBUtil.queryBeanList(connection, sql, Academy.class);
        for(Academy academy : academyList){
            academy_id = academy.getAcademy_id();
            reportContent.append("<div style='font-size:15px;text-align:center;'>").append(academy.getAcademy_name()).append("</div>");

            sql = "select * from t_teacher where academy_id = " + academy_id;
            List<Teacher> teacherList = DBUtil.queryBeanList(connection,sql,Teacher.class);
            for(Teacher teacher : teacherList){
                reportContent.append("<br>");
                reportContent.append("　■　" + teacher.getTeacher_name()+"(" + teacher.getTeacher_account() +")<br>");

                sql = "select * from t_class where teacher_id = " + teacher.getTeacher_id() + " order by class_name";
                List<ClassBean> classBeanList = DBUtil.queryBeanList(connection, sql,ClassBean.class);

                for(ClassBean classBean : classBeanList){
                    reportContent.append("　•　" + classBean.getClass_name() + "　");
                    // 班级人数
                    int sumOfClass = 0;
                    sql = "select count(t_student.student_id) as res from t_student where class_id = " + classBean.getClass_id();
                    resultSet = DBUtil.query(connection,preStmt,sql);
                    try{
                        while(resultSet.next()){
                            sumOfClass = resultSet.getInt("res");
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }finally {
                        try{
                            if (null != resultSet)
                                resultSet.close();
                            if (null != preStmt)
                                preStmt.close();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }

                    // 打分人数
                    double renshu = 1;
                    sql = "select count(t_result.mark_result_total) as res from t_result " +
                            "left join t_student on t_result.student_id = t_student.student_id " +
                            "where t_student.class_id = " + classBean.getClass_id();
                    resultSet = DBUtil.query(connection,preStmt,sql);
                    try{
                        while(resultSet.next()){
                            renshu = resultSet.getInt("res");
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }finally {
                        try{
                            if (null != resultSet)
                                resultSet.close();
                            if (null != preStmt)
                                preStmt.close();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }

                    // 总分数
                    double total = 0;
                    sql = "select sum(t_result.mark_result_total) as res from t_result " +
                            "left join t_student on t_result.student_id = t_student.student_id " +
                            "where t_student.class_id = " + classBean.getClass_id();
                    resultSet = DBUtil.query(connection,preStmt,sql);
                    try{
                        while(resultSet.next()){
                            total = resultSet.getInt("res");
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }finally {
                        try{
                            if (null != resultSet)
                                resultSet.close();
                            if (null != preStmt)
                                preStmt.close();
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }

                    // 平均分
                    String avg;
                    if(renshu == 0){
                        avg = "0";
                    }else{
                        avg = df.format(total / renshu);
                    }

                    reportContent.append("<br>　　 班级人数：").append(sumOfClass).append("　");
                    reportContent.append("<br>　　 打分人数：").append((int)renshu).append("　");
                    reportContent.append("<br>　　 平均分：").append(avg).append("　");
                    reportContent.append("<br><br>");
                }
                reportContent.append("<br>");
            }

        }

    }
    // 普通管理员：辅导员得分 & 班级打分
    else{

        reportContent.append("<div style='text-align:center;'>辅导员得分和班级打分情况报表</div>");

        sql = "select * from t_teacher where academy_id = " + academy_id;
        List<Teacher> teacherList = DBUtil.queryBeanList(connection,sql,Teacher.class);
        for(Teacher teacher : teacherList){
            reportContent.append("<br>");
            reportContent.append("■　" + teacher.getTeacher_name()+"(" + teacher.getTeacher_account() +")<br>");

            sql = "select * from t_class where teacher_id = " + teacher.getTeacher_id() + " order by class_name";
            List<ClassBean> classBeanList = DBUtil.queryBeanList(connection, sql,ClassBean.class);

            for(ClassBean classBean : classBeanList){
                reportContent.append("　•　" + classBean.getClass_name() + "　");

                // 班级人数
                int sumOfClass = 0;
                sql = "select count(t_student.student_id) as res from t_student where class_id = " + classBean.getClass_id();
                resultSet = DBUtil.query(connection,preStmt,sql);
                try{
                    while(resultSet.next()){
                        sumOfClass = resultSet.getInt("res");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }finally {
                    try{
                        if (null != resultSet)
                            resultSet.close();
                        if (null != preStmt)
                            preStmt.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }

                // 打分人数
                double renshu = 1;
                sql = "select count(t_result.mark_result_total) as res from t_result " +
                        "left join t_student on t_result.student_id = t_student.student_id " +
                        "where t_student.class_id = " + classBean.getClass_id();
                resultSet = DBUtil.query(connection,preStmt,sql);
                try{
                    while(resultSet.next()){
                        renshu = resultSet.getInt("res");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }finally {
                    try{
                        if (null != resultSet)
                            resultSet.close();
                        if (null != preStmt)
                            preStmt.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }

                // 总分数
                double total = 0;
                sql = "select sum(t_result.mark_result_total) as res from t_result " +
                        "left join t_student on t_result.student_id = t_student.student_id " +
                        "where t_student.class_id = " + classBean.getClass_id();
                resultSet = DBUtil.query(connection,preStmt,sql);
                try{
                    while(resultSet.next()){
                        total = resultSet.getInt("res");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }finally {
                    try{
                        if (null != resultSet)
                            resultSet.close();
                        if (null != preStmt)
                            preStmt.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }

                // 平均分
                String avg;
                if(renshu == 0){
                    avg = "0";
                }else{
                    avg = df.format(total / renshu);
                }

                reportContent.append("<br>　　 班级人数：").append(sumOfClass).append("　");
                reportContent.append("<br>　　 打分人数：").append((int)renshu).append("　");
                reportContent.append("<br>　　 平均分：").append(avg).append("　");
                reportContent.append("<br><br>");
            }
            reportContent.append("<br>");
        }

    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0 minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="apple-touch-icon" href="files/img/logo.jpg">
    <link rel="shortcut icon" href="files/img/logo.jpg">
    <link rel="apple-touch-startup-image" href="files/img/logo.jpg" media="screen and (max-device-width: 320px)">
    <link rel="apple-touch-startup-image" href="files/img/logo.jpg"
          media="(max-device-width: 480px) and (-webkit-min-device-pixel-ratio: 2)">
    <link rel="apple-touch-startup-image" sizes="640x1096" href="files/img/logo.jpg">
    <link rel="apple-touch-startup-image" sizes="1024x748" href="files/img/logo.jpg"
          media="screen and (min-device-width : 481px) and (max-device-width : 1024px) and (orientation : landscape)">
    <link rel="apple-touch-startup-image" sizes="768x1004" href="files/img/logo.jpg"
          media="screen and (min-device-width : 481px) and (max-device-width : 1024px) and (orientation : portrait)">
    <link rel="apple-touch-startup-image" sizes="1536x2008" href="files/img/logo.jpg"
          media="(device-width: 768px)	and (orientation: portrait)	and (-webkit-device-pixel-ratio: 2)">
    <link rel="apple-touch-startup-image" sizes="1496x2048" href="files/img/logo.jpg"
          media="(device-width: 768px)	and (orientation: landscape)	and (-webkit-device-pixel-ratio: 2)">
    <title>中央民族大学辅导员打分平台</title>

    <link href="files/css/style.css" rel="stylesheet" type="text/css">
    <link href="files/css/framework.css" rel="stylesheet" type="text/css">
    <link href="files/css/owl.carousel.css" rel="stylesheet" type="text/css">
    <link href="files/css/owl.theme.css" rel="stylesheet" type="text/css">
    <link href="files/css/swipebox.css" rel="stylesheet" type="text/css">
    <link href="files/css/font-awesome.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/public.js"></script>

</head>
<body>
<!--
<img src="http://edu-muc-market-items-image.bceimg.com/logo_share_wechat.jpg" style="width:0px;height:0px;">
-->

<div class="homepage-elements">

    <div class="homepage-one" data-scroll-index="0">

        <div class="footer-section" style="margin-top:30px;text-align: center;color: #FFFFFF;">
            <p id="message_box" style="font-size: 15px; line-height: 15px; margin-bottom: 10px;">辅导员打分平台 | 管理员</p>
            <p style="font-size: 10px;line-height: 10px;"><% out.print(academyName); %></p>
        </div>

        <div class="content" style="margin-left: 5px; margin-right: 5px;">
            <div class="one-half-responsive">

                <div class="container no-bottom">
                    <div class="contact-form no-bottom">

                        <div class="body" style=" background-color:#000; opacity:0.6;filter:alpha(opacity=60);">
                            <div class="fb_secondaryStatus"
                                 style="padding-left: 10px;padding-right: 10px;text-align: left;">

                                <br>

                                <div class="fb_ssItem text">
                                    <div class="fb_ssSubTitle" style="color:#ffffff;text-align: left;">

                                        <%
                                            out.println(reportContent.toString());
                                        %>

                                    </div>

                                </div>

                                <br>

                            </div>
                        </div>

                    </div>
                </div>
            </div>

        </div>

        <!-- footer -->


        <div class="footer-section" style="text-align: center;">
            <p class="center-text" style="color: #CCCCCC; font-size: 12px;line-height: 23px;">
                中央民族大学 Copyright 2017 <br>技术支持：<a id="power" style="color: #CCCCCC; font-size: 12px;"><u>wanlong.ma</u></a>
            </p>
            <div style="display: none;">
                <script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
                document.write(unescape("%3Cspan id='cnzz_stat_icon_1261407847'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1261407847%26show%3Dpic' type='text/javascript'%3E%3C/script%3E"));</script>
            </div>
        </div>
    </div>


</div>
<div class="homepage-overlay"></div>
<div class="homepage-background"></div>


</body>


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