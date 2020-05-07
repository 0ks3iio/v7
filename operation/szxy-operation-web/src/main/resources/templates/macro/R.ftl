<#macro region dataType nestedId regionCode="" call="">
<div class="multi-select macro-region">
    <div class="multi-select-text">
        <input id="regionName${nestedId}" readonly="readonly" type="text" class="form-control"/>
        <#nested />
    </div>
    <div class="multi-select-layer hide">
        <ul class="multi-select-tab clearfix">
            <li class="current tab-province can">选择省份</li>
            <li class="tab-city can">选择城市</li>
            <li class="tab-country can">选择区县</li>
        </ul>
        <div class="content-list province">

        </div>
        <ul class="content-list clearfix city " >

        </ul>
        <ul class="content-list clearfix country" >

        </ul>
    </div>
</div>
<script>
    $(function () {


    <!-- 1国家 2省 3市 4区县  -->
    <#assign level=regionHolder.getTopRegionLevel() />
    //根据当前用户模块所负责的行政区划的数据，将切换行政等级的tab设置为不可点击
    var $tab = $('#regionName${nestedId}').parents('.macro-region').children('.multi-select-layer').find('.multi-select-tab');

    //切换 省、市、区县点击事件
    $tab.find('li').click(function () {
        if ($(this).hasClass('can')) {
            $(this).addClass('current').siblings('li').removeClass('current');
            var index = $tab.find('li').index(this);
            if (index === 0) {
                $tab.next().show().siblings('.content-list').hide();
            } else if (index === 1) {
                $tab.next().next().show().siblings('.content-list').hide();
            } else if (index === 2) {
                $tab.next().next().next().show().siblings('.content-list').hide();
            }
        }
    });


    //行政区划点击事件
    $('#regionName${nestedId}').parents('.macro-region').click(function () {
        $(this).children('.multi-select-layer').removeClass('hide');
        //检查是否加载了省级数据，若已经加载省级数据则说明不是第一次选择行政区划，不用重复加载数据
        init${nestedId}();
    });

    init${nestedId}(true);

    function init${nestedId}(isInit) {
        var $provinceContainer = $('#regionName${nestedId}').parents('.multi-select-text').next().children('.province');
        var $macroRegion = $('#regionName${nestedId}').parents('.macro-region');
        if ($.trim($provinceContainer.html()) === '') {
            loadProvince${nestedId}($provinceContainer, function () {
                $tab.find('li:eq(0)').click();
                <#if regionCode == '' || regionCode=='000000'>
                //regionCode为空，则完全按照当前用户模块的行政区划判定
                $tab.find('li:eq(1)').removeClass('can');
                <#else>
                //regionCode不为空，则
                    var code = '${regionCode}';
                    if (code.length === 2) {
                        loadCity${nestedId}($provinceContainer.next(), '${regionCode}0000', function () {
                            setNested${nestedId}(null, true);
                            if (isInit) {
                                $macroRegion.children('.multi-select-layer').addClass('hide');
                            }
                        });
                    } else if(code.length >= 4 ) {
                        loadCity${nestedId}($provinceContainer.next(), '${regionCode}'.substr(0, 2) + '0000', function () {
                            if (code.length === 4) {
                                $tab.find('li:eq(1)').click();
                                loadCountry${nestedId}($provinceContainer.next().next(), '${regionCode}00', function () {
                                    setNested${nestedId}(null, true);
                                    if (isInit) {
                                        $macroRegion.children('.multi-select-layer').addClass('hide');
                                    }
                                });
                            } else {
                                $tab.find('li:eq(2)').click();
                                loadCountry${nestedId}($provinceContainer.next().next(), '${regionCode}'.substr(0, 4) + '00', function () {
                                    setNested${nestedId}(null, true);
                                    if (isInit) {
                                        $macroRegion.children('.multi-select-layer').addClass('hide');
                                    }
                                });
                            }
                        });
                    }
                </#if>
                setNested${nestedId}(null, true);
                if (isInit) {
                    $macroRegion.children('.multi-select-layer').addClass('hide');
                }
            });
        }
    }


    /**
     * 加载省级数据
     */
    function loadProvince${nestedId}($target, call) {
        $target.ajaxGetLoading('${springMacroRequestContext.contextPath}/operation/${dataType}/macro/region/provinceList?regionCode=${regionCode}', function (data) {
            $target.html(data.html);

            bindProvinceClick${nestedId}($target);
            call()
        });
    }

    /**
     * 加载市级数据
     * @param $target
     */
    function loadCity${nestedId}($target, parentCode, call) {
        $target.ajaxGetLoading('${springMacroRequestContext.contextPath}/operation/${dataType}/macro/region/subRegion?regionCode=${regionCode}&fullRegionCode=' + parentCode, function (data) {
            $target.html('');
            var subRegions = data.msg;
            var code = '${regionCode}';
            if (code.length === 6) {
                code = code.substr(0, 4);
            }
            for (var i = 0; i < subRegions.length; i++) {
                var regionObj = subRegions[i];
                //插入市列表
                $target.append(
                        '<li' + (regionObj.regionCode==code?' class="current"':'') +' data-region-code="' + regionObj.regionCode + '" data-full-code="' + regionObj.fullCode + '">' + regionObj.regionName + '</li>'
                );
            }
            bindCityClick${nestedId}($target);
            call()
        });
    }

    /**
     * 加载区县级数据
     */
    function loadCountry${nestedId}($target, parentCode, call) {
        $target.ajaxGetLoading('${springMacroRequestContext.contextPath}/operation/${dataType}/macro/region/subRegion?regionCode=${regionCode}&fullRegionCode=' + parentCode, function (data) {
            var subRegions = data.msg;
            $target.html('');
            for (var i = 0; i < subRegions.length; i++) {
                var regionObj = subRegions[i];
                //插入市列表
                $target.append(
                        '<li'+(regionObj.regionCode=='${regionCode}'?' class="current"':'')+ ' data-region-code="' + regionObj.regionCode + '" data-full-code="' + regionObj.fullCode + '">' + regionObj.regionName + '</li>'
                );
            }
            bindCountryClick${nestedId}($target);
            call()
        });
    }
    function bindProvinceClick${nestedId}($target) {
        $target.on('click', 'dl dd span', function () {
            $target.find('dl dd span').removeClass('current');
            var fullCode = $(this).data('full-code');
            $(this).addClass('current').siblings('span').removeClass('current')
            var $this = $(this);
            loadCity${nestedId}($(this).parents('.province').next(), fullCode, function () {
                var $curentTab = $this.parents('.province').prev().find('li.tab-city');
                $curentTab.addClass('can').click().addClass('current').next().removeClass('can');
                $curentTab.prev().removeClass('current');
                setNested${nestedId}($this)
            });

        })
    }
    function bindCityClick${nestedId}($target) {
        $target.on('click', 'li', function () {
            var fullCode = $(this).data('full-code');
            $(this).addClass('current').siblings('li').removeClass('current');
            var $this = $(this);
            loadCountry${nestedId}($(this).parents('.city').next(), fullCode, function () {
                var $curentTab = $this.parents('.city').prev().prev().find('li.tab-country');
                $curentTab.addClass('current').addClass('can').click();
                $curentTab.prev().removeClass('current');
                setNested${nestedId}($this)
            });

        })
    }
    function bindCountryClick${nestedId}($target) {
        $target.on('click', 'li', function () {
            var fullCode = $(this).data('full-code');
            $(this).addClass('current').siblings('li').removeClass('current')
            setNested${nestedId}($(this))
        })
    }

    function setNested${nestedId}($target, init) {
        if ($target) {
            $('#${nestedId}').val($target.data('region-code'));
        }
        var $txt = $('#regionName${nestedId}').parent().next();
        var p = $txt.find('.province').find('span.current').text();
        var c = $txt.find('.city').find('li.current').text();
        var t = $txt.find('.country').find('li.current').text();
        $('#regionName${nestedId}').val(p + c + t);
        if ('${call}' !== '' && !init) {
            try {
                    ${call}();
            }catch (e) {
                console.log(e)
            }
        }
    }
    })
</script>
</#macro>