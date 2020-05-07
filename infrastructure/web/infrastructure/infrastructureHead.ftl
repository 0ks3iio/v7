<div class="main-content-inner">
	<div class="page-content">
		<div class="box box-default">
			<div class="box-body">
				<div class="filter">
					<div class="filter-item">
						<span class="filter-name">项目学校：</span>
                        <div class="filter-content">
                            <input type="text" id="schoolName" style="width:168px;" class="form-control"  >
                        </div>
					</div>

                    <div class="filter-item filter-item-left">
                        <div class="filter-content">
                            <a class="btn btn-blue" onclick="searchList();" >查找</a>

                        </div>
                    </div>
                    <#if isAdmin?default(false) >
                        <div class="filter-item filter-item-right">
                            <button class="btn btn-blue js-addTerm " onclick="addInfrastructureProject();">+新增</button>
                            <a class="btn btn-danger" onclick="deleteInfrastructureProject();" >删除</a>
                        </div>
                    </#if>

				</div>
				<div class="table-container" id="showList">
				</div>
			</div>
		</div>
	</div>
</div>
<script>
$(function(){	
	searchList();
});

function addInfrastructureProject() {

    $(".model-div").load("${request.contextPath}/infrastructure/project/edit");

}
function searchList() {
    var schoolName = $("#schoolName").val();
    $("#showList").load("${request.contextPath}/infrastructure/project/list?schoolName="+schoolName);
}

function deleteInfrastructureProject() {
    var selEle = $('#list :checkbox:checked');
    layer.confirm("确认删除？", {
        btn: ['确定', '取消'],
        yes: function(index){
            if(selEle.length<1){
                layerTipMsg(false,"失败",'请先选中基建项目再删除');
                layer.close(index);
                return;
            }
            var param = new Array();
            for(var i=0;i<selEle.length;i++){
                //alert(selEle.eq(i).val());
                param.push(selEle.eq(i).val());
            }
            deleteByIds(param);

            layer.close(index);
        }
    });

}

//批量删除
function deleteByIds(idArray){
    var url = '${request.contextPath}/infrastructure/project/delete';
    var params = {"ids":idArray};
    $.ajax({
        type: "POST",
        url: url,
        data: params,
        success: function(msg){
            //alert( "Data Saved: " + msg );
            if(msg.success){
                searchList();
            }else{
                layerTipMsg(false,"失败",msg.msg);
            }
        },
        dataType: "JSON"
    });
}

</script>