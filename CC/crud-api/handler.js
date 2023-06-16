const jwt = require('jsonwebtoken')
const { v4: uuidv4 } = require('uuid');
const axios = require('axios')
const pool =  require('./database')
secretKey = "diginote-secret"

const uploadimghandler = (req,res,next) => {
  const data = req.body
  if (req.file && req.file.cloudStoragePublicUrl){
    data.imageUrl = req.file.cloudStoragePublicUrl
  }
  const urlphoto = data.imageUrl
  res.status(200).json({error: true, message :" data", data, urlphoto})
}


const createNoteHandler = async (req, res) => {
    const data = req.body;
    const title = req.body.title
    const description = req.body.description
    const authToken = req.headers.authorization;
    const secretKey = "diginote-secret"
  
    if (!title || !data) {
      return res.status(400).json({ error: true, message: 'input the title and image' });
    }
  
    try {
      jwt.verify(authToken, secretKey);
    } catch (error) {
      return res.status(401).json({ error: true, message: 'Unauthorized. Invalid token.' });
    }
  
    let decode = jwt.verify(authToken, secretKey);
    let decoded = decode.userId
    let userId = decoded.toString()
  
  
    const extractedText = description
    let urlphoto;
    if (req.file && req.file.cloudStoragePublicUrl){
      data.imageUrl = req.file.cloudStoragePublicUrl
      urlphoto = data.imageUrl
    }
  
    const url = urlphoto
    const noteId = uuidv4();
    const date = new Date();
    const options = { timeZone: 'Asia/Jakarta' };
    
    const dayOfWeek = date.toLocaleString('en-US', { ...options, weekday: 'long' });
    
    const day = date.toLocaleString('en-US', { ...options, day: 'numeric' });
    const month = date.toLocaleString('en-US', { ...options, month: 'long' });
    const year = date.toLocaleString('en-US', { ...options, year: 'numeric' });
    
    const hours = date.toLocaleString('en-US', { ...options, hour: 'numeric', hour12: false });
    const minutes = date.toLocaleString('en-US', { ...options, minute: 'numeric' });
    const seconds = date.toLocaleString('en-US', { ...options, second: 'numeric' });
    
    const formattedDateTime = `${dayOfWeek}, ${day} ${month} ${year}, ${hours}:${minutes}:${seconds}`;
    
    const note = {
      noteId,
      userId,
      title,
      description: extractedText,
      imageUrl : url,
      updated: formattedDateTime,
    };
  
    pool.query(
      'INSERT INTO notes (noteId, userId, title, description , imageUrl, updated) VALUES (?, ?, ?, ?, ?, ?)',
      [noteId, userId, title, note.description, note.imageUrl, note.updated],
      (error) => {
        if (error) {
          console.error('Error inserting note:', error);
          return res.status(500).json({ error: true, message: 'An error occurred while creating the note.' });
        }
  
        res.status(201).json({error : false, message: 'Note Created!', note});
      }
    );
  };


const getAllNoteHandler = (req,res) => {
  const authToken = req.headers.authorization
  //const {page, size} = req.body
  const secretKey = "diginote-secret"
  try {
    jwt.verify(authToken, secretKey);
  } catch (error) {
    return res.status(401).json({ error: true, message: 'Unauthorized. Invalid token.' });
  }

  let decode = jwt.verify(authToken, secretKey);
  let decoded = decode.userId
  let userId = decoded.toString()

  pool.query('SELECT * FROM notes where userId = ?',
  [userId],
  (error, results) => {
    if (error) {
      console.error('Error retrieving note list:', error)
      return res.status(500).json({ error: true, message: 'An error while retrieving notes'})
    }
    if (results.length === 0){
      return res.status(200).json({error: false, message: 'No notes yet.', listnote: results})
    }

    res.status(200).json({error: false, message: 'All Notes retrieved', listnote: results})
  })
}

const getNoteIdHandler = async (req, res) => {
  const { noteId } = req.params
  const authToken = req.headers.authorization
  const secretKey = "diginote-secret"

  try {
    jwt.verify(authToken, secretKey);
  } catch (error) {
    return res.status(401).json({ error: true, message: 'Unauthorized. Invalid token.' });
  }

  let decode = jwt.verify(authToken, secretKey);
  let decoded = decode.userId
  let userId = decoded.toString()

  pool.query('SELECT * FROM notes WHERE noteId = ? and userId = ?',
  [noteId,userId],
  (error, results) => {
    if(error){
      console.error('Error retrieving note:', error)
      return res.status(500).json({ error: true,  message: 'An error occured retrieving the note.'})
    }

    if(results.length === 0){
      return res.status(400).json({error:true, message: 'Note not found'})
    }

    const note = results[0]
    res.status(200).json({error: false, message: 'Note retrieved', note})
  })
}

const editNoteHandler = async (req, res) => {
  const { noteId } = req.params
  const {title, description}= req.body;
  const authToken = req.headers.authorization
  const secretKey = "diginote-secret"

  try {
    jwt.verify(authToken, secretKey);
  } catch (error) {
    return res.status(401).json({ error: true, message: 'Unauthorized. Invalid token.' });
  }

  let decode = jwt.verify(authToken, secretKey);
  let decoded = decode.userId
  let userId = decoded.toString()

  if (!title && !description) {
    return res.status(400).json({ error: true, message: 'Please provide title or description' })
  }

  let updateFields = []
  let queryParams = []
    
  if (title) {
    updateFields.push('title = ?')
    queryParams.push(title)
  }

  if (description) {
    updateFields.push('description = ?')
    queryParams.push(description)
  }
  const date = new Date();
  const options = { timeZone: 'Asia/Jakarta' };
  
  const dayOfWeek = date.toLocaleString('en-US', { ...options, weekday: 'long' });
  
  const day = date.toLocaleString('en-US', { ...options, day: 'numeric' });
  const month = date.toLocaleString('en-US', { ...options, month: 'long' });
  const year = date.toLocaleString('en-US', { ...options, year: 'numeric' });
  
  const hours = date.toLocaleString('en-US', { ...options, hour: 'numeric', hour12: false });
  const minutes = date.toLocaleString('en-US', { ...options, minute: 'numeric' });
  const seconds = date.toLocaleString('en-US', { ...options, second: 'numeric' });
  
  const formattedDateTime = `${dayOfWeek}, ${day} ${month} ${year}, ${hours}:${minutes}:${seconds}`;
  updateFields.push('updated = ?')
  queryParams.push(formattedDateTime)

  queryParams.push(noteId)
  queryParams.push(userId)

  pool.query(
    `UPDATE notes SET ${updateFields.join(', ')} WHERE noteId = ? AND userId = ?`,
    queryParams,
    (error, results) => {
      if (error) {
        console.error('Error updating note:', error);
        return res.status(500).json({ error: true, message: 'An error occurred while updating the note.' });
      }
  
      if (results.affectedRows === 0) {
        return res.status(404).json({ error: true, message: 'Note not found or not authorized to update.' });
      }
  
      // Fetch the updated note from the database
      pool.query(
        'SELECT imageUrl FROM notes WHERE noteId = ? AND userId = ?',
        [noteId, userId],
        (error, result) => {
          if (error) {
            console.error('Error retrieving updated note:', error);
            return res.status(500).json({ error: true, message: 'An error occurred while retrieving the updated note.' });
          }
  
          const updatedNote = {
            noteId,
            userId,
            title,
            description,
            imageUrl: result[0].imageUrl,
            updated: formattedDateTime
          }
  
          res.status(200).json({ error: false, message: 'Note updated!', updatedNote });
        }
      );
    }
  );
}

const deleteNoteHandler = async (req, res) => {
  const { noteId } = req.params;
  const authToken = req.headers.authorization;
  secretKey = "diginote-secret"

  try {
    jwt.verify(authToken, secretKey);
  } catch (error) {
    return res.status(401).json({ error: true, message: 'Unauthorized. Invalid token.' });
  }
  let decode = jwt.verify(authToken, secretKey);
  let decoded = decode.userId
  let userId = decoded.toString()

  pool.query(
    'DELETE FROM notes WHERE noteId = ? AND userId = ?',
    [noteId, userId],
    (error, results) => {
      if (error) {
        console.error('Error deleting note:', error);
        return res.status(500).json({ error: true, message: 'An error occurred while deleting the note.' });
      }

      if (results.affectedRows === 0) {
        return res.status(404).json({ error: true, message: 'Note not found or not authorized to delete.' });
      }

      res.status(200).json({ success: true, message: 'Note deleted successfully.' });
    });
};




  module.exports = {
    createNoteHandler, 
    getAllNoteHandler, 
    getNoteIdHandler,
    editNoteHandler,
    deleteNoteHandler,
    uploadimghandler, }
