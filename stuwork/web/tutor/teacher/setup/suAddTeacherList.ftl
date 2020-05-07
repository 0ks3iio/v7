<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="table-container-body">
	<table class="table table-striped">
		<thead>
			<tr>
				<th width="10%"><label><span class="lbl" > 序号</span></label></th>
				<th width="15%">姓名</th>
				<th width="15%">是否添加</th>
				<th width="20%">人员情况</th>
				<th width="40%">所导学生</th>
			</tr>
		</thead>
		<tbody >
		    <#if listTATD?exists&&listTATD?size gt 0>
              <#list listTATD as tatd>
		          <tr>
					<td class="cbx-td"><label><span class="lbl"> ${tatd_index+1+(pagination.pageSize)*(pagination.pageIndex-1)!}</span></label></td>
					<td>${tatd.teacher.teacherName!}</td>
					<td><label><input type="checkbox" <#if tatd.isAdd == "1" >checked="true"</#if> <#if tatd.isFull == "1"> disabled = "true"  </#if> onChange="saveTutor('${tatd.teacher.id!}');" class="wp wp-switch" /><span class="lbl"></span></label></td>
					<td><b class="color-red">${tatd.isChooseNum!}</b>/${tatd.param!}</td>
					<td>${tatd.students!}</td>
					<input type="hidden"  id="status" value="${tatd.isFull!}"/>
					<input type="hidden"  class="teacherId"  <#if tatd.isAdd == "1" >name = "teacherId" </#if> value="${tatd.teacher.id!}"/>
				 </tr> 
		      </#list>
		    </#if>
		</tbody>
	</table>
</div>
<div class="table-container-footer">
	<#--  <div class="pull-left">
		<button class="btn btn-sm btn-white" onclick= "checkAll()">全选</button>
		<button class="btn btn-sm btn-blue" onclick= "addTutor()">确定</button>
	</div> -->
	
	<nav class="nav-page no-margin clearfix">
		<ul class="pagination pagination-sm pull-right">
		  <#if listTATD?exists&&listTATD?size gt 0>
		    <@htmlcom.pageToolBar container=".table-container" class="noprint"/>
		  </#if>
		</ul>
	</nav>
</div>  <!-- table-container-footer -->


<script> 

    $(document).ready(function(){
    $('#status').each(function(){
       if($(this).val()== '1'){
       //disabled = "true"
         $(this).parent("tr").find(".wp").attr("disabled",true);
       }
    });
   });
   
    //添加导师 saveTutor
    function saveTutor(teacherId){
        $.ajax({
	            url:"${request.contextPath}/tutor/teacher/setUp/addTutor?tutorRoundId="+'${tutorRoundId!}'+"&teacherId="+teacherId,
	            clearForm : false,
	            resetForm : false,
	            dataType:'json',
	            contentType: "application/json",
	            type:'post',
	            success:function (data) {
	                if(data.success){
	                
	                }else{
	                    showErrorMsg(data.msg);
	                }
	            }
	      })
    }


</script>