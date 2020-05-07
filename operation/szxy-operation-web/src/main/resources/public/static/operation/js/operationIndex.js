/*
 * Operation 首页专用JS
 */
(function (root) {

    let indexUrl = window.location.href;
    if (indexUrl.indexOf('#') < 0 && indexUrl.endsWith('/')) {
        indexUrl = window.location.href + '#/';
    } else if (indexUrl.indexOf('#') < 0 && !indexUrl.endsWith('/')) {
        indexUrl = window.location.href + '/#/';
    }
    let router = new root.router({
        //TODO 这里写的有问题
        "/": function () {
            if (window.location.href == indexUrl) {

            } else {
                window.location.href = indexUrl;
            }
        }
    });
    router.init();
    let routing = router;
    routing.add('/operation/unit/manage/page/index', function () {
        $('.page-content').load(_contextPath + "/operation/unit/manage/page/index")
        // $('.page-content').load(_contextPath + "/operation/unit-manage/index")
    });
    routing.add('/operation/user/manage/page/index', function () {
        $('.page-content').load(_contextPath + "/operation/user/manage/page/index")
    });
    routing.add('/operation/log', function () {
        $('.page-content').load(_contextPath + "/operation/operationRecord/index")
    });

    routing.add('/logout', function () {
        window.href = _contextPath + "/operation/logout";
    });
    root.routeUtils = routing;
})(this);
