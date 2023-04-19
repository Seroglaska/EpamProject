# Restaurant
Restaurant system. The Client makes an order from the Menu. The administrator confirms the Order and sends it to the kitchen for execution. The Administrator issues an Invoice. The client makes his payment.

## Roles
### Guest available funtionality:
+ log in | sign up
+ view dishes on the menu
+ add dishes to the order
+ view dishes added to the order
+ change locale (EN|RU)
### User available functionality(in additional to guests' posibilities):
+ log out
+ view and edit profile
+ get a list of orders that are being processed by the administrator
+ get the order history
### Cook available functionality(in additional to users' posibilities):
+ mark the status "cooked" for ready orders
### Admin available functionality(in additional to users' posibilities):
+ confirm orders placed
+ change the information of a registered user
+ change the role of a registered user
+ edit | add | delete dishes from the order
+ edit | add | delete categories of dishes

### Database scheme

<p align="center">
  <img src="https://github.com/Bobrv3/restaurant/blob/main/db_scheme.png" width="1000" title="hover text">
</p>