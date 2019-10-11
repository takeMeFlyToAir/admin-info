
$(function(){
    //表单验证
    $("#form-article").validate({
        rules:{
            title:{
                required:true,
            },
            author:{
                required:true,
            },
            organizationName:{
                required:true,
            },
            source:{
                required:true,
            }
        },
        messages: {
            title: {
                required: "请输入标题"
            },
            author: {
                required: "请输入作者"
            },
            organizationName: {
                required: "请输入作者单位"
            },
            source:{
                required: "请输入来源"
            }
        },
        onkeyup:false,
        focusCleanup:true,
        success:"valid",
        submitHandler:function(form){
            $.ajax({
                type : "post",
                url : '/article/addOrUpdate',
                data : $("#form-article").serialize(),
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
    var id = getUrlParam("articleId");
    $.ajax({
        type : "get",
        url : '/article/detail?id='+id,
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.result) {
                $("#id").val(data.data.id);
                $("#subject").text(data.data.subjectStr);
                $("#aut").text(data.data.aut);
                $("#ati").text(data.data.ati);
                $("#aso").text(data.data.aso);
                $("#adt").text(data.data.adt);
                $("#apd").text(data.data.apd);
                $("#apy").text(data.data.apy);
                $("#avl").text(data.data.avl);
                $("#ais").text(data.data.ais);
                $("#ac1").text(data.data.ac1);
            }else{
                layer.msg(data.message,{icon:2,time:1000});
            }
        }
    });
}
