<#if chooseClazzList?exists && (chooseClazzList?size > 0)>
<table class="table table-striped table-hover tableSelectCourse">
	<thead>
		<tr>
			<th width="5%">选择</th>
			<th width="">班级</th>
		</tr>
	</thead>
	<tbody>
	<#list chooseClazzList?keys as key>
		<tr>
			<td>
				<label class="pos-rel">
					<input type="radio" class="wp form-control form-radio" name="chosenClass" value="${key!}"/>
					<span class="lbl"></span>
				</label>
			</td>
			<td class="canSelect">
			<#list chooseClazzList[key] as clsName>
				<div class="filter-item">
					${clsName.className!}
					<#if teacherNamesByClassId[clsName.id]?exists>
					<br/>
					${teacherNamesByClassId[clsName.id]?default('')}
					</#if>
					<br/>
					${clsName.studentCount}人
				</div>
			</#list>
			</td>
		</tr>
	</#list>
	</tbody>
</table>
</#if>
<#if chooseClazzList?exists && (chooseClazzList?size > 0)>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveStuSubChange()">确定</a>
</div>
<#else>
未找到合适的班级
</#if>
<script>
	var isSubmit=false;
	function doSaveStuSubChange(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		if(!$('input[name="chosenClass"]:checked').length>0){
			showMsgErrorSmall("请选择！");
			isSubmit = false;
			return;
		}
		var chosenClassIds = $('input[name="chosenClass"]:checked').val();
		var stuId = $("#stuId").val();
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/newgkelective/${arrayId!}/arrayResult/saveChangeStuChoose',
		    data: {'stuId':stuId,'chosenClassIds':chosenClassIds},
		    dataType : 'json',
		    success:function(data) {
		    	var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
		 			layer.msg("操作成功！", {
							offset: 't',
							time: 2000
						});
				  	doChangeStu();
    			}
		    	layer.close(ii);
		    }
		});
	}
	$(function(){
		$('.canSelect').on('click',function(){
			var $checkbox=$(this).parent().find('input[type=radio]')
			if(!$checkbox.is(':checked')){
				$checkbox.prop('checked',true);
			}
		});
	});
</script>