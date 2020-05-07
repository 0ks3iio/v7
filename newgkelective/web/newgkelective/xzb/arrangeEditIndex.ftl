<script>
	$(function(){
		//初次进入直接显示总课表
		<#if toLi?default('01') == '01'>
		gradeTable();
		<#elseif toLi?default('') == '02'>
		$('#li02').addClass('active').siblings().removeClass('active');
		subjectTable();
		<#elseif toLi?default('') == '03'>
		$('#li03').addClass('active').siblings().removeClass('active');
		teaTable();
		<#elseif toLi?default('') == '04'>
		$('#li04').addClass('active').siblings().removeClass('active');
		timeTable();
		<#elseif toLi?default('') == '05'>
		$('#li05').addClass('active').siblings().removeClass('active');
		classTable();
		</#if>
		
		$("#gradeTableList").on("click","ul li.courseLi",function(){
			var obj = this;
			setTimeout(function(){
				$(obj).siblings().removeClass("active");
				$(obj).addClass("active");
			},5);
		});
	});
	
	
	//wyy.返回到课表时间列表页面
	function backToGradeList(){
		var gradeId = '${gradeId!}';
		<#if fromSolve?default('0')=='1'>
            var url = '${request.contextPath}/newgkelective/xzb/arraySet/pageIndex?arrayId=${arrayId!}';
            $("#showList").load(url);
		<#elseif arrayId?default('')==''>
		   var array_item_id = $('[name="array_item_id"]').val();
		   var url =  '${request.contextPath}/newgkelective/xzb/addArray/page?divideId=${divideId!}&lessArrayId='+array_item_id;
			$("#showList").load(url);
	    <#else>
		   var url =  '${request.contextPath}/newgkelective/'+gradeId+'/goArrange/editArray/page?arrayId=${arrayId!}';
			$("#showList").load(url);
	    </#if>
		<#--url='${request.contextPath}/newgkelective/${divideId!}/gradeArrange/index?arrayId=${arrayId!}';
		$("#showList").load(url);-->
	}
var pageTitle='排课特征';
var arrayId= '${arrayId!}';
var divideId= '${divideId!}';

<#if arrayItemId?default('')==''>
	pageTitle='新增排课特征';
</#if>	
	showBreadBack(backToGradeList, false, pageTitle);
	function checkItemId(array_item_id){
		if(array_item_id==''){
			layer.msg("年级特征设置还没维护！", {
						offset: 't',
						time: 2000
					});
			//$('.nav-tabs-table li:eq(0)').addClass('active').siblings('li').removeClass('active');					
			return false;
		}
		return true;
	}
	
	//Tab1.总课表排课显示
	function gradeTable(){
		var array_item_id = $('[name="array_item_id"]').val();
		var url = "${request.contextPath}/newgkelective/xzb/gradeSet/edit?divideId=${divideId!}&arrayItemId="+array_item_id;
		$("#gradeTableList").load(url);
	}
	
	function subjectTable(){
		var array_item_id = $('[name="array_item_id"]').val();
		if(!checkItemId(array_item_id)){
			return;
		}
		var url = '${request.contextPath}/newgkelective/xzb/courseFeatures/index?arrayItemId='+array_item_id;
		$("#gradeTableList").load(url);
	}
	function classTable(){
		var array_item_id = $('[name="array_item_id"]').val();
		if(!checkItemId(array_item_id)){
			return;
		}
		var url = '${request.contextPath}/newgkelective/'+array_item_id+'/classFeature/idnex';
		$("#gradeTableList").load(url);
	}
	
	function teaTable(){
		var array_item_id = $('[name="array_item_id"]').val();
		if(!checkItemId(array_item_id)){
			return;
		}
		var url = '${request.contextPath}/newgkelective/${divideId!}/subjectTeacherArrange/add?gradeId=${gradeId!}&itemId='+array_item_id+'&arrayId=${arrayId!}';
		$("#gradeTableList").load(url);
	}
	
	function timeTable(){
		var array_item_id = $('[name="array_item_id"]').val();
		if(!checkItemId(array_item_id)){
			return;
		}
		var url = '${request.contextPath}/newgkelective/${divideId!}/gradeLessonTimeInf?array_item_id='+array_item_id;
		$("#gradeTableList").load(url);
	}
	
	function getHeadInf(rowObj){
		//获取当前行中的rowspan属性的单元格数量
		var head = $(rowObj).children("[rowspan]");
		//如果数量大于0 说明存在这样的单元格
		if(head.length>0){
			
			return {value:head.children(":hidden").val(),rowIndex:rowObj.rowIndex};
		}
		else {
			//如果数量为零，找前一行中的具有rowspan属性的单元格
			return getHeadInf($(rowObj).prev("tr")[0]);
		}
	}
</script>

<input type="hidden" name="array_item_id" value="${arrayItemId!}"/>		
<input type="hidden" name="divide_id" value="${divideId!}"/>		

<div class="box box-default">
	<div class="box-body">
		<ul class="nav nav-tabs nav-tabs-1 nav-tabs-table" role="tablist">
			<li class="active" id="li01" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onClick="gradeTable()">年级特征设置</a></li>
			<li class="" id="li02" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onClick="subjectTable()">课程特征设置</a></li>
			<li class="" id="li05" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onClick="classTable()">班级特征设置</a></li>
			<li class="" id="li03" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onClick="teaTable()">教师特征设置</a></li>
			<li class="" id="li04" role="presentation"><a href="javascript:void(0)" role="tab" data-toggle="tab" onClick="timeTable()">课表设置</a></li>
		</ul>
		<div class="tab-content" id="gradeTableList">
	</div>
</div>
