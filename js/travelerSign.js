
// Get the modal
var modal = document.getElementById('id01');


// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }

}

// Get the modal
var modal1 = document.getElementById('id02');

// When the user clicks anywhere outside of the modal, close it


function formValidation()
{
var uemail = document.registration.email;
var passid = document.registration.passid;

if(ValidateEmail(uemail))
{ 
if(passid_validation(passid,6,12))
{  
}
}
return false;
}
function ValidateEmail(uemail)
{
var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
if(uemail.value.match(mailformat))
{
return true;
}
else
{
alert("You have entered an invalid email address!");
uemail.focus();
return false;
}
} 

function passid_validation(passid,mx,my)
{
var passid_len = passid.value.length;
if (passid_len == 0 ||passid_len >= my || passid_len < mx)
{
alert("Password should not be empty / length must be between "+mx+" to "+my);
passid.focus();
return false;
}
alert('Form Succesfully Submitted');
window.location.href= 'index.html'
return true;
}


function formValidation1()
{
var uemail = document.registration1.email;
var passid = document.registration1.passid;

if(ValidateEmail(uemail))
{ 
if(passid_validation(passid,6,12))
{  
}
}
return false;
}
function ValidateEmail(uemail)
{
var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
if(uemail.value.match(mailformat))
{
return true;
}
else
{
alert("You have entered an invalid email address!");
uemail.focus();
return false;
}
} 

function passid_validation(passid,mx,my)
{
var passid_len = passid.value.length;
if (passid_len == 0 ||passid_len >= my || passid_len < mx)
{
alert("Password should not be empty / length must be between "+mx+" to "+my);
passid.focus();
return false;
}
alert('Form Succesfully Submitted');
window.location.href= 'index.html'
return true;
}



