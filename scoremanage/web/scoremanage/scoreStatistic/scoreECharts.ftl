<#import "/fw/macro/treemacro.ftl" as treemacro>
<div class="tab-content">
	<div class="tab-pane active">
		<div class="clearfix">
			<div class="tree-wrap tree-warp-before">
				<div class="list-group">
					<#if unitClass?default(-1) != 2>
						<div class="widget-box" style="border-color: #5090C1;">
							<div class="widget-body">
								<div class="widget-main padding-8" style="height:600px;overflow:auto;">
									<ul id="schoolTree" class="ztree"></ul>
								</div>
							</div>
						</div>
					<#else>
						<@treemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick"/>
					</#if>
				</div>
			</div>
			<div>
				<div class="filter" id="queryChartsList" style="">
					<div class="filter-item">
						<label for="" class="filter-name">学科：</label>
						<div class="filter-content">
							<select class="form-control" id="courseIdCharts" onchange="doChangeCourseId();">
								<option value="">暂无数据</option>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">学科分数线：</label>
						<div class="filter-content">
							<select class="form-control" id="courseLineCharts" onchange="doChangeLine();">
								<option value="">暂无数据</option>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">总分数线：</label>
						<div class="filter-content">
							<select class="form-control" id="allLineCharts" onchange="doChangeLine();">
								<option value="">暂无数据</option>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">范围：</label>
						<div class="filter-content">
							<select class="form-control" id="typeCharts" onchange="doChangeLine();">
								<option value="0">班级范围</option>
								<option value="1">学校范围</option>
								<#if isHaveTK>
								<option value="2">统考范围</option>
								</#if>
							</select>
							<span style="color:red">和名次分数线有关</span>
						</div>
					</div>
				</div><!-- 条件筛选结束 -->
				<div class="box-boder">
					<div class="widget-body" style="width:100%;height:420px;" >
						<div class="widget-main" id="showCharts">
						</div>
					</div>
				</div>
				<div class="box-boder box-bluebg">
					<h4>说明</h4>
					<div class="row">
						<div class="col-md-8">
							<p>A、象限：总分上线，学科上线；</p>
							<p>B、象限：总分上线，学科未上线，任课教师需要关注，提高班人选之一；</p>
							<p>C、象限：总分和学科均未上线，需要各学科综合分析，提高班人选之二；</p>
							<p>D、象限：总分未上线，学科上线。</p>
						</div>
						<div class="col-md-4">
							<img src="${request.contextPath}/scoremanage/images/quadrant.png" alt="">
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
var classCourseMap={};
var examCourseLineMap={};
var examClassAllLineMap={};
$(function(){
	<#if unitClass?default(-1) != 2>
		var setting = {
			check:{
				enable:false
			},
			data: {				
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pId"
				}
			},
			callback: {
				onClick: onTreeClick
			}
		};
		$.ajax({
			url:"${request.contextPath}/scoremanage/scoreStatistic/findSchoolList?documentLabel=${documentLabel}",
			data:{'examId':oldExamId},
			success:function(data){
				var jsonO = JSON.parse(data);
				if(jsonO.length == 0){
					alert("没有找到数据");
					return;
				}
	 			$.fn.zTree.init($("#schoolTree"), setting, jsonO);
			}
		});
	</#if>
});
function reloadTree(){
	var setting = {
		check:{
			enable:false
		},
		data: {				
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "pId"
			}
		},
		callback: {
			onClick: onTreeClick
		}
	};
	$.ajax({
		url:"${request.contextPath}/scoremanage/scoreStatistic/findSchoolList",
		data:{'examId':oldExamId},
		success:function(data){
			var jsonO = JSON.parse(data);
			if(jsonO.length == 0){
				alert("没有找到数据");
				return;
			}
 			$.fn.zTree.init($("#schoolTree"), setting, jsonO);
		}
	});
}
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "class"){
		var id = treeNode.id;
		doChangeClassId(id);
	}
}
$("#showDiv").show();
var oldClassIdSelected='';
function doChangeClassId(classId){
	$("#queryChartsList").show();
	oldClassIdSelected=classId;
	$("#courseIdCharts option").remove();
	if(classCourseMap[oldExamId+classId]){
		var jsonO = classCourseMap[oldExamId+classId];
		if(jsonO.length>0){
			$.each(jsonO,function(index){
	    		var htmlOption="<option ";
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    		$("#courseIdCharts").append(htmlOption);
	    	});
    	}else{
    		$("#courseIdCharts").append('<option value="">暂无数据</option>');
    	}
    	jsonO = examClassAllLineMap[oldExamId+classId];
    	$("#allLineCharts option").remove();
    	if(jsonO.length>0){
    		var htmlOption='<option value="">请选择</option>';
	    	$.each(jsonO,function(index){
	    		htmlOption+="<option ";
    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
    			htmlOption+="</option>";
	    	});
			$("#allLineCharts").append(htmlOption);
	    }else{
    		$("#allLineCharts").append('<option value="">暂无数据</option>');
    	}
    	doChangeCourseId();
    	return;
	}
	$.ajax({
	    url:'${request.contextPath}/scoremanage/scoreStatistic/findCourseListForCla',
	    data: {'classId':classId,'examId':oldExamId},  
	    type:'post',  
	    success:function(data) {
	    	var jsonArr = JSON.parse(data);
	    	var jsonO = jsonArr[0];
	    	classCourseMap[oldExamId+classId]=jsonO;
	    	if(jsonO.length>0){
		    	$.each(jsonO,function(index){
		    		var htmlOption="<option ";
	    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    			$("#courseIdCharts").append(htmlOption);
		    	});
		    }else{
	    		$("#courseIdCharts").append('<option value="">暂无数据</option>');
	    	}
	    	jsonO = jsonArr[1];
	    	examClassAllLineMap[oldExamId+classId]=jsonO;
	    	$("#allLineCharts option").remove();
	    	if(jsonO.length>0){
	    		var htmlOption='<option value="">请选择</option>';
		    	$.each(jsonO,function(index){
		    		htmlOption+="<option ";
	    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    			htmlOption+="</option>";
		    	});
    			$("#allLineCharts").append(htmlOption);
		    }else{
	    		$("#allLineCharts").append('<option value="">暂无数据</option>');
	    	}
	    	doChangeCourseId();
	    }
	});
}
function doChangeCourseId(){
	var courseId=$("#courseIdCharts").val();
	$("#courseLineCharts option").remove();
	if(examCourseLineMap[oldExamId+courseId]){
		var jsonO = examCourseLineMap[oldExamId+courseId];
		if(jsonO.length>0){
			var htmlOption='<option value="">请选择</option>';
			$.each(jsonO,function(index){
	    		htmlOption+="<option ";
	    		htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    		htmlOption+="</option>";
	    	});
    		$("#courseLineCharts").append(htmlOption);
    	}else{
    		$("#courseLineCharts").append('<option value="">暂无数据</option>');
    	}
    	doChangeLine();
    	return;
	}
	$.ajax({
	    url:'${request.contextPath}/scoremanage/scoreStatistic/findCourseLineList',
	    data: {'courseId':courseId,'examId':oldExamId,'classId':oldClassIdSelected},  
	    type:'post',  
	    success:function(data) {
	    	var jsonO = JSON.parse(data);
	    	examCourseLineMap[oldExamId+courseId]=jsonO;
	    	if(jsonO.length>0){
	    		var htmlOption='<option value="">请选择</option>';
		    	$.each(jsonO,function(index){
		    		htmlOption+="<option ";
	    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
	    			htmlOption+="</option>";
		    	});
    			$("#courseLineCharts").append(htmlOption);
		    }else{
	    		$("#courseLineCharts").append('<option value="">暂无数据</option>');
	    	}
	    	doChangeLine();
	    }
	});
}
function doChangeLine(){
	var courseId=$("#courseIdCharts").val();
	if(courseId == ''){
		return;
	}
	var courseLineId = $("#courseLineCharts").val();
	var allLineId = $("#allLineCharts").val();
	var type = $("#typeCharts").val();
	var url =  '${request.contextPath}/scoremanage/scoreStatistic/showCharts/page?examId='+oldExamId+'&classId='+oldClassIdSelected+'&courseId='+courseId+'&courseLineId='+courseLineId+"&allLineId="+allLineId+"&type="+type;
	$("#showCharts").load(url);
}
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "class"){
		var id = treeNode.id;
		doChangeClassId(id);
	}
}
</script>