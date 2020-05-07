var auto_search = {
    run: function () {// 运行应用
        var run = $('.data-search input[name=searcharea]'), runList = $('.searchList'), ac_menu = $('.searchList .area_menu');
        var def_text = '请搜索...';
        run.val(def_text);
        run.focus(function () {
            if (this.value == def_text) this.value = '';
        }).blur(function () {
            if (this.value == '') this.value = def_text;
            auto_search.delay(function () { runList.hide() }, 300);//延时，等待选择事件执行完成
        }).bind('keyup', function () {
            auto_search.appRunList(runList, run.val());
        }).keydown(function (e) {
            if (e.keyCode == 13) setTimeout(auto_search.appRunExec, 200);
        });
    },
    delay: function (f, t) {
        { if (typeof f != "function") return; var o = setTimeout(f, t); this.clear = function () { clearTimeout(o) } }
    },
    appRunList: function (runList, v) {//自动搜索应用
        if (v == '') {
            runList.hide();
            return;
        }
        var i, temp = '', n = 0, loaded = {};
        //搜索以关键词开头的应用
        for (i in searchValue) {
            if (isNaN(i) || loaded[i] || !searchValue[i].name) {
                continue;
            }
            runSearchCode = searchValue[i].code
            runSearchName = searchValue[i].name;
            runSearchTip = searchValue[i].tip;
            runSearchParent = searchValue[i].parent;
            runSearchType = searchValue[i].type;
            runSearchLevel = searchValue[i].level;
            runSearchId = searchValue[i].id;
            runSearchPinyin = searchValue[i].pinyin;
            runSearchPy = searchValue[i].py;
            if (runSearchName.indexOf(v) >= 0 || runSearchPinyin.indexOf(v) >= 0 || runSearchPy.indexOf(v) >= 0 || runSearchPinyin.toLowerCase().indexOf(v) >= 0 || runSearchPy.toLowerCase().indexOf(v) >= 0) {
                loaded[i] = 1;
                temp += '<a class="area_menu" href="javascript:;" data-flag=1 data-parent="' + runSearchParent + '"data-type="' + runSearchType + '"data-level="' + runSearchLevel+ '"data-id="' + runSearchId + '"data-code="' + runSearchCode + '" data-name="' + runSearchName + '" onclick="selectData(\'data\',this,\'\')"><em>' + runSearchTip.replace(v, "<b>" + v + "</b>") + '</em>' + runSearchName.replace(v, "<b>" + v + "</b>") + '</a>';
                if (++n > 10) break;
            }
        }
        if (temp) {// 搜索到应用则显示
            runList.show().html(temp);
        } else {
            runList.show().html('抱歉,没有您需要的结果!');
        }
    },

    appRunExec: function () {// 运行按纽点击
        ac_menu = $('.searchList .area_menu');
        if (ac_menu.length > 0) {
            ac_menu.eq(0).trigger('click');
        }
    },
};