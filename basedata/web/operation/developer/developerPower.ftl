<#import "/fw/macro/popupMacro.ftl" as popup />
<div class="table-container-body">
    <table class="table table-bordered table-striped layout-fixed">
		<thead>
			<tr>
				<th width="8%">
				                      选择
			    </th>
				<th>单位名称</th>
				<th>单位类型</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody id='list'>
		   <#if empowerUnits?exists && empowerUnits?size gt 0>
              <#list empowerUnits as unit>
                  <tr>
                    <td>
					    <label class="pos-rel">
	                    <input name="userId" type="checkbox" class="wp" value="${unit.id!}">
	                    <span class="lbl"></span>
	                    </label>
	                </td>
					<td>${unit.unitName!}</td>
					<td><#if unit.unitClass == 1>教育局<#else>学校</#if></td>
					<td>
						<a href="javascript:void(0);" class="color-red js-del"  value="${unit.id!}" >删除</a>
					</td>
				  </tr>
              </#list>
           <#else>
               <tr>
					<td  colspan="4" align="center">
					暂无单位记录
					</td>
			   <tr>
           </#if>   
		</tbody>
	</table>
</div>
<div class="">
	<div class="">
		<button class="btn btn-sm btn-white" onclick= "checkAllUnit()">全选</button>
		<button class="btn btn-sm btn-blue" onclick= "deletEmUnit()">删除</button>
		<@popup.selectMoreUnit clickId="addUnitIds" id="addUnitIds" name="addUnitName" handler="unitCallBack()">
			 <input type="hidden" class="form-control" id="addUnitIds" name="addUnitIds" value=""/>
			 <button class="btn btn-sm btn-blue js-add" onclick="addMoreUnit();">添加</button>
		</@popup.selectMoreUnit>
	</div> 
</div>  <!-- table-container-footer -->


<script>
//全选
function checkAllUnit(){
   var total = $('#list :checkbox').length;
	var length = $('#list :checkbox:checked').length;
	if(length != total){
		$('#list :checkbox').prop("checked", "true");
		$(this).prop("checked", "true");
	}else{
		$('#list :checkbox').removeAttr("checked");
		$(this).removeAttr("checked");
	}
}
//批量删除单位
var unitIds = new Array();
function deletEmUnit(){
    var selEle = $('#list :checkbox:checked');
	for(var i=0;i<selEle.length;i++){
		unitIds.push(selEle.eq(i).val());
	}
	deleteUnitIds(unitIds);
}  
    
//删除单位
$('.js-del').on('click', function(e){
    var unitId = $(this).attr("value");
    unitIds.push(unitId);
	e.preventDefault();
	deleteUnitIds(unitIds);
});

var isSubmit=false;	
function deleteUnitIds(unitIds){
    if(isSubmit){
		return;
	}
	isSubmit = true;
    var allIds = new Array();
	var url = '${request.contextPath}/system/developer/empower/deleteUnit?developerId='+'${developerId!}';
	var param = {"unitIds":unitIds};
	$.ajax({
		   type: "POST",
		   url: url,
		   data: JSON.stringify(param),
		   contentType: "application/json",
		   dataType: "JSON",
		   success:function (data) {
                isSubmit = false;
                if(data.success){
                    showSuccessMsgWithCall(data.msg,showEmpowerUnit);
                }else{
                    showErrorMsg(data.msg);
                }
		    }
		});
}  

function addMoreUnit() {
		$("#addUnitIds").click();
}

function unitCallBack(){
	var unitIds=$('#addUnitIds').val();
	if(unitIds==""){
		$("#chooseText").html("已选0单位");
	}else{
		var unitArr=unitIds.split(",");
		var selectCount=unitArr.length;
		$("#chooseText").html("已选"+selectCount+"单位");
		$("#addUnitName").val('');
		saveDeveloperUnit(unitIds);
	}
}

function saveDeveloperUnit(unitIds){
    var url = '${request.contextPath}/system/developer/empower/addUnit?developerId='+$('#developerId').val()+'&unitIds='+unitIds;
	$.ajax({
		   type: "POST",
		   url: url,
		   data:"",
		   contentType: "application/json",
		   dataType: "JSON",
		   success:function (data) {
                isSubmit = false;
                if(data.success){
                    showSuccessMsgWithCall(data.msg,showEmpowerUnit);
                }else{
                    showErrorMsg(data.msg);
                }
		    }
		});
}
</script>