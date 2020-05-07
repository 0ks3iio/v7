<div class="filter">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="showRecordList()">
					<#if acadyearList?exists && (acadyearList?size>0)>
	                    <#list acadyearList as item>
		                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
	                    </#list>
                    </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select id="searchSemester" name="searchSemester" class="form-control" onChange="showRecordList()">
					${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">记录类型：</span>
		<div class="filter-content">
			<select name="recordType" id="recordType" class="form-control" onChange="showRecordList();">
				  <#if recordTypes?exists && (recordTypes?size>0)>
				        <option value="">全部类型</option>
	                    <#list recordTypes as recordType>
		                     <option value="${recordType.thisId!}" <#if tutorRecord?exists &&tutorRecord.recordType==recordType.thisId> selected </#if>>${recordType.mcodeContent!}</option>
	                    </#list>
                  </#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<a href="javascript:void(0);"  class="btn btn-blue js-checkIn">登记记录</a>
	</div>
</div>
<div  class="table-container">
		
</div>
<script>
  
  $(function(){
     showRecordList();
  });
  
  function showRecordList(){
   var acadyear = $("#searchAcadyear").val();
   var semester = $("#searchSemester").val(); 
   var recordType = $("#recordType").val();
   var url =  '${request.contextPath}/tutor/record/manage/doRecordList?&acadyear='+acadyear+'&semester='+semester+'&recordType='+recordType;
   $(".table-container").load(url);
  }
  
   //批量登记
     $('.filter .js-checkIn').on('click', function(e){
		$('.layer-tutor-detailedId').load("${request.contextPath}/tutor/record/manage/register",function(){			
		layer.open({
					type: 1,
					shade: .5,
					title: '登记记录',
					area: '500px',
					btn: ['确定','取消'],
					yes:function(index,layero){
				       isShow();
					   if(isOk){
					    saveTutorRecord("${request.contextPath}/tutor/record/manage/saveRecord");
					   }
		            },
					content: $('.layer-tutor-detailedId')
		           })
         });
	})
//批量登记 和单个登记的保存
function saveTutorRecord(contextPath){
  $.ajax({
        url:contextPath,
        data:dealDValue(".layer-tutor-detailedId"),
        clearForm : false,
        resetForm : false,
        dataType:'json',
        contentType: "application/json",
        type:'post',
        success:function (data) {
            if(data.success){
                showSuccessMsgWithCall(data.msg,mangeStudentIndex);
            }else{
                showErrorMsg(data.msg);
            }
        }
  })
}
</script>




