<div class="filter">
    <div class="filter-item">
        <a type="button" class="btn btn-blue gotoAdjustClass">开始调课</a>
        <#if from != "01">
        <a type="button" class="btn btn-blue gotoAdjustExport">导出调课单</a>
        </#if>
    </div>
    <div class="filter-item" style="display:none">
        <a type="button" class="btn btn-blue gotoBack">返回</a>
        <a type="button" class="btn btn-blue" onclick="exportMore()">批量导出</a>
    </div>
     <#if from != "01">
    <div class="filter-item filter-item-right">
        <span class="filter-name">年级：</span>
        <div class="filter-content">
            <select id="gradeId" class="form-control" onchange="showSwitchDetail()">
            	 <option value="">全部</option>
                <#if gradeList?exists && gradeList?size gt 0>
                    <#list gradeList as grade>
                        <option value="${grade.id}">${grade.gradeName}</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
     </#if>
    <div class="filter-item filter-item-right">
        <span class="filter-name">周次：</span>
        <div class="filter-content">
            <select name="week" id="week" class="form-control" onchange="showSwitchDetail()">
            	 <option value="">全部</option>
                <#if max?exists && max gt 0>
                    <#list 1..max as item>
                        <option value="${item}" <#if item == nowWeek>selected="selected"</#if>>第${item}周</option>
                    </#list>
                </#if>
            </select>
        </div>
    </div>
    <div class="filter-item filter-item-right">
        <span class="filter-name">学期：</span>
        <div class="filter-content">
            <select name="semester" id="semester" class="form-control" onChange="searchWeek();">
                ${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
            </select>
        </div>
    </div>
    <div class="filter-item filter-item-right">
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
</div>
<div style="display: none">
    <input id="teacherId" value="${teacherId}">
    <input id="week_0">周一</input>
    <input id="week_1">周二</input>
    <input id="week_2">周三</input>
    <input id="week_3">周四</input>
    <input id="week_4">周五</input>
</div>
<div id="switchDetail">

</div>

<script>
var isExport=0;
    $(function () {
        showSwitchDetail();

        $(".gotoAdjustClass").on("click", function () {
            <#if from == "01">
                var url = "${request.contextPath}/basedata/classswitch/apply/index/page?schoolId=${schoolId}&teacherId=${teacherId}&nowAcadyear=${semester.acadyear}&nowSemester=${semester.semester}";
	            $("#gradeTableList").load(url);
            <#else>
                modifyArray();
            </#if>
        });
        
        $(".gotoAdjustExport").on("click", function () {
        	$(this).parent('.filter-item').hide().siblings().show();
        	isExport=1;
        	showSwitchDetail();
        });
        
        $(".gotoBack").on("click", function () {
        	$(this).parent('.filter-item').hide().siblings().show();
        	isExport=0;
        	showSwitchDetail();
        });
    });

    function showSwitchDetail() {
        var acadyear = $("#acadyear option:selected").val();
        var semester = $("#semester option:selected").val();
        var gradeId = $("#gradeId").val();
        var week = $("#week option:selected").val();
        <#if from == "01">
            $("#switchDetail").load("${request.contextPath}/basedata/classswitch/list/table?teacherId=${teacherId}&acadyear=" + acadyear + "&semester=" + semester + "&week=" + week);
        <#else>
            $("#switchDetail").load("${request.contextPath}/basedata/classswitch/manage/table?schoolId=${schoolId}&acadyear=" + acadyear + "&semester=" + semester +"&gradeId=" + gradeId + "&week=" + week+"&isExport="+isExport);
        </#if>
    }
    
    function searchWeek(){
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		if(acadyear=="" || semester==""){
			$("#week").html('<option value="">全部</option>');
			return;
		}
		$.ajax({
			url:'${request.contextPath}/basedata/tipsay/findWeekList',
			data:{'acadyear':acadyear,'semester':semester},
			type:'post', 
			dataType:'json',
			success:function(data){
				var htmlText='<option value="">全部</option>';
				if(data!=null && data.length>0){
					var obj = data;
					for(var iii=0;iii<obj.length;iii++){
						htmlText=htmlText+'<option value="'+obj[iii]+'">第'+obj[iii]+'周</option>';
					}
					$("#week").html(htmlText);
				}
				$("#week").html(htmlText);
				showSwitchDetail();
			}
		});		
	}
	
	function modifyArray(){
		//console.log("---i am here----");
		var gradeId = $("#gradeId").val();


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
		param += "&searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&weekIndex="+searchWeek;
		param = param.substr(1);
		var url = "${request.contextPath}/basedata/scheduleModify/"+gradeId+"/index?";
        if(!gradeId){
            gradeId = $("#gradeId option:eq(1)").val();
            if(!gradeId){
                tips('无可操作年级');
                return;
            }
            url = "${request.contextPath}/basedata/scheduleModify/mutil/"+gradeId+"/index?";
        }
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
</script>