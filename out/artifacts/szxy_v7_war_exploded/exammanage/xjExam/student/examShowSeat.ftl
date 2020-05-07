<div class="main-content">
	<div class="main-content-inner">
		<div class="page-content">
			<div class="box box-default">
				<div class="box-header">
					<h4 class="box-title">考试通知单</h4>
				</div>
						<div class="box-body">
							<div class="filter">
								<div class="filter-item filter-item-right">
									<button class="btn btn-white" onclick="doExport()">打印</button>
								</div>
							</div>
							<div class="row stuSeat">
								<div class="col-sm-6">
									<div class="exam-notice-paper">
										<h2 class="exam-notice-paper-title">${title!}通知单</h2>
										<ul class="exam-notice-paper-list">
											<li>考生姓名：${seatsList.stuName!}</li>
											<li>准考证号：${seatsList.admission!}</li>
											<li>年级：${seatsList.gradeName!}</li>
											<li>班级：${seatsList.className!}</li>
										</ul>
										<h4 class="exam-notice-paper-warning">注意事项</h4>
										<#if careful?exists && careful?size gt 0>
										   <#list careful as care>
										     <p>${care!}</p>
										   </#list>
										</#if>
									</div>
								</div>
								<div class="col-sm-6">
									<table class="table table-bordered table-striped text-center">
										<thead>
											<tr>
												<th class="text-center">考试科目</th>
												<th class="text-center">考试地点</th>
												<th class="text-center">座位号</th>
												<th class="text-center">考试时间</th>
											</tr>
										</thead>
										<tbody>
										    <#if seatsList.listess?exists && seatsList.listess?size gt 0>
									          	<#list seatsList.listess as exam>
										          	<tr>
														<td>${exam.examName!}</td>
														<td>${exam.examPlacd!}</td>
														<td>${exam.examStuNum!}</td>
														<td>${exam.examTime!}</td>
												    </tr>
									      	    </#list>
									  	    <#else>
												<tr>
													<td  colspan="88" align="center">
													暂无数据
													</td>
												<tr>
									        </#if>
										</tbody>
									</table>
									<p class="text-right">学军中学考试中心</p>
								</div>
							</div>
						</div>
			</div>
		</div><!-- /.page-content -->
	</div>
</div><!-- /.main-content -->


<script>

	function doExport(){
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		if (LODOP==undefined || LODOP==null) {
			return;
		}
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
		LODOP.ADD_PRINT_HTM("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".stuSeat")));
		LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//横向打印
		LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
		LODOP.PREVIEW();//打印预览
	}
		

</script>