<div id="templateTabShow">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-default">
                <div class="box-body clearfix" style="padding-top: 0;">
                    <div class="row">
                        <div class="col-xs-12">
                            <ul class="nav nav-tabs nav-tabs-1">
                                <li class="active">
                                    <a href="javascript:void(0)" data-toggle="tab" onclick="itemOptionShow();">评价项目</a>
                                </li>
                                <li class="">
                                    <a href="javascript:void(0)" data-toggle="tab" onclick="healthItem();">身心项目</a>
                                </li>
                                <li class="">
                                    <a href="javascript:void(0)" data-toggle="tab" onclick="rewardAndInterest();">其他项目</a>
                                </li>
                                <li class="">
                                    <a href="javascript:void(0)" data-toggle="tab" onclick="schoolNotice();">开学通知</a>
                                </li>
                            </ul>
                        </div>
                        <div id="showTabList">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(function() {
        var url = '${request.contextPath}/stutotality/item/show/index?acadyear=${acadyear!}&semester=${semester!}&gradeId=${gradeId!}';
        $('#showTabList').load(url);
    });
    function itemOptionShow () {
        var url = '${request.contextPath}/stutotality/item/show/index';
        $('#showTabList').load(url);
    }
    function healthItem() {
        var url = '${request.contextPath}/stutotality/healthItem/list';
        $('#showTabList').load(url);
    }
    function rewardAndInterest() {
        var url = '${request.contextPath}/stutotality/rewardAndInterest/index';
        $('#showTabList'). load(url);
    }
    function schoolNotice() {
        var url = '${request.contextPath}/stutotality/schoolNotice/index';
        $('#showTabList'). load(url);
    }
</script>
