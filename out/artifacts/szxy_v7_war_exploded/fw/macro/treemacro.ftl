<#--        	type
教育局	edu
学校		school
年级		grade
班级		class
部门		dept
学生		student

click参数例如
function onTreeClick(event, treeId, treeNode, clickFlag){
	if(treeNode.type == "class"){
		var id = treeNode.id;
		...
	}
}
传入onTreeClick
>

<#--年级-->
<#--
嵌入方式
本单位年级
id必须
parameter参数
-->
<#macro gradeForSchoolInsetTree id="gradeForSchoolInsetTree" class="" height="" parameter="" click="">
	<@findTree id=id class=class height=height url="/basedata/tree/gradeForSchoolInsetTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree>
</#macro>
<#--
嵌入方式
直属单位年级
id必须
parameter参数
-->
<#macro gradeForUnitInsetTree id="gradeForUnitInsetTree" class="" height="" parameter="" click="">
	<@findTree id=id class=class height=height url="/basedata/tree/gradeForUnitInsetTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree>
</#macro>


<#--班级-->
<#--
嵌入方式
本单位年级-班级
id必须
parameter参数
-->
<#macro gradeClassForSchoolInsetTree id="gradeClassForSchoolInsetTree" class="" height="" parameter="" click="">
	<@findTree id=id class=class height=height url="/basedata/tree/gradeClassForSchoolInsetTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree>
</#macro>
<#--
嵌入方式
本单位年级-班级-学生
id必须
parameter参数
-->
<#macro gradeClassStudentForSchoolInsetTree id="gradeClassStudentForSchoolInsetTree" class="" height="" parameter="" click="">
    <@findTree_1 id=id class=class height=height url="/basedata/tree/gradeClassStudentForSchoolInsetTree/page" parameter=parameter click=click>
    <#nested>
    </@findTree_1>
</#macro>


<#--
嵌入方式
本单位-用户类型-用户
id必须
parameter参数
-->
<#macro ownerTypeUserForUnitInsetTree id="ownerTypeUserForUnitInsetTree" class="" height="" parameter="" click="">
    <@findTree_1 id=id class=class height=height url="/basedata/tree/ownerTypeUserForUnitInsetTree/page" parameter=parameter click=click>
    <#nested>
    </@findTree_1>
</#macro>


<#--
嵌入方式
用户-角色类型-角色
id必须
parameter参数
-->
<#macro typeServerRoleForUserInsetTree id="typeServerRoleForUserInsetTree" class="" height="" parameter="" click="">
    <@findTree id=id class=class height=height url="/basedata/tree/typeServerRoleForUserInsetTree/page" parameter=parameter click=click>
    <#nested>
    </@findTree>
</#macro>

<#--
嵌入方式
直属单位年级-班级
id必须
parameter参数
-->
<#macro gradeClassForUnitInsetTree id="gradeClassForUnitInsetTree" class="" height="" parameter="" click="">
	<@findTree id=id class=class height=height url="/basedata/tree/gradeClassForUnitInsetTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree>
</#macro>


<#--单位-->
<#--
嵌入方式
直属单位
id必须
parameter参数：isSchool是否只显示学校默认false
-->
<#macro unitForDirectInsetTree id="unitForDirectInsetTree" class="" height="" parameter="" click="" onCheck="" checkEnable=false>
	<@findTree id=id class=class height=height url="/basedata/tree/unitForDirectInsetTree/page" parameter=parameter click=click onCheck=onCheck checkEnable=checkEnable>
	<#nested>
	</@findTree>
</#macro>


<#--单位-->
<#--
嵌入方式
下属单位
id必须
parameter参数：isSchool是否只显示学校默认false
-->
<#macro unitForSubInsetTree id="unitForSubInsetTree" class="" height="" parameter="" click="" onCheck="" checkEnable=false notRelate=false>
	<@findTree id=id class=class height=height url="/basedata/tree/unitForSubInsetTree/page" parameter=parameter click=click onCheck=onCheck checkEnable=checkEnable notRelate=notRelate>
	<#nested>
	</@findTree>
</#macro>

<#--部门-->
<#--
嵌入方式
本单位部门
id必须
parameter参数
-->
<#macro deptForUnitInsetTree id="deptForUnitInsetTree" class="" height="" parameter="" click="">
	<@findTree id=id class=class height=height url="/basedata/tree/deptForUnitInsetTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree>
</#macro>
<#--
嵌入方式
本单位部门 -教师
id必须
parameter参数
-->
<#macro deptTeacherForUnitInsetTree id="deptTeacherForUnitInsetTree" class="" height="" parameter="" click="">
	<@findTree_1 id=id class=class height=height url="/basedata/tree/deptTeacherForUnitInsetTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree_1>
</#macro>
<#--
嵌入方式
直属单位部门
id必须
parameter参数
-->
<#macro deptForDirectUnitInsetTree id="deptForDirectUnitInsetTree" class="" height="" parameter="" click="">
	<@findTree id=id class=class height=height url="/basedata/tree/deptForDirectUnitInsetTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree>
</#macro>
<#--
	场地
-->

<#macro placeForBuildingTree id="placeForBuildingTree" class="" height="" parameter="" click="">
	<@findTree id=id class=class height=height url="/basedata/tree/placeForBuildingTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree>
</#macro>
<#macro findTree id="" class="" height="" url="" parameter="" click="" onCheck="" checkEnable=false notRelate=false>
	<div class="widget-box ${class}" style="border-color: #5090C1;">
		<#nested>
		<div class="widget-body">
			<div class="widget-main padding-8" style="<#if height?default('')!=''>height:${height}px;<#else>height:600px;</#if>overflow:auto;">
					<ul id="${id}" class="ztree">${parameter}</ul>
			</div>
		</div>
	</div>
	<script>
		function ${id}zTreeFun(event, treeId, treeNode, clickFlag){
			${click}(event, treeId, treeNode, clickFlag);
		}
		function ${id}zTreeOnCheck(event, treeId, treeNode, clickFlag){
			${onCheck}(event, treeId, treeNode);
		}
		var ${id}setting = {
			check:{
				<#if notRelate >
				chkboxType: { "Y": "s", "N": "s" },
				</#if>
				enable:${checkEnable?default(false)?string("true","false")}
			},
			data: {				
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pId"
				}
			},
			callback: {
				onClick: ${id}zTreeFun,
				onCheck:${id}zTreeOnCheck
			}
		};
		function ${id}zTreeFind(){
			$.ajax({
				url:"${request.contextPath+url}?${parameter!}",
				success:function(data){
					var jsonO = JSON.parse(data);
					if(jsonO.length == 0){
						alert("没有找到数据");
						return;
					}
		 			$.fn.zTree.init($("#${id}"), ${id}setting, jsonO);
				}
			});
		}
		${id}zTreeFind();
		
	</script>
</#macro>
<#--
chooseTreeNodeId:默认选中的node的id
-->
<#macro findTree_1 id="" class="" height="" url="" parameter="" click="" chooseTreeNodeId="" checkEnable=false>
	<ul id="${id}" class="ztree" style=" height:645px; overflow:auto;" ></ul>
	<script>
		function ${id}zTreeFun(event, treeId, treeNode, clickFlag){
			${click}(event, treeId, treeNode, clickFlag);
		}
		var ${id}setting = {
			check:{
				enable:${checkEnable?default(false)?string("true","false")}
			},
			data: {				
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pId"
				}
			},
			callback: {
				onClick: ${id}zTreeFun
			}
		};
		function ${id}zTreeFind(){
			$.ajax({
				url:"${request.contextPath+url}"<#if parameter?default('')!=''>+"?${parameter}"</#if>,
				success:function(data){
					var jsonO = JSON.parse(data);
					if(jsonO.length == 0){
						alert("没有找到数据");
						return;
					}
		 			$.fn.zTree.init($("#${id}"), ${id}setting, jsonO);
		 			<#if chooseTreeNodeId!="">
		 			var zTree_Menu = $.fn.zTree.getZTreeObj("${id}");				
		 			var node = zTree_Menu.getNodeByParam("id",'${chooseTreeNodeId}' );	
		 			if(node){
		 				zTree_Menu.selectNode(node,true);//指定选中ID的节点	
		 				zTree_Menu.expandNode(node, true, false);//指定选中ID节点展开
		 			}
		 			</#if>		
				}
			});
		}
		
		
		${id}zTreeFind();
	</script>
</#macro>
<#--
嵌入方式
教研组-教师
id必须
parameter参数
-->
<#macro teacherForGroupTree id="teacherForGroupTree" class="" height="" parameter="" click="">
	<@findTree_1 id=id class=class height=height url="/basedata/tree/teacherForGroupTree/page" parameter=parameter click=click>
	<#nested>
	</@findTree_1>
</#macro>

