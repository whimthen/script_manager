function post(url, data) {
    $.ajax({
        //提交数据的类型 POST GET
        type: "POST",
        //提交的网址
        url: url,
        //提交的数据
        data: data,
        //返回数据的格式
        datatype: "json",//"xml", "html", "script", "json", "jsonp", "text".
        //在请求之前调用的函数
        beforeSend: function(){
            lodding();
        },
        //成功返回之后调用的函数
        success: function(data){
            $("#msg").html(decodeURI(data));
            hideLodding();
        },
        //调用执行后调用的函数
        complete: function(XMLHttpRequest, textStatus){
            alert(XMLHttpRequest.responseText);
            alert(textStatus);
            hideLodding();
        },
        //调用出错执行的函数
        error: function(){
            //请求出错处理
            hideLodding();
        }
    });
}

function lodding() {

}

function hideLodding() {

}

$('#sahre-script').click(function(){
    $(this).addClass("clicked");
});

$('#sahre-server').click(function(){
    $(this).addClass("clicked");
});

$('.script_close').click(function (e) {
    $('#sahre-script').removeClass('clicked');
    e.stopPropagation();
});


$('.server_close').click(function (e) {
    $('#sahre-server').removeClass('clicked');
    e.stopPropagation();
});