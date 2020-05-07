<title>活动图片列表</title>
<meta http-equiv="cache-control" content="no-cache">
<#import "/fw/macro/webUploaderMacro.ftl" as upload />
<#assign hasData = false />
<#assign picSize = maxSize?default(0) />
<#if actDetails?exists && actDetails?size gt 0>
<#assign picSize = picSize-actDetails?size />
<#assign hasData = true />
</#if>
		<div class="filter">
			<div class="state-default">
				<@upload.picUpload businessKey="${id!}" contextPath="${request.contextPath}" resourceUrl="${request.contextPath}/static" size="5" fileNumLimit="${picSize}" handler="savePicToAct">
					<div class="filter-item">
						<button class="btn btn-blue js-addPhotos">上传图片</button>
					</div>
				</@upload.picUpload>
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
		<div class="card-list card-list-sm js-layer-photos clearfix">
			<#if hasData>
			<#list actDetails as ad>
			<div class="card-item">
				<div class="card-content">
					<label class="card-checkbox">
						<input type="checkbox" class="wp detail-box" value="${ad.id!}">
						<span class="lbl"></span>
					</label>
					<div class="card-tools">
						<a href="" data-toggle="dropdown"><i class="fa fa-angle-down"></i></a>
						<div class="dropdown-menu card-tools-menu">
							<a class="detail-del" href="javascript:;" detailid="${ad.id!}">删除</a>
						</div>
					</div>
					<a href="javascript:;">
						<div class="card-img" style="text-align:center;">
							<img  data-img-action="adapte" layer-src="${request.contextPath}/studevelop/common/attachment/showPic?id=${ad.id!}&showOrigin=1" src="${fileUrl!}${ad.filePath!}" alt="">
						</div>
					</a>
				</div>
			</div>
			</#list>
			</#if>
		</div>
<script src="${request.contextPath}/studevelop/js/img-adapter.js"></script>
<script>
// refreshList();
var toDelIds = '';
$(function(){
	// 浏览图片
	layer.photos({
		shade: .6,
		photos:'.js-layer-photos',
		shift: 5
	});
	
	// 批量管理
	$('.js-toManage').on('click',function(){
		$('.state-default').addClass('hidden');
		$('.state-inManage').removeClass('hidden');
		$('.card-list').addClass('in-manage');
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
	
	// 批量删除
	$('.js-delete').on('click',function(){
		$('.detail-box').each(function(){
			if($(this).is(":checked")){
				toDelIds+=($(this).val()+',');
			}
		});
		if(toDelIds == ''){
			layerTipMsg(false,"提示","没有选择要删除的图片！");
			return;
		}	
		
		showConfirmSuccess('确定要删除该图片吗？','确认',deletePic);
	});
	
	$('.js-confirm').on('click',function(){
		$('.state-default').removeClass('hidden');
		$('.state-inManage').addClass('hidden');
		$('.card-list').removeClass('in-manage');
	});
	
	$('.detail-del').on('click',function(){
		var cid = $(this).attr('detailid');
		toDelIds = cid;
		showConfirmSuccess('确定要删除该图片吗？','确认',deletePic);
	});
});
    // 图片显示适配
    setTimeout(function(){
   		imgAdapter($('img[data-img-action=adapte]')); 
    },300);
// 保存文件到附件表
function savePicToAct(){
	if(hasUploadSuc){
		var ii = layer.load();
		$.ajax({
			url:'${request.contextPath}/studevelop/common/attachment/saveFromDir',
			data: {'objId':'${id!}','objType':'${actType}'},
			type:'post',
			success:function(data) {
				layer.close(ii);
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
					layer.closeAll();
		 		}
		 		else{
		 			layer.closeAll();
		 			layerTipMsg(jsonO.success,"提示",jsonO.msg);
				}
				refreshList();
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				layer.close(ii);
				layerTipMsg(false,'提示','保存失败！');
				refreshList();
			}
		});
	}
}

function deletePic(){
	$.ajax({
		url:'${request.contextPath}/studevelop/common/attachment/delete',
		data: {"ids":toDelIds},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
				layer.closeAll();
				layerTipMsg(jsonO.success,"删除成功",jsonO.msg);
				refreshList();
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

function refreshList(){
	<#-- var url = "${request.contextPath}/studevelop/activity/${actType!}/detailList?id=${id!}";
	$('#picList').load(url);-->
	var url = "${request.contextPath}/studevelop/activity/${actType!}/detail?id=${id!}";
	$('.model-div').load(url);
}
</script>