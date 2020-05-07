<#-- 
<style>
#mydiv{
//border:solid red 1px;
position:relative;
top:50%;
transform:translateY(200%);
}
</style>
div class="text-center" id = "mydiv">
	<button class="btn btn-blue" data="0">普通录分人员</button><br><br>
	<button class="btn btn-blue" data="1" >录分总管理员</button>
</div>-->
<div class="box box-default box-roleEntry" xmlns="http://www.w3.org/1999/html">
		<div class="box-body">
			<h3><span>成绩录入身份选择</span></h3>
			<a class="btn btn-block btn-blue text-left role-btn" data="0" href="javascript:void(0)">普通录分人员
			<i class="fa fa-long-arrow-right"></i></a>
			<a class="btn btn-block btn-blue text-left role-btn" data="1" href="javascript:void(0)">录分总管理员
			<i class="fa fa-long-arrow-right"></i></a>
		</div>
	</div>
</div>
<script>
$('.role-btn').on('click',function(){
	var data = $(this).attr('data');
	params = '?noLimit=' + data;
	if(${type!0} ==1){
		url = '${request.contextPath!}/scoremanage/scoreInfo/lock/index/page' + params;
	}else{
		url = '${request.contextPath!}/scoremanage/optionalScore/index/page' + params;
	}
	$('.model-div').load(url);
});
</script>