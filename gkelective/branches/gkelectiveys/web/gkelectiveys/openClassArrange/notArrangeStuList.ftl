<div class="box-body">
	<div class="clearfix">
	<em>共${isChosenCount!}个学生选课&nbsp;&nbsp;&nbsp;&nbsp;参考分：在参考成绩中的排班成绩依据中读取&nbsp;&nbsp;&nbsp;&nbsp;<span style="color:red">注：未安排学生需要尽可能安排才能进入下一步<#if (unChosenCount?default(0)>0)>&nbsp;&nbsp;&nbsp;&nbsp;另有${unChosenCount!}人未选课</#if></span></em>
	</div>
	<#if (rounds.step == 3)>
		<#assign nowStep=true>
	<#else>
		<#assign nowStep=false>
	</#if>
	<#if (unAllNum?default(0) > 0) && nowStep>
		<div class="filter-item noprint" style="margin-right:10px;">
			<span class="filter-name">请输入开班数（所有未安排的学生开为混合班，排除未选课的学生。开班后可在<em>手动开班结果</em>步骤中查看）：</span>
			<div class="filter-content">
				<input type="text" style="width:50px;margin-right:5px;" class="form-control" name="openNum" id="openNum" value=""  maxlength="3" >
			</div>
			<div class="filter-item-right">
				<a href="javascript:"  class="btn btn-sm btn-blue js-openSomeClass" onclick="openSomeClass()">智能分班</a>
			</div>
		</div>
	</#if>
	<table class="table table-striped table-bordered table-hover no-margin mainTable">
	    <thead>
	        <tr>
	            <th width="10%">学号</th>
	            <th width="10%">姓名</th>
	            <th width="8%">性别</th>
	            <th>行政班</th>
                <th>
                	选1(参考分)
                </th>
				<th>
					选2(参考分)
				</th>
				<th>
					选3(参考分)
				</th>
				<#if nowStep>
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
				<td class="course-review subColClass1 " value="${gkResult.ids[0]!}">
					<input type="hidden" class="courseScores" value="${gkResult.courseScores[0]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[0]" value="${gkResult.ids[0]!}"/><input id="${gkResult.ids[0]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[0]" value="${gkResult.statues[0]!}"/>
					<span >${gkResult.courseNames[0]!}</span>
				</td>
				<td class="course-review subColClass2 " value="${gkResult.ids[1]!}">
					<input type="hidden" class="courseScores" value="${gkResult.courseScores[1]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[1]" value="${gkResult.ids[1]!}"/><input id="${gkResult.ids[1]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[1]" value="${gkResult.statues[1]!}"/>
					<span>${gkResult.courseNames[1]!}</span>
				</td>
				<td class="course-review subColClass3 " value="${gkResult.ids[2]!}">
					<input type="hidden" class="courseScores" value="${gkResult.courseScores[2]}"/><input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[2]" value="${gkResult.ids[2]!}"/><input id="${gkResult.ids[2]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[2]" value="${gkResult.statues[2]!}"/>
					<span>${gkResult.courseNames[2]!}</span>
				</td>
				<#if nowStep>
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
	<div class="page-footer-btns text-right noprint">
		<a class="btn btn-white preStep-btn" href="#">上一步</a>
		<#if (unAllNum?default(0) <= 10)>
		<a class="btn btn-blue nextStep-btn" href="#">下一步</a>
		</#if>
	</div>
</div>
<script>
	$(function(){
		$(".preStep-btn").on("click",function(){
			toGroupResult();
		});
		<#if (unAllNum?default(0) <= 10)>
		$(".nextStep-btn").on("click",function(){
        	toSingle();
		});
		</#if>
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
htmlMain+='<td class="course-review subColClass1 " value="${gkResult.ids[0]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[0]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[0]" value="${gkResult.ids[0]!}"/>'+'<input id="${gkResult.ids[0]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[0]" value="${gkResult.statues[0]!}"/>'+'<span >${gkResult.courseNames[0]!}</span>'+'</td>';
htmlMain+='<td class="course-review subColClass2 " value="${gkResult.ids[1]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[1]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[1]" value="${gkResult.ids[1]!}"/>'+'<input id="${gkResult.ids[1]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[1]" value="${gkResult.statues[1]!}"/>'+'<span>${gkResult.courseNames[1]!}</span>'+'</td>';
htmlMain+='<td class="course-review subColClass3 " value="${gkResult.ids[2]!}">'+'<input type="hidden" class="courseScores" value="${gkResult.courseScores[2]}">'+'<input type="hidden" class="gkResId" name="gkResults[${gkResult_index}].ids[2]" value="${gkResult.ids[2]!}"/>'+'<input id="${gkResult.ids[2]!}" class="gkResStatues" type="hidden" name="gkResults[${gkResult_index}].statues[2]" value="${gkResult.statues[2]!}"/>'+'<span>${gkResult.courseNames[2]!}</span>'+'</td>';
<#if nowStep>
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
		$(".mainTable").DataTable({
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
	            { "orderDataType": "dom-text-numeric${ind}", type: 'numeric' }<#if (subjectMap?size == ind) && nowStep>,<#else>,</#if>
	            </#list>
	            <#else>
	            { "orderDataType": "dom-text-numeric1", type: 'numeric' },
	            { "orderDataType": "dom-text-numeric2", type: 'numeric' },
	            { "orderDataType": "dom-text-numeric3", type: 'numeric' }<#if nowStep>,</#if>
	            </#if>
	            <#if nowStep>
	            { "orderable": false }
	            </#if>
	        ]
		});
		layer.close(ii);
	},10);
	});
	function editResult(studentId){
		var url = "${request.contextPath}/gkelective/${arrangeId}/chosenSubject/edit/page?studentId="+studentId;
		indexDiv = layerDivUrl(url,{title: "编辑选课信息",width:400,height:430});
	}
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
    function openSomeClass(){
		var num=$("#openNum").val().trim();;
		if(num == ""){
			layer.msg("不能为空！", {
				offset: 't',
				time: 2000
			});
			$("#openNum").val('');
			$("#openNum").focus();
			return false;
		}
		var pattern=/[^0-9]/;
		if(pattern.test(num) || num.slice(0,1)=="0"){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#openNum").val('');
			$("#openNum").focus();
			return false;
		}
		var rows=parseInt(num);
		if(rows<=0){
			layer.msg("只能输入非零的整数！", {
				offset: 't',
				time: 2000
			});
			$("#openNum").val('');
			$("#openNum").focus();
			return false;
		}
		subjectIds=$("#groupId").val();
		var options = {btn: ['确定','取消'],title:'确认信息', icon: 1,closeBtn:0};
		showConfirm("是否确定根据系统规则进行开班",options,function(){
		$.ajax({
			url:'${request.contextPath}/gkelective/'+roundsId+'/openClassArrange/autoOpenClass',
			data:{"subjectIds":"00000000000000000000000000000000","openNum":rows},
			dataType : 'json',
			type:'post',
			success:function(data) {
				var jsonO = data;
	 			layer.closeAll();
		 		if(jsonO.success){
					findByCondition();
		 		}
		 		else{
		 			layerTipMsg(jsonO.success,"失败","原因："+jsonO.msg);
				}
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
			
		},function(){});
		
	}
</script>
