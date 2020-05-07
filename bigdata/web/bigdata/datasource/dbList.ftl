<div class="box-body no-padding">
	<#if databaseList?exists&&databaseList?size gt 0>
		<div class="card-list clearfix report">		
			<#list databaseList as db>
			<div class="card-item">
				<div class="bd-view">
					<a href="javascript:void(0);">
						<div class="drive no-padding">
							<img src="${request.contextPath}/bigdata/v3/static/images/big-data/${db.thumbnail!}" alt="" class="sixteen-nine">
							<div class="coverage">
								<div class="vetically-center">
									<img src="${request.contextPath}/static/bigdata/images/remove.png" width="20" height="20" class="remove" onclick="deleteDB('${db.id!}', '${db.type!}')"/>
									<button class="btn btn-blue btn-long" onclick="editDB('${db.id!}', '${db.type!}')">编辑</button>
								</div>
							</div>
						</div>
						<div class="padding-side-20">
							<h3><#if db.name?length gt 12>${db.name?substring(0,12)}...<#else>${db.name!}</#if></h3>
						</div>
					</a>
				</div>
			</div>
			</#list>
		</div>
	<#else>
	<div class="table-container no-margin">
		<div class="table-container-body subscription">
			<table class="table table-bordered table-striped table-hover">
		<tr >
			<td  colspan="3" align="center">
			暂无数据库!
			</td>
		<tr>
			</table>
		</div>
	</div>
	</#if>
</div>
<script>
function editDB(id, type){
    if (type == "06" || type == "07" || type == "08" || type == "09" || type == "12") {
        editNoSqlDB(id);
    } else if (type == "10" || type == "11") {
        editFileDB(id);
	} else if (type == "13") {
		$('#dbButton').hide();
		router.go({
			path: '/bigdata/nosqlDatasource/edit?id='+id,
			name: '编辑非关系型数据库',
			level: 3
		}, function () {
			var url = "${request.contextPath}/bigdata/nosqlDatasource/edit?id="+id;
			$("#tableList").load(url);
		});
    } else {
		$('#dbButton').hide();
		router.go({
			path: '/bigdata/datasource/db/edit?id='+id,
			name: '编辑数据库',
			level: 3
		}, function () {
			var url = "${request.contextPath}/bigdata/datasource/db/edit?id="+id;
			$('#tableList').removeClass().addClass('box box-default');
			$("#tableList").load(url);
		});
	}
}

function addDB(type){
    if (type == "06" || type == "07" | type == "12") {
        addNoSqlDB(type);
	} else {	
        $('#dbButton').hide();
        router.go({
            path: '/bigdata/datasource/db/edit?type=' + type,
            name: '新增数据库',
            level: 3
        }, function () {
        	 var url = "${request.contextPath}/bigdata/datasource/db/edit?type=" + type;
        	$("#tableList").load(url);
        });
    }
}

function deleteDB(id, type){
    var url = '${request.contextPath}/bigdata/datasource/db/delete';
    if (type == "06" || type == "07" || type == "08" || type == "09"|| type == "12" || type == "13") {
        url = '${request.contextPath}/bigdata/nosqlDatasource/delete';
	} else if (type == "10" || type == "11") {
        url = '${request.contextPath}/bigdata/nosqlDatasource/deleteFileDB';
	}
	showConfirmTips("prompt","提示","您确定要删除该数据源吗？",function(){
		$.ajax({
	            url: url,
	            data:{
	              'id':id
	            },
	            type:"post",
	            dataType: "json",
	            success:function(data){
	            	layer.closeAll();
			 		if(!data.success){
			 			showLayerTips4Confirm('error',data.msg);
			 		}else{
			 			showLayerTips('success',data.msg,'t');
					    $("#tableList").load("${request.contextPath}/bigdata/datasource/list?dsType=1");
	    			}
	          },
	          error:function(XMLHttpRequest, textStatus, errorThrown){}
	    });
	});
}
function editNoSqlDB(id){
    $('#dbButton').hide();
    router.go({
        path: '/bigdata/nosqlDatasource/edit?id='+id,
        name: '编辑非关系型数据库',
        level: 3
    }, function () {
    	  var url = "${request.contextPath}/bigdata/nosqlDatasource/edit?id="+id;
		  $("#tableList").load(url);
    });
}

function editFileDB(id){
   $('#dbButton').hide();
   router.go({
        path: '/bigdata/nosqlDatasource/editFileDB?id='+id,
        name: '编辑文件',
        level: 3
    }, function () {
	   var url = "${request.contextPath}/bigdata/nosqlDatasource/editFileDB?id="+id;
	   $("#tableList").load(url);
    });
}

function addNoSqlDB(type){
    $('#dbButton').hide();
    router.go({
        path: '/bigdata/nosqlDatasource/edit?type='+type,
        name: '新增非关系型数据库',
        level: 3
    }, function () {
    	  var url = "${request.contextPath}/bigdata/nosqlDatasource/edit?type="+type;
		  $("#tableList").load(url);
    });
}

function addHbase() {
	$('#dbButton').hide();
	router.go({
		path: '/bigdata/nosqlDatasource/edit?type=13',
		name: '新增非关系型数据库',
		level: 3
	}, function () {
		var url = "${request.contextPath}/bigdata/nosqlDatasource/edit?type=13";
		$("#tableList").load(url);
	});
}

function addFileDB(type){
    $('#dbButton').hide();
    router.go({
        path: '/bigdata/nosqlDatasource/editFileDB?type='+type,
        name: '新增文件',
        level: 3
    }, function () {
    	  var url = "${request.contextPath}/bigdata/nosqlDatasource/editFileDB?type="+type;
		  $("#tableList").load(url);
    });
}
</script>