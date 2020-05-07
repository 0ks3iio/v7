<title>部门管理</title>
<#import "/fw/macro/webmacro.ftl" as w>
<#import "/fw/macro/treemacro.ftl" as treemacro>
<#-- jqGrid报表引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<script src="${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js"></script>
<script src="${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js"></script>

<#-- ztree引入文件 -->
<link rel="stylesheet" href="${request.contextPath}/static/ztree/zTreeStyle/zTreeStyle.css" />
<script src="${request.contextPath}/static/ztree/js/jquery.ztree.all-3.5.min.js"></script>

<link rel="stylesheet" href="${request.contextPath}\static\ace\css\daterangepicker.css" />
<#-- sweetalert引入文件  -->
<link rel="stylesheet" href="${request.contextPath}/static/sweetalert/sweetalert.css" />
<script src="${request.contextPath}/static/sweetalert/sweetalert.min.js"></script>

<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-lg-12 col-md-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			相关功能：
			<@w.pageRef url="${request.contextPath}/basedata/teacher/index/page" name="教师管理" />	

		</div>
	</div>
</div>

<div class="row right">
	<div class="col-sm-12">
		<@w.btn btnId="btn-addDept" btnValue="新增部门" btnClass="fa-plus" />
	</div>
</div>

<div class="row">
	<div class="col-sm-3 col-lg-3">
		<@treemacro.deptForUnitInsetTree id="deptTree" height="550" class="widget-color-blue2" click="onTreeClick">
			<div class="widget-header">
				<h4 class="widget-title lighter smaller">请选择部门</h4>
			</div>
		</@treemacro.deptForUnitInsetTree>
	</div>
	
	<div class="col-sm-9 col-lg-9" id="deptGridDiv">
		<div class="jqGrid_wrapper">
            <table id="deptList"></table>
            <div id="deptPager"></div>
        </div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">

	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		//加载部门树
		<@deptList />
		
		// 新增操作
		$("#btn-addDept").on("click", function(){
			indexDiv = layerDivUrl("${request.contextPath}/basedata/dept/add/page");
		});
		
		//页面大小变化时的操作
		$(window).bind("resize",function(){
			var width = $(".row").width();
			var jqwidth=$(".jqGrid_wrapper").width();
			$("#deptList").setGridWidth(jqwidth);
		});

	});
	
	// 树节点点击操作，函数名称固定
	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type=="unit"){
			//单位
			jQuery("#deptList").jqGrid('setGridParam', {
			url : "${request.contextPath}/basedata/unit/"+treeNode.id+"/depts"
			    }).trigger("reloadGrid",[{page:1}]);
		}else{
			//部门
			jQuery("#deptList").jqGrid('setGridParam', {
			url : "${request.contextPath}/basedata/unit/${unitId!}/depts?deptId="+treeNode.id
			    }).trigger("reloadGrid",[{page:1}]);
		}
		
		$(window).trigger("resize");
	}
	
	
</script>

<#-- 部门列表 -->
<#macro deptList>
var rowList = [10, 50, 100, 200];
var rowNum = 10;
$("#deptList").jqGrid({
	url:"${request.contextPath}/basedata/unit/${unitId!}/depts",
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
	emptyrecords:'没有数据',//当空记录时显示
	caption: "部门信息",
	colModel:[
		{name:"_operation",label:"操作",fixed:true,width:80,sortable:false},
		{name:"deptName",label:"名称",width:90,sortable:false},
		{name:"deptTel",label:"部门电话",width:90,sortable:false},
		{name:"id",width:90,sortable:false,hidden:true},
	],
	gridComplete: function () {
	<#-- 处理部门操作，譬如增加删除和编辑按钮 -->
      dealDepts();
      updatePagerIcons(this);
    }, 
	jsonReader: {
		id: "id" 
	},
	pager:"#deptPager",
	pagerpos:'center',//页码栏位置
	recordpos:'right',//记录信息栏位置
	pgbuttons:true,//是否显示上下翻页按钮
	pginput:true//是否显示跳转页面的输入框
	
	}
);
		
function dealDepts(){
	var ids = jQuery("#deptList").jqGrid('getDataIDs');
	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
      	var rowData = $("#deptList").getRowData(id);
      	var editBtn = buttons(rowData.deptName, "edit_btn_", id, "green", "fa-pencil");
      	var trashBtn = buttons(rowData.deptName, "del_btn_", id, "red", "fa-trash-o");
      	var btns = "";
      	btns = editBtn + " " + trashBtn;
  		$("#deptList").jqGrid('setRowData', ids[i], {_operation: "<div class=\"action-buttons\">" + btns + "</div>" });
  		
  		$("#edit_btn_" + id).on("click", function(){
  			var value = $(this).attr("value");
  			var url =  '${request.contextPath}/basedata/dept/' + value + '/detail/page';
  			indexDiv = layerDivUrl(url);
  		});

  		$("#del_btn_" + id).on("click", function(){
  			var value = $(this).attr("value");
  			var name = $(this).attr("name");

  			swal({title: "删除部门", html: true, 
				text: "是否要删除<strong><font color='red'>" + name + "</font></strong>？",   
				type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
				cancelButtonText: "否",showLoaderOnConfirm: true,animation:false
			}, 
			function(){   
				$.ajax({
		    		url:'${request.contextPath}/basedata/dept/' + value + '/delete',
		    		data: "", type:'delete', beforeSend:function(XMLHttpRequest){},  
		    		success:function(data) {
		    			var jsonO = JSON.parse(data);
				 		if(jsonO.success){
				 			swal({title: "操作成功!",
		    					text: jsonO.msg,type: "success",showConfirmButton: true,confirmButtonText: "确定", timer:500},
		    					function(){
		    						$("#deptList").trigger("reloadGrid");
									//$("#deptTree").reloadTreeData("#deptList", "${unitId!}");
									reloadTree();
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
		     			swal({title: "操作失败!",text: "请联系管理员!", type:"error",showConfirmButton: true});
		    		}
		    	});
			});
		});
	}
}
</#macro>
