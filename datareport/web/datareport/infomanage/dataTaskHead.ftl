	<div class="row">
		<div class="col-xs-12">
		   <div class="box box-default">
				<div class="box-body clearfix">
                    <div class="tab-container">
						<div class="tab-header clearfix">
							<ul class="nav nav-tabs nav-tabs-1">
							 	<li class="active">
							 		<a data-toggle="tab" href="#" onClick="selectTab(1)">回收统计</a>
							 	</li>
							 	<li class="">
							 		<a data-toggle="tab" href="#" onClick="selectTab(2)">问卷详情</a>
							 	</li>
							</ul>
						</div>
						<div class="tab-content" id="tabContentDiv">
							
						</div>
				    </div>
				</div>
			</div>
		</div><!-- /.col -->
	</div><!-- /.row -->
<script>
	$(function(){
	    showBreadBack(showInfoListHead,true,"问卷详情");		
		selectTab(1);
	})
	
	function selectTab(type){
		var url = "";
		if (type == 1) {
			url = "${request.contextPath}/datareport/infomanage/showtasklist?infoId=${infoId!}";
		} else {
			url = "${request.contextPath}/datareport/infomanage/showinfo?infoId=${infoId!}";
		}
		$("#tabContentDiv").load(url);
	}
</script>