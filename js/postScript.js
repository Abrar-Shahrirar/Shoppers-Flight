// get selected row
 // display selected row data in text input
            
var table = document.getElementById("table"),rIndex;
            
for(var i = 0; i <=table.rows.length; i++){
    table.rows[i].onclick = function(){
        rIndex = this.rowIndex;
        console.log(rIndex);

        document.getElementById("avac").value = this.cells[0].innerHTML;
        document.getElementById("mobile").value = this.cells[1].innerHTML;
        document.getElementById("laptops").value = this.cells[2].innerHTML;
        document.getElementById("clothing").value = this.cells[3].innerHTML;
        document.getElementById("chocolates").value = this.cells[4].innerHTML;
    };
}
            
            
// edit the row
function editRow(){
    table.rows[rIndex].cells[0].innerHTML = document.getElementById("avac").value;
    table.rows[rIndex].cells[1].innerHTML = document.getElementById("mobile").value;
    table.rows[rIndex].cells[2].innerHTML = document.getElementById("laptops").value;
    table.rows[rIndex].cells[3].innerHTML = document.getElementById("clothing").value;
    table.rows[rIndex].cells[4].innerHTML = document.getElementById("chocolates").value;
}

// Data Update Table Here
function editTableDisplay(){
    document.querySelector('.editTable').setAttribute('style', 'display: block;')
}