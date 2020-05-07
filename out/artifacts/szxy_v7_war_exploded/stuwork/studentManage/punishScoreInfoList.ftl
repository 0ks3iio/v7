<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
								<div class="table-container">
									<div class="table-container-header text-right">
										<#--a class="btn btn-white js-all-del">清空</a-->
										<a class="btn btn-white" onClick="doImport();">导入</a>
										<a class="btn btn-white js-edit">新增</a>
									</div>
									<div class="table-container-body">
										<table class="table table-striped table-hover">
											<thead>
												<tr>
													<th>学生姓名</th>
													<th>学号</th>
													<th>违纪类型</th>
													<th>违纪原因</th>
													<th>违纪扣分</th>
													<th>违纪时间</th>
													<th>操作</th>
												</tr>
											</thead>
											<tbody>
										    <#if dyStuPunishmentList?exists && dyStuPunishmentList?size gt 0>
											    <#list dyStuPunishmentList as item>
												<tr>
												    <input type="hidden" class="tid" value="${item.id!}">
													<td width="10%">${item.studentName!}</td>
													<td width="10%">${item.studentCode!}</td>
													<td width="15%" style="word-break:break-all;">${item.punishName!}</td>
													<td width="35%" style="word-break:break-all;">${item.punishContent!}</td>
													<td width="10%">${(item.score)?string('0.#')!}</td>
													<td width="10%">${(item.punishDate?string("yyyy-MM-dd"))!}</td>
													<td width="10%"><a href="" class="color-lightblue js-edit">编辑</a>
													<a href="" class="color-red js-del">删除</a></td>
												</tr>
												</#list>
											</#if>
											</tbody>
										</table>
										<#if dyStuPunishmentList?exists && dyStuPunishmentList?size gt 0>
  	                                        <@htmlcom.pageToolBar container="#showList" class="noprint"/>
                                        </#if>
									</div>
								</div>
								
<script>


function doImport(){
	$("#importDiv").load("${request.contextPath}/stuwork/studentManage/import/main?acadyear=" + $("#acadyearId").val() + "&semester=" + $("#semesterId").val());
}

function layerShow(){
     layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: '新增',
	    	area: '500px',
	    	btn: ['确定','取消'],
	    	yes: function(index){
			    savePunish();
			},
	    	content: $('.layer-add')
	    });
    }
    
$(function(){	
    // 新增
	$('.js-edit').on('click',function(e){
    	e.preventDefault();
    	var that = $(this);
    	var id = that.closest('tr').find('.tid').val();
    	if(id != undefined){
    	    var url =  "${request.contextPath}/stuwork/studentManage/punishScoreInfoEdit?id="+id;
    	}else{
    	    var url =  "${request.contextPath}/stuwork/studentManage/punishScoreInfoEdit";
    	}
		$("#punishEdit").load(url,function() {
			layerShow();
		});
    })
    
    $('.js-del').on('click', function(e){
		e.preventDefault();
		var that = $(this);
		var index = layer.confirm("确定删除吗？", {
			btn: ["确定", "取消"]
		}, function(){
		    var id = that.closest('tr').find('.tid').val();
		    $.ajax({
		        url:"${request.contextPath}/stuwork/studentManage/punishScoreInfoDelete",
		        data:{id:id},
		        dataType : 'json',
		        success : function(data){
		 	       if(!data.success){
		 		      layerTipMsg(data.success,"删除失败",data.msg);
		 		      return;
		 	       }else{
		 		      layer.closeAll();
				      layerTipMsg(data.success,"删除成功","");
				      searchList();
    		       }
		        },
	        });
			//that.closest('tr').remove();
			//layer.close(index);
		});
	});

	$('.js-all-del').on('click', function(e){
		e.preventDefault();
		var that = $(this);
		var index = layer.confirm("是否清空所选学年学期下的所有学生的违纪记录？", {
			btn: ["确定", "取消"]
		}, function(){
			var acadyear = $("#acadyearId").val();
			var semester = $("#semesterId").val();
			$.ajax({
				url: "${request.contextPath}/stuwork/studentManage/punishScoreInfoAllDelete",
				data: {
					"acadyear": acadyear,
					"semester": semester
				},
				dataType: 'json',
				success: function (data) {
					if (!data.success) {
						layerTipMsg(data.success, "删除失败", data.msg);
						return;
					} else {
						layer.closeAll();
						layerTipMsg(data.success, "删除成功", "");
						searchList();
					}
				},
			});
			//that.closest('tr').remove();
			//layer.close(index);
		});
	});
});
</script>	