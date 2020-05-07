<#import "/fw/macro/webmacro.ftl" as w>
<div class="col-lg-12">
	<div class="jqGrid_wrapper">
	    <table id="gradeList"></table>
	    <div id="gradePager"></div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		<@gradeList />
	});
</script>
<#--
<div class="col-lg-12 grade-list">
<table id="simple-table" class="table  table-bordered table-hover">
	<thead>
		<tr>
			<th class="center">
				<@w.cbx />
			</th>
			<th>年级名称</th>
			<th>学年学期</th>
			<th class="hidden-480">年制</th>
			<th class="hidden-480">学段</th>
			<th class="hidden-480">班级数</th>
			<th>
				<i class="ace-icon fa fa-clock-o bigger-110 hidden-480"></i>
				操作
			</th>
		</tr>
	</thead>
	<tbody>
		<#list grades as grade>
		<tr>
			<td class="center">
				<@w.cbx />
			</td>
			<td>
				<a href="javascript:;">${grade.gradeName}</a>
			</td>
			<td>${grade.openAcadyear}</td>
			<td class="hidden-480">${grade.schoolingLength}</td>
			<td class="hidden-480">${grade.section}</td>
			<td class="hidden-480">${clazzCount!}</td>
			<th>
				<@w.btnEdit id=grade.id value=grade.id otherText=grade.gradeName permission="/basedata/grade/{id}/edit" />
				<@w.btnDelete id=grade.id value=grade.id  otherText=grade.gradeName permission="/basedata/grade/{id}/delete" />
			</th>
		</tr>
		</#list>
	</tbody>
	</table>
	<div class="row">
	<div class="col-xs-6"><div>Showing 1 to 10 of 23 entries</div></div>
	<div class="col-xs-6">
		<div class="dataTables_paginate paging_simple_numbers">
			<ul class="pagination">
			<li class="paginate_button previous disabled"><a href="javascript:;">上一页</a></li>
			<li class="paginate_button active"><a href="javascript:;">1</a></li>
			<li class="paginate_button"><a href="javascript:;">2</a></li>
			<li class="paginate_button"><a href="javascript:;">3</a></li>
			<li class="paginate_button next"><a href="javascript:;">下一页</a></li>
			</ul>
		</div>
	</div>
	</div>
</div>

<script type="text/javascript">
//page specific plugin scripts
	$("#btn-back").addClass("hide");

	$(".btn-grade-edit").each(function(){
		$(this).on("click", function(){
			var value = $(this).attr("value");
			var url =  '${request.contextPath}/basedata/grade/' + value + '/detail/page';
			indexDiv = layerDivUrl(url);
		});

	});

	$(".btn-grade-delete").each(function(){
		$(this).on("click", function(){
			var value = $(this).attr("value");
			var name = $(this).attr("otherText");

			swal({title: "删除年级", html: true, 
			text: "是否要删除<strong><font color='red'>" + name + "</font></strong>？",   
			type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
			cancelButtonText: "否",showLoaderOnConfirm: true,animation:false
			}, 
			function(){   
				$.ajax({
		    		url:'${request.contextPath}/basedata/grade/' + value + '/delete',
		    		success:function(data) {
		    			var jsonO = JSON.parse(data);
				 		if(jsonO.success){
				 			swal({title: "操作成功!",
		    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:500},
		    					function(){
		    						$("#gradeList").trigger("reloadGrid");
		    						sweetAlert.close();
		    					}
		    				);
				 		}
				 		else{
		    				swal({title: "操作失败!",
		    					text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
		    				);
		    			}
		    		}
				});
			});
		});
	});
</script>
 -->
 
<#-- 列表 -->
<#macro gradeList>
$("#gradeList").jqGrid({
	url:"${request.contextPath}/basedata/grade/unit/${unitId!}/list?"+searchUrlValue("#searchDiv"),
	datatype: "json",
	mtype:"GET",
	height:"auto",
	autowidth:true,
	shrinkToFit:true,
	viewrecords:true,
	hidegrid:true,
	rowNum:'all',
	loadtext:'正在加载...',//当数据还没加载完或数据格式不正确时显示
	caption: "年级信息",//设置为空，则不显示标题行
	emptyrecords:'没有数据',//当空记录时显示
	//rownumbers: true,//设置为true，则会多出一列，rn，作为id列，从1开始  
	colModel:[
		{name:"_operation",label:"操作",fixed:true,width:100,sortable:false},
		{name:"grade.id",sortable:false,hidden:true,},
		{name:"needGraduate",sortable:false,hidden:true,},
		{name:"grade.gradeName",label:"年级名称",sortable:false},
		{name:"grade.openAcadyear",label:"入学学年",sortable:false},
		{name:"grade.section",label:"学段",sortable:false,formatter:"select", editoptions:{value:"${mcodeSetting.getMcodeWithJqGrid("DM-RKXD")}"}},
		{name:"clazzCount",label:"班级数",sortable:false},
		{name:"grade.schoolingLength",label:"学制",sortable:false}
	],
	gridComplete: function () {
		dealGrades();
    },
    jsonReader: {
		id: "grade.id" 
    }
});
		
function dealGrades(){
	var ids = jQuery("#gradeList").jqGrid('getDataIDs');
	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
	  	var rowData = $("#gradeList").getRowData(id);
	  	var editBtn = buttons(rowData["grade.gradeName"], "edit_btn_", id, "green", "fa-pencil");
	  	var trashBtn = buttons(rowData["grade.gradeName"], "del_btn_", id, "red", "fa-trash-o");
	  	var clazzBtn = buttons(rowData["grade.section"], "clazz_btn_", id, "blue", "fa-group", "查看班级");
	  	var btns = editBtn + " " + trashBtn + " " + clazzBtn;
	  	//var btns = editBtn + " " + trashBtn;

		$("#gradeList").jqGrid('setRowData', ids[i], {_operation: "<div class=\"action-buttons\">" + btns + "</div>" });
		
		$("#clazz_btn_" + id).on("click", function(){
			var value = $(this).attr("value");
			var section = $(this).attr("name");
			var url =  '${request.contextPath}/basedata/class/grade/' + value + '/list/page';
			//indexDiv = $(".listDiv").load(url);
			$("#claDiv").load(url);
			$("#graDiv").find('.HeaderButton').click();
			$("#searchClassDiv").show();
			$("#selectGradeId").val(value);
			$("#selectSection").val(section);
		});
		$("#edit_btn_" + id).on("click", function(){
			var value = $(this).attr("value");
			//var url =  '${request.contextPath}/basedata/grade/' + value + '/detail/page';
			var url =  '${request.contextPath}/basedata/grade/edit/page?id='+value;
			indexDiv = layerDivUrl(url);
		});
		
		$("#del_btn_" + id).on("click", function(){
			var value = $(this).attr("value");
			var value = $(this).attr("value");
			var name = $(this).attr("name");
			swal(
			{
				title: "是否要删除<strong><font color='red'>" + name + "</font></strong>？", 
				html: true, 
				type: "warning", 
				showCancelButton: true, 
				closeOnConfirm: false, 
				confirmButtonText: "是",
				cancelButtonText: "否",
				showLoaderOnConfirm: true,
				animation:false
			}, 
			function(){   
				$.ajax({
		    		url:'${request.contextPath}/basedata/grade/' + value + '/delete',
		    		success:function(data) {
		    			var jsonO = JSON.parse(data);
				 		if(jsonO.success){
				 			swal({title: "操作成功!",
		    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:3000},
		    					function(){
		    						$("#gradeList").trigger("reloadGrid");
		    						sweetAlert.close();
		    					}
		    				);
				 		}
				 		else{
		    				swal({title: "操作失败!",
		    					text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
		    				);
		    			}
		    		}
				});
			});
		})
	}
}
</#macro>
