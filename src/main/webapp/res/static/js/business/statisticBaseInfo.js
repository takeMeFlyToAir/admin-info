
$(function(){
    //表单验证
    $("#form-statisticBaseInfo").validate({
        rules:{
            year:{
                required:true,
                digits:true
            },
            subject:{
                required:true
            },
            bonus:{
                required:true,
                number:true
            },
            allTc:{
                required:true,
                digits:true
            }
        },
        messages: {
            year: {
                required: "请输入年份",
                digits: "请输入合法的年份，比如:2019"
            },
            subject:{
                required: "请选择学科"
            },
            allTc: {
                required: "请输入总引用数",
                digits: "请输入整数"
            },
            bonus: {
                required: "请输入奖金",
                number: "请输入合法奖金数"
            }
        },
        onkeyup:false,
        focusCleanup:true,
        success:"valid",
        submitHandler:function(form){
            $.ajax({
                type : "post",
                url : '/statisticBaseInfo/check',
                data : {"id":$("#id").val(),"subject":$("#subject").val(),"year":$("#year").val()},
                dataType : "json",
                async : false,
                success : function(data) {
                    if (data.result) {
                        if(data.data){
                            $.ajax({
                                type : "post",
                                url : '/statisticBaseInfo/addOrUpdate',
                                data : $("#form-statisticBaseInfo").serialize(),
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
                        }else{
                            layer.msg("已存在此年份和学科的数据，请重新填写",{icon:2,time:1000});
                        }

                    }else{
                        layer.msg(data.message,{icon:2,time:1000});
                    }
                }
            });
        }
    });
});


function getDetail() {
    var id = getUrlParam("statisticBaseInfoId");
    $.ajax({
        type : "get",
        url : '/statisticBaseInfo/detail?id='+id,
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.result) {
                initFormValue("form-statisticBaseInfo",data.data);
            }else{
                layer.msg(data.message,{icon:2,time:1000});
            }
        }
    });
}
