const express = require('express')
const { registerHandler, loginHandler } = require('./reg-log-api/handler')
const { getAllNoteHandler, createNoteHandler, getNoteIdHandler, editNoteHandler, deleteNoteHandler } = require('./crud-api/handler')
const router = express.Router()

router.post('/register', registerHandler)
router.post('/login', loginHandler)
router.post('/notes', createNoteHandler)
router.get('/notes', getAllNoteHandler)
router.get('/notes/:id', getNoteIdHandler)
router.post('/notes/edit', editNoteHandler)
router.delete('/notes/delete', deleteNoteHandler)

module.exports = router
