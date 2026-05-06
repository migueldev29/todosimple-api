async function createTask() {
  let description = document.getElementById("description").value;
  let key = "Authorization";

  if (!description) {
    showToast("danger", "Descrição não pode ser vazia!"); // Exibe o toast de erro
    return; // Interrompe a execução da função
  }

  const response = await fetch("http://localhost:8080/task", {
    method: "POST",
    headers: new Headers({
      "Content-Type": "application/json; charset=utf8",
      Accept: "application/json",
      Authorization: localStorage.getItem(key),
    }),
    body: JSON.stringify({
      description: description,
    }),
  });

  if (response.ok) {
    showToast("success", "Atividade criada com sucesso. Redirecionando para o painel...");

    window.setTimeout(function () {
      window.location = "/index.html";
    }, 2000);
  } else {
    showToast("danger", "Não foi possível criar a atividade. Verifique os dados e tente novamente.");
  }
}

document.addEventListener("DOMContentLoaded", function (event) {
  if (!localStorage.getItem("Authorization"))
      window.location = "/login.html";
});