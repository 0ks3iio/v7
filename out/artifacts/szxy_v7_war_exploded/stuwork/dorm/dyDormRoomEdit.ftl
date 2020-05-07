<form id="submitForm">
<div class="layer layer-add">
	<div class="layer-content">
		<div class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>寝室楼</label>
				<div class="col-sm-8">
					<select name="buildingId" id="buildingId" nullable="false" class="form-control"  >
	                <#if buildingList?? && (buildingList?size>0)>
	                	<option value="">请选择</option>
	                    <#list buildingList as building>
	                    <option value="${building.id!}" <#if building.id==room.buildingId?default("")>selected="selected"</#if>>${building.name!}</option>
	                    </#list>
	                </#if> 
	                </select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>寝室类型</label>
				<div class="col-sm-8">
					 <select name="roomType" id="roomType" nullable="false"  class="form-control"  >
                    	<option value="">请选择</option>
                        <option value="1" <#if "1"==room.roomType?default("")>selected="selected"</#if>>男寝室</option>
                        <option value="2" <#if "2"==room.roomType?default("")>selected="selected"</#if>>女寝室</option>
                    </select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>寝室属性</label>
				<div class="col-sm-8">
					 <select name="roomProperty" id="roomProperty" nullable="false"  class="form-control"  >
                        <option value="1" <#if "1"==room.roomProperty?default("")>selected="selected"</#if>>学生寝室</option>
                        <option value="2" <#if "2"==room.roomProperty?default("")>selected="selected"</#if>>老师寝室</option>
                    </select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>寝室号</label>
				<div class="col-sm-8">
					<input type="text" name="roomName" nullable="false" class="form-control" maxlength="100" id="roomName"  value="${room.roomName!}">
					<input type="hidden" name="id" id="roomId" value="${room.id!}">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>容纳人数</label>
				<div class="col-sm-8">
					<input type="text" name="capacity" vtype="int" min="1" class="form-control" max="999" nullable="false"  maxlength="3" id="capacity" value="${room.capacity!}">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right"><font style="color:red;">*</font>楼层</label>
				<div class="col-sm-8">
					<input type="text" name="floor" vtype="int" min="1" max="99" class="form-control" nullable="false"  maxlength="3" id="floor" value="${room.floor!}">
				</div>
			</div>
		</div>
	</div>
</div>
</form>
<div class="layer-footer">
    <a href="javascript:" class="btn btn-lightblue" id="result-commit" onclick="doSaveRoom()">确定</a>
    <a href="javascript:" class="btn btn-lightblue" id="result-close">取消</a>
</div>
<script>
	$(function(){
		$(".layer-add").show();
		$("#result-close").on("click", function(){
		    layer.closeAll();
		 });
	});
	var isSubmit=false;
	function doSaveRoom(){
		if(isSubmit){
			return;
		}
		isSubmit = true;
		var check = checkValue('#submitForm');
		if(!check){
		 	isSubmit=false;
		 	return;
		}
		var ii = layer.load();
		var currentPageIndex=${currentPageIndex};
        var currentPageSize=${currentPageSize};
		var options = {
			url : "${request.contextPath}/stuwork/dorm/room/save",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit = false;
		 		}else{
		 			layer.closeAll();
					layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
				  	doRoomSearch(currentPageIndex,currentPageSize);
    			}
    			layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#submitForm").ajaxSubmit(options);
	}
</script>