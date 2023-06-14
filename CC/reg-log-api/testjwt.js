const jwt = require('jsonwebtoken')
const secret = "diginote-secret"

token = 'your-token'
const decoded = jwt.decode(token)

const authToken = jwt.verify(token, secret)
console.log ({decoded, authToken})
