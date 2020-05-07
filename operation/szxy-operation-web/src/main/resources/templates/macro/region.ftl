<#macro region dataType regionCode regionName>
<script src="${springMacroRequestContext.contextPath}/static/js/jquery.MultiSelect.js"></script>
<div class="multi-select" >
    <div class="multi-select-text" id="regionText">
        <input type="text"  class="form-control"  value="${regionName! }" id="regionName"/>
        <i class="fa fa-caret-down"></i>
    </div>
</div>
<input type="hidden" name="unit.regionCode" id="regionCode" value="${regionCode! }"/>
<script>
    var $multi
    $('#regionText').click(function(){
        if(typeof (parentRegionCode)=='undefined'){
            parentRegionCode='000000';
        }
        var name =$('#titleConvert').html();
        if($('#unitType').val()!='2' || name=='编辑'|| name=='维护单位'||name=='试用转正式'){
            $('.form-title').click();
            return;
        }
        $.ajaxSettings.async = false;
        regionIni($(this))
        if(typeof (parentRegionCode)=='undefined'){
            parentRegionCode='000000';
        }
        //判断上级单位的regionCode的省市县 是否存在
        if(parentRegionCode.substring(0,2)!='00' ){
            province($('.multi-select-layer').find('span[data-region-code='+provinceCode+']'));
            if(parentRegionCode.substring(2,4) != '00'){
                city( $('.multi-select-layer').find('li[data-region-code='+provinceCode+cityCode+']'));
                if(parentRegionCode.substring(4,6)!='00'){
                    dictrict($('.multi-select-layer').find('li[data-region-code='+provinceCode+cityCode+dictrictCode+']'));
                }
            }
        }
        $.ajaxSettings.async = true;
        //省点击事件
        $('#province').on('click','dl dd span',function(){province($(this))});


        //市选择获取数据
        $('#city').on('click','li',function () {
            city($(this))
            if(parentRegionCode.substring(4.6)=='00'){$('.form-title').click();}
        });


        //区选择获取数据
        $('#district').on('click','li',function(){dictrict($(this))});


        //tab切换-回退
        $('.multi-select-tab').on('click','li.can',function(){
            if(parentRegionCode.substring(2,4)!='00' && $(this).html()=='选择城市')return;
            if(parentRegionCode.substring(0,2)!='00' && $(this).html()=='选择省份')return;
            $(this).addClass('current').removeClass('can').siblings('li').removeClass('current');
            $('.multi-select-tab li:gt('+$(this).index()+')').removeClass('can');
            $multi.find('.multi-select-layer .content-list:eq('+$(this).index()+')').show().siblings('.content-list').hide();
        });

        //点击其他区域隐藏层
        $(document).click(function(event){
            var eo=$(event.target);
            if($('.multi-select-layer').is(':visible') && !eo.parents('.multi-select').length)
                $('.multi-select-layer').remove();
        });

    });
    function regionIni(obj) {
        $multi=$(obj).parent('.multi-select');
        $('.multi-select-layer').remove();
        $multi.append('<div class="multi-select-layer"><ul class="multi-select-tab clearfix"><li class="current">选择省份</li><li>选择城市</li><li>选择区县</li></ul><div id="province" class="content-list"></div><ul id="city" class="content-list clearfix"></ul><ul id="district" class="content-list clearfix"></ul></div>');
        //初始化
        $.ajax({
            url:"${springMacroRequestContext.contextPath}/operation/${dataType}/macro/region/provinceList",
            type: 'GET',
            dataType:'json',
            success:function(data){
                $("#province").append(data.html);
                $multi.children('.multi-select-layer').height($multi.children('.multi-select-layer').height());
            }
        });
    }
    function province(obj) {
        var province_txt = obj.text();
        var regionCode = obj.data("region-code");
        var fullCode = obj.data("full-code")
        $("#regionCode").val(fullCode);
        $('#province dd span').removeClass('current');
        obj.addClass('current');
        var textVal = province_txt;
        //textVal = textVal.substring(0,16);
        $multi.find('.form-control').val(textVal).attr('data-province', province_txt);

        //获取市列表
        $('.multi-select-tab li:eq(1)').addClass('current').siblings('li').removeClass('current');
        $('.multi-select-tab li:eq(0)').addClass('can');
        $('#province').hide().next('#city').show();
        $.ajax({
            url: "${springMacroRequestContext.contextPath}/operation/${dataType}/macro/region/subRegion?fullRegionCode=" + fullCode,
            type: "GET",
            dataType: "json",
            success: function (data) {
                $('#city,#district').html('');//清空市、区列表
                var subRegions = data.msg;
                for (var i = 0; i < subRegions.length; i++) {
                    var regionObj = subRegions[i];
                    //插入市列表
                    $('#city').append(
                            '<li data-region-code="' + regionObj.regionCode + '" data-full-code="' + regionObj.fullCode + '">' + regionObj.regionName + '</li>'
                    );
                }
            }
        });

    }
    function city(obj){
        var province_txt=$multi.find('.form-control').attr('data-province');
        var city_txt=obj.text();
        var regionCode = obj.data("region-code");
        var fullCode =obj.data("full-code")
        $("#regionCode").val(fullCode);
        obj.addClass('current').siblings('li').removeClass('current');
        var textVal = province_txt+'/'+city_txt;
        // textVal = textVal.substring(0,16);
        $multi.find('.form-control').val(textVal).attr('data-city',city_txt);

        //获取区列表
        $('.multi-select-tab li:eq(2)').addClass('current').siblings('li').removeClass('current');
        $('.multi-select-tab li:eq(0),.multi-select-tab li:eq(1)').addClass('can');
        $('#province').hide().next('#city').hide().next('#district').show();

        $.ajax({
            url:"${springMacroRequestContext.contextPath}/operation/${dataType}/macro/region/subRegion?fullRegionCode="+ fullCode,
            type:"GET",
            dataType:"json",
            success:function(data){
                $('#district').html('');//清空区列表
                var subRegions = data.msg;
                for(var i=0;i<subRegions.length;i++){
                    var regionObj = subRegions[i];
                    //插入区列表
                    $('#district').append(
                            '<li data-region-code="'+ regionObj.regionCode +'" data-full-code="'+regionObj.fullCode+'">' + regionObj.regionName + '</li>'
                    );
                }
            }
        });

    }
    function dictrict(obj){
        var province_txt=$multi.find('.form-control').attr('data-province');
        var city_txt=$multi.find('.form-control').attr('data-city');
        var district_txt=obj.text();
        var regionCode =obj.data("region-code");
        $("#regionCode").val(regionCode);
        obj.addClass('current').siblings('li').removeClass('current');
        var textVal = province_txt+'/'+city_txt+'/'+district_txt;
        //textVal = textVal.substring(0,16);
        $multi.find('.form-control').val(textVal).attr('data-district',district_txt);
        $multi.children('.multi-select-layer').remove();
    }
</script>
</#macro>


<#---
    不允许选择上级地区的宏
-->
<#macro regionNotSelectUpperRegion fieldId fieldName regionCode regionName parentRegionCodeCall >
<div class="multi-select">
    <div class="multi-select-text" id="${fieldId}RegionText">
        <input readonly="readonly" type="text" class="form-control" name="${fieldName}" value="${regionName!}" data-regioncode="${regionCode!}" id="${fieldId}"/>
    <#--<i class="fa fa-caret-down"></i>-->
    </div>
    <div class="multi-select-layer hide" id="${fieldId}RegionLayer">
        <ul class="multi-select-tab clearfix">
            <li class="current ${fieldId}Province">选择省份</li>
            <li class="${fieldId}City">选择城市</li>
            <li class="${fieldId}County">选择区县</li>
        </ul>
        <div id="${fieldId}Province" class="content-list">

        </div>
        <ul id="${fieldId}City" class="content-list clearfix" style="display: block;">

        </ul>
        <ul id="${fieldId}County" class="content-list clearfix" style="display: block;">

        </ul>
    </div>
</div>
<script>
    $(function () {
        let utils = {
            init: true
        };

        utils.initProvince = function (call) {
            $.ajax({
                url: "${springMacroRequestContext.contextPath}/operation/macro/region/provinceList",
                type: 'GET',
                dataType: 'json',
                async:false,
                success: function (data) {
                    $("#${fieldId}Province").append(data.html);
                    call();
                }
            });
        };
        /**
         * 省点击事件
         */
        utils.provinceClick = function (el) {

            let regionName = $(el).text();
            let regionCode = $(el).data('region-code');
            let fullCode = $(el).data("full-code");

            $("#${fieldId}").attr('data-provincename', regionName);

            $("#${fieldId}").val(regionName);
            $('#${fieldId}').attr("data-regioncode", fullCode);

            $(el).addClass('current').siblings('span').removeClass('current');

            $('.${fieldId}City').addClass('current').siblings('li').removeClass('current');
            $('#${fieldId}Province').hide().next('#city').show();

            //获取市列表
            $.ajax({
                url: "${springMacroRequestContext.contextPath}/operation/macro/region/subRegion?fullRegionCode=" + fullCode,
                type: "GET",
                dataType: "json",
                async:false,
                success: function (data) {
                    $('#${fieldId}City, #${fieldId}County').html('');//清空市、区列表
                    var subRegions = data.msg;
                    for (var i = 0; i < subRegions.length; i++) {
                        var regionObj = subRegions[i];
                        //插入市列表
                        $('#${fieldId}City').append(
                                '<li data-region-code="' + regionObj.regionCode + '" data-full-code="' + regionObj.fullCode + '">' + regionObj.regionName + '</li>'
                        );
                    }
                }
            });
        };
        /**
         * 市点击事件
         */
        utils.cityClick = function (el) {

            let regionName = $(el).text();
            let regionCode = $(el).data('region-code');
            let fullCode = $(el).data("full-code");

            $("#${fieldId}").attr('data-cityname', regionName);

            let provinceName =  $("#${fieldId}").attr('data-provincename')

            $("#${fieldId}").attr('data-regioncode', fullCode);
            $('#${fieldId}').val(provinceName + regionName);

            $(el).addClass('current').siblings('li').removeClass('current');

            $('.${fieldId}County').addClass('current').siblings('li').removeClass('current');
            $('#${fieldId}City').hide().next('#county').show();

            //获取市列表
            $.ajax({
                url: "${springMacroRequestContext.contextPath}/operation/macro/region/subRegion?fullRegionCode=" + fullCode,
                type: "GET",
                dataType: "json",
                async:false,
                success: function (data) {
                    $('#${fieldId}County').html('')
                    var subRegions = data.msg;
                    for (var i = 0; i < subRegions.length; i++) {
                        var regionObj = subRegions[i];
                        //插入市列表
                        $('#${fieldId}County').append(
                                '<li data-region-code="' + regionObj.regionCode + '" data-full-code="' + regionObj.fullCode + '">' + regionObj.regionName + '</li>'
                        );
                    }
                }
            });
        };
        /**
         * 区县点击事件
         */
        utils.countyClick = function (el) {

            let regionName = $(el).text();
            let fullCode = $(el).data("full-code");

            $("#${fieldId}").attr('data-countyname', regionName);

            let provinceName =  $("#${fieldId}").attr('data-provincename')
            let cityName =  $("#${fieldId}").attr('data-cityname')


            $("#${fieldId}").attr('data-regioncode', fullCode);
            $('#${fieldId}').val(provinceName+cityName+regionName);

            $(el).addClass('current').siblings('li').removeClass('current');
            $('#${fieldId}RegionLayer').addClass('hide');
        };

        //1 初始化所有的省分

        utils.initProvince(function () {
            //省点击事件
            $('#${fieldId}Province').on('click', 'dl dd span', function () {
                utils.provinceClick($(this))
            });
            utils.init = true;
        });
        $('#${fieldId}City').on('click', 'li', function () {
            utils.cityClick($(this))
        });
        //区选择获取数据
        $('#${fieldId}County').on('click', 'li', function () {
            utils.countyClick($(this))
        });

        $('#${fieldId}').click(function () {

            $('#${fieldId}RegionLayer').removeClass('hide');

            let parentRegionCode = $('#${fieldId}').attr('data-regioncode');
            if (typeof ${parentRegionCodeCall} === 'function') {

            }

            if (parentRegionCode && parentRegionCode !=='') {
                let pCode = parentRegionCode.substr(0, 2);
                let tCode = parentRegionCode.substr(2, 2);
                let cCode = parentRegionCode.substr(4, 2);
                if (pCode !== '00') {
                    utils.provinceClick($('#${fieldId}RegionLayer').find('span[data-region-code="' + pCode + '"]'));
                    $('.${fieldId}Province').removeClass('can');
                }
                if (tCode != '00') {
                    utils.cityClick($('#${fieldId}RegionLayer').find('li[data-region-code="' + pCode + tCode + '"]'));
                    $('.${fieldId}City').removeClass('can');
                }
                if (cCode != '00') {
                    utils.countyClick($('#${fieldId}RegionLayer').find('li[data-region-code="' + pCode + tCode + cCode + '"]'));
                    $('.${fieldId}County').removeClass('can');
                }
            }

            $('#${fieldId}RegionLayer').removeClass('hide');


            //市选择获取数据

            //tab切换-回退
            $('.multi-select-tab').on('click', 'li.can', function () {
                $(this).addClass('current').removeClass('can').siblings('li').removeClass('current');
                $('.multi-select-tab li:gt(' + $(this).index() + ')').removeClass('can');
                $('#${fieldId}RegionLayer').find('.content-list:eq(' + $(this).index() + ')').show().siblings('.content-list').hide();
            });

            //点击其他区域隐藏层
            $(document).click(function (event) {
                var eo = $(event.target);
                if ($('#${fieldId}RegionLayer').is(':visible') && !eo.parents('.multi-select').length) {
                    $('#${fieldId}RegionLayer').addClass('hide');
                }
            });
        });
    });

</script>
</#macro>