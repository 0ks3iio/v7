<#macro schActivity  class="" title="" activities=[]  dataGroup="">
<#if activities?exists && (activities?size gt 0) >
<div class="manual-item ${class}" title="${title}">
    <div class="manual-item-body">
        <div class="manual-item-title">${title}</div>
            <#list activities as activity>
                <#assign schAtts = (activityMap[activity.id])?if_exists />

                <div class="manual-item-group">
                    <div class="manual-item-group-title">${activity.actTheme!}</div>
                    <div class="manual-item-img">
                        <#if schAtts?exists && schAtts?size gt 0>
                            <#assign imagSize = schAtts?size >
                            <span class="manual-item-group-num">${imagSize!}</span>
                            <#list schAtts as att>
								<#if att_index==0>
                                <#--<img src="${request.contextPath}/studevelop/common/attachment/showPic?id=${att.id!}&showOrigin=0" data-preview-src="${request.contextPath}/studevelop/common/attachment/showPic?id=${att.id!}&showOrigin=1" data-preview-group="${dataGroup}" data-img-sort="${att_index}" <#if att_index != 0 > style="display: none;" </#if> >-->
                                <img src="${fileUrl!}${att.filePath!}" data-preview-src="${request.contextPath}/studevelop/common/attachment/showPic?id=${att.id!}&showOrigin=1" data-preview-group="${dataGroup}" data-img-sort="${att_index}" <#if att_index != 0 > style="display: none;" </#if> >
								<#else>
                                <img data-preview-src="${request.contextPath}/studevelop/common/attachment/showPic?id=${att.id!}&showOrigin=1" src="" data-preview-group="${dataGroup}" data-img-sort="${att_index}" <#if att_index != 0 > style="display: none;" </#if> >
								</#if>
                            </#list>
                        </#if>
                        <div class="manual-item-group-des letter-break" style="word-break: break-all;">${activity.actRemark!}</div>
                    </div>
                </div>
            </#list>
    </div>
</div>
</#if>
</#macro>

<#macro honor  class="" title="" detailes=[] dataGroup="" >
<#if detailes?exists && (detailes?size gt 0) >
<div class="manual-item ${class}" title="${title}">
    <div class="manual-item-body">
        <div class="manual-item-title">${title}</div>
        <ul class="mui-photo-add mui-honor-add mui-clearfix">
            
                <#list detailes as detail>
                    <li>
                        <a href="#">
                            <#--<img src="${request.contextPath}/studevelop/common/attachment/showPic?id=${detail.id!}&showOrigin=0" data-preview-src="${request.contextPath}/studevelop/common/attachment/showPic?id=${detail.id!}&showOrigin=1" data-preview-group="${dataGroup}" data-img-sort="${detail_index + 1}"   />-->
                            <img src="${fileUrl!}${detail.filePath!}" data-preview-src="${request.contextPath}/studevelop/common/attachment/showPic?id=${detail.id!}&showOrigin=1" data-preview-group="${dataGroup}" data-img-sort="${detail_index + 1}"   />
                        </a>
                    </li>

                </#list>
        </ul>
    </div>
</div>
</#if>
</#macro>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <meta name="format-detection" content="telephone=no,email=no">
    <meta name="ML-Config" content="fullscreen=yes,preventMove=no">
    <title>学生成长手册</title>
    <script src="${request.contextPath}/studevelop/devdoc/js/echarts.min.js"></script>
    <link href="${request.contextPath}/static/mui/css/mui.css" rel="stylesheet"/>
    <link href="${request.contextPath}/static/mui/css/mui.icons-extra.css" rel="stylesheet"/>
    <link href="${request.contextPath}/studevelop/mobile/css/style.css" rel="stylesheet"/>
    <style type="text/css">
        .mui-slider-indicator.mui-segmented-control{background-color: #e0be3a;border-radius: 5px 5px 0 0;}
        .mui-segmented-control .mui-control-item{line-height: 36px;}
        .mui-segmented-control.mui-segmented-control-inverted .mui-control-item{border-bottom: 2px solid #e0be3a;padding: 0 10px;margin-left: 10px;font-size: 14px;color: #fff3c5;}
        .mui-segmented-control.mui-segmented-control-inverted .mui-control-item.mui-active{border-bottom-color: #ae8b03;color: #fff;}
        .mui-content-padded{margin: 5px;}
        .mui-content-padded .mui-inline:first-child{width:60px;margin-top: 4px;text-align: right;font-size: 14px;color: #666;}
        .mui-content-padded .mui-inline .mui-icon-star-filled{color: #e9e0cf;}
        .mui-content-padded .mui-inline .mui-icon-star-filled.mui-active{color: #ff7e00;}
    </style>
</head>

<body class="manual">
<div class="manual-header">
	<div class="manual-header-avatar">
		<#if stuIntroduction?exists><#--&& stuIntroduction.filePath?exists-->
		<img src="${fileUrl!}${stuIntroduction.filePath!}"/>
		<#else>
		<img src="${request.contextPath}/static/jscrop/images/portrait_big.png"/>
		</#if>
	</div>
</div>

<div class="manual-container">
    <div class="manual-item-my" title="我的档案">
        <ul>
            <li class="left"><i></i><span>姓名：${student.studentName!}</span></li>
            <li><i></i><span>性别：${mcodeSetting.getMcode("DM-XB","${student.sex!}")}</span></li>
            <li class="left"><i></i><span>出生日期：${(student.birthday?string('yyyy/MM/dd'))!} </span></li>
            <li><i></i><span>班级：${student.className!}</span></li>
            <div class="mui-clearfix"></div>
            <#if stuIntroduction?exists>
                <li class="block letter-break"><i></i><span>${stuIntroduction.speciality!}</span></li>
                <li class="block letter-break"><i></i><span>${stuIntroduction.content!}</span></li>
            </#if>

        </ul>
    </div>
    <#if family?exists && family.id?exists>
    <div class="manual-item manual-item-1" title="幸福的一家">
        <div class="manual-item-body">
            <div class="manual-family mui-clearfix">
                <div class="manual-family-header">幸<br />福<br />的<br />一<br />家</div>
                <div class="manual-family-body">
                    <#if family.existsImgPath?default(false) && family.filePath?exists>
                        <div class="manual-item-img"><img src="${fileUrl!}${family.filePath!}" alt="" /></div>
                    </#if>

                    <div class="manual-item-des">
                        <p class="letter-break">${family.parentContent!}</p>

                    </div>
                </div>
            </div>
        </div>
    </div>
    </#if>
    
    <#if schoolInfo?exists && schoolInfo.id?exists>
    <div class="manual-item manual-item-2" title="美丽的校园">
        <div class="manual-item-body">
            <div class="manual-item-title">美丽的校园</div>
            <div class="manual-item-des">
                <p>${schoolInfo.actRemark!}</p>
            </div>
            <#if picList?exists && (picList?size gt 0) >
            <div class="manual-item-img">
                <#list picList as pic>
                    <#--<img  src="${request.contextPath}/studevelop/common/attachment/showPic?id=${pic.id!}&showOrigin=1" alt="" />-->
                    <img  src="${fileUrl!}${pic.filePath!}" alt="" />
                </#list>
            </div>
            </#if>
        </div>
    </div>
    </#if>
    
    <div class="manual-item manual-item-3" title="校长寄语">
        <div class="manual-item-body">
            <div class="manual-item-title">校长寄语</div>
            <div class="manual-item-avatar">
                <img src="${request.contextPath}/studevelop/common/attachment/masterpic/show?unitId=${student.schoolId!}" alt="" />
                <span>${myschool.schoolmaster!}</span>
            </div>
            <div class="manual-item-des">
                <p class="letter-break" >${(words.getWordsVal('words'+grade.gradeCode))!}</p>
            </div>
        </div>
    </div>
    <div class="manual-item manual-item-4" title="班级大家庭">
        <div class="manual-item-body">
            <div class="manual-item-title">班级大家庭</div>
            <ul class="manual-item-avatar-list mui-clearfix mt-15">

                <#if classTeachingList?exists && (classTeachingList?size gt 0) >
                    <#list classTeachingList as classTeaching>
                        <li class="manual-item-avatar-item">
                            <span class="manual-item-avatar-img" style="background-color: #0cc873;"><#if '' == classTeaching.teacherName?default('')>&nbsp;<#else>${classTeaching.teacherName!}</#if></span>
                            <span class="manual-item-avatar-txt">${classTeaching.subjectName!}老师</span>
                        </li>
                    </#list>
                </#if>

            </ul>
            <div class="manual-class">
                <i class="manual-class-bottom"></i>
                <i class="manual-class-top"></i>
                <ul class="manual-item-avatar-list mui-clearfix">

                <#if classmates?exists && (classmates?size gt 0) >
                    <#list classmates as classmate>

                        <li class="manual-item-avatar-item">
                            <span class="manual-item-avatar-img"><img <#if stuIntroductionMap?exists  && stuIntroductionMap?size gt 0 &&  stuIntroductionMap[classmate.id]?exists >
                                <#assign stuimg = stuIntroductionMap[classmate.id].filePath!>
                            </#if>
                            <img
                                <#if stuimg?default('') != ''>
                                        src="${fileUrl!}${stuimg!}"
                                <#elseif classmate.sex?default(0) == 2>
                                        src="${request.contextPath}/studevelop/images/user-female.png"
                                <#else>
                                        src="${request.contextPath}/studevelop/images/user-male.png"
                                </#if> alt="" /></span>
                            <span class="manual-item-avatar-txt">${classmate.studentName!}</span>
                         <#assign stuimg = "" />
                        </li>
                    </#list>
                </#if>

                </ul>
            </div>
        </div>
    </div>
    <div class="manual-item manual-item-5" title="我的成绩">
        <div class="manual-item-body">
            <#if achilist?exists && (achilist?size gt 0 )>
            <div class="manual-item-title">我的成绩</div>
            <ul class="manual-grade-header mui-clearfix mt-10">
                <li class="mui-clearfix"><i class="color-lump color-lump-1"></i><span>平时成绩</span></li>
                <li class="mui-clearfix"><i class="color-lump color-lump-2"></i><span>期末成绩</span></li>
                <li class="mui-clearfix"><i class="color-lump color-lump-3"></i><span>学习态度</span></li>
            </ul>
            <#list achilist as achi>
            <div class="manual-grade-item mui-clearfix">
                <span class="tt">${achi.subname!}</span>

                <div class="dd">
                    <#--<#assign psAchi = achi.psachi?number >
                    <#assign psFullMark = achi.psFullMark?number >
                    <#assign psPercent = psAchi/psFullMark >-->
                    <#if achi.psachi?exists && achi.psachi!="">
                    <p>
                        <i class="color-lump color-lump-1" style="width:${(achi.psPercent)?string('percent')};">
                        <span class="num">${achi.psachi!}</span>
                        </i>
                    </p>
                    </#if>
                    <#--<#assign qmAchi = achi.qmachi?number >
                    <#assign qmFullMark = achi.qmFullMark?number >
                    <#assign qmPercent = qmAchi/qmFullMark >-->
                    <#if achi.qmachi?exists && achi.qmachi!="">
                    <p>
                        <i class="color-lump color-lump-2" style="width:${(achi.qmPercent)?string('percent')};">
                        <span class="num">${achi.qmachi!}</span>
                        </i>
                    </p>
                    </#if>
                    <#if achi.xxtd!?default("") == '1'>
                        <#assign xxPercent = "100%" >
                    <#elseif achi.xxtd!?default("") == '2'>
                        <#assign xxPercent = "75%" >
                    <#elseif achi.xxtd!?default("") == '3' >
                        <#assign xxPercent = "50%" >
                    <#elseif achi.xxtd!?default("") == '4'>
                        <#assign xxPercent = "25%" >
                    </#if>
                    <p>
                        <i class="color-lump color-lump-3" style="width:${xxPercent?default('0%')};">
                        <span class="num">${mcodeSetting.getMcode("DM-XXTD", achi.xxtd!?default("1"))}</span>
                        </i>
                    </p>
                </div>
            </div>
            </#list>
        </#if>
            <div class="manual-grade-item mui-clearfix">
                <ul class="mui-clearfix">
                    <li>
                        <span class="tit">应上课</span>
                        <span class="did">${stuCheckAttendance.studyDate! }天</span>
                    </li>
                    <li>
                        <span class="tit">实际出席</span>
                        <span class="did"><#assign studyLastDays=(stuCheckAttendance.studyDate-stuCheckAttendance.businessVacation-stuCheckAttendance.illnessVacation)><#if studyLastDays lt 0>0<#else>${studyLastDays?default(0)}</#if>天</span>
                    </li>
                    <li>
                        <span class="tit">事假</span>
                        <span class="did">${stuCheckAttendance.businessVacation!}天</span>
                    </li>
                    <li>
                        <span class="tit">病假</span>
                        <span class="did">${stuCheckAttendance.illnessVacation!}天</span>
                    </li>
                    <li>
                    <#assign lateAndleaveEarly = stuCheckAttendance.late+stuCheckAttendance.leaveEarly>
                        <span class="tit">迟到早退</span>
                        <span class="did">${lateAndleaveEarly!}次</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="manual-item manual-item-6" title="我的身体心理素质">
        <div class="manual-item-body">
            <div class="manual-item-title">我的身体心理素质</div>
            <div class="manual-bmi mt-15">
                <ul class="manual-bmi-header mui-clearfix">
                    <li>身高：${stuHealthRecord.height!}cm</li>
                    <li>体重：${stuHealthRecord.weight!}kg</li>
                    <li>左眼视力：${stuHealthRecord.leftEye!}</li>
                    <li>右眼视力：${stuHealthRecord.rightEye!}</li>
                </ul>
                <div class="manual-bmi-echarts-outer">
                    <div class="manual-bmi-echarts-inner" id="echarts1"></div>
                </div>
            </div>
        </div>
    </div>
    <#if qualityOfMind?exists && qualityOfMind?size gt 0 >
    <div class="manual-item manual-item-1" title="我的思想品德素质">
        <div class="manual-item-body">
            <div class="manual-item-title">我的思想品德素质</div>
            <div class="manual-sxpd mt-10">
                <p class="tt" title="表扬项目"><img src="${request.contextPath}/studevelop/images/show/t1.png" alt="" /></p>
                    <#assign index  = 1>
                    <#list qualityOfMind as quality>
                        <#if quality[1]?exists && quality[1]  == 'A' >
                            <p class="letter-break" >${index}、${quality[0]}</p>
                            <#assign index  = index + 1 >
                        </#if>
                    </#list>
                <p class="tt" title="继续努力项目"><img src="${request.contextPath}/studevelop/images/show/t2.png" alt="" /></p>
                    <#assign index = 1 />
                    <#list qualityOfMind as quality>
                        <#if quality[1]?exists && quality[1] == "D">
                            <p class="letter-break" >${index}、${quality[0]}</p>
                            <#assign index  = index + 1 >
                        </#if>

                    </#list>
                <p class="tt" title="有待改进项目"><img src="${request.contextPath}/studevelop/images/show/t3.png" alt="" /></p>
                    <#assign index = 1 />
                    <#list qualityOfMind as quality>
                        <#if quality[1]?exists && quality[1] == "E">
                            <p class="letter-break" >${index}、${quality[0]}</p>
                            <#assign index  = index + 1 >
                        </#if>

                    </#list>
            </div>
        </div>
    </div>
    </#if>
    <div class="manual-item manual-item-2" title="每月在校表现">
        <div class="manual-item-body">
            <div class="manual-item-title">每月在校表现</div>
            <div class="manual-expression mt-15">
                <div class="mui-scroll-wrapper mui-slider-indicator mui-segmented-control mui-segmented-control-inverted">
                    <div class="mui-scroll">
                    <#assign first = 0 />
                    <#if yearMonths?exists && yearMonths?size gt 0 >
                        <#list yearMonths as yearMonth >
                            <#assign first = first +1 >
                            <a  class=" performanceMonth mui-control-item <#if first == 1>  mui-active </#if> " href="#expression${(yearMonth)?substring(0,4)}${(yearMonth)?substring(5)}">${yearMonth}</a>
                        </#list>
                    </#if>
                    </div>
                </div>
                <div class="manual-expression-body">
                <#if yearMonths?exists && yearMonths?size gt 0 >
                    <#assign firstAccess = 0 />
                    <#list yearMonths as yearMonth >
                        <#assign month = (yearMonth)?substring(5,7) />
                        <#if performanceMapList[month]?exists && (performanceMapList[month]?size gt 0)>
                            <#assign firstAccess = firstAccess+1 />
                            <#assign performanceItemMap = performanceMapList[month] >
                            <div class="mui-control-content <#if firstAccess ==1 > mui-active </#if> " id="expression${(yearMonth)?substring(0,4)}${(yearMonth)?substring(5)}">
                                <div class="manual-expression-item">

                                    <#if performanceItems?exists && (performanceItems?size gt 0) >
                                        <#list performanceItems as item>
                                            <#assign performance = performanceItemMap[item.id]?default('')  />
                                            <div class="mui-content-padded">
                                                <div class="mui-inline">${item.itemName!}</div>
                                                <div class="icons mui-inline">
                                                    <#if item.codeList?exists && (item.codeList?size gt 0) >
                                                        <#assign itemSize = item.codeList?size >
                                                        <#assign score = itemSize />
                                                        <#list item.codeList as code>
                                                            <#if performance !='' &&  code.id == performance.resultId >
                                                                <#break />
                                                            </#if>
                                                            <#assign score = score - 1 >
                                                        </#list>
                                                            <#list 1..itemSize as i>
                                                                <i data-index="${i}" class="mui-icon mui-icon-star-filled <#if  i lte score > mui-active</#if> "></i>
                                                            </#list>

                                                    </#if>
                                                </div>
                                            </div>
                                        </#list>
                                    </#if>
                                </div>
                            </div>
                        </#if>
                    </#list>
                </#if>
                        <div class="manual-expression-item letter-break"><p><#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
                            奖励：
                            <#list stuDevelopRewardsList as item>
                            ${item.rewardsname!}<#if item.remark?default('') !="" >,</#if>${item.remark!}&nbsp;&nbsp;&nbsp;
                            </#list>
                        </#if>
                            <br>
                        <#if stuDevelopPunishmentList?exists && (stuDevelopPunishmentList?size > 0)>
                            惩罚：
                            <#list stuDevelopPunishmentList as item>
                            ${item.punishname!}<#if item.remark?default('') !="" >,</#if>${item.remark!}&nbsp;&nbsp;&nbsp;
                            </#list>
                        </#if></p></div>
                        <div class="manual-expression-item letter-break"><p><#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.teacherEvalContent!}
				         </#if></p></div>


                </div>
            </div>
    </div>
</div>


<@schActivity class="manual-item-3" title="校园活动" activities = schoolActivity dataGroup="1" />
<@schActivity class="manual-item-4" title="主题活动" activities = themeActivity  dataGroup="2"/>
<@schActivity class="manual-item-5" title="班级活动" activities = classActivity  dataGroup="3"/>
<@honor class="manual-item-6" title="班级荣誉" detailes=classHonorDetails  dataGroup="4"/>
<@schActivity class="manual-item-1" title="校外表现" activities = schoolOutsideList  dataGroup="5"/>
<@schActivity class="manual-item-2" title="我的假期" activities = schoolHolodayList  dataGroup="6"/>
<@honor class="manual-item-3" title="我的荣誉" detailes=myHonorDetails dataGroup="7" />

</div>
<div class="manual-footer"></div>

<script src="${request.contextPath}/static/mui/js/jquery-1.9.1.min.js"></script>
<script src="${request.contextPath}/static/mui/js/mui.min.js"></script>
<script src="${request.contextPath}/static/mui/js/mui.zoom.js"></script>
<script src="${request.contextPath}/static/mui/js/mui.previewimage.js"></script>
<script src="${request.contextPath}/static/mui/js/img.ratio.js"></script>
<script type="text/javascript" charset="utf-8">
    mui.init();
    mui.previewImage();
    imgRatio2('.mui-photo-add',212/340);
    setTimeout(function(){
   	  imgRatio2('.mui-photo-add',212/340);
  	},500);
  	 setTimeout(function(){
   	  imgRatio2('.mui-photo-add',212/340);
  	 },2000);
  	 setTimeout(function(){
   	  imgRatio2('.mui-photo-add',212/340);
  	 },4000);
</script>
<script type="text/javascript">
    $(function(){
        var win_width = $(window).width();
        var header_height = win_width * (500/750);
        var footer_height = win_width * (344/750);
        var my_height = win_width * (800/750);
        var item_header_height = win_width * (64/750);
        var class_height = $('.manual-class .manual-item-avatar-list').outerHeight() + 40;

        $('.manual-header').css({width:win_width, height:header_height});
        if (header_height < 220) {
			$('.manual-header').addClass('manual-header-min');
		};
        $('.manual-footer').css({width:win_width, height:footer_height});
        $('.manual-item-my').css({width:win_width, height:my_height});
        $('.manual-item-header').css({width:win_width, height:item_header_height});
        $('.manual-class').css({height:class_height});

//        $("a.performanceMonth").click(function () {
//            $(this).addClass("mui-active");
//            $(this).siblings().removeClass("mui-active");
//            var str = $(this).attr("href").substr(1);
//            $("#expression" + str ).addClass("mui-active");
//            $("#expression" + str ).siblings().removeClass("mui-active");
//
//        })
    });
</script>
<script type="text/javascript">
    var myChart1 = echarts.init(document.getElementById('echarts1'));
    option1 = {
        tooltip: {},
        radar: {
            // shape: 'circle',
            name: {
                textStyle: {
                    color: '#9b9b9b',
                    backgroundColor: '#999',
                    borderRadius: 3,
                    padding: [3, 5]
                    
                }
            },
            indicator: [
                { name: '注意', max: 3.5},
                { name: '意志', max: 3.5},
                { name: '情绪', max: 3.5},
                { name: '思维', max: 3.5},
                { name: '记忆', max: 3.5},
                { name: '观察', max: 3.5}
            ]
        },
        series: [{
            name: '心理素质',
            type: 'radar',
	        data : [
	            {
	                value : [${stuHealthRecord.attention?default(0)}, ${stuHealthRecord.observation?default(0)}, ${stuHealthRecord.memory?default(0)}, ${stuHealthRecord.thinking?default(0)}, ${stuHealthRecord.mood?default(0)}, ${stuHealthRecord.will?default(0)}],
	                name : '心理素质',
	                label: {
                        normal: {
                            show: true,
                            formatter:function(params) {
                                switch(params.value){
                                	case 1: return '良好';
                                			break;
                                	case 2: return '中等';
                                			break;
                                	case 3: return '优秀';
                                			break;
                                }
                            }
                        }
                    }
	            }
	        ]
        }]
    };
    myChart1.setOption(option1);
</script>
</body>
</html>