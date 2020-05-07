<script type="text/javascript" src="${request.contextPath}/static/tablesort/js/jquery.tablesorter.js"></script>
<link href="${request.contextPath}/static/tablesort/css/style.css" rel="stylesheet" type="text/css" />
<#--<a href="javascript:void(0)" onclick="backToPerArrangeA()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<div class="box-body box-default" id="perArrangeADiv">
	<div class="filter">
		
		<#--  <div class="filter-item ">
			<a class="btn btn-blue" href="javascript:void(0)" onclick="backToPerArrangeA()">返回</a>
		</div>-->
	</div>		
	<div class="table-switch-container" >
		<div class="table-switch-box tableSwitchBox" style="width:45%;">
			<div class="table-switch-filter">
				<div class="filter filter-sm">
					<div class="filter-item" style="margin-right:10px;">
						<span class="filter-name">选考科目：</span>
						<div class="filter-content">
							<div class="input-group input-group-sm input-group-search">	
								
								<select name="subjectId" id="subjectId" class="form-control" onChange="selectSubjectId()">
									<#if courseList?exists && (courseList?size>0)>
										<#list courseList  as course>
											<option value="${course.id!}" <#if subjectId?default('')==course.id> selected="selected"</#if>>${course.subjectName!}</option>
										</#list>
									<#else>
										<option value="">暂无科目</option>
									</#if>
								</select>
								
						    </div>
						</div>
					</div>
					<div class="filter-item" style="margin-right:5px;">
						<span class="filter-name" style="margin-right:2px;">前</span>
						<div class="filter-content" >
							<input type="text" style="width:50px;margin-right:2px;" class="form-control" name="searchSomeRows" id="searchSomeRows" value="" maxlength="3">
						</div>
						<span class="filter-name" style="margin-right:2px;">行</span>
						<div class="filter-item-right">
							<a href="javascript:"  class="btn btn-sm btn-blue js-searchRow" onclick="searchRow()">确定勾选</a>
						</div>
					</div>
					<div class="filter-item" style="margin-right:10px;">
						<span class="filter-name">开班数：</span>
						<div class="filter-content">
							<input type="text" style="width:50px;margin-right:5px;" class="form-control" name="openNum" id="openNum" value=""  maxlength="3" >
						</div>
						<div class="filter-item-right">
							<a href="javascript:"  class="btn btn-sm btn-blue js-openSomeClass" onclick="openSomeClassA()">确定</a>
						</div>
					</div>
					
				</div>
			</div>
			<div style="width:100%;height:500px;overflow:auto;" class="tableDivClass" id="leftTableId"></div>
			<input type="hidden" id="leftTableChange" value="0">
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
								<select name="groupClassId" id="groupClassId" class="form-control" onChange="loadRightA()">
									<option value="">暂无班级</option>
								</select>
						    </div><!-- /input-group -->
						</div>
					</div>
					<div class="filter-item filter-item-right">
						<a href="javascript:"  class="btn btn-sm btn-blue js-addNewClass" onclick="addNewClassA()">新建班级</a>
						<a href="javascript:"  class="btn btn-sm btn-blue js-deleteClass" onclick="deleteClass()">删除</a>
					</div>
					
				</div>
			</div>
			<div style="width:100%;height:500px;overflow:auto;" class="tableDivClass" id="rightTableId">
				
			</div>
		</div>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
	<a class="btn btn-blue" href="javascript:void(0)" onclick="backToPerArrangeA()">上一步</a>
</div>
<script>
	var chooseStuIdsToAdd="";
	//返回
	
	function backToPerArrangeA(){
		$("#showList").load("${request.contextPath}/newgkelective/BathDivide/${divideId!}/arrangeAList/page");
	}
	
	$(function(){
		//showBreadBack(backToPerArrangeA,false,"分班安排");
		loadLeftA();
		selectSubjectId();
	});
	function selectSubjectId(){
		var subjectId=$("#subjectId").val();
		if(subjectId==""){
			$("#leftTableId").html("");
			$("#rightTableId").html("");
			$("#groupClassId option").remove();
			$("#groupClassId").append("<option value=''>暂无班级</option>");
			return;
		}
		$.ajax({
		    url:'${request.contextPath}/newgkelective/${divideId!}/divideGroup/findClassBysubject',
		    data: {'subjectId':subjectId},
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	$("#groupClassId option").remove();
		    	if(jsonO.length>0){
			    	$.each(jsonO,function(index){
			    		var htmlOption="<option ";
		    			htmlOption+=" value='"+jsonO[index].id+"'>"+jsonO[index].name;
		    			$("#groupClassId").append(htmlOption);
			    	});
			    }else{
			    	$("#groupClassId").append("<option value=''>暂无班级</option>");
			    }
			    loadLeftA();
				loadRightA();
		    }
		});
	}
	function loadLeftA(){
		var subjectId=$("#subjectId").val();
		if(subjectId==""){
			$("#leftTableId").html("");
			return;
		}
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/schedulingALeft/page?subjectId='+subjectId;
		$("#leftTableId").load(url);
	}
	function loadRightA(){
		var subjectId=$("#subjectId").val();
		var groupClassId=$("#groupClassId").val();
		if(groupClassId==""){
			$("#rightTableId").html("");
			return;
		}
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/schedulingARight/page?subjectId='+subjectId+'&groupClassId='+groupClassId;
		$("#rightTableId").load(url);
	}
	
	//移动之后直接保存
	//新增
	var isMove=false;
	function leftToRight(){
		if(isMove){
			return;
		}
		isMove=true;
		var groupClassId=$("#groupClassId").val();
		if(groupClassId==""){
			//layer.msg("请先新增班级！", {
			//	offset: 't',
			//	time: 2000
			//});
			addNewClassA();
			//isMove=false;
			return;
		}
		//layer.open();
		//左边选中的学生
		var stuIdStr="";
		if($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			$("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				stuIdStr=stuIdStr+","+$(this).val();
			});
			stuIdStr=stuIdStr.substring(1);
		}
		if(stuIdStr==""){
			layer.msg("没有选择需要移动的学生！", {
				offset: 't',
				time: 2000
			});
		}
		saveGroupClass(groupClassId,stuIdStr,1);
	}
	//删除
	function rightToLeft(){
		if(isMove){
			return;
		}
		isMove=true;
		var stuIdStr="";
		var groupClassId=$("#groupClassId").val();
		if($("#rightTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			$("#rightTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				stuIdStr=stuIdStr+","+$(this).val();
			});
			stuIdStr=stuIdStr.substring(1);
		}
		if(stuIdStr==""){
			layer.msg("没有选择需要移动的学生！", {
				offset: 't',
				time: 2000
			});
			isMove=false;
			return;
		}
		saveGroupClass(groupClassId,stuIdStr,-1);
	}
	//刷新
	function toRefresh(){
		var subjectId=$("#subjectId").val();
		var groupClassId=$("#groupClassId").val();
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/jzbGroupDetail/page?subjectId='+subjectId+"&groupClassId="+groupClassId;
		$("#showList").load(url);
	}
	
	
	function saveGroupClass(groupClassId,stuIdStr,type){
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideGroup/groupClassSaveStuA',
			data:{"stuId":stuIdStr,"groupClassId":groupClassId,"type":type},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.msg("操作成功！", {
							offset: 't',
							time: 2000
						});
		 			toRefresh();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
		 			toRefresh();
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}

	
	function deleteClass(){
		var groupClassId=$("#groupClassId").val();
		if(groupClassId==null || groupClassId==""){
		
			layer.msg("没有班级需要删除！", {
				offset: 't',
				time: 2000
			});
			return ;
		}
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
			showConfirm("是否确定删除班级",options,function(){
			$.ajax({
				url:"${request.contextPath}/newgkelective/${divideId!}/divideClass/deleteDivideClass",
				data:{"divideClassId":groupClassId},
				dataType : 'json',
				type:'post',
				success:function(data) {
					layer.closeAll();
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.msg("删除成功！", {
							offset: 't',
							time: 2000
						});
			 			toRefresh();
			 		}
			 		else{
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
			},function(){});
	}
	
	
	$('#perArrangeADiv').on('change','.checkboxAllClass',function(){
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
	$('#perArrangeADiv').on('change','.checkBoxItemClass',function(){
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
	
	function addNewClassA(){
		var subjectId=$("#subjectId").val();
		if(subjectId==null || subjectId==""){
			layer.msg("没有找到科目！", {
				offset: 't',
				time: 2000
			});
		}else{
			stuIdStr="";
			if($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
				$("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
					stuIdStr=stuIdStr+","+$(this).val();
				});
				stuIdStr=stuIdStr.substring(1);
			}
			chooseStuIdsToAdd=stuIdStr;
			var url = '${request.contextPath}/newgkelective/${divideId!}/divideGroup/schedulingAEdit/page?subjectId='+subjectId;
			indexDiv = layerDivUrl(url,{title: "新建班级",width:350,height:250});
		}
	}
	

	var isOpenSame=false;
	function openSomeClassA(){
		if(isOpenSame){
			return;
		}
		isOpenSame=true;
		var openSubmit=false;
		var num=$("#openNum").val().trim();;
		if(num == ""){
			layer.msg("不能为空！", {
				offset: 't',
				time: 2000
			});
			$("#openNum").val('');
			$("#openNum").focus();
			isOpenSame=false;
			return false;
		}
		var pattern=/[^0-9]/;
		if(pattern.test(num) || num.slice(0,1)=="0"){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#openNum").val('');
			$("#openNum").focus();
			isOpenSame=false;
			return false;
		}
		var rows=parseInt(num);
		if(rows<=0){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#openNum").val('');
			$("#openNum").focus();
			isOpenSame=false;
			return false;
		}
		var stuIdStr="";
		if($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			$("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				stuIdStr=stuIdStr+","+$(this).val();
			});
			stuIdStr=stuIdStr.substring(1);
		}
		
		var subjectId=$("#subjectId").val();
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定根据系统规则进行开班",options,function(){
			if(openSubmit){
				return;
			}
			openSubmit=true;
			var ii = layer.load();
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideGroup/autoOpenClassA',
				data:{"subjectId":subjectId,"openNum":rows,"stuids":stuIdStr},
				dataType : 'json',
				type:'post',
				success:function(data) {
					layer.closeAll();
					var jsonO = data;
			 		if(jsonO.success){
			 			
			 			if("没有学生需要分班"==jsonO.msg){
			 				layer.msg(jsonO.msg, {
								offset: 't',
								time: 2000
							});
			 			}else{
			 				toRefresh();
			 			}
			 			
					  	
			 		}else{
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
			 			isOpenSame=false;
			 			openSubmit=false;
					}
					
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
			
		},function(){
			isOpenSame=false;
		});
		
	}
</script>
