
    // 提示语
    // var prompts = ["xxx", "yyy", "zzz"];
    var promptHeight = 25;

    // addPrompts(prompts);

    function addPrompts(prompts) {
        if (!prompts || prompts.length == 0) {
            return;
        }
        var lis = "";
        for(var i = 0; i < prompts.length; i++) {
            lis += "<li class=\"ppmsg\" onmouseover=\"activePrompt(this)\" onmouseleave=\"inactivePrompt(this)\" onclick=\"choosePrompt(this)\">" + prompts[i] + "</li>";
        }
        $("#promptul").html(lis);

        $("#prompt").height(promptHeight * prompts.length + 10);
        $("#prompt").show();
    }

    function activePrompt(oPrompt) {
        oPrompt.style.background = "lightgray";
    }

    function inactivePrompt(oPrompt) {
        oPrompt.style.background = "white";
    }

    function choosePrompt(oPrompt) {
        $("#prompt").hide();
        $("#txt").val(oPrompt.innerText);
    }

    function sendMsg() {
        //定义需要添加的元素
        var question = $("#txt").val();
        printChat(question, "client");
        clearInput();
        var data = {"question": question};
        $.ajax({
            type: 'POST',
            contentType: "application/json;charset=UTF-8",
            url: 'chat-data/answer',
            dataType: 'json',
            data: JSON.stringify(data),
            success: function (resp) {
                if (!resp.successful) {
                    alert(resp.error);
                        return;
                    }
                    printChat(resp.data, "server");
                }
        });
    }

    function clearInput() {
        $("#txt").val("");
    }

    function printChat(msg, chatFrom) {
        if (!msg) {
            return;
        }
        var classname = (chatFrom == "server")? "showTxt left" : "showTxt right";

        //添加对话框
        $("#save").append("<li class=\"" + classname + "\">" + msg + "</li>");

        //清除浮动
        $("#save").append("<div style=\"clear: both;height: 10px;\"></div>");

        var scrollHeight = $("#content")[0].scrollHeight;
        $('#content').scrollTop(scrollHeight, 2000);
    }

    // 延时搜索
    var timeoutId = 0;

function delaySearch() {
    $('#txt').off('keyup').on('keyup', function (event) {
        clearTimeout(timeoutId);
        var data = {"question": $("#txt").val()}
        timeoutId = setTimeout(function () {
            $.ajax({
                type: 'POST',
                contentType: "application/json;charset=UTF-8",
                url: 'chat-data/prompts',
                dataType: 'json',
                data: JSON.stringify(data),
                success: function (resp) {
                    if (!resp.successful) {
                        alert(resp.error);
                        return;
                    }
                    addPrompts(resp.data);
                }
            });
        }, 500);
    });
}
