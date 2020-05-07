<div class="box box-default">
	<div class="box-body clearfix">
        <div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs nav-tabs-1">
					<li class="active">
						<a data-toggle="tab" href="#a1" onClick="searchBeginList('1')">报名中</a>
					</li>
					<li class="">
						<a data-toggle="tab" href="#a2" onClick="searchBeginList('2')">已结束</a>
					</li>
				</ul>
			</div>
		    <div class="tab-content" id="bmTblDiv"></div>
		</div>
	</div>
</div>
<script>
$(function(){	
	searchBeginList('1');
});

function searchBeginList(index){
    var url = "${request.contextPath}/teaexam/registerInfo/beginRegister?index="+index;
    $('#bmTblDiv').load(url);
}
</script>												