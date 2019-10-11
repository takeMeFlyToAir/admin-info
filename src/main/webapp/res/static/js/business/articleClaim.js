function getDetail() {
    var id = getUrlParam("id");
    $.ajax({
        type : "get",
        url : '/articleClaim/detail?id='+id,
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.result) {
                $("#id").val(data.data.id);
                $("#articleId").val(data.data.articleId);
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
                $("#remark").text(data.data.remark);
                if(data.data.status == 0 || data.data.status == 20){
                    $("#rejectClaimArticleButton").show();
                }
            }else{
                layer.msg(data.message,{icon:2,time:1000});
            }
        }
    });
}
