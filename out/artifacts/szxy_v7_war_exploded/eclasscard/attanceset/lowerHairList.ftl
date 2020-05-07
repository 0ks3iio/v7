<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#import "/fw/macro/popupMacro.ftl" as popupMacro>
<div class="filter">
	<div class="filter-item"><a class="btn btn-blue" href="javascript:void(0);" onclick="gobacktoPic()">返回</a></div>
</div>
<table id="example" class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th>设备号</th>
			<th>用途</th>
			<th>安装场地</th>
			<th>班牌照片数/总数</th>
			<th>下发班级</th>
			<th>状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#if eccInfos?exists&&eccInfos?size gt 0>
          	<#list eccInfos as item>
				<tr>
					<td>${item.name!}</td>
					<td width="150">${usedForMap[item.type!]!}</td>
					<td width="150">${item.placeName!}</td>
					<td><span class="<#if item.currentFaceNum==item.allNum><#else>color-red</#if>">${item.currentFaceNum!}</span>/${item.allNum!}</td>
					<td>${item.classNames!}</td>
					<td width="150">
					<#if item?exists&&item.status == 2>
					<i class="fa fa-circle color-green"></i> 在线</span><#else>
					<span><i class="fa fa-circle color-red"></i> 离线</span></#if></td>
					<td width="150">
					<a href="javascript:void(0);" onclick="lowerHair('${item.id!}')" >下发</a>
					<a href="javascript:void(0);" onclick="editFaceSet('${item.id!}','${item.classIds!}')" >选择范围</a>
					</td>
				</tr>
      	    </#list>
  	    <#else>
			<tr >
				<td  colspan="88" align="center">
				暂无数据
				</td>
			<tr>
        </#if>
	</tbody>
</table>
<#if eccInfos?exists&&eccInfos?size gt 0>
	<@htmlcom.pageToolBar container="#tabList" class="noprint"/>
</#if>
<!-- S 编辑班牌 -->
<div id="cardEditLayer" class="layer layer-edit">
</div><!-- E 编辑班牌 -->
<div id="choiseClzPlace" class="layer layer-selection">
</div><!-- E 选择班级-场地-->

<div style="">
	<@popupMacro.selectMoreClass  clickId="eclassSetClassIds" columnName="班级(多选)" dataUrl="${request.contextPath}/common/div/class/popupData" id="eclassSetClassIds" name="eclassSetClassIds" dataLevel="3" type="duoxuan" recentDataUrl="${request.contextPath}/common/div/type/recentData" resourceUrl="${resourceUrl}" handler="saveClasses()">
		<div class="input-group">
			<input type="hidden" id="eclassSetClassIds" name="eclassSetClassIds" value="">
		</div>
	</@popupMacro.selectMoreClass>
</div>

<script>
$(function(){
	showBreadBack(gobacktoPic,true,"照片上传");
    $('.js-edit').on('click',function(e){
    	e.preventDefault();
    	var id = $(this).find('input').val();
    	var url =  '${request.contextPath}/eclasscard/manage/edit?id='+id;
		$("#cardEditLayer").load(url,function() {
			  layerShow();
			});
    })
})
function gobacktoPic(){
	showList('5');
}
function layerShow(){
     layer.open({
	    	type: 1,
	    	shade: 0.5,
	    	title: '编辑',
	    	area: '400px',
	    	btn: ['确定','取消'],
	    	yes: function(index){
			    saveECLassCard(index);
			  },
	    	content: $('.layer-edit')
	    })
    }
    
var isSubmit=false; 
function manageDelete(id) {
	layer.confirm('删除班牌会将其相关的数据全部删除，确定要删除吗？', function(index){
		if(isSubmit){
        	return;
    	}
    	isSubmit = true;
		$.ajax({
			url:'${request.contextPath}/eclasscard/manage/delete',
			data: {'id':id},
			type:'post',
			success:function(data) {
				layer.close(index);
				var jsonO = JSON.parse(data);
	 			if(jsonO.success){
	 				showList();
	 			}else{
	 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
	 				isSubmit = false;
				}
			},
 			error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	})
}
function lowerHair(id) {
	clearInterval(interval_timer);
	$.ajax({
		url:'${request.contextPath}/eclasscard/face/lower/hair/todevice',
		data: {'cardId':id},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
 			if(jsonO.success){
 				layer.msg("下发中，班牌端正在同步，若照片数较多，同步时间较长，请稍后查看下发结果",{time:5000});
				interval_timer = setTimeout(function(){
					var url =  '${request.contextPath}/eclasscard/face/lower/hair';
					$("#tabList").load(url);
				},5000); 
 			}else{
 				layerTipMsg(jsonO.success,"失败",jsonO.msg);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

var cInfoId="";
function editFaceSet(id,oldClassId){
	cInfoId=id;
	$('#eclassSetClassIds').val(oldClassId);
	$('#eclassSetClassIds').click();
}



function saveClasses(){
	var classesId=$("#eclassSetClassIds").val();
	$.ajax({
		url:"${request.contextPath}/eclasscard/face/setSave",
		data:{"infoId":cInfoId, "classIds":classesId},
		dataType: "json",
		success: function(data){
			if(data.success){
	 			layer.msg(data.msg, {offset: 't',time: 2000});
				freshList1();
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
			}
		}
	});
}

function freshList1(){
	var url =  '${request.contextPath}/eclasscard/face/lower/hair';
	$("#tabList").load(url);
}

</script>