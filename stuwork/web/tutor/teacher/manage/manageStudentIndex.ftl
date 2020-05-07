<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1" role="tablist">
			<li class="active" role="presentation"><a href="#tabList" onclick="showTab('1')" role="tab" data-toggle="tab">登记记录</a></li>
            <li role="presentation"><a href="#tabList" onclick="showTab('2')" role="tab" data-toggle="tab">所带学生</a></li>
		</ul>
		<div class="tab-content">
			<div id="tabList" class="tab-pane active" role="tabpanel">

			</div>			
		</div>
	</div>
</div>
<div class="layer layer-tutor-detailedId ">

</div><!-- E 登记记录 -->
<script>
  var isOk = true;
  $(document).ready(function(){
	showTab('1');
  })
  function showTab(type){
	if(type=='1'){
		 var url =  '${request.contextPath}/tutor/record/manage/doRecordtab';
	     $("#tabList").load(url);
	}else{
		var url =  '${request.contextPath}/tutor/manage/showStudentList';
		$("#tabList").load(url);
	}
 }
</script>