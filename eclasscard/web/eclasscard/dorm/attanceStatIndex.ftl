<#import "/stuwork/tree/dytreemacro.ftl" as dytreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div id="checkDiv" class="box box-default" style="display:none">
</div>
<div id="statDiv" class="box box-default">
	<div class="box box-default">
		<div class="box-body">
			<div class="filter-container">
				<div class="filter">
					<div class="filter-item" id="dateDiv">
						<span class="filter-name">时段：</span>
						<div class="filter-content">
							<div class="input-group" style="width:260px;">
								<input class="form-control date-picker startTime-date date-picker-time" vtype="data" style="width: 120px" type="text"  name="startTime" id="startTime" value="${startTime?string('yyyy-MM-dd')}"  onchange="doSearch();">									
								<span class="input-group-addon">
									<i class="fa fa-minus"></i>
								</span>
								<input class="form-control date-picker startTime-date date-picker-time" vtype="data" style="width: 120px" type="text"  name="endTime" id="endTime" value="${(.now?string('yyyy-MM-dd'))!}" onchange="doSearch();">
							</div>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	<input type="hidden" id="classId">
	<div class="row">
		<div class="col-sm-3">
			<div class="box box-default" id="id1">
				<div class="box-header">
					<h3 class="box-title">班级菜单</h3>
				</div>
				<@dytreemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick"/>
			</div>
		</div>
		<div class="col-sm-9" id="showListId">
			<div class="box-body" id="id2">										
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		$('#id2').height($('#id1').height());
		
		$('.week-choose .btn').on('click', function(){
			$(this).removeClass('btn-white').addClass('btn-blue').siblings().removeClass('btn-blue').addClass('btn-white')
		})
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2',
			'endDate' : '${.now?string('yyyy-MM-dd')}',
			'startDate' : '${beginDate?string('yyyy-MM-dd')}'
		};
		initCalendarData("#dateDiv",".date-picker",viewContent);
	})
	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "class"){
			var id = treeNode.id;
			$("#classId").val(id);
			doSearch(id);
		}
	}
	function doSearch(classId){
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if(startTime>endTime){
			layerTipMsg(false,"","开始时间不能大于结束时间");
			return;
		}
		if(classId==undefined ||classId==""){
			classId=$("#classId").val();
			if(classId==undefined ||classId==""){
				return;
			}
		}
	    var   url =  '${request.contextPath}/eclasscard/dorm/attance/statList/page?classId='+classId+"&"+searchUrlValue("#dateDiv");
        $("#showListId").load(url);
	}
	function toCheck(studentId,startTime,endTime){
		var url="${request.contextPath}/eclasscard/dorm/attance/statCheck?studentId="+studentId+"&startTime="+startTime+"&endTime="+endTime;
		$("#checkDiv").load(url);
		$("#checkDiv").show();
		$("#statDiv").hide();
	}
	function goback(){
		$("#checkDiv").hide();
		$("#statDiv").show();
	}
	
</script>
