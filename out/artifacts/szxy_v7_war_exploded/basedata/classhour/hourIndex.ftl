<style>
	.xnClassSpan span{
		width:150px
	}
</style>
<div class="filter">
	<div class="filter-item">
		<span class="filter-name">学年：</span>
		<div class="filter-content">
			<select name="acadyear" id="acadyear" class="form-control" onchange="loadCourseList()">
			<#if acadyearList?exists && (acadyearList?size>0)>
                <#list acadyearList as item>
                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
                </#list>
            <#else>
                <option value="">未设置</option>
             </#if>
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">学期：</span>
		<div class="filter-content">
			<select name="semester" id="semester" class="form-control" onchange="loadCourseList()">
				${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">年级：</span>
		<div class="filter-content">
			<select name="gradeId" id="gradeId" class="form-control" onchange="loadCourseList()">
			<#if gradeList?exists && (gradeList?size>0)>
                <#list gradeList as item>
                     <option value="${item.id!}">${item.gradeName!}</option>
                </#list>
             <#else>
            	<option value="">暂无年级</option>
             </#if>
		</select>
		</div>
	</div>
	<div class="filter-item">
		<span class="filter-name">课程：</span>
		<div class="filter-content">
			<select name="courseId" id="courseId" class="form-control" onchange="loadXuniClass()">
            	<option value="">暂无数据</option>
			</select>
		</div>
	</div>
</div>
<div class="box-body no-padding-top xn_nodata" style="display:none">
	<div class="no-data-container mt20">
		<div class="no-data">
			<span class="no-data-img">
				<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
			</span>
			<div class="no-data-body">
				<p class="no-data-txt">暂无记录</p>
			</div>
		</div>
	</div>
</div>
<div id="timeClassTableXn" style="background-color: #ededed;">
	<div class="publish-course publish-course-made xnClassSpan">              
	</div>
</div>
<div id="timeTableXn" style="margin-top:10px;">
</div>
<script>
	$(function(){
		loadCourseList();
		initSpans();
	})
	var oldHourId="";
	function initSpans(){
		$(".xnClassSpan").on('click','span',function(){
			$(".xnClassSpan span").removeClass("active");
			$(this).addClass("active");
			loadXnTime();
		})
	}
	
	function loadCourseList(){
		//获取虚拟课程id
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var gradeId=$('#gradeId').val();
		oldHourId="";
		if(acadyear=="" || semester=="" || gradeId==""){
			$("#courseId").html('<option value="">暂无数据</option>');
			return;
		}
		$.ajax({
			url:'${request.contextPath}/basedata/classhour/findCourseList',
			data:{'acadyear':acadyear,'semester':semester,'gradeId':gradeId},
			type:'post', 
			dataType:'json',
			success:function(data){
				var htmlText='';
				if(data!=null && data.length>0){
					var obj = data;
					for(var iii=0;iii<obj.length;iii++){
						htmlText=htmlText+'<option value="'+obj[iii].id+'" >'+obj[iii].subjectName+'</option>';
					}
				}else{
					htmlText='<option value="">暂无数据</option>';
				}
				$("#courseId").html(htmlText);
				loadXuniClass();
			}
		});
	}
	function noDataFunction(){
		$(".xn_nodata").show();
		$("#timeClassTableXn").find(".xnClassSpan").html("");
		$("#timeClassTableXn").hide();
		$("#timeTableXn").html("");
		$("#timeTableXn").hide();
	}
	
	function hasDataFunction(){
		$(".xn_nodata").hide();
		$("#timeClassTableXn").show();
		$("#timeTableXn").show();
	}
	
	function loadXuniClass(){
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var gradeId=$('#gradeId').val();
		var courseId=$('#courseId').val();
		if(acadyear=="" || semester=="" || gradeId=="" || courseId==""){
			noDataFunction();
			return;
		}
		$.ajax({
			url:'${request.contextPath}/basedata/classhour/findXnClassList',
			data:{'acadyear':acadyear,'semester':semester,'gradeId':gradeId,"subjectId":courseId},
			type:'post', 
			dataType:'json',
			success:function(data){
				if(data!=null && data.length>0){
					hasDataFunction();
					//默认第一个选中
					var obj = data;
					var htmlText='';
					for(var iii=0;iii<obj.length;iii++){
						var classNames=obj[iii].classNames;
						if(classNames.length>10){
							classNames=classNames.substring(0,10)+"...";
						}
						if(iii==0){
							htmlText=htmlText+'<span class="active" hourid="'+obj[iii].id+'"   title="'+obj[iii].classNames+'" >'+classNames+'</span> ';
						}else{
							htmlText=htmlText+'<span hourid="'+obj[iii].id+'" title="'+obj[iii].classNames+'"  >'+classNames+'</span> ';
						}
						
					}
					$(".xnClassSpan").html(htmlText);
					loadXnTime();
				}else{
					noDataFunction();
				}
			}
		});
	}
	
	function loadXnTime(){
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var gradeId=$("#gradeId").val();
		if(acadyear==""){
			layer.tips('不能为空!', $("#acadyear"), {
				time:1000,
				tipsMore: true,
				tips: 2
			});
			return;
		}
		if(semester==""){
			layer.tips('不能为空!', $("#semester"), {
				time:1000,
				tipsMore: true,
				tips: 2
			});
			return;
		}
		if(gradeId==""){
			layer.tips('不能为空!', $("#gradeId"), {
				time:1000,
				tipsMore: true,
				tips: 2
			});
			return;
		}
		var hourIdArr=$(".xnClassSpan").find(".active").length;
		if(hourIdArr==0){
			layer.tips('先选中一个!', $("#timeClassTableXn"), {
				time:1000,
				tipsMore: true,
				tips: 2
			});
			return;
		}
		var hourId=$(".xnClassSpan").find(".active").attr("hourid");
		if(oldHourId!="" && oldHourId==hourId){
			return;
		}
		if(hourId!=""){
			oldHourId=hourId;
		}
		var url="${request.contextPath}/basedata/classhour/list/page?acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId+"&hourId="+hourId;
		$("#timeTableXn").load(url);
	}
</script>