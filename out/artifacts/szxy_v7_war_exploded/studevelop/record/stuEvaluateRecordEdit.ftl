<link rel="stylesheet" href="${request.contextPath}/static/components/chosen/chosen.min.css">
<link rel="stylesheet" href="${request.contextPath}/static/components/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css">
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<script>
	$(function(){
	    //实例化编辑器
	    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	    var ue = UE.getEditor('teacherEvalContent',{
	        //focus时自动清空初始化时的内容
	        autoClearinitialContent:false,
	        //关闭字数统计
	        wordCount:false,
	        //关闭elementPath
	        elementPathEnabled:false,
	        //默认的编辑区域高度
	        toolbars:[[
	        	'fullscreen', 'source','|', 'undo', 'redo'
	         <#--'fullscreen', 'source', '|', 'undo', 'redo', '|',
	         'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
	         'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
	         'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
	         'directionalityltr', 'directionalityrtl', 'indent', '|',
	         'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
	         'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
	         'horizontal', 'date', 'time'-->
	     	]],
	        initialFrameHeight:200
	        //更多其他参数，请参考ueditor.config.js中的配置项
	    });
		    ue.ready(function() {
				ue.execCommand( 'bold' );
				ue.execCommand( 'fontsize', '18px' );
				ue.execCommand( 'fontfamily', '楷体' );
				ue.execCommand( 'lineheight', '1.75' );
			});
	});
</script>
<title>学生评价登记详情</title>
<div class="layer layer-addTerm layer-change" style="display:block;" id="myDiv">
	<form id="subForm">
	<input type="hidden" name="id" id="id" value="${stuEvaluateRecord.id!}">
	<input type="hidden" name="studentId" id="studentId" value="${stuEvaluateRecord.studentId!}">
	<input type="hidden" name="acadyear" id="acadyear" value="${stuEvaluateRecord.acadyear!}">
	<input type="hidden" name="semester" id="semester" value="${stuEvaluateRecord.semester!}">
	<input type="hidden" name="parentEvalContent" id="parentEvalContent" value="${stuEvaluateRecord.parentEvalContent!}">
	<input type="hidden" name="stuHonorContent" id="stuHonorContent" value="${stuEvaluateRecord.stuHonorContent!}">
	<input type="hidden" name="stuGatherContent" id="stuGatherContent" value="${stuEvaluateRecord.stuGatherContent!}">
	<input type="hidden" name="stuWishContent" id="stuWishContent" value="${stuEvaluateRecord.stuWishContent!}">
		<div class="layer-body">
			<div class="filter clearfix">
					<div class="filter clearfix">
						<table class="table table-bordered table-striped table-hover no-margin">
			 <tr>
				<th width="20%"><span style="color:red">*</span>评语等级：</th>
				<td>
					${mcodeSetting.getMcodeRadio('DM-PYDJLB',((stuEvaluateRecord.evaluateLevel)?default(''))?string,'evaluateLevel')}
				</td>
			  </tr>
				<tr>
					<th><span style="color:red">*</span>个性特点：</th>
					<td width="" height="50"><textarea   style="width:435px;" id="strong"  maxLength="100" rows="7" cols="64" nullable="false" >${stuEvaluateRecord.strong!}</textarea></td>
				</tr>
				<tr>
					<th>兴趣爱好：</th>
					<td width="" height="50"><textarea   style="width:435px;" id="hobby"  maxLength="100" rows="7" cols="64" >${stuEvaluateRecord.hobby!}</textarea></td>
				</tr>
			  <tr>
				  <th><span style="color:red">*</span>老师寄语：</th>
				  <td width="" height="50">
					  <#--<textarea class="teacherEvalContent"  style="width:435px;" id="teacherEvalContent" name="teacherEvalContent" maxLength="360" rows="7" cols="64" nullable="false" >${stuEvaluateRecord.teacherEvalContent!}</textarea>-->
					   <textarea id="teacherEvalContent"  maxLength="560"  nullable="false" type="text/plain" style="width:100%;height:360px;">${stuEvaluateRecord.teacherEvalContent!}</textarea>
				  </td>
			  </tr>
			  	<tr>
                    <td colspan="2" align="center"> <a class="btn btn-blue" id="arrange-commit">保存</a>
                    </td>
                </tr>
				</table>
				</div>
			</div>
		</div>
	</form>
</div>

<script>
	$(function(){
		var isSubmit = false;
		
		$("#arrange-close").on("click", function(){
    		changeStuId();    
 		});
 		
 		$("#arrange-commit").on("click", function(){
 			if(isSubmit){
				return;
			} 	
 			var evaluateLevel = $('input:radio[name="evaluateLevel"]:checked').val();
			if(evaluateLevel==null){
//				layerTipMsg(false,"评语等级不能为空!","");
                layerTipMsgWarn("评语等级","不可为空");

				return;
			}
            var strong = $("#strong").val();
            var hobby = $("#hobby").val();
            if(strong == ""){
//                layerTipMsgWarn("个性特点","不可为空");
                layer.tips("个性特点不可为空", "#strong", {
                    tipsMore: true,
                    tips:3
                });
                return false;
            }
            var teacherEvalContent = UE.getEditor('teacherEvalContent').getContentTxt();
            if(!teacherEvalContent||teacherEvalContent==''){
                layer.tips("老师寄语不可为空", "#teacherEvalContent", {
                    tipsMore: true,
                    tips:3
                });
                return false;
            }
            var teacherEvalContentStyle = UE.getEditor('teacherEvalContent').getContent();
            if(teacherEvalContent.length > 270){
                layerTipMsgWarn("老师寄语","长度不能超过270字符");
                return false;
            }
            var styleL=teacherEvalContentStyle.length-teacherEvalContent.length;
            if(styleL+teacherEvalContent.length*2 >2500){
            	layerTipMsgWarn("老师寄语","样式长度不能超过2500字符");
                return false;
            }
           <#--  var myContent='<p style="line-height: 1.75em;"><strong><span style="font-size: 18px; font-family: 楷体;">'+teacherEvalContent;
			myContent+='</span></strong><br/></p>';
			UE.getEditor('teacherEvalContent').setContent(myContent);
			debugger;-->
			isSubmit = true;
			var check = checkValue('#subForm');
			if(!check){
				isSubmit = false;
				return;
			}
			var classId=$("#classId").val();
			var isAdmin=$("#isAdmin").val();
			var options = {
				url : "${request.contextPath}/studevelop/evaluateRecord/save.action?"+jQuery("#subForm").serialize(),
				data:{'classId':classId,'isAdmin':isAdmin,'strong':strong,'hobby':hobby,'teacherEvalContent':teacherEvalContentStyle},
				dataType : 'json',
				success : function(data){
		 			var jsonO = data;
			 		if(!jsonO.success){
			 			layerTipMsg(jsonO.success,"保存失败",jsonO.msg);
			 			$("#arrange-commit").removeClass("disabled");
                        isSubmit = false;
			 			return;
			 		}else{
			 			layer.closeAll();
						layer.msg(jsonO.msg, {
							offset: 't',
							time: 2000
						});
						isSubmit = true;
                        doSearch(6);
	    			}

				},
				clearForm : false,
				resetForm : false,
				type : 'post',
				error:function(XMLHttpRequest, textStatus, errorThrown){}//请求出错 
			};
//			$("#subForm").ajaxSubmit(options);
			jQuery.ajax(options);
 		});
	});
</script>