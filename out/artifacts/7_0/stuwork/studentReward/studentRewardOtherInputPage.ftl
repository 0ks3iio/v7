<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#import "/fw/macro/popupMacro.ftl" as popup />
<!-- /section:basics/sidebar -->

<div class="box box-default">
	<div class="box-body">
		
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li <#if classesType?default('1')=='1'>class="active"</#if> role="presentation" onclick="changeInput('1')"><a href="#" role="tab" data-toggle="tab">学科竞赛</a></li>
			<li <#if classesType?default('1')=='2'>class="active"</#if> role="presentation" onclick="changeInput('2')"><a href="#" role="tab" data-toggle="tab">校内奖励</a></li>
			<li <#if classesType?default('1')=='3'>class="active"</#if> role="presentation" onclick="changeInput('3')"><a href="#" role="tab" data-toggle="tab">节日活动奖励</a></li>
			<li <#if classesType?default('1')=='4'>class="active"</#if> role="presentation" onclick="changeInput('4')"><a href="#" role="tab" data-toggle="tab">其他奖励</a></li>
		</ul>
		<div class="tab-content">
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
			<div class="filter-item filter-item-right">
				<button type="button" class="btn btn-blue " onclick="doImport()">导入</button>
				<button type="button" class="btn btn-blue js-addActivityAward" >新建</button>
			</div>
		</div>
		<form id="subForm">
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>序号</th>
					<th>学年</th>
					<th>学期</th>
					<th>姓名</th>
					<th>学号</th>
					<th>奖励时间</th>
					<th>项目名称</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody class="point_body">
				<#assign points_index = 0>
				<#list pointList as item>
					<tr>
						<td class="point_index">${points_index?default(0)+1}</td>
						<td>${item.acadyear!}</td>
						<td><#if item.semester=='1'>第一学期<#elseif item.semester=='2'>第二学期</#if></td>
						<td>${item.studentName!}</td>
						<td>${item.stucode!}</td>
						<td>${item.creationTime?string('yyyy-MM-dd')}</td>
						<td>
						<input name="dyStudentRewardPoints[${points_index!}].id" type="hidden"  value="${item.id!}" />
						<input name="dyStudentRewardPoints[${points_index!}].studentId" type="hidden"  value="${item.studentId!}" />
						${item.remark!}
						</td>
						<td>
							<a class="color-red js-del" dataId="${item.id!}" onclick="deleteTr(this)">删除</a>
						</td>
					</tr>
					<#assign points_index =points_index+1 />
				</#list>
				
			</tbody>
		</table>
		</form>
		</div>
	</div>
</div>


<!-- S 新建活动奖励 -->
<div class="layer layer-addActivityAward">
	<form id="periodForm">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">学年：</label>
				<div class="col-sm-9">
					<div class="filter-content">
					<select name="addAcadyear" id="addAcadyear" class="form-control" >
						<#list acadyearList as ac>
							<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
						</#list>
					</select>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">学期：</label>
				<div class="col-sm-9">
					<select name="addAemester" id="addSemester" class="form-control" >
						<option value="1" <#if semester == 1>selected</#if>>第一学期</option>
						<option value="2" <#if semester == 2>selected</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">项目名称：</label>
				<div class="col-sm-9">
					<input name="remark" maxlength="50" class="remark_input form-control" id="remark"  value="" type="text"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">学生：</label>
				<div class="col-sm-9">
					
					<@popup.selectMoreStudent clickId="stuPointAdd" id="stuIds" name="stuNames" handler="onStuPointAdd()">
						<input type="hidden" id="stuIds" value="">
						<input type="text" id="stuNames" name="stuNames" class="form-control" value="" readonly="readonly" onclick="$('#stuPointAdd').click()">
						<button style="display:none" type="button" class="btn btn-blue" id="stuPointAdd">添加学生</button>
					</@popup.selectMoreStudent>
				</div>
			</div>
		</div>
	</div>
	</form>
</div>

<script>

	function onStuPointAdd(){
	}
	
	//var points_index = ${points_index?default(0)};
	$(function(){
		//dealInputTable('.combine_name');
		$('.js-addActivityAward').on('click', function(e){
			e.preventDefault();
			$("#remark").val('');
			
			layer.open({
				type: 1,
				shade: 0.5,
				title: '新增',
				area: '500px',
				btn: ['确定', '取消'],
				yes: function(index){
					var check = checkValue('#periodForm');
					if(!check){
						return;
					}
					var remark = $("#remark").val();
					console.log(remark)
					var acadyear = $("#addAcadyear").val();
					var semester = $("#addSemester").val();
					var stuIds = $("#stuIds").val();
					if(isSubmit){
						return;
					}
					$.ajax({
						url:"${request.contextPath}/stuwork/studentReward/studentRewardOtherAdd",
						data: { 'remark': remark,'stuId':stuIds,'acadyear':acadyear,'semester':semester},  
						type:'post',
						success:function(data) {
							var jsonO = JSON.parse(data);
								if(jsonO.success){
									layer.closeAll();
									layerTipMsg(jsonO.success,"成功",jsonO.msg);
									changeInput('4');
								}else{
									isSubmit = false;
									layerTipMsg(jsonO.success,"失败",jsonO.msg);
								}
						},
				 		error : function(XMLHttpRequest, textStatus, errorThrown) {
				 			
						}
					});
					
				},
				content: $('.layer-addActivityAward')
			})
		});

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

	
	
	function flashIndex(){
		var i=1;
		$(".point_index").each(function(){
			$(this).html(i);
			i++;
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
		$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardInputPage?classesType=4&acadyear="+$("#acadyear").val()+"&semester="+$("#semester").val());
	}
	
	function changeInput(classesType){
		$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardInputPage?classesType="+classesType);
	}
		
	function doImport(){
		$(".model-div").load("${request.contextPath}/stuwork/studentReward/studentRewardPointImport/main?classesType=${classesType!}");
	}

</script>
		
