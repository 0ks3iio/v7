<div id="studentAdminDiv">
            <#--<a id="backA" onclick="goBack();" style="display: none" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
    <#if tabType?default('') != '3'>
    <div class="filter" id="searchDiv">
        <div class="filter-item">
            <span class="filter-name">班级：</span>
            <div class="filter-content">
                <select name="" id="classIdSearch" class="form-control" id="classIdSearch" onChange="searchList()" style="width:168px;" >
                <#if classList?exists && (classList?size>0)>
                    <option value="">---请选择---</option>
                    <#list classList as item>
                        <option value="${item.id!}" <#if item.id == classId?default('') > selected</#if>>${item.classNameDynamic!}</option>
                    </#list>
                <#else>
                    <option value="">---请选择---</option>
                </#if>
                </select>
            </div>
        </div>

        <div class="filter-item">
            <span class="filter-name">字段：</span>
            <div class="filter-content">
                <select name="" id="field" class="form-control" name="field"  style="width:168px;" >
                    <option value="">---请选择---</option>
                    <option value="studentName">学生姓名</option>
                    <option value="51_realName">父亲姓名</option>
                    <option value="52_realName">母亲姓名</option>
                    <option value="51_company">父亲工作单位</option>
                    <option value="52_company">母亲工作单位</option>
                    <option value="oldSchoolName">原毕业学校</option>
                    <option value="homeAddress">家庭地址</option>
                </select>
            </div>
        </div>
        <div class="filter-item">
            <span class="filter-name">关键字：</span>
            <div class="filter-content">
                <input type="text" id="keyWord" style="width:168px;" class="form-control"  >
            </div>
        </div>
        <div class="filter-item filter-item-left">
            <div class="filter-content">
                <a class="btn btn-blue" onclick="searchList();" >查找</a>
            </div>
        </div>
    </div>
    </#if>
    <div id="showList" >
    </div>
</div>
<script type="text/javascript" >
<#if tabType?default('') != '3'>
    $(function(){
        //初始化单选控件
        var classIdSearch = $('#classIdSearch');
        $(classIdSearch).chosen({
            width:'130px',
            no_results_text:"未找到",//无搜索结果时显示的文本
            allow_single_deselect:true,//是否允许取消选择
            disable_search:false, //是否有搜索框出现
            search_contains:true,//模糊匹配，false是默认从第一个匹配
            //max_selected_options:1 //当select为多选时，最多选择个数
        });
    });
    
    function　searchList(){
        var classId = $('#classIdSearch').val();

        var tabIndex = $("#roleStuShow").find("li.active").attr("val");
        var field = $("#field").val() ;
        var keyWord = $("#keyWord").val();

        var url="${request.contextPath}/newstusys/sch/student/studentShowList.action?classId="+classId+"&tabType=" + tabIndex + "&field="+field+"&keyWord="+keyWord;
        $("#showList").load(encodeURI(url));
    }

    function goBack(){
        $("#backA").hide();
        $("#searchDiv").show();
        searchList();
    }
<#else>
	searchList();
	
	function　searchList(){
		var tabIndex = $("#roleStuShow").find("li.active").attr("val");
        var url="${request.contextPath}/newstusys/sch/student/tutorStudentShowList.action?tabType="+tabIndex;
        $("#showList").load(encodeURI(url));
    }
</#if>
</script>