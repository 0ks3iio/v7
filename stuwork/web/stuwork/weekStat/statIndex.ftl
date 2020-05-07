<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" onchange="changeAcadyear()">
						<#list acadyearList as ac>
							<option value="${ac}" <#if ac == acadyear>selected</#if>>${ac!}</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semester" id="semester" class="form-control" onchange="changeAcadyear()">
						<option value="1" <#if semester == 1>selected</#if>>第一学期</option>
						<option value="2" <#if semester == 2>selected</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">周次：</span>
				<div class="filter-content">
					<input type="hidden" name="classId" id="classId"/>
					<select name="week" id="week" class="form-control" onchange="onWeek()">
					<#list 1..maxWeek as i>
						<option value="${i}" <#if week == i>selected</#if>>第${i}周</option>
						</#list>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
                <div class="filter-content">
                    <a class="btn btn-white" onclick="doExport();" >导出</a>
                </div>
            </div>
		</div>
		<div id="statInfoDiv"></div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	onWeek();
});

function changeAcadyear(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/stuwork/weekStat/index/page?acadyear='+acadyear+'&semester='+semester;
	$(".model-div").load(url);
}

function doExport(){
	  var acadyear = $('#acadyear').val();
	  var semester = $('#semester').val();
	  var week = $('#week').val();
	  if(week != ''){
	  	var url = "${request.contextPath}/stuwork/weekStat/export?acadyear="+acadyear+"&semester="+semester+"&week="+week;
	  	window.open(url);
	  }
}

function onWeek(){
	  var acadyear = $('#acadyear').val();
	  var semester = $('#semester').val();
	  var week = $('#week').val();
	  if(week != ''){
	  	$("#statInfoDiv").load("${request.contextPath}/stuwork/weekStat/list/page?acadyear="+acadyear+"&semester="+semester+"&week="+week);
	  }
}
</script>