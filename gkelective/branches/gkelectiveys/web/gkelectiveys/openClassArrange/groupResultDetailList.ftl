<div class="detail-div">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">班级：</span>
			<div class="filter-content">
				<p>${className!}</p>
			</div>
		</div>
		<a href="javascript:" class="btn btn-blue pull-right back-btn detaileBack noprint">返回</a>
	</div>
	<#if (rounds.step < 4)>
		<#assign nowStep=true>
	<#else>
		<#assign nowStep=false>
	</#if>
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<#if nowStep>
				<th class="noprint">选择</th>
				</#if>
				<th>学号</th>
				<th>姓名</th>
				<th>性别</th>
			</tr>
		</thead>
		<tbody>
		<#if stuList?exists && stuList?size gt 0>
		<#list stuList as stu>
			<tr>
				<#if nowStep>
				<td class="noprint">
					<label class="pos-rel">
						<input name="studentIdName" type="checkbox" class="wp checkBoxItemClass" value="${stu.id!}">
						<span class="lbl"></span>
					</label>
				</td>
				</#if>
				<td>${stu.studentCode!}</td>
				<td>${stu.studentName!}</td>
				<td>${mcodeSetting.getMcode("DM-XB","${stu.sex!}")}</td>
			</tr>
		</#list>
		</#if>
		</tbody>
	</table>
	<#if nowStep>
	<div class="filter noprint">
		<div class="filter-item" style="margin-right:10px;">
			<span class="filter-name" style="margin-right:2px;">前</span>
			<div class="filter-content" >
				<input type="text" style="width:50px;margin-right:2px;" class="form-control" name="searchSomeRows" id="searchSomeRows" value="" maxlength="3">
			</div>
			<span class="filter-name" style="margin-right:2px;">行</span>
			<div class="filter-item-right">
				<a href="javascript:"  class="btn btn-sm btn-lightblue" onclick="searchRow()">确定勾选</a>
				<a href="javascript:"  class="btn btn-sm btn-lightblue" onclick="doDelStu('${groupId!}')">脱离</a>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">移动至&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<div class="filter-content">
			<select name="" id="chosenClassId" class="form-control" style="width:170px;">
				<#if groupList?? && (groupList?size>0)>
				<option value="">请选择班级</option>
				<#list groupList as item>
				<option value="1#${item.id!}#${item.subjectIds!}##" >${item.groupName!}</option>
				</#list>
				<#else>
				<option value="">无班级可移动</option>
				</#if>
			</select>
			</div>
			<div class="filter-item-right">
			<a href="javascript:" class="btn btn-sm btn-lightblue" onclick="doSaveChange()">确认</a>
			</div>
		</div>
	</div>
	</#if>
</div>
<script>
<#if !stuList?exists || stuList?size == 0>
	layerTipMsgWarn("提示","该班级下已经没有学生了");
	reloadListData();
</#if>
$(function(){
	$('.detaileBack').on("click",function(){
		reloadListData();
	});
});
function searchRow(){
	var num=$("#searchSomeRows").val().trim();
	var pattern=/[^0-9]/;
	if(pattern.test(num) || num.slice(0,1)=="0"){
		layer.msg("只能输入非零的整数！", {
			offset: 't',
			time: 2000
		});
		$("#searchSomeRows").val('');
		$("#searchSomeRows").focus();
		return false;
	}
	var rows=parseInt(num);
	if(num<=0){
		return false;
	}
	if($('input:checkbox[name=studentIdName]').length>=0){
		$('input:checkbox[name=studentIdName]').each(function(i){
			if(i<rows){
				$(this).prop('checked',true);
			}else{
				$(this).prop('checked',false);
			}
			
		});
	}
}
<#if nowStep>
var isSubmit=false;
function doSaveChange(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	var chosenClassId = $("#chosenClassId").val();
	if(chosenClassId == ''){
		layer.msg("未选择班级", {
			offset: 't',
			time: 2000
		});
		isSubmit = false;
		return;
	}
	if($("input[name='studentIdName']:checked").length == 0 ){
		layer.msg("未选择学生", {
			offset: 't',
			time: 2000
		});
		isSubmit = false;
		return;
	}
	var leftClassSelectId = "1#${groupClass.id!}#${groupClass.subjectIds!}##";
	var rightClassSelectId = chosenClassId;
	var leftAddStu = "";
	var rightAddStu = "";
	$("input[name='studentIdName']:checked").each(function(){
		if(rightAddStu != ""){
			rightAddStu+=","+$(this).val();
		}else{
			rightAddStu+=$(this).val()
		}
	});
	var ii = layer.load();
	$.ajax({
	    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/student/move',
	    data:{'oldClassId':leftClassSelectId,'newClassId':rightClassSelectId,'stuId':rightAddStu},
	    dataType : 'json',
	    success:function(data) {
	    	var jsonO = data;
	 		if(!jsonO.success){
	 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
	 		}else{
	 			layer.closeAll();
				reloadDetail();
			}
 			isSubmit = false;
			layer.close(ii);
	    }
	});
}
</#if>
function reloadDetail(){
	var url = "${request.contextPath}/gkelective/${roundsId!}/openClassArrange/group/detail/page?groupId=${groupId!}";
	$("#resultListDiv").load(url);
}

var oids = '';
function doDelStu(oldIds){
	if($("input[name='studentIdName']:checked").length == 0 ){
		layer.msg("未选择学生", {
			offset: 't',
			time: 2000
		});
		return;
	}
	oids = oldIds;
	var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
	showConfirm("确定将选中的学生脱离此组合班吗？",options,delStu,function(){});
}

function delStu(){
	var rightAddStu = "";
	$("input[name='studentIdName']:checked").each(function(){
		if(rightAddStu != ""){
			rightAddStu+=","+$(this).val();
		}else{
			rightAddStu+=$(this).val()
		}
	});
	var ii = layer.load();
	$.ajax({
		url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/student/delete',
		data:{"oldGroupId":oids,"stuId":rightAddStu},
		dataType : 'json',
		type:'post',
		success:function(data) {
			var jsonO = data;
	 		if(jsonO.success){
	 			layer.closeAll();
			  	reloadDetail();
	 		}
	 		else{
	 			layer.closeAll();
	 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
			layer.close(ii);
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
	
	oids = '';
}
</script>
