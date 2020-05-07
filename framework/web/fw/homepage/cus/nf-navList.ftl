<#import "/fw/macro/webmacro.ftl" as webmacro>

<!---->
<li class="">
	<a href="#${request.contextPath}/basedata/dept/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【部门管理】 </span>
	</a> 
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/basedata/teacher/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【教师管理】 </span>
	</a> 
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/basedata/school/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【学校信息设置】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/basedata/grade/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【年级班级管理】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/basedata/student/index/page" >
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【学生管理】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/basedata/teachclass/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【教学班】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/scoremanage/examInfo/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【考试管理】 </span>
	</a>
	<b class="arrow"></b>
</li> 
<li class="">
	<a href="#${request.contextPath}/basedata/course/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【科目管理】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/basedata/classTeaching/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【任课信息管理】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/scoremanage/courseInfo/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【考试科目设置】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/scoremanage/borderline/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【分数线设置】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/scoremanage/scoreInfo/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【成绩录入】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/scoremanage/filter/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【考号设置】 </span>
	</a>
	<b class="arrow"></b>
</li>
<li class="">
	<a href="#${request.contextPath}/scoremanage/scoreStatistic/index/page">
		<i class="menu-icon fa fa-asterisk"></i>
		<span class="menu-text"> 【统计图】 </span>
	</a>
	<b class="arrow"></b>
</li>
<#list subSystemDtos as subsystemDto>
	<@webmacro.putSubSystem subsystemDto />
</#list>
