<div class="row">
	<div <#if referScore?exists>class="col-md-8"</#if>>
		<#if courseNameMap?exists && recommendChoMap?exists && (recommendChoMap?size gt 0)>
		<div class="row margin-bottom-20">
			<div class="col-md-2 text-right">
				推荐组合：
			</div>
			<div class="col-md-10">
				<ul class="gk-course-list js-selectGroup">
					<#list recommendChoMap? keys as key>
					<li class="no-margin-left" data-course="${key!}">
						<a href="#">
							<div class="gk-course-img">
								<#list recommendChoMap[key] as item >
								<span class="gk-course gk-course-g">${courseNameMap[item]!}</span>
								</#list>
							</div>
							<h3 class="gk-course-name">${key!}</h3>
						</a>
					</li>
					</#list>
				</ul>
			</div>
		</div>
		</#if>
		<div class="row margin-bottom-20">
			<div class="col-md-2 text-right">
				选课科目：
			</div>
			<div class="col-md-8 bg-f2faff margin-t10">
				<#--<p class="color-red" id="tipsSpan"></p>-->
				<p class="gk-course-tip" id="tipsSpan" style="display:none"><i class="fa fa-minus-circle"></i></p>
				<#if courseNameMap?exists && categoryList?exists && categoryList?size gt 0>
				<#list categoryList as category>
				<p class="padding-l-10 margin-t-20 category" data-index="${category_index}" data-minnum="${category.minNum?default(0)}" data-maxnum="${category.maxNum?default(0)}" data-name="${category.categoryName!}">
					<span class="font-16"><b>${category.categoryName!}：</b></span>
					<img src="${request.contextPath}/static/images/7choose3/icon-warning.png" width="14" height="14"/>
					<span class="color-999">&nbsp;
					（最多选${category.maxNum?default(0)}组/门，最少选${category.minNum?default(0)}组/门）
					</span>
				</p>
				<ul class="gk-course-list margin-b-10 js-selectSingle category${category_index}">
					<#if category.courseList?exists && category.courseList?size gt 0>
						<#list category.courseList as item>
							<li data-course="${item.subjectName!}" id="${item.id!}">
								<a href="javascript:;">
									<div class="gk-course-img">
										<span class="gk-course gk-course-s">${item.subjectName!}</span>
									</div>
									<h3 class="gk-course-name">${item.subjectName!}</h3>
								</a>
							</li>
						</#list>
					</#if>
					<#if category.courseCombination?exists && category.courseCombination?size gt 0>
						<#list category.courseCombination as item>
							<li data-course="${item.courseList[0].subjectName!},${item.courseList[1].subjectName!}" id="${item.courseList[0].id!},${item.courseList[1].id!}">
								<a href="javascript:;">
									<div class="gk-course-img">
										<span class="gk-course gk-course-s">${item.courseList[0].subjectName!}</span>
										<span class="gk-course gk-course-s">${item.courseList[1].subjectName!}</span>
									</div>
									<h3 class="gk-course-name">${item.courseList[0].subjectName!},${item.courseList[1].subjectName!}</h3>
								</a>
							</li>
						</#list>
					</#if>
				</ul>
				</#list>
				</#if>
			</div>
		</div>
		
		<#if resultList?exists && (resultList?size gt 0)>
		<div class="row margin-bottom-20" id="subjectTypeGroup">
			<div class="col-md-2 text-right">
				已选科目：
			</div>
			<div class="col-md-8">
				<div class="form-horizontal margin-t6" role="form">
					<#list resultList as item>
					<div class="form-group border-bottom-dfdfdf" id="subjectType_${item.subjectId!}">
						<label class="col-sm-1 control-label no-padding-right text-left">${courseNameMap[item.subjectId!]!}</label>
						<div class="col-sm-11">
							<p class="js-explain">
								<label class="inline">
									<input type="radio" class="wp" <#if item.subjectType?default('1')=='1'>checked="checked"</#if> name="${newGkChoice.id}${item_index}" value="1">
									<span class="lbl"> 不可调剂</span>
								</label>
								<label class="inline">
									<input type="radio" class="wp" <#if item.subjectType?default('1')=='2'>checked="checked"</#if> name="${newGkChoice.id}${item_index}" value="2">
									<span class="lbl"> 可调剂</span>
								</label>
								<label class="inline">
									<input type="radio" class="wp" <#if item.subjectType?default('1')=='3'>checked="checked"</#if> name="${newGkChoice.id}${item_index}" value="3">
									<span class="lbl"> 优先调剂</span>
								</label>
							</p>
						</div>
					</div>
					</#list>
				</div>
			</div>
		</div>
		<#else>
		<div class="row margin-bottom-20" id="subjectTypeGroup" style="display:none">
			<div class="col-md-2 text-right">
				已选科目：
			</div>
			<div class="col-md-8">
				<div class="form-horizontal margin-t6" role="form">
					<#list 1..3 as item>
					<div class="form-group border-bottom-dfdfdf" id="subjectType_${item!}">
						<label class="col-sm-1 control-label no-padding-right text-left"></label>
						<div class="col-sm-11">
							<p class="js-explain">
								<label class="inline">
									<input type="radio" class="wp" name="${newGkChoice.id}${item_index}" value="1">
									<span class="lbl"> 不可调剂</span>
								</label>
								<label class="inline">
									<input type="radio" class="wp" name="${newGkChoice.id}${item_index}" value="2">
									<span class="lbl"> 可调剂</span>
								</label>
								<label class="inline">
									<input type="radio" class="wp" name="${newGkChoice.id}${item_index}" value="3">
									<span class="lbl"> 优先调剂</span>
								</label>
							</p>
						</div>
					</div>
					</#list>
				</div>
			</div>
		</div>
		</#if>
		
		<div id="isWantToSubject" style="display:none">
		<div class="row margin-bottom-20">
			<div class="col-md-2 text-right">
				优先调剂到（非必选）：
			</div>
			<div class="col-md-10" id="wantToSubject">
				<#if courseList?exists && courseList?size gt 0>
				<#list courseList as item>
				<label class="pos-rel margin-r-20" id="want_${item.id!}" data-reverse="nowant_${item.id!}" data-value="${item.id!}">
				    <input name="course-checkbox" type="checkbox" class="wp" value="${item.id!}">
				    <span class="lbl"> ${item.subjectName!}</span>
				</label>
				</#list>
				</#if>
			</div>
		</div>
		
		<div class="row margin-bottom-20">
			<div class="col-md-2 text-right">
				明确不选（非必选）：
			</div>
			<div class="col-md-10" id="noWantToSubject">
				<#if courseList?exists && courseList?size gt 0>
				<#list courseList as item>
				<label class="pos-rel margin-r-20" id="nowant_${item.id!}" data-reverse="want_${item.id!}" data-value="${item.id!}">
				    <input name="course-checkbox" type="checkbox" class="wp" value="${item.id!}">
				    <span class="lbl"> ${item.subjectName!}</span>
				</label>
				</#list>
				</#if>
			</div>
		</div>
		</div>
		
		<div class="row margin-bottom-20">
			<div class="col-md-offset-2 col-md-10">
				<#if timeState?default('0')=='0'>
				<button class="btn btn-long btn-blue disabled">选课未开始</button>
				<#elseif timeState?default('0')=='2'>
				<button class="btn btn-long btn-blue disabled">选课已结束</button>
				<#else>
				<button class="btn btn-long btn-blue disabled js-submitCourse">提交选课</button>
				<a href="javascript:;" class="js-alert"><span>已阅读选课说明</span></a>
				</#if>
			</div>
		</div>
	</div>
	<!--  成绩详情-->
	<#if referScore?exists>
	<div class="col-md-4">
		<div class="curricula-body-right">
		<div class="curricula-right-title">
	    	<span>${referScore.name!}</span>
	  	</div>
		<div class="curr-chart-title">各科年级排名</div>
		<div class="curr-chart-num">总分：${totalScore!}</div>
		<div class="curr-chart-num">总排名：${totalRanking!}</div>
		<div id="chart1" style="width: 100%;height:300px;margin-bottom: 20px;"></div>
		<div id="rankingDivId"></div>
	</div>
	</#if>
</div>
<div class="layer layer-addInputerset layui-layer-wrap" style="display: none;">
    <div class="layer-body" style="height: 350px;overflow-y: scroll">
        ${newGkChoice.notice!}
    </div>
</div>
<script>
var courseNameMap = {};
var showTips = false;
var course = [];	/*已选择的课程*/
var counter = 0;	/*已选课程数*/
var courseIds = [];	/*已选择的课程id*/
$(function(){
	$('.js-alert').on('click',function(){
		layer.open({
			type: 1,
			shade: .5,
			title: ['选课说明','font-size:16px;'],
			area: ['800px', '500px'],
			btn: ['知道了'],
			content: $('.layer-addInputerset')
		})
	});

	<#if courseNameMap?exists && courseNameMap?size gt 0>
	<#list courseNameMap? keys as key>
		courseNameMap['${key!}'] = '${courseNameMap[key]!}';
	</#list>
	</#if>
	
	var disabledGroup = [];
	var disabledShortGroup = [];
	<#if limitChoList?exists && (limitChoList?size gt 0)>
	<#list limitChoList as item>
		disabledGroup.push('${item[0]}');
		disabledShortGroup.push('${item[1]}');
	</#list>
	</#if>
	
	<#if isTips?default(false)>
		var showNum = '${showNum!}';
		var hintContent = '${hintContent!}';
		var resultIds = '${resultIds?default('')}';
		var resultCountMap = {};
		var resultNameMap = {};
		
		var jsonStringData=jQuery.parseJSON('${jsonStringData!}');
		var nameJson = jsonStringData.legendData;
	    var countJson=jsonStringData.loadingData;
	    
	    for(i=0;i<countJson.length;i++){
	   		resultNameMap[countJson[i].subjectId]=nameJson[i];
	    	resultCountMap[countJson[i].subjectId]=countJson[i].value;
		}
		
		if(resultIds!=''){
			if(resultCountMap[resultIds]!=null&&resultCountMap[resultIds]<showNum){
				$("#tipsSpan").html('<i class="fa fa-minus-circle">'+hintContent+'</i>');
				$("#tipsSpan").show();
			}
	    }
	</#if>
    

	var groupItem = $('.js-selectGroup li');
	var singleItem = $('.js-selectSingle li');
	var submitCourse = $('.js-submitCourse');
	
	var courseClass = {
		<#if codeNames?? && codeNames?size gt 0>
		<#list codeNames as cns>
		'${cns[0]!}': 'gk-course-s-${cns[1]!}'<#if cns_has_next>,</#if>
		</#list>
		</#if>
	};
	
	var groupClass = {
		<#if codeNames?? && codeNames?size gt 0>
		<#list codeNames as cns>
		'${cns[0]!}': 'gk-course-g-${cns[1]!}'<#if cns_has_next>,</#if>
		</#list>
		</#if>
	};
	
	//初始化样式
	singleItem.each(function(){
		var key = $(this).data('course');
		if ($(this).attr('id').length > 32) {
			$(this).find('.gk-course-s').removeClass('gk-course-s').addClass('gk-course-g').each(function () {
				$(this).addClass(groupClass[$(this).text().trim()]);
			});
		} else {
			$(this).find('.gk-course-s').addClass(courseClass[key]);
		}
	});
	groupItem.find('.gk-course-g').each(function(){
		var key = $(this).html();
		$(this).addClass(groupClass[key]);
	});
	
	
	$("#notice").click(function(){
		var url = '${request.contextPath}/newgkelective/stuChooseSubject/list/page?hasRead=0';
   		$("#itemShowDivId").load(url);
	});
	

	function isdisabled(){
		if(counter === ${newGkChoice.chooseNum!'3'}){
			$("#subjectTypeGroup").show();
			submitCourse.removeClass('disabled');
			$("#isWantToSubject").find("span").removeClass("disabled");
			singleItem.each(function(){
				if(!$(this).hasClass('selected')){
					$(this).addClass('disabled');
					if ($(this).attr('id').length > 32) {
						var tmp = $(this).attr('id').split(',');
						$("#want_" + tmp[0]).removeClass("opacity-6").find("input").prop("disabled", false);
						$("#nowant_" + tmp[0]).removeClass("opacity-6").find("input").prop("disabled", false);
						$("#want_" + tmp[1]).removeClass("opacity-6").find("input").prop("disabled", false);
						$("#nowant_" + tmp[1]).removeClass("opacity-6").find("input").prop("disabled", false);
					} else {
						$("#want_" + $(this).attr('id')).removeClass("opacity-6").find("input").prop("disabled", false);
						$("#nowant_" + $(this).attr('id')).removeClass("opacity-6").find("input").prop("disabled", false);
					}
				}else{
					if ($(this).attr('id').length > 32) {
						var tmp = $(this).attr('id').split(',');
						$("#want_"+tmp[0]).addClass("opacity-6").find("input").prop("disabled",true);
						$("#nowant_"+tmp[0]).addClass("opacity-6").find("input").prop("disabled",true);
						$("#want_"+tmp[1]).addClass("opacity-6").find("input").prop("disabled",true);
						$("#nowant_"+tmp[1]).addClass("opacity-6").find("input").prop("disabled",true);
					} else {
						$("#want_"+$(this).attr('id')).addClass("opacity-6").find("input").prop("disabled",true);
						$("#nowant_"+$(this).attr('id')).addClass("opacity-6").find("input").prop("disabled",true);
						$("#isWantToSubject input").prop("checked",false);
					}
				}
			});
		    
		    var i = 0;
		    $("#subjectTypeGroup").find(".form-group").each(function(){
		    	$(this).attr("id","subjectType_"+courseIds[i]);
		    	$(this).find("label.control-label").text(courseNameMap[courseIds[i]]);
		    	var j = true;
		    	$(this).find("input").each(function(){
		    		if(j){
		    			$(this).prop("checked",true);
		    			j=false;
		    		}else{
		    			$(this).prop("checked",false);
		    		}
		    	})
		    	i++;
		    })
		}else{
			submitCourse.addClass('disabled');
			singleItem.each(function(){
				if (courseIds.indexOf($(this).attr("id")) < 0) {
					$(this).removeClass('disabled');
				}
			});
			$("#subjectTypeGroup").hide();
			$(".category").each(function(){
				var len = 0;
				$(this).siblings('.category'+$(this).data('index')).find("li.selected").each(function(){
					len++;
				});
				if($(this).data("maxnum") && len>=$(this).data("maxnum")){
					$(this).siblings('.category'+$(this).data('index')).find("li").each(function(){
						if(!$(this).hasClass('selected')) {
							$(this).addClass('disabled');
						}
					});
				}
			});
		}
		
		$("#isWantToSubject").hide();
		
		var disStrs = '';
		if(disabledGroup.length > 0 && course.length > 0){
			for(var i=0;i<disabledGroup.length;i++){
				var dig = disabledGroup[i];
				var flag = true;
				for(var j=0;j<course.length;j++){
					if(dig.indexOf(course[j])<0){
						flag = false;
						break;
					}
				}
				if(!flag){
					continue;
				}
				if(disStrs!=''){
					disStrs+='、';
				}
				disStrs+=disabledShortGroup[i];
			}
		}
		if(disStrs != ''){
			$('#tipsSpan').html('<i class="fa fa-minus-circle">相关不推荐组合'+disStrs+'</i>');
			$('#tipsSpan').show();
			showTips =true;
		} else{
			showTips =false;
			$('#tipsSpan').hide();
		}
		
		<#if isTips?default(false)>
			if(counter == ${newGkChoice.chooseNum?default(3)}){
				var subjectIds = courseIds;
				subjectIds.sort();
				subjectIds = subjectIds.join(",");
				if(resultCountMap[subjectIds]!=null&&resultCountMap[subjectIds]<showNum){
					if(!showTips){
						$('#tipsSpan').html('<i class="fa fa-minus-circle">'+hintContent+'</i>');
						$("#tipsSpan").show();
					}
				}
			}else{
				if(!showTips){
					$("#tipsSpan").hide();
				}
			}
	    </#if>
	}

	// 选择组合
	groupItem.on('click', function(e){
		e.preventDefault();
		if($(this).hasClass('selected')){
			$(this).removeClass('selected');
			counter = 0;
			course = [];
			courseIds = [];
			singleItem.each(function(){
				$(this).removeClass('selected disabled');
			})
		}else{
			courseIds = [];
			counter = 0;
			course = [];
			$(this).addClass('selected').siblings().removeClass('selected');
			counter = ${newGkChoice.chooseNum!'3'};
			course = $(this).data('course').split('、');
			singleItem.removeClass('selected disabled');

			for(var j = 0; j < course.length; j++){
				singleItem.each(function(index){
					var c = $(singleItem[index]).data('course');
					if(c === course[j]){
						$(singleItem[index]).addClass('selected');
						courseIds.push($(singleItem[index]).attr("id"));
					}
				});
			}
			for (var i = 0; i < 3; i++) {
				for(var j = 0; j < 3; j++) {
					if ($('[id="' + courseIds[i] + ',' + courseIds[j] +'"]').length > 0) {
						$('[id="' + courseIds[i] + ',' + courseIds[j] +'"]').addClass("selected");
						$('[id="' + courseIds[i] + '"]').removeClass("selected").addClass("disabled");
						$('[id="' + courseIds[j] + '"]').removeClass("selected").addClass("disabled");
					}
				}
			}
		}

		isdisabled();
		showRanking();
	});

	// 单科选择
	singleItem.on('click', function(e){
		e.preventDefault();
		if($(this).hasClass('disabled')) return;
		if($(this).hasClass('selected')){
			$(this).removeClass('selected');
			if(course.length != 0){
				if ($(this).attr('id').length > 32) {
					var nameArr = $(this).data('course').split(",");
					var idArr = $(this).attr('id').split(",");
					for (var i = 0; i < 2; i++) {
						course.splice(course.indexOf(nameArr[i]), 1);
						courseIds.splice(courseIds.indexOf(idArr[i]), 1);
						$('[id="' + idArr[i] + '"]').removeClass('disabled');
					}
					counter -= 2;
				} else {
					for (var i = 0; i < counter; i++) {
						if (course[i] === $(this).data('course')) {
							course.splice(i, 1);
						}
						if (courseIds[i] === $(this).attr('id')) {
							courseIds.splice(i, 1);
						}
					}
					counter--;
				}
			}
		}else{
			if ($(this).attr('id').length > 32) {
				var nameArr = $(this).data('course').split(",");
				var idArr = $(this).attr('id').split(",");
				if (counter + 2 > 3) {
					var tmp = 0;
					if (courseIds.indexOf(idArr[0]) < 0) {
						tmp++;
					}
					if (courseIds.indexOf(idArr[1]) < 0) {
						tmp++;
					}
					if (counter + tmp > 3) {
						layer.msg('选课超过${newGkChoice.chooseNum!'3'}门',{
							offset: 't',
							time: 2000
						});
						return;
					}
				}
				$(this).addClass('selected');
				singleItem.each(function () {
					for (var i = 0; i < 2; i++) {
						if ($(this).attr('id').length === 32 && $(this).hasClass('selected') && idArr.indexOf($(this).attr('id')) > -1) {
							$(this).removeClass("selected").addClass("disabled");
							courseIds.splice(courseIds.indexOf(idArr[i]), 1);
							course.splice(courseIds.indexOf(idArr[i]), 1);
							counter--;
						}
					}
				});
				$('[id="' + idArr[0] + '"]').addClass('disabled');
				$('[id="' + idArr[1] + '"]').addClass('disabled');
				for (var i = 0; i < 2; i++) {
					course.push(nameArr[i]);
					courseIds.push(idArr[i]);
					counter++;
				}
			} else {
				var idTmp = $(this).attr('id');
				var flag = false;
				singleItem.each(function () {
					if ($(this).attr('id').length === 32 && $(this).hasClass('selected')) {
						if ($('[id="' + $(this).attr('id') + ',' + idTmp +'"]').length > 0) {
							$('[id="' + $(this).attr('id') + ',' + idTmp +'"]').addClass("selected");
							$(this).removeClass("selected").addClass("disabled");
							flag = true;
						}
						if ($('[id="' + idTmp + ',' + $(this).attr('id') +'"]').length > 0) {
							$('[id="' + $(this).attr('id') + ',' + idTmp +'"]').addClass("selected");
							$(this).removeClass("selected").addClass("disabled");
							flag = true;
						}
					}
				});
				if (flag) {
					$(this).addClass("disabled");
				} else {
					$(this).addClass('selected');
				}
				course.push($(this).data('course'));
				courseIds.push($(this).attr('id'));
				counter++;
			}
		}
		
		if(counter === ${newGkChoice.chooseNum!'3'}){
			groupItem.each(function(){
				if( course.sort().toString() === $(this).data('course').split('、').sort().toString()){
					$(this).addClass('selected');
				}
			})
		}else{
			groupItem.each(function(){
				if($(this).hasClass('selected')){
					$(this).removeClass('selected');
				}
			})
		}

		isdisabled();
		showRanking();
	});
	
	$(".inline span.lbl").on("click",function(){
		if($(this).siblings().val()=='1'){
			$("#isWantToSubject").hide();
			$(this).parents(".form-group").siblings().find("span.lbl").each(function(){
				if($(this).siblings().prop("checked") && $(this).siblings().val()!='1'){
					$("#isWantToSubject").show();
				}
			})
		}else{
			$("#isWantToSubject").show();
		}
	})
	
	$("#isWantToSubject").find("label").on("click",function(){
		if($(this).hasClass('opacity-6')) return;
		if($(this).find('input').prop("checked")){
			$(this).find("input").prop("checked",false);
			$("#"+$(this).attr("data-reverse")).removeClass("opacity-6").find("input").prop("disabled",false);
		}else{
			$(this).find("input").prop("checked",true);
			$("#"+$(this).attr("data-reverse")).addClass("opacity-6").find("input").prop("disabled",true);
		}
	})

	// 提交选课
	var isSubmit = false;
	submitCourse.on('click', function(e){
		e.preventDefault();
		if($(this).hasClass('disabled')) return;
		
		if(isSubmit){
			return;
		}
		isSubmit=true;

		if(counter !== ${newGkChoice.chooseNum!'3'} ){
			isSubmit=false;
			layer.msg('选课未满${newGkChoice.chooseNum!'3'}门',{
				offset: 't'
			});
			return false;
		}
		
		var tips = "";
		$(".category").each(function(){
			var len = 0;
			$(this).siblings('.category'+$(this).data('index')).find("li.selected").each(function(){
				len++;
			});
			var name= $(this).data("name");
			if($(this).data("minnum") && len<$(this).data("minnum")){
			 	tips = name+"最少选"+$(this).data("minnum")+"组/门";
				return;
			}
			if($(this).data("maxnum") && len>$(this).data("maxnum")){
				tips = name+"最多选"+$(this).data("maxnum")+"组/门";
				return;
			}
		});
		if(tips!=""){
			isSubmit=false;
			layer.msg(tips,{
				offset: 't'
			})
			return false;
		}
		submitCourse.addClass("disabled");
		var subjectTypes = new Array();
		
		for(i=0;i<courseIds.length;i++){
			$("#subjectType_"+courseIds[i]).find('input:checked').each(function(){
				subjectTypes.push($(this).val());
			})
		}
		subjectTypes = subjectTypes.join(",");
		subjectIds = courseIds.join(",");
		
		var wantToIds = new Array();
		var noWantToIds = new Array();
		if($("#isWantToSubject").css("display")=="block"){
			$("#wantToSubject input:checked").each(function(){
				wantToIds.push($(this).val());
			})
			wantToIds = wantToIds.join(",");
			$("#noWantToSubject input:checked").each(function(){
				noWantToIds.push($(this).val());
			})
			noWantToIds = noWantToIds.join(",");
		}
		
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/newgkelective/stuChooseSubject/save',
			data: {'choiceId':'${newGkChoice.id!}','subjectIds':subjectIds,'subjectTypes':subjectTypes,'wantToIds':wantToIds,'noWantToIds':noWantToIds},
			type:'post',
			success:function(data) {
				layer.closeAll();
				var jsonO = JSON.parse(data);
				if(jsonO.success){
					layer.msg(jsonO.msg, {offset: 't',time: 2000});
					var url = '${request.contextPath}/newgkelective/stuChooseSubject/detail/page?hasRead=1&choiceId=${newGkChoice.id!}&isMaster=1';
        			$("#showList").load(url);
				}else{
					isSubmit=false;
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				layer.close(ii);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
   	 });
   	 initData();
})

function initData(){
	<#if resultList?exists && resultList?size gt 0>
	<#list resultList as item>
		if(${item.subjectType?default('1')}!='1'){
			$("#isWantToSubject").show();
		}
		$(".js-selectSingle li[id='${item.subjectId!}']").addClass("selected");
		course.push('${courseNameMap[item.subjectId!]}');
		courseIds.push('${item.subjectId!}');
		counter++;
		$("#want_${item.subjectId!}").addClass("opacity-6").find("input").prop("disabled",true);
		$("#nowant_${item.subjectId!}").addClass("opacity-6").find("input").prop("disabled",true);
	</#list>
		$(".js-selectSingle li").each(function(){
			if(!$(this).hasClass("selected")){
				$(this).addClass("disabled");
			}
		})
		$('.js-submitCourse').removeClass('disabled');
		$('.js-selectGroup li').each(function(){
			if( course.sort().toString() === $(this).data('course').split('、').sort().toString()){
				$(this).addClass('selected');
			}
		})
	</#if>

	if (courseIds.length > 0) {
		for (var i = 0; i < 3; i++) {
			for (var j = 0; j < 3; j++) {
				if ($('[id="' + courseIds[i] + ',' + courseIds[j] + '"]').length > 0) {
					$('[id="' + courseIds[i] + ',' + courseIds[j] + '"]').removeClass("disabled").addClass("selected");
					$('[id="' + courseIds[i] + '"]').removeClass("selected").addClass("disabled");
					$('[id="' + courseIds[j] + '"]').removeClass("selected").addClass("disabled");
				}
			}
		}
	}
	
	<#if wantToSubjectList?exists && wantToSubjectList?size gt 0>
	<#list wantToSubjectList as item>
		$("#want_${item!}").find("input").prop("checked",true);
		$("#nowant_${item!}").addClass("opacity-6").find("input").prop("disabled",true);
	</#list>
	</#if>
	
	<#if noWantToSubjectList?exists && noWantToSubjectList?size gt 0>
	<#list noWantToSubjectList as item>
		$("#nowant_${item!}").find("input").prop("checked",true);
		$("#want_${item!}").addClass("opacity-6").find("input").prop("disabled",true);
	</#list>
	</#if>
	<#if referScore?exists>
	var chart1 = echarts.init(document.getElementById("chart1"));
    var zongnum = ${studentNum!}; //总数
    option1 = {
    	tooltip: {
        trigger: "item",
        formatter: function(params) {
		return `科目 : 排名<br />
			<#list subjectNames as subjectName>
		    ${subjectName} : ${rankingList[subjectName_index]}<br />
		    </#list>
		    `;
        }
      },
      radar: {
        // shape: 'circle',
        name: {
          textStyle: {
            color: "#333",
            backgroundColor: "#999",
            borderRadius: 3,
            padding: [3, 5],
            fontSize: 14
          }
        },
        indicator: [
			<#list subjectNames as subjectName>
	        	{name: '${subjectName!}', max: '${studentNum!}'}<#if subjectName_index!=subjectNames?size-1>,</#if>
			</#list>
        ],
        axisLine: {
          lineStyle: {
            color: "#CCCCCC"
          }
        },
        splitLine: {
          lineStyle: {
            color: "#CCCCCC"
          }
        },
        splitArea: {
          show: false
        }
      },
      series: [
        {
          type: "radar",
          areaStyle: {
            normal: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: "#317EEB" // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: "#317EEB" // 100% 处的颜色
                  }
                ],
                global: false // 缺省为 false
              },
              opacity: 0.4
            }
          },
          lineStyle: {
            normal: {
              opacity: 0
            }
          },
          symbolSize: 0,
          data: [
            {
              name: "排名",
              value: [
              	<#list rankingList as ranking>
                zongnum - ${ranking!}<#if ranking_index!=rankingList?size-1>,</#if>
                </#list>
              ]
            }
          ]
        }
      ]
    };

    chart1.setOption(option1);
	showRanking();
	</#if>
}
function showRanking(){
	<#if referScore?exists>
	var subjectIds = courseIds.join(",");
	var url = '${request.contextPath}/newgkelective/stuChooseSubject/ranking/page?choiceId=${newGkChoice.id!}&referScoreId=${referScore.id!}&subjectIds='+subjectIds;
	$("#rankingDivId").load(url);	
	</#if>
}
</script>
