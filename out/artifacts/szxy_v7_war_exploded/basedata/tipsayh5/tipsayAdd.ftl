<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>新增代课</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link href="${request.contextPath}/static/mui/css/mui.min.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}/static/mui/css/mui.picker.min.css">
	<link href="${request.contextPath}/static/mui/css/pages.css" rel="stylesheet"/>
</head>
<body>
    <header class="mui-bar mui-bar-nav" style="height:0px;display:none;">
        <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left" id="cancelId"></a>
        <h1 class="mui-title">新增代课</h1>
    </header>		
    <div class="mui-content formDiv" style="padding-bottom: 51px;padding-top:5px;">
    	<form id="tipsayForm" name="tipsayForm">
    	<div class="mui-card">
    		<input type="hidden" id="operateuser" name="operateuser" value="${teacherId!}">
    		<input type="hidden" id="acadyear" name="acadyear" value="${acadyear!}">
    		<input type="hidden" id="semester" name="semester" value="${semester!}">
        	<ul class="mui-table-view">
	            <li class="mui-table-view-cell">
	            	<input type="hidden" id="teacherId" name="teacherId" value="${teacherId!}">
	                <a class="mui-navigate-right choose-teacher1">需代课教师：<span id="teacherName" style="margin-left:10px;">${teacherName!}</span></a>
	            </li>
	            <li class="mui-table-view-cell">
	                <a class="mui-navigate-right date" data-options='{"type":"date","beginYear":${beginYear?default(2017)},"endYear":${endYear?default(2018)},"beginMonth":${beginMonth?default(1)},"beginDay":${beginDay?default(1)},"endMonth":${endMonth?default(1)},"endDay":${endDay?default(1)}}'>需代课时间：<span class="result" style="margin-left:10px;"></span></a>
	            </li>
	            <li class="mui-table-view-cell">
	            	需代课节次    	    
		        	<div id="oldTeacherDiv">
		        	</div>
	            </li>
	        </ul>
        </div>
        <!-- 不冲突的情况 -->
        <div class="mui-card">
        	<ul class="mui-table-view">
	            <li class="mui-table-view-cell">
	            	<input type="hidden" id="newTeacherId" name="newTeacherId" value="">
	                <a class="mui-navigate-right choose-teacher2">代课教师：<span id="newTeacherName" style="margin-left:10px;"></span></a>
	            </li>
	            <li class="mui-table-view-cell">
	            	<a>当日课程<span class="mui-badge mui-badge-green confinct-mess">可以代课</span></a>
		        	<div id="newTeacherDiv">
		        	</div>
	            </li>
	        </ul>
        </div>
        <div class="mui-card">
        	<ul class="mui-table-view">
	            <li class="mui-table-view-cell">
	            	备注
	            </li>
	            <li class="mui-table-view-cell">
	            	<textarea name="remark" rows="" cols="" maxLength="250"></textarea>
	            </li>
	        </ul>
        </div>
      	</form>
    </div>
    <nav class="mui-bar mui-bar-tab formDiv">
        <a class="mui-tab-item mui-active f-16 save-tipsay" href="javascript:">确定</a>
    </nav>
    
     <div class="mui-content teacherDiv" style="padding-bottom: 51px;padding-top:5px;display:none;">
        <div class="mui-content-padded">
        	<div class="mui-input-row mui-search">
	    	    <input type="search" class="mui-input-clear searchTeacher" id="searchTeacher" placeholder="请输入教师名称">
	    	</div>
        </div>
        <div class="mui-card"  style="margin-top: -15px;">
        	<ul class="mui-table-view mui-table-view-chevron">
        		<#if groupDtoList?exists && groupDtoList?size gt 0>
        		<!--teachGroupName; mainTeacherList( teacherId,teacherName)-->
        		<#list groupDtoList as group>
        	    <li class="mui-table-view-cell mui-collapse choose-group">
        	        <a class="mui-navigate-right" href="#">${group.teachGroupName!}</a>
        	        <ul class="mui-table-view mui-table-view-chevron">
        	        	<#list group.mainTeacherList as tt>
    	                <li class="mui-table-view-cell choose-group-teacher" data-value="${tt.teacherId!}">${tt.teacherName!}</li>
    	                </#list>
    	            </ul>
        	    </li>
        	    </#list>
        	    <#else>
	        	    <li class="mui-table-view-cell mui-collapse">
	        	        <a class="mui-navigate-right" href="#">未设置教研组数据(请先去设置)</a>
	        	        <ul class="mui-table-view mui-table-view-chevron">
	    	            </ul>
	        	    </li>
        	    </#if>
        	</ul>
        </div>
    </div>
    <nav class="mui-bar mui-bar-tab teacherDiv teacherDivCancel" style="display:none">
        <a class="mui-tab-item mui-active f-16" href="javascript:">取消</a>
    </nav>
    
    <script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/jquery.form.js"></script>
    
    <script src="${request.contextPath}/static/mui/js/mui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${request.contextPath}/static/mui/js/mui.picker.min.js"></script>
    <script src="${request.contextPath}/static/mui/js/storage.js"></script>
	<script src="${request.contextPath}/static/mui/js/common.js"></script>
	<script src="${request.contextPath}/static/mui/js/weike.js"></script>
	<script src="${request.contextPath}/static/mui/js/myWeike.js"></script>
    <script type="text/javascript">
    mui.init();
    $('#cancelId').off('click').on('click',function(){
    	if(!$(".teacherDivCancel").is(":hidden")){
    		$(".formDiv").show();
			$(".teacherDiv").hide();
    	}else{
			var parm='&acadyear=${acadyear!}&semester=${semester!}&week=${week!}';
			var url = '${request.contextPath}/mobile/open/tipsay/head/page?unitId=${unitId!}&teacherId=${teacherId!}&adminType=${adminType!}'+parm;
			loadByHref(url);
		}
	});
	
    (function($) {
		$.init();
		var result = $('.result')[0];
		var btns = $('.date');
		btns.each(function(i, btn) {
			btn.addEventListener('tap', function() {
				var optionsJson = this.getAttribute('data-options') || '{}';
				var options = JSON.parse(optionsJson);
				var id = this.getAttribute('id');
				/*
				 * 首次显示时实例化组件
				 * 示例为了简洁，将 options 放在了按钮的 dom 上
				 * 也可以直接通过代码声明 optinos 用于实例化 DtPicker
				 */
				var picker = new $.DtPicker(options);
				picker.show(function(rs) {
					/*
					 * rs.value 拼合后的 value
					 * rs.text 拼合后的 text
					 * rs.y 年，可以通过 rs.y.vaue 和 rs.y.text 获取值和文本
					 * rs.m 月，用法同年
					 * rs.d 日，用法同年
					 * rs.h 时，用法同年
					 * rs.i 分（minutes 的第二个字母），用法同年
					 */
					var old=result.innerText;
					result.innerText = rs.text;
					if(rs.text!=old){
						var oldId=document.getElementById('teacherId').value;
						var newId=document.getElementById('newTeacherId').value;
						changeDate(oldId,"oldTeacherDiv",true);
						changeDate(newId,"newTeacherDiv",false);
					}
					/* 
					 * 返回 false 可以阻止选择框的关闭
					 * return false;
					 */
					/*
					 * 释放组件资源，释放后将将不能再操作组件
					 * 通常情况下，不需要示放组件，new DtPicker(options) 后，可以一直使用。
					 * 当前示例，因为内容较多，如不进行资原释放，在某些设备上会较慢。
					 * 所以每次用完便立即调用 dispose 进行释放，下次用时再创建新实例。
					 */
					picker.dispose();
				});
			}, false);
		});
	})(mui);
	
	
	var keyTeacher='1';
	$(".choose-teacher1").on('tap',function(){
		keyTeacher='1';
		$(".formDiv").hide();
		$(".teacherDiv").show();
		$(".teacherDiv").find(".choose-group").show();
		$(".teacherDiv").find(".choose-group-teacher").show();
		$("#searchTeacher").val("");
	})
	$(".choose-teacher2").on('tap',function(){
		keyTeacher='2';
		$(".formDiv").hide();
		$(".teacherDiv").show();
		$(".teacherDiv").find(".choose-group").show();
		$(".teacherDiv").find(".choose-group-teacher").show();
		$("#searchTeacher").val("");
		
	})
	
	$(".teacherDivCancel").on('tap',function(){
		$(".formDiv").show();
		$(".teacherDiv").hide();
	})
	
	$(".teacherDiv").find(".choose-group-teacher").on('tap',function(){
		var chooseteacherId=$(this).attr("data-value");
		var chooseteacherName=$(this).html();
		$(".formDiv").show();
		$(".teacherDiv").hide();
		if(keyTeacher=='1'){
			//需要代课的老师
			var oldTT=$("#teacherId").val();
			$("#teacherName").html(chooseteacherName);
			$("#teacherId").val(chooseteacherId);
			if(oldTT!=chooseteacherId){
				changeDate(chooseteacherId,"oldTeacherDiv",true);
			}
		}else{
			//选择代课老师
			var oldTT=$("#newTeacherId").val();
			$("#newTeacherName").html(chooseteacherName);
			$("#newTeacherId").val(chooseteacherId);
			if(oldTT!=chooseteacherId){
				changeDate(chooseteacherId,"newTeacherDiv",false);
			}
		}
		
	})
	
	//取得当天上课时间
	function changeDate(tId,keyDiv,isRadio){
		var chooseDate = $('.result')[0].innerText;
		if(chooseDate=="" || tId==""){
			//时间为空
			$("#"+keyDiv).html('');
			return;
		}
		//需要代课节次
		var acadyear=document.getElementById('acadyear').value;
		var semester=document.getElementById('semester').value;
		//根据学年学期+时间+老师tId
		$.ajax({
			url:'${request.contextPath}/mobile/open/tipsay/chooseByDateAndTeacherId',
			data:{'unitId':'${unitId!}','acadyear':acadyear,'semester':semester,'dateStr':chooseDate,'teacherId':tId},
			type:'post', 
			dataType:'json',
			success:function(data){
				if(data.success){
					if(data.msg==""){
						$("#"+keyDiv).html('');
						$(".confinct-mess").removeClass("mui-badge-red").addClass("mui-badge-green");
						$(".confinct-mess").html("可以代课");
					}else{
						var obj = JSON.parse(data.msg);
						if(obj.length==0){
							$("#"+keyDiv).html('');
							$(".confinct-mess").removeClass("mui-badge-red").addClass("mui-badge-green");
							$(".confinct-mess").html("可以代课");
						}else{
							makeList(obj,keyDiv,isRadio);
						}
					}
					
				}else{
					
					toastMsg(data.msg);
					
				}
			}
		});		
	}
	
	function makeList(jsonObjArr,keyDiv,isRadio){
		var htmlText='';
		for(var i=0;i<jsonObjArr.length;i++){
			var ttime=jsonObjArr[i].periodInterval+"_"+jsonObjArr[i].period;
			if(isRadio){
		        htmlText=htmlText+'<div class="mui-input-row mui-radio mui-left" data-value="'+ttime+'">'
        		   +'<label>'+jsonObjArr[i].remark+'</label>'
        		   +'<input name="courseScheduleId" type="radio" value="'+jsonObjArr[i].id+'">'
        			+'</div>';
			}else{
				htmlText=htmlText+'<div class="mui-input-row mui-radio" data-value="'+ttime+'">'
        		   +'<label>'+jsonObjArr[i].remark+'</label>'
        		+'</div>';
        	}
		}
		$("#"+keyDiv).html(htmlText);
		checkConflict();
	}
	
	
	$(".formDiv").on("click", "input[name='courseScheduleId']",function(){
		checkConflict();
	});
	
	
	
	
	
	function checkConflict(){
		var oldLength=$("#oldTeacherDiv").find(".mui-input-row").length;
		var newLength=$("#newTeacherDiv").find(".mui-input-row").length;
		var ss=$("input[name='courseScheduleId']:checked").length;
		if(oldLength==0 || newLength==0 || ss==0){
			if($(".confinct-mess").hasClass("mui-badge-green")){
				return;
			}else{
				$(".confinct-mess").removeClass("mui-badge-red").addClass("mui-badge-green");
				$(".confinct-mess").html("可以代课");
			}
		}
		var isConflict=false;
		//选中的节次
		var oldTime=$("input[name='courseScheduleId']:checked").parent().attr("data-value");
		$("#newTeacherDiv").find(".mui-input-row").each(function(){
			var tt=$(this).attr("data-value");
			if(oldTime==tt){
				//存在
				if($(this).find("label").hasClass("c-red")){
					
				}else{
					$(this).find("label").addClass("c-red");
				}
				if(!isConflict){
					isConflict=true;
				}
				//跳出循环
				//return false;
			}else{
				$(this).find("label").removeClass("c-red");
			}
		});
		
		
		if(isConflict){
			//冲突
			if($(".confinct-mess").hasClass("mui-badge-red")){
				return;
			}else{
				$(".confinct-mess").removeClass("mui-badge-green").addClass("mui-badge-red");
				$(".confinct-mess").html("发现冲突");
			}
		}else{
			if($(".confinct-mess").hasClass("mui-badge-green")){
				return;
			}else{
				$(".confinct-mess").removeClass("mui-badge-red").addClass("mui-badge-green");
				$(".confinct-mess").html("可以代课");
			}
		}
	}
	
	var isflag=false;
	$(".save-tipsay").on("tap",function(){
		if(isflag){
			return;
		}
		isflag=true;
		//判断
		var teacherId=$("#teacherId").val();
		if(teacherId==""){
			isflag=false;
			toastMsg("请选择需要代课的老师");
			return false;
		}
		var newTeacherId=$("#newTeacherId").val();
		if(newTeacherId==""){
			isflag=false;
			toastMsg("请选择代课的老师");
			return false;
		}
		if(teacherId==newTeacherId){
			isflag=false;
			toastMsg("代课老师与原教师为同一个人，请重新选择代课老师");
			return false;
		}
		if($(".confinct-mess").hasClass("mui-badge-red")){
			isflag=false;
			toastMsg("所选的代课老师在这节课已经有课");
			return false;
		}
		var ss=$("input[name='courseScheduleId']:checked").length;
		if(ss==0){
			isflag=false;
			toastMsg("请选择需要代课的课程");
			return false;
		}
		var options = {
			url : "${request.contextPath}/mobile/open/tipsay/saveTipsay",
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			isflag=false;
		 			toastMsg(data.msg);
		 		}else{
		 			toastMsg(data.msg);
					var parm='&acadyear=${acadyear!}&semester=${semester!}&week=${week!}';
					var url = '${request.contextPath}/mobile/open/tipsay/head/page?unitId=${unitId!}&teacherId=${teacherId!}&adminType=${adminType!}'+parm;
					loadByHref(url);
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#tipsayForm").ajaxSubmit(options);
		
	})
	
	
	$('#searchTeacher').bind('search',function(){
		var searchText=$("#searchTeacher").val();
		var allLength=0;
		if(searchText=="" || searchText.trim()==""){
			//取消搜索 还原
			$(".teacherDiv").find(".choose-group").show();
			$(".teacherDiv").find(".choose-group-teacher").show();
		}else{
			$(".teacherDiv").find(".choose-group").each(function(){
				var groupLl=$(this);
				var length=0;
				$(groupLl).find(".choose-group-teacher").each(function(){
					var tName=$(this).html();
					if(tName.indexOf(searchText)>-1){
						$(this).show();
						length++;
						allLength++;
					}else{
						$(this).hide();
					}
				});
				if(length==0){
					$(groupLl).hide();
				}else{
					$(groupLl).show();
				}
			});
			if(allLength==0){
				toastMsg("没有找到对应老师，可能是该老师不存在于任意一个教研组");
			}
		}
	})
	
    </script>
</body>
</html>
