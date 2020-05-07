<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>成长手册</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="js/slick/slick.css">
    <style type="text/css">
        .letter-break{
            word-break:break-all;
        }
    </style>
</head>
<body>
<main class="main">
    <div class="container">
        <div class="box header">
            <img width="160" height="160" <#if stuIntroduction?exists && stuIntroduction.imgPath?exists> src="images/${stuIntroduction.imgPath!}"
             <#elseif student.sex?default(0) == 2>
             src = "images/user-female.png";
             <#else>
             src = "images/user-male.png";
             </#if> alt="">
        </div>

        <div class="box box-yellow myfile">
            <div class="box-header">
                <h3 class="box-title">我的档案</h3>
            </div>
            <div class="box-body">
                <ul class="myfile-list clearfix">
                    <li class="half">姓名：${student.studentName!}</li>
                    <li class="half">性别：${(xbMcodeMap[student.sex?default(0)?string].mcodeContent!)?if_exists}</li>
                    <li class="half">出生日期：${(student.birthday?string('yyyy-MM-dd'))!} </li>
                    <li class="half">班级：${student.className!}</li>
                    <#if stuIntroduction?exists >
                    <li class="letter-break">${stuIntroduction.speciality!}</li>
                    <li class="letter-break">${stuIntroduction.content!}</li>
                    </#if>
                </ul>
            </div>
        </div>

<#if family?exists && family.id?exists>
        <div class="box box-green">
            <div class="box-header">
                <h3 class="box-title">幸福的一家</h3>
            </div>
            <div class="box-body">
                <#if family.existsImgPath?default(false) && family.imgPath?exists>
	                <div class="img-box">
                        <img src="images/${family.imgPath!}" alt="">
    	            </div>
                </#if>
                <p class="paragraph letter-break">${family.parentContent!}</p>
            </div>
        </div>
</#if>

<#if schoolInfo?exists && schoolInfo.id?exists>
        <div class="box box-deepyellow">
            <div class="box-header">
                <h3 class="box-title">美丽的校园</h3>
            </div>
            <div class="box-body">
                <p class="paragraph text-indent letter-break">${schoolInfo.actRemark!}</p>
                <#if picList?exists && (picList?size gt 0) >
	                <div class="img-box-list clearfix">
                        <#list picList as pic>
                            <div class="img-box"><img width="350" height="278" src="images/${pic.id!}.${pic.extName!}" alt=""></div>
                        </#list>
    	            </div>
                </#if>
            </div>
        </div>
</#if>

        <div class="box box-purple">
            <div class="box-header">
                <h3 class="box-title">校长寄语</h3>
            </div>
            <div class="box-body">
                <div class="president">
                    <img src="images/${masterPic?default('master-pic.png')}" alt="">
                    <p>${myschool.schoolmaster!}</p>
                </div>
                <p class="paragraph text-indent letter-break">${(words.getWordsVal('words'+grade.gradeCode))!}</p>
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
                                <span class="bg-lightgreen">${classTeaching.teacherName!}</span>
                                <p>${classTeaching.subjectName!}老师</p>
                            </li>
                        </#list>
                    </#if>
                </ul>

                <div class="stu-box">
                    <ul class="stu-list clearfix">
                        <#if classmates?exists && (classmates?size gt 0) >
                            <#list classmates as classmate>
								<#assign stuimg = "" />
								<#if stuIntroductionMap?exists  && stuIntroductionMap?size gt 0 &&  stuIntroductionMap[classmate.id]?exists >
                                	<#assign stuimg = stuIntroductionMap[classmate.id].imgPath!>
                                </#if>
                                <li>
                                <img 
                                	<#if stuimg?default('') != ''>
                                            src="images/${stuimg!}"
                                    <#elseif classmate.sex?default(0) == 2>
                                            src="images/user-female.png"
                                    <#else>
                                            src="images/user-male.png"
                                    </#if>
                                 alt="">
                                <span>${classmate.studentName!}</span>
                                </li>
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
                <#if achilist?exists && (achilist?size gt 0 )>
                <ul class="legend-list clearfix">
                    <li><span class="gradient01"></span>平时成绩</li>
                    <li><span class="gradient02"></span>期末成绩</li>
                    <li><span class="gradient03"></span>学习态度</li>
                </ul>
                </#if>
                <ul class="subject-list">
                    <#if achilist?exists && (achilist?size gt 0 )>
                        <#list achilist as achi>
                            <li>
                                <span class="subject-name" style="width:70px;">${achi.subname!}</span>
                                <div class="subject-score">
                                    <#if achi.psachi?exists && achi.psachi!="">
	                                    <div class="subject-score-item">
	                                       <#-- <#assign psAchi = achi.psachi?number >
	                                        <#assign psFullMark = achi.psFullMark?number >
	                                        <#assign psPercent = psAchi/psFullMark >-->
	
	                                        <div class="progress gradient01" style="width:${(achi.psPercent)?string('percent')};"></div>
	                                        <span>${achi.psachi!}</span>
	                                    </div>
                                    </#if>
                                    <#if achi.qmachi?exists && achi.qmachi!="">
	                                    <div class="subject-score-item">
	                                        <#-- <#assign qmAchi = achi.qmachi?number >
	                                        <#assign qmFullMark = achi.qmFullMark?number >
	                                        <#assign qmPercent = qmAchi/qmFullMark >-->
	                                        <div class="progress gradient02" style="width:${(achi.qmPercent)?string('percent')};"></div>
	                                        <span>${achi.qmachi!}</span>
	                                    </div>
                                    </#if>
                                    <div class="subject-score-item">
                                        <#assign xxPercent = "0%" >
                                        <#if achi.xxtd!?default("") == '1'>
                                            <#assign xxPercent = "100%" >
                                        <#elseif achi.xxtd!?default("") == '2'>
                                            <#assign xxPercent = "75%" >
                                        <#elseif achi.xxtd!?default("") == '3' >
                                            <#assign xxPercent = "50%" >
                                        <#elseif achi.xxtd!?default("") == '4'>
                                            <#assign xxPercent = "25%" >
                                        </#if>
                                        <div class="progress gradient03" style="width:${xxPercent!};"></div>
                                        <span>
                                        <#if xxtdMcodeMap?exists>
                                        ${(xxtdMcodeMap[achi.xxtd!?default("1")].mcodeContent!)?if_exists}
                                        </#if>
                                        </span>
                                    </div>
                                </div>
                            </li>
                        </#list>
                    </#if>
                    <li>
                        <ul class="attend-list clearfix">
                            <li><span>应上课</span><span>${stuCheckAttendance.studyDate}天</span></li>
                            <li><span>实际出席</span><span><strong>${stuCheckAttendance.studyDate - stuCheckAttendance.businessVacation - stuCheckAttendance.illnessVacation}天</strong></span></li>
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
<#if qualityOfMind?exists && qualityOfMind?size gt 0>
        <div class="box box-green">
            <div class="box-header">
                <h3 class="box-title">我的思想品德素质</h3>
            </div>
            <div class="box-body">
                <div class="moral-item">
                    <span><i class="moral-icon moral-icon-great"></i>表扬项目</span>
                    
                        <#assign index=0 />
                        <#list qualityOfMind as quality>
                            <#if quality[1]?exists && quality[1] == 'A'>
                                <p class="letter-break">${index +1}、${quality[0]}</p>
                                <#assign index=index+1>
                            </#if>

                        </#list>
                </div>
                <div class="moral-item">
                    <span><i class="moral-icon moral-icon-star"></i>继续努力项目</span>
                    <#assign index=0 />
                    <#list qualityOfMind as quality>
                        <#if quality[1]?exists && quality[1] == 'D'>
                            <p class="letter-break">${index +1}、${quality[0]}</p>
                            <#assign index=index+1>
                        </#if>
                    </#list>
                </div>
                <div class="moral-item">
                    <span><i class="moral-icon moral-icon-sigh"></i>有待改进项目</span>
                    <#assign index=0 />
                    <#list qualityOfMind as quality>
                        <#if quality[1]?exists && quality[1] == 'E'>
                            <p class="letter-break">${index +1}、${quality[0]}</p>
                            <#assign index=index+1>
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
                        <p class="letter-break" ><#if stuDevelopRewardsList?exists && (stuDevelopRewardsList?size > 0)>
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
                        <p class="letter-break" ><#if stuEvaluateRecord?exists>
                         ${stuEvaluateRecord.teacherEvalContent!}
				    </#if>
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <#if schoolActivity?exists && (schoolActivity?size gt 0) >
        <div class="box box-purple">
            <div class="box-header">
                <h3 class="box-title">校园活动</h3>
            </div>
            <div class="box-body">
                    <#list schoolActivity as activity>
                        <h3 class="img-box-title">${activity.actTheme!}</h3>
                        <div class="img-box">
                            <div class="slider-box">
                                <div class="slider-btn"><a href="" class="slider-prev"></a><a href="" class="slider-next"></a></div>
                                <span class="slider-counter"></span>
                                <div class="slider">
                                    <#if activityMap?exists && activityMap[activity.id]?exists >
                                        <#assign schAtts = activityMap[activity.id] />
                                        <#if schAtts?exists && (schAtts?size gt 0) >
                                            <#list schAtts as att>
                                                <div class="img-wrap" style="background-image: url(images/${att.id!}.${att.extName!})">

                                                </div>
                                            </#list>

                                        </#if>
                                    </#if>
                                </div>
                            </div>
                            <p class="letter-break">${activity.actRemark!}</p>
                        </div>
                    </#list>
            </div>
        </div>
        </#if>
        
        <#if themeActivity?exists && (themeActivity?size gt 0) >
        <div class="box box-lightblue">
            <div class="box-header">
                <h3 class="box-title">主题活动</h3>
            </div>
            <div class="box-body">
            
                <#list themeActivity as activity>
                    <h3 class="img-box-title">${activity.actTheme!}</h3>
                    <div class="img-box">
                        <div class="slider-box">
                            <div class="slider-btn"><a href="" class="slider-prev"></a><a href="" class="slider-next"></a></div>
                            <span class="slider-counter"></span>
                            <div class="slider">
                                <#if activityMap?exists && activityMap[activity.id]?exists >
                                    <#assign schAtts = activityMap[activity.id] />
                                    <#if schAtts?exists && (schAtts?size gt 0) >
                                        <#list schAtts as att>
                                            <div class="img-wrap" style="background-image: url(images/${att.id!}.${att.extName!})">

                                            </div>
                                        </#list>

                                    </#if>
                                </#if>
                            </div>
                        </div>
                        <p class="letter-break">${activity.actRemark!}</p>
                    </div>
                </#list>
            
            </div>
        </div>
        </#if>
        
        <#if classActivity?exists && (classActivity?size gt 0) >
        <div class="box box-deepblue">
            <div class="box-header">
                <h3 class="box-title">班级活动</h3>
            </div>
            <div class="box-body">
            
                <#list classActivity as activity>
                    <h3 class="img-box-title">${activity.actTheme!}</h3>
                    <div class="img-box">
                        <div class="slider-box">
                            <div class="slider-btn"><a href="" class="slider-prev"></a><a href="" class="slider-next"></a></div>
                            <span class="slider-counter"></span>
                            <div class="slider">
                                <#if activityMap?exists && activityMap[activity.id]?exists >
                                    <#assign schAtts = activityMap[activity.id] />
                                    <#if schAtts?exists && (schAtts?size gt 0) >
                                        <#list schAtts as att>
                                            <div class="img-wrap" style="background-image: url(images/${att.id!}.${att.extName!})">

                                            </div>
                                        </#list>

                                    </#if>
                                </#if>
                            </div>
                        </div>
                        <p class="letter-break">${activity.actRemark!}</p>
                    </div>
                </#list>
            
            </div>
        </div>
        </#if>
        
        <#if classHonorDetails?exists && (classHonorDetails?size gt 0) >
        <div class="box box-yellow">
            <div class="box-header">
                <h3 class="box-title">班级荣誉</h3>
            </div>
            <div class="box-body">
                <ul class="honor-list">
                        <#list classHonorDetails as classHonor>
                            <li><img src="images/${classHonor.id!}.${classHonor.extName!}" alt=""></li>
                        </#list>
                </ul>
            </div>
        </div>
        </#if>
        
        <#if schoolOutsideList?exists && (schoolOutsideList?size gt 0) >
        <div class="box box-green">
            <div class="box-header">
                <h3 class="box-title">校外表现</h3>
            </div>
            <div class="box-body">
                <#list schoolOutsideList as activity>
                    <h3 class="img-box-title">${activity.actTheme!}</h3>
                    <div class="img-box">
                        <#if activityMap?exists && activityMap[activity.id]?exists >
                        <div class="slider-box">
                            <div class="slider-btn"><a href="" class="slider-prev"></a><a href="" class="slider-next"></a></div>
                            <span class="slider-counter"></span>
                            <div class="slider">
                                    <#assign schAtts = activityMap[activity.id] />
                                    <#if schAtts?exists && (schAtts?size gt 0) >
                                        <#list schAtts as att>
                                            <div class="img-wrap" style="background-image: url(images/${att.id!}.${att.extName!})">

                                            </div>
                                        </#list>
                                    </#if>
                            </div>
                        </div>
                        </#if>
                        <p class="letter-break">${activity.actRemark!}</p>
                    </div>
                </#list>
            
            </div>
        </div>
        </#if>
        
        <#if schoolHolodayList?exists && (schoolHolodayList?size gt 0) >
        <div class="box box-deepyellow">
            <div class="box-header">
                <h3 class="box-title">我的假期</h3>
            </div>
            <div class="box-body">
            
                <#list schoolHolodayList as activity>
                    <h3 class="img-box-title">${activity.actTheme!}</h3>
                    <div class="img-box">
                        <div class="slider-box">
                            <div class="slider-btn"><a href="" class="slider-prev"></a><a href="" class="slider-next"></a></div>
                            <span class="slider-counter"></span>
                            <div class="slider">
                                <#if activityMap?exists && activityMap[activity.id]?exists >
                                    <#assign schAtts = activityMap[activity.id] />
                                    <#if schAtts?exists && (schAtts?size gt 0) >
                                        <#list schAtts as att>
                                            <div class="img-wrap" style="background-image: url(images/${att.id!}.${att.extName!})">
                                            </div>
                                        </#list>
                                    </#if>
                                </#if>
                            </div>
                        </div>
                        <p class="letter-break">${activity.actRemark!}</p>
                    </div>
                </#list>
            
            </div>
        </div>
        </#if>
        
        <#if myHonorDetails?exists && (myHonorDetails?size gt 0)>
        <div class="box box-lightblue">
            <div class="box-header">
                <h3 class="box-title">我的荣誉</h3>
            </div>
            <div class="box-body">
                <ul class="honor-list">
                    
                        <#list myHonorDetails as imag>
                            <li><img src="images/${imag.id!}.${imag.extName!}" alt=""></li>
                        </#list>
                    
                </ul>
            </div>
        </div>
        </#if>
        <div class="footer"></div>
    </div>
</main>

<script src="js/jquery.min.js"></script>
<script src="js/echarts.min.js"></script>
<script src="js/slick/slick.min.js"></script>


<script type="text/javascript">
    $(function(){
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