/**
 * Created by qiuyujiang on 2018/11/1.
 */

var isSysAdmin = false;
var appPower  = [];
function loadSysAdmin() {
    $.ajax({
        type : "get",
        url : '/user/isSysAdmin',
        data : {},
        dataType : "json",
        async : false,
        success : function(data) {
            if(data.result){
                isSysAdmin = data.data;
            }
        }
    });
}

function loadAppPower() {
    $.ajax({
        type : "get",
        url : '/power/appPower',
        data : {},
        dataType : "json",
        async : false,
        success : function(data) {
            if(data.result){
                appPower = data.data;
            }
        }
    });
}
function isHasPower(code) {
    return isSysAdmin || appPower.indexOf(code) != -1;
}
$(function () {
    loadSysAdmin();
    loadAppPower();
    $(".power").each(function () {
        if(isSysAdmin){
            $(this).removeClass("hide");
            $(this).show();
        }else{
            var code =$(this).attr("code");
            if(code){
                if(appPower.indexOf(code) != -1){
                    $(this).removeClass("hide");
                    $(this).show();
                }
            }
        }
    });
});

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return decodeURI(r[2]);
    return null; //返回参数值
}

function initFormValue(formId,jsonValue) {
    var obj = $("#"+formId);
    $.each(jsonValue, function (name, ival) {
        var $input = obj.find("input[name=\'" + name + "\']");
        if ($input.attr("type") == "radio" || $input.attr("type") == "checkbox") {
            $input.each(function () {
                if (Object.prototype.toString.apply(ival) == '[object Array]') { // 是复选框，并且是数组
                    for (var i = 0; i < ival.length; i++) {
                        if ($(this).val() == ival[i])
                            $(this).attr("checked", "checked");
                    }
                } else {
                    if ($(this).val() == ival)
                        $(this).attr("checked", "checked");
                }
            });
        } else if ($input.attr("type") == "textarea") { // 多行文本框
            obj.find("[name=\'" + name + "\']").html(ival);
        } else {
            obj.find("[name=\'" + name + "\']").val(ival);
        }
    });
}


function cancelEdit() {
    var index = parent.layer.getFrameIndex(window.name);
    console.log(window.name)
    console.log(index)
    parent.layer.close(index);
}

function tip(message) {
    layer.msg(message,{icon:2,time:1000});
}

function keyPress(ob) {
    ob.value=ob.value.replace(/[^\d]/g,'');
}
function keyUp(ob) {
    if (!ob.value.match(/^[\+\-]?\d*?\.?\d*?$/)) ob.value = ob.t_value; else ob.t_value = ob.value; if (ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?)?$/)) ob.o_value = ob.value;
}
function onBlur(ob) {
    if(!ob.value.match(/^(?:[\+\-]?\d+(?:\.\d+)?|\.\d*?)?$/))ob.value=ob.o_value;else{if(ob.value.match(/^\.\d+$/))ob.value=0+ob.value;if(ob.value.match(/^\.$/))ob.value=0;ob.o_value=ob.value};
}