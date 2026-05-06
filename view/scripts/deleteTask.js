function deleteTask(button) {
  // Obtém a linha (tr) onde o botão foi clicado
  const row = button.closest('tr');

  // Obtém o valor do ID da tarefa a partir do elemento com id="taskId"
  const taskId = row.querySelector('#taskId').textContent;

  console.log("Task ID:", taskId);
  
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
      showToast("success", "Atividade deletada com sucesso. Redirecionando para o painel...");

      window.setTimeout(function () {
        window.location = "/view/index.html";
      }, 500);
    } else {
      showToast("danger", "Não foi possível deletar a atividade. Verifique os dados e tente novamente.");
    }
  } catch (error) {
    console.error("Erro ao deletar tarefa:", error);
    showToast("danger", "Não foi possível deletar a atividade. Verifique os dados e tente novamente.");
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (!localStorage.getItem("Authorization"))
      window.location = "/view/login.html";
});