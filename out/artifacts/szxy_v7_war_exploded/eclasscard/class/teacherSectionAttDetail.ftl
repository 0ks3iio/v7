<a href="javascript:void(0);" onclick="backIndex()" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box-header">
	<h4 class="box-title">上课签到详情</h4>
</div>
<div class="box-body">

	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">节次：</span>
			<div class="filter-content">
				<p>${classAttence.sectionName!}</p>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">迟到：</span>
			<div class="filter-content">
				<p>${classAttence.cdStuNum!}</p>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">缺课：</span>
			<div class="filter-content">
				<p>${classAttence.qkStuNum!}</p>
			</div>
		</div>
	</div>
	<div class="table-container">
		<div class="table-container-header">
			<a class="btn btn-blue" onclick="saveStatus('',4)">正常</a>
			<a class="btn btn-blue" onclick="saveStatus('',2)">迟到</a>
			<a class="btn btn-blue" onclick="saveStatus('',1)">缺课</a>
		</div>
		<div class="table-container-body">
			<table class="table table-striped">
				<thead>
					<tr>
						<th width="80">
							<label><input type="checkbox" id="checkAll" class="wp"><span class="lbl"></span> 序号</label>
						</th>
						<th>教师姓名</th>
						<th>任教科目</th>
						<th>班级</th>
						<th>状态</th>
					</tr>
				</thead>
				<tbody>
				<#if teaclzAttences?exists&&teaclzAttences?size gt 0>
		          	<#list teaclzAttences as item>
					<tr>
					<td><label><input type="checkbox" class="wp checked-input" value="${item.id!}"><span class="lbl"></span> ${item_index+1}</label></td>
						<td>${item.teaRealName!}</td>
						<td>${item.subjectName!}</td>
						<td>${item.className!}</td>
						<td>
						<select name="" id="${item.id!}" class="form-control" onchange="saveStatus('${item.id!}')">
							<option <#if item.status==4> selected</#if>value="4">正常</option>
							<option <#if item.status==2> selected</#if> value="2">迟到</option>
							<option <#if item.status==1> selected</#if> value="1">缺课</option>
						</select>
						</td>
					</tr>
		      	    </#list>
		  	    <#else>
					<tr>
						<td  colspan="88" align="center">
						暂无数据
						</td>
					<tr>
		        </#if>
				</tbody>
			</table>
		</div>
	</div>

</div>
<script type="text/javascript">
$(function(){
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

isSubmit = false;
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
		layerTipMsg(false,"","请选择要更改的教师");
		return;
	}
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/teacher/signin/detail/save",
			data:{"ids":ids,"status":status},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"修改失败",data.msg);
	    			isSubmit = false;
		 		}else{
		 			showDetail("${classAttence.sectionNumber!}")
					layerTipMsg(data.success,"修改成功","");
    			}
			},
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
	$.ajax(options);
}
</script>