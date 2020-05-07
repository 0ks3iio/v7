<script>
	$(function(){
		gradeTable();
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			var chooseId=$(this).parent().attr("id");
			if("li01"==chooseId){
				gradeTable();
			}else if("li02"==chooseId){
				subjectTable();
			}else if("li03"==chooseId){
				teaTable();
			}else if("li04"==chooseId){
				placeTable();
			}else if("li05"==chooseId){
				scoreTable();
			}
		})
		
		$("#gradeTableList").on("click","ul li.courseLi",function(event){
			var obj = this;
			setTimeout(function(){
				$(obj).siblings().removeClass("active");
				$(obj).addClass("active");
				
			},5);
		});
	});
	
	function goBack(){
		if(canLoadOther()){
			var url =  '${request.contextPath}/newgkelective/index/page';
			$("#showList").load(url);
		}
	}
	
	showBreadBack(goBack,false,"基础条件");
	
	function gradeTable(){
		if(canLoadOther()){
			var url = "${request.contextPath}/newgkelective/${gradeId!}/goBasic/gradeSet/index/page";
			$("#gradeTableList").load(url);
		}else{
			backClick();
		}
	}
	
	function subjectTable(){
		if(canLoadOther()){
			var url = '${request.contextPath}/newgkelective/${arrayItemId!}/courseFeatures/gradeIndex';
			$("#gradeTableList").load(url);
		}else{
			backClick();
		}
	}
	
	function teaTable(){
		if(canLoadOther()){
			var array_item_id = $('[name="array_item_id"]').val();
			var url = '${request.contextPath}/newgkelective/'+array_item_id+'/goBasic/teacherSet/index/page';
			$("#gradeTableList").load(url);
		}else{
			backClick();
		}
	}
	
	function placeTable(){
		if(canLoadOther()){
			var array_item_id = $('[name="array_item_id"]').val();
			var url = '${request.contextPath}/newgkelective/${gradeId!}/goBasic/placeSet/index/page';
			$("#gradeTableList").load(url);
		}else{
			backClick();
		}
	}
	
	function scoreTable(){
		if(canLoadOther()){
			var array_item_id = $('[name="array_item_id"]').val();
			var url = '${request.contextPath}/newgkelective/newGkScoreResult/index?gradeId=${gradeId}';
			$("#gradeTableList").load(url);
		}else{
			backClick();
		}
	}
	
	function canLoadOther(){
		return true;
	}
	function backClick(){
		//如果不执行切换
	}

	var arrayId = "";
</script>
<input type="hidden" name="array_item_id" value="${arrayItemId!}"/>		
<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1 nav-tabs-table" role="tablist">
			<li  class="active li_class" id="li01" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" >年级特征</a></li>
			<li class="li_class" id="li02" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" >课程特征</a></li>
			<li class="li_class" id="li03" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab">教师特征</a></li>
			<li class="li_class" id="li04" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" >教室场地</a></li>
			<li  class="li_class" id="li05" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" >参考成绩</a></li>
			
		</ul>
		<div class="tab-content" id="gradeTableList">
		</div>
	</div>
</div>
