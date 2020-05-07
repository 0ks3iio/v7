<div class="table-container-body">
    <table class="table table-bordered table-striped layout-fixed">
		<thead>
			<tr>
			    <#if isEmpower?default(false) && isFirst?default(true)>
				    <th width="8%">
				                      选择
			        </th>
		        </#if> 
				<th>权限名称</th>
				<th>权限类型</th>
				<th>第三方ap的名称</th>
				<th>特征值</th>
				<th>是否启用</th>
				<#if !isEmpower?default(false)>
				     <th>操作</th>
				</#if>
			</tr>
		</thead>
		<tbody id='list'>
		   <#if apPowerDtos?exists && apPowerDtos?size gt 0>
              <#list apPowerDtos as powerDto>
                  <tr>
                    <#if isEmpower?default(false) && isFirst?default(true)>
	                    <td>
						    <label class="pos-rel">
		                    <input name="userId" type="checkbox" class="wp" <#if powerDto.isEmpower?default('false') == 'true'> checked = "true"</#if>
		                    <#if powerDto.description?default('') == '角色授权'>disabled="true"  </#if>     value="${powerDto.sysPower.id!}">
		                    <span class="lbl">${powerDto.description!}</span>
		                    </label>
		                </td>
	                </#if>
					<td>${powerDto.sysPower.powerName!}</td>
					<td><#if powerDto.sysPower.source == 1>默认<#else>第三方ap</#if></td>
					<td>${powerDto.sourceName!}</td>
					<td>${powerDto.sysPower.value!}</td>
					<td><#if powerDto.sysPower.isActive == '0'>不启用<#else>启用</#if></td> 
					<#if !isEmpower?default(false)>                   
						<td>
							<a href="javascript:void(0);" class="js-show-power"     value="${powerDto.sysPower.id!}">查看</a>
						  	<a href="javascript:void(0);" class="js-power-checkIn"  value="${powerDto.sysPower.id!}">编辑</a>
							<a href="javascript:void(0);" class="color-red js-del"  value="${powerDto.sysPower.id!}">删除</a>
						</td>
					</#if>
				  </tr>
              </#list>
           <#else>
               <tr>
					<td  colspan="6" align="center">
					暂无权限记录
					</td>
			   <tr>
           </#if>   
		</tbody>
	</table>
</div>
<#if isEmpower?default(false) && isFirst?default(true)>
	<div class="table-container-footer">
		<div class="">
			<button class="btn btn-sm btn-white" onclick= "checkAll()">全选</button>
			<button class="btn btn-sm btn-blue" onclick= "saveUserPower()">保存</button>
		</div> 
	</div>  <!-- table-container-footer -->
</#if>
<script>
//全选
function checkAll(){
   var total = $('#list :checkbox').length;
	var length = $('#list :checkbox:checked').length;
	if(length != total){
		$('#list :checkbox').prop("checked", "true");
		$(this).prop("checked", "true");
	}else{
		$('#list :checkbox').removeAttr("checked");
		$(this).removeAttr("checked");
	}
}
//批量委派用户权限
function saveUserPower(){
    var selEle = $('#list :checkbox:checked');
	var param = new Array();
	for(var i=0;i<selEle.length;i++){
		param.push(selEle.eq(i).val());
	}
	saveUserByIds(param,selEle);
}

 var isSubmit=false;
function saveUserByIds(idArray,that){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var allIds = new Array();
   <#if apPowerDtos?exists&&apPowerDtos?size gt 0>
		 <#list apPowerDtos as powerDto>
		     allIds.push('${powerDto.sysPower.id!}');    	
		 </#list>
   </#if>     	
	var url = '${request.contextPath}/system/user/power/saveUserPower?targetId='+'${targetId!}'+'&type='+'${type!}';
	var params = {"ids":idArray,
	              "allIds":allIds
	              };
	$.ajax({
		   type: "POST",
		   url: url,
		   data: JSON.stringify(params),
		   contentType: "application/json",
		   dataType: "JSON",
		   success:function (data) {
                isSubmit = false;
                if(data.success){
                    showSuccessMsg(data.msg);
                }else{
                    showErrorMsg(data.msg);
                }
		    }
		});
}  
    
//删除权限
$('.js-del').on('click', function(e){
	        var powerId = $(this).attr("value");
			e.preventDefault();
			var that = $(this);
			var index = layer.confirm("是否删除这条权限？", {
			 btn: ["确定", "取消"]
			}, 
			function(){
				$.ajax({
		            url:"${request.contextPath}/system/ap/power/deletePower?powerId="+powerId,
		            data:{},
		            dataType:'json',
		            contentType:'application/json',
		            type:'GET',
		            success:function (data) {
		                if(data.success){
		                    showSuccessMsgWithCall(data.msg,flushShowPower);
		                }else{
		                    showErrorMsg(data.msg);
		                }
		            }
		        });
			  layer.close(index);
			})
	});
//编辑
$('.js-power-checkIn').on('click', function(e){
	    var powerId = $(this).attr("value");
	    var isSee = "false";
		$('.layer-power-detailedId').load("${request.contextPath}/system/ap/power/register/power/page?powerId="+powerId+"&isSee="+isSee,function(){	
	    layer.open({
				type: 1,
				shade: .5,
				title: '登记权限',
				area: '500px',
				btn: ['确定','取消'],
				scrollbar: false,
				yes:function(index,layero){
				   savePower("${request.contextPath}/system/ap/power/savePower?powerId="+powerId);
	            },
				content: $('.layer-power-detailedId')
	           })
        });
})
//查看
$('.js-show-power').on('click', function(e){
	    var powerId = $(this).attr("value");
	    var isSee = "true";
		$('.layer-power-detailedId').load("${request.contextPath}/system/ap/power/register/power/page?powerId="+powerId+"&isSee="+isSee,function(){			
	    layer.open({
				type: 1,
				shade: .5,
				title: '查看权限',
				area: '500px',
				scrollbar: false,
				yes:function(index,layero){
	            },
				content: $('.layer-power-detailedId')
	           })
        });
})

</script>