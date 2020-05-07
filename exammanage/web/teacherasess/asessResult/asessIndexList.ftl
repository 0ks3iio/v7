<div class="">
	<div class="row">
		<div class="col-xs-12">
		   <div class="box box-default">
				<div class="box-body clearfix">
					<div class="filter">
						<div class="filter-item">
							<button class="btn btn-blue" onclick="addAsess()">新增方案</button>
						</div>
						<div class="filter-item filter-item-right">
							<span class="filter-name">学年：</span>
							<div class="filter-content">
								<select class="form-control" id="acadyear" name="acadyear" onChange="changeAcadyear()">
									<#if acadyearList?exists && (acadyearList?size>0)>
					                    <#list acadyearList as item>
						                     <option value="${item!}" <#if acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
					                    </#list>
				                    <#else>
					                    <option value="">未设置</option>
				                     </#if>
								</select>
							</div>
						</div>
					</div>
                    <div class="table-container">
						<div class="table-container-body">
							<#if teacherAsessList?exists&& (teacherAsessList?size > 0)>
							<table class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th width="5%">序号</th>
							            <th width="21%">方案名称</th>
							            <th width="6%">学年</th>
							            <th width="5%">年级</th>
							            <th width="14%">本次考核方案</th>
							            <th width="14%">原始参照方案</th>
							            <th width="8%">创建时间</th>
							            <th width="7%">状态</th>
							            <th class="text-center">操作</th>
									</tr>
								</thead>
								<tbody>
								<#list teacherAsessList as item>
									<tr>
										<td>${item_index+1}</td>
										<td>${item.name!}</td>
										<td>${item.acadyear!}</td>
										
										<td>${item.gradeName!}</td>
										<td>${item.convertName!}</td>
										<td>${item.referConvertName!}</td>
										<td>${(item.creationTime?string('yyyy-MM-dd'))!}</td>
									    <td>
									    <#if item.status == '0'>
									    	<span id="span${item.id!}"><i class="fa fa-circle color-blue font-12"></i> 未对比</span>
									    <#elseif item.status == '1'>
									    	<span id="span${item.id!}"><i class="fa fa-undo color-lightblue font-12"></i> 对比中</span>
									    <#elseif item.status == '2'>
									    	<span id="span${item.id!}"><i class="fa fa-circle color-green font-12"></i> 已对比</span>
									    <#elseif item.status == '3'>
									    	<span id="span${item.id!}"><i class="fa fa-circle color-red font-12"></i> 对比失败</span>
									    </#if>
									    </td>
									    <td>
									    	<#if item.status == '0'>
												<a id="stat${item.id!}" class="color-blue mr10" href="javascript:;" onclick="statCon(this,'${item.id}')">对比</a>
										    	<a id="show${item.id!}" class="color-blue disabled mr10" href="javascript:;" onclick="showResult(this,'${item.id}')">查看结果</a>
										    <#elseif item.status == '1'>
												<a id="stat${item.id!}" class="color-blue mr10 disabled" href="javascript:;" onclick="statCon(this,'${item.id}')">对比</a>
												<a id="show${item.id!}" class="color-blue disabled mr10" href="javascript:;" onclick="showResult(this,'${item.id}')">查看结果</a>
										    <#elseif item.status == '2'>
										    	<a id="stat${item.id!}" class="color-blue mr10" href="javascript:;" onclick="statCon(this,'${item.id}')">对比</a>
										    	<a id="show${item.id!}" class="color-blue mr10" href="javascript:;" onclick="showResult(this,'${item.id}')">查看结果</a>
										    <#elseif item.status == '3'>
										    	<a id="stat${item.id!}" class="color-blue mr10" href="javascript:;" onclick="statCon(this,'${item.id}')">对比</a>
										    	<a id="show${item.id!}" class="color-blue disabled mr10" href="javascript:;" onclick="showResult(this,'${item.id}')">查看结果</a>
										    </#if>
										    <a id="del${item.id}" class="color-red delete pos-rel" href="javascript:;" onclick="onDel(this,'${item.id}')">删除
										    <div class="modify-name-layer">
												<p class="clearfix mt20">
													<span class="fa fa-exclamation-circle color-yellow font-30 pull-left"></span>
													<span class="pull-left mt6">确定要删除吗?</span>
												</p>
												<div class="text-right">
													<button class="btn btn-sm btn-white" onclick="closeDel()" >取消</button>
													<button class="btn btn-sm btn-blue ml10" onclick="subDel()" >确定</button>
												</div>
											</div>
										    </a>
										    <a id="change${item.id}" class="color-blue mr10" href="javascript:;" onclick="doChange(this,'${item.id}')">AB分层</a>
									    </td>
									</tr>
								</#list>
								</tbody>
							</table>
							<#else>
							<div class="no-data-container">
								<div class="no-data">
									<span class="no-data-img">
										<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
									</span>
									<div class="no-data-body">
										<p class="no-data-txt">暂无记录</p>
									</div>
								</div>
							</div>
							</#if>
						</div>
					</div>
				</div>
			</div>
		</div><!-- /.col -->
	</div><!-- /.row -->
</div><!-- /.page-content -->
<script>
function changeAcadyear(){
	 var acadyear = $('#acadyear').val();
	 var str = "?acadyear="+acadyear;
     var url =  '${request.contextPath}/teacherasess/asess/index/page'+str;
	$('.model-div').load(url);
}
function addAsess(){
   	var url = "${request.contextPath}/teacherasess/asess/edit/page";
    indexDiv = layerDivUrl(url,{title: "信息",width:500,height:400});
}
function doChange(owm,obj){
	if($(owm).hasClass('disabled')){
		return;
	}
	var url =  '${request.contextPath}/teacherasess/asessResult/asessResultChange/page?teacherAsessId='+obj+"&type=1";
	$('.model-div').load(url);
}
function showResult(owm,obj){
	if($(owm).hasClass('disabled')){
		return;
	}
	var url =  '${request.contextPath}/teacherasess/asessResult/tab/page?assessId='+obj+"&type=1";
	$('.model-div').load(url);
}
function statCon(owm,obj){
	if($(owm).hasClass('disabled')){
		return;
	}
	var spanHtml = '<i class="fa fa-undo color-lightblue font-12"></i> 对比中';
	$("#span"+obj).html(spanHtml);
	$("#stat"+obj).addClass("disabled");
	$("#show"+obj).addClass("disabled");
	statCon1(obj);
}
function onDel1(owm,obj){
	$.ajax({
	  	url:'${request.contextPath}/teacherasess/asess/delete',
        data: {'id':obj},
		dataType : 'json',
		success : function(data){
			isSaveSubmit = false;
 			if(!data.success){
 				layerTipMsg(data.success,"失败",data.msg);
 				$(owm).removeClass("disabled");
 			}else{
 				layer.msg("操作成功", {
					offset: 't',
					time: 2000
				});
				changeAcadyear();
 			}
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}
var isClose = 0;
function onDel(owm,convertId){
	$(".modify-name-layer").hide();
	if($(owm).hasClass('disabled')){
		return;
	}
	
	if(isClose == 1){
		$(owm).children(".modify-name-layer").hide();
	}else if(isClose == 0){
		$(owm).children(".modify-name-layer").show();
	}else{
		onDel1(owm,convertId);
	}
	isClose = 0;
}

function statCon1(obj){
	$.ajax({
	  	url:'${request.contextPath}/teacherasess/asess/dealData',
	  	data:{"teacherAsessId":obj},
		dataType : 'json',
		success : function(data){
 			if(data.type=="success"){
 				var spanHtml = '<i class="fa fa-circle color-green font-12"></i> 已对比';
 				$("#span"+obj).html(spanHtml);
 				$("#show"+obj).removeClass("disabled");
 				$("#stat"+obj).removeClass("disabled");
 			}else if(data.type=="error"){
 				var spanHtml = '<i class="fa fa-circle color-red font-12"></i> 对比失败';
 				$("#span"+obj).html(spanHtml);
 				$("#stat"+obj).removeClass("disabled");
 			}else{
 				//循环访问结果
 				window.setTimeout("statCon1('"+obj+"')",5000);
 			}	
		},
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	});
}

$(function(){
	<#if ids?exists && ids?size gt 0>
	<#list ids as id>
		statCon1('${id!}');
	</#list>
	</#if>
	$(document).click(function(e){
		if(!$(e.target).hasClass("delete") && !$(e.target).parent().hasClass("delete") && !$(e.target).parents().hasClass("delete")){
			$(".modify-name-layer").hide();//点页面其他地方 隐藏表情区
		}
	});
});
function closeDel(){
	isClose = 1;
}
function subDel(){
	isClose = 2;
}
</script>

				