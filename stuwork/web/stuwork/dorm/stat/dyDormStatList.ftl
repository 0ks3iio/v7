<div class="box box-default">
		<div class="box-header">
			<h3 class="box-title">${className!}</h3>
		</div>
		<div class="box-body">

			<div class="stat">
				<div class="stat-item">
					<div class="stat-item-content">
						<strong>${scoreAverage?string('0.#')!}</strong>
						<p>平均得分</p>
					</div>
				</div>
				<div class="stat-item">
					<div class="stat-item-content">
						<strong>${rewardAverage?string('0.#')!}</strong>
						<p>平均奖励得分</p>
					</div>
				</div>
			</div>
			<div class="table-container">
				<div class="table-container-header text-right">
					<button class="btn btn-white" onclick="doExport();">导出</button>
				</div>
				<div class="table-container-body">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>寝室</th>
								<th>总得分</th>
								<th>文明寝室</th>
								<th>奖励</th>
								<th class="noprint">操作</th>
							</tr>
						</thead>
						<tbody>
							<#if statList?exists && statList?size gt 0>
								<#list statList as item>
								<tr>
									<td>${item.roomName!}</td>
									<td>${(item.scoreALL?default(0))?string('0.#')!}</td>
									<td><#if item.isExcellent?default(false)><i class="fa fa-star color-red"></#if></i></td>
									<td>${(item.excellentScore)?default(0)?string('0.#')}</td>
									<td><a href="javascript:" class="noprint" onclick="statDetail('${item.id!}')">查看明细</a></td>
								</tr>
								</#list>
							</#if>
						</tbody>
					</table>
				</div>
				<form  class="print">
					<div class="table-container-body" style="display:none;">
					<table class="table table-striped">
						<thead>
							<tr>
								<th>寝室</th>
								<th>总得分</th>
								<th >文明寝室</th>
								<th>奖励</th>
								<th class="noprint">操作</th>
							</tr>
						</thead>
						<tbody>
							<#if statList?exists && statList?size gt 0>
								<#list statList as item>
								<tr>
									<td>${item.roomName!}</td>
									<td>${(item.scoreALL?default(0))?string('0.#')!}</td>
									<td class='text-center'><#if item.isExcellent?default(false)><font color="red">★</font></#if></i></td>
									<td>${(item.excellentScore)?default(0)?string('0.#')}</td>
									<td><a href="javascript:" class="noprint" onclick="statDetail('${item.id!}')">查看明细</a></td>
								</tr>
								</#list>
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