<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${request.contextPath}/static/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/layer/layer.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/moment/min/moment-with-locales.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/js/bootstrap-datetimepicker.js"></script>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/default/css/ueditor.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/ueditor/themes/iframe.css"/>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/static/components/bootstrap-datetimepicker-4.17/css/bootstrap-datetimepicker.min.css">
<style>
	.datetimepicker {
		margin-top: auto;
	}

	.curricula-title {
		text-align: center;
		font-size: 18px;
		color: #000000;
		margin-bottom: 10px;
		line-height: 25px;
	}

	.curricula-tip {
		font-size: 14px;
		color: #666666;
		text-align: center;
		line-height: 20px;
		margin-bottom: 16px;
	}
</style>

<script>
var oldName = "${choiceName!}";
var pass = true;
var checkMsg;
function modiName(obj,val){
	var newName = $.trim(val);
	if(newName == oldName){
		pass = true;
		return;
	}
	if(newName==''){
		layer.tips('不能为空',$(obj), {
				tipsMore: true,
				tips: 3
			});
		pass = false;
		checkMsg = '不能为空';
		return;	
	}
	if(getLength(newName)>50){
		layer.tips('名称内容不能超过50个字节（一个汉字为两个字节）！',$(obj), {
				tipsMore: true,
				tips: 3
			});
		pass = false;
		checkMsg = '名称内容不能超过50个字节（一个汉字为两个字节）！';
		return;	
	}
	
	console.log(oldName+" : "+newName);
	
	var id = <#if newGkChoiceDto.choiceId?exists>'${newGkChoiceDto.choiceId!}'<#else>"create"</#if>;
	$.ajax({
		url:'${request.contextPath}/newgkelective/choice/'+id+'/saveName',
		data: {'gradeId':'${gradeId!}','choiceName':newName},
		type:'post',
		success:function(data) {
			var jsonO = JSON.parse(data);
	 		if(jsonO.success){
	 			layer.closeAll();
			  	layer.msg(jsonO.msg, {
						offset: 't',
						time: 2000
					});
				oldName = newName;
				pass = true;
	 		}else{
	 			//obj.value=oldName;
	 			layer.tips(jsonO.msg,$(obj), {
					tipsMore: true,
					tips: 3
				});
				pass = false;
				checkMsg = jsonO.msg;
			}
		},
 		error:function(XMLHttpRequest, textStatus, errorThrown){}
	});
}

</script>
<div class="stepsContainer">
	<ul class="steps-default clearfix">
		<li class="step_label step_label_1 active">
			<span><i>1</i>基本设置</span>
		</li>
		<li class="step_label step_label_2">
			<span><i>2</i>科目设置</span>
		</li>
		<li class="step_label step_label_3">
			<span><i>3</i>参考成绩设置</span>
		</li>
		<li class="step_label step_label_4">
			<span><i>4</i>公告设置</span>
		</li>
	</ul>
</div>
<form id="myform">
<div class="box box-default">
	<div class="box-body">
		<div class="form-horizontal step_switch step_switch_1" role="form">
			<#--<div class="form-group">
				<label class="col-sm-2 control-title no-padding-right"><span class="form-title">基本设置</span></label>
			</div>-->
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">选课名称：</label>
				<div class="col-sm-3">
					<input style="" type="text" onblur="modiName(this,this.value)" name="choiceName" id="choiceName" nullable="false" class="form-control" value="${choiceName!}">
				</div>
			</div>
			<div class="form-group">
				<input type="hidden" name="choiceId" value="${newGkChoiceDto.choiceId!}">
				<input type="hidden" name="gradeId" value="${newGkChoiceDto.gradeId}">
				<label class="col-sm-3 control-label no-padding-right">学生选课数：</label>
				<div class="col-sm-3">
					<input type="text" name="chooseNum" id="chooseNum" vtype="int" maxlength="1" nullable="false" value="${newGkChoiceDto.chooseNum?default(3)}" class="form-control" readonly="true" placeholder="请输入选课数">
				</div>
				<div class="col-sm-4 control-tips"></div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">选课时间：</label>
				<div class="col-sm-6">
					
					<div class="input-group float-left" style="width: 40%;">
						<input id="startTime" autocomplete="off" name="startTime" class="form-control datetimepicker" type="text" nullable="false"  placeholder="开始时间" value="${(newGkChoiceDto.startTime?string('yyyy-MM-dd HH:mm:ss'))!}" />
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
					<span class="float-left mt7 mr10 ml10"> 至 </span>
					<div class="input-group float-left" style="width: 40%;">
						<input id="limitedTime" autocomplete="off" name="endTime" class="form-control datetimepicker" type="text" nullable="false"  placeholder="结束时间" value="${(newGkChoiceDto.endTime?string('yyyy-MM-dd HH:mm:ss'))!}" />
						<span class="input-group-addon">
							<i class="fa fa-calendar"></i>
						</span>
					</div>
				</div>
				<div class="col-sm-4 control-tips"></div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">是否显示同选学生数：</label>
				<div class="col-sm-6">
					<p class="js-explain">
						<label class="inline">
							<input type="radio" class="wp" <#if newGkChoiceDto.showSamesele?default(0)==0>checked="checked"</#if> name="showSamesele" value="0"/>
							<span class="lbl"> 不显示</span>
						</label>
						<label class="inline">
							<input type="radio" class="wp" <#if newGkChoiceDto.showSamesele?default(0)==1>checked="checked"</#if> name="showSamesele" value="1"/>
							<span class="lbl"> 显示</span>
						</label>
					</p>
				</div>
			</div>
            <div class="form-group">
                <label class="col-sm-3 control-label no-padding-right">选课结果是否开放：</label>
                <div class="col-sm-6">
                    <p class="js-explain">
                        <label class="inline">
                            <input type="radio" class="wp" <#if newGkChoiceDto.statShow?default(0)==0>checked="checked"</#if> name="statShow" value="0"/>
                            <span class="lbl"> 不公开</span>
                        </label>
                        <label class="inline">
                            <input type="radio" class="wp" <#if newGkChoiceDto.statShow?default(0)==1>checked="checked"</#if> name="statShow" value="1"/>
                            <span class="lbl"> 公开</span>
                        </label>
                    </p>
                </div>
            </div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">选课结果是否提示：</label>
				<div class="col-sm-6">
					<p class="js-explain js-notice">
						<label class="inline">
							<input type="radio" class="wp" <#if newGkChoiceDto.showNum?default(-1)==-1>checked="checked"</#if> name="noticeShow" value="0"/>
							<span class="lbl"> 不提示</span>
						</label>
						<label class="inline">
							<input id="noticeShowId" type="radio" class="wp" <#if newGkChoiceDto.showNum?default(-1) gt -1>checked="checked"</#if> name="noticeShow" value="1"/>
							<span class="lbl"> 提示</span>
						</label>
					</p>
					<div class="promptContainer" <#if !(newGkChoiceDto.showNum?default(-1) gt -1)>style="display: none;"</#if>>
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding">提示条件：</label>
							<div class="col-sm-9">
								<input id="showNumId" type="text" vtype="int" maxlength="3" min="0" class="form-control inline-block input-sm no-margin" name="showNum" style="width:60px;" value="${newGkChoiceDto.showNum!}"> 人以下的选课组合进行提示。
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding">提示开始时间：</label>
							<div class="col-sm-4">
								<div class="input-group">
									<input id="showTimeId" autocomplete="off" type="text" class="form-control datetimepicker" name="showTime" value="${(newGkChoiceDto.showTime?string('yyyy-MM-dd HH:mm:ss'))!}">
									<span class="input-group-addon">
										<i class="fa fa-calendar"></i>
									</span>
								</div>
							</div>
							<div class="col-sm-5 color-grey control-tips"><span class="glyphicon glyphicon-exclamation-sign color-yellow"></span> 建议设为选课截止时间前两天</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label no-padding">提示文字：</label>
							<div class="col-sm-4">
								<input id="hintContentId" type="text" class="form-control inline-block no-margin" name="hintContent" value="${newGkChoiceDto.hintContent?default('当前已选组合人数偏少，建议调整！')}">
							</div>
							<div class="col-sm-5 color-grey control-tips"><span class="glyphicon glyphicon-exclamation-sign color-yellow"></span> 建议字数在20个以内</div>
						</div>
					</div>
				</div>
				<div class="col-sm-3 control-tips"></div>
			</div>
		</div>
		<div class="form-horizontal step_switch step_switch_2" role="form" style="display: none">
			<#--<div class="form-group">
				<label class="col-sm-2 control-title no-padding-right"><span class="form-title">科目设置</span></label>
			</div>-->
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">选课科目：</label>
				<div class="col-sm-9">
                    <button type="button" class="btn btn-blue margin-b-10 <#if !canChange?default(true)>disabled</#if>" <#if canChange?default(true)>onclick="defaultChoiceSwitch(0);"</#if>>六选三</button>
                    <#if isZheJiang?default(false)><button type="button" class="btn btn-blue margin-b-10 <#if !canChange?default(true)>disabled</#if>" <#if canChange?default(true)>onclick="defaultChoiceSwitch(1);"</#if>>七选三</button></#if>
                    <button type="button" class="btn btn-blue margin-b-10 <#if !canChange?default(true)>disabled</#if>" <#if canChange?default(true)>onclick="defaultChoiceSwitch(2);" title="物理、历史二选一，化学、生物、政治、地理四选二"</#if>>3+1+2</button>
                    <button type="button" class="btn btn-blue margin-b-10 <#if !canChange?default(true)>disabled</#if>" <#if canChange?default(true)>onclick="defaultChoiceSwitch(3);" title="物理化学、政治历史两个组合二选一，其余一课从另外四门中任选"</#if>>3+2+1</button>
					<button type="button" class="btn btn-blue margin-b-10 js-edit <#if !canChange?default(true)>disabled</#if>">自定义</button>

                    <div class="class-array">
                        <#if newGkChoiceDto.courseCategoryDtoList?exists && newGkChoiceDto.courseCategoryDtoList?size gt 0>
                            <#list newGkChoiceDto.courseCategoryDtoList as item>
                                <p>
                                    <span class="font-16">
                                        <input type="hidden" name="courseCategoryDtoList[${item_index}].id" value="${item.id!}">
										<input type="hidden" name="courseCategoryDtoList[${item_index}].orderId" value="${item.orderId!}">
                                        <input type="hidden" name="courseCategoryDtoList[${item_index}].categoryName" value="${item.categoryName!}">
                                        <b>${item.categoryName!}：</b>
                                    </span>
                                    <img src="${request.contextPath}/static/images/7choose3/icon-warning.png" width="14" height="14">
                                    <span class="color-999">
                                        <input type="hidden" name="courseCategoryDtoList[${item_index}].maxNum" value="${item.maxNum?default(3)}">
                                        <input type="hidden" name="courseCategoryDtoList[${item_index}].minNum" value="${item.minNum?default(0)}">
                                        &nbsp;最多选${item.maxNum?default(3)}组/门，最少选${item.minNum?default(0)}组/门
                                    </span>
                                </p>
                                <div class="publish-course-just-show no-padding-top">
                                    <#if item.courseList?exists && item.courseList?size gt 0>
							        <#list item.courseList as course>
                                        <input type="hidden" name="courseCategoryDtoList[${item_index}].courseList[${course_index}].id" value="${course.id}">
                                        <span shortName="${course.shortName}" courseId="${course.id}" class="subject">${course.subjectName}</span>
                                    </#list>
                                    </#if>
									<#if item.courseCombination?exists && item.courseCombination?size gt 0>
										<#list item.courseCombination as combination>
                                            <input type="hidden" name="courseCategoryDtoList[${item_index}].courseCombination[${combination_index}].id" value="${combination.id}">
                                            <input type="hidden" name="courseCategoryDtoList[${item_index}].courseCombination[${combination_index}].categoryName" value="${combination.categoryName}">
											<input type="hidden" name="courseCategoryDtoList[${item_index}].courseCombination[${combination_index}].courseList[0].id" value="${combination.courseList[0].id}">
											<input type="hidden" name="courseCategoryDtoList[${item_index}].courseCombination[${combination_index}].courseList[1].id" value="${combination.courseList[1].id}">
											<span shortName="${combination.categoryName}" courseId="${combination.courseList[0].id},${combination.courseList[1].id}" class="subject">${combination.categoryName}</span>
										</#list>
									</#if>
                                </div>
                            </#list>
                        </#if>
                    </div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">推荐组合：</label>
				<div class="col-sm-9">
					<div class="publish-course-g recommend-list">
						<#if newGkChoiceDto.recommendList?exists && (newGkChoiceDto.recommendList?size>0)>
							<#list newGkChoiceDto.recommendList as item>
								<input type="hidden" name="recommendList[${item_index}].ids" value="${item.ids}">
								<span>${item.shortNames}</span>
							</#list>
						</#if>
						<a href="javascript:void(0)" onclick="alertList(1,'${newGkChoiceDto.choiceId!}')">修改</a>
					</div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label no-padding-right">不推荐组合：</label>
				<div class="col-sm-9">
					<div class="publish-course-g ban-list">
						<#if newGkChoiceDto.banCourseList?exists && (newGkChoiceDto.banCourseList?size>0)>
							<#list newGkChoiceDto.banCourseList as item>
								<input type="hidden" name="banCourseList[${item_index}].ids" value="${item.ids}">
								<span>${item.shortNames}</span>
							</#list>
						</#if>
						<a href="javascript:void(0)" onclick="alertList(2,'${newGkChoiceDto.choiceId!}')">修改</a>
					</div>
				</div>
			</div>
		</div>
		<div class="form-horizontal step_switch step_switch_3" role="form" style="display: none">
			<div class="curricula-title">请选择一份考试成绩</div>
			<div class="curricula-tip">
				学生可根据自身成绩的排名，更好的选择适合自己的科目
			</div>
			<div class="form-group">
				<input id='refer-score-id' type="hidden" name="referScoreId" value="${newGkChoiceDto.referScoreId!}">
				<label class="col-sm-2 control-label no-padding-right"></label>
				<ul class="courseList clearfix">
					<#if referScoreList?exists && referScoreList?size gt 0>
						<#list referScoreList as referScore>
							<li class="referScoreIdClass <#if newGkChoiceDto.referScoreId?default("") == referScore.id>active</#if>" referScoreId="${referScore.id}">
								<div class="title score"> ${referScore.name}</div>
							</li>
						</#list>
					</#if>
				</ul>
			</div>
		</div>
		<div class="form-horizontal step_switch step_switch_4" role="form" style="display: none">
			<#--<div class="form-group">
				<label class="col-sm-2 control-title no-padding-right"><span class="form-title">公告设置</span></label>
			</div>-->
			<div class="form-group">
				<label class="col-sm-2 control-label no-padding-right">编辑内容：<br><a href="javascript:" class="btn btn-sm btn-white" onclick="setNotice();">参考公告</a>
				</label>
				<div class="col-sm-9" style="z-index:900;">
					<input type="hidden" id="notice" name="notice" value=""/>
					<textarea id="noticediv" >${newGkChoiceDto.notice!}</textarea>
				</div>
			</div>
			<br>
		</div>
	</div>
</div>
</form>
<div class="navbar-fixed-bottom opt-bottom step_bottom step_bottom_1">
	<a href="javascript:void(0)" class="btn btn-blue" onclick="stepSwitch(1, 2);">下一步</a>
</div>
<div class="navbar-fixed-bottom opt-bottom step_bottom step_bottom_2" style="display: none">
	<a href="javascript:void(0)" class="btn btn-blue" onclick="stepSwitch(2, 1);">上一步</a>
	<a href="javascript:void(0)" class="btn btn-blue" onclick="stepSwitch(2, 3);">下一步</a>
</div>
<div class="navbar-fixed-bottom opt-bottom step_bottom step_bottom_3" style="display: none">
	<a href="javascript:void(0)" class="btn btn-blue" onclick="stepSwitch(3, 2);">上一步</a>
	<a href="javascript:void(0)" class="btn btn-blue" onclick="stepSwitch(3, 4);">下一步</a>
</div>
<div class="navbar-fixed-bottom opt-bottom step_bottom step_bottom_4" style="display: none">
	<a href="javascript:void(0)" class="btn btn-blue" onclick="stepSwitch(4, 3);">上一步</a>
    <a href="javascript:void(0)" class="btn btn-blue" onclick="doChoiceSave();">发布</a>
</div>

<div class="layer layer-edit">
    <div class="layer-content">
        <div id="revertContent">
            <div class="row clearfix margin-b-20 padding-l-10">
                <div class="col-sm-5 left-course course-class chose-panel">
                    <p class=" margin-b-20 padding-l-10"><b>所有科目 </b><span class="color-999"> 拖动至右侧选择框</span></p>
                    <div class="publish-course publish-course-made js-click-move">
                        <#if allCourse?exists && allCourse?size gt 0>
                            <#list allCourse as item>
                                <span courseName="${item.subjectName}" shortName="${item.shortName}" courseId="${item.id}">${item.subjectName}</span>
                            </#list>
                        </#if>
                    </div>
                    <div class="merge-outter clearfix">
                        <div class="merge-inner publish-course publish-course-remove js-click-move">
                        </div>
                        <a href="#" class="btn btn-blue btn-lg merge-btn js-merge">合并</a>
                    </div>
                </div>

                <div class="col-sm-7 all-course float-left">
                    <button class="btn btn-default margin-b-10 js-add-new" onclick="addNewCourseCategory(this)">新增类别</button>
                    <div class="all-course-class">
                        <#if newGkChoiceDto.courseCategoryDtoList?exists && newGkChoiceDto.courseCategoryDtoList?size gt 0>
                            <#list newGkChoiceDto.courseCategoryDtoList as item>
                            <div class="course-class border-1-cfd2d4">
                                <div class="clearfix padding-10">
                                    <div class="float-left">
                                        <div class="pos-rel course-name padding-r-20"><span>${item.categoryName!}</span> <img src="${request.contextPath}/static/images/7choose3/edits.png" class="pos-right js-edit-name" alt="" />
                                        </div>
                                    </div>
                                    <div class="right-course-num float-right">
                                    最多选 <input type="number" name="" id="" min="0" value="${item.maxNum?default(0)}" />组/门，
                                    最少选 <input type="number" name="" id="" min="0" value="${item.minNum?default(0)}" />组/门
                                    &nbsp;<a href="javascript:void(0);" class="classArrayDel">×</a>
                                    </div>
                                </div>
                                <div class="publish-course publish-course-made publish-course-remove js-click-move">
                                  
                                </div>
                            </div>
                            </#list>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
        <div class="text-right">
            <button class="btn btn-default margin-r-10 js-cancel js-cancel-edit">取消</button>
            <button class="btn btn-blue js-ensure">确定</button>
        </div>
    </div>
</div>

<div class="move-box" courseId="" courseName="" shortName=""></div>

<style>
    .publish-course-just-show span{
        display: inline-block;
        position: relative;
        width: 90px;
        height: 36px;
        border: 1px solid #d2d5d7;
        margin: 0 10px 10px 0;
        font-size: 14px;
        line-height: 34px;
        text-align: center;
    }
    .course-merge{
        z-index: 2;
    }
</style>

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

<#if canChange?default(true)>
var TYPE_3_IN_6 = "<p>" +
    "    <span class=\"font-16\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].id\" value=\"\">" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].orderId\" value=\"0\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].categoryName\" value=\"6选3\">" +
    "        <b>6选3：</b>" +
    "    </span>" +
    "    <img src=\"${request.contextPath}/static/images/7choose3/icon-warning.png\" width=\"14\" height=\"14\">" +
    "    <span class=\"color-999\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].maxNum\" value=\"3\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].minNum\" value=\"3\">" +
    "        &nbsp;最多选3门，最少选3门" +
    "    </span>" +
    "</p>" +
    "<div class=\"publish-course-just-show no-padding-top\">" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[1].id\" value=\"${codeToIdMap['3001']?default('')}\">" +
	"        <span shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" class=\"subject\">政治</span>" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[0].id\" value=\"${codeToIdMap['3002']?default('')}\">" +
    "        <span shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" class=\"subject\">历史</span>" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[2].id\" value=\"${codeToIdMap['3003']?default('')}\">" +
    "        <span shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" class=\"subject\">地理</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[4].id\" value=\"${codeToIdMap['3006']?default('')}\">" +
	"        <span shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" class=\"subject\">物理</span>" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[3].id\" value=\"${codeToIdMap['3011']?default('')}\">" +
    "        <span shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" class=\"subject\">化学</span>" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[5].id\" value=\"${codeToIdMap['3020']?default('')}\">" +
    "        <span shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" class=\"subject\">生物</span>" +
    "</div>";

var CHOSE_PANEL_3_IN_6 = "<div class=\"row clearfix margin-b-20 padding-l-10\">\n" +
		"    <div class=\"col-sm-5 left-course course-class chose-panel\">\n" +
		"        <p class=\" margin-b-20 padding-l-10\"><b>所有科目 </b><span class=\"color-999\"> 拖动至右侧选择框</span></p>\n" +
		"        <div class=\"publish-course publish-course-made js-click-move\">\n" +
		"            <span coursename=\"物理\" shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" class=\"disabled\">物理</span>\n" +
		"            <span coursename=\"化学\" shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" class=\"disabled\">化学</span>\n" +
		"            <span coursename=\"生物\" shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" class=\"disabled\">生物</span>\n" +
		"            <span coursename=\"政治\" shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" class=\"disabled\">政治</span>\n" +
		"            <span coursename=\"历史\" shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" class=\"disabled\">历史</span>\n" +
		"            <span coursename=\"地理\" shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" class=\"disabled\">地理</span>\n" +
		<#if isZheJiang?default(false)>"            <span coursename=\"技术\" shortname=\"技\" courseid=\"${codeToIdMap['3037']?default('')}\" class=\"\">技术</span>\n" +</#if>
		"        </div>\n" +
		"        <div class=\"merge-outter clearfix\">\n" +
		"            <div class=\"merge-inner publish-course publish-course-remove js-click-move\"></div>\n" +
		"            <a href=\"#\" class=\"btn btn-blue btn-lg merge-btn js-merge\">合并</a>\n" +
		"        </div>\n" +
		"    </div>\n" +
		"\n" +
		"    <div class=\"col-sm-7 all-course float-left\">\n" +
		"        <button class=\"btn btn-default margin-b-10 js-add-new\" onclick=\"addNewCourseCategory(this)\">新增类别</button>\n" +
		"        <div class=\"all-course-class\">\n" +
		"            <div class=\"course-class border-1-cfd2d4\">\n" +
		"                <div class=\"clearfix padding-10\">\n" +
		"                    <div class=\"float-left\">\n" +
		"                        <div class=\"pos-rel course-name padding-r-20\"><span>6选3</span> <img\n" +
		"                                src=\"/v7/static/images/7choose3/edits.png\" class=\"pos-right js-edit-name\" alt=\"\">\n" +
		"                        </div>\n" +
		"                    </div>\n" +
		"                    <div class=\"right-course-num float-right\">\n" +
		"                        最多选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"3\">组/门，\n" +
		"                        最少选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"3\">组/门\n" +
		"                        &nbsp;<a href=\"javascript:void(0);\" class=\"classArrayDel\">×</a>\n" +
		"                    </div>\n" +
		"                </div>\n" +
		"                <div class=\"publish-course publish-course-made publish-course-remove js-click-move\">\n" +
		"                    <span shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" coursename=\"政治\">政治</span>\n" +
		"                    <span shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" coursename=\"历史\">历史</span>\n" +
		"                    <span shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" coursename=\"地理\">地理</span>\n" +
		"                    <span shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" coursename=\"物理\">物理</span>\n" +
		"                    <span shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" coursename=\"化学\">化学</span>\n" +
		"                    <span shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" coursename=\"生物\">生物</span>\n" +
		"                </div>\n" +
		"            </div>\n" +
		"        </div>\n" +
		"    </div>\n" +
		"</div>";

var TYPE_3_IN_7 = "<p>\n" +
    "    <span class=\"font-16\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].id\" value=\"\">\n" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].orderId\" value=\"0\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].categoryName\" value=\"7选3\">\n" +
    "        <b>7选3：</b>\n" +
    "    </span>\n" +
    "    <img src=\"${request.contextPath}/static/images/7choose3/icon-warning.png\" width=\"14\" height=\"14\">\n" +
    "    <span class=\"color-999\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].maxNum\" value=\"3\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].minNum\" value=\"3\">\n" +
    "        &nbsp;最多选3门，最少选3门\n" +
    "    </span>\n" +
    "</p>\n" +
    "<div class=\"publish-course-just-show no-padding-top\">\n" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[1].id\" value=\"${codeToIdMap['3001']?default('')}\">" +
	"        <span shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" class=\"subject\">政治</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[0].id\" value=\"${codeToIdMap['3002']?default('')}\">" +
	"        <span shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" class=\"subject\">历史</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[2].id\" value=\"${codeToIdMap['3003']?default('')}\">" +
	"        <span shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" class=\"subject\">地理</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[4].id\" value=\"${codeToIdMap['3006']?default('')}\">" +
	"        <span shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" class=\"subject\">物理</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[3].id\" value=\"${codeToIdMap['3011']?default('')}\">" +
	"        <span shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" class=\"subject\">化学</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[5].id\" value=\"${codeToIdMap['3020']?default('')}\">" +
	"        <span shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" class=\"subject\">生物</span>" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[6].id\" value=\"${codeToIdMap['3037']?default('')}\">\n" +
    "        <span shortname=\"技\" courseid=\"${codeToIdMap['3037']?default('')}\" class=\"subject\">技术</span>\n" +
    "</div>";

var CHOSE_PANEL_3_IN_7 = "<div class=\"row clearfix margin-b-20 padding-l-10\">\n" +
		"    <div class=\"col-sm-5 left-course course-class chose-panel\">\n" +
		"        <p class=\" margin-b-20 padding-l-10\"><b>所有科目 </b><span class=\"color-999\"> 拖动至右侧选择框</span></p>\n" +
		"        <div class=\"publish-course publish-course-made js-click-move\">\n" +
		"            <span coursename=\"物理\" shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" class=\"disabled\">物理</span>\n" +
		"            <span coursename=\"化学\" shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" class=\"disabled\">化学</span>\n" +
		"            <span coursename=\"生物\" shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" class=\"disabled\">生物</span>\n" +
		"            <span coursename=\"政治\" shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" class=\"disabled\">政治</span>\n" +
		"            <span coursename=\"历史\" shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" class=\"disabled\">历史</span>\n" +
		"            <span coursename=\"地理\" shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" class=\"disabled\">地理</span>\n" +
		"            <span coursename=\"技术\" shortname=\"技\" courseid=\"${codeToIdMap['3037']?default('')}\" class=\"disabled\">技术</span>\n" +
		"        </div>\n" +
		"        <div class=\"merge-outter clearfix\">\n" +
		"            <div class=\"merge-inner publish-course publish-course-remove js-click-move\"></div>\n" +
		"            <a href=\"#\" class=\"btn btn-blue btn-lg merge-btn js-merge\">合并</a>\n" +
		"        </div>\n" +
		"    </div>\n" +
		"\n" +
		"    <div class=\"col-sm-7 all-course float-left\">\n" +
		"        <button class=\"btn btn-default margin-b-10 js-add-new\" onclick=\"addNewCourseCategory(this)\">新增类别</button>\n" +
		"        <div class=\"all-course-class\">\n" +
		"            <div class=\"course-class border-1-cfd2d4\">\n" +
		"                <div class=\"clearfix padding-10\">\n" +
		"                    <div class=\"float-left\">\n" +
		"                        <div class=\"pos-rel course-name padding-r-20\"><span>7选3</span> <img\n" +
		"                                src=\"/v7/static/images/7choose3/edits.png\" class=\"pos-right js-edit-name\" alt=\"\">\n" +
		"                        </div>\n" +
		"                    </div>\n" +
		"                    <div class=\"right-course-num float-right\">\n" +
		"                        最多选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"3\">组/门，\n" +
		"                        最少选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"3\">组/门\n" +
		"                        &nbsp;<a href=\"javascript:void(0);\" class=\"classArrayDel\">×</a>\n" +
		"                    </div>\n" +
		"                </div>\n" +
		"                <div class=\"publish-course publish-course-made publish-course-remove js-click-move\">\n" +
		"                    <span shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" coursename=\"政治\">政治</span>\n" +
		"                    <span shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" coursename=\"历史\">历史</span>\n" +
		"                    <span shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" coursename=\"地理\">地理</span>\n" +
		"                    <span shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" coursename=\"物理\">物理</span>\n" +
		"                    <span shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" coursename=\"化学\">化学</span>\n" +
		"                    <span shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" coursename=\"生物\">生物</span>\n" +
		"                    <span shortname=\"技\" courseid=\"${codeToIdMap['3037']?default('')}\" coursename=\"技术\">技术</span>\n" +
		"                </div>\n" +
		"            </div>\n" +
		"        </div>\n" +
		"    </div>\n" +
		"</div>";

var TYPE_3_1_2 = "<p>\n" +
    "    <span class=\"font-16\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].id\" value=\"\">\n" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].orderId\" value=\"0\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].categoryName\" value=\"2选1\">\n" +
    "        <b>2选1：</b>\n" +
    "    </span>\n" +
    "    <img src=\"${request.contextPath}/static/images/7choose3/icon-warning.png\" width=\"14\" height=\"14\">\n" +
    "    <span class=\"color-999\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].maxNum\" value=\"1\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].minNum\" value=\"1\">\n" +
    "        &nbsp;最多选1门，最少选1门\n" +
    "    </span>\n" +
    "</p>\n" +
    "<div class=\"publish-course-just-show no-padding-top\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[0].id\" value=\"${codeToIdMap['3002']?default('')}\">\n" +
    "        <span shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" class=\"subject\">历史</span>\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseList[1].id\" value=\"${codeToIdMap['3006']?default('')}\">\n" +
    "        <span shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" class=\"subject\">物理</span>\n" +
    "</div>\n" +
    "<p>\n" +
    "    <span class=\"font-16\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[1].id\" value=\"\">\n" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].orderId\" value=\"1\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[1].categoryName\" value=\"4选2\">\n" +
    "        <b>4选2：</b>\n" +
    "    </span>\n" +
    "    <img src=\"${request.contextPath}/static/images/7choose3/icon-warning.png\" width=\"14\" height=\"14\">\n" +
    "    <span class=\"color-999\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[1].maxNum\" value=\"2\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[1].minNum\" value=\"2\">\n" +
    "        &nbsp;最多选2门，最少选2门\n" +
    "    </span>\n" +
    "</p>\n" +
    "<div class=\"publish-course-just-show no-padding-top\">\n" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[0].id\" value=\"${codeToIdMap['3001']?default('')}\">" +
	"        <span shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" class=\"subject\">政治</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[1].id\" value=\"${codeToIdMap['3003']?default('')}\">" +
	"        <span shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" class=\"subject\">地理</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[2].id\" value=\"${codeToIdMap['3011']?default('')}\">" +
	"        <span shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" class=\"subject\">化学</span>" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[3].id\" value=\"${codeToIdMap['3020']?default('')}\">" +
	"        <span shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" class=\"subject\">生物</span>" +
    "</div>";

var CHOSE_PANEL_3_1_2 = "<div class=\"row clearfix margin-b-20 padding-l-10\">\n" +
		"    <div class=\"col-sm-5 left-course course-class chose-panel\">\n" +
		"        <p class=\" margin-b-20 padding-l-10\"><b>所有科目 </b><span class=\"color-999\"> 拖动至右侧选择框</span></p>\n" +
		"        <div class=\"publish-course publish-course-made js-click-move\">\n" +
		"            <span coursename=\"物理\" shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" class=\"disabled\">物理</span>\n" +
		"            <span coursename=\"化学\" shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" class=\"disabled\">化学</span>\n" +
		"            <span coursename=\"生物\" shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" class=\"disabled\">生物</span>\n" +
		"            <span coursename=\"政治\" shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" class=\"disabled\">政治</span>\n" +
		"            <span coursename=\"历史\" shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" class=\"disabled\">历史</span>\n" +
		"            <span coursename=\"地理\" shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" class=\"disabled\">地理</span>\n" +
		<#if isZheJiang?default(false)>"            <span coursename=\"技术\" shortname=\"技\" courseid=\"${codeToIdMap['3037']?default('')}\" class=\"\">技术</span>\n" +</#if>
		"        </div>\n" +
		"        <div class=\"merge-outter clearfix\">\n" +
		"            <div class=\"merge-inner publish-course publish-course-remove js-click-move\">\n" +
		"            </div>\n" +
		"            <a href=\"#\" class=\"btn btn-blue btn-lg merge-btn js-merge\">合并</a>\n" +
		"        </div>\n" +
		"    </div>\n" +
		"\n" +
		"    <div class=\"col-sm-7 all-course float-left\">\n" +
		"        <button class=\"btn btn-default margin-b-10 js-add-new\" onclick=\"addNewCourseCategory(this)\">新增类别</button>\n" +
		"        <div class=\"all-course-class\">\n" +
		"            <div class=\"course-class border-1-cfd2d4\">\n" +
		"                <div class=\"clearfix padding-10\">\n" +
		"                    <div class=\"float-left\">\n" +
		"                        <div class=\"pos-rel course-name padding-r-20\"><span>2选1</span> <img\n" +
		"                                src=\"/v7/static/images/7choose3/edits.png\" class=\"pos-right js-edit-name\" alt=\"\">\n" +
		"                        </div>\n" +
		"                    </div>\n" +
		"                    <div class=\"right-course-num float-right\">\n" +
		"                        最多选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"1\">组/门，\n" +
		"                        最少选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"1\">组/门\n" +
		"                        &nbsp;<a href=\"javascript:void(0);\" class=\"classArrayDel\">×</a>\n" +
		"                    </div>\n" +
		"                </div>\n" +
		"                <div class=\"publish-course publish-course-made publish-course-remove js-click-move\">\n" +
		"                    <span shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" coursename=\"历史\">历史</span>\n" +
		"                    <span shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" coursename=\"物理\">物理</span>\n" +
		"                </div>\n" +
		"            </div>\n" +
		"            <div class=\"course-class border-1-cfd2d4\">\n" +
		"                <div class=\"clearfix padding-10\">\n" +
		"                    <div class=\"float-left\">\n" +
		"                        <div class=\"pos-rel course-name padding-r-20\"><span>4选2</span> <img\n" +
		"                                src=\"/v7/static/images/7choose3/edits.png\" class=\"pos-right js-edit-name\" alt=\"\"> </div>\n" +
		"                    </div>\n" +
		"                    <div class=\"right-course-num float-right\"> 最多选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"2\">\n" +
		"                        组/门，最少选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"2\"> 组/门&nbsp;<a\n" +
		"                            href=\"javascript:void(0);\" class=\"classArrayDel\">×</a> </div>\n" +
		"                </div>\n" +
		"                <div class=\"publish-course publish-course-made publish-course-remove js-click-move\">\n" +
		"                    <span shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" coursename=\"政治\">政治</span>\n" +
		"                    <span shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" coursename=\"地理\">地理</span>\n" +
		"                    <span shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" coursename=\"化学\">化学</span>\n" +
		"                    <span shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" coursename=\"生物\">生物</span>\n" +
		"                </div>\n" +
		"            </div>\n" +
		"        </div>\n" +
		"    </div>\n" +
		"</div>";

var TYPE_3_2_1 = "<p>\n" +
    "    <span class=\"font-16\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].id\" value=\"\">\n" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[0].orderId\" value=\"0\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].categoryName\" value=\"组合2选1\">\n" +
    "        <b>组合2选1：</b>\n" +
    "    </span>\n" +
    "    <img src=\"${request.contextPath}/static/images/7choose3/icon-warning.png\" width=\"14\" height=\"14\">\n" +
    "    <span class=\"color-999\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].maxNum\" value=\"1\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].minNum\" value=\"1\">\n" +
    "        &nbsp;最多选1组，最少选1组\n" +
    "    </span>\n" +
    "</p>\n" +
    "<div class=\"publish-course-just-show no-padding-top\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseCombination[0].categoryName\" value=\"物化（合）\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseCombination[0].courseList[0].id\" value=\"${codeToIdMap['3006']?default('')}\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseCombination[0].courseList[1].id\" value=\"${codeToIdMap['3011']?default('')}\">\n" +
    "        <span shortname=\"物,化\" courseid=\"${codeToIdMap['3006']?default('')},${codeToIdMap['3011']?default('')}\" class=\"subject\">物化（合）</span>\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseCombination[1].categoryName\" value=\"政历（合）\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseCombination[1].courseList[0].id\" value=\"${codeToIdMap['3001']?default('')}\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[0].courseCombination[1].courseList[1].id\" value=\"${codeToIdMap['3002']?default('')}\">\n" +
    "        <span shortname=\"政,历\" courseid=\"${codeToIdMap['3001']?default('')},${codeToIdMap['3002']?default('')}\" class=\"subject\">政历（合）</span>\n" +
    "</div>" +
    "<p>\n" +
    "    <span class=\"font-16\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[1].id\" value=\"\">\n" +
	"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].orderId\" value=\"1\">" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[1].categoryName\" value=\"排除以上组合4选1\">\n" +
    "        <b>排除以上组合4选1：</b>\n" +
    "    </span>\n" +
    "    <img src=\"${request.contextPath}/static/images/7choose3/icon-warning.png\" width=\"14\" height=\"14\">\n" +
    "    <span class=\"color-999\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[1].maxNum\" value=\"1\">\n" +
    "        <input type=\"hidden\" name=\"courseCategoryDtoList[1].minNum\" value=\"1\">\n" +
    "        &nbsp;最多选1门，最少选1门\n" +
    "    </span>\n" +
    "</p>\n" +
    "<div class=\"publish-course-just-show no-padding-top\">\n" +
		"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[0].id\" value=\"${codeToIdMap['3001']?default('')}\">" +
		"        <span shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" class=\"subject\">政治</span>" +
		"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[1].id\" value=\"${codeToIdMap['3002']?default('')}\">" +
		"        <span shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" class=\"subject\">历史</span>" +
		"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[2].id\" value=\"${codeToIdMap['3003']?default('')}\">" +
		"        <span shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" class=\"subject\">地理</span>" +
		"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[3].id\" value=\"${codeToIdMap['3006']?default('')}\">" +
		"        <span shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" class=\"subject\">物理</span>" +
		"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[4].id\" value=\"${codeToIdMap['3011']?default('')}\">" +
		"        <span shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" class=\"subject\">化学</span>" +
		"        <input type=\"hidden\" name=\"courseCategoryDtoList[1].courseList[5].id\" value=\"${codeToIdMap['3020']?default('')}\">" +
		"        <span shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" class=\"subject\">生物</span>" +
    "</div>";

var CHOSE_PANEL_3_2_1 = "<div class=\"row clearfix margin-b-20 padding-l-10\">\n" +
		"    <div class=\"col-sm-5 left-course course-class chose-panel\">\n" +
		"        <p class=\" margin-b-20 padding-l-10\"><b>所有科目 </b><span class=\"color-999\"> 拖动至右侧选择框</span></p>\n" +
		"        <div class=\"publish-course publish-course-made js-click-move\">\n" +
		"            <span coursename=\"物理\" shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" class=\"disabled\">物理</span>\n" +
		"            <span coursename=\"化学\" shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" class=\"disabled\">化学</span>\n" +
		"            <span coursename=\"生物\" shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" class=\"disabled\">生物</span>\n" +
		"            <span coursename=\"政治\" shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" class=\"disabled\">政治</span>\n" +
		"            <span coursename=\"历史\" shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" class=\"disabled\">历史</span>\n" +
		"            <span coursename=\"地理\" shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" class=\"disabled\">地理</span>\n" +
		<#if isZheJiang?default(false)>"            <span coursename=\"技术\" shortname=\"技\" courseid=\"${codeToIdMap['3037']?default('')}\" class=\"\">技术</span>\n" +</#if>
		"            <span class=\"course-merge disabled\" shortname=\"物,化\"\n" +
		"                courseid=\"${codeToIdMap['3006']?default('')},${codeToIdMap['3011']?default('')}\" coursename=\"物化(合)\">物化(合) <i\n" +
		"                    class=\"wpfont icon-close my-close\"></i></span><span class=\"course-merge disabled\" shortname=\"政,历\"\n" +
		"                courseid=\"${codeToIdMap['3001']?default('')},${codeToIdMap['3002']?default('')}\" coursename=\"政历(合)\">政历(合) <i\n" +
		"                    class=\"wpfont icon-close my-close\"></i></span></div>\n" +
		"        <div class=\"merge-outter clearfix\">\n" +
		"            <div class=\"merge-inner publish-course publish-course-remove js-click-move\"></div>\n" +
		"            <a href=\"#\" class=\"btn btn-blue btn-lg merge-btn js-merge\">合并</a>\n" +
		"        </div>\n" +
		"    </div>\n" +
		"\n" +
		"    <div class=\"col-sm-7 all-course float-left\">\n" +
		"        <button class=\"btn btn-default margin-b-10 js-add-new\" onclick=\"addNewCourseCategory(this)\">新增类别</button>\n" +
		"        <div class=\"all-course-class\">\n" +
		"            <div class=\"course-class border-1-cfd2d4\">\n" +
		"                <div class=\"clearfix padding-10\">\n" +
		"                    <div class=\"float-left\">\n" +
		"                        <div class=\"pos-rel course-name padding-r-20\"><span>组合2选1</span> <img\n" +
		"                                src=\"/v7/static/images/7choose3/edits.png\" class=\"pos-right js-edit-name\" alt=\"\">\n" +
		"                        </div>\n" +
		"                    </div>\n" +
		"                    <div class=\"right-course-num float-right\">\n" +
		"                        最多选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"1\">组/门，\n" +
		"                        最少选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"1\">组/门\n" +
		"                        &nbsp;<a href=\"javascript:void(0);\" class=\"classArrayDel\">×</a>\n" +
		"                    </div>\n" +
		"                </div>\n" +
		"                <div class=\"publish-course publish-course-made publish-course-remove js-click-move\">\n" +
		"                    <span shortname=\"物,化\" courseid=\"${codeToIdMap['3006']?default('')},${codeToIdMap['3011']?default('')}\"\n" +
		"                        coursename=\"物化(合)\">物化(合)</span>\n" +
		"                    <span shortname=\"政,历\" courseid=\"${codeToIdMap['3001']?default('')},${codeToIdMap['3002']?default('')}\"\n" +
		"                        coursename=\"政历(合)\">政历(合)</span>\n" +
		"                </div>\n" +
		"            </div>\n" +
		"            <div class=\"course-class border-1-cfd2d4\">\n" +
		"                <div class=\"clearfix padding-10\">\n" +
		"                    <div class=\"float-left\">\n" +
		"                        <div class=\"pos-rel course-name padding-r-20\"><span>排除以上组合4选1</span> <img\n" +
		"                                src=\"/v7/static/images/7choose3/edits.png\" class=\"pos-right js-edit-name\" alt=\"\"> </div>\n" +
		"                    </div>\n" +
		"                    <div class=\"right-course-num float-right\"> 最多选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"1\">\n" +
		"                        组/门，最少选 <input type=\"number\" name=\"\" id=\"\" min=\"0\" value=\"1\"> 组/门&nbsp;<a\n" +
		"                            href=\"javascript:void(0);\" class=\"classArrayDel\">×</a> </div>\n" +
		"                </div>\n" +
		"                <div class=\"publish-course publish-course-made publish-course-remove js-click-move\">\n" +
		"                    <span shortname=\"政\" courseid=\"${codeToIdMap['3001']?default('')}\" coursename=\"政治\">政治</span>\n" +
		"                    <span shortname=\"历\" courseid=\"${codeToIdMap['3002']?default('')}\" coursename=\"历史\">历史</span>\n" +
		"                    <span shortname=\"地\" courseid=\"${codeToIdMap['3003']?default('')}\" coursename=\"地理\">地理</span>\n" +
		"                    <span shortname=\"物\" courseid=\"${codeToIdMap['3006']?default('')}\" coursename=\"物理\">物理</span>\n" +
		"                    <span shortname=\"化\" courseid=\"${codeToIdMap['3011']?default('')}\" coursename=\"化学\">化学</span>\n" +
		"                    <span shortname=\"生\" courseid=\"${codeToIdMap['3020']?default('')}\" coursename=\"生物\">生物</span>\n" +
		"                </div>\n" +
		"            </div>\n" +
		"        </div>\n" +
		"    </div>\n" +
		"</div>";
</#if>

function goBack() {
	var url =  '${request.contextPath}/newgkelective/${newGkChoiceDto.gradeId}/goChoice/index/page';
	$("#showList").load(url);
}

var contentTmp;

$(function(){
	showBreadBack(goBack,false,"发布选课");

	<#if canChange?default(true)>
	$(".class-array").empty().append(TYPE_3_IN_6);
	$("#revertContent").empty().append(CHOSE_PANEL_3_IN_6);
	</#if>

	$('.js-notice label').click(function(){
		if($(this).index()==1){
			$(this).parent().siblings('.promptContainer').show();
		}else{
			$(this).parent().siblings('.promptContainer').hide();
		}
	});

	// 时间
	$('.datetimepicker').datetimepicker4({
		format: 'YYYY-MM-DD HH:mm',
    	sideBySide: true,
		locale: moment.locale('zh-cn'),
		dayViewHeaderFormat: 'YYYY MMMM',
		useCurrent: false,
    }).next().on('click', function(){
		$(this).prev().focus();
	});

	$('.select-course span').on('click', function(e){
		e.preventDefault();
		if($(this).hasClass('active')){
			$(this).removeClass('active');
			$(this).prev().val(0);
		}else{
			$(this).addClass('active');
			$(this).prev().val(1);
		}
	});

    //自定义数字加减框
    $('.layer-edit').on('click','.js-click button',function(){
        var v = parseInt($(this).parent().siblings('input').val()) >= 0?parseInt($(this).parent().siblings('input').val()):0;
        if ($(this).index() == 0){
            $(this).parent().siblings('input').val(v + 1);
        }else {
            if(v < 1){
                v = 1
            }
            $(this).parent().siblings('input').val(v - 1);
        }
    });
    
    //删除选课
	$('body').on('click','.left-course .publish-course-made i',function(){
		if($(this).hasClass("my-close")){
			if($(this).parent().hasClass("disabled")){
				return;
			}else{
				$(this).parent().remove();
			}
		}else{
			$(this).parent().remove();
		}

	});

    $('.layer-edit').on('change','input',function(){
        $(this).attr("value", $(this).val());
    });

    //编辑科目
    $('.js-edit').on('click', function(e){
        e.preventDefault();
         <#if !canChange?default(true)>
         return;
         </#if>
        var txt = $(this).data('text');
        var cla = $(this).data('class');
        layer.open({
            type: 1,
            shadow: 0.5,
            title: txt,
			scrollbar: false,
            area: ['780px','520px'],
            btn: 0,
            content: $('.layer-edit'),
            end: function () {
                $(".move-box").hide();
                $("#revertContent").html(contentTmp);
            }
        });

        var arr=new Array();
        $(".all-course-class span").each(function(i){
            arr.push($(this).attr("courseId"));
        });
        $(".chose-panel span").each(function () {
            if (arr.indexOf($(this).attr("courseId")) == -1) {
                $(this).removeClass("disabled");
            }
        });

        contentTmp = $("#revertContent").html();

        $('.layer-edit').find('.publish-course-sm').data('class',cla).removeClass('publish-course-forbid').removeClass('publish-course-recommend').addClass(cla);
    });

    //删除类别
    $('.layer-edit').on('click','.classArrayDel',function(){
        $(this).closest(".course-class").find('.publish-course span').each(function () {
            $('.js-click-move').find('span[courseId="'+ $(this).attr("courseId") +'"]').removeClass('disabled');
        });
        $(this).closest(".course-class").remove();
    });

    //编辑
    $('.layer-edit').on('click','.js-edit-name',function(){
        var $t = $(this).siblings('span').text();
        var str = '<input type="text" name="" id="" value="'+ $t +'" />';
        $(this).siblings('span').replaceWith(str);
        $(this).siblings('input').select();
    });

    $('.layer-edit').on('blur','.course-name input[type="text"]',function(){
        var val = $(this).val().trim() == ''?'暂无':$(this).val();
        var str = '<span>'+ val +'</span>';
        $(this).replaceWith(str);
    });

    //拖动开始
    $('.layer-edit').on('mousedown','.js-click-move span',function(e){
        if ($(this).hasClass('disabled') == false && $('.move-box').css('display') == 'none'){
            var x = e.pageX - $(window).scrollLeft() + 3;
            var y = e.pageY - $(window).scrollTop() + 3;
            var str = '<span>'+ $(this).attr("courseName") +'</span>';
            $('.move-box').show().empty().append(str);
            if ($(this).parent().hasClass('publish-course-remove')) {
                $(this).remove();
            }
            $('.move-box').attr('courseId',$(this).attr('courseId')).attr('shortName',$(this).attr('shortName')).attr('courseName',$(this).attr('courseName')).css({
                left: x,
                top: y,
                zIndex: 100000000
            });
        }
    });

    //拖动范围
    $('.layer-edit').on('mousemove','#revertContent',function(e){
        if ($('.move-box').css('display') == 'block'){
            var x = e.pageX - $(window).scrollLeft() + 3;
            var y = e.pageY - $(window).scrollTop() + 3;
            $('.move-box').css({
                left: x,
                top: y
            });
        }
    });

    $('.layer-edit').bind('selectstart', function(e){
        return false;
    });

	//拖动结束添加
	$('.layer-edit').on('mouseup', '.course-class .publish-course', function () {
		if ($('.move-box').css('display') == 'block') {
			var str = '<span shortName="' + $(".move-box").attr("shortName") + '" courseId="' + $(".move-box").attr("courseId") + '" courseName="' + $(".move-box").attr("courseName") + '">' + $('.move-box').find('span').text() + '</span>';
			if ($(this).parent().hasClass("chose-panel")) {
				$('.js-click-move').eq(0).find('span[courseId="' + $(".move-box").attr("courseId") + '"]').removeClass("disabled");
			} else if ($(this).parent().parent().hasClass("chose-panel")) {
				$(this).append(str);
				$('.js-click-move').eq(0).find('span[courseId="' + $(".move-box").attr("courseId") + '"]').addClass('disabled');
			} else {
				var flag = false;
				var tmp = $('.move-box').attr("courseId").split(",");
				for (var i = 0; i < tmp.length; i++) {
					$(this).find("span").each(function () {
						if($(this).attr("courseId").indexOf(tmp[i]) > -1) {
							layer.alert('该类目下科目存在交集',{icon:7});
							flag = true;
						}
					});
				}
				if (!flag) {
					$(this).append(str);
					$('.js-click-move').eq(0).find('span[courseId="' + $(".move-box").attr("courseId") + '"]').addClass('disabled');
				}
			}
			$('.move-box').hide();
		}
	});

    $('.js-ensure').on('click',function(){
        var str = '';
        var error = false;
        var reg = /^(0|[1-9][0-9]*)$/;
        var reg1 = /^\+?[1-9]\d*$/; 
        var totalMax = 0;
        var totalMin = 0;
        var totalHeight = 0;
        if($('.all-course-class .course-class').length == 0) {
            layer.alert('至少创建一个类目',{icon:7});
            return;
        }
        if($('.all-course-class .course-class').length > 0) {
            $('.all-course-class .course-class').each(function(i,pa){
                if (error) {
                    return;
                }
                $(".all-course-class").scrollTop(totalHeight);
                totalHeight += ($(this).height() + 10);
                var name = $(this).find('.course-name span').text().trim();
                if (name == '') {
                    layer.tips('名称不能为空！',$(this).find('.course-name span'), {
                        tipsMore: true,
                        tips: 3
                    });
                    error = true;
                    return;
                }

                var num1 = $(this).find('.right-course-num input').eq(0).val().trim();
                if (num1 == '' || !reg1.test(num1)) {
                    layer.tips('选课数量为大于0的整数！',$(this).find('.right-course-num input').eq(0), {
                        tipsMore: true,
                        tips: 3
                    });
                    error = true;
                    return;
                }

                var num2 = $(this).find('.right-course-num input').eq(1).val().trim();
                if (num2 == '' || !reg.test(num2)) {
                    layer.tips('选课数量为大于等于0的整数！',$(this).find('.right-course-num input').eq(1), {
                        tipsMore: true,
                        tips: 3
                    });
                    error = true;
                    return;
                }

                if (parseInt(num1) < parseInt(num2)) {
                    layer.tips('最大选课数量不应小于最小选课数量！',$(this).find('.right-course-num input').eq(1), {
                        tipsMore: true,
                        tips: 3
                    });
                    error = true;
                    return;
                }

                totalMax += parseInt(num1);
                totalMin += parseInt(num2);

                if ($(this).find('.publish-course span').length == 0) {
                    layer.tips('该类目无课程！',$(this), {
                        tipsMore: true,
                        tips: 3
                    });
                    error = true;
                    return;
                }

/*                var totalTmp=0;
                var combination = false;
                $(this).find('.publish-course span').each(function () {
                    if ($(this).attr("courseId").length > 32) {
                        totalTmp += 2;
                        combination = true;
                    } else {
                        totalTmp++;
                    }
                });

                if (combination && parseInt(num1) < 2) {
					layer.tips('该类目下存在科目组合，最多选课数不应少于两门',$(this), {
						tipsMore: true,
						tips: 3
					});
					error = true;
					return;
				}

                if (totalTmp < num2) {
                    layer.tips('该类目最少' + num2 + '门！',$(this), {
                        tipsMore: true,
                        tips: 3
                    });
                    error = true;
                    return;
                }
*/
                var span = '';
                var singleIndex = 0;
                var combinationIndex = 0;
                $(this).find('.publish-course span').each(function(j,ch){
					if ($(this).attr("courseName").indexOf("合") < 0) {
						span += '<input type="hidden" name="courseCategoryDtoList[' + i + '].courseList[' + singleIndex + '].id" value="' + $(this).attr("courseId") + '"><span shortName="' + $(this).attr("shortName") + '" courseId="' + $(this).attr("courseId") + '" class="subject">' + $(this).attr("courseName") + '</span>';
						singleIndex++;
					} else {
						var idArr = $(this).attr("courseId").split(",");
						span += '<input type="hidden" name="courseCategoryDtoList[' + i + '].courseCombination[' + combinationIndex + '].categoryName" value="' + $(this).attr("courseName") + '"><input type="hidden" name="courseCategoryDtoList[' + i + '].courseCombination[' + combinationIndex + '].courseList[0].id" value="' + idArr[0] + '"><input type="hidden" name="courseCategoryDtoList[' + i + '].courseCombination[' + combinationIndex + '].courseList[1].id" value="' + idArr[1] + '"><span shortName="' + $(this).attr("shortName") + '" courseId="' + $(this).attr("courseId") + '" class="subject">' + $(this).attr("courseName") + '</span>';
						combinationIndex++;
					}
                });
                str += '<p class="">\
                            <input type="hidden" name="courseCategoryDtoList[' + i + '].id" value="">\
							<input type="hidden" name="courseCategoryDtoList[' + i + '].orderId" value="' + i + '">\
                            <input type="hidden" name="courseCategoryDtoList[' + i + '].categoryName" value="' + name + '">\
                            <span class="font-16"><b>'+ name +'</b></span>\
						    <img src="${request.contextPath}/static/images/7choose3/icon-warning.png" width="14" height="14"/>\
						    <span class="color-999">\
						    <input type="hidden" name="courseCategoryDtoList[' + i + '].maxNum" value="'+ num1 +'">\
						    <input type="hidden" name="courseCategoryDtoList[' + i + '].minNum" value="'+ num2 +'">\
						    &nbsp;最多选'+ num1 +'组\/门，最少选'+ num2 +'组\/门\
						    </span>\
						</p>\
					    <div class="publish-course-just-show no-padding-top">'+ span +'</div>';
            });
        }
        if (error) {
            return;
        }
        /*
        if (totalMax < 3) {
            layer.alert('各类目最多选课数之和小于3门',{icon:7});
            return;
        }
        if (totalMin > 3) {
            layer.alert('各类目最少选课数之和大于3门',{icon:7});
            return;
        }*/
        $('.class-array').empty().append(str);
        contentTmp = $("#revertContent").html();
        $('.layui-layer-close').trigger('click');
        contentTmp = "";
    });

    // 新增推荐组合，禁选组合
    $('.js-add').each(function(index,ele){
        $(this).on('click', function(e){
            e.preventDefault();
            var txt = $(this).data('text');
            var cla = $(this).data('class');
            layer.open({
                type: 1,
                shadow: 0.5,
                title: txt,
                area: '730px',
                btn: 0,
                content: $('.layer-add')
             });
            $('.layer-add').find('.publish-course-sm').data('class',cla).removeClass('publish-course-forbid').removeClass('publish-course-recommend').addClass(cla);
        });
    });

    // 合并
    $('.layer-edit').on('click', '.js-merge', function(){
        var len = $(this).siblings(".merge-inner").children().length;
        if (len === 2) {
            var type = '';
            var typeArr = new Array(2);
            var text = '';
            var shortName = '';
            // 默认无“合”
            var flag = false;
            var repeat = false;
            $(this).siblings(".merge-inner").children().each(function (index, element) {
                if ($(this).text().indexOf("合") >= 0) {
                    layer.msg('已合并的课程不能再次合并');
                    flag = true;
                    return;
                } else {
                    if (type && type.substring(0, 32)==$(this).attr('courseId')) {
						layer.msg('相同课程无需合并');
						flag = true;
						return;
					}
                    type = type + $(this).attr('courseId') + ",";
                    typeArr[index] = $(this).attr('courseId');
                    text = text + $(this).attr("shortName");
                    shortName = shortName + $(this).attr("shortName") + ",";
                    
                }
            });
            
            type = type.substring(0, type.length - 1);
            shortName = shortName.substring(0, shortName.length - 1);
            $('.js-click-move').eq(0).children().each(function(){
            	var cIds=$(this).attr('courseId');
                if (cIds.length > 32 && cIds.indexOf(typeArr[0]) > -1 && cIds.indexOf(typeArr[1])>-1) {
                    layer.msg('已存在该合并课程');
                    repeat = true;
                    return;
                }
            });
            if (!flag && !repeat) {
                var temp = '<span class="course-merge" shortName="' + shortName + '" courseId="' + type + '" courseName="' + text + '(合)">'+text+'(合) <i class="wpfont icon-close my-close"></i></span>';
                $('.js-click-move').eq(0).append(temp);
                $(this).siblings(".merge-inner").empty();
                for (var i = 0; i < typeArr.length; i++) {
                    $('.js-click-move').eq(0).find('span[courseid="' + typeArr[i] + '"]').removeClass('disabled');
                }
            } else {
            	//让合并的科目可以再次选中
            	$(this).siblings(".merge-inner").children().each(function (index, element) {
            		var courseIds=$(this).attr('courseId');
	                $('.js-click-move').eq(0).find('span[courseid="' + courseIds + '"]').removeClass('disabled');
	            });
                $(this).siblings(".merge-inner").empty();
            }
        }else {
            layer.msg('请选择两门课程进行合并');
        }
    });
    

    $('.js-cancel').on('click',function(){
        $('.layui-layer-close').trigger('click')
    });

	$(".referScoreIdClass").on("click", function () {
		if ($(this).hasClass("active")) {
			$(this).removeClass("active");
			$("#refer-score-id").removeAttr("value");
		} else {
			$(this).addClass("active");
			$("#refer-score-id").val($(this).attr("referScoreId"));
			$(this).siblings().removeClass("active");
		}
	});
});

function stepSwitch(index, target) {
	if (target > index) {
		if (1 == index) {
			var checkVal = checkValue('.step_switch_1');
			if (!checkVal) {
				return;
			}
			var chooseNum = $("#chooseNum").val();
			var chooseNumInt = parseInt(chooseNum);
			if (chooseNumInt <= 0) {
				layer.tips('请输入正整数', $("#chooseNum"), {
					tipsMore: true,
					tips: 3
				});
				return;
			}
			if (CompareDate($("#startTime").val(), $("#limitedTime").val())) {
				layer.tips('选课截止时间必须大于选课开始时间', $("#limitedTime"), {
					tipsMore: true,
					tips: 3
				});
				return;
			}
			if ($("#noticeShowId").prop("checked")) {
				if ($("#showNumId").val() == '') {
					layer.tips('提示条件不能为空', $("#showNumId"), {
						tipsMore: true,
						tips: 3
					});
					return;
				}
				if ($("#showTimeId").val() == '') {
					layer.tips('提示开始时间不能为空', $("#showTimeId"), {
						tipsMore: true,
						tips: 3
					});
					return;
				}
				if ($("#hintContentId").val().trim() == '') {
					layer.tips('提示文字不能为空', $("#hintContentId"), {
						tipsMore: true,
						tips: 3
					});
					return;
				}
				if (CompareDate($("#showTimeId").val(), $("#limitedTime").val())) {
					layer.tips('提示开始时间不能大于选课截止时间', $("#showTimeId"), {
						tipsMore: true,
						tips: 3
					});
					return;
				}
				if (CompareDate($("#startTime").val(), $("#showTimeId").val())) {
					layer.tips('提示开始时间不能小于选课开始时间', $("#showTimeId"), {
						tipsMore: true,
						tips: 3
					});
					return;
				}
			} else {
				$("#showNumId").val("");
				$("#showTimeId").val("");
				$("#hintContentId").val("");
			}
		}
		if (2 == index) {
			var items = $(".class-array .subject");
			if (items.length < parseInt($("#chooseNum").val())) {
				layer.alert('请选择大于学生选课数的选课科目数', {icon: 7});
				return;
			}
		}
	}

	$(".step_label").removeClass("active");
	$(".step_label_" + target).addClass("active");
	$(".step_switch").hide();
	$(".step_switch_" + target).show();
	$(".step_bottom").hide();
	$(".step_bottom_" + target).show();
}

// 预置选课模式切换
<#if canChange?default(true)>
function defaultChoiceSwitch(index) {
    if (index == 0) {
        $(".class-array").empty().append(TYPE_3_IN_6);
        $("#revertContent").empty().append(CHOSE_PANEL_3_IN_6);
    } else if (index == 1) {
        $(".class-array").empty().append(TYPE_3_IN_7);
		$("#revertContent").empty().append(CHOSE_PANEL_3_IN_7);
    } else if (index == 2) {
        $(".class-array").empty().append(TYPE_3_1_2);
		$("#revertContent").empty().append(CHOSE_PANEL_3_1_2);
    } else if (index == 3) {
        $(".class-array").empty().append(TYPE_3_2_1);
		$("#revertContent").empty().append(CHOSE_PANEL_3_2_1);
    }
}
</#if>

function addNewCourseCategory(obj) {
	var numb = $(".course-name").length;
    var str = '<div class="course-class border-1-cfd2d4">\
				       <div class="clearfix padding-10">\
				           <div class="float-left">\
				               <div class="pos-rel course-name padding-r-20"><span>选课科目'+numb+'</span> <img src="${request.contextPath}/static/images/7choose3/edits.png" class="pos-right js-edit-name" alt="" />\
				               </div>\
				           </div>\
				           <div class="right-course-num float-right">\
				               最多选 <input type="number" name="" id="" min="0" value="" />\
				               组\/门，最少选 <input type="number" name="" id="" min="0" value="" />\
				               组\/门&nbsp;<a href="javascript:void(0);" class="classArrayDel">×</a>\
				           </div>\
				       </div>\
				       <div class="publish-course publish-course-made publish-course-remove js-click-move">\
				       </div>\
				   </div>';
    $(obj).next('.all-course-class').append(str);
}

function alertList(state,choiceId){
	var reg = /^[1-9]$/;
	if (!reg.test($("#chooseNum").val())) {
		layer.alert('请输入正确的选择科目数', {icon: 7});
		return;
	}
	var items = $(".class-array .subject");
	if (items.length < $("#chooseNum").val()) {
		layer.alert('请选择大于学生科目数的科目', {icon: 7});
		return;
	}
	var arr = new Array();
	items.each(function (i) {
		var id = $(this).attr("courseId");
		var shortName = $(this).attr("shortName");
		if (id.length > 32) {
			var tmpId = id.split(",");
			var tmpName = shortName.split(",");
			for (var i = 0; i < 2; i++) {
				if (arr.indexOf(tmpId[i]) < 0) {
					arr.push(tmpId[i] + "," + tmpName[i]);
				}
			}
		} else {
			arr.push(id + "," + shortName);
		}
	});
	var chooseNum = $("#chooseNum").val();
	var openTitle = "设置推荐组合";
	if (state == 2) {
		openTitle = "设置不推荐组合";
	} else if (state == 3) {//临时演示
		openTitle = "设置互斥组合";
		chooseNum = 2;
	}

	var type="";
	<#if canChange?default(true)>
	if($(".class-array").html()==TYPE_3_1_2){
		type="3_1_2";
	}else if($(".class-array").html()==TYPE_3_2_1){
		type="3_2_1";
	}
	</#if>

	var str = arr.join("-");
	var state = "&state=" + state;
	str += state;
	var choiceId = "&choiceId=" + choiceId;
	var url = "${request.contextPath}/newgkelective/choice/getSubjectList/page" + "?str=" + str + "&chooseNum=" + chooseNum + choiceId+"&type="+type;
	url = encodeURI(encodeURI(url));

	indexDiv = layerDivUrl(url, {title: openTitle, width: 850, height: 470});
}
<#--临时演示-->
function alertList2(){
	var reg = /^[1-9]$/;
	if(!reg.test($("#chooseNum").val())){
		layer.alert('请输入正确的选择科目数',{icon:7});
		return;
	}
	var items = $(".subject.active");
	if(items.length < $("#chooseNum").val()) {
		layer.alert('请选择大于学生科目数的科目',{icon:7});
		return;
	}
	var arr=new Array();
	items.each(function(i){
		var id = $(this).prev().prev().prev().val();
		var shortName = $(this).prev().prev().val();
		arr.push(id + "," + shortName);
	});
	var chooseNum = $("#chooseNum").val();
	var str=arr.join("-");
	var state = "&state=" + state;
	str +=state;
	var choiceId = "&choiceId=" + choiceId;
	var url = "${request.contextPath}/newgkelective/choice/setStudentNum/page"+"?str="+str+"&chooseNum="+chooseNum + choiceId;
	url = encodeURI(encodeURI(url));
	indexDiv = layerDivUrl(url,{title: "设置组合限定人数",width:1000,height:600});
}

function CompareDate(d1,d2){
	return ((new Date(d1.replace(/-/g,"\/"))) > (new Date(d2.replace(/-/g,"\/"))));
}
var isSubmit=false;

function setNotice(){
	showConfirmMsg('使用参考公告会对现有的公告进行替换，建议对公告进行微调，保存修改后生效','提示',function(ii){
		var notticeContext="<p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:2em'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'><span style='font-family:仿宋'>请在仔细了解政策、理解学校所做解读的基础上，请根据学生自己个人情况确定</span>选考科目的初步意向。学校要求班主任、任课老师接受家长和学生的咨询，请学生自主选择、慎重选择。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;color: rgb(255, 0, 0);letter-spacing: 0;font-size: 16px'><span style='font-family:仿宋'>注意：</span></span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>①每位同学必须选择三门课程，且只能选择三门课程，否则无法提交。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>②本次选择将作为下学期学校开课的依据，请各位学生根据自身情况认真选择。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>③本次选择不作为学生高考选考课程的最终依据，本次仅为预选。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>④使用中如遇浏览器兼容性原因，建议使用谷歌浏览器，或360极速模式。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>⑤非选课时间学生无法查看相应信息，老师发布选课项目后，学生才能看到选课信息。</span></p><p style='margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-indent:0'><span style='font-family: 仿宋;letter-spacing: 0;font-size: 16px'>⑥学生在选课截止时间之前，可以操作选课，并且可以修改再提交，一旦超过选课截止时间，学生无法继续操作。</span></p><p><br/></p>";
		UE.getEditor('noticediv').setContent(notticeContext);
		layer.close(ii);
	});
};

function doChoiceSave(){
	if(isSubmit){
		return;
	}
	
	if(!pass){
		layer.tips(checkMsg,$("#choiceName"), {
			tipsMore: true,
			tips: 3
		});
		return;
	}
	
	isSubmit=true;
	var checkVal = checkValue('#myform');
	if(!checkVal){
	 	isSubmit=false;
	 	return;
	}
	var chooseNum=$("#chooseNum").val();
	var chooseNumInt=parseInt(chooseNum);
	if(chooseNumInt<=0){
		layer.tips('请输入正整数', $("#chooseNum"), {
				tipsMore: true,
				tips:3				
			});
		isSubmit=false;
		return;
	}
	if(CompareDate($("#startTime").val(),$("#limitedTime").val())){
		layer.tips('选课截止时间必须大于选课开始时间', $("#limitedTime"), {
				tipsMore: true,
				tips:3				
			});
		isSubmit=false;
	 	return;
	}
	if( $("#noticeShowId").prop("checked")){
		if($("#showNumId").val()==''){
			layer.tips('提示条件不能为空', $("#showNumId"), {
				tipsMore: true,
				tips:3				
			});
			isSubmit=false;
		 	return;
		}
		if($("#showTimeId").val()==''){
			layer.tips('提示开始时间不能为空', $("#showTimeId"), {
				tipsMore: true,
				tips:3				
			});
			isSubmit=false;
		 	return;
		}
		if($("#hintContentId").val().trim()==''){
			layer.tips('提示文字不能为空', $("#hintContentId"), {
				tipsMore: true,
				tips:3				
			});
			isSubmit=false;
		 	return;
		}
		if(CompareDate($("#showTimeId").val(),$("#limitedTime").val())){
			layer.tips('提示开始时间不能大于选课截止时间', $("#showTimeId"), {
				tipsMore: true,
				tips:3				
			});
			isSubmit=false;
		 	return;
		}
		if(CompareDate($("#startTime").val(),$("#showTimeId").val())){
			layer.tips('提示开始时间不能小于选课开始时间', $("#showTimeId"), {
				tipsMore: true,
				tips:3				
			});
			isSubmit=false;
		 	return;
		}
	}else{
		$("#showNumId").val("");
		$("#showTimeId").val("");
		$("#hintContentId").val("");
	}
	var items = $(".class-array .subject");
	if(items.length < chooseNumInt) {
		layer.alert('请选择大于学生选课数的选课科目数',{icon:7});
		isSubmit=false;
		return;
	}
	
	var notice = UE.getEditor('noticediv').getContent();
	if(notice == ""){
		layer.alert('请填写公告设置！',{icon:7});
		isSubmit=false;
		return;
	}
	$("#notice").val(notice);
	
	// 提交数据
	var ii = layer.load();
	var options = {
		url : '${request.contextPath}/newgkelective/choice/saveChoice',
		dataType : 'json',
		success : function(data){
	 		if(data.success){
	 			layer.closeAll();
	 			layer.msg(data.msg, {
					offset: 't',
					time: 2000
				});
				//setTimeout(function(){
					doEnterSetp('${newGkChoiceDto.gradeId}',1,1);
				//},500)
				
	 		}
	 		else{
	 			layerTipMsg(data.success,"失败",data.msg);
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
	
};
$(function(){

	$('[data-toggle="tooltip"]').tooltip({
			container: 'body',
			trigger: 'hover'
		});	
});
</script>