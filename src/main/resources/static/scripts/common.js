const toggle = document.querySelector(".menu-toggle");
const menu = document.querySelector(".menu");

function toggleMenu() {
    if (menu.classList.contains("expanded")) {
        menu.classList.remove("expanded");
        toggle.querySelector('a').innerHTML = '<i id="toggle-icon" class="far fa-plus-square"></i>';
    } else {
        menu.classList.add("expanded");
        toggle.querySelector('a').innerHTML = '<i id="toggle-icon" class="far fa-minus-square"></i>';
    }
}

toggle.addEventListener("click", toggleMenu, false);

function showLoginDialog() {
    const dialog = document.querySelector("dialog");
    dialog.addEventListener("cancel", (event) => {
        closeDialog(dialog);
    });
    const closeDialogButton = dialog.getElementsByClassName("close")[0];
    closeDialogButton.addEventListener("click", (event) => {
        closeDialog(dialog);
    });
    dialog.showModal();
}

function closeDialog(dialog) {
    dialog.remove()
}

function processLogin(event) {
    if (event.detail.successful) {
        console.log('login success');
        location.reload();
    } else {
        console.log("login failed");
        document.getElementsByClassName("login-error")[0].hidden = false;
    }
}