<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<#if hasStuCount?? && hasStuCount>
<div class="filter noprint">
<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="findBySearchViewType(true);">返回</a>
</div>
</#if>
<div id="aa" class="tab-pane active" role="tabpanel">
<div class="clearfix">
<em>共${isChosenCount!}个学生已选择&nbsp;&nbsp;&nbsp;&nbsp;参考分：在参考成绩中的排班成绩依据中读取</em>
<p class="tip tip-grey pull-right noprint">
	<i class="fa fa-lock color-blue"></i> 已通过审核&emsp;
	<i class="fa fa-unlock color-yellow"></i> 未通过审核
	<span class="fa fa-question-circle color-grey" data-toggle="tooltip" data-placement="top" title="" data-original-title="已通过审核的学生科目无法修改，未通过审核的学生可以再次调整"></span>
</p>
</div>
<form id="myform" class="print">
<table class="table table-striped table-bordered table-hover no-margin mainTable">
    <thead>
        <tr>
            <th width="10%">学号</th>
            <th width="10%">姓名</th>
            <th width="8%">性别</th>
            <th>行政班</th>
            <#if subjectMap?? && (subjectMap?size>0) && (subjectMap?size<4)>
            	<#list 1..subjectMap?size as ind>
            		<th>
                    	选${ind}(参考分)
                    	<span class="table-th-tool">
                        	<i class="fa fa-lock color-blue" onclick="saveAll(${ind},1)"></i>&emsp;
							<i class="fa fa-unlock color-yellow" onclick="saveAll(${ind},0)"></i>
						</span>
                    </th>
            	</#list>
            <#else>
                <th>
                	选1(参考分)
                	<span class="table-th-tool">
                    	<i class="fa fa-lock color-blue" onclick="saveAll(1,1)"></i>&emsp;
						<i class="fa fa-unlock color-yellow" onclick="saveAll(1,0)"></i>
					</span>
                </th>
				<th>
					选2(参考分)
					<span class="table-th-tool">
                    	<i class="fa fa-lock color-blue" onclick="saveAll(2,1)"></i>&emsp;
						<i class="fa fa-unlock color-yellow" onclick="saveAll(2,0)"></i>
					</span>
				</th>
				<th>
					选3(参考分)
					<span class="table-th-tool">
                    	<i class="fa fa-lock color-blue" onclick="saveAll(3,1)"></i>&emsp;
						<i class="fa fa-unlock color-yellow" onclick="saveAll(3,0)"></i>
					</span>
				</th>
            </#if>
            <#if gkArrange.isLock == 0>
            <th class="noprint">操作</th>
            </#if>
        </tr>
    </thead>
    <tbody>
	<#if gkResultList?? && (gkResultList?size>0)>
		<#list gkResultList as gkResult>
		<#if (gkResult_index <= 80)>
		<tr >
			<td>${gkResult.stucode!}</td><td>${gkResult.stuName!}</td><td>${mcodeSetting.getMcode("DM-XB","${gkResult.stuSex!}")}</td><td>${gkResult.className!}</td>
			<#if subjectMap?? && (subjectMap?size>0) && (subjectMap?size<4)>
				<#assign mapInd = 0>
				<#if gkResult.courseIds[0]?? && subjectMap[gkResult.courseIds[0]]??>
					<td class="course-review subColClass${mapInd+1} <#if gkResult.statues[0]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[0]!}">
						<input type="hidden" class="courseScores" value="${gkResult.courseScores[0]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[${mapInd}]" value="${gkResult.ids[0]!}"/><input id="${gkResult.ids[0]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[${mapInd}]" value="${gkResult.statues[0]!}"/>
						<span >${gkResult.courseNames[0]!}</span>
					</td>
					<#assign mapInd = mapInd + 1 >
				</#if>
				<#if gkResult.courseIds[1]?? && subjectMap[gkResult.courseIds[1]]??>
					<td class="course-review subColClass${mapInd+1} <#if gkResult.statues[1]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[1]!}">
						<input type="hidden" class="courseScores" value="${gkResult.courseScores[1]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[${mapInd}]" value="${gkResult.ids[1]!}"/><input id="${gkResult.ids[1]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[${mapInd}]" value="${gkResult.statues[1]!}"/>
						<span>${gkResult.courseNames[1]!}</span>
					</td>
					<#assign mapInd = mapInd + 1 >
				</#if>
				<#if gkResult.courseIds[2]?? && subjectMap[gkResult.courseIds[2]]??>
					<td class="course-review subColClass${mapInd+1} <#if gkResult.statues[2]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[2]!}">
						<input type="hidden" class="courseScores" value="${gkResult.courseScores[2]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[${mapInd}]" value="${gkResult.ids[2]!}"/><input id="${gkResult.ids[2]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[${mapInd}]" value="${gkResult.statues[2]!}"/>
						<span>${gkResult.courseNames[2]!}</span>
					</td>
				</#if>
			<#else>
				<td class="course-review subColClass1 <#if gkResult.statues[0]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[0]!}">
					<input type="hidden" class="courseScores" value="${gkResult.courseScores[0]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[0]" value="${gkResult.ids[0]!}"/><input id="${gkResult.ids[0]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[0]" value="${gkResult.statues[0]!}"/>
					<span >${gkResult.courseNames[0]!}</span>
				</td>
				<td class="course-review subColClass2 <#if gkResult.statues[1]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[1]!}">
					<input type="hidden" class="courseScores" value="${gkResult.courseScores[1]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[1]" value="${gkResult.ids[1]!}"/><input id="${gkResult.ids[1]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[1]" value="${gkResult.statues[1]!}"/><span>${gkResult.courseNames[1]!}</span>
				</td>
				<td class="course-review subColClass3 <#if gkResult.statues[2]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[2]!}">
					<input type="hidden" class="courseScores" value="${gkResult.courseScores[2]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[2]" value="${gkResult.ids[2]!}"/><input id="${gkResult.ids[2]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[2]" value="${gkResult.statues[2]!}"/>
					<span>${gkResult.courseNames[2]!}</span>
				</td>
			</#if>
			<#if gkArrange.isLock == 0>
				<td class="noprint">
					<a href="javascript:;" class="table-btn color-orange js-editCourse" onclick="editResult('${gkResult.studentId!}')">编辑</a>
					<a href="javascript:;" class="table-btn color-red js-delete" onclick="doDelete('${gkResult.studentId!}','${gkResult.stuName!}')">删除</a>
				</td>
			</#if>
		</tr>
		</#if>
		</#list>
	</#if> 
	</tbody>
</table>
</form>
<#-- <div class="page-footer-btns text-right">
	<button class="btn btn-blue js-saveSelectResult" onclick="saveAll();" id="save">保存</button>
	<a href="javascript:void(0);" onclick="openClassArrange();" class="btn btn-blue">去开班</a>
</div> -->
</div>
<script  type="text/javascript">
  $(function(){
    // #############提示工具#############
	$('[data-toggle="tooltip"]').tooltip({
		container: 'body',
		trigger: 'hover'
	});
	var ii = layer.load();
	setTimeout(function(){
<#if gkResultList?? && (gkResultList?size>0)>
<#list gkResultList as gkResult>
<#if (gkResult_index > 80) >
var htmlMain = "";
htmlMain+="<tr >";
htmlMain+="<td>${gkResult.stucode!}</td>";
htmlMain+="<td>${gkResult.stuName!}</td>";
htmlMain+="<td>${mcodeSetting.getMcode("DM-XB","${gkResult.stuSex!}")}</td>";
htmlMain+="<td>${gkResult.className!}</td>";
<#if subjectMap?? && (subjectMap?size>0) && (subjectMap?size<4)>
<#assign mapInd = 0>
<#if gkResult.courseIds[0]?? && subjectMap[gkResult.courseIds[0]]??>
	htmlMain+='<td class="course-review subColClass${mapInd+1} <#if gkResult.statues[0]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[0]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[0]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[${mapInd}]" value="${gkResult.ids[0]!}"/>'+'<input id="${gkResult.ids[0]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[${mapInd}]" value="${gkResult.statues[0]!}"/>'+'<span >${gkResult.courseNames[0]!}</span>'+'</td>';
	<#assign mapInd = mapInd + 1 >
</#if>
<#if gkResult.courseIds[1]?? && subjectMap[gkResult.courseIds[1]]??>
	htmlMain+='<td class="course-review subColClass${mapInd+1} <#if gkResult.statues[1]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[1]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[1]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[${mapInd}]" value="${gkResult.ids[1]!}"/>'+'<input id="${gkResult.ids[1]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[${mapInd}]" value="${gkResult.statues[1]!}"/>'+'<span>${gkResult.courseNames[1]!}</span>'+'</td>';
	<#assign mapInd = mapInd + 1 >
</#if>
<#if gkResult.courseIds[2]?? && subjectMap[gkResult.courseIds[2]]??>
	htmlMain+='<td class="course-review subColClass${mapInd+1} <#if gkResult.statues[2]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[2]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[2]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[${mapInd}]" value="${gkResult.ids[2]!}"/>'+'<input id="${gkResult.ids[2]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[${mapInd}]" value="${gkResult.statues[2]!}"/>'+'<span>${gkResult.courseNames[2]!}</span>'+'</td>';
</#if>
<#else>
	htmlMain+='<td class="course-review subColClass1 <#if gkResult.statues[0]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[0]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[0]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[0]" value="${gkResult.ids[0]!}"/>'+'<input id="${gkResult.ids[0]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[0]" value="${gkResult.statues[0]!}"/>'+'<span >${gkResult.courseNames[0]!}</span>'+'</td>';
	htmlMain+='<td class="course-review subColClass2 <#if gkResult.statues[1]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[1]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[1]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[1]" value="${gkResult.ids[1]!}"/>'+'<input id="${gkResult.ids[1]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[1]" value="${gkResult.statues[1]!}"/>'+'<span>${gkResult.courseNames[1]!}</span>'+'</td>';
	htmlMain+='<td class="course-review subColClass3 <#if gkResult.statues[2]! == 1>locked<#else>unlocked</#if>" value="${gkResult.ids[2]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[2]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[2]" value="${gkResult.ids[2]!}"/>'+'<input id="${gkResult.ids[2]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[2]" value="${gkResult.statues[2]!}"/>'+'<span>${gkResult.courseNames[2]!}</span>'+'</td>';
</#if>
<#if gkArrange.isLock == 0>
	htmlMain+='<td class="noprint">'+'<a href="javascript:;" class="table-btn color-orange js-editCourse" onclick="editResult(\'${gkResult.studentId!}\')">编辑</a>'+'<a href="javascript:;" class="table-btn color-red js-delete" onclick="doDelete(\'${gkResult.studentId!}\',\'${gkResult.stuName!}\')">删除</a>'+'</td>';
</#if>
htmlMain+="</tr >";
$(".mainTable tbody").append(htmlMain);
</#if>
</#list>
</#if>
		<#if subjectMap?? && (subjectMap?size>0) && (subjectMap?size<4)>
		<#list 1..subjectMap?size as ind>
		$.fn.dataTable.ext.order['dom-text-numeric${ind}'] = function  ( settings, col )
		{
		    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
		        return $('.courseScores', td).val() * 1;
		    } );
		};
	    </#list>
	    <#else>
		$.fn.dataTable.ext.order['dom-text-numeric1'] = function  ( settings, col )
		{
		    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
		        return $('.courseScores', td).val() * 1;
		    } );
		};
		$.fn.dataTable.ext.order['dom-text-numeric2'] = function  ( settings, col )
		{
		    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
		        return $('.courseScores', td).val() * 1;
		    } );
		};
		$.fn.dataTable.ext.order['dom-text-numeric3'] = function  ( settings, col )
		{
		    return this.api().column( col, {order:'index'} ).nodes().map( function ( td, i ) {
		        return $('.courseScores', td).val() * 1;
		    } );
		};
	    </#if>
		$("#myform .table").DataTable({
			paging:false,
			"language": {
				"emptyTable": "暂无数据"
			},
			info:false,
			searching:false,
			autoWidth:false,
			"order": [[ 3, 'asc' ]],
			columns: [
	            null,
	            { "orderable": false },
	            null,
	            null,
	            <#if subjectMap?? && (subjectMap?size>0) && (subjectMap?size<4)>
	            <#list 1..subjectMap?size as ind>
	            { "orderDataType": "dom-text-numeric${ind}", type: 'numeric' },
	            </#list>
	            <#else>
	            { "orderDataType": "dom-text-numeric1", type: 'numeric' },
	            { "orderDataType": "dom-text-numeric2", type: 'numeric' },
	            { "orderDataType": "dom-text-numeric3", type: 'numeric' },
	            </#if>
	            <#if gkArrange.isLock == 0>
	            { "orderable": false }
	            </#if>
	            
	        ]
		});
		layer.close(ii);
	},10);
  });
  //保存审核信息
var isSubmit=false;
function saveAll(chooseIndex,isPass){
  if(isSubmit){
    return;
    }
  isSubmit=true;
  var obj = new Object();
  // 获取此控件下所有的可输入内容，并组织成json格式
  // obj，是因为url所对应的接收对象是个dto，数据是存在dto
  var ii = layer.load();
  obj= JSON.parse(dealValue('#myform'));
  var options = {
      url : '${request.contextPath}/gkelective/${arrangeId}/chosenSubject/saveStatues?index='+chooseIndex+'&isPass='+isPass,
      dataType : 'json',
      success : function(data){
          if(data.success){
              layer.closeAll();
              layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
              //layerTipMsg(data.success,"成功",data.msg);
              //findByCondition();
              if(isPass == 1){
              	$(".subColClass"+chooseIndex).removeClass("unlocked");
              	$(".subColClass"+chooseIndex).addClass("locked");
              }else{
              	$(".subColClass"+chooseIndex).removeClass("locked");
              	$(".subColClass"+chooseIndex).addClass("unlocked");
              }
          }
          else{
              layerTipMsg(data.success,"失败",data.msg);
          }
          isSubmit=false;
          layer.close(ii);
      },
      clearForm : false,
      resetForm : false,
      type : 'post',
      error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
  };
  $("#myform").ajaxSubmit(options);
}
  
  // 审核课程
	$('.mainTable tbody').on('click','.course-review',function(){
		var id = $(this).find('.gkResId').val();
		var isPass = 0;
		if($(this).hasClass('locked')){
			$(this).removeClass('locked').addClass('unlocked');
			$('#'+$(this).attr('value')).attr('value','0');
			$(this).find('.gkResStatues').val('0');
			isPass = 0;
		}else{
			$(this).removeClass('unlocked').addClass('locked');
			$('#'+$(this).attr('value')).attr('value','1');
			$(this).find('.gkResStatues').val('1');
			isPass = 1;
		}
		var ii = layer.load();
		$.ajax({
	        url:'${request.contextPath}/gkelective/${arrangeId}/chosenSubject/saveOneStatues',
	        data: {'resId':id,'isPass':isPass},
	        type:'post',
	        success:function(data) {
	            var jsonO = JSON.parse(data);
	            if(jsonO.success){
	                layer.closeAll();
	                layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
	            }
	            else{
	                layerTipMsg(jsonO.success,"失败",jsonO.msg);
	            }
	            layer.close(ii);
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown){}
	    });
	})
  
  
    function doDelete(id,stuName){
        layer.confirm("确定要删除<span style='color:red'>"+stuName+"</span>的选课信息吗？", {
          title: ['提示','font-size:20px;'],
          btn: ['确认','取消'] //按钮
        }, function(){
            doDeleteById(id);
        }, function(){
            layer.closeAll();
        });
    }
    function doDeleteById(id){
        var ii = layer.load();
        $.ajax({
            url:'${request.contextPath}/gkelective/${arrangeId}/chosenSubject/delete',
            data: {'studentId':id},
            type:'post',
            success:function(data) {
                var jsonO = JSON.parse(data);
                if(jsonO.success){
                    layer.closeAll();
                    layerTipMsg(jsonO.success,"成功",jsonO.msg);
                    findByCondition();
                }
                else{
                    layerTipMsg(jsonO.success,"失败",jsonO.msg);
                }
                layer.close(ii);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }
</script>
