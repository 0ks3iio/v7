<title>教学班管理</title>
<!-- ajax layout which only needs content area -->
<div class="jqGrid_wrapper">
    <table id="teachclassList"></table>
    <div id="teachclassPager"></div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		<@teachclassList />
	});
</script>

<#macro teachclassList>
var rowList = [10, 20,50, 100, 200];
var rowNum = 50;
var lastsel;
var url = "${request.contextPath}/basedata/teachclass/list?acadyear=${acadyear!}&semester=${semester!}";
$("#teachclassList").jqGrid({
	url: url,
	datatype: "json",
	mtype:"GET",
	height:"auto",
	autowidth:true,
	shrinkToFit:true,
	viewrecords:true,
	hidegrid:false,
	rowList:rowList,
	rowNum:rowNum,	
	loadtext:'正在加载...',//当数据还没加载完或数据格式不正确时显示
	caption: "教学班信息",//设置为空，则不显示标题行
	emptyrecords:'没有数据',//当空记录时显示
	colModel:[
	//修改了jqgrid的源码，使其支持表头th元素的样式控制(thclasses)
	{name:"teachClass.id",width:90,sortable:false,hidden:true},
	{name:"teachClass.name",label:"班级名称",width:90,sortable:false},
	{name:"classTypeName",label:"班级属性",width:120,sortable:false},
	{name:"teacherName",label:"任课教师",width:90,sortable:false},
	{name:"subjectName",label:"科目",width:120,sortable:false},
	{name:"assistantTeacherName",label:"辅助教师",width:90,sortable:false},
	{name:"_operation_1",label:"学生管理",width:120,sortable:false},
	{name:"_operation_2",label:"操作",fixed:true,width:60,sortable:false}
	
],
gridComplete: function () {
  dealTeachClass();
  updatePagerIcons(this);
}, 

jsonReader: {
	id: "teachClass.id"
},
pager:"#teachclassPager"}
);

function dealTeachClass(){
	var ids = jQuery("#teachclassList").jqGrid('getDataIDs');
	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
  		var rowData = $("#teachclassList").getRowData(id);
  		var mid=rowData["id"];
  	    var stueditBtn = buttons(mid, "stu_edit_btn_", id, "green", "fa-pencil","教学班学生管理");
	  	var editBtn = buttons(mid, "edit_btn_", id, "green", "fa-pencil","编辑");
	  	var trashBtn = buttons(mid, "del_btn_", id, "red", "fa-trash-o","删除");
	  	$("#teachclassList").jqGrid('setRowData', ids[i], {_operation_1: "<div class=\"action-buttons\">" + stueditBtn  + "</div>" });
	  	$("#teachclassList").jqGrid('setRowData', ids[i], {_operation_2: "<div class=\"action-buttons\">" + editBtn + " " + trashBtn + "</div>" });
			
		$("#stu_edit_btn_" + id).on("click", function(){
			var id = $(this).attr("value");
			var url = "${request.contextPath}/basedata/teachclass/student/page?teachClassId="+id;
			//location.href=url;
			$('.listDiv').load(url);
		});
		$("#edit_btn_" + id).on("click", function(){
			var id = $(this).attr("value");
			var url =  '${request.contextPath}/basedata/teachclass/addoredit/'+id+'/page';
			indexDiv = layerDivUrl(url,{height:370});
		});
			
		$("#del_btn_" + id).on("click", function(){
			var id = $(this).attr("value");
			swal({title: "删除班级", html: true, 
					text: "是否要删除？",   
					type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
					cancelButtonText: "否",showLoaderOnConfirm: true,animation:false
				}, 
				function(){
						$.ajax({
				    		url:'${request.contextPath}/basedata/teachclass/delete/'+id,
				    		success:function(data) {
				    			var jsonO = JSON.parse(data);
						 		if(jsonO.success){
						 			swal({title: "操作成功!",
				    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:500},
				    					function(){
				    						$("#teachclassList").trigger("reloadGrid");
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
		
  	}
  		
}
 		

</#macro>
