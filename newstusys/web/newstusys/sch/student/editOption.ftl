<div class="box box-default">
    <div class="box-body" id="studentContent">
        <form name="optionForm" id="optionForm">
        <input type="hidden" name="id" value="${option.id!}">
        <input type="hidden" name="unitId" value="${option.unitId!}">
        <div class="filter" id="searchDiv">
			<div class="filter-item">
				<span class="filter-name">是否允许学生/家长端维护信息：</span>
                <div class="filter-content">
				<label>
					<input type="checkbox" name="isOpen" id="isOpen" value="1" class="wp wp-switch" <#if option.isOpen==1>checked="checked"</#if> onclick="changeOp(this)">
					<span class="lbl"></span>
				</label>
				</div>
			</div>
		</div>
		<div id="showList">
			<#if cols?exists && cols?size gt 0>
			<table class="table table-bordered table-striped table-hover no-margin">
				<thead>
						<tr>
							<th colspan="8" class="text-center">学生家长端可编辑字段勾选</th>
						</tr>
						<tr>
							<th>选择</th>
							<th>字段名称</th>
							<th>选择</th>
							<th>字段名称</th>
							<th>选择</th>
							<th>字段名称</th>
							<th>选择</th>
							<th>字段名称</th>
						</tr>
				</thead>
				<tbody id="list">
				<#list cols as col>
				<#if col_index%4 == 0>
					<tr>
				</#if>
					<td> <label class="pos-rel">
                        <input name="displayCols" type="checkbox" <#if option.isOpen==0>disabled</#if> <#if option.displayCols?indexOf(col.colsCode) != -1>checked="checked"</#if>class="wp check-option" value="${col.colsCode!}">
                        <span class="lbl"></span>
                    </label>
					</td>
					<td>${col.colsName!}</td>
				<#if col_index%4 == 3 || !col_has_next>
				</tr>
				</#if>
				</#list>
				</tbody>
			</table>	
			</#if>
        <div class="base-bg-gray text-center">
		    <a class="btn btn-white selectAll" <#if option.isOpen==0>style="display:none;"</#if> onclick="selectAll();" href="javascript:;">全选</a>
		    <a class="btn btn-blue" onclick="opSave();" href="javascript:;">保存</a>
		    <a class="btn btn-white" onclick="opCancelOperate();" href="javascript:;">取消</a>
		</div>
	</div>	
</div>
<script>
//全选
function selectAll(){
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

function changeOp(ob){
	var chk = $(ob).prop('checked');
	if(chk=='checked' || chk == true){
		$('#list :checkbox').removeAttr("disabled");
		$('.selectAll').show();
	} else {
		$('#list :checkbox').prop("disabled", "disabled");
		$('.selectAll').hide();
	}
}

var isSubmit = false;
function opSave(){
    isSubmit = true;

    $("#btnSaveAll").attr("class", "abtn-unable");
    var options = {
        url : "${request.contextPath}/newstusys/sch/student/option/save",
        dataType : 'json',
        success : function(data){
            isSubmit=false;
            var jsonO = data;
            if(!jsonO.success){
                layerTipMsg(jsonO.success,"",jsonO.msg);
                isSubmit=false;
                return;
            }else{
                layer.msg("保存成功");
                opCancelOperate();
            }
        },
        clearForm : false,
        resetForm : false,
        type : 'post',
        error:function(XMLHttpRequest, textStatus, errorThrown){}
    };
    $("#optionForm").ajaxSubmit(options);
}

function opCancelOperate() {
    var url="${request.contextPath}/newstusys/sch/student/option/index";
    $(".model-div").load(url);
}
</script>
