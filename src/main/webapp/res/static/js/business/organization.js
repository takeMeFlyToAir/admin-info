
$(function(){
    //表单验证
    $("#form-organization").validate({
        rules:{
            name:{
                required:true,
                remote: {
                    url: "/organization/validateName",
                    type: "post",
                    data: {
                        name: function () {
                            return $('#name').val();
                        },
                        id: function(){
                            return $('#id').val();
                        }
                    }
                }
            },
            remark:{
                required:true
            }
        },
        messages: {
            name: {
                required: "请输入组织名称",
                remote: "此组织名称已存在"
            },
            remark:{
                required: "请输入备注"
            }
        },
        onkeyup:false,
        focusCleanup:true,
        success:"valid",
        submitHandler:function(form){
            $.ajax({
                type : "post",
                url : '/organization/addOrUpdate',
                data : $("#form-organization").serialize(),
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


function getDetail() {
    var id = getUrlParam("organizationId");
    $.ajax({
        type : "get",
        url : '/organization/detail?id='+id,
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.result) {
                initFormValue("form-organization",data.data);
            }else{
                layer.msg(data.message,{icon:2,time:1000});
            }
        }
    });
}