<#if finList?exists && (finList?size > 0)>
<table class="table table-striped table-hover tableSelectCourse">
	<thead>
		<tr>
			<th width="5%">选择</th>
			<th width="20%">组合班</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<#list finList as item>
			<tr>
				<td>
					<label class="pos-rel">
						<input type="radio" class="wp form-control form-radio" name="chosenClass" value="1#${item.id!}"/>
						<span class="lbl"></span>
					</label>
				</td>
				<td class="canSelect">${item.groupName!}（${item.number!}人）</td>
				<td class="canSelect"></td>
			</tr>
		</#list>
	</tbody>
</table>
</#if>
<#if dtolist?exists && (dtolist?size > 0)>
<table class="table table-striped table-hover tableSelectCourse">
	<thead>
		<tr>
			<th width="5%">选择</th>
			<th width="">班级</th>
		</tr>
	</thead>
	<tbody>
	<#list dtolist as item>
		<tr>
			<td>
				<label class="pos-rel">
					<input type="radio" class="wp form-control form-radio" name="chosenClass" value="2#<#if item.groupClassId?default('')!=''>${item.groupClassId!}@</#if>${item.classIds!}"/>
					<span class="lbl"></span>
				</label>
			</td>
			<td class="canSelect">
			<#if item.groupClassId?default('')!=''>
				<div class="filter-item">
					${item.groupClassName!}
				</div>
			</#if>
			<#list item.classNames as clsName>
				<div class="filter-item">
					${clsName}
				</div>
			</#list>
			</td>
		</tr>
	</#list>
	</tbody>
</table>
</#if>
<#if (finList?exists && (finList?size > 0)) || (dtolist?exists && (dtolist?size > 0))>
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
		var strSp = chosenClassIds.split("#");
		var searchClassType = strSp[0];
		chosenClassIds = strSp[1];
		var stuId = $("#stuId").val();
		var searchSubject = $("#searchSubject").val();
		var searchSubjectIds = "";
		for(var i=0;i<searchSubject.length;i++){
			if(searchSubjectIds == ""){
				searchSubjectIds+=searchSubject[i];
			}else{
				searchSubjectIds+=","+searchSubject[i];
			}
		}
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/gkelective/${roundsId!}/openClassArrange/list/stuSubChange/save',
		    data: {'stuId':stuId,'searchClassType':searchClassType,'chosenClassIds':chosenClassIds,'searchSubjectIds':searchSubjectIds},
		    dataType : 'json',
		    success:function(data) {
		    	var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"操作成功",jsonO.msg);
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
