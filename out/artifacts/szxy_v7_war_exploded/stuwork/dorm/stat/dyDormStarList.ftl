<div class="box box-default">
	<div class="box-body">
		<div class="filter" id="searchType">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" onchange="doSearch()" class="form-control">
						<#if acadyearList?exists && acadyearList?size gt 0>
							<#list acadyearList as item >
								<option value="${item!}" <#if item==dormDto.acadyear?default("")>selected="selected"</#if>>${item!}</option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="semesterStr" id="semesterStr" onchange="doSearch()" class="form-control">
						<option value="1" <#if "1"==dormDto.semesterStr?default("")>selected="selected"</#if>>第一学期</option>
						<option value="2" <#if "2"==dormDto.semesterStr?default("")>selected="selected"</#if>>第二学期</option>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学段：</span>
				<div class="filter-content">
					<select name="section" id="section" class="form-control" onchange="doSearch()">
						<#if sectionList?exists && sectionList?size gt 0>
							<#list sectionList as item >
								<option value="${item!}" <#if item==dormDto.section?default(0)>selected="selected"</#if>><#if item==0>幼儿园<#elseif item==1>小学<#elseif item==2>初中<#elseif item==3>高中<#else>剑桥高中</#if></option>
							</#list>
						</#if>
					</select>
				</div>
			</div>
		</div>
		
		<div class="table-container">
			<div class="table-container-header text-right">
				<button class="btn btn-white" onclick="doExport();">导出</button>
			</div>
			<div class="table-container-body">
				<table id="example" class="table table-bordered "></table>
			</div>
			<form class="print">
				<div class="table-container-body" style="display:none">
					<table id="examplePrint" class="table table-bordered "></table>
				</div>
			</form>
		</div>
	</div>
</div>
		<link rel="stylesheet" href="${request.contextPath!}/static/components/datatables/media/css/jquery.dataTables.min.css">
		<script src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
		<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
		<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.fixedColumns.min.js"></script>
		<script src="${request.contextPath}/static/components/datatables/media/js/dataTables.rowsGroup.js"></script>
		<script src="${request.contextPath}/static/js/LodopFuncs.js" />
<script>
	$(function(){
		var data = [
			<#if formList?exists && formList?size gt 0>
				<#list formList as item>
					['${item.className!}','${item.roomName!}',<#if item.statList?exists && item.statList?size gt 0><#list item.statList as stat>'<#if stat.isExcellent?default(false)><i class="fa fa-star color-red"></i></#if>'<#if !(stat_index==(item.statList?size-1))>,</#if></#list></#if>]<#if !(item_index==(formList?size-1))>,</#if>
				</#list>
			<#else>
				['无','无']
			</#if>
		];
		var data1 = [
			<#if formList?exists && formList?size gt 0>
				<#list formList as item>
					['${item.className!}','${item.roomName!}',<#if item.statList?exists && item.statList?size gt 0><#list item.statList as stat>'<#if stat.isExcellent?default(false)><font color="red">★</font></#if>'<#if !(stat_index==(item.statList?size-1))>,</#if></#list></#if>]<#if !(item_index==(formList?size-1))>,</#if>
				</#list>
			<#else>
				['无','无']
			</#if>
		];
		 var table=$('#example').DataTable( {
		 	destroy: true,
	        scrollY: "500px",
			info: false,
			searching: false,
			autoWidth: false,
	        sort: false,
	        scrollX: true,
	        paging: false,
	        columns: [
				{title:'&emsp;&emsp;班级&emsp;&emsp;', width:"100", class:'text-center'},
				{title:'&emsp;寝室&emsp;&emsp;', width:"100", class:'text-center'},
				<#if statList?exists && statList?size gt 0>
					<#list statList as stat> 
						{title:'第${stat_index+1}周', width:"100", class:'text-center'},
					</#list>
				</#if>
			],
			rowsGroup:[0],
			data:data
	    });
	    new $.fn.dataTable.FixedColumns( table, {
		    leftColumns: 2
		} );
		var table1=$('#examplePrint').DataTable( {
		 	destroy: true,
	        scrollY: "500px",
			info: false,
			searching: false,
			autoWidth: false,
	        sort: false,
	        scrollX: true,
	        paging: false,
	        columns: [
				{title:'&emsp;&emsp;班级&emsp;&emsp;', width:"100", class:'text-center'},
				{title:'&emsp;寝室&emsp;&emsp;', width:"100", class:'text-center'},
				<#if statList?exists && statList?size gt 0>
					<#list statList as stat> 
						{title:'第${stat_index+1}周', width:"100", class:'text-center'},
					</#list>
				</#if>
			],
			rowsGroup:[0],
			data:data1
	    });
	    new $.fn.dataTable.FixedColumns( table1, {
		    leftColumns: 2
		} );
	});
	function doSearch(){
		var url="${request.contextPath}/stuwork/dorm/stat/starList/page?"+searchUrlValue("#searchType");
		$("#itemShowDivId").load(url);
	}
	function doExport(){
		var LODOP=getLodop('${request.contextPath}');  //初始化打印控件，如果发现没有安装的话，会出现一个安装对话框进行下载安装
		//设置打印内容，getPrintContent函数是LodopFuncs中增加的一个函数，参数传入jQuery对象，第二个参数表示要忽略的样式名，可以不写（主要是有些样式与打印冲突，如prt-report-wrap，这个价了后，后面的内容就不会显示）
		LODOP.ADD_PRINT_TABLE("20mm","15mm","RightMargin:15mm","BottomMargin:15mm",getPrintContent($(".print")));
		LODOP.SAVE_TO_FILE(getConditions()+"星级寝室汇总"+".xls");
	}
	function getConditions(){
		var acadyear=$("#acadyear").val();
		var semesterStr=$("#semesterStr").val();
		var section=$("#section").val();
		if(section==0){
			section="幼儿园";
		}else if(section==1){
			section="小学";
		}else if(section==2){
			section="初中";
		}else if(section==3){
			section="高中";
		}else if(section==4){
			section="剑桥高中";
		}
		if(semesterStr=="1"){
			semesterStr="一";
		}else if(semesterStr=="2"){
			semesterStr="二";
		}
		return acadyear+"学年第"+semesterStr+"学期"+section;
	}
</script>
