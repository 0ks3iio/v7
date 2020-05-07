<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div id="a1" class="tab-pane active">
	<div class="filter">
		<div class="filter-item">
			<a class="btn btn-blue" href="#" onClick="bacthAudting('2');">审核通过</a>
			<a class="btn btn-white js-nopass" href="#" onClick="bacthNoAudting('-1');">审核不通过</a>
		</div>
		<div class="filter-item filter-item-right">
			<span class="filter-name">考试名称：</span>
			<div class="filter-content">
				<select name="" id="examId" class="form-control" style="width:400px;" onchange="searchAudit();">
				<#if teaexamInfoList?exists && teaexamInfoList?size gt 0>
				    <#list teaexamInfoList as item>
					    <option value="${item.id!}" <#if '${examId!}' == '${item.id!}'>selected="selected"</#if>>
					    ${item.examName!}
					    </option>
					</#list>
			    <#else>
			        <option value="">--请选择--</option>
				</#if>
				</select>
			</div>
		</div>
		<div class="filter-item filter-item-right">
			<span class="filter-name">类型：</span>
			<div class="filter-content">
				<select name="infoType" id="infoType" class="form-control" onchange="searchExam()">
                    <option value="0" <#if '${infoType!}'=='0'>selected="selected"</#if>>考试</option>	
                    <option value="1" <#if '${infoType!}'=='1'>selected="selected"</#if>>培训</option>			                       
                </select>
			</div>
		</div>
	</div>
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
						<th>教师姓名</th>
						<th>性别</th>
						<th>民族</th>
						<th>身份证号</th>
						<th>单位</th>
						<th>照片</th>
						<#if infoType?default(0)==0>
						<th>报名科目</th>
						</#if>
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
					    <td>${item.nation!}</td>
					    <td>${item.identityCard!}</td>
					    <td>${item.unitName!}</td>
					    <td>
					    	<div class="photo pos-rel" value="${item.teacherName!}" name="${item.sex!}" id="${item.avatarUrl!}">
					    		<a class="color-blue" href="#">${item.teacherName!}.img</a>
					    	</div>
					    </td>
					    <#if infoType?default(0)==0>
					    <td>${item.subName!}</td>
					    </#if>
					    <td>${item.creationTime?string('yyyy-MM-dd')!}</td>
					    <td><a class="color-blue mr10" href="#" onClick="doAudting('${item.id!}','2')">通过</a><a class="color-blue" href="#" onClick="audting('${item.id!}','-1')">不通过</a></td>
					</tr>
					</#list>
				<#else>
					<tr>
						<td colspan="<#if infoType?default(0)==0>10<#else>9</#if>" align="center">暂无数据</td>
					</tr>
				</#if>
				</tbody>
			</table>
			<@htmlcom.pageToolBar container="#auditTabDiv" class="noprint">
	    	</@htmlcom.pageToolBar>
		</div>
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
	$(".photo").each(function(){
	    var name = $(this).attr("value");
	    var sex = $(this).attr("name");
	    var src = $(this).attr("id");
		var modifyNameLayer = '<div class="modify-name-layer">'
								   +'<div class="mt15">'
								       +'<span class="mr20">姓名:'+name+'</span><span>性别:'+sex+'</span>'
								   +'</div>'
								   +'<div class="text-center mt15">'
								      +'<img width="71" height="99" src="'+src+'">'
								   +'</div>'
							   +'</div>';
		$(this).append(modifyNameLayer);
	});
	
	$(".photo").mouseover(function(){
		$(this).children(".modify-name-layer").show();
	});
	
	$(".photo").mouseout(function(){
		$(this).children(".modify-name-layer").hide();
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
	if(GetLength(remark)>100 && status=='-1'){
		layerTipMsgWarn("提示","最多100个字符！");
		return;
	}
    var index = layer.confirm("确定审核吗？", {
		btn: ["确定", "取消"]
		}, function(){
		   $.ajax({
		        url:"${request.contextPath}/teaexam/registerAudit/doAuditing",
		       data:{id:id,status:status,remark:remark},
		        dataType : 'json',
		       success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"审核失败！",data.msg);
		 		      return;
		 	      }else{
		 		      layer.closeAll();
				      layerTipMsg(data.success,"审核成功！",data.msg);
                      searchAudit();
    		      }
		        }
	        });
	})
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
	if(GetLength(remark)>100 && status=='-1'){
	    layerTipMsgWarn("提示","最多100个字符！");
		return;
	}
	isSubmit = true;
	$.ajax({
		url:'${request.contextPath}/teaexam/registerAudit/bacthAudting',
		data:{'ids':ids,'status':status,'remark':remark},
		type:"post",
		success:function(data){
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 		    layer.closeAll();
				layerTipMsg(jsonO.success,"审核成功",jsonO.msg);
	 			searchAudit();
	 		}else{
	 			layerTipMsg(jsonO.success,"审核失败",jsonO.msg);
	 			isSubmit = false;
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});	
}

function searchAudit(){
	var infoType = $('#infoType').val();
   var examId = $('#examId').val();
   var url = "${request.contextPath}/teaexam/registerAudit/auditingList?examId="+examId+"&infoType="+infoType;
   $('#auditTabDiv').load(url);
}

function searchExam(){
	var infoType = $('#infoType').val();
   var url = "${request.contextPath}/teaexam/registerAudit/auditingList?infoType="+infoType;
   $('#auditTabDiv').load(url);
}

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