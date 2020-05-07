<form id="checkForm">
<div class="filter">
	<div class="filter-item">
		<span class="filter-name">班主任：</span>
		<div class="filter-content">
			<p>${teacherName!}</p>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">班级：</span>
		<div class="filter-content">
			<select name="classId" id="classId" class="form-control" onchange="doSearch()" style="width:120px">
				<#if clazzList?exists && clazzList?size gt 0>.
					<#list clazzList as item>
						<option value="${item.id!}" <#if item.id==attDto.classId?default("")>selected="selected"</#if> >${item.classNameDynamic!}</option>
					</#list>
				</#if>
			</select>
		</div>
	</div>
	<#--<div class="filter-item" id="dateDiv">
		<span class="filter-name">日期：</span>
		<div class="filter-content">
			<div class="input-group">
				<input class="form-control date-picker startTime-date date-picker-time" value="${attDto.searchDate?string("yyyy-MM-dd")}" vtype="data" style="width: 120px" type="text"  name="searchDate" id="searchDate"  onchange="doSearch();">
				<span class="input-group-addon">
					<i class="fa fa-calendar"></i>
				</span>
			</div>
		</div>
	</div>-->
	<div class="filter-item">
		<span class="filter-name">状态：</span>
		<div class="filter-content">
			<select name="attStatus" id="attStatus" class="form-control" onchange="doSearch()">
				<option value=0>---请选择---</option>
				<option value=1 <#if 1==attDto.attStatus?default("")>selected="selected"</#if>>在校</option>
				<option value=2 <#if 2==attDto.attStatus?default("")>selected="selected"</#if>>离校</option>
			</select>
		</div>
	</div>
</div>
</form>

<div class="table-container">
	<div class="table-container-header">
		<button class="btn btn-blue"  onclick="saveStatus('',1)">在校</button>
		<button class="btn btn-blue"  onclick="saveStatus('',2)">离校</button>
	</div>
	<div class="table-container-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th width="80">
						<label><input type="checkbox" id="checkAll" class="wp"><span class="lbl"></span> 序号</label>
					</th>
					<th>学生姓名</th>
					<th>学号</th>
					<th>刷卡时间</th>
					<th>状态</th>
				</tr>
			</thead>
			<tbody>
				<#if gateList?exists && gateList?size gt 0>
					<#list gateList as item>
						<tr>
							<td><label><input type="checkbox" class="wp checked-input" value="${item.id!}"><span class="lbl"></span>${item_index+1}</label></td>
							<td>${item.studentName!}</td>
							<td>${item.studentCode!}</td>
							<td>${(item.clockInTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
							<td>
								<select name="status" id="${item.id!}" style="width:150px;" class="form-control" onchange="saveStatus('${item.id!}')">
									<option value=1 <#if 1==item.status?default(0)>selected="selected"</#if>>在校</option>
									<option value=2 <#if 2==item.status?default(0)>selected="selected"</#if>>离校</option>
								</select>
							</td>
						</tr>
					</#list>
				<#else>
					<tr>
						<td colspan="5" align="center">暂无数据</td>
					</tr>
				</#if>
			</tbody>
		</table>
	</div>
</div>
<script>
	$(function(){
		<#-- //初始化日期控件
			var viewContent={
				'format' : 'yyyy-mm-dd',
				'minView' : '2',
				'endDate' : '${.now?string('yyyy-MM-dd')}',
				'startDate' : '${beginDate?string('yyyy-MM-dd')}'
			};
			initCalendarData("#dateDiv",".date-picker",viewContent);-->
			
		$("#checkAll").click(function(){
			var ischecked = false;
			if($(this).is(':checked')){
				ischecked = true;
			}
		  	$(".checked-input").each(function(){
		  		if(ischecked){
		  			$(this).prop('checked',true);
		  		}else{
		  			$(this).prop('checked',false);
		  		}
			});
		});
	});
	function doSearch(){
		$("#itemShowDivId").load("${request.contextPath}/eclasscard/gate/attance/myClassList/page?"+searchUrlValue("#checkForm"));
	}

	var isSubmit = false;
	function saveStatus(id,status){
		if(isSubmit){
        	return;
	    }
	    var ids = "";
		if(id&&id!=''){
			ids = id;
			status = $("#"+id).val();
		}else{
			$(".checked-input").each(function(){
		  		if($(this).is(':checked')){
		  			if(ids==''){
		  				ids = $(this).val();
		  			}else{
		  				ids+=','+$(this).val();
		  			}
		  		}
	  		});
		}
		if(ids==""){
			layerTipMsg(false,"","请选择要更改的学生");
			return;
		}
		isSubmit = true;
		$.ajax({
			url:'${request.contextPath}/eclasscard/gate/attance/saveStatus',
			data:{'ids':ids,'status':status},
			type:"post",
			success:function(data){
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
		 			doSearch();
		 		}else{
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
    			}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		});
	}
</script>
