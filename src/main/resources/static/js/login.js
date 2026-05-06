async function login() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
  
    console.log(username, password);
  
    const response = await fetch("http://localhost:8080/login", {
      method: "POST",
      headers: new Headers({
        "Content-Type": "application/json; charset=utf8",
        Accept: "application/json",
      }),
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    });
  
    let key = "Authorization";
    let token = response.headers.get(key);
    window.localStorage.setItem(key, token);
  
    if (response.ok) {
      showToast("success", "Login efetuado com sucesso. Redirecionando ao painel...");

      window.setTimeout(function () {
        window.location = "/index.html";
      }, 2000);
    } else {
      showToast("danger", "Não foi possível efetuar o login. Verifique suas credenciais e tente novamente.");
    }

  }