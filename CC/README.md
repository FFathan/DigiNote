##Here is the Deployed Link
(https://backend14-dot-diginote-final.et.r.appspot.com)

#Rest API for DigiNote
-  this is an API that conclude Register, Login and also Notes handler.
-  Please read carefully the methods in the routes.js
-   Handler.js concludes algorithm for the methods

##Install Requirements:
1. npm init --y (to create package.json file)
2. npm install

##How to deploy it?
1. You can deploy it with App engine
2. Edit your app.yaml data that include the version run time and service.
3. Change the database variable with your own (database requirement can be seen in the databasereq.txt .
4. Add this command to Cloud Shell if cloud shell error when you run local server.
```bash
npm cache clean --force
```
```bash
rm -rf node_modules
npm install
```
```bash
sudo apt-get install build-essential
```

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



--------------------------------------------------------------------------------
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



-------------------------------------------------------------------------------------
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
        "noteId": "id",
        "userId": "id",
        "title": "test dengan postman",
        "description": "iniadalahtextcobacoba",
        "imageUrl": "url",
        "updated": "2023-06-13T06:29:54.418Z"
    }


    
------------------------------------------------------------------------------------------------
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
            "description": "iniadalahtextcobacoba",
            "imageUrl": "url",
            "updated": "2023-06-12 16:13:15.330"
        },
        {
            "noteId": "id",
            "userId": "id",
            "title": "cobacoba",
            "description": "iniadalahtextcobacoba",
            "imageUrl": "url",
            "updated": "2023-06-12 16:13:21.632"
        }
    ]
}

-----------------------------------------------------------------------------------------------------------
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
        "imageUrl": "url",
        "updated": "2023-06-12 16:13:04.836"
    }
}

-----------------------------------------------------------------------------------------------------
EditNote
URL : /notes/edit/:noteId
Method : post
Request
  const { noteId } = req.params
  const { title, description } = req.body
  const authToken = req.headers.authorization

Response
{
    "error": false,
    "message": "Note updated!",
    "updatedNote": {
        "noteId": "id",
        "userId": "id",
        "title": "sudahdiubah",
        "description": "ini adalah catatan yang sudah diubah",
        "imageUrl": "url",
        "updated": "2023-06-13T12:30:44.042Z"
    }
}

------------------------------------------------------------------------------------

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

can you create read me md for this




