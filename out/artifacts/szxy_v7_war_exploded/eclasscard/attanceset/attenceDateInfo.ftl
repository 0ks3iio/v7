<form id="dateInfoForm" >
<table id="dateInfoTable" class="table table-bordered">
</table>
</form>
<script type="text/javascript">
$(function(){
	var data = []; 
	<#if dateInfos?exists&&dateInfos?size gt 0>
      	<#list dateInfos as item>
      	var row = [];
		row.push('${item.week!}');
		row.push('${item.weekDay!}');
		row.push('${item.infoDate?string('yyyy-MM-dd')}<input type="hidden" name="eccDateInfoDtoList[${item_index}].infoDate" value="${item.infoDate?string('yyyy-MM-dd')}">');
		row.push('<label><input type="checkbox" <#if item.markup >checked="true"</#if> name="eccDateInfoDtoList[${item_index}].markup" class="wp wp-switch" /><span class="lbl"></span></label>');
		row.push('<input name="eccDateInfoDtoList[${item_index}].remark" type="text" class="form-control" value="${item.remark!}" maxLength="60"/>'+
				'<input type="hidden" name="eccDateInfoDtoList[${item_index}].id" value="${item.id!}">');
		data.push(row);
  	    </#list>
    </#if>

	$('#dateInfoTable').DataTable( {
        scrollY: "600px",
		info: false,
		searching: false,
		autoWidth: false,
        sort: false,
        paging: false,
        columns: [
			{title:'周次',class:'text-center'},
			{title:'星期',class:'text-center'},
			{title:'日期'},
			{title:'是否补课'},
			{title:'备注'}
		],
		data:data,
		rowsGroup:[0]
    });
    $("#justShowSunDay").show();
    if(data.length==0){
	    $(".dataTables_empty").html("请设置学年学期");
    }
});
var isSubmit=false;
function saveInfoDate(){
	if(isSubmit){
        return;
    }
    var check = checkValue('#dateInfoTable');
	if(!check){
		layerTipMsgWarn("备注","备注不能超过125字符");
	 	return;
	}
	var grade =$("#grade").val();
	isSubmit = true;
	var options = {
			url : "${request.contextPath}/eclasscard/attence/date/info/save",
			data:{'grade':grade},
			dataType : 'json',
			success : function(data){
		 		if(!data.success){
		 			layerTipMsg(data.success,"保存失败",data.msg);
		 			isSubmit = false;
		 		}else{
					layer.msg("保存成功");
					justSunDay();
    			}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#dateInfoForm").ajaxSubmit(options);
}
</script>