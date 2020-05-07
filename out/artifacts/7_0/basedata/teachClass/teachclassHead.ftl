<div class="filter">
	<input type="hidden" id="tabType" value="${tabType!}" />
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="acadyearSearch" id="acadyearSearch" class="form-control" style="width:188px;" onchange="serarchBySemester()">
				<#if acadyearList?exists && acadyearList?size gt 0>
			     	<#list acadyearList as item>
			     		<option value="${item!}" <#if acadyear?default('')== item >selected</#if> >${item!}学年</option>
			     	</#list>
			     <#else>
			     	<option value="">--未设置--</option>
			     </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select name="semesterSearch" id="semesterSearch" class="form-control" style="width:188px;"  onchange="serarchBySemester()">
				${mcodeSetting.getMcodeSelect('DM-XQ',semester?default('0'),'0')}
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">科目：</span>
		<div class="filter-content">
			<select name="subjectId" id="subjectId" class="form-control" style="width:188px;" onchange="findList()">
			     	<option value="">--全部--</option>
			</select>
		</div>
	</div>
	<#if tabType?default('')=='2' && limit?default('') == '0'>
		<div class="filter-item">
			<span class="filter-name">班级类型：</span>
			<div class="filter-content">
				<select name="teachClassType" id="teachClassType" class="form-control" style="width:188px;" onchange="findList()">
				   	<option value="">--全部--</option>
					<option value="1">普通班</option>
					<option value="2">用于合并小班</option>
					<option value="3">合并大班</option>
				</select>
			</div>
		</div>
	</#if>	
	
</div>
<div class="filter">
 	<div class="filter-item" style="height:auto;">
		<span class="filter-name" id="gradeIdSpan">面向对象：</span>
		<div class="filter-content">
			<#if gradeList?exists && gradeList?size gt 0>
			<#list gradeList as grade>
				<label class="pos-rel labelchose-w">
					<input name="gradeId" type="checkbox" <#if gradeIds?default('')?index_of(grade.id) gte 0>checked</#if> value="${grade.id!}" class="wp"/>
					<span class="lbl"> ${grade.gradeName!}</span>
			    </label>
			</#list>  
			</#if>
		</div>
	</div>
</div>
<div class="filter">
	<div class="filter-item">
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <div class="pos-rel pull-left">
		        	<input type="text" id="teachClassName" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="输入教学班名称查询" maxlength="25">
		        </div>
			    <div class="input-group-btn">
			    	<a type="button" onclick="findList()" class="btn btn-default">
				    	<i class="fa fa-search"></i>
				    </a>
			    </div>
		    </div>
		</div>
	</div>
	<div class="filter-item">
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <div class="pos-rel pull-left">
		        	<input type="text" id="studentName" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="输入学生名称查询" maxlength="8">
		        </div>
			    <div class="input-group-btn">
			    	<a type="button" onclick="findList()" class="btn btn-default">
				    	<i class="fa fa-search"></i>
				    </a>
			    </div>
		    </div>
		</div>
	</div>
	<div class="filter-item">
		<div class="filter-content">
			<div class="input-group input-group-search">
		        <div class="pos-rel pull-left">
		        	<input type="text" id="teacherName1" class="typeahead scrollable form-control" autocomplete="off" data-provide="typeahead" placeholder="输入老师名称查询" maxlength="8">
		        </div>
			    <div class="input-group-btn">
			    	<a type="button" onclick="findList()" class="btn btn-default">
				    	<i class="fa fa-search"></i>
				    </a>
			    </div>
		    </div>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<a href="javascript:" class="btn btn-blue js-searchClass" onclick="findList()">查询</a>
		<#--<#if tabType?default('')!='0'>-->
		<a href="javascript:" class="btn btn-blue js-addClass" onclick="addNewTeachClass()">新增教学班</a>
			<#if limit?default('') == '0' && tabType?default('')=='2'>
				<a href="javascript:" class="btn btn-blue js-addClass" onclick="mergeClass()">合并教学班</a>
			</#if>
		<#--</#if>-->
	</div>
	
</div>
<div class="tab-content" id="listDiv">
	
</div>


<script type="text/javascript">
	$(function(){
		$("#teachClassName").val('${teachClassName!}');
		$("#studentName").val('${studentName!}');
		$("#teacherName1").val('${teacherName!}');
		serarchBySemester();
	})
	$("#teachClassName").bind('keypress',function(event){
	    if(event.keyCode == "13")    
	    {
	      findList();
	    }
	});
	function serarchBySemester(){
		var acadyearSearch=$("#acadyearSearch").val();
		var semesterSearch=$("#semesterSearch").val();
		var $subjectClass=$("#subjectId");
		$.ajax({
			url:'${request.contextPath}/basedata/teachclass/gradeOpenSubject',
			data:{acadyearSearch:acadyearSearch,semesterSearch:semesterSearch,showTabType:'${tabType!}'},
			dataType : 'json',
			success: function(data){
				$subjectClass.html("");
				$subjectClass.append("<option value='' >--全部--</option>");
				if(data.length>0){
					for(var i = 0; i < data.length; i ++){
						$subjectClass.append("<option value='"+data[i].id+"' >"+data[i].subjectName+"</option>");
					}
				}
				findList();
			}			
		});
	}
	function makeGradeIds(){
		var gradeIds="";
		$("input[name='gradeId']:checked").each(function(){
			var gradeId=$(this).val();
			gradeIds=gradeIds+","+gradeId;
		});
		if(gradeIds!=""){
			gradeIds=gradeIds.substring(1);
		}
		return gradeIds;
	}
	function findList(useMaster){
		var acadyearSearch=$("#acadyearSearch").val();
		var semesterSearch=$("#semesterSearch").val();
		var gradeIds=makeGradeIds();
		var subjectId=$("#subjectId").val();
		var showTabType=$("#tabType").val();
		var teachClassName=$("#teachClassName").val();
		var studentName = $("#studentName").val();
		var teacherName = $("#teacherName1").val();
		<#if tabType?default('')=='2'>
			var teachClassType = $("#teachClassType").val();
			var url =  '${request.contextPath}/basedata/teachclass/teachclassList/page';
			url=url+'?acadyearSearch='+acadyearSearch+'&semesterSearch='+semesterSearch+'&gradeIds='+gradeIds+'&showTabType='+showTabType+'&subjectId='+subjectId+'&teachClassName='+teachClassName+'&studentName='+studentName+'&teachClassType=' + teachClassType+'&teacherName='+teacherName;
		<#else>
			var url =  '${request.contextPath}/basedata/teachclass/teachclassList/page';
			url=url+'?acadyearSearch='+acadyearSearch+'&semesterSearch='+semesterSearch+'&gradeIds='+gradeIds+'&showTabType='+showTabType+'&subjectId='+subjectId+'&teachClassName='+teachClassName+'&studentName='+studentName+'&teacherName='+teacherName;
		</#if>
		
		url += "&useMaster="+useMaster;
		$("#listDiv").load(encodeURI(url));
	}
	<#--<#if tabType?default('')!='0'>-->
	function addNewTeachClass(){
		var acadyearSearch=$("#acadyearSearch").val();
		var semesterSearch=$("#semesterSearch").val();
		var length=$("input[name='gradeId']:checked").length;
		var gradeIds=makeGradeIds();
		var showTabType=$("#tabType").val();
		var title="新增教学班";
		if(showTabType=="2"){
			title=title+"（选修课）";
			if(!length==1){
				layer.tips("新增选修课教学班，至少选中一个年级", "#gradeIdSpan", {
					tipsMore: true,
					tips:3				
				});
				return false;
			}
		}else if(showTabType=="1"){
			title=title+"（必修课）";
			//必须先选择年级，不存在跨年级
			if(!(length==1)){
				layer.tips("新增必修课教学班，必须只能选某一个年级", "#gradeIdSpan", {
					tipsMore: true,
					tips:3				
				});
				return false;
			}
		}
		var url = "${request.contextPath}/basedata/teachclass/addOredit/page?acadyearSearch="+acadyearSearch+"&semesterSearch="+semesterSearch+"&showTabType="+showTabType+"&gradeIds="+gradeIds;
	    indexDiv = layerDivUrl(url,{title: title,width:800,height:800});
	}
	
	<#if limit?default('') == '0' && tabType?default('')=='2'>
		function mergeClass(){
			var acadyearSearch=$("#acadyearSearch").val();
			var semesterSearch=$("#semesterSearch").val();
			var showTabType=$("#tabType").val();
			$.ajax({
				url:'${request.contextPath}/basedata/teachclass/check/mergeclass', 
				data:{acadyearSearch:acadyearSearch,semesterSearch:semesterSearch},
			    dataType:'json',
			    type:'post',
			    success:function(data) {
			    	if(data.success){
					    var url =  "${request.contextPath}/basedata/teachclass/mergeclass/index/page?acadyearSearch="+acadyearSearch+"&semesterSearch="+semesterSearch+"&showTabType="+showTabType;
						$("#indexContent").load(url);
			        }else{
			         	layerTipMsg(data.success,"失败",data.msg);
			        }
			    },
			    error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			});	
		}
	</#if>
	<#--</#if>-->
</script>
