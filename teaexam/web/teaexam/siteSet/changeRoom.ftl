<div class="layer-content" style="overflow-y:auto;height:520px;">
<form method="post" id="changeForm" name="changeForm">
<input type="hidden" name="examId" value="${examId!}">
<input type="hidden" name="subjectInfoId" value="${subInfoId!}">
<input type="hidden" name="id" value="${teaIds!}">
<input type="hidden" name="locationId" id="toLocationId" value="">
<input type="hidden" name="roomNo" id="toRoomNo" value="">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">科目：</span>
			<div class="filter-content">${sub.subjectName!}</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">学段：</span>
			<div class="filter-content">${mcodeSetting.getMcode("DM-XD",sub.section?string)}</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">当前考场：</span>
			<div class="filter-content">${oldRoomNo!}</div>
		</div>
		<div class="filter-item">
			<span class="filter-name"><#if type?default('') == '1'>已容纳<#else>已选</#if>考生人数：</span>
			<div class="filter-content">${teaNum?default(0)}</div>
		</div>
	</div>
	<table class="table table-bordered table-striped table-hover no-margin" style="overflow-y:auto;">
		<thead>
			<tr>
				<th>序号</th>
				<th><#if type?default('') == '1'>合并<#else>更换</#if>至同时段考试的考场</th>
				<th>考试科目</th>
				<th>考场可容纳人数</th>
				<th>剩余可容纳人数</th>
			</tr>
		</thead>
		<tbody>
			<#if sets?exists && sets?size gt 0>
			<#list sets as set>
			<tr>
			    <td>
			    	<label class="inline">
						<input type="radio" class="wp check-rm" name="roomCheck" value="${set.roomNo!}">
						<span class="lbl">&nbsp;&nbsp;&nbsp;${set_index+1}</span>
					</label>
					<input type="hidden" id="${set.roomNo!}_school" value="${set.schoolId!}">
			    </td>
			    <td>${set.roomNo!}</td>
			    <td>${set.subName!}</td>
			    <td>${set.perNum}</td>
			    <td>${set.perNum-set.usedNum}</td>
			</tr>
			</#list>
			<#else>
			<tr>
				<td colspan="5">
					<div class="no-data-container">
						<div class="no-data">
							<span class="no-data-img">
								<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
							</span>
							<div class="no-data-body">
								<p class="no-data-txt">没有符合条件的考场</p>
							</div>
						</div>
					</div>
				</td>
			</tr>
			</#if>
		</tbody>
	</table>
</form>
</div>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="commitBtn1">确定</a>
	<a href="javascript:" class="btn btn-grey" id="closeBtn1">取消</a>
</div>
<script>
$('#closeBtn1').on("click", function(){
    doLayerOk("#commitBtn1", {
    	redirect:function(){},
    	window:function(){layer.closeAll()}
    });     
 });

$('.check-rm').on('click',function(){
	var sid=$(this).val();
	if($(this).is(':checked')){
		$('#toLocationId').val($('#'+sid+'_school').val());
		$('#toRoomNo').val(sid);
	}
	<#-- else {
		$('.check-rm').each(function(){
			if($(this).val()!=sid){
				$(this).removeAttr('checked');
			}
		});
		$('#toLocationId').val('');
		$('#toRoomNo').val('');
	}-->
});

var lett = '更换';
<#if type?default('') == '1'>
lett='合并';
</#if>
var isSubmit = false;
$('#commitBtn1').on("click", function(){
	if(isSubmit){
		return;
	}
	isSubmit = true;
	
	if($('#toRoomNo').val()==''){
		isSubmit = false;
		layerTipMsg(false,"提示","没有选择要"+lett+"到的考场");
		return false;
	}
	
	showConfirmMsg("确定要"+lett+"考场吗？","提示",function(){
		var options = {
			url : "${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/saveChangeRm",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		isSubmit = false;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			return;
		 		}else{
		 			layer.closeAll();
					layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
					<#if type?default('') == '1'>
					arrangeList();
					<#else>
					searchList();
					</#if>
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#changeForm").ajaxSubmit(options);
	}, function(index){
		isSubmit = false;
		layer.close(index);
	});
}); 
</script>