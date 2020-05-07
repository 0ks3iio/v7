<div class="page-sidebar">
    <div class="page-sidebar-header ml10 mr10">
        <div class="input-group mt20">
            <input type="text" class="form-control">
            <a href="#" class="input-group-addon">
                <i class="fa fa-search"></i>
            </a>
        </div>
    </div>
    <#import "../macro/unitTree.ftl" as u />
    <@u.unitTree dataType='user' callback='changeUsermanageCurrentUnit'/>
</div>
</div>

<div class="page-content-inner">
    <div class="box box-default">
        <div class="box-body no-padding-top">
            <div class="tab-container">
                <div class="tab-header clearfix">
                    <ul class="nav nav-tabs nav-tabs-1">
                        <li class="active teacher-tab" id="teacherAccountIndex">
                            <a href="#/operation/user/manage/teacher">教师</a>
                        </li>
                        <li class="" id="studentIndex">
                            <a href="#/operation/user/manage/student">学生</a>
                        </li>
                        <li class="" id="familyIndex">
                            <a href="#/operation/user/manage/family">家长</a>
                        </li>
                    </ul>
                </div>
                <!-- tab切换开始 -->
                <div class="tab-content">
                    <div id="usermanageListContent" class="tab-pane active">

                    </div>
                </div><!-- tab切换结束 -->
            </div>
        </div>
    </div>
</div>
<script>
    //TODO 点击页面调用
    var usermanageCurrentUnitId = '';
    $(function () {

        routeUtils.add('/operation/user/manage/teacher', function () {
            loadTeacher();
        });
        routeUtils.add('/operation/user/manage/family', function () {
            loadFamily();
        });
        routeUtils.add('/operation/user/manage/student', function () {
            loadStudent();
        });

        routeUtils.go('/operation/user/manage/teacher');
    });

    function loadTeacher() {
        $('#usermanageListContent').load(_contextPath + '/operation/user/manage/teacher/index?unitId=' + usermanageCurrentUnitId)
        $('.teacher-tab').addClass('active').siblings('li').removeClass('active');
    }

    function loadFamily() {
        $('#usermanageListContent').load(_contextPath + '/operation/user/manage/family/index?unitId=' + usermanageCurrentUnitId)
        $('#familyIndex').addClass('active').siblings('li').removeClass('active');
    }

    function loadStudent() {
        $('#usermanageListContent').load(_contextPath + '/operation/user/manage/student/index?unitId=' + usermanageCurrentUnitId)
        $('#studentIndex').addClass('active').siblings('li').removeClass('active');
    }



    function changeUsermanageCurrentUnit(unitId) {
        console.log(unitId)
        usermanageCurrentUnitId = unitId;
        if ($('#teacherAccountIndex').hasClass('active')) {
            routeUtils.go('/operation/user/manage/teacher');
        }
        else if ($('#studentIndex').hasClass('active')) {
            routeUtils.go('/operation/user/manage/student');
        } else {
            routeUtils.go('/operation/user/manage/family');
        }
    }
</script>