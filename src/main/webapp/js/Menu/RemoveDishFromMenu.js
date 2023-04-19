function removeDishFromMenu(dishID, event) {
    let url = 'http://localhost:8888/ajaxController'
    let body = `command=remove_from_menu&&dishId=${dishID}`

    const promise = sendRequest(url, 'POST', body)
    promise.then((response) => onDishRemoved(response, dishID))
        .catch((response) => {
            var error = response.statusText
            window.location = `http://localhost:8888/errorPage?errorMsg=${error}`
        })
}

function onDishRemoved(response, dishID) {
    let errorMsgElement = document.querySelector('#errorMsg');
    let removedDishRow = document.querySelector(`#dish${dishID}row`)

    if (response.validationError) {
        if (removedDishRow.lastChild == errorMsgElement) {
            removedDishRow.removeChild(errorMsgElement);
        }
        errorMsgElement = document.createElement('h3');
        errorMsgElement.id = "errorMsg";
        errorMsgElement.innerHTML = response.message;

        removedDishRow.appendChild(errorMsgElement);
    } else {
        removedDishRow.remove()
    }
}