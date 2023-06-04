const jwt = require ('jsonwebtoken')
const {nanoid} = require ('nanoid')
const axios = require('axios')
const pool =  require('./database')

const createNoteHandler = async (req, res) => {
    const { title, tags } = req.body;
    const token = req.headers.authorization; 
    const image = req.body.image; 
  
    if (!title || !tags || !image) {
      return res.status(400).json({ error: true, message: 'Please provide title, tags, and OCR image data for the note.' });
    }
  
    try {
      const decoded = jwt.verify(token, 'your-secret-key');
      const userId = decoded.id;
  
      // Make a request to the OCR API to extract text from the image
      const ocrResponse = await axios.post('https://api.ocr.space/parse/image', {
        apikey: 'YOUR_API_KEY',
        base64Image: image,
        isOverlayRequired: true,
      });
  
      const extractedText = ocrResponse.data.ParsedResults[0].ParsedText;
  
      const id = nanoid(16);

      const note = {
        id,
        userId,
        title,
        tags,
        body: extractedText,
        updated: new Date(),
      };
  
      pool.query(
        'INSERT INTO notes (id, user_id, title, tags, body, updated) VALUES (?, ?, ?, ?, ?, ?)',
        [id, userId, title, tags, extractedText, note.updated],
        (error) => {
          if (error) {
            console.error('Error inserting note:', error);
            return res.status(500).json({ error: true, message: 'An error occurred while creating the note.' });
          }
  
          res.status(201).json(note);
        }
      );
    } catch (error) {
      return res.status(401).json({ error: true, message: 'Invalid token' });
    }
};

const getAllNoteHandler = (req,res) => {
  const { authToken, page, size } = req.body

  try{
    const userID = authToken
    const startIndex = (page - 1) * size

    pool.query('SELECT * FROM notes where user_id = ? LIMIT ?, ?',
    [userID, startIndex, size],
    (error, results) => {
      if (error) {
        console.error('Error retrieving note list:', error)
        return res.status(500).json({ error: true, message: 'An error while retrieving notes'})
      }
      if (results.length === 0){
        return res.status(404).json({error: true, message: 'No notes yes.'})
      }

      res.status(200).json({error: false, message: 'Note list retrieved', listnote: results})
    }
    )
  }
  catch(error) {
    return res.status(401).json({ error: true, message: 'invalid token ID'})
  }
}

const getNoteIdHandler = async (req, res) => {
  const { id } =  req.params
  const token = req.headers.authorization

  try {
    const decoded = jwt.verify(token, 'your-secret-key')
    const userId = decoded.id

    pool.query('SELECT * FROM notes WHERE id = ? and user_id = ?',
    [id,userId],
    (error, results) => {
      if(error){
        console.error('Error retrieving note:', error)
        return res.status(500).json({ error: true,  message: 'An error occured retrieving the note.'})
      }

      if(results.length === 0){
        return res.status(400).json({error:true, message: 'Note not found'})
      }

      const note = results[0]
      res.status(200).json(note)
    })
  }
  catch(error){
    return res.status(401).json({error: true, message:'Invalid token'})
  }
}

const editNoteHandler = async (req, res) => {
  const { id } = req.params
  const { title, tags, body } = req.body
  const token = req.headers.authorization

  try {
    const decoded = jwt.verify(token, 'your-secret-key')
    const userId = decoded.id

    if (!title && !tags && !body) {
      return res.status(400).json({ error: true, message: 'Please provide at least one field to update (title, tags, or body).' })
    }

    let updateFields = []
    let queryParams = []
    
    if (title) {
      updateFields.push('title = ?')
      queryParams.push(title)
    }

    if (tags) {
      updateFields.push('tags = ?')
      queryParams.push(tags)
    }

    if (body) {
      updateFields.push('body = ?')
      queryParams.push(body)
    }

    updateFields.push('updated = ?')
    queryParams.push(new Date())

    queryParams.push(id)
    queryParams.push(userId)

    pool.query(
      `UPDATE notes SET ${updateFields.join(', ')} WHERE id = ? AND user_id = ?`,
      queryParams,
      (error, results) => {
        if (error) {
          console.error('Error updating note:', error);
          return res.status(500).json({ error: true, message: 'An error occurred while updating the note.' });
        }

        if (results.affectedRows === 0) {
          return res.status(404).json({ error: true, message: 'Note not found or not authorized to update.' });
        }

        const updatedNote = {
          id,
          userId,
          title,
          tags,
          body,
          updated: new Date(),
        }

        res.status(200).json(updatedNote);
      }
    )
  } catch (error) {
    return res.status(401).json({ error: true, message: 'Invalid token' });
  }
}

const deleteNoteHandler = async (req, res) => {
  const { id } = req.params;
  const token = req.headers.authorization;

  try {
    const decoded = jwt.verify(token, 'your-secret-key');
    const userId = decoded.id;

    pool.query(
      'DELETE FROM notes WHERE id = ? AND user_id = ?',
      [id, userId],
      (error, results) => {
        if (error) {
          console.error('Error deleting note:', error);
          return res.status(500).json({ error: true, message: 'An error occurred while deleting the note.' });
        }

        if (results.affectedRows === 0) {
          return res.status(404).json({ error: true, message: 'Note not found or not authorized to delete.' });
        }

        res.status(200).json({ success: true, message: 'Note deleted successfully.' });
      }
    );
  } catch (error) {
    return res.status(401).json({ error: true, message: 'Invalid token' });
  }
};




  module.exports = {
    createNoteHandler, 
    getAllNoteHandler, 
    getNoteIdHandler,
    editNoteHandler,
    deleteNoteHandler} 