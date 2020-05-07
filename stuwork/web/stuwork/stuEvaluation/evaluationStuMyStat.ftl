<div class="main-content" >
	<div class="main-content-inner">
		<div class="page-content">
			<div class="box box-default">
				<div class="box-body">
					<div class="filter">
						<div class="filter-item">
							<span class="filter-name">班级：</span>
			                <div class="filter-content">
			                    <select name="classId" id="classId" class="form-control" onchange="doSearch()" style="width:120px">
			                       <#if classList?exists && classList?size gt 0>
		                    		<#list classList as item>
				                        <option value="${item.id!}" <#if item.id==classId?default("")>selected="selected"</#if>>${item.classNameDynamic!}</option>
		                    		</#list>
		                    		</#if>
		                   		</select>
			                </div>
			                <input type="hidden" id="className" value="${className!}">
						</div>
					</div>
					
					<div class="table-container">
						<div class="table-container-header text-right">
							<button class="btn btn-lightgreen" onclick="doExport();">导出</button>
						</div>
						<form id="submitForm" class="print">
						<div class="table-container-body print" style="max-width:100%;overflow:auto;">
							<table class="table table-striped">
								<thead>
									<tr>
										<th style="padding-right:30px;">姓名</th>
										<#if titleList?exists && titleList?size gt 0>
											<#list titleList as item>
												<th>${item!}</th>
											</#list>
										</#if>
									</tr>
								</thead>
								<tbody>
										<#if evaList?exists && evaList?size gt 0>
											<#list evaList as item>
											<tr>
												<td>${item.studentName!}</td>
												<#if item.dtoList?exists && item.dtoList?size gt 0>
													<#list item.dtoList as dto>
														<td>${dto.optionName!}</td>
													</#list>
												</#if>
											</tr>
											</#list>
										</#if>
								</tbody>
							</table>
						</div>
						</form>
					</div>
				</div>
			</div>
		</div><!-- /.page-content -->
	</div>
</div><!-- /.main-content -->
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
	
	function doSearch(){
		var url="${request.contextPath}/stuwork/evaluation/stu/myStat?classId="+$("#classId").val();
		$("#itemShowDivId").load(url);
	}
	function doExport(){
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
		LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
		LODOP.SAVE_TO_FILE($("#className").val()+"学生评语汇总"+".xls");
	}
</script>
