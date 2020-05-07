<div class="filter">
	<div class="filter-item">
		<a class="btn btn-white" href="javascript:;" onclick="backSite();">返回</a>
		<a class="btn btn-blue" href="javascript:;" onclick="changeAll();">更换考场</a>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name">考生数：${(infos?size)?default(0)}</span>
	</div>
	<div class="filter-item filter-item-right">
	<#if rooms?exists && rooms?size gt 0>
		<span class="filter-name">考场：</span>
		<select name="roomNo" id="roomNo" class="form-control" onchange="searchList();">
			<#list rooms as room>
			<option value="${room.roomNo!}" id="${room.roomNo!}_room" school="${room.schoolId!}" <#if roomNo?default('')==room.roomNo!>selected</#if>>${room.roomNo!}</option>
			</#list>
		</select>
	</#if>
	</div>
	<div class="filter-item filter-item-right">
		<span class="filter-name subject-span">科目：${(infos?size)?default(0)}</span>
	</div>
</div>
<#if infos?exists && infos?size gt 0>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th>
						<label class="inline">
							<input class="wp check-all" type="checkbox">
							<span class="lbl">&nbsp;&nbsp;&nbsp;全选</span>
						</label>
					</th>
					<th>座位号</th>
					<th>考生</th>
					<th>考号</th>
					<th>身份证号</th>
					<th>所在单位</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<#list infos as info>
				<tr>
					<td>
			    	<label class="inline">
						<input class="wp check-info" type="checkbox" value="${info.id!}">
						<span class="lbl">&nbsp;&nbsp;&nbsp;${info_index+1}</span>
					</label>
			    	</td>
					<td>${info.seatNo!}</td>
					<td>${info.teacherName!}</td>
					<td>${info.cardNo!}</td>
					<td>${info.identityCard!}</td>
					<td>${info.schName!}</td>
				    <td><a class="color-blue mr10" href="javascript:;" onclick="changeRoom('${info.id!}');">更换考场</a></td>
				</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
<#else>
<div class="no-data-container">
	<div class="no-data">
		<span class="no-data-img">
			<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
		</span>
		<div class="no-data-body">
			<p class="no-data-txt">没有相关数据</p>
		</div>
	</div>
</div>
</#if>
<script>
$('.subject-span').text('科目：'+$('#${subInfoId!}_option').text());

$('.check-all').on('click',function(){
	var chk = $(this).is(':checked'); 
	$('.check-info').each(function(){
		if(chk){
			$(this).prop("checked","checked") ;
		} else {
			$(this).removeAttr('checked');
		}
	});
});

function searchList(){
	var rm = $('#roomNo').val();
	teaList(rm);
}

function backSite(){
	$('.site-filter').show();
	arrangeList();
	//var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/arrange/index?subInfoId=${subInfoId!}';
	//$('#showList').load(url);
}

function changeRoom(tid){
	var orm = $('#roomNo').val();
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/changeRmList?subInfoId=${subInfoId!}&teaIds='+tid+"&oldRoomNo="+orm;
	indexDiv = layerDivUrl(url,{title: "更换考场",width:750,height:650});
}

function changeAll(){
	var tids = '';
	$('input.check-info:checked').each(function(){
		if(tids != ''){
			tids=tids+',';
		}
		tids=tids+$(this).val();
	});
	if(tids==''){
		layerTipMsg(false,"提示","没有选择要更换考场的考生");
		return false;
	}
	var orm = $('#roomNo').val();
	var url = '${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/changeRmList?subInfoId=${subInfoId!}&teaIds='+tids+"&oldRoomNo="+orm;
	indexDiv = layerDivUrl(url,{title: "更换考场",width:750,height:650});
}
</script>