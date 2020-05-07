<#--学生列表(通用)
	1-> 行政班
	2-> 教学班
-->

<#import "/fw/macro/webmacro.ftl" as w>
<#import "/basedata/class/detailmacro.ftl" as d>
<#if type?default('')=="2">
<#if students?exists && students?size gt 0>
<#list students?keys as key>
<#assign item=students[key] />
<div class="col-lg-3 confirm-student-list" triggerId="d-${key}">
<table id="simple-table" class="table  table-bordered table-hover confirmStuListTable">
	<thead>
		<tr>
			<th>姓名</th>
			<th>班级</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#if item?exists && item?size gt 0>
		<#list item as student>
		<tr>
			<td id="${student.student.id!}">${student.student.studentName!}</td>
			<td>${student.className!}</td>
			<td>
				<div class="action-buttons">
					<a class="red" href="javascript:;" onclick="doDelete('${student.student.id!}')">
						<i class="fa fa-trash bigger-130"></i>
					</a>
				</div>
			</td>
		</tr>
		</#list>
		<#else>
			<tr>
			<td colspan="4">
				<p class="alert alert-info center" style="padding:10px;margin:0;">暂时还没有数据哦</p>
			</td>
			<tr>
		</#if>
	</tbody>
	</table>
</div>
</#list>
</#if>
<div class="col-lg-3 confirm-student-list-none" style="display:none;">
	<table id="simple-table" class="table  table-bordered table-hover confirmStuListTable">
		<thead>
			<tr>
				<th>姓名</th>
				<th>班级</th>
				<th>操作</th>
			</tr>
		</thead>
	</table>
	</div>
<#else>
	<div class="col-lg-12 grade-list">
<table id="simple-table" class="table  table-bordered table-hover studentTable">
	<thead>
		<tr>
			<th class="center">
				<label class="pos-rel">
					<input type="checkbox"  class="ace"/>
					<span class="lbl" id="checkAll" chck="false"></span>
				</label>
			</th>
			<th>学生姓名</th>
			<th>班级</th>
			<th>身份证号码</th>
		</tr>
	</thead>
	<tbody>
		<#if students?exists && students?size gt 0>
		<#list students as student>
		<tr>
			<td class="center cbx_td">
				<label class="pos-rel">
					<input type="checkbox"  class="ace" name="nnnn"/>
					<!--多个自定义参数-->
					<span class="lbl" id="${student.student.id!}" studentName="${student.student.studentName!}" classid="${student.student.classId!}" classname="${student.className!}" chk="false"></span>
				</label>
			</td>
			<td>${student.student.studentName!}</td>
			<td class="hidden-480">${student.className!}</td>
			<td class="hidden-480">${student.student.identityCard!}</td>
		</tr>
		</#list>
		<#else>
			<tr>
			<td colspan="4">
				<p class="alert alert-info center" style="padding:10px;margin:0;">暂时还没有数据哦</p>
			</td>
			<tr>
		</#if>
	</tbody>
	</table>
</div>
</#if>
<#if type?default('')=="1">
<@w.pagination  container=".studentListDiv" pagination=pagination page_index=2 callback="bindClickFunction"/>
<#else>
</#if>
<!-- page specific plugin scripts -->
<script type="text/javascript">
	var indexDiv = 0;
	$('.page-content-area').ace_ajax('loadScripts', [], function() {
		//页面大小变化时的操作
		$(window).bind("resize",function(){
			var width = $(".row").width();
			var jqwidth=$(".jqGrid_wrapper").width();
			layer.style(indexDiv, {
				//width:calWidthPx('body', null, null)
			});
		});
		var allSize=$("input[name='nnnn']").length;
		var selSize=0;
		<#if type?default('')=="1">
			$(".confirmStuListTable").each(function(){
				$(this).find("td").each(function(){
					var selectId = $(this).attr("id");
					if(typeof(selectId)!="undefined"){
						var isCheck = checkSelect(selectId);
						if(isCheck){
							selSize=selSize+1;
						}
					}
				});
				if(allSize==selSize){
					$("#checkAll").attr("chck","true");
					$("#checkAll").prev("input").attr("checked","checked");
				}
			});
			$("#checkAll").unbind("click").bind("click",function(){

				var check = $("#checkAll").attr("chck")=="true"?true:false;
				if(check){
						$("#checkAll").attr("chck","false");
						//取消全选
						$(".cbx_td").each(function(){
						if($(this).find("input[name='nnnn']").is(":checked")){
								$(this).find("span").click();
							}
						});
				}
				//全选
				else{
					$("#checkAll").attr("chck","true");
					//全选
					$(".cbx_td").each(function(){
					if(!$(this).find("input[name='nnnn']").is(":checked")){
						$(this).find("span").click();
					}
				});
					
				}
			});
		</#if>
	});
	function checkSelect(id){
		var isCheck = false;
		$('.studentTable').find("span").each(function(){
			if($(this).attr("id")==id){
				$(this).prev("input").attr("checked","checked");
				$(this).attr("chk","true");
				isCheck = true;
			}
		});
		return isCheck;
	}
</script>
