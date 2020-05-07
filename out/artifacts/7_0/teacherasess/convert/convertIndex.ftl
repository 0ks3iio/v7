<div class="">
	<div class="row">
		<div class="col-xs-12">
		   <div class="box box-default">
				<div class="box-body clearfix">
					<div class="filter">
						<div class="filter-item">
							<button class="btn btn-blue" onclick="addConvert()">新增方案</button>
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
							<#if dtolist?exists && dtolist?size gt 0>
							<table class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th width="5%">序号</th>
										<th width="21%">方案名称</th>
										<th width="6%">学年</th>
										<th width="5%">年级</th>
										<th width="20%">所选考试</th>
										<th width="10%">占比</th>
										<th width="10%">创建时间</th>
										<th width="8%">状态</th>
										<th width="15%">操作</th>
									</tr>
								</thead>
								<tbody>
								<#list dtolist as dto>
									<tr>
										<td rowspan="${dto.examNum}">${dto_index+1}</td>
									    <td rowspan="${dto.examNum}">${dto.convertName!}</td>
									    <td rowspan="${dto.examNum}">${dto.acadyear}</td>
									    <td rowspan="${dto.examNum}">${dto.gradeName}</td>
									    <td>${dto.examDtos[0].examName!}</td>
									    <td>${dto.examDtos[0].scale!}%</td>
									    <td rowspan="${dto.examNum}">${dto.creationTime?string('yyyy-MM-dd')}</td>
									    <td rowspan="${dto.examNum}">
									    <#if dto.status == '0'>
									    	<span id="span${dto.convertId!}"><i class="fa fa-circle color-blue font-12"></i> 未统计</span>
									    <#elseif dto.status == '1'>
									    	<span id="span${dto.convertId!}"><i class="fa fa-undo color-lightblue font-12"></i> 统计中</span>
									    <#elseif dto.status == '2'>
									    	<span id="span${dto.convertId!}"><i class="fa fa-circle color-green font-12"></i> 已统计</span>
									    <#elseif dto.status == '3'>
									    	<span id="span${dto.convertId!}"><i class="fa fa-circle color-red font-12"></i> 统计失败</span>
									    </#if>
									    </td>
									    <td rowspan="${dto.examNum}">
									    	<#if dto.status == '0'>
												<a id="stat${dto.convertId!}" class="color-blue mr10" href="javascript:;" onclick="statCon(this,'${dto.convertId}')">统计</a>
										    	<a id="show${dto.convertId!}" class="color-blue disabled mr10" href="javascript:;" onclick="showResult(this,'${dto.convertId}')">查看结果</a>
										    <#elseif dto.status == '1'>
												<a id="stat${dto.convertId!}" class="color-blue mr10 disabled" href="javascript:;" onclick="statCon(this,'${dto.convertId}')">统计</a>
												<a id="show${dto.convertId!}" class="color-blue disabled mr10" href="javascript:;" onclick="showResult(this,'${dto.convertId}')">查看结果</a>
										    <#elseif dto.status == '2'>
										    	<a id="stat${dto.convertId!}" class="color-blue mr10" href="javascript:;" onclick="statCon(this,'${dto.convertId}')">统计</a>
										    	<a id="show${dto.convertId!}" class="color-blue mr10" href="javascript:;" onclick="showResult(this,'${dto.convertId}')">查看结果</a>
										    <#elseif dto.status == '3'>
										    	<a id="stat${dto.convertId!}" class="color-blue mr10" href="javascript:;" onclick="statCon(this,'${dto.convertId}')">统计</a>
										    	<a id="show${dto.convertId!}" class="color-blue disabled mr10" href="javascript:;" onclick="showResult(this,'${dto.convertId}')">查看结果</a>
										    </#if>
										    <a id="del${dto.convertId}" class="color-red delete pos-rel" href="javascript:;" onclick="onDel(this,'${dto.convertId}')">删除
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
									    </td>
									</tr>
									<#list dto.examDtos as examDto>
										<#if examDto_index gt 0>
										<tr>
											<td>${examDto.examName!}</td>
								    		<td>${examDto.scale!}%</td>
										</tr>										
										</#if>
									</#list>
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
	var url =  '${request.contextPath}/teacherasess/convert/index/page?acadyear='+acadyear;
	$('.model-div').load(url);
}
function addConvert(){
	var url =  '${request.contextPath}/teacherasess/convert/addConvert/page';
	$('.model-div').load(url);
}
function showResult(owm,convertId){
	if($(owm).hasClass('disabled')){
		return;
	}
	var url =  '${request.contextPath}/teacherasess/convert/showResult/page?convertId='+convertId;
	$('.model-div').load(url);
}
function statCon(owm,convertId){
	if($(owm).hasClass('disabled')){
		return;
	}
	var spanHtml = '<i class="fa fa-undo color-lightblue font-12"></i> 统计中';
	$("#span"+convertId).html(spanHtml);
	$("#stat"+convertId).addClass("disabled");
	$("#show"+convertId).addClass("disabled");
	statCon1(convertId);
}
function onDel1(owm,convertId){
	$.ajax({
	  	url : "${request.contextPath}/teacherasess/convert/doDel",
	  	data:{convertId:convertId},
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

function statCon1(convertId){
	$.ajax({
	  	url : "${request.contextPath}/teacherasess/convert/stat",
	  	data:{convertId:convertId},
		dataType : 'json',
		success : function(data){
			var obj=data;
 			if(obj.type=="success"){
 				var spanHtml = '<i class="fa fa-circle color-green font-12"></i> 已统计';
 				$("#span"+convertId).html(spanHtml);
 				$("#show"+convertId).removeClass("disabled");
 				$("#stat"+convertId).removeClass("disabled");
 			}else if(obj.type=="error"){
 				var spanHtml = '<i class="fa fa-circle color-red font-12"></i> 统计失败';
 				$("#span"+convertId).html(spanHtml);
 				$("#stat"+convertId).removeClass("disabled");
 			}else{
 				//循环访问结果
 				window.setTimeout("statCon1('"+convertId+"')",5000);
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

				