<div class="box box-default">
		<div class="box-header">
			<h3 class="box-title">${className!}</h3>
		</div>
		<div class="box-body">

			<div class="stat">
				<div class="stat-item">
					<div class="stat-item-content">
						<strong>${allCheck.allScore?string("0.#")}</strong>
						<p>小计</p>
					</div>
				</div>
				<div class="stat-item">
					<div class="stat-item-content">
						<strong>${allCheck.allRank!}</strong>
						<p>排名</p>
					</div>
				</div>
			</div>
			<div class="table-container">
				<div class="table-container-header text-right">
					<button class="btn btn-white" onclick="doExport();">导出</button>
				</div>
				<form  class="print">
				<div class="table-container-body">
					<table class="table table-striped">
						<tbody>
							<tr><td class="text-center" style="width:30%;">卫生总得分</td><td style="width:70%;" class="text-center">${allCheck.healthScore?string("0.#")}</td></tr>
							<tr><td class="text-center">卫生奖励</td><td class="text-center">${allCheck.healthExcellentScore?string("0.#")}</td></tr>
							<tr><td class="text-center">纪律总得分</td><td class="text-center">${allCheck.disciplineScore?string("0.#")}</td></tr>
							<tr><td class="text-center">纪律奖励</td><td class="text-center">${allCheck.disExcellentScore?string("0.#")}</td></tr>
							<tr><td class="text-center">寝室得分</td><td class="text-center">${allCheck.dormScore?string("0.#")}</td></tr>
							<tr><td class="text-center">文明寝室奖励</td><td class="text-center">${allCheck.dormExcellentScore?string("0.#")}</td></tr>
							<tr><td class="text-center">学生个人扣分</td><td class="text-center"><#if allCheck.studentDecScore?default(0) gt 0>-</#if>${allCheck.studentDecScore?string("0.#")}</td></tr>
							<#if otherCheckList?exists && otherCheckList?size gt 0>
								<#list otherCheckList as item>
								<tr>
									<#if item_index==0><td style="width:100px;" class="text-center" rowspan="${otherCheckList?size}">班级其他考核</td></#if>
									<td style="word-wrap:break-word; word-break:break-all;" class="text-center">考核分:${item.score?string("0.#")};考核内容:${item.remark!}</td>
								</tr>
								</#list>
							<#else>
								<tr>
									<td class="text-center">班级其他考核</td>
									<td class="text-center"></td>
								</tr>
							</#if>
						</tbody>
					</table>
				</div>
				</form>
			</div>
			<input type="hidden" id="acadyear" value="${dormDto.acadyear!}">		
			<input type="hidden" id="semesterStr" value="${dormDto.semesterStr!}">		
			<input type="hidden" id="week" value="${dormDto.week!}">		
			<input type="hidden" id="className" value="${className!}">		
		</div>
	</div>
	<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
	function statDetail(id){
		var url="${request.contextPath}/stuwork/dorm/stat/statDetail?id="+id;
		indexDiv = layerDivUrl(url,{title: "考核明细",width:600,height:350});
	}
	function doExport(){
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
		LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
		LODOP.SAVE_TO_FILE(getConditions()+"寝室考核汇总"+".xls");
	}
	function getConditions(){
		var acadyear=$("#acadyear").val();
		var semesterStr=$("#semesterStr").val();
		var week=$("#week").val();
		var className=$("#className").val();
		if(semesterStr=="1"){
			semesterStr="一";
		}else if(semesterStr=="2"){
			semesterStr="二";
		}
		return acadyear+"学年第"+semesterStr+"学期第"+week+"周"+className;
	}
</script>