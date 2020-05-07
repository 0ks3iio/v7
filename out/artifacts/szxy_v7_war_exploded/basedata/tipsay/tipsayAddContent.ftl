<#assign allPeriod=0>
<#assign mm=mm?default(0)>
<#assign am=am?default(0)>
<#assign pm=pm?default(0)>
<#assign nm=nm?default(0)>
<#assign allPeriod=mm+am+pm+nm>
<!---keyPeiodInval已经减了一节课---->
<#assign keyPeiodInval="">
<input type="hidden" id="week" value="${week!}">
<div class="col-xs-8">
	<table class="table table-bordered text-center replace-table js-track" id="centerSchedule">
		<thead>
			<tr>
				<th width="15%">教师名称</th>
				<th width="7%">时段</th>
				<th width="5%">序号</th>
				<#if weekDayList?exists && weekDayList?size gt 0>
				<#list weekDayList as weekDay>
					<th >${weekDay[1]!}</th>
				</#list>
				</#if>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td rowspan="${allPeriod?default(1)}" class="accordion">
					
					<div class="panel-group" id="accordion">
						<#if groupDtoList?exists && groupDtoList?size gt 0>
						<#list groupDtoList as dto>
	                    <div class="panel panel-default">
	                    	
	                        <div class="panel-heading">
	                            <h4 class="panel-title">
	                                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${dto.teachGroupId!}" data-value="${dto.teachGroupId!}" aria-expanded="false" class="collapsed">
										${dto.teachGroupName!}
	                                </a>
	                            </h4>
	                        </div>
	                        <div id="collapse${dto.teachGroupId}" class="panel-collapse collapse" aria-expanded="false">
	                            <ul class="list-group">
	                            <#if dto.mainTeacherList?exists && dto.mainTeacherList?size gt 0>
	                            <#list dto.mainTeacherList as tt>
	                            	<a class="list-group-item searchTeacher"  data-value="${tt.teacherId!}" data-groupId="${dto.teachGroupId!}" href="javascript:">${tt.teacherName!}</a>
	                             </#list>
	                             </#if>
	                            </ul>
	                        </div>
	                    </div>
	                    </#list>
	                    </#if>
	                </div>
				</td>
				<#assign keyPeiodInvalName="">
				<#if mm gt 0>
					<#assign keyPeiodInval="1">
					<#assign keyPeiodInvalName="早自习">
					<td rowspan="${mm}">早自习</td>
					<td>1</td>
				<#elseif am gt 0>
					<#assign keyPeiodInval="2">
					<#assign keyPeiodInvalName="上午">
					<td rowspan="${am}">上午</td>
					<td>1</td>
				<#elseif pm gt 0>
					<#assign keyPeiodInval="3">
					<#assign keyPeiodInvalName="下午">
					<td rowspan="${pm}">下午</td>
					<td>1</td>
				<#else>
					<#assign keyPeiodInval="4">
					<#assign keyPeiodInvalName="晚上">
					<td rowspan="${nm}">晚上</td>
					<td>1</td>
				</#if>
				<#if weekDayList?exists && weekDayList?size gt 0>
				<#list weekDayList as weekDay>
					<td class="item td_${weekDay[0]!}_${keyPeiodInval}_1" data-value="${weekDay[0]!}_${keyPeiodInval}_1" data-name="${weekDay[2]!}${keyPeiodInvalName!}第1节"></td>
				</#list>
				</#if>
			</tr>
			<#if mm gt 0>
				<#if keyPeiodInval=="1">
					<#if mm gt 1>
					<#list 2..mm as ii>
						<tr>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_1_${ii}" data-value="${weekDay[0]!}_1_${ii}" data-name="${weekDay[2]!}早自习第${ii}节"></td>
							</#list>
							</#if>
						</tr>
					</#list>
					</#if>
				<#else>
					<#list 1..mm as ii>
						<#if ii_index==0>
						<tr>
							<td rowspan="${mm}">早自习</td>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_1_${ii}" data-value="${weekDay[0]!}_1_${ii}" data-name="${weekDay[2]!}早自习第${ii}节"></td>
							</#list>
							</#if>
						</tr>	
						<#else>
						<tr>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_1_${ii}"  data-value="${weekDay[0]!}_1_${ii}"  data-name="${weekDay[2]!}早自习第${ii}节"></td>
							</#list>
							</#if>
						</tr>
						</#if>
					</#list>
				</#if>
			</#if>
			<#if am gt 0>
				<#if keyPeiodInval=="2">
					<#if am gt 1>
					<#list 2..am as ii>
						<tr>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_2_${ii}"  data-value="${weekDay[0]!}_2_${ii}"  data-name="${weekDay[2]!}上午第${ii}节"></td>
							</#list>
							</#if>
						</tr>
					</#list>
					</#if>
				<#else>
					<#list 1..am as ii>
						<#if ii_index==0>
						<tr>
							<td rowspan="${am}">上午</td>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_2_${ii}"  data-value="${weekDay[0]!}_2_${ii}"  data-name="${weekDay[2]!}上午第${ii}节"></td>
							</#list>
							</#if>
						</tr>	
						<#else>
						<tr>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_2_${ii}"  data-value="${weekDay[0]!}_2_${ii}" data-name="${weekDay[2]!}上午第${ii}节"></td>
							</#list>
							</#if>
						</tr>
						</#if>
					</#list>
				</#if>
			</#if>
			
			<#if pm gt 0>
				<#if keyPeiodInval=="3">
					<#if pm gt 1>
					<#list 2..pm as ii>
						<tr>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_3_${ii}" data-value="${weekDay[0]!}_3_${ii}" data-name="${weekDay[2]!}下午第${ii}节"></td>
							</#list>
							</#if>
						</tr>
					</#list>
					</#if>
				<#else>
					<#list 1..pm as ii>
						<#if ii_index==0>
						<tr>
							<td rowspan="${pm}">下午</td>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_3_${ii}" data-value="${weekDay[0]!}_3_${ii}" data-name="${weekDay[2]!}下午第${ii}节"></td>
							</#list>
							</#if>
						</tr>	
						<#else>
						<tr>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_3_${ii}" data-value="${weekDay[0]!}_3_${ii}" data-name="${weekDay[2]!}下午第${ii}节"></td>
							</#list>
							</#if>
						</tr>
						</#if>
					</#list>
				</#if>
			</#if>
			
			<#if nm gt 0>
				<#if keyPeiodInval=="4">
					<#if nn gt 1>
					<#list 2..nm as ii>
						<tr>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_4_${ii}" data-value="${weekDay[0]!}_4_${ii}" data-name="${weekDay[2]!}下午第${ii}节"></td>
							</#list>
							</#if>
						</tr>
					</#list>
					</#if>
				<#else>
					<#list 1..nm as ii>
						<#if ii_index==0>
						<tr>
							<td rowspan="${nm}">晚上</td>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_4_${ii}" data-value="${weekDay[0]!}_4_${ii}" data-name="${weekDay[2]!}下午第${ii}节"></td>
							</#list>
							</#if>
						</tr>	
						<#else>
						<tr>
							<td>${ii}</td>
							<#if weekDayList?exists && weekDayList?size gt 0>
							<#list weekDayList as weekDay>
								<td class="item td_${weekDay[0]!}_4_${ii}" data-value="${weekDay[0]!}_4_${ii}"  data-name="${weekDay[2]!}下午第${ii}节"></td>
							</#list>
							</#if>
						</tr>
						</#if>
					</#list>
				</#if>
			</#if>
		</tbody>
	</table>
</div>
<div class="col-xs-4">
	<input type="hidden" id="selectTeacherId" value=""/>
	<input type="hidden" id="selectGroupId" value=""/>
	<input type="hidden" id="clazzId" value=""/>
	<div class="nav-tabs-wrap">
		<ul class="nav nav-tabs  tab_ul">
			<li class="aa active"><a href="javascript:" data-toggle="tab" onclick="searchTab('aa')">同教研组教师</a></li>
			<li class="bb"><a href="javascript:" data-toggle="tab" onclick="searchTab('bb')">本班任课教师</a></li>
			<li class="cc"><a href="javascript:" data-toggle="tab" onclick="searchTab('cc')">全校教师</a></li>
		</ul>
	</div>				
	<div class="tab-content" id="teacherTab">
		<div class="replace-list-outter">
			<div class="filter">
				<div class="filter-item">
					<div class="filter-content">
						<div class="input-group input-group-search">
					        <div class="pull-left">
					        	<input type="text" id="inputTeacherName" class="form-control" placeholder="请输入教师姓名">
					        </div>		    
						    <div class="input-group-btn">
						    	<a type="button" class="btn btn-default btn-searchTeacherName">
							    	<i class="fa fa-search"></i>
							    </a>
						    </div>
					    </div>
					</div>
				</div>
		    </div>	
			<ul class="replace-list" style="display:none;">
				
			</ul>
			<div class="no-data-container">
				<div class="no-data">
					<span class="no-data-img">
						<img src="${request.contextPath}/static/images/public/nodata.png" alt="">
					</span>
					<div class="no-data-body">
						<p class="no-data-txt color-grey inputMessName">请选择需要代课的老师</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<h3><span id="rightTeacherName"></span>老师的课表</h3>
	<table class="table table-bordered text-center replace-sub-table js-track" id="rightSchedule">
		<thead>
			<tr>
				<th style="padding:1px"></th>
				<th style="padding:1px"></th>
				<#if weekDayList?exists && weekDayList?size gt 0>
				<#list weekDayList as weekDay>
					<th style="padding:1px">${weekDay[1]!}</th>
				</#list>
				</#if>
			</tr>
		</thead>
		<tbody>
			<#if mm gt 0>
				<#list 1..mm as ii>
					<#if ii_index==0>
					<tr>
						<td rowspan="${mm}">早自习</td>
						<td>${ii}</td>
						<#if weekDayList?exists && weekDayList?size gt 0>
						<#list weekDayList as weekDay>
							<td class="item td_${weekDay[0]!}_1_${ii}"></td>
						</#list>
						</#if>
					</tr>	
					<#else>
					<tr>
						<td>${ii}</td>
						<#if weekDayList?exists && weekDayList?size gt 0>
						<#list weekDayList as weekDay>
							<td class="item td_${weekDay[0]!}_1_${ii}"></td>
						</#list>
						</#if>
					</tr>
					</#if>
				</#list>
			</#if>
			<#if am gt 0>
				<#list 1..am as ii>
					<#if ii_index==0>
					<tr>
						<td rowspan="${am}">上午</td>
						<td>${ii}</td>
						<#if weekDayList?exists && weekDayList?size gt 0>
						<#list weekDayList as weekDay>
							<td class="item td_${weekDay[0]!}_2_${ii}"></td>
						</#list>
						</#if>
					</tr>	
					<#else>
					<tr>
						<td>${ii}</td>
						<#if weekDayList?exists && weekDayList?size gt 0>
						<#list weekDayList as weekDay>
							<td class="item td_${weekDay[0]!}_2_${ii}"></td>
						</#list>
						</#if>
					</tr>
					</#if>
				</#list>
			</#if>
			<#if pm gt 0>
				<#list 1..pm as ii>
					<#if ii_index==0>
					<tr>
						<td rowspan="${pm}">下午</td>
						<td>${ii}</td>
						<#if weekDayList?exists && weekDayList?size gt 0>
						<#list weekDayList as weekDay>
							<td class="item td_${weekDay[0]!}_3_${ii}"></td>
						</#list>
						</#if>
					</tr>	
					<#else>
					<tr>
						<td>${ii}</td>
						<#if weekDayList?exists && weekDayList?size gt 0>
						<#list weekDayList as weekDay>
							<td class="item td_${weekDay[0]!}_3_${ii}"></td>
						</#list>
						</#if>
					</tr>
					</#if>
				</#list>
			</#if>
			<#if nm gt 0>
				<#list 1..nm as ii>
					<#if ii_index==0>
					<tr>
						<td rowspan="${nm}">晚上</td>
						<td>${ii}</td>
						<#if weekDayList?exists && weekDayList?size gt 0>
						<#list weekDayList as weekDay>
							<td class="item td_${weekDay[0]!}_4_${ii}"></td>
						</#list>
						</#if>
					</tr>	
					<#else>
					<tr>
						<td>${ii}</td>
						<#if weekDayList?exists && weekDayList?size gt 0>
						<#list weekDayList as weekDay>
							<td class="item td_${weekDay[0]!}_4_${ii}"></td>
						</#list>
						</#if>
					</tr>
					</#if>
				</#list>
			</#if>
		</tbody>
	</table>	
</div>
<script>
	
	$(function(){
		
		//点击教研组下某个老师对应的事件
		$("#accordion").on('click',".searchTeacher",function(){
			$("#accordion").find(".searchTeacher").removeClass("active");
			$(this).addClass("active");
			//右上角隐藏tab参数
			var selectTeacherId=$(this).attr("data-value");
			$("#selectTeacherId").val(selectTeacherId);
			var selectGroupId=$(this).attr("data-groupId");
			$("#selectGroupId").val(selectGroupId);
			//组装
			var acadyear=$('#acadyear').val();
			var semester=$('#semester').val();
			var week= $("#week").val();
			searchSchedules(acadyear,semester,week,selectTeacherId,"centerSchedule",true);
			searchRight();
		})
	

		//中间上课点击事件
		$(".replace-table .item").click(function(){
			if(!$(this).text() == ''){
				var timeTd=$(this).attr("data-value");
				$(".js-track").each(function(index){
					$(this).find(".item").removeClass('active');
				});
				$(".td_"+timeTd).addClass('active');
				var clazzId=$(this).find(".clazzId_item").attr("data-value");
				$("#clazzId").val(clazzId);
			}
			//代课展现的数据隐藏 防止切换的时候数据不对
			var forms=$("#teacherTab").find(".replace-list").find("form");
			if(forms){
				forms.hide();
			}
		});
		$(".btn-searchTeacherName").on('click',function(){
			searchRight();
		})
		
		var $replaceSubTable = $(".replace-sub-table").height();
		$(".replace-list-outter").height(713 - $replaceSubTable);
		
		
	})
	
	function searchSchedules(acadyear,semester,week,teacherId,key,showId){
		if(teacherId ==""){
			$("#"+key).find(".item").html("");
			return;
		}
		$.ajax({
			url:'${request.contextPath}/basedata/tipsay/scheduleByTeacherId',
			data:{'acadyear':acadyear,'semester':semester,'week':week,'teacherId':teacherId},
			type:'post', 
			dataType:'json',
			success:function(data){
				$("#"+key).find(".item").html("");
				if(data!=""){
					var obj = eval(data);
					if(obj.length==0){
						
					}else{
						for(var i=0;i<obj.length;i++){
							var dataKey=obj[i].dayOfWeek+"_"+obj[i].periodInterval+"_"+obj[i].period;
							var htmlTex='';
							if(showId){
								htmlTex='<input type="hidden" class="courseScheduleId" value="'+obj[i].id+'">';
							
								if(obj[i].subjectName){
									htmlTex=htmlTex+'<span class="subjectId_item">'+obj[i].subjectName+'</span><br/>';
								}else{
									htmlTex=htmlTex+'<span class="subjectId_item"></span><br/>';
								}
								if(obj[i].className){
									htmlTex=htmlTex+'<span class="clazzId_item" data-value="'+obj[i].classId+'">'+obj[i].className+'</span>';
								}else{
									htmlTex=htmlTex+'<span class="clazzId_item" data-value=""></span>';
								}
							}else{
								if(obj[i].subjectName){
									htmlTex=htmlTex+'<span class="subjectId_item">'+obj[i].subjectName+'</span><br/>';
								}
							}
							$("#"+key).find(".td_"+dataKey).html(htmlTex);
						}
					} 
				}
				
				
			}
		});		
	}
	
	function searchRight(){
		var keyTab="aa";
		$(".tab_ul").find("li").each(function(){
			if($(this).hasClass("active")){
				if($(this).hasClass("aa")){
					keyTab="aa";
				}else if($(this).hasClass("bb")){
					keyTab="bb";
				}else{
					keyTab="cc";
				}
				return false;
			}
		})
		searchTab(keyTab);
	}
	function searchTab(tabkey){
		$(".tab_ul").find("li").removeClass("active");
		$("."+tabkey).addClass("active");
		var inputTeacherName=$("#inputTeacherName").val();
		var acadyear=$("#acadyear").val();
		var semester=$("#semester").val();
		var type="";
		var teacherId="";
		var groupId="";
		var clazzId="";
		var teacherName=inputTeacherName;

		if("aa"==tabkey){
			//根据选中teacherId
			var selectTeacherId=$("#selectTeacherId").val();
			var selectGroupId=$("#selectGroupId").val();
			if(selectGroupId=="" || selectTeacherId==""){
				$("#teacherTab").find(".inputMessName").html("请选择需要代课的老师");
				$("#teacherTab").find(".replace-list").html("");
				$("#teacherTab").find(".no-data-container").show();
				$("#teacherTab").find(".replace-list").hide();
				return;
			}else{
				//加载后台老师
				type="1";
				teacherId=selectTeacherId;
				groupId=selectGroupId;
			}
			
		}else if("bb"==tabkey){
			clazzId=$("#clazzId").val();
			if(clazzId=="" || selectGroupId==""){
				$("#teacherTab").find(".inputMessName").html("请选择需要代课的课程");
				$("#teacherTab").find(".replace-list").html("");
				$("#teacherTab").find(".no-data-container").show();
				$("#teacherTab").find(".replace-list").hide();
				return;
			}else{
				//加载后台老师replace-list
				type="2";
			}
		}else{
			if(inputTeacherName=="" || inputTeacherName.trim()==""){
				$("#teacherTab").find(".inputMessName").html("请输入教师姓名查询");
				$("#teacherTab").find(".replace-list").html("");
				$("#teacherTab").find(".no-data-container").show();
				$("#teacherTab").find(".replace-list").hide();
				return;
			}else{
				//加载后台老师replace-list
				type="3";
			}
		}
		loadTeacherId(acadyear,semester,type,teacherId,groupId,clazzId,teacherName);
	}
	
	function loadTeacherId(acadyear,semester,type,teacherId,groupId,clazzId,teacherName){
		$("#teacherTab").find(".no-data-container").hide();
		$("#teacherTab").find(".replace-list").show();
		var url='${request.contextPath}/basedata/tipsay/loadTeacherList';
		var parm="acadyear="+acadyear+"&semester="+semester+"&type="+type+"&teacherId="+teacherId+"&groupId="+groupId+"&classId="+clazzId
					+"&teacherName="+teacherName;
		url=encodeURI(url+"?"+parm);
		$("#teacherTab").find(".replace-list").load(url);
	}
	
</script>

