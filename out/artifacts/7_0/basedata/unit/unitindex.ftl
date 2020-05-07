
<title>jqGrid - Ace Admin</title>

<link rel="stylesheet" href="${request.contextPath}/static/ace/css/ui.jqgrid.css" />
<script src="${request.contextPath}/static/ace/js/jqGrid/jquery.jqGrid.src.js"></script>
<script src="${request.contextPath}/static/ace/js/jqGrid/i18n/grid.locale-cn.js"></script>
<script src="${request.contextPath}/static/js/tool.js"></script>

<script src="${request.contextPath}/static/echarts/esl.js"></script>
<script src="${request.contextPath}/static/echarts/echarts.js"></script>


<!-- ajax layout which only needs content area -->
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div class="well well-sm">
			相关流程：
			<a href="http://www.trirand.com/blog/?page_id=6" target="_blank">
				单位管理
				<i class="fa bigger-110"></i>
			</a>
			<i class="fa fa-long-arrow-right blue" aria-hidden="true"></i>
			<a href="http://www.trirand.com/blog/?page_id=6" target="_blank">
				部门管理
				<i class="fa bigger-110"></i>
			</a>
			<i class="fa fa-long-arrow-right blue" aria-hidden="true"></i>
			<a href="#${request.contextPath}/basedata/teacher">
				教师管理
				<i class="fa bigger-110"></i>
			</a>
		</div>
		
		

		<div id="main" style="width:100%;height:300px;margin-bottom:10px;"></div>

		<div class="jqGrid_wrapper">
            <table id="dept-list"></table>
            <div id="dept-pager"></div>
        </div>
        <br>
		<div class="jqGrid_wrapper">
            <table id="user-list"></table>
            <div id="user-pager"></div>
        </div>

		<!-- PAGE CONTENT ENDS -->
	</div><!-- /.col -->
</div><!-- /.row -->

<div id="content"></div>


<script type="text/javascript">
    
</script>
    

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		//加载部门列表
		$("#dept-list").jqGrid({url:"${request.contextPath}/basedata/unit/402880944393B2C3014393BAC0580000/depts",
			datatype: "json",mtype:"GET",height:"auto",autowidth:true,shrinkToFit:true,rowNum:10,rowList:[10,20,30,100],
			viewrecords:true,hidegrid:true,
			caption: "部门信息",
			colNames:["", "ID", "部门名称","部门编号", "部门类型", "所属单位", "教师列表"],
			colModel:[
				{name:"_operation",fixed:true,width:60,sortable:false},
				{name:"id",width:90,sortable:false,hidden:true},
				{name:"deptName",width:90,sortable:false},
				{name:"deptCode",width:100,sortable:false},
				{name:"deptType",width:100,sortable:false, formatter:"select", editoptions:{value:"${mcodeSetting.getMcodeWithJqGrid("DM-JYBZ")}"}},
				{name:"unit.unitName",width:100,sortable:false},
				{name:"_showTeachers",width:50,sortable:false},
			],
			gridComplete: function () {
	          dealDataDept();
	        }, 
			jsonReader: {
				root: "rows", page: "page",total: "total",  records: "records", repeatitems: false,id: "id" 
            },
			pager:"#dept-pager"}
		);
		
		//加载用户列表
		$("#user-list").jqGrid({url:"${request.contextPath}/basedata/unit/402880944393B2C3014393BAC0580000/users",
			datatype: "json",mtype:"GET",height:"auto",autowidth:true,shrinkToFit:true,rowNum:10,rowList:[10,20,30,100],
			viewrecords:true,hidegrid:true,
			caption: "用户信息",
			colNames:["", "ID","账号","名称", "用户类型"],
			colModel:[
				{name:"operation",fixed:true,width:60,sortable:false},
				{name:"id",width:60,sorttype:"int",sortable:false},
				{name:"username",width:90,sortable:false},
				{name:"realName",width:100,sortable:false},
				{name:"ownerType",width:100,sortable:false, formatter:'select', editoptions:{value:'${mcodeSetting.mcodeSelectJqGrid("DM-QUERY-USERTYPE")}'}},
			],
			gridComplete: function () {
	          dealDataUser();
	        }, 
			jsonReader: {
				root: "rows", page: "page",total: "total",  records: "records", repeatitems: false,id: "id" 
            },
			pager:"#user-pager"}
		);
		
		require.config({
			paths : {
				'echarts' : '${request.contextPath}/static/echarts/echarts',
				'echarts/chart/bar' : '${request.contextPath}/static/echarts/chart/bar',
				'echarts/chart/line' : '${request.contextPath}/static/echarts/chart/line'
			}
		});
		
		var myChart;
		require([ 'echarts', 'echarts/chart/bar', 'echarts/chart/line'], function(ec) {
			// 基于准备好的dom，初始化echarts图表
			myChart = ec.init(document.getElementById('main'));
			$.ajax({
	    		url:'${request.contextPath}/basedata/unit/402880944393B2C3014393BAC0580000/statTeachersByDept',
	    		type:'get', beforeSend:function(XMLHttpRequest){},  
	    		success:function(data) {
	    			var opt = JSON.parse(data);
	    			// 为echarts对象加载数据 
					var ecConfig = require('echarts/config'); 
					myChart.setOption(opt);
					myChart.on(ecConfig.EVENT.CLICK, function(param){
				    var mes = '【' + param.type + '】';
				    if (typeof param.seriesIndex != 'undefined') {
				        mes += '  seriesIndex : ' + param.seriesIndex;
				        mes += '  dataIndex : ' + param.dataIndex;
				    }
				    var tt = param.data.name;
					    jQuery("#dept-list").jqGrid('setGridParam', {
					      url : "${request.contextPath}/basedata/unit/402880944393B2C3014393BAC0580000/depts?" + tt
					    }).trigger("reloadGrid");
					  
				});
	    		},
	     		error : function(XMLHttpRequest, textStatus, errorThrown) {  
	     			var text = syncText(XMLHttpRequest);
	     			swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
	    		}
	    	});
		});
		
		//resize
		$(window).bind("resize",function(){
			var width=$(".jqGrid_wrapper").width();
			$("#user-list").setGridWidth(width);
			$("#dept-list").setGridWidth(width);
			$("#main").width(width);
			myChart.resize();
		});
		
		function dealDataUser(){
			var ids = jQuery("#user-list").jqGrid('getDataIDs');
      		for (var i = 0; i < ids.length; i++) {
				var id = ids[i];
	          	var rowData = $("#user-list").getRowData(id);
	          	var editBtn = buttons(rowData.realName, "edit_btn_", id, "green", "fa-pencil");
	          	var trashBtn = buttons(rowData.realName, "del_btn_", id, "red", "fa-trash-o");
          		$("#user-list").jqGrid('setRowData', ids[i], { operation: "<div class=\"action-buttons\">" + editBtn + " " + trashBtn + "</div>" });
          
          		$("#edit_btn_" + id).on("click", function(){
          			var value = $(this).attr("value");
          			var index = layer.load(2);
          			$("#content").load('${request.contextPath}/basedata/unit/' + value + '/show',
          				function(){
          					layer.close(index);  
          					layerDiv('700px', '650px', $('#content'), '用户信息');
          				}
          			);
          		});
          
          		$("#del_btn_" + id).on("click", function(){
          			var value = $(this).attr("value");
          			var name = $(this).attr("name");
          			swal({title: "删除用户", html: true, 
						text: "是否要删除<strong><font color='red'>" + name + "</font></strong>？",   
						type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
						cancelButtonText: "否",showLoaderOnConfirm: true
					}, 
					function(){   
						$.ajax({
				    		url:'${request.contextPath}/basedata/user/' + value + '/delete',
				    		type:'delete', beforeSend:function(XMLHttpRequest){},  
				    		success:function(data) {
				    			swal({title: "操作成功!",
				    			text: data,type: "success",showConfirmButton: true,confirmButtonText: "是"},
				    			function(){$("#user-list").trigger("reloadGrid");});},
				     		error : function(XMLHttpRequest, textStatus, errorThrown) {  
				     			var text = syncText(XMLHttpRequest);
				     			swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
				    		}
				    	});
					});
				});
      		}
      		}
      		
      		function dealDataDept(){
      			var ids = jQuery("#dept-list").jqGrid('getDataIDs');
	      		for (var i = 0; i < ids.length; i++) {
					var id = ids[i];
		          	var rowData = $("#dept-list").getRowData(id);
		          	var editBtn = buttons(rowData.deptName, "edit_btn_", id, "green", "fa-pencil");
		          	var trashBtn = buttons(rowData.deptName, "del_btn_", id, "red", "fa-trash-o");
		          	var showTeachersUrl = "<a href='${request.contextPath}/basedata/dept/" + id + "/teacher'>查看</a>";
	          		$("#dept-list").jqGrid('setRowData', ids[i], { _showTeachers:showTeachersUrl, _operation: "<div class=\"action-buttons\">" + editBtn + " " + trashBtn + "</div>" });
	          
	          		$("#edit_btn_" + id).on("click", function(){
	          			var value = $(this).attr("value");
	          			var index = layer.load(2);
	          			$("#content").load('${request.contextPath}/basedata/dept/' + value + '/detail',
	          				function(){
	          					layer.close(index);  
	          					layerDiv('', '360px', $('#content'), '部门信息');
	          				}
	          			);
	          		});
	          
	          		$("#del_btn_" + id).on("click", function(){
	          			var value = $(this).attr("value");
	          			var name = $(this).attr("name");
	          			swal({title: "删除部门", html: true, 
							text: "是否要删除<strong><font color='red'>" + name + "</font></strong>？",   
							type: "warning", showCancelButton: true, closeOnConfirm: false, confirmButtonText: "是",
							cancelButtonText: "否",showLoaderOnConfirm: true
						}, 
						function(){   
							$.ajax({
					    		url:'${request.contextPath}/basedata/dept/' + value + '/delete',
					    		data: "", type:'delete', beforeSend:function(XMLHttpRequest){},  
					    		success:function(data) {
					    			swal({title: "操作成功!",
					    			text: data,type: "success",showConfirmButton: true,confirmButtonText: "是"},
					    			function(){$("#dept-list").trigger("reloadGrid");});},
					     		error : function(XMLHttpRequest, textStatus, errorThrown) {  
					     			var text = syncText(XMLHttpRequest);
					     			swal({title: "操作失败!",text: text, type:"error",showConfirmButton: true});
					    		}
					    	});
						});
					});
	      		}
      		}
	});
</script>
