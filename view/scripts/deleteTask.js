function deleteTask(button) {
  // Obtém a linha (tr) onde o botão foi clicado
  const row = button.closest('tr');

  // Obtém o valor do ID da tarefa a partir do elemento com id="taskId"
  const taskId = row.querySelector('#taskId').textContent;

  console.log("Task ID:", taskId);

  // Agora você pode usar o taskId conforme necessário
  deleteTaskById(taskId);
}

async function deleteTaskById(taskId) {
  let key = "Authorization";
  let endpoint = "http://localhost:8080/task/" + taskId;

  console.log(endpoint);

  try {
    const response = await fetch(endpoint, {
      method: "DELETE",
      headers: new Headers({
        "Content-Type": "application/json; charset=utf8",
        Accept: "application/json",
        Authorization: localStorage.getItem(key),
      }),
    });

    if (response.ok) {
      showToast("#successDelete");

      window.setTimeout(function () {
        window.location = "/view/index.html";
      }, 1000);
    } else {
      showToast("#errorDelete");
    }
  } catch (error) {
    console.error("Erro ao deletar tarefa:", error);
    showToast("#errorDelete");
  }
}
  
function showToast(id) {
  var toastElList = [].slice.call(document.querySelectorAll(id));
  var toastList = toastElList.map(function (toastEl) {
    return new bootstrap.Toast(toastEl);
  });
  toastList.forEach((toast) => toast.show());
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (!localStorage.getItem("Authorization"))
      window.location = "/view/login.html";
});