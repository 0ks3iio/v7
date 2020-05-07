<div class="box box-default">
	<div class="box-body">
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">年　　　级：</span>
				<div class="filter-content">
					<select name="gradeId" id="gradeId" class="form-control">
						<#if gradeList?exists && (gradeList?size>0)>
		                    <#list gradeList as item>
			                     <option value="${item.id!}" <#if gradeId?default('a')==item.id?default('b')>selected</#if>>${item.gradeName!}</option>
		                    </#list>
		                <#else>
		                	 <option value=''>暂无年级</option>
		                </#if>
					</select>
				</div>
			</div>
			<div class="filter-item filter-item-right">
				<a href="javascript:" class="btn btn-blue" onclick="toBackTeaching()">返回</a>
			</div>
		</div>
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name">内　　　容：</span>
				<div class="filter-content is-copy">
					<p>
					<label class="pos-rel mr20">
						<input type="radio" class="wp" checked="true" name="isCopySchedule" value="0">
					   	<span class="lbl"> 只复制课程开设</span>
					</label>
					<label class="pos-rel mr20">
						<input type="radio" class="wp" name="isCopySchedule" value="1">
					   	<span class="lbl"> 同时复制课程开设和课程表</span>
					</label>
					<label class="pos-rel">
						<input type="radio" class="wp" name="isCopySchedule" value="2">
					   	<span class="lbl"> 行政班课程表导入</span>
					</label>
					</p>
				</div>
			</div>
		</div>
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name copy-mark">从　　学年：</span>
				<span class="filter-name import-mark" style="display:none">模板　学年：</span>
				<div class="filter-content">
					<select name="srcAcadyear" id="srcAcadyear" class="form-control" style="width:188px;" onchange="changeSrcWeek()">
						<#if acadyearList?exists && acadyearList?size gt 0>
					     	<#list acadyearList as item>
					     		<option value="${item!}" <#if srcAcadyear?default('')== item >selected</#if> >${item!}学年</option>
					     	</#list>
					     <#else>
					     	<option value="">--未设置--</option>
					     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="srcSemester" id="srcSemester" class="form-control" style="width:188px;" onchange="changeSrcWeek()">
						${mcodeSetting.getMcodeSelect('DM-XQ',srcSemester?default('0'),'0')}
					</select>
				</div>
			</div>
			<div class="filter-item import-schedule" style="display:none">
  				<span class="filter-name">周　　次：</span>
  				<div class="filter-content">
  					<select name="srcWeek" id="srcWeek" class="form-control" style="width:188px;">
    				<#if (srcMax??)>
				      <#list 1..srcMax as t>
				      <option value="${t!}" <#if t==srcWeek>selected="selected"</#if>>第${t!}周</option>
				      </#list>
				    <#else>
				        <option value="">暂无数据</option>
				    </#if>
  					</select>
				</div>
          	</div>
		</div>
		<div class="filter">
			<div class="filter-item">
				<span class="filter-name copy-mark">复制到学年：</span>
				<span class="filter-name import-mark" style="display:none">导入到学年：</span>
				<div class="filter-content">
					<select name="destAcadyear" id="destAcadyear" class="form-control" style="width:188px;" onchange="changeDestWeek()">
						<#if acadyearList?exists && acadyearList?size gt 0>
					     	<#list acadyearList as item>
					     		<option value="${item!}" <#if destAcadyear?default('')== item >selected</#if> >${item!}学年</option>
					     	</#list>
					     <#else>
					     	<option value="">--未设置--</option>
					     </#if>
					</select>
				</div>
			</div>
			<div class="filter-item">
				<span class="filter-name">学期：</span>
				<div class="filter-content">
					<select name="destSemester" id="destSemester" class="form-control" style="width:188px;" onchange="changeDestWeek()">
						${mcodeSetting.getMcodeSelect('DM-XQ',destSemester?default('0'),'0')}
					</select>
				</div>
			</div>
			<div class="filter-item import-schedule" style="display:none">
  				<span class="filter-name">起始周次：</span>
  				<div class="filter-content">
  					<select name="destWeek" id="destWeek" class="form-control" style="width:188px;">
    				<#if (destMax??)>
				      <#list 1..destMax as t>
				      <option value="${t!}" <#if t==destWeek>selected="selected"</#if>>第${t!}周</option>
				      </#list>
				    <#else>
				        <option value="">暂无数据</option>
				    </#if>
  					</select>
				</div>
          	</div>
		</div>
		<#--
		<div class="filter copy-schedule" style="display:none">
			<div class="filter-item">
				<span class="filter-name">是否　覆盖：</span>
				<div class="filter-content">
					<select name="isCover" id="isCover" class="form-control">
		                     <option value="1">是</option>
		                     <option value="0" selected>否</option>
					</select>
				</div>
			</div>
		</div>
		-->
		<div class="box box-graybg copy-mark">
			<h4 class='box-graybg-title'>说明</h4>
			<p class="copy-mark">1、复制时会同时复制年级和班级课程开设数据，并清空目标学年学期已开设的年级班级课程<#--和当前时间之后的课程表--></p>
			<p class="copy-schedule" style="display:none">2、<font color="red">起始周次</font>即在复制时只改变起始周次及其之后的课程表</p>
		</div>
		<div class="filter copy-mark">
			<div class="filter-item">
				<a href="javascript:" class="btn btn-blue" onclick="doCopy()">开始复制</a>
			</div>
		</div>
		<div id="importContentDiv" class="import-mark">
		</div>
	</div>
</div>
<script>
	
	$('.is-copy label').click(function(){
		if($(this).index()==0){
			$('.copy-schedule').hide();
			$('.import-schedule').hide();
			$('.copy-mark').show();
			$('.import-mark').hide();
			$("#gradeId").unbind("change");
		}else if($(this).index()==1){
			$('.copy-schedule').show();
			$('.import-schedule').show();
			$('.copy-mark').show();
			$('.import-mark').hide();
			$("#gradeId").unbind("change");
			$("#srcWeek").unbind("change");
			$("#destWeek").unbind("change");
		}else{
			$('.copy-schedule').hide();
			$('.import-schedule').show();
			$('.copy-mark').hide();
			$('.import-mark').show();
			$("#gradeId").bind("change",function(){
				findImportIndex();
			})
			$("#srcWeek").bind("change",function(){
				findImportIndex();
			})
			$("#destWeek").bind("change",function(){
				findImportIndex();
			})
			findImportIndex();
		}
	});
	
	var isSubmit=false;
	function doCopy(){
		if(isSubmit){
			return;
		}
		
		var gradeId=$("#gradeId").val();
		var isCopySchedule=$("input[name=isCopySchedule]:checked").val();
		var srcAcadyear=$("#srcAcadyear").val();
		var srcSemester=$("#srcSemester").val();
		var destAcadyear=$("#destAcadyear").val();
		var destSemester=$("#destSemester").val();
		var srcWeek="";
		var destWeek="";
		if(gradeId==""){
			layer.tips("年级不能为空", "#gradeId", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(srcAcadyear==""){
			layer.tips("学年不能为空", "#srcAcadyear", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(srcSemester==""){
			layer.tips("学期不能为空", "#srcSemester", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(destAcadyear==""){
			layer.tips("学年不能为空", "#destAcadyear", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		if(destSemester==""){
			layer.tips("学期不能为空", "#destSemester", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(isCopySchedule=="1"){
			srcWeek=$("#srcWeek").val();
			destWeek=$("#destWeek").val();
			if(srcWeek==""){
				layer.tips("周次不能为空", "#srcWeek", {
						tipsMore: true,
						tips:3				
					});
				isSubmit=false;
				return;
			}
			if(destWeek==""){
				layer.tips("周次不能为空", "#destWeek", {
						tipsMore: true,
						tips:3				
					});
				isSubmit=false;
				return;
			}
		}
		var srcAS=srcAcadyear+srcSemester;
		var destAS=destAcadyear+destSemester;
		var reg=/[^0-9]/g;
		srcAS=srcAS.replace(reg,"");
		destAS=destAS.replace(reg,"");
		if(parseInt(srcAS)>=parseInt(destAS)){
			layer.tips("只能复制当前学年学期之前的学年学期", "#srcAcadyear", {
					tipsMore: true,
					tips:3				
				});
			layer.tips("只能复制当前学年学期之前的学年学期", "#srcSemester", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		var srcText=$("#srcAcadyear").find("option:selected").text()+$("#srcSemester").find("option:selected").text();
		if(isCopySchedule=="1"){
			srcText+=$("#srcWeek").find("option:selected").text(); 
		}
		var destText=$("#destAcadyear").find("option:selected").text()+$("#destSemester").find("option:selected").text(); 
		layer.confirm('确定将'+srcText+'的课程复制到'+destText+'吗？', function(index){
			layer.closeAll();
			isSubmit=true;
			var ii = layer.load();
			$.ajax({
				url:"${request.contextPath}/basedata/copyCourseSchedule",
				data:{"gradeId":gradeId,"isCopySchedule":isCopySchedule,"srcAcadyear":srcAcadyear,"srcSemester":srcSemester,"srcWeek":srcWeek,"destAcadyear":destAcadyear,"destSemester":destSemester,"destWeek":destWeek},
				type:'post', 
				dataType:'json',
				success:function(data){
					layer.closeAll();
			    	if(data.success){
			    		layer.msg("复制成功！", {
							offset: 't',
							time: 2000
						});
						isSubmit=false;
			        }else{
			         	layerTipMsg(data.success,"失败",data.msg);
			         	isSubmit=false;
			        }
    			}
			});
		});
	}
	
	function changeSrcWeek(){
		var acadyear = $("#srcAcadyear").val();
		var semester = $("#srcSemester").val();
		$.ajax({
			url:"${request.contextPath}/basedata/getWeek/json",
			data:{"acadyear":acadyear,"semester":semester},
			type:'post', 
			dataType:'json',
			async:false,
			success:function(data){
				$('#srcWeek').empty();
				var dataJson = data;
				var max = dataJson.max;
				var week = dataJson.week;
				var sh = '';
				if(max){
					for(var i=1;i<=max;i++){
						sh=sh+('<option value="'+i+'"');
						if(i==week){
							sh=sh+(' selected="selected"');
						}
						sh=sh+('>第'+i+'周</option>');
					}
				}else{
					sh='<option value="">暂无数据</option>';
				}
				$('#srcWeek').html(sh);
			}
		});
		var isCopySchedule=$("input[name=isCopySchedule]:checked").val();
		if(isCopySchedule==2){
			findImportIndex();
		}
	}
	
	function changeDestWeek(){
		var acadyear = $("#destAcadyear").val();
		var semester = $("#destSemester").val();
		$.ajax({
			url:"${request.contextPath}/basedata/getWeek/json",
			data:{"acadyear":acadyear,"semester":semester},
			type:'post', 
			dataType:'json',
			async:false,
			success:function(data){
				$('#destWeek').empty();
				var dataJson = data;
				var max = dataJson.max;
				var week = dataJson.week;
				var sh = '';
				if(max){
					for(var i=1;i<=max;i++){
						sh=sh+('<option value="'+i+'"');
						if(i==week){
							sh=sh+(' selected="selected"');
						}
						sh=sh+('>第'+i+'周</option>');
					}
				}else{
					sh='<option value="">暂无数据</option>';
				}
				$('#destWeek').html(sh);
			}
		});
		var isCopySchedule=$("input[name=isCopySchedule]:checked").val();
		if(isCopySchedule==2){
			findImportIndex();
		}
	}
	
	function findImportIndex(){
		$("#importContentDiv").empty();
		var gradeId=$("#gradeId").val();
		var isCopySchedule=$("input[name=isCopySchedule]:checked").val();
		var srcAcadyear=$("#srcAcadyear").val();
		var srcSemester=$("#srcSemester").val();
		var destAcadyear=$("#destAcadyear").val();
		var destSemester=$("#destSemester").val();
		var srcWeek=$("#srcWeek").val();
		var destWeek=$("#destWeek").val();
		if(gradeId==""){
			layer.tips("年级不能为空", "#gradeId", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(srcAcadyear==""){
			layer.tips("学年不能为空", "#srcAcadyear", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(srcSemester==""){
			layer.tips("学期不能为空", "#srcSemester", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(destAcadyear==""){
			layer.tips("学年不能为空", "#destAcadyear", {
					tipsMore: true,
					tips:3				
				});
			return;
		}
		if(destSemester==""){
			layer.tips("学期不能为空", "#destSemester", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(srcWeek==""){
			layer.tips("周次不能为空", "#srcWeek", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		if(destWeek==""){
			layer.tips("周次不能为空", "#destWeek", {
					tipsMore: true,
					tips:3				
				});
			isSubmit=false;
			return;
		}
		var parmUrl="srcAcadyear="+srcAcadyear+"&srcSemester="+srcSemester+"&gradeId="+gradeId+"&srcWeek="+srcWeek+"&destAcadyear="+destAcadyear+"&destSemester="+destSemester+"&destWeek="+destWeek;
		var url='${request.contextPath}/basedata/classStu/courseScheduleImport/index/page?'+parmUrl;
		$("#importContentDiv").load(url);
	}
	
	function toBackTeaching(){
		var acadyear=$("#destAcadyear").val();
		var semester=$("#destSemester").val();
		var gradeId=$("#gradeId").val();
		var parmUrl="?tabIndex=${tabIndex?default(2)}&acadyear="+acadyear+"&semester="+semester+"&gradeId="+gradeId;
		var url =  '${request.contextPath}/basedata/courseopen/real/index/page'+parmUrl;
		$("#showList").load(url);
	}
</script>