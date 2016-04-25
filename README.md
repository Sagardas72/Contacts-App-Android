# Contacts-App-Android

This is an App which stores details of your Contacts.

It provides you the functionality of associating a photo with the contact, but the photo has to be clicked. Accessing the Photo Gallery will be done in the later commits.
For clicking a photo:
a) Click on the Toggle Button to enable field editing
b) Click on the Image Icon besides the Contact Name field
c) Click the image once the camera opens
d) Click on the Save Button to save the image

The Address of the contact can be viewed using Google Maps, for which the google play services are used in the app.
a) To access Google Maps from the App, first go to https://console.developers.google.com/, create a project, get the API Key and finally enable the Google Map API for the project
b) Go to Android-Manifest, and in <meta-data>, add the API Key
c) Run the App.
d) If the App contains Contacts, and you navigate to Maps Activity, it will show you the location of all the Contacts. Otherwise if you select a particular activity from the list, and then navigate to the Map Icon, it will show you the location of that particular contact.

The App provides usage of all the features for an App, which includes the Call Functionality, Battery Level Indicator, Accelerometer and Magnetometer Sensors, Proximity Sensors, Custom Adapter, SharedPreferences, connection to SQLite, apart from the two features mentioned above.
