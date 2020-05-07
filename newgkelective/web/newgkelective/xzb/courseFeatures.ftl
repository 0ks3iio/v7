<div class="filter">
	<div class="filter-item">
		<a href="javascript:void(0)" class="btn btn-white js-courseAdd">新增科目</a>
	</div>
	<div class="filter-item">
		<b class="float-left mt3" style="font-size:15px;">
			行政班课程课时：<span id="lecCountXzb">0</span>,
			走班课程总课时：<span id="lecCountMove">0</span>
		</b>
	</div>
</div>
<form id="mySubjectTime">
<input type="hidden" class="js-noContinueSubjectIds" name="noContinueSubjectIds" id="noContinueSubjectIds" value="">
<div class="table-container">
	<div class="table-container-body" style="overflow-x: auto;">
		<table class="table table-bordered table-striped table-hover" id="subjectTable">
			<thead>
				<tr>
					<th rowspan="2">序<br>号</th>
					<th rowspan="2">科目</th>
					<th colspan="2" class="text-center">课时设置</th>
					<th colspan="2" class="text-center">连堂设置 <a class="js-setting-tip js-continue-tip" href="javascript:void(0);"><i class="fa fa-question-circle color-yellow"></i></a></th>
					<th rowspan="2">不连排<br>科目</th>
					<th rowspan="2">课时分配</th>
					<th rowspan="2">半天内<br>课时分配</th>
					<th rowspan="2">禁排时间</th>
					<th rowspan="2">优先级</th>
					<#--th rowspan="2">是否<br>需要考勤</th-->  
					<th rowspan="2">是否<br>需要教室</th>
					<th rowspan="2">操作</th>
				</tr>
				<tr>
					<th width="93">周课时数</th>
					<th>单双周科目</th>
					<th width="93">连堂次数</th>
					<th>连堂时间范围</th>
				</tr>
			</thead>
			<tbody>
				<#if dtoList?exists && dtoList?size gt 0>
				<#list dtoList as dto>
				<#assign indexNumber=dto_index>
 				<tr>
				    <td>
				    <input type="hidden" name="subjectIdType" value="${(dto.subjectId + '-' + dto.subjectType)!}">
					<input type="hidden" name="dtoList[${indexNumber}].subjectId" value="${dto.subjectId!}">
					<input type="hidden" name="dtoList[${indexNumber}].subjectType" class="subjectType" value="${dto.subjectType!}">
				    ${indexNumber+1}
				    </td>
				    <td class="td-subjectName">${dto.courseName!}</td>
				    <td>
				    	<div class="form-number  form-number-sm period-number" data-step="1">
				    		<input type="text" name="dtoList[${indexNumber}].courseWorkDay" id="" class="form-control courseWorkDay"  value="${dto.courseWorkDay?default(0)}" onblur="changeInputNum(this)"/>
				    		<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
					    	<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
				    	</div>
				    </td>
				    <td>
				    	<#if dto.subjectType! == 'O'>
				    	<select name="dtoList[${indexNumber}].firstsdWeekSubjectId" id="" class="form-control firstsdWeekSubjectId" <#if (dto.courseWorkDay?default(0)) == 0>disabled='disabled'</#if> onChange='changeFirstsdWeek(this)'>
				    		<option value="">无</option>
				    		<#list dtoList as itemDto>
					    		<#if itemDto.subjectId != dto.subjectId  && itemDto.subjectType == 'O'>
								<option value="${(itemDto.subjectId + '-' + itemDto.subjectType)!}" <#if dto.firstsdWeekSubjectId?default('')==(itemDto.subjectId + '-' + itemDto.subjectType)>selected="selected"</#if> >${itemDto.courseName!}</option>
								</#if>
							</#list>
						</select>
						<#else>
				    	<select disabled name="dtoList[${indexNumber}].firstsdWeekSubjectId" id="" class="form-control firstsdWeekSubjectId" <#if (dto.courseWorkDay?default(0)) == 0>disabled='disabled'</#if> onChange='changeFirstsdWeek(this)'>
				    		<option value="">无</option>
						</select>
			    		</#if>
				    </td>
				    <td>
				    	<div class="form-number form-number-sm courseCoupleTimes-number" data-step="1" >
				    		<#if (dto.courseWorkDay?default(0)) gt 1>
				    			<input type="text" name="dtoList[${indexNumber}].courseCoupleTimes" id="" class="form-control courseCoupleTimes" value="${dto.courseCoupleTimes?default(0)}" onblur="changeInputNum(this)"/>
					    		<button class="btn btn-sm btn-default btn-block form-number-add"><i class="fa fa-angle-up"></i></button>
					    		<button class="btn btn-sm btn-default btn-block form-number-sub"><i class="fa fa-angle-down"></i></button>
				    		<#else>
				    			<input type="text" name="dtoList[${indexNumber}].courseCoupleTimes" id="" class="form-control courseCoupleTimes" disabled value="0" onblur="changeInputNum(this)"/>
				    			<button class="btn btn-sm btn-default btn-block form-number-add" disabled><i class="fa fa-angle-up"></i></button>
					    		<button class="btn btn-sm btn-default btn-block form-number-sub" disabled><i class="fa fa-angle-down"></i></button>
				    		</#if>
				    	</div>
				    </td>
				    <td class="ellipsis courseCoupleTimes-times">
				    	<input type="hidden" name="dtoList[${indexNumber}].courseCoupleTypeTimes" class="js-choosetime courseCoupleTypeTimes" value="${dto.courseCoupleTypeTimes?default('')}">
						<p><label><input type="radio" name="dtoList[${indexNumber}].courseCoupleType" value="0"  class="wp js-timeAreaNo" <#if dto.courseCoupleType?default('0')=='0'>checked="checked" </#if> <#if (dto.courseCoupleTimes?default(0)) lt 1 || (dto.courseWorkDay?default(0)) lt 2>disabled </#if> ><span class="lbl"> 无限制</span></label></p>
						<p><label><input type="radio" name="dtoList[${indexNumber}].courseCoupleType" value="1" class="wp js-timeArea" <#if dto.courseCoupleType?default('0')=='1'>checked="checked"</#if> <#if (dto.courseCoupleTimes?default(0)) lt 1 || (dto.courseWorkDay?default(0)) lt 2 >disabled </#if> ><span class="lbl"> 指定时间范围</span></label></p>
					</td>
				    <td>
				    	<div class="clearfix js-hover">
				    		<span class="pull-left js-hover-num noContinueNum  color-blue">${dto.noContinueNum?default(0)}</span>
					    	<span class="pull-right js-hover-opt" style="display: block;">
					    		<a href="javascript:void(0)" data-toggle="tooltip" data-placement="top" title="" data-original-title="编辑" class="js-separate"><i class="fa fa-edit color-blue"></i></a>
					    	</span>
				    	</div>
				    </td>
				    <td>
				    	<select name="dtoList[${indexNumber}].arrangeDay" class="arrangeDay form-control" style="min-width:90px;" onChange='changeFunctionOpo(this)'>
				    	<#if ksfpList?exists && ksfpList?size gt 0>
				    		<#list ksfpList as ksfp>
								<option value="${ksfp[0]!}" <#if dto.arrangeDay?default('01')==ksfp[0]>selected="selected"</#if>>${ksfp[1]!}</option>
							</#list>
						<#else>
							<option value="01">不限</option>
						</#if>
						</select>
				    </td>
				    <td>
				    	<select name="dtoList[${indexNumber}].arrangeHalfDay"  class="arrangeHalfDay form-control" style="min-width:90px;" onChange='changeFunctionOpo(this)'>
						<#if btksfpList?exists && btksfpList?size gt 0>
				    		<#list btksfpList as ksfp>
								<option value="${ksfp[0]!}" <#if dto.arrangeHalfDay?default('01')==ksfp[0]>selected="selected"</#if> >${ksfp[1]!}</option>
							</#list>
						<#else>
							<option value="01">不限</option>
						</#if>
						</select>
				    </td>
				    <td>
				    	<div class="clearfix js-hover">
				    		<input type="hidden" class="js-choosetime noArrangeTime" name="dtoList[${indexNumber}].noArrangeTime" value="${dto.noArrangeTime?default('')}" >
				    		<span class="pull-left js-hover-num color-blue">${dto.noArrangeTimeNum?default(0)}</span>
					    	<span class="pull-right js-hover-opt" style="display: block;">
					    		<a href="javascript:void(0)" class="js-changeTime" data-toggle="tooltip" data-placement="top" title="" data-original-title="编辑" onclick="changeLimitTime(this)"><i class="fa fa-edit color-blue"></i></a>
					    	</span>
				    	</div>
				    </td>
				    <td>
				    	<select name="dtoList[${indexNumber}].arrangePrior" id="" class="form-control arrangePrior" style="min-width:90px;" onChange='changeFunctionOpo(this)'>
							<option value="0" <#if dto.arrangePrior?default('0')=='0'>selected="selected"</#if>>默认</option>
							<option value="1" <#if dto.arrangePrior?default('0')=='1'>selected="selected"</#if>>高</option>
						</select>
				    </td>
				    <td>
				    	<label>
							<input type="checkbox" name="dtoList[${indexNumber}].needRoom" value="1" class="wp wp-switch" <#if (dto.needRoom!1) == 1>checked</#if> onclick="changeFunctionOpo(this)">
							<span class="lbl"></span>
						</label>
				    </td>
				    <td class="ellipsis">
				    	<a class="color-blue" href="javascript:void(0)" onclick="copySubject(this)">复制到</a>
				    	<#if (dto.deletedStr!0)==0>
				    	<a class="color-blue" href="javascript:void(0)" onclick="deleteSubject(this)">删除</a>
				    	</#if>
				    </td>
				</tr>
				</#list>
				</#if>
			</tbody>
		</table>
	</div>
</div>
</form>
<#-- 选学考科目 -->
<div class="layer layer-courseAdd">
	<div class="layer-content courseAdd-div">
		<div style="max-height:350px;overflow-y:scroll;">
		<table class="" width="100%">
			<tr>
				<td class="text-right" valign="top"></td>
				<td class="course-O">
					<#if xzbCourseList?exists && xzbCourseList?size gt 0>
						<#list xzbCourseList as course>
						<label class="mb10 mr10 w155 ellipsis">
						<input type="checkbox" class="wp" value="${course.id!}-O" onclick="checkChange(this)">
						<span class="lbl"> ${course.subjectName!}</span>
						</label>
						</#list>
					</#if>
				</td>
			</tr>
		</table>
		</div>
	</div>
</div>
<#---时间---->
<div class="layer layer-timeArea">
	<div class="layer-content timeArea-div">
		<p class="courseScheduleTime-explain" style="display:none;"><strong class="subjectName">科目：</strong><span>（请选择连续课次）</span></p>
		<table class="table table-bordered table-striped text-center table-schedule table-schedule-sm" style="margin-bottom: -20px;">
			 <#assign weekDays = (weekDays!7) - 1>
			<thead>
				<tr>
				<th width="30" class="text-center"></th>
				<th width="30" class="text-center"></th>
				<#list 0..weekDays as day>
	           		 	<th class="text-center">${dayOfWeekMap[day+""]!}</th>
	            		</#list>
				</tr>
			</thead>
			<tbody>						
				<#list piMap?keys as piFlag>
				    <#if piMap[piFlag]?? && piMap[piFlag] gt 0>
				    <#assign interval = piMap[piFlag]>
				    <#assign intervalName = intervalNameMap[piFlag]>
				    <#list 1..interval as pIndex>
				    <tr>
				    <#if pIndex == 1>
				    	<td class="text-center" rowspan="${interval}">${intervalName!}<input type="hidden" disabled name="period_interval" value="${piFlag}" disable/></td>
				    </#if>
			        	<td class="text-center">${pIndex!}</td>
						<#list 0..weekDays as day>
			            <#assign tc = day+"_"+piFlag+"_"+pIndex>
						<td class="text-center edited <#if noClickTimeMap?exists && noClickTimeMap[tc!]?exists> disabled</#if>" data-value="${tc!}" ></td>
						</#list>
				    </tr>
				    
				    </#list>
				    
				    <#if piFlag == "2" && piMap["3"]?? && piMap["3"] gt 0>
						<tr>
							<td class="text-center" colspan="${weekDays + 3}">午休</td>
						</tr>
					</#if>
				    
				    </#if>
			    </#list>	
			</tbody>
		</table>
	</div>
</div>
<#--不连排设置--->
<div class="layer layer-separate">
	<div class="layer-content">
		<p><strong class="subjectName">科目：</strong></p>
		<input type="hidden" class="subjectIdType" value=""/>
		<div style="max-height:350px;overflow-y:scroll;">
		<table class="table table-bordered table-striped table-hover" style="margin-bottom:0px;">
			<thead>
				<tr>
					<th width="40%">与其不连排科目</th>
					<th width="40%">不连排方式</th>
					<th width="20%">操作</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="3" class="text-center">
						<a class="color-blue js-separate-add" href="javascript:void(0)">+ 新增</a>
					</td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>
</div>
<#--复制参数--->
<div class="layer layer-copyCourseParm">
	<div class="layer-content copyCourseParm-div">
	
		<table width="100%">
			<tr>
				<td class="text-right" width="70"><p>当前科目：</p></td>
				<td><p class="nowSubjectName">语文</p></td>
			</tr>
			<tr>
				<td class="text-right" valign="top"><p>禁排参数：</p></td>
				<td>
					<table width="100%" class="copyContentOpto">
						<tr>
							<td width="33%">
								<label class="mb10 mr10">
									<input name="noTimeType" type="radio" checked="checked" class="wp" value="1" >
									<span class="lbl">追加 </span>
								</label>
							</td>
							<td width="33%">
								<label class="mb10 mr10">
									<input name="noTimeType" type="radio" class="wp" value="2" >
									<span class="lbl">覆盖 </span>
								</label>
							</td>
							<td width="33%">
								<label class="mb10 mr10">
									<span class="lbl"></span>
								</label>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="text-right" valign="top"><p>复制内容：</p></td>
				<td>
					<table width="100%" class="copyContentOpto">
						<tr>
							<td width="33%">
								<label class="mb10 mr10">
									<input type="checkbox" class="wp" value="1" onclick="checkChange(this)">
									<span class="lbl">课时设置 </span>
								</label>
							</td>
							<td width="33%">
								<label class="mb10 mr10">
									<input type="checkbox" class="wp" value="2" onclick="checkChange(this)">
									<span class="lbl">连堂设置 </span>
								</label>
							</td>
							<td width="33%">
								<label class="mb10 mr10">
									<input type="checkbox" class="wp" value="3" onclick="checkChange(this)">
									<span class="lbl">不连排科目 </span>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<label class="mb10 mr10">
									<input type="checkbox" class="wp" value="4" onclick="checkChange(this)">
									<span class="lbl">课时分配 </span>
								</label>	
							</td>
							<td>
								<label class="mb10 mr10">
									<input type="checkbox" class="wp" value="5" onclick="checkChange(this)">
									<span class="lbl">半天内课时分配 </span>
								</label>
							</td>
							<td>
								<label class="mb10 mr10">
									<input type="checkbox" class="wp" value="6" onclick="checkChange(this)">
									<span class="lbl">禁排时间 </span>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<label class="mb10 mr10">
									<input type="checkbox" class="wp" value="7" onclick="checkChange(this)">
									<span class="lbl">优先级 </span>
								</label>
							</td>
							<td></td>
							<td></td>
						</tr>
					</table>											
				</td>
			</tr>
			<tr>
				<td class="text-right" valign="top"><p>复制到：</p></td>
				<td class="copyCourse">
					<#if dtoList?exists && dtoList?size gt 0>
						<#list dtoList as itemDto>
							<label class="mb10 mr10 w155 ellipsis"><input type="checkbox" class="wp" value="${(itemDto.subjectId + '-' + itemDto.subjectType)!}" onclick="checkChange(this)"><span class="lbl"> ${itemDto.courseName!}</span></label>
						</#list>
					</#if>
				</td>
			</tr>
		</table>
	</div>
</div>
<div class="navbar-fixed-bottom opt-bottom">
    <a href="javascript:void(0)" class="btn btn-blue" onclick="saveMySubjectTime()">保存</a>
</div>
<div class="layer layer-settingtip continue-tip">
	<div class="layer-cotnent">
		<p>
			<strong>连堂设置：</strong>
			周课时大于2时才能设置连堂次数，连堂次数大于1时才能设置连堂时间范围
		</p>
	</div>
</div>


<#--课程周课时设置--->
<div class="layer layer-periodClassSet">
	<div class="layer-content">
		<form id="periodClassSet">
            <p><strong class="subjectName">科目：</strong></p>
            <input type="hidden" class="subjectIdType" value=""/>
            <div style="max-height:350px;overflow-y:scroll;">
                <table class="table table-bordered table-striped table-hover" style="margin-bottom:0px;">
                    <thead>
                        <tr>
                            <th width="40%">班级</th>
                            <th width="40%">周课时</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
		</form>
	</div>
</div>
<script src="${request.contextPath}/static/newgkelective/courseTime.js"></script>
<script>
	var opoNum=0;//操作次数
	var isZugd = false;
	var indexNum=0;
	var dtoAllMap={};
	<#if dtoList?exists && dtoList?size gt 0>
		indexNum=${dtoList?size};
		<#list dtoList as  itemDto >
			dtoAllMap['${(itemDto.subjectId + '-' + itemDto.subjectType)!}']='${itemDto.courseName!}';
		</#list>
	</#if>
	var gkCourse={};
	<#if gkCourseList?exists && gkCourseList?size gt 0>
		<#list gkCourseList as course>
			gkCourse['${course.id!}']='${course.id!}';
		</#list>
	</#if>
	<#---不连排科目--  subjectId1_subjectId2 type  -->
	var noContinueSubject={};
	<#if subjectNoContinueList ?exists && subjectNoContinueList?size gt 0>
		<#list subjectNoContinueList as item>
			noContinueSubject['${item[0]!}']='${item[1]!}';
		</#list>
	</#if>
	
	<#--不连排方式微代码--->
	var noContinueList=new Array();	
	<#if blpfsList?exists && blpfsList?size gt 0>
		<#list blpfsList as blpfs>
			noContinueList[${blpfs_index}]=new Array('${blpfs[0]!}','${blpfs[1]!}');	
		</#list>
	<#else>
		noContinueList[0]=new Array('01','无');	
	</#if>
	<#--半天课时分配微代码--->
	var arrangeHalfList=new Array();	
	<#if btksfpList?exists && btksfpList?size gt 0>
		<#list btksfpList as ksfp>
			arrangeHalfList[${ksfp_index}]=new Array('${ksfp[0]!}','${ksfp[1]!}','0'); 
		</#list>
	<#else>
		arrangeHalfList[0]=new Array('01','不限','0');	
	</#if>
	<#--课时分配微代码--->
	var arrangeDayList=new Array();	
	<#if ksfpList?exists && ksfpList?size gt 0>
		<#list ksfpList as ksfp>
			arrangeDayList[${ksfp_index}]=new Array('${ksfp[0]!}','${ksfp[1]!}','0'); 
		</#list>
	<#else>
		arrangeDayList[0]=new Array('01','不限','0');
	</#if>
	
	<#--上午大课间-->
	var abnum=0;
	<#if ab?exists>
		abnum=${ab};
	</#if>
	<#--下午大课间-->
	var pbnum=0;
	<#if pb?exists>
		pbnum=${pb};
	</#if>
	
	function canLoadOther(){
		if(opoNum>0){
			var con;
			con=confirm("是否确定页面数据已经保存？"); //在页面上弹出对话框
			if(con==true){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	function backClick(){
		$(".li_class").removeClass("active");
		$("#li02").addClass("active");
	}
	$(function(){
		//提示
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});
		// 新增科目
		
		$('.js-courseAdd').on('click', function(){
			//已存在科目
			var nowSubjectMap={};
			$("#subjectTable tbody").find("tr").each(function(){
				var keyId=$(this).find("input[name='subjectIdType']").val();
				nowSubjectMap[keyId]=keyId;
			})
			$(".layer-courseAdd").find("input[type='checkbox']").each(function(){
				var vv=$(this).val();
				var arr=vv.split("-");
				
				if(nowSubjectMap[vv]){
					$(this).attr("checked",true);
					$(this).attr("disabled",true);
				}else{
					$(this).attr("checked",false);
					$(this).attr("disabled",false);
					
					if(arr[1]=='O'){
						var key1=arr[0]+"-A";
						var key2=arr[0]+"-B";
						if(nowSubjectMap[key1] || nowSubjectMap[key2]){
							$(this).attr("disabled",true);
						}
					}else{
						var key1=arr[0]+"-O";
						if(nowSubjectMap[key1]){
							$(this).attr("disabled",true);
						}
					}
				}
				
				
			})
			
			layer.open({
				type: 1,
				shadow: 0.5,
				title: '新增科目',
				area: ['600px','500px'],
				btn: ['确定', '取消'],
				scrollbar:false,
				btn1:function(){
					
					var newAddMap={};
					var num=0;
 					$(".layer-courseAdd").find("input[type='checkbox']").each(function(){
 						var vv=$(this).val();
						if($(this).attr("disabled")){
							
						}else{
							if($(this).attr("checked")){
								newAddMap[vv]=$(this).parent().find("span").html();
								num=num+1;
							}
						}
					})
					if(num>0){
						opoNum++;
					
						//判断科目属于A也属于O
						var f=false;
						for(var key in newAddMap){
							var arr1=key.split("-");
							if(gkCourse[arr1[0]]){
								var k1=arr1[0]+"-A";
								var k2=arr1[0]+"-B";
								var k3=arr1[0]+"-O";
								if(newAddMap[k3] && (newAddMap[k1] || newAddMap[k2])){
									f=true;
									layer.msg('科目：'+newAddMap[key]+'既要开设行政班科目，又要开设选学科目，不符合实际！', {
										icon: 2,
										time: 1500,
										shade: 0.2
									});
									break;
								}
							}
							if(arr1[1]=='A'){
								newAddMap[key]=newAddMap[key]+"选";
							}else if(arr1[1]=='B'){
								newAddMap[key]=newAddMap[key]+"学";
							}
						}
						if(f){
							
						}else{
							//增加tr科目
							var alltrNum=$("#subjectTable tbody").find("tr").length+1;
							for(var keykey in newAddMap){
								var $hh=addSubjectTr(alltrNum,keykey,newAddMap[keykey]);
								$("#subjectTable tbody").append($hh);
								alltrNum++;
							}
							
							
							//每个单科下拉增加科目
							addSomeSubjectInOther(newAddMap);

							for(var keykey in newAddMap){
								dtoAllMap[keykey]=newAddMap[keykey];
							}
							$('[data-toggle="tooltip"]').tooltip({
								container: 'body',
								trigger: 'hover'
							});
							layer.closeAll();
						}
					}else{
						layer.closeAll();
					}
				},
				btn2:function(){
					layer.closeAll();
				},
				content: $('.layer-courseAdd')
			});
		});
		
		//数字增加以及减少
		$("#subjectTable").on('click','.form-number > button',function(e){
			e.preventDefault();
			var $num = $(this).siblings('.form-control');
			var val = $num.val();
			if (!val ) val = 0;
			var num = parseInt(val);
			var step = $num.parent('.form-number').attr('data-step');
			if (step === undefined) {
				step = 1;
			} else{
				step = parseInt(step);
			}
			if ($(this).hasClass('form-number-add')) {
				num += step;
			} else{
				num -= step;
				if (num <= 0) num = 0;
			}
			
			opoNum++;
			
			$num.val(num);
			
			changeIsDisabled($(this),num);
			countTotal();
		});
		
		//时间编辑
		<#--
		$("#subjectTable").on('mouseenter','.js-hover',function(e){
			$(this).children('.js-hover-opt').show();
		});
		$("#subjectTable").on('mouseleave','.js-hover',function(e){
			$(this).children('.js-hover-opt').hide();
		});
		-->
		
		
		//时间点击js
		$(".edited").click(function(e){
			
			if(!$(this).hasClass("disabled")){
				if($(this).hasClass("active")){
					$(this).removeClass("active");
				}else{
					$(this).addClass("active");
				}
			}else{
				if($(this).hasClass("active")){
					$(this).removeClass("active");
				}
			}
		});
		
		//指定时间
		$("#subjectTable").on('click','.js-timeArea', function(){
			opoNum++;
			arrangeTime($(this));
		});
		$("#subjectTable").on('click','.js-timeAreaNo', function(){
			opoNum++;
		});
		// 不连排科目
		$("#subjectTable").on('click', '.js-separate',function(){
			var chooseObj=$(this);
			var keyId=$(this).parents("tr").find("input[name='subjectIdType']").val();
			var subjectName=$(this).parents("tr").find(".td-subjectName").html().trim();
			$('.layer-separate').find(".subjectName").html('科目：'+subjectName);
			$('.layer-separate').find(".subjectIdType").val(keyId);
			
			//删除已有数据
			$('.layer-separate').find('tbody').find('tr:not(:last)').remove();
			
			var m=0;
			var addTrhtml="";	
			for(var ccKey in noContinueSubject){
				if(noContinueSubject[ccKey]!=""){
					if(ccKey.indexOf(keyId)>-1){
						var arr=ccKey.split('_');
						var cId="";
						var cType=noContinueSubject[ccKey];
						if(arr[0]==keyId){
							cId=arr[1];
						}else{
							cId=arr[0];
						}
						addTrhtml=addTrhtml+addSeparateTr(keyId,cId,cType);
					}
				}
				
			}
			if(addTrhtml!=""){
				$(".js-separate-add").parents('tr').before(addTrhtml);
			}
			
			
			layer.open({
				type: 1,
				shadow: 0.5,
				title: '不连排科目',
				area: '520px',
				btn: ['确定', '取消'],
				scrollbar:false,
				btn1:function(){
					opoNum++;
					//先判断是不是选择同样的科目
					var trnum=$(".layer-separate").find("tbody").find("tr").length;
					var keySubjectIdType=$(".layer-separate").find(".subjectIdType").val();
					if(trnum>1){
						var chooseNoContinue={};
						var chooseSubjectNum=0;
						chooseSubjectNum=trnum-1;
						var f=false;
						$(".layer-separate").find("tbody").find("tr:not(:last)").each(function(){
							var chooseIdsubject=$(this).find(".separateSubject").val();
							var chooseIdType=$(this).find(".separateType").val();
							if(chooseNoContinue[chooseIdsubject]){
								f=true;
								return false;
							}else{
								chooseNoContinue[chooseIdsubject]=chooseIdType;
							}
						})
						if(f){
							layer.msg('与其不连排科目中，选择了同样的科目！', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
						}else{
							var chooseInMap={};
							var i=0;
							for(var ccKey in noContinueSubject){
								if(ccKey.indexOf(keySubjectIdType)>-1){
									chooseInMap[ccKey]=noContinueSubject[ccKey];
									//用于删除的值
									noContinueSubject[ccKey]="";
									i=i+1;
								}
							}
							
							if(i>0){
								//取出对应参数
								for(var ccKey2 in chooseNoContinue){
									for(var ccKey in chooseInMap){
										if(ccKey.indexOf(ccKey2)>-1){
											noContinueSubject[ccKey]=chooseNoContinue[ccKey2];
											break;
										}else{
											var mm=keySubjectIdType+"_"+ccKey2;
											noContinueSubject[mm]=chooseNoContinue[ccKey2];
											break;
										}
									}
								}
								
							}else{
								for(var ccKey2 in chooseNoContinue){
									var mm=keySubjectIdType+"_"+ccKey2;
									noContinueSubject[mm]=chooseNoContinue[ccKey2];
								}
							}
							$(chooseObj).parent().parent().find(".js-hover-num").html(chooseSubjectNum);
							makeNoContinueNum();
							layer.closeAll();
						}
					}else{
						//清空与这个科目有关的值
						//先删除原有的旧数据
						for(var ccKey in noContinueSubject){
							if(noContinueSubject[ccKey]!="" && noContinueSubject[ccKey]!=null){
								if(ccKey.indexOf(keySubjectIdType)>-1){
									noContinueSubject[ccKey]=null;
								}
							}
						}
						makeNoContinueNum();
						layer.closeAll();
					}

				},
				btn2:function(){
					layer.closeAll();
				},
				content: $('.layer-separate')
			});
		});
		
		//不连排 删除
		$(".layer-separate").on('click','.js-separate-delete',function(){
			$(this).parents("tr").remove();
		})
		
		//不连排 增加
		$(".js-separate-add").on('click',function(){
			var kkId=$('.layer-separate').find(".subjectIdType").val();
			$(this).parents("tr").before(addSeparateTr(kkId,'',''));
		})
		
		$('.js-continue-tip').on('click', function(e){
			e.preventDefault();
			layer.open({
				type: 1,
				shadow: 0.5,
				title: '连堂设置说明',
				area: '520px',
				btn:'知道了',
				content: $('.continue-tip')
			})
		});
	})
	
	
	
	function addSeparateTr(keySubjectIdType,chooseSubjectIdType,chooseType){
		var courseList=new Array();
		var i=0;
		for(var gtoKey in dtoAllMap){
			if(dtoAllMap[gtoKey]!=null){
				if(gtoKey!=keySubjectIdType){
					if(chooseSubjectIdType==gtoKey){
						courseList[i]=new Array(gtoKey,dtoAllMap[gtoKey],'1');
					}else{
						courseList[i]=new Array(gtoKey,dtoAllMap[gtoKey],'0');
					}
					
					i++;
				}
			}
			
			
		}
		var courseSelectHtml=addSelectHtml(courseList,"","","separateSubject",false);
		
		var typeList=new Array();
		for(var j=0;j<noContinueList.length;j++){
			if(chooseType==noContinueList[j][0]){
				typeList[j]=new Array(noContinueList[j][0],noContinueList[j][1],'1');
			}else{
				typeList[j]=new Array(noContinueList[j][0],noContinueList[j][1],'0');
			}
			
		}
		
		var typeSelectHtml=addSelectHtml(typeList,"","","separateType",false);	
			
		var $html='<tr><td>'+courseSelectHtml+'</td><td>'+typeSelectHtml+'</td>'
			+'<td><a class="color-blue js-separate-delete" href="javascript:void(0)">删除</a></td>'
			+'</tr>';				
		return 	$html;		
				
	}
	

	
	//修改的input数据
	function changeInputNum(obj){
		opoNum++;
		var val = $(obj).val();
		if (!val ) val = 0;
		var num = parseInt(val);
		changeIsDisabled(obj,num);
	}
	
	
	
	//连堂
	function arrangeTime(obj){
		var chooseValueObj=$(obj).parents("td").find(".js-choosetime");
		var oldTimes=$(chooseValueObj).val();
		var title="指定时间范围";
		showTime(title,oldTimes,chooseValueObj,true);
	}
	//禁排
	function changeLimitTime(obj){
		var chooseValueObj=$(obj).parents("td").find(".js-choosetime");
		var oldTimes=$(chooseValueObj).val();
		var title="禁排时间";
		showTime(title,oldTimes,chooseValueObj,false);
	}
	
	function showTime(title,oldClikeTimes,divKeyObj,isNear){
		if(isNear){
			var subjectName=$(divKeyObj).parents("tr").find(".td-subjectName").html().trim();
			$('.layer-timeArea').find(".subjectName").html('科目：'+subjectName);
			$(".courseScheduleTime-explain").show();
		}else{
			$(".courseScheduleTime-explain").hide();
		}
		$('.layer-timeArea').find(".edited").removeClass("active");
		if(oldClikeTimes!=""){
			$('.layer-timeArea').find(".edited").each(function(){
				var key=$(this).attr("data-value");
				if(oldClikeTimes.indexOf(key)>-1){
					$(this).addClass("active");
				}
			})
		}
		layer.open({
			type: 1,
			shadow: 0.5,
			title: title,
			area: '620px',
			btn: ['确定', '取消'],
			btn1:function(){
				opoNum++;
				var chooseTimes="";
				var arr=new Array();
				var i=0;
				$('.layer-timeArea').find(".edited").each(function(){
					if($(this).hasClass("active")){
						chooseTimes=chooseTimes+","+$(this).attr("data-value");
						arr[i]=$(this).attr("data-value");
						i++;
					}
				})
				if(chooseTimes!=""){
					chooseTimes=chooseTimes.substring(1);
					if(isNear){
						var flag=true;
						for(var j=0;j<arr.length;j++){
							var temp=arr[j];
							var rr=temp.split("_");
							var pp=parseInt(rr[2]);
							var beforeStr=rr[0]+"_"+rr[1]+"_"+(pp-1);
							var afterStr=rr[0]+"_"+rr[1]+"_"+(pp+1);
							var chooseStr=new Array();
							if(abnum>0 && rr[1]=='2'){
								//如果是上午
								if(pp==abnum){
									//只能考虑 beforeStr
									chooseStr[0]=beforeStr;
								}else if(pp== 1 +abnum){
									//只能考虑 afterStr
									chooseStr[0]=afterStr;
								}else{
									//考虑 beforeStr,afterStr
									chooseStr[0]=beforeStr;
									chooseStr[1]=afterStr;
								}
								
							}else if(pbnum>0 && rr[1]=='3'){
								if(pp==pbnum){
									//只能考虑 beforeStr
									chooseStr[0]=beforeStr;
								}else if(pp== 1+pbnum){
									//只能考虑 afterStr
									chooseStr[0]=afterStr;
								}else{
									//考虑 beforeStr,afterStr
									chooseStr[0]=beforeStr;
									chooseStr[1]=afterStr;
								}
							}else{
								//考虑 beforeStr,afterStr
								chooseStr[0]=beforeStr;
								chooseStr[1]=afterStr;
							}
							if(chooseStr.length==1){
								if(chooseTimes.indexOf(chooseStr[0])>-1){
									
								}else{
									flag=false;
									break;
								}
							}else{
								if(chooseTimes.indexOf(chooseStr[0])>-1 || chooseTimes.indexOf(chooseStr[1])>-1){
									
								}else{
									flag=false;
									break;
								}
							}
						}
						if(flag){
							$(divKeyObj).val(chooseTimes);
							layer.closeAll();
						}else{
							layer.msg('时间范围需是选择连续的节次！', {
								icon: 2,
								time: 1500,
								shade: 0.2
							});
						}
					}else{
						$(divKeyObj).parents("td").find(".js-hover-num").html(arr.length);
						$(divKeyObj).val(chooseTimes);
						layer.closeAll();
					}
					
				}else{
					if(!isNear){
						$(divKeyObj).parents("td").find(".js-hover-num").html(0);
					}
					$(divKeyObj).val(chooseTimes);
					layer.closeAll();
				}
				
			},
			btn2:function(){
				layer.closeAll();
			},
			content: $('.layer-timeArea')
		});
	}
	
	//组装新增一行科目
	function addSubjectTr(indexTr,subjectIdType,subjectName){
		var $html= addSubjectRow(indexTr,subjectIdType,subjectName,indexNum);
		indexNum++;
		return $html;
	}
	
	
	
		
	var isSubmit=false;
	function saveMySubjectTime(){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		if(!checkSubjectIdParm()){
			isSubmit=false;
		 	return;
		}
		//组装关联字段
		$("#noContinueSubjectIds").val(makeNoContinueStr());
		var url = '${request.contextPath}/newgkelective/${arrayItemId!}/courseFeatures/courseFeaturesSave';
		var options = {
			url : url,
			dataType : 'json',
			success : function(data){
	 			var jsonO = data;
		 		if(!jsonO.success){
		 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
		 			isSubmit=false;
		 			return;
		 		}else{
		 			layer.closeAll();
		 			layer.msg("保存成功", {
						offset: 't',
						time: 2000
					});
					useMaster = "1";
					toRefesh();
				}
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#mySubjectTime").ajaxSubmit(options);
		
	}	
	var useMaster = "";
	function toRefesh(){
		var url = '${request.contextPath}/newgkelective/xzb/courseFeatures/index?arrayItemId=${arrayItemId!}&useMaster=1';
		$("#gradeTableList").load(url);
		useMaster = "";
	}	  
	
	function changeFunctionOpo(obj){
		opoNum++;
	} 
	

 //删除某一行科目
var isdeleted=false;
function deleteSubject(obj){
	if(isdeleted){
		return ;
	}
	isdeleted=true;
	
	
	layer.confirm('将会直接删除生效,确定删除吗？', {
	  btn: ['确定','取消'] //按钮
	  
	}, function(){
		
	  	var url = '${request.contextPath}/newgkelective/${arrayItemId!}/courseFeatures/deleteSubject';
		var $tr=$(obj).parents("tr");
		var subjectIdType=$tr.find("input[name='subjectIdType']").val();
		var arr=subjectIdType.split("-");
		$.ajax({
            url:url,
            data:{'subjectId':arr[0],'subjectType':arr[1],'arrayId':arrayId},
            type:"post",
            dataType:'json',
            success:function(jsonO){
            	layer.closeAll();
                if (jsonO.success) {
                    deleteOneSubject(subjectIdType);
                    //关联数据
					$tr.remove();
					countTotal();
                }else{
                	layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                }
                isdeleted=false;
            }
         })
	}, function(){
        isdeleted=false;
        layer.closeAll();
	});
	
}

function countTotal(){
	//console.log("refresh total");
	var xzbNum = 0;
	var moveNum = 0;
	$("#subjectTable tbody tr").each(function(){
		var subId =  $(this).find(".subjectId").val();
		var subType =  $(this).find(".subjectType").val();
		var workDay =  parseInt($(this).find(".courseWorkDay").val());
		if(workDay > 0){
			var dsz = $(this).find(".firstsdWeekSubjectId").val();
			if(dsz){
				workDay -=0.5;
			}
			if(subType == "O"){
				xzbNum += workDay;
			}else{
				moveNum += workDay;
			}
		}
	});
	
	
	$("#lecCountXzb").text(xzbNum);
	$("#lecCountMove").text(moveNum);
}

$("#subjectTable").on("change","input.courseWorkDay",function(){
	console.log("input change");
	countTotal();
});
$(function(){
	// 统计总课时数
	countTotal();
});
<#--
	function courseExportTeacher(){
		var url = '${request.contextPath}/newgkelective/exportXzbIndex/main?arrayItemId=${arrayItemId!}';
		$("#gradeTableList").load(url);
	}
-->
	
</script>
