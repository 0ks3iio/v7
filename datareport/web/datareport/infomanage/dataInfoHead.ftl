<div class="row">
	<div class="col-xs-12">
	   <div class="box box-default">
			<div class="box-body clearfix">
				<div class="filter">
					<div class="filter-item">
						<a class="btn btn-blue" href="#" onClick="editInfo('')">新增问卷</a>
						<a style="display:none;" class="btn btn-blue" href="#" onClick="dataStructure()">修改数据结构</a>
					</div>
					<div class="filter-item-right">
						<div class="filter-content">
							<div class="input-group input-group-search">
								<div class="pos-rel pull-left">
									<input type="text" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="请输入问卷名称" id="title" name="title">
								</div>
								<div class="input-group-btn">
									<button type="button" class="btn btn-default" onClick="showInfoList()">
										<i class="fa fa-search"></i>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div id="showInfoListDiv">
					
				</div>
			</div>
		</div>
	</div><!-- /.col -->
</div><!-- /.row -->
<script>
	$(function(){
		showInfoList();
	})
	
	function showInfoList() {
		var title = $("#title").val();
		var url = "${request.contextPath}/datareport/infomanage/showinfolist?title="+title;
		url = encodeURI(encodeURI(url));
		$("#showInfoListDiv").load(url);
	}
	
	function editInfo(infoId) {
		var url = "${request.contextPath}/datareport/infomanage/editinfo?infoId="+infoId;
		$("#dataInfoManageDiv").load(url);
	}
	
	function dataStructure() {
		$.ajax({
			url:"${request.contextPath}/datareport/table/datastructure",
			dataType : 'json',
			success:function(data) {
				if (data.success) {
					layer.msg("成功");
				} else {
					layer.msg("失败");
				}
			},
 			error : function(XMLHttpRequest, textStatus, errorThrown) {  
 				layer.msg(XMLHttpRequest.status);
			}
		});
	}
</script>