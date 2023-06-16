const express = require('express')
const Multer = require('multer')
const { registerHandler, loginHandler } = require('./reg-log-api/handler')
const { getAllNoteHandler, createNoteHandler, getNoteIdHandler, editNoteHandler, deleteNoteHandler, uploadimghandler, createNoteHandler2, cobapredict } = require('./crud-api/handler')
const router = express.Router()
const imgUpload = require ('./imgUploads/imgUpload')

const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 5 * 1024 * 1024
})

router.post('/register', registerHandler)

router.post('/login', loginHandler)

router.post('/notes',multer.single('image'), imgUpload.uploadToGcs, createNoteHandler)

router.get('/notes', getAllNoteHandler)

router.get('/notes/:noteId', getNoteIdHandler)

router.post('/notes/edit/:noteId', editNoteHandler)

router.delete('/notes/delete/:noteId', deleteNoteHandler)

router.post('/upload', multer.single('image'), imgUpload.uploadToGcs, uploadimghandler )

module.exports = router
