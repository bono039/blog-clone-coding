// 삭제 기능
const deleteBtn = document.getElementById('delete-btn');

if(deleteBtn) {
    deleteBtn.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;

        fetch(`/api/articles/${id}`, {
            method: 'DELETE'
        })
        .then(() => {
            alert('삭제 완료');
            location.replace('/articles');
        });
    });
}


// 수정 기능
const modifyBtn = document.getElementById('modify-btn');

if(modifyBtn) {
    modifyBtn.addEventListener('click', event=> {
       let params = new URLSearchParams(location.search);
       let id = params.get('id');

       fetch(`/api/articles/${id}`, {
          method : 'PUT',
          headers: {  // headers에 요청 형식 지정
              "Content-Type": "application/json",
          },
          body: JSON.stringify({    // body에 HTML에 입력한 데이터를 JSON 형식으로 바꿔 보냄
              title:   document.getElementById('title').value,
              content: document.getElementById('content').value
          })
       })
       .then(() => {    // 요청 완료 시 마무리 작업
           alert('수정이 완료되었습니다ㅏ.');
           location.replace(`/articles/${id}`);
       });
    });
}