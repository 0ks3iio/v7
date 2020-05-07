<link rel="stylesheet" href="${request.contextPath}/static/css/iconfont.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/components.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/page-desktop.css">
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css">
<script src="${request.contextPath}/static/components/layer/layer.js"></script>
<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
    <#--
    <div class="box-header">
        <h3 class="box-title">${gradeName}</h3>
        <div class="box-header-tools">
            <span class="color-red"></span>
        </div>
    </div>-->
        <div class="row">
            <div class="col-sm-4 group-filter-left">
                <div class="box box-primary">
                    <div class="box-header">
                        <h3 class="box-title">可选教师${teacherNumber}个</h3>
                    </div>
                    <div class="box-body no-padding js-container">
                        <ul class="selection-list js-groups"></ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-8 group-filter-right">
                <div class="box box-primary">
                    <div class="box-header">
                        <h3 class="box-title">
                        <a href="javascript:void(0);" class="js-selAll">全选</a>&nbsp;&nbsp;<a href="javascript:void(0);" class="js-reverseAll">反选</a>&nbsp;&nbsp;
                        </h3>
                        <h3 class="box-title">
                            选
                            <input type="text" class="form-control input-sm inline-block number">
                            个
                            <button class="btn btn-sm btn-blue js-selectSubmit">确定</button>
                        </h3>
                        <h3 class="box-title teach-group-filter">

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
<#if selectTeacher?default('')=='1'>
<div class="layui-layer-btn">
	<a class="layui-layer-btn0" id="arrange-commit" onClick="saveTea();">确定</a>
	<a class="layui-layer-btn1" id="arrange-close">取消</a>
</div>
<#else>
<div class="navbar-fixed-bottom opt-bottom">
    <a class="btn btn-blue"  href="javascript:void(0)" onclick="saveTea();">下一步</a>
</div>
</#if>
<script>
    $(function(){
        $('.js-container').each(function(){
            $(this).css({
                overflow: 'auto',
                <#if selectTeacher?default('')=='1'>
                height: 380
                <#else>
                height: $(window).innerHeight() - $(this).offset().top - 98
                </#if>
            })
        });

        ;(function(){
            var data = {
                groups: [
                <#if subjectInfoMap?exists>
                    <#list subjectInfoMap?keys as key>
                        <#if key_index == 0>
                            {
                                id: '${key_index}',
                                subjectId: '${subjectInfoMap[key].subjectId}',
                                name: '${subjectInfoMap[key].subjectName}' + '组',
                                <#if courseId?default('')==subjectInfoMap[key].subjectId>
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
                                            <#if ke_index == 0>
                                                {
                                                    id:'${ke}',
                                                    name:
                                                    <#if teacherIdToTeacherName?exists>
                                                        <#list teacherIdToTeacherName?keys as k>
                                                            <#if ke == k>
                                                                '${teacherIdToTeacherName[k]}'
                                                            </#if>
                                                        </#list>
                                                    </#if>,
                                                    <#if subjectInfoMap[key].teacherIdAndState[ke] == '0'>
                                                    checked: ''
                                                    <#else>
                                                    checked: 'checked'
                                                    </#if>,
                                                    teachGroupId: '${teacherIdToTeachGroupId[ke]}'
                                                }
                                            <#else>
                                                ,{
                                                id:'${ke}',
                                                name:
                                                <#if teacherIdToTeacherName?exists>
                                                    <#list teacherIdToTeacherName?keys as k>
                                                        <#if ke == k>
                                                                '${teacherIdToTeacherName[k]}'
                                                        </#if>
                                                    </#list>
                                                </#if>,
                                                <#if subjectInfoMap[key].teacherIdAndState[ke] == '0'>
                                                checked: ''
                                                <#else>
                                                checked: 'checked'
                                                </#if>,
                                                teachGroupId: '${teacherIdToTeachGroupId[ke]}'
                                            }
                                            </#if>
                                        </#list>
                                    </#if>
                                ],
                                teachGroups:[
                                    <#if subjectIdToTeachGroupId[key]?exists>
                                        <#list subjectIdToTeachGroupId[key] as k>
                                            <#if k_index == 0>
                                                {
                                                    id: '${k}',
                                                    name: '${teachGroupIdToName[k]}'
                                                }
                                            <#else>
                                                ,{
                                                    id: '${k}',
                                                    name: '${teachGroupIdToName[k]}'
                                                }
                                            </#if>
                                        </#list>
                                    </#if>
                                ]
                            }
                        <#else>
                            ,{
                            id: '${key_index}',
                            subjectId: '${subjectInfoMap[key].subjectId}',
                            name: '${subjectInfoMap[key].subjectName}' + '组',
                            <#if courseId?default('')==subjectInfoMap[key].subjectId>
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
                                        <#if ke_index == 0>
                                            {
                                                id:'${ke}',
                                            name:
                                                <#if teacherIdToTeacherName?exists>
                                                    <#list teacherIdToTeacherName?keys as k>
                                                        <#if ke == k>
                                                                '${teacherIdToTeacherName[k]}'
                                                        </#if>
                                                    </#list>
                                                </#if>,
                                                <#if subjectInfoMap[key].teacherIdAndState[ke] == '0'>
                                                    checked: ''
                                                <#else>
                                                    checked: 'checked'
                                                </#if>,
                                                teachGroupId: '${teacherIdToTeachGroupId[ke]}'
                                            }
                                        <#else>
                                            ,{
                                            id:'${ke}',
                                        name:
                                            <#if teacherIdToTeacherName?exists>
                                                <#list teacherIdToTeacherName?keys as k>
                                                    <#if ke == k>
                                                            '${teacherIdToTeacherName[k]}'
                                                    </#if>
                                                </#list>
                                            </#if>,
                                            <#if subjectInfoMap[key].teacherIdAndState[ke] == '0'>
                                                checked: ''
                                            <#else>
                                                checked: 'checked'
                                            </#if>,
                                            teachGroupId: '${teacherIdToTeachGroupId[ke]}'
                                        }
                                        </#if>
                                    </#list>
                                </#if>
                            ],
                            teachGroups:[
                                <#if subjectIdToTeachGroupId[key]?exists>
                                    <#list subjectIdToTeachGroupId[key] as k>
                                        <#if k_index == 0>
                                        {
                                            id: '${k}',
                                            name: '${teachGroupIdToName[k]}'
                                        }
                                        <#else>
                                        ,{
                                            id: '${k}',
                                            name: '${teachGroupIdToName[k]}'
                                        }
                                        </#if>
                                    </#list>
                                </#if>
                            ]
                        }
                        </#if>
                    </#list>
                </#if>
                ]
            };
            //console.log(data);
            var group_list = $('.js-groups');
            var teacher_list = $('.js-teacher');
            var seleted_list = $('.js-selected');
            var selectSome = $('.js-selectSubmit');
            var group_filter = $('.teach-group-filter');


            // 获取事件触发节点
            function getNode(node, name){
                return node.nodeName === name ? node : getNode(node.parentNode, name);
            }

            // 渲染组
            function renderGroups(change){
                var groups = document.createDocumentFragment();

                if (change) {
                    group_filter.html("");
                }

                for(var i=0; i < data.groups.length; i++){
                    if(parseInt(data.groups[i].selected) == 0){
                        data.groups[i].selectTip ='有'+ data.groups[i].classNumber + '个班 ';
                    }else{
                        data.groups[i].selectTip = '<i class="fa selection-success">已选'+ data.groups[i].selected+'个</i>';
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

                var firstShowId;
                if (group.teachGroups.length > 1) {
                    firstShowId = group_filter.children("select").val();
                    //console.log(firstShowId);
                    var filterItem = '<select>';
                    for (var i=0; i < group.teachGroups.length; i++){
                        if (firstShowId == null && i==0) {
                            firstShowId = group.teachGroups[i].id;
                        }
                        filterItem += '<option value="' + group.teachGroups[i].id + '">' + group.teachGroups[i].name + '</option>';
                    }
                    filterItem += '</select>';
                    group_filter.html(filterItem);
                    group_filter.find("option[value='" + firstShowId + "']").attr("selected", true);
                    group_filter.children("select").on("change", function () {
                        var showId = $(this).val();
                        //console.log(showId);
                        teacher_list.children("li").each(function () {
                            if ($(this).attr("teachGroupId").indexOf(showId) >= 0) {
                                $(this).show();
                            } else {
                                $(this).hide();
                            }
                        });
                    });
                } else {
                    group_filter.html("");
                }

                if (firstShowId) {
                    for(var i=0; i < group.items.length; i++){
                        if (group.items[i].teachGroupId.indexOf(firstShowId) >= 0) {
                            var item = '<li data-owner="' + group.id + '" data-sort="' + i + '" teachGroupId="' + group.items[i].teachGroupId + '">\
								<label>\
									<input type="checkbox" name="checkbox' + group.id + '" ' + group.items[i].checked + ' class="wp">\
									<span class="lbl"> ' + group.items[i].name + '</span>\
								</label>\
							</li>';
                        } else {
                            var item = '<li data-owner="' + group.id + '" data-sort="' + i + '" teachGroupId="' + group.items[i].teachGroupId + '" style="display: none">\
								<label>\
									<input type="checkbox" name="checkbox' + group.id + '" ' + group.items[i].checked + ' class="wp">\
									<span class="lbl"> ' + group.items[i].name + '</span>\
								</label>\
							</li>';
                        }
                        $(teacher).append(item);
                    }
                } else {
                    for(var i=0; i < group.items.length; i++){
                        var item = '<li data-owner="' + group.id + '" data-sort="' + i + '">\
								<label>\
									<input type="checkbox" name="checkbox' + group.id + '" ' + group.items[i].checked + ' class="wp">\
									<span class="lbl"> ' + group.items[i].name + '</span>\
								</label>\
							</li>';
                        $(teacher).append(item);
                    }
                }
                if($(teacher).children().length==0){
                	var noitem = '<div class="no-data-container">\
										<div class="no-data">\
											<span class="no-data-img">\
												<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">\
											</span>\
											<div class="no-data-body">\
												<p class="no-data-txt">暂无待分配的教师</p>\
											</div>\
										</div>\
									</div>';
					$(teacher).append(noitem);					
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
												<i class="fa showOrhide fa-caret-down"></i>' + data.groups[i].name + '\
												<input type="hidden" value="'+data.groups[i].name+'" name="teacherPlanList['+i+'].subjectName">\
												<input type="hidden" value="'+data.groups[i].min+'" name="teacherPlanList['+i+'].teacherNumber">\
												<input type="hidden" value="'+data.groups[i].subjectId+'" name="teacherPlanList['+i+'].subjectId">\
												<small class="pull-right">已选 <em>'+ data.groups[i].selected +'</em></small>\
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
                                tempInput = '<input type = "hidden" value="'+teacherId+'" name="teacherPlanList['+i+'].exTeacherIdList['+j+']" >';
                                tempfragment = tempfragment + tempInput + '<li data-owner="' + data.groups[i].id + '" data-sort="'+ j +'">' + subItem.name + ' <i class="fa fa-times-circle color-grey"></i></li>';

                            }
                        }

                        $('#selected'+i).html(tempfragment);
                    }
                }
            }

            // 渲染页面
            function render(){
                renderGroups(false);
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

                renderGroups(true);
            });

            // 教师组员事件处理
            teacher_list.on('click', function(e){
                e.preventDefault();
                var currentNode = $(getNode(e.target, 'LI'));
                var owner = currentNode.data('owner');
                var sort = currentNode.data('sort');

                if(data.groups[owner].items[sort].checked === 'checked'){
                    data.groups[owner].items[sort].checked = '';
                    data.groups[owner].selected--;
                }else{
                    data.groups[owner].items[sort].checked = 'checked';
                    data.groups[owner].selected++;
                }

                render();
            });

			$(".js-selAll").on('click', function(e){
				//console.log("selaall");
				var groupId = group_filter.children("select").val();

                if (groupId) {
                    for (var i = 0; i < data.groups.length; i++) {
                        if (data.groups[i].actived === 'active') {
                            for (var j = 0; j < data.groups[i].items.length; j++) {
                                if (data.groups[i].items[j].teachGroupId.indexOf(groupId) >= 0 &&
                                    data.groups[i].items[j].checked == '') {
                                    data.groups[i].items[j].checked = 'checked';
                                    data.groups[i].selected++;
                                }
                            }
                        }
                    }
                } else {
                    for (var i = 0; i < data.groups.length; i++) {
                        if (data.groups[i].actived === 'active') {
                            data.groups[i].selected = 0;
                            for (var j = 0; j < data.groups[i].items.length; j++) {
                                data.groups[i].items[j].checked = '';

                            }
                            for (var j = 0; j < data.groups[i].items.length; j++) {
                                data.groups[i].items[j].checked = 'checked';
                                if (data.groups[i].selected < data.groups[i].items.length) {
                                    data.groups[i].selected++;
                                }
                            }
                        }
                    }
                }

                //console.log(data);
		    	render();
		    });
			$(".js-reverseAll").on('click', function(e){
				//console.log("reverseall");
		    	$("ul.js-teacher").find(":visible :checked").addClass("temp");
		    	$("ul.js-teacher").find(":visible :not(:checked)").prop("checked",true);
		    	$("ul.js-teacher").find(".temp").removeClass("temp").prop("checked",false);

                var groupId = group_filter.children("select").val();
		    	 
				for(var i=0; i<data.groups.length; i++){
				    // 当前组别
				   if(data.groups[i].actived === 'active'){
                       if (groupId) {
                           for (var j = 0; j < data.groups[i].items.length; j++) {
                               if (data.groups[i].items[j].teachGroupId.indexOf(groupId) >= 0) {
                                   if (data.groups[i].items[j].checked == 'checked') {
                                       data.groups[i].items[j].checked = '';
                                       data.groups[i].selected--;
                                   } else {
                                       data.groups[i].items[j].checked = 'checked';
                                       data.groups[i].selected++;
                                   }
                               }
                           }
                       } else {
                           data.groups[i].selected = 0;
                           for (var j = 0; j < data.groups[i].items.length; j++) {
                               if (data.groups[i].items[j].checked == 'checked') {
                                   data.groups[i].items[j].checked = '';
                               } else {
                                   data.groups[i].items[j].checked = 'checked';
                                   data.groups[i].selected++;
                               }
                           }
                       }
				   }
				}
		    	
		    	render();
		    });

            // 选择当前组前n个教师
            selectSome.on('click', function(e){
                e.preventDefault();

                var groupId = group_filter.children("select").val();

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
                        if (groupId) {
                            for (var j = 0; j < data.groups[i].items.length; j++) {
                                if (data.groups[i].items[j].teachGroupId.indexOf(groupId) >= 0) {
                                    if (data.groups[i].items[j].checked == 'checked') {
                                        data.groups[i].items[j].checked = '';
                                        data.groups[i].selected--;
                                    }
                                }
                            }
                            for (var j = 0; j < data.groups[i].items.length; j++) {
                                if (data.groups[i].items[j].teachGroupId.indexOf(groupId) >= 0) {
                                    if (n >= value) break;
                                    data.groups[i].items[j].checked = 'checked';
                                    if (data.groups[i].selected < data.groups[i].items.length) {
                                        data.groups[i].selected++;
                                    }
                                    n++;
                                }
                            }
                        } else {
                            data.groups[i].selected = 0;
                            for (var j = 0; j < data.groups[i].items.length; j++) {
                                data.groups[i].items[j].checked = '';
                            }
                            for (var j = 0; j < data.groups[i].items.length; j++) {
                                if (n >= value) break;
                                data.groups[i].items[j].checked = 'checked';
                                if (data.groups[i].selected < data.groups[i].items.length) {
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
    
	var isSubmit=false;
    function saveTea(){
    	if(isSubmit){
			return;
		}
		isSubmit=true;
        var options = {
            url : '${request.contextPath}/newgkelective/${divideId}/subjectTeacherArrange/save',
            data: {"itemId" : '${itemId!}',"arrayId":"${arrayId!}"},
            dataType : 'json',
            success : function(data){
                if(data.success){
                    //setTimeout(function(){
                    	layer.closeAll();
                    	layer.msg(data.msg, {offset: 't',time: 2000});
	                    var url =   '${request.contextPath}/newgkelective/${divideId}/subjectTeacherArrange/add?gradeId=${gradeId}'
	                    	+'&arrayId=${arrayId}&courseId=${courseId!}&useMaster=1';
	                    url = url + "&itemId="+data.businessValue;
	                    $("#gradeTableList").load(url);
                    //},500);
                }
                else{
                    layerTipMsg(data.success,"保存失败",data.msg);
                }
                isSubmit=false;
            },
            clearForm : false,
            resetForm : false,
            type : 'post',
            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
        };
        $("#myForm").ajaxSubmit(options);
    }

    function autoArrayTeacher(){
        url =  '${request.contextPath}/newgkelective/${divideId}/teacherArray/index/page?gradeId=${gradeId}&arrayId=${arrayId}&courseId=${courseId!}';
        $("#gradeTableList").load(url);
    }
</script>