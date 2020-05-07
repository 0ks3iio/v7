<div class="jqGrid_wrapper">
    <table id="subsystemList"></table>
</div>
<script type="text/javascript">
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		<@subsystemList />
	});
</script>

<#macro subsystemList>
var rowList = [];
var rowNum = 0;
var url = "${request.contextPath}/basedata/subsystem/all";
$("#subsystemList").jqGrid({url: url,
datatype: "json",mtype:"GET",height:"auto",autowidth:true,shrinkToFit:true,
viewrecords:true,hidegrid:false,rowList:rowList,rowNum:rowNum,
caption: "应用信息",
colModel:[
	{name:"_operation",label:"操作",fixed:true,width:60,sortable:false},
	{name:"id",width:90,sortable:false,hidden:true},
	{name:"intId",label:"ID",width:40,sortable:false},
	{name:"name",label:"名称",width:60,sortable:false},
	{name:"code",label:"编号",width:60,sortable:false},
	{name:"url",label:"地址",width:100,sortable:false},
	{name:"parentId",label:"上级系统",width:30,sortable:false, formatter:"select", editoptions:{value:"${parentDir}"}},
	{name:"displayOrder",label:"排序号",width:30,sortable:false},
],
gridComplete: function () {
	dealSubsystems();
}, 
jsonReader: {
	id: "subsystem.id" 
}}
);

function dealSubsystems(){
	var ids = jQuery("#subsystemList").jqGrid('getDataIDs');
	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
  		var rowData = $("#subsystemList").getRowData(id);
  		var editBtn = buttons(rowData["name"], "edit_btn_", rowData["intId"], "green", "fa-pencil");
  		var trashBtn = buttons(rowData["name"], "del_btn_", rowData["intId"], "red", "fa-trash-o");
		$("#subsystemList").jqGrid('setRowData', ids[i], {_operation: "<div class=\"action-buttons\">" + editBtn + " " + trashBtn + "</div>" });
		
		$("#edit_btn_" + rowData["intId"]).on("click", function(){
			var value = $(this).attr("value");
			var url =  '${request.contextPath}/basedata/subsystem/' + value + '/detail/page';
			indexDiv = layerDivUrl(url, {title:"",height:500});
		});
		
		$("#del_btn_" + rowData["intId"]).on("click", function(){
			var value = $(this).attr("value");
			var name = $(this).attr("name");
			swal({title: "删除", html: true, 
			text: "<strong><font color='red'>删除应用后，此应用下所有的功能都无法使用！！</font></strong><br>请确认是否要删除<strong><font color='red'>" + name + "</font></strong>？",   
			type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
			cancelButtonText: "否",showLoaderOnConfirm: true,animation:false
		}, 
		function(){   
			<#--重要操作，再次确认！-->
			setTimeout(function(){
				swal({title: "删除", html: true, 
				text: "<font weight='blod' color='red'>再次确认是否要删除？</font>",   
				type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
				cancelButtonText: "否",showLoaderOnConfirm: true,animation:false
				}, 
				function(){
					$.ajax({
	    			url:'${request.contextPath}/basedata/subsystem/' + value + '/delete',
	    			data: "", type:'delete', beforeSend:function(XMLHttpRequest){},  
	    			success:function(data) {
	    			var jsonO = JSON.parse(data);
			 		if(jsonO.success){
			 			swal({title: "操作成功!",
	    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定"},
	    					function(){
	    						$("#subsystemList").trigger("reloadGrid");
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
				}, 0);
			});
		});
	}
}
</#macro>

