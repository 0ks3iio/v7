(function (root) {

    var DataVContext = function () {
        this._init();
    };

    var Utils = {
        updateLevel: function (data) {
            for (var index in data) {
                var target = data[index];
                dataVRender.updateConfig({
                    dom: $('.box-data[data-index="'+ target.diagramId +'"]'),
                    key: 'level',
                    value: target.level
                });
            }
            dataVUI.loadLayer();
        },
        notifyLock: function (ids, lock) {
            for (var index in ids) {
                $('.box-data[data-index="'+ids[index]+'"]').data('resize-options').lock = lock;
            }
        }
    };

    DataVContext.prototype = {
        _init: function () {
            $('.js-highest').on('mousedown', this.top);
            $('.js-lowest').on('mousedown', this.bottom);
            $('.js-up').on('mousedown', this.up);
            $('.js-down').on('mousedown', this.down);
            $('.js-grand-up').on('mousedown', this.grandUp);
            $('.js-grand-up-cancel').on('mousedown', this.unGrandUp);
            $('.js-collect-library').on('mousedown', this.collect);
            $('.js-remove-choose').on('mousedown', this.remove);
            $('.js-lock').on('mousedown', this.lock);
            $('.js-unlock').on('mousedown', this.unLock);

            $('body').on('mousedown', function (e) {
                if (e.target.className !== "key-box") {
                    $('.key-box').hide()
                }
            });
        },
        /**
         * 置顶
         */
        top: function () {
            var $choose = $('.box-data.choose');
            if ($choose.length > 1) {
                netUtils.error("无法同时设置多个图表");
                return;
            }
            var $this = this;
            dataVNet.modifyLevel($choose.attr('data-index'), true, function (data) {
                Utils.updateLevel(data);
                //当前选中的图表参数变更
            });
        },

        bottom: function () {
            var $choose = $('.box-data.choose');
            if ($choose.length > 1) {
                netUtils.error("无法同时设置多个图表");
                return;
            }
            var $this = this;
            dataVNet.modifyLevel($choose.attr('data-index'), false, function (data) {
                Utils.updateLevel(data);
                //当前选中的图表参数变更
            });
        },

        remove: function () {
            $('div.box-data.choose').each(function () {
                dataVUI.deleteDiagram(_screenId, $(this).attr('data-index'), function (diagramId) {
                    $(".scrollbar-made").find('dd[data-index=' + diagramId + ']').remove();
                });
            });
        },
        grandUp: function () {

        },
        unGrandUp: function () {

        },

        up: function () {
            var $choose = $('.box-data.choose');
            if ($choose.length > 1) {
                netUtils.error("无法同时设置多个图表");
                return;
            }
            var $this = this;
            dataVNet.upDownLevel($choose.attr('data-index'), true, function (data) {
                Utils.updateLevel(data);
                //当前选中的图表参数变更
            });
        },

        down: function () {
            var $choose = $('.box-data.choose');
            if ($choose.length > 1) {
                netUtils.error("无法同时设置多个图表");
                return;
            }
            var $this = this;
            dataVNet.upDownLevel($choose.attr('data-index'), false, function (data) {
                Utils.updateLevel(data);
                //当前选中的图表参数变更
            });
        },

        collect: function () {
            var $choose = $('.box-data.choose');
            if ($choose.length > 1) {
                netUtils.error("无法同时收藏多个图表");
                return;
            }
            var diagramId = $choose.attr('data-index');
            layer.prompt({title: '请输入收藏的名称', formType: 3, maxlength: 50}, function (name, index) {
                layer.close(index);
                dataVNet.doCollect(diagramId, name);
            });
        },

        lock: function () {
            var diagramIds = [];
            $('.box-data.choose').each(function () {
                diagramIds.push($(this).data('index'));
            });
            dataVNet.lockDiagram(diagramIds, true, function () {
                Utils.notifyLock(diagramIds, true);
            })
        },

        unLock: function(){
            var diagramIds = [];
            $('.box-data.choose').each(function () {
                diagramIds.push($(this).data('index'));
            });
            dataVNet.lockDiagram(diagramIds, false, function () {
                Utils.notifyLock(diagramIds, false);
            })
        },

        showContext: function (e) {
            //设置所有的菜单全部显示
            $('.key-box li').removeClass('hide');

            var $keybox = $('.key-box');

            //判断是否超过屏幕
            var y = $('body').height() - e.pageY;
            var h = $keybox.height();
            if (y < h){
                $keybox.show().css({
                    left: e.pageX,
                    top: (e.pageY - $keybox.height())
                });
            } else {
                $('.key-box').show().css({
                    left: e.pageX,
                    top: e.pageY
                });
            }

            //判断锁定和取消锁定菜单是否显示
            var lock = false;
            var unLock = false;
            var $choose = $('.box-data.choose');
            $choose.each(function (index, el) {
                if ($(el).data('resize-options').lock) {
                    lock = true;
                } else {
                    unLock = true;
                }
            });
            if (lock && !unLock) {
                $('.js-lock').addClass('hide');
            }
            else if (!lock && unLock) {
                $('.js-unlock').addClass('hide');
            }
        },
        hideContext: function (e) {
            $('.key-box').hide()
        }
    };

    root.DataVContext = DataVContext;
}(this));