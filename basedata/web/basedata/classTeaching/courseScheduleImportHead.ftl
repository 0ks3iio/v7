<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select name="acadyear" id="acadyear" class="form-control" style="width:188px;" onchange="changeWeek()">
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
					<select name="semester" id="semester" class="form-control" style="width:188px;"  onchange="changeWeek()">
						${mcodeSetting.getMcodeSelect('DM-XQ',semester?default('0'),'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select name="gradeId" id="gradeId" class="form-control" onchange="findImportIndex()">
						<#if gradeList?exists && (gradeList?size>0)>
		                    <#list gradeList as item>
			                     <option value="${item.id!}" <#if gradeId?default('a')==item.id?default('b')>selected</#if>>${item.gradeName!}</option>
		                    </#list>
		                <#else>
		                	 <option value=''>暂无年级</option>
		                </#if>
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
			<#if tabIndex?default('2')=='2'>
			<div class="filter-item copy-schedule">
  				<span class="filter-name">模板周次：</span>
  				<div class="filter-content">
  					<select name="week" id="week" class="form-control" style="width:188px;" onchange="findImportIndex()">
  					 <#if (max??)>
                      <#list 1..max as t>
                      <option value="${t!}" <#if t==week>selected="selected"</#if>>第${t!}周</option>
                      </#list>
                    <#else>
                        <option value="">暂无数据</option>
                    </#if>
  					</select>
				</div>
          	</div>
          	</#if>
			<div class="filter-item filter-item-right">
				<a href="javascript:" class="btn btn-blue" onclick="toBackTeaching()">返回</a>
			</div>
		</div>
		<div id="importContentDiv">
		</div>
	</div>
</div>
<script>
	$(function(){
		findImportIndex();
	})
	function findImportIndex(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeId=$("#gradeId").val();
		var isCover=$("#isCover").val();
		var week=$("#week").val();
		if(acadyear==""){
			layer.tips("学年不能为空", "#acadyear", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		if(semester==""){
			layer.tips("学期不能为空", "#semester", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		if(gradeId==""){
			layer.tips("年级不能为空", "#gradeId", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		var parmUrl="acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&isCover="+isCover+"&week="+week;
		var url = '';
		if(${tabIndex?default(2)}==2){
			url='${request.contextPath}/basedata/classStu/courseScheduleImport/index/page?'+parmUrl;
		}else{
			url='${request.contextPath}/basedata/teacher/courseScheduleImport/index/page?'+parmUrl;
		}
		$("#importContentDiv").load(url);
	}
	function toBackTeaching(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeId=$("#gradeId").val();
		var parmUrl="?tabIndex=${tabIndex?default(2)}&acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
		var url =  '${request.contextPath}/basedata/courseopen/real/index/page'+parmUrl;
		$("#showList").load(url);
	}
	function changeWeek(){
		var acadyear = $("#acadyear").val();
		var semester = $("#semester").val();
		$.ajax({
			url:"${request.contextPath}/basedata/getWeek/json",
			data:{"acadyear":acadyear,"semester":semester},
			type:'post', 
			dataType:'json',
			success:function(data){
				$('#week').empty();
				var dataJson = data;
				var max = dataJson.max;
				var week = dataJson.week;
				var sh = '';
				if(max){
					for(var i=1;i<=max;i++){
						sh=sh+('<option value="'+i+'"');
						if(i==week){
							sh=sh+(' selected="selected"');
						}
						sh=sh+('>第'+i+'周</option>');
					}
				}else{
					sh='<option value="">暂无数据</option>';
				}
				$('#week').html(sh);
			}
		});
		findImportIndex();
	}
</script>