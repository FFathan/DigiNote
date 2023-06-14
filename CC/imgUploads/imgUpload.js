'use strict'
const {Storage} = require('@google-cloud/storage')
const fs = require('fs')
const path = require('path');

const pathKey = path.resolve('./testing-diginote-2023-a9ca4c63bb0a.json')

// TODO: Sesuaikan konfigurasi Storage
const gcs = new Storage({
    projectId: 'diginote-final',
    keyFilename: pathKey
})

// TODO: Tambahkan nama bucket yang digunakan
const bucketName = 'digi-photos-bucket'
const bucket = gcs.bucket(bucketName)

function getPublicUrl(filename) {
    return 'https://storage.googleapis.com/' + bucketName + '/' + filename;
}

let ImgUpload = {}

ImgUpload.uploadToGcs = (req, res, next) => {
    if (!req.file) return next()

    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(currentDate.getDate()).padStart(2, '0');
    const hours = String(currentDate.getHours()).padStart(2, '0');
    const minutes = String(currentDate.getMinutes()).padStart(2, '0');
    const seconds = String(currentDate.getSeconds()).padStart(2, '0');
    const gcsname = `${year}${month}${day}-${hours}${minutes}${seconds}`;
    const file = bucket.file(gcsname);

    const stream = file.createWriteStream({
        metadata: {
            contentType: req.file.mimetype
        }
    })

    stream.on('error', (err) => {
        req.file.cloudStorageError = err
        next(err)
    })

    stream.on('finish', () => {
        req.file.cloudStorageObject = gcsname
        req.file.cloudStoragePublicUrl = getPublicUrl(gcsname)
        next()
    })

    stream.end(req.file.buffer)
}

module.exports = ImgUpload