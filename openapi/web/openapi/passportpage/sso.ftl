<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>单点登录</title>
<#include "/openapi/homepage/head.ftl">
</head>

<body>
<div class="header">
<link rel="stylesheet" href="${resourceUrl}/components/bootstrap/dist/css/bootstrap.css" />
<link rel="stylesheet" href="${resourceUrl}/components/font-awesome/css/font-awesome.css" />
<link rel="stylesheet" href="${resourceUrl}/components/layer/skin/layer.css">
<link rel="stylesheet" href="${resourceUrl}/components/chosen/chosen.min.css">
<link rel="stylesheet" href="${resourceUrl}/components/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css">
<link rel="stylesheet" href="${resourceUrl}/components/highlight/styles/monokai-sublime-custom.css">
<link rel="stylesheet" href="${resourceUrl}/css/iconfont.css">
<link rel="stylesheet" href="${resourceUrl}/css/components.css">
<link rel="stylesheet" href="${resourceUrl}/css/page-desktop.css">
<link rel="stylesheet" href="${resourceUrl}/css/pages.css">
<script src="${resourceUrl}/assets/js/ace-extra.js"></script>

    <div class="main-content-inner">
        <div class="page-content">
            <div class="row">
                <div class="col-xs-12">
                    <!-- PAGE CONTENT BEGINS -->
                    <div class="box box-default">
                        <div class="box-body">
                            <ul class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active">
                                    <a href="#web" role="tab" data-toggle="tab">
                                        网站接入
                                    </a>
                                </li>
                                <li role="presentation">
                                    <a href="#app" role="tab" data-toggle="tab">
                                        移动应用
                                    </a>
                                </li>
                            </ul>
                            <div class="tab-content">
                                <div role="tabpanel" class="tab-pane active" id="web">
                                    <ul class="base-fixed-menu" style="right: 50px !important;">
                                        <li class="base-fixed-menu-tier1 active"><a href="#tabpanel1-item1"><i
                                                class="fa fa-circle"></i>总体描述</a></li>
                                        <li class="base-fixed-menu-tier1"><a href="#tabpanel1-item2"
                                                                             style="font-weight:bold"><i
                                                class="fa fa-circle"></i>术语</a></li>
                                        <li class="base-fixed-menu-tier1"><a href="#loginMenu"
                                                                             style="font-weight:bold"><i
                                                class="fa fa-circle"></i>登录</a></li>
                                        <li class="base-fixed-menu-tier2"><a href="#loginMenu"
                                                                             style="font-size: 14px"><i
                                                class="fa fa-circle"></i>应用场景</a></li>
                                        <li class="base-fixed-menu-tier2"><a href="#loginInterface"
                                                                             style="font-size: 14px"><i
                                                class="fa fa-circle"></i>接口样例</a></li>
                                        <li class="base-fixed-menu-tier1"><a href="#exitMenu"
                                                                             style="font-weight:bold"><i
                                                class="fa fa-circle"></i>退出</a></li>
                                        <li class="base-fixed-menu-tier2"><a href="#exitMenu"
                                                                             style="font-size: 14px"><i
                                                class="fa fa-circle"></i>应用场景</a></li>
                                        <li class="base-fixed-menu-tier2"><a href="#exitInterface"
                                                                             style="font-size: 14px"><i
                                                class="fa fa-circle"></i>接口样例</a></li>
                                        <li style="font-weight: bold">&emsp;附录</li>
                                        <li >&emsp;&emsp;
                                            <a href="${demoDownloadUrl! }" style="color: #2c9ae8">demo下载</a></li>
                                        <li >&emsp;&emsp;
                                            <a href="${docDownloadUrl! }" style="color: #2c9ae8">文档下载</a></li>
                                    </ul>
                                    <div class="row">
                                        <div class="col-xs-9">
                                            <div class="base-item" id="tabpanel1-item1">
                                                <h2>总体描述</h2>
                                                <p class="lead">Passport是万朋基于CAS（Central Authentication
                                                    Service）结构自主研发的统一认证平台，接入的子系统登录/退出都需要通过passport平台来完成。</p>
                                            </div>
                                            <div class="base-item" id="tabpanel1-item2">
                                                <h2>术语</h2>
                                                <table class="table">
                                                    <tbody>
                                                    <tr class="table-first">
                                                        <th class="table-title" width="200">术语</th>
                                                        <th>解释</th>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">URLEncode</th>
                                                        <td>
                                                            将URL中的字符串参数进行编码，中文和一些特殊字符会被替换，采用UTF-8进行编码。比如：username=万朋，URLEncode后为username=%E4%B8%87%E6%9C%8B
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">MD5</th>
                                                        <td>
                                                            消息摘要算法5，结果用16进制的字符串表示。比如：winupon经过MD5后为8d6654f977b88f4cb44c230b0fe16426
                                                            <b>注：MD5结果均为小写</b></td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">WSDL</th>
                                                        <td>webservice 描述语言</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <div class="base-item" id="loginMenu">
                                                <h2>登录</h2>
                                                <h3 style="font-weight: bold">&emsp;•应用场景</h3>
                                                <p><img src="${resourceUrl}/images/base/temp/1.png" alt=""/></p>

                                            </div>
                                            <div class="base-item" id="loginInterface">
                                                <h3 style="font-weight: bold">&emsp;&emsp;•接口样例</h3>
                                                <ul class="base-li-group">
                                                    <li class="base-li">• 用户登录门户平台，在个人桌面点击第三方应用图标或文字(超链接)(地址由第三方应用提供)
                                                    </li>
                                                    <li class="base-li">• 重定向到第三方应用提供的地址</li>
                                                    <li class="base-li">• 第三方应用判断当前会话是否是已登录状态，未登录时需要重定向至如下地址：</li>
                                                </ul>
                                                <br/>
                                                <table class="table " style="table-layout:fixed">
                                                    <col style="width: 101px"/>
                                                    <col style="width: 85px"/>
                                                    <col style="width: 290px%"/>
                                                    <col style="width: 373px%"/>

                                                    <tbody>
                                                    <tr class="table-first">
                                                        <th class="table-title" style="width: 69px!important;">接口协议</th>
                                                        <td colspan="3">http</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口地址</th>
                                                        <td colspan="3" id="passport">${passportUrl}</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口描述</th>
                                                        <td colspan="3">统一认证接口（接入应用服务端302重定向）</br> 接入应用->passport</td>
                                                    </tr>
                                                    <tr>
                                                        <th rowspan="5" style="vertical-align: middle;"
                                                            class="table-title">接口参数
                                                        </th>
                                                        <td>server</td>
                                                        <td class="td-2">
                                                        <#if serverList??&&(serverList?size>0)>
                                                            <select   onchange="changeServerOfLogin(this.value)" id="serverOfLogin" >
                                                                <option value="000000">应用样例</option>
                                                                <#list serverList as server>
                                                                    <option value="${server.id}">${server.name}</option>
                                                                </#list>
                                                            </select>
                                                        <#else >
                                                            <select  id="serverOfLogin" disabled="disabled">
                                                                <option value="000000" >应用样例</option>
                                                            </select>
                                                        </#if>
                                                        </td>
                                                        <td class="td-3">接入应用编号</td>
                                                    </tr>
                                                    <tr>
                                                        <td>url</td>
                                                        <td >
                                                            <input onchange="modifyUrlOfLogin(this.value)" type="text"  style="width: 300px" id="indexUrlOfLogin"
                                                                   value="http://www.demo.net/context/index.aspx">
                                                        </td>
                                                        <td>用户最终要访问的地址，接口二会原样返回</td>
                                                    </tr>
                                                    <tr>
                                                        <td>root</td>
                                                        <td id="root">0</td>
                                                        <td>接入应用是否根目录部署 1：是 0：否</td>
                                                    </tr>
                                                    <tr>
                                                        <td>context</td>
                                                        <td id="context">/context</td>
                                                        <td>非根目录部署时，接入应用的上下文路径request.getContextPath()</td>
                                                    </tr>
                                                    <tr>
                                                        <td>auth</td>
                                                        <td >密文:<p id="authOfLogin">5eb6c89b9b56a966787becb43d8b777e</p>明文:<p id="decodeAuthOfLogin" class="newline">X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y000000http://www.demo.net/context/index.aspx0</p></td>
                                                        <td>接口校验串（小写）auth=md5(appKey+appId+url+root)</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口样例</th>
                                                        <td colspan="3" class="newline" id="urlOfLogin" >
                                                            <p style="color:red;">http://passport.demo.net/login.htm?server=12345&url=http%3a%2f%2fshiping.demo.net%2fcontext%2findex.aspx&root=0&auth=5eb6c89b9b56a966787becb43d8b777e</p>注：如果url有中文、特殊字符，需要urlEcode
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                <ul class="base-li-group">
                                                    <li class="base-li">• Passport判断用户是否登录成功，登录成功时重定向回第三方应用</li>
                                                </ul>
                                                <br/>
                                                <table class="table " style="table-layout:fixed">
                                                    <col style="width: 101px"/>
                                                    <col style="width: 85px"/>
                                                    <col style="width: 290px%"/>
                                                    <col style="width: 373px%"/>
                                                    <tbody>
                                                    <tr class="table-first">
                                                        <th class="table-title" style="width: 69px!important;">接口协议</th>
                                                        <td colspan="3">http</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口地址</th>
                                                        <td id="verifyUrl" colspan="3">
                                                            http://www.demo.net/context/index.aspx（登记信息中填写的地址）
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口描述</th>
                                                        <td colspan="3">统一认证接口（passport服务端302重定向)<br/> passport->接入应用
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title" rowspan="3"
                                                            style="vertical-align: middle;">接口参数
                                                        </th>
                                                        <td>ticket</td>
                                                        <td id="ticketOfVerify">8A5F449F5A8A3716015AAC13B7EE4041</td>
                                                        <td>用户登录接入应用的临时票据，一般保存在接入应用的用户会话中，退出通知接口会用到</td>
                                                    </tr>
                                                    <tr>
                                                        <td>url</td>
                                                        <td id="indexUrlOfVerify">http://www.demo.net/context/index.aspx
                                                        </td>
                                                        <td>用户最终要访问的地址，接口一中url参数</td>
                                                    </tr>
                                                    <tr>
                                                        <td>auth</td>
                                                        <td >密文:<p>72abd61c34cc15be87869e33b5df65e6</p>明文:<p class="newline">8A5F449F5A8A3716015AAC13B7EE4041http://www.demo.net/context/index.aspx000000X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y</p>
                                                        </td>
                                                        <td>接口校验串（小写）auth=md5(ticket+url+appId+appKey)</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口样例</th>
                                                        <td id="urlOfVerify" colspan="3" class="newline" >
                                                            <p style="color: red">http://shipin.demo.net/context/login.do?ticket=5eb6c89b9b56a966787becb43d8b777e&url=http%3a%2f%2fwww.demo.net%2fcontext%2findex.aspx&auth=5eb6c89b9b56a966787becb43d8b777e</p>注：如果url有中文、特殊字符，需要urlEcode
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                <ul class="base-li-group">
                                                    <li class="base-li">• 第三方应用得到票据（ticket）后，再调用passport如下接口获取登录用户基本信息
                                                    </li>
                                                </ul>
                                                <br/>
                                                <table class="table " style="table-layout:fixed">
                                                    <col style="width: 101px"/>
                                                    <col style="width: 85px"/>
                                                    <col style="width: 290px%"/>
                                                    <col style="width: 373px%"/>
                                                    <tbody>
                                                    <tr class="table-first">
                                                        <th class="table-title">接口协议</th>
                                                        <td colspan="3">http</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口地址</th>
                                                        <td colspan="3" id="checkTicket">${checkTicketUrl} </td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口描述</th>
                                                        <td colspan="3">接入应用从passport拉取用户信息 接入应用->passport</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title" rowspan="3"
                                                            style="vertical-align: middle;">接口参数
                                                        </th>
                                                        <td>serverId</td>
                                                        <td >
                                                            <#--<select   onchange="changeServerOfCheckTicket(this.value)" id="serverIdOfCheckTicket">-->
                                                                <#--<option value="000000">测试样例</option>-->
                                                            <#--<#list serverList as server>-->
                                                                <#--<option value="${server.id}">${server.name}</option>-->
                                                            <#--</#list>-->
                                                            <#--</select>-->
                                                        <#if serverList??&&(serverList?size>0)>
                                                            <select   onchange="changeServerOfLogin(this.value)" id="serverOfLogin" >
                                                                <option value="000000">应用样例</option>
                                                                <#list serverList as server>
                                                                    <option value="${server.id}">${server.name}</option>
                                                                </#list>
                                                            </select>
                                                        <#else >
                                                            <select  id="serverOfLogin" disabled="disabled">
                                                                <option value="000000" >应用样例</option>
                                                            </select>
                                                        </#if>

                                                        </td>
                                                        <td>接入应用</td>
                                                    </tr>
                                                    <tr>
                                                        <td>ticket</td>
                                                        <td><input  type="text"  style="width:300px;"
                                                                    value="402896315ACFC878015ADAE5D35A03C0"
                                                                    onchange="changeTicketOfCheckTicket(this.value)"
                                                                    id="ticketOfCheckTicket"></td>
                                                        <td>接口二得到的用户临时票据</td>
                                                    </tr>
                                                    <td>auth</td>
                                                    <td >
                                                        密文:<p id="authOfCheckTicket">833cf5d834fc6263a6eabadaf03767b1</p>明文:<p id="decodeAuthOfCheckTicket" class="newline">X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y000000402896315ACFC878015ADAE5D35A03C0</p>
                                                    </td>
                                                    <td>接口校验串（小写）auth=md5(appKey+appId+ticket)</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口样例</th>
                                                        <td colspan="3" class="newline" id="urlOfCheckTicket" style="color: red">
                                                            http://passport.wp.tst/api/checkTicket?ticket=8A5F449F5A8A3716015AAC13B7EE4041&serverId=000000&auth=c94712a5189035a622b38c814d685199
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                            <p class="base-warn">注意：第三方应用保存ticket与用户会话对应关系，接收退出请求时根据ticket销毁对应的会话信息</p>
                                            <div class="base-item" id="tabpanel2-item3">
                                                <h4>返回结果</h4>
                                                <h3>JSON</h3>
                                                <pre><code>
                                        {
                                        "account":{
                                        "fixedType":2,
                                        "id":"402880CE5190258C0151903F88F7000D",
                                        "properties":{

                                        },
                                        "realName":"admin",
                                        "sex":1,
                                        "username":"640022admin"
                                        },
                                        "message":"success",
                                        "success":1
                                        }
                                        </code></pre>
                                            </div>
                                            <div class="base-item" id="exitMenu">
                                                <h2>退出</h2>
                                                <h3 style="font-weight: bold">&emsp;•应用场景</h3>
                                                <p><img src="${resourceUrl}/images/base/temp/logout-1.png" alt=""/></p>
                                            </div>
                                            <div class="base-item" id="exitInterface">
                                                <div class="base-item" >
                                                    <h3 style="font-weight: bold"> •接口样例</h3>
                                                </div>
                                                </ul>
                                                <ul>
                                                    <li class="base-li">&emsp;&emsp;被动退出：接入应用被通知注销登录用户在应用内的会话信息</li>
                                                </ul>
                                                <table class="table " style="table-layout:fixed">
                                                    <col style="width: 101px"/>
                                                    <col style="width: 85px"/>
                                                    <col style="width: 290px%"/>
                                                    <col style="width: 373px%"/>
                                                    <tbody>
                                                    <tr class="table-first">
                                                        <th class="table-title">接口协议</th>
                                                        <td colspan="3">http</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口地址</th>
                                                        <td colspan="3" id="urlOfPassiveExit">http://www.demo.net/context/logou.aspx</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口描述</th>
                                                        <td colspan="3">被动通知退出接口（服务端通知），接入应用收到通知后注销对应的用户会话信息<br/>passport->接入应用
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title" rowspan="2"
                                                            style="vertical-align: middle;">接口参数
                                                        </th>
                                                        <td>ticket</td>
                                                        <td id="ticketOfPassiveExit">
                                                            8A5F449F5A8A3716015AAC13B7EE4041
                                                        </td>
                                                        <td>登录接口二得到并保存的用户临时票据</td>
                                                    </tr>
                                                    <td>auth</td>
                                                    <td >833cf5d834fc6263a6eabadaf03767b1
                                                    </td>
                                                    <td>接口校验串（小写）auth=md5(appKey+appId+ticket)</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口样例</th>
                                                        <td colspan="3" class="newline" id="eg-UrlOfPassive" style="color: red">
                                                            http://www..demo.net/context/logout.aspx?ticket=8A5F449F5A8A3716015AAC13B7EE4041&auth=5eb6c89b9b56a966787becb43d8b777e
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                <ul>
                                                    <li class="base-li">&emsp;&emsp;主动退出：保留退出按钮的应用需要实现，除销毁用户在本应用的会话之外，还需要通知passport，从而通知其他接入应用注销用户会话</li>
                                                </ul>
                                                <table class="table " style="table-layout:fixed">
                                                    <col style="width: 101px"/>
                                                    <col style="width: 85px"/>
                                                    <col style="width: 290px%"/>
                                                    <col style="width: 373px%"/>
                                                    <tbody>
                                                    <tr class="table-first">
                                                        <th class="table-title">接口协议</th>
                                                        <td colspan="3">http</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口地址</th>
                                                        <td colspan="3" id="urlOfActiveExit">http://passport.demo.net/logout.htm</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口描述</th>
                                                        <td colspan="3">主动退出接口（服务端通知），接入应用点击退出，通知passport<br/>接入应用->passport
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title" rowspan="3"
                                                            style="vertical-align: middle;">接口参数
                                                        </th>
                                                        <td>server</td>
                                                        <td>
                                                            <#--<select   onchange="changeServerOfActiveExit(this.value)" id="serverOfActiveExit">-->
                                                                <#--<option value="000000">测试用例</option>-->
                                                            <#--<#list serverList as server>-->
                                                                <#--<option value="${server.id}">${server.name}</option>-->
                                                            <#--</#list>-->
                                                            <#--</select>-->
                                                        <#if serverList??&&(serverList?size>0)>
                                                            <select   onchange="changeServerOfLogin(this.value)" id="serverOfLogin" >
                                                                <option value="000000">应用样例</option>
                                                                <#list serverList as server>
                                                                    <option value="${server.id}">${server.name}</option>
                                                                </#list>
                                                            </select>
                                                        <#else >
                                                            <select  id="serverOfLogin" disabled="disabled">
                                                                <option value="000000" >应用样例</option>
                                                            </select>
                                                        </#if>

                                                        </td>
                                                        <td>应用编号</td>

                                                    </tr>
                                                    <tr>
                                                        <td>ticket</td>
                                                        <td >
                                                            <input onchange="modifyTicketOfActiveExit(this.value)" type="text"  style="width: 300px" id="ticketOfActiveExit"
                                                                   value="8A5F449F5A8A3716015AAC13B7EE4041">
                                                        </td>
                                                        <td>登录接口二得到并保存的用户临时票据</td>
                                                    </tr>
                                                    <tr>
                                                        <td>auth</td>
                                                        <td >
                                                            密文:<p id="authOfActiveExit">833cf5d834fc6263a6eabadaf03767b1</p>明文:<p id="decodeAuthOfActiveExit" class="newline">X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y0000008A5F449F5A8A3716015AAC13B7EE4041</p>
                                                        </td>
                                                        <td>接口校验串（小写）auth=md5(appKey+appId+ticket)</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口样例</th>
                                                        <td colspan="3" class="newline" id="eg-urlOfActiveExit" style="color: red">
                                                            http://passport.demo.net/context/logout.htm?ticket=8A5F449F5A8A3716015AAC13B7EE4041&auth=5eb6c89b9b56a966787becb43d8b777e&server=123456
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>

                                            </div>
                                            <div class="base-item" id="tabpanel1-item6">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane" id="app">
                                    <ul class="base-fixed-menu" style="right: 50px !important;">
                                        <li class="base-fixed-menu-tier1 active"><a href="#appLoginMenu"><i
                                                class="fa fa-circle"></i>总体描述</a></li>
                                        <li class="base-fixed-menu-tier1"><a href="#appScene"
                                                                             style="font-weight:bold"><i
                                                class="fa fa-circle"></i>应用场景</a></li>
                                        <li class="base-fixed-menu-tier1"><a href="#appLoginInterface"
                                                                             style="font-weight:bold"><i
                                                class="fa fa-circle"></i>接入流程及接口样例</a></li>
                                    </ul>
                                    <div class="row">
                                        <div class="col-xs-9">
                                            <div class="base-item" id="appLoginMenu">
                                                <h2>总体描述</h2>
                                                <p class="lead">目前仅提供登录接口</p>
                                            </div>
                                            <div class="base-item" id="appScene">
                                                <h2>应用场景</h2>
                                                <p><img src="${resourceUrl}/images/base/temp/passport-5.png" alt=""/>
                                                </p>
                                            </div>

                                            <div class="base-item">
                                                <h2 id="appLoginInterface">接入流程及接口样例</h2>
                                                <h3 id="tabpanel2-item3-2">&emsp;实现验证接口</h3>
                                                <table class="table " style="table-layout:fixed">
                                                    <col style="width: 101px"/>
                                                    <col style="width: 85px"/>
                                                    <col style="width: 290px%"/>
                                                    <col style="width: 373px%"/>
                                                    <tbody>
                                                    <tr class="table-first">
                                                        <th class="table-title">接口协议</th>
                                                        <td colspan="3">http</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口地址</th>
                                                        <td colspan="3" id="clientLoginUrl">${clientLoginUrl}
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口描述</th>
                                                        <td colspan="3">获取能过户信息接口</br> 接入应用->passport</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title" rowspan="3"
                                                            style="vertical-align: middle;">接口参数
                                                        </th>
                                                        <td>serverId</td>
                                                        <td>
                                                            <#--<select   onchange="changeServerOfClientLogin(this.value)" id="serverOfClientLoign">-->
                                                                <#--<option value="000000">测试用例</option>-->
                                                            <#--<#list serverList as server>-->
                                                                <#--<option value="${server.id}">${server.name}</option>-->
                                                            <#--</#list>-->
                                                            <#--</select>-->
                                                        <#if serverList??&&(serverList?size>0)>
                                                            <select   onchange="changeServerOfLogin(this.value)" id="serverOfLogin" >
                                                                <option value="000000">应用样例</option>
                                                                <#list serverList as server>
                                                                    <option value="${server.id}">${server.name}</option>
                                                                </#list>
                                                            </select>
                                                        <#else >
                                                            <select  id="serverOfLogin" disabled="disabled">
                                                                <option value="000000" >应用样例</option>
                                                            </select>
                                                        </#if>
                                                        </td>
                                                        <td>接入应用</td>
                                                    </tr>
                                                    <tr>
                                                        <td>username</td>
                                                        <td><input  id="username" type="text" value="123" onchange="modifyUsernameOfClientLoign(this.value)"/></td>
                                                        <td>用户名登录</td>
                                                    </tr>
                                                    <td>password</td>
                                                    <td>明文:<input  id="decodePassword" type="text" value="123" style="width:157px;" onchange="moidfyPasswordOfClientLogin(this.value)"/><br>
                                                        密文:<p id="encodePassword">732a7b2e95aeeb636de6b6fd438056ea</p>
                                                    </td>
                                                    <td>密码串（小写）password=md5(appKey+appId+username+密码明文)</td>
                                                    </tr>
                                                    <tr>
                                                        <th class="table-title">接口样例</th>
                                                        <td colspan="3" class="newline" id="urlOfClientLogin" style="color: red">
                                                        ${clientLoginUrl}?username=123&password=732a7b2e95aeeb636de6b6fd438056ea&serverId=000000
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                                <h4 id="tabpanel2-item3-2">&emsp;返回结果：JSON</h4>
                                                <pre><code>
                                           {
                                                "account":{
                                                    "fixedType":2,
                                                    "id":"402882873B2A940B013B502A058C2C6s",
                                                    "properties":{},
                                                    "realName":"学生",
                                                    "sex":0,
                                                    "username":"t1640500229wzh"
                                                },
                                                "message":"success",
                                                "success":1
                                           }
                                            </code></pre>
                                            </div>
                                            <div class="base-item">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- PAGE CONTENT ENDS -->
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.page-content -->
    </div>

    <!-- basic scripts -->
    <script>
        $(function () {
            $('.base-fixed-menu li').click(function () {
                $(this).addClass('active').siblings('li').removeClass('active');
            });
        });

        //切换应用

        function changeServerOfLogin(server){
            $.ajax({
                url:'${request.contextPath}/developer/changeServerOfLogin',
                data: {
                    'url': $("#indexUrlOfLogin").val(),
                    'passport': $("#passport").html(),
                    'serverId': server
                },
                async: false,
                type: 'post',
                success: function (result) {
                    var data = JSON.parse(result);
                    //切换到默认应用时需要处理
                    $("#root").html(data.root);
                    $("#context").html(data.context);
                    $("#authOfLogin").html(data.auth);
                    $("#urlOfLogin").html(data.url);
                }
            });
        }
        //修改url
        function modifyUrlOfLogin(url) {
            $.ajax({
                url:'${request.contextPath}/developer/modifyUrlOfLogin',
                data: {
                    'url': url,
                    'passport': $("#passport").html(),
                    'serverId': $("#serverOfLogin option:selected").val(),
                    'root':$("#root").html()
                },
                async: false,
                type: 'post',
                success: function (result) {
                    var data = JSON.parse(result);
                    $("#authOfLogin").html(data.auth);
                    $("#urlOfLogin").html(data.url);
                }
            });
        }
        //拉取用户信息接口用例
        function changeTicketOfCheckTicket(ticket){
            $.ajax({
                url:'${request.contextPath}/developer/changeTicketOfCheckTicket',
                data:{
                    'ticket':ticket,
                    'serverId':$("#serverIdOfCheckTicket  option:selected").val(),
                    'checkTicket':$("#checkTicket").html()
                },
                type:'post',
                async:false,
                success:function (result){
                    var data = JSON.parse(result);
                    $("#authOfCheckTicket").html(data.auth);
                    $("#urlOfCheckTicket").html(data.url);
                }
            });
        }
        function changeServerOfCheckTicket(serverId){
            $.ajax({
                url:'${request.contextPath}/developer/changeServerOfCheckTicket',
                data:{
                    'ticket':$("#ticketOfCheckTicket").val(),
                    'serverId':serverId,
                    'checkTicket':$("#checkTicket").html()
                },
                type:'post',
                async:false,
                success:function (result){
                    var data = JSON.parse(result);
                    $("#authOfCheckTicket").html(data.auth);
                    $("#urlOfCheckTicket").html(data.url);
                }
            });
        }
        function changeServerOfPassiveExit(server){
            $.ajax({
                url:'${request.contextPath}/developer/changeServerOfPassiveExit',
                type:'post',
                async:false,
                data:{
                    'serverId':server,
                    'ticket':$("#ticketOfPassiveExit").val(),
                    'urlOfPassiveExit':$("#urlOfPassiveExit").html()
                },
                success:function(result){
                    var data = JSON.parse(result);
                    $("#authOfPassiveExit").html(data.auth);
                    $("#eg-UrlOfPassive").html(data.url);
                }
            });
        }
        function modifyTicketOfPassiveExit(ticket){
            $.ajax({
                url:'${request.contextPath}/developer/modifyTicketOfPassiveExit',
                type:'post',
                async:false,
                data:{
                    'serverId':$("#serverOfPassiveExit option:selected").val(),
                    'ticket':ticket,
                    'urlOfPassiveExit':$("#urlOfPassiveExit").html()
                },
                success:function(result){
                    var data = JSON.parse(result);
                    $("#authOfPassiveExit").html(data.auth);
                    $("#eg-UrlOfPassive").html(data.url);
                }
            });
        }
        function changeServerOfActiveExit(server){
            $.ajax({
                url:'${request.contextPath}/developer/changeServerOfActiveExit',
                type:'post',
                async:false,
                data:{
                    'serverId':server,
                    'ticket':$("#ticketOfActiveExit").val(),
                    'urlOfActiveExit':$("#urlOfActiveExit").html()
                },
                success:function(result){
                    var data = JSON.parse(result);
                    $("#authOfActiveExit").html(data.auth);
                    $("#eg-urlOfActiveExit").html(data.url);
//                $("#serverIdOfActiveExit").html(data.serverId);
                }
            });
        }
        function modifyTicketOfActiveExit(ticket){
            $.ajax({
                url:'${request.contextPath}/developer/modifyTicketOfActiveExit',
                type:'post',
                async:false,
                data:{
                    'serverId':$("#serverOfActiveExit option:selected").val(),
                    'ticket':ticket,
                    'urlOfActiveExit':$("#urlOfActiveExit").html()
                },
                success:function(result){
                    var data = JSON.parse(result);
                    $("#authOfActiveExit").html(data.auth);
                    $("#eg-urlOfActiveExit").html(data.url);
                }
            });
        }
        function modifyUsernameOfClientLoign(username){
            $.ajax({
                url:'${request.contextPath}/developer/modifyOfClientLoign',
                type:'post',
                async:false,
                data:{
                    'username':username,
                    'serverId':$("#serverOfClientLoign option:selected").val(),
                    'password':$("decodePassword").val(),
                    'clientLoginUrl':$("#clientLoginUrl").html()
                },
                success:function(result){
                    var data = JSON.parse(result);
                    $("#encodePassword").html(data.encodePassword);
                    $("#urlOfClientLogin").html(data.url);
                }
            });
        }
        function changeServerOfClientLogin(server){
            $.ajax({
                url:'${request.contextPath}/developer/modifyOfClientLoign',
                type:'post',
                async:false,
                data:{
                    'username':$("#username").val(),
                    'serverId':server,
                    'password':$("decodePassword").val(),
                    'clientLoginUrl':$("#clientLoginUrl").html()
                },
                success:function(result){
                    var data =JSON.parse(result);
                    $("#encodePassword").html(data.encodePassword);
                    $("#urlOfClientLogin").html(data.url);
                }
            });
        }
        function moidfyPasswordOfClientLogin(password){
            $.ajax({
                url:'${request.contextPath}/developer/modifyOfClientLoign',
                type:'post',
                async:false,
                data:{
                    'username':$("#username").val(),
                    'serverId':$("#serverOfClientLoign option:selected").val(),
                    'password':password,
                    'clientLoginUrl':$("#clientLoginUrl").html()
                },
                success:function(result){
                    var data = JSON.parse(result);
                    $("#encodePassword").html(data.encodePassword);
                    $("#urlOfClientLogin").html(data.url);
                }
            });
        }
    </script>

    <script type="text/javascript">
        if(browser.ie){
            document.write("<script src='${resourceUrl}/components/jquery.1x/dist/jquery.js'>"+"<"+"/script>");
        }else{
            document.write("<script src='${resourceUrl}/components/jquery/dist/jquery.js'>"+"<"+"/script>");
        }
        if('ontouchstart' in document.documentElement) document.write("<script src='${resourceUrl}/components/_mod/jquery.mobile.custom/jquery.mobile.custom.js'>"+"<"+"/script>");
    </script>
<script src="${resourceUrl}/components/bootstrap/dist/js/bootstrap.js"></script>
<script src="${resourceUrl}/components/layer/layer.js"></script>
<script src="${resourceUrl}/components/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${resourceUrl}/components/chosen/chosen.jquery.min.js"></script>
<script src="${resourceUrl}/components/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${resourceUrl}/components/bootstrap-datepicker/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
<script src="${resourceUrl}/assets/js/src/ace.ajax-content.js"></script>
<script src="${resourceUrl}/js/jquery.form.js"></script>
<script src="${resourceUrl}/js/tool.js"></script>
<script src="${resourceUrl}/openapi/js/openUtil.js"></script>
<script>
$(function(){
$('.nav-list').find('li').on('click',changeActive);
// $('#user-bar').on('click',showDropbox);
loadPage('/appManage/appList');
});

function changeActive(){
$(this).addClass('active').siblings('[class="active"]').removeClass('active');
}

function loadPage(url){
loadDiv('.main-content',url);              $('.dropbox').hide();
}

/*  function showDropbox(){
$(this).next().toggle();
} */
</script>
    <style>
        .newline {
            word-wrap: break-word
        }

        .p-1 {
            font-size: 15px
        }
    </style>


</body>
</html>
