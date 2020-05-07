<script type="text/javascript" src="${request.contextPath}/static/tablesort/js/jquery.tablesorter.js"></script>
<link href="${request.contextPath}/static/tablesort/css/style.css" rel="stylesheet" type="text/css" />
<div class="box-body" id="perArrangeDiv">
	<div id="headSelectDivId"></div>
	<p class="text-right"><a class="btn btn-sm btn-blue" href="javascript:" onclick="backToPerArrange()">返回</a></p>
	<div class="table-switch-container">
		<div class="table-switch-box tableSwitchBox" style="width:45%;">
			<div class="table-switch-filter">
				<div class="filter filter-sm">
					<div class="filter-item" style="margin-right:10px;">
						<span class="filter-name">组合：</span>
						<div class="filter-content">
							<div class="input-group input-group-sm input-group-search">	
								<select name="groupId" id="groupId" class="form-control" onChange="selectGroup()">
									<#if groupMap?exists && (groupMap?size>0)>
										<#list groupMap?keys as key>
										<option value="${key!}" <#if subjectIds?default('')==key> selected="selected"</#if>>${groupMap[key]!}</option>
										</#list>
									<#else>
										<option value="">暂无组合</option>
									</#if>
								</select>
						    </div>
						</div>
					</div>
					<div class="filter-item" style="margin-right:5px;">
						<input type="hidden" name="groupId" id="groupId" value="${subjectIds?default('')}">
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
							<a href="javascript:"  class="btn btn-sm btn-blue js-openSomeClass" onclick="openSomeClass()">确定</a>
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
								<select name="groupClassId" id="groupClassId" class="form-control" onChange="selectGroupClass()">
								<#if groupClassList?exists && (groupClassList?size>0)>
									<#list groupClassList as group>
									<option value="${group.id!}" <#if groupClassId?default('')==group.id> selected="selected"</#if>>${group.groupName!}</option>
									</#list>
								<#else>
									<option value="">暂无班级</option>
								</#if>
								</select>
							   
						    </div><!-- /input-group -->
						</div>
					</div>
					<div class="filter-item filter-item-right">
						<a href="javascript:"  class="btn btn-sm btn-blue js-addNewClass" onclick="addNewClass()">新建班级</a>
						<a href="javascript:"  class="btn btn-sm btn-blue js-deleteClass" onclick="deleteClass()">删除</a>
					</div>
					
				</div>
			</div>
			<div style="width:100%;height:500px;overflow:auto;" class="tableDivClass" id="rightTableId">
				
			</div>
		</div>
	</div>
	<div class="text-right">
		<a class="btn btn-blue" onclick="saveGroupClass()">保存</a>
	</div>
	<a href="javascript:" id="bbbbbbbb"></a>
</div>
<script>
	contextPath = '${request.contextPath}';
	roundsId = '${roundsId!}';
	//stuDtoMap:页面学生信息
	var stuDtoMap = {};
	var courseMap={};
	var tableMap = {};
	var stuIdStr="";//保存原来页面左边选中学生
	function backToPerArrange(){
		toPerArrange();
	}
	var subjectIds='${subjectIds!}';
	var groupClassId='${groupClassId!}';
	//加载左边
	function loadLeft(){
		var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/schedulingLeft/page?subjectIds='+subjectIds;
		//var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/schedulingLeft/page?subjectIds='+subjectIds+'&stuIdStr='+stuIdStr;
		
		$("#leftTableId").load(url);
	}
	//加载右边
	function loadRight(){
		groupClassId=$("#groupClassId").val();
		var url =  contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/schedulingRight/page?subjectIds='+subjectIds+'&groupClassId='+groupClassId;
		$("#rightTableId").load(url);
	}
	
	function selectGroupClass(){
		var change=$("#leftTableChange").val();
		if(change=="1"){
			//左边有动
			subjectIds=$("#groupId").val();
			groupClassId=$("#groupClassId").val();
			loadLeft();
			loadRight();
		}else{
			loadRight();
		}
	}
	function selectGroup(selectSubIds){
		if(selectSubIds){
			subjectIds=selectSubIds;
			$("#groupId").val(subjectIds);
		}else{
			subjectIds=$("#groupId").val();
		}
		$.ajax({
		    url:contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/findGroupClass',
		    data: {'subjectIds':subjectIds},
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
			    loadLeft();
				loadRight();
		    }
		});
	}
	
	function refHeadSelectDiv(){
		$("#headSelectDivId").load(contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/headSelectList/page');
	}
	
	$(function(){
		refHeadSelectDiv();
		
		loadLeft();
		loadRight();
	});
	
	function addNewClass(){
		subjectIds=$("#groupId").val();
		if(subjectIds==null || subjectIds==""){
			layer.msg("请先选择组合！", {
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
			var url = contextPath+'/gkelective/'+roundsId+'/openClassArrange/perArrange/schedulingEdit/page?subjectIds='+subjectIds+'&stuIdStr='+stuIdStr;
			indexDiv = layerDivUrl(url,{title: "新建班级",width:350,height:250});
		}
	}
	function leftToRight(){
		groupClassId=$("#groupClassId").val();
		if(groupClassId==""){
			addNewClass();
			//alert("先选择班级");
			return;
		}
		if($("#rightTableId").find("table").length == 0){
			return;
		}
		//左边数据
		
		
		var allTr=$("#rightTableId").find("table").find("tbody").find("tr");
		if(allTr.length==0){
			//右边数据是空的  清空
		}
		
		var i=0;
		var stuId=[];
		//增加到右边的html
		var htmlText="";
		$("#leftTableId").find("table").find(".linChangeClass").removeClass("linChangeClass");
		if($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			$("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				stuId[i]=$(this).val();
				i=i+1;
				$(this).prop('checked',false);
				htmlText=htmlText+'<tr class="new">'+$(this).parents("tr").html()+'</tr>';
				$(this).parents("tr").addClass("linChangeClass");
			});
			$("#rightTableId").find("table").find("tbody").append(htmlText);
			$("#leftTableId").find(".checkboxAllClass").prop('checked',false);
			$("#leftTableChange").val("1");
			$("#leftTableId").find("table").find(".linChangeClass").remove();
			
			//计算左边
			refHeadMsg(stuId,"0","leftTableId");
			//计算右边
			refHeadMsg(stuId,"1","rightTableId");
			updateSort();
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
		subjectIds=$("#groupId").val();
		if(subjectIds==""){
			layer.msg("请选择组合", {
				offset: 't',
				time: 2000
			});
			return;
		}
		if($("#leftTableId").find("table").length == 0){
			return;
		}
		
		var stuId=[];
		var i=0;
		var htmlText="";
		if($("#rightTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			$("#rightTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				stuId[i]=$(this).val();
				i=i+1;
				$(this).prop('checked',false);
				htmlText=htmlText+'<tr class="new">'+$(this).parents("tr").html()+'</tr>';
				$(this).parents("tr").addClass("linChangeClass");
			});
			$("#leftTableId").find("table").find("tbody").append(htmlText);
			$("#rightTableId").find(".checkboxAllClass").prop('checked',false);
			$("#rightTableId").find("table").find(".linChangeClass").remove();
			$("#leftTableChange").val("1");
			//计算左边
			refHeadMsg(stuId,"1","leftTableId");
			//计算右边
			refHeadMsg(stuId,"0","rightTableId");
			updateSort();
		}
		
	}
	
	function saveGroupClass(){
		groupClassId=$("#groupClassId").val();
		var stuId="";
		if($("#rightTableId").find('input:checkbox[name=studentIdName]').length>=0){
			$("#rightTableId").find('input:checkbox[name=studentIdName]').each(function(i){
				var studentId = $(this).val();
				stuId=stuId+","+studentId;
			});
			stuId=stuId.substring(1);
		}
		$.ajax({
			url:'${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/groupClass/save',
			data:{"stuId":stuId,"groupClassId":groupClassId},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			subjectIds=$("#groupId").val();
					scheduling(subjectIds,groupClassId);
					refHeadSelectDiv();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	function deleteClass(){
		var groupId=$("#groupClassId").val();
		if(groupId==null || groupId==""){
		
			layer.msg("没有班级需要删除！", {
				offset: 't',
				time: 2000
			});
			return ;
		}
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
			showConfirm("是否确定删除班级",options,function(){
			$.ajax({
				url:'${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/moveGroup',
				data:{"groupId":groupId},
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
					  	scheduling(subjectIds,"");
			 		}
			 		else{
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
			},function(){});
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
	
	function openSomeClass(){
		var num=$("#openNum").val().trim();;
		if(num == ""){
			layer.msg("不能为空！", {
				offset: 't',
				time: 2000
			});
			$("#openNum").val('');
			$("#openNum").focus();
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
			return false;
		}
		var stuIdStr="";
		if($("#leftTableId").find('input:checkbox[name=studentIdName]:checked').length>0){
			$("#leftTableId").find('input:checkbox[name=studentIdName]:checked').each(function(i){
				stuIdStr=stuIdStr+","+$(this).val();
			});
			stuIdStr=stuIdStr.substring(1);
		}
		
		subjectIds=$("#groupId").val();
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定根据系统规则进行开班",options,function(){
		$.ajax({
			url:'${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/autoOpenClass',
			data:{"subjectIds":subjectIds,"openNum":rows,"stuids":stuIdStr},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
				  	scheduling(subjectIds,"");
		 		}
		 		else{
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
			
		},function(){});
		
	}
	function updateSort(){
		$("#left_sort").trigger("update");
		$("#right_sort").trigger("update");
	}

</script>
