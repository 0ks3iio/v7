
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
                                <div class="row">
                                    <div class="col-xs-9">
                                        <div class="base-item">
                                            <h2 class="base-menu-from" data-tier="1">总体描述</h2>
                                            <p class="lead">Passport是基于CAS（Central Authentication
                                                Service）结构自主研发的统一认证平台，接入的子系统登录/退出都需要通过passport平台来完成。</p>
                                        </div>
                                        <div class="base-item" >
                                            <h2 class="base-menu-from" data-tier="1">术语</h2>
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
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="base-item" id="loginMenu" style="padding-top: 50px;">
                                            <h2 class="base-menu-from" data-tier="1">登录</h2>
                                            <h3 class="base-menu-from" data-tier="2">应用场景</h3>
                                            <p><img src="${resourceUrl}/images/base/temp/1.png" alt=""/></p>

                                        </div>
                                        <div class="base-item" id="loginInterface" style="padding-top: 50px;">
                                            <h3 class="base-menu-from" data-tier="2">接口样例</h3>
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
                                                    <td colspan="3" id="passport">${verifyUrl}</td>
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
                                                               value="http://www.demo.net/index.aspx">
                                                    </td>
                                                    <td>用户最终要访问的地址(域名+首页URL)，接口二会原样返回</td>
                                                </tr>
                                                <tr>
                                                    <td>root</td>
                                                    <td id="root">1</td>
                                                    <td>接入应用是否根目录部署 1：是 0：否</td>
                                                </tr>
                                                <tr>
                                                    <td>context</td>
                                                    <td id="context">/</td>
                                                    <td>非根目录部署时，接入应用的上下文路径request.getContextPath()</td>
                                                </tr>
                                                <tr>
                                                    <td>auth</td>
                                                    <td >密文:<p
                                                            id="authOfLogin">708b13374b838d17222e51bbf6c86446</p>明文:<p id="decodeAuthOfLogin" class="newline">X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y000000http://www.demo.net/index.aspx1</p></td>
                                                    <td>接口校验串（小写）auth=md5(appKey+appId+url+root)</td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title">接口样例</th>
                                                    <td colspan="3" class="newline" id="urlOfLogin" >
                                                        <p style="color:red;
">${verifyUrl}?server=12345&url=http://shiping.demo.net/index.aspx&root=0&auth=708b13374b838d17222e51bbf6c86446</p>注：如果url有中文、特殊字符，需要urlEcode
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
                                                        http://www.demo.net/verify.aspx（域名+登录URL）
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title">接口描述</th>
                                                    <td colspan="3">passport重定向接入应用,回传ticket
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
                                                    <td id="indexUrlOfVerify">http://shipin.demo.net/index.aspx
                                                    </td>
                                                    <td>同接口一参数url</td>
                                                </tr>
                                                <tr>
                                                    <td>auth</td>
                                                    <td >密文:<p
                                                            id="verifyEncodeAuth">3f185650731b63dc68244aab14ce05f2</p>明文:<p class="newline" id="verifyDecodeAuth">8A5F449F5A8A3716015AAC13B7EE4041http://shipin.demo.net/index.aspx000000X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y</p>
                                                    </td>
                                                    <td>接口校验串（小写）auth=md5(ticket+url+appId+appKey)</td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title">接口样例</th>
                                                    <td id="urlOfVerify" colspan="3" class="newline" >
                                                        <p style="color: red">http://shipin.demo.net/verify.aspx?ticket=8A5F449F5A8A3716015AAC13B7EE4041&url=http://shipin.demo.net/index.aspx&auth=3f185650731b63dc68244aab14ce05f2 </p>  注：如果url有中文、特殊字符，需要urlEcode
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
                                                        <select   onchange="changeServerOfCheckTicket(this.value)" id="serverIdOfCheckTicket">
                                                            <option value="000000">测试样例</option>
                                                        <#if serverList??&&(serverList?size>0)>
                                                        <#list serverList as server>
                                                            <option value="${server.id}">${server.name}</option>
                                                        </#list>
                                                        </#if >
                                                        </select>

                                                    </td>
                                                    <td>接入应用</td>
                                                </tr>
                                                <tr>
                                                    <td>ticket</td>
                                                    <td><input  type="text"  style="width:300px;"
                                                                value="8A5F449F5A8A3716015AAC13B7EE4041"
                                                                onchange="changeTicketOfCheckTicket(this.value)"
                                                                id="ticketOfCheckTicket"></td>
                                                    <td>接口二得到的用户临时票据</td>
                                                </tr>
                                                <td>auth</td>
                                                <td >
                                                    密文:<p id="authOfCheckTicket">c94712a5189035a622b38c814d685199</p>明文:<p id="decodeAuthOfCheckTicket" class="newline">X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y0000008A5F449F5A8A3716015AAC13B7EE4041</p>
                                                </td>
                                                <td>接口校验串（小写）auth=md5(appKey+appId+ticket)</td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title">接口样例</th>
                                                    <td colspan="3" class="newline" id="urlOfCheckTicket" style="color: red">
                                                    ${checkTicketUrl}?ticket=8A5F449F5A8A3716015AAC13B7EE4041&serverId=000000&auth=c94712a5189035a622b38c814d685199
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
                                        <div class="base-item" id="exitMenu" style="padding-top: 50px;">
                                            <h2 class="base-menu-from" data-tier="1">退出</h2>
                                            <h3 class="base-menu-from" data-tier="2">应用场景</h3>
                                            <p><img src="${resourceUrl}/images/base/temp/logout-1.png" alt=""/></p>
                                        </div>
                                        <div class="base-item" id="exitInterface" style="padding-top: 50px;">
                                            <div class="base-item" >
                                                <h3 class="base-menu-from" data-tier="2">接口样例</h3>
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
                                                    <td colspan="3" id="urlOfPassiveExit">http://demo.net/logout
                                                        .aspx（域名+退出URL）</td>
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
                                                <td >密文:<p>c94712a5189035a622b38c814d685199</p>明文:<p class="newline">X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y0000008A5F449F5A8A3716015AAC13B7EE4041</p>
                                                </td>
                                                <td>接口校验串（小写）auth=md5(appKey+appId+ticket)</td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title">接口样例</th>
                                                    <td colspan="3" class="newline" id="eg-UrlOfPassive" style="color: red">
                                                        http://demo.net/logout.aspx?ticket=8A5F449F5A8A3716015AAC13B7EE4041&auth=c94712a5189035a622b38c814d685199
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
                                                    <td colspan="3" id="urlOfActiveExit">${activeLogoutUrl}</td>
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
                                                        <select   onchange="changeServerOfActiveExit(this.value)" id="serverOfActiveExit">
                                                            <option value="000000">测试用例</option>
                                                        <#if serverList??&&(serverList?size>0)>
                                                        <#list serverList as server>
                                                            <option value="${server.id}">${server.name}</option>
                                                        </#list>
                                                        </#if>
                                                        </select>
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
                                                        密文:<p id="authOfActiveExit">c94712a5189035a622b38c814d685199</p>明文:<p id="decodeAuthOfActiveExit" class="newline">X1J9BHLLBCA4QQL5F1UP6SACS1E2K32Y0000008A5F449F5A8A3716015AAC13B7EE4041</p>
                                                    </td>
                                                    <td>接口校验串（小写）auth=md5(appKey+appId+ticket)</td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title">接口样例</th>
                                                    <td colspan="3" class="newline" id="eg-urlOfActiveExit" style="color: red">
                                                    ${activeLogoutUrl}?ticket=8A5F449F5A8A3716015AAC13B7EE4041&auth=c94712a5189035a622b38c814d685199&server=000000
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
                                <div class="row">
                                    <div class="col-xs-9">
                                        <div class="base-item" id="appLoginMenu" style="padding-top: 50px;">
                                            <h2 class="base-menu-from" data-tier="1">总体描述</h2>
                                            <p class="lead">目前仅提供登录接口</p>
                                        </div>
                                        <div class="base-item" id="appScene" style="padding-top: 50px;">
                                            <h2 class="base-menu-from" data-tier="1">登录</h2>
                                            <h3 class="base-menu-from" data-tier="2">应用场景</h3>
                                            <p><img src="${resourceUrl}/images/base/temp/passport-5.png" alt=""/>
                                            </p>
                                        </div>

                                        <div class="base-item" id="appLoginInterface" style="padding-top: 50px;">
                                            <h3 class="base-menu-from" data-tier="2">接口样例</h3>
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
                                                    <td colspan="3" id="clientLoginUrl">${clientLoginUrl}</td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title">接口描述</th>
                                                    <td colspan="3">获取用户信息接口</br> 接入应用->passport</td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title" rowspan="3"
                                                        style="vertical-align: middle;">接口参数
                                                    </th>
                                                    <td>serverId</td>
                                                    <td>
                                                        <select   onchange="changeServerOfClientLogin(this.value)" id="serverOfClientLoign">
                                                            <option value="000000">测试用例</option>
                                                        <#if serverList??&&(serverList?size>0)>
                                                        <#list serverList as server>
                                                            <option value="${server.id}">${server.name}</option>
                                                        </#list>
                                                        </#if>
                                                        </select>
                                                    </td>
                                                    <td>接入应用</td>
                                                </tr>
                                                <tr>
                                                    <td>username</td>
                                                    <td><input  id="username" type="text" value="wanpeng"
                                                                onchange="modifyUsernameOfClientLoign(this.value)"/></td>
                                                    <td>用户名登录</td>
                                                </tr>
                                                <td>password</td>
                                                <td>明文:<input  id="decodePassword" type="text" value="wanpeng123"
                                                               style="width:157px;"
                                                               onchange="moidfyPasswordOfClientLogin(this.value)"/><br>
                                                    密文:<p id="encodePassword">1f469e88e2bd8265f457a0285da9c85c</p>
                                                </td>
                                                <td>密码串（小写）encodePassword=md5(appKey+appId+username+password)</td>
                                                </tr>
                                                <tr>
                                                    <th class="table-title">接口样例</th>
                                                    <td colspan="3" class="newline" id="urlOfClientLogin" style="color: red">
                                                    ${clientLoginUrl}?username=123&password=1f469e88e2bd8265f457a0285da9c85c&serverId=000000
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
                        <#--</div>-->
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
        $('.page-content .tab-pane').each(function(){
            var paneIndex=$(this).index()+1;
            var $menu='';
            var $list='';
            var itemIndex=0;
            $(this).find('.base-menu-from').each(function(){
                itemIndex++;
                var itemId='tabpanel'+paneIndex+'-item'+itemIndex;
                $(this).attr('id',itemId);
                var off_top=parseInt($(this).offset().top);
                $list=$list+'<li class="base-fixed-menu-tier'+$(this).attr('data-tier')+'" data-scroll="'+off_top+'"><a href="#'+itemId+'"><i class="fa fa-circle"></i>'+$(this).text()+'</a></li>';
            });
            $menu='<ul class="base-fixed-menu">'+$list+'</ul>';
            $(this).append($menu);
            $(this).find('.base-fixed-menu li:eq(0)').addClass('active');
        });
        //切换的时候重新算高度，隐藏层offset().top不准确
        $('.nav-tabs li').click(function(){
            setTimeout(function(){
                var $pane=$('.page-content .tab-pane:eq('+$(this).index()+')');
                var itemIndex=0;
                $pane.find('.base-menu-from').each(function(){
                    var off_top=parseInt($(this).offset().top);
                    $pane.find('.base-fixed-menu li:eq('+itemIndex+')').attr('data-scroll',off_top);
                    itemIndex++;
                });
            },200);
        });
        //点击左侧定位
        $('.page-content .tab-pane').on('click','.base-fixed-menu li',function(e){
            e.preventDefault();
            var sTop=$(this).attr('data-scroll')-136;
            $(this).addClass('active').siblings('li').removeClass('active');
            $(window).scrollTop(sTop);
            //alert(sTop);
            //alert($(window).scrollTop());
            //$('html,body').animate({scrollTop:$(this).attr('data-scroll')},500);
        });
        //滚动右侧导航切换
        scrollFixed();
        $(window).scroll(function(){
            scrollFixed();
        });
        function scrollFixed(){
            var $menu_li=$('.page-content .tab-pane:visible .base-fixed-menu li');
            var sTop=$(this).scrollTop()+136;
            var menu_len=$('.page-content .tab-pane:visible').find('.base-fixed-menu li').length;
            var i=0;
            $menu_li.each(function(){
                var data_scroll=$(this).attr('data-scroll');
                if (sTop >= data_scroll) {
                    i++;
                };
            });
            if (i == 0) {
                i==0;
            } else{
                i--;
            };
            $menu_li.eq(i).addClass('active').siblings('li').removeClass('active');
            //判断是否滚动到底部
            var canTop=$(document).height()-$(window).height();
            if (sTop == canTop) {
                $menu_li.last().addClass('active').siblings('li').removeClass('active');
            }
        };
    });

    //切换应用

    function changeServerOfLogin(server){
        $.ajax({
            url:'${request.contextPath}/developer/sso/changeServerOfLogin',
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
                $("#decodeAuthOfLogin").html(data.decodeAuth);
            }
        });
    }
    //修改url
    function modifyUrlOfLogin(url) {
        $.ajax({
            url:'${request.contextPath}/developer/sso/modifyUrlOfLogin',
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
                $("#decodeAuthOfLogin").html(data.decodeAuth);
                $("#urlOfLogin").html(data.url);
            }
        });
    }
    //拉取用户信息接口用例
    function changeTicketOfCheckTicket(ticket){
        $.ajax({
            url:'${request.contextPath}/developer/sso/changeTicketOfCheckTicket',
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
                $("#decodeAuthOfCheckTicket").html(data.decodeAuth);
                $("#urlOfCheckTicket").html(data.url);
            }
        });
    }
    function changeServerOfCheckTicket(serverId){
        $.ajax({
            url:'${request.contextPath}/developer/sso/changeServerOfCheckTicket',
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
                $("#decodeAuthOfCheckTicket").html(data.decodeAuth);
                $("#urlOfCheckTicket").html(data.url);
            }
        });
    }
    function changeServerOfActiveExit(server){
        $.ajax({
            url:'${request.contextPath}/developer/sso/changeServerOfActiveExit',
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
                $("#decodeAuthOfActiveExit").html(data.decodeAuth);
                $("#eg-urlOfActiveExit").html(data.url);
            }
        });
    }
    function modifyTicketOfActiveExit(ticket){
        $.ajax({
            url:'${request.contextPath}/developer/sso/modifyTicketOfActiveExit',
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
                $("#decodeAuthOfActiveExit").html(data.decodeAuth);
                $("#eg-urlOfActiveExit").html(data.url);
            }
        });
    }
    function modifyUsernameOfClientLoign(username){
        $.ajax({
            url:'${request.contextPath}/developer/sso/modifyOfClientLoign',
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
            url:'${request.contextPath}/developer/sso/modifyOfClientLoign',
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
            url:'${request.contextPath}/developer/sso/modifyOfClientLoign',
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
    function download(type){
        location.href="${request.contextPath}/developer/sso/download?type="+type;
    }
    function changeServerOfVerify(serverId){

        $.ajax({
            url:"${request.contextPath}/developer/sso/changeServerOfVerify",
            data:{"serverId":serverId},
            type:"post",
            sync:false,
            success:function(result){
                var data =JSON.parse(result);
                $("#indexUrlOfVerify").html(data.indexUrl);
                $("#verifyEncodeAuth").html(data.verifyEncodeAuth);
                $("#verifyDecodeAuth").html(data.verifyDecodeAuth);
//                alert($("#urlOfVerify").html());
                $("#urlOfVerify").html(data.urlOfVerify);
            }
        });
    }
</script>

<!--代码高亮-->
<!--  <script src="${resourceUrl}/components/highlight/highlight.min.js"></script>
<script type="text/javascript">
    hljs.initHighlightingOnLoad();
</script>-->

<!-- inline scripts related to this page -->
<script>
    $(function(){
        $('.base-fixed-menu li').click(function(){
            $(this).addClass('active').siblings('li').removeClass('active');
        });

        modifyUrlOfLogin("http://www.demo.net/index.aspx");
    })
</script>
<style>
    .newline {
        word-wrap: break-word
    }

    .p-1 {
        font-size: 15px
    }
</style>

