let taskId;
let key = "Authorization";
const url = "http://localhost:8080/task/";

document.addEventListener("DOMContentLoaded", async function () {
  // Obtém os parâmetros da URL
  const urlParams = new URLSearchParams(window.location.search);
  taskId = urlParams.get("id"); // Obtém o ID da atividade
  
  if (taskId) {
    try {
      const endpoint = url + taskId;
      const response = await fetch(endpoint, {
        method: "GET",
        headers: new Headers({
          "Content-Type": "application/json; charset=utf8",
          Accept: "application/json",
          Authorization: localStorage.getItem(key),
        }),
      });

      if (!response.ok) {
        throw new Error("Erro ao buscar atividade");
      }

      const taskData = await response.json();
      document.getElementById("description").value = taskData.description; // Define o valor do campo
    } catch (error) {
      console.error("Erro ao carregar a atividade:", error);
    }
  }
});

async function updateTask() {
  const description = document.getElementById('description').value;

  if (!description) {
    showToast("danger", "Descrição não pode ser vazia!"); // Exibe o toast de erro
    return; // Interrompe a execução da função
  }

  try {
    const endpoint = url + taskId;
    const response = await fetch(endpoint, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json; charset=utf8",
        Accept: "application/json",
        Authorization: localStorage.getItem(key),
      },
      body: JSON.stringify({
        description: description,
      }),
    });

    if (response.ok) {
      showToast("success", "Atividade atualizada com sucesso. Redirecionando para o painel...");

      window.setTimeout(function () {
        window.location = "/view/index.html";
      }, 2000);
    } else {
      showToast("danger", "Não foi possível atualizar a atividade. Verifique os dados e tente novamente.");
    }
  } catch (error) {
    console.error("Erro ao atualizar a atividade:", error);
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (!localStorage.getItem("Authorization"))
    window.location = "/view/login.html";
});