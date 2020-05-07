<form id="filterForm">
<input type="hidden" name="examId" value="${examId!}"/>
<table class="table table-bordered table-striped table-hover no-margin">

	<tbody>
		<#if stuDtoList?exists && (stuDtoList?size > 0)>
            <thead>
            <tr>
                <th style="width:5%">
                    <label class="pos-rel"><input type="checkbox" id="studentCheckboxAll" class="wp"  value="" onchange="studentCheckboxAllSelect()">
                        <span class="lbl" style="font-weight:bold;">全选</span></label></th>
                <th class="">姓名</th>
                <th class="">性别</th>
                <th class="">学号</th>
                <th class="">班级</th>
                <th class="">操作</th>
            </tr>
            </thead>
			<#list stuDtoList as dto>
				<tr>
					<td> <label class="pos-rel">
                        <input   name="stuCheckboxName" type="checkbox" class="wp" value="${dto.student.id!}">
                        <span class="lbl"></span>
                    </label></td>
					<td class=""><input type="hidden" name="stuDtoList[${dto_index}].studentId" value="${dto.student.id!}">
					${dto.student.studentName!}
					</td>
					<td class="">${mcodeSetting.getMcode("DM-XB","${dto.student.sex!}")}</td>
					<td class="">${dto.student.studentCode!}</td>
					<td class="">${dto.className!}</td>
					<td class="">
						<label class="pos-rel">
							<input type="radio" class="wp form-control form-radio" name="stuDtoList[${dto_index}].filter" value="1"  <#if dto.filter?default('1') == '1'>checked</#if>>
							<span class="lbl">排考</span>
						</label>
						<label class="pos-rel">
							<input type="radio" class="wp form-control form-radio" name="stuDtoList[${dto_index}].filter" value="0"  <#if dto.filter?default('1') == '0'>checked</#if>>
							<span class="lbl">不排考</span>
						</label>
					</td>
				</tr>
			</#list>
		<#else >
            <div class="no-data-container">
                <div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
                    <div class="no-data-body">
                        <p class="no-data-txt">暂无相关数据</p>
                    </div>
                </div>
            </div>
		</#if>
	</tbody>
</table>
</form>
<script>
$(function(){
	var canEdit=$("#canEdit").val();
	if(canEdit && canEdit=="false"){
		$(".form-radio").attr("disabled", true);
	}
});
function studentCheckboxAllSelect(){
	if($("#studentCheckboxAll").is(':checked')){
		$('input:checkbox[name=stuCheckboxName]').each(function(i){
			$(this).prop('checked',true);
		});
	}else{
		$('input:checkbox[name=stuCheckboxName]').each(function(i){
			$(this).prop('checked',false);
		});
	}
}
var ids;
function setFilter(type){
	ids="";
	var objElem=$('input:checkbox[name=stuCheckboxName]');
	if(objElem.length>0){
		$('input:checkbox[name=stuCheckboxName]').each(function(i){
			if($(this).is(':checked')){
				ids=ids+","+$(this).val();
			}
		});
	}else{
		layerTipMsg(false,"提示","请先选择学生！");
		return;
	}
	if(ids==""){
		layerTipMsg(false,"提示","请先选择学生！");
		return;
	}
	ids=ids.substring(1);


	var ii = layer.load();
    $.ajax({
			url:'${request.contextPath}/exammanage/examArrange/filterSet',
			data: {'ids':ids,'examId':'${examId!}','type':type},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
					isSubmit=false;
				  	showFilterList();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"设置失败",jsonO.msg);
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
}


</script>