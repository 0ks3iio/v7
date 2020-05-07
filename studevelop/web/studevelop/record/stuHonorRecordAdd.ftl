<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
	<input type="hidden" name="acadyear" id="acadyear" value="${acadyear!}">
	<input type="hidden" name="semester" id="semester" value="${semester!}">
	<input type="hidden" name="stuId" id="stuId" value="${stuId!}">
	<table class="table table-bordered table-striped table-hover no-margin">
		<tr class="first">
        	<th colspan="4" style="text-align:center;">星级人物</th>
        </tr>
          <tr>
          	 <th style="width:100px">
          	 	获得日期：
          	 </th>	
          	 <td>
          	 	<div class="filter-item">
					<div class="filter-content">
						<div class="input-group">
							<input class="form-control date-picker" vtype="data" style="width: 120px;height:28px;" type="text" name="honorRecordXJRW.giveDate" id="XJRWDate" placeholder="获得时间">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
						</div>
					 </div>
				</div>
          	 </td>
          </tr>
		  <tr>
		  	 <th>
		  	               荣誉名称：
		  	 </th>
		  	 <td>
		  	     ${mcodeSetting.getMcodeCheckbox('DM-XJRW','','XJRWhonorLevel')} 
		  	 </td> 
          </tr>
          <tr>
            <th>
          	 	备注：
          	 </th>
          	 <td>
          		<textarea style="width:435px;" id="XJRWremark" name="honorRecordXJRW.remark" maxLength="100" rows="2" cols="64"></textarea>
          	 </td>          
          </tr>
          <tr class="first">
        	<th colspan="4" style="text-align:center;">七彩阳光卡</th>
          </tr>
          <tr>
          	<th>
          	 	  获得日期：
          	 </th>	
          	 <td>
          	 	<div class="filter-item">
					<div class="filter-content">
						<div class="input-group">
							<input class="form-control date-picker" vtype="data" style="width: 120px;height:28px;" type="text" name="honorRecordQCYGK.giveDate" id="QCYGKDate" placeholder="获得时间">
							<span class="input-group-addon">
								<i class="fa fa-calendar bigger-110"></i>
							</span>
						</div>
					 </div>
				</div>
          	 </td>
          </tr>
		  <tr>
		  	 <th>
		  	              荣誉名称：
		  	 </th>
		  	 <td>
		  	    <label><input type="checkbox" id="QCYGKhonorLevel_01" name="QCYGKhonorLevel" value="01" title="聪慧博学" class="ace" /><span class='lbl'>聪慧博学</span></label>
		  	 	<label><input type="checkbox" id="QCYGKhonorLevel_02" name="QCYGKhonorLevel" value="02" title="领袖气质" class="ace" /><span class='lbl'>领袖气质</span></label>
		  	 	<label><input type="checkbox" id="QCYGKhonorLevel_03" name="QCYGKhonorLevel" value="03" title="健康体魄" class="ace" /><span class='lbl'>健康体魄</span></label>
		  	 	<label><input type="checkbox" id="QCYGKhonorLevel_04" name="QCYGKhonorLevel" value="04" title="阳光心态" class="ace" /><span class='lbl'>阳光心态</span></label>
		  	 	<label><input type="checkbox" id="QCYGKhonorLevel_05" name="QCYGKhonorLevel" value="05" title="品格健全" class="ace" /><span class='lbl'>品格健全</span></label>
		  	 	<label><input type="checkbox" id="QCYGKhonorLevel_06" name="QCYGKhonorLevel" value="06" title="创新实践" class="ace" /><span class='lbl'>创新实践</span></label>
		  	 	<label><input type="checkbox" id="QCYGKhonorLevel_07" name="QCYGKhonorLevel" value="07" title="艺术素养" class="ace" /><span class='lbl'>艺术素养</span></label>  
		  	 </td> 
          </tr>
          <tr>
          	 <th>
          	 	备注：
          	 </th>
          	 <td>
          		<textarea style="width:435px;" id="QCYGKremark" name="honorRecordQCYGK.remark" maxLength="100" rows="2" cols="64" ></textarea>
          	 </td>
          </tr>
	</table>		
</form>
</div>	
	<div class="layer-footer">
    <button class="btn btn-lightblue" id="arrange-commit">确定</button>
    <button class="btn btn-grey" id="arrange-close">取消</button>
    </div>

<script>
	$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2'
		};
		initCalendarData("#myDiv",".date-picker",viewContent);
		//初始化多选控件
		initChosenMore("#myDiv");
		$('.date-picker').next().on("click", function(){
			$(this).prev().focus();
		});
	});
	
	$("#arrange-close").on("click", function(){
    	doLayerOk("#arrange-commit", {
    	redirect:function(){},
    	window:function(){layer.closeAll()}
    	});     
 	});
 	
 	var isSubmit=false;
 	$("#arrange-commit").on("click", function(){
 		if(isSubmit){
 			return;
 		}
 		
 		isSubmit=true;
 		
		if(!checkValue('#subForm')){
 			isSubmit=false;
 			return;
 		} 
 		
 		var levelXjrw = $('input[name="XJRWhonorLevel"]:checked');
		var levelQcygk = $('input[name="QCYGKhonorLevel"]:checked');
		if(levelXjrw.length == 0 && levelQcygk.length == 0){
			layerTipMsg(false,"提示!","没有需要提交的内容！");
			isSubmit=false;
			return;
		}
		
	    var giveDateXjrw = $("#XJRWDate").val();
		if(levelXjrw.length != 0){
	    	if(giveDateXjrw == "") {
	    		layerTipMsg(false,"提示!","星级人物的获得日期不能为空！");
				isSubmit=false;
	    		return;
	    	}
		}
		
	    var giveDateQcygk = $("#QCYGKDate").val();
		if(levelQcygk.length != 0){
	    	if(giveDateQcygk == "") {
	    		layerTipMsg(false,"提示!","七彩阳光卡的获得日期不能为空！");
				isSubmit=false;
	    		return;
	    	}
		}

		var XJRWremark = $("#XJRWremark").val();
		var QCYGKremark = $("#QCYGKremark").val();
		var gg = {"giveDateXjrw":giveDateXjrw,"giveDateQcygk":giveDateQcygk,"XJRWremark":XJRWremark,"QCYGKremark":QCYGKremark};
		var xq = "?levelXjrw="+levelXjrw+"&levelQcygk="+levelQcygk;
		var options = {
			url : "${request.contextPath}/studevelop/honorRecord/save"+xq,
			type : "POST",
			dataType : 'json',
			data : gg ,
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
		 			return;
		 		}else{
		 			layer.closeAll();
				  	changeStuId();
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#subForm").ajaxSubmit(options);
		
 	});
</script>
    