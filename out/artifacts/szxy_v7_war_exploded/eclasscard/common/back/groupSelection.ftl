<#macro group_div dataUrl="" id="" >
<#nested>
<div class="publish-obj">
	<ul id="group_head_ul"class="nav nav-sm nav-tabs" role="tablist">
	</ul>
	<div id="group_body_div" class="tab-content">
	</div>
</div>
<script>
var point_out_str = '<div class="wrap-1of1 centered no-data-state">';
	point_out_str += '	<div class="text-center">';
	point_out_str += '		<img src="${request.contextPath}/static/images/classCard/no-data-common.png" width="120" height="120"/>';
	point_out_str += '		<p>该用途类型的班牌还没有添加哦，<br/>可前往"班牌管理"中选择相应的设备进行设置</p>';
	point_out_str += '	</div>';
	point_out_str += '</div>';
$(document).ready(function(){
	initGroupData();
})
function initGroupData(){
	var dataIds =$("#${id}").val();
	$.ajax({
		url:"${dataUrl}",
		type:'post',
		data:{"dataIds":dataIds},
		success:function(data) {
			var resultarrary = JSON.parse(data);
			initHeadUl(resultarrary);
		},
 		error : function(XMLHttpRequest, textStatus, errorThrown) {  
		}
	});
}

var bodyHtml = '';
function initHeadUl(resultarrary){
	var headarrary = JSON.parse(resultarrary[0]);
	var dataarrary = JSON.parse(resultarrary[1]);
	var xzharrary = JSON.parse(resultarrary[2]);
	var fxzharrary = JSON.parse(resultarrary[3]);
	var headHtml = '';
	for (var i in headarrary) {
		 var className='';
		 var numberShow='';
		 var number = parseInt(headarrary[i][2]);
		 var allNumber = parseInt(headarrary[i][6]);
		 var showAlltag = false;
		 if(number>0){
		 	numberShow = '<em class="badge badge-yellow">'+number+'</em>';
		 }
		 if(allNumber>0){
			 showAlltag = true;
		 }
		 if(0==i){
		 	className = 'active';
		 }
	 	headHtml+='<li class="'+className+'" role="presentation"><a href="#'+headarrary[i][0]+'" role="tab" data-toggle="tab">'+headarrary[i][1]+numberShow+'</a></li>';
		headHtml+='<input type="hidden" class="type-all-num-show" value="'+allNumber+'" >'
		bodyHtml += '<div id="'+headarrary[i][0]+'" class="tab-pane '+className+'" role="tabpanel">';
		initDataDiv(dataarrary,xzharrary,fxzharrary,headarrary[i][0],headarrary[i][5],headarrary[i][7],showAlltag);
		bodyHtml += '</div>';
	}
	$("#group_head_ul").html(headHtml);
	$("#group_body_div").html(bodyHtml);
}

function initDataDiv(dataarrary,xzharrary,fxzharrary,type,isGroup,checked,showAlltag){
	if(isGroup=='1'){
		if(showAlltag){
			bodyHtml += '<div class="filter">';
			bodyHtml += '	<div class="filter-item block">';
			bodyHtml += '		<span class="filter-name">';
			bodyHtml += '			<label>';
			bodyHtml += '				<input type="checkbox" id="selectAllClass" onChange="selectAll(\'selectAllClass\')" '+checked+' class="wp">';
			bodyHtml += '				<span class="lbl"> 全选</span>';
			bodyHtml += '			</label>';
			bodyHtml += '		</span>';
			bodyHtml += '	</div>';
			bodyHtml += '</div>';
			bodyHtml += '<div class="filter">';
			for (var i in xzharrary) {
				bodyHtml += '<div class="filter-item block">';
				bodyHtml += '	<span class="filter-name">';
				bodyHtml += '		<label>';
				bodyHtml += '			<input type="checkbox" onclick="checkGroupAll(\''+xzharrary[i][0]+'\',this)" '+xzharrary[i][2]+' class="wp group-checked-tag">';
				bodyHtml += '			<span class="lbl"> '+xzharrary[i][1]+'：</span>';
				bodyHtml += '		</label>';
				bodyHtml += '	</span>';
				bodyHtml += '	<div class="filter-content">';
				bodyHtml += '		<div id="'+xzharrary[i][0]+'" class="class-card-labels">';
				for (var j in dataarrary) {
					if(dataarrary[j][5] == type &&xzharrary[i][0] == dataarrary[j][6]){
						var selectclass = '';
						if(dataarrary[j][4] == '1'){
							selectclass = 'selected'
						}
						bodyHtml+='<span class="class-card-label '+selectclass+'" data-id="' + dataarrary[j][0] + '"><span>'+dataarrary[j][2]+'</span><br>'+dataarrary[j][1]+'</span>';
					}
				}
				bodyHtml += '</div>';
				bodyHtml += '</div>';
				bodyHtml += '</div>';
			}
			bodyHtml += '</div>';
		}else{
			bodyHtml += point_out_str;
		}
		
	}else if(isGroup=='2'){
		if(showAlltag){
			bodyHtml += '<div class="filter">';
			bodyHtml += '	<div class="filter-item block">';
			bodyHtml += '		<span class="filter-name">';
			bodyHtml += '			<label>';
			bodyHtml += '				<input type="checkbox" id="selectAllRoom" onChange="selectAll(\'selectAllRoom\')" '+checked+' class="wp">';
			bodyHtml += '				<span class="lbl"> 全选</span>';
			bodyHtml += '			</label>';
			bodyHtml += '		</span>';
			bodyHtml += '	</div>';
			bodyHtml += '</div>';
			bodyHtml += '<div class="filter">';
			for (var i in fxzharrary) {
				bodyHtml += '<div class="filter-item block">';
				bodyHtml += '	<span class="filter-name">';
				bodyHtml += '		<label>';
				bodyHtml += '			<input type="checkbox" onclick="checkGroupAll(\''+fxzharrary[i][0]+'\',this)" '+fxzharrary[i][2]+'  class="wp group-checked-tag">';
				bodyHtml += '			<span class="lbl"> '+fxzharrary[i][1]+'：</span>';
				bodyHtml += '		</label>';
				bodyHtml += '	</span>';
				bodyHtml += '	<div class="filter-content">';
				bodyHtml += '		<div id="'+fxzharrary[i][0]+'" class="class-card-labels">';
				for (var j in dataarrary) {
					if(dataarrary[j][5] == type &&fxzharrary[i][0] == dataarrary[j][6]){
						var selectclass = '';
						if(dataarrary[j][4] == '1'){
							selectclass = 'selected'
						}
						bodyHtml+='<span class="class-card-label '+selectclass+'" data-id="' + dataarrary[j][0] + '"><span>'+dataarrary[j][2]+'</span><br>'+dataarrary[j][1]+'</span>';
					}
				}
				bodyHtml += '</div>';
				bodyHtml += '</div>';
				bodyHtml += '</div>';
			}
			bodyHtml += '</div>';
		}else{
			bodyHtml += point_out_str;
		}
	
	}else{
		if(dataarrary.length > 0){
			if(showAlltag){
				bodyHtml += '<div class="filter">';
				bodyHtml += '<div class="filter-item block">';
				bodyHtml += '	<span class="filter-name">';
				bodyHtml += '		<label>';
				bodyHtml += '			<input type="checkbox" onclick="checkGroupAll(\''+type+'\',this)" '+checked+' class="wp">';
				bodyHtml += '			<span class="lbl"> 全选：</span>';
				bodyHtml += '		</label>';
				bodyHtml += '	</span>';
				bodyHtml += '	<div class="filter-content">';
				bodyHtml += '		<div class="class-card-labels">';
				for (var i in dataarrary) {
					if(dataarrary[i][5] == type){
						var selectclass = '';
						if(dataarrary[i][4] == '1'){
							selectclass = 'selected'
						}
						bodyHtml+='<span class="class-card-label '+selectclass+'" data-id="' + dataarrary[i][0] + '"><span>'+dataarrary[i][2]+'</span><br>'+dataarrary[i][1]+'</span>';
					}
				}
				bodyHtml += '</div>';
				bodyHtml += '</div>';
				bodyHtml += '</div>';
				bodyHtml += '</div>';
			}else{
				bodyHtml += point_out_str;
			}
		}
	}
}
// 选择
$('#group_body_div').on('click','.class-card-label', function(){
	if($(this).hasClass('disabled')){
		return false;
	}
	var vals = $("#${id}").val();
	if($(this).hasClass('selected')){
		var thisDataId = $(this).data("id");
		if(thisDataId && thisDataId != ''){
			if(vals.indexOf(thisDataId+",") != -1){
				vals = vals.split(thisDataId+",").join("");
			}else if(vals.indexOf(","+thisDataId) != -1){
				vals = vals.split(","+thisDataId).join("");
			}else{
				vals = vals.split(thisDataId).join("");
			}
		}
		changeChoiseNum(false);
	}else{
		if(vals == ''){
			vals += $(this).data("id");
		}else{
			vals += ',' + $(this).data("id");
		}
		changeChoiseNum(true);
	}
	$("#${id}").val(vals);
	$(this).toggleClass('selected');
});

function checkGroupAll(id,thisObj){
	var ischecked = false;
	if($(thisObj).is(':checked')){
		ischecked = true;
	}
  	$("#"+id+" .class-card-label").each(function(){
  		if(ischecked){
  			if(!$(this).hasClass('selected')){
  				$(this).click();
  			}
  		}else{
  			if($(this).hasClass('selected')){
  				$(this).click();
  			}
  		}
	});
}

function changeChoiseNum(isAdd){
	$("#group_head_ul li").each(function(){
		if($(this).hasClass('active')){
			if($(this).find('a').find('.badge').length>0){
				var num = parseInt($(this).find('a').find('.badge').text());
				if(isAdd){
					$(this).find('a').find('.badge').text(num+1);
				}else{
					if(num-1<1){
						var htmlStr = $(this).find('a').html();
						htmlStr = htmlStr.substring(0, htmlStr.lastIndexOf('<em'));
						$(this).find('a').html(htmlStr);
					}else{
						$(this).find('a').find('.badge').text(num-1);
					}
				}
			}else{
				$(this).find('a').append('<em class="badge badge-yellow">1</em>');
			}
		}
	});
}

function selectAll(id) {
	var ischecked = false;
	if($("#"+id).is(':checked')){
		ischecked = true;
	}
	$("#"+id).closest(".filter").next().find(".group-checked-tag").each(function(){
		if(ischecked){
  			if(!$(this).is(':checked')){
  				$(this).click();
  			}
  		}else{
  			if($(this).is(':checked')){
  				$(this).click();
  			}
  		}
	});
}
</script>
</#macro>