<div class="col-lg-12">
	<div class="jqGrid_wrapper">
        <table id="teachAreaList"></table>
        <div id="teachAreaPager"></div>
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
$("#teachAreaList").jqGrid({
	url:"${request.contextPath}/basedata/teachArea/unit/${unitId!}/list",
	datatype: "json",
	mtype:"GET",
	height:"auto",
	autowidth:true,
	shrinkToFit:true,
	viewrecords:true,
	hidegrid:true,
	rowNum:'all',
	loadtext:'正在加载...',//当数据还没加载完或数据格式不正确时显示
	caption: "校区信息",//设置为空，则不显示标题行
	emptyrecords:'没有数据',//当空记录时显示
	//rownumbers: true,//设置为true，则会多出一列，rn，作为id列，从1开始  
	colModel:[
		{name:"_operation",label:"操作",fixed:true,width:70,sortable:false},
		{name:"teachArea.id",sortable:false,hidden:true},
		{name:"teachArea.areaName",label:"名称",sortable:false},
		{name:"teachArea.address",label:"地址",sortable:false},
		{name:"teachArea.linkPhone",label:"联系电话",sortable:false},
		{name:"teachArea.email",label:"Email地址",sortable:false},
	],
	gridComplete: function () {
		dealGrades();
    },
    jsonReader: {
		id: "teachArea.id" 
    }
});
		
function dealGrades(){
	var ids = jQuery("#teachAreaList").jqGrid('getDataIDs');
	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
	  	var rowData = $("#teachAreaList").getRowData(id);
	  	var editBtn = buttons(rowData["teachArea.areaName"], "edit_btn_", id, "green", "fa-pencil");
	  	var trashBtn = buttons(rowData["teachArea.areaName"], "del_btn_", id, "red", "fa-trash-o");
	  	var btns = editBtn + " " + trashBtn;

		$("#teachAreaList").jqGrid('setRowData', ids[i], {_operation: "<div class=\"action-buttons\">" + btns + "</div>" });
		
		$("#edit_btn_" + id).on("click", function(){
			var value = $(this).attr("value");
			var url =  '${request.contextPath}/basedata/teachArea/edit/page?id='+value;
			indexDiv = layerDivUrl(url);
		});
		
		$("#del_btn_" + id).on("click", function(){
			var value = $(this).attr("value");
			var name = $(this).attr("name");

			swal({
			title: "是否要删除<strong><font color='red'>" + name + "</font></strong>？", 
			html: true, 
			type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
			cancelButtonText: "否",showLoaderOnConfirm: true,animation:false
			}, 
			function(){   
				$.ajax({
		    		url:'${request.contextPath}/basedata/teachArea/' + value + '/delete',
		    		success:function(data) {
		    			var jsonO = JSON.parse(data);
				 		if(jsonO.success){
				 			swal({title: "操作成功!",
		    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:500},
		    					function(){
		    						$("#teachAreaList").trigger("reloadGrid");
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
