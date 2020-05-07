<div class="filter filter-f16">
    <div class="filter-item">
        <span class="filter-name">导入科目：</span>
        <div class="filter-content" id="subjectIdDiv">
        	<select name="subjectId" id="subjectId" class="form-control" onChange="findByCondition1()">
			     <#if courseList?exists && courseList?size gt 0>
            		<#list courseList as course>
					<option value = "${course.id!}" <#if subjectId?default("")==course.id>selected="selected"</#if>>${course.subjectName!}</option>
					</#list>
            	<#else>
            		<option value="">无课程设置</option>
            	</#if>
			</select>
        </div>
    </div>
    <div class="filter-item">
        <span class="filter-name">班级：</span>
        <div class="filter-content">
        	<select name="clsTypeId" id="clsTypeId" class="form-control" onChange="findByCondition1()">
		     	<#if clslist?exists && clslist?size gt 0>
            		<#list clslist as cls>
					<option value="${cls.id}_1" <#if clsTypeId?default("")==cls.id+'_1'>selected="selected"</#if>>${cls.classNameDynamic!}</option>
					</#list>
            	</#if>
            	<#if teaClslist?exists && teaClslist?size gt 0>
            		<#list teaClslist as cls>
					<option value="${cls.id}_2" <#if clsTypeId?default("")==cls.id+'_2'>selected="selected"</#if>>${cls.name!}</option>
					</#list>
            	</#if>
			</select>
        </div>
    </div>
     <div class="filter-item filter-item-right">
     	<input type = "hidden" id = "acadyear" value="${acadyear!}"/>
     	<input type = "hidden" id = "semester" value="${semester!}"/>
     	<input type = "hidden" id = "gradeId" value="${gradeId!}"/>
     	<input type = "hidden" id = "type" value="${type!}"/>
	   <#--a href="javascript:" class="btn btn-blue"  onclick="toIndex();">返回</a-->
	</div>
</div>
<div id="importEditDiv">
</div>
<script>
$(function(){
	<#if isAdmin>
	showBreadBack(toIndex,true,"返回");
	<#else>
	showBreadBack(gobackIndex,true,"返回");
	</#if>
	findByCondition();
})
function toIndex(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/exammanage/credit/register?acadyear='+acadyear+'&semester='+semester;
	$('#model-div-37097').load(url);
}

function findByCondition1(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var subjectId = $('#subjectId').val();
	var clsTypeId = $('#clsTypeId').val();
	var gradeId = $('#gradeId').val();
	var str = '?acadyear='+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&type=${type!}"+"&subjectId="+subjectId+"&clsTypeId="+clsTypeId;
	var url='${request.contextPath}/exammanage/credit/import/register/head'+str;
	url=encodeURI(encodeURI(url));
	$('#model-div-37097').load(url);
}

function findByCondition(){
    //查询录入权限，如果成绩已经提交或者 没有录入权限，则不能导入
    var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	var subjectId = $("#subjectId").val();
	var clsTypeId=$("#clsTypeId").val();
    var url = '${request.contextPath}/exammanage/credit/checkInputPower';
    var params ='acadyear='+acadyear+'&subjectId='+subjectId+'&semester='+semester+"&clsTypeId="+clsTypeId;
    $.ajax({url:url,
    	type:"POST",
    	data:params,
    	dataType:"JSON",
    	success:function(data){
    		if(!data.success){
    			layerTipMsg(false,"失败",data.msg);
    			$("#importEditDiv").html(data.msg);
    			return;
    		}
    		doImport(subjectId,clsTypeId);
    	}
    });
    
    
}
function doImport(subjectId,clsTypeId){
	var acadyear=$("#acadyear").val();
	var semester=$("#semester").val();
	var type = $("#type").val();
	var str='?acadyear='+acadyear+'&subjectId='+subjectId+"&semester="+semester+"&type="+type+"&clsTypeId="+clsTypeId;
    var url =  '${request.contextPath}/exammanage/credit/import/register/main'+str;
	$("#importEditDiv").load(url);
}
</script>