<a href="javascript:void(0);" onclick="goback('${isbackResultList!}','${divideClassId!}')" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css"/>
<div class="box-body" id="perArrangeDiv">
	<div class="table-switch-container">
		<div class="table-switch-box tableSwitchBox" style="width:45%;">
			<div class="table-switch-filter">
				<div class="filter filter-sm">
					<div class="filter-item">
						<#if classType?default("")=="0">
						<span class="filter-name">组合：</span>
						<div class="filter-content">
							<div class="input-group input-group-sm input-group-search">	
								<select name="subjectIdstr" id="subjectIdstr" class="form-control" onChange="selectLeftStudent()">
									<#if newDtoList?exists && (newDtoList?size>0)>
										<#list newDtoList as item>
										<option value="${item.subjectIdstr!}" <#if subjectIdstr?default('')==item.subjectIdstr> selected="selected"</#if>>${item.subShortNames!}</option>
										</#list>
									<#else>
										<option value="">--请选择--</option>
									</#if>
								</select>
						    </div>
						</div>
						<#elseif classType?default("")=="1">
						<span class="filter-name">班级：</span>
						<div class="filter-content">
							<div class="input-group input-group-sm input-group-search">	
								<select name="classId" id="classId" class="form-control" onChange="selectLeftStudent()">
									<#if classList?exists && (classList?size>0)>
										<#list classList as item>
										<option value="${item.id!}" <#if classId?default('')==item.id> selected="selected"</#if>>${item.classNameDynamic!}</option>
										</#list>
									<#else>
										<option value="">--请选择--</option>
									</#if>
								</select>
						    </div>
						</div>
						</#if>
					</div>
					<div class="filter-item">
						<#--<input type="hidden" name="subjectIdstr" id="subjectIdstr" value="${subjectIdstr?default('')}">-->
						<span class="filter-name" style="margin-right:2px;">前</span>
						<div class="filter-content" >
							<input type="text" style="width:50px;margin-right:2px;" class="form-control" name="searchSomeRows" id="searchSomeRows" value="" maxlength="3">
						</div>
						<span class="filter-name" style="margin-right:2px;">行</span>
						<div class="filter-item-right">
							<a href="javascript:"  class="btn btn-sm btn-blue js-searchRow" onclick="searchRow()">确定勾选</a>
						</div>
					</div>
				</div>
			</div>
			<div style="width:100%;" class="tableDivClass" id="leftTableId"></div>
			<input type="hidden" id="leftTableChange" value="0">
			<input type="hidden" id="classType" value="${classType!}">
		</div>
		<div class="table-switch-control" style="width:5%;">
			<a href="javascript:" class="btn btn-sm" onclick="leftToRight()"><i class="wpfont icon-arrow-right"></i></a>
			<a href="javascript:" class="btn btn-sm" onclick="rightToLeft()"><i class="wpfont icon-arrow-left"></i></a>
		</div>
		<div class="table-switch-box tableSwitchBox" style="width:45%;">
			<div class="table-switch-filter">
				<div class="filter filter-sm">
					<div class="filter-item">
						<span class="filter-name">班级：</span>
						<div class="filter-content">
							<div class="input-group input-group-sm input-group-search">
								<select name="divideClassId" id="divideClassId" class="form-control" onChange="selectGroupClass()">
								<#if divideClassList?exists && (divideClassList?size>0)>
									<#list divideClassList as item>
									<option value="${item.id!}" <#if divideClassId?default('')==item.id> selected="selected"</#if>>${item.className!}</option>
									</#list>
								<#else>
									<option value="">暂无班级</option>
								</#if>
								</select>
							   
						    </div><!-- /input-group -->
						</div>
					</div>
					
				</div>
			</div>
			<div style="width:100%;" class="tableDivClass" id="rightTableId">
				
			</div>
		</div>
	</div>
	<div class="text-center">
		<button class="btn btn-long btn-blue" onclick="saveDivideClass()">确定</button>
	</div>
</div>
<script>
	var divideId = '${divideId!}';
	var classType='${classType!}';
	//stuDtoMap:页面学生信息
	var stuDtoMap = {};
	var courseMap={};
	var tableMap = {};
	var stuIdStr="";//保存原来页面左边选中学生
	
	var subjectIdstr='${subjectIdstr!}';
	var classId='${classId!}';
	var divideClassId='${divideClassId!}';
	
	$(function(){
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
		loadLeft();
		loadRight();
	});
	//加载左边
	function loadLeft(){
		subjectIdstr=$("#subjectIdstr").val();
		classId=$("#classId").val();
		var url = "${request.contextPath}/newgkelective/"+divideId+"/divideClass/loadLeft?subjectIdstr="+subjectIdstr+"&classId="+classId+"&classType="+classType;
		$("#leftTableId").load(url);
	}
	//加载右边
	function loadRight(){
		divideClassId=$("#divideClassId").val();
		var url = "${request.contextPath}/newgkelective/"+divideId+"/divideClass/loadRight?divideClassId="+divideClassId+"&classType="+classType;
		$("#rightTableId").load(url);
	}
	function saveDivideClass(){
		divideClassId=$("#divideClassId").val();
		var studentIdstr="";
		if($("#rightTableId").find('input:checkbox[name=studentIdName]').length>=0){
			$("#rightTableId").find('input:checkbox[name=studentIdName]').each(function(i){
				var studentId = $(this).val();
				studentIdstr=studentIdstr+","+studentId;
			});
			studentIdstr=studentIdstr.substring(1);
		}
		$.ajax({
			url:'${request.contextPath}/newgkelective/'+divideId+'/divideClass/saveClassStudent',
			data:{"studentIdstr":studentIdstr,"divideClassId":divideClassId},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
					//layerTipMsg(jsonO.success,"成功",jsonO.msg);
					layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
					//$("#showList").load('${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassList');
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
		 			
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	function selectGroupClass(){
		var change=$("#leftTableChange").val();
		if(change=="1"){
			//左边有动
			loadLeft();
			loadRight();
		}else{
			loadRight();
		}
	}
	function selectLeftStudent(){
		var classType=$("#classType").val();
		if(classType=="0"){
			var subjectIdstr=$("#subjectIdstr").val();
			$.ajax({
			    url:'${request.contextPath}/newgkelective/'+divideId+'/divideClass/findDivideClass',
			    data: {'subjectIdstr':subjectIdstr,'classType':classType},
			    success:function(data) {
			    	var jsonO = JSON.parse(data);
			    	$("#divideClassId option").remove();
			    	if(jsonO.length>0){
				    	$.each(jsonO,function(index){
				    		var htmlOption="<option ";
			    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].className;
			    			$("#divideClassId").append(htmlOption);
				    	});
				    }else{
				    	$("#divideClassId").append("<option value=''>暂无班级</option>");
				    }
				    loadLeft();
					loadRight();
			    }
			});
		}else{
			loadLeft();
		}
	}
	
	
	function leftToRight(){
		divideClassId=$("#divideClassId").val();
		if(divideClassId==""){
			//addNewClass();
			//alert("先选择班级");
			return;
		}
		if($("#rightTableId").find("table").length == 0){
			return;
		}
		if($("#rightTableId").find("table").find(".dataTables_empty").length != 0){
			//右边是空的  清空
			$("#rightTableId").find(".dataTables_empty").parents("tr").remove();
		}
		var i=0;
		var stuId=[];
		if($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			$("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				stuId[i]=$(this).val();
				i=i+1;
				$(this).prop('checked',false);
				$(this).parents("tr").addClass("linChangeClass");
				if($(this).parents("tr").hasClass("new")){
					$(this).parents("tr").removeClass("new");
				}else{
					$(this).parents("tr").addClass("new");
				}
			});
			
			$("#leftTableId").find(".checkboxAllClass").prop('checked',false);
			$("#leftTableChange").val("1");
			var leftToRight = $("#leftTableId").find("table").eq(1).find(".linChangeClass");
			leftToRight.removeClass("linChangeClass")
			$("#rightTableId").find("table").eq(1).append(leftToRight);
			//多行删除
    		tableMap['left'].DataTable().rows(leftToRight).remove().draw();
			tableMap['right'].DataTable().rows.add(leftToRight).draw();
			
			//计算左边
			refHeadMsg(stuId,"0","leftTableId");
			//计算右边
			refHeadMsg(stuId,"1","rightTableId");
			
		}
	}
	
	
	
	//type 1:增 0:减
	function refHeadMsg(stuId,type,id){
		var manCount=parseInt($("#"+id).find(".manCount").html());
		var womanCount=parseInt($("#"+id).find(".womanCount").html());
		var m=0;
		var score={};
		
		for(var i=0;i<stuId.length;i++){
			var sex=stuDtoMap[stuId[i]]['sex'];
			if(sex==1){
				m=m+1;
			}
			for(var key in courseMap){
    			var stuScore=stuDtoMap[stuId[i]]['course_'+key];
    			if(!score[key]){
    				score[key]=0.0;
    			}
    			if(stuScore){
    				score[key]=score[key]+stuScore;
    			}
			} 
		}
		
		var w=stuId.length-m;
		if("1"==type){
			manCount=manCount+m;
			womanCount=womanCount+w;
		}else{
			manCount=manCount-m;
			womanCount=womanCount-w;
		}
		var count=manCount+womanCount;
		if(count<=0){
			$("#"+id).find(".maxCount").html(0);
			$("#"+id).find(".manCount").html(0);
			$("#"+id).find(".womanCount").html(0);
		}else{
			$("#"+id).find(".maxCount").html(count);
			$("#"+id).find(".manCount").html(manCount);
			$("#"+id).find(".womanCount").html(womanCount);
		}
		for(var key in courseMap){  
			if($("#"+id).find(".courseScore_"+key).length>0){
				var old=parseFloat($("#"+id).find(".course_"+key).val());
				if(count<=0){
					$("#"+id).find(".courseScore_"+key).html(0);
					$("#"+id).find(".course_"+key).val(0);
				}else{
					if(!score[key]){
						score[key]=0.0;
					}
					var allScore=0.0;
					var ss=0.0;
					if("1"==type){
						allScore=old+score[key];
						ss=(allScore/count).toFixed(2);
					}else{
						allScore=old-score[key];
						ss=(allScore/count).toFixed(2);
					}
					if(allScore<=0){
						$("#"+id).find(".courseScore_"+key).html(0);
						$("#"+id).find(".course_"+key).val(0);
					}else{
						$("#"+id).find(".courseScore_"+key).html(ss);
						$("#"+id).find(".course_"+key).val(allScore);
					}
					
				}
				
			}
		} 
		
	}
	
	
	function rightToLeft(){
		subjectIdstr=$("#subjectIdstr").val();
		if(subjectIdstr==""){
			layer.msg("请选择组合", {
				offset: 't',
				time: 2000
			});
			return;
		}
		if($("#leftTableId").find("table").length == 0){
			return;
		}
		if($("#leftTableId").find("table").find(".dataTables_empty").length != 0){
			//右边是空的  清空
			$("#leftTableId").find(".dataTables_empty").parents("tr").remove();
		}
		var stuId=[];
		var i=0;
		if($("#rightTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			$("#rightTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				stuId[i]=$(this).val();
				i=i+1;
				$(this).prop('checked',false);
				$(this).parents("tr").addClass("linChangeClass");
				if($(this).parents("tr").hasClass("new")){
					$(this).parents("tr").removeClass("new");
				}else{
					$(this).parents("tr").addClass("new");
				}
			});
			
			$("#rightTableId").find(".checkboxAllClass").prop('checked',false);
			$("#leftTableChange").val("1");
			//计算左边
			refHeadMsg(stuId,"1","leftTableId");
			//计算右边
			refHeadMsg(stuId,"0","rightTableId");
			var rightToLeft = $("#rightTableId").find("table").eq(1).find(".linChangeClass");
			rightToLeft.removeClass("linChangeClass")
			$("#leftTableId").find("table").eq(1).append(rightToLeft);
			//多行删除
    		tableMap['right'].DataTable().rows(rightToLeft).remove().draw();
			tableMap['left'].DataTable().rows.add(rightToLeft).draw();
		}
		
	}
	$('#perArrangeDiv').on('change','.checkboxAllClass',function(){
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
	$('#perArrangeDiv').on('change','.checkBoxItemClass',function(){
		if($(this).parents("table").find('input:checkbox[name=studentIdName]:checked').length>0){
			$(this).parents(".tableSwitchBox").find(".checkboxAllClass").prop('checked',true);
		}else{
			$(this).parents(".tableSwitchBox").find(".checkboxAllClass").prop('checked',false);
		}
	});
	
	function searchRow(){
		var num=$("#searchSomeRows").val().trim();
		var pattern=/[^0-9]/;
		if(pattern.test(num) || num.slice(0,1)=="0"){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#searchSomeRows").val('');
			$("#searchSomeRows").focus();
			return false;
		}
		var rows=parseInt(num);
		if(num<=0){
			return false;
		}
		if($("#leftTableId").find('input:checkbox[name=studentIdName]').length>=0){
			$("#leftTableId").find('input:checkbox[name=studentIdName]').each(function(i){
				if(i<rows){
					$(this).prop('checked',true);
				}else{
					$(this).prop('checked',false);
				}
				
			});
		}
		$("#leftTableId").find(".checkboxAllClass").prop('checked',true);
		
	}
	function goback(isbackResultList,divideClassId){
		var url="";
		if(isbackResultList){
			url='${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassDeatil?divideClassId='+divideClassId;
		}else{
			url='${request.contextPath}/newgkelective/'+divideId+'/divideClass/resultClassList';
		}
		$("#showList").load(url);
	}
</script>
