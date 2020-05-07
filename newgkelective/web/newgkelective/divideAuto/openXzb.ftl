<link rel="stylesheet" href="${request.contextPath}/static/components/jQuerycxColor/css/jquery.cxcolor.css">
<script src="${request.contextPath}/static/components/jQuerycxColor/js/jquery.cxcolor.js"></script>
<div class="stepsContainer">
	<ul class="steps-default clearfix">
		<li class="active"><span><i>1</i>分行政班</span></li>
		<li><span><i>2</i>分选考班</span></li>
		<li><span><i>3</i>分学考班</span></li>
	</ul>
</div>
<div class="box box-default">
	<div class="box-body">
		<div class="filter" id="groupDiv">
			<#if isCanEdit>
				<div class="filter-item">
					<div class="filter-content">
						<a class="btn btn-blue js-group" href="javascript:" onclick="auto2xFun()">智能分班</a>
						<a class="btn btn-default" href="javascript:" onclick="deleteAll()">全部解散</a>
						<a class="btn btn-default" href="javascript:" onclick="sysnChoose()">更新选课数据</a>
					</div>
				</div>
			<#else>
				<#if isAuto2>
					<div class="filter-item">
						
						<span class="filter-name"><img src="${request.contextPath}/static/images/icons/icon-loading-blue.gif" alt="" width="28">正在分班中...</span>
					</div>
				</#if>
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
					<b>3科组合开班</b>
				</h4>
				<table class="table table-striped table-bordered table-hover" id="group3">
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
							<tr>
								<td>${dto.conditionName!}
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
												<a class="btn btn-sm btn-white my2 <#if dto.notexists==1 || groupDto.notexists==1>color-red <#else>color-blue</#if> " href="javascript:void(0)" <#if isCanEdit>onclick="scheduling('${dto.subjectIds!}','${groupDto.id}')"<#else> onclick="showStu('${dto.subjectIds!}','${groupDto.id}')"</#if>  >
													${groupDto.className!}(${groupDto.studentCount?default(0)})
												</a>
										</#list>
									</td>
								<#else>
									<td></td>
								</#if>
								<td>
									<#if isCanEdit>
										<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${dto.subjectIds!}','3')">快捷开班</a>
										<a class="table-btn color-blue" href="javascript:void(0)" onclick="scheduling('${dto.subjectIds!}','')">手动开班</a>
										<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
										<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="editClazz('${dto.subjectIds!}')">编辑</a>
										</#if>
									<#else>
										<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
											<a class="table-btn color-blue js-edit" href="javascript:void(0)"  onclick="showStu('${dto.subjectIds!}','')">查看</a>
										</#if>
									</#if>
								</td>
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<h4 class="form-title">
					<b class="mr20">定1走2开班</b>
					<label class="pos-rel color-blue font-12 no-margin">
						<input type="checkbox" class="wp group-open"  value="2">
						<span class="lbl"> 仅查看已开班组合</span>
					</label>
				</h4>
				<table class="table table-striped table-bordered table-hover no-margin group-two">
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
							<tr>
								<td>${dto.conditionName!}
									<input type="hidden" class="subIds" value="${dto.subjectIds!}">
								</td>
								<td>${dto.allNumber?default(0)}</td>
								<td>${dto.leftNumber?default(0)}</td>
								<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
								<td>
									<#list dto.gkGroupClassList as groupDto>
											<a class="btn btn-sm btn-white my2 <#if dto.notexists==1 || groupDto.notexists==1>color-red <#else>color-blue</#if> " href="javascript:void(0)" <#if isCanEdit>onclick="scheduling('${dto.subjectIds!}','${groupDto.id}!')" <#else> onclick="showStu('${dto.subjectIds!}','${groupDto.id}')"</#if>>
												${groupDto.className!}(${groupDto.studentCount?default(0)})
											</a>
									</#list>
								</td>
								<#else>
									<td></td>
								</#if>
								<td>
									<#if isCanEdit>
										<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${dto.subjectIds!}','1')">快捷开班</a>
										<a class="table-btn color-blue" href="javascript:void(0)" onclick="scheduling('${dto.subjectIds!}','')">手动开班</a>
										<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
										<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="editClazz('${dto.subjectIds!}')">编辑</a>
										</#if>
									<#else>
										<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size > 0)>
										<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="showStu('${dto.subjectIds!}','')">查看</a>
										</#if>
									</#if>
								</td>
							</tr>
							</#list>
						</#if>
					</tbody>
				</table>
			</div>
			<div class="col-xs-6">
				<h4 class="form-title">
					<b class="mr20">定2走1开班</b>
					<label class="pos-rel color-blue font-12 no-margin">
						<input type="checkbox" class="wp group-open" name="" value="1">
						<span class="lbl"> 仅查看已开班组合</span>
					</label>
				</h4>
				<table class="table table-striped table-bordered table-hover group-one" id="group2">
					<thead>
						<tr>
							<th>组合名称</th>
							<th>颜色</th>
							<th>总人数</th>
							<th>未排人数</th>
							<th>分班班级</th>
							<th>选课组合</th>
							<th <#if !isCanEdit>width="60"<#else>width="200"</#if>>操作</th>
						</tr>
					</thead>
					<tbody>
						<#if gDtoList2?exists && (gDtoList2?size > 0)>
							<#list gDtoList2 as dto>
							<#assign ss=1>
							<#if (dto.gkGroupClassList?exists) && (dto.gkGroupClassList?size gt 0)>
								<#assign ss=dto.gkGroupClassList?size>
							</#if>
							<tr data-value="${dto.subjectIds!}">
								<td <#if ss gt 1>rowspan="${ss}"</#if>>${dto.conditionName!}
									<input type="hidden" class="subIds" value="${dto.subjectIds!}">
								</td>
								<td <#if ss gt 1>rowspan="${ss}"</#if>>
									<#if dto.colorList?? && dto.colorList?size gt 0>
									<#list dto.colorList as cor>
										<input class="corId" type="hidden" value="${cor.id!}">
										<input <#if !(dto.gkGroupClassList??) || (dto.gkGroupClassList?size lt 1)>style="display:none;"</#if> class="input_cxcolor" type="text" value="${cor.color!}" readonly>
									</#list>
									</#if>
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
										<td>
											<#if (groupDto.stuNumBySubList?exists) && (groupDto.stuNumBySubList?size > 0)>
												<#list groupDto.stuNumBySubList as iik>
													<div>${iik!}</div>
												</#list>
											</#if>
										</td>
										<td <#if ss gt 1>rowspan="${ss}"</#if>>
											<#if isCanEdit>
												<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${dto.subjectIds!}','2')">快捷开班</a>
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
												<td>
													<#if (groupDto.stuNumBySubList?exists) && (groupDto.stuNumBySubList?size > 0)>
														<#list groupDto.stuNumBySubList as iik>
															<#if iik_index gt 0>
															<br/>
															</#if>
															${iik!}
														</#list>
													</#if>
												</td>
											</tr>
										</#if>
									</#list>
								<#else>
									<td></td>
									<td></td>
									<td <#if ss gt 1>rowspan="${ss}"</#if>>
										<#if isCanEdit>
											<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${dto.subjectIds!}','2')">快捷开班</a>
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
				<h4 class="form-title">
					<b class="mr20">混合</b>
				</h4>
				<table class="table table-striped table-bordered table-hover no-margin">
					<thead>
						<tr>
							<th>组合名称</th>
							<th>总人数</th>
							<th>未排人数</th>
							<th>分班班级</th>
							<th <#if !isCanEdit>width="60" <#else>width="200"</#if>>操作</th>
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
									<a class="btn btn-sm btn-white my2  <#if gDto.notexists==1 || groupDto.notexists==1>color-red <#else> color-blue </#if>" href="javascript:void(0)" <#if isCanEdit>onclick="scheduling('${gDto.subjectIds}','${groupDto.id!}')" <#else> onclick="showStu('${gDto.subjectIds!}','${groupDto.id}')" </#if>  >
										${groupDto.className!}(${groupDto.studentCount?default(0)})
									</a>
								</#list>
							</td>
							<#else>
								<td></td>
							</#if>
							<td>
								<#if isCanEdit>
									<a class="table-btn color-blue js-quick-start-three-group" href="javascript:void(0)" onclick="quickOpenClass('${gDto.subjectIds!}','0')">快捷开班</a>
									<a class="table-btn color-blue" href="javascript:void(0)" onclick="scheduling('${gDto.subjectIds}','')">手动开班</a>
									<#if (gDto.gkGroupClassList?exists) && (gDto.gkGroupClassList?size > 0)>
									<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="editClazz('${gDto.subjectIds!}')">编辑</a>
									</#if>
								<#else>
									<#if (gDto.gkGroupClassList?exists) && (gDto.gkGroupClassList?size > 0)>
									<a class="table-btn color-blue js-edit" href="javascript:void(0)" onclick="showStu('${gDto.subjectIds!}','')">查看</a>
									</#if>
								</#if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<div class="layer layer-group">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group" id="auto2x">
				<label class="col-sm-4 control-label no-padding-right">行政班开班数：</label>
				<div class="col-sm-3">
					<div class="input-group form-num" data-step="1">
                        <input class="form-control" type="text" name="openClassnum" id="openClassnum" value="${xzbNum?default(1)}">
                        <span class="input-group-btn">
                            <button class="btn btn-default form-number-add" type="button">
                            	<i class="fa fa-angle-up"></i>
                            </button>
                            <button class="btn btn-default form-number-sub" type="button">
                            	<i class="fa fa-angle-down"></i>
                            </button>
                        </span>
                    </div>
				</div>
				<div class="col-sm-4">
				<span class="filter-name font-12 color-999">整个年级的开班数<span>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label no-padding-right">不开班的2科组合：</label>
				<div class="col-sm-8">
					<#if gDtoList2?exists && (gDtoList2?size > 0)>
						<#list gDtoList2 as dto>
							<label><input type="checkbox" class="wp" name="noArrid" value="${dto.subjectIds!}"><span class="lbl"> ${dto.conditionName!}</span></label>
						</#list>
					</#if>
				</div>
			</div>
			<div class="form-group" id="auto2x">
				<label class="col-sm-4 control-label no-padding-right">分班模式：</label>
				<div class="col-sm-8">
					<div class="input-group">
                       <label>
							<input type="radio" class="wp" name="auto2type" id="auto2type" value="1"  checked="">
							<span class="lbl">模式1<br/><font class="color-999 font-12">将所有学生分配到三科或定二走一班级</font></span>
						</label>
						<label>
							<input type="radio" class="wp" name="auto2type"  value="2">
							<span class="lbl">模式2<br/><font class="color-999 font-12">为保证定二走一班级更加均衡，可能会遗留少量学生未分配班级</font></span>
						</label>
                    </div>
				</div>
			</div>
		</div>
	</div>
</div>
<#-- 底部按钮 -->
<div class="navbar-fixed-bottom opt-bottom">
	<#if haserror>
		<a class="btn btn-blue"   href="javascript:" onclick="errorClear()">异常处理</a>
	<#else>
		<a class="btn btn-blue"   href="javascript:" <#if !isAuto2>onclick="nextToArrangeClassNum()" <#else>disabled</#if> >下一步</a>
	</#if>
   
	<#if !isCanEdit>
   		<a class="btn btn-blue" href="javascript:" <#if !isAuto2> onclick="reStart()" <#else>disabled</#if>>重新安排</a>
   	</#if>

</div>


<script>
	var setTimeClick;
	$(function(){
		showBreadBack(gobackResult,false,"分班安排");
		<#if isAuto2>
			setTimeClick=setTimeout("refArrange1()",30000);
		</#if>
		<#--仅查看已开班组合-->
		$(".group-open").on('change',function(){
			var subType=$(this).val();
			var tableClass="";
			if("1"==subType){
				tableClass="group-one";
			}else if("2"==subType){
				tableClass="group-two";
			}else{
				return;
			}
			if($(this).is(':checked')){
				//选中
				$("."+tableClass).find("tbody tr").each(function(){
					if(!$(this).hasClass("comGroup")){
						if($(this).find(".my2").length>0){
							$(this).show();
						}else{
							$(this).hide();
						}
					}
					
				})
			}else{
				$("."+tableClass).find("tbody tr").show();
			}
		})
		<#--智能分班弹出框数据增减-->
		$("#auto2x").on('click','.form-num >span > button',function(e){
			e.preventDefault();
			var $num = $(this).parent().siblings('.form-control');
			var val = $num.val();
			if (!val ) val = 0;
			if (!/^\d+$/.test(val)){
				val=0;
			}
			var num = parseInt(val);
			var step = $num.parent('.form-num').attr('data-step');
			if (step === undefined) {
				step = 1;
			} else{
				step = parseInt(step);
			}
			if ($(this).hasClass('form-number-add')) {
				num += step;
			} else{
				num -= step;
				if (num <= 0) num = 1;
			}
			$num.val(num);
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
	<#--颜色切换-->
	(function(){
		$(".input_cxcolor").each(function(){
			$(this).cxColor();
		});
		$(".input_cxcolor").bind("change",function(){
			var now_cor = this.value;
			var subIds = $(this).parents("tr").find(".subIds").val().split(",");
			var id = $(this).prev(".corId").val();
			changeColor(id,now_cor,subIds);
		});
		
		function changeColor(id,now_cor,subIds){
			if(!now_cor)
				now_cor = $.cxColor.defaults.color;
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
			title="定1走2快捷开班";
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
							$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/floatingPlan/index");
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
			$("#showList").load("${request.contextPath}/newgkelective/${divideId!}/floatingPlan/index");
		</#if>
		
	}
	<#--弹出2+x-->
	function auto2xFun(){
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '智能分班',
			area: ['520px'],
			btn: ['确定', '取消'],
			btn1:function(){
				save2x();
			},
			btn2:function(){
				layer.closeAll();
			},
			content: $('.layer-group')
		});
	}
	
	<#--页面参数校验--->
	var is2x=false;
	function save2x(){
		if(is2x){
			return;
		}
		is2x=true;
		if(!checkByDivideIdCanEdit()){
			is2x=false;
			return;
		}
		//验证
		var type=$("input[name='auto2type']:checked").val();
		if(!type){
			is2x=false;
			layer.tips('不能为空', $("#auto2type"), {
				tipsMore: true,
				tips:3				
			});
			return;
		}
		var openClassnum=$.trim($("#openClassnum").val());
		if(openClassnum==""){
			is2x=false;
			layer.tips('不能为空', $("#openClassnum"), {
				tipsMore: true,
				tips:3				
			});
			return;
		}
		if (!/^\d+$/.test(openClassnum)) {
			layer.tips('请输入整数', $("#openClassnum"), {
				tipsMore: true,
				tips:3				
			});
			is2x=false;
			return false;
		}
		var openClassnumInt=parseInt(openClassnum);
		if(openClassnumInt<1){
			layer.tips('请输入正整数', $("#openClassnum"), {
				tipsMore: true,
				tips:3				
			});
			is2x=false;
			return false;
		}
		var noArrIds="";
		var length=$(".layer-group").find("input:checkbox[name='noArrid']:checked").length;
		if(length>0){
			$(".layer-group").find("input:checkbox[name='noArrid']:checked").each(function(){
				noArrIds=noArrIds+";"+$(this).val();
			})
			noArrIds=noArrIds.substring(1);
		}
		repeatSaveSix(noArrIds,type,openClassnumInt,"1");
	}
	
	function repeatSaveSix(noArrIds,type,openClassnum,actionIndex){
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/auto2x',
			data:{'noArrIds':noArrIds,'type':type,'openClassnum':openClassnum},
			dataType: "json",
			success: function(data){
					if(data.stat=="success"){
						layer.closeAll();
						<#--本页面刷新-->
						layer.msg("智能分班成功！", {
							offset: 't',
							time: 2000
						});
						refArrange();
		 			}else if(data.stat=="error"){
		 				if(actionIndex=="1"){
		 					<#--上次失败进入分班-->
		 					repeatSaveSix(noArrIds,type,openClassnumInt,"0");
		 				}else{
		 					layer.closeAll();
			 				is2x=false;
			 				layerTipMsg(false,"失败","原因："+data.message);
			 				setTimeout("refArrange()",3000);
		 				}
		 			}else{
		 				layer.closeAll();
		 				<#--分班中 进去首页-->
		 				//刷新本页面
		 				//refArrange();
		 				gobackResult();
		 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
	
	
	
	
	
	
	$("#group3 tbody tr").mouseenter(function(){
		var subIds = $(this).find(".subIds").val();
		var subIdArr = subIds.split(",");
		var s1 = subIdArr[0]+","+subIdArr[1];
		var s2 = subIdArr[0]+","+subIdArr[2];
		var s3 = subIdArr[1]+","+subIdArr[2];
		
		var colorstr = "#fee8ba"	// 橙色
		$("tr[data-value='"+s1+"']").css("backgroundColor",colorstr);
		$("tr[data-value='"+s2+"']").css("backgroundColor",colorstr);
		$("tr[data-value='"+s3+"']").css("backgroundColor",colorstr);
	});
	$("#group3 tbody tr").mouseleave(function(){
		var subIds = $(this).find(".subIds").val();
		var subIdArr = subIds.split(",");
		var s1 = subIdArr[0]+","+subIdArr[1];
		var s2 = subIdArr[0]+","+subIdArr[2];
		var s3 = subIdArr[1]+","+subIdArr[2];
		var colorstr = "#ffffff"	// 白色
		$("tr[data-value='"+s1+"']").css("backgroundColor", colorstr);
		$("tr[data-value='"+s2+"']").css("backgroundColor", colorstr);
		$("tr[data-value='"+s3+"']").css("backgroundColor", colorstr);
	});
	
	
	$("#group2 tbody tr").mouseenter(function(){
		var subIds = $(this).attr("data-value");
		var colorstr1 = "#fee8ba"	// 橙色
		var colorstr2 = "#ffffff"	// 白色
		makeThreeColor(colorstr1,colorstr2,subIds);
	});
	$("#group2 tbody tr").mouseleave(function(){
		$("#group2 tbody tr").css("backgroundColor","#ffffff");
		$("#group3 tbody tr").css("backgroundColor","#ffffff");
	});
	
	
	
	function makeThreeColor(color1,color2,subIds){
		var subIdArr = subIds.split(",");
		$("#group3 tbody tr").each(function(){
			var subIds = $(this).find(".subIds").val();
			var f=true;
			for(var i=0;i<subIdArr.length;i++){
				if(subIds.indexOf(subIdArr[i])>-1){
				
				}else{
					f=false;
					break;
				}
			}
			if(f){
				$(this).css("backgroundColor",color1);
			}else{
				$(this).css("backgroundColor",color2);
			}
		})
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
	
</script>