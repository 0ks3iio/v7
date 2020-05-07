
	<div class="table-container">
		<div class="table-container-body">
			<table class="table table-bordered table-striped table-hover">
				<thead>
					<tr>
						<th width="30">
							<label class="inline">
								<input class="wp" type="checkbox" id="checkAll">
								<span class="lbl"></span>
							</label>
						</th>
						<th>干部姓名</th>
						<th>性别</th>
						<th>部门</th>
						<th>结亲对象村</th>
						<th>报名批次</th>
						<th>结亲村</th>
						<th>报名时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<#if registerInfoList?exists && registerInfoList?size gt 0>
				    <#list registerInfoList as item>
					<tr>
					    <td class="cbx-td">
					    	<label class="inline">
								<input class="wp checked-input" type="checkbox" value="${item.id!}">
								<span class="lbl"></span>
							</label>
					    </td>
					    <td>${item.teacherName!}</td>
					    <td>${item.sex!}</td>
					    <td>${item.deptName!}</td>
					    <td>${item.contry!}</td>
					    <td>${item.batchType!}</td>
					    <td title=${item.contrys!}>${item.contrysSub!}</td>
					    <td>${item.applyTime?string('yyyy-MM-dd')!}</td>
					    <td><a class="color-blue mr10" href="#" onClick="doAudting('${item.id!}','2')">通过</a><a class="color-blue" href="#" onClick="audting('${item.id!}','-1')">不通过</a><a class="table-btn color-red" style="cursor: pointer"   onclick="del('${item.id}')">  删除 </a></td>
					</tr>
					</#list>
				<#else>
					<tr>
						<td colspan="9" align="center">暂无数据</td>
					</tr>
				</#if>
				</tbody>
			</table>
		</div>
	</div>
<!-- 审核不通过 -->
<div class="layer layer-nopass">
	<div class="layer-content">
		<textarea rows="5" class="form-control" id="remark" placeholder="请输入不通过原因" maxLength="100"></textarea>
	</div>
</div>
<script>
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
})
function audting(id,status){
	layer.open({
		type: 1,
		shadow: 0.5,
		title: '审核不通过',
		area: '500px',
		btn: ['确定', '取消'],
		yes: function(index){
			doAudting(id,status);
		},
		content: $('.layer-nopass')
	});
}

function GetLength(str) {
    var realLength = 0;
    for (var i = 0; i < str.length; i++) 
    {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) 
		realLength += 1;
        else 
		realLength += 2;
    }
    return realLength;
}


function doAudting(id,status){
    var remark = $('#remark').val();
    if(remark=="" && status=='-1'){
		layerTipMsgWarn("提示","请输入不通过原因！");
		return;
	}
	if(GetLength(remark)>200 && status=='-1'){
		layerTipMsgWarn("提示","最多200个字符！");
		return;
	}
    var index = layer.confirm("确定审核吗？", {
		btn: ["确定", "取消"]
		}, function(){
		   $.ajax({
		        url:"${request.contextPath}/familydear/registerAudit/doAuditing",
		       data:{id:id,status:status,remark:remark},
		        dataType : 'json',
		       success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"审核失败！",data.msg);
		 		      return;
		 	      }else{
		 		      layer.closeAll();
				      layerTipMsg(data.success,"审核成功！",data.msg);
                       searchList1();
    		      }
		        }
	        });
	})
}

function del(id) {
    showConfirmMsg('确认删除？','提示',function(){
        var ii = layer.load();
        $.ajax({
            url: '${request.contextPath}/familydear/registerAudit/auditingDel?',
            data: {'id':id},
            type:'post',
            success:function(data) {
                layer.closeAll();
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    searchList1();
                }else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    });

}

var isSubmit=false;
function bacthAudting(status){
	if(isSubmit){
		return;
	}
	var ids="";
	var remark = $('#remark').val();
	$(".checked-input").each(function(){
  		if($(this).is(':checked')){
  			if(ids==''){
  				ids = $(this).val();
  			}else{
  				ids+=','+$(this).val();
  			}
  		}
	});
	if(ids==""){
		layerTipMsgWarn("提示","请选择要审核的报名记录！");
		return;
	}
    if(remark=="" && status=='-1'){
		layerTipMsgWarn("提示","请输入不通过原因！");
		return;
	}
	if(GetLength(remark)>200 && status=='-1'){
	    layerTipMsgWarn("提示","最多200个字符！");
		return;
	}
	isSubmit = true;
	$.ajax({
		url:'${request.contextPath}/familydear/registerAudit/bacthAudting',
		data:{'ids':ids,'status':status,'remark':remark},
		type:"post",
		success:function(data){
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 		    layer.closeAll();
				layerTipMsg(jsonO.success,"审核成功",jsonO.msg);
                searchList1();
	 		}else{
	 			layerTipMsg(jsonO.success,"审核失败",jsonO.msg);
	 			isSubmit = false;
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});	
}

<#--function searchAudit(){-->
    <#--var options=$("#contryName option:selected");-->
    <#--var contryName = "";-->
    <#--if(options.val()) {-->
        <#--contryName = options.text();-->
    <#--}-->
   <#--var activityId = $('#activityId').val();-->
   <#--var url = "${request.contextPath}/familydear/registerAudit/auditingList?activityId="+activityId+"&contryName="+encodeURIComponent(contryName);-->
   <#--$('#auditTabDiv').load(url);-->
<#--}-->

function bacthNoAudting(status){
    var ids="";
    $(".checked-input").each(function(){
  		if($(this).is(':checked')){
  			if(ids==''){
  				ids = $(this).val();
  			}else{
  				ids+=','+$(this).val();
  			}
  		}
	});
	if(ids==""){
		layerTipMsgWarn("提示","请选择要审核的报名记录！");
		return;
	}
    layer.open({
		type: 1,
		shadow: 0.5,
		title: '审核不通过',
		area: '500px',
		btn: ['确定', '取消'],
		yes: function(index){
			bacthAudting(status);
		},
		content: $('.layer-nopass')
	});
}
</script>