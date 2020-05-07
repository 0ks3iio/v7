function load_media_select_one(obj, toObjectId, dataId,handler) {
	$.ajax({
		url : _contextPath + '/mobile/open/eclasscard/media/group/select',
		data : {
			'userId' : userId,
			'unitId' : unitId,
			'dataId' : dataId,
			'toObjectId' : toObjectId
		},
		type : 'post',
		success : function(data) {
			var result = JSON.parse(data);
			show_media_select(obj,result,handler);
			obj.on('click','.box-media', function(){
				var html = this.innerHTML;
				eval(handler)(html);
			});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

function load_info_select_mult(obj, dataIds,handler) {
	$.ajax({
		url : _contextPath + '/mobile/open/eclasscard/info/group/select',
		data : {
			'unitId' : unitId,
			'dataIds' : dataIds,
		},
		type : 'post',
		success : function(data) {
			var result = JSON.parse(data);
			show_ecc_info_select(obj,result);
			obj.on('click','.select-info-btn', function(){
				var num = $(this).prev().text();
				var ids = '';
				var index = 1;
				$('.tab-content-list .mui-table-view-cell input:checked').each(function(){
					ids+=$(this).val();
					if(num!=index){
						ids+=',';
					}
					index++;
			    });
				eval(handler)(num,ids);
			});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}

function show_media_select(obj,mediaArr,handler) {
	var html_media = '<div class="wrap-full"><div class="choose-media-wrap"><div class="mui-slider"><div class="mui-slider-indicator mui-segmented-control mui-segmented-control-inverted bgwhite"><a class="mui-control-item mui-active" href="#media-select-item1">相册</a><a class="mui-control-item" href="#media-select-item2">视频集</a><a class="mui-control-item" href="#media-select-item3">PPT</a></div><div class="mui-slider-group">';
	var html_photos = '';
	var html_vedios = '';
	var html_ppt = '';
	html_photos+='<div id="media-select-item1" class="mui-control-content  mui-active"><div class="box-media-wrap clearfix">'
	html_vedios+='<div id="media-select-item2" class="mui-control-content "><div class="box-media-wrap clearfix">'
	html_ppt+='<div id="media-select-item3" class="mui-control-content "><div class="box-media-wrap clearfix">'
	for ( var i in mediaArr) {
		if (mediaArr[i][0] == '1') {
			html_photos+='<div class="box-media" ><input type="hidden" class="media-object-id" value="'+mediaArr[i][1]+'"><input type="hidden" class="media-object-type" value="03"><div class="box-media-img clearfix bg-green"><img src='+_contextPath;
			html_photos+='"/static/eclasscard/mobileh5/multimediaPublishing/images/photos.png" ><div class="mui-input-row mui-radio mui-right"><label></label>';
			html_photos+='       <input name="radio" type="radio"></div></div>';
			html_photos+='<div class="box-media-name">'+mediaArr[i][2]+'</div></div>';
		} else if (mediaArr[i][0] == '2') {
			html_vedios+='<div class="box-media"><input type="hidden" class="media-object-id" value="'+mediaArr[i][1]+'"><input type="hidden" class="media-object-type" value="04"><div class="box-media-img clearfix bg-blue"><img src='+_contextPath;
			html_vedios+='"/static/eclasscard/mobileh5/multimediaPublishing/images/videos.png" ><div class="mui-radio"><label></label>';
			html_vedios+='<input name="radio" type="radio"></div></div>';
			html_vedios+='<div class="box-media-name">'+mediaArr[i][2]+'</div></div>';
		} else if (mediaArr[i][0] == '3') {
			html_ppt+='<div class="box-media"><input type="hidden" class="media-object-id" value="'+mediaArr[i][1]+'"><input type="hidden" class="media-object-type" value="05"><div class="box-media-img clearfix bg-orange"><img src='+_contextPath;
			html_ppt+='"/static/eclasscard/mobileh5/multimediaPublishing/images/PPT.png" ><div class="mui-radio"><label></label>';
			html_ppt+='       <input name="radio" type="radio"></div></div>';
			html_ppt+='<div class="box-media-name">'+mediaArr[i][2]+'</div></div>';
		}
	}
	html_photos+='</div></div>';
	html_vedios+='</div></div>';
	html_ppt+='</div></div>';
	html_media+=html_photos+html_vedios+html_ppt;
	obj.html(html_media);
}

function show_ecc_info_select(obj,resultarrary) {
	var headarrary = JSON.parse(resultarrary[0]);
	var dataarrary = JSON.parse(resultarrary[1]);
	var xzharrary = JSON.parse(resultarrary[2]);
	var fxzharrary = JSON.parse(resultarrary[3]);
	var html_info = '<div class="top-tab px10"><div id="ecc-info-scroll" class="mui-scroll-wrapper mui-slider-indicator mui-segmented-control mui-segmented-control-inverted"><div class="mui-scroll">';
	var html_body = '<div class="tab-content-list">';
	for (var i in headarrary) {
		var activeClass = '';
		if(i==0){
			activeClass = 'mui-active';
			html_info+='<a class="mui-control-item mui-active" href="#info-'+headarrary[i][0]+'">'+headarrary[i][1]+'</a>';
		}else{
			html_info+='<a class="mui-control-item" href="#info-'+headarrary[i][0]+'">'+headarrary[i][1]+'</a>';
		}
		html_body+=init_info_body(headarrary[i][0],headarrary[i][5],dataarrary,xzharrary,fxzharrary,activeClass);
	}
	html_body+='</div>';
    html_info+='</div></div></div>'+html_body;
    html_info+='<div class="bottom-sure">已选<span class="choose-num c-1687F0">0</span>个<button type="button" class="mui-btn mui-btn-blue pos-right-btn select-info-btn">确定</button></div>';
    
    obj.html(html_info);
    var scroll=mui('#ecc-info-scroll').scroll();//初始化
	scroll.refresh();//高度变化后，刷新滚动区域高度
	$('.check-all-wrap').on('change','input',function(){
        if ($(this).prop('checked')){
            $(this).closest('.check-all-wrap').next('ul').find('input').prop('checked',true)
        } else{
            $(this).closest('.check-all-wrap').next('ul').find('input').prop('checked',false)
        }
        
        $('.choose-num').text($('.tab-content-list .mui-table-view-cell input:checked').length)
    });
    
    $('.tab-content-list').on('click','.mui-table-view-cell',function(){
        $('.choose-num').text($('.tab-content-list .mui-table-view-cell input:checked').length);
    });
}

function init_info_body(bodyId,group,dataarrary,xzharrary,fxzharrary,activeClass) {
	var html_body = '';
	if(group=='1'){
		html_body += '<div class="mui-control-content '+activeClass+'" id="info-'+bodyId+'">';
		for (var i in xzharrary) {
			html_body += '<div class="check-all-wrap"><span class="c-777">'+xzharrary[i][1]+'</span><div class="mui-checkbox"><input  name="checkbox" type="checkbox">全选</div></div><ul id="ecc-'+xzharrary[i][0]+'" class="mui-table-view release-object">';
			for (var j in dataarrary) {
				if(dataarrary[j][5] == bodyId &&xzharrary[i][0] == dataarrary[j][6]){
					html_body += '<li class="mui-table-view-cell mui-checkbox mui-right">'+dataarrary[i][2]+'<span class="c-999"> ('+dataarrary[i][1]+')</span><input name="checkbox"';
					if(dataarrary[j][4] == '1'){
						html_body += ' checked ';
					}
					html_body += 'type="checkbox" class="select-info-one" value="'+dataarrary[j][0]+'"> </li>';
				}
			}
			html_body += '</ul>';
		}
		html_body+='</div>';
	}else if(group=='2'){
		html_body += '<div class="mui-control-content '+activeClass+'" id="info-'+bodyId+'">';
		for (var i in fxzharrary) {
			html_body += '<div class="check-all-wrap"><span class="c-777">'+fxzharrary[i][1]+'</span><div class="mui-checkbox"><input name="checkbox" type="checkbox">全选</div></div><ul  id="ecc-'+fxzharrary[i][0]+'" class="mui-table-view release-object">';
			for (var j in dataarrary) {
				if(dataarrary[j][5] == bodyId &&fxzharrary[i][0] == dataarrary[j][6]){
					html_body += '<li class="mui-table-view-cell mui-checkbox mui-right">'+dataarrary[i][2]+'<span class="c-999"> ('+dataarrary[i][1]+')</span><input name="checkbox" type="checkbox"';
					if(dataarrary[j][4] == '1'){
						html_body += ' checked ';
					}
					html_body += ' class="select-info-one" value="'+dataarrary[j][0]+'"></li>';
				}
			}
			html_body += '</ul>';
		}
		html_body+='</div>';
	}else{
		html_body += '<div class="mui-control-content '+activeClass+'" id="info-'+bodyId+'"><div class="check-all-wrap"><div class="mui-checkbox"><input name="checkbox" type="checkbox">全选</div></div><ul id="ecc-'+bodyId+'" class="mui-table-view release-object">';
		if(dataarrary.length > 0){
			for (var i in dataarrary) {
				if(dataarrary[i][5] == bodyId){
					html_body += '<li class="mui-table-view-cell mui-checkbox mui-right">'+dataarrary[i][2]+'<span class="c-999"> ('+dataarrary[i][1]+')</span><input name="checkbox"';
					if(dataarrary[i][4] == '1'){
						html_body += ' checked ';
					}
					html_body += 'type="checkbox" class="select-info-one" value="'+dataarrary[i][0]+'"></li>';
				}
			}
		}
		html_body += '</ul></div>';
	}
	return html_body;
}


