<a href="javascript:void(0);" onclick="black();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box print">
	<div class="tab-pane chosenSubjectHeaderClass">
		<div class="filter-item" style="margin-right: 10px;">
            <span class="filter-name">年级：</span>
            <div class="filter-content">
                <select name="gradeId" id="gradeId" class="form-control" onchange="doSearch()" style="width:120px">
                <option value="">---请选择---</option>
                <#if gradeList?exists && gradeList?size gt 0>
					<#list gradeList as item>
						<option value="${item.id!}" <#if item.id==gradeId?default("")>selected="selected"</#if> >${item.gradeName!}</option>
					</#list>
				</#if>
                </select>
            </div>
        </div>
        <#if canAdd?default("")=="true">
	        <div class="filter filter-f16">
	            <div class="filter">
				<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="saveRelation()">保存</a>
				</div>
	        </div>
        </#if>
    </div>
    <table class="table table-striped table-hover table-layout-fixed no-margin">
      <thead>
          <tr>
                <th style="width:20%" align="center">班级名</th>
                <th style="width:20%" align="center">课程名</th>
                <th style="width:20%" align="center">所属学科</th>
                <th style="width:20%" align="center">所属年级</th>
                <th style="width:20%" align="center"><label><input type="checkbox" id="checkAll" class="wp"><span class="lbl"></span>是否参加评教</label></th>
          </tr>
      </thead>
      <tbody>
      	  <#if relationList?exists && relationList?size gt 0>
      	  	<#list relationList as item>
      	  	<tr>
      	  		<td>${item.teachClassName!}</td>
      	  		<td>${item.courseName!}</td>
      	  		<td><#if evaluateType=="13">选修课类型<#else>${item.courseTypeName!}</#if></td>
      	  		<td>${item.gradeName!}</td>
      	  		<td><label><input type="checkbox" class="wp checked-reinput"  value="${item.valueId!}_${item.id!}" <#if item.haveSelected>checked="true"</#if>><span class="lbl"></span></label></td>
      	  	</tr>
      	  	</#list>
      	  <#else>
	          <tr >
	          	<td colspan="5" align="center">
	          		暂无数据
	          	</td>
	          <tr>
          </#if>
      </tbody>
  </table>
  <input type="hidden" value="${acadyear!}" id="acadyear">
  <input type="hidden" value="${semester!}" id="semester">
  <input type="hidden" value="${projectId!}" id="projectId">
  <input type="hidden" value="${evaluateType!}" id="evaluateType">
</div>
<script>
	$(function(){
		$("#checkAll").click(function(){
			var ischecked = false;
			if($(this).is(':checked')){
				ischecked = true;
			}
		  	$(".checked-reinput").each(function(){
		  		if(ischecked){
		  			$(this).prop('checked',true);
		  		}else{
		  			$(this).prop('checked',false);
		  		}
			});
		});
	})
	function doSearch(){
		var acadyear = $('#acadyear').val();
		var semester = $('#semester').val();
		var projectId = $('#projectId').val();
		var evaluateType = $('#evaluateType').val();
		var gradeId = $('#gradeId').val();
		var url =  '${request.contextPath}/evaluate/project/addRelation/page?projectId='+projectId+'&canAdd=${canAdd!}&acadyear='+acadyear+'&semester='+semester+"&evaluateType="+evaluateType+"&gradeId="+gradeId;
		$(".model-div").load(url);
	}
	var isSubmit=false;
	function saveRelation(){
		if(isSubmit){
			return;
		}
		var projectId=$("#projectId").val();
		var ids="";
		var noCheckIds="";
		$(".checked-reinput").each(function(){
	  		if($(this).is(':checked')){
	  			if(ids==''){
	  				ids = $(this).val();
	  			}else{
	  				ids+=','+$(this).val();
	  			}
	  		}else{
	  			if(noCheckIds==''){
	  				noCheckIds = $(this).val();
	  			}else{
	  				noCheckIds+=','+$(this).val();
	  			}
	  		}
		});
		isSubmit = true;
		$.ajax({
			url:'${request.contextPath}/evaluate/project/addRelation/saveRelation',
			data:{'valueIds':ids,'noCheckIds':noCheckIds,'projectId':projectId},
			type:"post",
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
		 			doSearch();
		 		}else{
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
		
	}
	function black(){
		var url =  '${request.contextPath}/evaluate/project/index/page?acadyear=${acadyear!}&semester=${semester!}';
		$(".model-div").load(url);
	}
</script>