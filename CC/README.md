Rest API for DigiNote
1. this is an API that conclude Register, Login and also Notes handler.
2. Please read carefully the methods in the routes.js
3. handler.js concludes algorithm for the methods

Install Requirements:
1. npm init --y (to create package.json file)
2. npm install

How to deploy it?
1. You can deploy it with App engine
2. Edit your app.yaml data that include the version run time and service.
3. Change the database variable with your own (database requirement can be seen in the databasereq.txt .
4. Add this command to Cloud Shell if cloud shell error when you run local server.

npm cache clean --force

rm -rf node_modules
npm install

sudo apt-get install build-essential

--------------------------------------------------------
Methods:

Register Accout
URL: /register
Method: POST
Request Body :

-username as varchar

-email as varchar

password as varchar, must be at least 8 characters, Uppercase, number and special characters

Response
{
	“error” : false,
	“message” : “Account registered successfully”
}

LOGIN
URL: /login

Method: post

Request Body:

usernameORemail as String
password as String

Response
{
	“error” : “false”,
	“message” : “Login Succeed”
    	“token” : “randomgeneratenumbersandalphabet”
}

CREATE NOTE
URL : /notes

Methods : post

Headers.authorization
authToken

Request body
image as file ,  pakai form data
title as string

Response
{
    "error": false,
    "message": "Note Created!",
    "note": {
        "noteId": "7e7f83dd-d614-4f34-a967-eeb2f7cf6353",
        "userId": "6",
        "title": "test dengan postman",
        "description": "iniadalahtextcobacoba",
        "imageUrl": "https://storage.googleapis.com/digi-photos-bucket/20230613-062953",
        "updated": "2023-06-13T06:29:54.418Z"
    }

GetAllNotes
URL : /notes
Methods : get
Headers.authorization
authToken
Response berhasil: 
{
    "error": false,
    "message": "All Notes retrieved",
    "listnote": []
        {
            "noteId": "feaf01db-e644-4038-be94-ed8a77efcf8a",
            "userId": "1",
            "title": "cobacoba",
            "description": "iniadalahtextcobacoba",
            "imageUrl": "https://storage.googleapis.com/digi-photos-bucket/20230612-161303",
            "updated": "2023-06-12 16:13:04.836"
        },
        {
            "noteId": "2889cf55-6345-4629-9e16-7b9d725ac949",
            "userId": "1",
            "title": "cobacoba",
            "description": "iniadalahtextcobacoba",
            "imageUrl": "https://storage.googleapis.com/digi-photos-bucket/20230612-161314",
            "updated": "2023-06-12 16:13:15.330"
        },
        {
            "noteId": "f0d9c98c-abca-4d22-8e22-cd6c572c2477",
            "userId": "1",
            "title": "cobacoba",
            "description": "iniadalahtextcobacoba",
            "imageUrl": "https://storage.googleapis.com/digi-photos-bucket/20230612-161320",
            "updated": "2023-06-12 16:13:21.632"
        }
    ]
}


GetNotesbyId
URL : /notes/:noteId
Methods : get
Request params
noteId
Request headers authorization
authToken
Response berhasil
{
    "error": false,
    "message": "Note retrieved",
    "note": {
        "noteId": "feaf01db-e644-4038-be94-ed8a77efcf8a",
        "userId": "1",
        "title": "cobacoba",
        "description": "iniadalahtextcobacoba",
        "imageUrl": "https://storage.googleapis.com/digi-photos-bucket/20230612-161303",
        "updated": "2023-06-12 16:13:04.836"
    }
}


EditNote
URL : /notes/edit/:noteId
Method : post
Request
  const { noteId } = req.params
  const { title, description } = req.body
  const authToken = req.headers.authorization

Response berhasil: Udah dibenerin? {sudah}
{
    "error": false,
    "message": "Note updated!",
    "updatedNote": {
        "noteId": "1bbf2451-0db4-41e6-a59d-e8449858ac1d",
        "userId": "6",
        "title": "sudahdiubah",
        "description": "ini adalah catatan yang sudah diubah",
        "imageUrl": "https://storage.googleapis.com/digi-photos-bucket/20230613-061044",
        "updated": "2023-06-13T12:30:44.042Z"
    }
}





DELETE NOTE
URL : /notes/delete/:noteId
Method : delete
Request: 
  const { noteId } = req.params;
  const authToken = req.headers.authorization;

Response berhasil
{
    "success": true,
    "message": "Note deleted successfully."
}





