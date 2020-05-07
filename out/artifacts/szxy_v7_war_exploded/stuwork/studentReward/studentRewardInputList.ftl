<#import "/fw/macro/popupMacro.ftl" as popup />
<!-- /section:basics/sidebar -->
<#assign project = dyStudentRewardPointDto.dyStudentRewardProject>
<#assign setting = dyStudentRewardPointDto.dyStudentRewardSetting>
<#assign pointList = dyStudentRewardPointDto.dyStudentRewardPoints>



<a onclick="script:backInputPage()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>

<div class="box box-default">
	<div class="box-body">
		<div class="explain">
			<#if classesType?default('1')=='1'>
			<p>名称：${project.rewardClasses!}--${project.projectName!}--${setting.rewardGrade!}&emsp;&emsp;奖级：${setting.rewardLevel!}&emsp;&emsp;分值:${setting.rewardPoint?default('录入时输入')}</p>
			<#elseif classesType?default('1')=='2'>
			<p>名称：${project.rewardClasses!}--${project.projectName!}&emsp;&emsp;分值:${setting.rewardPoint?default('录入时输入')}</p>
			<#elseif classesType?default('1')=='3'>
			<p>名称：${project.rewardClasses!}-第${project.rewardPeriod!}年--${project.projectName!}奖级：${setting. rewardLevel!}&emsp;&emsp;分值:${setting.rewardPoint?default('录入时输入')}</p>
			</#if>
		</div>

		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" onchange="flashPage()">
						<#if classesType?default('1')=='3'>
							<option value="${acadyear!}" selected>${acadyear!}</option>
						<#else>
							<#list acadyearList as ac>
								<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
							</#list>
						</#if>	
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" onchange="flashPage()">
						<#if classesType?default('1')=='3'>
							<#if semester == 1>
								<option value="1" selected>第一学期</option>
							<#elseif semester == 2>
								<option value="2" selected>第二学期</option>
							</#if>
						<#else>
							<option value="1" <#if semester == 1>selected</#if>>第一学期</option>
							<option value="2" <#if semester == 2>selected</#if>>第二学期</option>
						</#if>	
					</select>
				</div>
			</div>
			<div class="filter-item">
				<div class="filter-content">
					<div class="input-group input-group-search">
						<select id="field" class="form-control" name="field" style="width:130px;" >
							<option value="1" <#if field?default("")=="1">selected</#if>>学生姓名</option>
							<option value="2" <#if field?default("")=="2">selected</#if>>学号</option>
						</select>
						<div class="pos-rel pull-left">
							<input type="text" name="stuSearch" id="stuSearch" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" value="${stuSearch!}">
						</div>
						<div class="input-group-btn">
							<a type="button" class="btn btn-default" onClick="flashPage()">
								<i class="fa fa-search"></i>
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<button type="button" class="btn btn-blue" id="stuPointAdd">添加学生</button>
				<@popup.selectMoreStudent clickId="stuPointAdd" id="stuIds" name="stuNames" handler="onStuPointAdd()">
					<input type="hidden" id="stuIds" value="">
					<input type="hidden" id="stuNames" class="form-control" value="">
				</@popup.selectMoreStudent>
				<button type="button" class="btn btn-blue" id="stuPointSave" onclick="onStuPointSave()">保存</button>
				<button type="button" class="btn btn-danger" id="stuPointDel" onclick="onStuPointDel()">删除</button>
				<#--
				<button class="btn btn-blue">导出Excel</button>
				-->
			</div>
		</div>
		<form id="subForm">
		<input name="dyStudentRewardProject.id" type="hidden"  value="${project.id!}" />
		<input name="dyStudentRewardSetting.id" type="hidden"  value="${setting.id!}" />
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th ><label class="pos-rel">
							<input type="checkbox" class="wp" id="selectAll">
							<span class="lbl"></span>
						</label>选择</th>
					<th>序号</th>
					<th>学年</th>
					<th>学期</th>
					<th>姓名</th>
					<th>学号</th>
					<th>奖励时间</th>
					<th>分值</th>
					<th>备注</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody class="point_body" id="list">
				<#assign points_index = 0>
				<#list pointList as item>
					<tr>
						<td>
							<label class="pos-rel">
								<input name="itemId" type="checkbox" class="wp" value="${item.id!}">
								<span class="lbl"></span>
							</label>
						</td>
						<td class="point_index">${points_index?default(0)+1}</td>
						<td>${item.acadyear!}</td>
						<td><#if item.semester=='1'>第一学期<#elseif item.semester=='2'>第二学期</#if></td>
						<td>${item.studentName!}</td>
						<td>${item.stucode!}</td>
						<td>${item.creationTime?string('yyyy-MM-dd')}</td>
						<td>
						<input name="dyStudentRewardPoints[${points_index!}].id" type="hidden"  value="${item.id!}" />
						<input name="dyStudentRewardPoints[${points_index!}].studentId" type="hidden"  value="${item.studentId!}" />
						<input nullable="false" <#if setting.rewardPoint?default('')!="">readonly="readonly"</#if> name="dyStudentRewardPoints[${points_index!}].rewardPoint" id="rewardPoint_${points_index!}" maxlength="6" decimalLength="2" vtype="number" max="999" min="0.01" class="remark_input form-control"   value="${item.rewardPoint?string('0.##')}" type="text"/>
						</td>
						<td><input name="dyStudentRewardPoints[${points_index!}].remark" maxlength="50" class="remark_input form-control" id=""  value="${item.remark!}" type="text"/></td>
						<td>
							<a class="color-red js-del" dataId="${item.id!}" onclick="deleteTr(this)">删除</a>
							<a class="color-red js-del" dataId="${item.id!}" onclick="editTr(this)">更改</a>
						</td>
					</tr>
					<#assign points_index =points_index+1 />
				</#list>
				
			</tbody>
		</table>
		</form>
	</div>
</div>

<script>
	//var points_index = ${points_index?default(0)};
	$('#selectAll').on('click',function(){
		var total = $('#list :checkbox').length;
		var length = $('#list :checkbox:checked').length;
		if(length != total){
			$('#list :checkbox').prop("checked", "true");
			$(this).prop("checked", "true");
		}else{
			$('#list :checkbox').removeAttr("checked");
			$(this).removeAttr("checked");
		}
	});

	function deleteTr(obj){
		var that = $(obj);
		var pointId = that.attr("dataId");
		layer.confirm('确定删除吗？', {
			btn: ['确定', '取消'],
			yes: function(index){
				if(pointId && pointId!=''){
					$.ajax({
						url:"${request.contextPath}/stuwork/studentReward/studentRewardPointDelete",
						data: { 'pointId': pointId},  
						type:'post',
						success:function(data) {
							var jsonO = JSON.parse(data);
								if(jsonO.success){
									layerTipMsg(jsonO.success,"成功",jsonO.msg);
									flashPage();
									layer.closeAll();
								}else{
									layerTipMsg(jsonO.success,"失败",jsonO.msg);
									layer.close(index);
								}
						},
				 		error : function(XMLHttpRequest, textStatus, errorThrown) {
				 			
						}
					});
				}else{
					that.closest('tr').remove();
					flashIndex();
					layer.close(index);
				}
				
			}
		})
	}

	function editTr(obj){
     	var that = $(obj);
		var pointId = that.attr("dataId");
	    var url = "${request.contextPath}/stuwork/studentReward/studentRewardPointEdit?projectId=${projectId!}&pointId="+pointId+"&classesType=${classesType!}&settingId=${setting.id!}";
		indexDiv = layerDivUrl(url,{title: "更改类型",width:1300,height:600});
	}

	function onStuPointAdd(){
		var points_index = $(".point_index").size();
		//console.log($("#stuIds").val());
		if($("#stuIds").val()){
			var stuIdArray = $("#stuIds").val().split(",");
			var stuNameArray= $("#stuNames").val().split(",");
			var oldHtml = $(".point_body").html();
			var size = $(".point_index").size();
			for(var i=0;i<stuIdArray.length;i++){
				oldHtml +='<tr>';
				size++;
				oldHtml +='<td class="point_index"><label class="pos-rel"> <input name="itemId" type="checkbox" class="wp" value=""><span class="lbl"></span></label></td>';
				oldHtml +='<td class="point_index">'+size+'</td>';
				oldHtml +='<td>${acadyear!}</td>';
				oldHtml +='<td><#if semester==1>第一学期<#elseif semester==2>第二学期</#if></td>';
				oldHtml +='<td>'+stuNameArray[i]+'</td>';
				oldHtml +='<td></td>';
				oldHtml +='<td></td>';
				points_index +=1;
				oldHtml +='<td><input name="dyStudentRewardPoints['+points_index+'].studentId" type="hidden"  value="'+stuIdArray[i]+'" />';
				oldHtml +='<input  nullable="false" <#if setting.rewardPoint?default('')!="">readonly="readonly"</#if>  name="dyStudentRewardPoints['+points_index+'].rewardPoint" id="rewardPoint_'+points_index+'" maxlength="6" decimalLength="2" vtype="number" max="999" min="0.01" class="remark_input form-control"  value="<#if setting.rewardPoint?default('')!="">${setting.rewardPoint!}<#else>0</#if>" type="text"/></td>';
				oldHtml +='<td><input name="dyStudentRewardPoints['+points_index+'].remark" maxlength="50" class="remark_input form-control" id=""  value="" type="text"/></td>';
				oldHtml +='<td><a class="color-red" onclick="deleteTr(this)">删除</a></td>';
				oldHtml +='</tr>';
			}
			$(".point_body").html(oldHtml);
		}
		flashIndex();

	}


	function flashIndex(){
		var i=1;
		$(".point_index").each(function(){
			$(this).html(i);
			i++;
		});
	}

	function onStuPointDel(){
		var selEle = $('#list :checkbox:checked');
		if(selEle.length<1){
			layerTipMsg(false,"失败",'请先选中学生再删除');
			layer.close(index);
			return;
		}
		layer.confirm('确定要删除选中的奖励记录吗？', {
			btn: ['确定', '取消'],
			yes: function(index){
				var param = new Array();
				for(var i=0;i<selEle.length;i++){
					var rid = selEle.eq(i).val()
					if(rid == ''){
						$(selEle.eq(i)).closest('tr').remove();
						continue;
					}
					param.push(rid);
				}
				deleteByIds(param);
				layer.close(index);
			}
		});
	}

	function deleteByIds(ids){
		if(!ids || ids.length == 0){
			return;
		}
		var url = '${request.contextPath}/stuwork/studentReward/studentRewardPointDeleteBatch';
		var params = {"ids":ids};
		$.ajax({
			type: "POST",
			url: url,
			data: params,
			success: function(msg){
				//alert( "Data Saved: " + msg );
				if(msg.success){
					flashPage();
				}else{
					layerTipMsg(false,"失败",msg.msg);
				}
			},
			dataType: "JSON"
		});
	}

	var isSubmit =false;
	function onStuPointSave(){
		if(isSubmit){
			return;
		}
		var check = checkValue('#subForm');
		//alert(check);
		if(!check){
			return;
		}
		isSubmit=true;
		var options = {
			url : "${request.contextPath}/stuwork/studentReward/pointSave?classesType=${classesType!}&acadyear="+$("#acadyear").val()+"&semester="+$("#semester").val(),
			dataType : 'json',
			success : function(data){
		 		var jsonO = data;
			 	if(!jsonO.success){
			 		layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
			 		isSubmit=false;
			 		return;
			 	}else{
			 		layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
					flashPage();
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subForm").ajaxSubmit(options);
	}
	
	function flashPage(){
		var sn = $('#stuSearch').val();
		if(sn.indexOf('\'')>-1 || sn.indexOf('%')>-1){
			layerTipMsgWarn("请确认输入内容中不包含单引号、百分号等特殊符号！");
			return ;
		}
		var url = "${request.contextPath}/stuwork/studentReward/studentRewardInputList?settingId=${setting.id!}&acadyear="+$("#acadyear").val()
					+"&semester="+$("#semester").val()+"&field="+$('#field').val()+"&stuSearch="+encodeURI(sn);
		$(".model-div").load(url);
	}
	
	function backInputPage(){
		$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardInputPage?classesType=${classesType!}<#if classesType?default('1')=='3'>&_pageSize=${_pageSize!}&_pageIndex=${_pageIndex!}</#if>");
	}
</script>