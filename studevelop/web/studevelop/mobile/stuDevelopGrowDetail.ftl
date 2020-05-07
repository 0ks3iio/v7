<#macro developGrowActivity title=""  style="" activities=[]  >
<#if activities?exists && (activities?size gt 0) >
<div class="box ${style}">
    <div class="box-header">
        <h3 class="box-title">${title!}</h3>
    </div>
    <div class="box-body">
        
            <#list activities as activity>
                <h3 class="img-box-title">${activity.actTheme!}</h3>
                <div class="img-box">
                    <#if activityMap[activity.id]?exists >
                    <div class="slider-box">
                        <div class="slider-btn"><a href="" class="slider-prev"></a><a href="" class="slider-next"></a></div>
                        <span class="slider-counter"></span>
                        <div class="slider">
                                <#assign schAtts = activityMap[activity.id] />
                                <#if schAtts?exists && (schAtts?size gt 0) >
                                    <#list schAtts as att>
                                        <div class="img-wrap" style="background-image: url(${request.contextPath}/studevelop/common/attachment/showPic?id=${att.id!}&showOrigin=1)">

                                        </div>
                                    </#list>
                                </#if>
                        </div>
                    </div>
                    </#if>
                    <p>${activity.actRemark!}</p>
                </div>
            </#list>
    </div>
</div>
</#if>
</#macro>

<#macro developPerformance title=""  style="" activities=[]  >
<#if activities?exists && (activities?size gt 0) >
<div class="box ${style}">
    <div class="box-header">
        <h3 class="box-title">${title!}</h3>
    </div>
    <div class="box-body">
        
            <#list activities as activity>
                <h3 class="img-box-title">${activity.title!}</h3>
                <div class="img-box">
                    <#if activityMap[activity.id]?exists >
                        <div class="slider-box">
                            <div class="slider-btn"><a href="" class="slider-prev"></a><a href="" class="slider-next"></a></div>
                            <span class="slider-counter"></span>
                            <div class="slider">
                                <#assign schAtts = activityMap[activity.id] />
                                <#if schAtts?exists && (schAtts?size gt 0) >
                                    <#list schAtts as att>
                                        <div class="img-wrap" style="background-image: url(${request.contextPath}/studevelop/common/attachment/showPic?id=${att.id!}&showOrigin=1)">

                                        </div>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                    </#if>
                    <p>${activity.content!}</p>
                </div>
            </#list>
    </div>
</div>
</#if>
</#macro>
<#macro studevelopHonor title="" style="" detailes=[]>
<#if detailes?exists && (detailes?size gt 0) >
<div class="box ${style!}">
    <div class="box-header">
        <h3 class="box-title">${title!}</h3>
    </div>
    <div class="box-body">
        <ul class="honor-list">
                <#list detailes as detail>
                    <li><img src="${request.contextPath}/studevelop/common/attachment/showPic?id=${detail.id!}&showOrigin=1" alt=""></li>
                </#list>
        </ul>
    </div>
</div>
</#if>
</#macro>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>成长手册</title>
    <link rel="stylesheet" href="${request.contextPath}/static/components/slick/slick.css">
</head>
<body>
<main class="main">

	    <#if stuIntroduction?exists>
			<#assign stuRel = stuIntroduction.hasRelease?default(0) />	
		</#if>
        <div class="buttons">
            <button id="exportBtn">导出HTML</button>
            <button id="releaseBtn"><#if stuRel==1>取消发布<#else>发布给家长</#if></button>
        </div>

    <div class="container">
        <div class="box header">
        <#if stuIntroduction?exists && stuIntroduction.imgPath?exists > <img  width="160" height="160"  class="card-stu-img"  src="${stuIntroduction.imgPath!}" alt=""> </#if>
        </div>

        <div class="box box-yellow myfile">
            <div class="box-header">
                <h3 class="box-title">我的档案</h3>
            </div>
            <div class="box-body">
                <ul class="myfile-list clearfix">
                    <li class="half">姓名：${student.studentName!}</li>
                    <li class="half">性别：${mcodeSetting.getMcode("DM-XB","${student.sex!}")}</li>
                    <li class="half">出生日期：${(student.birthday?string('yyyy-MM-dd'))!} </li>
                    <li class="half">年级：${student.className!}</li>
                    <#if stuIntroduction?exists>
                    <li>${stuIntroduction.speciality!}</li>
                    <li>${stuIntroduction.content!}</li>
                    </#if>
                </ul>
            </div>
        </div>

        <div class="box box-green">
            <div class="box-header">
                <h3 class="box-title">幸福的一家</h3>
            </div>
            <div class="box-body">
                <#if family.existsImgPath?default(false) && family.imgPath?exists>
                    <div class="img-box">
                            <img src="${family.imgPath!}" alt="">
                    </div>
                </#if>
                <p class="paragraph">${family.parentContent!}</p>
            </div>
        </div>

        <div class="box box-deepyellow">
            <div class="box-header">
                <h3 class="box-title">美丽的校园</h3>
            </div>
            <div class="box-body">
                <p class="paragraph text-indent">${schoolInfo.actRemark!}</p>
                <div class="img-box-list clearfix">
                    <#if picList?exists && (picList?size gt 0) >
                        <#list picList as pic>
                            <div class="img-box"><img width="350" height="278" src="${request.contextPath}/studevelop/common/attachment/showPic?id=${pic.id!}&showOrigin=1" alt=""></div>
                        </#list>
                    </#if>
                </div>
            </div>
        </div>

        <div class="box box-purple">
            <div class="box-header">
                <h3 class="box-title">校长寄语</h3>
            </div>
            <div class="box-body">
                <div class="president">
                    <#if wordsAtts?exists && wordsAtts?size gt 0>
                        <img src="${request.contextPath}/studevelop/words/masterpic/show" alt="">
                    </#if>
                    <p>${myschool.schoolmaster!}</p>
                </div>
                <p class="paragraph text-indent">${(words.getWordsVal('words'+grade.gradeCode))!}</p>

            </div>
        </div>

        <div class="box box-lightblue">
            <div class="box-header">
                <h3 class="box-title">班级大家庭</h3>
            </div>
            <div class="box-body">
                <ul class="teacher-list clearfix">
                    <#if classTeachingList?exists && (classTeachingList?size gt 0) >
                        <#list classTeachingList as classTeaching>
                            <li>
                                <span class="bg-lightgreen"><#if '' == classTeaching.teacherName?default('')>&nbsp;<#else>${classTeaching.teacherName!}</#if></span>
                                <p>${classTeaching.subjectName!}老师</p>
                            </li>
                        </#list>
                    </#if>


                </ul>

                <div class="stu-box">
                    <ul class="stu-list clearfix">
                        <#if classmates?exists && (classmates?size gt 0) >
                            <#list classmates as classmate>

                                <li>
                                <#if stuIntroductionMap?exists  && stuIntroductionMap?size gt 0 &&  stuIntroductionMap[classmate.id]?exists >
                                	<#assign stuimg = stuIntroductionMap[classmate.id].imgPath!>
                                </#if>
                                <img
                                    <#if stuimg?default('') != ''>
                                            src="${stuimg!}"
                                    <#elseif classmate.sex?default(0) == 2>
                                            src="${request.contextPath}/studevelop/images/user-female.png"
                                    <#else>
                                            src="${request.contextPath}/studevelop/images/user-male.png"
                                    </#if>

                                        alt=""><span>${classmate.studentName!}</span></li>
                                <#assign stuimg="" />
                            </#list>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>

        <div class="box box-deepblue">
            <div class="box-header">
                <h3 class="box-title">我的成绩</h3>
            </div>
            <div class="box-body">
                <ul class="legend-list clearfix">
                    <li><span class="gradient01"></span>平时成绩</li>
                    <li><span class="gradient02"></span>期末成绩</li>
                    <li><span class="gradient03"></span>学习态度</li>
                </ul>
                <ul class="subject-list">
                    <#if achilist?exists && (achilist?size gt 0 )>
                        <#list achilist as achi>
                            <li>
                                <span class="subject-name">${achi.subname!}</span>
                                <div class="subject-score">
                                    <div class="subject-score-item">
                                        <#assign psAchi = achi.psachi?number >
                                        <#assign psFullMark = achi.psFullMark?number >
                                        <#assign psPercent = psAchi/psFullMark >

                                        <div class="progress gradient01" style="width:${psPercent?string('percent')};"></div>
                                        <span>${achi.psachi!}</span>
                                    </div>
                                    <div class="subject-score-item">
                                        <#assign qmAchi = achi.qmachi?number >
                                        <#assign qmFullMark = achi.qmFullMark?number >
                                        <#assign qmPercent = qmAchi/qmFullMark >
                                        <div class="progress gradient02" style="width:${qmPercent?string('percent')};"></div>
                                        <span>${achi.qmachi!}</span>
                                    </div>
                                    <div class="subject-score-item">
                                        <#if achi.xxtd!?default("1") == '1'>
                                            <#assign xxPercent = "100%" >
                                        <#elseif achi.xxtd!?default("1") == '2'>
                                            <#assign xxPercent = "75%" >
                                        <#elseif achi.xxtd!?default("1") == '3' >
                                            <#assign xxPercent = "50%" >
                                        <#elseif achi.xxtd!?default("1") == '4'>
                                            <#assign xxPercent = "25%" >
                                        </#if>
                                        <div class="progress gradient03" style="width:${xxPercent!};"></div>
                                        <span>${mcodeSetting.getMcode("DM-XXTD", achi.xxtd!?default("1"))}</span>
                                    </div>
                                </div>
                            </li>
                        </#list>
                    </#if>
                    <li>
                        <ul class="attend-list clearfix">
                            <li><span>应上课</span><span>${stuCheckAttendance.studyDate + stuCheckAttendance.businessVacation + stuCheckAttendance.illnessVacation  }天</span></li>
                            <li><span>实际出席</span><span><strong>${stuCheckAttendance.studyDate!}天</strong></span></li>
                            <li><span>事假</span><span>${stuCheckAttendance.businessVacation!}天</span></li>
                            <li><span>病假</span><span>${stuCheckAttendance.illnessVacation!}天</span></li>
                            <#assign lateAndleaveEarly = stuCheckAttendance.late+stuCheckAttendance.leaveEarly>
                            <li><span>迟到早退</span><span>${lateAndleaveEarly!}次</span></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>

        <div class="box box-yellow">
            <div class="box-header">
                <h3 class="box-title">我的身心素质</h3>
            </div>
            <div class="box-body">
                <div class="chart-box">
                    <ul class="health-list clearfix">
                        <li>身高：${stuHealthRecord.height!}cm </li>
                        <li>体重：${stuHealthRecord.weight!}kg</li>
                        <li>左眼视力：${stuHealthRecord.leftEye!}</li>
                        <li>右眼视力：${stuHealthRecord.rightEye!}</li>
                    </ul>
                    <div id="chart01" style="height:580px;"></div>
                </div>
            </div>
        </div>

<#if qualityOfMind?exists && qualityOfMind?size gt 0 >
        <div class="box box-green">
            <div class="box-header">
                <h3 class="box-title">我的思想品德素质</h3>
            </div>
            <div class="box-body">
                <div class="moral-item">
                    <span><i class="moral-icon moral-icon-great"></i>表扬项目</span>
                    
                        <#assign index  = 1>
                        <#list qualityOfMind as quality>
                            <#if quality[1]?exists && quality[1]  == 'A' >
                                <p>${index}、${quality[0]}</p>
                                <#assign index  = index + 1 >
                            </#if>

                        </#list>
                </div>
                <div class="moral-item">
                    <span><i class="moral-icon moral-icon-star"></i>继续努力项目</span>
                    <#assign index = 1 />
                    <#list qualityOfMind as quality>
                        <#if quality[1]?exists && quality[1] == "D">
                            <p>${index}、${quality[0]}</p>
                            <#assign index  = index + 1 >
                        </#if>

                    </#list>
                </div>
                <div class="moral-item">
                    <span><i class="moral-icon moral-icon-sigh"></i>有待改进项目</span>
                    <#assign index = 1 />
                    <#list qualityOfMind as quality>
                        <#if quality[1]?exists && quality[1] == "E">
                            <p>${index}、${quality[0]}</p>
                            <#assign index  = index + 1 >
                        </#if>

                    </#list>
                
                </div>
            </div>
        </div>
</#if>

        <div class="box box-deepyellow">
            <div class="box-header">
                <h3 class="box-title">每月在校表现</h3>
            </div>
            <div class="box-body">
                <div class="performance">
                    <div class="performance-header">
                        <ul class="tab clearfix">
                            <#assign first = 0 />
                            <#if yearMonths?exists && yearMonths?size gt 0 >
                              <#list yearMonths as yearMonth >
                                  <#assign first = first +1 >
                                  <li <#if first == 1> class="active" </#if>  ><a href="#${(yearMonth)?substring(5,7)}" <#if first == 1> class="active" </#if> >${yearMonth}</a></li>
                                </#list>
                            </#if>
                        </ul>
                    </div>
                    <div class="performance-body">
                        <div class="tab-content">
                            <#if yearMonths?exists && yearMonths?size gt 0 >
                                <#assign firstAccess = 0 />
                                <#list yearMonths as yearMonth >

                                    <#assign month = (yearMonth)?substring(5,7) />
                                    <#if performanceMapList[month]?exists && (performanceMapList[month]?size gt 0)>
                                        <#assign firstAccess = firstAccess+1 />
                                        <#assign performanceItemMap = performanceMapList[month] >
                                        <div id="${month!}" class="tab-pane  <#if firstAccess ==1 > active </#if> ">
                                            <ul class="rate-list">
                                                <#if performanceItems?exists && (performanceItems?size gt 0) >
                                                    <#list performanceItems as item>
                                                        <#assign performance = performanceItemMap[item.id]?default('')  />
                                                        <li class="rate-item">
                                                            <span class="rate-name">${item.itemName!}</span>
                                                            <#if item.codeList?exists && (item.codeList?size gt 0) >
                                                                <#assign itemSize = item.codeList?size >
                                                                <#assign score = itemSize />
                                                                <#list item.codeList as code>
                                                                    <#if performance !='' &&  code.id == performance.resultId >
                                                                        <#break />
                                                                    </#if>
                                                                    <#assign score = score - 1 >
                                                                </#list>
                                                                <div class="rate-result" data-score="${score!}">
                                                                    <#list 1..itemSize as i>
                                                                        <span  ></span>
                                                                    </#list>
                                                                </div>
                                                            </#if>
                                                        </li>
                                                    </#list>

                                                </#if>
                                            </ul>
                                        </div>
                                    </#if>
                                </#list>
                            </#if>
                        </div>
                    </div>
                </div>
                <div class="performance">
                    <div class="performance-body">
                        <p><#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
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
                        </#if></p>
                        <p><#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.teacherEvalContent!}
				    </#if></p>
                    </div>
                </div>
            </div>
        </div>
        <@developGrowActivity title="校园活动"  style="box-purple" activities = schoolActivity  />
        <@developGrowActivity title="主题活动"  style="box-lightblue" activities = themeActivity  />
        <@developGrowActivity title="班级活动"  style="box-deepblue" activities = classActivity  />
        <@studevelopHonor title="班级荣誉" style="box-yellow" detailes=classHonorDetails />
        <@developPerformance title="校外表现"  style="box-green" activities = schoolOutsideList  />
        <@developPerformance title="我的假期"  style="box-deepyellow" activities = schoolHolodayList  />
        <@studevelopHonor title="我的荣誉" style="box-lightblue" detailes=myHonorDetails />

        <div class="footer"></div>
    </div>
</main>

<script src="${request.contextPath}/static/jscrop/js/jquery.min.js"></script>
<script src="${request.contextPath}/static/echarts/echarts.min.js"></script>
<script src="${request.contextPath}/static/components/slick/slick.min.js"></script>


<script type="text/javascript">
    $(function(){
        $('#exportBtn').on('click',function(){
        	window.open("${request.contextPath}/studevelop/devdoc/export?acadyear=${acadyear!}&semester=${semester!}&stuIds=${studentId!}");
        });
        
        $('#releaseBtn').on('click',function(){
        	var release = 0;
        	<#if stuRel?default(0) == 0>
        		release = 1;
        	</#if>
        	$.ajax({
	    		url:"${request.contextPath}/studevelop/devdoc/release",
	    		dataType:"json",
	            type:"post",
	            data:{"acadyear":"${acadyear!}","semester":"${semester!}","stuIds":"${studentId!}" ,"release":release},
	            success:function(data){
	                layer.closeAll();
	                var jsonO = data;
	                if(!jsonO.success){
	                    showWarnMsg(jsonO.msg);
	                    return;
	                }else{
	                    window.location.href=window.location.href;
	                }
	            },
	            error:function(XMLHttpRequest ,textStatus,errorThrown){}
	    	});
        });
        
        var option = {
            tooltip: {},
            radar: {
                name: {
                    textStyle: {
                        color: '#9b9b9b',
                        padding: [3, 5],
                        fontSize: 26
                    }
                },
                indicator: [
                    { name: '注意', max: 3},
                    { name: '意志', max: 3},
                    { name: '情绪', max: 3},
                    { name: '思维', max: 3},
                    { name: '记忆', max: 3},
                    { name: '观察', max: 3}
                ]
            },
            series: [{
                name: '预算 vs 开销',
                type: 'radar',
                // areaStyle: {normal: {}},
                data : [
                    {
                        value : [${stuHealthRecord.attention?default(0)}, ${stuHealthRecord.observation?default(0)}, ${stuHealthRecord.memory?default(0)}, ${stuHealthRecord.thinking?default(0)}, ${stuHealthRecord.mood?default(0)}, ${stuHealthRecord.will?default(0)}],
                        name : '实际开销（Actual Spending）'
                    }
                ]
            }]
        };
        echarts.init(document.getElementById('chart01')).setOption(option);

        // tab切换
        $('.tab a').on('click',function(e){
            e.preventDefault();
            var id = $(this).attr('href').split('#')[1];
            $(this).parent().addClass('active').siblings().removeClass('active');
            $('#'+id).addClass('active').siblings().removeClass('active');
        })

        // 评分
        $('[data-score]').each(function(){
            var stars = $(this).find('span');
            for(var i=0; i< $(this).data('score'); i++){
                stars.eq(i).addClass('active');
            }
        })

        // 幻灯片
        if($('.slider').length > 0){
            $('.slider').each(function(){
                var that = $(this),
                        counter = that.closest('.slider-box').find('.slider-counter');
                var sl = that.slick({
                    fade: true,
                    prevArrow: that.parent().find('.slider-prev'),
                    nextArrow: that.parent().find('.slider-next'),
                    autoplay:true
                });
                if(that.find('.slick-slide').length > 0){
                	counter.text(1 + '/' + that.find('.slick-slide').length);
                }
                $(sl).on('afterChange', function(slick, currentSlide){
                    counter.text(currentSlide.currentSlide + 1 + '/' + currentSlide.slideCount);
                })
            })
        }
    })

</script>
</body>
</html>