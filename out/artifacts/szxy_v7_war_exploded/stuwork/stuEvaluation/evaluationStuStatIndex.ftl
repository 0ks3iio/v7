<#import "/stuwork/tree/dytreemacro.ftl" as dytreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="box box-default">
	<div class="row">
		<input type="hidden" id="classId">
		<div class="col-sm-3">
			<div class="box box-default" id="id1">
				<div class="box-header">
					<h3 class="box-title">班级菜单</h3>
				</div>
				<@dytreemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick"/>
			</div>
		</div>
		<div class="col-sm-9" id="showList">
		</div>
	</div>
</div>
<script>
	$(function(){
		$('#id2').height($('#id1').height());
					
		$('.week-choose .btn').on('click', function(){
			$(this).removeClass('btn-white').addClass('btn-blue').siblings().removeClass('btn-blue').addClass('btn-white')
		})
	})
	function onTreeClick(event, treeId, treeNode, clickFlag){
		if(treeNode.type == "class"){
			var id = treeNode.id;
			$("#classId").val(id);
			doSearch(id);
		}
	}
	function doSearch(classId){
		if(classId==undefined ||classId==""){
			classId=$("#classId").val();
			if(classId==undefined ||classId==""){
				return;
			}
		}
	    var   url =  '${request.contextPath}/stuwork/evaluation/stat/list?classId='+classId;
        $("#showList").load(url);
	}
</script>
