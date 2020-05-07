<!-- chosen -->
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="row">
	<div class="col-xs-12">
		<div class="box box-default">
		    <div class="box-header header_filter">
		        <div class="filter filter-f16">
		        	<div class="filter-item">
						<label for="" class="filter-name">学年：</label>
						<div class="filter-content">
							<select class="form-control" id="acadyearSearch" onChange="doChangeDate();">
							<#if (acadyearList?size>0)>
								<#list acadyearList as item>
								<option value="${item!}" <#if item==acadyearSearch>selected="selected"</#if>>${item!}学年</option>
								</#list>
							</#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">学期：</label>
						<div class="filter-content">
							<select class="form-control" id="semesterSearch" onChange="doChangeDate();">
							 ${mcodeSetting.getMcodeSelect("DM-XQ", semesterSearch, "0")}
							</select>
						</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">考试选择：</label>
						<div class="filter-content">
							<select  class="form-control" id="searchType" name="searchType" onChange="doChangeDate();">
								<option value="1">本单位设定的考试</option>
								<#if unitClass?default(-1) == 2>
								<option value="2">直属教育局设定的考试</option>
								<option value="3">参与的校校联考</option>
								</#if>
							</select>
						</div>	
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">考试名称：</label>
						<div class="filter-content">
							<select class="form-control" id="examIdSearch" onChange="searchList();">
							
							</select>
						</div>
					</div>	
					<a href="javascript:" class="btn btn-blue pull-left btn-seach" onclick="searchList()">查找</a>
		     	</div>
				<div class="listDiv">
		    
		    	</div>
		    </div>
		</div>
	</div>
</div>
<script>
	$(function(){
		doChangeDate();
	});
	
	function doChangeDate(){
		var acadyear = $("#acadyearSearch").val();
		var semester = $("#semesterSearch").val();
		var searchType = $("#searchType").val();
		$("#examIdSearch option").remove();
		$.ajax({
		    url:'${request.contextPath}/scoremanage/hierarchy/findList',
		    data: {'searchAcadyear':acadyear,'searchSemester':semester,'searchType':searchType,'isgkExamType':'1'},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].examNameOther;
		    			$("#examIdSearch").append(htmlOption);
			    	});
			    }else{
		    		$("#examIdSearch").append('<option value="">暂无数据</option>');
		    	}
		    	searchList();
		    }
		});
	}
	
	function searchList(){
		var examIdSearch=$("#examIdSearch").val();
		var c2='?examId='+examIdSearch;
		var url='${request.contextPath}/scoremanage/hierarchy/showList/page'+c2;
		$(".listDiv").load(url);
	}
	
</script>