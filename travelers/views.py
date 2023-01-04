from django.shortcuts import render, redirect
from django.http import HttpResponse
from .models import Traveler
from django.contrib.auth.models import User, auth


def home(request):
    return render(request, "home.html")


def account_info(request):
    return render(request, "workOnPage.html")


def my_requests(request):
    return render(request, "workOnPage.html")


def post_requests(request):
    return render(request, "postReq.html")


def flight_details(request):
    return render(request, "flightDetails.html")


def complaints(request):
    return render(request, "workOnPage.html")


def payments_and_refunds(request):
    return render(request, "workOnPage.html")


def shipping_address(request):
    return render(request, "workOnPage.html")


def track_request(request):
    return render(request, "workOnPage.html")


def pricing(request):
    return render(request, "workOnPage.html")


def support_tickets(request):
    return render(request, "workOnPage.html")


def sign_up(request):
    if request.method == "POST":
        username = request.POST["username"]
        email = request.POST["email"]
        phone = request.POST["phone"]
        password = request.POST["password"]
        user_type = request.POST["user_type"]

        new_user = Traveler.objects.create(
            username=username, email=email, phone=phone, password=password, user_type=user_type
        )
        new_user.save()
        return redirect("post_requests")
    else:
        return render(request, "signUp.html")


def sign_in(request):
    if request.method == "POST":
        email = request.POST["email"]
        password = request.POST["password"]

        try:
            user_details = Traveler.objects.get(email=email, password=password)
            request.session['email']=user_details.email
            return redirect("post_requests")
        except Traveler.DoesNotExist as e:
            return redirect("sign_in")
    else:
        return render(request, 'signIn.html')


def sign_out(request):
    try:
        del request.session['email']
    except:
        return render(request, 'home.html')
    return render(request, 'home.html')