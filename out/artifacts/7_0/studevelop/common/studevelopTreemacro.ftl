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
<#macro gradeClassStudentForSchoolInsetTree url="/basedata/tree/gradeClassStudentForSchoolInsetTree/page" id="gradeClassStudentForSchoolInsetTree" class="" height="" parameter="" click="">
    <@findTree_1 id=id class=class height=height url=url parameter=parameter click=click>
    <#nested>
    </@findTree_1>
</#macro>

<#macro findTree id="" class="" height="" url="" parameter="" click="">
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
		var ${id}setting = {
			check:{
				enable:false
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
				url:"${request.contextPath+url}?${parameter!}",
				success:function(data){
					var jsonO = JSON.parse(data);
					if(jsonO.length == 0){
						//alert("没有找到数据");
						return;
					}
		 			$.fn.zTree.init($("#${id}"), ${id}setting, jsonO);
				}
			});
		}
		${id}zTreeFind();
	</script>
</#macro>

<#macro findTree_1 id="" class="" height="" url="" parameter="" click="">
	<ul id="${id}" class="ztree" style=" height:645px; overflow:auto;" ></ul>
	<script>
		function ${id}zTreeFun(event, treeId, treeNode, clickFlag){
			${click}(event, treeId, treeNode, clickFlag);
		}
		var ${id}setting = {
			check:{
				enable:false
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
				url:"${request.contextPath+url}<#if parameter?default('')!=''>+"?${parameter}"</#if>",
				success:function(data){
					var jsonO = JSON.parse(data);
					if(jsonO.length == 0){
						//alert("没有找到数据");
						return;
					}
		 			$.fn.zTree.init($("#${id}"), ${id}setting, jsonO);
				}
			});
		}
		${id}zTreeFind();
	</script>
</#macro>