
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
    var type = getUrlParam("type");
    $.ajax({
        type : "get",
        url : '/article/detail?id='+id,
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.result) {
                initFormValue("form-article",data.data);
                if(type =="claim" && data.data.status == 0){
                    $("#claimArticleButton").show();
                }else if(type == "audit"){
                    $("#auditArticleButton").show();
                }else if(type == "rejectClaim"){
                    $("#rejectClaimArticleButton").show();
                }
            }else{
                layer.msg(data.message,{icon:2,time:1000});
            }
        }
    });
}
