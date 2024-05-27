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
        location.reload();
    } else {
        document.getElementsByClassName("login-error")[0].hidden = false;
    }
}

function fileUpload() {
    const fileField = document.getElementById('poster-upload')
    const formData = new FormData();
    formData.append("file", fileField.files[0]);
    fetch('/admin/images', {
        method: 'POST',
        body: formData
    })
        .then((response) => response.json())
        .then(jsonResponse => {
            const movieCover = document.getElementById('movieCover');
            movieCover.src = `/img/${jsonResponse.fileName}`;
            movieCover.hidden = false;
            const poster = document.getElementById('poster');
            poster.value = jsonResponse.fileName;
        });
}