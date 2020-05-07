<#import "/fw/macro/htmlcomponent.ftl" as htmlcom>
<form id="subForm1">
<div class="layer-content">
    <div class="form-horizontal">
        <div class="form-group">
            <a class="btn btn-info btn-blue" onclick="addfen()">新增类别</a>
        </div>
        <div class="form-group  credit-modal-body">
            <table class="table table-bordered table-striped table-hover no-margin">
                <thead>
                <tr>
                    <th width="20%">类别</th>
                    <th width="70%">评分项</th>
                    <th width="10%">操作</th>
                </tr>
                </thead>
                <tbody>
                <input type="hidden" name="id" value=${creditSet.id!}>
                    <#assign index = 0>
                    <#assign index1 = 1>
                    <#if creditSet.dailySetList?exists&& (creditSet.dailySetList?size > 0)>
                        <#list creditSet.dailySetList as item>
                            <#if item.parentId?default("")?trim?length lt 1>
                                <#if item.subSetList?exists&& (item.subSetList?size > 0)>
                                    <#list item.subSetList as item1>
                                    <tr data-id="${index1}">
                                        <#if item1_index ==0>
                                            <input type="hidden" name="dailySetList[${index}].isParent" value="1">
                                        <#--<input type="hidden" id="subSetListIndex" value=${index}/>-->
                                            <input type="hidden" name="dailySetList[${index}].id" value=${item.id!}>
                                            <input type="hidden" name="dailySetList[${index}].setId" value=${creditSet.id!}>
                                            <input type="hidden" name="dailySetList[${index}].parentId" value=${item.parentId!}>
                                            <input type="hidden" name="dailySetList[${index}].name" value=${item.name!}>
                                            <td rowspan=${item.subSetList?size}>${item.name!}</td>
                                            <#assign index = index + 1>
                                        <#else >
                                            <input type="hidden" name="dailySetList[${index}].isParent" value="0">
                                            <#--<input type="hidden" id="subSetListIndex" value=${index}/>-->
                                            <input type="hidden" name="dailySetList[${index}].id" value=${item1.id!}>
                                            <input type="hidden" name="dailySetList[${index}].setId" value=${creditSet.id!}>
                                            <input type="hidden" name="dailySetList[${index}].parentId" value=${item1.parentId!}>
                                            <input type="hidden" name="dailySetList[${index}].name" value=${item1.name!}>
                                        </#if>

                                    <#--<td>${item1.name!}</td>-->
                                        <td>
                                            <input class="form-control credit-control" type="text" value="${item1.name!}" style="width: 180px;"/>
                                            <i class="wpfont icon-plus credit-add" style="color:#317EEB" id="${item1.parentId!}" onclick="addminscroe(this)"></i>
                                            <i class="fa fa-trash-o credit-delete" style="color:#317EEB"></i>
                                        </td>
                                        <#if item1_index ==0>
                                            <td  rowspan=${item.subSetList?size}>
                                                <a class="btn btn-link btn-info credit-text" id="${item.id}" onclick="delfen(this)">
                                                    删除
                                                </a>
                                            </td>
                                        </#if>
                                        <#assign index = index + 1>
                                    </tr>
                                    </#list>
                                <#else >
                                <tr data-id="${index1}">
                                    <td rowspan="1">
                                        <input type="hidden" name="dailySetList[${index}].isParent" value="1">
                                        <input type="hidden" name="dailySetList[${index}].id" value="${item.id!}">
                                        <input class="form-control credit-control" name="dailySetList[${index}].name" type="text" value="" style="width: 200px;" />
                                    </td>
                                    <#assign index = index + 1>
                                      <td>
                                          <input type="hidden" name="dailySetList[${index}].isParent" value="0">
                                          <input type="hidden" name="dailySetList[${index}].parentId" value="${item.id!}">
                                          <input
                                                  class="form-control credit-control" name="dailySetList[${index}].name"
                                                  type="text"
                                                  value=""
                                                  style="width: 180px;"
                                          />
                                          <i
                                                  class="wpfont icon-plus credit-add"
                                                  style="color:#317EEB"
                                                  id="${item.id!}"
                                                  onclick="addminscroe(this)"
                                          ></i>
                                          <i
                                                  class="fa fa-trash-o credit-delete"
                                                  style="color:#317EEB"
                                          ></i>
                                      </td>
                                  <td rowspan="1">
                                      <a class="btn btn-link btn-info credit-text" id="${item.id}" onclick="delfen(this)">
                                          删除
                                      </a>
                                  </td>
                                </tr>
                                    <#assign index = index + 1>
                                </#if>
                                <#assign index1 = index1 + 1>
                            </#if>

                        </#list>
                    </#if>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</form>
<script>
    var setListSize=0
    <#if creditSet.dailySetList?exists&& (creditSet.dailySetList?size > 0)>
        setListSize = ${creditSet.dailySetList?size};
    </#if>
    //增加评分项
    function addminscroe(_this) {
        var seindex = $(_this)
                .parents("tr")
                .attr("data-id");
        var num = 0;
        $(".layer-new")
                .find("tr")
                .each((i, n) => {
                    if ($(n).attr("data-id") == seindex) {
                        num++;
                    }
                });
        $(".layer-new")
                .find("tr")
                .each((i, n) => {
                    if ($(n).attr("data-id") == seindex) {
                        $(n)
                                .find("td")
                                .first()
                                .attr("rowspan", num + 1);
                        $(n)
                                .find("td")
                                .last()
                                .attr("rowspan", num + 1);
                        return false;
                    }
                });
        $(_this).parents("tr").after(`
          <tr data-id="`+seindex+`">
                  <td>
                    <input type="hidden" name="dailySetList[` + (Number(setListSize)+Number(1)) + `].isParent" value="0">
                    <input type="hidden" name="dailySetList[` + (Number(setListSize)+Number(1)) + `].parentId" value="` + $(_this).attr("id") + `">
                    <input
                      class="form-control credit-control"
                      type="text"
                      value=""
                      name="dailySetList[`+(Number(setListSize)+Number(1))+`].name"
                      style="width: 180px;"
                    />
                    <i class="wpfont icon-plus credit-add" style="color:#317EEB" id="` + $(_this).attr("id")  + `" onclick="addminscroe(this)"></i>
                    <i class="fa fa-trash-o credit-delete" style="color:#317EEB"></i>
                  </td>
                </tr>
          `);
        setListSize = setListSize+1;
    }
    $(function() {
        // 编辑弹窗
        //删除评分项
        $("body").on("click", ".credit-delete", function(e) {
            e.preventDefault();
            var seindex = $(this)
                    .parents("tr")
                    .attr("data-id");
            var firstindex = false;
            if ($(this).parents("tr").find("td").length < 3)
            {
                firstindex = true;
                $(this).parents("tr").remove();
            }

            var num = 0;
            $(".layer-new").find("tr").each((i, n) => {
                if ($(n).attr("data-id") == seindex) {
                    $(n)
                            .find("td")
                            .attr("rowspan", 1);
                    num++;
                }
            });
            if (firstindex) {
                $(".layer-new")
                        .find("tr")
                        .each((i, n) => {
                            if ($(n).attr("data-id") == seindex) {
                                $(n)
                                        .find("td")
                                        .first()
                                        .attr("rowspan", num);
                                $(n)
                                        .find("td")
                                        .last()
                                        .attr("rowspan", num);
                                return false;
                            }
                        });
            } else {
                var islong = 0;
                $(".layer-new")
                        .find("tr")
                        .each((i, n) => {
                            if ($(n).attr("data-id") == seindex) {
                                islong++;
                            }
                        });
                if (islong > 1) {
                    $(this)
                            .parents("tr")
                            .find("td")
                            .eq(1)
                            .find("input")
                            .val(
                                    $(this)
                                            .parents("tr")
                                            .next()
                                            .find("td")
                                            .find("input")
                                            .val()
                            );
                    $(this)
                            .parents("tr")
                            .next()
                            .remove();

                    $(".layer-new")
                            .find("tr")
                            .each((i, n) => {
                                if ($(n).attr("data-id") == seindex) {
                                    $(n)
                                            .find("td")
                                            .first()
                                            .attr("rowspan", num - 1);
                                    $(n)
                                            .find("td")
                                            .last()
                                            .attr("rowspan", num - 1);
                                    return false;
                                }
                            });
                } else {
                    $(this)
                            .parents("tr")
                            .remove();
                }
            }
        });
    });
    function delfen(_this) {
        var url = "${request.contextPath}/exammanage/credit/delCreditDailySet";
        $.ajax({
            url:url,
            data:{'id':$(_this).attr("id")},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success) {
                    layerTipMsg(jsonO.success, "删除失败！", jsonO.msg);
                    isSubmit = false;
                    return;
                }else {
                    var seindex = $(_this)
                            .parents("tr")
                            .attr("data-id");
                    $(".layer-new")
                            .find("tr")
                            .each((i, n) => {
                                if ($(n).attr("data-id") == seindex) {
                                    $(n).remove();
                                }
                            });
                }
            }

        })
    }
    var dataid = 0;
    <#if creditSet.dailySetList?exists&& (creditSet.dailySetList?size > 0)>
        <#list creditSet.dailySetList as item>
            <#if item.parentId?default("")?trim?length lt 1>
                ++dataid;
            </#if>
        </#list>
    </#if>
    function addfen() {
        ++dataid;
        var id=0;
        var url = "${request.contextPath}/exammanage/credit/addCreditDailySet";
        $.ajax({
            url:url,
            data:{'setId':'${creditSet.id!}'},
            dataType : 'json',
            type : 'post',
            success : function(data){
                var jsonO = data;
                if(!jsonO.success) {
                    layerTipMsg(jsonO.success, "添加失败！", jsonO.msg);
                    isSubmit = false;
                    return;
                }else {
                    if (jsonO.id) {
                        id=jsonO.id;
                        $(".layer-new").find("table").append(`<tr data-id="` + dataid + `">
                          <td rowspan="1">
                            <input type="hidden" name="dailySetList[` + setListSize + `].isParent" value="1">
                            <input type="hidden" name="dailySetList[` + setListSize + `].id" value="` + id + `">
                            <input class="form-control credit-control" name="dailySetList[` + setListSize + `].name" type="text" value="" style="width: 200px;" />
                          </td>
                          <td>
                            <input type="hidden" name="dailySetList[` + (Number(setListSize)+Number(1)) + `].isParent" value="0">
                            <input type="hidden" name="dailySetList[` + (Number(setListSize)+Number(1)) + `].parentId" value="` + id + `">
                            <input
                              class="form-control credit-control" name="dailySetList[` + (Number(setListSize)+Number(1)) + `].name"
                              type="text"
                              value=""
                              style="width: 180px;"
                            />
                            <i
                              class="wpfont icon-plus credit-add"
                              style="color:#317EEB"
                              id="` + id + `"
                              onclick="addminscroe(this)"
                            ></i>
                            <i
                              class="fa fa-trash-o credit-delete"
                              style="color:#317EEB"
                            ></i>
                          </td>
                          <td rowspan="1">
                            <a onclick="delfen(this)" class="btn btn-link btn-info credit-text" id="`+id+`">
                              删除
                            </a>
                          </td>
                        </tr>`);
                        setListSize = setListSize + 1;
                    }
                }
            }

        })
    }
</script>