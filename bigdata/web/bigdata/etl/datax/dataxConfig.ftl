<div class="elt-body scrollBar4">
    <!-- 基本参数 -->
    <div class="form-horizontal elt-form">
        <div class="form-group">
            <h4 class="col-sm-2 control-label mt-30">基本参数</h4>　
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label"><font color="red">*</font>通道数</label>
            <#--<div class="col-sm-6">-->
            <#--<input type="text" name="channel" value="${dataxJobInsParamMap['channel']!}"-->
            <#--class="form-control jobParam required" placeholder="请输入通道数" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"-->
            <#--onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="2">-->
            <#--</div>-->
            <div class="col-sm-6">
                <input type="text" name="channel" class="form-control jobParam required"
                       value="${dataxJobInsParamMap['channel']!}" id="channelRange">
            </div>
            <img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png" data-toggle="tooltip"
                 data-placement="right" title="任务管道数（并发）">
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">记录流速度</label>
            <div class="col-sm-6">
                <input type="text" id="byteInput" name="byte" value="${dataxJobInsParamMap['byte']!}"
                       class="form-control jobParam"
                       placeholder="请输入记录流速度（非必填）"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
            </div>
            <img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png" data-toggle="tooltip"
                 data-placement="right" title="单位为byte/s，会尽可能接近该值但是不会超过它，可使用默认配置">
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">字节流</label>
            <div class="col-sm-6">
                <input type="text" name="record" value="${dataxJobInsParamMap['record']!}" class="form-control jobParam"
                       placeholder="请输入字节流（非必填）"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
            </div>
            <img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png" data-toggle="tooltip"
                 data-placement="right" title="单位为byte/s，会尽可能接近该值但是不会超过它，可使用默认配置">
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">脏数据限制</label>
            <div class="col-sm-6">
                <input type="text" name="errorRecord" value="${dataxJobInsParamMap['errorRecord']!}"
                       class="form-control jobParam" placeholder="请输入脏数据限制（非必填）"
                       onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                       onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
            </div>
            <img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png" data-toggle="tooltip"
                 data-placement="right" title="脏数据的上限，如果一个表超过该值，那么就会导入失败并报错">
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label mt-20">脏数据所占比例上限</label>
            <#--<div class="col-sm-6">-->
            <#--<input type="text" name="errorPercentage" value="${dataxJobInsParamMap['errorPercentage']!}"-->
            <#--class="form-control" placeholder="请输入脏数据所占比例上限">-->
            <#--</div>-->
            <div class="col-sm-6 mt-20">
                <input type="text" name="errorPercentage" class="form-control jobParam"
                       value="${dataxJobInsParamMap['errorPercentage']?default('1.0')}" id="errorPercentageRange">
            </div>
            <img class="mt-20" src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                 data-toggle="tooltip"
                 data-placement="right" title="脏数据所占比例上限，1.0代表100%">
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">是否增量同步</label>
            <div class="col-sm-6">
                <label class="switch switch-txt mt-5">
                    <input type="checkbox" <#if dataxJobIns.isIncrement?default(0) == 1>checked="checked"</#if>
                           id="isIncrement">
                    <span class="switch-name">显示</span>
                </label>
            </div>
            <img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png" data-toggle="tooltip"
                 data-placement="right"
                 title="如果开启了增量同步，每次只会同步上次同步时间至今产生的数据，开启增量同步，必须配置where筛选条件或者自定义sql中增加时间筛选，例如 creationTime > '${r'${start_time}'}' and creationTime < '${r'${end_time}'}',(必须加上引号！否则会读取不到数据)">
        </div>
    </div>
    <input type="hidden" name="oldSourceId" class="jobParam" value="${dataxJobInsParamMap['appId']!}">
    <input type="hidden" name="oldTargetId" class="jobParam" value="${dataxJobInsParamMap['targetId']!}">
    <!-- 源数据 -->
    <div class="box-table-made pt-10">
        <div class="clearfix">
            <div class="half-box clearfix">
                <div class="box-table-head clearfix">
                    <b>源数据</b>&nbsp;<img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                                         data-toggle="tooltip"
                                         data-placement="right" title="读取系统添加的数据源，目前支持关系型数据库mysql、oracle、sqlserver,非关系型数据库hbase，本地文件和ftp文件读取">
                    <div class="pull-right clearfix">
                        <div class="btn-group filter fn-left">
                            <button class="btn btn-default dropdown-toggle">
                                <#if dataxJobInsParamMap['sourceType']?default('') == ''>选择源数据类型</#if>
                                <#if dataxJobInsParamMap['sourceType']?default('') == 'datasource'>数据源</#if>
                                <#if dataxJobInsParamMap['sourceType']?default('') == 'textfile'>本地文件</#if>
                                <#if dataxJobInsParamMap['sourceType']?default('') == 'ftp'>FTP文件</#if>
                            </button>
                            <ul class="dropdown-menu right-side" id="sourceTypeSelect">
                                <li sourceType="datasource" style="cursor: pointer"
                                    <#if 'datasource' == dataxJobInsParamMap['sourceType']!>class="active"</#if>>
                                    <a>数据源</a>
                                </li>
                                <li sourceType="textfile" style="cursor: pointer"
                                    <#if 'textfile' == dataxJobInsParamMap['sourceType']!>class="active"</#if>>
                                    <a>本地文件</a>
                                </li>
                                <li sourceType="ftp" style="cursor: pointer"
                                    <#if 'ftp' == dataxJobInsParamMap['sourceType']!>class="active"</#if>><a>FTP文件</a>
                                </li>
                            </ul>
                        </div>
                        <div class="btn-group filter fn-left">
                            <button class="btn btn-default dropdown-toggle">${datasourceMap[dataxJobInsParamMap['sourceId']?default('')]?default('选择数据源')}</button>
                            <ul class="dropdown-menu" id="sourceDB">
                                <#list dbList as item>
                                    <li datasourceId="${item.id!}"
                                        datasourceType="${item.thumbnail?replace(".png", "")}"
                                        style="cursor: pointer"
                                        <#if item.id! == dataxJobInsParamMap['sourceId']!>class="active"</#if> >
                                        <a>${item.name!}</a>
                                    </li>
                                </#list>
                                <#list nosqlDatabaseList as item>
                                    <li datasourceId="${item.id!}"
                                        datasourceType="${item.thumbnail?replace(".png", "")}"
                                        connectMode="${item.connectMode!}"
                                        style="cursor: pointer"
                                        <#if item.id! == dataxJobInsParamMap['sourceId']!>class="active"</#if> >
                                        <a>${item.name!}</a>
                                    </li>
                                </#list>
                            </ul>
                        </div>

                        <div class="btn-group filter fn-left">
                            <button class="btn btn-default dropdown-toggle">${appMap[dataxJobInsParamMap['appId']?default('')]?default('选择应用')}</button>
                            <ul class="dropdown-menu" id="sourceApp">
                                <#list apps as item>
                                    <li appId="${item.id!}" style="cursor: pointer"
                                        <#if item.id! == dataxJobInsParamMap['appId']!>class="active"</#if> >
                                        <a>${item.name!}</a>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="box-table-body scrollBar4" id="readerConfigDiv">
                    <table class="tables" style="height: 180px;">
                    </table>
                </div>
            </div>
            <div class="half-box clearfix">
                <div class="box-table-head">
                    <b>目标数据</b>&nbsp;<img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                                          data-toggle="tooltip"
                                          data-placement="right" title="支持mysql、hbase，kafka消息队列">
                    <div class="pull-right clearfix">

                        <div class="btn-group filter fn-left">
                            <button class="btn btn-default dropdown-toggle">
                                <#if dataxJobInsParamMap['targetType']?default('') == ''>选择目标数据类型</#if>
                                <#if dataxJobInsParamMap['targetType']?default('') == 'metadata'>数据表</#if>
                                <#if dataxJobInsParamMap['targetType']?default('') == 'message'>消息队列</#if>
                            </button>
                            <ul class="dropdown-menu right-side" id="targetTypeSelect">
                                <li targetType="metadata" style="cursor: pointer"
                                    <#if 'metadata' == dataxJobInsParamMap['targetType']!>class="active"</#if>>
                                    <a>数据表</a></li>
                                <li targetType="message" style="cursor: pointer"
                                    <#if 'message' == dataxJobInsParamMap['targetType']!>class="active"</#if>>
                                    <a>消息队列</a>
                                </li>
                            </ul>
                        </div>

                        <div class="btn-group filter fn-left <#if dataxJobInsParamMap['targetId']?default('') == ''>hide</#if>">
                            <button class="btn btn-default dropdown-toggle">${metadataMap[dataxJobInsParamMap['targetId']?default('')]?default('选择表')}</button>
                            <ul class="dropdown-menu right-side" id="targetDB">
                                <#list metadataList as item>
                                    <li metadataId="${item.id!}" dbType="${item.dbType!}" style="cursor: pointer"
                                        <#if item.id! == dataxJobInsParamMap['targetId']!>class="active"</#if>>
                                        <a>${item.name!}</a></li>
                                </#list>
                            </ul>
                        </div>

                    </div>
                </div>
                <div class="box-table-body" id="writerConfigDiv">
                </div>
            </div>
        </div>
    </div>

    <!-- 映射关系 -->
    <div class="mapping-relation">
        <div class="mapping-relation-head">
            <b>映射关系</b>&nbsp;<img src="${request.contextPath}/bigdata/v3/static/images/public/img-focus.png"
                                  data-toggle="tooltip"
                                  data-placement="right"
                                  title="字段映射关系，左右字段必须要对应，根据字段名进行匹配，可以根据业务需求添加字段转换规则，每个字段可以添加多个规则，目前支持截取、填补、替换、过滤、正则表达式、数据字典、非空7种转换规则。">
        </div>
        <div class="mapping-relation-body">
            <#if writerColumns?exists && writerColumns?size gt 0>
                <#list writerColumns as column>
                    <div class="single-mapping position-relative clearfix">
                        <div class="mapping-one">
                            <div class="box-content"><span class="readerColumn">${readerColumns[column_index]!}</span>
                                <div class="pos-right"><i class="iconfont icon-editor-fill js-edit-column"></i></div>
                            </div>
                        </div>
                        <div class="mapping-two">
                            <#if transformerMap[column]?exists>
                                <#list transformerMap[column] as rule>
                                    <div class="box-content">
                                        <span class="transformer_rule" rule=${rule!}>
                                            <#if rule?contains('dx_substr')>
                                                截取
                                            </#if>
                                            <#if rule?contains('dx_pad')>
                                                填补
                                            </#if>
                                            <#if rule?contains('dx_replace')>
                                                替换
                                            </#if>
                                            <#if rule?contains('dx_filter')>
                                                过滤
                                            </#if>
                                            <#if rule?contains('dx_regex')>
                                                正则表达式
                                            </#if>
                                            <#if rule?contains('dx_data_dict')>
                                                数据字典
                                            </#if>
                                            <#if rule?contains('dx_not_null')>
                                                非空
                                            </#if>
                                        </span>
                                        <div class="pos-right">
                                            <i class="iconfont icon-delete-bell js-remove-field"></i>
                                            <i class="iconfont icon-editor-fill js-edit-two"></i>
                                        </div>
                                    </div>
                                </#list>
                            </#if>
                            <div class="box-content add-mapping-rule"><span order="${column_index+1}"
                                                                            columnId="${column!}">+添加规则</span></div>
                        </div>
                        <div class="mapping-one clearfix">
                            <div class="box-content pull-right"><span class="writerColumn"
                                                                      columnId="${column!}">${column!}</span>
                                <div class="pos-right"><i class="iconfont icon-delete-bell js-remove-all"></i></div>
                            </div>
                        </div>
                    </div>
                </#list>
                <div class="single-mapping add-mapping-wrap clearfix">
                    <div class="mapping clearfix">
                        <div style="float: right;"
                             class="box-content <#if dataxJobInsParamMap['kafkaTopic']?exists>add-kafka-mapping<#else>add-mapping</#if>">
                            <span>+添加</span>
                        </div>
                    </div>
                </div>
            </#if>
        </div>
    </div>

    <div class="layer layer-mapping-rule">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label">类型：</label>
                <div class="col-sm-8">
                    <select class="form-control" id="transformerSelect">
                        <option value="dx_substr">截取</option>
                        <option value="dx_pad">填补</option>
                        <option value="dx_replace ">替换</option>
                        <option value="dx_filter">过滤</option>
                        <option value="dx_regex">正则表达式</option>
                        <option value="dx_data_dict">数据字典</option>
                        <option value="dx_not_null">非空</option>
                    </select>
                </div>
                <label class="col-sm-3 control-label">字段编号：</label>
                <div class="col-sm-8" style="margin-top: 5px;">
                    <input id="columnIndex" class="form-control" readonly="readonly" value="">
                </div>

                <div class="trans_div dx_substr">
                    <label class="col-sm-3 control-label">开始位置：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <input id="substrStartIndex" placeholder="输入字段值的开始位置" class="form-control" value=""
                               onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               maxlength="3">
                    </div>
                    <label class="col-sm-3 control-label">目标字段长度：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <input id="substrLength" placeholder="输入目标字段长度" class="form-control" value=""
                               onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               maxlength="3">
                    </div>
                </div>

                <div class="trans_div dx_pad hide">
                    <label class="col-sm-3 control-label">目标字段长度：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <input id="padLength" placeholder="输入目标字段长度" class="form-control" value=""
                               onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               maxlength="3">
                    </div>

                    <label class="col-sm-3 control-label">填补位置：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <select class="form-control" id="padPosition">
                            <option value="l">头</option>
                            <option value="r">尾</option>
                        </select>
                    </div>

                    <label class="col-sm-3 control-label">填补的字符：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <input id="padStr" class="form-control" value="" placeholder="输入填补的字符">
                    </div>
                </div>

                <div class="trans_div dx_replace hide">
                    <label class="col-sm-3 control-label">开始位置：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <input id="replaceStartIndex" placeholder="输入字段值的开始位置" class="form-control" value=""
                               onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               maxlength="3">
                    </div>

                    <label class="col-sm-3 control-label">替换字段长度：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <input id="replaceLength" placeholder="输入替换的字段长度" class="form-control" value=""
                               onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                               maxlength="3">
                    </div>

                    <label class="col-sm-3 control-label">替换字符串：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <input id="replaceStr" class="form-control" value="" placeholder="输入替换的字符串">
                    </div>
                </div>

                <div class="trans_div dx_filter hide">
                    <label class="col-sm-3 control-label">运算符：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <select class="form-control" id="operator">
                            <option value="like">like</option>
                            <option value="notlike">not like</option>
                            <option value=">">></option>
                            <option value="=">=</option>
                            <option value="<"><</option>
                            <option value=">=">>=</option>
                            <option value="!=">!=</option>
                            <option value="<="><=</option>
                        </select>
                    </div>

                    <label class="col-sm-3 control-label">正则表达式：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <textarea rows="3" class="form-control" id="filterRegexStr" placeholder="输入正则表达式或值"></textarea>
                    </div>
                </div>

                <div class="trans_div dx_regex hide">
                    <label class="col-sm-3 control-label">规则：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <textarea rows="3" class="form-control" id="regexRule" placeholder="输入正则表达式规则"></textarea>
                    </div>
                </div>

                <div class="trans_div dx_data_dict hide">
                    <label class="col-sm-3 control-label">数据字典：</label>
                    <div class="col-sm-8" style="margin-top: 5px;">
                        <input id="dictStr" class="form-control" placeholder="输入数据字典">
                    </div>
                </div>

                <div class="col-sm-12 mt-30 trans_div dx_substr" style="background: azure;">
                    <span style="display: block">截取 dx_substr</span></span>
                    <span style="display: block">参数：3个</span>
                    <span style="display: block">第一个参数：字段编号，对应record中第几个字段。</span>
                    <span style="display: block">第二个参数：字段值的开始位置。</span>
                    <span style="display: block">第三个参数：目标字段长度。</span>
                    <span style="display: block">返回： 从字符串的指定位置（包含）截取指定长度的字符串。如果开始位置非法抛出异常。如果字段为空值，直接返回（即不参与本transformer）</span>
                    <span style="display: block">举例：</span>
                    <span style="display: block">dx_substr(1,"2","5")  column 1的value为“dataxTest”=>"taxTe"</span>
                    <span style="display: block">dx_substr(1,"5","10")  column 1的value为“dataxTest”=>"Test"</span>
                </div>

                <div class="col-sm-12 mt-30 trans_div dx_pad hide" style="background: azure;">
                    <span style="display: block">填补 dx_pad</span>
                    <span style="display: block">第一个参数：字段编号，对应record中第几个字段。</span>
                    <span style="display: block">第二个参数："l","r", 指示是在头进行pad，还是尾进行pad。</span>
                    <span style="display: block">第三个参数：目标字段长度。</span>
                    <span style="display: block">第四个参数：需要pad的字符。</span>
                    <span style="display: block">返回： 如果源字符串长度小于目标字段长度，按照位置添加pad字符后返回。如果长于，直接截断（都截右边）。如果字段为空值，转换为空字符串进行pad，即最后的字符串全是需要pad的字符</span>
                    <span style="display: block">举例：</span>
                    <span style="display: block">dx_pad(1,"l","4","A"), 如果column 1 的值为 xyz=> Axyz， 值为 xyzzzzz => xyzz</span>
                    <span style="display: block">dx_pad(1,"r","4","A"), 如果column 1 的值为 xyz=> xyzA， 值为 xyzzzzz => xyzz</span>
                </div>

                <div class="col-sm-12 mt-30 trans_div dx_replace hide" style="background: azure;">
                    <span style="display: block">替换 dx_replace</span>
                    <span style="display: block">参数：4个</span>
                    <span style="display: block">第一个参数：字段编号，对应record中第几个字段。</span>
                    <span style="display: block">第二个参数：字段值的开始位置。</span>
                    <span style="display: block">第三个参数：需要替换的字段长度。</span>
                    <span style="display: block">第四个参数：需要替换的字符串。</span>
                    <span style="display: block">返回： 从字符串的指定位置（包含）替换指定长度的字符串。如果开始位置非法抛出异常。如果字段为空值，直接返回（即不参与本transformer）</span>
                    <span style="display: block">举例：</span>
                    <span style="display: block">dx_replace(1,"2","4","****")  column 1的value为“dataxTest”=>"da****est"</span>
                    <span style="display: block">dx_replace(1,"5","10","****")  column 1的value为“dataxTest”=>"data****"</span>
                </div>

                <div class="col-sm-12 mt-30 trans_div dx_filter hide" style="background: azure;">
                    <span style="display: block">过滤 dx_filter</span>
                    <span style="display: block">参数：</span>
                    <span style="display: block">第一个参数：字段编号，对应record中第几个字段。</span>
                    <span style="display: block">第二个参数：运算符，支持一下运算符：like, not like, >, =, <, >=, !=, <=</span>
                    <span style="display: block">第三个参数：正则表达式（java正则表达式）、值。</span>
                    <span style="display: block">返回：</span>
                    <span style="display: block">如果匹配正则表达式，返回Null，表示过滤该行。不匹配表达式时，表示保留该行。（注意是该行）。对于>=<都是对字段直接compare的结果.</span>
                    <span style="display: block">        like ， not like是将字段转换成String，然后和目标正则表达式进行全匹配。</span>
                    <span style="display: block">        , =, <, >=, !=, <= 对于DoubleColumn比较double值，对于LongColumn和DateColumn比较long值，其他StringColumn，BooleanColumn以及ByteColumn均比较的是StringColumn值。</span>
                    <span style="display: block">如果目标colunn为空（null），对于 = null的过滤条件，将满足条件，被过滤。！=null的过滤条件，null不满足过滤条件，不被过滤。 like，字段为null不满足条件，不被过滤，和not like，字段为null满足条件，被过滤。</span>
                    <span style="display: block">举例：</span>
                    <span style="display: block">dx_filter(1,"like","dataTest")</span>
                    <span style="display: block">dx_filter(1,">=","10")</span>
                </div>

                <div class="col-sm-12 mt-30 trans_div dx_regex hide" style="background: azure;">
                    <span style="display: block">正则表达式 dx_regex</span>
                    <span style="display: block">参数：</span>
                    <span style="display: block">第一个参数：字段编号，对应record中第几个字段。</span>
                    <span style="display: block">第二个参数：正则表达式（java正则表达式）、值。</span>
                    <span style="display: block">返回：</span>
                    <span style="display: block">如果匹配正则表达式，返回Null，表示过滤该行。不匹配表达式时，表示保留该行。（注意是该行）</span>
                    <span style="display: block">举例：</span>
                    <span style="display: block">dx_regex(1, "/\w+@[a-z0-9]+\.[a-z]{2,4}/")</span>
                </div>

                <div class="col-sm-12 mt-30 trans_div dx_data_dict hide" style="background: azure;">
                    <span style="display: block">数据字典 dx_data_dict</span>
                    <span style="display: block">参数：</span>
                    <span style="display: block">第一个参数：字段编号，对应record中第几个字段。</span>
                    <span style="display: block">第二个参数：数据字典。</span>
                    <span style="display: block">返回：</span>
                    <span style="display: block">如果匹配数据字典key，返回数据字段value,格式:{code:value, code1:value1}</span>
                    <span style="display: block">举例：</span>
                    <span style="display: block">dx_data_dict(1, "{"1":"男","2":"女"}")</span>
                </div>

                <div class="col-sm-12 mt-30 trans_div dx_not_null hide" style="background: azure;">
                    <span style="display: block">字段非空 dx_not_null</span>
                    <span style="display: block">参数：</span>
                    <span style="display: block">第一个参数：字段编号，对应record中第几个字段。</span>
                    <span style="display: block">返回：</span>
                    <span style="display: block">如果字段不是空则返回，否则过滤</span>
                    <span style="display: block">举例：</span>
                    <span style="display: block">dx_not_null(1)</span>
                </div>
            </div>
        </div>
    </div>
    <div class="layer layer-mapping">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label">字段：</label>
                <div class="col-sm-8">
                    <select class="form-control" id="fieldSelect">
                    </select>
                </div>
            </div>
        </div>
    </div>
    <div class="layer layer-column-type">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label">字段类型：</label>
                <div class="col-sm-8">
                    <select class="form-control" id="columnTypeSelect" onchange="changeColumnType()">
                        <option value="string">string</option>
                        <option value="long">long</option>
                        <option value="double">double</option>
                        <option value="boolean">boolean</option>
                        <option value="date">date</option>
                    </select>
                </div>
            </div>
            <div class="form-group hide">
                <label class="col-sm-3 control-label">格式：</label>
                <div class="col-sm-8">
                    <input class="form-control" id="dateFormat" value="yyyy-MM-dd">
                </div>
            </div>
        </div>
    </div>
    <div class="layer layer-kafka-mapping">
        <div class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-3 control-label">字段名：</label>
                <div class="col-sm-8">
                    <input type="text" id="kafkaColumn" class="form-control">
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 保存按钮 -->
<div class="save-btn-wrap centered">
    <button type="button" id="saveBtn" class="btn btn-lg btn-blue">保存</button>
</div>
<script>
    $(function () {

        //数值范围选择
        $('#channelRange').jRange({
            theme: '',
            from: 1,
            to: 16,
            step: 1,
            scale: [1, 6, 11, 16],
            format: '%s',
            showLabels: true,
            width: $('#byteInput').width() + 20,
            isRange: false
        });

        //数值范围选择
        $('#errorPercentageRange').jRange({
            theme: '',
            from: 0.01,
            to: 1,
            step: 0.01,
            scale: [0.01, 0.34, 0.67, 1],
            format: '%s',
            showLabels: true,
            width: $('#byteInput').width() + 20,
            isRange: false
        });

        //数值范围选择
        $('#kafkaPartitionRange').jRange({
            theme: '',
            from: 1,
            to: 5,
            step: 1,
            scale: [1, 3, 5],
            format: '%s',
            showLabels: true,
            isRange: false
        });

        //数值范围选择
        $('#kafkaReplicationFactorRange').jRange({
            theme: '',
            from: 1,
            to: 3,
            step: 1,
            scale: [1, 2, 3],
            format: '%s',
            showLabels: true,
            isRange: false
        });

        $('[data-toggle="tooltip"]').tooltip();

        //屏蔽不可用状态事件
        $('.disabled a, a.disabled').click(function (e) {
            e.preventDefault();
        });

        //屏蔽a链接选中虚线
        $('a').attr('hidefocus', 'true');

        //下拉菜单
        $('.dropdown-toggle').not(':disabled').click(function (e) {
            e.preventDefault();
            $('.dropdown-toggle').each(function () {
                var $g = $(this).parent('.btn-group');
                if ($g.hasClass('open')) {
                    $g.removeClass('open');
                }
            });
            $(this).parent('.btn-group').toggleClass('open');
        });
        $('.dropdown-menu li:not(.disabled)').click(function (e) {
            //e.preventDefault();
            var $btnGroup = $(this).parents('.btn-group');
            $(this).addClass('active').siblings('li').removeClass('active');
            $btnGroup.removeClass('open');
            if ($btnGroup.hasClass('filter')) {
                e.preventDefault();
                var txt = $(this).text();
                $btnGroup.children('.dropdown-toggle').text(txt);
            }
        });
        $(document).click(function (event) {
            var eo = $(event.target);
            if ($('.dropdown-menu').is(':visible') && eo.attr('class') != 'dropdown-toggle' && !eo.parent('.btn-group').length && !eo.parents('.dropdown-menu').length)
                $('.btn-group').removeClass('open');
        });

        $('#saveBtn').click(function () {

            // 参数map
            var paramMap = {};

            if ($('#sourceTypeSelect li.active').length < 1) {
                showLayerTips4Confirm('error', '请选择源数据类型');
                return;
            }

            if ($('#sourceTypeSelect li.active').attr('sourceType') == 'datasource') {
                if ($('#sourceDB li.active').length < 1) {
                    showLayerTips4Confirm('error', '请选择源数据');
                    return;
                }
                paramMap['sourceId'] = $('#sourceDB li.active').attr('dataSourceId');
            }

            if ($('#targetTypeSelect li.active').length < 1) {
                showLayerTips4Confirm('error', '请选择目标数据类型');
                return;
            }

            if ($('#targetTypeSelect li.active').attr('targetType') == 'metadata') {
                if ($('#targetDB li.active').length < 1) {
                    showLayerTips4Confirm('error', '请选择目标数据');
                    return;
                }
                paramMap['targetId'] = $('#targetDB li.active').attr('metadataId');
            }

            if ($('#sourceApp li.active').length < 1) {
                showLayerTips4Confirm('error', '请选择应用');
                return;
            }

            paramMap['sourceType'] = $('#sourceTypeSelect li.active').attr('sourceType');
            paramMap['targetType'] = $('#targetTypeSelect li.active').attr('targetType');
            paramMap['appId'] = $('#sourceApp li.active').attr('appId');

            var isTrue = true;
            $.each($('.jobParam'), function (i, v) {
                // 判断是否必填
                if ($(v).hasClass('required')) {
                    if ($(v).val() == null || $.trim($(v).val()) == '') {
                        layer.tips("不能为空", $(v), {
                            tipsMore: true,
                            tips: 3
                        });
                        isTrue = false;
                        return;
                    }
                }
                if (!isTrue) {
                    return;
                }
                if ($(v).attr('type') == 'radio') {
                    if ($(v).is(':checked')) {
                        paramMap[$(v).attr('name')] = $(v).val();
                    }
                } else {
                    paramMap[$(v).attr('name')] = $(v).val();
                }
            });

            if (!isTrue) {
                return;
            }

            if ($('.writerColumn').length < 1) {
                showLayerTips4Confirm('error', '请添加字段映射关系');
                return;
            }

            var readerColumnArray = [];
            // 读字段
            $.each($('.readerColumn'), function (i, v) {
                if (paramMap['sourceType'] == 'textfile' || paramMap['sourceType'] == 'ftp') {
                    if ($(v).text() == '字段名称') {
                        isTrue = false;
                        return;
                    }
                }
                readerColumnArray.push($(v).text());
            });

            if (!isTrue) {
                showLayerTips4Confirm('error', '文件或者FTP同步时，请选择字段类型');
                return;
            }
            paramMap['readerColumn'] = readerColumnArray.join(',');

            // 写字段
            var writerColumnArray = [];
            $.each($('.writerColumn'), function (i, v) {
                writerColumnArray.push($(v).text());
            });
            paramMap['writerColumn'] = writerColumnArray.join(',');

            var ruleArray = [];
            // 获取字段转换规则
            $.each($('.transformer_rule'), function (i, v) {
                var transformerArray = JSON.parse($(v).attr('rule'));
                ruleArray.push(transformerArray);
            });
            paramMap['transformer'] = JSON.stringify(ruleArray);

            if ($('#hbaseTable').hasClass('jobParam')) {
                paramMap['hbaseReadMode'] = $('#hbaseReadMode').val();
            }

            if ($('#isCustomSelect').val() == 'custom') {
                paramMap['querySql'] = $('#querySql').text();
            }

            var jobInsId = $('.steps li.active').attr('id');
            var jobId = $('#jobId').val();

            var isIncrement = 0;
            if ($('#isIncrement').is(':checked')) {
                isIncrement = 1;
                if ($('#querySqlBtn').hasClass('active')) {
                    var querySql = $('#querySql').text();
                    if (querySql.indexOf("${r'${start_time}'}") == -1 || querySql.indexOf("${r'${end_time}'}") == -1) {
                        layer.tips("增量同步时，筛选条件必须配置${r'${start_time}'}和${r'${end_time}'}", '#querySql', {
                            tipsMore: true,
                            tips: 3
                        });
                        isTrue = false;
                        return;
                    }
                } else {
                    var sql = $('#readerWhere').val();
                    if (sql.indexOf("${r'${start_time}'}") == -1 || sql.indexOf("${r'${end_time}'}") == -1) {
                        layer.tips("增量同步时，筛选条件必须配置${r'${start_time}'}和${r'${end_time}'}", '#readerWhere', {
                            tipsMore: true,
                            tips: 3
                        });
                        isTrue = false;
                        return;
                    }
                }
            }

            $.ajax({
                url: _contextPath + '/bigdata/datax/job/saveJobParam',
                type: 'POST',
                dataType: 'json',
                data: {
                    paramMap: JSON.stringify(paramMap),
                    jobId: jobId,
                    jobInsId: jobInsId,
                    isIncrement: isIncrement
                },
                success: function (response) {
                    if (response.success) {
                        showLayerTips('success', '保存成功', 't');
                    } else {
                        showLayerTips4Confirm('error', response.message);
                    }
                }
            });
        });

        //映射关系
        //添加字段
        $('.elt-body').on('click', '.add-mapping', function (e) {
            var self = $(this).closest('.single-mapping');

            var metadataId = $('#targetDB li.active').attr('metadataId');
            $.ajax({
                url: _contextPath + '/bigdata/metadata/' + metadataId + '/getField',
                type: 'POST',
                dataType: 'json',
                success: function (response) {
                    if (response.success) {
                        $('#fieldSelect').empty();
                        $('#fieldSelect').append('<option value="">请选择字段</option>');
                        $.each(response.data, function (i, v) {
                            console.info($('.writerColumn[columnId="' + v.columnName + '"]'));
                            if ($('.writerColumn[columnId="' + v.columnName + '"]').length > 0) {
                                $('#fieldSelect').append('<option disabled="disabled" value="' + v.columnName + '">' + v.name + '(已选择)</option>');
                            } else {
                                $('#fieldSelect').append('<option columnId="' + v.columnName + '" value="' + v.columnName + '">' + v.name + '</option>');
                            }
                        });
                    } else {
                        showLayerTips4Confirm('error', response.message);
                    }
                }
            });

            layer.open({
                type: 1,
                shade: .6,
                title: '选择字段',
                btn: ['确定', '取消'],
                area: ['500px', '200px'],
                content: $('.layer-mapping'),
                yes: function (index, layero) {

                    if ($('#fieldSelect').val() == null || $('#fieldSelect').val() == '') {
                        layer.tips("请选择字段!", '#fieldSelect', {
                            tipsMore: true,
                            tips: 3
                        });
                        return;
                    }

                    var columnName = $('#fieldSelect option:selected').val();
                    var columnId = $('#fieldSelect option:selected').attr('columnId');


                    var fieldRelation = '<div class="single-mapping position-relative clearfix">\
                                        <div class="mapping-one">\
                                            <div class="box-content">\
                                                <span class="readerColumn">字段名称</span>\
                                                <div class="pos-right">\
                                                    <i class="iconfont icon-editor-fill js-edit-column"></i>\
                                                </div>\
                                            </div>\
                                        </div>\
                                        <div class="mapping-two">\
                                            <div class="box-content add-mapping-rule">\
                                                <span order="' + $('.single-mapping').length + '" columnId="' + columnName + '">+添加规则</span>\
                                            </div>\
                                        </div>\
                                        <div class="mapping-one clearfix">\
                                            <div class="box-content pull-right">\
                                                <span class="writerColumn" columnId="' + columnName + '">' + columnName + '</span>\
                                                <div class="pos-right">\
                                                   <i class="iconfont icon-delete-bell js-remove-all"></i>\
                                                </div>\
                                            </div>\
                                        </div>\
                                    </div>';

                    self.before(fieldRelation);
                    layer.close(index);
                }
            });
        });

        //映射关系
        //添加字段
        $('.elt-body').on('click', '.add-kafka-mapping', function (e) {
            var self = $(this).closest('.single-mapping');
            $('#kafkaColumn').val('');
            layer.open({
                type: 1,
                shade: .6,
                title: '添加字段',
                btn: ['确定', '取消'],
                area: ['500px', '180px'],
                content: $('.layer-kafka-mapping'),
                yes: function (index, layero) {

                    if ($('#kafkaColumn').val() == '') {
                        layer.tips("字段名不能为空!", '#kafkaColumn', {
                            tipsMore: true,
                            tips: 3
                        });
                        return;
                    }

                    var fieldRelation = '<div class="single-mapping position-relative clearfix">\
                                        <div class="mapping-one">\
                                            <div class="box-content">\
                                                <span class="readerColumn">字段名称</span>\
                                                <div class="pos-right">\
                                                    <i class="iconfont icon-editor-fill js-edit-column"></i>\
                                                </div>\
                                            </div>\
                                        </div>\
                                        <div class="mapping-two">\
                                            <div class="box-content add-mapping-rule">\
                                                <span order="' + $('.single-mapping').length + '" columnId="' + $('#kafkaColumn').val() + '">+添加规则</span>\
                                            </div>\
                                        </div>\
                                        <div class="mapping-one clearfix">\
                                            <div class="box-content pull-right">\
                                                <span class="writerColumn" columnId="' + $('#kafkaColumn').val() + '">' + $('#kafkaColumn').val() + '</span>\
                                                <div class="pos-right">\
                                                   <i class="iconfont icon-delete-bell js-remove-all"></i>\
                                                </div>\
                                            </div>\
                                        </div>\
                                    </div>';

                    self.before(fieldRelation);
                    layer.close(index);
                }
            });
        });

        //删除
        $('.elt-body').on('click', '.js-remove-field', function () {
            target = $(this).closest('.box-content');
            layer.tips('<div class="msg">\
                                <div class="mb-20">确定删除此数据吗</div>\
                                <div class="msg-btn clearfix">\
                                    <button type="button" class="btn btn-blue js-remove-target">确定</button>\
                                    <button type="button" class="btn btn-default js-cancel">取消</button>\
                                </div>\
                            </div>', this, {
                tips: 1,
                time: 4000
            });
        });
        $('.elt-body').on('click', '.js-remove-all', function () {
            target = $(this).closest('.single-mapping');
            layer.tips('<div class="msg">\
                                <div class="mb-20">确定删除此数据吗</div>\
                                <div class="msg-btn clearfix">\
                                    <button type="button" class="btn btn-blue js-remove-target">确定</button>\
                                    <button type="button" class="btn btn-default js-cancel">取消</button>\
                                </div>\
                            </div>', this, {
                tips: 1,
                time: 4000
            });
        });

        $('body').on('click', '.js-remove-target', function () {
            target.remove();
            $('.layui-layer-tips').remove();
        });
        $('body').on('click', '.js-cancel', function () {
            $('.layui-layer-tips').remove();
        });

        // 选择目标类型
        $('.elt-body').on('click', '#sourceTypeSelect li', function () {
            var sourceType = $(this).attr('sourceType');
            var dbType = $('#sourceDB li.active').attr('datasourcetype');
            var connectmode = $('#sourceDB li.active').attr('connectmode');
            changeSourceType(sourceType, dbType, connectmode);
        });

        $('.elt-body').on('click', '#sourceDB li', function () {
            var connectmode = $(this).attr('connectmode');
            var dbType = $(this).attr('datasourceType');
            changeSourceDB(dbType, '', connectmode);
        });

        // =======================进入时加载=============================================================
        var dbType = $('#sourceDB li.active').attr('datasourceType');
        var jobInsId = $('.steps li.active').attr('id');
        var connectmode = '';
        if ('${dataxJobInsParamMap['querySql']!}' != '') {
            connectmode = 'onlySql';
        }

        changeSourceType($('#sourceTypeSelect li.active').attr('sourceType'), dbType, jobInsId, connectmode);

        // 选择目标类型
        $('.elt-body').on('click', '#targetTypeSelect li', function () {
            var targetType = $(this).attr('targetType');
            var dbType = $('#targetDB li.active').attr('dbType');
            var metadataId = $('#targetDB li.active').attr('metadataId');
            changeTargetType(targetType, dbType);
            if (targetType != 'message') {
                loadMetadataColumn(metadataId);
            }
        });

        changeTargetType($('#targetTypeSelect li.active').attr('targetType'), $('#targetDB li.active').attr('dbType'), jobInsId);

        // =======================================================================================================================

        // 选择表后
        $('.elt-body').on('click', '#targetDB li', function () {
            var metadataId = $(this).attr('metadataId');
            var type = $(this).attr('dbType');
            changeMetadata(metadataId, type);
        });

        //编辑mapping-one
        $('.elt-body').on('click', '.js-edit-column', function () {
            var target = $(this).parent().siblings('span');
            var text = target.text();
            var sourceType = $('#sourceTypeSelect li.active').attr('sourceType');
            if (sourceType == 'textfile' || sourceType == 'ftp') {
                if (text.indexOf('|') != -1) {
                    var split = text.split('|');
                    $('#columnTypeSelect').val(split[0]);
                    $('#dateFormat').val(split[1]);
                    $('#dateFormat').parent().parent().removeClass('hide');
                } else {
                    if (text != '字段类型') {
                        $('#columnTypeSelect').val(text);
                    } else {
                        $('#columnTypeSelect').val('string');
                    }
                    $('#dateFormat').parent().parent().addClass('hide');
                }

                layer.open({
                    type: 1,
                    shade: .6,
                    title: '修改名称',
                    btn: ['确定', '取消'],
                    area: ['500px', '200px'],
                    content: $('.layer-column-type'),
                    success: function (layero, index) {
                    },
                    yes: function (index, layero) {
                        if ($('#columnTypeSelect').val() == 'date') {
                            target.text($('#columnTypeSelect').val() + '|' + $('#dateFormat').val());
                        } else {
                            target.text($('#columnTypeSelect').val());
                        }
                        layer.close(index);
                    }
                });
            } else {
                var str = '<input type="text" name="" value="' + text + '"/>';
                target.text('').append(str).find('input').select().on('blur', function () {
                    target.text($(this).val());
                    $(this).remove();
                });
            }
        });

        //编辑规则
        $('.elt-body').on('click', '.js-edit-two', function () {
            $('.trans_div').find('input').val('');
            $('.trans_div').find('textarea').val('');

            var self = $(this);
            var $span = self.parent().prev();
            var rule = $span.attr('rule');
            var ruleArray = JSON.parse(rule);

            var type = ruleArray[1];
            $('#transformerSelect').val(type);
            $('#transformerSelect').trigger('change');
            var columnIndex = ruleArray[2];
            var columnId = ruleArray[0];
            $('#columnIndex').val(columnIndex);

            if (type == 'dx_substr') {
                $('#substrStartIndex').val(ruleArray[3]);
                $('#substrLength').val(ruleArray[4]);
            } else if (type == 'dx_pad') {
                $('#padPosition').val(ruleArray[3]);
                $('#padLength').val(ruleArray[4]);
                $('#padStr').val(ruleArray[5]);
            } else if (type == 'dx_replace') {
                $('#replaceStartIndex').val(ruleArray[3]);
                $('#replaceLength').val(ruleArray[4]);
                $('#replaceStr').val(ruleArray[5]);
            } else if (type == 'dx_filter') {
                $('#operator').val(ruleArray[3]);
                $('#filterRegexStr').val(ruleArray[4]);
            } else if (type == 'dx_regex') {
                $('#regexRule').val(ruleArray[3]);
            } else if (type == 'dx_data_dict') {
                $('#dictStr').val(ruleArray[3]);
            } else if (type == 'dx_not_null') {
            }

            layer.open({
                type: 1,
                shade: .6,
                title: '修改规则',
                btn: ['确定', '取消'],
                area: ['500px', '500px'],
                content: $('.layer-mapping-rule'),
                success: function (layero, index) {
                    $(".js-rule-name").val('');
                    $(".js-rule-content").val('');
                },
                yes: function (index, layero) {

                    var type = $('#transformerSelect').val();
                    var columnIndex = $('#columnIndex').val();

                    var str = '<div class="box-content">';

                    var ruleArray = [];
                    ruleArray.push(columnId);
                    ruleArray.push(type);
                    ruleArray.push(columnIndex);

                    var name = '截取';

                    if (type == 'dx_substr') {
                        var substrIndex = $('#substrStartIndex').val();
                        var substrLength = $('#substrLength').val();
                        if (substrIndex == '') {
                            showLayerTips4Confirm('error', '请输入字段开始位置');
                            return;
                        }
                        if (substrLength == '') {
                            showLayerTips4Confirm('error', '请输入目标字段长度');
                            return;
                        }
                        ruleArray.push(substrIndex);
                        ruleArray.push(substrLength);
                    } else if (type == 'dx_pad') {
                        var padPosition = $('#padPosition').val();
                        var padLength = $('#padLength').val();
                        var padStr = $('#padStr').val();
                        if (padLength == '') {
                            showLayerTips4Confirm('error', '请输入目标字段长度');
                            return;
                        }
                        if (padStr == '') {
                            showLayerTips4Confirm('error', '请输入填补的字符');
                            return;
                        }
                        ruleArray.push(padPosition);
                        ruleArray.push(padLength);
                        ruleArray.push(padStr);
                        name = '填补';
                    } else if (type == 'dx_replace') {
                        var replaceStartIndex = $('#replaceStartIndex').val();
                        var replaceLength = $('#replaceLength').val();
                        var replaceStr = $('#replaceStr').val();
                        if (replaceStartIndex == '') {
                            showLayerTips4Confirm('error', '请输入字段开始位置');
                            return;
                        }
                        if (replaceLength == '') {
                            showLayerTips4Confirm('error', '请输入字段的替换长度');
                            return;
                        }
                        if (replaceStr == '') {
                            showLayerTips4Confirm('error', '请输入替换的字符');
                            return;
                        }
                        ruleArray.push(replaceStartIndex);
                        ruleArray.push(replaceLength);
                        ruleArray.push(replaceStr);
                        name = '替换';
                    } else if (type == 'dx_filter') {
                        var operator = $('#operator').val();
                        if (operator == '>') {
                            operator = '&gt;';
                        }
                        if (operator == '>=') {
                            operator = '&gt;=';
                        }
                        var filterRegexStr = $('#filterRegexStr').val();
                        if (filterRegexStr == '') {
                            showLayerTips4Confirm('error', '请输入正则表达式值');
                            return;
                        }

                        ruleArray.push(operator);
                        ruleArray.push(filterRegexStr);
                        name = '过滤';
                    } else if (type == 'dx_regex') {
                        var regexRule = $('#regexRule').val();
                        if (regexRule == '') {
                            showLayerTips4Confirm('error', '请输入正则表达式规则');
                            return;
                        }
                        ruleArray.push(regexRule);
                        name = '正则表达式';
                    } else if (type == 'dx_data_dict') {
                        var dictStr = $('#dictStr').val();
                        if (dictStr == '') {
                            showLayerTips4Confirm('error', '请输入数据字典');
                            return;
                        }
                        ruleArray.push(dictStr);
                        name = '数据字典';
                    } else if (type == 'dx_not_null') {
                        name = '非空';
                    }
                    str = str + '<span class="transformer_rule" rule=' + JSON.stringify(ruleArray) + ' >' + name + '</span>';

                    str = str + '<div class="pos-right">\n' +
                        '                                    <i class="iconfont icon-delete-bell js-remove-field"></i>\n' +
                        '                                    <i class="iconfont icon-editor-fill js-edit-two"></i>\n' +
                        '                                </div>\n' +
                        '                            </div>';

                    self.parent().parent().before(str);
                    self.parent().parent().remove();
                    layer.close(index);
                }
            });
        });

        //添加规则
        $('.elt-body').on('click', '.add-mapping-rule', function () {
            var self = $(this);
            var columnIndex = $(this).find('span').attr('order');
            $('.trans_div').find('input').val('');
            $('.trans_div').find('textarea').val('');
            $('#columnIndex').val(columnIndex);
            $('#transformerSelect').val('dx_substr');
            $('.trans_div').addClass('hide');
            $('.dx_substr').removeClass('hide');

            var columnId = $(this).find('span').attr('columnId');
            layer.open({
                type: 1,
                shade: .6,
                title: '添加规则',
                btn: ['确定', '取消'],
                area: ['500px', '500px'],
                content: $('.layer-mapping-rule'),
                success: function (layero, index) {
                    $(".js-rule-name").val('');
                    $(".js-rule-content").val('');
                },
                yes: function (index, layero) {

                    var type = $('#transformerSelect').val();
                    var columnIndex = $('#columnIndex').val();

                    var str = '<div class="box-content">';

                    var ruleArray = [];
                    ruleArray.push(columnId);
                    ruleArray.push(type);
                    ruleArray.push(columnIndex);

                    var name = '截取';

                    if (type == 'dx_substr') {
                        var substrIndex = $('#substrStartIndex').val();
                        var substrLength = $('#substrLength').val();
                        if (substrIndex == '') {
                            showLayerTips4Confirm('error', '请输入字段开始位置');
                            return;
                        }
                        if (substrLength == '') {
                            showLayerTips4Confirm('error', '请输入目标字段长度');
                            return;
                        }
                        ruleArray.push(substrIndex);
                        ruleArray.push(substrLength);
                    } else if (type == 'dx_pad') {
                        var padPosition = $('#padPosition').val();
                        var padLength = $('#padLength').val();
                        var padStr = $('#padStr').val();
                        if (padLength == '') {
                            showLayerTips4Confirm('error', '请输入目标字段长度');
                            return;
                        }
                        if (padStr == '') {
                            showLayerTips4Confirm('error', '请输入填补的字符');
                            return;
                        }
                        ruleArray.push(padPosition);
                        ruleArray.push(padLength);
                        ruleArray.push(padStr);
                        name = '填补';
                    } else if (type == 'dx_replace') {
                        var replaceStartIndex = $('#replaceStartIndex').val();
                        var replaceLength = $('#replaceLength').val();
                        var replaceStr = $('#replaceStr').val();
                        if (replaceStartIndex == '') {
                            showLayerTips4Confirm('error', '请输入字段开始位置');
                            return;
                        }
                        if (replaceLength == '') {
                            showLayerTips4Confirm('error', '请输入字段的替换长度');
                            return;
                        }
                        if (replaceStr == '') {
                            showLayerTips4Confirm('error', '请输入替换的字符');
                            return;
                        }
                        ruleArray.push(replaceStartIndex);
                        ruleArray.push(replaceLength);
                        ruleArray.push(replaceStr);
                        name = '替换';
                    } else if (type == 'dx_filter') {
                        var operator = $('#operator').val();
                        if (operator == '>') {
                            operator = '&gt;';
                        }
                        if (operator == '>=') {
                            operator = '&gt;=';
                        }
                        var filterRegexStr = $('#filterRegexStr').val();
                        if (filterRegexStr == '') {
                            showLayerTips4Confirm('error', '请输入正则表达式值');
                            return;
                        }

                        ruleArray.push(operator);
                        ruleArray.push(filterRegexStr);
                        name = '过滤';
                    } else if (type == 'dx_regex') {
                        var regexRule = $('#regexRule').val();
                        if (regexRule == '') {
                            showLayerTips4Confirm('error', '请输入正则表达式规则');
                            return;
                        }
                        ruleArray.push(regexRule);
                        name = '正则表达式';
                    } else if (type == 'dx_data_dict') {
                        var dictStr = $('#dictStr').val();
                        if (dictStr == '') {
                            showLayerTips4Confirm('error', '请输入数据字典');
                            return;
                        }
                        ruleArray.push(dictStr);
                        name = '数据字典';
                    } else if (type == 'dx_not_null') {
                        name = '非空';
                    }
                    str = str + '<span class="transformer_rule" rule=' + JSON.stringify(ruleArray) + ' >' + name + '</span>';
                    str = str + '<div class="pos-right">\n' +
                        '                                    <i class="iconfont icon-delete-bell js-remove-field"></i>\n' +
                        '                                    <i class="iconfont icon-editor-fill js-edit-two"></i>\n' +
                        '                                </div>\n' +
                        '                            </div>';
                    self.before(str);
                    layer.close(index);
                }
            });
        });

        $('#transformerSelect').change(function () {
            var type = $(this).val();
            $('.trans_div').addClass('hide');
            $('.' + type).removeClass('hide');
        });

    });

    function changeSourceDB(dbType, jobInsId, connectmode) {
        if (connectmode == 'phoenix') {
            dbType = 'phoenix';
        }
        $("#readerConfigDiv").load(_contextPath + '/bigdata/datax/job/jobReaderConfig?jobInsId=' + jobInsId + '&dbType=' + dbType + '&connectMode=' + connectmode);
    }

    function changeSourceType(sourceType, dbType, jobInsId, connectmode) {
        $('#sourceDB').parent().addClass('hide');
        if (sourceType == 'datasource') {
            $('#sourceDB').parent().removeClass('hide');
            if (dbType != '') {
                changeSourceDB(dbType, jobInsId, connectmode);
            }
        } else if (sourceType == 'textfile') {
            changeSourceDB('textfile', jobInsId, connectmode);
        } else if (sourceType == 'ftp') {
            changeSourceDB('ftp', jobInsId, connectmode);
        }
        if ($('#targetTypeSelect li.active').attr('targetType') != 'message') {
            var metadataId = $('#targetDB li.active').attr('metadataId');
            changeMetadata(metadataId, $('#targetDB li.active').attr('dbType'));
        }
    }

    function changeTargetType(targetType, dbType, jobInsId) {
        $('#targetDB').parent().addClass('hide');
        if (targetType == 'metadata') {
            $('#targetDB').parent().removeClass('hide');
            if ($('.mapping-relation-body').find('.add-kafka-mapping').length > 0) {
                $('.mapping-relation-body').empty();
            }
            $("#writerConfigDiv").load(_contextPath + '/bigdata/datax/job/jobWriterConfig?jobInsId=' + jobInsId + '&dbType=' + dbType);
        } else if (targetType == 'message') {
            $("#writerConfigDiv").load(_contextPath + '/bigdata/datax/job/jobWriterConfig?jobInsId=' + jobInsId + '&dbType=kafka');

            if ($('.mapping-relation-body').find('.add-mapping').length > 0) {
                $('.mapping-relation-body').empty();
            }

            if ($('.mapping-relation-body').find('.add-kafka-mapping').length < 1) {
                var addBtn = '<div class="single-mapping add-mapping-wrap clearfix">\n' +
                    '                            <div class="mapping clearfix">\n' +
                    '                            <div style="float: right;" class="box-content add-kafka-mapping">\n' +
                    '                            <span>+添加</span>\n' +
                    '                            </div>\n' +
                    '                            </div>\n' +
                    '                            </div>';

                $('.mapping-relation-body').append(addBtn);
            }
        } else {
            $("#writerConfigDiv").empty();
        }
    }

    function changeMetadata(metadataId, type) {
        changeTargetType('metadata', type, '');
        loadMetadataColumn(metadataId);
    }
    
    function loadMetadataColumn(metadataId) {
        $.ajax({
            url: _contextPath + '/bigdata/metadata/' + metadataId + '/getField',
            type: 'POST',
            dataType: 'json',
            success: function (response) {
                if (response.success) {
                    $('.mapping-relation-body').empty();
                    var type = '字段名称';
                    var sourceType = $('#sourceTypeSelect li.active').attr('sourceType');
                    if (sourceType == 'textfile' || sourceType == 'ftp') {
                        type = '字段类型';
                    }
                    $.each(response.data, function (i, v) {
                        var fieldRelation = '<div class="single-mapping position-relative clearfix">\
                                        <div class="mapping-one">\
                                            <div class="box-content">\
                                                <span class="readerColumn">'+type+'</span>\
                                                <div class="pos-right">\
                                                    <i class="iconfont icon-editor-fill js-edit-column"></i>\
                                                </div>\
                                            </div>\
                                        </div>\
                                        <div class="mapping-two">\
                                            <div class="box-content add-mapping-rule">\
                                                <span order="' + (i + 1) + '" columnId="' + v.columnName + '">+添加规则</span>\
                                            </div>\
                                        </div>\
                                        <div class="mapping-one clearfix">\
                                            <div class="box-content pull-right">\
                                                <span class="writerColumn" columnId="' + v.columnName + '">' + v.columnName + '</span>\
                                                <div class="pos-right">\
                                                   <i class="iconfont icon-delete-bell js-remove-all"></i>\
                                                </div>\
                                            </div>\
                                        </div>\
                                    </div>';

                        $('.mapping-relation-body').append(fieldRelation);
                    });


                    var addBtn = '<div class="single-mapping add-mapping-wrap clearfix">\n' +
                        '                            <div class="mapping clearfix">\n' +
                        '                            <div class="box-content add-mapping">\n' +
                        '                            <span>+添加</span>\n' +
                        '                            </div>\n' +
                        '                            </div>\n' +
                        '                            </div>';

                    $('.mapping-relation-body').append(addBtn);
                } else {
                    showLayerTips4Confirm('error', response.message);
                }
            }
        });
    }

    function changeIsCustom() {
        if ($('#isCustomSelect').val() == 'config') {
            changeSourceDB('mysql', '', '');
        } else {
            changeSourceDB('mysql', '', 'onlySql');
        }
    }

    function changeColumnType() {
        if ($('#columnTypeSelect').val() == 'date') {
            $('#dateFormat').parent().parent().removeClass('hide');
            $('#dateFormat').val('yyyy-MM-dd');
        } else {
            $('#dateFormat').parent().parent().addClass('hide');
        }
    }
</script>