async function signup() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
  
    console.log(username, password);
  
    const response = await fetch("http://localhost:8080/user", {
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
  
    if (response.ok) {
      showToast("success", "Cadastro realizado com sucesso. Redirecionando para login...");
      setTimeout(() => {
        window.location.href = "login.html";
      }, 2000);
    } else {
      showToast("danger", "Não foi possível cadastrar o usuário. Verifique os dados e tente novamente.");
    }
  }