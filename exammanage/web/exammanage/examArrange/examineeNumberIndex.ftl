<div class="filter filter-f16">
	<div class="filter-item">
		<span class="filter-name">班级：</span>
		<div class="filter-content">
			<select class="form-control" id="searchClassId" name="searchClassId" onChange="showNumberList()">
				<#if clazzList?exists && (clazzList?size>0)>
                    <#list clazzList as item>
	                     <option value="${item.id!}">${item.classNameDynamic!}</option>
                    </#list>
                <#else>
                    <option value="">未设置</option>
                 </#if>
			</select>
		</div>
	</div>
</div>
<div class="table-container">
<#if isEdit && canEdit?exists && canEdit>
	<input type="hidden" id="canEdit" value="true">
    <div class="filter">
        <div class="filter-item">
            <button class="btn btn-blue" id="js-saveNumber" onclick="saveNumber();">保存</button>
            <button class="btn btn-blue" id="js-clearNumber" onclick="clearNumber();">清空</button>
            <button class="btn btn-white" id="js-autoSaveNumber" onclick="autoSaveNumber();">自动安排</button>
            <button class="btn btn-white" id="js-exportNumber" onclick="exportNumber();">导入</button>
        </div>
    </div>
<#else>
	<input type="hidden" id="canEdit" value="false">
        <#--<div class="table-container-header text-right">
            <a href="javascript:" class="btn btn-blue js-autoSaveNumber" id="js-autoSaveNumber" onclick="autoSaveNumber();">自动安排</a>
            <a href="javascript:" class="btn btn-blue js-exportNumber" id="js-exportNumber" onclick="exportNumber();">导入</a>
            <a href="javascript:" class="btn btn-blue js-saveNumber" id="js-saveNumber" onclick="saveNumber();">保存</a>
        </div>-->
</#if>
	<div class="table-container-body examineeNumberDiv" id="examineeNumberList">
</div>
<div class="layer layer-chooseType">
	<div class="layer-body">
		<div class="filter clearfix">
			<div class="filter-item block">
				<label for="" class="filter-name">方式：</label>
				<div class="filter-content">
					<label class="pos-rel">
						<input type="radio" class="wp form-control form-radio" name="chooseType" value="0" checked>
						<span class="lbl">按随机</span>
					</label>
					<label class="pos-rel">
						<input type="radio" class="wp form-control form-radio" name="chooseType" value="1" >
						<span class="lbl">按学籍号</span>
					</label>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
$(function(){
	showNumberList();
});

function showNumberList(){
	var searchClassId=$("#searchClassId").val();
	var str='&searchClassId='+searchClassId;
	var url =  '${request.contextPath}/exammanage/examArrange/examineeNumberList/page?examId=${examId!}'+str;
	$("#examineeNumberList").load(url);
}

function exportNumber(){
     var url="${request.contextPath}/exammanage/examineeNumber/main?examId=${examId!}"
	 $("#showTabDiv").load(url);
}
function checkNumber(theObj) { 
    var reg = /^[0-9]*$/;  
    if (reg.test(theObj)) {  
        return true;  
    }  
    return false;  
} 
function clearNumber(){
	showConfirmMsg('确认清空考号？','提示',function(){
		$.ajax({
		    url:'${request.contextPath}/exammanage/examArrange/clearNumber',
		    data: {'examId':'${examId!}'},  
		    type:'post',  
		    success:function(data) {
		    	var jsonO = JSON.parse(data);
		    	if(jsonO.success){
		    		// 显示成功信息
	                layer.msg("操作成功", {
	                    offset: 't',
	                    time: 2000
	                });
	                showNumberList();
		 		}
		 		else{
	                layer.msg("操作失败", {
	                    offset: 't',
	                    time: 2000
	                });
				}
		    }
		});
	});
	
}
var isNumSubmit=false;
function saveNumber(){
	if(isNumSubmit){
		return;
	}
	isNumSubmit=true;
	$("#js-saveNumber").addClass("disabled");
	var check = checkValue('.examineeNumberDiv');
    if(!check){
        $("#js-saveNumber").removeClass("disabled");
        isNumSubmit=false;
        return;
    }
    var ff=false;
    var examNumberMap={};
    $(".examNumber_class").each(function(){
    	 var mm=$(this).val();
    	 if(mm!="" && $.trim(mm)!== ""){
    	 	mm=$.trim(mm);
    	 	$(this).val(mm);
    		if(!checkNumber(mm)){
    			 if(!ff){
		    	 	ff=true;
		    	 }
		    	 layer.tips('考号需由数字组成!', $(this), {
					tipsMore: true,
					tips: 3
				});
    		}else{
	    	 	if(examNumberMap[mm]!=null){
		    	 	if(!ff){
		    	 		ff=true;
		    	 	}
		    	 	layer.tips('考号重复!', $(this), {
						tipsMore: true,
						tips: 3
					});
		    	 }else{
		    	 	examNumberMap[mm]=mm;
		    	 }
	    	 }
    	 }
    	 
    });
    
    if(ff){
    	$("#js-saveNumber").removeClass("disabled");
        isNumSubmit=false;
        return;
    }
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/exammanage/examArrange/examineeNumberSave',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
                layer.msg("保存成功", {
                    offset: 't',
                    time: 2000
                });
				$("#js-saveNumber").removeClass("disabled");
	 			isNumSubmit=false;
				showNumberList();
	 		}
	 		else{
                layer.msg("保存失败", {
                    offset: 't',
                    time: 2000
                });
	 			$("#js-saveNumber").removeClass("disabled");
	 			isNumSubmit=false;
			}
			layer.close(ii);
		},
		clearForm : false,
		resetForm : false,
		type : 'post',
		error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
	};
	$("#examNumberForm").ajaxSubmit(options);
}
function exportNumber(){
	 var url="${request.contextPath}/exammanage/examNumber/main?examId=${examId!}"
	 $("#showTabDiv").load(url);
}

var isSubmit=false;
function autoSaveNumber(){
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
			isSubmit=true;
			layer.close(index);
			$.ajax({
			    url:'${request.contextPath}/exammanage/examArrange/examineeNumberAutoSave',
			    data: {'examId':'${examId!}','chooseType':chooseType},  
			    type:'post',  
			    success:function(data) {
			    	var jsonO = JSON.parse(data);
			    	layer.close(index);
			    	if(jsonO.success){
			    		// 显示成功信息
                        layer.msg("操作成功", {
                            offset: 't',
                            time: 2000
                        });
			 			isSubmit=false;
			 			showNumberList();
			 		}
			 		else{
                        layer.msg("操作失败", {
                            offset: 't',
                            time: 2000
                        });
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