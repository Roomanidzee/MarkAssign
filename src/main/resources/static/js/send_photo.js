function sendFile(file){

    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();

    xhr.open("POST", "/storage", true);
    xhr.send(formData);

    alert("Фото успешно отправлено!");

}