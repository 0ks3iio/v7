<a href="javascript:;" onclick="black();" class="page-back-btn"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">问卷预览</h4>
	</div>
	<div class="box-body">
	<#-- 
		<div class="explain">
			<h4>亲爱的同学：</h4>
			<p>一、本调查中班主任是指现任班主任；</p>
			<p>二、每位同学应本着负责任的态度，独立、认真完成</p>
		</div> -->
		<div class="table-container">
			<div class="table-container-header text-right">
				<a class="btn btn-sm btn-white" onClick="doExport();">导出</a>
				<a class="btn btn-sm btn-white" onClick="doPrint();">打印</a>
			</div>
			<div class="table-container-body" id="print">
				<table class="table table-bordered table-striped">
					<tbody>
						<#assign i = 1>
						<#if dto.evaluaList?exists && dto.evaluaList?size gt 0>
							<tr>
								<th>序号</th>
								<th width="50%">名称</th>
								<th>类型</th>
								<th>选项</th>
							</tr>
							<#list dto.evaluaList as item>
								<tr>
									<td>${i}</td>
									<td style="word-break:break-all;">${item.itemName}</td>
									<td>
										<#if item.showType == 'O'>
										单选
										<#elseif item.showType == 'M'>
										多选
										</#if>
									</td>
									<td style="word-break:break-all;">
										<#if item.showType == 'O'>
											<#list item.optionList as option>
											<p><label><input type="radio" name="${item.id!}" class="wp"><span class="lbl">${option.optionName!}</span></label></p>
											</#list>
										<#elseif item.showType == 'M'>
											<#list item.optionList as option>
											<p><label><input type="checkbox" name="${item.id!}" class="wp"><span class="lbl">${option.optionName!}</span></label></p>
											</#list>
										</#if>
									</td>
								</tr>
								<#assign i = i+1>
							</#list>
						</#if>
						<#assign i = 1>
						<#if dto.fillList?exists && dto.fillList?size gt 0>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<tr>
							<th>序号</th>
							<th width="50%">名称</th>
							<th>类型</th>
							<th>选项</th>
						</tr>
						<#list dto.fillList as item>
							<tr>
								<td>${i}</td>
								<td style="word-break:break-all;">${item.itemName}</td>
								<td>
									<#if item.showType == 'O'>
									单选
									<#elseif item.showType == 'M'>
									多选
									</#if>
								</td>
								<td style="word-break:break-all;">
									<#if item.showType == 'O'>
										<#list item.optionList as option>
										<p><label><input type="radio" name="${item.id!}" class="wp"><span class="lbl">${option.optionName!}</span></label></p>
										</#list>
									<#elseif item.showType == 'M'>
										<#list item.optionList as option>
										<p><label><input type="checkbox" name="${item.id!}" class="wp"><span class="lbl">${option.optionName!}</span></label></p>
										</#list>
									</#if>
								</td>
							</tr>
							<#assign i = i+1>
						</#list>
						</#if>
						<#assign i = 1>
						<#if dto.answerList?exists && dto.answerList?size gt 0>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						<#list dto.answerList as item>
						<tr>
							<td>${i}</td>
							<td width="50%" style="word-break:break-all;">${item.itemName!}</td>
							<td colspan="2" style="word-break:break-all;">
								<textarea name="${item.id!}" rows="5" class="form-control"></textarea>
							</td>
						</tr>
						<#assign i = i+1>
						</#list>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
function black(){
	var url =  '${request.contextPath}/evaluate/project/index/page?acadyear=${acadyear!}&semester=${semester!}';
	$(".model-div").load(url);
}

function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($("#print")));
	LODOP.SAVE_TO_FILE("问卷"+".xls");
}

function doPrint(){
    var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($("#print")));
	LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//横向打印
    LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
	LODOP.PREVIEW();//打印预览
}
</script>