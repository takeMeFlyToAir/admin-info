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
                $("#authorTypeStr").text(data.data.authorTypeStr);
                $("#author").text(data.data.author);
                $("#claimUserOrganizationName").text(data.data.claimUserOrganizationName);
                $("#claimUserName").text(data.data.claimUserName);
                $("#claimNickName").text(data.data.claimNickName);
            }else{
                layer.msg(data.message,{icon:2,time:1000});
            }
        }
    });
}
