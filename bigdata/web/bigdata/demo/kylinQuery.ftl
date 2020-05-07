<link href="${request.contextPath}/static/components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
 <link href="${request.contextPath}/static/components/datatables/media/css/jquery.dataTables.min.css" rel="stylesheet"/>
<script src="${request.contextPath}/static/components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${request.contextPath}/static/components/datatables/media/js/jquery.dataTables.min.js"></script>
<li><a href="javascript:void(0);" onclick="loadData()">重新加载数据</a></li>
<div class="box box-default">
	<div class="box-body">
		<div class="tab-content">
				<table id="multiDimensionTable" class="table table-bordered" style="text-align: center" width="100%">
				     <thead id="head" ></thead>
    				<tbody id="data"></tbody>
				</table>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
 	loadData();
});

var table;
var originalColumnDefs =[
 		{
 		targets:0,
 		createdCell:function(td, cellData, rowData, row, col) {
      			//第0列需要合并的行数然后移除未设置num的行
     			 var rowspan = rowData.num0; //拿到第一列需要合并的数据
      			if(rowspan == undefined) {
          			$(td).remove();
      			}else{
      				$(td).attr('rowspan', rowspan)
      			}
 			}
 		},
 		{
 		targets:1,
 		createdCell:function(td, cellData, rowData, row, col) {
      			//第0列需要合并的行数然后移除未设置num的行
     			 var rowspan = rowData.num1; //拿到第一列需要合并的数据
      			if(rowspan == undefined) {
          			$(td).remove();
      			}else{
      				$(td).attr('rowspan', rowspan)
      			}
 			}
 		},
 		{
 		targets:2,
 		createdCell:function(td, cellData, rowData, row, col) {
      			//第0列需要合并的行数然后移除未设置num的行
     			 var rowspan = rowData.num2; //拿到第一列需要合并的数据
      			if(rowspan == undefined) {
          			$(td).remove();
      			}else{
      				$(td).attr('rowspan', rowspan)
      			}
 			}
 		},
 		{
 		targets:3,
 		createdCell:function(td, cellData, rowData, row, col) {
      			//第0列需要合并的行数然后移除未设置num的行
     			 var rowspan = rowData.num3; //拿到第一列需要合并的数据
      			if(rowspan == undefined) {
          			$(td).remove();
      			}else{
      				$(td).attr('rowspan', rowspan)
      			}
 			}
 		},
 		{
 		targets:4,
 		createdCell:function(td, cellData, rowData, row, col) {
      			//第0列需要合并的行数然后移除未设置num的行
     			 var rowspan = rowData.num4; //拿到第一列需要合并的数据
      			if(rowspan == undefined) {
          			$(td).remove();
      			}else{
      				$(td).attr('rowspan', rowspan)
      			}
 			}
 		}
 ];
 
function loadData(){
     var dataList = []; 
     var columnList = [];
     var columnDefs =[];
     if(table){
    	 table.destroy();
         $("#head").empty();
         $("#data").empty();
    }
    $.post('${request.contextPath}/bigdata/frame/common/demo/kylin/data', '',function(result){
        var columnSize=parseInt(result.totalColumnSize);
        var dimRowSize =parseInt(result.dimRowSize);
       for(var i=0;i<result.data.length;i++){
        	  var dataObj ={};
	          for(var j=0;j<columnSize;j++){
	             dataObj[''+j] = result.data[i][j];
	      	 }
	      	 for (var ii = 0; ii < dimRowSize; ii++) {
	      	 	dataObj['num'+ii] =  result.data[i]['num'+ii];
	      	 }
	      	 dataList.push(dataObj);
       }
       for (var i = 0; i < columnSize; i++) {
            var columnObj = {};
            columnObj['data'] = i;
            columnObj['orderable'] = false;
            columnObj['searchable'] = false;
            columnList.push(columnObj);
        }
        
        $("#head").html( result.head);
        
        for (var i = 0; i < dimRowSize-1; i++) {
        	columnDefs.push(originalColumnDefs[i])
        }
        
        createTable(dataList,columnList,columnDefs);
    }, "json");
}

function createTable(dataList,columnList,columnDefs) {
    table= $("#multiDimensionTable").DataTable({
	        //因为需要多次初始化，所以需要设置允许销毁实例
	        "destroy": true,
			"paging": false,
	        "processing": true,
	        "serverSide": false,
	        "searching":false, //搜索栏
	        "info":false, //
	        "ordering":false, //全局定义是否启用排序，优先级比columns.orderable高
	        "language": {
		       "sProcessing": "处理中...",
		       "sZeroRecords": "没有匹配结果",
		       "sSearch": "搜索:",
		       "sLoadingRecords": "载入中..."
		   },
		   "data":dataList,
		   "columns":columnList,
		   "columnDefs":columnDefs
	});
}
</script>