<div class="layer-content" style="overflow-y:auto;height:520px;">
<form method="post" id="setForm" name="setForm">
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name">科目：</span>
			<div class="filter-content" style="word-break:break-all;">${sub.subjectName!}</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">学段：</span>
			<div class="filter-content">${mcodeSetting.getMcode("DM-XD",sub.section?string)}</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">已通过审核人数：</span>
			<div class="filter-content">${allCount?default(0)}<span class="color-red">（已分配${hasCount?default(0)}人，待分配${noCount?default(0)}人）</span></div>
		</div>
	</div>
	<div class="filter">
		<div class="filter-item">
			<span class="filter-name text-blue">说明：没有勾选或者分配考场数维护为0，则科目将不使用该考点作为考场</span>
		</div>
	</div>
	<table class="table table-bordered table-striped table-hover no-margin">
		<thead>
			<tr>
				<th>
					<label class="inline">
						<input class="wp all-site" type="checkbox" >
						<span class="lbl">&nbsp;&nbsp;&nbsp;序号</span>
					</label>
				</th>
				<th>考点名称</th>
				<th>考点可容纳人数</th>
				<th>考场总数</th>
				<th>可用数量</th>
				<th>分配考场数</th>
			</tr>
		</thead>
		<tbody>
			<#if sites?exists && sites?size gt 0>
			<#list sites as site>
			<tr>
			    <td>
			    	<label class="inline">
						<input class="wp check-site" type="checkbox" name="sites[${site_index}].schoolId" <#if site.setNum?default(0) gt 0>checked</#if> value="${site.schoolId!}">
						<span class="lbl">&nbsp;&nbsp;&nbsp;${site_index+1}</span>
					</label>
			    </td>
			    <td id="${site.schoolId!}_siteName">${site.siteName!}</td>
			    <td>${site.siteNum*site.capacity}</td>
			    <td>${site.siteNum}</td>
			    <td>${site.validNum}</td>
			    <td id="${site.schoolId!}_td"><input type="text" name="sites[${site_index}].roomNum" id="${site.schoolId!}_num" size="3" min="${site.usedNum?default(0)}" max="${site.validNum?default(0)}" <#if site.setNum?default(0) gt 0>class="table-input"<#else>disabled class="table-input"</#if> value="${site.setNum?default(0)}"></td>
			</tr>
			</#list>
			<#else>
			<tr>
				<td colspan="6">
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
				</td>
			</tr>
			</#if>
		</tbody>
	</table>
</form>
</div>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-lightblue" id="commitBtn">确定</a>
	<a href="javascript:" class="btn btn-grey" id="closeBtn">取消</a>
</div>
<script>
//$('#layerTable').height($('.layui-layer-content').height()-$('.layer-footer').height());

$('#closeBtn').on("click", function(){
    doLayerOk("#commitBtn", {
    	redirect:function(){},
    	window:function(){layer.closeAll()}
    });     
 });

$('.all-site').on('click',function(){
	var chk = $(this).is(':checked'); 
	$('.check-site').each(function(){
		var sid=$(this).val();
		if(chk){
			$(this).prop("checked","checked") ;
			$('#'+sid+'_num').removeAttr('disabled');
		} else {
			$(this).removeAttr('checked');
			$('#'+sid+'_num').attr('disabled','disabled');
		}
	});
});

$('.check-site').on('click',function(){
	var sid=$(this).val();
	if($(this).is(':checked')){
		$('#'+sid+'_num').removeAttr('disabled');
	} else {
		$('#'+sid+'_num').attr('disabled','disabled');
	}
});

var isSubmit = false;
$('#commitBtn').on("click", function(){
	
	if(isSubmit){
		return;
	}
	isSubmit = true;
	var flag = true;
	var i=0;
	$("input.check-site:checked").each(function() {
		i=i+1;
		var sid = $(this).val();
		var vname = '考点'+$('#'+sid+'_siteName').text()+'的分配数';
		var vnum = $('#'+sid+'_num').val();
		var minv = parseInt($('#'+sid+'_num').attr('min'));
		var maxv = parseInt($('#'+sid+'_num').attr('max'));
		if(maxv>999){
			maxv=999;
		}
		if (!/^\d+$/.test(vnum)) {
			flag = false;
			layer.tips("请输入"+minv+"-"+maxv+"的整数", "#"+sid+"_td", {
				tipsMore: true,
				tips:3		
			});
			return;
		}
		var num = parseInt(vnum);
		if(num<minv || num>maxv){
			flag = false;
			layer.tips("请输入"+minv+"-"+maxv+"的整数", "#"+sid+"_td", {
				tipsMore: true,
				tips:3		
			});
			return;
		}
	});
	if(!flag){
		isSubmit = false;
		return;
	}
	
	var options = {
		url : "${request.contextPath}/teaexam/siteSet/setIndex/${examId!}/room/save?subInfoId=${subInfoId!}",
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
				toRoom('${examId!}');
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#setForm").ajaxSubmit(options);
}); 
</script>