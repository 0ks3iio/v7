<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<table class="table table-bordered table-striped table-hover" id="showtable${viewType!}">

	<tbody>
	<#if examInfoList?exists&& (examInfoList?size > 0)>
        <thead>
        <tr>
            <th class="text-center">考试编号</th>
            <th class="text-center">考试名称</th>
            <th class="text-center">考试类型</th>
            <th class="text-center">统考类型</th>
            <th class="text-center">年级</th>
            <th class="text-center">起始时间</th>
            <th class="text-center">截止时间</th>
            <th class="text-center">科目设置</th>
            <th class="text-center">操作</th>
        </tr>
        </thead>
	    <#list examInfoList as item>
		<tr>
			<td class="text-center">${item.examCode!}</td>
			<td class="text-center">${item.examName!}</td>
			<td class="text-center">${mcodeSetting.getMcode("DM-KSLB", item.examType?default(1)?string)}</td>
			
			<td class="text-center">${item.examUeTypeName!}</td>
			<td class="text-center">${item.gradeCodeName!}</td>
			<td class="text-center">${(item.examStartDate?string('yyyy-MM-dd'))!}</td>
			<td class="text-center">${(item.examEndDate?string('yyyy-MM-dd'))!}</td>
			<td class="text-center">
			<#if item.examUeType?default('')=='4'>
				<#if unitClass?default(-1) == 1>
					<a href="javascript:doSetSubject('${item.id!}','0');" class="color-blue">去设置</a>
				<#else>
					<a href="javascript:doSetSubject('${item.id!}','1');" class="color-blue">去查看</a>
				</#if>
			<#else>
				<#if item.isEdit?default('')=='1'>
					<a href="javascript:doSetSubject('${item.id!}','1');" class="color-blue">去查看</a>
				<#else>
					<a href="javascript:doSetSubject('${item.id!}','0');" class="color-blue">去设置</a>
				</#if>
			</#if>
			</td>
			<td class="text-center">
				<#if unitId==item.unitId>
				<a href="javascript:doEdit('${item.id!}','${item.examUeType!}');" class="color-blue">编辑</a>
				<#--<a href="javascript:;" id="${item.id!}" class="color-blue delete pos-rel">删除</a>
				<a href="javascript:doDelete('${item.id!}');" class="color-blue">删除</a>-->
				<a id="del${item.id}" class="color-red delete pos-rel" href="javascript:;" onclick="onDel(this,'${item.id}')">删除
				<div class="modify-name-layer">
					<p class="clearfix mt20">
						<span class="fa fa-exclamation-circle color-yellow font-30 pull-left"></span>
						<span class="pull-left mt6">确定要删除吗?</span>
					</p>
					<div class="text-right">
						<button class="btn btn-sm btn-white" onclick="closeDel()" >取消</button>
						<button class="btn btn-sm btn-blue ml10" onclick="subDel()" >确定</button>
					</div>
				</div>
				</a>
				</#if>
			</td>
		</tr>
		</#list>
	<#else >
        <div class="no-data-container">
            <div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
                <div class="no-data-body">
                    <p class="no-data-txt">暂无相关数据</p>
                </div>
            </div>
        </div>
	</#if>												
	</tbody>
</table>	

<script type="text/javascript">
<#if viewType=="1">
function gobackIndex(){
    var url='${request.contextPath}/exammanage/examInfo/index/page';
    $(".model-div").load(url);
}

</#if>
$(function(){
	$(document).click(function(e){
		if(!$(e.target).hasClass("delete") && !$(e.target).parent().hasClass("delete") && !$(e.target).parents().hasClass("delete")){
			$(".modify-name-layer").hide();//点页面其他地方 隐藏表情区
		}
	});
})
<#-- $(function(){
	$(document).click(function(e){
		if(!$(e.target).hasClass("delete") && !$(e.target).parent().hasClass("delete") && !$(e.target).parents().hasClass("delete")){
			$(".modify-name-layer").hide();//点页面其他地方 隐藏表情区
		}
	});
	
	$(".delete").each(function(){
		var deleteLayer = '<div class="modify-name-layer">\
						<p class="clearfix mt20"><span class="fa fa-exclamation-circle color-yellow font-30 pull-left"></span><span class="pull-left mt6">确定要删除吗?</span></p>\
						<div class="text-right"><button class="btn btn-sm btn-white noDelete">取消</button><button class="btn btn-sm btn-blue ml10">确定</button></div>\
					</div>';
		$(this).append(deleteLayer);
	});
	$(".noDelete").click(function(e){
		$(".modify-name-layer").hide();
	})
	$(".delete").click(function(e){
		$(".modify-name-layer").hide();
		e.preventDefault();
		$(this).children(".modify-name-layer").show();
	});
})-->
</script>
