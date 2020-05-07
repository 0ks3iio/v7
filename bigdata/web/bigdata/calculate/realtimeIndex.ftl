 <div class="row">
    <div class="col-xs-12">
        <div class="box-default clearfix padding-20 text-center count-wrap js-scroll-height">
            <div class="row no-margin height-1of2">
                <div class="col-md-3">
                    <div class="bg-e75270">
                        <img src="${request.contextPath}/static/images/big-data/Flink-logo.png" alt="">
                        <p>Flink</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="bg-e1691a">
                        <img src="${request.contextPath}/static/images/big-data/spark-logo.png" alt="">
                        <p>Spark</p>
                    </div>
                </div>
            <div class="col-md-3 ">
                <div class="bg-000c40">
                    <img src="${request.contextPath}/static/images/big-data/Jstorm-logo.png" alt="">
                    <p>Jstorm</p>
                </div>
            </div>
        </div>
		<div class="row no-margin height-1of2">
                <div class="col-md-3">
                    <div class="bg-e75270">
                        <img src="${request.contextPath}/static/images/big-data/Flink-logo.png" alt="">
                        <p>Flink</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="bg-e1691a">
                        <img src="${request.contextPath}/static/images/big-data/spark-logo.png" alt="">
                        <p>Spark</p>
                    </div>
                </div>
            <div class="col-md-3 ">
                <div class="bg-000c40">
                    <img src="${request.contextPath}/static/images/big-data/Jstorm-logo.png" alt="">
                    <p>Jstorm</p>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function userTagManage(calculateType) {
        openModel('708324', '实时计算', 1, '${request.contextPath}/bigdata/calculate/realtime/frame?calculateType=' + calculateType, '大数据管理', '数据计算', null, false);
    }
    
    $(function(){
        $('.js-scroll-height').each(function () {
            $(this).css({
                height: $(window).height() - $(this).offset().top -20,
                overflow: 'auto'
            })
        });
    })
</script>