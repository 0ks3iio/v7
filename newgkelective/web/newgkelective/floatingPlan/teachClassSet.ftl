<#if planType == "A">
    <#assign batchNameMap={"1":"选考一", "2":"选考二", "3":"选考三"}/>
<#else>
    <#assign batchNameMap={"1":"学考一", "2":"学考二", "3":"学考三", "4":"学考四"}/>
</#if>
<style>
	.jxbtable .my2{position: relative;cursor: pointer;}
	.jxbtable .my2 i{display:none;position: absolute;top: -5px;right: -5px;border-radius:50%;background: #fff;}
	.jxbtable .my2:hover i{display: block;}
</style>
<div class="clearfix">
    <h4 class="form-title float-left mt5">
        <b class="mr20">需要走班的<#if planType == "A">选考<#else>学考</#if>教学班</b>
    </h4>
    <button class="btn btn-default btn-sm float-left" style="margin-right:10px;" type="button" onclick="reStartArrangeJxb();">重新安排教学班</button>
    <button class="btn btn-default btn-sm float-left" type="button" onclick="clearArrangeJxb();">解散教学班</button>
    <button class="btn btn-default btn-sm float-left hidden" type="button" onclick="teachClassImport();">导入教学班</button>
</div>
<table class="table table-striped table-bordered table-hover no-margin jxbtable">
    <thead>
    <tr>
        <th rowspan="2"><#if planType == "A">选考<#else>学考</#if>走班时间点</th>
        <th rowspan="2">科目</th>
        <th rowspan="2">总人数</th>
        <th colspan="2" class="text-center">独立开班班级</th>
        <th colspan="2" class="text-center">合班班级</th>
        <th rowspan="2">待开班人数</th>
        <th rowspan="2" width="60">操作</th>
    </tr>
    <tr>
        <th class="text-center">人数</th>
        <th class="text-center">独立开班班级</th>
        <th class="text-center">人数</th>
        <th class="text-center">合班班级</th>
    </tr>
    </thead>
    <tbody>
    <#list batchMap?keys as key>
        <#list batchMap[key] as item>
            <#assign rows = 1>
            <#if item.combinationDivideClassList?size gt 1>
                <#assign rows = item.combinationDivideClassList?size>
            </#if>
            <tr batch="${key}">
                <#if item_index == 0>
                    <td rowspan="${batchRowCount[key]}">${batchNameMap[key]}</td>
                </#if>
                <td rowspan="${rows}">${item.course.subjectName!}</td>
                <td rowspan="${rows}">${item.stuNum!}</td>
                <td rowspan="${rows}">${item.otherInfo!}</td>
                <td rowspan="${rows}">
                    <#if item.devideClassList?exists && item.devideClassList?size gt 0>
                        <#list item.devideClassList as sub>
                            <a class="btn btn-sm btn-white my2 color-blue" href="javascript:void(0)" data-class="${sub.id!}"  teachClassId="${sub.id!}" onclick='teachClassRename("${sub.id!}", this)'>
                                ${sub.className!}<span class="color-999">(${sub.studentCount})</span>
                                <i class="fa fa-times-circle color-red js-del"></i>
                            </a>
                        </#list>
                    </#if>
                </td>
                <#if item.combinationDivideClassList?exists && item.combinationDivideClassList?size gt 0>
                    <td>${item.combinationDivideClassList[0].studentCount}</td>
                    <td>
                        <a class="btn btn-sm btn-white my2 color-blue" href="javascript:void(0)" data-class="${item.combinationDivideClassList[0].id!}" onclick='teachClassRename("${item.combinationDivideClassList[0].id!}", this)'>
                            ${item.combinationDivideClassList[0].className!}<span class="color-999">(${item.combinationDivideClassList[0].studentCount})</span>
                            <i class="fa fa-times-circle color-red js-del"></i>
                        </a>
                    </td>
                <#else>
                    <td></td>
                    <td></td>
                </#if>
                <td rowspan="${rows}">${item.freeStuNum!}</td>
                <td rowspan="${rows}">
                    <a class="table-btn color-blue teachClassOpen" href="javascript:void(0)" courseId="${item.course.id!}" batch="${key}">分班</a>
                    <#--<a class="table-btn color-blue" href="javascript:void(0)">解散</a>-->
                </td>
            </tr>
            <#if item.combinationDivideClassList?exists && item.combinationDivideClassList?size gt 0>
                <#list 1..item.combinationDivideClassList?size as index>
                    <#if index == item.combinationDivideClassList?size>
                        <#break>
                    </#if>
                    <tr>
                        <td>${item.combinationDivideClassList[index].studentCount}</td>
                        <td>
                            <a class="btn btn-sm btn-white my2 color-blue" data-class="${item.combinationDivideClassList[index].id!}" href="javascript:void(0)" onclick='teachClassRename("${item.combinationDivideClassList[index].id!}", this)' >
                                ${item.combinationDivideClassList[index].className!}<span class="color-999">(${item.combinationDivideClassList[index].studentCount})</span>
                                <i class="fa fa-times-circle color-red js-del"></i>
                            </a>
                        </td>
                    </tr>
                </#list>
            </#if>
        </#list>
    </#list>
    </tbody>
</table>
<h4 class="form-title">
    <b class="mr20">跟随行政班上课的<#if planType == "A">选考<#else>学考</#if>教学班</b>
</h4>
<table class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th>序号</th>
        <th>行政班</th>
        <th>所属组合班</th>
        <th>人数</th>
        <th>跟随行政班上课科目</th>
        <th>合班班级</th>
    </tr>
    </thead>
    <tbody>
    <#if combinationClassList?exists && combinationClassList?size gt 0>
        <#list combinationClassList as item>
            <tr>
                <td>${item_index + 1}</td>
                <td>${item.relateName!}</td>
                <td>${item.className!}</td>
                <td>${item.studentCount!}</td>
                <td>${item.subNames!}</td>
                <td>
                    <#if combinationToTeachClassMap?exists && combinationToTeachClassMap?size gt 0>
                        <#if combinationToTeachClassMap[item.id]?exists && combinationToTeachClassMap[item.id]?size gt 0>
                            <#list combinationToTeachClassMap[item.id] as teachClass>
                                <a class="btn btn-sm btn-white my2" href="javascript:void(0)" onclick='teachClassRename("${teachClass.id}", this)'>
                                    <input class="hidden" value="${teachClass.id}">
                                    <span class="color-blue">${teachClass.className!}</span>
                                    <span class="label label-lightgrey">${teachClass.batch}</span>
                                </a>
                            </#list>
                        </#if>
                    </#if>
                </td>
                <#--<a class="btn btn-sm btn-white my2" href="javascript:void(0)">
                    <span class="color-blue">01班-物理选(50)</span>
                    <span class="label label-lightgrey">加选考1</span>
                </a>-->
            </tr>
        </#list>
    </#if>
    </tbody>
</table>
<div class="layer layer-arrangejxb">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group" id="arrangejxb">
				<label class="col-sm-4 control-label no-padding-right">班级最大人数：</label>
				<div class="col-sm-3">
					<div class="input-group">
                        <input class="form-control" type="text" name="openjxbStunum" id="openjxbStunum"  maxLength="3" value="0">
                    </div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
    var renameObj;

    $(function () {
        $(".teachClassOpen").on("click", function () {
            var url =  '${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/teachClassStudentSet?subjectId=' + $(this).attr("courseId") + '&batch=' + $(this).attr("batch") + '&planType=${planType!}';
            $("#aa").load(url);
        });
        
        // 删除
		$(".js-del").click(function(e){
			e.stopPropagation();
			var delId=$(this).parent().attr("data-class");
			deletejxb(delId);
		})
    });

    // 查看教学班学生
    function showTeachClassStudent(id) {
        var url =  '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/showTeachClassStudent?teachClassId=' + id;
        layerDivUrl(url, {title: "查看学生", width: 700, height: 515});
    }

    function teachClassRename(id ,obj) {
        renameObj = obj;
        var url =  '${request.contextPath}/newgkelective/${divideId!}/floatingPlan/renameTeachClass?teachClassId=' + id;
        layerDivUrl(url, {title: "修改教学班名称", width: 350, height: 200});
    }
    
    var isDelete=false;
    function deletejxb(id) {
        var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("确定删除该教学班",options,function(){
			deleteByjxbId(id);
		},function(){
			isDelete=false;
		});
    }
    function deleteByjxbId(id){
    	if(isDelete){
    		return;
    	}
    	var ii = layer.load();	
    	isDelete=true;
    	$.ajax({
    		 url: "${request.contextPath}/newgkelective/${divideId!}/floatingPlan/deleteTeachClass",
             data: {"classIds": id},
             dataType: 'json',
             type: 'post',
             success: function (data) {
             	var jsonO=data;
				if(jsonO.success){
					layer.closeAll();
					layer.msg("删除成功！", {
						offset: 't',
						time: 2000
					});
					isDelete=false;
					buildTeachClass();
	 			}else{
	 				isDelete=false;
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
	 			}	
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
    }

    function refreshTeachClassSet() {
        $("#aa").load("${request.contextPath}/newgkelective/${divide.id!}/floatingPlan/teachClassSet?planType=${planType!}");
    }

    function teachClassImport() {
        $("#aa").load("${request.contextPath}/newgkelective/floatingplan/teachClass/import/execute?divideId=${divide.id!}&subjectType=${planType!}");
    }
    var isStarts=false;
    function reStartArrangeJxb(){
    	var subjectType='${planType!}';
    	var subjectTypeName=subjectType=="A"?'选考':'学考';
		if(isStarts){
			return;
		}
		isStarts=true;
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否需要重新安排教学班，如果选择确定，将会清除"+subjectTypeName+"已经安排的所有教学班",options,function(){
			layer.closeAll();
			arrangeJxb(subjectType);	
		},function(){
			isStarts=false;
		});
		isStarts=false;
    }
    var isSave=false;
    function arrangeJxb(subjectType){
    	if(isSave){
    		return;
    	}
    	isSave=true;
    	//弹出框
    	layer.open({
			type: 1,
			shadow: 0.5,
			title: '分教学班',
			area: ['520px'],
			btn: ['确定', '取消'],
			btn1:function(){
				saveArrangeJxb(subjectType);
			},
			btn2:function(){
				isSave=false;
				isStarts=false;
				layer.closeAll();
			},
			content: $('.layer-arrangejxb')
		});
    }
    function saveArrangeJxb(subjectType){
    	var openjxbStunum=$.trim($("#openjxbStunum").val());
		if(openjxbStunum==""){
			isStarts=false;
			layer.tips('不能为空', $("#openjxbStunum"), {
				tipsMore: true,
				tips:3				
			});
			isSave=false;
			return;
		}
		if (!/^\d+$/.test(openjxbStunum)) {
			layer.tips('请输入整数', $("#openjxbStunum"), {
				tipsMore: true,
				tips:3				
			});
			isSave=false;
			return false;
		}
		var oo=parseInt(openjxbStunum);
		if(oo<1){
			layer.tips('请输入正整数', $("#openjxbStunum"), {
				tipsMore: true,
				tips:3				
			});
			isSave=false;
			return false;
		}
		if(oo>999){
			layer.tips('不能超过3位', $("#openjxbStunum"), {
				tipsMore: true,
				tips:3				
			});
			isSave=false;
			return false;
		}
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/autoJxb',
			data:{'studentNum':oo,'subjectType':subjectType},
			dataType: "json",
			success: function(data){
					var jsonO = data;
					if(jsonO.success){
						layer.closeAll();
						layer.msg("分班成功！", {
							offset: 't',
							time: 2000
						});
						isSave=false;
						isStarts=false;
						buildTeachClass();
		 			}else{
		 				isSave=false;
						isStarts=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
		 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
		
    }
    var isClearJxb=false;
    function clearArrangeJxb(){
    	var subjectType='${planType!}';
    	var subjectTypeName=subjectType=="A"?'选考':'学考';
    	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定需要解散教学班，如果选择确定，将会清除"+subjectTypeName+"已经安排的所有教学班",options,function(){
			layer.closeAll();
			clearJxbs(subjectType);	
		},function(){
			isClearJxb=false;
		});
		isClearJxb=false;
    }
    
    function clearJxbs(subjectType){
    	if(isClearJxb){
    		return;
    	}
    	isClearJxb=true;
    	$.ajax({
			url:'${request.contextPath}/newgkelective/${divideId!}/divideClass/clearJxbOnly',
			data:{'subjectType':subjectType},
			dataType: "json",
			success: function(data){
					var jsonO = data;
					if(jsonO.success){
						layer.closeAll();
						layer.msg("解散成功！", {
							offset: 't',
							time: 2000
						});
						isClearJxb=false;
						buildTeachClass();
		 			}else{
						isClearJxb=false;
			 			layer.closeAll();
			 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
		 			}	
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
    }
    
</script>