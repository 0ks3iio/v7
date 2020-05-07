<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="mysavediv">
<#assign shoename ='' />
<#assign buttonName ='' />
<#if (tabType =='1')>
	<#assign shoename ='不排考' />
<#elseif (tabType =='2')>
	<#assign shoename ='不统分' />
</#if>
<#if (arrType =='1')>
	<#assign buttonName ='设置${shoename!}' />
<#else>
	<#assign buttonName ='撤销${shoename!}' />
</#if>

<div class="table-wrapper">
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
				<tr>
					<th>选择</th>
					<th>姓名</th>
					<th>身份证号</th>
					<th>班级</th>
					<th>学号</th>
				</tr>
		</thead>
		<tbody>
			<#if stuDtoList?exists && (stuDtoList?size > 0)>
				<#list stuDtoList as dto>
					<tr>
						<td> <label class="pos-rel">
	                        <input  name="stu-checkbox" type="checkbox" class="wp" value="${dto.student.id!}">
	                        <span class="lbl"></span>
	                    </label></td>
						<td>${dto.student.studentName!}</td>
						<td>${dto.student.identityCard!}</td>
						<td>${dto.className!}</td>
						<td>${dto.student.studentCode!}</td>
					</tr>
				</#list>
		</#if>
		</tbody>
	</table>
</div>
</form>
<#if stuDtoList?exists && (stuDtoList?size > 0)>
 <div class="row">
	<div class="col-xs-12 text-right">
		<a href="javascript:void(0);" class="btn btn-blue" id="testNum-save" onclick="setStu();">${buttonName!}</a>
	</div>
</div>
</#if>	
<script>
var isSubmit=false;
function setStu(){
	if(isSubmit){
		return;
	}
	isSubmit=true;
	//取得选中学生ids
	var num=0;
	var sendIds='';
    $("input[name='stu-checkbox']:checked").each(function(){
        sendIds+=","+$(this).val(); 
        num=num+1;
    });
	if(num==0){
		layer.alert('没有选择数据',{icon:7});
		isSubmit=false;
		return;
	}
	sendIds=sendIds.substring(1,sendIds.length);
	layer.confirm(
		"您确定将选中学生,进行${buttonName!}操作。", 
		{ btn: ['确定','取消'] }, 
		function(){
			layer.closeAll();
 			doById(sendIds);
		}, function(){
			//取消按钮方法
			isSubmit=false;
				layer.closeAll();			
		 });
}

function doById(ids){
	$.ajax({
		url:'${request.contextPath}/scoremanage/filter/filterSet',
		data: {'ids':ids,'examId':'${examId!}','arrType':'${arrType!}','tabType':'${tabType!}'},
		type:'post',
		beforeSend:function(XMLHttpRequest){
			
		},  
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(!jsonO.success){
    			layerTipMsg(data.success,"失败",jsonO.msg);
    			isSubmit=false;
    			searchList();
	 		}
	 		else{
	 			// 显示成功信息
    			layerTipMsg(jsonO.success,"成功",jsonO.msg);	 
    			searchList();			
			}
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
 			var text = syncText(XMLHttpRequest);
 			swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
		}
	});
}
</script>