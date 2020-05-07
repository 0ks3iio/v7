<div class="row">
	<input type = "hidden" value="${acadyear!}" id="acadyear"/>
	<input type = "hidden" value="${semester!}" id="semester"/>
	<input type = "hidden" value="${gradeId!}" id="gradeId"/>
	<div class="col-xs-12">
		<div class="box box-default">
			<div class="box-body clearfix">
                <div class="tab-container">
					<div class="tab-header clearfix">
						<ul class="nav nav-tabs nav-tabs-1">
						 	<li <#if type! != '2'>class="active"</#if> id="li1">
						 		<a data-toggle="tab" href="javascript:;" onclick="loadTabList('1');">日常表现登记</a>
						 	</li>
						 	<li <#if type! == '2'>class="active"</#if> id="li2">
						 		<a data-toggle="tab" href="javascript:;" onclick="loadTabList('2');">成绩登记</a>
						 	</li>
						</ul>
					</div>
					<!-- tab切换开始 -->
					<div class="tab-content" id="editDiv">
					</div><!-- tab切换结束 -->
			    </div>
			</div>
		</div>
	</div><!-- /.col -->
</div><!-- /.row -->
<script>

	function gobackIndex(){
		var acadyear = $('#acadyear').val();
		var semester = $('#semester').val();
		var url =  '${request.contextPath}/exammanage/credit/register?acadyear='+acadyear+'&semester='+semester;
		$('#model-div-37097').load(url);
	}
	
	$(function(){
		loadTabList('${type!}');
		<#if hasAdmin?exists && hasAdmin == '1'>
			showBreadBack(gobackIndex,true,"返回");
		</#if>
	});
	
	function loadTabList(type) {
		if(type == '2'){
			$("#li2").addClass("active");
			$("#li1").removeClass("active");
			var url='${request.contextPath}/exammanage/credit/register/exam?acadyear=${acadyear!}&semester=${semester!}&gradeId=${gradeId!}';		
		}else{
			$("#li1").addClass("active");
			$("#li2").removeClass("active");
			var url='${request.contextPath}/exammanage/credit/register/daily?acadyear=${acadyear!}&semester=${semester!}&gradeId=${gradeId!}';
		}
		$("#editDiv").load(url); 
	}
</script>