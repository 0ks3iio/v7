<#macro media_div dataUrl="" id="" typeId="" toObjectId="">
<#nested>
<div class="layer layer-choose-media">
<div class="clearfix">
	<a id="jpgtab" class="fake-btn fake-btn-blue no-radius-right float-left js-jpg" style="text-decoration:none;">相册集</a>
	<a id="movtab" class="fake-btn fake-btn-default no-radius-right no-radius-left float-left js-mov" style="text-decoration:none;">视频集</a>
	<a id="ppttab" class="fake-btn fake-btn-default no-radius-left float-left js-ppt" style="text-decoration:none;">PPT</a>
</div>
<div class="layer-content"><br />
	<div class="card-list card-list-large">
		<div id="jpgGroupDiv" class="jpg-group card-choose clearfix"></div>
		<div id="movGroupDiv" class="mov-group card-choose clearfix none"></div>
		<div id="pptGroupDiv" class="ppt-group card-choose clearfix none"></div>
	</div>
</div>
</div>
<script>
$(document).ready(function(){
	//添加媒体文件
	$('.js-choose-media').click(function(){
		initMediaData();
		index=layer.open({
			type: 1,
			shade: .6,
			title:'选择媒体文件',
			btn :['确定','取消'],
			area: ['800px','60%'],
			content: $('.layer-choose-media')
		});
		$('.layui-layer-content').css({
			overflow: 'auto'
		});
		$('.layui-layer-btn0').click(function(){
			var thisDataId = $('.layer-choose-media .card-item.bg-choose').data("id");
			var thisDataType = $('.layer-choose-media .card-item.bg-choose').data("type");
			$("#${id}").val(thisDataId);
			$("#${typeId}").val(thisDataType);
			$('.media-place').empty().append($('.layer-choose-media .card-item.bg-choose .card-content').clone());
		})
	});
	$('.layer-choose-media .fake-btn').click(function(){
		$(this).removeClass('fake-btn-default').addClass('fake-btn-blue').siblings('.fake-btn').removeClass('fake-btn-blue').addClass('fake-btn-default');
		$('.layer-choose-media .card-list>.clearfix').eq($(this).index()).removeClass('none').siblings().addClass('none')
	});
	$('.layer-choose-media .card-list').on('click','.card-item', function(){
		$('.layer-choose-media .card-list .card-item').removeClass('bg-choose');
		$(this).addClass('bg-choose');
	});
	var typeId = $("#${typeId}").val();
	if(typeId=="04"){
		$("#movtab").click();
	}else if(typeId=="05"){
		$("#ppttab").click();
	}
})

function initMediaData(){
	var dataId =$("#${id}").val();
	var toObjectId = '';
	<#if toObjectId != ''>
		toObjectId=$("#${toObjectId}").val();
	</#if>
	$.ajax({
		url:"${dataUrl}",
		type:'post',
		data:{"dataId":dataId,"toObjectId":toObjectId},
		success:function(data) {
			var resultarrary = JSON.parse(data);
			initMediaDiv(resultarrary);
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		}
	});
}

function initMediaDiv(mediaArr){
	var jpgHtml = '<div class="wrap-1of1 centered no-data-state">';
		jpgHtml+= '	<div class="text-center">';
		jpgHtml+= '		<img src="${request.contextPath}/static/images/classCard/no-data-common.png" width="120" height="120"/>';
		jpgHtml+= '		<p>您还没有上传相应的相册内容哦，<br/>请前往<a href="javascript:;" onclick="gotoMedieSet()">“多媒体”</a>中进行相应的设置</p>';
		jpgHtml+= '	</div>';
		jpgHtml+= '</div>';
	var movHtml = '<div class="wrap-1of1 centered no-data-state">';
		movHtml+= '	<div class="text-center">';
		movHtml+= '		<img src="${request.contextPath}/static/images/classCard/no-data-common.png" width="120" height="120"/>';
		movHtml+= '		<p>您还没有上传相应的视频内容哦，<br/>请前往<a href="javascript:;" onclick="gotoMedieSet()">“多媒体”</a>中进行相应的设置</p>';
		movHtml+= '	</div>';
		movHtml+= '</div>';
	var pptHtml = '<div class="wrap-1of1 centered no-data-state">';
		pptHtml+= '	<div class="text-center">';
		pptHtml+= '		<img src="${request.contextPath}/static/images/classCard/no-data-common.png" width="120" height="120"/>';
		pptHtml+= '		<p>您还没有上传相应的PPT内容哦，<br/>请前往<a href="javascript:;" onclick="gotoMedieSet()">“多媒体”</a>中进行相应的设置</p>';
		pptHtml+= '	</div>';
		pptHtml+= '</div>';
	var jpdnum = 0;
	var movnum = 0;
	var pptnum = 0;
	for (var i in mediaArr) {
		var choiseClass = '';
		if(mediaArr[i][3] == '1'){
			choiseClass = 'bg-choose';
		}	
		if(mediaArr[i][0] == '1'){
			if(jpdnum==0)jpgHtml = '';
			jpgHtml += '<div class="card-item '+choiseClass+'"  data-id="' + mediaArr[i][1] + '" data-type="03">';
			jpgHtml += '		<div class="card-content">';
			jpgHtml += '			<a  href="javascript:;" >';
			jpgHtml += '				<div class="card-img  ratio-16of9">';
			jpgHtml += '				<img src="${request.contextPath}/static/images/growth-manual/photos.png" alt="">';
			jpgHtml += '				</div>';
			jpgHtml += '			</a>';
			jpgHtml += '			<h4 class="card-name card-name-edit">';
			jpgHtml += '				<p>'+mediaArr[i][2]+'</p>';
			jpgHtml += '				<img src="${request.contextPath}/static/images/growth-manual/jpg-icon.png" alt="">';
			jpgHtml += '			</h4>';
			jpgHtml += '		</div>';
			jpgHtml += '	</div>';
			jpdnum++;
		}else if(mediaArr[i][0] == '2'){
			if(movnum==0)movHtml = '';
			movHtml += '<div class="card-item '+choiseClass+'" data-id="' + mediaArr[i][1] + '" data-type="04">';
			movHtml += '		<div class="card-content">';
			movHtml += '			<a  href="javascript:;" >';
			movHtml += '				<div class="card-img  ratio-16of9">';
			movHtml += '				<img src="${request.contextPath}/static/images/growth-manual/videos.png" alt="">';
			movHtml += '				</div>';
			movHtml += '			</a>';
			movHtml += '			<h4 class="card-name card-name-edit">';
			movHtml += '				<p>'+mediaArr[i][2]+'</p>';
			movHtml += '				<img src="${request.contextPath}/static/images/growth-manual/mov-icon.png" alt="">';
			movHtml += '			</h4>';
			movHtml += '		</div>';
			movHtml += '	</div>';
			movnum++;
		}else if(mediaArr[i][0] == '3'){
			if(pptnum==0)pptHtml = '';
			pptHtml += '<div class="card-item '+choiseClass+'"  data-id="' + mediaArr[i][1] + '" data-type="05">';
			pptHtml += '		<div class="card-content">';
			pptHtml += '			<a  href="javascript:;" >';
			pptHtml += '				<div class="card-img ratio-16of9">';
			pptHtml += '				<img src="${request.contextPath}'+mediaArr[i][4]+'" alt="">';
			pptHtml += '				</div>';
			pptHtml += '			</a>';
			pptHtml += '			<h4 class="card-name card-name-edit">';
			pptHtml += '				<p>'+mediaArr[i][2]+'</p>';
			pptHtml += '				<img src="${request.contextPath}/static/images/growth-manual/ppt-icon.png" alt="">';
			pptHtml += '			</h4>';
			pptHtml += '		</div>';
			pptHtml += '	</div>';
			pptnum++;
		}
	}
	$("#jpgGroupDiv").html(jpgHtml);
	$("#movGroupDiv").html(movHtml);
	$("#pptGroupDiv").html(pptHtml);
}

</script>
</#macro>