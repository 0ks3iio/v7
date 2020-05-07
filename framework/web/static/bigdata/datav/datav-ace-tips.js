(function (root) {

    let dataVAce = {};

    let staticData = [];
    staticData.push({meta: "login user id", caption: "${userId}", value: "${userId}", score:1});
    staticData.push({meta: "login user unit id", caption: "${unitId}", value: "${unitId}", score:2});
    staticData.push({meta: "login user unit class", caption: "${unitClass}", value: "${unitClass}", score:3});
    staticData.push({meta: "login user owner id", caption: "${ownerId}", value: "${ownerId}", score:4});
    staticData.push({meta: "login user owner type", caption: "${ownerType}", value: "${ownerType}", score:5});
    staticData.push({meta: "login user user type", caption: "${userType}", value: "${userType}", score:6});
    staticData.push({meta: "login user unit region", caption: "${region}", value: "${region}", score:7});
    staticData.push({meta: "login user name", caption: "${userName}", value: "${userName}", score:8});
    staticData.push({meta: "login user real name", caption: "${realName}", value: "${realName}", score:9});
    staticData.push({meta: "login user unit name", caption: "${unitName}", value: "${unitName}", score:10});
    staticData.push({meta: "login user sex", caption: "${sex}", value: "${sex}", score:11});
    dataVAce.tips = staticData;

    dataVAce.refreshTips = function() {
       $.ajax({
           url: _contextPath + '/bigdata/datav/ace/tips/' + _screenId,
           type: 'GET',
           async: true,
           success: function (res) {
               if (res.success) {
                   dataVAce.tips = staticData.concat(res.data);
               }
           },
           error: function () {
               layer.msg("网络异常", {icon: 2});
           }
       })
    };

    dataVAce.getTips = function() {
        return dataVAce.tips;
    };

    // dataVAce.refreshTips();
    root.dataVAce = dataVAce;
})(this);