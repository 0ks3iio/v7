<#--<a href="javascript:void(0)" onclick="gobackResult()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>-->
<link rel="stylesheet" href="${request.contextPath}/static/components/jQuerycxColor/css/jquery.cxcolor.css">
<script src="${request.contextPath}/static/components/jQuerycxColor/js/jquery.cxcolor.js"></script>
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${divideName!}-手动开班</h3>
	</div>
	<div class="box-body" id="groupDiv">
		<div class="explain-default clearfix">
			<ul class="gk-student-stat">
				<li><span>选课学生：<em>${chosenStudentNum!}</em>人</span></li>
				<li><span>已安排：<em>${fixStudentNum!}</em>人</span></li>
				<li><span>未安排：<em>${noFixStudentNum!}</em>人</span></li>
			</ul>
		</div>
		<#if openType?default('')!='06'>
			<div class="filter">
			<#if openType?default('')=='01'>
				<div class="filter-item">
					<#if !isAutoTwo>
						<a class="btn btn-blue" <#if isCanEdit> onclick="makeSix()" <#else> disabled</#if>>智能分班</a>
					<#else>
						<span class="color-blue" id="showMessId">
						 	<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">
								正在智能分班中，请稍等．．．
						 </span>
					</#if>
					
					<em>智能分配将学生放在组合班级内</em>
				</div>
			</#if>
			<#if isCanEdit>
				<div class="filter-item filter-item-right">
					<a class="btn btn-blue"  onclick="deleteAll()">全部解散</a>
					<#--<a class="btn btn-blue"  onclick="gotoDivideXZB()">进入智能分班</a>-->
				</div>
			</#if>
			</div>
			<#if isShowAll><p class="tip"><em>*提醒：务必将学生完全分配各个组合，可以是混合班</em></p></#if>
		</#if>
		
		<div class="row" style="margin-left:5px;margin-right:5px;">
			<div class="col-sm-6">
				<h3>三科组合情况</h3>
				<table class="table table-bordered table-striped table-hover " id="group3">
					<thead>
						<tr>
							<th>组合</th>
							<th>总人数</th>
							<th>剩余人数</th>
							<th>手动排班班级</th>
							<th style="width:20%;">操作</th>
						</tr>
					</thead>
					<tbody>
						<#if gDtoList?exists && (gDtoList?size > 0)>
							<#list gDtoList as dto>
							<tr>
								<td <#if dto.notexists==1>class="color-red"</#if>>${dto.conditionName!}
								<input type="hidden" class="subIds" value="${dto.subjectIds!}">
								<#if dto.colorList?? && dto.colorList?size gt 0>
								<#list dto.colorList as cor>
									<span class="fill" id="title" style="background-color: ${cor.color!};"></span>
								</#list>
								</#if>
								</td>
								<td>${dto.allNumber?default(0)}</td>
								<td>${dto.leftNumber?default(0)}</td>
								<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
									<td>
										<#list dto.gkGroupClassList as groupDto>
											<#if groupDto_index!=0>
												、
											</#if>
											<#if isCanEdit>
												<a href="javascript:" onclick="scheduling('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.className!}(${groupDto.studentCount?default(0)})</a>
											<#else>
												<a href="javascript:" onclick="showStu('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.className!}(${groupDto.studentCount?default(0)})</a>
											</#if>
										</#list>
									</td>
								<#else>
									<td>无手动排班</td>
								</#if>
								<td>
								<#if isCanEdit>
									<#if dto.notexists==1>
										<a href="javascript:" onclick="moveGroup('${dto.subjectIds!}','','${dto.conditionName!}')">解散组合</a>
									<#else>
										<a href="javascript:" onclick="scheduling('${dto.subjectIds!}','')">手动排班</a>
										<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
											<a href="javascript:" onclick="moveGroup('${dto.subjectIds!}','','${dto.conditionName!}')">解散组合</a>
										</#if>
									</#if>
								<#else>
									<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
										<a href="javascript:" onclick="showStu('${dto.subjectIds!}','')">查看</a>
									</#if>
								</#if>
								</td>
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<#if isShowAll>
				<#if openType?default('')=='06'>
				</div>
				<div class="col-sm-6">
				</#if>
				<h3>混合组合情况</h3>
				<table class="table table-bordered table-striped table-hover ">
					<thead>
						<tr>
							<th>组合</th>
							<th>总人数</th>
							<th>剩余人数</th>
							<th>手动排班班级</th>
							<th class="noprint" style="width:20%;">操作</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${gDto.conditionName!}</td>
							<td>${gDto.allNumber?default(0)}</td>
							<td>${gDto.leftNumber?default(0)}</td>
							<#if (gDto.gkGroupClassList?exists) && (gDto.gkGroupClassList?size > 0)>
								<td>
									<#list gDto.gkGroupClassList as groupDto>
										<#if groupDto_index!=0>
											、
										</#if>
										<#if isCanEdit>
											<a href="javascript:" onclick="scheduling('${gDto.subjectIds!}','${groupDto.id}')" >${groupDto.className!}(${groupDto.studentCount?default(0)})</a>
										<#else>
											<a href="javascript:" onclick="showStu('${gDto.subjectIds!}','${groupDto.id}')" >${groupDto.className!}(${groupDto.studentCount?default(0)})</a>
										</#if>
									</#list>
								</td>
							<#else>
								<td>无手动排班</td>
							</#if>
							<td class="noprint">
							<#if isCanEdit>
								<a href="javascript:" onclick="scheduling('${gDto.subjectIds!}','')">手动排班</a>
								<#if (gDto.gkGroupClassList?exists) && (gDto.gkGroupClassList?size > 0)>
									<a href="javascript:" onclick="moveGroup('${gDto.subjectIds!}','','${gDto.conditionName!}')">解散组合</a>
								</#if>
							<#else>
								<#if (gDto.gkGroupClassList?exists) && (gDto.gkGroupClassList?size > 0)>
									<a href="javascript:" onclick="showStu('${gDto.subjectIds!}','')">查看</a>
								</#if>
							</#if>
							</td>
						</tr>
					</tbody>
				</table>
				</#if>
			</div>
			<#if openType?default('')!='06'>
			<div class="col-sm-6">
				<h3>两科组合情况</h3>
				<#assign hasColor = false>
				<#if gDtoList2?exists && (gDtoList2?size > 0)>
					<#list gDtoList2 as dto>
						<#if dto.gkGroupClassList?? && dto.gkGroupClassList?size gt 0><#assign hasColor = true><#break></#if>
					</#list>
				</#if>
				<table class="table table-bordered table-striped table-hover " id="group2">
					<thead>
						<tr>
							<#if hasColor>
							<th class="cortd"></th>
							</#if>
							<th>组合</th>
							<th>总人数</th>
							<th>剩余人数</th>
							<th>手动排班班级</th>
							<th style="width:20%;">操作</th>
						</tr>
					</thead>
					<tbody>
					<#if gDtoList2?exists && (gDtoList2?size > 0)>
						<#list gDtoList2 as dto>
						<tr>
							<#if hasColor>
							<td>
								<#if dto.colorList?? && dto.colorList?size gt 0>
								<#list dto.colorList as cor>
									<input class="corId" type="hidden" value="${cor.id!}">
									<input <#if !(dto.gkGroupClassList??) || (dto.gkGroupClassList?size lt 1)>style="display:none;"</#if> class="input_cxcolor" type="text" value="${cor.color!}" readonly>
								</#list>
								</#if>
							</td>
							</#if>
							<td <#if dto.notexists==1>class="color-red"</#if>>
							<input type="hidden" class="subIds" value="${dto.subjectIds!}">
							${dto.conditionName!}
							</td>
							<td>${dto.allNumber?default(0)}</td>
							<td>${dto.leftNumber?default(0)}</td>
							<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
								<td>
									<#list dto.gkGroupClassList as groupDto>
										<#if groupDto_index!=0>
											、
										</#if>
										<#if isCanEdit>
											<a href="javascript:" onclick="scheduling('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.className!}(${groupDto.studentCount?default(0)})</a>
										<#else>
											<a href="javascript:" onclick="showStu('${dto.subjectIds!}','${groupDto.id}')" <#if dto.notexists==1 || groupDto.notexists==1>class="color-red"</#if>>${groupDto.className!}(${groupDto.studentCount?default(0)})</a>
										</#if>
									</#list>
								</td>
							<#else>
								<td>无手动排班</td>
							</#if>
							<td>
							<#if isCanEdit>
								<#if dto.notexists==1>
									<a href="javascript:" onclick="moveGroup('${dto.subjectIds!}','','${dto.conditionName!}')">解散组合</a>
								<#else>
									<a href="javascript:" onclick="scheduling('${dto.subjectIds!}','')">手动排班</a>
									<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
										<a href="javascript:" onclick="moveGroup('${dto.subjectIds!}','','${dto.conditionName!}')">解散组合</a>
									</#if>
								</#if>
							<#else>
								<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
									<a href="javascript:" onclick="showStu('${dto.subjectIds!}','')">查看</a>
								</#if>
							</#if>
							</td>
						</tr>
						</#list>
					</#if>
				</tbody>
			</table>
		</div>
		</#if>
		<div class="col-xs-12">
			<#if haserror>
				<#if isCanEdit>
				<em>说明：红色组合：该组合已经不存在，请进行解散操作。红色班级：该班级下存在学生选择组合与该班级科目组合不同。</em>
				<#else>
				<em>说明：红色组合：该组合已经不存在。红色班级：该班级下存在学生选择组合与该班级科目组合不同。在清除后续操作数据后，可进行修改</em>
				</#if>
			</#if>
			<#--
				<div class="text-center">
					<#if isShowAll>
					 	<a class="btn btn-blue" id="autoAllClassId" <#if haserror || !isCanEdit>disabled</#if> href="javascript:" onclick="autoAll('${divideId!}')">开始分班</a>
						<span class="color-blue" id="showMessId">
						 	<#if !isCanEdit><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">
								正在分班中，请稍等．．．
							</#if>
						 </span>
					<#else>
					 	<a class="btn btn-blue"  <#if haserror>disabled</#if> href="javascript:" onclick="nextToArrangeClassNum('${divideId!}')">下一步</a>
					</#if>
				</div>
			-->
		</div>
	</div>
</div>

<div class="navbar-fixed-bottom opt-bottom">
    <#if isCanEdit && haserror>
		<a class="btn btn-blue" id="errorHref" href="javascript:" onclick="removeNot()">异常清除</a>
	</#if>
    <a class="btn btn-blue"  
	 	
		<#if haserror || isAutoTwo>
			<#if haserror && !isAutoTwo && !isCanEdit>
				href="javascript:"  
				<#if openType?default('')=='06' || openType?default('')=='02'>
					onclick="nextToArrangeClassNum('${divideId!}','4')" 
				<#else>
					onclick="nextToArrangeClassNum('${divideId!}','2')" 
				</#if>
			<#else>
				disabled
			</#if>
		<#else>
			href="javascript:"  <#if openType?default('')=='06' || openType?default('')=='02'>onclick="nextToArrangeClassNum('${divideId!}','3')" 
	 		<#else>
	 			<#if isCanEdit> onclick="nextToArrangeClassNum('${divideId!}','1')" <#else> onclick="nextToArrangeClassNum('${divideId!}','2')" </#if>
	 		</#if>
		</#if> >下一步</a>
</div>
<#if true>
<#--
<div class="layer layer-six-course">
	<div class="layer-content">
		<em>请输入行政班数量(包括已经安排的3+0与2+x)</em>
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">行政班数量:</span>
				<div class="filter-content">
					<input type="text" class="form-control pull-left" name="xzbNum" id="xzbNum" nullable="false" vtype="int" maxLength="3" min="1" value="${xzbNum}"/>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">浮动人数:</span>
				<div class="filter-content">
					<input type="text" class="form-control pull-left" name="marginNum" id="marginNum" nullable="false" vtype="int" maxLength="2" min="0" value="${divide.maxGalleryful?default(0)}"/>
				</div>
			</div>
		</div>
		<em>提示：全固定组合形式，考虑到将2+x中x在同个时间上课，分班需要受教师数量影响</em>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>科目</th>
					<th>可提供教师数(0:代表不受限制，教师充足)</th>
				</tr>
			</thead>
			<tbody>
			<#if courseList?exists && courseList?size gt 0>
			<#list courseList as course>
				<tr>
					<td>${course.subjectName!}</td>
					<td class="corseTeacherNum">
						<input type="hidden" id="course_${course.id}" name="courseId" value="${course.id!}">
						<input type="text" class="form-control pull-left" name="teacherNum" id="course_t_${course.id!}" nullable="false" vtype="int" maxLength="2" value="0"/>
					</td>
				</tr>
			</#list>
			</#if>
			</tbody>
			
		</table>
	</div>
</div>
-->
<div class="layer layer-six-course">
	<div class="layer-content">
		<em>请输入行政班数量(包括已经安排的3+0与2+x)</em>
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">行政班数量:</span>
				<div class="filter-content">
					<input type="text" class="form-control pull-left" name="xzbNum" id="xzbNum" nullable="false" vtype="int" maxLength="3" min="1" value="${xzbNum}"/>
				</div>
			</div>
		</div>
	</div>
</div>

</#if>

<script>
	var setTimeClick;
	$(function(){
		showBreadBack(gobackResult,false,"分班安排");
		<#if openType?default('')=='01' && isAutoTwo>
			setTimeClick=setTimeout("refArrange1()",30000);
		</#if>
	});
	function refArrange1(){
		if(document.getElementById("groupDiv")){
 			refArrange();
		} else {
			clearTimeout(setTimeClick);
		}
	}
	
	(function(){
		//var color=$("#color");
		$(".input_cxcolor").each(function(){
			$(this).cxColor();
		});
		$(".input_cxcolor").bind("change",function(){
			var now_cor = this.value;
			var subIds = $(this).parents("tr").find(".subIds").val().split(",");
			var id = $(this).prev(".corId").val();
			//console.log("now_cor="+now_cor+" subIds="+subIds+" id = "+id);
			changeColor(id,now_cor,subIds);
		});
		
		function changeColor(id,now_cor,subIds){
			if(!now_cor)
				now_cor = $.cxColor.defaults.color;
				//now_cor = "#000000";
		
			var url =  '${request.contextPath}/newgkelective/subjectGroupColor/changeColor';
			$.ajax({
				url:url,
				data:{"id":id,"color":now_cor,"subjectIds":subIds,"groupType":"2"},
				dataType: "JSON",
				success: function(data){
					if(data.success){
						layer.msg("操作成功！", {
							offset: 't',
							time: 2000
						});
						refArrange();
					}else{
						layerTipMsg(data.success,"失败",data.msg);
					}
				},
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}
			});<#--请求出错 -->
		};
	})();
	
	function checkByDivideIdCanEdit(){
		var editDate=false;
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideGroup/checkByDivideIdCanEdit',
			dataType : 'json',
			type:'post',
			async:false, <#--//同步-->
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			if("分班已完成"==jsonO.msg){
		 				layer.msg("已完成分班，自动进入结果！", {
							offset: 't',
							time: 2000
						});
						gobackResultList();
						editDate=false;
		 			}else{
		 				//继续下一步操作
		 				editDate=true;
		 			}
		 		}else{
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
		 			refArrange();
		 			editDate=false;
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		
		return editDate;
	}

	<#--返回分班方案列表-->
	var isGobackResult=false;
	function gobackResult(){
		if(isGobackResult){
			return;
		}
		isGobackResult=true;
		var url =  '${request.contextPath}/newgkelective/${gradeId!}/goDivide/index/page';
		<#if fromArray?default('') == '1'>
			<#if arrayId?default('')==''>
			   url = '${request.contextPath}/newgkelective/${gradeId!}/goArrange/addArray/page?divideId=${divideId!}&lessArrayId=${lessArrayId!}&plArrayId=${plArrayId!}';
		   <#else>
			   url = '${request.contextPath}/newgkelective/${gradeId!}/goArrange/editArray/page?arrayId=${arrayId!}';
		   </#if>
		</#if>		
		$("#showList").load(url);
	}
	<#--进入结果页 --这个不是点击事件，无需添加防止重复-->
	function gobackResultList(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultClassList';
		$("#showList").load(url);
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
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/groupDetail/page?subjectIds='+subjectIds+"&groupClassId="+divideClassId;
		$("#showList").load(url);
	}
	<#--异常清除-->
	var isCanRemoveNot=false;
	function removeNot(){
		if(!checkByDivideIdCanEdit()){
			ss=false;
			return;
		}
		if(isCanRemoveNot){
			return;
		}
		isCanRemoveNot=true;
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideGroup/clearNotPerArrange',
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
		isAllMove=true;
		moveGroup("","","");
		isAllMove=false;
	}
	
	<#--解散-->
	var mm=false;
	function moveGroup(subjectIds,groupClassId,conditionName){
		if(!checkByDivideIdCanEdit()){
			return;
		}
		if(mm){
			return;
		}
		mm=true;
		var mess="";
		if(subjectIds==""){
			mess="是否确定解散所有班级";
		}else{
			mess="是否确定解散<span style='color:red'>"+conditionName+"</span>组合下所有班级";
		}
		
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
			showConfirm(mess,options,function(){
			$.ajax({
				url:'${request.contextPath}/newgkelective/${divideId!}/divideGroup/moveGroup',
				data:{"subjectIds":subjectIds},
				dataType : 'json',
				type:'post',
				success:function(data) {
					var jsonO = data;
			 		if(jsonO.success){
			 			layer.closeAll();
			 			setTimeout(function(){refArrange();},600);
					  	
			 		}
			 		else{
			 			mm=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
					}
				},
		 		error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
				
			},function(){
				mm=false;
			});
	}
	
	
	<#--纯查看-->
	var isShowStu=false;
	function showStu(subjectIds,divideClassId){
		if(isShowStu){
			return;
		}
		isShowStu=true;
		var url = '${request.contextPath}/newgkelective/${divideId!}/divideGroup/showStu/page?subjectIds='+subjectIds+"&divideClassId="+divideClassId;
		$("#showList").load(url);
	}
	
	<#--刷新当前页-->
	function refArrange(){
		var url =  '${request.contextPath}/newgkelective/${divideId!}/divideGroup/groupIndex/page';
		$("#showList").load(url);
	}
	
	
	
	
	<#--
	//分班
	function autoAll(divideId){
		if(document.getElementById("errorHref")){
 			layer.msg("请先点击异常清除，去除异常数据！", {
				offset: 't',
				time: 2000
			});
		} else {
			//没有异常处理
			autoAllClass(divideId);
		}
	}
	
	var isAuto=false;
	function autoAllClass(divideId){
		editDate=false;
		if(isAuto){
			$("#autoAllClassId").addClass("disabled");
			return;
		}
		if($("#autoAllClassId").hasClass("disabled")){
			isAuto=true;
			return;
		}
		isAuto=true;
		$("#autoAllClassId").addClass("disabled");
		var text='<img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">'
					+'正在分班中，请稍等．．．';
		$("#showMessId").html(text);
		//分班代码
		autoAllClass2(divideId,"1");
	}
	
	function autoAllClass2(divideId,indexStr){
	   // var urlStr="${request.contextPath}/newgkelective/"+divideId+"/divideClass/autoDivideClass";
	    var urlStr="${request.contextPath}/newgkelective/"+divideId+"/divideClass/autoDivideClass";
		$.ajax({
			url:urlStr,
			data:{"divideId":divideId},
			dataType: "json",
			success: function(data){
				if(data.stat=="success"){
					//进入结果
					gobackResultList(divideId);
	 			}else if(data.stat=="error"){
	 				if(indexStr=="1"){
	 					//上次失败进入分班
	 					autoAllClass2(divideId,"0");
	 				}else{
	 					$("#autoAllClassId").removeClass("disabled");
		 				isAuto=false;
		 				editDate=true;
		 				$("#showMessId").html(data.message);
	 				}
	 			}else{
	 				//不循环访问结果--直接进入首页autoAllClass2
	 				gobackResult(divideId);
	 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	-->
	function makeSix(){
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '设置',
			//area: ['620px', '600px'],
			area: ['400px', '250px'],
			btn: ['确定', '取消'],
			content: $('.layer-six-course'),
			yes:function(index,layero){
				//makeSix2();
				makeSix2New();
			}
		})
	}
	
	
	var isMakeSix=false;
	
	function makeSix2New(){
		if(isMakeSix){
			return;
		}
		isMakeSix=true;
		var check = checkValue('.layer-six-course');
		if(!check){
	 		isMakeSix=false;
	 		return;
		}
		var xzbNum=$("#xzbNum").val().trim();
		var ii = layer.load();
		repeatSaveSixNew(xzbNum,"1");
	}
	function repeatSaveSixNew(xzbNum,sixIndex){
		$.ajax({
			url:"${request.contextPath}/newgkelective/${divideId!}/divideGroup/saveAutoSixNew",
			data:{'xzbNum':xzbNum,},
			dataType: "json",
			success: function(data){
					if(data.stat=="success"){
					 	layer.closeAll();
						<#--进入结果-->
						layer.msg("智能分班成功！", {
							offset: 't',
							time: 2000
						});
						refArrange();
		 			}else if(data.stat=="error"){
		 				if(sixIndex=="1"){
		 					<#--上次失败进入分班-->
		 					repeatSaveSix(divideId,"0");
		 				}else{
		 					layer.closeAll();
			 				isMakeSix=false;
			 				layerTipMsg(false,"失败","原因："+data.message);
			 				refArrange();
		 				}
		 			}else{
		 				layer.closeAll();
						gobackResult();
		 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	
	function makeSix2(){
		if(isMakeSix){
			return;
		}
		isMakeSix=true;
		var check = checkValue('.layer-six-course');
		if(!check){
	 		isMakeSix=false;
	 		return;
		}
		var courseTeachNum="";
		$(".layer-six-course").find(".corseTeacherNum").each(function(){
			var courseId=$(this).find("input[name=courseId]").val();
			var teacherNum=$(this).find("input[name=teacherNum]").val();
			if(courseTeachNum==""){
				courseTeachNum=courseId+"_"+teacherNum;
			}else{
				courseTeachNum=courseTeachNum+","+courseId+"_"+teacherNum;
			}
		})
		var xzbNum=$("#xzbNum").val().trim();
		var marginNum=$("#marginNum").val().trim();
		var ii = layer.load();
		repeatSaveSix(courseTeachNum,xzbNum,marginNum,"1");
	}
	
	
	function repeatSaveSix(courseTeachNum,xzbNum,marginNum,sixIndex){
		$.ajax({
			url:"${request.contextPath}/newgkelective/${divideId!}/divideGroup/saveAutoSix",
			data:{'courseTeachNum':courseTeachNum,'xzbNum':xzbNum,'marginNum':marginNum},
			dataType: "json",
			success: function(data){
					if(data.stat=="success"){
					 	layer.closeAll();
						<#--进入结果-->
						layer.msg("智能分班成功！", {
							offset: 't',
							time: 2000
						});
						refArrange();
		 			}else if(data.stat=="error"){
		 				if(sixIndex=="1"){
		 					<#--上次失败进入分班-->
		 					repeatSaveSix(divideId,"0");
		 				}else{
		 					layer.closeAll();
			 				isMakeSix=false;
			 				layerTipMsg(false,"失败","原因："+data.message);
			 				refArrange();
		 				}
		 			}else{
		 				
		 				setTimeout(function(){
							repeatSaveSix(courseTeachNum,xzbNum,marginNum,"0");
						},60000)
		 				
		 				<#--循环访问
		 				//repeatSaveSix(courseTeachNum,xzbNum,marginNum,"0");
		 				//setTimeout(function(){
							//refArrange();
							
						//},5000)-->
		 				
		 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	
	
	
	
	
	var isCheck=false;
	function nextToArrangeClassNum(divideId,type){
		if(isCheck){
			return ;
		}
		isCheck=true;
		if("2"==type){
			<#if openType?default('')=='01'>
				<#--自动安排A-->
				$("#showList").load("${request.contextPath}/newgkelective/BathDivide/"+divideId+"/arrangeAList/page");
			<#else>
				$("#showList").load("${request.contextPath}/newgkelective/BathDivide/"+divideId+"/singleList2/page");
			</#if>
			isCheck=false;
			return;
		}else if("4"==type){
			$("#showList").load("${request.contextPath}/newgkelective/clsBatch/"+divideId+"/index");
			isCheck=false;
			return;
		}
		
		<#--验证-->
		var url =  '${request.contextPath}/newgkelective/BathDivide/'+divideId+'/openClassArrange/checkAllInGroup';
		$.ajax({
			url:url,
			dataType: "JSON",
			success: function(data){
				if(data.success){
					if("allArrange"==data.msg){
						gobackResultList(divideId);
					}else{
						if("3"==type){
							$("#showList").load("${request.contextPath}/newgkelective/clsBatch/"+divideId+"/index");
							<#--
							$("#showList").load("${request.contextPath}/newgkelective/BathDivide/"+divideId+"/singleList3/page");
							-->
						}else{
							<#if openType?default('')=='01'>
								<#--自动安排A-->
								$("#showList").load("${request.contextPath}/newgkelective/BathDivide/"+divideId+"/arrangeAList/page");
							<#else>
								$("#showList").load("${request.contextPath}/newgkelective/BathDivide/"+divideId+"/singleList2/page");
							</#if>
							
						}
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
	}
	<#if openType?default('')=='01'>
	function gotoDivideXZB(){
		$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/section2x/index/page");
	} 
	</#if>
	
	
	$("#group3 tbody tr").mouseenter(function(){
		var subIds = $(this).find(".subIds").val();
		var subIdArr = subIds.split(",");
		var s1 = subIdArr[0]+","+subIdArr[1];
		var s2 = subIdArr[0]+","+subIdArr[2];
		var s3 = subIdArr[1]+","+subIdArr[2];
		
		var colorstr = "#fee8ba"	// 橙色
		$(".subIds[value='"+s1+"']").parents("tr").css("backgroundColor",colorstr);
		$(".subIds[value='"+s2+"']").parents("tr").css("backgroundColor",colorstr);
		$(".subIds[value='"+s3+"']").parents("tr").css("backgroundColor",colorstr);
	});
	$("#group3 tbody tr").mouseleave(function(){
		var subIds = $(this).find(".subIds").val();
		var subIdArr = subIds.split(",");
		var s1 = subIdArr[0]+","+subIdArr[1];
		var s2 = subIdArr[0]+","+subIdArr[2];
		var s3 = subIdArr[1]+","+subIdArr[2];
		var colorstr = "#ffffff"	// 白色
		$(".subIds[value='"+s1+"']").parents("tr").css("backgroundColor", colorstr);
		$(".subIds[value='"+s2+"']").parents("tr").css("backgroundColor", colorstr);
		$(".subIds[value='"+s3+"']").parents("tr").css("backgroundColor", colorstr);
	});
</script>