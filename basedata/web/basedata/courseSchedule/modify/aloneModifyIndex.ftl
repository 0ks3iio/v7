<div class="box box-default">
<div class="box-body">
<div class="filter" id="aloneModify">
    <div class="filter-item filter-item-left">
        <span class="filter-name">学年：</span>
        <div class="filter-content">
            <select name="acadyear" id="acadyear" class="form-control" onChange="searchWeek();">
                <#if acadyearList?exists && acadyearList?size gt 0>
                    <#list acadyearList as item>
                        <option value="${item}" <#if item == semester.acadyear>selected="selected"</#if>>${item}</option>
                    </#list>
                <#else>
                    <option value="">未设置</option>
                </#if>
            </select>
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <span class="filter-name">学期：</span>
        <div class="filter-content">
            <select name="semester" id="semester" class="form-control" onChange="searchWeek();">
                ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
            </select>
        </div>
    </div>
    
    <div class="filter-item filter-item-left">
        <span class="filter-name">周次：</span>
        <div class="filter-content">
            <select name="week" id="week" class="form-control" onchange="">
                <#if max?exists && max gt 0>
                    <#list 1..max as item>
                        <option value="${item}" <#if item == nowWeek>selected="selected"</#if>>第${item}周</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
    <div class="filter-item filter-item-left">
        <span class="filter-name">年级：</span>
        <div class="filter-content">
            <select id="gradeId" class="form-control" onchange="">
                <#if gradeList?exists && gradeList?size gt 0>
                    <#list gradeList as grade>
                        <option value="${grade.id}">${grade.gradeName}</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
    
    <div class="filter-item">
        <a type="button" class="btn btn-blue gotoAdjustClass" onclick="modifyArray();">开始调课</a>
    </div>
    <div class="filter-item">
        <a type="button" class="btn btn-blue gotoAdjustClass" onclick="backupSchedule();">备份课表</a>
    </div>
    <div class="filter-item">
        <a type="button" class="btn btn-blue gotoAdjustClass" onclick="revertSchedule();">还原课表</a>
    </div>
</div>

<div class="import-explain">
    <p class="mt-20" id="import_panelWindow_tip"></p>
	<p class="mt-20"><b>手动调课说明：</b></p>
    <p>1、调课前请先设置好相应的教学计划 </p>
    <p>2、待安排课程的教师取自班级课程开设 </p>
    <p>3、待安排课程的场地取自年级班级设置中维护的场地</p>
    <p>4、<font color="red">应用本周课程至以后周： 会将指定周的课表复制到以后的周次。如果存在单双周，单双周课程所在时间的课程不会被覆盖</font></p>
    <p>例： 本周是1班第9周，单周，其中有一节课 音乐（单）(周一上午第2节)。</p>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;
    	将本周复制到以后周时，当复制到第11周（单），将会用第九周的课表完全覆盖第11周的课表；<br>
    	&nbsp;&nbsp;&nbsp;&nbsp;
    	当复制到第10周（双）时，则不会将音乐（单）复制到第10周，并且会保留 原来第10周在 周一上午第2节 的课程  </p>
    <p>5、<font color="red">存在单双周的课程，执行 应用至以后周操作后 请检验以后周课程的正确性。</font></p>
    <p>例： 将单周课程（存在音乐单周）应用至以后周之后，查看双周（存在美术双周）课程的正确性。</p>
    <p>&nbsp;&nbsp;&nbsp;&nbsp;
    	由于不会覆盖单双周位置的课程，到双周将美术课移动至和 单音乐课相同的位置，其他行政班课程时间不变，再次执行 应用至以后周的操作，就可以充分利用单双周时间。<br>
    
    <p class="mt-20"><b>备份还原课表说明：</b></p>
    <p>1、备份还原课表仅对指定的年级有效。</p>
    <p>2、建议每次导入课表后再使用备份功能。</p>
    <p>3、还原功能使用前提：请确保原来的班级还在，如果不存在，这部分班级对应的课表也不会还原。</p>
    
</div>

</div>
</div>
<script>
    function searchWeek(){
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		if(acadyear=="" || semester==""){
			$("#week").html('');
			return;
		}
		$.ajax({
			url:'${request.contextPath}/basedata/tipsay/findWeekList',
			data:{'acadyear':acadyear,'semester':semester},
			type:'post', 
			dataType:'json',
			success:function(data){
				var htmlText='';
				if(data!=null && data.length>0){
					var obj = data;
					for(var iii=0;iii<obj.length;iii++){
						htmlText=htmlText+'<option value="'+obj[iii]+'">第'+obj[iii]+'周</option>';
					}
					$("#week").html(htmlText);
				}
				$("#week").html(htmlText);
			}
		});		
	}
	
	function modifyArray(){
		//console.log("---i am here----");
		var gradeId = $("#gradeId").val();
		
		if(!gradeId){
			tips('请先选择要查询年级');
			return;
		}
		var searchWeek = $("#week").val();
		if(!searchWeek){
			tips('无法获取此学期周次信息');
			return;
		}
		
    	modifyArray2(gradeId);
	}
	function modifyArray2(gradeId,cid){
		var searchAcadyear = $("#acadyear").val();
		var searchSemester = $("#semester").val();
		var searchWeek = $("#week").val();
		if(!searchWeek){
			searchWeek = 1;
		}
		  
		var param = "";
		if(cid) param += "&objId="+cid;
		param += "&searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&weekIndex="+searchWeek+"&fromAlone=1";
		param = param.substr(1);
		var url = "${request.contextPath}/basedata/scheduleModify/"+gradeId+"/index?";
		url += param;
		//console.log(param);
		//return;
		var width = screen.availWidth;
		var height = screen.availHeight -60;
		//var height = window.outerHeight;
		
		window.open  
			(url,'newwindow','fullscreen=no, height='+height+',width='+width+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,\
			resizable=no,location=no, status=no');
	}
	function check(){
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		if(acadyear=="" || semester==""){
			tips('请先设置学年学期');
			return false;
		}
		var gradeId = $("#gradeId").val();
		if(!gradeId){
			tips('请选择年级');
			return false;
		}
		var searchWeek = $("#week").val();
		if(!searchWeek){
			tips('无法获取此学期周次信息');
			return false;
		}
		return true;
	}
	function tips(msg){
		layer.msg(msg, { offset: 't',  time: 2000 });
	}
	function backupSchedule(){
		if(!check())
			return;
	
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var gradeId = $("#gradeId").val();
		
		//debugger;
		var str = $('#aloneModify #acadyear option[value="'+acadyear+'"]').text()+$('#aloneModify #semester option[value="'+semester+'"]').text()
			+$('#aloneModify #gradeId option[value="'+gradeId+'"]').text();
		
		layer.confirm('确定备份'+str+'课表吗？', function(index){
			var ii= layer.load();
			$.ajax({
				url:'${request.contextPath}/basedata/scheduleModify/'+gradeId+'/backupSchedule',
				data:{'acadyear':acadyear,'semester':semester},
				type:'post', 
				dataType:'json',
				success:function(data){
					layer.close(ii);
					if(data.success){
						tips('备份成功');
						layer.close(index);
					}else{
						layerTipMsg(data.success,"失败","原因："+data.msg);
					}
				}
			});		
		})
		
	}
	
	
	function revertSchedule(){
		if(!check())
			return;
	
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var gradeId = $("#gradeId").val();
		
		// 先检查 是否存在备份数据
		var f = false;
		$.ajax({
				url:'${request.contextPath}/basedata/scheduleModify/'+gradeId+'/checkBackupExists',
				data:{'acadyear':acadyear,'semester':semester},
				type:'post', 
				async: false,
				dataType:'json',
				success:function(data){
					if(data.success){
						//tips('恢复成功');
						f = true;
					}else{
						layerTipMsg(data.success,"失败","原因："+data.msg);
					}
				}
			});		
			
		if(!f)
			return;
		
		// 还原课表
		var str = $('#aloneModify #acadyear option[value="'+acadyear+'"]').text()+$('#aloneModify #semester option[value="'+semester+'"]').text()
			+$('#aloneModify #gradeId option[value="'+gradeId+'"]').text();
		layer.confirm('确定还原'+str+'课表吗？<br>课表将被还原至上一次备份的状态', function(index){
			var ii= layer.load();
			$.ajax({
				url:'${request.contextPath}/basedata/scheduleModify/'+gradeId+'/revertSchedule',
				data:{'acadyear':acadyear,'semester':semester},
				type:'post', 
				dataType:'json',
				success:function(data){
					layer.close(ii);
				
					if(data.success){
						tips('恢复成功');
					}else{
						layerTipMsg(data.success,"失败","原因："+data.msg);
					}
				}
			});		
		});
		
	}
</script>