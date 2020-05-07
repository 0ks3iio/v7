/**
 * -- 设置 不排课时间部分的JS 
 */

//为每个单元格填充 位置信息 周几 上午/下午  第几节
	function initContent(){
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
			if($(this).parents("#aaa").length>0){
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
			
			$(this).html(content);
		});
	}
	
	
	//动态生成各科目课表
	function generateSubjectTable(subjectTimeDtos){
		var content = "";
		for(var i=0;i<subjectTimeDtos.length;i++){
			//在不排课时间 要排课时间 单元格 插入预选中信息
			/*<#--
				var contentnosel = ""    //01 不排课
				var contentsel = "";	//02 必排课
			-->*/  
			var contentnoselStr="";//01 不排课
			var timeInf = subjectTimeDtos[i].timeInf;
			
			var noTimes="";
			var times="";
			var nn=0;
			var yy=0;
			for(var j=0;j<timeInf.length;j++){
				
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
			
			if(noTimes!=""){
				noTimes=noTimes.substring(1);
			}
			if(times!=""){
				times=times.substring(1);
			}
			contentnoselStr='<input type="hidden" name="coordinate" value="'+ noTimes +'"/>'
						+'<span>'+nn+'节</span>';
			$("[value='"+subjectTimeDtos[i].subjectId+"']").parents("tr").find("span.coordinate").html(nn);
			$("[value='"+subjectTimeDtos[i].subjectId+"']").parents("tr").find('[name="coordinate"]').val(noTimes);
		}

	}
	//根据总课表限制 科目课表
	function limitTable(lessonTimeDtosJson,isGrade){
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
			if(!isGrade){
				$parm.addClass("time-dis");
			}
		}
	}
	
	// ${request.contextPath}
	function initTimeEx(){
		var array_item_id = $("input[name='array_item_id']").val();
		if(array_item_id==""){
			layer.msg("还没有提交总课表！", {
						offset: 't',
						time: 2000
					});
			return;
		}
		var url = _contextPath+"/newgkelective/"+array_item_id+"/subjectTimeinf/json";
		$.post(url, 
			function(data){
				
				var dataJson = $.parseJSON(data);
				
				//动态生成各班课表
				var subjectTimeDtos = dataJson.subjectTimeDtosJson;
				if(subjectTimeDtos.length>0){
					generateSubjectTable(subjectTimeDtos);
				}
				
				var lessonTimeDtosJson = dataJson.lessonTimeDtosJson;
				limitTable(lessonTimeDtosJson,true);
				
				var timeDtosJson = dataJson.timeDtosJson;
				limitTable(timeDtosJson,false);
		});
	}
	
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
		var norange = $('#courseTable tbody tr:eq('+(rowIndex-1)+') td.coordinate [name="coordinate"]').val();
		//0.不排课 1.排课
		importw(norange,"#aaa");
	}
	
	//针对不同的表，进行不同的单元格 数据改变
	function tdFill(cell,is_join){
		if(($(cell).parents("#aaa").length>0)||($(cell).parents("#bbb").length>0)){
			//id为aaa或者bbb表明位置是在不排课的表
		}else{
			$(cell).children(":hidden:eq(0)").val(is_join);
		}
	}
	
	//点击时标记
	$('#aaa').on('click','.table-editable', function(e){
		function getNode(node){
			if(!node){
				return null;
			}
			if(node.nodeName === 'TD'){
				return node;
			}else{
				if(!getNode(node.parentNode)){
					return null;
				}else{
					return getNode(node.parentNode);
				}
			}
			return node.nodeName === 'TD' ? node : getNode(node.parentNode);
		}
		var nn=$(getNode(e.target));
		if(nn==null){
			return;
		}
		var cnode = $(nn),
			content = '<span class="color-red">' + $(this).data('label') + '</span>';
		
		if(!cnode.hasClass('editable') || cnode.hasClass('disabled')) return;
	
	
		if(cnode.hasClass('active')){
			//取消不可排课文字
			cnode.removeClass('active').children("span").css({visibility:'hidden'});
			//将总课表的单元格中is_join字段改为1
			tdFill(cnode[0],1);
		}else{
			//设为不可排课
			cnode.addClass('active').children("span").css({visibility:'visible'});
			tdFill(cnode[0],0);
		}
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
	
	// 修改时间
	$('.js-changeTime').on('click', function(e){
		//将#aaa中的数据清除
		$("#aaa").find(".editable").removeClass('active').children("span").css({visibility:'hidden'});
		var disablel = $("#aaa").find(".editable.newdisabled");
		for(var i=0;i<disablel.length;i++){
			disablel.eq(i).removeClass("disabled").removeClass("newdisabled");
		}
		
		//获取行数插入到弹出框区域
		var rowIndex = $(this).parents("tr").children("td:eq(0)").text();
		var subjectId = $(this).parents("tr").find("[name='subjectIdType']").val();
		var stype = subjectId.split('-')[1];
		if(stype == 'A' || stype == 'B'){
			$('#aaa .time-dis').addClass('disabled');
		} else {
			$('#aaa .time-dis').removeClass('disabled');
		}
		
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
			area: '700px',
			btn: ['确定', '取消'],
			content: $('.layer-changeTime'),
			yes:function(index,layero){
				var actives  = $("#aaa").find(".active");
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
					var intervalName = $("#aaa [rowspan]:eq("+(Number(period_interval)-2)+")").text();
					
					var inrowIndex = actives.eq(i)[0].parentNode.rowIndex-1;
					var incellIndex = actives.eq(i)[0].cellIndex;
					var coordinate = weekday+'_'+period_interval+ '_'+ period;
					//根据是否排课构造显示信息
					if(time_type=="01"){
						content01=content01+","+coordinate;
						/*--
							content01 = content01 + '<span>'+$("#aaa table th:eq("+n+")").text()+intervalName+'第'+period+'节</span><br>'
							//构造字符串，用来修改此处信息时，将上一次信息重新显示在弹出框中
							content01 = content01 + '<input type="hidden" name="coordinate" value="'+coordinate+'"/>';
						-->*/
						num1++;
					}else if(time_type=="02"){
						content02=content02+","+coordinate;
						/*<#--
							content02 = content02 + '<span>'+$("#aaa table th:eq("+n+")").text()+intervalName+'第'+period+'节</span><br>'
							content02 = content02 + '<input type="hidden" name="coordinate" value="'+coordinate+'"/>';
						-->*/
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
				//content01='<input type="hidden" name="coordinate" value="'+ content01 +'"/>'
				//		+'<span>'+num1+'节</span>';
				content01 = {coordinate:content01,num:num1};
				
				content02='<input type="hidden" name="coordinate" value="'+ content02 +'"/>'
				+'<span>'+num2+'</span>';

				//提交本次选择结果
				submitSubject(submitInf,subjectId,rowIndex,content01,content02);
				
				layer.close(index);
			}
			
		})
		
	});
	
	//提交结果
	function submitSubject(submitInf,subjectId,rowIndex,content01,content02){
		var array_item_id = $('[name="array_item_id"]').val();
		var url = _contextPath+'/newgkelective/'+array_item_id+'/updateSubjectLessonTime';
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
					$("#courseTable tbody tr:eq("+(rowIndex-1)+") [name='coordinate']").val(content01.coordinate);
					$("#courseTable tbody tr:eq("+(rowIndex-1)+") span.coordinate").html(content01.num);
				}else{
					layerTipMsg(false,"失败","");
				}
			});
		
	}
	
	/*-- 复制到功能 --*/
	function getSubjectId(subIdStr){
		if(subIdStr && subIdStr != ''){
			var strs = subIdStr.split('-');
			return strs[0];
		}
		return '';
	}
	
	$('.js-copyTime').on('click', function(e){
		//获取行数插入到弹出框区域
		var rowIndex = $(this).parents("tr").children("td:eq(0)").text();
		var subIdStr = $(this).parents("tr").find("[name='subjectIdType']").val();
		var subjectId = getSubjectId(subIdStr);
		var norange = $('#courseTable tbody tr:eq('+(rowIndex-1)+') td.coordinate [name="coordinate"]').val();
		var dorange= '';
		/*<#--$('#bb tbody tr:eq('+(rowIndex-1)+') td:eq(3) [name="coordinate"]').val();-->*/
		if(norange==""){
			layer.msg("没有限制可以复制", {
							offset: 't',
							time: 2000
						});
			return;
		}
		e.preventDefault();
		//$('.choose-course span').removeClass("active").removeClass("disabled").addClass("active");
		$('.choose-course span').removeClass("active").removeClass("disabled");
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
		
		var url = _contextPath+'/newgkelective/'+arItemId+'/copySubjectLessonTime';
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
					updateNoLecture();
		 		}else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 		}	
			}
		});

	}
	
	function updateNoLecture(){
		var url = _contextPath+"/newgkelective/"+arItemId+"/subjectTimeinf/json";
		$.post(url, 
			function(data){
				
				var dataJson = $.parseJSON(data);
				
				//动态生成各班课表
				var subjectTimeDtos = dataJson.subjectTimeDtosJson;
				if(subjectTimeDtos.length>0){
					generateSubjectTable(subjectTimeDtos);
				}
				
				//var lessonTimeDtosJson = dataJson.lessonTimeDtosJson;
				//limitTable(lessonTimeDtosJson,true);
			});
	}
	

	$(function(){
		initContent();

		initTimeEx();
		
		initSpan();
	});