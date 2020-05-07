<#import "/fw/macro/chartstructure.ftl" as chartstructure />
<#if examList?exists && examList?size gt 0>
	<#if type?default("1")=="2">
		<div>
			<h4><b>历次考试对比分析</b><span class="font-12 color-999">（建议参照标准分T为准）</span></h4>
			<div class="filter">
				<div class="filter-item">
					<label><input type="radio" name="conType"  onclick="changeScore('1')" <#if conType?default("")=='1'>checked=""</#if> class="wp"><span class="lbl"> 考试分</span></label>
				</div>
				<div class="filter-item">
					<label><input type="radio" name="conType" onclick="changeScore('2')"  <#if conType?default("")=='2'>checked=""</#if>class="wp"><span class="lbl"> 赋分</span></label>
				</div>
				<div class="filter-item">
					<label><input type="radio" name="conType" onclick="changeScore('3')"  <#if conType?default("")=='3'>checked=""</#if>class="wp"><span class="lbl"> 标准分T(年级)</span></label>
				</div>
			</div>
			<#--<div id="mychart02" style="height:400px;"></div>-->
			<@chartstructure.histogram loadingDivId="mychart02"  isShowDataLabel=false isLine=true divStyle="height:550px;" jsonStringData=jsonStringData />
		</div>
	<#else>
		<div id="myTableDivId">
			<div class="table-container">
				<div class="table-container-body" style="overflow-x: auto;">
					<form class="print">
					<table class="table table-bordered table-striped table-hover">
						<thead>
							<tr>
								<th>学科</th>
								<th>维度</th>
								<#list examList as item>
									<th>${item.examName!}</th>
								</#list>
							</tr>
						</thead>
						<tbody>
							<#if subjectDtoList?exists && subjectDtoList?size gt 0>
							<#list subjectDtoList as subjectDto>
				 				<tr>
								    <th rowspan="3">${subjectDto.subjectName!}</th>
								    <th>成绩</th>
								    <#list examList as item>
								    	<td><#if statMap[item.id+subjectDto.subjectId]?exists>${(statMap[item.id+subjectDto.subjectId].subjectScore)?default(0)}<#else>-</#if></td>
								    </#list>
								</tr>
								<tr>
								    <th>班级排名</th>
								    <#list examList as item>
								    	<td><#if statMap[item.id+subjectDto.subjectId]?exists>${(statMap[item.id+subjectDto.subjectId].classRank)?default(0)}<#else>-</#if></td>
								    </#list>
								</tr>
								<tr>
								    <th>年级排名</th>
								    <#list examList as item>
								    	<td><#if statMap[item.id+subjectDto.subjectId]?exists>${(statMap[item.id+subjectDto.subjectId].gradeRank)?default(0)}<#else>-</#if></td>
								    </#list>
								</tr>
							</#list>
							</#if>
						</tbody>
					</table>
					</form>
				</div>
			</div>
		</div>
	</#if>
	<script type="text/javascript">
		function changeScore(conType){
			var studentId =$('#studentId').val();
		    var acadyear = $('#acadyear').val();
		    var semester = $('#semester').val();
		    var type = $('#type').val();
		    var showAll='';
		    if($("#showAll").attr("checked")){
		    	showAll='1';
		    }
	  	    $("#showListDiv").load("${request.contextPath}/examanalysis/examNewStudent/List/page?acadyear="+acadyear+"&semester="+semester+"&studentId="+studentId+"&type="+type+"&conType="+conType+"&showAll="+showAll);
		}
		function doExport(){
			var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
			//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
			LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
			var acadyear = $('#acadyear').val();
		    var semester = $('#semester').val();
		    var studentName = $('#studentName').val();
			if(semester=="1"){
				semester="一";
			}else if(semester=="2"){
				semester="二";
			}
			LODOP.SAVE_TO_FILE(acadyear+"学年第"+semester+"学期"+studentName+"个人成绩分析报表"+".xls");
		}
	</script>
	<#if type?default("1")=="2">
	<script type="text/javascript">
	    // 基于准备好的dom，初始化echarts实例
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