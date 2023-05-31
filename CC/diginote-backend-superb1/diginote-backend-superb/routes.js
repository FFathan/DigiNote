const express = require('express')
const { registerHandler, loginHandler } = require('./reg-log-api/handler')
const router = express.Router()

router.post('/register', registerHandler)
router.post('/login', loginHandler)

module.exports = router
