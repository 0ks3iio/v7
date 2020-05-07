<#import "/fw/macro/mobilecommon.ftl" as common>
<#import "/studevelop/mobile/commonStuDevelop.ftl" as stuDevelop>
<@common.moduleDiv titleName="我的荣誉">
<link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
	<@stuDevelop.acadyearSemester stuDevelopUrl="${request.contextPath}/mobile/open/studevelop/honor/index?" params="studentId=${studentId!}" >

	</@stuDevelop.acadyearSemester>
<div class="mui-content">
	<ul class="mui-photo-add mui-honor-add mui-clearfix" id="fileDiv">
		<#if item?exists && item.images?exists && (item.images?size>0)>
			<#list item.images as img>
		    	<li>
		    		<a href="#">
		    			<img src="${fileUrl!}${img.filePath!}" id="${img.id!}"  data-preview-src="${img.path!}" data-preview-group="1"  />
		    		</a>
		    	</li>
	    	</#list>	
    	</#if>
    </ul>
</div>
<div id="delSheet" class="mui-popover mui-popover-bottom mui-popover-action">
	<input type="hidden" id="delImgId" value=""/>
    <!-- 可选择菜单 -->
    <ul class="mui-table-view">
      <li class="mui-table-view-cell">
        <a href="#" class="del-img">删除图片</a>
      </li>
    </ul>
    <!-- 取消菜单 -->
    <ul class="mui-table-view">
      <li class="mui-table-view-cell">
        <a href="#sheet1"><b>取消</b></a>
      </li>
    </ul>
</div>
<script type="text/javascript" charset="utf-8">
  	mui.init({
  		gestureConfig:{
			doubletap: true,
			longtap: true
		}
  	});
  	mui.previewImage();
  	mui('.mui-photo-add').on('longtap', 'img', function() {
  		//长按弹出删除框
  		var id = $(this).attr("id");
  		mui('#delSheet').popover('toggle');
  		$('#delImgId').val(id);
		return false;
	});
	mui('body').on('tap', '.mui-popover-action li>a', function() {
		if($(this).hasClass('del-img')){
			var delImgId = $("#delImgId").val();
			deleteImg(delImgId);
		};
		mui('#delSheet').popover('toggle');
		$("#delImgId").val('');
	});
</script>
<script type="text/javascript">
   setTimeout(function(){
   	  imgRatio2('.mui-photo-add',212/340);
   },500);
//   document.querySelector('.add').addEventListener('tap', function(e){
//		e.preventDefault();
//		mui.toast('正在上传图片',{
//			duration: 3500,
//			offset: 'center'
//		});
//	});
</script>
<script>
	
	var _index = 0;
	//追加拍照
	function addFileElem(){
		_index++;
		var html = '<li>';
			html += '<a href="javascript:void(0);" class="add" id="file_a_'+_index+'" style="height: 119px;">';
	    	html +=		'<div style="display:none;"><input type="file" id="file_input_'+_index+'" name="file_name_'+_index+'" accept="image/*" ></div>';
	    	html +=		'<img src="" id="file_img_'+_index+'" data-preview-src="" />';
	    	html +=		'<span id="file_span_'+_index+'" class="mui-icon mui-icon-plusempty"></span>';
	    	html +=		'<div id="file_msg_div_'+_index+'"></div>';
	    	html +=	'</a>';
			html += '</li>';
		
		$("#fileDiv").prepend(html);
		
		mui('#fileDiv').on('tap',"#file_a_"+_index, function(){
			$("#file_input_"+_index).click();
		});
		
		
		$("#file_input_"+_index).on('change',function(){
			var file = $(this).get(0).files[0];
			$("#file_img_"+_index).show();
			$("#file_span_"+_index).hide();
			previewFile(file, "#file_img_"+_index, function(){
				//上传
				upLoadFile("file_input_"+_index,_index);
			});
			
		});
		
		
	}	
	//初始化
	addFileElem();
	
	/**
	* js文件上传
	* fileObjId 附件input id
	*/
	function upLoadFile(fileObjId, index){
		var _uploadUrl = "${request.contextPath!}/mobile/open/studevelop/honor/upload";
		
		var fileObj = document.getElementById(fileObjId).files[0]; // 获取文件对象  
	    // FormData 对象  
	    var form = new FormData();
	    form.append("file", fileObj);// 文件对象
	    form.append("acadyear", '${acadyear!}');//
		form.append("semester", '${semester!}');
	    form.append("studentId", '${studentId!}');
	    // XMLHttpRequest 对象  
	    var xhr = new XMLHttpRequest();
	    xhr.open("post", _uploadUrl, true);
	    
	    xhr.onreadystatechange=function(){
	    	if(xhr.readyState==4){ 
	    		//追加
				addFileElem();
				imgRatio2('.mui-photo-add',212/340);
				//取消点击事件
				mui('#fileDiv').off('tap',"#file_a_"+_index);
				$("#file_msg_div_"+index).hide();
	    		if(xhr.status==200){
	    			var data = JSON.parse(xhr.responseText);
	    			if(data.success == false){//失败
	    				$("#file_msg_div_"+index).show();
						showImageTip("#file_msg_div_"+index, data.msg);
					}else{//成功
						//$("#"+delImgId).parents('li').remove();
			    		toastMsg(data.msg);
			    		location.reload();
					}
	    		}else{
	    			$("#file_msg_div_"+index).hide();
            		showImageTip("#file_msg_div_"+index, "上传失败，请重新上传");
	    		}
		    	
            }
	    };
	    
	    // 实现进度条功能  
	    xhr.upload.addEventListener("progress", function(e){
	    	if (e.lengthComputable) {
			      var percentLoaded = Math.round((e.loaded/e.total) * 100);
			      updateImageProgress("#file_msg_div_"+index, percentLoaded);
		    }
	    }, false);
	    
	    //失败
	    xhr.upload.addEventListener("error", function(data){
	    	showImageTip("#file_msg_div_"+index, "上传失败");
	    });
	    
	    //发送数据
    	xhr.send(form);
	} 
	
	//图片上传显示提示信息
	function showImageTip(fileMsgObjId, msg){
		var html = '';
		html += '<span class="layer-state">';
		html += '<i class="warning"></i><br />';
		html += '<span class="f-9">'+msg+'</span>';
		html += '</span>';
		
		$(fileMsgObjId).html(html);
	}
	
	//图片进度条
	function updateImageProgress(fileMsgObjId, progress){
		if($(fileMsgObjId).hasClass("layer-state")){//只更新数字
			$(fileMsgObjId).find(".my-span").text(progress+"%");
		}else{
			var html = '<span class="layer-state">';
				html += '<span class="f-15 my-span">'+progress+'%</span>';
				html += '</span>';
	        $(fileMsgObjId).html(html);		
		}
	}
	
	//删除图片
	function deleteImg(attId){
		mui.post('${request.contextPath!}/mobile/open/studevelop/honor/delete',{
				"attId":attId,
			},function(data){
				if(data.success == true){
					$('#'+attId).parents('li').remove();
					toastMsg(data.msg);
				}else{
					toastMsg(data.msg);
				}
			},'json'
		);
	}
</script>
</@common.moduleDiv>