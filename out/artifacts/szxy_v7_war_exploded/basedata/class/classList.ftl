<div class="col-lg-12">
	<div class="jqGrid_wrapper">
        <table id="classList"></table>
        <div id="classPager"></div>
    </div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		<@clazzList />
	});
</script>

<#-- 列表 -->
<#macro clazzList>
$("#classList").jqGrid({
	url:"${request.contextPath}/basedata/class/grade/${gradeId!}/list",
	datatype: "json",
	mtype:"GET",
	height:"auto",
	autowidth:true,
	shrinkToFit:true,
	viewrecords:true,
	hidegrid:true,
	rowNum:'all',
	loadtext:'正在加载...',//当数据还没加载完或数据格式不正确时显示
	caption: "<span style='font-size:20px'>[${gradeName!}年级]</span>班级信息",//设置为空，则不显示标题行
	emptyrecords:'没有数据',//当空记录时显示
	//rownumbers: true,//设置为true，则会多出一列，rn，作为id列，从1开始  
	colModel:[
		{name:"_operation",label:"操作",fixed:true,width:70,sortable:false},
		{name:"clazz.id",sortable:false,hidden:true},
		{name:"clazz.classnamedynamic",label:"名称",sortable:false},
		{name:"clazz.acadyear",label:"学年学期",sortable:false},
		{name:"clazz.section",label:"学段", sortable:false,formatter:"select", editoptions:{value:"${mcodeSetting.getMcodeWithJqGrid("DM-RKXD")}"}},
		{name:"clazz.isGraduate",label:"是否毕业", sortable:false,formatter:"select", editoptions:{value:"${mcodeSetting.getMcodeWithJqGrid("DM-BOOLEAN")}"}},
		{name:"teacher.teacherName",label:"班主任",sortable:false},
		{name:"studentCount",label:"学生数",sortable:false},
		{name:"clazz.schoolingLength",label:"学制",sortable:false}
	],
	gridComplete: function () {
      dealGrades();
    },
    jsonReader: {
		id: "clazz.id" 
    }
});
		
function dealGrades(){
	var ids = jQuery("#classList").jqGrid('getDataIDs');
	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
	  	var rowData = $("#classList").getRowData(id);
	  	var editBtn = buttons(rowData["clazz.classnamedynamic"], "edit_btn_", id, "green", "fa-pencil");
	  	var trashBtn = buttons(rowData["clazz.classnamedynamic"], "del_btn_", id, "red", "fa-trash-o");
	  	//var studentBtn = buttons(rowData["clazz.classnamedynamic"], "student_btn_", id, "blue", "fa-user", "查看学生");
	  	//var btns = editBtn + " " + trashBtn + " " + studentBtn;
	  	var btns = editBtn + " " + trashBtn;

		$("#classList").jqGrid('setRowData', ids[i], {_operation: "<div class=\"action-buttons\">" + btns + "</div>" });
		
		//$("#studentBtn_btn_" + id).on("click", function(){
		//	var value = $(this).attr("value");
		//	var url =  '${request.contextPath}/basedata/class/' + value + '/student/page';
		//	indexDiv = $(".listDiv").load(url);
		//});

		$("#edit_btn_" + id).on("click", function(){
			var value = $(this).attr("value");
			var url =  '${request.contextPath}/basedata/class/edit/page?id='+value;
			indexDiv = layerDivUrl(url);
		});
		
		$("#del_btn_" + id).on("click", function(){
			var value = $(this).attr("value");
			var name = $(this).attr("name");

			swal({
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
		    		url:'${request.contextPath}/basedata/class/' + value + '/delete',
		    		success:function(data) {
		    			var jsonO = JSON.parse(data);
				 		if(jsonO.success){
				 			swal({title: "操作成功!",
		    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:500},
		    					function(){
		    						$("#classList").trigger("reloadGrid");
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
