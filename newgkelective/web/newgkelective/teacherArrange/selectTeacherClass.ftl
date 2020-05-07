<script type="text/javascript" >
</script>

<div class="sortContainer">
    <div class="hd">科目</div>
    <#if courseList?exists && courseList?size gt 0 >
    <div id="course-tabs-ul" class="bd">
        <#assign cc="">
		<#list courseList as course>
			<#if course_index == 0>
				<#assign cc=course.id>
			</#if>
            <a <#if course.id==courseId?default(cc)> class="active"</#if> role="presentation" data-subject-types="${course.subjectTypes!}" data-course-id="${course.id!}" href="#a${course_index!}" role="tab" data-toggle="tab">${course.subjectName!}</a>
		</#list>
    </div>
	</#if>
</div>
<div class="tab-content" id="teacherArrangeDiv" style="margin-left: 110px;padding-top: 0px">
</div>

<!-- 复制到 -->
<div class="layer layer-copy">
	<div class="layer-content">
		<div class="explain" style="margin-bottom:5px;">
			<p>请先确保您的修改已经保存</p>
		</div>
		<table width="100%">
			<tr>
				<td width="100"><p class="mb10">当前教师：</p></td>
				<td>
					<p class="mb10" id="curTeacher"></p>
				</td>
			</tr>
			<tr>
				<td valign="top"><p class="mb10">复制内容：</p></td>
				<td>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="1" class="wp"><span class="lbl"> 每天课时分配</span></label>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="2" class="wp"><span class="lbl"> 周课时分布</span></label>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="3" class="wp"><span class="lbl"> 互斥设置</span></label>
					<label class="mb10 mr20"><input type="checkbox" name="copyField" value="4" class="wp"><span class="lbl"> 禁排时间</span></label>
				</td>
			</tr>
		</table>
		<div class="gk-copy" style="border: 1px solid #eee;">
			<div class="box-body padding-5 clearfix">
				<b class="float-left mt3">各科教师</b>
				<div class="float-right input-group input-group-sm input-group-search">
			        <div class="pull-left">
			        	<input type="text" id="findTeacher" class="form-control input-sm js-search" placeholder="输入教师姓名查询" value="">
			        </div>
				    <div class="input-group-btn">
				    	<button type="button" class="btn btn-default" onClick="findTeacher();">
					    	<i class="fa fa-search"></i>
					    </button>
				    </div>
			    </div>
			</div>
			<table class="table no-margin">
				<tr>
					<th width="127">科目</th>
					<th><label><input type="checkbox" name="copyTeacherAll" class="wp"><span class="lbl"> 全选</span></label></th>
				</tr>
			</table>
			<div class="gk-copy-side" id="myscrollspy">
				<ul class="nav gk-copy-nav" style="margin: 0 -1px 0 -1px;height:378px;">
					<#if courseList?exists && courseList?size gt 0 >
	                    <#list courseList as course>
	                    	<li id="course_${course.id!}" class="courseLi"><a href="#aaa_${course.id!}" data-value="${course.id!}">${course.subjectName!}
	                    	<span class="badge badge-default"></span></a></li>
	                    </#list>
	                </#if>
				</ul>
				</ul>
			</div>
			<div class="gk-copy-main copyteacherTab">
				<div data-spy="scroll" data-target="#myscrollspy" id="scrollspyDivId" style="position:relative;height:378px;overflow:auto;border-left: 1px solid #eee;">
					<#if courseList?exists && courseList?size gt 0 >
	                    <#list courseList as course>
	                    	<div id="aaa_${course.id!}"  data-value="${course.id!}">
								<div class="form-title ml-15 mt10 mb10">${course.subjectName!}<a class="color-blue ml10 font-12 js-clearChoose" href="javascript:">取消</a> <a class="color-blue ml10 font-12 js-allChoose" href="javascript:">全选</a> </div>
								<ul class="gk-copy-list">
									 <#assign teachTeachers = subjectTeacherPlanExMap[course.id] />
									 <#if teachTeachers?exists && teachTeachers?size gt 0>
	                               	     <#list teachTeachers as tt >
											<label class="mr20"><input type="checkbox" class="wp" name="copyTeacher" value="${tt.id!}" data-value="${tt.teacherName!}"><span class="lbl"> ${tt.teacherName!}</span></label>
										 </#list>
									 </#if>
								</ul>
							</div>
	                    </#list>
	                </#if>
				</div>
			</div>
		</div>
		
		<div class="no-data-container" id="noDataId" style="display:none;">
			<div class="no-data">
				<span class="no-data-img">
					<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
				</span>
				<div class="no-data-body">
					<p class="no-data-txt">没有相关数据</p>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	$(function(){
		$('#scrollspyDivId').scrollspy({ target: '#myscrollspy' });
		
		 //回车
		$('#findTeacher').bind('keypress',function(event){//监听回车事件
	        if(event.keyCode == "13" || event.which == "13" )    
	        {  
	            findTeacher();
	        }
	    });
		
		$('#course-tabs-ul a').on('click',function(){
		    $(this).siblings().each(function () {
				$(this).removeClass("active");
            });
            //console.log("切换 课程");
		    $(this).addClass("active");
		    var cid = $(this).attr('data-course-id');
			var url="${request.contextPath}/newgkelective/${divideId!}/teacherClass/selected?gradeId=${gradeId!}&arrayId=${arrayId!}&arrayItemId=${arrayItemId!}&courseId="+cid;
			$('#teacherArrangeDiv').load(url);
		});
		
		var $init = $('#course-tabs-ul a.active');
        var cid = $init.attr('data-course-id');
        var url="${request.contextPath}/newgkelective/${divideId!}/teacherClass/selected?gradeId=${gradeId!}&arrayId=${arrayId!}"
        	+"&arrayItemId=${arrayItemId!}&courseId="+cid+"&useMaster=${useMaster!}";
        $('#teacherArrangeDiv').load(url);
       
       
    });
	
	function getWeekTime(oldTime, sid){
		<#if courseList?exists && courseList?size gt 0 >
		<#list courseList as course>
			if(sid.indexOf('${course.id!}') != -1){
				
			}
		</#list>
		<#else>
			return oldTime;
		</#if>
	}
	function findTeacher(){
		var teacherName=$('#findTeacher').val().trim();
		if(teacherName!=""){
			$(".gk-copy-main .lbl").removeClass("color-blue");
			var first;
			$(".gk-copy-main input").each(function(){
				if ($(this).attr("data-value").includes(teacherName)) {
					if(!first){
						first=$(this);
					}
					$(this).siblings().addClass("color-blue");
				}
			});
			if(first){
				//模仿锚点定位
				var divId=$(first).parents("div").attr("id");
				document.getElementById(divId).scrollIntoView();
				
				var buid = $(first).parents("div").attr("data-value");
				document.getElementById("course_"+buid).scrollIntoView();
				setTimeout(function(){
					$("#course_"+buid).siblings().removeClass("active");
					$("#course_"+buid).addClass("active");
				},50);
			}
		}else{
			$(".gk-copy-main .lbl").removeClass("color-blue");
		}
	}
	
	//点中数量
	$(".copyteacherTab").off('click').on('click','input:checkbox[name=copyTeacher]',function(){
		var closeDiv=$(this).closest("div");
		var course_id=$(closeDiv).attr("data-value");
		var num=$("#course_"+course_id).find("span.badge").text();
		if(num.trim()==""){
			num=parseInt(0);
		}else{
			num=parseInt(num);
		}
		if($(this).is(":checked")){
			//+1
			num=num+1;
		}else{
			//-1
			num=num-1;
		}
		if(num>0){
			$("#course_"+course_id).find("span.badge").text(""+num);
			//用取消
			$(closeDiv).find(".js-allChoose").hide();
			$(closeDiv).find(".js-clearChoose").show();
		}else{
			$("#course_"+course_id).find("span.badge").text("");
			//用全选
			$(closeDiv).find(".js-allChoose").show();
			$(closeDiv).find(".js-clearChoose").hide();
		}
	})
	
	$('input:checkbox[name=copyTeacherAll]').on('change',function(){
		var actioveDiv=$(".copyteacherTab").find("div.active");
		if($(this).is(':checked')){
			$('input:checkbox[name=copyTeacher]').each(function(i){
				if(!$(this).is(':disabled')){
					$(this).prop('checked',true);
				}
			})
		}else{
			$('input:checkbox[name=copyTeacher]').each(function(i){
				$(this).prop('checked',false);
			})
		}
		//整体数量操作
		$(".courseLi").each(function(){
			var cid=$(this).find("a").attr("data-value");
			//计算数量
			var length=$("#aaa_"+cid).find('input:checkbox[name=copyTeacher]:checked').length;
			if(length>0){
				$(this).find("span").text(""+length);
				$("#aaa_"+cid).find(".js-allChoose").hide();
				$("#aaa_"+cid).find(".js-clearChoose").show();
			}else{
				$(this).find("span").text("");
				$("#aaa_"+cid).find(".js-allChoose").show();
				$("#aaa_"+cid).find(".js-clearChoose").hide();
			}
		})
	});
	
	$(".js-allChoose").on('click',function(){
		var closeDiv=$(this).parent().parent();
		var cId=$(closeDiv).attr("data-value");
		var num=0;
		$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
			if(!$(this).is(':disabled')){
				$(this).prop('checked',true);
				num++;
			}
		})
		$("#course_"+cId).find("span").text(""+num);
		$(closeDiv).find(".js-allChoose").hide();
		$(closeDiv).find(".js-clearChoose").show();
	})
	
	$(".js-clearChoose").on('click',function(){
		var closeDiv=$(this).parent().parent();
		var cId=$(closeDiv).attr("data-value");
		$(closeDiv).find('input:checkbox[name=copyTeacher]').each(function(i){
			$(this).prop('checked',false);
		})
		$("#course_"+cId).find("span").text("");
		$(closeDiv).find(".js-allChoose").show();
		$(closeDiv).find(".js-clearChoose").hide();
	})
	
	
</script>