<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<form id="myform">
<div class="box box-default">
    <div class="box-header">
        <h4 class="box-title">
        	${dto.gsaEnt.arrangeName!}
        </h4>
    </div>
    <div class="box-body">
    	 <div class="filter filter-f16">
    	 	<div class="filter-item">
                <span class="filter-name">学生选课：</span>
                <div class="filter-content">
                	<#if courseList?exists && courseList?size gt 0>
						<#list courseList as c>
						<label>
							<input name="subjectIds" <#if gkxkMap?size=0 || gkxkMap[c.id]?exists> checked </#if> type="checkbox" class="wp" value="${c.id!}" <#if dto.gsaEnt.isUsing == 1 || dto.stuXuanKe!>disabled</#if>>
							<span class="lbl">${c.subjectName!}&nbsp;&nbsp;</span>
						</label>
						</#list>
						
						<em>(浙江实行"7选3"模式，其他地区实行"6选3"模式，根据地区自行设置)</em>
						</#if>
				</div>
              </div>
        	</div>
        	<div class="filter filter-f16">
            <div class="filter-item">
                <span class="filter-name">学生选课数：</span>
                <div class="filter-content">
                	<input type="text" class="form-control" readonly="true" name="gsaEnt.subjectNum" id="subjectNum" value="${dto.gsaEnt.subjectNum?default(3)}">
                </div>
            </div>
            <div class="filter-item">
                <span class="filter-name"><font style="color:red;">*</font>选课开始时间：</span>
                <div class="filter-content">
					<div class="input-group">
						<input class="form-control date-picker" <#if dto.gsaEnt.isUsing == 1>readonly="true"</#if> vtype="data" style="width: 150px" type="text" nullable="false" name="gsaEnt.startTime" id="startTime" placeholder="截止时间" value="${(dto.gsaEnt.startTime?string('yyyy-MM-dd HH:mm'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					</div>
				</div>
            </div>
            <div class="filter-item">
                <span class="filter-name"><font style="color:red;">*</font>选课截止时间：</span>
                <div class="filter-content">
					<div class="input-group">
						<input class="form-control date-picker" <#if dto.gsaEnt.isUsing == 1>readonly="true"</#if> vtype="data" style="width: 150px" type="text" nullable="false" name="gsaEnt.limitedTime" id="limitedTime" placeholder="截止时间" value="${(dto.gsaEnt.limitedTime?string('yyyy-MM-dd HH:mm'))!}">
						<span class="input-group-addon">
							<i class="fa fa-calendar bigger-110"></i>
						</span>
					</div>
				</div>
            </div>
        </div>
        <h3>通告设置 <a href="javascript:" class="btn btn-blue btn-sm pull-right" style='margin-left:5px;' onclick="doSetNotice();">参考公告</a><a href="javascript:" class="btn btn-blue btn-sm pull-right" style='margin-left:5px;' onclick="selectSet();">限选设置</a></h3>
		<p>
			<input type="hidden" id="notice" name="gsaEnt.notice" value=""/>
			<textarea id="noticediv" name="noticediv" type="text/plain" style="width:100%;height:500px;">${dto.gsaEnt.notice!}</textarea>
		</p>
		
        <div class="text-right">
        <input type="hidden" id="isUsing" name="gsaEnt.isUsing" value="${dto.gsaEnt.isUsing!}"/>
        	<#if dto.gsaEnt.isUsing==0>
           	 	<a href="javascript:" class="btn btn-blue" onclick="doGoClassSave(0);"><#if dto.gsaEnt.limitedTime?exists>修改<#else>保存</#if></a>
           	 	<a href="javascript:" class="btn btn-blue" onclick="doGoClassSave(1);">发布</a>
            </#if>
            <#if dto.gsaEnt.isUsing==1>
            	<#if dto.gsaEnt.isLock == 0>
            		<a href="javascript:" class="btn btn-blue" onclick="doGoClassSave(0);">取消发布</a>
            		<#else>
            		<em>温馨提示：开班开课流程正在进行中不能修改，需流程走完之后才能再次设置</em>
            	</#if>
            	<a href="javascript:" class="btn btn-blue" onclick="doSelResu();">去参考成绩设置</a>
            </#if>
        </div>
    </div>
</div>
</form>
<script>
	//实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
    var ue = UE.getEditor('noticediv',{
        //focus时自动清空初始化时的内容
        autoClearinitialContent:false,
        //关闭字数统计
        wordCount:false,
        //关闭elementPath
        elementPathEnabled:false,
        //默认的编辑区域高度
        toolbars:[[
	         'fullscreen', 'source', '|', 'undo', 'redo', '|',
	         'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
	         'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
	         'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
	         'directionalityltr', 'directionalityrtl', 'indent', '|',
	         'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
	         'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
	         'horizontal', 'date', 'time', '|',
	         'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts'
	     	]],
        initialFrameHeight:300
        //更多其他参数，请参考ueditor.config.js中的配置项
    });

	$(function(){
		// #############提示工具#############
		$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});
		//初始化日期控件
		var viewContent={
			'format' : 'yyyy-mm-dd hh:ii',
			'minView' : '0'
		};
		initCalendarData("#myform",".date-picker",viewContent);
		
	});
	
	function CompareDate(d1,d2){
	  return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
	}
	
	function doSelResu(){
		url =  '${request.contextPath}/gkelective/${arrangeId}/basisSet/index/page';
		$("#showList").load(url);
	}
	
	function selectSet() {
		url =  '${request.contextPath}/gkelective/${arrangeId}/selectSet/List/page';
		$("#showList").load(url);
	}
	
	function doSetNotice(){
		
		showConfirmMsg('使用参考公告会对现有的公告进行替换，建议对公告进行微调，保存修改后生效','提示',function(ii){
			var notticeContext="<p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:44px'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'><span style='font-family:仿宋'>浙</span><span style='font-family:仿宋'>江省新的高考改革方案早</span><span style='font-family:仿宋'>已出台（浙江教育考试网</span></span><a href='http://www.zjzs.net/'><span style='font-family: 仿宋;color: rgb(0, 0, 0);letter-spacing: 0;font-size: 16px'>www.zjzs.net</span></a><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'><span style='font-family:仿宋'>有发布），在仔细了解政策、理解学校所做解读的基础上，请根据学生自己个人情况确定</span>&quot;7选3&quot;的初步意向。学校要求班主任、任课老师接受家长和学生的咨询，请学生自主选择、慎重选择。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;color: rgb(255, 0, 0);letter-spacing: 0;font-size: 16px'><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;color: rgb(255, 0, 0);letter-spacing: 0;font-size: 16px'><span style='font-family:仿宋'>注意：</span></span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>①每位同学必须选择三门课程，且只能选择三门课程，否则无法提交。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>②本次选择将作为下学期学校开课的依据，请各位学生根据自身情况认真选择。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>③本次选择不作为学生高考选考课程的最终依据，本次仅为预选。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>④使用中如遇浏览器兼容性原因，建议使用谷歌浏览器，或360极速模式。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>⑤非选课时间学生无法查看相应信息，老师发布选课项目后，学生才能看到选课信息。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>⑥学生在选课截止时间之前，可以操作选课，并且可以修改再提交，一旦超过选课截止时间，学生无法继续操作。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>⑦学生个人信息和密码暂不支持修改。</span></p><p><br/></p>";
			UE.getEditor('noticediv').setContent(notticeContext);
			layer.close(ii);
		});
		
	}
	
	var isSubmit=false;
	function doGoClassSave(type){
		if(isSubmit){
			return;
		}
		isSubmit=true;
		var checkVal = checkValue('#myform');
		if(!checkVal){
		 	isSubmit=false;
		 	return;
		}
		if(CompareDate($("#startTime").val(),$("#limitedTime").val())){
			layer.alert('选课截止时间必须大于选课开始时间，请修改！',{icon:7});
			isSubmit=false;
		 	return;
		}
		var notice = UE.getEditor('noticediv').getContent();
		if(notice!=""){
			$("#notice").val(notice);
		}
		
		$("#isUsing").val(type);
		// 提交数据
		var ii = layer.load();
		var options = {
			url : '${request.contextPath}/gkelective/${arrangeId}/notice/save',
			dataType : 'json',
			success : function(data){
		 		if(data.success){
		 			layer.closeAll();
					layerTipMsg(data.success,"成功",data.msg);
				  	itemShowList();
		 		}
		 		else{
		 			layerTipMsg(data.success,"失败",data.msg);
		 			$("#arrange-commit").removeClass("disabled");
		 			isSubmit=false;
				}
				layer.close(ii);
			},
			clearForm : false,
			resetForm : false,
			type : 'post',
			error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
		};
		$("#myform").ajaxSubmit(options);
	}
</script>
