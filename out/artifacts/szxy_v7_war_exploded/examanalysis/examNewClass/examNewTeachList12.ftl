<#if clslist?exists && clslist?size gt 0 && linelist?exists && linelist?size gt 0>
<#if infoList?exists && infoList?size gt 0>
<div class="explain">
	<p>
	注：
	<#list infoList as info>
	参照${info_index+1}（${info.examName!}）&nbsp&nbsp
	</#list>
	</p>
</div>
</#if>
<div class="table-container">
	<div class="table-container-body" style="overflow-x: auto;">
		<form class="print">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th class="text-center" colspan="${(infoList?size+1)*linelist?size+1}">${title!}</th>
				</tr>
				<tr>
					<th rowspan="2">班级</th>
					<#list linelist as line>
					<th colspan="${infoList?size+1}">${line}</th>
					</#list>
				</tr>
				<tr>
				<#list linelist as line>
					<td>本次上线人数</td>
					<#if infoList?exists && infoList?size gt 0>
					<#list infoList as info>
						<td>参照${info_index+1}上线人数</td>
					</#list>
					</#if>
				</#list>
				</tr>
			</thead>
			<tbody>
				<#list clslist as cls>
	 				<tr>
					    <td>${cls.className}</td>
					    <#list linelist as line>
					    	<#if dtoMap1[line+'_'+cls.id]?exists>
								<td>${dtoMap1[line+'_'+cls.id].scoreNum!}</td>
							<#else>
								<td>0</td>
							</#if>
							<#if infoList?exists && infoList?size gt 0>
								<#list infoList as info>
									<#if dtoMap2[info.id+'_'+cls.id+'_'+line]?exists>
										<td>${dtoMap2[info.id+'_'+cls.id+'_'+line].scoreNum!}</td>
									<#else>
										<td>-</td>
									</#if>
								</#list>
							</#if>
						</#list>
					</tr>
				</#list>
			</tbody>
		</table>
		</form>
	</div>
</div>
<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
</#if>
<script>
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	var acadyear = $('#acadyear').val();
	LODOP.SAVE_TO_FILE('${title!}'+".xls");
}
</script>