function ensureToastContainer() {
  let container = document.getElementById("toastContainer");
  if (!container) {
    container = document.createElement("div");
    container.id = "toastContainer";
    container.className = "toast-container position-fixed top-0 start-50 translate-middle-x p-3";
    document.body.appendChild(container);
  }
  return container;
}

function createToastElement(type, message) {
  const toastEl = document.createElement("div");
  toastEl.className = `toast text-bg-${type === "success" ? "success" : "danger"} border-0 mb-2`;
  toastEl.role = "alert";
  toastEl.ariaLive = "assertive";
  toastEl.ariaAtomic = "true";
  toastEl.innerHTML = `
    <div class="d-flex">
      <div class="toast-body">${message}</div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  `;
  return toastEl;
}

function showToast(type, message, delay = 3000) {
  const container = ensureToastContainer();
  const toastEl = createToastElement(type, message);
  container.appendChild(toastEl);

  const toast = new bootstrap.Toast(toastEl, { delay });
  toast.show();

  toastEl.addEventListener("hidden.bs.toast", () => {
    toastEl.remove();
  });
}
