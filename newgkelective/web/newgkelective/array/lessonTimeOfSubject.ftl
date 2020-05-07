<div id="bb" role="tabpanel">
<#if subjects?exists && subjects?size gt 0>
	<form id="subjectTime">
	<table class="table table-bordered table-striped table-hover">
		<thead>
			<tr>
				<th>序号</th>
				<th>科目</th>
				<th>不排课节次数</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<#list subjects as subject>
				<tr>
					<td>${subject_index+1}</td>
					<td>${subject.subjectName}<input type="hidden" name="subjectId" value="${subject.id}"/></td>
					<td><input type="hidden" name="coordinate" value=""/><span>0</span></td>
					<td>
						<a class="table-btn js-changeTime" href="javascript:">修改时间</a>
						<a class="table-btn js-copyTime" href="javascript:">复制到</a>
					</td>
				</tr>
			</#list>
		</tbody>
	</table>
	</form>
<#else>
	没有安排科目
</#if>
</div>
<!--|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||-->
<div class="layer layer-changeTime">
	<div class="layer-content">
		<#--<ul class="nav nav-tabs" role="tablist">
			<li class="active" role="presentation" id="aa"><a href="#FamDearPlanService" role="tab" data-toggle="tab">不排课时间</a></li>
			<li role="presentation"><a href="#bbb" role="tab" data-toggle="tab">要排课时间</a></li>
		</ul-->
		<div class="">
			<!-- 这里用来存储 正在编辑的是第几行的数据  从0开始 -->
			<input type="hidden" name="rowIndexFrom"/>
			<div id="aaa" class="tab-pane active" role="tabpanel">
				<div class="explain">
					<p>小提示：灰色背景的不排课需要在总课表<#--和 要排课时间 选项卡-->里设置</p>
				</div>
				<table class="table table-bordered layout-fixed table-editable" data-label="不排课">
					<thead>
						<tr>
							<th width="8%" class="text-center"></th>
							<th width="4.5%" class="text-center"></th>
							<th width="12.5%" class="text-center">周一</th>
							<th width="12.5%" class="text-center">周二</th>
							<th width="12.5%" class="text-center">周三</th>
							<th width="12.5%" class="text-center">周四</th>
							<th width="12.5%" class="text-center">周五</th>
							<th width="12.5%" class="text-center">周六</th>
							<th width="12.5%" class="text-center">周日</th>
						</tr>
					</thead>
					<tbody>						
						<#if amList?exists && amList?size gt 0>
						<#list amList as i>
							<tr>
								<#if (i=='1')>
									<td class="text-center" rowspan="${amList?size}">上午<input type="hidden" disabled name="period_interval" value="2" disable/></td>
								</#if>
								<td class="text-center">${i}</td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
							</tr>
						</#list>
						</#if>
						
						<#if pmList?exists && pmList?size gt 0>
						<#list pmList as i>
							<tr>
								<#if (i=='1')>
									<td class="text-center" rowspan="${pmList?size}">下午<input type="hidden" disabled name="period_interval" value="3" disable/></td>
								</#if>
								<td class="text-center">${i}</td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
							</tr>
						</#list>
						</#if>
						
						<#if nightList?exists && nightList?size gt 0>
						<#list nightList as i>
							<tr>
								<#if (i=='1')>
									<td class="text-center" rowspan="${nightList?size}">晚上<input type="hidden" disabled name="period_interval" value="4" disable/></td>
								</#if>
								<td class="text-center">${i}</td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
							</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div>
			<#--div id="bbb" class="tab-pane" role="tabpanel">
				<div class="explain">
					<p>小提示：灰色背景的不排课需要在总课表和 不排课时间 选项卡里设置</p>
				</div>
				<table class="table table-bordered layout-fixed table-editable" data-label="优先排课">
					<thead>
						<tr>
							<th width="8%" class="text-center"></th>
							<th width="4.5%" class="text-center"></th>
							<th width="12.5%" class="text-center">周一</th>
							<th width="12.5%" class="text-center">周二</th>
							<th width="12.5%" class="text-center">周三</th>
							<th width="12.5%" class="text-center">周四</th>
							<th width="12.5%" class="text-center">周五</th>
							<th width="12.5%" class="text-center">周六</th>
							<th width="12.5%" class="text-center">周日</th>
						</tr>
					</thead>
					<tbody>
						<#if amList?exists && amList?size gt 0>
						<#list amList as i>
							<tr>
								<#if (i=='1')>
									<td class="text-center" rowspan="${amList?size}">上午<input type="hidden" disabled name="period_interval" value="2" disable/></td>
								</#if>
								<td class="text-center">${i}</td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
							</tr>
						</#list>
						</#if>
						
						<#if pmList?exists && pmList?size gt 0>
						<#list pmList as i>
							<tr>
								<#if (i=='1')>
									<td class="text-center" rowspan="${pmList?size}">下午<input type="hidden" disabled name="period_interval" value="3" disable/></td>
								</#if>
								<td class="text-center">${i}</td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
								<td class="text-center editable "><span class="color-red">不可排课</span></td>
							</tr>
						</#list>
						</#if>
						
						<#if nightList?exists && nightList?size gt 0>
						<#list nightList as i>
							<tr>
								<#if (i=='1')>
									<td class="text-center" rowspan="${nightList?size}">晚上<input type="hidden" disabled name="period_interval" value="4" disable/></td>
								</#if>
								<td class="text-center">${i}</td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"></td>
								<td class="text-center editable"><span class="color-red">不可排课</span></td>
								<td class="text-center editable"><span class="color-red">不可排课</span></td>
							</tr>
						</#list>
						</#if>
					</tbody>
				</table>
			</div-->
		</div>
	</div>
</div>
<div class="layer layer-copyTime">
	<div class="layer-content">
		<div class="publish-course choose-course">
			<#if subjects?exists && subjects?size gt 0>
				<#list subjects as subject>
					<span class="active"  data-value="${subject.id!}" id="course_${subject.id!}">
						${subject.subjectName}
					</span>
				</#list>	
			</#if>
		</div>
	</div>
</div>
<script>
$(function(){
	var allSubjectIds={};
	<#if subjects?exists && subjects?size gt 0>
	<#list subjects as subject>
		allSubjectIds['${subject.id!}']='${subject.id!}';
	</#list>	
	</#if>
	initSpan();
	function importw(eles,flag){
		if(eles!=""){
			var s=eles.split(",");
			for(var j=0;j<s.length;j++){
				var ss = s[j].split("_");
				var weekday = ss[0];
				var period_interval = ss[1];
				var period = new Number(ss[2]);
				//TODO1
				var inrowIndex = $(flag).find("[rowspan] [value="+period_interval+"]").parents("tr")[0].rowIndex+ period -2;
				var incellIndex = weekday;
				
				$(flag).find("tbody tr:eq("+inrowIndex+") .editable:eq("+incellIndex+")").click();
			}
		}
	}
	function importInfToPopup(rowIndex){
		var norange = $('#bb tbody tr:eq('+(rowIndex-1)+') td:eq(2) [name="coordinate"]').val();
		//0.不排课 1.排课
		importw(norange,"#FamDearPlanService");
	}

	function getSubjectId(subIdStr){
		if(subIdStr && subIdStr != ''){
			var strs = subIdStr.split('-');
			return strs[0];
		}
		return '';
	}

	// 修改时间
	$('.js-changeTime').on('click', function(e){
		//将#aaa中的数据清除
		$("#FamDearPlanService").find(".editable").removeClass('active').children("span").css({visibility:'hidden'});
		var disablel = $("#FamDearPlanService").find(".editable.newdisabled");
		for(var i=0;i<disablel.length;i++){
			disablel.eq(i).removeClass("disabled").removeClass("newdisabled");
		}
		
		//获取行数插入到弹出框区域
		var rowIndex = $(this).parents("tr").children("td:first").text();
		var subjectId = $(this).parents("tr").find("[name='subjectId']").val();
		$("[name='rowIndexFrom']").val(rowIndex);
		
		//将排课信息带入到弹出框内
		importInfToPopup(rowIndex);
		//默认tab 不排课时间
		
		$("#aa").find("a").click();
		
		e.preventDefault();
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '设置',
			area: ['600px','620px'],
			btn: ['确定', '取消'],
			content: $('.layer-changeTime'),
			yes:function(index,layero){
				var actives  = $("#FamDearPlanService").find(".active");
				var content01 = '';
				var content02 = '';
				var submitInf = '';
				var num1=0;
				var num2=0;
				//遍历每个选中的TD
				for(var i=0;i<actives.length;i++){
					//先获取td中的信息
					var weekday = actives.eq(i).children("[name='weekday']").val();
					var period_interval = actives.eq(i).children("[name='period_interval']").val();
					var period = actives.eq(i).children("[name='period']").val();
					var time_type = actives.eq(i).children("[name='time_type']").val();
					
					var n = Number(weekday)+2;
					var intervalName = $("#FamDearPlanService [rowspan]:eq("+(Number(period_interval)-2)+")").text();
					
					var inrowIndex = actives.eq(i)[0].parentNode.rowIndex-1;
					var incellIndex = actives.eq(i)[0].cellIndex;
					var coordinate = weekday+'_'+period_interval+ '_'+ period;
					//根据是否排课构造显示信息
					if(time_type=="01"){
						content01=content01+","+coordinate;
						<#--
							content01 = content01 + '<span>'+$("#FamDearPlanService table th:eq("+n+")").text()+intervalName+'第'+period+'节</span><br>'
							//构造字符串，用来修改此处信息时，将上一次信息重新显示在弹出框中
							content01 = content01 + '<input type="hidden" name="coordinate" value="'+coordinate+'"/>';
						-->
						num1++;
					}else if(time_type=="02"){
						content02=content02+","+coordinate;
						<#--
							content02 = content02 + '<span>'+$("#FamDearPlanService table th:eq("+n+")").text()+intervalName+'第'+period+'节</span><br>'
							content02 = content02 + '<input type="hidden" name="coordinate" value="'+coordinate+'"/>';
						-->
						num2++;
					}
					
					//构造提交信息
					submitInf = submitInf+'timeInf['+i+'].day_of_week='+weekday
								+'&timeInf['+i+'].period_interval='+period_interval
								+'&timeInf['+i+'].period='+period
								+'&timeInf['+i+'].time_type='+time_type+'&';
				}
				
				if(content01!=""){
					content01=content01.substring(1);
				}
				if(content02!=""){
					content02=content02.substring(1);
				}
				content01='<input type="hidden" name="coordinate" value="'+ content01 +'"/>'
						+'<span>'+num1+'</span>';
				content02='<input type="hidden" name="coordinate" value="'+ content02 +'"/>'
				+'<span>'+num2+'</span>';

				//提交本次选择结果
				submitSubject(submitInf,subjectId,rowIndex,content01,content02);
				
				layer.close(index);
			}
			
		})
		
	})
	// 修改时间
	$('.js-copyTime').on('click', function(e){
		//获取行数插入到弹出框区域
		var rowIndex = $(this).parents("tr").children("td:first").text();
		var subIdStr = $(this).parents("tr").find("[name='subjectId']").val();
		var subjectId = getSubjectId(subIdStr);
		var norange = $('#bb tbody tr:eq('+(rowIndex-1)+') td:eq(2) [name="coordinate"]').val();
		var dorange= '';
		<#--$('#bb tbody tr:eq('+(rowIndex-1)+') td:eq(3) [name="coordinate"]').val();-->
		if(norange==""){
			layer.msg("没有限制可以复制", {
							offset: 't',
							time: 2000
						});
			return;
		}
		e.preventDefault();
		$('.choose-course span').removeClass("active").removeClass("disabled").addClass("active");
		$("#course_"+subIdStr).removeClass("active").addClass("disabled");
		layer.open({
			type: 1,
			shadow: 0.5,
			title: '复制到',
			area: '720px',
			btn: ['确定', '取消'],
			content: $('.layer-copyTime'),
			yes:function(index,layero){
				layer.confirm('确定需要复制？如果复制，将会追加上复制的限制条件。', function(index2){
					var chooseSubjectIds="";
					$('.choose-course span').each(function(){
						if(!$(this).hasClass("disabled") && $(this).hasClass("active")){
							chooseSubjectIds=chooseSubjectIds+","+$(this).attr("data-value");
						}
					});
					if(chooseSubjectIds==""){
						layer.closeAll();
						layerTipMsg(false,"失败","请选择需要复制的科目");
						return;
					}
					chooseSubjectIds=chooseSubjectIds.substring(1);
					submitCopySubject(chooseSubjectIds,norange,dorange,subIdStr);
				})

			}
			
		});
	})
	
	function submitCopySubject(subjectIds,timeDto1,timeDto2,subIdStr){
		if(!subIdStr){
			subIdStr='';
		}
		var array_item_id = $('[name="array_item_id"]').val();
		var url = '${request.contextPath}/newgkelective/'+array_item_id+'/copySubjectLessonTime';
		$.ajax({
			url:url,
			data:{'subjectIds':subjectIds,'timeDto1':timeDto1,'timeDto2':timeDto2,'fromSubId':subIdStr},
			type:'post', 
			dataType:'json',
			success:function(data){
				layer.closeAll();
		    	if(data.success){
		    		// 显示成功信息
		 			layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
					subjectLessonTable('1');
		 		}else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 		}	
			}
		});

	}
	
	//提交结果
	function submitSubject(submitInf,subjectId,rowIndex,content01,content02){
		var array_item_id = $('[name="array_item_id"]').val();
		var url = '${request.contextPath}/newgkelective/'+array_item_id+'/updateSubjectLessonTime';
		var params = submitInf+'subjectId='+subjectId;
		$.post(url, 
			params, 
			function(data){
				if(data=="SUCCESS"){
					layer.msg("保存成功", {
							offset: 't',
							time: 2000
						});
					//将结果插入到主表的相应位置
					$("#bb tbody tr:eq("+(rowIndex-1)+") td:eq(2)").html(content01);
				}else{
					layerTipMsg(false,"失败","");
				}
			});
		
	}
	//为每个单元格填充数据
	$(".editable").each(function(i){
		//找出在一天中的那个时间段
		var rowHeadInf = getHeadInf(this.parentNode);
		var period_interval = rowHeadInf.value;
		//这个时间段中的第几节课
		var rowIndex = this.parentNode.rowIndex;
		var period = rowIndex-rowHeadInf.rowIndex+1;
		//找出是周几
		var weekday;
		if($(this).parent().children("[rowspan]").length>0){
			weekday = this.cellIndex-2;
		}else{
			weekday = this.cellIndex-1;
		}
		
		//插入<span>
		var content ='';
		var labledata = $(this).parents("table").first().data('label');
		
		
		//针对不同的表插入不同的<input/>内容
		if($(this).parents("#FamDearPlanService").length>0){
			//id为aaa表明位置是在不排课 表 01.不排课
			content = '<input type="hidden" name="time_type" value="01"/>';
			if($(this).hasClass('active')){
				content = content + '<span class="color-red">'+labledata+'</span>';
						
			}else{
				content = content + '<span class="color-red" style="visibility:hidden;">'+labledata+'</span>';
			}
			
			content = content +'<input type="hidden" name="weekday" value="'+weekday+'"/>'
							+'<input type="hidden" name="period_interval" value="'+period_interval+'"/>'
							+'<input type="hidden" name="period" value="'+period+'"/>';
		}
		<#--else if($(this).parents("#bbb").length>0){
			//id为bbb表明位置是在不排课 表  02.必排课
			content = '<input type="hidden" name="time_type" value="02"/>';
			if($(this).hasClass('active')){
				content = content + '<span class="color-red">'+labledata+'</span>';
			}else{
				content = content + '<span class="color-red" style="visibility:hidden;">'+labledata+'</span>';
			}
			
			content = content +'<input type="hidden" name="weekday" value="'+weekday+'"/>'
							+'<input type="hidden" name="period_interval" value="'+period_interval+'"/>'
							+'<input type="hidden" name="period" value="'+period+'"/>';
		}-->
		$(this).html(content);
	});
	
	//生成显式课时信息 在各科目课表中
	function getTime(weekday,period_interval,period){
		var content = "";
		var weekJson = ["一","二","三","四","五","六","日"];
		weekday = weekJson[weekday];
		if(period_interval=='1'){
			content = content+"周"+weekday+"早自习"+"第"+period+"节";
		}else if(period_interval=='2'){
			content = content+"周"+weekday+"上午"+"第"+period+"节";
		}else if(period_interval=='3'){
			content = content+"周"+weekday+"下午"+"第"+period+"节";
		}else if(period_interval=='4'){
			content = content+"周"+weekday+"晚自习"+"第"+period+"节";
		}else if(period_interval=='9'){
			content = content+"周"+weekday+"特殊时间"+"第"+period+"节";
		}
		return content;
	}
	
	//动态生成各科目课表
	function generateSubjectTable(subjectTimeDtos){
		var content = "";
		for(var i=0;i<subjectTimeDtos.length;i++){
			//在不排课时间 要排课时间 单元格 插入预选中信息
			<#--
				var contentnosel = ""    //01 不排课
				var contentsel = "";	//02 必排课
			-->  
			var contentnoselStr="";//01 不排课
			var timeInf = subjectTimeDtos[i].timeInf;
			
			var noTimes="";
			var times="";
			var nn=0;
			var yy=0;
			for(var j=0;j<timeInf.length;j++){
				<#--
					var coordinate = timeInf[j].day_of_week+'_'+timeInf[j].period_interval+'_'+timeInf[j].period;
					var temp = '<input type="hidden" name="coordinate" value="'+ coordinate +'"/>'
						+'<span>'+getTime(timeInf[j].day_of_week,timeInf[j].period_interval,timeInf[j].period)+'</span><br>';
					if(timeInf[j].time_type=="01"){
						contentnosel = contentnosel + temp;
					}else{
						contentsel = contentsel + temp;
					}
				-->
				var coordinate = timeInf[j].day_of_week+'_'+timeInf[j].period_interval+'_'+timeInf[j].period;
				if(timeInf[j].time_type=="01"){
					noTimes=noTimes+","+coordinate;
					nn++;
				}else{
					times=times+","+coordinate;
					yy++;
				}
				
				//插入显式信息
			}
			//生成科目课时表
			<#--
				$("[value='"+subjectTimeDtos[i].subjectId+"']").parents("tr").find("td:eq(2)").html(contentnosel);
				$("[value='"+subjectTimeDtos[i].subjectId+"']").parents("tr").find("td:eq(3)").html(contentsel);
			-->
			if(noTimes!=""){
				noTimes=noTimes.substring(1);
			}
			if(times!=""){
				times=times.substring(1);
			}
			contentnoselStr='<input type="hidden" name="coordinate" value="'+ noTimes +'"/>'
						+'<span>'+nn+'</span>';
			$("[value='"+subjectTimeDtos[i].subjectId+"']").parents("tr").find("td:eq(2)").html(contentnoselStr);
		}

	}
	//根据总课表限制 科目课表
	function limitTable(lessonTimeDtosJson){
		for(var i=0;i<lessonTimeDtosJson.length;i++){
			var weekday = lessonTimeDtosJson[i].weekday;
			var period_interval = lessonTimeDtosJson[i].period_interval;
			var period = lessonTimeDtosJson[i].period;
			
			//通过获取tr和td来定位
			var $tr = $("[disabled][name='period_interval'][value='"+period_interval+"']").parents("tr");
			if(period == 1){
			
			}else{
				for(var j=0;j<(period-1);j++){
					$tr = $tr.next("tr");
				}
			}
			$parm = $tr.find(".editable:eq("+weekday+")");
			$parm.addClass("disabled");
		}
	}
	//页面加载完毕时，获取课时安排数据，并且模拟点击
	$(function(){
		var array_item_id = $("input[name='array_item_id']").val();
		if(array_item_id==""){
			layer.msg("还没有提交总课表！", {
						offset: 't',
						time: 2000
					});
			return;
		}
		var url = "${request.contextPath}/newgkelective/"+array_item_id+"/subjectTimeinf/json";
		$.post(url, 
			function(data){
				
				var dataJson = $.parseJSON(data);
				
				//动态生成各班课表
				var subjectTimeDtos = dataJson.subjectTimeDtosJson;
				if(subjectTimeDtos.length>0){
					generateSubjectTable(subjectTimeDtos);
				}
				
				var lessonTimeDtosJson = dataJson.lessonTimeDtosJson;
				limitTable(lessonTimeDtosJson);
			});
	});
});
function initSpan(){
	$('.choose-course span').off('click').on('click', function(e){
		e.preventDefault();
		if($(this).hasClass('disabled')) return;

		if($(this).hasClass('active')){
			$(this).removeClass('active');
		}else{
			$(this).addClass('active');
		}
	});
}
</script>