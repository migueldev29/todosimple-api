// Get task ID from URL
const params = new URLSearchParams(window.location.search);
const taskId = params.get('id');

const taskIdDisplay = document.getElementById('task-id-display');

if (taskId) {
  taskIdDisplay.textContent = `Task ID: ${taskId}`;
} else {
  taskIdDisplay.textContent = 'No task ID provided in the URL';
}

async function updateTask() {
  const description = document.getElementById('description').value;
  const endpoint = "http://localhost:8080/task/" + taskId;
  
  if (!description) {
    alert('Description cannot be empty!');
    return;
  }

  let key = "Authorization";

  const response = await fetch(endpoint, {
    method: "PUT",
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
    showToast("#okToast");

    window.setTimeout(function () {
      window.location = "/view/index.html";
    }, 2000);
  } else {
    showToast("#errorToast");
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