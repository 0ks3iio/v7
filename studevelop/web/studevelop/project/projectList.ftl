<div class="table-wrapper">
    <form id="mannReForm">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th class="t-center"><label><input type="checkbox" class="wp" id="checkAll"><span class="lbl"> 全选</span></label></th>
					<th class="t-center">项目</th>
					<th class="t-center">显示对象</th>
					<th class="t-center">操作</th>
				</tr>
		</thead>
		<tbody>
			<#if stuDevelopProjectList?exists && (stuDevelopProjectList?size > 0)>
				<#list stuDevelopProjectList as item>
				   <tr>
				      <input type="hidden" class="tid" value="${item.id!}">
				      <td class="t-center">
				          <label><input type="checkbox" class="wp checked-input" value="${item.id!}"><span class="lbl"></span></label>
				      </td>
				      <td class="t-center" style="word-break:break-all;" width="40%">${item.projectName!}</td>
				      <td class="t-center">
				          <#if item.state == '1'>
				                                 仅对学科类别显示
				          <#else>
				                                仅对学科显示
				          </#if>
				      </td>
				      <td class="t-center">
				         <a href="javascript:" class="color-lightblue js-edits">修改</a>
      	  	  	 		 <a href="javascript:" class="color-blue" onClick="doProjectDelete('${item.id!}');">删除</a>
				      </td>
				   </tr>
                </#list>
            </#if>
		</tbody>
	</table>
	<#if stuDevelopProjectList?exists && (stuDevelopProjectList?size > 0)>
	<div class="text-left" style="margin-top:10px;">
		<button class="btn btn-blue" onclick="doProjectDelete('');">删除</button>
	</div>
	</#if>
	</form>	
</div>
<script>
$(function(){
	$('.js-edits').on('click',function(e){
    	e.preventDefault();
    	var acadyear = $('#acadyear').val();
    	var semester = $('#semester').val();
    	var gradeId = $('#gradeId').val();
    	var that = $(this);
    	var id = that.closest('tr').find('.tid').val();
    	if(id != undefined){
    	    var url =  "${request.contextPath}/studevelop/project/edit?id="+id;
    	}else{
    	    var url =  "${request.contextPath}/studevelop/project/edit?acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
    	}
		$("#projectEdit").load(url,function() {
			layerShow();
		});
    });
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

var isSubmit=false;
function doProjectDelete(id){
     showConfirmMsg('确认删除？','提示',function(){
     var projectIds = "";
     if(id != ''){
        projectIds = id;
     }else{
        $(".checked-input").each(function(){
  		   if($(this).is(':checked')){
  			   if(projectIds==''){
  				  projectIds = $(this).val();
  			   }else{
  				  projectIds+=','+$(this).val();
  			   }
  		    }
	    });
     }
     if(projectIds==""){
		layerTipMsg(false,"","请选择要删除的项目！");
		return;
	 }
     var ii = layer.load();
     $.ajax({
			url:'${request.contextPath}/studevelop/project/delete',
			data: {'projectIds':projectIds},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
                    layer.closeAll();
					layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
				  	searchList();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
		 			isSubmit=false;
				}
				layer.close(ii);
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
		});
}
</script>