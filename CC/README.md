DigiNote REST API
The DigiNote REST API provides endpoints for user registration, login, and managing notes. It allows users to register an account, authenticate with their credentials, create, retrieve, update, and delete notes.

Installation
To set up the DigiNote API locally, follow these steps:

Clone the repository:

bash
Copy code
git clone <repository_url>
Navigate to the project directory:

bash
Copy code
cd DigiNote-API
Install the required dependencies:

bash
Copy code
npm install
Deployment
To deploy the DigiNote API, you can use the App Engine service on Google Cloud Platform. Here's a summary of the deployment process:

Configure your app.yaml file:

Update the runtime and service fields according to your requirements.
Set up your database:

Refer to the databasereq.txt file for the database requirements.
Update the database variable in the code with your database configuration.
Deploy the API to Google Cloud App Engine.

bash
Copy code
gcloud app deploy
Access the deployed API using the provided URL.

API Documentation
Register Account
URL: /register
Method: POST
Request Body:
username (string)
email (string)
password (string) - Must be at least 8 characters and contain uppercase letters, numbers, and special characters.
Response:
json
Copy code
{
  "error": false,
  "message": "Account registered successfully"
}
Login
URL: /login
Method: POST
Request Body:
usernameORemail (string)
password (string)
Response:
json
Copy code
{
  "error": "false",
  "message": "Login Succeed",
  "token": "randomgeneratenumbersandalphabet"
}
Create Note
URL: /notes
Method: POST
Headers:
Authorization: authToken
Request Body:
image (file) - Use form-data for uploading.
title (string)
Response:
json
Copy code
{
  "error": false,
  "message": "Note Created!",
  "note": {
    "noteId": "id",
    "userId": "id",
    "title": "test dengan postman",
    "description": "iniadalahtextcobacoba",
    "imageUrl": "url",
    "updated": "2023-06-13T06:29:54.418Z"
  }
}
Get All Notes
URL: /notes
Method: GET
Headers:
Authorization: authToken
Response:
json
Copy code
{
  "error": false,
  "message": "All Notes retrieved",
  "listnote": [
    {
      "noteId": "id",
      "userId": "id",
      "title": "cobacoba",
      "description": "iniadalahtextcobacoba",
      "imageUrl": "url",
      "updated": "2023-06-12 16:13:04.836"
    },
    {
      "noteId": "id",
      "userId": "id",
      "title": "cobacoba",
      "description": "iniadalahtextcobacoba





