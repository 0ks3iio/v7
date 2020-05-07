<#import "/stuwork/tree/dytreemacro.ftl" as dytreemacro>
<#import  "/fw/macro/htmlcomponent.ftl" as htmlcomponent />
<link rel="stylesheet" href="${request.contextPath}/static/css/pages.css"/>  
<!-- ztree -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>

				<div class="box box-default">
					<div class="box-body">
						<div class="filter-container">
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
								
								<div class="filter-item">
									<!--数据统计-->
									<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="saveStat()">统计</a>
								</div>
									<button class="btn btn-white" onclick="doExport();">导出</button>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-3">
						<div class="box box-default">
							<div class="box-header">
								<h3 class="box-title">班级菜单</h3>
							</div>
							<#--<div class="box-body">
								<ul id="tree" class="ztree"></ul>
							</div>-->
							<!-- 树菜单开始 -->
		                    <div class="box-body">
		                        <@dytreemacro.gradeClassForSchoolInsetTree height="500" click="onTreeClick"/>
		                    </div><!-- 树菜单结束 -->
						</div>
					</div>
					<div class="col-sm-9 print"  id="statInfoDiv">
					
					</div>
				</div>
			</div><!-- /.model-div -->
			<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script type="text/javascript">
//TODO
function changeAcadyear(){
	var acadyear = $('#acadyear').val();
	var semester = $('#semester').val();
	var url =  '${request.contextPath}/stuwork/classStart/index/page?acadyear='+acadyear+'&semester='+semester;
	$(".model-div").load(url);
}
function doExport(){
	var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
	LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
	LODOP.SAVE_TO_FILE("班级考核汇总表"+getNowFormatDate()+".xls");
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
function statInfoList(){
	var url =  '${request.contextPath}/stuwork/classStart/list/page';
	$("#statInfoDiv").load(url);
}
function onTreeClick(event, treeId, treeNode, clickFlag){
  var id = treeNode.id;
  var acadyear = $('#acadyear').val();
  var semester = $('#semester').val();
  var week = $('#week').val();
  if(treeNode.type == "class"){
  	$('#classId').attr('value',id);
  	$("#statInfoDiv").load("${request.contextPath}/stuwork/classStart/list/page?acadyear="+acadyear+"&semester="+semester+"&week="+week+"&classId="+id);
  }
}

function onWeek(){
	   var classId = $('#classId').val();
	  var acadyear = $('#acadyear').val();
	  var semester = $('#semester').val();
	  var week = $('#week').val();
	  if(classId && classId != ''){
	  	$("#statInfoDiv").load("${request.contextPath}/stuwork/classStart/list/page?acadyear="+acadyear+"&semester="+semester+"&week="+week+"&classId="+classId);
	  }
}

function saveStat(){
	  var acadyear = $('#acadyear').val();
	  var semester = $('#semester').val();
	  var week = $('#week').val();
	  $.ajax({
			url:"${request.contextPath}/stuwork/classStart/classStat/save",
			data: {'acadyear':acadyear,'semester':semester,'week':week},  
			type:'post',
			success:function(data) {
				//alert(data);
				var jsonO = JSON.parse(data);
				//alert(jsonO.msg);
				if(jsonO.success){
					layerTipMsg(jsonO.success,"成功",jsonO.msg);
					onWeek();
				}else{
					layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
			},
	 		error : function(XMLHttpRequest, textStatus, errorThrown) {
	 			
			}
		});
}
</script>