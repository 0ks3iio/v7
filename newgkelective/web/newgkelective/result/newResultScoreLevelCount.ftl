<#if isReferScoreImported?default(false)>
    <div class="result-header">
        <h3>选课情况</h3>
        <div class="explain-default clearfix" >
            <ul class="gk-student-stat">
                <li><span>已选择学生：<a href="javascript:void(0);" onclick="toChosenList('1','');">${chosenStuIdsNum!}</a>人</span></li>
                <li><span>未选择学生：<a href="javascript:void(0);" onclick="toChosenList('0','');">${noChosenStuIdsNum!}</a>人</span></li>
            </ul>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-6">
            <div class="result-chart-box">
                <div class="result-chart-title">总分排名统计</div>
                <div class="result-chart-input">
                <span>
                    <label>总分前(名)：</label>
                    <input type="text" class="edge-rank edge-1 form-control radius-4" style="width: 60px;display: inline-block;" value="${firstParam?default("200")}"/>
                </span>
                </div>
                <div class="result-item-header">
                    单科统计
                    <span>注：显示数据为选择人数</span>
                </div>
                <table class="result-item-table">
                    <tr>
                        <th></th>
                        <#if courseNameCountMap?exists && courseNameCountMap?size gt 0>
                        <#list courseNameCountMap?keys as key>
                            <th>${key!}</th>
                        </#list>
                        </#if>
                    </tr>
                    <tr>
                        <td>前${firstParam?default("200")}名</td>
                        <#if courseNameCountMap?exists && courseNameCountMap?size gt 0>
                            <#list courseNameCountMap?keys as key>
                                <td>${topSingleCourseTotalScoreRankMap[key]!}</td>
                            </#list>
                        </#if>
                    </tr>
                    <tr>
                        <td>全校</td>
                        <#if courseNameCountMap?exists && courseNameCountMap?size gt 0>
                            <#list courseNameCountMap?keys as key>
                                <td>${courseNameCountMap[key]!}</td>
                            </#list>
                        </#if>
                    </tr>
                </table>

                <div class="result-item-header">
                    组合统计
                    <span>注：总分前200名与全校选择人数对比统计</span>
                </div>
                <div>
                    <#if courseCombineNames?exists && courseCombineNames?length gt 0>
                        <div id="chart02" data-id="view-chart" <#if rowNums?default(0) lt 6>style="height:300px;"<#else>style="height:500px;"</#if>></div>
                    </#if>
                    <div data-id="view-table" style="display: none;">
                        <table class="result-item-table">
                            <tr>
                                <th>序号</th>
                                <th>科目</th>
                                <th>组合前200名选择人数</th>
                                <th>全校</th>
                            </tr>
                            <#if courseCombine?exists && courseCombine?size gt 0>
                                <#list courseCombine as one>
                                    <tr>
                                        <td>${one_index + 1}</td>
                                        <td>${one.subjectNames!}</td>
                                        <td>${topThreeCourseTotalScoreRankMap[one.subjectIdstr]!}</td>
                                        <td>${one.sumNum!}</td>
                                    </tr>
                                </#list>
                            </#if>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm-6">
            <div class="result-chart-box">
                <div class="result-chart-title">
                    单科排名与组合总分排名统计
                </div>
                <div class="result-chart-input">
                <span>
                    <label>单科前(名)：</label>
                    <input type="text" class="edge-rank edge-2 form-control radius-4" value="${secondParam?default("200")}" style="width: 60px;display: inline-block; margin-right: 30px"/>
                    <label>所选组合+语数外总分前(名)：</label>
                    <input type="text" class="edge-rank edge-3 form-control radius-4" value="${thirdParam?default("200")}" style="width: 60px;display: inline-block;"/>
                </span>
                </div>
                <div class="result-item-header">
                    单科统计
                    <span>例：物理年级前200，且又将物理作为选科的人数</span>
                </div>
                <table class="result-item-table">
                    <tr>
                        <th></th>
                        <#if courseNameCountMap?exists && courseNameCountMap?size gt 0>
                            <#list courseNameCountMap?keys as key>
                                <th>${key!}</th>
                            </#list>
                        </#if>
                    </tr>
                    <tr>
                        <td>前${secondParam?default("200")}名</td>
                        <#if courseNameCountMap?exists && courseNameCountMap?size gt 0>
                            <#list courseNameCountMap?keys as key>
                                <td>${topSingleCourseSingleScoreRankMap[key]!}</td>
                            </#list>
                        </#if>
                    </tr>
                </table>

                <div class="result-item-header">
                    组合统计
                    <span>例：物化生+语数英年级前200，且又将物化生作为选科的人数</span>
                </div>
                <div>
                    <#if courseCombineNames?exists && courseCombineNames?length gt 0>
                        <div id="chart03" data-id="view-chart" <#if rowNums?default(0) lt 6>style="height:340px;"<#else>style="height:540px;"</#if>></div>
                    </#if>
                    <div data-id="view-table" style="display: none;">
                        <table class="result-item-table">
                            <tr>
                                <th>序号</th>
                                <th>科目</th>
                                <th>组合前${thirdParam?default("200")}名选择人数</th>
                            </tr>
                            <#if courseCombineResort?exists && courseCombineResort?size gt 0>
                                <#list courseCombineResort as one>
                                    <tr>
                                        <td>${one_index + 1}</td>
                                        <td>${one.subjectNames!}</td>
                                        <td>${one.sumNum!}</td>
                                    </tr>
                                </#list>
                            </#if>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        $(function () {
            <#if courseCombineNames?exists && courseCombineNames?length gt 0>
            console.log(${courseCombineNames});
            var chart02 = document.getElementById("chart02");
            echarts.init(chart02).setOption({
                tooltip: {
                    trigger: "axis",
                    axisPointer: {
                        // 坐标轴指示器，坐标轴触发有效
                        type: "line" // 默认为直线，可选为：'line' | 'shadow'
                    },
                    formatter: "{b} {c1}人"
                },
                grid: {
                    top: "0",
                    left: "2%",
                    bottom: "10%",
                    containLabel: true
                },
                xAxis: {
                    type: "value",
                    position: "top",
                    axisLine: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    }
                },
                yAxis: {
                    type: "category",
                    data: [
                        ${courseCombineNames!}
                    ],
                    axisTick: {
                        show: false
                    },
                    axisLine: {
                        show: false
                    }
                },
                series: [
                    {
                        // For shadow
                        type: "bar",
                        itemStyle: {
                            normal: { color: "#DDEBF4" }
                        },
                        barGap: "-100%",
                        barCategoryGap: "40%",
                        data: [${courseCombineCounts!}],
                        barMaxWidth: '50',
                        animation: false,
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    color: "#333"
                                },
                                position: "right"
                            }
                        }
                    },
                    {
                    	name:'选课人数',
                        type: "bar",
                        data: [${courseTotalCounts!}],
                        barMaxWidth: '50',
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    color: "#333"
                                },
                                position: "right"
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: {
                                    type: "linear",
                                    x: 0,
                                    y: 0,
                                    x2: 1,
                                    y2: 0,
                                    colorStops: [
                                        {
                                            offset: 0,
                                            color: "#29A8FA" // 0% 处的颜色
                                        },
                                        {
                                            offset: 1,
                                            color: "#56E4FC" // 100% 处的颜色
                                        }
                                    ],
                                    global: false // 缺省为 false
                                }
                            }
                        }
                    }
                ]
            });

            var chart03 = document.getElementById("chart03");
            echarts.init(chart03).setOption({
                tooltip: {
                    trigger: "axis",
                    axisPointer: {
                        // 坐标轴指示器，坐标轴触发有效
                        type: "line" // 默认为直线，可选为：'line' | 'shadow'
                    },
                    formatter: "{b} {c1}人"
                },
                grid: {
                    top: "0",
                    left: "2%",
                    bottom: "10%",
                    containLabel: true
                },
                xAxis: {
                    type: "value",
                    position: "top",
                    axisLine: {
                        show: false
                    },
                    axisLabel: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    }
                },
                yAxis: {
                    type: "category",
                    data: [
                        ${courseCombineNames!}
                    ],
                    axisTick: {
                        show: false
                    },
                    axisLine: {
                        show: false
                    }
                },
                series: [
                    {
                        // For shadow
                        type: "bar",
                        itemStyle: {
                            normal: { color: "#DDEBF4" }
                        },
                        barGap: "-100%",
                        barCategoryGap: "40%",
                        data: [${courseCombineCounts!}],
                        barMaxWidth: '50',
                        animation: false,
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    color: "#333"
                                },
                                position: "right"
                            }
                        }
                    },
                    {
                    	name:'选课人数',
                        type: "bar",
                        data: [${coursePartCounts!}],
                        barMaxWidth: '50',
                        label: {
                            normal: {
                                show: true,
                                textStyle: {
                                    color: "#333"
                                },
                                position: "right"
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: {
                                    type: "linear",
                                    x: 0,
                                    y: 0,
                                    x2: 1,
                                    y2: 0,
                                    colorStops: [
                                        {
                                            offset: 0,
                                            color: "#29A8FA" // 0% 处的颜色
                                        },
                                        {
                                            offset: 1,
                                            color: "#56E4FC" // 100% 处的颜色
                                        }
                                    ],
                                    global: false // 缺省为 false
                                }
                            }
                        }
                    }
                ]
            });
            </#if>

            if(${showType?default("1")}=="2"){
                $("#view-table").addClass('active').siblings().removeClass('active');
                $('[data-id="view-table"]').show().siblings().hide();
            }

            $(".edge-rank").on("blur", function () {
                if (!parseInt($(this).val()) || parseInt($(this).val()) < 0) {
                    $(this).val("200");
                }
                if (parseInt($(this).val()) > 100000) {
                    $(this).val("100000");
                }
                $(this).val(parseInt($(this).val()));
                changeView();
            });

        });

        function toChosenList(type,subjectIds){
            var choiceId=$("#choiceId").val();
            var url ='${request.contextPath}/newgkelective/'+choiceId+'/chosen/tabHead/page?chosenType='+type+"&subjectIds="+subjectIds+"&scourceType=2";
            if(!$("#allClass").hasClass("selected")){
                var classIdArr = new Array();
                $(".clazz-mark a.selected").each(function(){
                    classIdArr.push($(this).attr("data-value"));
                })
                url += "&classIds="+classIdArr.join(",");
            }
            $("#showList").load(url);
        }
    </script>
<#else>
    <!--  没有内容-->
    <div style="text-align: center; margin-top: 20px">
        <img src="${request.contextPath}/static/images/7choose3/nocontent.png">
        <p>请于基础条件导入成绩并在选课设置中选择参考成绩后查看统计结果哦~</p>
    </div>
</#if>