<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<div class="filter">
	<#if infoType?default(0)==0>
	<div class="filter-item filter-item" style="margin-right:10px;">
		<span class="filter-name">科目：</span>
		<div class="filter-content">
			<select name="" id="subId" class="form-control" onChange="searchAudit();" style="width:120px;">
			<option value="">--请选择--</option>
			<#if subList?exists && subList?size gt 0>
			    <#list subList as sub>
				    <option value="${sub.id!}" <#if subId?exists && sub.id == subId>selected="selected"</#if>>${sub.subjectName!}
				    <#if sub.section == 1>
					    (小学)
					    <#elseif sub.section == 0>
					    (学前)
					    <#elseif sub.section == 2>
					    (初中)
					    <#elseif sub.section == 3>
					    (高中)
					    </#if>
				    </option>
				</#list>
		    </#if>
			</select>
		</div>
	</div>
	<#else>
	<input type="hidden" name="" id="subId" value="">
	</#if>
	<div class="filter-item filter-item" style="margin-right:10px;">
		<span class="filter-name">状态：</span>
		<div class="filter-content">
			<select name="" id="status" class="form-control" onChange="searchAudit();" style="width:120px;">
			    <option value="">--请选择--</option>
				<option value="2" <#if status?exists && status == '2'>selected="selected"</#if>>通过</option>
				<option value="-1" <#if status?exists && status == '-1'>selected="selected"</#if>>不通过</option>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item" style="margin-right:10px;">
		<span class="filter-name">单位：</span>
		<div class="filter-content">
			<select name="" id="schId" class="form-control" onChange="searchAudit();" style="width:150px;"> 
			<option value="">--请选择--</option>
			<#if unitList?exists && unitList?size gt 0>
			    <#list unitList as sch>
				<option value="${sch.id!}" <#if schId?exists && sch.id == schId>selected="selected"</#if>>${sch.unitName!}</option>
				</#list>
			</#if>
			</select>
		</div>
	</div>
	<div class="filter-item filter-item">
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <select name="" id="type" class="form-control" style="width:100px;">
		        	<option value="1" <#if type?default("1")=='1'>selected="selected"</#if>>姓名</option>
		        	<option value="2" <#if type?default("1")=='2'>selected="selected"</#if>>身份证号</option>
		        </select>
		        <div class="pos-rel pull-left">
		        	<input type="text" class="typeahead scrollable form-control"  autocomplete="off" data-provide="typeahead" id="searchCon" value="${searchCon!}">
		        </div>
			    
			    <div class="input-group-btn">
			    	<button type="button" class="btn btn-default" onClick="searchAudit();">
				    	<i class="fa fa-search"></i>
				    </button>
			    </div>
		    </div><!-- /input-group -->
		</div>
	</div>
</div>
<div class="filter">
	<div class="filter-item" style="margin-right:10px;">
		<a class="btn btn-blue" href="#" onClick="bacthAudting('2');">审核通过</a>
		<a class="btn btn-white js-nopass" href="#" onClick="bacthNoAudting('-1');">审核不通过</a>
	</div>	
	<#if registerInfoList?exists && registerInfoList?size gt 0>
	<div class="filter-item">
		<div class="filter-content">
			<a class="btn btn-blue" onclick="doExportPass();" >通过名单导出</a>
		</div>	
	</div>
	</#if>
</div>
<#if registerInfoList?exists && registerInfoList?size gt 0>
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
					<th>审核状态</th>
					<th>审核人</th>
					<th>审核时间</th>
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
				    <td>
				    <#if item.status == 2>
				        <span><i class="fa fa-circle color-green font-12"></i> 通过</span>
				    <#elseif item.status == -1>
				        <span><i class="fa fa-circle color-red font-12"></i> 未通过</span>
						<a class="color-blue" data-toggle="tooltip" data-placement="right" title="${item.remark!}" href="javascript:;"><i class="fa fa-commenting-o"></i></a>
				    </#if>
				    </td>
				    <td>${item.userName!}</td>
				    <td>${item.modifyTime?string('yyyy-MM-dd')!}</td>
				</tr>				
				</#list>
			</#if>
			</tbody>
		</table>
		<@htmlcom.pageToolBar container="#haveAudtionDiv" class="noprint">
	    </@htmlcom.pageToolBar>
	</div>
</div>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">没有相关数据</p>
		</div>
	</div>
</div>
</#if>
<!-- 审核不通过 -->
<div class="layer layer-nopass">
	<div class="layer-content">
		<textarea rows="5" class="form-control" id="remark" placeholder="请输入不通过原因" maxLength="100"></textarea>
	</div>
</div>
<script>
function searchAudit(){
   var examId = $('#examId').val();
   var schId = $('#schId').val();
   var subId = $('#subId').val();
   var status = $('#status').val();
   var searchCon = $('#searchCon').val();
   var type = $('#type').val();
   if(type == '1'){
       var url = "${request.contextPath}/teaexam/registerAudit/haveAudtingList?infoType=${infoType?default(0)}&schId="+schId+"&subId="+subId+"&status="+status+"&examId="+examId+"&teacherName="+searchCon+"&type="+type;
       $('#haveAudtionDiv').load(url);
   }else{
       var url = "${request.contextPath}/teaexam/registerAudit/haveAudtingList?infoType=${infoType?default(0)}&schId="+schId+"&subId="+subId+"&status="+status+"&examId="+examId+"&identityCard="+searchCon+"&type="+type;
       $('#haveAudtionDiv').load(url);
   }
}

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
				refreshJumpPage();
	 		}else{
	 			layerTipMsg(jsonO.success,"审核失败",jsonO.msg);
	 			isSubmit = false;
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});	
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

function doExportPass(){
	var examId = $('#examId').val();
   var schId = $('#schId').val();
   var subId = $('#subId').val();
   var url = "${request.contextPath}/teaexam/registerAudit/export?schId="+schId+"&subId="+subId+"&examId="+examId;
   window.open(url);
}
</script>