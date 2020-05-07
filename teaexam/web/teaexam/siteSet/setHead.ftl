<input type="hidden" id="canEdit" value="${canEdit!}">
<div class="box box-default">
	<div class="box-body clearfix">
        <div class="tab-container">
			<div class="tab-header clearfix">
				<ul class="nav nav-tabs nav-tabs-1">
				 	<li <#if type?default('1')=='1'>class="active"</#if> id="roomLi">
				 		<a data-toggle="tab" href="javascript:;" onclick="toRoom();">考场设置</a>
				 	</li>
				 	<li <#if type?default('1')=='2'>class="active"</#if> id="noLi">
				 		<a data-toggle="tab" href="javascript:;" onclick="toNo();">考号设置</a>
				 	</li>
				 	<li <#if type?default('1')=='3'>class="active"</#if> id="stuLi">
				 		<a data-toggle="tab" href="javascript:;" onclick="toStu();">考场考生设置</a>
				 	</li>
				</ul>
			</div>
			<div class="tab-content" id="showList"></div>
		</div>
	</div>
</div>
<script>
$(function(){
showBreadBack(toExam,true,"考场设置");

<#if type?default('1')=='1'>
	toRoom();
<#elseif type?default('1')=='2'>
	toNo();
<#elseif type?default('1')=='3'>
	toStu();
</#if>	
});											


function toExam(){
	var url = '${request.contextPath}/teaexam/siteSet/index/page?year=${year!}';
	$('.model-div').load(url);
}
	
function toRoom(eid){
	$('#roomLi').addClass('active').siblings('li').removeClass('active');
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/room/page';
	$('#showList').load(url);
}

function toNo(eid){
	$('#noLi').addClass('active').siblings('li').removeClass('active');
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/cardno/page';
	$('#showList').load(url);
}

function toStu(eid){
	$('#stuLi').addClass('active').siblings('li').removeClass('active');
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/arrange/index';
	$('#showList').load(url);
}
</script>