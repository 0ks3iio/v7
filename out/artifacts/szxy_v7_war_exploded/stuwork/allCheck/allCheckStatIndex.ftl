<#import "/stuwork/tree/dytreemacro.ftl" as dytreemacro>
<script src="${request.contextPath}/static/components/zTree/js/jquery.ztree.all.min.js"></script>
<link rel="stylesheet" href="${request.contextPath}/static/components/zTree/css/zTreeStyle.css">
<div class="box box-default">
	<div class="box-body">
		<div class="filter-container">
			<div class="filter" id="searchType">
				<div class="filter-item">
					<span class="filter-name">学年：</span>
					<div class="filter-content">
						<select name="acadyear" id="acadyear" onchange="changeWeek()" class="form-control">
							<#if acadyearList?exists && acadyearList?size gt 0>
								<#list acadyearList as item >
									<option value="${item!}" <#if item==dormDto.acadyear?default("")>selected="selected"</#if>>${item!}</option>
								</#list>
							</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">学期：</span>
					<div class="filter-content">
						<select name="semesterStr" id="semesterStr" onchange="changeWeek()" class="form-control">
							<option value="1" <#if "1"==dormDto.semesterStr?default("")>selected="selected"</#if>>第一学期</option>
							<option value="2" <#if "2"==dormDto.semesterStr?default("")>selected="selected"</#if>>第二学期</option>
						</select>
					</div>
				</div>
				<div class="filter-item">
					<span class="filter-name">周次：</span>
					<div class="filter-content">
						<select name="week" id="week" onchange="doSearch()" class="form-control">
							<#if weekList?exists && weekList?size gt 0>
								<#list weekList as item >
									<option value="${item!}" <#if item==dormDto.week?default(0)>selected="selected"</#if>>${item!}</option>
								</#list>
							</#if>
						</select>
					</div>
				</div>
				<div class="filter-item">
				<a href="javascript:" class="btn btn-blue pull-right" style="margin-bottom:5px;" onclick="saveStat()">统计</a>
				</div>
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="classId">
<div class="row">
	<div class="col-sm-3">
		<div class="box box-default" id="id1">
			<div class="box-header">
				<h3 class="box-title">班级菜单</h3>
			</div>
			<@dytreemacro.gradeClassForSchoolInsetTree height="550" click="onTreeClick"/>
		</div>
	</div>
	<div class="col-sm-9" id="showList">
		<div class="box-body" id="id2">										
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
		var week=$("#week").val();
		if(week==undefined || week==""){
			layerTipMsgWarn("提示","此学年学期周次未维护");
			return;
		}
		if(classId==undefined ||classId==""){
			classId=$("#classId").val();
			if(classId==undefined ||classId==""){
				return;
			}
		}
	    var   url =  '${request.contextPath}/stuwork/allCheck/statList/page?classId='+classId+"&"+searchUrlValue("#searchType");
        $("#showList").load(url);
	}
	function changeWeek(){
		var acadyear=$("#acadyear").val();
		var semesterStr=$("#semesterStr").val();
		$.ajax({
			url:'${request.contextPath}/stuwork/allCheck/setWeekList',
			data: {'acadyear':acadyear,'semesterStr':semesterStr},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		var weekList=jsonO.weekList;
	 			$("#week").empty();
	 			var weekHtml="";
	 			if(weekList!=undefined){
			 		for(var i=0;i<weekList.length;i++){
			 			weekHtml+='<option value="'+weekList[i].week+'">';
			 			weekHtml+=weekList[i].week+'</option>';
			 		}
	 			}
		 		$("#week").append(weekHtml);
		 		doSearch("");
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
	var isSubmit=false;
	function saveStat(){
		if(isSubmit){
			return false;
		}
		isSubmit=true;
		var acadyear=$("#acadyear").val();
		var semesterStr=$("#semesterStr").val();
		var week=$("#week").val();
		var classId=$("#classId").val();
		if(week==undefined || week==""){
			layerTipMsgWarn("提示","此学年学期周次未维护");
			isSubmit=false;
			return;
		}
		$.ajax({
			url:'${request.contextPath}/stuwork/allCheck/statSave/page',
			data: {'acadyear':acadyear,'semesterStr':semesterStr,'week':week},
			type:'post',
			success:function(data) {
				var jsonO = JSON.parse(data);
		 		if(jsonO.success){
		 			layerTipMsg(jsonO.success,"成功",jsonO.msg);
		 			if(classId!=null && classId!=""){
				  		doSearch(classId);
		 			}
		 		}else{
		 			layerTipMsg(jsonO.success,"失败",jsonO.msg);
				}
				isSubmit=false;
			},
	 		error:function(XMLHttpRequest, textStatus, errorThrown){}
		});
	}
</script>

