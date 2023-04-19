var quantityInp;

function addToOrder(el, categoryID, dishID) {
    quantityInp = document.querySelector(`#quantityOf${dishID}_${categoryID}`);
    const url = "http://localhost:8888/ajaxController";
    const body = `command=add_to_order&dish_id=${dishID}&quantity=${quantityInp.value}`;

    sendRequest(url, 'POST', body)
        .then((response) => { onDishAdded(response, el) })
        .catch((msg) => { alert(msg) })
}

function onDishAdded(response, addToOrderBtn) {
    if (response.validationError) {
        alert(response.message)
    } else {
        let addedSpan = document.querySelector('#dishAddedMsg');
        let td4 = addToOrderBtn.parentElement;

        let quantityOfDishesSpan = document.querySelector('#quantityOfDishes')
        let oldCount = quantityOfDishesSpan.innerHTML
        quantityOfDishesSpan.innerHTML = Number(oldCount) + Number(quantityInp.value)

        if (addedSpan != addToOrderBtn.nextElementSibling) {
            addedSpan.remove()

            addedSpan = document.createElement('span');
            addedSpan.id = "dishAddedMsg"
            addedSpan.innerHTML = "<br> <div style=\"color: rgb(27, 167, 27);\">&#10004</div>";

            td4.appendChild(addedSpan);
        } else if (addToOrderBtn.nextElementSibling == null) {
            addedSpan = document.createElement('span');
            addedSpan.id = "dishAddedMsg"
            addedSpan.innerHTML = "<br> <div style=\"color: rgb(27, 167, 27);\">&#10004</div>";

            td4.appendChild(addedSpan);
        }
    }
}
