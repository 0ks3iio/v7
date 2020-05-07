<div class="page-content" id="showResourceTabDiv">
	<div class="row">
		<div class="col-xs-12">
		   <div class="box box-default">
				<div class="box-body clearfix">
                    <div class="tab-container">
						<div class="tab-header clearfix">
							<ul class="nav nav-tabs nav-tabs-1">
							 	<li class="active" onClick="showResourceList(1)">
							 		<a data-toggle="tab" href="#">志愿填报指导</a>
							 	</li>
							 	<li class="" onClick="showResourceList(2)">
							 		<a data-toggle="tab" href="#">选科指导</a>
							 	</li>
							 	<li class="" onClick="showResourceList(3)">
							 		<a data-toggle="tab" href="#">自主招生指导</a>
							 	</li>
							</ul>
						</div>
						<!-- tab切换开始 -->
						<div class="tab-content">
							<div id="a1" class="tab-pane active">
								<div class="filter">
									<div class="filter-item">
										<button class="btn btn-blue" onClick="oneResource('')">新增</button>
										<button class="btn btn-white" onClick="deleteResource('')">删除</button>
									</div>
									<div class="filter-item filter-item-right">
										<div class="filter-content">
											<div class="input-group input-group-search">
												<select class="form-control" id="selectType" name="selectType" onChange="findByTitleName()">
													<option value="0">视频、资讯</option>
													<option value="1">视频</option>
													<option value="2">资讯</option>
												</select>
												<div class="pos-rel pull-left">
													<input type="text" class="form-control" placeholder="请输入标题名" id="titleName" name="titleName">
												</div>
												<div class="input-group-btn">
													<button type="button" class="btn btn-default" onClick="findByTitleName()">
														<i class="fa fa-search"></i>
													</button>
												</div>
											</div><!-- /input-group -->
										</div>
									</div>
								</div>
                                <div class="table-container" id="resourceListDiv">
                                	
								</div>
							</div>
						</div>
				    </div>
				</div>
			</div>
		</div><!-- /.col -->
	</div><!-- /.row -->
</div><!-- /.page-content -->
<div id="showResourceDiv" style="display:none">
	
</div>
<script>
	$(function(){
		showResourceList(1);
	});
	
	function showResourceList(resourceType) {
		document.getElementById("selectType").options[0].selected = true;
		$("#titleName").val("");
		var str = "?resourceType="+resourceType+"&type=0&titleName=";
		var url = "${request.contextPath}/career/resourcecenter/managepage/resourcelist" + str;
		$("#resourceListDiv").load(url);
	}
	
	$("#titleName").bind("keyup",function(event){
		if(event.keyCode ==13){
   			findByTitleName();
 	 	}
	});
	
	function findByTitleName() {
		var resourceType = 1;
		$(".nav li").each(function(){
			if ($(this).attr("class") == "active") {
				resourceType = $(this).index()+1;
			}
		});
		var type = $("#selectType").val();
		var titleName = $("#titleName").val();
		var str = "?resourceType=" + resourceType + "&type="+ parseInt(type) +"&titleName="+titleName;
		var url = "${request.contextPath}/career/resourcecenter/managepage/resourcelist" + str;
		url = encodeURI(encodeURI(url));
		$("#resourceListDiv").load(url);
	}
	
	function oneResource(resourceId) {
		var resourceType = 1;
		$(".nav li").each(function(){
			if ($(this).attr("class") == "active") {
				resourceType = $(this).index()+1;
			}
		});
		var url = "${request.contextPath}/career/resourcecenter/managepage/resourceadd?resourceType="+resourceType+"&resourceId="+resourceId;
		$("#showResourceDiv").load(url);
		$("#showResourceTabDiv").attr("style","display:none");
		$("#showResourceDiv").attr("style","display:block");
	}
	
	function deleteResource(resourceId) {
		var resourceIds = "";
		if (resourceId != '') {
			resourceIds = resourceId;
		} else {
			var carbox=$('input:checkbox[name=carResouceCheckbox]');
			if (carbox == null) {
				return;
			} else {
				if (carbox.length>0) {
					$('input:checkbox[name=carResouceCheckbox]').each(function(i){
						if($(this).is(':checked')){
							resourceIds += "," + $(this).val();
						}
					});
				} else {
					layer.msg('请先选择资源！');
					return;
				}
			}
	
			if(resourceIds==""){
				layer.msg('请先选择资源！');
				return;
			}
			resourceIds = resourceIds.substring(1);
		}
		var dellayer = layer.confirm('确定要删除吗？', function(index){
			$.ajax({
				url:'${request.contextPath}/career/resourcecenter/managepage/resourcedelete',
				type:'post',
				data:{'resourceIds':resourceIds},
				success:function(data) {
					layer.close(dellayer);
					var jsonO = JSON.parse(data);
					if(jsonO.success){
						findByTitleName();
	        		}else{
	        			layerTipMsgWarn("删除失败","");
	        		}
				},
	 			error:function(XMLHttpRequest, textStatus, errorThrown){}
			});
		});
	}
	
	var loadTime;

	function layerTime() {
		loadTime = layer.msg('加载中', {
  			icon: 16,
  			shade: 0.01,
  			time: 60*1000
		});
	}

	function layerClose() {
		layer.close(loadTime);
	}
</script>