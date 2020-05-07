<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if dataReportInfos?exists&&dataReportInfos?size gt 0>
<div class="table-container">
	<div class="table-container-body">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th width="50px">序号</th>
					<th>问卷名称</th>
					<th width="200px">问卷收集日期</th>
					<th width="100px">发布者</th>
					<th width="100px">状态</th>
					<th width="250px">操作</th>
				</tr>
			</thead>
			<tbody>
				<#list dataReportInfos as info>
				<tr>
					<td>${info_index+1}</td>
					<td title="${info.title!}"><span <#if info.state == 2 || info.state == 3>class="color-blue"</#if>><#if info.title?exists&&info.title?length gt 20>${info.title?substring(0,20)}...<#else>${info.title!}</#if></span><#if info.state==1><span class="label label-warning">草稿</span></#if></td>
					<td>${info.startTime!}~${info.endTime!}</td>
					<td>${info.ownerName!}</td>
					<td>
						<#if info.state == 1>
							<span class="color-red">未发布</span>
						<#elseif info.state == 2 || info.state == 3>
							<span class="color-blue">收集中</span>
						<#else>
							已结束
						</#if>
					</td>
				    <td>
				    	<#if info.state == 1>
				    		<a class="color-blue mr10" href="#" onClick="editInfo('${info.id!}')">编辑</a>
				    		<a class="color-blue" href="javascript:void(0)" onClick="deleteOrRevoke('${info.id!}','1')">删除</a>
				    	<#else>
				    		<#if info.state == 2 || info.state == 3>
				    			<a class="color-blue mr10" href="#" onClick="deleteOrRevoke('${info.id!}','2')">撤销</a>
				    		</#if>
				    		<a class="color-blue mr10" href="javascript:void(0)" onClick="deleteOrRevoke('${info.id!}','1')">删除</a>
				    		<a class="color-blue mr10" href="#" onClick="updateInfo('${info.id!}','${info.state!}')">修改</a>
				    		<a class="color-blue mr10" href="#" onClick="copyInfo('${info.id!}')">复制</a>
				    		<a class="color-blue" href="#" onClick="showInfoHead('${info.id!}')">查看详情</a>
				    	</#if>
				    </td>
				</tr>
				</#list>
			</tbody>
		</table>
	</div>
</div>
<@htmlcom.pageToolBar container="#showInfoListDiv" class="noprint"/>
<#else>
	<div class="no-data-container">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无相关数据</p>
			</div>
		</div>
	</div>
</#if>
<div id="updateInfoDiv" class="layer layer-update">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">收集日期：</label>
				<div class="col-sm-8">
					<div class="input-group">
						<input class="form-control form datetimepicker" autocomplete="off" id="updateEndTime" name="updateEndTime" type="text" placeholder="结束时间">
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		if(date_timepicker &&date_timepicker!=null){
			date_timepicker.remove();
		}
		setTimeout(function(){
			date_timepicker = $('.datetimepicker').datetimepicker({
				language: 'zh-CN',
				minView: 'month',
    			format: 'yyyy-mm-dd',
    			zIndex: 10000,
    			autoclose: true
	    	})
		},100);
	});

	<#-- 删除或撤销  -->
	function deleteOrRevoke(infoId,type) {
		var title = "";
		if (type == 1) {
			title = "确定要删除吗？";
		} else {
			title = "撤销后已上传的文件都将被删除，确定要撤销吗？";
		}
		var delOrRevlayer = layer.confirm(title,{scrollbar:false},function(index){
			$.ajax({
				url:'${request.contextPath}/datareport/infomanage/deleteorrevoke',
				type:'post',
				data:{'infoId':infoId,'type':type},
				success:function(data) {
					layer.close(delOrRevlayer);
					var jsonO = JSON.parse(data);
					if(jsonO.success){
						layer.msg("操作成功!");
						showInfoList();
	        		}else{
	        			layerTipMsgWarn("操作失败","");
	        		}
				},
	 			error:function(XMLHttpRequest, textStatus, errorThrown){
	 				layer.close(delOrRevlayer);
	 			}
			});
		});
	}
	
	<#-- 修改弹框 -->
	function updateInfo(infoId,state) {
		$("#updateEndTime").val("");
		layer.open({
    		type: 1,
    		shade: 0.5,
    		title: '修改',
    		area: '650px',
    		btn: ['确定','取消'],
		  	zIndex: 1000,
    		yes: function(index){
		    	saveUpdateTime(infoId,index,state);
		  	},
    		content: $('.layer-update')
    	})
	}
	
	<#-- 修改时间 -->
	var editsubmit = false;
	function saveUpdateTime(infoId,index,state){
		if (editsubmit) {
			return;
		}
		var endTime = $("#updateEndTime").val();
		if (endTime == '') {
			layer.msg("结束时间不能为空！");
			return;
		}
		var nowTime = "${((.now)?string('yyyy-MM-dd'))?if_exists}";
		if (endTime<=nowTime) {
			layer.msg("结束时间不能小于等于当前时间！");
			return;
		}
		editsubmit = true;
		var updatelayer = layer.msg("修改中", {
  			icon: 16,
  			shade: 0.01,
  			time: 60*1000,
		});
		$.ajax({
			url:'${request.contextPath}/datareport/infomanage/updatetime',
			type:'post',
			data:{'infoId':infoId,"endTime":endTime,"state":state},
			success:function(data) {
				layer.close(index);
				layer.close(updatelayer);
				editsubmit = false;
				var jsonO = JSON.parse(data);
				if(jsonO.success){
					layer.msg("修改成功!");
					showInfoList();
        		}else{
        			layerTipMsgWarn("修改失败","");
        		}
			},
 			error:function(XMLHttpRequest, textStatus, errorThrown){
 				layer.close(dellayer);
 				editsubmit = false;
 			}
		});
	}
	
	<#-- 复制问卷 -->
	function copyInfo(infoId) {
		var copylayer = layer.confirm('确定要复制吗？',{scrollbar:false},function(index){
			$.ajax({
				url:'${request.contextPath}/datareport/infomanage/copyinfosave',
				data:{'infoId':infoId},
				dataType : 'json',
				success:function(data) {
					layer.close(copylayer);
					if(data.success){
						layer.msg("复制成功");
						showInfoList();
					}else{
						layerTipMsg(data.success,"复制失败",data.msg);
					}
				},
	 			error:function(XMLHttpRequest, textStatus, errorThrown){
	 				layer.close(dellayer);
	 			}
			});
		});
	}
	
	<#-- 查看详情 -->
	function showInfoHead(infoId) {
		var url = "${request.contextPath}/datareport/infomanage/showinfohead?infoId="+infoId;
		$("#dataInfoManageDiv").load(url);
	}
</script>