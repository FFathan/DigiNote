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
```json
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




