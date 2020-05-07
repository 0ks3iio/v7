<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div class="layer-content">
	<div class="tab-content">
		<div class="form-horizontal">
			<form id = "teachGroupForm">
				<input type="hidden" name="teachGroupId" value="${teachGroupDto.teachGroupId!}">
                <div class="form-group">
                    <label for="" class="control-label no-padding-right col-sm-3">学段：</label>
                    <div id="sectionId" class="col-sm-8">
                        <#list secList as section>
                            <#if section=="1">
                                <label class="pos-rel">
                                    <input type="radio" <#if section==currentSection?default('')>checked="true"</#if> class="wp secChange" name="sections" value="1">
                                    <span class="lbl">&nbsp;小学</span>
                                </label>
                            <#elseif section=="2">
                                <label class="pos-rel">
                                    <input type="radio" <#if section==currentSection?default('')>checked="true"</#if> class="wp secChange" name="sections" value="2">
                                    <span class="lbl">&nbsp;初中</span>
                                </label>
                            <#elseif section=="3">
                                <label class="pos-rel">
                                    <input type="radio" <#if section==currentSection?default('')>checked="true"</#if> class="wp secChange" name="sections" value="3">
                                    <span class="lbl">&nbsp;高中</span>
                                </label>
                            </#if>
                        </#list>
                    </div>
                </div>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">科目：</label>
					<div class="col-sm-8" style="height:230px;overflow-y:auto;">
						<#if teachGroupDto.courseList?exists && (teachGroupDto.courseList?size>0)>
							<#list teachGroupDto.courseList as item>
								<label class="pos-rel courseList">
									<#if item.id == teachGroupDto.subjectId!>
										<input type="radio" class="wp ${item.section}" checked="true" onClick="setTeachGroupName('${item.subjectName}')"  name="subjectId" value="${item.id}">
								  	<#else>
										<input type="radio" class="wp ${item.section}" onClick="setTeachGroupName('${item.subjectName}')" name="subjectId" value="${item.id}">
								  	</#if>
								   <span class="lbl"> ${item.subjectName}</span>
								</label>
							</#list>
						</#if>
					</div>
				</div>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">教研组名称：</label>
					<div class="col-sm-8" id="sectionId">
						<input type="text" class="form-control" id="teachGroupName" name="teachGroupName" value="${teachGroupDto.teachGroupName!}" nullable="false">
					</div>
				</div>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">负责人：</label>
					<div class="col-sm-8">
						<input type="hidden" id="mainOldTeacherId" value="${teachGroupDto.mainTeacherIds!}"/>
						<input type="hidden" id="mainOldTeacherName" class="form-control" value="${teachGroupDto.mainTeacherNames!}"/>
						<@popupMacro.selectMoreTeacher clickId="mainTeacherName" id="mainTeacherId" name="mainTeacherName" >
							<input type="hidden" id="mainTeacherId" name="mainTeacherIds" value="${teachGroupDto.mainTeacherIds!}"/>
							<input type="text" id="mainTeacherName" class="form-control" value="${teachGroupDto.mainTeacherNames!}"/>
						</@popupMacro.selectMoreTeacher>
					</div>
				</div>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">成员：</label>
					<div class="col-sm-8">
						<input type="hidden" id="oldteacherId"  value="${teachGroupDto.memberTeacherIds!}"/>
						<input type="hidden" id="oldteacherName" class="form-control" value="${teachGroupDto.memberTeacherName!}"/>
						<@popupMacro.selectMoreTeacher clickId="memberTeacherName" id="memberTeacherId" name="memberTeacherName" >
							<input type="hidden" id="memberTeacherId" name="memberTeacherIds" value="${teachGroupDto.memberTeacherIds!}"/>
							<input type="text" id="memberTeacherName" class="form-control" value="${teachGroupDto.memberTeacherName!}"/>
						</@popupMacro.selectMoreTeacher>
					</div>
				</div>
				<div class="form-group">
					<label for="" class="control-label no-padding-right col-sm-3">排序号：</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="orderId" name="orderId" value="${teachGroupDto.orderId!}" vtype="int" maxlength="4">
					</div>
				</div>
			</form>
		</div>
		<div class="layer-footer" style="vertical-align: middle">
			<button class="btn btn-lightblue" id="arrange-commit">确定</button>
			<button class="btn btn-grey" id="arrange-close">取消</button>
		</div>
	</div>
</div>


<script>
$(function(){
    $(".secChange").each(function () {
       $(this).on("click", function () {
           secChange();
       });
    });
    secChange();
});

function secChange() {
    $(".courseList").each(function () {
        $(this).hide();
    });
    $(".secChange").each(function () {
        if($(this).prop("checked")) {
            var sectionId = $(this).val();
            $("."+sectionId).each(function () {
                $(this).parent().show();
            });
        }
    });
}

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 var isSubmit=false;
 
 // 确定按钮操作功能
 $("#arrange-commit").on("click", function(){	
 
 	if(isSubmit){
		return;
	}
	if(!checkValue("#teachGroupForm")){
		isSubmit=false;
		return;
	}
	isSubmit=true;
	
	
	var val=$('input:radio[name="subjectId"]:checked').val();
	if(val == null){
		layer.alert('请选择科目',{icon:7});
		isSubmit=false;
		return;
	}
	
	var checkVal = checkValue('#teachGroupForm');
	if(!checkVal){
	 	isSubmit=false;
	 	return;
	}
	
	var val=$('#mainTeacherId').val();
	if(val == ""){
		layer.alert('请选择至少一位负责人',{icon:7});
		isSubmit=false;
		return;
	}
	
	var val=$('#memberTeacherId').val();
	if(val == ""){
		layer.alert('请选择至少一位成员',{icon:7});
		isSubmit=false;
		return;
	}
 	
 	var options = {
		url : '${request.contextPath}/basedata/teachgroup/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.msg(data.msg, {offset: 't',time: 2000});
				load();
				layer.closeAll();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
				isSubmit=false;
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#teachGroupForm").ajaxSubmit(options);
});

function setTeachGroupName(teachGroupName) {
	$("#teachGroupName").val(teachGroupName + "组");
}
</script>