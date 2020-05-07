<div id="aa" class="tab-pane active" role="tabpanel">
	<div class="filter">
		<div class="filter-item">
			<button class="btn btn-blue" onClick="doprint();">打印</button>
			<button class="btn btn-white" onClick="onBatchPrint();">批量打印</button>
		</div>
	</div>
	<div class="picker-table">
		<table class="table">
			<tbody>
				<tr>
					<th>科目：</th>
					<td>
						<div class="float-left mt3"></div>
						<div class="float-left mr10">
							<select name="" id="subjectId" class="form-control" onChange="queryZList('${roomNo!}');">
							<option value="">--请选择--</option>
							<#if subList?exists && subList?size gt 0>
							    <#list subList as sub>
								    <option value="${sub.id!}" <#if subId?exists && '${subId!}' == '${sub.id!}'>selected="selected"</#if>>${sub.subjectName!}
								    (
								    <#if sub.section == 1>
								     小学
								    <#elseif sub.section == 0>
						    		学前
								    <#elseif sub.section == 2>
								    初中
								    <#elseif sub.section == 3>
								    高中
								    </#if>
								    )</option>
								</#list>
						    </#if>
							</select>
						</div>
					</td>
					<td></td>
				</tr>
				<tr>
					<th width="150" style="vertical-align: top;">考场：</th>
					<td>
						<div class="outter">
							<#if roomNoList?exists && roomNoList?size gt 0>
							    <#list roomNoList as item>
							          <a <#if roomNo?exists && roomNo == item>class="selected"</#if> href="javascript:void(0);" onClick="queryZList('${item!}');">${item!}</a>
							    </#list>
							</#if>
						</div>
					</td>
					<td width="75" style="vertical-align: top;">
						<div class="outter">
							<a class="picker-more" href="#"><span>展开</span><i class="fa fa-angle-down"></i></a>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="filter">
		<div class="filter-item">
			<div class="filter-name">考试名称：</div>
			<div class="filter-content">${examName!}</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考点：</div>
			<div class="filter-content">${roomNoLocationIdMap[roomNo!]!}</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考场：</div>
			<div class="filter-content">${roomNo!}</div>
		</div>
		<div class="filter-item">
			<div class="filter-name">考生：</div>
			<div class="filter-content">${regList2?size}人</div>
		</div>
	</div>
	<h1 class="text-center">${roomNo!}考场座位号</h1>
    <div class="row" id="print">
    <#if regList2?exists && regList2?size gt 0>
		    <#list regList2 as item>
		<div class="col-xs-4" <#if (item_index+1)%12==0>style="page-break-after:always;"</#if>> 
            <div class="box-boder exaRoom-border">
		         <h1 class="exaRoom-fontSize text-center">${item.seatNo!}</h1>
		         <table class="table table-bordered table-striped table-condensed table-hover exaRoom-table">
					<tbody>
						<tr>
							<td class="text-center">姓名：</td>
							<td>${item.teacherName!}</td>
						</tr>
						<tr>
							<td class="text-center">科目：</td>
							<td>${item.subName!}</td>
						</tr>
						<tr>
							<td class="text-center">考号：</td>
							<td>${item.cardNo!}</td>
						</tr>
						<tr>
							<td class="text-center">考场：</td>
							<td>${roomNo!}</td>
						</tr>
					</tbody>
				 </table>
		    </div>
	   </div>
	   </#list>
	</#if>
	</div>
</div>
<iframe name="batchPrintId" id="batchPrintId" style="display:none;" width="100%" height="100%" frameborder="0" ></iframe>	
<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
function queryZList(roomNo){
     var examId = '${examId!}';
     var subId = $('#subjectId').val();
     var url = "${request.contextPath}/teaexam/siteSet/query/zPasteList?examId="+examId+"&subId="+subId+"&roomNo="+roomNo;
     $('#siteTabDiv').load(url);
}

$(function(){
    $('.picker-more').click(function(){
		if($(this).children('span').text()=='展开'){
			$(this).children('span').text('折叠');
			$(this).children('.fa').addClass('fa-angle-up').removeClass('fa-angle-down');
		}else{
			$(this).children('span').text('展开');
			$(this).children('.fa').addClass('fa-angle-down').removeClass('fa-angle-up');
		};
		$(this).parents('td').siblings('td').children('.outter').toggleClass('outter-auto');
	});
})


function onBatchPrint(){
    var examId = '${examId!}';
    var subId = $('#subjectId').val();
    document.getElementById('batchPrintId').src="${request.contextPath}/teaexam/siteSet/query/zBatchPrint?examId="+examId+"&subId="+subId+"&batchId=${batchId!}";
}

function doBatchPrint(){
	var batchIdLeftVal = window.frames["batchPrintId"].document.getElementById("batchIdLeft").value;
	var examName = window.frames["batchPrintId"].document.getElementById("examName").value;
	var unitName = window.frames["batchPrintId"].document.getElementById("unitName").value;
	var roomNo = window.frames["batchPrintId"].document.getElementById("roomNo").value;
	var listSize = window.frames["batchPrintId"].document.getElementById("listSize").value;
	//alert(batchIdLeftVal)
	if (batchIdLeftVal == "") {
		if (window.frames["batchPrintId"].document.getElementById("doNotPrint").value == "0") {
			LODOP=getLodop();  
			//LODOP.ADD_PRINT_TEXT('20', '250','400', '50', '考试名称：'+examName+'考点：'+unitName+'考场：'+roomNo+'考生'+listSize+'人');
	        //设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	        LODOP.ADD_PRINT_HTM("10mm","10mm","RightMargin:10mm","BottomMargin:10mm",batchPrintId.window.getSubContent());
	        LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//横向打印
	        LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
			LODOP.PRINT();
			LODOP.SET_PRINT_STYLE("ItemType", 2);
	        LODOP.SET_PRINT_STYLE("Alignment", 2);
	        LODOP.SET_PRINT_STYLE("FontSize", 11.5);
            LODOP.ADD_PRINT_TEXT(0,0,"95%",30,'考试名称：'+examName+'   考点：'+unitName+'   考场：'+roomNo+'   考生'+listSize+'人     第#页/共&页');
			//LODOP.PREVIEW();//打印预览
	     	showMsgSuccess("打印成功！");
		} 
	} else {
	    var subId = $('#subjectId').val();
		LODOP=getLodop();  
		//LODOP.ADD_PRINT_TEXT('20', '250','400', '50', '考试名称：'+examName+'考点：'+unitName+'考场：'+roomNo+'考生'+listSize+'人');
	    //设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	    LODOP.ADD_PRINT_HTM("10mm","10mm","RightMargin:10mm","BottomMargin:10mm",batchPrintId.window.getSubContent());
	    LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//横向打印
	    LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
		LODOP.SET_PRINT_STYLE("ItemType", 2);
	    LODOP.SET_PRINT_STYLE("Alignment", 2);
	    LODOP.SET_PRINT_STYLE("FontSize", 11.5);
        LODOP.ADD_PRINT_TEXT(0,0,"95%",30,'考试名称：'+examName+'   考点：'+unitName+'   考场：'+roomNo+'   考生'+listSize+'人     第#页/共&页');
		LODOP.PRINT();
		//LODOP.PREVIEW();//打印预览
		document.getElementById('batchPrintId').src="${request.contextPath}/teaexam/siteSet/query/zBatchPrint?examId=${examId!}&subId="+subId+"&batchId="+batchIdLeftVal;
	}
}


function doprint(){
    var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
    if (LODOP==undefined || LODOP==null) {
		return;
	}
	//LODOP.ADD_PRINT_TEXT('20', '350','400', '50', '考试名称：${examName!}考点：${roomNoLocationIdMap[roomNo!]!}考场：${roomNo!}考生${regList2?size}人');
	//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
	LODOP.ADD_PRINT_HTM("10mm","10mm","RightMargin:10mm","BottomMargin:10mm",getPrintContent($("#print")));
	LODOP.SET_PRINT_PAGESIZE(1,0,0,"");//横向打印
	LODOP.SET_SHOW_MODE("LANDSCAPE_DEFROTATED",1);//横向时的正向显示
	LODOP.SET_PRINT_STYLE("ItemType", 2);
	LODOP.SET_PRINT_STYLE("Alignment", 2);
	LODOP.SET_PRINT_STYLE("FontSize", 11.5);
    LODOP.ADD_PRINT_TEXT(0,0,"95%",30,"考试名称：${examName!}   考点：${roomNoLocationIdMap[roomNo!]!}   考场：${roomNo!}   考生${regList2?size}人     第#页/共&页");
	LODOP.PREVIEW();//打印预览
}
</script>