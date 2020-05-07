<div class="stepsContainer">
	<ul class="steps-default clearfix">
		<li class="active"><span><i>1</i>分行政班</span></li>
		<li><span><i>2</i>分教学班</span></li>
	</ul>
</div>
<div class="box box-default">
	<div class="box-body">
		<div class="filter" id="groupDiv">
		<#if isCanEdit>
			<div class="filter-item">
				<div class="filter-content">
					<a class="btn btn-blue js-group intelliDivide" href="javascript:" onclick="intelliDivide()">智能分班</a>
					<a class="btn btn-default" href="javascript:" onclick="deleteAll();">全部解散</a>
				</div>
			</div>
		</#if>
			<div class="filter-item">
				<span class="filter-name">全部学生：<strong class="color-blue">${chosenStudentNum!}</strong>人</span>
			</div>
			<div class="filter-item">
				<span class="filter-name">已安排：<strong class="color-blue">${fixStudentNum!}</strong>人</span>
			</div>
			<div class="filter-item">
				<span class="filter-name">未安排：<strong class="color-blue">${noFixStudentNum!}</strong>人</span>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-6">
				<h4 class="form-title">
					<b class="mr20">三科组合开班</b>
					<label class="pos-rel color-blue font-12 no-margin">
						<input type="checkbox" class="wp group-open"  value="3">
						<span class="lbl"> 仅查看已开班组合</span>
					</label>
				</h4>
				<table class="table table-striped table-bordered table-hover group-three" >
					<thead>
						<tr>
							<th>组合名称</th>
							<th>总人数</th>
							<th>未排人数</th>
							<th>分班班级</th>
							<th <#if !isCanEdit>width="60"<#else>width="200"</#if>>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if gDtoList?exists && (gDtoList?size > 0)>
							<#list gDtoList as dto>
							<#assign ss=1>
							<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size gt 0)>
								<#assign ss=dto.gkGroupClassList?size>
							</#if>
							<tr data-value="${dto.subjectIds!}">
								<td <#if ss gt 1>rowspan="${ss}"</#if>>${dto.conditionName!}
									<input type="hidden" class="subIds" value="${dto.subjectIds!}">
								</td>
								<td <#if ss gt 1>rowspan="${ss}"</#if>>${dto.allNumber?default(0)}</td>
								<td <#if ss gt 1>rowspan="${ss}"</#if>>${dto.leftNumber?default(0)}</td>
								<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
									<#list dto.gkGroupClassList as groupDto>
										<#if groupDto_index==0>
										<td>
											<a class="btn btn-sm btn-white my2 <#if dto.notexists==1 || groupDto.notexists==1>color-red <#else>color-blue</#if> " href="javascript:void(0)" <#if isCanEdit>onclick="scheduling('${dto.subjectIds!}','${groupDto.id}!')" <#else> onclick="showStu('${dto.subjectIds!}','${groupDto.id}')"</#if>>
												${groupDto.className!}(${groupDto.studentCount?default(0)})
											</a>
										</td>
										
										<td <#if ss gt 1>rowspan="${ss}"</#if>>
											<#if isCanEdit>
												<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${dto.subjectIds!}','3')">快捷开班</a>
												<a class="table-btn color-blue" href="javascript:void(0)" onclick="scheduling('${dto.subjectIds}','')">手动开班</a>
												<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
												<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="editClazz('${dto.subjectIds!}')">编辑</a>
												</#if>
											<#else>
												<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
												<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="showStu('${dto.subjectIds!}','')">查看</a>
												</#if>
											</#if>
										</td>
										<#else>
											<tr data-value="${dto.subjectIds!}" class="comGroup">
												<td>
													<a class="btn btn-sm btn-white my2 color-blue" href="javascript:void(0)" <#if isCanEdit>onclick="scheduling('${dto.subjectIds!}','${groupDto.id}!')" <#else> onclick="showStu('${dto.subjectIds!}','${groupDto.id}')"</#if>>
														${groupDto.className!}(${groupDto.studentCount?default(0)})
													</a>
												</td>
											</tr>
										</#if>
									</#list>
								<#else>
									<td></td>
									<td <#if ss gt 1>rowspan="${ss}"</#if>>
										<#if isCanEdit>
											<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${dto.subjectIds!}','3')">快捷开班</a>
											<a class="table-btn color-blue" href="javascript:void(0)" onclick="scheduling('${dto.subjectIds}','')">手动开班</a>
											<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
											<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="editClazz('${dto.subjectIds!}')">编辑</a>
											</#if>
										<#else>
											<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
												<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="showStu('${dto.subjectIds!}','')">查看</a>
											</#if>
										</#if>
									</td>
								</#if>
								
							</tr>
							</#list>
						</#if>

					</tbody>
				</table>
				
			</div>
			<div class="col-xs-6">
				<h4 class="form-title">
					<b>单科开班</b>
				</h4>
				<table class="table table-striped table-bordered table-hover no-margin">
					<thead>
						<tr>
							<th>学科</th>
							<th>总人数</th>
							<th>未排人数</th>
							<th>分班班级</th>
							<th <#if !isCanEdit>width="60" <#else>width="200"</#if>>操作</th>
						</tr>
					</thead>
					<tbody>
					
						<#if gDtoList3?exists && (gDtoList3?size > 0)>
							<#list gDtoList3 as dto>
							<#assign ss=1>
							<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size gt 0)>
								<#assign ss=dto.gkGroupClassList?size>
							</#if>
							<tr data-value="${dto.subjectIds!}">
								<td <#if ss gt 1>rowspan="${ss}"</#if>>${dto.conditionName!}
									<input type="hidden" class="subIds" value="${dto.subjectIds!}">
								</td>
								<td <#if ss gt 1>rowspan="${ss}"</#if>>${dto.allNumber?default(0)}</td>
								<td <#if ss gt 1>rowspan="${ss}"</#if>>${dto.leftNumber?default(0)}</td>
								<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
									<#list dto.gkGroupClassList as groupDto>
										<#if groupDto_index==0>
										<td>
											<a class="btn btn-sm btn-white my2 <#if dto.notexists==1 || groupDto.notexists==1>color-red <#else>color-blue</#if> " href="javascript:void(0)" <#if isCanEdit>onclick="scheduling('${dto.subjectIds!}','${groupDto.id}!')" <#else> onclick="showStu('${dto.subjectIds!}','${groupDto.id}')"</#if>>
												${groupDto.className!}(${groupDto.studentCount?default(0)})
											</a>
										</td>
										
										<td <#if ss gt 1>rowspan="${ss}"</#if>>
											<#if isCanEdit>
												<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${dto.subjectIds!}','1')">快捷开班</a>
												<a class="table-btn color-blue" href="javascript:void(0)" onclick="scheduling('${dto.subjectIds}','')">手动开班</a>
												<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
												<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="editClazz('${dto.subjectIds!}')">编辑</a>
												</#if>
											<#else>
												<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
												<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="showStu('${dto.subjectIds!}','')">查看</a>
												</#if>
											</#if>
										</td>
										<#else>
											<tr data-value="${dto.subjectIds!}" class="comGroup">
												<td>
													<a class="btn btn-sm btn-white my2 color-blue" href="javascript:void(0)" <#if isCanEdit>onclick="scheduling('${dto.subjectIds!}','${groupDto.id}!')" <#else> onclick="showStu('${dto.subjectIds!}','${groupDto.id}')"</#if>>
														${groupDto.className!}(${groupDto.studentCount?default(0)})
													</a>
												</td>
											</tr>
										</#if>
									</#list>
								<#else>
									<td></td>
									<td <#if ss gt 1>rowspan="${ss}"</#if>>
										<#if isCanEdit>
											<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${dto.subjectIds!}','1')">快捷开班</a>
											<a class="table-btn color-blue" href="javascript:void(0)" onclick="scheduling('${dto.subjectIds}','')">手动开班</a>
											<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
											<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="editClazz('${dto.subjectIds!}')">编辑</a>
											</#if>
										<#else>
											<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
												<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="showStu('${dto.subjectIds!}','')">查看</a>
											</#if>
										</#if>
									</td>
								</#if>
								
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<#-- 底部按钮 -->
<div class="navbar-fixed-bottom opt-bottom">
	<#if !isCanEdit>
   		<a class="btn btn-default" href="javascript:" onclick="reStart()">重新安排</a>
   	</#if>
	<#if haserror>
		<a class="btn btn-blue"   href="javascript:" onclick="errorClear()">异常处理</a>
	<#else>
		<a class="btn btn-blue"   href="javascript:" onclick="nextToArrangeClassNum()" >下一步</a>
	</#if>
   
	

</div>


<script>
	var isDivide = false;
	function intelliDivide() {
		var options = {
			btn: ['确定', '取消'],
			title: '智能分班',
			icon: 1,
			closeBtn: 0
		};
		showConfirm("是否确定根据系统规则开班", options, function () {
			if (isDivide) {
				return;
			}
			isDivide = true;
			$.ajax({
				url: '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/intelliDivide?subjectType=A',
				dataType: 'json',
				type: 'post',
				success: function (data) {
					if (data.success) {
						isDivide = false;
						layer.closeAll();
						refArrange();
					} else {
						isDivide = false;
						layer.closeAll();
						layerTipMsg(data.success, "失败", "原因：" + data.msg);
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					isDivide = false;
				}
			});

		}, function () {
		});
	}

	var setTimeClick;
	$(function(){
		showBreadBack(gobackResult,false,"分班安排");
		<#--仅查看已开班组合-->
		$(".group-open").on('change',function(){
			if($(this).is(':checked')){
				//选中
				$(".group-three").find("tbody tr").each(function(){
					if(!$(this).hasClass("comGroup")){
						if($(this).find(".my2").length>0){
							$(this).show();
						}else{
							$(this).hide();
						}
					}
					
				})
			}else{
				$(".group-three").find("tbody tr").show();
			}
		});
	});
	<#--分班中自动刷新-->
	function refArrange1(){
		if(document.getElementById("groupDiv")){
 			refArrange();
		} else {
			clearTimeout(setTimeClick);
		}
	}
	<#--刷新当前页-->
	function refArrange(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/item';
		$("#showList").load(url);
	}
	
	<#--返回分班方案列表-->
	var isGobackResult=false;
	function gobackResult(){
		if(isGobackResult){
			return;
		}
		isGobackResult=true;
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
		$("#showList").load(url);
	}
	
	//底部操作按钮样式
	if ($(".opt-bottom").length > 0) {
		$(".page-content").css("padding-bottom","77px")
	}
	
	<#--全部解散-->
	var isAllMove=false;
	function deleteAll(){
		if(isAllMove){
			return;
		}
		if(!checkByDivideIdCanEdit()){
			isAllMove=false;
			return;
		}
		isAllMove=true;
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定解散所有班级",options,function(){
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/moveAllGroup',
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
			 			refArrange();
			 		}
			 		else{
			 			isAllMove=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
		},function(){
			isAllMove=false;
		});
		isAllMove=false;
	}
	<#--快捷安排--->
	function quickOpenClass(subjectIds,type){
		if(!checkByDivideIdCanEdit()){
			return;
		}
		var title="";
		var width=600;
		var height=400;
		if("3"==type){
			title="3科组合快捷开班";
		}else if("2"==type){
			title="定2走1快捷开班";
			 width=650;
			 height=480;
		}else if("1"==type){
			title="单科开班";
		}else if("0"==type){
			title="混合组合快捷开班";
			width=650;
			height=700;
		}else{
			return;
		}
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/quickOpenClass/page?subjectIds='+subjectIds+'&type='+type;
		indexDiv = layerDivUrl(url,{title: title,width:width,height:height});
	}
	<#--编辑--->
	function editClazz(subjectIds){
		if(!checkByDivideIdCanEdit()){
			return;
		}
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/editClass/page?subjectIds='+subjectIds;
		indexDiv = layerDivUrl(url,{title: "编辑",width:520,height:520});
	}
	
	<#--手动安排，调整页面-->
	var isEditGroup=false;
	function scheduling(subjectIds,divideClassId){
		if(isEditGroup){
			return;
		}
		isEditGroup=true;
		if(!checkByDivideIdCanEdit()){
			isEditGroup=false;
			return;
		}
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/scheduleIndex?subjectIds='+subjectIds+"&groupClassId="+divideClassId;
		$("#showList").load(url);
	}
	
	<#--多一层验证-->
	function checkByDivideIdCanEdit(){
		var editDate=false;
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/checkByDivideIdCanEdit',
			dataType : 'json',
			type:'post',
			async:false, <#--//同步-->
			success:function(data) {
				var jsonO = data;
				if(jsonO.type=="0"){
					<#--继续下一步操作-->
					editDate=true;
		 		}else if(jsonO.type=="1"){
		 			<#--不能下一步操作-->
		 			layerTipMsg(false,"失败","原因："+jsonO.msg);
		 			refArrange();
		 			editDate=false;
		 		}else if(jsonO.type=="2"){
		 			<#--进入结果-->
	 				layer.msg("已完成分班，自动进入结果！", {
						offset: 't',
						time: 2000
					});
					gobackResultList();
					editDate=false;
		 		}else{
		 			<#--回到首页-->
		 			layerTipMsg(false,"失败","原因："+jsonO.msg);
		 			gobackResult();
		 			editDate=false;
		 		}
		 		
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		
		return editDate;
	}
	
	
	<#--进入分班完成后结果页-->
	function gobackResultList(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultClassList';
		$("#showList").load(url);
	}
	
	<#---下一步-->
	var isCheck=false;
	function nextToArrangeClassNum(){
		if(isCheck){
			return;
		}
		isCheck=true;
		<#if isCanEdit>
			<#--验证-->
			var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/checkAllInGroup';
			$.ajax({
				url:url,
				dataType: "JSON",
				success: function(data){
					if(data.success){
						if("allArrange"==data.msg){
							layer.msg("所有都是三科组合，直接进入结果！", {
								offset: 't',
								time: 2000
							});
							gobackResultList(divideId);
						}else{
							$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/openTeachClass?subjectType=A");
						}
						isCheck=false;
					}else{
						isCheck=false;
						var myError=data.msg;
						if(myError.length>150){
							myError=myError.substring(0,150)+"...";
						}
						layerTipMsg(data.success,"失败",myError);
					}
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}<#--请求出错 -->
			});
		<#else>
			$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/openTeachClass?subjectType=A");
		</#if>
		
	}
	
	var isSys=false;
	function sysnChoose(){
		if(!checkByDivideIdCanEdit()){
			isSys=false;
			return;
		}
		if(isSys){
			return;
		}
		isSys=true;
		var options = {btn: ['更新','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否更新学生选课数据，如果选择更新，可能出现异常数据，学生不应存在某个组合班",options,function(){
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/sysStuChoose',
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
			 			layer.msg("操作成功！", {
							offset: 't',
							time: 2000
						});
			 			refArrange();
			 		}
			 		else{
			 			isSys=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
		},function(){
			isSys=false;
		});
		isSys=false;
	}
	
	
	
	<#--纯查看-->
	var isShowStu=false;
	function showStu(subjectIds,divideClassId){
		if(isShowStu){
			return;
		}
		isShowStu=true;
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/showStu/page?subjectIds='+subjectIds+"&divideClassId="+divideClassId;
		$("#showList").load(url);
	}
	var isReS=false;
	function reStart(){
		if(isReS){
			return;
		}
		isReS=true;
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否需要重新安排，如果选择确定，将会清除后续所有产生的数据",options,function(){
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/clearOpenNext',
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
			 			layer.msg("操作成功！", {
							offset: 't',
							time: 2000
						});
			 			refArrange();
			 		}
			 		else{
			 			isSys=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
		},function(){
			isReS=false;
		});
		isReS=false;
	}
	var isCanRemoveNot=false;
	function errorClear(){
		if(!checkByDivideIdCanEdit()){
			ss=false;
			return;
		}
		if(isCanRemoveNot){
			return;
		}
		isCanRemoveNot=true;
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/clearNotPerArrange',
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
				  	refArrange();
		 		}
		 		else{
		 			isCanRemoveNot=false;
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	
	
		<#--全部解散-->
	var isAllMove=false;
	function deleteAll(){
		if(isAllMove){
			return;
		}
		if(!checkByDivideIdCanEdit()){
			isAllMove=false;
			return;
		}
		isAllMove=true;
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定解散所有班级",options,function(){
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/moveAllGroup',
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
			 			refArrange();
			 		}
			 		else{
			 			isAllMove=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
		},function(){
			isAllMove=false;
		});
		isAllMove=false;
	}
	
	
</script>