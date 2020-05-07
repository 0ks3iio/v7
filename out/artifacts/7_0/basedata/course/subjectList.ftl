<table class="table table-bordered table-striped" >
	<table-hover>
		<thead>
			<tr>
				<th <#if !canEdit>width="8%"</#if>>
				<#if canEdit>
					<label class="pos-rel">
		                <input type="checkbox" class="wp" id="selectAllSubject">
		                <span class="lbl"></span>
		             </label>选择
		        <#else>
		        	序号
				</#if>
				</th>
				<th>学科名称</th>
				<th>学科编号</th>
				<#if canEdit>
					<th>操作</th>
				</#if>
			</tr>
		</thead>
		<tbody id='list'>
		<#if subjectList?exists && subjectList?size gt 0>
		<#list subjectList as item>
			<tr>
				<td><#--<input type="checkbox" name="subjectId" value="${item.id!}"/> -->
				<#if canEdit>
				 <label class="pos-rel">
	                    <input name="subjectId" type="checkbox" class="wp" value="${item.id!}">
	                    <span class="lbl"></span>
	              </label>
	            <#else>
	            	${item_index+1}
				</#if>
				</td>
				<td><span>${item.name!}</span></td>
				<td><span>${item.code!}</span></td>
				<#if canEdit>
					<td>
						<a class="table-btn js-subjectEdit" href="javascript:;">编辑</a>
						<a class="table-btn js-subjectDel" href="">删除</a>
					</td>
				</#if>
			</tr>
		</#list>
		</#if>
		</tbody>
	</table-hover>
</table>

<script>
$(function(){
	var delRemark = '确定删除吗？<br>'+
			'<span class="text-danger">随意删除学科可能导致某些课程数据异常</span>';
	//单个删除课程
	$('.js-subjectDel').on('click', function(e){
		e.preventDefault();
		var that = $(this);
		var subjectId = $(this).parents('tr').find('[name="subjectId"]').val();
		
		layer.confirm(delRemark, {
			btn: ['确定', '取消'],
			yes: function(index){
				deleteByIds(new Array(subjectId),that);
				layer.close(index);
			}
		})
	});

	//批量删除课程
	$('.btn-danger').click(function(e){
		e.preventDefault();
		layer.confirm(delRemark, {
			btn: ['确定', '取消'],
			yes: function(index){
				var selEle = $('#list :checkbox:checked');
				if(selEle.length<1){
					layerTipMsg(false,"失败",'请先选中学科再删除');
					layer.close(index);
					return;
				}
				var param = new Array();
				for(var i=0;i<selEle.length;i++){
					param.push(selEle.eq(i).val());
				}
				deleteByIds(param,selEle);
				layer.close(index);
			}
		});
	});
	
	//全选
	$('#selectAllSubject').on('click',function(){
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
	$('.js-addSubject').unbind('click').bind('click', function(e){
		checkPower("");
	});
	$('.js-subjectEdit').unbind('click').bind('click', function(e){
		var subjectId=$(this).parents('tr').find('[name="subjectId"]').val();
		checkPower(subjectId);
	});
	
})

function deleteByIds(idArray,that){
	var url = '${request.contextPath}/basedata/subject/deletes';
	var params = {"ids":idArray};
	$.ajax({
	   type: "POST",
	   url: url,
	   data: params,
	   success: function(msg){
	     if(msg.success){
	     	layer.msg("操作成功", {offset: 't',time: 3000});
		    refreshPage();
	     }else{
	     	layerTipMsg(false,"失败",msg.msg);
	     }
	   },
	   dataType: "JSON"
	});
}

function subjectEdit(subjectId){
	var title="新建学科";
	var url = '${request.contextPath}/basedata/subject/detail';
	if(subjectId!=""){
		title="编辑学科";
		url=url+"?subjectId="+subjectId;
	}
	indexDiv = layerDivUrl(url,{title: title,width:420,height:420});
}
function checkPower(courseTypeId){
	var url = '${request.contextPath}/basedata/subject/checkPower';
	jQuery.post(url,	
		 function(data){
		 	if(data.success){
				//成功
				subjectEdit(courseTypeId);
		 	}else{
		 		layerTipMsg(false,"失败","您没有权限进行此操作");
		 	}
		 }, 
	 "JSON");
}
</script>
