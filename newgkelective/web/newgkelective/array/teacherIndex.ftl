<link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
<script src="${request.contextPath}/static/components/layer/layer.js"></script>
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<#--
	<a  href="javascript:void(0);" onclick="goback();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
-->
<div class="box box-default">
	<div class="box-header">
		<h3 class="box-title">${gradeName}</h3>
		<div class="box-header-tools">
			
			<span class="color-red">已选择教师列表<#if beforeArrangeSize gt 0>（已安排课程的老师不能撤销）</#if>，点击下一步才会保存</span>
		</div>
	</div>
	<div class="box-body">
		<div class="row">
			<div class="col-sm-4">
				<div class="box box-primary">
					<div class="box-header">
						<h3 class="box-title">可选教师${teacherNumber}个</h3>
					</div>
					<div class="box-body no-padding js-container">
						<ul class="selection-list js-groups"></ul>
					</div>
				</div>
			</div>
			<div class="col-sm-8">
				<div class="box box-primary">
					<div class="box-header">
						<h3 class="box-title">
							选
							<input type="text" class="form-control input-sm inline-block number">
							个
							<button class="btn btn-sm btn-blue js-selectSubmit">确定</button>
						</h3>
					</div>
					<div class="box-body js-container">
						<ul class="selection-list selection-list-inline js-teacher"></ul>
					</div>
				</div>
			</div>
			<div class="col-sm-4" style="display:none;">
				<div class="box box-primary">
					<div class="box-header">
						<h3 class="box-title">已选择</h3>
					</div>
					<div class="box-body js-container">
						<form id="myForm">
							<div class="panel-group js-selected" role="tablist" aria-multiselectable="true"></div>
						</form>
					</div>
				</div>
			</div>
		</div>

	</div>
</div>

<div class="navbar-fixed-bottom opt-bottom">
	<div class="text-center"><a class="btn btn-blue"  href="javascript:void(0)" onclick="save();">下一步</a></div>
</div>

<script>
function goback(){
	
	<#if arrangeType?default('01')=='01'>
		var url =  '${request.contextPath}/newgkelective/${gradeId}/goArrange/index/page';
	<#else>
		var url =  '${request.contextPath}/newgkelective/xzb/index/page';
	</#if>
	
	$("#showList").load(url);
}
$(function(){
	
	showBreadBack(goback,false,"教师选择");
	
	$('.js-container').each(function(){
		$(this).css({
			overflow: 'auto',
			height: $(window).innerHeight() - $(this).offset().top - 98
		})
	});

	;(function(){
		var data = {
			groups: [
				<#if subjectInfoMap?exists>
					<#list subjectInfoMap?keys as key>
						<#if key_index != 0>
						,
						</#if>
						{
							id: '${key_index}',
							subjectId: '${subjectInfoMap[key].subjectId}',
							name: '${subjectInfoMap[key].subjectName}' + '组',
							<#if key_index == 0>
							actived: 'active',
							<#else>
							actived: '',
							</#if>
							classNumber: '${subjectInfoMap[key].classNumber}',
							min: '${subjectInfoMap[key].teacherNumber}',
							selected: '${subjectInfoMap[key].selectTeacherNumber}',
							selectTip: '',
							items: [
								<#if subjectInfoMap[key].teacherIdAndState?exists>
									<#list subjectInfoMap[key].teacherIdAndState?keys as ke>
										<#if ke_index != 0>
										,
										</#if>
										{
											id:'${ke}',
											name: 
												<#if teacherIdToTeacherName?exists>
													'${teacherIdToTeacherName[ke]?default("未知")}'
												<#else>
												'未知'
												</#if>,
											state:'${subjectInfoMap[key].teacherIdAndState[ke]}',
											<#if subjectInfoMap[key].teacherIdAndState[ke] == '0'>
												checked: ''
											<#else>
												checked: 'checked'
											</#if>
										}
									</#list>
								</#if>
							]
						}
					</#list>
				</#if>
			]
		};
		var group_list = $('.js-groups');
		var teacher_list = $('.js-teacher');
		var seleted_list = $('.js-selected');
		var selectSome = $('.js-selectSubmit');
		
		// 获取事件触发节点
		function getNode(node, name){
			return node.nodeName === name ? node : getNode(node.parentNode, name);
		}

		// 渲染组
		function renderGroups(){
			var groups = document.createDocumentFragment();

			for(var i=0; i < data.groups.length; i++){
				var selectedCount=parseInt(data.groups[i].selected);
				var selectMin=parseInt(data.groups[i].min);
				if(selectedCount == 0){
					data.groups[i].selectTip ='有'+ data.groups[i].classNumber + '个班 至少需要' + data.groups[i].min + '位';
				}else if(selectedCount > 0 && selectedCount < selectMin){
					data.groups[i].selectTip = '<span class="selection-warning">至少还差' + (selectMin - selectedCount) + '位</span>';
				}else if(selectedCount >= selectMin){
					data.groups[i].selectTip = '<i class="fa fa-check selection-success"></i>';
				}
				
				var item = '<li class="' + data.groups[i].actived + '" data-id="' + data.groups[i].id + '">\
							<a href="#">'+ data.groups[i].name + '<small>' + data.groups[i].selectTip + '</small></a>\
						</li>';
				$(groups).append(item);

				if(data.groups[i].actived === 'active'){
					renderTeacher(data.groups[i]);
				}
			}

			group_list.html('');
			group_list[0].appendChild(groups);
		}

		// 渲染组员教师
		function renderTeacher(group){
			var teacher = document.createDocumentFragment();

			for(var i=0; i < group.items.length; i++){
				var item='';
				if(group.items[i].state=="2"){
					item = '<li data-owner="' + group.id + '" data-sort="' + i + '">\
								<label>\
									<input type="checkbox" name="checkbox' + group.id + '" ' + group.items[i].checked + ' class="wp " disabled>\
									<span class="lbl"> ' + group.items[i].name + '</span>\
								</label>\
							</li>';
				}else{
					item = '<li data-owner="' + group.id + '" data-sort="' + i + '">\
								<label>\
									<input type="checkbox" name="checkbox' + group.id + '" ' + group.items[i].checked + ' class="wp">\
									<span class="lbl"> ' + group.items[i].name + '</span>\
								</label>\
							</li>';
				}
				$(teacher).append(item);
			}

			teacher_list.html('');
			teacher_list[0].appendChild(teacher);
		}

		// 渲染选择结果
		function renderSelected(){
			seleted_list.html('');
			for(var i=0; i < data.groups.length; i++){
				if(data.groups[i].selected > 0){
					var item = '<div class="panel panel01">\
									<div class="panel-heading" role="tab">\
										<h4 class="panel-title">\
											<a data-toggle="collapse" href="#m' + i + '" aria-expanded="true" aria-controls="m' + i + '">\
												<i class="fa fa-caret-down showOrhide"></i>' + data.groups[i].name + '\
												<input type="hidden" value="'+data.groups[i].name+'" name="subjectInfoList['+i+'].subjectName">\
												<input type="hidden" value="'+data.groups[i].min+'" name="subjectInfoList['+i+'].teacherNumber">\
												<input type="hidden" value="'+data.groups[i].subjectId+'" name="subjectInfoList['+i+'].subjectId">\
												<small class="pull-right">已选 <em>'+ data.groups[i].selected +'</em>/'+ data.groups[i].min + '</small>\
											</a>\
										</h4>\
									</div>\
									<div id="m' + i + '" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">\
										<div class="panel-body">\
											<ul class="selection-list" id="selected' + i + '" data-id="' + data.groups[i].id + '"></ul>\
										</div>\
									</div>\
								</div>';
					seleted_list.append(item);

					var tempfragment ='';
					for(var j=0; j < data.groups[i].items.length; j++){
						var subItem = data.groups[i].items[j];
						var teacherId = data.groups[i].items[j].id;
						if(subItem.checked === 'checked'){
							tempInput = '<input type = "hidden" value="'+teacherId+'" name="subjectInfoList['+i+'].teacherIds['+j+']" >';
							
							if(data.groups[i].items[j].state=="2"){
								tempfragment = tempfragment + tempInput + '<li data-owner="' + data.groups[i].id + '" data-sort="'+ j +'">' + subItem.name + '</li>';
							}else{
								tempfragment = tempfragment + tempInput + '<li data-owner="' + data.groups[i].id + '" data-sort="'+ j +'">' + subItem.name + ' <i class="fa fa-times-circle color-grey"></i></li>';
							}
							
						
						}
					}

					$('#selected'+i).html(tempfragment);
				}
			}
		}

		// 渲染页面
		function render(){
			renderGroups();
			renderSelected();
		}

		// 初次渲染页面
		render();

		// 组别事件处理
		group_list.on('click', function(e){
			e.preventDefault();
			var $id = $(getNode(e.target, 'LI')).data('id');
			
			if(data.groups[$id].actived === 'active') return;

			for(var i=0; i < data.groups.length; i++){
				data.groups[i].actived = '';
			}
			data.groups[$id].actived = 'active';
			
			renderGroups();
		});

		// 教师组员事件处理
		teacher_list.on('click', function(e){
			e.preventDefault();
			var currentNode = $(getNode(e.target, 'LI'));
			var owner = currentNode.data('owner');
			var sort = currentNode.data('sort');
			if(data.groups[owner].items[sort].state=="2"){
				return;
			}
			if(data.groups[owner].items[sort].checked === 'checked'){
				data.groups[owner].items[sort].checked = '';
				data.groups[owner].selected--;
			}else{
				data.groups[owner].items[sort].checked = 'checked';
				data.groups[owner].selected++;
			}
			
			render();
		});

		// 选择当前组前n个教师
		selectSome.on('click', function(e){
			e.preventDefault();
			var value = 0; parseInt($(this).prev().val());
			var n = 0;

			if($(this).prev().val() === ''){
				value = 0;
			}else{
				value = parseInt($(this).prev().val());
			}

			if(/^[1-9]\d*|0$/.test(value) === false){
				layer.msg('请输入非负整数进行选择！');
				return false;
			}

			for(var i=0; i<data.groups.length; i++){
				if(data.groups[i].actived === 'active'){
					data.groups[i].selected = 0;
					var n=0;
					for(var j=0; j<data.groups[i].items.length; j++){
						if(data.groups[i].items[j].state=="2"){
							//不能改变
							data.groups[i].items[j].checked = 'checked';
							n++;
						}else{
							data.groups[i].items[j].checked = '';
						}
					}
					data.groups[i].selected=n;
					if(n>=value){
						break;
					}
					for(var j=0; j<data.groups[i].items.length; j++){
						if(n >= value) break;
						if(data.groups[i].items[j].state!="2"){
							data.groups[i].items[j].checked = 'checked';
							if(data.groups[i].selected < data.groups[i].items.length){
								data.groups[i].selected++;
							}
							n++;
						}

					}
				}
			}
			render();
		});

		// 删除事件
		seleted_list.on('click', function(e){
			e.preventDefault();
			if(e.target.nodeName !== 'I') return;
			var currentNode = $(e.target);
			if(currentNode.hasClass("showOrhide")){
				return;
			}
			var owner = currentNode.parent().data('owner');
			var sort = currentNode.parent().data('sort');
			data.groups[owner].items[sort].checked = '';
			data.groups[owner].selected--;
			render();
		});
	})()


	// 展开折叠
	$(document).on('shown.bs.collapse', '.panel-collapse', function(){
		$(this).parent().find('.panel-title i')
			.removeClass('fa-caret-right').addClass('fa-caret-down');
	}).on('hidden.bs.collapse', '.panel-collapse', function(){
		$(this).parent().find('.panel-title i')
			.removeClass('fa-caret-down').addClass('fa-caret-right');
	})
})
function achieve(arrayId){
	$.ajax({
		url:'${request.contextPath}/newgkelective/${arrayId}/arrayLesson/checkAllTeacher',
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			var url ='${request.contextPath}/newgkelective/${arrayId}/arrayResult/pageIndex';
				$("#showList").load(url);
	 		}else{
	 			layerTipMsg(data.success,"失败",jsonO.msg);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}
function save(){
	var options = {
		url : '${request.contextPath}/newgkelective/${arrayId}/array/teacher/list/save',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
				if(data.code == '01'){
		 			layer.confirm('您未选中任何教师，确定不安排老师吗？', function(index){
		 				//自动安排功能
		 				layer.closeAll();
						achieve("${arrayId!}");
					});
				}else{
					var url = '${request.contextPath}/newgkelective/${arrayId}/adjust/teacher/page';
				    $("#showList").load(url);
				}
	 		}
	 		else{
	 			layerTipMsg(data.success,"保存失败",data.msg);
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#myForm").ajaxSubmit(options);
}

// 暂时没有用了
function autoArrayTeacher(){
	$.ajax({
	    url:'${request.contextPath}/newgkelective/${arrayId}/arrayLesson/autoTeacher',
	    dataType : 'json',
	    success:function(data1) {
	    	if(data1.success){
	    		//响应成功，跳转下一个界面
	    		var url = '${request.contextPath}/newgkelective/${arrayId}/adjust/teacher/page';
				$("#showList").load(url);
	 		}else{
	 			layerTipMsg(data1.fali,"自动安排失败",data1.msg);
			}
	    }
	});
}



</script>