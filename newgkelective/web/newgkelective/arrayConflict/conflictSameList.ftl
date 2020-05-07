<table class="table table-striped table-hover">
	<thead>
		<tr>
			<th width="5%">选择</th>
			<th width="">班级</th>
		</tr>
	</thead>
	<tbody>
	<#if factList?exists && factList?size gt 0>
	<#list factList?keys as key>
		<tr>
			<td>
				<label class="pos-rel">
					<input type="radio" class="wp form-control form-radio" name="chosenClass" value="${key!}"/>
					<span class="lbl"></span>
				</label>
			</td>
			<td class="canSelect">
			<#list factList[key] as items>
				<div class="filter-item">
					${items.className!}(${items.studentCount?default(0)})
				</div>
			</#list>
			</td>
		</tr>
	</#list>
	</#if>
	</tbody>
</table>

<#if factList?exists && (factList?size gt 0)>
<div style="text-align:center">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="saveClasses()">确定</a>
</div>
<#else>
未找到合适的班级
</#if>
<script>
	var isSubmit=false;
	function saveClasses(){
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
		var ii = layer.load();
		$.ajax({
		    url:'${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/saveBySameStudent',
			data: {'studentId':'${studentId!}','classIdstr':chosenClassIds},
			dataType : 'json',
		    success:function(data) {
		    	var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"操作失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layer.msg(jsonO.msg, {offset: 't',time: 2000});
					var url =  '${request.contextPath}/newgkelective/${arrayId!}/arrayStudent/conflictIndex/page';
					$("#showList").load(url);
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