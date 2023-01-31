$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	if($(btn).hasClass("btn-info")) {
		// Follow
		$.post(
		    CONTEXT_PATH + "/follow",
		    {"entityType": 3, "entityId": $(btn).prev().val()},
		    function(data) {
		        data = $.parseJSON(data);
		        if (data.code == 0) {
                    window.location.reload();
		        } else {
                    alert(data.msg);
		        }
		    }
		);
	} else {
		// Unfollow
		$.post(
            CONTEXT_PATH + "/unFollow",
            {"entityType": 3, "entityId": $(btn).prev().val()},
            function(data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
	}
}