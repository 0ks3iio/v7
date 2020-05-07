<div class="page-sidebar page-sidebar-compare">
	<div class="page-sidebar-header">
		<select name="" class="form-control" id="gradeCode"  onchange="clickGrade();">
			<#list gradeCodeList as item>
		     <option value="${item[0]!}">${item[1]!}</option>
		     </#list>
		</select>
	</div>
	<div class="page-sidebar-operation">
		<a href="javascript:" onclick="findNoReport();">未上报的学校</a>
	</div>
	<#if type?default('')=='1'>
	<div class="page-sidebar-footer">
		<ul class="chosen-compare-list" style="display: none;">

		</ul>
		<a href="javascript:" class="chosen-compare-btn" disabled onclick="compareResult();">开始对比<span style="display: none;">（2）</span></a>
	</div>
	</#if>
	<div class="page-sidebar-body" id="unitTree">
		
	</div>
</div>

<div class="page-content-inner" id="rightContent">
	

</div>
<script>
$(function(){
	$('.page-sidebar').on('click', '.chosen-tree-item .arrow', function(e){
		e.stopPropagation();
		var $li = $(this).parent('.chosen-tree-item').parent('li');
		if ($li.hasClass('sub-tree')) {
			$li.toggleClass('open');
		}
	});
	//选中树
	$('.page-sidebar').on('click', '.chosen-tree-item', function(){
		var $li = $(this).parent('li');
		$('.chosen-tree li').removeClass('active');
		$li.addClass('active');
		var id = $(this).data('id');
		loadRightContentByUnitId(id);
	});
	
	loadUnitTree();
	loadRightContentByUnitId('${unitId!}');
})
function loadUnitTree(){
	iii = layer.load();
	var gradeYear=$("#gradeCode").val();
	$("#unitTree").load("${request.contextPath}/newgkelective/edu/unittree/page?type=${type!}&gradeYear="+gradeYear);
}
function clickGrade(){
	//去除
	if($('.chosen-compare-list li')){
		
		while(true){
			if($('.chosen-compare-list li .remove').length>0){
				$('.chosen-compare-list li .remove')[0].click();
			}else{
				break;
			}
		}
	}
	loadUnitTree();
	loadRightContentByUnitId('${unitId!}');
}

function loadRightContentByUnitId(unitId){
	var gradeYear=$("#gradeCode").val();
	$("#rightContent").load("${request.contextPath}/newgkelective/edu/loadRightContent/page?type=${type!}&unitId="+unitId+"&gradeYear="+gradeYear);
}


function findNoReport(){
	var gradeYear=$("#gradeCode").val();
	$("#rightContent").load("${request.contextPath}/newgkelective/edu/noreport/page?gradeYear="+gradeYear+"&type=${type!}");
}
<#if type?default('')=='1'>
function compareResult(){
	var len = $('.chosen-compare-list').children('li').length;
	if(len ==2){
		//加载对比
		var gradeYear=$("#gradeCode").val();
		var compareIds="";
		$('.chosen-compare-list').children('li').each(function(){
			compareIds=compareIds+","+$(this).data('id');
		})
		compareIds=compareIds.substring(1);
		$("#showDate").load("${request.contextPath}/newgkelective/edu/compareChoose/page?gradeYear="+gradeYear+"&compareIds="+compareIds);
	}
}
</#if>


</script>
