 <div class="filter filter-f16 header_filter">
    <div class="filter-item" >
		<span class="filter-name">教师：</span>
		<div class="filter-content searchTeacherDiv">
				<select class="form-control"  vtype="selectOne" name="searchTeacher" id="searchTeacher">
	                <#if teacherList?exists && (teacherList?size>0)>
	                	<option value="">请选择</option>	
			           <#list teacherList as item>
				          <option value="${item.id!}">${item.teacherName!}</option>
			           </#list>
			         <#else>
			           	<option value="">暂无数据</option>	
		            </#if>
	            </select>
		</div>
	</div>
	<div class="filter-item">
 		<a class="btn btn-blue" href="javascript:" onclick="searchByTeacher()">查询</a>
 		<span class="tip tip-grey">（查询老师的权限）</span>
	</div>
	<#if (emSubjectList?exists && emSubjectList?size>0)>
		<div class="filter-item filter-item-right">
		    <span class="tip tip-grey">（自动安排会安排班主任为本班级的所有科目录分人员，安排任课老师为所教科目的录分人员）</span>
		    <a class="btn btn-blue" href="javascript:" onclick="autoSave();">自动安排</a>
		    <a class="btn btn-blue" href="javascript:" onclick="save();">保存</a>
		</div>
	</#if>
</div>
<div class="tab-header clearfix">
	<ul class="nav nav-tabs nav-tabs-1">
		<#if (emSubjectList?exists && emSubjectList?size>0)>
			<#list emSubjectList as item>
			    <li <#if item_index==0>class="active"</#if>>
			 		<a data-toggle="tab" href="#a_${item_index}" aria-expanded="true" onclick="showPermission('${item.subjectId}')">${item.courseName}</a>
			 	</li>
		 	</#list>
		<#else>
			 <span class="tip tip-grey">该考试下还没有安排考试科目，请先维护考试科目</span>
	 	</#if>
	 </ul>
</div>
<div class="tab-content permissionDiv">
</div>
<script>
var subId="";
$(function(){
	<#if (emSubjectList?exists && emSubjectList?size>0)>
	<#list emSubjectList as item>
	    <#if item_index==0>
	    	showPermission('${item.subjectId}');
	    </#if>
 	</#list>
</#if>
	$("#searchTeacher").chosen({
		width:'150px',
		no_results_text:"未找到",//无搜索结果时显示的文本
		allow_single_deselect:true,//是否允许取消选择
		disable_search:false, //是否有搜索框出现
		search_contains:true,//模糊匹配，false是默认从第一个匹配
		//max_selected_options:1 //当select为多选时，最多选择个数
	}); 
});

function showPermission(subjectId){
	subId=subjectId;
	var url="${request.contextPath}/exammanage/scorePermission/list/page?examId=${examId!}&subjectId="+subjectId;
	$(".permissionDiv").load(url);
}
var isAutoSubmit=false;
function autoSave(){
	if(isAutoSubmit){
		return;
	}
	isAutoSubmit=true;
	$.ajax({
	    url:'${request.contextPath}/exammanage/scorePermission/autoArrange',
	    data: {'examId':'${examId!}'},  
	    type:'post',  
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	if(jsonO.success){
	    		// 显示成功信息
			   layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
	 			isAutoSubmit=false;
	 			showPermission(subId);
	 		}
	 		else{
				layerTipMsg(jsonO.success,"失败",jsonO.msg);
				isAutoSubmit=false;
			}
	    }
	});

}

function searchByTeacher(){
   var searchTeacher=$("#searchTeacher").val();
   var teacherName=$("#searchTeacher option:selected").text();
   if(searchTeacher==""){
   		layer.tips("请选择一个老师", $(".searchTeacherDiv"), {
			tipsMore: true,
			tips:3		
		});
   		return;
   }
   var url = "${request.contextPath}/exammanage/scorePermission/findLimit/page?examId=${examId}&teacherId="+searchTeacher;
   indexDiv = layerDivUrl(url,{title: teacherName+"权限信息",width:750,height:500});
}



</script>