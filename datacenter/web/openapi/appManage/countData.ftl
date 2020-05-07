<div class="page-content">
    <div class="row">
        <div class="col-md-12">
            <div class="box box-default">
                <div class="box-body no-padding">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <div class="row base-apply base-apply2" >
                                <div class="col-xs-5 col-sm-5 col-md-3">
                                    <div class="base-apply-img"><img src="${app.fullIcon!}" alt=""></div>
                                </div>
                                <div class="col-xs-7 col-sm-7 col-md-9">
                                    <div class="base-apply-title">${app.name!}</div>
                                    <div class="base-apply-time">${app.timeStr!}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <div class="row base-apply-stat">
                                <dl class="col-xs-12 col-sm-6 col-md-3 base-apply-stat-item">
                                    <dt>2563</dt>
                                    <dd>订阅人数统计</dd>
                                </dl>
                                <dl class="col-xs-12 col-sm-6 col-md-3 base-apply-stat-item">
                                    <dt>6</dt>
                                    <dd>昨日登录统计</dd>
                                </dl>
                                <dl class="col-xs-12 col-sm-6 col-md-3 base-apply-stat-item">
                                    <dt>39</dt>
                                    <dd>近30登录统计</dd>
                                </dl>
                                <dl class="col-xs-12 col-sm-6 col-md-3 base-apply-stat-item">
                                    <dt>1422</dt>
                                    <dd>总登录统计</dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="box box-default">
                <div class="box-header">
                    <h4 class="box-title">登录分析</h4>
                </div>
                <div class="box-body">
                    <div class="filter filter-f16">
                        <div class="filter-item">
                            <div class="btn-group" role="group" aria-label="...">
                                <button type="button" class="btn btn-default btn-blue">按日</button>
                                <button type="button" class="btn btn-default">按周</button>
                                <button type="button" class="btn btn-default">按月</button>
                            </div>
                        </div>
                        <div class="filter-item">
                            <span class="filter-name">日期：</span>
                            <div class="filter-content">
                                <div class="input-group">
                                    <input class="form-control date-range" id="reservation" type="text">
                                    <label for="reservation" class="input-group-addon">
                                        <i class="fa fa-calendar bigger-110"></i>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="filter-item">
                            <button class="btn btn-blue">查询</button>
                        </div>
                    </div>
                    <div id="echart-stat" style="height: 600px;margin-top: 30px;"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
      $(function(){
          $('#reservation').daterangepicker(null, function(start, end, label){});
          $('.btn-group .btn-default').click(function(e){
              e.preventDefault();
              $(this).addClass('btn-blue').siblings('.btn-default').removeClass('btn-blue');
          });
          
          // 基于准备好的dom，初始化echarts实例
          var myChart = echarts.init(document.getElementById('echart-stat'));

          // 指定图表的配置项和数据
          option = {
              title : {
                  text: '未来一周气温变化',
                  subtext: '纯属虚构'
              },
              tooltip : {
                  trigger: 'axis'
              },
              legend: {
                  data:['最高气温','最低气温']
              },
              toolbox: {
                  show : false,
                  feature : {
                      mark : {show: true},
                      dataView : {show: true, readOnly: false},
                      magicType : {show: true, type: ['line', 'bar']},
                      restore : {show: true},
                      saveAsImage : {show: true}
                  }
              },
              calculable : true,
              xAxis : [
                  {
                      type : 'category',
                      boundaryGap : false,
                      data : ['周一','周二','周三','周四','周五','周六','周日']
                  }
              ],
              yAxis : [
                  {
                      type : 'value',
                      axisLabel : {
                          formatter: '{value} °C'
                      }
                  }
              ],
              series : [
                  {
                      name:'最高气温',
                      type:'line',
                      data:[11, 11, 15, 13, 12, 13, 10],
                      markPoint : {
                          data : [
                              {type : 'max', name: '最大值'},
                              {type : 'min', name: '最小值'}
                          ]
                      },
                      markLine : {
                          data : [
                              {type : 'average', name: '平均值'}
                          ]
                      }
                  },
                  {
                      name:'最低气温',
                      type:'line',
                      data:[1, -2, 2, 5, 3, 2, 0],
                      markPoint : {
                          data : [
                              {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                          ]
                      },
                      markLine : {
                          data : [
                              {type : 'average', name : '平均值'}
                          ]
                      }
                  }
              ]
          };
                              
          // 使用刚指定的配置项和数据显示图表。
          myChart.setOption(option);
      })
  </script>