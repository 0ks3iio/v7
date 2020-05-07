<#import "/stuwork/tree/dytreemacro.ftl" as dytreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-sm-3">
						<div class="box box-default">
							<div class="box-header">
								<h3 class="box-title">班级菜单</h3>
							</div>
							<div class="box-body">
								<ul id="tree" class="ztree">
								   <@dytreemacro.gradeClassStudentForSchoolInsetTree height="550" click="onTreeClick"/>
								</ul>
							</div>
							<input type="hidden" id="studentId" value="">
						</div>
					</div>
					<div class="col-sm-9">
						<div class="box box-default">
							<div class="box-body">
								<div class="filter" id="healthDC" style="display:none">
									<div class="filter-item filter-item-right" >
										<button class="btn btn-blue" onclick="doHealehExport()">导出</button>
										<button class="btn btn-blue" onclick="doPrint()">打印</button>
									</div>
								</div>
								<div id="showList" class="healthAll" >
		                            
	                            </div>
							</div>
						</div>
					</div>
				</div>
			</div><!-- /.page-content -->
		</div>
	</div><!-- /.main-content -->
</div><!-- /.main-container -->
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
$(function(){
	//showList('1','');
});
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "student"){
		var id = treeNode.id;
		$("#studentId").val(id);
		showList(id);
	}
}
function showList(id){
	if(id==undefined ||id==""){
		id=$("#studentId").val();
	}
	$("#healthDC").show();
	var url =  '${request.contextPath}/stuwork/health/result/list?studentId='+id;
	$("#showList").load(url);
}

//导出
function doHealehExport(){
	var studentId = $("#studentId").val().trim();
	if(studentId==undefined ||studentId=="")
	 return;
	document.location.href = "${request.contextPath}/stuwork/health/export?studentId="+studentId;
}

 //打印
   function doPrint(){
    var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
    if (LODOP==undefined || LODOP==null) {
			return;
		}
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_HTM("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".healthAll")));
	LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//横向打印
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
	LODOP.PREVIEW();//打印预览
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
