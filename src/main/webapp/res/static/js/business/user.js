
$(function(){
    //表单验证
    $("#form-user-add").validate({
        rules:{
            userName:{
                required:true,
                remote: {
                    url: "/user/checkUserName",
                    type: "post",
                    data: {
                        name: function () {
                            return $('#userName').val();
                        }
                    }
                }
            },
            nickName:{
                required:true
            },
            organizationId:{
                required:true
            },
            password:{
                required:true
            }
        },
        messages: {
            userName: {
                required: "请输入用户名",
                remote: "此用户名已存在"
            },
            nickName:{
                required: "请输入昵称"
            },
            organizationId:{
                required: "请选择组织"
            },
            password:{
                required: "请输入默认密码"
            }
        },
        onkeyup:false,
        focusCleanup:true,
        success:"valid",
        submitHandler:function(form){
            var organizationId = $("#organizationId").val();
            if(!organizationId){
                tip("请选择组织")
                return;
            }
            $.ajax({
                type : "post",
                url : '/user/add',
                data : $("#form-user-add").serialize(),
                dataType : "json",
                async : false,
                success : function(data) {
                    if (data.result) {
                        layer.msg(data.message,{icon:1,time:1000});
                        parent.dataTable.api().ajax.reload();
                        cancelEdit();
                    }else{
                        layer.msg(data.message,{icon:2,time:1000});
                    }
                }
            });
        }
    });

    //表单验证
    $("#form-user-edit").validate({
        rules:{
            nickName:{
                required:true
            },
            organizationId:{
                required:true
            }
        },
        messages: {
            nickName:{
                required: "请输入昵称"
            },
            organizationId:{
                required: "请选择组织"
            }
        },
        onkeyup:false,
        focusCleanup:true,
        success:"valid",
        submitHandler:function(form){
            var id = $("#id").val();
            var nickName = $("#nickName").val();
            var organizationId = $("#organizationId").val();
            if(!organizationId){
                tip("请选择组织")
                return;
            }
            $.ajax({
                type : "post",
                url : '/user/edit',
                data : {"id":id,"nickName":nickName,"organizationId":organizationId},
                dataType : "json",
                async : false,
                success : function(data) {
                    if (data.result) {
                        layer.msg(data.message,{icon:1,time:1000});
                        parent.dataTable.api().ajax.reload();
                        cancelEdit();
                    }else{
                        layer.msg(data.message,{icon:2,time:1000});
                    }
                }
            });
        }
    });

    //表单验证
    $("#form-user-edit-password").validate({
        rules:{
            oldPassword:{
                required:true
            },
            newPassword:{
                required:true
            },
            againNewPassword:{
                required:true,
                equalTo: "#newPassword"
            }
        },
        messages: {
            oldPassword:{
                required: "请输入旧密码"
            },
            newPassword:{
                required: "请输入新密码"
            },
            againNewPassword:{
                required: "请输入新密码",
                equalTo: "两次密码输入不一致"
            }
        },
        onkeyup:false,
        focusCleanup:true,
        success:"valid",
        submitHandler:function(form){
            var oldPassword = $("#oldPassword").val();
            var newPassword = $("#newPassword").val();
            $.ajax({
                type : "post",
                url : '/user/modifyPassword',
                data : {"oldPassword":oldPassword,"newPassword":newPassword},
                dataType : "json",
                async : false,
                success : function(data) {
                    if (data.result) {
                        layer.msg(data.message,{icon:1,time:1000});
                    }else{
                        layer.msg(data.message,{icon:2,time:1000});
                    }
                }
            });
        }
    });
});



function allOrganization(addFlag,organizationId) {
    $.ajax({
        type : "get",
        url : '/organization/findAll',
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.result) {
                var content = '';
                var result = data.data;
                if (result == null || result.length === 0) {
                    content += ('<option value="">请先添加组织</option>')
                    $('#organizationId').html(content);
                } else {
                    if(addFlag){
                        content += ('<option  value="">请选择组织</option>')
                        for (var i = 0; i < result.length; i++) {
                            content += ('<option  value='+ result[i].id +'>'+ result[i].name +'</option>')
                        }
                        console.log(content)
                        $('#organizationId').html(content);
                    }else{
                        var organizationIdHtml = ('<option  value="">请选择组织</option>');
                        for (var i = 0; i < result.length; i++) {
                            if(result[i].id == organizationId){
                                organizationIdHtml += ('<option selected="selected" value='+ result[i].id +'>'+ result[i].name +'</option>')
                            }else{
                                organizationIdHtml += ('<option  value='+ result[i].id +'>'+ result[i].name +'</option>')
                            }
                        }
                        $('#organizationId').html(organizationIdHtml);
                    }
                }

            }else{
            }
        }
    });

}


function getDetail() {
    var id = getUrlParam("userId");
    $.ajax({
        type : "get",
        url : '/user/detail?id='+id,
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.result) {
                initFormValue("form-user-edit",data.data);
                allOrganization(false, data.data.organizationId);
            }else{
                layer.msg(data.message,{icon:2,time:1000});
            }
        }
    });
}

function info() {
    $.ajax({
        type : "get",
        url : '/user/info',
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.result) {
                initFormValue("form-user-info",data.data);
            }else{
                layer.msg(data.message,{icon:2,time:1000});
            }
        }
    });
}