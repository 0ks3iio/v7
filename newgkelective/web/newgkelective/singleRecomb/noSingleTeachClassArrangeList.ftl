<div class="stepsContainer">
    <ul class="steps-default clearfix">
        <li class="active"><span><i>1</i><#if divide.openType?default('')=='12'>原行政班<#else>分行政班</#if></span></li>
        <li class="active"><span><i>2</i>分教学班</span></li>
    </ul>
</div>
<div class="box box-default">
    <div class="box-body" id="settleContent">
        <div>
            <div class="filter" id="groupDiv">
                <div class="filter-item">
                    <div class="filter-content">
                        <a class="btn btn-blue js-group" href="javascript:" onclick="intelliDivide()">智能分班</a>
                        <a class="btn btn-default" href="javascript:" onclick="deleteAll();">全部解散</a>
                    </div>
                </div>
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
                <div class="col-xs-12">
                	<h4 class="form-title">
						<b>二科组合开班</b>
					</h4>
                    <table class="table table-bordered" style="margin-top:10px">
                        <tbody>
                        <tr>
                            <th width="10%">序号</th>
                            <th width="10%">组合名称</th>
                            <th width="10%">总人数</th>
                            <th width="10%">未排人数</th>
                            <th width="30%">分班班级</th>
                            <th width="30%">操作</th>
                        </tr>
                        <#list twoCombineSubjectDtoList as item>
                            <tr>
                                <td>${item_index + 1}</td>
                                <td>${item.shortNames!}</td>
                                <td>${item.totalNum!}</td>
                                <td>${item.unSettledNum!}</td>
                                <td>
                                    <#if subjectIdsToClassMap[item.ids]?exists && subjectIdsToClassMap[item.ids]?size gt 0>
                                        <#list subjectIdsToClassMap[item.ids] as teachClass>
                                            <a class="btn btn-sm btn-white my2 color-blue " href="javascript:void(0)" onclick="showStudentList('${teachClass.id!}')">
                                                ${teachClass.className!}(${teachClass.studentCount!'0'})
                                            </a>
                                        </#list>
                                    </#if>
                                </td>
                                <td style="text-align: center">
                                    <a href="javascript:;" class="table-btn color-blue" onclick="quickOpenTeachClass('${item.ids}')">快捷开班</a>
                                    <a href="javascript:;" class="table-btn color-blue" onclick="twoCombineTeachClass('${item.ids}')">手动开班</a>
                                    <a href="javascript:;" class="table-btn color-blue" onclick="editClazz('${item.ids}')">编辑</a></td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<#-- 底部按钮 -->
<div class="navbar-fixed-bottom opt-bottom">
    <a class="btn btn-default" href="javascript:" onclick="prevHtml()">上一步</a>
    <a class="btn btn-blue <#if !allSolved!false>disabled</#if>" href="javascript:" <#if allSolved!false>onclick="resultHtml()"</#if>>完成开班</a>
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
                url: '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/intelliDivide?subjectType=B',
                dataType: 'json',
                type: 'post',
                success: function (data) {
                    if (data.success) {
                        isDivide = false;
                        layer.closeAll();
                        refreshThis()
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

    var isAllMove=false;
    function deleteAll(){
        if(isAllMove){
            return;
        }
        isAllMove=true;
        var options = {btn: ['确定','取消'],title:'确认信息', icon: 0,closeBtn:0};
        showConfirm("是否确定解散所有两科组合班",options,function(){
            $.ajax({
                url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/moveAllGroup',
                dataType : 'json',
                type:'post',
                success:function(data) {
                    var jsonO = data;
                    if(jsonO.success){
                        layer.closeAll();
                        refreshThis();
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

    function quickOpenTeachClass(subjectIds) {
        var title = "2科组合快捷开班";
        var width = 600;
        var height = 400;
        var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/quickOpenClass/page?subjectIds=' + subjectIds;
        indexDiv = layerDivUrl(url, {title: title, width: width, height: height});
    }

    var isFinsh=false;
	function resultHtml(){
		if(isFinsh){
			return;
		}
		isFinsh=true;
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/finshComDivide',
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
		 		if(jsonO.success){
		 			layer.closeAll();
		 			var url =  '${request.contextPath}/newgkelective/${divideId!}/divideClass/resultClassList';
					$("#showList").load(url);
		 		}
		 		else{
		 			isFinsh=false;
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
    
    function twoCombineTeachClass(subjectIds) {
        $("#showList").load("${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/teachClassStudentSet?subjectIds=" + subjectIds);
    }

    function refreshThis() {
        $("#showList").load("${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/openTeachClass?subjectType=A")
    }

    function prevHtml() {
        var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/item';
        $("#showList").load(url);
    }
    
    function showStudentList(classId) {
        var title = "查看学生";
        var width = 500;
        var height = 500;
        var url = '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/showTeachClassStudent?teachClassId=' + classId;
        indexDiv = layerDivUrl(url, {title: title, width: width, height: height});
    }

    function editClazz(subjectIds){
        /*if(!checkByDivideIdCanEdit()){
            return;
        }*/
        var url = '${request.contextPath}/newgkelective/${divideId!}/divideClass/singleRecomb/editClass/page?subjectIds='+subjectIds;
        indexDiv = layerDivUrl(url,{title: "编辑",width:520,height:520});
    }
</script>