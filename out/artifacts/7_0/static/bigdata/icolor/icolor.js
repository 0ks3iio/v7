(function (c) {
    var d;
    c(function () {
        $iColor = c("#iColorPicker").length ? c("#iColorPicker") : a();
        c(document).bind("click", function () {
            $iColor.is(":visible") && $iColor.fadeOut()
        })
    });

    function a() {
        var h = ["f00", "ff0", "0f0", "0ff", "00f", "f0f", "fff", "ebebeb", "e1e1e1", "d7d7d7", "ccc", "c2c2c2", "b7b7b7", "acacac", "a0a0a0", "959595", "ee1d24", "fff100", "00a650", "00aeef", "2f3192", "ed008c", "898989", "7d7d7d", "707070", "626262", "555", "464646", "363636", "262626", "111", "000", "f7977a", "fbad82", "fdc68c", "fff799", "c6df9c", "a4d49d", "81ca9d", "7bcdc9", "6ccff7", "7ca6d8", "8293ca", "8881be", "a286bd", "bc8cbf", "f49bc1", "f5999d", "f16c4d", "f68e54", "fbaf5a", "fff467", "acd372", "7dc473", "39b778", "16bcb4", "00bff3", "438ccb", "5573b7", "5e5ca7", "855fa8", "a763a9", "ef6ea8", "f16d7e", "ee1d24", "f16522", "f7941d", "fff100", "8fc63d", "37b44a", "00a650", "00a99e", "00aeef", "0072bc", "0054a5", "2f3192", "652c91", "91278f", "ed008c", "ee105a", "9d0a0f", "a1410d", "a36209", "aba000", "588528", "197b30", "007236", "00736a", "0076a4", "004a80", "003370", "1d1363", "450e61", "62055f", "9e005c", "9d0039", "790000", "7b3000", "7c4900", "827a00", "3e6617", "045f20", "005824", "005951", "005b7e", "003562", "002056", "0c004b", "30004a", "4b0048", "7a0045", "7a0026"];
        var g,
            f = c('<div id="iColorPicker"><table class="pickerTable"><thead></thead><tbody><tr><td style="border:1px solid #000;background:#fff;cursor:pointer;height:60px;-moz-background-clip:-moz-initial;-moz-background-origin:-moz-initial;-moz-background-inline-policy:-moz-initial;" colspan="16" id="colorPreview"></td></tr></tbody></table></div>').css("display", "none").appendTo(c("body"));
        f.on({
            "mouseover": function (i) {
                var j = c(i.target).attr("hx");
                j != undefined && c("#colorPreview").css("background", "#" + j).attr("hx", j)
            }, "click": function (i) {
                var j = c(i.target).attr("hx");
                if (j == undefined) {
                    i.stopPropagation();
                    return false
                }
                b(d, j);
                d.callback(d, j)
            }
        });
        c.each(h, function (j, i) {
            g += '<td style="background:#' + i + '" hx="' + i + '"></td>';
            if (j % 16 == 15) {
                c("<tr>" + g + "</tr>").appendTo(f.find("thead"));
                g = ""
            }
        });
        return f
    }

    function e(f, i, g) {
        var h = f.offset();
        g = c.extend({"x": 0, "y": 0}, g);
        i.css({
            "top": h.top + f.outerHeight() - i.outerHeight() + g.y + "px",
            "left": h.left + f.outerWidth() + g.x + "px",
            "position": "absolute"
        }).fadeIn("fast")
    }

    function b(f, h) {
        var g = "#" + h;
        f[f.attr("type") == undefined ? "html" : "val"](g).css("background", g).attr("hx", g).attr('v', g).val(g).text('');
    }

    c.fn.iColor = function (f, g) {
        return this.each(function () {
            var i = c(this), h = i.attr("hx");
            if (h != undefined) {
                h = c.trim(h);
                if (h[0] == "#") {
                    h = h.substring(1)
                }
                h.length && b(i, h + ["", "00", "0"][h.length % 3])
            }
            i.click(function (j) {
                d = c(j.target);
                d.callback = f || b;
                e(i, $iColor, g);
                j.stopPropagation()
            })
        })
    }
})(jQuery);