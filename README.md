# Project specification

The task is to create a software support for an information system of a taxi service. This application needs to have 3 types (roles) of users: **clients**, **dispatchers(admins)** and **drivers**. Application entities are described by the following data:

- User
  - Username (unique)
  - Password
  - First name
  - Last name
  - Gender
  - Phone number
  - Email (unique)
  - Role
- Driver (User)
  - Location
  - Vehicle
  - Status (not working, on ride, waiting for ride)
- Client (User)
- Dispatcher (User)
- Location
  - Latitude
  - Longitude
- Ride
  - Date and time of the order
  - Start location (location on which a taxi will arrive)
  - Desired type of a vehicle (default type is car)
  - Client which ordered ride (only if the client initiated the ride creation)
  - Destination (location on which the ride is successfully finished, driver is responsible for updating this value)
  - Dispatcher (the dispatcher that formed or processed the ride, if a driver was the one who accepted the ride, this field is  
    empty)
  - Driver (the driver that accepted a ride or to whom a ride was assigned by a dispatcher)
  - Price
  - Status (created, formed, processed, accepted, cancelled, failed, successful)
  - Rating (value from 1 to 5, 0 value is interpreted as if a client didn't rate a ride)
- Vehicle
  - Year of manufacturing
  - Registration number
  - Vehicle type (company has cars and vans in its service)
- Comment
  - Text
  - Date of posting
  - Client that posted a comment
  - Ride on which a comment was posted
- Report
  - Text
  - Date of posting
  - Driver that created a report
  - Ride for which a report is written
- Ride status
  - Created (initial status of a ride when it's created by a client)
  - Cancelled (a ride that was in *created* status and then a client cancelled it for some reason)
  - Formed (initial status of a ride when it's created by a dispatcher)
  - Processed (a ride that was in *created* status and then a dispatcher assigned it to a driver)
  - Accepted (a ride that was in *created* status and a driver self initiatively took it)
  - Failed (a ride that was in *formed*, *processed* or *accepted* status, but a driver didn't successfully transported a client)
  - Successful (a ride that was in *formed*, *processed* or *accepted* status, and a driver successfully transported a client)

Implement following functionalities:
- Registration - clients are registered by filling registration form.
- Administrators (dispatchers) are read from a database and can't be added later. Drivers can be created only by dispatchers.
- Login - user is logged in by providing username and password. User can performs activities that are defined by his role.
- All users can see their profiles and change their personal data.
- Drivers can update their current location.
- Clients can order ride by providing starting location and required vehicle type(optional). Status of a ride is set to *created*.
- Clients can update or cancel (ride status is set to *canceled*) a ride as long as the ride is in *created* status. Canceling a ride is an unavailable         functionality for a ride when it's formed by a dispatcher.
- Dispatchers can form and process rides.
  - When a dispatcher forms a ride, the following data needs to be provided:
    - Location on which the taxi is coming,
    - Vehicle type,
    - Available driver which will be assigned to the ride,
    - A dispatcher that formed the ride.
  - If a client orders a ride and none of drivers accepted the ride, a dispatcher can assign this ride to any of free drivers.
- Drivers can change a status of the ride to *failed* or *successful* status.
  - If a driver changes the ride status to *failed*, data for *destination* and *price* are not entered. When ride status is changed to *failed*, a driver needs to write a report (to describe why the ride has failed).
  -If the driver changes the status of the ride to *Successful*, data about the *Destination* and *Price* of the ride are entered.
- When a ride ends, a client can post a comment about a ride.
- Clients can only see own rides.
- Dispatchers can see a list of rides in which they participated. Also, they can see all the rides in the system.
- Driver can see a list of rides in which they participated. Also, they can see all rides in the system that are in *created* status.
- Filtering - User can filter rides by status of a ride.
- Sorting - User can choose sorting by:
  - Date (newest)
  - Rating (from highest to lowest)
- Search criteria - User can search the rides by:
  - Date of order (from, to, from - to)
  - Rating (from, to, from - to)
  - Price (from, to, from - to)
- Search criteria for rides that's available only for dispatchers
  - by name or lastname of the driver
  - by name or lastname of the client
