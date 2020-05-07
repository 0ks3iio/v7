<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
	<div class="layer-body">
		<div class="filter clearfix"> 
			<div class="filter clearfix">
			       <input name="acadyear" type="hidden" value="${acadyear!}">
			       <input name="semester" type="hidden" value="${semester!}">
			       <input name="queryDate" type="hidden" value="${queryDate?string('yyyy-MM-dd')!}">
			       <input name="week" type="hidden" value="${week!}">
			       <input name="day" type="hidden" value="${day!}">
			       <input name="period" type="hidden" value="${period!}">
			       <input name="type" type="hidden" value="1">
			       <input name="subjectId" type="hidden" value="${subjectId!}">
			       <input name="classId" type="hidden" value="${classId!}">
			       <input name="recordClass" type="hidden" value="${recordClass!}">
			       <input name="teacherId" type="hidden" value="${teacherId!}">
			       <input name="id" type="hidden" value="${recordId!}">
			       <div class="filter-item">
					    <label for="" class="filter-name"><span style="color:red">*</span>考核分：</label>
					    <div class="filter-content">
						   <select name="score" id="score" class="form-control" nullable="false" style="width:230px;">
						 	   <option value='0' <#if dyCourseRecord?? && dyCourseRecord.score == 0>selected</#if>>0</option>
						 	   <option value='1' <#if dyCourseRecord?? && dyCourseRecord.score == 1>selected</#if>>1</option>
						 	   <option value='2' <#if dyCourseRecord?? && dyCourseRecord.score == 2>selected</#if>>2</option>
						 	   <option value='3' <#if !dyCourseRecord?? || (dyCourseRecord?? && dyCourseRecord.score == 3)>selected</#if>>3</option>
						   </select>
					    </div>
				    </div>
				    
				    
				    <div class="filter-item" id="lkxzSelectDiv">
                       <label class="filter-name">违纪名单：</label>
                       <div class="filter-content">
                          <select multiple="multiple" name="lkxzSelect" id="lkxzSelect"  data-placeholder="违纪名单">
                            <#if studentList?? && (studentList?size>0)>
								<#list studentList as item>
									<option value="${item.id!}" <#if selectStuMap?? && selectStuMap[item.id]??>selected</#if>>${item.studentName?default('')}</option>
								</#list>
							<#else>
								<option value="">暂无数据</option>
							</#if>
                         </select>
                      </div>
                   <div>
                   <br><br>
                   <div class="filter-item">
						<label for="" class="filter-name">备注：</label>
						<div class="filter-content">
							<textarea name="remark" id="remark" maxLength="500" cols="63" rows="4" style="width:230px;" class="form-control col-xs-10 col-sm-10 col-md-10 " value="">${(dyCourseRecord.remark)!}</textarea>
						</div>
				   </div> 
			</div>	
	   </div>				
    </div>
</form>
</div>	
	<div class="layer-footer">
       <a class="btn btn-lightblue" id="arrange-commit">确定</a>
       <a class="btn btn-grey" id="arrange-close">取消</a>
    </div>
<script>

// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
$('#lkxzSelect').chosen({
	width:'230px',
	results_height:'100px',
	multi_container_height:'100px',
	no_results_text:"未找到",//无搜索结果时显示的文本
	search_contains:true,//模糊匹配，false是默认从第一个匹配
	//max_selected_options:1 //当select为多选时，最多选择个数
});

var isSubmit=false;
$("#arrange-commit").on("click", function(){		    
	var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/stuwork/courserecord/myCourseSave",
		dataType : 'json',
		success : function(data){
	 		var jsonO = data;
		 	if(!jsonO.success){
		 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 		$("#arrange-commit").removeClass("disabled");
		 		return;
		 	}else{
		 		layer.closeAll();
				layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				searchMyList();
    		}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
});
</script>