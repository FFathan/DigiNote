const nanoid = require('nanoid')
const pool = require('pool')

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
  
      const id = nanoid();

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

  module.exports = createNoteHandler