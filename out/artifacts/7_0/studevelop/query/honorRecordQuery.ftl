<title>荣誉评选查询</title>
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
					<select vtype="selectOne" class="form-control" name="acadyear" id="acadyear" onChange="changeExam()">
					<#if acadyearList?? && (acadyearList?size>0)>
						<#list acadyearList as item>
							<option value="${item}" <#if item==acadyear?default('')>selected</#if>>${item!}</option>
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
					<select vtype="selectOne" class="form-control" id="semester" name="semester" onChange="changeExam()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester?default(0))?string,'0')}
					</select>
				</div>
			</div>		
        
        	<div class="filter-item">
				<label for="" class="filter-name">类型：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" id="honortype" name="honortype" onChange="honorList()">
						<option value="0">--请选择--</option>
    					<option value="1">星级人物</option>
    					<option value="2">七彩阳光卡</option>
					</select>
				</div>
			</div>
        
			<div class="filter-item">
				<label for="" class="filter-name">班级：</label>
				<div class="filter-content">
					<select vtype="selectOne" class="form-control" id="classIdSearch" name="classIdSearch" onChange="honorList()"> 
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
		changeExam();
	});
	function changeExam(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var classId=$("#classIdSearch");
		$.ajax({
			url:"${request.contextPath}/studevelop/honorRecord/classIds",
			data:{acadyear:acadyear,semester:semester},
			dataType: "json",
			success: function(data){
				classId.html("");
				classId.chosen("destroy");
				$("#honortype").val("0");
				if(data.length==0){
					classId.append("<option value='' >-----请选择-----</option>");
				}else{
					classId.append("<option value='' >-----请选择-----</option>");
					for(var i = 0; i < data.length; i ++){
						classId.append("<option value='"+data[i].id+"' >"+data[i].classNameDynamic+"</option>");
					}
				}
				classId.chosen({
					width:'145px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				honorList();
			}
		});
	}
	
	function honorList(){
		var acadyear = $("#acadyear").val();
		var semester = $("#semester").val();
		var honortype = $("#honortype").val();
		var classIdSearch = $("#classIdSearch").val();
		var ashc = '?acadyear='+acadyear+'&semester='+semester+"&honortype="+honortype+"&classIdSearch="+classIdSearch;
		var url='${request.contextPath}/studevelop/honorRecordQuery/list'+ashc;
		$(".showList").load(url);
	}
	
</script>