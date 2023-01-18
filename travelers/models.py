from django.db import models

class TravelersPost(models.Model):
    flight_date = models.DateField()
    arrival_date = models.DateField()
    destination_country = models.CharField(max_length=50)
    origin_country = models.CharField(max_length=50)
    passport_no = models.IntegerField()
    ticket_no = models.IntegerField()
    airlines_name = models.CharField(max_length=50)
    email = models.EmailField()
    contact_no = models.IntegerField()