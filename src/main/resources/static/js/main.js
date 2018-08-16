var result = null;
var host = null;
var port = null;

/**
 * POST 请求
 *
 * @param url    请求路径
 * @param data   请求数据(json)
 */
function post(url, data, isAsync) {
    if (isAsync == undefined) {
        isAsync = true;
    }
    $.ajax({
        async: isAsync,
        //提交数据的类型 POST GET
        type: "POST",
        //提交的网址
        url: url,
        //提交的数据
        data: data,
        //返回数据的格式
        datatype: "json",//"xml", "html", "script.db", "json", "jsonp", "text".
        //在请求之前调用的函数
        beforeSend: function(){
            lodding();
        },
        //成功返回之后调用的函数
        success: function(data){
            if (!data.success) {
                alertErrorMsg(data.msg);
            } else {
                alertSuccMsg("真好, 处理成功!&nbsp;👍");
            }
            hideLodding();
        },
        //调用出错执行的函数
        error: function(){
            //请求出错处理
            hideLodding();
            alertErrorMsg("哎呀，出错了!");
        }
    });
}

/**
 * GET 请求
 *
 * @param url       请求地址
 * @param data      数据
 * @param isAsync   异步(true)/同步(false)
 */
function get(url, data, isAsync) {
    if (isAsync == undefined) {
        isAsync = true;
    }
    $.ajax({
        async: isAsync,
        //提交数据的类型 POST GET
        type: "GET",
        //提交的网址
        url: url,
        //提交的数据
        data: data,
        //返回数据的格式
        datatype: "json",//"xml", "html", "script.db", "json", "jsonp", "text".
        //在请求之前调用的函数
        beforeSend: function(){
            lodding();
        },
        //成功返回之后调用的函数
        success: function(data){
            if (!data.success) {
                alertErrorMsg(data.msg);
            } else {
                hideLodding();
                result = null;
                result = data.payload;
            }
        },
        //调用出错执行的函数
        error: function(){
            //请求出错处理
            hideLodding();
            alertErrorMsg("哎呀，出错了!");
        }
    });
}

var serverWs;

function connectServer(serverId) {
    get("/serverController/getById", { id: serverId }, false);

    /*if (serverWs != undefined && serverWs != null && serverWs.readyState == 1) {
        serverWs.close();
        serverWs = null;
    }*/
    /**
     * CONNECTING：值为0，表示正在连接。
     * OPEN：值为1，表示连接成功，可以通信了。
     * CLOSING：值为2，表示连接正在关闭。
     * CLOSED：值为3，表示连接已经关闭，或者打开连接失败。
     */
    if (serverWs == undefined || serverWs == null || serverWs.readyState == 3) {
        serverWs = createIfDisConnect(serverWs, "/server/connect", serverId);
    }

    var wsObj = {serverId: serverId, command: ""};
    serverWs.onopen = function (event) {
        sendTextIfConnect(serverWs, wsObj);
    }
    serverWs.onmessage = function (event) {
        serverAppend(event.data);
    }
    serverWs.onclose = function (event) {
        // $("#server_output").val("");
        alertWarnMsg(result.alias + " is Disconnect");
    }
    serverWs.onerror = function (event) {
        alertErrorMsg("Server WebSocket is Unexpected shutdown")
    }

    changeOutputHeight();
    $("#server_output").css("height", serverOutputHeight() - 30)
    $("#script-output").css("display", "block");
    $("#server_table").css("display", "none");
}

function serverAppend(data) {
    $("#server_output").append(data);
    $("#server_output").focus();
}

function sendTextIfConnect(ws, obj) {
    if (checkWsStatus(ws, 1)) {
        ws.send(JSON.stringify(obj));
    }
}

function createIfDisConnect(ws, url) {
    if (ws == null || ws.readyState == 3) {
        return new WebSocket("ws://" + host + ":" + port + url);
    }
}

function checkWsStatus(ws, state) {
    if (ws != null && ws.readyState == state) {
        return true;
    }
    return false;
}

function closeIfConnect(ws) {
    if (checkWsStatus(ws, 1)){
        ws.close();
        ws = null;
    }
}

function closeConnServer(ws) {
    $("#server_table").css("display", "block");
    $("#script-output").css("display", "none");
    // closeIfConnect(ws);
}

function deleteItem() {
    var tabId = $(".mdui-tab-active").attr("href").split("-")[1];
    if (tabId == "list") {
        var scriptIds = [];
        $("#script-list #script-tbody").find("tr").each(function () {
            var trArr = $(this).attr("class");
            if (trArr == "mdui-table-row-selected") {
                scriptIds.push($(this).find("td").eq(1).text());
            }
        });
        mdui.alert(scriptIds);
    } else if (tabId == "server") {
        var serverIds = "";
        $("#script-server #server-tbody").find("tr").each(function () {
            var trArr = $(this).attr("class");
            if (trArr == "mdui-table-row-selected") {
                serverIds += ($(this).find("td").eq(1).text()) + ",";
            }
        });
        if (serverIds == "" || serverIds == null) {
            alertErrorMsg("请先选择要删除的项, OK? 😟");
            return;
        }
        post("/serverController/deleteServers", JSON.stringify(serverIds.substring(0, serverIds.length - 1)));
        getServerByPage();
    } else {
        alertWarnMsg("少年, 你觉得这个页面有需要删除的东西吗?&nbsp;[手动滑稽]");
    }
}

function deleteById(id) {
    var server = {
        serverId : id
    }
    post("/serverController/deleteById", server, false);
    getServerByPage();
}

function getServerByPage() {
    $("#server_table").text("");
    var page = {
        pageIndex: 0,
        pageSize: 20
    };
    get("/serverController/getByPage", page, false);
    var txt = "<table class=\"mdui-table mdui-table-selectable mdui-table-hoverable\">\n" +
              "    <thead>\n" +
              "        <tr>\n" +
              "            <th>Id</th>\n" +
              "            <th>Alias</th>\n" +
              "            <th>Host</th>\n" +
              "            <th>Port</th>\n" +
              "            <th>CreateTime</th>\n" +
              "            <th>LastLoginTime</th>\n" +
              "            <th>Operation</th>\n" +
              "        </tr>\n" +
              "    </thead>\n" +
              "    <tbody id=\"server-tbody\">";
    for (var i = 0 ; i < result.list.length; i++) {
        txt +=  "<tr>" +
                    "<td>" + result.list[i].id + "</td>" +
                    "<td>" + result.list[i].alias + "</td>" +
                    "<td>" + result.list[i].host + "</td>" +
                    "<td>" + result.list[i].port + "</td>" +
                    "<td>" + convertTime(result.list[i].createTime) + "</td>" +
                    "<td>" + convertTime(result.list[i].lastLoginTime) + "</td>" +
                    "<td>" +
                        "<button class=\"mdui-btn mdui-btn-dense mdui-color-indigo-600 mdui-ripple mdui-hoverable\" onclick='openEditServer(" + result.list[i].id + ")'><i class=\"mdui-icon material-icons\">edit</i>&nbsp;edit</button>" + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "<button class=\"mdui-btn mdui-btn-dense mdui-color-green-500 mdui-ripple mdui-hoverable\" onclick='connectServer(" + result.list[i].id + ")'><i class=\"mdui-icon material-icons\">play_arrow</i>&nbsp;connect</button>" + "&nbsp;&nbsp;&nbsp;&nbsp;" +
                        "<button class=\"mdui-btn mdui-btn-dense mdui-color-red-600 mdui-ripple mdui-hoverable\" onclick='confirmDelete(function () { deleteById(" + result.list[i].id + "); })'><i class=\"mdui-icon material-icons\">delete_forever</i>&nbsp;delete</button>" +
                    "</td>" +
                "</tr>";
    }
    txt += "</tbody></table>"
    $("#server_table").append(txt);
    mdui.mutation();
}

function openEditServer(id) {
    get("/serverController/getById", { id: id }, false);
    mdui.dialog({
        title: '修改',
        content: '<input type="hidden" id="server_update_id" placeholder="Please enter alias" class="layui-input" value="' + result.id + '">\n' +
                 '<input type="hidden" id="server_update_status" placeholder="Please enter alias" class="layui-input" value="' + result.status + '">\n' +
                 '<div class="layui-form-item" style="padding-top: 20px">\n' +
                 '    <label class="layui-form-label">Alias</label>\n' +
                 '    <div class="layui-input-block">\n' +
                 '         <input type="text" id="server_update_alias" placeholder="Please enter alias" class="layui-input" value="' + result.alias + '">\n' +
                 '    </div>\n' +
                 '</div>\n' +
                 '<div class="layui-form-item">\n' +
                 '     <label class="layui-form-label">Host</label>\n' +
                 '     <div class="layui-input-block">\n' +
                 '          <input type="text" id="server_update_host" placeholder="Please enter Host" class="layui-input" value="' + result.host + '">\n' +
                 '     </div>\n' +
                 '</div>\n' +
                 '<div class="layui-form-item">\n' +
                 '     <label class="layui-form-label">Port</label>\n' +
                 '     <div class="layui-input-block">\n' +
                 '          <input type="text" id="server_update_port" placeholder="Please enter Host" class="layui-input" value="' + result.port + '">\n' +
                 '     </div>\n' +
                 '</div>\n' +
                 '<div class="layui-form-item">\n' +
                 '     <label class="layui-form-label">User</label>\n' +
                 '     <div class="layui-input-block">\n' +
                 '          <input type="text" id="server_update_user" placeholder="Please enter User" class="layui-input" value="' + result.user + '">\n' +
                 '     </div>\n' +
                 '</div>\n' +
                 '<div class="layui-form-item">\n' +
                 '     <label class="layui-form-label">Password</label>\n' +
                 '     <div class="layui-input-block">\n' +
                 '          <input type="password" id="server_update_password" placeholder="Please enter Password" class="layui-input" value="' + result.password + '">\n' +
                 '     </div>\n' +
                 '</div>',
        buttons: [
            {
                text: '取消'
            },
            {
                text: '确认',
                onClick: function () {
                    editServer()
                }
            }
        ]
    });
}

function editServer() {
    var obj = {};
    obj.id = $("#server_update_id").val();
    obj.status = $("#server_update_status").val();
    obj.alias = $("#server_update_alias").val();
    obj.host = $("#server_update_host").val();
    obj.port = $("#server_update_port").val();
    obj.user = $("#server_update_user").val();
    obj.password = $("#server_update_password").val();
    if (checkServerParam(obj)) {
        post("/serverController/editServer", JSON.stringify(obj), false);
        getServerByPage();
    }
}

function confirmDelete(func) {
    mdui.confirm('数据无价, 请谨慎删除! 如确认无误, 请点击<strong>OK</strong>.', '你想好了吗? ',
        func,
        null
    );
}

function addServer() {
    var obj = {};
    obj.alias = $("#server_alias").val();
    obj.host = $("#server_host").val();
    obj.port = $("#server_port").val();
    obj.user = $("#server_user").val();
    obj.password = $("#server_password").val();
    if (checkServerParam(obj)) {
        post("/serverController/addServer", JSON.stringify(obj));
    }

    // $("#server_alias").val("");
    // $("#server_host").val("");
    // $("#server_port").val("");
    // $("#server_user").val("");
    // $("#server_password").val("");
}

function checkServerParam(obj) {
    if (obj.alias == null || obj.alias == "") {
        paramMissing("Alias");
        return false;
    }
    if (obj.host == null || obj.host == "") {
        paramMissing("Host");
        return false;
    }
    if (obj.port == null || obj.port == "") {
        paramMissing("Port");
        return false;
    }
    if (obj.user == null || obj.user == "") {
        paramMissing("User");
        return false;
    }
    if (obj.password == null || obj.password == "") {
        paramMissing("Password");
        return false;
    }
    return true;
}

function convertTime(time) {
    var date = new Date(time);
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = date.getDate() + ' ';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    return time == null ? "－－" : Y + M + D + h + m + s;
}

function lodding() {

}

function hideLodding() {

}

function paramMissing(field) {
    alertErrorMsg("亲亲, <font color=\"red\"> [ " + field + " ] </font>不能为空啊")
}

function alertSuccMsg(text) {
    var options = {
        confirmText: '好的, 好的!',       // 按钮上的文本
        history: true,                  // 监听 hashchange 事件
        modal: false,                   // 是否模态化对话框，为 false 时点击对话框外面区域关闭对话框，为 true 时不关闭
        closeOnEsc: true,               // 按下 esc 关闭对话框
    };
    mdui.alert(text, "恭喜你 👏", undefined, options);
}

function alertErrorMsg(text) {
    var options = {
        confirmText: '知道了, 别烦!',     // 按钮上的文本
        history: true,                  // 监听 hashchange 事件
        modal: false,                   // 是否模态化对话框，为 false 时点击对话框外面区域关闭对话框，为 true 时不关闭
        closeOnEsc: true,               // 按下 esc 关闭对话框
    };
    mdui.alert(text, "貌似又出问题了? 🙄", undefined, options);
}

function alertWarnMsg(text) {
    var options = {
        confirmText: '懂了, 懂了!',     // 按钮上的文本
        history: true,                  // 监听 hashchange 事件
        modal: false,                   // 是否模态化对话框，为 false 时点击对话框外面区域关闭对话框，为 true 时不关闭
        closeOnEsc: true,               // 按下 esc 关闭对话框
    };
    mdui.alert(text, "哎呀, 你这人 🤓", undefined, options);
}

function serverOutputHeight() {
    return $("#script_output_div").height();
}

function changeOutputHeight() {
    $("#script_output_div").css("height", (changeDivHeight() - 45) + "px");
}

function changeDivHeight() {
    // 浏览器可见区域的高
    var bodyHeight = $(document).height();
    var divHeight = barHeight();
    return bodyHeight - divHeight;
}

function barHeight() {
    return $("#bar").height();
}

$('#sahre-script').click(function(){
    $(this).addClass("clicked-script");
    $(this).removeAttr("style");
    $(this).css("position", "fixed");
    $(this).height(changeDivHeight() - 50);
});

$('#sahre-server').click(function(){
    $(this).addClass("clicked-server");
    $(this).css("position", "fixed");
    $(this).css("margin", "-40px 690px");
    $(this).css("top", "400px");
});

$('.script_close').click(function (e) {
    $("#sahre-script").removeClass('clicked-script');
    $("#sahre-script").removeAttr("style");
    // $("#sahre-script").css("margin-left", "-400px");
    $("#sahre-script").css("margin", "0px 750px");
    $("#sahre-script").css("z-index", "2000");
    e.stopPropagation();
});

$('.server_close').click(function (e) {
    $('#sahre-server').removeClass('clicked-server');
    $("#sahre-server").css("margin", "0px 880px");
    $("#sahre-server").css("z-index", "2000");
    $("#sahre-server").css("position", "absolute");
    $("#sahre-server").css("top", "35px");
    e.stopPropagation();
});