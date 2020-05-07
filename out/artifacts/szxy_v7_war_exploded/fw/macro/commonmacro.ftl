<#--FreeMarker页面通用功能宏定义文件，每增加一个宏，请说明功能及参数意义
1、getVlaue 功能：从List列表中的String[key,value]对象中，取key对应的value
2、acadyearlist 功能：把学年列表组yearlist组装成HTML标筌语句 
-->

<#--1
*　功能：从List列表中的String[key,value]对象中，取key对应的value
*　宏名：getVlaue
*　参数：list　List列表，元素是String[]{key,value}对象，
*            key一般表示数据库中存储的值（主键），vlaue一般表示在显示的值
-->
<#macro getValue list key>
	<#if list?exists && key?exists>
		<#list list as item>
			<#if item[0] == key>
				${item[1]?if_exists}
				<#return>
			</#if>
    	</#list>
	</#if>
</#macro>

<#macro select idObjectId nameObjectId url divName idObjectName=idObjectId nameObjectName=nameObjectId selectedValue="" otherParam="" callback="" className="" onclick="" params="" mode="s" defaultItem="Y">
<div class="${className}" id="${idObjectId}Div"></div>
<script>
	//加载条件
	function onLoadSelect(idObjectId,nameObjectId,idObjectName,nameObjectName,url,divName,selectedValue,onclick,callback,mode,defaultItem,otherParam,params){
		var assembledUrl =_contextPath+url+"?idObjectId="+idObjectId+"&nameObjectId="+nameObjectId+"&idObjectName="+idObjectName+"&nameObjectName="+nameObjectName+"&selectedValue="+selectedValue+"&divName="+divName+"&onclick="+onclick+"&callback="+callback+"&mode="+mode+"&defaultItem="+defaultItem+"&params="+params;
		if(otherParam != ""){
 			assembledUrl+="&"+otherParam;
 	} 
		load('#'+idObjectId+'Div',assembledUrl);	
	}
	onLoadSelect('${idObjectId}','${nameObjectId}','${idObjectName}','${nameObjectName}','${url}','${divName}','${selectedValue}','${onclick}','${callback}','${mode}','${defaultItem}','${otherParam}','${params}');
</script>
</#macro>

<#macro couplingSelect idObjectId nameObjectId url divName otherParam="" onclick="" callback="" idObjectName=idObjectId! >
<div class="dt">
    <b>您已选择：</b>
    <div class="tree-menu-selected fn-left"></div>
    <input type="hidden" name="${idObjectName}" id="${idObjectId}">
    <#nested />
    <span class="show-part mt-5">收起</span>
</div>
<div class="tree-menu-box" id="couplingInnerId"></div>
<script>
	//加载条件
	function onLoadInnerSelect(idObjectId,nameObjectId,url,divName,onclick,callback,parentId){
		var _otherParam ='${otherParam}';
		var assembledUrl =_contextPath+url+"?idObjectId="+idObjectId+"&nameObjectId="+nameObjectId+"&divName="+divName+"&onclick="+onclick+"&callback="+callback+"&url="+url+"&parentId="+parentId;
		if(_otherParam != ""){
 			assembledUrl+="&"+_otherParam;
 		} 
		load('#couplingInnerId',assembledUrl);	
	}
	onLoadInnerSelect('${idObjectId}','${nameObjectId}','${url}','${divName}','${onclick}','${callback}','');
</script>
</#macro>

<#macro selectDiv idObjectId nameObjectId url divName otherParam="" callback="" onclick="" selectedValue="" dependson="" referto="" width="150" divWidth="600" defaultItem="N" tipMsg="" readonly="" singleSelect="true" isFirstDiv=true>
<div class="query-tt b <#if dependson =="" && isFirstDiv>ml-10</#if>">${divName!}：</div>

<div id="${idObjectId}SelectDiv" objectId="${idObjectId}" <#if readonly == 'true'>class="ui-select-box-disable <#if singleSelect == 'true'>NB_select_box</#if> fn-left mr-10"<#else>class="select_box <#if singleSelect == 'true'>NB_select_box</#if> fn-left mr-10"</#if> style="width:${width!}px;" >
    <#assign inputWidth = width?number -33 />
    <div>
	    <div id="ovLayer" style="position:absolute;z-index:-1;width:101%;height:101%;margin-left:0px;"><iframe style="width:100%;height:100%;border:0;filter:alpha(opacity=0);-moz-opacity:0"></iframe></div>
	    <input type="text" id="${idObjectId}SelectedName" class="select_current" value="请选择${divName!}" style="width:${inputWidth!}px;" readonly="readonly"/>
	    <ul id="${idObjectId}Option" class="select_list" <#if singleSelect == 'false'>style="width:${divWidth!}px;"<#else>style="min-width:149px;"</#if>></ul>
		<input id="${idObjectId!}Id" type="hidden" value="${idObjectId!}"/>
		<input id="${idObjectId!}Name" type="hidden" value="${nameObjectId!}"/>
		<input id="${idObjectId!}Url" type="hidden" value="${url!}"/>
		<input id="${idObjectId!}DivName" type="hidden" value="${divName!}"/>
		<input id="${idObjectId!}OtherParam" type="hidden" value="${otherParam!}"/>
		<input id="${idObjectId!}Callback" type="hidden" value="${callback!}"/>
		<input id="${idObjectId!}Onclick" type="hidden" value="${onclick!}"/>
		<input id="${idObjectId!}Dependson" type="hidden" value="${dependson!}"/>
		<input id="${idObjectId!}DependState" type="hidden" value="0"/>
		<input id="${idObjectId!}Referto" type="hidden" value="${referto!}"/>
		<input id="${idObjectId!}DefaultItem" type="hidden" value="${defaultItem!}"/>
		<input id="${idObjectId!}SelectedValue" type="hidden" value="${selectedValue!}"/>
		<input id="${idObjectId!}TipMsg" type="hidden" value="${tipMsg!}"/>
		<input id="${idObjectId!}singleSelect" type="hidden" value="${singleSelect!}"/>
		<input type="hidden" name="${idObjectId!}" id="${idObjectId!}" value="${selectedValue!}"/>
	</div>
</div>
<script>
	//加载条件
	function dynamicLoadDivData(id){
		onLoadSelectDiv($('#'+id+'Id').val(),$('#'+id+'Name').val(),$('#'+id+'Url').val(),$('#'+id+'DivName').val(),$('#'+id+'Onclick').val(),$('#'+id+'Callback').val(),$('#'+id+'SelectedValue').val(),$('#'+id+'OtherParam').val(),$('#'+id+'Dependson').val(),$('#'+id+'Referto').val(),$('#'+id+'DefaultItem').val(),$('#'+id+'TipMsg').val());
	}
	
	function onLoadSelectDiv(idObjectId,nameObjectId,url,divName,onclick,callback,selectedValue,otherParam,dependson,referto,defaultItem,tipMsg){
		var assembledUrl =_contextPath+url+"?idObjectId="+idObjectId+"&nameObjectId="+nameObjectId+"&divName="+divName+"&onclick="+onclick+"&callback="+callback+"&selectedValue="+selectedValue+"&dependson="+dependson+"&referto="+referto+"&defaultItem="+defaultItem+"&tipMsg="+tipMsg;
		var assembledParam ="";
		//依赖的元素
		if(dependson != ""){
			var dependsonElements=dependson.split(",");
			for (i=0;i<dependsonElements.length;i++){
				if($('#'+dependsonElements[i]).val()){
					if( i == dependsonElements.length-1)
						assembledParam +=dependsonElements[i]+"="+$('#'+dependsonElements[i]).val()+"";
					else
						assembledParam +=dependsonElements[i]+"="+$('#'+dependsonElements[i]).val()+"&";
				}
			}
		}
		if(otherParam != ""){
 			assembledUrl+="&"+otherParam;
 		} 
 		if(assembledParam != ""){
 			assembledUrl+="&"+assembledParam;
 		}
 		var singleSelect=$('#'+idObjectId+'singleSelect').val();
 		if(singleSelect =="true")
			load('#'+idObjectId+'Option',assembledUrl,"","",true);	
		else
			load('#'+idObjectId+'Option',assembledUrl);	
	}
	onLoadSelectDiv('${idObjectId!}','${nameObjectId!}','${url!}','${divName!}','${onclick!}','${callback!}','${selectedValue!}','${otherParam!}','${dependson!}','${referto!}','${defaultItem!}','${tipMsg!}');
</script>
</#macro>

<#macro fuzzySelectDiv idObjectId nameObjectId url divName otherParam="" callback="" onclick="" selectedValue="" dependson="" referto="" width="150" divWidth="600" defaultItem="N" tipMsg="" singleSelect="true" isFirstDiv=true>
<div class="query-tt b <#if dependson == "" && isFirstDiv>ml-10</#if>">${divName!}：</div>
<div id="${idObjectId}FuzzyDiv" objectId="${idObjectId}" class="search-box fn-left ml-5 mr-10" style="z-index:1000">
	<div>
	    <div id="ovLayer" style="position:absolute;z-index:-1;width:101%;height:101%;margin-left:-1px;left:-2px;"><iframe style="width:101%;height:101%;border:0;filter:alpha(opacity=0);-moz-opacity:0"></iframe></div>
		<input type="text" value="请选择${divName!}" class="txt" onmousedown="if(this.value == '请选择${divName!}'){this.value=''}" onBlur="if(this.value == ''){this.value='请选择${divName!}';clearData('${idObjectId}')}" onkeyup="delayLoadFuzzyDivWithData('${idObjectId}',this.value);" id="${idObjectId}SelectedName">
		<span class="icon-search"></span>
			<div id="${idObjectId}Option" class="search-list-wrap" style="z-index:1001"></div>
			<input id="${idObjectId!}Id" type="hidden" value="${idObjectId!}"/>
			<input id="${idObjectId!}Name" type="hidden" value="${nameObjectId!}"/>
			<input id="${idObjectId!}Url" type="hidden" value="${url!}"/>
			<input id="${idObjectId!}DivName" type="hidden" value="${divName!}"/>
			<input id="${idObjectId!}OtherParam" type="hidden" value="${otherParam!}"/>
			<input id="${idObjectId!}Callback" type="hidden" value="${callback!}"/>
			<input id="${idObjectId!}Onclick" type="hidden" value="${onclick!}"/>
			<input id="${idObjectId!}Dependson" type="hidden" value="${dependson!}"/>
			<input id="${idObjectId!}Referto" type="hidden" value="${referto!}"/>
			<input id="${idObjectId!}DefaultItem" type="hidden" value="${defaultItem!}"/>
			<input id="${idObjectId!}SelectedValue" type="hidden" value="${selectedValue!}"/>
			<input id="${idObjectId!}TipMsg" type="hidden" value="${tipMsg!}"/>
			<input id="${idObjectId!}singleSelect" type="hidden" value="${singleSelect!}"/>
			<input type="hidden" name="${idObjectId!}" id="${idObjectId!}" value="${selectedValue!}"/>
	</div>
</div>
<script>
	function clearData(id){
		$('#'+id).val('');
		var referto =$('#'+id+'Referto').val();
		if(referto != ""){
			var refertoElements=referto.split(",");
			for (i=0;i<refertoElements.length;i++){
				if($('#'+refertoElements[i]+'Option')){
					$('#'+refertoElements[i]).val('');
					$('#'+refertoElements[i]+'SelectedName').val('请选择'+$('#'+refertoElements[i]+'DivName').val());
					$('#'+refertoElements[i]+'Option').html('');
				}
			}
		}
	}
	
	var timer; 
	function delayLoadFuzzyDivWithData(id,data){
		if(timer)
			window.clearTimeout(timer); 
		timer=window.setTimeout(function(){loadFuzzyDivWithData(id,data);},500); 
	} 
	
	function loadFuzzyDivWithData(id,data){
		 dynamicLoadFuzzyDivData(id,data);
		 $('#'+id+'FuzzyDiv').find(".search-list-wrap,#ovLayer").fadeIn(function(){
			var myW=$(this).width()+5;
	        var myH=$(this).height()+30;
			$(this).siblings('#ovLayer').css({'width':myW,'height':myH});
		},0);
	    $('#'+id+'FuzzyDiv').find('.search-list-wrap').show();
	}

	//加载条件
	function dynamicLoadFuzzyDivData(id,queryData){
		onLoadSelectFuzzyDiv($('#'+id+'Id').val(),$('#'+id+'Name').val(),$('#'+id+'Url').val(),$('#'+id+'DivName').val(),$('#'+id+'Onclick').val(),$('#'+id+'Callback').val(),$('#'+id+'SelectedValue').val(),$('#'+id+'OtherParam').val(),$('#'+id+'Dependson').val(),$('#'+id+'Referto').val(),$('#'+id+'DefaultItem').val(),$('#'+id+'TipMsg').val(),queryData);
	}
	
	function onLoadSelectFuzzyDiv(idObjectId,nameObjectId,url,divName,onclick,callback,selectedValue,otherParam,dependson,referto,defaultItem,tipMsg,queryData){
		var assembledUrl =_contextPath+url+"?idObjectId="+idObjectId+"&nameObjectId="+nameObjectId+"&divName="+divName+"&onclick="+onclick+"&callback="+callback+"&selectedValue="+selectedValue+"&dependson="+dependson+"&referto="+referto+"&defaultItem="+defaultItem+"&tipMsg="+tipMsg;
		var assembledParam ="";
		//依赖的元素
		if(dependson != ""){
			var dependsonElements=dependson.split(",");
			for (i=0;i<dependsonElements.length;i++){
				if($('#'+dependsonElements[i]).val()){
					if( i == dependsonElements.length-1)
						assembledParam +=dependsonElements[i]+"="+$('#'+dependsonElements[i]).val()+"";
					else
						assembledParam +=dependsonElements[i]+"="+$('#'+dependsonElements[i]).val()+"&";
				}
			}
		}
		if(otherParam != ""){
 			assembledUrl+="&"+otherParam;
 		} 
 		if(assembledParam != ""){
 			assembledUrl+="&"+assembledParam;
 		}
 		
 		if(queryData !=""){
 			queryData = encodeURIComponent(queryData);
 			assembledUrl+="&"+nameObjectId+"="+queryData;
 		}
 		var singleSelect=$('#'+idObjectId+'singleSelect').val();
 		if(singleSelect =="true")
			load('#'+idObjectId+'Option',assembledUrl,"","",true);	
		else
			load('#'+idObjectId+'Option',assembledUrl);	
	}
	onLoadSelectFuzzyDiv('${idObjectId!}','${nameObjectId!}','${url!}','${divName!}','${onclick!}','${callback!}','${selectedValue!}','${otherParam!}','${dependson!}','${referto!}','${defaultItem!}','${tipMsg!}','');
</script>
</#macro>
<#--
*  宏名：acadyearlist
*  功能：把学年列表组yearlist组装成HTML标筌语句 
*  参数：yearlist：	 学年列表yearlist  
*       currentyear: 当前学年currentyear
*       defaultitem：是否有默认选择项（即“请选择”）
-->
<#macro acadyearlist yearlist currentyear="" defaultitem=true>
	<#if yearlist?exists>
		<#if defaultitem || currentyear="">
  			<option value="">--请选择--</option>
  		</#if>
  		
  		<#if currentyear="">
		  	<#list yearlist as item>
		   		<option value=${item}>${item}</option>
			</#list> 
		<#else>
			<#list yearlist as item>
				<#if currentyear = item>
					<option value=${item} selected>${item}</option>
				<#else>
				    <option value=${item}>${item}</option>
				</#if>
		  	</#list>
		</#if>
  	</#if>
</#macro>



<#-- ======================================内部宏========================================-->
<#--
*宏名：selectObject
*功能 ：选择对象：学生、教师、职务等
-->
<#macro selectObject2 useCheckbox idObjectId nameObjectId url otherParam="" callback="" preset="" dynamicParam="" width=300 height=380>
	<#assign showObjectsFunction = "javascript:showObjects(this,'"+idObjectId+"','"+nameObjectId+"','"+url+"','"+useCheckbox?string+"','"+otherParam+"','"+callback+"','"+preset+"','"+dynamicParam+"',"+width+","+height+");">	
	<script language="JavaScript" src="${request.contextPath}/static/common/objectSelect.js"></script>
	<script>
	
	</script>
	<img src="${request.contextPath}/static/images/tree_icon.png" width="15" height="14" onClick="${showObjectsFunction}" style="cursor:pointer;">
             
</#macro>

<#macro selectObject useCheckbox idObjectId nameObjectId url otherParam="" callback="" preset="" dynamicParam="" width=300 height=380 switchSelector="" loadCallback="" showCancelButton="true">
	<#assign showObjectsFunction = "showPopupObjects('"+idObjectId+"','"+nameObjectId+"','"+url+"','"+useCheckbox?string+"','"+otherParam+"','"+callback+"','"+preset+"','"+dynamicParam+"',"+width+","+height+");">	

	<div id="${idObjectId}_nestedDiv"><#nested /></div>
	<div id="${idObjectId}_div" class="select_box02" style="z-index:999;" val_height="${height}" val_switchSelector="${switchSelector}" val_loadCallback="${loadCallback}" val_showCancelButton="${showCancelButton}">
		<div class="select_list02_container" style="width:${width}px;">
		</div>
	</div>
	<script language="JavaScript" src="${request.contextPath}/static/common/popupObjectSelect.js"></script>	
	<script>
		$(document).ready(function(){
			${showObjectsFunction}
		});
	</script>
</#macro>

<#--
*宏名：selectObjectTree
*功能 ：分左右两部分：左边为树、右边为选择对象
-->
<#macro selectObjectTree useCheckbox idObjectId nameObjectId url otherParam="" callback="" preset=""  width=650 height=420>
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId url=url otherParam=otherParam callback=callback preset=preset width=width height=height>
		<#nested />
	</@selectObject>
</#macro>
<#-- ======================================用户宏========================================-->

<#--
*宏名：selectUser
*功能 ：选择单个用户
-->
<#macro selectOneUser idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectUser useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectUser>	
</#macro>

<#--
*宏名：selectUsers
*功能 ：选择多个用户
-->
<#macro selectMoreUser idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectUser useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectUser>
</#macro>

<#macro selectUser useCheckbox idObjectId nameObjectId url="${request.contextPath}/common/getUserDataPopup.action" otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId url=url otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectObject>	
</#macro>
<#-- ======================================学生宏========================================-->

<#--
*宏名：selectStudent
*功能 ：选择单个学生
-->
<#macro selectOneStudent idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectStudent useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectStudent>	
</#macro>

<#--
*宏名：selectStudents
*功能 ：选择多个学生
-->
<#macro selectMoreStudent idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectStudent useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectStudent>
</#macro>

<#macro selectStudent useCheckbox idObjectId nameObjectId url="${request.contextPath}/common/getStudentDataPopup.action" otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId url=url otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectObject>	
</#macro>

<#--
*宏名：选择学生树
-->
<#macro selectMoreStudentTree idObjectId nameObjectId callback="" preset=""  otherParam="codeType=2" width=650 height=420 showCancelButton="true">
	<@selectStudentTree useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectStudentTree>	
</#macro>

<#macro selectOneStudentTree idObjectId nameObjectId callback="" preset=""  width=650 height=420 otherParam="codeType=2" showCancelButton="true">
	<@selectStudentTree useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectStudentTree>		
</#macro>

<#macro selectMoreStudentTreeUnitiveCode idObjectId nameObjectId callback="" preset=""  otherParam="codeType=1" width=650 height=420 showCancelButton="true">
	<@selectStudentTree useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectStudentTree>	
</#macro>

<#macro selectOneStudentTreeUnitiveCode idObjectId nameObjectId callback="" preset="" otherParam="codeType=1"  width=650 height=420 showCancelButton="true">
	<@selectStudentTree useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectStudentTree>	
</#macro>

<#macro selectStudentTree useCheckbox idObjectId nameObjectId url="${request.contextPath}/common/showStudentTreeDiv.action" otherParam="" callback="" preset=""  width=650 height=420 showCancelButton="true">
	<@selectObjectTree useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId url=url otherParam=otherParam callback=callback preset=preset width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectObjectTree>	
</#macro>

<#-- ======================================教师宏========================================-->
<#macro selectOneTeacher idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam="" width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectTeacher useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectTeacher>
</#macro>

<#macro selectMoreTeacher idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam="" width=400 height=380 switchSelector="" showCancelButton="true">
	<@selectTeacher useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectTeacher>
</#macro>

<#macro selectTeacher useCheckbox idObjectId nameObjectId url="${request.contextPath}/common/getTeacherDataPopup.action" otherParam="" callback="" preset="" dynamicParam="" width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId url=url otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectObject>
</#macro>

<#-- ===================教师宏(带查询)============================== -->
<#macro selectOneTeacherQuery idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam="" width=500 height=420 showCancelButton="true">
	<@selectTeacherQuery useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectTeacherQuery>
</#macro>

<#macro selectMoreTeacherQuery idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam="" width=500 height=420 showCancelButton="true">
	<@selectTeacherQuery useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectTeacherQuery>
</#macro>

<#macro selectTeacherQuery useCheckbox idObjectId nameObjectId url="${request.contextPath}/common/showTeacherDiv.action" otherParam="" callback="" preset="" dynamicParam="" width=500 height=420 showCancelButton="true">
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId url=url otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectObject>
</#macro>
<#--
*宏名：选择教师树
-->
<#macro selectMoreTeacherTree idObjectId nameObjectId callback="" preset="" otherParam=""  width=650 height=420 switchSelector="" showCancelButton="true">
	<@selectTeacherTree useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId callback=callback preset=preset otherParam=otherParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectTeacherTree>
</#macro>

<#macro selectOneTeacherTree idObjectId nameObjectId callback="" preset="" otherParam="" width=650 height=420 switchSelector="" showCancelButton="true">
	<@selectTeacherTree useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId callback=callback preset=preset otherParam=otherParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectTeacherTree>
</#macro>

<#macro selectTeacherTree useCheckbox idObjectId nameObjectId url="${request.contextPath}/common/showTeacherTreeDiv.action" callback="" preset="" otherParam="" width=650 height=420 showCancelButton="true">
	<@selectObjectTree useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId url=url otherParam="codeType=2${otherParam}" callback=callback preset=preset width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectObjectTree>
</#macro>

<#-- ======================================微代码宏========================================-->
<#macro selectMoreMcodeDetail idObjectId nameObjectId otherParam callback="" preset="" width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectMcodeDetail useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectMcodeDetail>
</#macro>
<#macro selectOneMcodeDetail idObjectId nameObjectId otherParam callback="" preset=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectMcodeDetail useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectMcodeDetail>
</#macro>
<#macro selectMcodeDetail useCheckbox idObjectId nameObjectId otherParam callback="" preset=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam url="${request.contextPath}/common/getMcodeDetailDataPopup.action" callback=callback preset=preset width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectObject>
</#macro>

<#-- ======================================教学场地宏========================================-->
<#--
*宏名：selectTeachPlace
*功能 ：选择单个教学场地
-->
<#macro selectOneTeachPlace idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectTeachPlace useCheckbox=false idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectTeachPlace>	
</#macro>

<#--
*宏名：selectUsers
*功能 ：选择多个教学场地
-->
<#macro selectMoreTeachPlace idObjectId nameObjectId otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectTeachPlace useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectTeachPlace>
</#macro>

<#macro selectTeachPlace useCheckbox idObjectId nameObjectId url="${request.contextPath}/common/getTeachPlaceData.action" otherParam="" callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId url=url otherParam=otherParam callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectObject>	
</#macro>

<#-- ======================================树宏========================================-->
<#--
*宏名 selectTree
*功能 选择树节点,返回的节点名称不带链接，需要自己在对应td中定义
*参数 url表示调用action完整路径(选择的树不同)，otherParam表示需要另外需要的参数及值
*js需要的参数：tdName:节点名显示的td对应id  ,idName:存储节点id的域id,id选中的节点id,name选中的节点名字
-->
<#macro selectTree_old idObjectId nameObjectId treeUrl useCheckbox=false callback="" preset="" dynamicParam=""  width=300 height=380 showCancelButton="true">
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId 
		url="${request.contextPath}/common/xtree/showTreeDiv.action" otherParam="url="+treeUrl?url('UTF-8') callback=callback preset=preset dynamicParam=dynamicParam width=width height=height showCancelButton=showCancelButton>
		<#nested />
	</@selectObject>		
</#macro>

<#macro selectTree idObjectId nameObjectId treeUrl useCheckbox=false callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectObject useCheckbox=useCheckbox idObjectId=idObjectId nameObjectId=nameObjectId 
		url=treeUrl callback=callback preset=preset dynamicParam=dynamicParam width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectObject>
</#macro>

<#macro selectMoreTree idObjectId nameObjectId treeUrl callback="" preset="" dynamicParam=""  width=300 height=380 switchSelector="" showCancelButton="true">
	<@selectTree useCheckbox=true idObjectId=idObjectId nameObjectId=nameObjectId treeUrl=treeUrl callback=callback preset=preset dynamicParam=dynamicParam  width=width height=height switchSelector=switchSelector showCancelButton=showCancelButton>
		<#nested />
	</@selectTree>	
</#macro>

<#macro selectAddressBookLayer idObjectId deptObjectId="" deptLeaderObjectId="" groupObjectId="" nameObjectId="" detailObjectId="" otherParam="" callback="" preset="" height="430" currentUnit="false" needGroupId="false">
	<#assign showAddressBookLayer = "openAddressBook('"+idObjectId+"','"+deptObjectId+"','"+deptLeaderObjectId+"','"+groupObjectId+"','"+nameObjectId+"','"+detailObjectId+"','"+height+"',"+currentUnit+",'${request.contextPath}/component/addressbook/addressBook-userAddressBookLayer.action','"+otherParam+"','"+callback+"','"+preset+"','"+needGroupId+"');">	
	<span onclick="${showAddressBookLayer}"><#nested /></span>
	<script>
		function openAddressBook(idObjectId,deptObjectId,deptLeaderObjectId,groupObjectId,nameObjectId,detailObjectId,height,currentUnit,url,params,callback,preset,needGroupId){
			if (preset != null && preset != "") {
				if(eval(preset+"()") == false){
					return false;
				}
			}
		 	url=url+(url.indexOf('?') > -1 ? '&':'?')+"idObjectId="+idObjectId+"&deptObjectId="+deptObjectId+"&deptLeaderObjectId="+deptLeaderObjectId+"&groupObjectId="+groupObjectId+"&nameObjectId="+nameObjectId+"&detailObjectId="+detailObjectId+"&height="+height+"&currentUnit="+currentUnit+"&callback="+callback+"&needGroupId="+needGroupId;
		 	if(params != ""){
		 		url+="&"+params;
		 	} 	
		 	openDiv('#addressBookLayer',null,url,true,'#addressBookLayer .wrap','410');
		}
	</script>
</#macro>
<#macro selectAddressBook idObjectId deptObjectId="" deptLeaderObjectId="" groupObjectId="" nameObjectId="" detailObjectId="" otherParam="" changeHeight="" callback="" preset="" height="370" currentUnit="false" needGroupId="false">
	<#assign loadAddressBookFunction = "loadAddressBookDetail('"+idObjectId+"','"+deptObjectId+"','"+deptLeaderObjectId+"','"+groupObjectId+"','"+nameObjectId+"','"+detailObjectId+"','"+height+"',"+currentUnit+",'${request.contextPath}/component/addressbook/addressBook-userAddressBook.action','"+otherParam+"','"+changeHeight+"','"+callback+"','"+preset+"','"+needGroupId+"');">	
	<script language="JavaScript" src="${request.contextPath}/static/common/addressObjectSelect.js"></script>	
	<div class="address-wrap-layer address-wrap-layer2" id="addressBookDetail_div">
		<div class="pub-tab fn-rel">
	        <ul class="pub-tab-list">
	            <li class="current" onclick="changeToGroup('${currentUnit}');">用户组</li>
	            <#if currentUnit == 'false'>
	            <li onclick="changeToAllUnit();">所有单位</li>
	            </#if>
	        </ul>
	        <div class="search-wrap">
                <input type="text" id="searchName" name="searchName" onkeyup="searchUser();" class="input-txt fn-left" style="width:200px;margin-left:-1px;">
                <a href="javascript:void(0);" onclick="searchUser();" class="abtn-blue fn-left ml-15">搜索</a>
            </div>
	    </div>
	    <div class="pub-table-wrap">
	        <div class="user-inner pt-10 fn-rel">
				<div id="addressBookDetail"></div>
				<#nested/>
			</div>
	    </div>
	</div>
	<script>
		$(document).ready(function(){
			${loadAddressBookFunction}
		});
		function treeItemClick(id,name){
			load("#userAllDiv","${request.contextPath}/common/xtree/deptUserTree.action?height=${height}&unitId="+id);
			$('#userAllDiv').show();
		}
		function treeItemDblClick(id,name) {
			//未实现
		}
		
		function searchUser(){
			//中文转码
			var searchName = $("#searchName").val();
			setTimeout(function(){
				var searchName2 = $("#searchName").val();
				if(searchName == searchName2 && searchName!='' && trim(searchName)!=''){
					load("#userAllDiv","${request.contextPath}/component/addressbook/addressBook-searchUser.action?height=${height!}&searchName="+encodeURIComponent(searchName));
					$("#userAllDiv").show();
				}
			},500);
		}
		
		function trim(str) {
			if(str !='' && str != null){
				str= str.replace(/(^\s+)|(\s+$)/g, "");
			}
			return str;
		}
	</script>
</#macro>

<#macro selectAddressBookDirectLayer idObjectId deptObjectId="" deptLeaderObjectId="" groupObjectId="" nameObjectId="" detailObjectId="" otherParam="" callback="" preset="" height="430" currentUnit="false" needGroupId="false" currentUnitId="">
	<#assign showAddressBookLayer = "openAddressBook('"+idObjectId+"','"+deptObjectId+"','"+deptLeaderObjectId+"','"+groupObjectId+"','"+nameObjectId+"','"+detailObjectId+"','"+height+"',"+currentUnit+",'${request.contextPath}/component/addressbook/addressBook-userAddressBookDirectLayer.action','"+otherParam+"','"+callback+"','"+preset+"','"+needGroupId+"','"+currentUnitId+"');">	
	<span onclick="${showAddressBookLayer}"><#nested /></span>
	<script>
		function openAddressBook(idObjectId,deptObjectId,deptLeaderObjectId,groupObjectId,nameObjectId,detailObjectId,height,currentUnit,url,params,callback,preset,needGroupId,currentUnitId){
			if (preset != null && preset != "") {
				if(eval(preset+"()") == false){
					return false;
				}
			}
		 	url=url+(url.indexOf('?') > -1 ? '&':'?')+"idObjectId="+idObjectId+"&deptObjectId="+deptObjectId+"&deptLeaderObjectId="+deptLeaderObjectId+"&groupObjectId="+groupObjectId+"&nameObjectId="+nameObjectId+"&detailObjectId="+detailObjectId+"&height="+height+"&currentUnit="+currentUnit+"&callback="+callback+"&needGroupId="+needGroupId+"&currentUnitId="+currentUnitId;
		 	if(params != ""){
		 		url+="&"+params;
		 	} 	
		 	openDiv('#addressBookLayer',null,url,true,'#addressBookLayer .wrap','410');
		}
	</script>
</#macro>
<#macro selectAddressBookDirect idObjectId deptObjectId="" deptLeaderObjectId="" groupObjectId="" nameObjectId="" detailObjectId="" otherParam="" changeHeight="" callback="" preset="" height="370" currentUnit="false" needGroupId="false" currentUnitId="">
	<#assign loadAddressBookFunction = "loadAddressBookDetail('"+idObjectId+"','"+deptObjectId+"','"+deptLeaderObjectId+"','"+groupObjectId+"','"+nameObjectId+"','"+detailObjectId+"','"+height+"',"+currentUnit+",'${request.contextPath}/component/addressbook/addressBook-userAddressBookDirect.action','"+otherParam+"','"+changeHeight+"','"+callback+"','"+preset+"','"+needGroupId+"','"+currentUnitId+"');">	
	<script language="JavaScript" src="${request.contextPath}/static/common/addressObjectSelect.js"></script>	
	<div class="address-wrap-layer address-wrap-layer2" id="addressBookDetail_div">
		<div class="pub-tab fn-rel">
	        <ul class="pub-tab-list">
	            <li class="current" onclick="changeToDirectUnit();">直属单位</li>
	        </ul>
	    </div>
	    <div class="pub-table-wrap">
	        <div class="user-inner pt-10 fn-rel">
				<div id="addressBookDetail"></div>
				<#nested/>
			</div>
	    </div>
	</div>
	<script>
		$(document).ready(function(){
			${loadAddressBookFunction}
		});
		function treeItemClick(id,name){
			load("#userAllDiv","${request.contextPath}/common/xtree/deptUserTree.action?height=${height}&unitId="+id);
			$('#userAllDiv').show();
		}
		function treeItemDblClick(id,name) {
			//未实现
		}
		
		function trim(str) {
			if(str !='' && str != null){
				str= str.replace(/(^\s+)|(\s+$)/g, "");
			}
			return str;
		}
	</script>
</#macro>

<#macro selectAddressBookAllLayer userObjectId deptObjectId unitObjectId detailObjectId otherParam="" callback="" preset="" height="430">
	<#assign showAddressBookAllLayer = "openAddressBookAll('"+userObjectId+"','"+deptObjectId+"','"+unitObjectId+"','"+detailObjectId+"','"+height+"','${request.contextPath}/component/addressbook/addressBook-userAddressBookAllLayer.action','"+otherParam+"','"+callback+"','"+preset+"');">	
	<span onclick="${showAddressBookAllLayer}"><#nested /></span>
	<script>
		function openAddressBookAll(userObjectId,deptObjectId,unitObjectId,detailObjectId,height,url,params,callback,preset){
			if (preset != null && preset != "") {
				if(eval(preset+"()") == false){
					return false;
				}
			}
		 	url=url+(url.indexOf('?') > -1 ? '&':'?')+"userObjectId="+userObjectId+"&deptObjectId="+deptObjectId+"&unitObjectId="+unitObjectId+"&detailObjectId="+detailObjectId+"&height="+height+"&callback="+callback;
		 	if(params != ""){
		 		url+="&"+params;
		 	}
		 	openDiv('#addressBookLayer',null,url,true,'#addressBookLayer .wrap','410');
		}
	</script>
</#macro>
<#macro selectAddressBookAll userObjectId deptObjectId unitObjectId detailObjectId otherParam="" callback="" preset="" height="370">
	<#assign loadAddressBookAllFunction = "loadAddressBookAllDetail('"+userObjectId+"','"+deptObjectId+"','"+unitObjectId+"','"+detailObjectId+"','"+height+"','${request.contextPath}/component/addressbook/addressBook-userAddressBookAll.action','"+otherParam+"','"+callback+"','"+preset+"');">	
	<script language="JavaScript" src="${request.contextPath}/static/common/addressObjectAllSelect.js"></script>
	<div class="address-wrap-layer address-wrap-layer2">
		<div class="pub-tab fn-rel">
	        <ul class="pub-tab-list">
	            <li class="current" onclick="changeToGroup();">用户组</li>
	            <li onclick="changeToUnitGroup();">单位组</li>
	            <li onclick="changeToAllUnit();">所有单位</li>
	        </ul>
	        <div class="search-wrap">
                <input type="text" id="searchName" name="searchName" onkeyup="searchUser();" class="input-txt fn-left" style="width:200px;margin-left:-1px;">
                <a href="javascript:void(0);" onclick="searchUser();" class="abtn-blue fn-left ml-15">搜索</a>
            </div>
	    </div>
	    <div class="pub-table-wrap">
	        <div class="user-inner pt-10 fn-rel">
				<div id="addressBookDetailAll"></div>
				<#nested/>
			</div>
	    </div>
	</div>
	<script>
		$(document).ready(function(){
			${loadAddressBookAllFunction}
		});
		function treeItemClick(id,name){
			load("#userAllDiv","${request.contextPath}/common/xtree/deptUserAllTree.action?height=${height}&unitId="+id);
			$('#userAllDiv').show();
		}
		function treeItemCheck(treeNode,cascade){
			if(cascade){
				var zTree = $.fn.zTree.getZTreeObj("zTree");
				var childNodes = zTree.transformToArray(treeNode);
	        	var nodes = new Array();
	        	for(i = 0; i < childNodes.length; i++) {
	            	setTreeNode(childNodes[i]);
	        	}
			}else{
				setTreeNode(treeNode);
			}
			setNameListHtml(userSelectionAll);
		}
		function treeItemDblClick(id,name) {
			//未实现
		}
		
		function searchUser(){
			//中文转码
			var searchName = $("#searchName").val();
			setTimeout(function(){
				var searchName2 = $("#searchName").val();
				if(searchName == searchName2 && searchName!='' && trim(searchName)!=''){
					load("#userAllDiv","${request.contextPath}/component/addressbook/addressBook-searchAllUser.action?height=${height!}&searchName="+encodeURIComponent(searchName));
					$("#userAllDiv").show();
				}
			},500);
		}
		
		function trim(str) {
			if(str !='' && str != null){
				str= str.replace(/(^\s+)|(\s+$)/g, "");
			}
			return str;
		}
	</script>
</#macro>

