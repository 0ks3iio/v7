<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
	<div class="layer-body">
		<div class="filter clearfix"> 
			<div class="filter clearfix">
			     <#if type = '1'>
				    <input type="hidden" name="acadyear" value="${acadyear!}"/>
				    <input type="hidden" name="semester" value="${semester!}"/>
				    <input type="hidden" name="stuid" value="${stuid!}"/>
				    <input type="hidden" name="schid" value="${schid!}"/>
				    <div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>奖励名称：</label>
						<div class="filter-content">
							<input type="text" name="rewardsname" nullable="false" maxLength="60" id="rewardsname" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
					    <label for="" class="filter-name"><span style="color:red">*</span>奖励级别：</label>
					    <div class="filter-content">
						   <select name="rewardslevel" nullable="false" id="rewardslevel" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-JLJB", '', "1")}
						   </select>
					    </div>
				    </div>
				    
				    <div class="filter-item">
					    <label for="" class="filter-name"><span style="color:red">*</span>奖励类别：</label>
					    <div class="filter-content">
						   <select name="rewardstype"  id="rewardstype" nullable="false" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-XSHJLB", '', "1")}
						   </select>
					    </div>
				    </div>
				    
				    <div class="filter-item">
						<label for="" class="filter-name">奖励文号：</label>
						<div class="filter-content">
							<input type="text" name="filecode" maxLength="24" id="filecode" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>获奖日期：</label>
						<div class="filter-content">
					    <div class="input-group">
						<input class="form-control date-picker" vtype="data" style="width: 130px" type="text" nullable="false" name="rewardsdate" id="rewardsdate" placeholder="获奖日期" value="">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					    </div>
					     </div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">获奖名次：第</label>
						<div class="filter-content">
							<input type="text" name="rewardPosition" id="rewardPosition" vtype="int" maxLength="6" class="form-control col-xs-10 col-sm-10 col-md-10 " style="width:168px;"/>名
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>颁奖单位：</label>
						<div class="filter-content">
							<input type="text" name="rewardsunit" id="rewardsunit" nullable="false" maxLength="60" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">颁奖原因：</label>
						<div class="filter-content">
							<input type="text" name="rewardsreason" maxLength="80" id="rewardsreason" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">颁奖金额：</label>
						<div class="filter-content">
							<input type="text" name="money" vtype="number" id="money" maxLength="5" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					
					
                   <div class="filter-item">
						<label for="" class="filter-name">备注：</label>
						<div class="filter-content">
							<textarea name="remark" maxLength="200" id="remark" cols="63" rows="4" style="width:494px;" class="form-control col-xs-10 col-sm-10 col-md-10 " value=""></textarea>
						</div>
				  </div>
				 <#else>
				    <input type="hidden" name="acadyear" value="${stuDevelopRewards.acadyear!}"/>
				    <input type="hidden" name="semester" value="${stuDevelopRewards.semester!}"/>
				    <input type="hidden" name="stuid" value="${stuDevelopRewards.stuid!}"/>
				    <input type="hidden" name="schid" value="${stuDevelopRewards.schid!}"/>
				    <input type="hidden" name="id" value="${stuDevelopRewards.id!}"/>
				    <div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>奖励名称：</label>
						<div class="filter-content">
							<input type="text" name="rewardsname" maxLength="60" id="rewardsname"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopRewards.rewardsname!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
					    <label for="" class="filter-name"><span style="color:red">*</span>奖励级别：</label>
					    <div class="filter-content">
						   <select name="rewardslevel" id="rewardslevel" nullable="false" class="form-control" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-JLJB", '${stuDevelopRewards.rewardslevel!}', "1")}
						   </select>
					    </div>
				    </div>
				    
				    <div class="filter-item">
					    <label for="" class="filter-name"><span style="color:red">*</span>奖励类别：</label>
					    <div class="filter-content">
						   <select name="rewardstype" id="rewardstype" class="form-control" notnull="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-XSHJLB", '${stuDevelopRewards.rewardstype!}', "1")}
						   </select>
					    </div>
				    </div>
				    
				    <div class="filter-item">
						<label for="" class="filter-name">奖励文号：</label>
						<div class="filter-content">
							<input type="text" name="filecode" id="filecode" maxLength="24" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopRewards.filecode!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>获奖日期：</label>
						<div class="filter-content">
					    <div class="input-group">
						<input class="form-control date-picker" vtype="data" style="width: 130px" type="text" nullable="false" name="rewardsdate" id="rewardsdate" placeholder="获奖日期" value="${(stuDevelopRewards.rewardsdate?string('yyyy-MM-dd'))}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					    </div>
					     </div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">获奖名次：第</label>
						<div class="filter-content">
							<input type="text" name="rewardPosition" id="rewardPosition" vtype="int" maxLength="6" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopRewards.rewardPosition!}" style="width:168px;"/>名
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>颁奖单位：</label>
						<div class="filter-content">
							<input type="text" name="rewardsunit" id="rewardsunit" nullable="false" maxLength="60" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopRewards.rewardsunit!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">颁奖原因：</label>
						<div class="filter-content">
							<input type="text" name="rewardsreason" id="rewardsreason" maxLength="80" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopRewards.rewardsreason!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">颁奖金额：</label>
						<div class="filter-content">
							<input type="text" name="money" id="money" maxLength="5" vtype="number"  class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopRewards.money!}" style="width:168px;"/>
						</div>
					</div>
					
					
					
                   <div class="filter-item">
						<label for="" class="filter-name">备注：</label>
						<div class="filter-content">
							<textarea name="remark" id="remark" maxLength="200" cols="63" rows="4" style="width:494px;" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopRewards.remark!}">${stuDevelopRewards.remark!}</textarea>
						</div>
				   </div>
				 </#if>
		
					
			</div>	
	   </div>				
    </div>
</form>
</div>	
	<div class="layer-footer">
    <a class="btn btn-lightblue" id="arrange-commit">确定</a>
    <a class="btn btn-grey" id="arrange-close">取消</a>
    </div>
<script>

$(function(){
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd',
			'minView' : '2'
		};
		initCalendarData("#myDiv",".date-picker",viewContent);
	});


// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
 });
 
 
var isSubmit=false;
$("#arrange-commit").on("click", function(){		    
	var check = checkValue('#subForm');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
	var options = {
		url : "${request.contextPath}/studevelop/rewardsRecord/rewardsSave",
		dataType : 'json',
		success : function(data){
	 	var jsonO = data;
		    if(!jsonO.success){
		        layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		        $("#arrange-commit").removeClass("disabled");
		 	    return;
		    }else{
		 	    layer.closeAll();
			    layerTipMsg(jsonO.success,"保存成功",jsonO.msg);
			    searchList();
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