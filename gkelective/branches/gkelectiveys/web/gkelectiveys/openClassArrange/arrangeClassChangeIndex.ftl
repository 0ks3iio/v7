<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css">
<div id="resultListDiv">
	<div id="dd" class="tab-pane active" role="tabpanel">
		<div id="teachClassCountDiv">
		</div>
		<div class="filter">
			<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="doSaveChang()">保存结果</a>
		</div>
	
		<div class="table-switch-container">
			<div class="table-switch-box tableSwitchBox " style="width:45%;">
				<div class="table-switch-filter">
					<div class="filter filter-sm">
					
						<div class="filter-item chosenClassHeaderClass">
							<span class="filter-name">班级：</span>
							<div class="filter-content">
								<select vtype="selectOne" id="leftClassSelectId" class="form-control input-sm" data-placeholder="<#if classDtoList?exists && (classDtoList?size>0)>选择班级<#else>未找到班级</#if>" onchange="leftChangeClass()">
									<option value=""></option>
									<#if classDtoList?exists && (classDtoList?size>0)>
										<#list classDtoList as item>
											<option value="${item.type!}#${item.classId!}#${item.subjectIds!}#${item.batch!}#${item.classType!}">${item.name}</option>
										</#list>
									</#if>
								</select>
							</div>
						</div>
						
					</div>
				</div>
				<div style="width:100%;" class="tableDivClass" id="leftTableId"></div>
				<input type="hidden" id="leftTableChange" value="0"/>
			</div>
			<div class="table-switch-control" style="width:5%;">
				<a href="javascript:" class="btn btn-sm" onclick="leftToRight()"><i class="wpfont icon-arrow-right"></i></a>
				<a href="javascript:" class="btn btn-sm" onclick="rightToLeft()"><i class="wpfont icon-arrow-left"></i></a>
			</div>
			<div class="table-switch-box tableSwitchBox" style="width:45%;">
				<div class="table-switch-filter">
					<div class="filter filter-sm">
						<div class="filter-item chosenClassHeaderClass">
							<span class="filter-name">班级：</span>
							<div class="filter-content">
								<select vtype="selectOne" name="" id="rightClassSelectId" class="form-control input-sm" data-placeholder="未找到班级" onchange="rightChangeClass()">
									<option value=""></option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div style="width:100%;" class="tableDivClass" id="rightTableId"></div>
			</div>
		</div>
	</div>
</div>
<script>
	var leftClassRightClassMap = {};
	var studentMap = {};
	var classDtoMap = {};
	var oldLeftCourseScore = {};
	var oldRightCourseScore = {};
	var tableMap = {};
	function loadTeachClassCount(){
		var url="${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/classChange/classList/page";
		$("#teachClassCountDiv").load(url);
	}
	$(function(){
		//初始化单选控件
		var viewContent={
			'allow_single_deselect':'false',//是否可清除，第一个option的text必须为空才能使用
			'select_readonly':'false',//是否只读
			'width' : '220px',//输入框的宽度
			'results_height' : '200px'//下拉选择的高度
		}
		initChosenOne(".chosenClassHeaderClass","",viewContent);
		<#--
			var viewContent2={
				'width' : '220px',//输入框的宽度
				'multi_container_height' : '33px',//输入框的高度
				'results_height' : '150px',//下拉选择的高度
			}
			initChosenOne(".chosenSubjectHeaderClass","",viewContent2);
		-->
		$('.js-sort-table').DataTable({
			paging:false,
			scrollY:372,
			info:false,
			searching:false,
			autoWidth:false,
			columnDefs: [
			    { "orderable": false, "targets": 0 }
			]
		}).columns.adjust();
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			$('.js-sort-table').DataTable().columns.adjust();
		});
		
		loadTeachClassCount();
	});
	
	function selectClazz(key){
		$("#leftClassSelectId").val(key);
		$('#leftClassSelectId').trigger('chosen:updated');//更新选项  
		leftChangeClass();
	}
	function searchLeftClass(){
		var searchStuCon = $("#stuId").val();
		if(searchStuCon == ''){
			itemShowList();
			return;
		}
		$("#leftTableId").html("");
		$("#leftClassSelectId option").remove();
		$("#leftClassSelectId").append("<option value=''></option>");
		$("#rightTableId").html("");
		$("#rightClassSelectId option").remove();
		$("#rightClassSelectId").append("<option value=''></option>");
		$('#rightClassSelectId').trigger("chosen:updated");
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/classChangeList/findLeftClass',
		    data: {'searchStuCon':searchStuCon},
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	$("#leftClassSelectId").append("<option value=''></option>");
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].type+"#"+jsonO[index].classId+"#"+jsonO[index].subjectIds+"#"+jsonO[index].batch+"#"+jsonO[index].classType+"'>"+jsonO[index].name;
		    			$("#leftClassSelectId").append(htmlOption);
			    	});
			    }else{
			    	layer.msg("未找到班级", {
						offset: 't',
						time: 2000
					});
			    }
				$('#leftClassSelectId').trigger("chosen:updated");
				layer.close(ii);
		    }
		});
	}
	function refLeftDiv(){
		var classSelectId = $("#leftClassSelectId").val();
		var url='${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/classChangeList/page?classSelect='+encodeURIComponent(classSelectId);
		$("#leftTableId").load(url);
		$("#leftTableChange").val("0");
		oldLeftCourseScore = {};
		oldRightCourseScore = {};
	}
	function leftChangeClass(){
		refLeftDiv();
		var classSelectId = $("#leftClassSelectId").val();
		$("#rightTableId").html("");
		$("#rightClassSelectId option").remove();
		$("#rightClassSelectId").append("<option value=''></option>");
		if(leftClassRightClassMap[classSelectId]){
			var jsonO = leftClassRightClassMap[classSelectId];
			if(jsonO.length>0){
				$("#rightClassSelectId").attr("data-placeholder","选择班级");
		    	$.each(jsonO,function(index){
		    		var htmlOption="<option ";
	    			htmlOption+=" value='"+jsonO[index].type+"#"+jsonO[index].classId+"#"+jsonO[index].subjectIds+"#"+jsonO[index].batch+"#"+jsonO[index].classType+"'>"+jsonO[index].name;
	    			$("#rightClassSelectId").append(htmlOption);
		    	});
		    }else{
		    	$("#rightClassSelectId").attr("data-placeholder","未找到班级");
		    }
			$('#rightClassSelectId').trigger("chosen:updated");
	    	return;
		}
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/classChangeList/findClass?classSelect='+encodeURIComponent(classSelectId),
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	leftClassRightClassMap[classSelectId]=jsonO;
		    	if(jsonO.length>0){
		    		$("#rightClassSelectId").attr("data-placeholder","选择班级");
			    	$.each(jsonO,function(index){
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].type+"#"+jsonO[index].classId+"#"+jsonO[index].subjectIds+"#"+jsonO[index].batch+"#"+jsonO[index].classType+"'>"+jsonO[index].name;
		    			$("#rightClassSelectId").append(htmlOption);
			    	});
			    }else{
			    	$("#rightClassSelectId").attr("data-placeholder","未找到班级");
			    }
				$('#rightClassSelectId').trigger("chosen:updated");
				layer.close(ii);
		    }
		});
	}
	function rightChangeClass(){
		var classSelectId = $("#rightClassSelectId").val();
		var url='${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/classChangeList/page?classSelect='+encodeURIComponent(classSelectId);
		$("#rightTableId").load(url);
		if($("#leftTableChange").val() == '1'){
			refLeftDiv();
		}
	}
	function leftCheckboxAllSelect(){
		if($("#leftCheckboxAllId").is(':checked')){
			$("#leftTableId").find('input:checkbox[name=studentIdName]').each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$("#leftTableId").find('input:checkbox[name=studentIdName]').each(function(i){
				$(this).prop('checked',false);
			});
		}
	}
	function rightCheckboxAllSelect(){
		if($("#rightCheckboxAllId").is(':checked')){
			$("#rightTableId").find('input:checkbox[name=studentIdName]').each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$("#rightTableId").find('input:checkbox[name=studentIdName]').each(function(i){
				$(this).prop('checked',false);
			});
		}
	}
	function leftToRight(){
		if($("#rightTableId").find("table").length == 0){
			return;
		}
		if($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			var leftClassSelectId = $("#leftClassSelectId").val();
			leftClassSelectId = leftClassSelectId.split("#")[1];
			var rightClassSelectId = $("#rightClassSelectId").val();
			rightClassSelectId = rightClassSelectId.split("#")[1];
			var oldLeftAllCountNum = classDtoMap[leftClassSelectId]['allCountNum'];
			var oldRightAllCountNum = classDtoMap[rightClassSelectId]['allCountNum'];
			$("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				var studentId = $(this).val();
				var sex = studentMap[studentId+'one']['sex'];
				classDtoMap[leftClassSelectId]['allCountNum']=classDtoMap[leftClassSelectId]['allCountNum']-1;
				classDtoMap[rightClassSelectId]['allCountNum']=classDtoMap[rightClassSelectId]['allCountNum']+1;
				if(sex == 1){
					classDtoMap[leftClassSelectId]['mCountNum']=classDtoMap[leftClassSelectId]['mCountNum']-1;
					classDtoMap[rightClassSelectId]['mCountNum']=classDtoMap[rightClassSelectId]['mCountNum']+1;
				}else if(sex == 2){
					classDtoMap[leftClassSelectId]['wCountNum']=classDtoMap[leftClassSelectId]['wCountNum']-1;
					classDtoMap[rightClassSelectId]['wCountNum']=classDtoMap[rightClassSelectId]['wCountNum']+1;
				}
				var leftCourseIds = classDtoMap[leftClassSelectId]['courseIds'];
				for(var i=0 ; i<leftCourseIds.length ; i++){
					var couId = leftCourseIds[i];
					if(classDtoMap[leftClassSelectId]['courseScore'][couId] == 0 || classDtoMap[leftClassSelectId]['courseScore'][couId]){
						if(oldLeftCourseScore[couId] == 0 || oldLeftCourseScore[couId]){
							if(studentMap[studentId][couId]){
								oldLeftCourseScore[couId]=oldLeftCourseScore[couId]-studentMap[studentId][couId];
							}
						}else{
							var sco = classDtoMap[leftClassSelectId]['courseScore'][couId];
							if(studentMap[studentId][couId]){
								oldLeftCourseScore[couId]=sco*oldLeftAllCountNum-studentMap[studentId][couId];
							}else{
								oldLeftCourseScore[couId]=sco*oldLeftAllCountNum;
							}
						}
					}
				}
				var rightCourseIds = classDtoMap[rightClassSelectId]['courseIds'];
				for(var i=0 ; i<rightCourseIds.length ; i++){
					var couId = rightCourseIds[i];
					if(classDtoMap[rightClassSelectId]['courseScore'][couId] == 0 || classDtoMap[rightClassSelectId]['courseScore'][couId]){
						if(oldRightCourseScore[couId] == 0 || oldRightCourseScore[couId]){
							if(studentMap[studentId][couId]){
								oldRightCourseScore[couId]=oldRightCourseScore[couId]+studentMap[studentId][couId];
							}
						}else{
							var sco = classDtoMap[rightClassSelectId]['courseScore'][couId];
							if(studentMap[studentId][couId]){
								oldRightCourseScore[couId]=sco*oldRightAllCountNum+studentMap[studentId][couId];
							}else{
								oldRightCourseScore[couId]=sco*oldRightAllCountNum;
							}
						}
					}
				}
				$(this).prop('checked',false);
				$(this).parents("tr").addClass("linChangeClass");
				if($(this).parents("tr").hasClass("new")){
					$(this).parents("tr").removeClass("new");
				}else{
					$(this).parents("tr").addClass("new");
				}
			});
			var leftToRight = $("#leftTableId").find("table").eq(1).find(".linChangeClass");
			leftToRight.removeClass("linChangeClass")
			$("#rightTableId").find("table").eq(1).append(leftToRight);
			//多行删除
    		tableMap[leftClassSelectId].DataTable().rows(leftToRight).remove().draw();
			tableMap[rightClassSelectId].DataTable().rows.add(leftToRight).draw();
			
			refHeadMsg();
			
			$("#leftTableId").find(".checkboxAllClass").prop('checked',false);
			$("#leftTableChange").val("1");
		}
	}
	function rightToLeft(){
		if($("#rightTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			var leftClassSelectId = $("#leftClassSelectId").val();
			leftClassSelectId = leftClassSelectId.split("#")[1];
			var rightClassSelectId = $("#rightClassSelectId").val();
			rightClassSelectId = rightClassSelectId.split("#")[1];
			var oldLeftAllCountNum = classDtoMap[leftClassSelectId]['allCountNum'];
			var oldRightAllCountNum = classDtoMap[rightClassSelectId]['allCountNum'];
			$("#rightTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				var studentId = $(this).val();
				var sex = studentMap[studentId+'one']['sex'];
				classDtoMap[leftClassSelectId]['allCountNum']=classDtoMap[leftClassSelectId]['allCountNum']+1;
				classDtoMap[rightClassSelectId]['allCountNum']=classDtoMap[rightClassSelectId]['allCountNum']-1;
				if(sex == 1){
					classDtoMap[leftClassSelectId]['mCountNum']=classDtoMap[leftClassSelectId]['mCountNum']+1;
					classDtoMap[rightClassSelectId]['mCountNum']=classDtoMap[rightClassSelectId]['mCountNum']-1;
				}else if(sex == 2){
					classDtoMap[leftClassSelectId]['wCountNum']=classDtoMap[leftClassSelectId]['wCountNum']+1;
					classDtoMap[rightClassSelectId]['wCountNum']=classDtoMap[rightClassSelectId]['wCountNum']-1;
				}
				var leftCourseIds = classDtoMap[leftClassSelectId]['courseIds'];
				for(var i=0 ; i<leftCourseIds.length ; i++){
					var couId = leftCourseIds[i];
					if(classDtoMap[leftClassSelectId]['courseScore'][couId] == 0 || classDtoMap[leftClassSelectId]['courseScore'][couId]){
						if(oldLeftCourseScore[couId] == 0 || oldLeftCourseScore[couId]){
							if(studentMap[studentId][couId]){
								oldLeftCourseScore[couId]=oldLeftCourseScore[couId]+studentMap[studentId][couId];
							}
						}else{
							var sco = classDtoMap[leftClassSelectId]['courseScore'][couId];
							if(studentMap[studentId][couId]){
								oldLeftCourseScore[couId]=sco*oldLeftAllCountNum+studentMap[studentId][couId];
							}else{
								oldLeftCourseScore[couId]=sco*oldLeftAllCountNum;
							}
						}
					}
				}
				var rightCourseIds = classDtoMap[rightClassSelectId]['courseIds'];
				for(var i=0 ; i<rightCourseIds.length ; i++){
					var couId = rightCourseIds[i];
					if(classDtoMap[rightClassSelectId]['courseScore'][couId] == 0 || classDtoMap[rightClassSelectId]['courseScore'][couId]){
						if(oldRightCourseScore[couId] == 0 || oldRightCourseScore[couId]){
							if(studentMap[studentId][couId]){
								oldRightCourseScore[couId]=oldRightCourseScore[couId]-studentMap[studentId][couId];
							}
						}else{
							var sco = classDtoMap[rightClassSelectId]['courseScore'][couId];
							if(studentMap[studentId][couId]){
								oldRightCourseScore[couId]=sco*oldRightAllCountNum-studentMap[studentId][couId];
							}else{
								oldRightCourseScore[couId]=sco*oldRightAllCountNum;
							}
						}
					}
				}
				$(this).prop('checked',false);
				$(this).parents("tr").addClass("linChangeClass");
				if($(this).parents("tr").hasClass("new")){
					$(this).parents("tr").removeClass("new");
				}else{
					$(this).parents("tr").addClass("new");
				}
			});
			var rightToLeft = $("#rightTableId").find("table").eq(1).find(".linChangeClass");
			rightToLeft.removeClass("linChangeClass")
			$("#leftTableId").find("table").eq(1).append(rightToLeft);
			//多行删除
    		tableMap[rightClassSelectId].DataTable().rows(rightToLeft).remove().draw();
			tableMap[leftClassSelectId].DataTable().rows.add(rightToLeft).draw();
			
			refHeadMsg();
			
			$("#rightTableId").find(".checkboxAllClass").prop('checked',false);
			$("#leftTableChange").val("1");
		}
	}
	function refHeadMsg(){
		var leftClassSelectId = $("#leftClassSelectId").val();
		leftClassSelectId = leftClassSelectId.split("#")[1];
		var rightClassSelectId = $("#rightClassSelectId").val();
		rightClassSelectId = rightClassSelectId.split("#")[1];
		$("#leftTableId").find(".allCountNumClass").html(classDtoMap[leftClassSelectId]['allCountNum']);
		$("#leftTableId").find(".mCountNumClass").html(classDtoMap[leftClassSelectId]['mCountNum']);
		$("#leftTableId").find(".wCountNumClass").html(classDtoMap[leftClassSelectId]['wCountNum']);
		$("#rightTableId").find(".allCountNumClass").html(classDtoMap[rightClassSelectId]['allCountNum']);
		$("#rightTableId").find(".mCountNumClass").html(classDtoMap[rightClassSelectId]['mCountNum']);
		$("#rightTableId").find(".wCountNumClass").html(classDtoMap[rightClassSelectId]['wCountNum']);
		var leftCourseIds = classDtoMap[leftClassSelectId]['courseIds'];
		for(var i=0 ; i<leftCourseIds.length ; i++){
			var couId = leftCourseIds[i];
			if(oldLeftCourseScore[couId] == 0 || oldLeftCourseScore[couId]){
				if(classDtoMap[leftClassSelectId]['allCountNum'] == 0){
					$("#leftTableId").find("."+couId+"Class").html('0')
				}else{
					if(oldLeftCourseScore[couId] < 0){
						oldLeftCourseScore[couId] = 0;
						$("#leftTableId").find("."+couId+"Class").html(0);
					}else{
						$("#leftTableId").find("."+couId+"Class").html(Math.round((oldLeftCourseScore[couId]/classDtoMap[leftClassSelectId]['allCountNum']) * 100) / 100)
						//$("#leftTableId").find("."+couId+"Class").html(Math.floor((oldLeftCourseScore[couId]/classDtoMap[leftClassSelectId]['allCountNum']).toFixed(2) * 100) / 100)
					}
				}
			}
		}
		var rightCourseIds = classDtoMap[rightClassSelectId]['courseIds'];
		for(var i=0 ; i<rightCourseIds.length ; i++){
			var couId = rightCourseIds[i];
			if(oldRightCourseScore[couId] == 0 || oldRightCourseScore[couId]){
				if(classDtoMap[rightClassSelectId]['allCountNum'] == 0){
					$("#rightTableId").find("."+couId+"Class").html('0')
				}else{
					if(oldRightCourseScore[couId] < 0){
						oldRightCourseScore[couId] = 0;
						$("#rightTableId").find("."+couId+"Class").html(0);
					}else{
						$("#rightTableId").find("."+couId+"Class").html(Math.round((oldRightCourseScore[couId]/classDtoMap[rightClassSelectId]['allCountNum']) * 100) / 100)
						//$("#rightTableId").find("."+couId+"Class").html(Math.floor((oldRightCourseScore[couId]/classDtoMap[rightClassSelectId]['allCountNum']).toFixed(2) * 100) / 100)
					}
				}
			}
		}
	}
	$('#resultListDiv').on('change','.checkboxAllClass',function(){
		if($(this).is(':checked')){
			$(this).parents(".tableDivClass").find('input:checkbox[name=studentIdName]').each(function(i){
				$(this).prop('checked',true);
			});
		}else{
			$(this).parents(".tableDivClass").find('input:checkbox[name=studentIdName]').each(function(i){
				$(this).prop('checked',false);
			});
		}
	});
	$('#resultListDiv').on('change','.checkBoxItemClass',function(){
		if($(this).parents("table").find('input:checkbox[name=studentIdName]:checked').length>0){
			$(this).parents(".tableSwitchBox").find(".checkboxAllClass").prop('checked',true);
		}else{
			$(this).parents(".tableSwitchBox").find(".checkboxAllClass").prop('checked',false);
		}
	});
	var isSubmit=false;
	function doSaveChang(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		if(($("#leftTableId").html() == '' || $("#rightTableId").html() == '') || ($("#leftTableId").find(".new").length == 0 && $("#rightTableId").find(".new").length == 0)){
			layer.msg("没有需要处理的数据", {
				offset: 't',
				time: 2000
			});
			isSubmit = false;
			return;
		}
		var leftClassSelectId = $("#leftClassSelectId").val();
		var rightClassSelectId = $("#rightClassSelectId").val();
		var leftAddStu = "";
		$("#leftTableId").find(".new").each(function(i){
			if(leftAddStu != ""){
				leftAddStu+=","+$(this).find(".checkBoxItemClass").val();
			}else{
				leftAddStu+=$(this).find(".checkBoxItemClass").val()
			}
		});
		var rightAddStu = "";
		$("#rightTableId").find(".new").each(function(i){
			if(rightAddStu != ""){
				rightAddStu+=","+$(this).find(".checkBoxItemClass").val();
			}else{
				rightAddStu+=$(this).find(".checkBoxItemClass").val()
			}
		});
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/classChangeList/save',
		    data:{'leftClassSelect':leftClassSelectId,
		    'rightClassSelect':rightClassSelectId,'leftAddStu':leftAddStu,'rightAddStu':rightAddStu},
		    dataType : 'json',
		    success:function(data) {
		    	var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"操作成功",jsonO.msg);
					$("#leftTableId").find(".new").each(function(i){
						$(this).removeClass("new");
					});
					$("#rightTableId").find(".new").each(function(i){
						$(this).removeClass("new");
					});
					loadTeachClassCount();
    			}
	 			isSubmit = false;
				layer.close(ii);
		    }
		});
	}
</script>
