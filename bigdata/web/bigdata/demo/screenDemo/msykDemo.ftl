<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta charset="UTF-8">
	<title>美师优课数据监控</title>
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/iconfont.css"/>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/all.css"/>
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/bigdata/v3/static/datav/custom/css/style.css"/>
</head>
<body>
	<!--头部 S-->
	<div class="head clearfix">
        <div class="logo-wrap clearfix">
            <div class="flex-centered">
                <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/wp-logo.png" class="mt-5">
            </div>
            <div class="flex-centered">
                <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/ms-logo.png" >
            </div>
            <div class="flex-centered">
                <img src="${request.contextPath}/bigdata/v3/static/datav/custom/images/ws-logo.png" >
            </div>
        </div>
        <div class="titl-wrap">
            <span>美师优课数据监控</span>
        </div>
        <div class="full-screen-wrap">
            <div class="full-screen-btn">
                <div class="btn-inner"></div>
            </div>
        </div>
    </div>
    <!--头部 E-->
    
    <!--主体 S-->
    <div class="main-container">
        <!-- 资源建设情况 -->
        <div class="box-data-wrap ">
            <div class="box-data">
                <div class="box-data-head clearfix">
                    <div class="box-data-name">
                        <span>资源建设情况</span>
                    </div>
                    <div class="box-data-scope clearfix">
                        <div class="btn-group filter">
                        	<button class="btn dropdown-toggle">近一周 <i class="wpfont icon-angle-single-down"></i></button>
                        	<ul class="dropdown-menu">
                        		<li><a href="#">全部</a></li>
                        		<li><a href="#">近一月</a></li>
                        		<li><a href="#">近一周</a></li>
                            </ul>
                        </div>
                        <div class="btn-group filter">
                        	<button class="btn dropdown-toggle">全校 <i class="wpfont icon-angle-single-down"></i></button>
                        	<ul class="dropdown-menu">
                        		<li><a href="#">全校</a></li>
                        		<li><a href="#">全年级</a></li>
                            </ul>
                        </div>
                        <div class="btn-group filter">
                        	<button class="btn dropdown-toggle">全科 <i class="wpfont icon-angle-single-down"></i></button>
                        	<ul class="dropdown-menu">
                        		<li><a href="#">全科</a></li>
                                <li><a href="#">语文</a></li>
                                <li><a href="#">数学</a></li>
                                <li><a href="#">英语</a></li>
                                <li><a href="#">物理</a></li>
                                <li><a href="#">化学</a></li>
                                <li><a href="#">生物</a></li>
                                <li><a href="#">地理</a></li>
                                <li><a href="#">政治</a></li>
                                <li><a href="#">历史</a></li>
                                <li><a href="#">科学</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="box-data-content clearfix js-one">
                    <div class="left-btn-wrap">
                        <ul>
                            <li class="active" data-num = "1382" data-option = "opOne1">备课数</li>
                            <li data-num = "257" data-option = "opOne2">素材库</li>
                            <li data-num = "51498" data-option = "opOne3">校本素材</li>
                        </ul>
                    </div>
                    <div class="right-chart-wrap">
                        <div class="right-chart-head text-right">
                            <span class="font-quartz">1382</span>
                        </div>
                        <div class="right-chart-content">
                            <div class="box-chart partOne"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 数据总览 -->
        <div class="box-data-wrap"> 
            <div class="box-data">
                <div class="box-data-head clearfix">
                    <div class="box-data-name">
                        <span>数据总览</span>
                    </div>
                    <div class="box-data-scope clearfix js-data-pandect">
                        <button type="button" class="btn active"><span>全部</span></button>
                        <button type="button" class="btn"><span>近一周</span></button>
                        <button type="button" class="btn"><span>近一月</span></button>
                    </div>
                </div>
                <div class="box-data-content js-two">
                    <div class="no-chart-wrap clearfix">
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="54" data-speed="500"></b>
                                    </div>
                                    <div class="box-name">班级总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="354" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="31594" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃用户数 / 用户总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="34" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="112" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃教师数 / 教师总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="234" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="10012" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃学生数 / 学生总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="9" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="72" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">授课次数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="168" data-speed="500"></b>
                                    </div>
                                    <div class="box-name">布置作业数</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="no-chart-wrap clearfix hide">
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="234" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="10012" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃学生数 / 学生总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="9" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="72" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">授课次数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="168" data-speed="500"></b>
                                    </div>
                                    <div class="box-name">布置作业数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="54" data-speed="500"></b>
                                    </div>
                                    <div class="box-name">班级总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="354" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="31594" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃用户数 / 用户总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="34" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="112" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃教师数 / 教师总数</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="no-chart-wrap clearfix hide">
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="9" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="72" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">授课次数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="354" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="31594" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃用户数 / 用户总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="168" data-speed="500"></b>
                                    </div>
                                    <div class="box-name">布置作业数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="54" data-speed="500"></b>
                                    </div>
                                    <div class="box-name">班级总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="34" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="112" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃教师数 / 教师总数</div>
                                </div>
                            </div>
                        </div>
                        <div class="no-chart-box-wrap">
                            <div class="no-chart-box">
                                <div class="">
                                    <div class="box-num flex-centered">
                                        <b class="font-quartz" data-from="0" data-to="234" data-speed="500"></b>
                                        <span class="font-quartz" data-from="0" data-to="10012" data-speed="500"></span>
                                    </div>
                                    <div class="box-name">活跃学生数 / 学生总数</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
        
        <!-- 班级活跃情况TOP5 -->
        <div class="box-data-wrap">
            <div class="box-data">
                <div class="box-data-head clearfix">
                    <div class="box-data-name">
                        <span>班级活跃情况TOP5</span>
                    </div>
                    <div class="box-data-scope clearfix">
                        <div class="btn-group filter">
                        	<button class="btn dropdown-toggle">全校 <i class="wpfont icon-angle-single-down"></i></button>
                        	<ul class="dropdown-menu">
                        		<li><a href="#">全校</a></li>
                        		<li><a href="#">全年级</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="box-data-content js-three">
                    <ul class="tabs tabs-card" tabs-group="g1">
                    	<li class="tab active"><a href="#g1-tab1">作业布置情况</a></li>
                    	<li class="tab"><a href="#g1-tab2">作业完成情况</a></li>
                    	<li class="tab"><a href="#g1-tab3">自主学习情况</a></li>
                    </ul>
                    <div class="box-data-content">
                        <div class="tabs-panel active" tabs-group="g1" id="g1-tab1">
                            <div class="left-btn-wrap left-btn-big-wrap">
                                <ul class="ranking-list">
                                    <li class="active">高一(1)班</li>
                                    <li>高一(2)班</li>
                                    <li>高一(3)班</li>
                                    <li>4&nbsp; 高一(4)班</li>
                                    <li>5&nbsp; 高一(5)班</li>
                                </ul>
                            </div>
                            <div class="right-chart-wrap">
                                <div class="right-chart-content">
                                    <div class="box-chart partThree"></div>
                                </div>
                            </div>
                        </div>
                        <div class="tabs-panel" tabs-group="g1" id="g1-tab2">
                            <div class="left-btn-wrap left-btn-big-wrap">
                                <ul class="ranking-list">
                                    <li class="active">高一(4)班</li>
                                    <li>高一(2)班</li>
                                    <li>高一(5)班</li>
                                    <li>4&nbsp; 高一(1)班</li>
                                    <li>5&nbsp; 高一(3)班</li>
                                </ul>
                            </div>
                            <div class="right-chart-wrap">
                                <div class="right-chart-content">
                                    <div class="box-chart">
                                        <ul class="proportion-made">
                                            <li data-text = "作业提交率" data-num = "98%">
                                                <div class="proportion-wrap">
                                                    <div class="green"></div>
                                                </div>
                                            </li>
                                            <li data-text = "作业批改率" data-num = "82%">
                                                <div class="proportion-wrap">
                                                    <div class="yellow"></div>
                                                </div>
                                            </li>
                                            <li data-text = "客观题得分率" data-num = "72%">
                                                <div class="proportion-wrap">
                                                    <div class="purple"></div>
                                                </div>
                                            </li>
                                            <li data-text = "主观题得分率" data-num = "79%">
                                                <div class="proportion-wrap">
                                                    <div class="blue"></div>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tabs-panel" tabs-group="g1" id="g1-tab3">
                            <div class="left-btn-wrap left-btn-big-wrap">
                                <ul class="ranking-list">
                                    <li class="active">高一(2)班</li>
                                    <li>高一(5)班</li>
                                    <li>高一(1)班</li>
                                    <li>4&nbsp; 高一(3)班</li>
                                    <li>5&nbsp; 高一(4)班</li>
                                </ul>
                            </div>
                            <div class="right-chart-wrap">
                                <div class="right-chart-content">
                                    <div class="box-chart ">
                                        <ul class="circle-made">
                                            <li>
                                                <div class="circle-wrap">
                                                    <div class="box-circle">
                                                        <span>132</span>
                                                    </div>
                                                    <span class="box-circle-title">自主刷题</span>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="circle-wrap">
                                                    <div class="box-circle">
                                                        <span>78</span>
                                                    </div>
                                                    <span class="box-circle-title">错题重做</span>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="circle-wrap">
                                                    <div class="box-circle">
                                                        <span>36</span>
                                                    </div>
                                                    <span class="box-circle-title">错题导出</span>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 教学使用情况 -->
        <div class="box-data-wrap box-data-wrap-big">
            <div class="box-data">
                <div class="box-data-head clearfix">
                    <div class="box-data-name">
                        <span>教学使用情况</span>
                    </div>
                    <div class="box-data-scope clearfix">
                        <div class="btn-group filter">
                        	<button class="btn dropdown-toggle">近一周 <i class="wpfont icon-angle-single-down"></i></button>
                        	<ul class="dropdown-menu">
                        		<li><a href="#">全部</a></li>
                        		<li><a href="#">近一月</a></li>
                        		<li><a href="#">近一周</a></li>
                            </ul>
                        </div>
                        <div class="btn-group filter">
                        	<button class="btn dropdown-toggle">全校 <i class="wpfont icon-angle-single-down"></i></button>
                        	<ul class="dropdown-menu">
                        		<li><a href="#">全校</a></li>
                        		<li><a href="#">全年级</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="box-data-content js-four">
                    <ul class="tabs tabs-card" tabs-group="g2">
                    	<li class="tab active"><a href="#g2-tab1">课堂资源</a></li>
                    	<li class="tab"><a href="#g2-tab2">课堂互动</a></li>
                    	<li class="tab"><a href="#g2-tab3">科目作业</a></li>
                        <li class="tab"><a href="#g2-tab4">备课情况</a></li>
                    </ul>
                    <div class="tabs-panel active" tabs-group="g2" id="g2-tab1">
                    	<div class="box-chart teach1"></div>
                    </div>
                    <div class="tabs-panel" tabs-group="g2" id="g2-tab2">
                    	<div class="box-chart teach2"></div>
                    </div>
                    <div class="tabs-panel" tabs-group="g2" id="g2-tab3">
                        <div class="box-data-content big">
                            <div class="left-btn-wrap">
                                <div class="up-arrow"></div>
                                <ul class="">
                                    <li class="active">语文</li>
                                    <li>数学</li>
                                    <li>英语</li>
                                    <li>科学</li>
                                    <li>物理</li>
                                    <li>化学</li>
                                    <li>生物</li>
                                    <li>地理</li>
                                    <li>政治</li>
                                    <li>历史</li>
                                </ul>
                                <div class="down-arrow"></div>
                            </div>
                            <div class="right-chart-wrap clearfix">
                                <div class="right-chart-head clearfix float-wrap">
                                    <span class="ml-20">作业份数：</span><span class="font-quartz mr-30">89</span>
                                    <span>作业题目数：</span><span class="font-quartz">356</span>
                                </div>
                                <div class="right-chart-content">
                                    <div class="box-chart teach3"></div>
                                    <div class="box-chart teach4"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="tabs-panel" tabs-group="g2" id="g2-tab4">
                    	<div class="box-chart teach5"></div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 教师活跃情况TOP5 -->
        <div class="box-data-wrap box-data-wrap-big">
            <div class="box-data">
                <div class="box-data-head clearfix">
                    <div class="box-data-name">
                        <span>教师活跃情况TOP5</span>
                    </div>
                    <div class="box-data-scope clearfix">
                        <div class="btn-group filter">
                        	<button class="btn dropdown-toggle">近一周 <i class="wpfont icon-angle-single-down"></i></button>
                        	<ul class="dropdown-menu">
                        		<li><a href="#">全部</a></li>
                        		<li><a href="#">近一月</a></li>
                        		<li><a href="#">近一周</a></li>
                            </ul>
                        </div>
                        <div class="btn-group filter">
                        	<button class="btn dropdown-toggle">全校 <i class="wpfont icon-angle-single-down"></i></button>
                        	<ul class="dropdown-menu">
                        		<li><a href="#">全校</a></li>
                        		<li><a href="#">全年级</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="box-data-content js-five">
                    <ul class="tabs tabs-card limit-width" tabs-group="g3">
                    	<li class="tab active"><a href="#g3-tab1">布置作业数</a></li>
                    	<li class="tab"><a href="#g3-tab2">备课数</a></li>
                    	<li class="tab"><a href="#g3-tab3">有效授课数</a></li>
                        <li class="tab"><a href="#g3-tab4">上传资源数</a></li>
                        <li class="tab"><a href="#g3-tab5">校本贡献数</a></li>
                    </ul>
                    <div class="box-data-content">
                        <div class="tabs-panel active" tabs-group="g3" id="g3-tab1">
                            <div class="left-btn-wrap left-btn-big-wrap">
                                <ul class="ranking-list">
                                    <li class="active">张嘉雯</li>
                                    <li>李国军</li>
                                    <li>张雅丽</li>
                                    <li>4&nbsp; 徐庆明</li>
                                    <li>5&nbsp; 李华</li>
                                </ul>
                            </div>
                            <div class="right-chart-wrap">
                                <div class="right-chart-content">
                                    <div class="box-special-effects ">
                                        <div class="center-circle">
                                            <div class="">
                                                <b>136</b>
                                                <div>作业数</div>
                                            </div>
                                        </div>
                                        <div class="circum-box left1" data-before = "主观题" data-after = "78"></div>
                                        <div class="circum-box left3" data-before = "客观题" data-after = "56"></div>
                                        <div class="circum-box right1" data-before = "提交率" data-after = "98%"></div>
                                        <div class="circum-box right3" data-before = "批改率" data-after = "100%"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tabs-panel" tabs-group="g3" id="g3-tab2">
                            <div class="left-btn-wrap left-btn-big-wrap">
                                <ul class="ranking-list">
                                    <li class="active">张嘉雯</li>
                                    <li>李国军</li>
                                    <li>张雅丽</li>
                                    <li>4&nbsp; 徐庆明</li>
                                    <li>5&nbsp; 李华</li>
                                </ul>
                            </div>
                            <div class="right-chart-wrap">
                                <div class="right-chart-content">
                                    <div class="box-special-effects ">
                                        <div class="center-circle">
                                            <div class="">
                                                <b>136</b>
                                                <div>作业数</div>
                                            </div>
                                        </div>
                                        <div class="circum-box left1" data-before = "主观题" data-after = "78"></div>
                                        <div class="circum-box left3" data-before = "客观题" data-after = "56"></div>
                                        <div class="circum-box right1" data-before = "提交率" data-after = "98%"></div>
                                        <div class="circum-box right3" data-before = "批改率" data-after = "100%"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tabs-panel" tabs-group="g3" id="g3-tab3">
                            <div class="left-btn-wrap left-btn-big-wrap">
                                <ul class="ranking-list">
                                    <li class="active">张嘉雯</li>
                                    <li>李国军</li>
                                    <li>张雅丽</li>
                                    <li>4&nbsp; 徐庆明</li>
                                    <li>5&nbsp; 李华</li>
                                </ul>
                            </div>
                            <div class="right-chart-wrap clearfix">
                                <div class="right-chart-head box-data-head clearfix float-wrap">
                                    <span class="ml-20">有效授课：</span><span class="font-quartz">131</span>
                                    <div class="box-data-scope clearfix">
                                        <button type="button" class="btn active"><span>课堂资源</span></button>
                                        <button type="button" class="btn"><span>课堂互动</span></button>
                                    </div>
                                </div>
                                <div class="right-chart-content">
                                    <div class="box-chart teacher"></div>
                                </div>
                            </div>
                        </div>
                        <div class="tabs-panel" tabs-group="g3" id="g3-tab4">
                            <div class="left-btn-wrap left-btn-big-wrap">
                                <ul class="ranking-list">
                                    <li class="active">张嘉雯</li>
                                    <li>李国军</li>
                                    <li>张雅丽</li>
                                    <li>4&nbsp; 徐庆明</li>
                                    <li>5&nbsp; 李华</li>
                                </ul>
                            </div>
                            <div class="right-chart-wrap">
                                <div class="right-chart-content">
                                    <div class="box-special-effects ">
                                        <div class="center-circle">
                                            <div class="">
                                                <b>136</b>
                                            </div>
                                        </div>
                                        <div class="circum-box left1" data-before = "PPT" data-after = "123"></div>
                                        <div class="circum-box left2" data-before = "图片" data-after = "78"></div>
                                        <div class="circum-box left3" data-before = "音频" data-after = "11"></div>
                                        <div class="circum-box right1" data-before = "文档" data-after = "23"></div>
                                        <div class="circum-box right2" data-before = "视频" data-after = "13"></div>
                                        <div class="circum-box right3" data-before = "题集数" data-after = "263"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tabs-panel" tabs-group="g3" id="g3-tab5">
                            <div class="left-btn-wrap left-btn-big-wrap">
                                <ul class="ranking-list">
                                    <li class="active">张嘉雯</li>
                                    <li>李国军</li>
                                    <li>张雅丽</li>
                                    <li>4&nbsp; 徐庆明</li>
                                    <li>5&nbsp; 李华</li>
                                </ul>
                            </div>
                            <div class="right-chart-wrap">
                                <div class="right-chart-content">
                                    <div class="box-special-effects ">
                                        <div class="center-circle">
                                            <div class="">
                                                <b>136</b>
                                            </div>
                                        </div>
                                        <div class="circum-box left1" data-before = "PPT" data-after = "123"></div>
                                        <div class="circum-box left2" data-before = "图片" data-after = "78"></div>
                                        <div class="circum-box left3" data-before = "音频" data-after = "11"></div>
                                        <div class="circum-box right1" data-before = "文档" data-after = "23"></div>
                                        <div class="circum-box right2" data-before = "视频" data-after = "13"></div>
                                        <div class="circum-box right3" data-before = "题集数" data-after = "263"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
	</div>
    <!--主体 E-->
	
	<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/echarts.all.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/jquery-tabs.js" type="text/javascript" charset="utf-8"></script>
	<script src="${request.contextPath}/bigdata/v3/static/datav/custom/js/myscript.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
       $(function () {
           //图表
           var arr=[];
           function resizeChart(){
               for(var i = 0;i < arr.length;i ++){
                   arr[i].resize()
               }
           }

           // 资源建设情况
           var chartOne = echarts.init($('.partOne')[0]);
           arr.push(chartOne);
           var opOne1 = {
               color: ['#ef913a', '#00cce4', '#9a48d8','#d242a4','#1f83f5'],
               legend: {
                   x : 'right',
                   y : '10',
                   data:['','','','个人备课数','校本备课数'],
                   orient: 'vertical',
                   textStyle:{
                       color: '#fff'
                   }
               },
               calculable : true,
               series : [
                   {
                       name:'半径模式',
                       type:'pie',
                       radius : [20, 100],
                       center : ['50%', '50%'],
                       roseType : 'radius',
                       label: {
                           normal: {
                               show: false
                           },
                           emphasis: {
                               show: false
                           }
                       },
                       lableLine: {
                           normal: {
                               show: false
                           },
                           emphasis: {
                               show: true
                           }
                       },
                       data:[
                           {value:15, name:''},
                           {value:10, name:''},
                           {value:17, name:''},
                           {value:22, name:'个人备课数'},
                           {value:27, name:'校本备课数'}
                       ]
                   }
               ]
           };
           var opOne2 = {
               color: ['#ef913a', '#00cce4', '#9a48d8','#d242a4','#1f83f5'],
               legend: {
                   x : 'right',
                   y : '10',
                   data:['','','','个人素材库','学校素材库'],
                   orient: 'vertical',
                   textStyle:{
                       color: '#fff'
                   }
               },
               calculable : true,
               series : [
                   {
                       name:'半径模式',
                       type:'pie',
                       radius : [20, 100],
                       center : ['50%', '50%'],
                       roseType : 'radius',
                       label: {
                           normal: {
                               show: false
                           },
                           emphasis: {
                               show: false
                           }
                       },
                       lableLine: {
                           normal: {
                               show: false
                           },
                           emphasis: {
                               show: true
                           }
                       },
                       data:[
                           {value:6, name:''},
                           {value:3, name:''},
                           {value:15, name:''},
                           {value:10, name:'个人素材库'},
                           {value:9, name:'学校素材库'}
                       ]
                   }
               ]
           };
           var opOne3 = {
               color: ['#ef913a', '#00cce4', '#9a48d8','#d242a4','#1f83f5'],
               legend: {
                   x : 'right',
                   y : '10',
                   data:['','','','个人素材','校本素材'],
                   orient: 'vertical',
                   textStyle:{
                       color: '#fff'
                   }
               },
               calculable : true,
               series : [
                   {
                       name:'半径模式',
                       type:'pie',
                       radius : [20, 100],
                       center : ['50%', '50%'],
                       roseType : 'radius',
                       label: {
                           normal: {
                               show: false
                           },
                           emphasis: {
                               show: false
                           }
                       },
                       lableLine: {
                           normal: {
                               show: false
                           },
                           emphasis: {
                               show: true
                           }
                       },
                       data:[
                           {value:22, name:''},
                           {value:6, name:''},
                           {value:17, name:''},
                           {value:10, name:'个人素材'},
                           {value:20, name:'校本素材'}
                       ]
                   }
               ]
           };
           chartOne.setOption(opOne1);
           var timeNum = 0;
           var timer = setInterval(slide,5000);
           $('.js-one .left-btn-wrap li').on('click',function(){
               clearInterval(timer);
               $('.js-one').find('.font-quartz').text($(this).data('num'));
               eval('chartOne.setOption(' + $(this).data('option') + ')');
               chartOne.resize();
               timeNum = $(this).index();
               timer = setInterval(slide,5000);
           });
           function slide(){
               timeNum ++;
               if (timeNum == $('.js-one li').length){
                   timeNum = 0
               }
               var $target = $('.js-one li').eq(timeNum);
               $target.addClass('active').siblings().removeClass('active');
               $('.js-one .font-quartz').text($target.data('num'));
               eval('chartOne.setOption(' + $target.data('option') + ')');
               chartOne.resize();
           }

           //数据总览
           $('.js-data-pandect .btn').on('click',function(){
               $(this).closest('.box-data-head').next('.box-data-content').find('.no-chart-wrap').eq($(this).index()).removeClass('hide').siblings().addClass('hide');
               fontEffects();
           });
           fontEffects();
           function fontEffects(){
               $.fn.countTo = function (options) {
                   options = options || {};
                   return $(this).each(function () {
                       // set options for current element
                       var settings = $.extend({}, $.fn.countTo.defaults, {
                           from:            $(this).data('from'),
                           to:              $(this).data('to'),
                           speed:           $(this).data('speed'),
                           refreshInterval: $(this).data('refresh-interval'),
                           decimals:        $(this).data('decimals')
                       }, options);

                       // how many times to update the value, and how much to increment the value on each update
                       var loops = Math.ceil(settings.speed / settings.refreshInterval),
                           increment = (settings.to - settings.from) / loops;

                       // references & variables that will change with each update
                       var self = this,
                           $self = $(this),
                           loopCount = 0,
                           value = settings.from,
                           data = $self.data('countTo') || {};

                       $self.data('countTo', data);

                       // if an existing interval can be found, clear it first
                       if (data.interval) {
                           clearInterval(data.interval);
                       }
                       data.interval = setInterval(updateTimer, settings.refreshInterval);

                       // initialize the element with the starting value
                       render(value);

                       function updateTimer() {
                           value += increment;
                           loopCount++;

                           render(value);

                           if (typeof(settings.onUpdate) == 'function') {
                               settings.onUpdate.call(self, value);
                           }

                           if (loopCount >= loops) {
                               // remove the interval
                               $self.removeData('countTo');
                               clearInterval(data.interval);
                               value = settings.to;

                               if (typeof(settings.onComplete) == 'function') {
                                   settings.onComplete.call(self, value);
                               }
                           }
                       }

                       function render(value) {
                           //value=toValue;
                           var formattedValue = settings.formatter.call(self, value, settings);
                           $self.html(formattedValue);
                       }

                   });
               };

               $.fn.countTo.defaults = {
                   from: 0,               // the number the element should start at
                   to: 0,                 // the number the element should end at
                   speed: 1000,           // how long it should take to count between the target numbers
                   refreshInterval: 100,  // how often the element should be updated
                   decimals: 0,           // the number of decimal places to show
                   formatter: formatter,  // handler for formatting the value before rendering
                   onUpdate: null,        // callback method for every time the element is updated
                   onComplete: null       // callback method for when the element finishes updating
               };

               function formatter(value, settings) {
                   return value.toFixed(settings.decimals);
               }

               // custom formatting example
               $('.js-two .font-quartz').data('countToOptions', {
                   formatter: function (value, options) {
                       return value.toFixed(options.decimals).replace(/\B(?=(?:\d{3})+(?!\d))/g, '');
                   }
               });

               // start all the timers
               $('.js-two .font-quartz').each(count);

               function count(options) {
                   var $this = $(this);
                   options = $.extend({}, options || {}, $this.data('countToOptions') || {});
                   $this.countTo(options);
               }
           }

           //班级活跃情况
           var myChartThree = echarts.init($('.partThree')[0]);
           arr.push(myChartThree);
           var opThree = {
               color: ['#1f83f5','#9a48d8','#d242a4','#ef913a','#b8396c','#00cce4'],
               legend: {
                   data:['预习数','课后数','复习数','阅读数','',''],
                   itemWidth: 13,
                   itemHeight: 13,
                   textStyle:{
                       color: '#fff',
                   }
               },
               grid: {
                   left: '3%',
                   right: '4%',
                   bottom: '3%',
                   containLabel: true
               },
               xAxis : {
                   type : 'category',
                   data : ['语文', '数学', '英语', '物理', '化学', '生物', '地理','政治','历史'],
                   axisLine: {
                       show: false
                   },
                   axisTick: {
                       show: false
                   },
                   axisLabel: {
                       vertical: 0,
                       textStyle: {
                           color: '#5f90bf'
                       }
                   },
                   splitLine:{
                       show:false
                   }
               },
               yAxis : {
                   type : 'value',
                   axisLine: {
                       show: false
                   },
                   axisTick: {
                       show: false
                   },
                   axisLabel: {
                       textStyle: {
                           color: '#5f90bf'
                       }
                   },
                   splitLine:{
                       show:false
                   }
               },
               series : [
                   {
                       name:'预习数',
                       type:'bar',
                       barWidth : 10,
                       stack: '广告',
                       data:[150, 140, 141, 153, 140, 140, 154, 130, 100]
                   },
                   {
                       name:'课后数',
                       type:'bar',
                       stack: '广告',
                       data:[130, 121, 112, 91, 134, 110, 122, 121, 91]
                   },
                   {
                       name:'复习数',
                       type:'bar',
                       stack: '广告',
                       data:[100, 103, 101, 54, 80, 78, 130, 90, 100]
                   },
                   {
                       name:'阅读数',
                       type:'bar',
                       stack: '广告',
                       data:[60, 62, 72, 74, 60, 62, 74, 70, 60]
                   },
                   {
                       name:'',
                       type:'bar',
                       stack: '广告',
                       data:[41, 64, 50, 48, 60, 42, 54, 60, 48]
                   },
                   {
                       name:'',
                       type:'bar',
                       stack: '广告',
                       data:[32, 21, 34, 10, 22, 11, 30, 12, 21]
                   }
               ]
           };
           myChartThree.setOption(opThree);
           $('.js-three li.tab').on('click',function(){
               if ($(this).index() == 1){
                   proportion()

               } else if($(this).index() == 2){
                   circle();
               }
           });
           function proportion(){
               $('.proportion-made .proportion-wrap div').each(function(){
                   var w = $(this).closest('li').attr('data-num');
                   $(this).css({
                       width: w
                   })
               });
           }
           function circle(){
               $('.box-circle').each(function(){
                   var w = $(this).width();
                   $(this).css({
                       height: w
                   })
               });
           }

           //教学使用情况
           var teachOne = echarts.init($('.teach1')[0]);
           var teachTwo = echarts.init($('.teach2')[0]);
           var teachThree = echarts.init($('.teach3')[0]);
           var teachFour = echarts.init($('.teach4')[0]);
           var teachFive = echarts.init($('.teach5')[0]);
           arr.push(teachOne,teachTwo,teachThree,teachFour,teachFive);
           $('.js-four .tab').each(function(index,ele){
               $(this).click(function(){
                   if (index == 0) {
                       teach1();
                       teachOne.resize();

                   } else if(index == 1){
                       teach2();
                       teachTwo.resize();

                   } else if(index == 2){
                       teach3();
                       teachThree.resize();
                       teachFour.resize();
                       btnOverstep();

                   } else{
                       teach4();
                       teachFive.resize();
                   }
               })
           });
           //左侧按钮是否超出
           function btnOverstep(){
               var wrapH = $('.js-four .left-btn-wrap').removeClass('overstep').height();
               var contentH = $('.js-four .left-btn-wrap ul').height();
               if (wrapH < contentH){
                   $('.js-four .left-btn-wrap').addClass('overstep');
               } else {
                   $('.js-four .left-btn-wrap').removeClass('overstep')
               }
           }
           var i = 0;
           //向下
           $('body').on('click','.down-arrow',function(){
               var h = $('.js-four .overstep ul').height() - $('.js-four .overstep').height();
               if (h <= 38){
                   $(this).siblings('ul').css({
                       transform: 'translateY(-'+ h +'px )'
                   })
               } else{
                   i ++;
                   if ((h - 38*i) > 0){
                       $(this).siblings('ul').css({
                           transform: 'translateY(-'+ 38 * i +'px)'
                       })
                   } else {
                       i --;
                       $(this).siblings('ul').css({
                           transform: 'translateY(-'+ h +'px)'
                       })
                   }
               }
           });
           //向上
           $('body').on('click','.up-arrow',function(){
               var h = parseFloat($('.js-four .overstep ul').css('transform').split(',')[5].slice(0,-1));
               if (h){
                   if (h >= -38){
                       i = 0;
                       $(this).siblings('ul').css({
                           transform: 'translateY(0)'
                       })
                   }else {
                       i --;
                       $(this).siblings('ul').css({
                           transform: 'translateY('+ (h + 38) +'px)'
                       })
                   }
               }
           });
           function teach1(){
               var dataAxis = ['PPT', '图片', '文档', '音频', '视频', '讲解', '检测', '白板'];
               var data = [350, 460, 560, 350, 400, 460, 250, 420];
               option = {
                   xAxis: {
                       data: dataAxis,
                       axisLabel: {
                           textStyle: {
                               color: '#fff'
                           }
                       },
                       axisTick: {show: false},
                       axisLine: {show: false},
                       z: 10
                   },
                   yAxis: {
                       axisLine: {show: false},
                       axisTick: {show: false},
                       axisLabel: {
                           textStyle: {
                               color: '#fff'
                           }
                       },
                       min: 100,
                       splitLine:{show:false}
                   },
                   grid:{
                       x:40,
                       y:30,
                       x2:0,
                       y2:30,
                   },
                   series: [
                       {
                           type: 'bar',
                           barWidth : 30,
                           itemStyle: {
                               normal: {
                                   color: new echarts.graphic.LinearGradient(
                                       0, 0, 0, 1,
                                       [
                                           {offset: 0, color: '#70c7f2'},
                                           {offset: 0.5, color: '#0290d7'},
                                           {offset: 1, color: '#0290d7'}
                                       ]
                                   )
                               },
                               emphasis: {
                                   color: new echarts.graphic.LinearGradient(
                                       0, 0, 0, 1,
                                       [
                                           {offset: 0, color: '#2378f7'},
                                           {offset: 0.7, color: '#2378f7'},
                                           {offset: 1, color: '#83bff6'}
                                       ]
                                   )
                               }
                           },
                           data: data
                       }
                   ]
               };
               teachOne.setOption(option)
           }
           teach1();
           function teach2(){
               var dataAxis = ['语文', '数学', '英语', '科学', '物理', '化学', '生物', '地理','政治'];
               var data = [350, 460, 560, 200, 350, 400, 460, 250, 420];
               option = {
                   tooltip: {
                       trigger: 'axis',
                       axisPointer: {
                           type: 'none'
                       },
                       formatter: function (params) {
                           return params[0].name + ': ' + params[0].value;
                       }
                   },
                   grid:{
                       x:60,
                       y:30,
                       x2:0,
                       y2:30,
                   },
                   xAxis: {
                       data: ['随机选人', '抢答', '批注', '分组', '反馈' ],
                       axisTick: {show: false},
                       axisLine: {show: false},
                       axisLabel: {
                           textStyle: {
                               color: '#5f90bf'
                           }
                       }
                   },
                   yAxis: {
                       splitLine: {show: false},
                       axisTick: {show: false},
                       axisLine: {show: false},
                       axisLabel: {show: false}
                   },
                   color: ['#e54035'],
                   series: [{
                       name: 'hill',
                       type: 'pictorialBar',
                       barCategoryGap: '-130%',
                       symbol: 'path://M0,10 L10,10 C5.5,10 5.5,5 5,0 C4.5,5 4.5,10 0,10 z',
                       itemStyle: {
                           normal: {
                               opacity: 0.5,
                               color: '#075182',
                           },
                           emphasis: {
                               opacity: 1,
                               color: '#018fd7',
                           }
                       },
                       data: [263, 70, 35, 18, 12],
                       z: 10
                   }]
               };
               teachTwo.setOption(option);
           }
           function teach3(){
               var dataAxis = ['预习', '课后', '复习', '阅读', '小测', '答题卡'];
               var data = [350, 160, 260, 200, 350, 400];
               option = {
                   title: {
                       text: '作业次数统计',
                       left: 15,
                       top: 5,
                       textStyle: {
                           color: '#fff',
                           fontSize: 14,
                           fontWeight: 'bold'
                       }
                   },
                   xAxis: {
                       data: dataAxis,
                       axisLabel: {
                           textStyle: {
                               color: '#5f90bf'
                           }
                       },
                       axisTick: { show: false},
                       axisLine: {show: false},
                       z: 10
                   },
                   yAxis: {
                       axisLine: {show: false},
                       axisTick: {show: false},
                       axisLabel: {
                           textStyle: {
                               color: '#5f90bf'
                           }
                       },
                       min: 100,
                       splitLine:{show:false}
                   },
                   grid:{
                       x:50,
                       y:50,
                       x2:0,
                       y2:30,
                   },
                   series: [
                       {
                           type: 'bar',
                           barWidth : 30,
                           itemStyle: {
                               normal: {
                                   color: new echarts.graphic.LinearGradient(
                                       0, 0, 0, 1,
                                       [
                                           {offset: 0, color: '#70c7f2'},
                                           {offset: 0.5, color: '#0290d7'},
                                           {offset: 1, color: '#0290d7'}
                                       ]
                                   )
                               },
                               emphasis: {
                                   color: new echarts.graphic.LinearGradient(
                                       0, 0, 0, 1,
                                       [
                                           {offset: 0, color: '#2378f7'},
                                           {offset: 0.7, color: '#2378f7'},
                                           {offset: 1, color: '#83bff6'}
                                       ]
                                   )
                               }
                           },
                           data: data
                       }
                   ]
               };
               teachThree.setOption(option);

               option2 = {
                   color: ['#00cce4','#9a48d8','#ef913a'],
                   title: {
                       text: '题目类型统计',
                       left: 15,
                       top: 5,
                       textStyle: {
                           color: '#fff',
                           fontSize: 14,
                           fontWeight: 'bold'
                       }
                   },
                   // tooltip: {
                   //     trigger: 'item',
                   //     formatter: "{a} <br/>{b}: {c} ({d}%)"
                   // },
                   legend: {
                       orient: 'horizontal',
                       x: 'center',
                       y: 'bottom',
                       data:['单选','多选','判断'],
                       textStyle:{
                           color: '#fff',
                       }
                   },
                   series: [
                       {
                           name:'访问来源',
                           type:'pie',
                           radius: ['40%', '60%'],
                           avoidLabelOverlap: false,
                           label: {
                               normal: {
                                   show: false,
                                   position: 'center'
                               },
                               emphasis: {
                                   show: true,
                                   textStyle: {
                                       fontSize: '30',
                                       fontWeight: 'bold'
                                   }
                               }
                           },
                           labelLine: {
                               normal: {
                                   show: false
                               }
                           },
                           data:[
                               {value:135, name:'单选'},
                               {value:110, name:'多选'},
                               {value:334, name:'判断'}
                           ]
                       }
                   ]
               };

               teachFour.setOption(option2);

           }
           function teach4(){
               var dataAxis = ['语文', '数学', '英语', '科学', '物理', '化学', '生物', '地理','政治'];
               var data = [350, 460, 560, 200, 350, 400, 460, 250, 420];
               option = {
                   xAxis: {
                       data: dataAxis,
                       axisLabel: {
                           textStyle: {
                               color: '#fff'
                           }
                       },
                       axisTick: {
                           show: false
                       },
                       axisLine: {
                           show: false
                       },
                       z: 10
                   },
                   yAxis: {
                       axisLine: {
                           show: false
                       },
                       axisTick: {
                           show: false
                       },
                       axisLabel: {
                           textStyle: {
                               color: '#fff'
                           }
                       },
                       min: 100,
                       splitLine:{
                           show:false
                       }

                   },
                   grid:{
                       x:40,
                       y:30,
                       x2:0,
                       y2:30,
                   },
                   series: [
                       {
                           type: 'bar',
                           barWidth : 30,
                           itemStyle: {
                               normal: {
                                   color: new echarts.graphic.LinearGradient(
                                       0, 0, 0, 1,
                                       [
                                           {offset: 0, color: '#70c7f2'},
                                           {offset: 0.5, color: '#0290d7'},
                                           {offset: 1, color: '#0290d7'}
                                       ]
                                   )
                               },
                               emphasis: {
                                   color: new echarts.graphic.LinearGradient(
                                       0, 0, 0, 1,
                                       [
                                           {offset: 0, color: '#2378f7'},
                                           {offset: 0.7, color: '#2378f7'},
                                           {offset: 1, color: '#83bff6'}
                                       ]
                                   )
                               }
                           },
                           data: data
                       }
                   ]
               };

               teachFive.setOption(option)
           }

           //教师活跃情况
           var teacher = echarts.init($('.teacher')[0]);
           arr.push(teacher);
           $('.js-five .tab').each(function(index,ele){
               $(this).click(function(){
                   if (index == 0) {

                   } else if(index == 1){

                   } else if(index == 2){
                       five();
                       teacher.resize();

                   } else if(index == 3){

                   } else{

                   }
               })
           });

           function five(){
               var dataAxis = ['PPT', '图片', '文档', '音频', '视频', '作业讲解', '答题卡', '课堂检测', '白板'];
               var data = [370, 260, 310, 220, 250, 170, 160, 150, 170];
               option = {
                   title: {
                       text: '课堂使用资源次数',
                       left: 15,
                       top: 5,
                       textStyle: {
                           color: '#fff',
                           fontSize: 14,
                           fontWeight: 'bold'
                       }
                   },
                   xAxis: {
                       data: dataAxis,
                       axisLabel: {
                           interval: 0,
                           textStyle: {
                               color: '#5f90bf'
                           }
                       },
                       axisTick: { show: false},
                       axisLine: {show: true},
                       z: 10
                   },
                   yAxis: {
                       axisLine: {show: false},
                       axisTick: {show: false},
                       axisLabel: {
                           textStyle: {
                               color: '#5f90bf'
                           }
                       },
                       min: 100,
                       splitLine:{show:false}
                   },
                   grid:{
                       x:50,
                       y:50,
                       x2:0,
                       y2:30,
                   },
                   series: [
                       {
                           type: 'bar',
                           barWidth : 40,
                           itemStyle: {
                               normal: {
                                   color: new echarts.graphic.LinearGradient(
                                       0, 0, 0, 1,
                                       [
                                           {offset: 0, color: '#70c7f2'},
                                           {offset: 0.5, color: '#0290d7'},
                                           {offset: 1, color: '#0290d7'}
                                       ]
                                   )
                               },
                               emphasis: {
                                   color: new echarts.graphic.LinearGradient(
                                       0, 0, 0, 1,
                                       [
                                           {offset: 0, color: '#2378f7'},
                                           {offset: 0.7, color: '#2378f7'},
                                           {offset: 1, color: '#83bff6'}
                                       ]
                                   )
                               }
                           },
                           data: data
                       }
                   ]
               };
               teacher.setOption(option);
           }

           //窗口变化，图表resize
           $(window).resize(function(){
               circle();
               resizeChart();
           })
       })
    </script>
</body>
</html>