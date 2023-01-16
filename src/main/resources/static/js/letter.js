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
            }, 1000);
	    }
	);
}

function delete_msg() {
	// Delete the message
	$(this).parents(".media").remove();
	btn = this
    var deleteMsgId = $(btn).prev().val();
    $.post(
        CONTEXT_PATH + "/message/delete",
        {"messageId": deleteMsgId},
        function(data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                $("#hintBody").text("Success in deleting the message!");
            } else {
                $("#hintBody").text(data.msg);
            }
            $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                location.reload();
            }, 1000);
        }
    );
}