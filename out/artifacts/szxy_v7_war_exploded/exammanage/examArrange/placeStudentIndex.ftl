
<#if isgk=="1">
	<#if (groupList?exists && groupList?size>0)&&(placeList?exists && placeList?size>0)>
    	<div class="filter ">
		<div class="filter-item">
			<span class="filter-name">科目组：</span>
			<div class="filter-content">
				<select name="groupId" id="groupId" class="form-control" onChange="showGroupPlaceList()">
						<#list groupList as group>
						<option  value = "${group.id!}" <#if group.id == groupId>selected</#if>>${group.groupName!}</option>
						</#list>
				</select>
			</div>
		</div>
		<div class="filter-item">
			<span class="filter-name">考场编号：</span>
			<div class="filter-content">
				<select name="examPlaceId" id="examPlaceId" class="form-control" onChange="showPlaceStudentList()">
					<#list placeList as place>
					<option  value = "${place.id!}" >${place.examPlaceCode!}</option>
					</#list>
				</select>
			</div>
		</div>
    </div>
	<#else>
        <div class="no-data-container">
            <div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
				</span>
				<#if (groupList?exists && groupList?size>0)>
                    <h3 class="no-data-body">
                        还未设置过考试场地
                    </h3>
				<#else >
                    <h3 class="no-data-body">
                        还未设置过考试科目
                    </h3>
				</#if>
            </div>
        </div>
	</#if>
<#else>

	<#if (placeList?exists && placeList?size>0)>
    <div class="filter ">
    <div class="filter-item">
        <span class="filter-name">考场编号：</span>
    <div class="filter-content">
		<select name="examPlaceId" id="examPlaceId" class="form-control" onChange="showPlaceStudentList()">
			<#list placeList as place>
				<option  value = "${place.id!}" >${place.examPlaceCode!}</option>
			</#list>
		</select>
	</div>
	</div>
	<#else>
		<div class="no-data-container">
			<div class="no-data">
						<span class="no-data-img">
							<img src="${request.contextPath}/static/images/classCard/no-tutor-project.png" alt="">
						</span>
				<h3 class="no-data-body">
					还未设置过考试场地
				</h3>
			</div>
		</div>
	</#if>
</#if>
<#if (placeList?exists && placeList?size>0)&&(placeList?exists && placeList?size>0)>
	<#if canEdit?exists && canEdit>
	<div class="filter ">
		<div class="filter-item">
			<a href="javascript:" class="btn btn-blue js-exportAuto" id="js-exportAuto" onclick="exportAuto();">自动安排</a>
			<a href="javascript:" class="btn btn-white js-clearAll" id="js-clearAll" onclick="clearAll();">清除所有</a>
		</div>
	</div>
	</#if>
</#if>
<div class="table-container">
	<div class="table-container-body" id="placeStudentList">
	</div>
</div>
<div class="layer layer-chooseType">
	<div class="layer-body">
		<div class="filter clearfix">
			<div class="filter-item block">
				<label for="" class="filter-name">方式：</label>
				<div class="filter-content">
					<label class="pos-rel">
						<input type="radio" class="wp form-control form-radio" name="chooseType" value="1" checked>
						<span class="lbl">按考号安排</span>
					</label>
					<label class="pos-rel">
						<input type="radio" class="wp form-control form-radio" name="chooseType" value="0" >
						<span class="lbl">随机安排</span>
					</label>
				</div>
			</div>
		</div>
	</div>
</div>


<script>
$(function(){
	showPlaceStudentList();
})
function showPlaceStudentList(){
    var examPlaceId=$("#examPlaceId").val();
    if(examPlaceId==""||typeof (examPlaceId)=="undefined"){
        $("#placeStudentList").remove();
    }else{
		<#if isgk=="1">
			<#if (groupList?exists && groupList?size>0)>
	            var groupId=$('#groupId').val();
                if(examPlaceId!=""&&typeof (examPlaceId)!="undefined") {
                    var url = '${request.contextPath}/exammanage/examArrange/placeStudentList/page?examId=${examId!}&examPlaceId=' + examPlaceId + '&groupId=' + groupId;
                    $("#placeStudentList").load(url);
                }
            </#if>
		<#else>
            var url =  '${request.contextPath}/exammanage/examArrange/placeStudentList/page?examId=${examId!}&examPlaceId='+examPlaceId;
	        $("#placeStudentList").load(url);
		</#if>
    }
}

function showGroupPlaceList(){
	var examPlaceId=$("#examPlaceId").val();
	var groupId = $("#groupId").val();
	var url =  '${request.contextPath}/exammanage/examArrange/placeStudentIndex/page?examId=${examId!}&groupId='+groupId;
	$("#showTabDiv").load(url);
}

var isSubmit=false;
function clearAll(){
	if(isSubmit){
		isSubmit=true;
	}
	isSubmit=true;
	$("#js-clearAll").addClass("disabled");
	$.ajax({
	    url:'${request.contextPath}/exammanage/examArrange/clearAll',
	    data: {'examId':'${examId!}'},  
	    type:'post',
	    dataType : 'json',  
	    success:function(data) {
	    	if(data.success){
	 			layer.closeAll();
				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				itemShowList('3');
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#js-clearAll").removeClass("disabled");
	 			isSubmit=false;
			}
	    }
	});
}

/**
function exportAuto(){
	if(isSubmit){
		isSubmit=true;
	}
	isSubmit=true;
	$("#js-exportAuto").addClass("disabled");
	$.ajax({
	    url:'${request.contextPath}/exammanage/examArrange/saveExportAuto',
	    data: {'examId':'${examId!}'},  
	    type:'post',
	    dataType : 'json',  
	    success:function(data) {
	    	if(data.success){
	 			layer.closeAll();
				layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				itemShowList('3');
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
	 			$("#js-exportAuto").removeClass("disabled");
	 			isSubmit=false;
			}
	    }
	});
}
**/

function exportAuto(){
	var index = layer.open({
		type: 1,
        shade: .5,
        area: '360px',
        title: ['选择','font-size:20px;'],
        move: true,
        btn: ['确定','取消'],
        btnAlign: 'C',
		content: $('.layer-chooseType'),
		yes:function(index,layerDiv){
			var layerIndex = layer.load();
			var chooseType=$(layerDiv).find("input[name='chooseType']:checked").val();
			if(isSubmit){
				return;
			}
            layer.close(index);
			isSubmit=true;
			$.ajax({
//			    url:'${request.contextPath}/exammanage/examArrange/examineeNumberAutoSave',
			    url:'${request.contextPath}/exammanage/examArrange/saveExportAuto',
			    data: {'examId':'${examId!}','chooseType':chooseType},  
			    type:'post',  
			    success:function(data) {
			    	var jsonO = JSON.parse(data);
			    	if(jsonO.success){
			    		// 显示成功信息
					   layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
			 			isSubmit=false;
			 			//showNumberList();
			 			itemShowList('3');
			 		}
			 		else{
						layerTipMsg(jsonO.success,"失败",jsonO.msg);
						isSubmit=false;
					}
					layer.close(layerIndex);
			    }
			});
			
		},
		btn2:function(index){
			layer.close(index);
		}
	});
}

</script>

