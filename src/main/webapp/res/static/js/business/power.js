
$(function(){
    //表单验证
    $("#form-power").validate({
        rules:{
            code:{
                required:true,
                remote: {
                    url: "/power/checkCode",
                    type: "post",
                    data: {
                        name: function () {
                            return $('#code').val();
                        }
                    }
                }
            },
            remark:{
                required:true
            }
        },
        messages: {
            code: {
                required: "请输入code值",
                remote: "此code已存在"
            },
            chargePerson:{
                required: "请输入备注"
            }
        },
        onkeyup:false,
        focusCleanup:true,
        success:"valid",
        submitHandler:function(form){
            $.ajax({
                type : "post",
                url : '/power/addPower',
                data : $("#form-power").serialize(),
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
});
