<title>学生管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<!-- ajax layout which only needs content area -->
<#if unitClass==2>
<@w.btn btnId="btn-addStudent" btnValue="新增学生" btnClass="fa-plus" />
</#if>
<div class="jqGrid_wrapper">
	<table id="studentList"></table>
    <div id="studentPager"></div>
</div>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		<@studentList />
	});
</script>

<#macro studentList>
var rowList = [10, 20,50, 100, 200];
var rowNum = 20;
var url = encodeURI("${request.contextPath}/basedata/student/stulist?unitId=${unitId!}&schoolId=${schoolId!}&gradeId=${gradeId!}&classId=${classId!}&stuName=${stuName!}");
$("#studentList").jqGrid({
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
	caption: "学生信息",//设置为空，则不显示标题行
	emptyrecords:'没有数据',//当空记录时显示
	multiselect: true,
	colModel:[
	//修改了jqgrid的源码，使其支持表头th元素的样式控制(thclasses)
	{name:"id",width:90,sortable:false,hidden:true},
	{name:"studentName",label:"姓名",width:120,sortable:false},
	{name:"studentCode",label:"学号",width:90,sortable:false},
	{name:"identityCard",label:"身份证号",width:90,sortable:false},
	<#if unitClass==1>
	{name:"schoolName",label:"学校",width:90,sortable:false},
	</#if>
	{name:"className",label:"班级",width:90,sortable:false},
	{name:"isLeaveSchool",label:"是否离校",width:60,sortable:false,formatter:"select",
		editoptions:{value:"${mcodeSetting.getMcodeWithJqGrid('DM-BOOLEAN')}"}
	},
	{name:"_operation",label:"操作",fixed:true,width:60,sortable:false}

],
gridComplete: function () {
  dealStudents();
  updatePagerIcons(this);
}, 
jsonReader: {
	id: "id"
},
pager:"#studentPager"}
);
$("#studentList").jqGrid('navGrid',"#studentPager",
	{ 
		add: false,
		addicon : 'ace-icon fa fa-plus-circle purple',
		edit: false,
		editicon : 'ace-icon fa fa-pencil blue',
		view: false,
		viewicon : 'ace-icon fa fa-search-plus grey',
		del: true,
		delicon : 'ace-icon fa fa-trash-o red',
		delfunc : function(ids){
			swal(
			{	title: "删除学生", 
				html: true, 
				text: "确认要删除？",   
				type: "warning", 
				showCancelButton: true, 
				closeOnConfirm: false, 
				confirmButtonText: "是",
				cancelButtonText: "否",
				showLoaderOnConfirm: true,
				animation:false
			},
			function(){
				doDeleteById(ids);
			});
		},
		search: false,
		searchicon : 'ace-icon fa fa-search orange',
		refresh: true,
		refreshicon : 'ace-icon fa fa-refresh green',
		alerttext : '请选择行',
		alertcap : '警告'
	}
);



function doDeleteById(ids){
	var sendIds;
	if(ids instanceof Array){
		sendIds='';
		for(var i=0;i<ids.length;i++){
			sendIds+=","+ids[i];
		}
		sendIds=sendIds.substring(1,sendIds.length);
	}else{
		sendIds=ids;
	}
	$.ajax({
		url:'${request.contextPath}/basedata/student/deletes',
		data: {'ids':sendIds},
		type:'post',
		beforeSend:function(XMLHttpRequest){
			
		},  
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			swal({title: "操作成功!",
					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定"},
					function(){
						$("#studentList").trigger("reloadGrid");
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
}
function dealStudents(){
	var ids = jQuery("#studentList").jqGrid('getDataIDs');
	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
  		var rowData = $("#studentList").getRowData(id);
  		var editBtn = buttons(rowData["id"], "edit_btn_", id, "green", "fa-pencil","编辑");
	  	var trashBtn = buttons(rowData["id"], "del_btn_", id, "red", "fa-trash-o","删除");
		$("#studentList").jqGrid('setRowData', ids[i], {_operation: "<div class=\"action-buttons\">" + editBtn + " " + trashBtn + "</div>" });		
  		$("#edit_btn_" + id).on("click", function(){
  			var value=$(this).attr("value");
			var url =  '${request.contextPath}/basedata/student/' + value + '/edit/page';
			indexDiv = layerDivUrl(url,{title:"学生信息"});
		});
		$("#del_btn_" + id).on("click", function(){
			var value=$(this).attr("value");
			swal(
			{	title: "删除学生", 
				html: true, 
				text: "确认要删除？",   
				type: "warning", 
				showCancelButton: true, 
				closeOnConfirm: false, 
				confirmButtonText: "是",
				cancelButtonText: "否",
				showLoaderOnConfirm: true,
				animation:false
			},
			function(){
				doDeleteById(value);
			});
		});
		
  	}
 }
<#if unitClass==2>
$("#btn-addStudent").on("click",function(){
	<#if (classId?exists && classId!="")>
		var url =  '${request.contextPath}/basedata/student/add/page?classId=${classId!}';
		indexDiv = layerDivUrl(url,{title:"学生信息"});
	<#else>
		swal("请先选择一个班级!");
	</#if>
})
</#if>

</#macro>
