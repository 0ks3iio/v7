<#import "/fw/macro/popupMacro.ftl" as popup />
<form id="subForm" method="post">
<div class="table-container">
	<div class="table-container-header text-right">
		<#if recordAdmin?default(false)>
		<a class="btn btn-white mr20" onclick="toImport();" >导入</a>
		</#if>
		<a class="btn btn-blue" onClick="save();" style="visibility: visible">保存</a>
	</div>
	<div class="table-container-body" id="myDiv">
	    <input name="acadyear" type="hidden" value="${acadyear!}">
		<input name="semester" type="hidden" value="${semester!}">
		<input name="queryDate" type="hidden" value="${queryDate?string('yyyy-MM-dd')!}">
		<input name="week" type="hidden" value="${week!}">
		<input name="day" type="hidden" value="${day!}">
		<input name="type" type="hidden" value="2">
		<table class="table table-striped layout-fixed">
			<thead>
				<tr>
					<th width="10%">行政班</th>
					<th width="10%">考核分</th>
					<th width="40%">违纪名单</th>
					<th width="40%">备注</th>
				</tr>
			</thead>
			<tbody>
			<#if clsList?exists && clsList?size gt 0>
			   <#list clsList as item>
			       <tr>
			           <input type="hidden" name="dyCourseRecordList[${item_index!}].classId" value="${item.id!}">
					   <td>${item.classNameDynamic!}</td>
					   <td>
						   <input type="text" class="form-control" nullable="false" name="dyCourseRecordList[${item_index!}].score" id="dyCourseRecordList${item_index!}score" vtype="number" min="0" max="5" maxLength="3" decimalLength="1" <#if isDefault?default(false)>value="3"<#else> value="${item.score!}"</#if>>
					   </td>
					   <td>
					       <input type="text" class="form-control" id="dyCourseRecordList${item_index!}name" onClick="getStudent2('${item.id!}','dyCourseRecordList${item_index!}name','dyCourseRecordList${item_index!}id');" value="${item.punishStuNames!}">
						   <input type="hidden" class="form-control" id="dyCourseRecordList${item_index!}id" name="dyCourseRecordList[${item_index!}].studentIds" value="${item.studentIds!}">
					       <#--@popup.selectMoreStuByClass clickId="dyCourseRecordList${item_index!}name" dataUrl="${request.contextPath}/common/div/student/popupData/class?classId=${item.id!}" id="dyCourseRecordList${item_index!}id" name="dyCourseRecordList${item_index!}name" handler="">
                                <input type="text" id="dyCourseRecordList${item_index!}name" value="${item.punishStuNames!}" class="form-control"/>
                                <input type="hidden" id="dyCourseRecordList${item_index!}id" name="dyCourseRecordList[${item_index!}].studentIds" value="${item.studentIds!}"/>
                           </@popup.selectMoreStuByClass-->
					   </td>
					   <td>
						   <input type="text" class="form-control" name="dyCourseRecordList[${item_index!}].remark" id="dyCourseRecordList${item_index!}remark" value="${item.remark!}" maxLength="500">
					   </td>
				   </tr>
			   </#list>
			</#if>				
			</tbody>
		</table>
	</div>
</div>
</form>
<@popup.selectMoreStuByClass clickId="studentName" dataUrl="" id="studentId" name="studentName" handler="popupCallBack();">
    <input type="hidden" id="studentName" />
    <input type="hidden" id="studentId" name="studentId" />
</@popup.selectMoreStuByClass>
<script>
function getStudent2(classId,nameId,id){
    $('.checkid').attr('class','form-control');
    $('.checkname').attr('class','form-control');
    var url = "${request.contextPath}/common/div/student/popupData/class?classId="+classId;
    $('#popup-extra-param').val(url); 
	$('#'+id).attr('class','form-control checkid');
	$('#'+nameId).attr('class','form-control checkname');
	$('#studentName').click();
}

function popupCallBack(){
   $('.checkid').val($('#studentId').val());
   $('.checkname').val($('#studentName').val());
   $('.checkid').attr('class','form-control');
   $('.checkname').attr('class','form-control');
}

var isSubmit=false;
function save(){
   var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/stuwork/courserecord/nightCourseRecordSave",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		return;
		 	}else{
		 		layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				searchList();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
}
</script>