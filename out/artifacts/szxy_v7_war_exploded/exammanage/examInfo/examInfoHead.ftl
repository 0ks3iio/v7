<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">最近30天新增的考试</h4>
			<#if isShowRh>
				<div class="filter-item filter-item-right">
					<a href="javascript:" class="btn btn-white js-sysncyExam " onclick="sysncyExam();">同步阅卷系统考试</a>
				</div>
			</#if>
		
	</div>
	<div class="box-body">
		<div class="filter filter-f16 filter-right">
			<div class="filter-item">
				<a href="javascript:" class="btn btn-white js-addExam" onclick="addExam();">新增考试</a>
			</div>
		</div>
		<div class="table-container no-margin" >
			<div class="table-container-body" id="showList1">
			</div>
		</div>
	</div>
</div>
<div class="box box-default">
	<div class="box-header">
		<h4 class="box-title">30天以前的考试</h4>
	</div>
	<div class="box-body">
		<div class="filter filter-f16">
			<div class="filter-item">
				<span class="filter-name">学年：</span>
				<div class="filter-content">
					<select class="form-control" id="searchAcadyear" name="searchAcadyear" onChange="showList2()">
						<#if acadyearList?exists && (acadyearList?size>0)>
		                    <#list acadyearList as item>
			                     <option value="${item!}" <#if semester.acadyear?default('a')==item?default('b')>selected</#if>>${item!}</option>
		                    </#list>
	                    <#else>
		                    <option value="">未设置</option>
	                     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select class="form-control" id="searchSemester" name="searchSemester" onChange="showList2()">
						${mcodeSetting.getMcodeSelect('DM-XQ',(semester.semester?default(0))?string,'0')}
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">年级：</span>
				<div class="filter-content">
					<select class="form-control" id="searchGradeCode" name="searchGradeCode"  onChange="showList2()">
						<option value="">全部</option>
						<#if gradeList?exists && (gradeList?size>0)>
		                    <#list gradeList as item>
			                     <option value="${item.gradeCode!}">${item.gradeName!}</option>
		                    </#list>
	                     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span  class="filter-name">类型：</span>
				<div class="filter-content">
					<select class="form-control" style="width:180px;" id="searchType" name="searchType" onChange="showList2()">
						<option value="1">本单位设定的考试</option>
	                    <#if unitClass?default(-1) == 2>
	                        <option value="2">直属教育局设定的考试</option>
	                        <option value="3">参与的校校联考</option>
	                    </#if>
					</select>
				</div>
			</div>
		</div>
		<div class="table-container no-margin">
			<div class="table-container-body" id="showList2">
			</div>
		</div>
	</div>
</div>

<!-- page specific plugin scripts -->
<script type="text/javascript">
	$(function(){
		showList1();
		showList2();
	});
	function showList2(){
		 var searchAcadyear = $('#searchAcadyear').val();
	     var searchSemester = $('#searchSemester').val();
	     var searchGradeCode = $('#searchGradeCode').val();
		 var searchType = $('#searchType').val();
		 var str = "?searchAcadyear="+searchAcadyear+"&searchSemester="+searchSemester+"&searchGradeCode="+searchGradeCode+"&searchType="+searchType;
	     var url =  '${request.contextPath}/exammanage/examInfo/list2/page'+str;
		 $("#showList2").load(url);
	}
	function showList1(){
		var url =  '${request.contextPath}/exammanage/examInfo/list1/page';
		$("#showList1").load(url);
	}
	function addExam(){
	   	var url = "${request.contextPath}/exammanage/examInfo/edit/page";
	    indexDiv = layerDivUrl(url,{title: "信息",width:700,height:490});
	}
	function closeDel(){
		isClose = 1;
	}
	function subDel(){
		isClose = 2;
	}
	var isClose = 0;
	function onDel(owm,id){
		$(".modify-name-layer").hide();
		if(isClose == 1){
			$(owm).children(".modify-name-layer").hide();
		}else if(isClose == 0){
			$(owm).children(".modify-name-layer").show();
		}else{
			$.ajax({
                    url:'${request.contextPath}/exammanage/examInfo/delete',
                    data: {'id':id},
                    type:'post',
                    success:function(data) {
                        var jsonO = JSON.parse(data);
                        if(jsonO.success){
                            layer.closeAll();
                            layer.msg(jsonO.msg, {
                                offset: 't',
                                time: 2000
                            });
                            showList1();
                            showList2();
                        }
                        else{
                            layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                            isSubmit=false;
                        }
                        layer.close(ii);
                    },
                    error:function(XMLHttpRequest, textStatus, errorThrown){}
                });
		}
		isClose = 0;
	}
	function doDelete(id){
        var ii = layer.open({
            content: '确认删除吗？'
            ,btn: ['确认', '取消']
            ,yes: function(index, layero){
                $.ajax({
                    url:'${request.contextPath}/exammanage/examInfo/delete',
                    data: {'id':id},
                    type:'post',
                    success:function(data) {
                        var jsonO = JSON.parse(data);
                        if(jsonO.success){
                            layer.closeAll();
                            layer.msg(jsonO.msg, {
                                offset: 't',
                                time: 2000
                            });
                            showList1();
                            showList2();
                        }
                        else{
                            layerTipMsg(jsonO.success,"删除失败",jsonO.msg);
                            isSubmit=false;
                        }
                        layer.close(ii);
                    },
                    error:function(XMLHttpRequest, textStatus, errorThrown){}
                });
            },btn2: function(index, layero){
                layer.close(ii);
            }
            ,cancel: function(){
                layer.close(ii);
            }
        });
	}
	
	function doEdit(id){
	   var url = "${request.contextPath}/exammanage/examInfo/edit/page?id="+id;
	   indexDiv = layerDivUrl(url,{title: "信息",width:750,height:500});
	}
	
	function doSetSubject(examId,isView){
		var url =  '${request.contextPath}/exammanage/subjectClassIndex/index/page?examId='+examId+'&isView='+isView;
		$("#exammanageDiv").load(url);
	}
	<#if isShowRh>
	function sysncyExam(){
		var url =  '${request.contextPath}/exammanage/sysncy/index/page';
		$("#exammanageDiv").load(url);
	}
	</#if>
</script>

