<div class="box box-default">
    <div class="box-body">

        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('86906','总评成绩设置','/comprehensive/select/index/page','74120');">总评成绩设置</a></div>
        <div class="filter" style="margin-bottom: 10px;"><a href="javascipt:void(0)" onclick="showListDiv('86907','总评成绩汇总','/comprehensive/score/index/page','74120');">总评成绩汇总</a></div>
        <div class="filter" ><a href="javascipt:void(0)" onclick="showListDiv('86908','成绩折分计算','/comprehensive/qualityScoreStat/index/page','74120');">成绩折分计算</a></div>
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