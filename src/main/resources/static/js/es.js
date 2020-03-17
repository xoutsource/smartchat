function createIndices() {
    $.ajax({
        type: 'POST',
        contentType: "application/json;charset=UTF-8",
        url: 'dataimport/createIndices',
        dataType: 'json',
        data: "{}",
        success: function (resp) {
            if (resp.successful) {
                alert("成功");
            } else {
                alert("失败");
            }
        }
    });
}

function syncEs() {
    $.ajax({
        type: 'POST',
        contentType: "application/json;charset=UTF-8",
        url: 'dataimport/syncEs',
        dataType: 'json',
        data: "{}",
        success: function (resp) {
            if (resp.successful) {
                alert("成功");
            } else {
                alert("失败");
            }
        }
    });
}