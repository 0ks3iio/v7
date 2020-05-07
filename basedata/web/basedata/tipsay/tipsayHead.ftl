<div class="filter">
	<div class="filter-item">
		<input type="hidden" id="listType" value="${type?default('1')}"/>
	    <#if type?default('1')=='1'>
	    	<a class="btn btn-blue " onClick="addSelfTipsay1()" >申请教务安排</a>
	    	<a class="btn btn-white " onClick="addSelfTipsay2()">自主申请代课</a>
	    <#else>
	    	 <a class="btn btn-blue right js_addTipsay" onClick="addTipsay()">教务安排</a>
	    </#if>
	</div>
	<div class="filter-item filter-item-right">
	   	 <label for="" class="filter-name">周次：</label>
	     <div class="filter-content">
		     <select class="form-control" id="week" onChange="searchTipsay();" style="width:168px;">
		     	 <option value="">全部</option>
			     <#if weekList?exists && (weekList?size>0)>
		              <#list weekList as item>
			               <option value="${item!}" <#if nowWeek==item>selected="selected"</#if> >第${item!}周</option>
		              </#list>
	             </#if>						           
		     </select>
	    </div>
    </div>
    <div class="filter-item filter-item-right">
		<label for="" class="filter-name">学期：</label>
		<div class="filter-content">
			<select class="form-control" id="semester" onChange="searchWeek();" style="width:168px;">
			 ${mcodeSetting.getMcodeSelect('DM-XQ',(semesterObj.semester?default(0))?string,'0')}
			</select>
		</div>
	</div>
	<div class="filter-item filter-item-right">
		<label for="" class="filter-name">学年：</label>
		<div class="filter-content">
			<select class="form-control" id="acadyear" onChange="searchWeek();" style="width:168px;">
			<#if (acadyearList?size>0)>
				<#list acadyearList as item>
				<option value="${item!}" <#if semesterObj.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
				</#list>
			</#if>
			</select>
		</div>
	</div>
</div>

<div class="table-wrapper" id="showTipsayList">
	
</div>
<script>
	$(function(){
		searchTipsay();
	})
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
				}
				$("#week").html(htmlText);
				searchTipsay();
			}
		});		
	}
	function searchTipsay(){
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var week=$('#week').val();
		var type=$('#listType').val();
		var url =  '${request.contextPath}/basedata/tipsay/tipsayList/page?acadyear='+acadyear+'&semester='+semester+'&week='+week+'&type='+type;
		$("#showTipsayList").load(url);
	}
	<#if type?default('1')=='1'>
	function addSelfTipsay1(){
		var url = '${request.contextPath}/basedata/tipsay/applyArrange/index/page';
		$("#showTabList").load(encodeURI(url));
	}
	
	
	function addSelfTipsay2(){
		//全屏展现
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var week= $("#week").val();
		if(acadyear=="" || semester==""){
			return;
		}
		var url =  '${request.contextPath}/basedata/tipsay/addSelfTipsay2/page?acadyear='+acadyear+'&semester='+semester+'&week='+week;
		
		var availHeight = window.screen.availHeight;
 		var availWidth = window.screen.availWidth;
 		
		window.open(url,
			'tipsayArrangAdmin',
			'height='+availHeight+',width='+availWidth+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}
	<#else>
	function addTipsay(){
		var acadyear=$('#acadyear').val();
		var semester=$('#semester').val();
		var week= $("#week").val();
		if(acadyear=="" || semester==""){
			return;
		}
		
		var url =  '${request.contextPath}/basedata/tipsay/addTipsay/page?acadyear='+acadyear+'&semester='+semester+'&week='+week;
		
		var availHeight = window.screen.availHeight;
 		var availWidth = window.screen.availWidth;
 		
		window.open(url,
			'tipsayArrangAdmin',
			'height='+availHeight+',width='+availWidth+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		
		//var url =  '${request.contextPath}/basedata/tipsay/addhead/page?acadyear='+acadyear+'&semester='+semester+'&week='+week;
		//$("#showList").load(url);
	}
	</#if>
	
	
</script>