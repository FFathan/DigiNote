const jwt = require('jsonwebtoken')
const secret = "diginote-secret"

token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIsImlhdCI6MTY4NjUyMzQzNn0.0InSB5rDXhw0-EnBI2rRYxqSZ33X--ky1KCQpUf48mE'
const decoded = jwt.decode(token)

const authToken = jwt.verify(token, secret)
console.log ({decoded, authToken})
