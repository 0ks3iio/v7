<title>孩子期末评价</title>
<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
<div class="row">
	<div class="col-xs-12">
	   <div class="box box-default">
	      <div class="box-body">
		  <!-- PAGE CONTENT BEGINS -->
			<div class="filter clearfix" style="padding-left: 20px;">
        	<div class="filter-item">
				<label for="" class="filter-name">学年：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" name="acadyear" id="acadyear" onChange="detail()">
					<#if acadyearList?? && (acadyearList?size>0)>
						<#list acadyearList as item>
							<option value="${item}" <#if item==acadyear?default('')>selected</#if>>${item!}学年</option>
						</#list>
					<#else>
						<option value="">暂无数据</option>
					</#if>
				</select>
				</div>
			</div>
			<div class="filter-item">
				<label for="" class="filter-name">学期：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" id="semester" name="semester" onChange="detail()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
					</select>
				</div>
			</div>		 
    </div>
    <div class="box-body showList">
    
    </div>
</div>
</div>
</div>
</div>
<script>
	$(function(){
		detail();
	});
	
	function detail(){
			var acadyear=$("#acadyear").val();
			var semester=$("#semester").val();
			var as = '?acadyear='+acadyear+'&semester='+semester;
			var url='${request.contextPath}/studevelop/semesterEndEvaluate/detail'+as;
			$(".showList").load(url);
		}
</script>