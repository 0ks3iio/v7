<title>活动列表</title>
<#assign hasData = false />
<#if actList?exists && actList?size gt 0>
<#assign hasData = true />
</#if>
<div class="filter">
	<div class="state-default">
		<div class="filter-item">
			<button class="btn btn-blue js-createActivity">创建活动</button>
		</div>
		<#if hasData>
		<div class="filter-item">
			<button class="btn btn-blue js-toManage">批量管理</button>
		</div>
		</#if>
	</div>
	<div class="state-inManage hidden">
		<div class="filter-item">
			<label><input type="checkbox" class="wp" id="allCheck"><span class="lbl"> 全选</span></label>
		</div>
		<div class="filter-item"><button class="btn btn-danger js-delete">删除</button></div>
		<div class="filter-item"><button class="btn btn-blue js-confirm">取消</button></div>
	</div>
</div>
<#if hasData>
<div class="card-list clearfix">
	<#list actList as act>
	<div class="card-item">
		<div class="card-content">
			<label class="card-checkbox">
				<input type="checkbox" class="wp detail-box" value="${act.id!}">
				<span class="lbl"></span>
			</label>
			<div class="card-tools">
				<a href="" data-toggle="dropdown"><i class="fa fa-angle-down"></i></a>
				<div class="dropdown-menu card-tools-menu" cardid="${act.id!}">
					<a class="card-edit" href="javascript:;">编辑</a>
					<a class="card-del" href="javascript:;">删除</a>
				</div>
			</div>
			<a href="javascript:;" class="card-detail" cardid="${act.id!}">
				<div class="card-img" style="text-align:center;">
				<img data-img-action="adapte" src="<#if act.coverAttId?default('') != ''>${request.contextPath}/studevelop/common/attachment/showPic?id=${act.coverAttId!}<#else>${request.contextPath}/static/images/growth-manual/img-default.png</#if>" alt="">
				</div>
				<h4 class="card-name">${act.actTheme!}</h4>
			</a>
		</div>
	</div>
	</#list>
</div>	
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/growth-manual/no-img.png" alt="">
		</span>
		<div class="no-data-body">
			<h3>暂无活动</h3>
			<p class="no-data-txt">请点击左上角的“创建活动”按钮添加</p>
		</div>
	</div>
</div>
</#if>
<div id="actEditDiv" class="layer"></div>
<script src="${request.contextPath}/studevelop/js/img-adapter.js"></script>
<script>
var toDelIds = '';
$(function(){
	// 浏览图片
	layer.photos({
		shade: .6,
		photos:'.js-layer-photos',
		shift: 5
	});
	// 新增
	$('.js-createActivity').on('click',function(){
		var acy = $('#acadyear').val();
		var sem = $('#semester').val();
		var rid = $('#rangeId').val();
		if(rid == ''){
			
		}
		var rtype = $('#rangeType').val();
		var url = "${request.contextPath}/studevelop/activity/${actType!}/edit?"+getIndexParam();
		indexDiv = layerDivUrl(url,{title: "创建活动",width:460,height:490});
	});

	// 编辑
	$('.card-edit').on('click',function(){
		var cid = $(this).parent().attr('cardid');
		var url = "${request.contextPath}/studevelop/activity/${actType!}/edit?id="+cid+"&"+getIndexParam();
		indexDiv = layerDivUrl(url,{title: "修改活动",width:460,height:490});
	});
	
	$("#allCheck").on('click',function () {
        if($(this).is(":checked")){
            $('input.detail-box').each(function () {
                if(!$(this).is(":checked")){

                    $(this).prop("checked",true);
                }
            })
        }else{
            $('.wp.detail-box').each(function () {
                $(this).removeAttr("checked");
            })
        }
    });
	
	// 详情
	$('.card-detail').on('click',function(){
		var cid = $(this).attr('cardid');
		//var url = '${request.contextPath}/webuploader/upload/pic';
		var url = "${request.contextPath}/studevelop/activity/${actType!}/detail?id="+cid;
		$('.model-div').load(url);
	});
	
	// 删除
	$('.card-del').on('click',function(){
		var cid = $(this).parent().attr('cardid');
		toDelIds = cid;
		showConfirmSuccess('确定要删除该活动吗？','确认',deleteAct);
	});
	
	// 批量管理
	$('.js-toManage').on('click',function(){
		$('.state-default').addClass('hidden');
		$('.state-inManage').removeClass('hidden');
		$('.card-list').addClass('in-manage');
	});
	
	// 批量删除
	$('.js-delete').on('click',function(){
		toDelIds = '';
		$('.detail-box').each(function(){
			if($(this).is(":checked")){
				toDelIds+=($(this).val()+',');
			}
		});
		if(toDelIds == ''){
			layerTipMsg(false,"提示","没有选择要删除的活动！");
			return;
		}
		showConfirmSuccess('确定要删除选中的活动吗？','确认',deleteAct);
	});
	
	// 批量删除取消
	$('.js-confirm').on('click',function(){
		$('.state-default').removeClass('hidden');
		$('.state-inManage').addClass('hidden');
		$('.card-list').removeClass('in-manage');
	});
	
});
	// 图片显示适配
    setTimeout(function(){
   		imgAdapter($('img[data-img-action=adapte]')); 
    },500);
function deleteAct(){
	$.ajax({
		url:'${request.contextPath}/studevelop/activity/${actType!}/delAct',
		data: {ids:toDelIds},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
				layer.closeAll();
				layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
				refreshPage();
	 		}
	 		else{
	 			layer.closeAll();
	 			layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

function cancelDel(){
	toDelIds = '';
	layer.closeAll();
}

function refreshPage(){
	changeCls();
}

function getIndexParam(){
	var acy = $('#acadyear').val();
	var sem = $('#semester').val();
	var rid = $('#rangeId').val();
	var rtype = $('#rangeType').val();
	return "acadyear="+acy
				+"&semester="+sem+"&rangeId="+rid+"&rangeType="+rtype;
}
</script>