$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	// Get title and content
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
	    CONTEXT_PATH + "/discuss/add",
	    {"title": title, "content":content},
	    function(data) {
	        data = $.parseJSON(data);
	        // Show notification
	        $("#hintBody").text(data.msg);
	        $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                // If success, refresh the page
                if (data.code == 0) {
                    window.location.reload();
                }
            }, 700);
	    }
	)
}