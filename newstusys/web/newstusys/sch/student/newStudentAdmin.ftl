<div id="studentAdminDiv">
            <a id="backA" onclick="goBack();" style="display: none" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
            <div class="box box-default">
                <div class="box-body" id="studentContent">
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
                                <select name="" id="field" class="form-control" name="field" onChange="searchList()" style="width:168px;" >
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
                                <a class="btn btn-blue hide" onclick="htmlShow();" >html显示</a>
								<a class="btn btn-blue" onclick="pdfExport();" >导出PDF</a>
                            </div>
                        </div>
                        <div class="filter-item filter-item-right">
                            <div class="filter-content">
                                <span class="color-blue">导入学生时，新增学生信息只能通过"导入"操作！</span>
                                <a class="btn btn-blue" onclick="addNewStudent();" >新增</a>
                                <a class="btn btn-white" onclick="toImport();" title="新增或更新学生信息">导入</a>
                                <a class="btn btn-white" onclick="toUpImport();" title="仅更新学生信息">更新导入</a>
                                <a class="btn btn-white" onclick="doExport();" >导出</a>
                                <a class="btn btn-white" onclick="bak();" title="${bakMsg!}">存档</a>
                                <a class="btn btn-danger">删除</a>
                            </div>
                        </div>
                    </div>
                    <div id="showList" >
                    </div>
                </div>
            </div>
        </div><!-- /.page-content -->
</div><!-- /.main-container -->
<iframe style="display:none;" id="hiddenFrame" name="hiddenFrame"></iframe>
<script src="${request.contextPath}/static/js/login/jquery.cookies.js"></script>
<script type="text/javascript" >
    function addNewStudent(){
        var classId = $('#classIdSearch').val();
        $("#showList").load("${request.contextPath}/newstusys/sch/student/newStudent?classId="+classId);
    }
    
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


    function toImport(){
        var iurl = "${request.contextPath}/newstusys/sch/student/studentImport/index";
        $("#studentAdminDiv").load(iurl);
    }

	function toUpImport(){
		var iurl = "${request.contextPath}/newstusys/sch/student/studentImport/index?update=1";
        $("#studentAdminDiv").load(iurl);
	}

    function　searchList(){
        var classId = $('#classIdSearch').val();
//        var studentName = $.trim($('#studentName').val());
//        if(studentName != ''){
//            if(studentName.indexOf('\'')>-1||studentName.indexOf('%')>-1){
//                layerTipMsgWarn("请确认欲查询的学生姓名不包含单引号、百分号等特殊符号！");
//                return ;
//            }
//        }
        var field = $("#field").val();
        var keyWord = $("#keyWord").val();
        if(keyWord != ''){
            if(keyWord.indexOf('\'')>-1||keyWord.indexOf('%')>-1){
                layerTipMsgWarn("请确认欲查询的关键字不包含单引号、百分号等特殊符号！");
                return ;
            }
        }
        var url="${request.contextPath}/newstusys/sch/student/studentList?&classId="+classId+ "&field="+field+"&keyWord="+keyWord;
        $("#showList").load(encodeURI(url));
    }

    function goBack(){
        $("#backA").hide();
        $("#searchDiv").show();
        searchList();
    }
    
    //删除
    var delRemark = '确定删除吗？<br>';
    //批量删除
    $('.btn-danger').click(function(){
        var selEle = $('#list :checkbox:checked');
        layer.confirm(delRemark, {
            btn: ['确定', '取消'],
            yes: function(index){
                if(selEle.length<1){
                    layerTipMsg(false,"失败",'请先选中学生再删除');
                    layer.close(index);
                    return;
                }
                var param = new Array();
                for(var i=0;i<selEle.length;i++){
                    //alert(selEle.eq(i).val());
                    param.push(selEle.eq(i).val());
                }
                deleteByIds(param);

                layer.close(index);
            }
        });

    });
    //批量删除
    function deleteByIds(idArray){
        var url = '${request.contextPath}/newstusys/sch/student/deleteStudent?classId=${classId!}&studentId=${studentId!}';
        var params = {"ids":idArray};
        $.ajax({
            type: "POST",
            url: url,
            data: params,
            success: function(msg){
                //alert( "Data Saved: " + msg );
                if(msg.success){
                    searchList();
                }else{
                    layerTipMsg(false,"失败",msg.msg);
                }
            },
            dataType: "JSON"
        });
    }
    
	function  doStudentDelete(id){
        layer.confirm(delRemark, {
            btn: ['确定', '取消'],
            yes: function(index){

                var param = new Array();
                param.push(id);

                deleteByIds(param);

                layer.close(index);
            }
        });
	}
    function doExport(){
        var classId = $('#classIdSearch').val();
        if(classId == ""){
            layerTipMsgWarn("提示","请选择一个班级！");
            return;
        }
        var url="${request.contextPath}/newstusys/sch/student/studentExport?classId="+classId;
        document.location.href = url;
    }
    
    function htmlShow(){
    	var classId = $('#classIdSearch').val();
        if(classId == ""){
            layerTipMsgWarn("提示","请选择一个班级！");
            return;
        }
        var url="${request.contextPath}/newstusys/common/pdfHtml?clsId="+classId;
        window.open(url);
    }
    
    function pdfExport(){
    	var classId = $('#classIdSearch').val();
        if(classId == ""){
            layerTipMsgWarn("提示","请选择一个班级！");
            return;
        }
        var ii = layer.load();
		var downId=new Date().getTime();//以时间戳来区分每次下载
        var url="${request.contextPath}/newstusys/sch/student/pdfExport?clsId="+classId+"&downId="+downId;
        document.getElementById('hiddenFrame').src=url;
		        
		var interval=setInterval(function(){
			var down=$.cookies.get("D"+downId);
			if(down==downId){
				layer.close(ii);
				$.cookies.del("D"+downId);
			}
		},1000);
    }
    
    var hasbak=false;
    function bak(){
    	layer.confirm("学生数据每学期存档一次，当前学期的如果已经存档过，则会覆盖重新存档。确定要存档数据吗？", {
            btn: ['确定', '取消'],
            yes: function(index){

                if(hasbak){
		    		return;
		    	}
		    	hasbak=true;
		    	var url = '${request.contextPath}/newstusys/bak/schBakSave';
		        var params = {};
		        $.ajax({
		            type: "POST",
		            url: url,
		            data: params,
		            success: function(msg){
		                if(msg.success){
		                    layer.msg(msg.msg, {
								offset: 't',
								time: 2000
							});
		                }else{
		                    layerTipMsg(false,"失败",msg.msg);
		                }
		            },
		            dataType: "JSON"
		        });
		        hasbak=false;
                layer.close(index);
            }
        });
    }
    
$(function(){
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
	});	
});    
</script>