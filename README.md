# MPI2_2nd_practical
## Maps activity
Before using the app an API key is required so that the Google Maps fragment displays. The API key can be obtained on the Google Cloud Platform by creating a project in the “Credentials” section and then creating new credentials. The maps SDK for Android has to be enabled as well.

<p align="center">
  <img src="preview/picture1.png?raw=true" />
</p>

### Location
Maps activity initially displays a Google Maps fragment with an options menu on the top right that leads to the Repository Activity. If it’s the first time the app is opened then it will ask for permission to access the devices location.

<p align="center">
  <img src="preview/picture2.png?raw=true" />
</p>

### Markers
The primary marker to display the users exact position on the map is an icon of a green person and depending on the location, multiple nearby towns and cities are automatically marked. All the information (name, description and coordinates) is stored in the strings.xml file. Currently there is only information about the larger cities and towns in the Vidzeme region of Latvia. The string XML tags are later used in Java code to create markers on the map which can be selected to display a dialog popup about the city or town.

<p align="center">
  <img src="preview/picture3.png?raw=true" />
  <img src="preview/picture4.png?raw=true" />
</p>

## Reader activity
Reader activity demonstrates a request to web based API, in this case from https://newsapi.org/. When this activity is opened, in this case, it creates 5 requests to newsapi.org searching for JSON arrays that contain the keyword “Ukraine” and extracts information about titles, descriptions, urls, images, authors and the published dates of the articles. The returned data is loaded in a listview as seen in the images below.

<p align="center">
  <img src="preview/picture5.jpg?raw=true" />
  <img src="preview/picture6.jpg?raw=true" />
</p>
