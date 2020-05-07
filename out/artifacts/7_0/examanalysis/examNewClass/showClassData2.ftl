<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<#if spaceList?exists && spaceList?size gt 0>
<#if showType?default("0")!="1">
<div class="table-container">
	<div class="table-container-body" style="overflow-x: auto;">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
				<#if clazzList?exists && clazzList?size gt 0>
					<th class="text-center" colspan="${clazzList?size + 2}">${title!}</th>
				<#else>
					<th class="text-center" colspan="">${title!}</th>
				</#if>
				</tr>
				<tr>
					<th><#if type?default("")=="21">名次段<#else>分数段</#if></th>
					<#if clazzList?exists && clazzList?size gt 0>
						<#list clazzList as cls>
							<th>${cls.classNameDynamic!}</th>
						</#list>
					</#if>			
					<th>年级全体</th>
				</tr>
			</thead>
			<tbody>
				<#if spaceList?exists && spaceList?size gt 0>
					<#list spaceList as item>
		 				<tr>
						    <td>${item.name!}</td>
						    <#if clazzList?exists && clazzList?size gt 0>
								<#list clazzList as cls>
									<td><#if emStatMap[cls.id+item.id]?exists>${emStatMap[cls.id+item.id]?default('0')}<#else>0</#if></td>
								</#list>
							</#if>
						    <td><#if emStatMap[item.id]?exists>${emStatMap[item.id]?default('0')}<#else>0</#if></td>
						</tr>
					</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>
<#else>
<div class="filter">
	<div class="filter-item">
		<label><input type="radio" name="conType"  onclick="changeScore('1')" checked=""class="wp"><span class="lbl">各班间对比</span></label>
	</div>
	<div class="filter-item">
		<label><input type="radio" name="conType" onclick="changeScore('2')"  class="wp"><span class="lbl">年级全局</span></label>
	</div>
</div>
<@chartstructure.histogram loadingDivId="mychart02" isShowLegend=false isShowDataLabel=false divStyle="height:550px;" jsonStringData=jsonStringData2 />
<@chartstructure.histogram isStack=true isShowDataLabel=false loadingDivId="mychart01" divStyle="height:550px;" jsonStringData=jsonStringData1 />
<script type="text/javascript">
	function changeScore(conType){
		if(conType &&conType=="2"){
			$("#mychart02").show();
			$("#mychart01").hide();
		}else{
			$("#mychart01").show();
			$("#mychart02").hide();
		}
	}
	$("#mychart02").hide();
</script>
</#if>
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
	LODOP.SAVE_TO_FILE("${title!}.xls");
}
</script>