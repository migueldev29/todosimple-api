//Url de endpoint
const url = "http://localhost:8080/task/user/1";

function hideLoader() {
    //Altera a propriedade display para "none" do elemento "loading", tornando invisivel.
    document.getElementById("loading").style.display = "none";
}

function show(tasks) {
    //Cabeçalho da tabela
    let tab = `<thead>
            <th scope="col">#</th>
            <th scope="col">Description</th>
            <th scope="col">Username</th>
            <th scope="col">User Id</th>
        </thead>`;

    //Linhas de dados
    for (let task of tasks) {
        tab += `
            <tr>
                <td scope="row">${task.id}</td>
                <td scope="row">${task.description}</td>
                <td scope="row">${task.user.username}</td>
                <td scope="row">${task.user.id}</td>
            </tr>
            `;
    }

    //Insere a tabela carregada no corpo do HTML dentro do elemento com id = tasks
    document.getElementById("tasks").innerHTML = tab;
}

async function getAPI(url){
    //Executa a requisição na URL com método GET e recebe o retorno
    const response = await fetch(url, { method: "GET"});
    var data = await response.json();

    console.log(data);

    //Se tiver um retorno no json então esconder o Loader e mostrar os dados.
    if(response) hideLoader();
    show(data);
}

//Executa a função getAPI
getAPI(url);