$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");

	var toName = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
	    CONTEXT_PATH + "/message/send",
	    {"toName": toName, "content": content},
	    function(data) {
	        data = $.parseJSON(data);
	        if (data.code == 0) {
                $("#hintBody").text("Success in sending the message!");
	        } else {
	            $("#hintBody").text(data.msg);
	        }
	        $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                location.reload();
            }, 700);
	    }
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}