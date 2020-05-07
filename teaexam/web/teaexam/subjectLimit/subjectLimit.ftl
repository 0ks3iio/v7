<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div style="display:none">
<@popupMacro.selectMoreTeacher clickId="teacherName" id="teacherId" name="teacherName" handler="saveTeachers();">
	<div class="input-group">
		<input type="hidden" id="teacherId" name="teacherId" value="${teacherIds!}"/>
		<input type="text" id="teacherName" class="form-control" style="width:400px;" value="${teacherNames!}"/>
	</div>
</@popupMacro.selectMoreTeacher>
</div>
<input type="hidden" id="examId2" name="examId2" value=""/>
<input type="hidden" id="subjectId2" name="subjectId2" value=""/>
<div class="row">
	<div class="col-xs-12">
	    <div class="box box-default">
	    	<div class="box-body">
	    		<h4>总管理员</h4>
	    		<button class="btn btn-blue mb5" onclick="updateTea('','');">+ 新增</button>
	    		<#if limitList?exists && limitList?size gt 0>
	    			<#list limitList as limit>
	    			    <#if limit.subjectInfoId == '00000000000000000000000000000000' && limit.examId == '00000000000000000000000000000000'>
	    			        <#if limit.teacherDtoList?exists && limit.teacherDtoList?size gt 0>
	    			           <#list limit.teacherDtoList as teacher>
	    			               <button class="btn btn-white mb5" onclick="deleteTeacher('00000000000000000000000000000000','00000000000000000000000000000000','${teacher.teacherId!}');">${teacher.teacherName!}<span>×</span></button>
	    			           </#list>
	    			        </#if>
	    			    </#if>
	    			</#list>
	    		</#if>
	    	</div>
	    </div>
	    <div class="box box-default">
			<div class="box-body clearfix">
                <div class="tab-container">
					<div class="tab-content" id="tabList">
					     <div class="filter">
			                  <div class="filter-item">
				                  <span class="filter-name">年份：</span>
				                  <div class="filter-content">
					                  <select class="form-control" id="year" name="year" onchange="searchList();">
						                  <#list minYear..maxYear as item>
						                        <option value="${item!}" <#if '${year!}'=='${item!}'>selected="selected"</#if>>${item!}年</option>
						                    </#list>
					                  </select>
				                  </div>
			                  </div>
			                 <div class="filter-item">
				                 <span class="filter-name">考试名称：</span>
				                 <div class="filter-content">
					                <select class="form-control" id="examId" name="examId" onchange="searchList();" style="width:200px;">
					                <#if examList?exists && examList?size gt 0>
					                   <#list examList as item>
					                        <option value="${item.id!}" <#if '${examId!}'=='${item.id!}'>selected</#if>>${item.examName!}</option>
					                   </#list>
					                <#else>
						               <option value="">---请选择考试---</option>
						            </#if>
					                </select>
				                 </div>
		                     </div>		                   	                     
		                     <table class="table table-bordered table-striped table-hover no-margin" id="limit1table">	 																
                                <thead>
		                           <tr>
		                              <th width="10%">序号</th>
			                          <th width="30%" style="word-break:break-all;"><b>科目</b></th>
			                          <th width="40%" style="word-break:break-all;"><b>录分人员</b></th>
			                          <th width="20%"><b>操作</b></th>
		                           </tr>
	                           </thead>	
	                           <tbody>
	                               <#if subList?exists && subList?size gt 0>
	                                  <#list subList as item>
	                                  <tr>
	                                      <td>${item_index+1!}</td>
	                                      <td>${item.subjectName!}（
	                                      <#if item.section == 1>
	                                          小学
	                                      <#elseif item.section == 0>
						    				学前
	                                      <#elseif item.section == 2>
	                                       	初中
	                                      <#elseif item.section == 3>
	                                        	高中
	                                      </#if>
	                                                                                              ）
	                                      </td>
	                                      <td>
	                                          <#if limitList?exists && limitList?size gt 0>
	    			                             <#list limitList as limit>
	    			                                  <#if limit.subjectInfoId == item.id && limit.examId == item.examId>
	    			                                      <#if limit.teacherDtoList?exists && limit.teacherDtoList?size gt 0>
	    			                                          <#list limit.teacherDtoList as teacher>
	    			                                               <button class="btn btn-white mb5" onclick="deleteTeacher('${item.examId!}','${item.id!}','${teacher.teacherId!}');">${teacher.teacherName!}<span>×</span></button>
	    			                                          </#list>
	    			                                      </#if>
	    			                                   </#if>
	    			                            </#list>
	    		                              </#if>
	                                      </td>
	                                      <td>
	                                          <a class="color-blue" href="javascript:void(0);" onClick="updateTea('${item.examId!}','${item.id!}');">添加人员</a>
	                                      </td>
	                                  </tr>
	                                  </#list>
	                               <#else>
						              <tr>
							              <td colspan="4" align="center">暂无数据</td>
						              </tr>
	                               </#if>
	                           </tbody>
                            </table>		                     	
		                 </div>
			         </div>
			     </div>
		    </div>
		</div>
	</div>
</div>
					
<script>
function updateTea(examId,subjectId){
    $.ajax({
		url:'${request.contextPath}/teaexam/subjectLimit/getTeacherIds',
		data:{'examId':examId,'subjectId':subjectId},
		//dataType:'json',
		success:function(data){
	    	$("#teacherId").val(data);
	    	$("#examId2").val(examId);
	    	$("#subjectId2").val(subjectId);	
		}
	});
	$("#teacherName").click();
}

function saveTeachers(subjectId){
	//保存
	var teacherIds=$("#teacherId").val();
	var examId=$("#examId2").val();
	var subjectId = $('#subjectId2').val();
	//直接保存
	$.ajax({
		url:'${request.contextPath}/teaexam/subjectLimit/saveLimit',
		data:{'teacherIds':teacherIds,'examId':examId,'subjectId':subjectId},
		type:'post', 
		dataType:'json',
		success:function(data){
	    	if(data.success){
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				searchList();
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 		}	
		}
	});
}


function deleteTeacher(examId,subjectId,teacherId){
	$.ajax({
		url:'${request.contextPath}/teaexam/subjectLimit/deleteTeacher',
		data:{'teacherId':teacherId,'examId':examId,'subjectId':subjectId},
		type:'post', 
		dataType:'json',
		success:function(data){
	    	if(data.success){
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				searchList();
	 		}else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 		}	
		}
	});
}

function searchList(){
    var acadyear = $('#year').val();
    var examId = $('#examId').val();
    var url='${request.contextPath}/teaexam/subjectLimit/index/page?year='+acadyear+'&type=${type!}&examId='+examId;
	$(".model-div").load(url);
}
</script>