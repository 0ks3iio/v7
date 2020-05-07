<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="gradeId" id="gradeId" class="form-control" onchange="changeSubject()">
						<#if gradeList?exists && (gradeList?size>0)>
		                    <#list gradeList as item>
			                     <option value="${item.id!}" <#if gradeId?default('')==item.id?default('')>selected</#if>>${item.gradeName!}</option>
		                    </#list>
		                <#else>
		                	 <option value=''>暂无数据</option>
		                </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">班级类型：</span>
				<div class="filter-content">
					<select name="classType" id="classType" class="form-control" onchange="changeSubject()">
		                     <option value="1" <#if classType?default('1')=='1'>selected</#if>>行政班</option>
		                     <option value="2" <#if classType?default('1')=='2'>selected</#if>>教学班</option>
					</select>
				</div>
			</div>
			<div class="filter-item subject-div">
  				<span class="filter-name">科目：</span>
  				<div class="filter-content">
  					<select name="subjectId" id="subjectId" class="form-control" style="width:188px;" onchange="changeTeachClass()">
  					</select>
				</div>
          	</div>
			<div class="filter-item">
				<span class="filter-name">班级：</span>
				<div class="filter-content">
					<select name="classId" id="classId" class="form-control" onchange="findImportIndex()">
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">是否覆盖：</span>
				<div class="filter-content">
					<select name="isCover" id="isCover" class="form-control" onchange="findImportIndex()">
						<option value="1">是</option>
						<option value="0" selected>否</option>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a href="javascript:" class="btn btn-blue" onclick="backSeatSetIndex()">返回</a>
			</div>
		</div>
		<div id="importContentDiv">
		</div>
	</div>
</div>
<script>
	$(function(){
		changeSubject('${subjectId!}','${classId!}');
	})
	function findImportIndex(){
		$("#importContentDiv").html('');
		var classId = $("#classId").val();
		if(classId==""){
			layer.tips("班级不能为空", "#classId", {
					tipsMore: true,
					tips:3
				});
			return;
		}

		var isCover = $("#isCover").val();
		$.ajax({
			url:"${request.contextPath}/eclasscard/seatSetImport/checkSeatSet",
			data:{"classId":classId},
			type:'post',
			dataType:'json',
			success:function(data){
				if(data.success){
					var parmUrl="&classId="+classId+"&isCover="+isCover;
					var url='${request.contextPath}/eclasscard/seatSetImport/index/page?'+parmUrl;
					$("#importContentDiv").load(url);
				}else{
					layer.msg(data.msg, {offset: 't',time: 2000});
				}
			}
		});

	}
	function backSeatSetIndex(){
		var gradeId = $("#gradeId").val();
		var classType = $("#classType").val();
		var classId = $("#classId").val();
		var url = '${request.contextPath}/eclasscard/standard/classSeating/index/page?gradeId='+gradeId+"&classType="+classType+"&classId="+classId;
		if(2==classType){
			var subjectId=$("#subjectId").val();
			url+="&subjectId="+subjectId;
		}
		$("#showList").load(url);
	}
	function changeSubject(subId,claId){
		var gradeId = $("#gradeId").val();
		var classType = $("#classType").val();
		if(1==classType){
			$(".subject-div").hide();
			$.ajax({
				url:"${request.contextPath}/eclasscard/seatSetImport/getClassList",
				data:{"gradeId":gradeId},
				type:'post',
				dataType:'json',
				success:function(data){
					$('#classId').empty();
					var classList = data;
					var sh = '';
					if(classList && classList.length>0){
						for(var i=0;i<classList.length;i++){
							sh+='<option value="'+classList[i].id+'"';
							if(claId && classList[i].id==claId){
								sh+=' selected="selected"';
							}
							sh+='>'+classList[i].className+'</option>';
						}
					}else{
						sh='<option value="">暂无数据</option>';
					}
					$('#classId').html(sh);
					findImportIndex();
				}
			});
		}else{
			$(".subject-div").show();
			$('#subjectId').empty();
			$.ajax({
				url:"${request.contextPath}/eclasscard/seatSetImport/getCourseList",
				data:{"gradeId":gradeId},
				type:'post',
				dataType:'json',
				success:function(data){
					var courseList = data;
					var sh = '';
					if(courseList && courseList.length>0){
						for(var i=0;i<courseList.length;i++){
							sh+='<option value="'+courseList[i].id+'"';
							if(subId && courseList[i].id==subId){
								sh+=' selected="selected"';
							}
							sh+='>'+courseList[i].subjectName+'</option>';
						}
					}else{
						sh='<option value="">暂无数据</option>';
					}
					$('#subjectId').html(sh);
					changeTeachClass(claId);
				}
			});
		}
	}

	function changeTeachClass(claId) {
		var gradeId = $("#gradeId").val();
		var subjectId = $("#subjectId").val();
		$('#classId').empty();
		$.ajax({
			url:"${request.contextPath}/eclasscard/seatSetImport/getTeachClassList",
			data:{"gradeId":gradeId,"subjectId":subjectId},
			type:'post',
			dataType:'json',
			success:function(data){
				var teachClassList = data;
				var sh = '';
				if(teachClassList && teachClassList.length>0){
					for(var i=0;i<teachClassList.length;i++){
						sh+='<option value="'+teachClassList[i].id+'"';
						if(claId && teachClassList[i].id==claId){
							sh+=' selected="selected"';
						}
						sh+='>'+teachClassList[i].name+'</option>';
					}
				}else{
					sh='<option value="">暂无数据</option>';
				}
				$('#classId').html(sh);
				findImportIndex();
			}
		});
	}
</script>