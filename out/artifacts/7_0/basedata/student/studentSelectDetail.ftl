<form id="myform">
    <table class="table table-striped table-bordered table-hover no-margin mainTable tablesorter" style="font-size:1em;">
        <thead>
        <tr>
            <th width="4%">序号</th>
            <th width="6%">姓名</th>
            <th width="10%">学号</th>
            <th width="8%">性别</th>
            <th width="8%">行政班</th>
   			<#if courseList?? && courseList?size gt 0>
                <#list courseList as item>
   			        <th>${item.subjectName!}</th>
                </#list>
            </#if>
            <th width="13%">操作</th>
        </tr>
        </thead>
        <tbody id="selectSubjectList">
	<#if selectList?? && (selectList?size>0)>
		<#list selectList as item>
		<tr>
            <td>${item_index+1}</td>
            <td class="studentSelectName">${item.studentName!}</td>
            <td class="studentSelectCode">${item.studentCode!}</td>
            <td>${mcodeSetting.getMcode("DM-XB","${item.sex!}")}</td>
            <td>${item.className!}</td>
			<#if courseList?? && courseList?size gt 0>
                <#list courseList as course>
                    <td>
                        <#if (item.selectList)?exists && (item.selectList)?size gt 0>
				        <#list item.selectList as result>
					        <#if course.id==result>
					            <span><i class="fa fa-circle font-12 color-green"></i> ${courseNameMap[result]!}<#if item.subjectScore?exists && item.subjectScore[result.subjectId]?exists>(${item.subjectScore[result.subjectId]?string("##.##")})</#if></span>
                            </#if>
                        </#list>
                        </#if>
                    </td>
                </#list>
            </#if>
			<#if scourceType?default('') != '9'>
			<td>
                <#--<a href="javascript:;" class="table-btn color-green js-toggleLock unlock_${item.studentId!}" onclick="lockResult('${item.studentId!}','0')" <#if item.lock?default('0')=='0'>style="display:none"</#if>>解锁</a>
                <a href="javascript:;" class="table-btn color-green js-toggleLock lock_${item.studentId!}" onclick="lockResult('${item.studentId!}','1')"  <#if item.lock?default('0')=='1'>style="display:none"</#if>>锁定</a>-->
                <a href="javascript:;" class="table-btn color-green js-editCourse edit_${item.studentId!} <#if item.lock?default('0')=='1'>disabled</#if>" onclick="editResult('${item.studentId!}')">编辑</a>
                <a href="javascript:;" class="table-btn color-red js-delete delete_${item.studentId!} <#if item.lock?default('0')=='1'>disabled</#if>" onclick="doDeleteChoose('${item.studentId!}','${item.studentName!}')">清空</a>
            </td>
            </#if>
        </tr>
        </#list>
    </#if>
        </tbody>
    </table>
</form>

<script>
    function editResult(studentId){
        var acadyear = $("#acadyear option:selected").val();
        var semester = $("#semester option:selected").val();
        if($(".edit_"+studentId).hasClass("disabled")){
            return;
        }
        var url = "${request.contextPath}/basedata/stuselect/edit/page?studentId="+studentId+"&acadyear=" + acadyear + "&semester=" + semester;
        indexDiv = layerDivUrl(url,{title: "编辑选课信息",width:400,height:430});
    }

    function doDeleteChoose(studentId,stuName){
        if($(".delete_"+studentId).hasClass("disabled")){
            return;
        }
        layer.confirm("确定要清空<span style='color:red'>"+stuName+"</span>的选课信息吗？", {
            title: ['提示','font-size:20px;'],
            btn: ['确认','取消'] //按钮
        }, function(){
            doDeleteByStudentId(studentId);
        }, function(){
            layer.closeAll();
        });
    }

    function doDeleteByStudentId(studentId){
        var acadyear = $("#acadyear option:selected").val();
        var semester = $("#semester option:selected").val();
        var ii = layer.load();
        $.ajax({
            url:'${request.contextPath}/basedata/stuselect/edit/save',
            data:
                    {
                        "acadyear":acadyear,
                        "semester":semester,
                        'studentId':studentId
                    },
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    layer.closeAll();
                    layer.msg(jsonO.msg, {offset: 't',time: 2000});
                    change2();
                }
                else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }
</script>