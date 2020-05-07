<form id="myform">
<a href="javascript:" class="page-back-btn gotoLcIndex"><i class="fa fa-arrow-left"></i> 返回</a>
<div class="box box-default">
    <div class="box-header">
        <h4 class="box-title">
        	${dto.gsaEnt.arrangeName!}
        </h4>
     
    </div>
    <div class="box-body">
    	 <div class="filter filter-f16">
            <div class="filter-item">
                <span class="filter-name"><font style="color:red;">*</font>每班容纳数：</span>
                <div class="filter-content">
                    <input type="text" <#if dto.gsaEnt.isLock == 1>readonly="true"</#if> class="form-control" nullable="false"  name="gsaEnt.claNum" id="claNum" min="1" max="999" vtype="int" maxlength="3" value="<#if dto.gsaEnt.claNum?exists>${dto.gsaEnt.claNum}<#else>50</#if>">
                </div>
            </div>
        </div>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th style="width:20%;">科目</th>
                    <th style="width:20%;">教学方式
                    <span class="fa fa-question-circle color-grey" data-toggle="tooltip" data-placement="top" title="" data-original-title="走班按照教学班安排，不走班则按照行政班安排课程"></span></th>
                    <th>任课教师
                    <span class="fa fa-question-circle color-grey" data-toggle="tooltip" data-placement="top" title="" data-original-title="此处任课老师设置：用于自动排班预设班级授课老师"></span></th>
                </tr>
            </thead>
            <tbody>
              <#list dto.gksubList as item>
                <tr>
                    <td>
                    <input type="hidden" name="gksubList[${item_index}].id" value="${item.id!}">
                    <input type="hidden" name="gksubList[${item_index}].roundsId" value="${item.roundsId!}">
                    <input type="hidden" name="gksubList[${item_index}].subjectId" value="${item.subjectId!}">
                    ${item.subjectName!}
                    </td>
                    <td>
                        <select <#if dto.gsaEnt.isLock == 1> disabled="disabled"</#if> name="gksubList[${item_index}].teachModel" class="form-control">
                            <option value="1" 
                            <#if item.teachModel?default(1) == 1>selected = "selected"</#if>
                            > 走班
                            </option>
                            <option value="0"
                            <#if item.teachModel?default(1) == 0>selected = "selected"</#if>
                            >不走班</option>
                        </select>
                    </td>
                     <td>
                        <select <#if dto.gsaEnt.isLock == 1> disabled="disabled"</#if> multiple vtype="selectMore" name="gksubList[${item_index}].teacherIds" id="teacherIds_${item_index}" class="teacherIdsClass" data-placeholder="选择教师" >
							<#if dto.teacherMap?exists>
								<option value=""></option>
								<#list dto.teacherMap?keys as key>
									<#assign teasel=false />
									<#if item.teacherIds?exists>
									<#list item.teacherIds as teaid>
										<#if teaid==key>
							                <#assign teasel=true />
							                <#break>
							            </#if>
									</#list>
									</#if>
									<option value="${key}" <#if teasel>selected="selected"</#if>>${dto.teacherMap[key]?default("")}</option>
								</#list>
							<#else>
								<option value="">暂无数据</option>
							</#if>
						</select>
                    </td>
                </tr>
              </#list>
            </tbody>
        </table>
        
        
        <div>
        	<em>温馨提示：上课时间已经设置或者开班进行中不能修改（历史轮次也不能修改哦！）。</em>
    		<#if dto.gsaEnt.isLock == 0>
        		<a  href="javascript:" class="btn btn-blue pull-right jsocs-save" ><#if isAddorUp=="0">保存<#else>修改</#if></a>
           	</#if>
        </div>
     
    </div>
</div>
</form>
<script>
	$(function(){
		// #############提示工具#############
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});
		//初始化多选控件
		var viewContent1={
			'width' : '100%',//输入框的宽度
			'multi_container_height' : '28px',//输入框的高度
			'results_height':'200px'
		}
		initChosenMore("#myform","",viewContent1);
		$('.date-picker').next().on("click", function(){
			$(this).prev().focus();
		});
		
		$('.jsocs-save').on('click',function(){
			doGoClassSave();
		});
		
		$('.gotoLcIndex').on('click',function(){
			var url =  contextPath+'/gkelective/${arrangeId!}/arrangeRounds/index/page';
			$("#showList").load(url);
		});
	});

	var isSubmit=false;
	function doGoClassSave(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var checkVal = checkValue('#myform');
		if(!checkVal){
		 	isSubmit=false;
		 	return;
		}
		
		// 提交数据
		var ii = layer.load();
		var options = {
			url : '${request.contextPath}/gkelective/${arrangeId!}/goClass/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.closeAll();
					layerTipMsg(data.success,"成功",data.msg);
					var url =  '${request.contextPath}/gkelective/${arrangeId!}/arrangeRounds/index/page';
				  	//var url =  '${request.contextPath}/gkelective/${arrangeId!}/openClasSub/list/page?roundsId=${roundsId!}';
					$("#showList").load(url);
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#myform").ajaxSubmit(options);
	}
</script>
