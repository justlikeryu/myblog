const delBtn = document.getElementById('delete-btn');

if (delBtn) {
    delBtn.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;

        function success() {
            alert("삭제가 완료되었습니다.");//todo 1. 내가 작성한 글을 삭제할 수 없음(그런데 메세지는 삭제 완료됐다고 뜸) 2. 내가 작성한 글이 아니어도 삭제 시도하면 삭제 완료되었다고 뜸(실제로 삭제는 안 됨)
            location.replace("/articles");
        }

        function fail() {
            alert("삭제 실패하였습니다.");
            location.replace("/articles");
        }

        httpRequest("DELETE", "/api/articles/" + id, null, success, fail);
    });
}

const editBtn = document.getElementById('edit-btn');

if (editBtn) {
    editBtn.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value,
        });

        function success() {
            alert("수정 되었습니다.");
            location.replace("/articles/" + id);
        }

        function fail() {
            alert("수정 실패했습니다.");
            location.replace("/articles/" + id);
        }

        httpRequest("PUT", "/api/articles/" + id, body, success, fail);
    });
}

const createBtn = document.getElementById('create-btn');

if (createBtn) {
    createBtn.addEventListener('click', (event) => {
        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value,
        });

        function success() {
            alert("등록 되었습니다");
            location.replace("/articles");
        }

        function fail() {
            alert("등록 실패하였습니다.");
            location.replace("/articles");
        }

        httpRequest("POST", "/api/articles", body, success, fail);
    });
}

function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(";");

    cookie.some(function (item) {
        item = item.replace(" ", "");
        var dic = item.split("=");
        if (key == dic[0]) {
            result = dic[1];
            return true;
        }
    });
    return result;
}

function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            //로컬 스토리지에서 액세스 토큰값을 가져와 헤더에 추가
            Authorization: "Bearer " + localStorage.getItem("access_token"),
            "Content-Type": "application/json",
        },
        body: body,
    }).then((response) => {
        if (response.status == 200 || response.status == 201) {
            return success();
        }
        const refresh_token = getCookie("refresh_token");
        if (response.status == 401 && refresh_token) {
            fetch("/api/token", {
                method: "POST",
                headers: {
                    Authorization: "Bearer " + localStorage.getItem("access_token"),
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    refreshToken: getCookie("refresh_token"),
                }),
            })
                .then((result) => {
                    if (result.ok) {
                        return result.json();
                    }
                })
                .then((result) => {
                    //재발급에 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체한다
                    localStorage.setItem("access_token", result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch((error) => fail());
        } else {
            return fail();
        }
    });
}