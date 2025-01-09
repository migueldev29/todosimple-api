//Url de endpoint
const tasksEndpoint = "http://localhost:8080/task/user";

function hideLoader() {
    //Altera a propriedade display para "none" do elemento "loading", tornando invisivel.
    document.getElementById("loading").style.display = "none";
}

function show(tasks) {
    //Cabeçalho da tabela
    let tab = `<thead>
                    <th scope="col"></th>
                    <th scope="col"></th>
                    <th scope="col">Id</th>
                    <th scope="col">Description</th>
                </thead>`;

    //Linhas de dados
    for (let task of tasks) {
    tab += `
            <tr>
                <td><button type="button" class="btn btn-primary" onclick="window.location = '/view/updatetask.html?id=${task.id}'"><i class="bi bi-pencil-fill"></i>Update</button>
                <td><button type="button" class="btn btn-outline-danger" onclick="deleteTask(this)"><i class="bi bi-x-circle-fill"></i>Delete</button></td>
                <td scope="row" id="taskId">${task.id}</td>
                <td>${task.description}</td>
            </tr>
        `;
  }
  //Insere a tabela carregada no corpo do HTML dentro do elemento com id = tasks
  document.getElementById("tasks").innerHTML = tab;
}

async function getTasks() {
    //Obtém token de autorização e executa a requisição na URL com método GET e recebe o retorno

    let key = "Authorization";
    const response = await fetch(tasksEndpoint, {
        method: "GET",
        headers: new Headers({
            Authorization: localStorage.getItem(key),
        }),
    });

    var data = await response.json();
    console.log(data);

    if (response) hideLoader();
        show(data);
}

document.addEventListener("DOMContentLoaded", function (event) {
    if (!localStorage.getItem("Authorization"))
        window.location = "/view/login.html";
});

//Executa a função getTasks
getTasks();