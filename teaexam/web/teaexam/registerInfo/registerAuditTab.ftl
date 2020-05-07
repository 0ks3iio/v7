<div class="box box-default">
	<div class="box-body clearfix">
        <div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs nav-tabs-1">
				 	<li class="active">
				 		<a data-toggle="tab" href="#" onClick="searchBeginList('1');">待审核</a>
				 	</li>
				 	<li class="">
				 		<a data-toggle="tab" href="#" onClick="searchBeginList('2');">已审核</a>
				 	</li>
				</ul>
			</div>			
	       <div class="tab-content" id="auditTabDiv"></div>
	   </div>
	</div>
</div>

<script>
$(function(){	
	searchBeginList('1');
});

function searchBeginList(index){
    if(index == '1'){
        var url = "${request.contextPath}/teaexam/registerAudit/auditingList";
        $('#auditTabDiv').load(url);
    }else{
        var url = "${request.contextPath}/teaexam/registerAudit/haveAudtingHead";
        $('#auditTabDiv').load(url);
    }
}
</script>
