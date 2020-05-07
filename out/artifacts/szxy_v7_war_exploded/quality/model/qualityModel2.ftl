<div class="box box-default">
    <div class="box-body">
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74041','奖励设置','/stuwork/studentReward/studentRewardSettingPage','74040');">奖励设置</a></div>
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74042','奖励录入','/stuwork/studentReward/studentRewardInputPage','74040');">奖励录入</a></div>
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74043','奖励查询','/stuwork/studentReward/studentRewardSearchPage','74040');">奖励查询</a></div>

        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74093','评语录入','/stuwork/evaluation/stu/index/page','74090');">评语录入</a></div>
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74094','评语汇总','/stuwork/evaluation/stat/index','74090');">评语汇总</a></div>
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74091','违纪录入','/stuwork/studentManage/punishScoreInfo','74090');">违纪录入</a></div>
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74092','违纪查询','/stuwork/studentManage/punishScoreQuery','74090');">违纪查询</a></div>
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74096','值周表现录入','/stuwork/weekCheckPerformance/pageIndex','74090');">值周表现录入</a></div>
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('74095','军训管理','/stuwork/militaryTraining/pageIndex','74090');">军训管理</a></div>
        <div class="filter"><a href="javascipt:void(0)" onclick="showListDiv('74097','学农管理','/stuwork/studyingFarming/pageIndex','74090');">学农管理</a></div>
        </div>
    </div>
</div>

<script>

    function showListDiv(id,name,url,dataId){
        openModel(id,name,'1','${request.contextPath}'+url,name,'','','');
        expand(id,dataId);
    }
    //跳转后左侧导航栏调整
    function expand(id,dataId){
        var liarr =$('.nav-list > li');
        liarr.each(function () {
            if($(this).hasClass('open')) {
                $(this).find('ul').stop().slideUp(150).removeClass('open');
                $(this).find('li').removeClass('open').removeClass('active');
                $(this).removeClass('open');
            }
            var a = $(this).find('a');
            if(a[0].dataset.show =='7033301'){
                $("#"+id).parent().parent().stop().slideDown(150).addClass('open');
                $("#"+dataId).parent().parent().slideDown(150).addClass('open');
                $("#"+id).parent().addClass('open');
                $("#"+dataId).parent().addClass('open');
                $("#"+dataId).parent().parent().parent().addClass("open");
            }
        })
    }

</script>