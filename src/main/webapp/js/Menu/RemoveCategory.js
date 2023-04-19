function removeCategory(categoryID, event) {
    let url = 'http://localhost:8888/ajaxController'
    let body = `command=remove_category&categoryId=${categoryID}`

    const promise = sendRequest(url, 'POST', body)
    promise.then((response) => onCategoryRemoved(response, categoryID))
        .catch((response) => {
            var error = response.statusText
            window.location = `http://localhost:8888/errorPage?errorMsg=${error}`
        })
}

function onCategoryRemoved(response, categoryID) {
    let errorMsgElement = document.querySelector('#errorMsg');
    let categoryNameH = document.querySelector(`#category${categoryID}`)

    if (response.validationError) {
        if (categoryNameH.lastChild == errorMsgElement) {
            categoryNameH.removeChild(errorMsgElement);
        }
        errorMsgElement = document.createElement('div');
        errorMsgElement.id = "errorMsg";
        errorMsgElement.innerHTML = response.message;

        categoryNameH.appendChild(errorMsgElement);
    } else {
        dishesTableFromCategory = categoryNameH.nextElementSibling

        categoryNameH.remove()
        dishesTableFromCategory.remove()
    }
}