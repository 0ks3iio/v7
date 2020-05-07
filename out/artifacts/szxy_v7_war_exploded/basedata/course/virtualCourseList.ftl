<table class="table table-bordered table-striped">
    <table-hover>
        <thead>
        <tr>
            <th>
                <label class="pos-rel">
                    <input type="checkbox" class="wp" id="selectAll">
                    <span class="lbl"></span>
                </label>
                选择
            </th>
            <th>课程码</th>
            <th>虚拟课程名称</th>
            <th>简称</th>
            <th>课程类型</th>
            <#--<th>学段</th>-->
            <#--<th>是否在用</th>-->
            <th>排序号</th>
            <th width="100px"><label class="pos-rel"><span>操作</span> </label></th>
        </tr>
        </thead>
        <tbody id="list">
        <#if courseList?exists && courseList?size gt 0>
            <#list courseList as item>
                <tr>
                    <td>
                        <#if unitId == item.unitId>
                            <label class="pos-rel">
                                <input type="checkbox" class="wp" name="courseId" value="${item.id!}">
                                <span class="lbl"></span>
                            </label>
                        </#if>
                    </td>
                    <td><span>${item.subjectCode!}</span></td>
                    <td><span>${item.subjectName!}</span></td>
                    <td><span>${item.shortName!}</span></td>
                    <td><span>${item.courseTypeName!}</span></td>
                    <#--<td><span class="sectionName">${item.sectionName!}</span></td>-->
                    <#--<td><span>${item.isUsingName!}</span></td>-->
                    <td><span class="orderId">${item.orderId!}</span></td>
                    <td>
                        <#if unitId == item.unitId>
                            <a class="table-btn js-editCourse" href="javascript:;">编辑</a>
                            <a class="table-btn js-del" href="javascript:;">删除</a>
                        </#if>
                    </td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table-hover>
</table>
<script>
$(function(){
	//全选
	$('#selectAll').on('click',function(){
		var total = $('#list :checkbox').length;
		var length = $('#list :checkbox:checked').length;
		if(length != total){
			$('#list :checkbox').prop("checked", "true");
			$(this).prop("checked", "true");
		}else{
			$('#list :checkbox').removeAttr("checked");
			$(this).removeAttr("checked");
		}
	});
    //编辑课程
    $('.js-editCourse').on('click',function(e){
        var courseId = $(this).parents('tr').find('[name="courseId"]').val();
        editCourse(courseId);
    });

    //新增
    $('.js-addCourse').unbind('click').bind('click',function(e){
        editCourse("");
    });

    //删除课程
    var delRemark = '确定删除吗？<br>'+
        '<span class="text-danger">随意删除课程可能导致成绩、班级等数据异常</span>';
    $('.js-del').on('click', function(e){
        e.preventDefault();
        // var that = $(this);
        var courseId = $(this).parents('tr').find('[name="courseId"]').val();
        layer.confirm(delRemark, {
            btn: ['确定', '取消'],
            yes: function(index){
                deleteByCourseIds(new Array(courseId));
                layer.close(index);
            }
        })
    });
    //批量删除课程
    $('.btn-danger').click(function(){
        var selEle = $('#list :checkbox:checked');
        if(selEle.length == 0) {
			layer.alert('请勾选要删除的虚拟课程',{icon:7});
			return;
		}
        layer.confirm(delRemark, {
            btn: ['确定', '取消'],
            yes: function(index){
                if(selEle.length<1){
                    layerTipMsg(false,"失败",'请先选中科目再删除');
                    layer.close(index);
                    return;
                }
                var param = new Array();
                for(var i=0;i<selEle.length;i++){
                    param.push(selEle.eq(i).val());
                }
                deleteByCourseIds(param);
                layer.close(index);
            }
        });
    });
});

//批量删除课程
function deleteByCourseIds(idArray){
	var url = '${request.contextPath}/basedata/course/deletes';
    var params = {"ids":idArray};
    $.ajax({
        type: "POST",
        url: url,
        data: params,
        success: function(msg){
            if(msg.success){
               	refreshPage();
                layer.msg("删除成功", {offset: 't',time: 1000});
            }else{
            	layerTipMsg(false,"失败",msg.msg);
            }
        },
        dataType: "JSON"
    }); 
}

function editCourse(courseId){
    var title="新建虚拟课程";
    var url = '${request.contextPath}/basedata/course/add/page?type=${type!}';
    if(courseId!=""){
        title="编辑课程";
        url=url+"&courseId="+courseId;
    }
    indexDiv = layerDivUrl(url,{title: title,width:630,height:300});
}
</script>