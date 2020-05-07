<#import "/fw/macro/webmacro.ftl" as w>
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>
<title>教师管理</title>
<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-sm-12 right">
		<#--@w.btnEdit id="btn-addTeacher" value="新增教师"  class="fa-user-plus" /-->
		<@w.btn btnId="btn-addTeacher" btnValue="新增教师" btnClass="fa-plus" />
	</div>
</div>

<div class="jqGrid_wrapper">
    <table id="teacherList"></table>
    <div id="teacherPager"></div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		$("#btn-addTeacher").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/teacher/add/page");
		});
		<@teacherList />
	});
</script>

<#macro teacherList>
var rowList = [10, 50, 100, 200];
var rowNum = 50;
<#if deptId?default("") == "" && unitId?default("") != "">
var url = "${request.contextPath}/basedata/unit/${unitId!}/teacher";
<#else>
var url = "${request.contextPath}/basedata/dept/${deptId!}/teacher";
</#if>
$("#teacherList").jqGrid({url: url,
datatype: "json",mtype:"GET",height:"auto",autowidth:true,shrinkToFit:true,
viewrecords:true,hidegrid:false,rowList:rowList,rowNum:rowNum,
caption: "教师信息",
colModel:[
	{name:"_operation",label:"操作",fixed:true,width:60,sortable:false},
	//修改了jqgrid的源码，使其支持表头th元素的样式控制(thclasses)
	{name:"teacher.id",width:90,sortable:false,hidden:true},
	{name:"teacher.teacherCode",label:"编号",width:90,sortable:false},
	{name:"teacher.teacherName",label:"姓名",width:90,sortable:false},
	{name:"teacher.sex",label:"性别",width:100,sortable:false, formatter:"select", editoptions:{value:"${mcodeSetting.getMcodeWithJqGrid("DM-XB")}"}},
	{name:"teacher.birthday",label:"出生日期",width:100,sortable:false,formatter : formatDate},
	{name:"teacher.mobilePhone", label:"手机号码",width:100,sortable:false}
],
gridComplete: function () {
  dealTeachers();
  updatePagerIcons(this);
}, 
jsonReader: {
	id: "teacher.id" 
},
pager:"#teacherPager"}
);

	function dealTeachers(){
		var ids = jQuery("#teacherList").jqGrid('getDataIDs');
		for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
	  	var rowData = $("#teacherList").getRowData(id);
	  	var editBtn = buttons(rowData["teacher.teacherName"], "edit_btn_", id, "green", "fa-pencil");
	  	var trashBtn = buttons(rowData["teacher.teacherName"], "del_btn_", id, "red", "fa-trash-o");
		$("#teacherList").jqGrid('setRowData', ids[i], {_operation: "<div class=\"action-buttons\">" + editBtn + " " + trashBtn + "</div>" });
			
		$("#edit_btn_" + id).on("click", function(){
				var value = $(this).attr("value");
				<#-- var index = layer.load(2); -->
				var url =  '${request.contextPath}/basedata/teacher/' + value + '/detail/page';
				indexDiv = layerDivUrl(url,{height:600});
		});
			
		$("#del_btn_" + id).on("click", function(){
			var val = $(this).attr("value");
			var name = $(this).attr("name");
			swal({	title: "删除教师", 
						html: true, 
						text: "是否要删除<strong><font color='red'>" + name + "</font></strong>？",   
						type: "warning", 
						showCancelButton: true, 
						closeOnConfirm: false, 
						confirmButtonText: "是",
						cancelButtonText: "否",
						showLoaderOnConfirm: true,
						animation:false
					},function(){
						$.ajax({
				    		url:'${request.contextPath}/basedata/teacher/' + val + '/delete',
				    		data: "",
				    		type:'post',
				    		beforeSend:function(XMLHttpRequest){},  
				    		success:function(data) {
				    			var jsonO = JSON.parse(data);
						 		if(jsonO.success){
						 			swal({title: "操作成功!",
				    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定"},
				    					function(){
				    						$("#teacherList").trigger("reloadGrid");
				    					}
				    				);
						 		}
						 		else{
				    				swal({title: "操作失败!",
				    					text: jsonO.msg,type: "error",showConfirmButton: true,confirmButtonText: "确定"}
				    				);
				    			}
				    		},
				     		error : function(XMLHttpRequest, textStatus, errorThrown) {  
				     			var text = syncText(XMLHttpRequest);
				     			swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
				    		}
				    	});
					});
			
		});
	}
}
</#macro>
