let count;

function addOne(event, categoryID, dishID) {
    event.preventDefault();

    let inputQuantity = document.querySelector(`#quantityOf${dishID}_${categoryID}`);

    count = +inputQuantity.value;

    inputQuantity.value = ++count;
}

function reduceOne(event, categoryID, dishID) {
    event.preventDefault();

    let inputQuantity = document.querySelector(`#quantityOf${dishID}_${categoryID}`);

    count = +inputQuantity.value;

    if (--count < 1) {
        count = 1;
    }
    inputQuantity.value = count;
}

function validate(evt) {
    var theEvent = evt || window.event;
    var key = theEvent.keyCode || theEvent.which;
    key = String.fromCharCode(key);
    var regex = /^[\d]+$/

    if (!regex.test(key)) {
        theEvent.returnValue = false;
        if (theEvent.preventDefault) theEvent.preventDefault();
    }
}
