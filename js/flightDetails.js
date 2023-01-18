function w3_open() {
  document.getElementById("main").style.marginLeft = "25%";
  document.getElementById("mySidebar").style.width = "25%";
  document.getElementById("mySidebar").style.display = "block";
  document.getElementById("openNav").style.display = 'none';
}
function w3_close() {
  document.getElementById("main").style.marginLeft = "0%";
  document.getElementById("mySidebar").style.display = "none";
  document.getElementById("openNav").style.display = "inline-block";
}


function formValidation()
{
var ucountry = document.registration.country;
var ocountry = document.registration.Ocountry;
var uemail = document.registration.email;

if(countryselect(ucountry))
{
if(ocountryselect(ocountry))
{
if(ValidateEmail(uemail))
{ 
}
}
}
return false;

}

function countryselect(ucountry)
{
if(ucountry.value == "Default")
{
alert('Select your destination country from the list');
ucountry.focus();
return false;
}
else
{
return true;
}
}
function ocountryselect(ocountry)
{
if(ocountry.value == "Default")
{
alert('Select your origin country from the list');
ocountry.focus();
return false;
}
else
{
return true;
}
}

function ValidateEmail(uemail)
{
var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
if(uemail.value.match(mailformat))
{
alert('Form Succesfully Submitted');
window.location.href= 'postReq.html'
return true;
}
else
{
alert("You have entered an invalid email address!");
uemail.focus();
return false;
}
} 
