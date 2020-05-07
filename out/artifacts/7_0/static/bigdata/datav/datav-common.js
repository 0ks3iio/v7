/*
 *
 */

(function (root) {
    let dataVCommon = {};

    $(".screen-interaction-element").on('input propertychange', function (e) {
        dataVNet.doUpdateScreenInteractionElement($(e.target).attr('id'), $(e.target).val());
    });

    /**
     * 展开折叠
     */
    $('body').on('click', '.set-op-toggle', function () {
        $(this).toggleClass('active');
        $(this).find('.assist>i').toggleClass('rotate-180');
    });

    $('body').on('click', '.js-open-add', function () {
        $(this).parent().siblings('.add-son-set').toggle()
    });

    $('body').on('click', '.element-item', function () {
        let name = $(this).text();
        let diagramId = $('div.box-data.choose').attr('data-index');
        let id = dataVNet.doAddElement({diagramId: diagramId, diagramType: $(this).data('type')});
        if (id) {
            let html = '<div class="filter-item relative margin-b-10 hover-effect">\
                        <div class="width-2of3" data-elementId="">' + name + '</div>\
                        <div class="pos-right right-0">\
                            <img src="' + _contextPath + '/static/bigdata/images/remove-grey.png"\
                             class="pointer js-remove-element" data-elementid="' + id + '"/>\
                        </div>\
                    </div>';
            $('.js-component-target').append(html);
            $('.js-component-target').find('.filter-item:last').children('div:first').on('click', dataVCommon.onLoadElement);
            let $c = $('<div class="js-same hidden" id="' + id + '"></div>');
            $c.load(_contextPath + '/bigdata/datav/diagram/config/' + diagramId + '/' + id)
            $('.config-panel').append($c);
            dataVRender.doRender({screenId: _screenId, diagramId: diagramId, dispose: true});
        }
        $('.js-open-add').parent().siblings('.add-son-set').toggle()
    });

    $('body').on('click', '.js-remove-element', function () {
        let diagramId = $('div.box-data.choose').attr('data-index');
        let $this = $(this);
        dataVNet.doDeleteElement({diagramId: diagramId, elementId: $(this).data('elementid')}, function () {
            $this.parent().parent().remove();
            dataVRender.doRender({screenId: _screenId, diagramId: diagramId, dispose: true});
        });
    });
    $('body').on('click', '.js-back', function () {
        $('.js-back').addClass('hidden').siblings('span').text('图表设置');
        $('#elemet_show_div').removeClass('hidden');
        $('.root-same').removeClass('hidden').siblings('div').addClass('hidden');
        $('.js-back').attr('data-elementid', '');
    });
    dataVCommon.onLoadElement = function (e) {
        $('#elemet_show_div').addClass('hidden');
        let name = $(this).text();
        let elementId = $(this).next().find('img').data('elementid');
        $('.js-back').removeClass('hidden').attr('data-elementid', elementId).siblings('span').text(name);
        $('#' + elementId).removeClass('hidden').siblings('div').addClass('hidden');
    };

    //-----------------------------------yes no-------------------------

    $('body').on('click', '.js-click-yes button', function () {
        $(this).parent().siblings('input').val($(this).data('text'));
        let config = {};
        let $valdom = $(this).parent().siblings('input');
        config.key = $valdom.attr('name');
        let humanValue = $valdom.val();
        config.arrayName = $valdom.attr('data-array');
        if (humanValue == '是') {
            config.value = true;
        } else {
            config.value = false;
        }
        if (config.key == 'active') {
            //激活
            if (config.value) {
                dataVNet.doActiveInteraction($('div.box-data.choose').attr('data-index'), null, function () {
                    $('.interaction-active-input').each(function () {
                        addDefaultInteractionElement($(this).val());
                        $(this).removeAttr('readOnly');
                    });
                    dataVAce.refreshTips();
                });
            } else {
                dataVNet.doCancelActiveInteraction($('div.box-data.choose').attr('data-index'), function () {
                    dataVAce.refreshTips();
                    $('.interaction-active-input').each(function () {
                        $('#' + $(this).val()).parent().parent().html("");
                        $(this).val($(this).prev().html().replace('：', '')).attr('readOnly', 'readOnly');
                    });
                });
            }
            return;
        }

        config.dom = $('div.box-data.choose')[0];
        dataConfig.onChange(config)
    });
    //+ -
    let arr2 = [1, -1];
    $('body').on('click', '.js-click button', function () {
        let originVal = $(this).parent().siblings('input').val();
        var $val = parseInt(originVal);
        var $num = $val + arr2[$(this).index()];
        $(this).parent().siblings('input').val($num + (originVal.indexOf('%') > 0 ? '%' : ''));
        let config = {};
        let $valdom = $(this).parent().siblings('input');
        config.key = $valdom.attr('name');
        config.value = $valdom.val();
        config.dom = $('div.box-data.choose')[0];
        config.arrayName = $valdom.attr('data-array');
        dataConfig.onChange(config)
    });

    function addDefaultInteractionElement(bindKey) {
        if ($('#' + bindKey).length == 0) {
            let appendElement = '<div class="filter-item">\n' +
                '<span class="front-title">' + bindKey + '：</span>\n' +
                '<input id="' + bindKey + '" value="" type="text" class="form-control inside-select" placeholder="请输入默认值"/>\n' +
                '</div>';
            $('.screen-interaction-element').append(appendElement);
        }
    }

    //------------------------------------数据系列----------------------------------
    $('body').on('click', '.js-hover', clickArray);

    function clickArray() {
        $(this).toggleClass('bg-DEEBFC');
    }

    $('body').on('click', '.js-add-array', function (e) {
        e.stopPropagation();
        let $cloneDom = $('.js-array-target').find('.clearfix').eq(0).clone(true);
        let max = parseInt($(this).attr('data-max'));
        let items = [];
        let arrayName = '系列' + (max + 1);

        $cloneDom.find('span.front-title').find('b').text(arrayName);
        $cloneDom.find('input[name="seriesShowName"]').val(max + 1);
        $cloneDom.find('input[name="seriesName"]').val(max + 1);
        $cloneDom.find('input').attr('data-array', arrayName);
        $cloneDom.find('input').each(function () {
            let item = {};
            item.key = $(this).attr('name');
            item.value = $(this).val();
            items.push(item);
        });
        if ($cloneDom.find('select').length > 0) {
            $cloneDom.find('select').attr('data-array', arrayName);
            $cloneDom.find('select').each(function () {
                let item = {};
                item.key = $(this).attr('name');
                item.value = $(this).val();
                items.push(item);
            });
        }
        $this = $(this);
        dataVNet.addConfigArray($('div.box-data.choose').attr('data-index'), $(this).attr('data-groupkey'), arrayName, items, function () {
            $('.js-array-target').append($cloneDom);
            $this.attr('data-max', max + 1);
            dataVRender.doRender({
                screenId: _screenId,
                diagramId: $('div.box-data.choose').attr('data-index'),
                dispose: true
            })
        });
    });
    $('body').on('click', '.js-remove-array', function (e) {
        e.stopPropagation();
        let max = parseInt($('.js-add-array').attr('data-max'));
        if ($('.js-array-target').find('.clearfix').length == 1) {
            return;
        }
        let arrayname = $('.js-array-target').find('.clearfix:last').find('.front-title:first').text();
        dataVNet.deleteConfigArray($('div.box-data.choose').attr('data-index'), $.trim(arrayname), function () {
            $('.js-array-target').find('.clearfix:last').remove();
            $('.js-add-array').attr('data-max', max - 1);
            dataVRender.doRender({
                screenId: _screenId,
                diagramId: $('div.box-data.choose').attr('data-index'),
                dispose: true
            })
        });
    });

    //图片

    //img盒子添加图片
    $('body').on('change', '#upFile', function () {
        var ts = this;
        if ($('.box-data.choose').hasClass('box-slide')) {
            addPics(ts)
        } else {
            addOnePic(ts)
        }
    });
    //换图片
    $('body').on('change', '.js-change-pic', function () {
        var ts = this;
        changePic(ts);
    });
    //删除图片
    $('body').on('click', '.delete', function () {
        let index = $(this).next().find('input').attr('data-index');
        let $this = $(this);
        let diagramId = $('.box-data.choose').attr('data-index');
        dataVNet.doDeleteImage(index, diagramId, function () {
            dataVRender.doRender({screenId:_screenId, diagramId: diagramId});
            $this.parents('li').remove();
        });
    });

    function addOnePic(ele) {
        var file = ele.files[0];
        let suffix = file.name.substring(file.name.lastIndexOf('.')+1);
        var $target = $('.box-data.choose').children('.box-data-body');
        var $dad = $(ele).parents('li');
        dataVNet.doUploadImage(file, '1', $('.box-data.choose').attr('data-index'), suffix, function (url) {
            let imageSrc = _fileUrl + '/' + url + "?" + new Date().getTime();
            var str = '<li>\
                            <div class="vetically-center">\
                                    <a href="javascript:void(0);" class="form-file js-add-pic">\
                                        <input data-index="1" class="js-change-pic" type="file" accept=".tiff,.tif,.svf,.png,.jpg,.jpeg,.jpe,.jp2,.gif,.dwg" name="file" id="">\
                                    </a>\
                                </div>\
                                <img src="' + imageSrc + '"/>\
                            </li>';
            $target.empty().append('<div class="height-1of1"><img src="' + imageSrc + '" alt=""></div>');
            $dad.before(str);
            $('.js-add-pic-wrap').addClass('hidden');
        });
    }

    //添加几张图片
    function addPics(ele) {
        var file = ele.files[0];
        let suffix = file.name.substring(file.name.lastIndexOf('.')+1);
        var $target = $('.box-data.choose').children('.box-data-body');
        var $dad = $(ele).parents('li');
        let index = new Date().getTime();
        dataVNet.doUploadImage(file, index, $('.box-data.choose').attr('data-index'), suffix, function (url) {
            let imageSrc = _fileUrl + '/' + url;
            var str = '<li>\
                                    <img class="delete" src="'+_contextPath+'/static/bigdata/images/delate.png" alt="">\
                                    <div class="vetically-center">\
                                        <a href="javascript:void(0);" class="form-file js-add-pic">\
                                            <input data-index="'+index+'" class="js-change-pic" type="file" accept=".tiff,.tif,.svf,.png,.jpg,.jpeg,.jpe,.jp2,.gif,.dwg" name="file" id="">\
                                        </a>\
                                    </div>\
                                    <img src="' + imageSrc + '"/>\
                                </li>';
            $dad.before(str);
            if ($target.children().is('div')) {
                chooseSlideImg();
                dataVRender.slideShfflingImage($('.box-data.choose').find('.slide-class')[0]);
            } else {
                $target.empty().append('<div class="slide-class height-1of1"><div class="height-1of1"><img src="' + imageSrc + '" alt=""></div></div>');
            }
        });
    }

    //改变添加的图片
    function changePic(ele) {
        var file = ele.files[0];
        let suffix = file.name.substring(file.name.lastIndexOf('.')+1);
        var $target = $('.box-data.choose').children('.box-data-body');
        var $next = $(ele).parent().parent().next();
        let index = $(ele).attr('data-index');
        dataVNet.doUploadImage(file, index, $('.box-data.choose').attr('data-index'), suffix, function (url) {
            let imageSrc = _fileUrl + '/' + url + "?" + new Date().getTime();
            $next.replaceWith('<img src="' + imageSrc + '" alt="">');
            if ($('.box-data.choose').hasClass('box-slide')) {
                if ($('.add-img-ul>li').length > 2) {
                    chooseSlideImg();
                } else {
                    $target.empty().append('<div class="slide-class height-1of1"><div class="height-1of1"><img src="' + imageSrc + '" alt=""></div></div>');
                }
            } else {
                $target.empty().append('<div class="height-1of1"><img src="' + imageSrc + '" alt=""></div>');
            }
        })
    }

    function chooseSlideImg() {
        var $imgs = $('.add-img-ul li>img:last-child');
        var str = '';
        $imgs.each(function (index, ele) {
            str += '<div class="height-1of1"><img src="' + $(ele).attr('src') + '" alt=""></div>';
        });
        str = '<div class="slide-class height-1of1">' + str + '</div>';
        $('.box-data.choose').children('.box-data-body').empty().append(str);

        dataVRender.doRender({diagramId: $('.box-data.choose').data('index'), screenId: _screenId, dispose: true});
    }


    let dt = {};

    dt.TEXT_TITLE = 94;
    dt.STATE_CARD = 100;

    root.dataVDiagramTypes = dt;
    root.dataVCommon = dataVCommon;
}(this));