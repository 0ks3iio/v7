<div class="box box-default">
		<div class="box-body">
			<div class="filter-container">
				<div class="filter">
					<div class="filter-item">
						<span class="filter-name">学年：</span>
						<div class="filter-content">
							<select name="searchAcadyear" id="searchAcadyear" class="form-control" onChange="showRankList()">
									<#if acadyearList?exists && (acadyearList?size>0)>
					                    <#list acadyearList as item>
						                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
					                    </#list>
				                    </#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">学期：</span>
						<div class="filter-content">
							<select id="searchSemester" name="searchSemester" class="form-control" onChange="showRankList()">
									${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
							</select>
						</div>
					</div>
					<div class="filter-item">
						<span class="filter-name">学段：</span>
						<div class="filter-content">
							<select name="section" id="section" class="form-control" onChange="showRankList()">
							<#if tis?exists && (tis?size>0)>
							    <#list tis as i>     
								 <option value="${i}">${mcodeSetting.getMcode('DM-RKXD',i)}</option>
								</#list>
							</#if>
							</select>
						</div>
					</div>
					
					<button class="btn btn-white" id="rankExport"  onclick="doExport();">导出</button>
				</div>
			</div>
		</div>
	</div>
	<div class="table-container">
	
	</div>
</div><!-- /.model-div -->
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
var isShow = false;
$(function(){
     $('#rankExport').hide(); 
     showRankList();
});
  
function showRankList(){
   isShow = false;
   var acadyear = $('#searchAcadyear').val();
   var semester = $('#searchSemester').val();
   var section = $('#section').val();
   var url =  '${request.contextPath}/stuwork/check/ranking/list/page?acadyear='+acadyear+'&semester='+semester+'&section='+section;
   $(".table-container").load(url);
   if( !isShow){
       $('#rankExport').hide(); 
   }
  }

//导出
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	if (LODOP==undefined || LODOP==null) {
		return;
	 }
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".rankList")));
	LODOP.SAVE_TO_FILE("考核排名汇总表"+getNowFormatDate()+".xls");
}
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = "-";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + "," + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}
</script>


