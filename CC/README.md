## Here is the Deployed Link
(https://backend14-dot-diginote-final.et.r.appspot.com)
# Rest API for DigiNote
-  this is an API that conclude Register, Login and also Notes handler.
-  Please read carefully the methods in the routes.js
-   Handler.js concludes algorithm for the methods

## Install Requirements:
- npm init --y (to create package.json file)
- npm install

## How to deploy it?
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
## Methods:

### Register Accout
1. URL: /register
2. Method: POST
3. Request Body :
   - username as varchar
   - email as varchar
   - password as varchar, must be at least 8 characters, Uppercase, number and special characters
```json
Response
{
	“error” : false,
	“message” : “Account registered successfully”
}
```


--------------------------------------------------------------------------------
## LOGIN
1. URL: /login
2. Method: post
3. Request Body:
   - usernameORemail as String
   - password as String
```json
Response
{
	“error” : “false”,
	“message” : “Login Succeed”
    	“token” : “randomgeneratenumbersandalphabet”
}
```


-------------------------------------------------------------------------------------
##CREATE NOTE
1. URL : /notes
2. Methods : post
3. Headers.authorization
   - authToken
4. Request body
   - image as file ,  pakai form data
   - title as string
```json
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
```

    
------------------------------------------------------------------------------------------------
### GetAllNotes
1. URL : /notes
2. Methods : get
3. Headers.authorization
   - authToken
```json
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
```
-----------------------------------------------------------------------------------------------------------
## GetNotesbyId
1. URL : /notes/:noteId
2. Methods : get
3. Request params
   - noteId
4. Request headers authorization
   - authToken
```json
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
```
-----------------------------------------------------------------------------------------------------
### EditNote
1. URL : /notes/edit/:noteId
2. Method : post
3. Request
   - const { noteId } = req.params
   - const { title, description } = req.body
   - const authToken = req.headers.authorization
```json
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
```
------------------------------------------------------------------------------------

### DELETE NOTE
1. URL : /notes/delete/:noteId
2. Method : delete
3. Request:
   - const { noteId } = req.params;
   - const authToken = req.headers.authorization;
```json
Response berhasil
{
    "success": true,
    "message": "Note deleted successfully."
}
```




