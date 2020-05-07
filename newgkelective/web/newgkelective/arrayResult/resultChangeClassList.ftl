<script language="javascript" type="text/javascript">
</script>
<div style="" class="harftable_${right!'0'}">
	<div class="table-switch-data default labelInf">
		<span>总数：<em>${dtoList?size!}</em></span>
		<span>男：<em>${manCount!}</em></span>
		<span>女：<em>${woManCount!}</em></span>
		<span>${courseName!}：<em>${courseAvg?string("#.##")!'0'}</em></span>
		<span>语数英：<em>${ysyAvg?string("#.##")!'0'}</em></span>
		<span>总成绩：<em>${totalAvg?string("#.##")!'0'}</em></span>
	</div>
	<table class="table table-bordered table-striped js-sort-table show-stulist-table mytable_${right!'0'}">
		<thead>
			<tr>
				<th>
					<label class="pos-rel" style="margin-right: 0px">
						<input type="checkbox" class="wp"  name="allStudentId">
						<span class="lbl"></span>
					</label>
				</th>
				<th>序号</th>
				<th>姓名</th>
				<th>性别</th>
				<th>原行政班 <a class="float-right color-grey js-popover-all js-popover-filter js-popover-filter-class popover_${right!'0'} mr10" href="#" aria-describedby="filterClass"><i class="fa fa-filter"></i></a></th>
				<th>选课 <a class="float-right color-grey js-popover-all js-popover-filter js-popover-filter-subject popover_${right!'0'} mr10" href="#"><i class="fa fa-filter"></i></a></th>
				<th>${courseName!}</th>
				<th>语数英</th>
				<th>总成绩</th>
			</tr>
		</thead>
		<tbody>
			<#if dtoList?exists && (dtoList?size>0)>
			<#list dtoList as dto>
				<tr>
					<td>
						<label class="pos-rel">
							<input name="course-checkbox"  type="checkbox" class="wp"  value="${dto.studentId!}">
							<span class="lbl"></span>
						</label>
					</td>
					<td class="xuhao">${dto_index +1}</td>
					<td>${dto.studentName!}</td>
					<td>${dto.sex!}</td>
					<td data-name="${dto.className!}"><input type="hidden" class="classId" value="${dto.classId!}">${dto.className!}</td>
					<td data-name="${dto.choResultStr!}"><input type="hidden" class="subjectId" value="${dto.chooseSubjects!}">${dto.choResultStr!}</td>
					<td>${dto.subjectScore[subjectId]!}</td>
					<td>${dto.subjectScore["YSY"]!}</td>
					<td>${dto.subjectScore["TOTAL"]!}</td>
				</tr>
			</#list>
			</#if>
		</tbody>
	</table>
</div>
<div id="filterClassId${right!'0'}" style="display:none;">
	<div id="filterClassId1${right!'0'}">
	 	<#list classFilterList as classFilter>
		<div>
			<label class="pos-rel">
				<input type="checkbox" class="wp" name="classFilter" checked value="${classFilter[0]!}" onChange="myfilter${right!'0'}(this,1)">
				<span class="lbl"> ${classFilter[1]!}(${classFilter[2]!})</span>
			</label>
		</div>
		
		</#list>
	</div>
<div id="filterSubjectId${right!'0'}"  style="display:none;">
	<div id="filterSubjectId1${right!'0'}">
 	<#list subjectFilterList as subjectFilter>
		<div>
			<label class="pos-rel">
				<input type="checkbox" class="wp" name="subjectFilter" checked value="${subjectFilter[0]!}" onChange="myfilter${right!'0'}(this,2)">
				<span class="lbl"> ${subjectFilter[1]!}(${subjectFilter[2]!})</span>
			</label>
		</div>	
	</#list>
	</div>
</div>
<script>
	var isShowClass${right!'0'}=true;
	var isShowSubject${right!'0'}=true;
	//班级选中
	var map1${right!'0'}={};
	//科目选中
	var map2${right!'0'}={};
	
	var chooseStuIdsToAdd${right!'0'}="";//用于新增存放学生
	
	var page_dt${right!'0'};
	
	function loadMyTable${right!'0'}(){
		page_dt${right!'0'}=$(".mytable_${right!'0'}").DataTable({
			// 设置垂直方向高度
	        scrollY: 450,
			// 禁用搜索
			searching: false,
			destroy:true,
	        // 禁止表格分页
	        paging: false,
	        // 禁止宽度自动
			autoWidth: false,
			info: false,
			order: [
				[8,'desc']
			],
			// 禁用指定列排序
			columnDefs: [
				{ orderable: false, targets: 0 },
				{ orderable: false, targets: 1 }
		    ],
		    language: {
		    	 emptyTable:"暂无数据"
		    },
		    fnDrawCallback:function(){
		    	makeXuhao${right!'0'}("harftable_${right!'0'}");
		    }
	    });
	}
	
	$(function(){
		// 通过js添加table水平垂直滚动条
		loadMyTable${right!'0'}(); 
	    
	    //左右全选功能
	    $(".harftable_${right!'0'}").on('change','.show-stulist-table input:checkbox[name=allStudentId]',function(){
			if($(this).is(':checked')){
				$(this).parents(".table-switch-box").find('input:checkbox[name=course-checkbox]').each(function(i){
					var $tr=$(this).parents("tr");
					if(!$($tr).is(":hidden")){
						$(this).prop('checked',true);
					}else{
						$(this).prop('checked',false);
					}
					
				});
			}else{
				$(this).parents(".table-switch-box").find('input:checkbox[name=course-checkbox]').each(function(i){
					$(this).prop('checked',false);
				});
			}
		});
		
		$(".harftable_${right!'0'}").on('change','.show-stulist-table input:checkbox[name=course-checkbox]',function(){
			if($(this).is(':checked')){
				
			}else{
				$(this).parents(".table-switch-box").find("input:checkbox[name=allStudentId]").prop('checked',false);
			}
		});
	    
		
		//行政班过滤
		$(".mytable_${right!'0'}").find('.js-popover-filter-class').popover({
			content:$("#filterClassId1${right!'0'}"),
			html: true,
			placement: 'bottom',
			trigger: 'manual',
		    container: ".harftable_${right!'0'}"
		})
		//选课过滤
		$(".mytable_${right!'0'}").find('.js-popover-filter-subject').popover({
			content:$("#filterSubjectId1${right!'0'}"),
			html: true,
			placement: 'bottom',
			trigger: 'manual',
		    container: ".harftable_${right!'0'}"
		})
		//显示过滤列表
		$(".mytable_${right!'0'}").find('.js-popover-filter-subject').click(function(){
			$('.js-popover-filter-class').popover('hide');
			$(".js-popover-filter-subject:not(.popover_${right!'0'})").popover('hide');
			$(this).popover('show');
		})
		$(".mytable_${right!'0'}").find('.js-popover-filter-class').click(function(){
			$('.js-popover-filter-subject').popover('hide');
			$(".js-popover-filter-class:not(.popover_${right!'0'})").popover('hide');
			$(this).popover('show');
		})
	})
	
	
	//过滤的地方处理
	function myfilter${right!'0'}(obj,type){
		var filter1=$(obj).val();
		if(type==1){
			filterItem${right!'0'}(filter1,obj,type);
		}else{
			filterItem${right!'0'}(filter1,obj,type);
		}
	}
	//obj:过滤器中点击的复选框
	function filterItem${right!'0'}(filterValue,obj,type){
		var isShowAll=false;
		var objDiv=$(obj).parent().parent();
		var otherObj=$(objDiv).siblings();
		if(filterValue==""){
			//操作的是全部
			if($(obj).is(':checked')){
				//选中所有
				if(otherObj.length>0){
					$(objDiv).siblings().each(function(){
						$(this).find("input").prop('checked',true);
					})
				}
				isShowAll=true;
			}else{
				isShowAll=false;
				if(otherObj.length>0){
					$(objDiv).siblings().each(function(){
						$(this).find("input").prop('checked',false);
					})
				}
			}
		}else{
			if($(obj).is(':checked')){
				isShowAll=false;
			}else{
				isShowAll=false;
				//全选部分不选中
				$(objDiv).siblings().each(function(){
					if($(this).val()==""){
						$(this).find("input").prop('checked',false);
						return false;
					}
				})
			}
		}
		if(type==1){
			if(isShowAll){
				isShowClass${right!'0'}=true;
			}else{
				isShowClass${right!'0'}=false;
				map1${right!'0'}={};
				$("#filterClassId1${right!'0'}").find("input").each(function(){
					if($(this).is(':checked')){
						var iid=$(this).val();
						map1${right!'0'}[iid]=iid;
					}
				})
			}
		}else{
			if(isShowAll){
				isShowSubject${right!'0'}=true;
			}else{
				isShowSubject${right!'0'}=false;
				map2${right!'0'}={};
				$("#filterSubjectId1${right!'0'}").find("input").each(function(){
					if($(this).is(':checked')){
						var iid=$(this).val();
						map2${right!'0'}[iid]=iid;
					}
				})
			}
		}
		filterTable${right!'0'}();
	}
	function filterTable${right!'0'}(){
		//过滤
		var trList=$(".mytable_${right!'0'} tbody").find("tr");
		if(trList.length==0){
			return;
		}
		if(isShowSubject${right!'0'} && isShowClass${right!'0'}){
			for(var i=0;i<trList.length;i++){
				$(trList[i]).find("input[name='studentId']").prop('checked',true);
				$(trList[i]).show();
			}
		}else{
			for(var i=0;i<trList.length;i++){
				var ccd=$(trList[i]).find(".classId").val();
				var ssd=$(trList[i]).find(".subjectId").val();
				if((isShowClass${right!'0'} || map1${right!'0'}[ccd]) && (isShowSubject${right!'0'} || map2${right!'0'}[ssd])){
					$(trList[i]).find("input[name='course-checkbox']").prop('checked',true);
					$(trList[i]).show();
				}else{
					$(trList[i]).find("input[name='course-checkbox']").prop('checked',false);
					$(trList[i]).hide();
				}
			}
		}
		makeXuhao${right!'0'}("harftable_${right!'0'}");
	}

	function makeXuhao${right!'0'}(objClass){
		var $tdody=$("."+objClass).find("tbody");
		if($tdody){
			var trList=$($tdody).find("tr");
			var ii=1;
			for(var i=0;i<trList.length;i++){
				if(!$(trList[i]).is(":hidden")){
					$(trList[i]).find(".xuhao").html(ii);
					ii++;
				}
			}
		}
	}
	
	function updatePovpor${right!'0'}(){
		//修改筛选值
		var $tdody=$(".mytable_${right!'0'}").find('tbody');
		if($tdody){
			var allStuNum=0;
			var classNameMap={};
			var subjectNameMap={};
			var classmap={};
			var subjectmap={};
			var trList=$($tdody).find("tr");
			var classChoosemap={};
			var subjectChoosemap={};
			var chooseStuNum=0;
			for(var i=0;i<trList.length;i++){
				var ccd=$(trList[i]).find(".classId").val();
				var ssd=$(trList[i]).find(".subjectId").val();
				var cName=$(trList[i]).find("td").eq(4).attr("data-name");
				var sName=$(trList[i]).find("td").eq(5).attr("data-name");
				allStuNum++;
				if(!classmap[ccd]){
					classmap[ccd]=1;
					classNameMap[ccd]=cName;
				}else{
					classmap[ccd]=classmap[ccd]+1;
				}
				if(!subjectmap[ssd]){
					subjectmap[ssd]=1;
					subjectNameMap[ssd]=sName;
				}else{
					subjectmap[ssd]=subjectmap[ssd]+1;
				}
				if(!$(trList[i]).is(":hidden")){
					if(!classChoosemap[ccd]){
						classChoosemap[ccd]=1;
					}else{
						classChoosemap[ccd]=classChoosemap[ccd]+1;
					}
					if(!subjectChoosemap[ssd]){
						subjectChoosemap[ssd]=1;
					}else{
						subjectChoosemap[ssd]=subjectChoosemap[ssd]+1;
					}
					chooseStuNum++;
				}
			}
			$(".mytable_${right!'0'}").find('.js-popover-filter-class').popover('show');
			$("#filterClassId1${right!'0'}").html("");
			var classHtml="";
			if(chooseStuNum==allStuNum){
				classHtml=makeFliterSpan${right!'0'}("classFilter",true,"","myfilter${right!'0'}(this,1)","全部",allStuNum);
			}else{
				classHtml=makeFliterSpan${right!'0'}("classFilter",false,"","myfilter${right!'0'}(this,1)","全部",allStuNum);
			}
			if(allStuNum>0){
				var objAtt=makeSortNum(classmap);
				for (var kk=0;kk<objAtt.length;kk++) {
 					var v=objAtt[kk];
 					if(classChoosemap[(v[0])]){
 						//当作选中
 						classHtml=classHtml+makeFliterSpan${right!'0'}("classFilter",true,v[0],"myfilter${right!'0'}(this,1)",classNameMap[(v[0])],v[1]);
 					}else{
 						//不选中
 						classHtml=classHtml+makeFliterSpan${right!'0'}("classFilter",false,v[0],"myfilter${right!'0'}(this,1)",classNameMap[(v[0])],v[1]);
 					}
 				} 
			}
			$("#filterClassId1${right!'0'}").html(classHtml);
			$(".mytable_${right!'0'}").find('.js-popover-filter-class').popover('hide');
			$(".mytable_${right!'0'}").find('.js-popover-filter-subject').popover('show');
			$("#filterSubjectId1${right!'0'}").html("");
			var subjectHtml="";
			if(chooseStuNum==allStuNum){
				subjectHtml=subjectHtml+makeFliterSpan${right!'0'}("subjectFilter",true,"","myfilter${right!'0'}(this,2)","全部",allStuNum);
			}else{
				subjectHtml=subjectHtml+makeFliterSpan${right!'0'}("subjectFilter",false,"","myfilter${right!'0'}(this,2)","全部",allStuNum);
			}
			if(allStuNum>0){
				var objAtt=makeSortNum(subjectmap);
 				for (var kk=0;kk<objAtt.length;kk++) {
 					var v=objAtt[kk];
 					if(subjectChoosemap[(v[0])]){
 						//当作选中
 						subjectHtml=subjectHtml+makeFliterSpan${right!'0'}("subjectFilter",true,v[0],"myfilter${right!'0'}(this,2)",subjectNameMap[v[0]],v[1]);
 					}else{
 						//不选中
 						subjectHtml=subjectHtml+makeFliterSpan${right!'0'}("subjectFilter",false,v[0],"myfilter${right!'0'}(this,2)",subjectNameMap[v[0]],v[1]);
 					}
 				} 
			}
			$("#filterSubjectId1${right!'0'}").html(subjectHtml);
			$(".mytable_${right!'0'}").find('.js-popover-filter-subject').popover('hide');
		}
	}
	
	function makeSortNum(ccmap){
		var mmmap=new Map();
		for (var prop in ccmap) {
			mmmap.set(prop,ccmap[prop]);
		}
		var arrayObj=Array.from(mmmap);
		arrayObj.sort(function(a,b){return b[1]-a[1]});
		return arrayObj;
	}
	
	
	
	function makeFliterSpan${right!'0'}(cname,ccheck,cvalue,cfunction,cfiltername,cnum){
		if(ccheck){
			return '<div><label class="pos-rel"><input type="checkbox" class="wp" name="'+cname+'" checked ' 
		 +' value="'+cvalue+'" onChange="'+cfunction+'"><span class="lbl"> '+cfiltername+'('+cnum+')</span></label></div>';
		}else{
			return '<div><label class="pos-rel"><input type="checkbox" class="wp" name="'+cname+'" ' 
		 +' value="'+cvalue+'" onChange="'+cfunction+'"><span class="lbl"> '+cfiltername+'('+cnum+')</span></label></div>';
		}
		
	}
	
	function updateTableData${right!'0'}(){
		if($(".mytable_${right!'0'}")){
			 var allContent=$(".mytable_${right!'0'} tbody").html();
			 page_dt${right!'0'}.clear();
        	 page_dt${right!'0'}.destroy();
			 $(".mytable_${right!'0'} tbody").append(allContent);
			 loadMyTable${right!'0'}(); 
			 //修改过滤数据
			 updatePovpor${right!'0'}();
		}
		
	}
</script>
