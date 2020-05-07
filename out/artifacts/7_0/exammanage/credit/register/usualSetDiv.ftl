<form id="subForm">
<div class="layer-content">
	<div class="form-horizontal">
	  <div class="form-group  credit-modal-body">
	    <table class="table table-bordered table-striped table-hover no-margin">
	      <thead>
	        <tr>
	          <th>考试序号</th>
	          <th>创建时间</th>
	          <th>操作</th>
	        </tr>
	      </thead>
	      <tbody>
	      <#assign row = 0>
	      <#if examSets?exists && examSets?size gt 0>
	      <#assign row = examSets?size>
	      <#list examSets as set>
	        <tr id="tr_${set_index + 1}">
	          <td id="sp_${set_index + 1}">
	            ${set.name!}
	          </td>
	          <td>
	            ${set.creationTime?string('yyyy-MM-dd')}
	          </td>
	          <td>
	            <#--<button class="btn btn-link btn-info credit-text">删除</button>-->
	            <a class="table-btn" href="javascript:;" onclick="delCol(${set_index + 1});">删除</a>
	          </td>
	        </tr>
	        </#list>
	        </#if>
	        <tr id="addTr">
			    <td class="text-center" colspan="3"><a class="table-btn" href="javascript:;" onclick="addCol();">+新增</a></td>
			</tr>
	      </tbody>
	    </table>
	  </div>
	</div>
</div>
</form>
<div class="layer-footer">
	<a href="javascript:" class="btn btn-blue" id="arrange-commit">确定</a>
	<a href="javascript:" class="btn btn-white" id="arrange-close">取消</a>
</div>
<script>
// 取消按钮操作功能
$("#arrange-close").on("click", function(){
    doLayerOk("#arrange-commit", {
    redirect:function(){},
    window:function(){layer.closeAll()}
    });     
});

 
var isSubmit=false;
$("#arrange-commit").on("click", function(){	
	isSubmit = true;
	var options = {
		url : "${request.contextPath}/exammanage/credit/register/exam/usualSet/save?setId=${setId}&gradeId=${gradeId}&subjectId=${subjectId}&classId=${classId}&classType=${classType}&col="+col,
		dataType : 'json',
		success : function(data){
 			var jsonO = data;
	 		if(!jsonO.success){
	 			$("#arrange-commit").removeClass("disabled");
				layerTipMsg(jsonO.success, "保存失败", jsonO.msg);
				return;
	 		}
	 		else{
	 			layer.closeAll();
				layer.msg(jsonO.msg, {
					offset: 't',
					time: 2000
				});
				showTableList();
			}
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#subForm").ajaxSubmit(options);
		
});

var maxCol = parseInt('${row!}');//记录页面最大的列数,只加不减
var col = parseInt('${row}');//实际的列数
function addCol(){
	if(col > 4){
		alert('已经达到最大行数！');
		layerTipMsg(false,"","已经达到最大行数！");
		return;
	}
	maxCol = maxCol + 1;
	col = col + 1;
	 var date=new Date;
	 var year=date.getFullYear(); 
	 var month=date.getMonth()+1;
	 month =(month<10 ? "0"+month:month); 
	var dd = date.getDate();
	var str = year.toString() + '-'+month.toString()+'-'+dd.toString();
	var TrHtml = '<tr id="tr_'+maxCol+'"><td id="sp_'+maxCol+'">'+col+'</td>'
			+'<td>'+str+'</td>'
			+'<td><a class="table-btn" href="javascript:;" onclick="delCol('+maxCol+');">删除</a></td></tr>';
	$("#addTr").before($(TrHtml));  
}

function delCol(trIndex){
	if(col < 2){
		layerTipMsg(false,"","至少为1行！");
		return;
	}
	col = col - 1;
	var index = trIndex
	for(;index<maxCol+1;index++){
		if($("#sp_"+index)){
			var oldCol = parseInt($("#sp_"+index).html());
			var newCol = oldCol - 1;
			$("#sp_"+index).html(newCol);
		}
	}
	$("#tr_"+trIndex).remove();
}
 
</script>
    