<div class="layer-addTerm layer-change" style="display:block;" id="myDiv">
<form id="subForm">
	<div class="layer-body">
		<div class="filter clearfix"> 
			<div class="filter clearfix">
			    <#if type == '1'>
				    <input type="hidden" name="acadyear" value="${acadyear!}"/>
				    <input type="hidden" name="semester" value="${semester!}"/>
				    <input type="hidden" name="stuid" value="${stuid!}"/>
				    <input type="hidden" name="schid" value="${schid!}"/>
				    <div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>惩处名称：</label>
						<div class="filter-content">
							<input type="text" name="punishname" id="punishname" nullable="false" maxLength="60" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
					    <label for="" class="filter-name"><span style="color:red">*</span>惩处类型：</label>
					    <div class="filter-content">
						   <select name="punishType" id="punishType" class="form-control" nullable="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-CFMC", '', "1")}
						   </select>
					    </div>
				    </div>
				    
				    <div class="filter-item">
					    <label for="" class="filter-name"><span style="color:red">*</span>惩处单位：</label>
					    <div class="filter-content">
						   <select name="punishUnit" id="punishUnit" class="form-control" nullable="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-CCDW", '', "1")}
						   </select>
					    </div>
				    </div>
				    
				    <div class="filter-item">
						<label for="" class="filter-name">惩处个人扣分：</label>
						<div class="filter-content">
							<input min="0" type="text" name="selfScore" id="selfScore" vtype="int" maxLength="5" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">惩处班级扣分：</label>
						<div class="filter-content">
							<input min="0" type="text" name="classScore" id="classScore"  vtype="int" maxLength="5" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">操作员：</label>
						<div class="filter-content">
							<input type="hidden" name="operateUserId" id="" oid="" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${operateUserId!}" style="width:168px;"/>
							<input readonly type="text" name="" id="" oid="" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${operateUserName!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>惩处日期：</label>
						<div class="filter-content">
					    <div class="input-group">
						<input class="form-control date-picker" vtype="data" style="width: 130px" type="text" nullable="false" name="punishdate" id="punishdate" placeholder="惩处日期" value="">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					    </div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>惩处文号：</label>
						<div class="filter-content">
							<input type="text" name="punishfilecode" id="punishfilecode" nullable="false" maxLength="24" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">撤销日期：</label>
						<div class="filter-content">
					    <div class="input-group">
						<input class="form-control date-picker" vtype="data" style="width: 130px" type="text" name="canceldate" id="canceldate" placeholder="撤销日期" value="">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					    </div>
					</div>

               
					
					<div class="filter-item">
						<label for="" class="filter-name">撤销文号：</label>
						<div class="filter-content">
							<input type="text" name="cancelfilecode" id="cancelfilecode" maxLength="24" class="form-control col-xs-10 col-sm-10 col-md-10 " value="" style="width:168px;"/>
						</div>
					</div>
					
					
					
				    <div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>惩处原因：</label>
						<div class="filter-content">
							<textarea name="punishreason" id="punishreason" style="width:494px;" nullable="false" maxLength="80" cols="63" rows="4" class="form-control col-xs-10 col-sm-10 col-md-10 " value=""></textarea>
						</div>
                    </div>
                    
                    
                 
                    <div class="filter-item">
						<label for="" class="filter-name">备注：</label>
						<div class="filter-content">
							<textarea name="remark" id="remark" maxLength="200" cols="63" rows="4" style="width:494px;" class="form-control col-xs-10 col-sm-10 col-md-10 " value=""></textarea>
						</div>
				   </div> 
				<#else>
				    <input type="hidden" name="acadyear" value="${stuDevelopPunishment.acadyear!}"/>
				    <input type="hidden" name="semester" value="${stuDevelopPunishment.semester!}"/>
				    <input type="hidden" name="stuid" value="${stuDevelopPunishment.stuid!}"/>
				    <input type="hidden" name="schid" value="${stuDevelopPunishment.schid!}"/>
				    <input type="hidden" name="id" value="${stuDevelopPunishment.id!}"/>
				    <div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>惩处名称：</label>
						<div class="filter-content">
							<input type="text" name="punishname" id="punishname" maxLength="60" notnull="false" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopPunishment.punishname!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
					    <label for="" class="filter-name"><span style="color:red">*</span>惩处类型：</label>
					    <div class="filter-content">
						   <select name="punishType" id="punishType" class="form-control" nullable="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-CFMC", '${stuDevelopPunishment.punishType!}', "1")}
						   </select>
					    </div>
				    </div>
				    
				    <div class="filter-item">
					    <label for="" class="filter-name">惩处单位：</label>
					    <div class="filter-content">
						   <select name="punishUnit" id="punishUnit" class="form-control" nullable="false" style="width:168px;">
						 	   ${mcodeSetting.getMcodeSelect("DM-CCDW", '${stuDevelopPunishment.punishUnit!}', "1")}
						   </select>
					    </div>
				    </div>
				    
				    <div class="filter-item">
						<label for="" class="filter-name">惩处个人扣分：</label>
						<div class="filter-content">
							<input type="text" name="selfScore" id="selfScore" vtype="int" maxLength="5" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopPunishment.selfScore!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">惩处班级扣分：</label>
						<div class="filter-content">
							<input type="text" name="classScore" id="classScore" vtype="int" maxLength="5" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopPunishment.classScore!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">操作员：</label>
						<div class="filter-content">
							<input type="hidden" name="operateUserId" id="" oid="" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${operateUserId!}" style="width:168px;"/>
							<input readonly type="text" name="" id="" oid="" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${operateUserName!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>惩处日期：</label>
						<div class="filter-content">
					    <div class="input-group">
						<input class="form-control date-picker" vtype="data" style="width: 130px" type="text" nullable="false" name="punishdate" id="punishdate" placeholder="惩处日期" value="${(stuDevelopPunishment.punishdate?string('yyyy-MM-dd'))}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					    </div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name"><span style="color:red">*</span>惩处文号：</label>
						<div class="filter-content">
							<input type="text" name="punishfilecode" id="punishfilecode" nullable="false" maxLength="24" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopPunishment.punishfilecode!}" style="width:168px;"/>
						</div>
					</div>
					
					<div class="filter-item">
						<label for="" class="filter-name">撤销日期：</label>
						<div class="filter-content">
					    <div class="input-group">
						<input class="form-control date-picker" vtype="data" style="width: 130px" type="text" name="canceldate" id="canceldate" placeholder="撤销日期" value="${(stuDevelopPunishment.canceldate?string('yyyy-MM-dd'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					    </div>
					</div>

               
					
					<div class="filter-item">
						<label for="" class="filter-name">撤销文号：</label>
						<div class="filter-content">
							<input type="text" name="cancelfilecode" id="cancelfilecode"  maxLength="24" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopPunishment.cancelfilecode!}" style="width:168px;"/>
						</div>
					</div>
					
					
					
				<div class="filter-item">
                        <label for="" class="filter-name"><span style="color:red">*</span>惩处原因：</label>
						<div class="filter-content">
							<textarea name="punishreason" id="punishreason" nullable="false" style="width:494px;" maxLength="80" cols="63" rows="4" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopPunishment.punishreason!}">${stuDevelopPunishment.punishreason!}</textarea>
						</div>
                 </div>
                 <div class="filter-item">
						<label for="" class="filter-name">备注：</label>
						<div class="filter-content">
							<textarea name="remark" id="remark" cols="63" rows="4" maxLength="200" style="width:494px;" class="form-control col-xs-10 col-sm-10 col-md-10 " value="${stuDevelopPunishment.remark!}">${stuDevelopPunishment.remark!}</textarea>
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
	var check = checkValue('#myDiv');
    if(!check){
        $(this).removeClass("disabled");
        isSubmit=false;
        return;
    }
    
    var punishdate = $("#punishdate").val();
 	var canceldate = $("#canceldate").val();
 	if(punishdate != '' && canceldate!=''){
 	    if(punishdate>canceldate){
		layerTipMsg(false,"提示!","惩处日期不能大于撤销日期!");
		isSubmit=false;
		return;
	}
 	}
    
	var options = {
		url : "${request.contextPath}/studevelop/rewardsRecord/punishSave",
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