<div class="box box-default box-roleEntry" xmlns="http://www.w3.org/1999/html">
		<div class="box-body">
			<h3><span>成绩录入身份选择</span></h3>
			<a class="btn btn-block btn-blue text-left" onclick="toCheckEdit('0')" href="javascript:void(0)">普通录分人员
			<i class="fa fa-long-arrow-right"></i></a>
			<a class="btn btn-block btn-blue text-left" onclick="toCheckEdit('1')" href="javascript:void(0)">录分总管理员
			<i class="fa fa-long-arrow-right"></i></a>
		</div>
	</div>
</div><!-- /.model-div -->

<script>
function toCheckEdit(data){
	var params = '?noLimit=' + data;
	if(${type!0} ==1){
		url = '${request.contextPath!}/exammanage/scoreNewInput/index/page' + params;
	}else{
		url = '${request.contextPath!}/exammanage/scoreOptional/index/page' + params;
	}
	$(".model-div").load(url);
}
</script>