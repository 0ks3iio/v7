<link rel="stylesheet" href="${request.contextPath}/static/css/newcss/iconfont.css" />
<div class="row">
	<div class="col-sm-9">
		<div class="table-container">
			<div class="table-container-body">
				<table class="table table-bordered table-view replace-table js-track  show-del-table myxntable">
					<thead>
						<tr>
							<th class="text-center" width="30"></th>
							<th class="text-center" width="35"></th>
					        <#if (edudays?default(7)>0)>
					        <th class="text-center">周一</th>
					        </#if>
					        <#if (edudays?default(7)>1)>
					        <th class="text-center">周二</th>
					        </#if>
					        <#if (edudays?default(7)>2)>
					        <th class="text-center">周三</th>
					        </#if>
					        <#if (edudays?default(7)>3)>
					        <th class="text-center">周四</th>
					        </#if>
					        <#if (edudays?default(7)>4)>
					        <th class="text-center">周五</th>
					        </#if>
					        <#if (edudays?default(7)>5)>
					        <th class="text-center">周六</th>
					        </#if>
					        <#if (edudays?default(7)>6)>
					        <th class="text-center">周日</th>
					        </#if>
						</tr>
					</thead>
					<tbody>
						<#assign rowIndex=0>
						<#if mm gt 0>
							 <#list 1..mm as t>
							 	 <tr>
						            <#if t==1>
						            <td rowspan="${mm}">早自习</td>
						            </#if>
						            <td>${rowIndex+t}</td>
						            <#list 0..(edudays-1) as d>
						            	<td class="edited" data-value="${d}_1_${t}"></td>
						            </#list>
						          </tr>
							 </#list>
						</#if>
						<#assign rowIndex=rowIndex+mm>
						<#if am gt 0>
							 <#list 1..am as t>
							 	 <tr>
						            <#if t==1>
						            <td rowspan="${am}">上午</td>
						            </#if>
						            <td>${rowIndex+t}</td>
						            <#list 0..(edudays-1) as d>
						            	<td class="edited"  data-value="${d}_2_${t}">
				                         </td>
						            </#list>
						          </tr>
							 </#list>
						</#if>
						<#assign rowIndex=rowIndex+am>
						<#if pm gt 0>
							 <#list 1..pm as t>
							 	 <tr>
						            <#if t==1>
						            <td rowspan="${pm}">下午</td>
						            </#if>
						            <td>${rowIndex+t}</td>
						            <#list 0..(edudays-1) as d>
						            	<td class="edited"  data-value="${d}_3_${t}"></td>
						            </#list>
						          </tr>
							 </#list>
						</#if>
						<#assign rowIndex=rowIndex+pm>
						<#if nm gt 0>
							 <#list 1..nm as t>
							 	 <tr>
						            <#if t==1>
						            <td rowspan="${nm}">晚上</td>
						            </#if>
						            <td>${rowIndex+t}</td>
						            <#list 0..(edudays-1) as d>
						            	<td class="edited"  data-value="${d}_4_${t}">
			
						            	
						            	</td>
						            </#list>
						          </tr>
							 </#list>
						</#if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	
	<div class="col-sm-3" id="errorTable">
		<div class="box box-primary" style="height:500px;overflow:auto;">
			<div class="box-header">
				<h3 class="box-title"><b>冲突记录</b></h3>
			</div>
			<div class="box-body no-padding-top">
				<div class="no-data-container mt20" id="noerrordiv">
					<div class="no-data">
						<span class="no-data-img">
							<img src="${request.contextPath}/static/images/public/nodata6.png" alt="">
						</span>
						<div class="no-data-body">
							<p class="no-data-txt">暂无记录</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	function loadTable(){
		var url="${request.contextPath}/basedata/classhour/list/page?acadyear=${acadyear!}&semester=${semester!}&gradeId=${gradeId!}&hourId=${hourId!}";
		$("#timeTableXn").load(url);
	}

	var length=12;
	var subNameMap={};
	var isSubmit=false;
	$(function(){
		<#if hourExList?exists && hourExList?size gt 0>
			<#list hourExList as hourItem>
				$(".myxntable").find('.edited[data-value="${hourItem.dayOfWeek}_${hourItem.periodInterval}_${hourItem.period}"]').addClass("show-active").append('<span>√</span><i class="sz-icon iconshanchu show-del-icon"></i>');
			</#list>
		</#if>
		<#if hourId?default('')!="">
			
		</#if>
		
		// 删除
		$(".myxntable").on('click','.show-del-icon',function(){
			var active=$(this);
			//删除课程
			var delRemark = '确定删除吗？<br>'+
					'<span class="text-danger">删除课程时间点会删除所有关联的班级对应时间的课表</span>';
			var active = $(this);
			var time1=$(active).parent().attr("data-value");
			layer.confirm(delRemark, {
				btn: ['确定', '取消'],
				yes: function(index){
					$.ajax({
							url:'${request.contextPath}/basedata/classhour/delete',
							data:{'acadyear':'${acadyear!}','semester':'${semester!}','gradeId':'${gradeId!}','hourId':'${hourId!}','time1':time1},
							type:'post', 
							dataType:'json',
							success:function(jsonO){
								layer.close(index);
								if(jsonO.success){
									$(active).parent().html('');
									$(active).removeClass('show-active');
									layer.closeAll();
									isSubmit=false;
									layer.msg("操作成功", {
										offset: 't',
										time: 2000
									});
									
								}else{
									isSubmit=false;
									addErrorTable(jsonO.errorTitle,jsonO.errorHead,jsonO.errorContent);
								}
							}
						});
					
				}
			});
			
			
		})
		
		
		
		
		// 替换--移动到有值的地方
		$(".myxntable").on('click','.edited',function(){
			var active = $(".myxntable").find(".active");
			var len = active.length;
			if (len === 0) {
				//选中某一格
				$(this).addClass("active");
			}else if (len === 1) {
				if ($(this).hasClass("active")) {
					//再次点击之前选中课程
					$(this).removeClass("active");
				} else {
					//选中的其他数据
					var course1="0";
					var time1=$(active).attr("data-value");
					if($(active).hasClass("show-active")){
						course1="1";
					}
					var course2="0";
					var time2=$(this).attr("data-value");
					if($(this).hasClass("show-active")){
						course2="1";
					}
					var other=$(this);
					if(course1==course2){
						if(course1=="0"){
							addErrorTable("同类数据","提示","请选中有安排的时间点");
						}else{
							addErrorTable("同类数据","提示","请选中未安排时间点");
						}
						moveStartState();
					}else{
						if(isSubmit){
							//防止重复
							return;
						}
						isSubmit=true;
						var ii = layer.load();
						$.ajax({
							url:'${request.contextPath}/basedata/classhour/move',
							data:{'acadyear':'${acadyear!}','semester':'${semester!}','gradeId':'${gradeId}','hourId':'${hourId!}','time1':time1,'time2':time2},
							type:'post', 
							dataType:'json',
							success:function(jsonO){
								if(jsonO.success){
									if(course1=="1"){
										//原来有值
										$(active).removeClass("show-active");
										$(other).addClass("show-active");
										$(other).html($(active).html());
										$(active).html("");
									}else{
										$(active).addClass("show-active");
										$(other).removeClass("show-active");
										$(active).html($(other).html());
										$(other).html("");
									}
									
									
									moveStartState();
									layer.closeAll();
									isSubmit=false;
									layer.msg("操作成功", {
										offset: 't',
										time: 2000
									});
									
								}else{
									moveStartState();
									layer.closeAll();
									isSubmit=false;
									addErrorTable(jsonO.errorTitle,jsonO.errorHead,jsonO.errorContent);
								}
							}
						});
					}
				}
			}
		})
	})
	//清除掉选中
	function moveStartState(){
		$(".myxntable").find(".active").removeClass("active");
	}
	
	
	
	
	function delErrorTable(elem){
		$(elem).parents("table").remove();
		var len=$("#errorTable").find(".errorItemTable").length;
		if(len==0){
			$("#noerrordiv").show();
		}
	}
	
	function addErrorTable(title,itemHead,content){
		var htmltable='<table width="100%" class="box-line mt10 errorItemTable" >'
					+'<tr><td colspan="2"><p class="clearfix"><b class="float-left">● '+title+'('+makeNowTime()+')</b>'
					+'<a class="float-right" href="javascript:" onclick="delErrorTable(this)"><i class="glyphicon glyphicon-trash color-blue"></i></a>'
					+'</p></td></tr>'
					+'<tr><td width="80" valign="top"><p class="color-grey font-12">'+itemHead+'：</p></td><td><p class="color-grey font-12">'+content+'</p></td>'
					+'</tr></table>';
				
		$("#noerrordiv").before(htmltable);
		$("#noerrordiv").hide();
		
	}
	
	function makeNowTime(){
		var date = new Date();
		var seperator1 = "-";
    	var seperator2 = ":";
    	var month = date.getMonth() + 1;
   		var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    var time1=+date.getFullYear() + seperator1 + month + seperator1 + strDate;
	    var hours=date.getHours();
	    if(hours>=1 && hours<=9){
	    	hours="0"+hours;
	    }
	    var minutes=date.getMinutes();
	    if(minutes>=0 && minutes<=9){
	    	minutes="0"+minutes;
	    }
	    var seconds=date.getSeconds();
	    if(seconds>=0 && seconds<=9){
	    	seconds="0"+seconds;
	    }
   		var time2=hours + seperator2 + minutes + seperator2 + seconds;
    	return time1+" "+time2;
	}
	
	
	
</script>