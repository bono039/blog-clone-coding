// 삭제 기능
const deleteBtn = document.getElementById('delete-btn');

if(deleteBtn) {
    deleteBtn.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;

        function success() {
            alert('삭제 완료!');
            location.replace('/articles');
        }

        function fail() {
            alert('삭제 실패ㅠ');
            location.replace('/articles');
        }

        httpRequest('DELETE', '/api/articles/' + id, null, success, fail);
    });
}

// 수정 기능
const modifyBtn = document.getElementById('modify-btn');

if(modifyBtn) {
    modifyBtn.addEventListener('click', event=> {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body: JSON.stringify({    // body에 HTML에 입력한 데이터를 JSON 형식으로 바꿔 보냄
            title:   document.getElementById('title').value,
            content: document.getElementById('content').value
        });

        function success() {
            alert('수정 완료!');
            location.replace('/articles/' + id);
        }

        function fail() {
            alert('수정 실패ㅠ');
            location.replace('/articles/' + id);
        }

        httpRequest('PUT', '/api/articles/' + id, body, success, fail);
    });
}

// 등록(생성) 기능
const createBtn = document.getElementById('create-btn');

if(createBtn) {
    // 등록 버튼 클릭하면 /api/articles로 요청 보냄
    createBtn.addEventListener('click', event => {
        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        });

        function success() {
            alert('등록 완료!');
            location.replace('/articles');
        };

        function fail() {
            alert('등록 실패ㅠ');
            location.replace('/articles');
        };

        httpRequest('POST', '/api/articles', body, success, fail)
    });
}

// 쿠키 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
       item = item.replace(' ', '');

       var dic = item.split('=');

       if(key === dic[0]) {
           result = dic[1];
           return true;
       }
    });

    return result;
}

// HTTP 요청 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            // 로컬 스토리지에서 액세스 토큰 값 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if(response.status === 200 || response.status === 201) {
            return success;
        }

        const refresh_token = getCookie('refresh_token');
        if(response.status === 401 && refresh_token) {   // 권한 없다는 에러코드 발생
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if(res.ok) {
                        return res.json();
                    }
                })
                .then(result => {
                    // 재발급 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        }
        else {
            return fail();
        }
    });
}