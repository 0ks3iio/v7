<script src="${request.contextPath}/static/components/chosen/chosen.jquery.min.js"></script>
		<div class="box box-default">
		    <div class="box-body">
		    	<div class="filter box-graybg mb10 no-padding-bottom">
					<div class="filter-item">
						<label for="" class="filter-name">学年：</label>
						<div class="filter-content">
							<select class="form-control" id="acadyear" onChange="changeExam()">
							<#if acadyearList?exists && acadyearList?size gt 0>
								<#list acadyearList as item>
								<option value="${item!}" <#if item==semester.acadyear?default("")>selected="selected"</#if>>${item!}</option>
								</#list>
							</#if>
							</select>
						</div>
					</div>
					<div class="filter-item">
						<label for="" class="filter-name">学期：</label>
						<div class="filter-content">
							<select class="form-control" id="semester" onChange="changeExam()">
								${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
							</select>
						</div>
					</div>		
			        <div class="filter-item">
						<label for="" class="filter-name">年级：</label>
						<div class="filter-content">
							<select class="form-control" id="gradeCode" onChange="changeExam()">
								<#if gradeList?exists && gradeList?size gt 0>
									<#list gradeList as item>
									<option value="${item.gradeCode!}" <#if item.gradeCode==gradeCode?default("")>selected="selected"</#if>>${item.gradeName!}</option>
									</#list>
								</#if>
							</select>
						</div>
					</div>	
					<div class="filter-item">
						<label for="" class="filter-name">考试名称：</label>
						<div class="filter-content" id="examDiv">
							<select vtype="selectOne" class="form-control" id="examId" onChange="<#if type?default("")=="1">changeSubjectList()<#else>searchList()</#if>">
								<option value="">---请选择---</option>
							</select>
						</div>
					</div>
					<#if type?default("")=="1">		
						<div class="filter-item">
							<label for="" class="filter-name">科目：</label>
							<div class="filter-content">
								<select class="form-control" id="subjectId" onChange="searchList()">
									<option value="">---请选择---</option>
								</select>
							</div>
						</div>
						<div class="filter-item filter-item-right">
							<button class="btn btn-blue" onclick="searchList();">查询</button>
							<button class="btn btn-white" onclick="doExport();">导出</button>
						</div>
					<#else>
						<div class="filter-item filter-item-right">
							<button class="btn btn-blue" onclick="toEditAbi();" >更改科目权重</button>
							<button class="btn btn-blue" onclick="searchList();">查询</button>
							<button class="btn btn-white" onclick="doExportAbi();">导出</button>
						</div>
					</#if>
				</div>
				<div id="tableDiv"></div>
		    </div>
		</div>
<#if type?default("")!="1">
	<div class="layer layer-weight">
		<div class="layer-content">
			<p>各个科目的标准分T乘以各自的权重并累加计算综合能力分。</p>
			<form id="checkType">
				<input type="hidden" id="statObjectId" name="statObjectId" value="${statObjectId!}">
				<table class="table table-striped table-hover no-margin">
					<thead>
						<tr>
							<th>序号</th>
							<th>科目</th>
							<th>权重比</th>
						</tr>
					</thead>
					<tbody id="abiTbody">
					</tbody>
				</table>
			</form>
		</div>
	</div>
</#if>
<script>
	$(function(){
		//初始化单选控件
		initChosenOne(".header_filter");
		changeExam();
	});
	function changeExam(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var examClass=$("#examId");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryExamList",
			data:{acadyear:acadyear,semester:semester,gradeCode:gradeCode,unitId:'${unitId!}'},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				examClass.html("");
				examClass.chosen("destroy");
				if(infolist.length==0){
					examClass.append("<option value='' >--请选择--</option>");
				}else{
					var examHtml='';
					for(var i = 0; i < infolist.length; i ++){
						examHtml="<option value='"+infolist[i].id+"' ";
						examHtml+=" >"+infolist[i].name+"</option>";
						examClass.append(examHtml);
					}
				}
				$(examClass).chosen({
					width:'200px',
					no_results_text:"未找到",//无搜索结果时显示的文本
					allow_single_deselect:true,//是否允许取消选择
					disable_search:false, //是否有搜索框出现
					search_contains:true,//模糊匹配，false是默认从第一个匹配
					//max_selected_options:1 //当select为多选时，最多选择个数
				}); 
				<#if type?default("")=="1">
					changeSubjectList();
				<#else>
					searchList();
				</#if>
			}
		});
	}
	function changeSubjectList(){
		var examId=$("#examId").val();
		var subjectIdClass=$("#subjectId");
		$.ajax({
			url:"${request.contextPath}/exammanage/common/queryExamSubList",
			data:{examId:examId,unitId:'${unitId!}',fromNewGrade:'true'},
			dataType: "json",
			success: function(data){
				var infolist=data.infolist;
				subjectIdClass.html("");
				if(infolist==null || infolist.length==0){
					subjectIdClass.append("<option value='' >---请选择---</option>");
				}else{
					var subjectHtml='';
					for(var i = 0; i < infolist.length; i ++){
						subjectHtml="<option value='"+infolist[i].id+"' ";
						subjectHtml+=" >"+infolist[i].name+"</option>";
						subjectIdClass.append(subjectHtml);
					}
				}
				searchList();
			}
		});
	}
	function searchList(){
		layer.msg("正在加载中,请稍后...");
		var examId=$("#examId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		<#if type?default("")=="1">
			var subjectId=$("#subjectId").val();
			var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&subjectId='+subjectId+'&gradeCode='+gradeCode;
			var url='${request.contextPath}/examanalysis/examNewGrade/List/page'+c2;
			$("#tableDiv").load(url);
		<#else>
			var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&gradeCode='+gradeCode;
			var url='${request.contextPath}/examanalysis/examNewGrade/abilityList/page'+c2;
			$("#tableDiv").load(url);
		</#if>
	}
	function doExport(){
		var examId=$("#examId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var subjectId=$("#subjectId").val();
		var gradeCode=$("#gradeCode").val();
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&subjectId='+subjectId+'&gradeCode='+gradeCode;
		location.href="${request.contextPath}/examanalysis/examNewGrade/export"+c2;
	}
	function doExportAbi(){
		var examId=$("#examId").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeCode=$("#gradeCode").val();
		var c2='?examId='+examId+'&acadyear='+acadyear+'&semester='+semester+'&gradeCode='+gradeCode;
		location.href="${request.contextPath}/examanalysis/examNewGrade/exportAbi"+c2;
	}
	function toEditAbi(){
		var examId=$("#examId").val();
		if(!examId){
			layer.tips('请选择考试', $("#examDiv"), {
				tipsMore: true,
				tips: 3
			});
			return;
		}
	    $.ajax({
			url:"${request.contextPath}/examanalysis/examNewGrade/abiEdit/page",
			data:{examId:examId},
			dataType: "json",
			success: function(data){
				$("#statObjectId").val(data.statObjectId);
				var courses=data.courses;
				var abiMap=data.abiMap;
				var abiTbody=$("#abiTbody");
				abiTbody.html("");
				if(courses!=null){
					var abiTbodyHtml='';
					var courseId='';
					for(var i = 0; i < courses.length; i ++){
						courseId=courses[i].id;
						abiTbodyHtml="<tr><td>"+(i+1)+"</td>";
						abiTbodyHtml+="<td>"+courses[i].subjectName+"</td>";
						abiTbodyHtml+="<td><input type='text' class='form-control abiWeigthClass' vtype='number' maxLength='5' nullable='false' name='abiList["+i+"].weights' id='abiList"+i+"' ";
						if(abiMap[courseId]){
							abiTbodyHtml+=" value='"+abiMap[courseId].weights+"'>";
						}else{
							abiTbodyHtml+=" value='1'> ";
						}
						abiTbodyHtml+="<input type='hidden' name='abiList["+i+"].subjectId' value='"+courseId+"'></td></tr>";
						abiTbody.append(abiTbodyHtml);
					}
				}
				openAbiEdit(examId);
			}
		});
	}
	var isSubmit=false;
	function openAbiEdit(examId){
		var index = layer.open({
			type: 1,
	        shadow: 0.5,
	        area: '350px',
	        title: '更改科目权重',
	        btn: ['保存并计算', '取消'],
			content: $('.layer-weight'),
			yes:function(index,layerDiv){
				var chooseType=$(layerDiv).find("input[name='chooseType']:checked").val();
				if(isSubmit){
					return;
				}
				isSubmit=true;
				var check = checkValue('#checkType');
				if(!check){
				 	isSubmit=false;
				 	return;
				}
				var reg=/^(0|[1-9]\d{0,2})(\.\d{1})?$/;
				var f=false;
				$(".abiWeigthClass").each(function(){
					var r = $(this).val().match(reg);
					var max=$(this).siblings().val();
					if(r==null){
						f=true;
						layer.tips('格式不正确(最多3位整数，1位小数)!', $(this), {
							tipsMore: true,
							tips: 3
						});
						f=true;
						return false;
					}
				});	
				if(f){
					isSubmit=false;
				 	return;
				}
				var layerIndex = layer.load();
				layer.close(index);
				
				var options = {
		            url:'${request.contextPath}/examanalysis/examNewGrade/abiSave',
		            data:{'examId':examId},
		            dataType : 'json',
		            type : 'post',
		            success : function(data){
		                var jsonO = data;
		                if(!jsonO.success){
		                   layer.msg("操作失败", {
	                            offset: 't',
	                            time: 2000    });
		                    isSubmit = false;
		                }else{
		                    layer.msg("操作成功", { offset: 't',time: 2000 });
		                    isSubmit = false;
		                }
		                layer.close(layerIndex);
		                searchList();
		            },
		            error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错
		        };
		        $('#checkType').ajaxSubmit(options);
			},
			btn2:function(index){
				layer.close(index);
			}
		});
	}
</script>