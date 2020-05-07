<#import "/bigdata/v3/templates/webUploaderMacro.ftl" as upload />
<div class="row">
        <div class="col-xs-12">
			<div class="box box-default padding-20 js-height">
				<div class="transfer-box">
					<div class="left-part-240">
						<ul>
							<li class="active" id="fileBtn" onclick="switch2page('fileBtn')">
								添加数据文件
								<img src="${request.contextPath}/static/bigdata/images/icon-add-hover.png"/>
							</li>
							<li id="processBtn">
								传输列表
								<img src="${request.contextPath}/static/bigdata/images/icon-transfer.png"/>
							</li>
							<li id="historyBtn" onclick="switch2page('historyBtn')">
								历史导入数据
								<img src="${request.contextPath}/static/bigdata/images/icon-history.png"/>
							</li>
						</ul>
					</div>
					<div class="right-part_240">
						<div class="right-part-page height-1of1 padding-20">
							<div class="margin-bottom-20 clearfix">						
								<div class="btn-add-file float-left margin-r-10">
									 <@upload.fileUpload businessKey="event-import" contextPath="${request.contextPath}" resourceUrl="${resourceUrl!}"  extensions="xls,xlsx" size="100" fileNumLimit="1" handler="loadFile">
								            <label for="file" id="upFile" class="no-margin btn-left-img js-addFiles">添加文件<img src="${request.contextPath}/static/bigdata/images/icon-add.png" alt="" /></label>
								            <input type="hidden" id="event-import-path" value="">
											<input type="hidden" name="filePath" id="filePath" value="">
											<input type="hidden" name="fileName" id="fileName" value="">
									</@upload.fileUpload>
				        		</div>
								<button class="btn btn-blue btn-left-img" onclick="eventImport()">开始导入<img src="${request.contextPath}/static/bigdata/images/icon-transfer-white.png" alt="" />
								</button>
							</div>
							<ul class="file-list-wrap" id="fileNameDiv">
							</ul>
							<div id="tipsDiv"></div>
						</div>
						<div class="right-part-page height-1of1 hide" id="processDiv">
							
						</div>
						<div class="right-part-page height-1of1 hide" id="historyDiv">
						
						</div>
				</div>
			</div>
		</div>
    </div>
</div>
<script type="text/javascript">
	var isSubmit=false;

	function loadFile(){
		if(hasUploadSuc){
			$("#filePath").val($("#event-import-path").val());
			$("#fileNameDiv").html('<li class="">'+fileName+'<i class="wpfont icon-close js-delete"></i></li>');
			$("#fileName").val(fileName);
			//fileName= "";
		}
	}

	function eventImport(){
		if(isSubmit){
			return;
		}
		var filePath=$("#filePath").val();
		if(filePath == ""){
			showLayerTips4Confirm('error','请先选择导入文件');
			return;
		}
		switch2page('processBtn');
		$.ajax({
		            url:"${request.contextPath}/bigdata/event/import/submit",
		            data:{
					  'eventId':'${eventId!}',
		              'businessId':'${businessId!}',
		              'filePath':filePath,
		              'fileName':$("#fileName").val()
		            },
		            type:"post",
		            clearForm : false,
					resetForm : false,
		            dataType: "json",
		            success:function(response){
				 		if(!response.success){
		 					isSubmit = false;
				 		}else{
				 			isSubmit = false;
		    			}
		          },
		          error:function(XMLHttpRequest, textStatus, errorThrown){
		          }
		    });
	}
	
	function switch2page(btnType){
		var obj=$("#"+btnType);
		if (obj.hasClass('active') == false) {
			var src = obj.siblings('li.active').find('img').attr('src').slice(0,-10) + '.png';
			obj.siblings('li.active').find('img').attr('src',src);
	    	obj.addClass('active').siblings().removeClass('active');
	    	$('.right-part_240').find('.right-part-page').eq(obj.index()).removeClass('hide').siblings('.right-part-page').addClass('hide');
    	}
		if(btnType !="fileBtn"){
			var index=layer.msg('数据加载中......', {
		      	icon: 16,
		     	 time:0,
		      	shade: 0.01
		    });
			if(btnType=="processBtn"){
				obj.find('img').attr('src',"${request.contextPath}/static/bigdata/images/icon-transfer-hover.png");
				var url =  "${request.contextPath}/bigdata/event/import/progress?businessId=${businessId!}";
				$("#processDiv").load(url,function() {
		  			layer.close(index);
				});
			}else if(btnType=="historyBtn"){
				var url =  "${request.contextPath}/bigdata/event/import/history?eventId=${eventId!}";
				$("#historyDiv").load(url,function() {
		  			layer.close(index);
				});
			}
 		}else{
 			eventDataImport('${eventId!}');
 		}
	}

    $(function(){
        function height(){
            $('.js-height').each(function(){
                $(this).css({
                    height: $(window).height() - $(this).offset().top - 20,
                });
            });
        }
        height();
        
        $('.left-part-240 li').on('mouseenter',function(){
        	if ($(this).hasClass('active') == false) {
        		var src = $(this).find('img').attr('src').slice(0,-4) + '-hover.png';
				$(this).find('img').attr('src',src);
        	}
		}).on('mouseleave',function(){
			if ($(this).hasClass('active') == false) {
				var src = $(this).find('img').attr('src').slice(0,-10) + '.png';
				$(this).find('img').attr('src',src);
			}
		});
        
		$('body').on('click','.js-delete',function(){
			$(this).parents('li').remove();
			$("#filePath").val('');
		});
    });
</script>