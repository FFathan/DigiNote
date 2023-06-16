const jwt = require('jsonwebtoken')
const pool = require('./database')
const bcrypt = require('bcrypt')
const secret = "diginote-secret"



const registerHandler = (req, res) => {
    const { username, email, password } = req.body;
  
    if (!username || !email || !password) {
      return res.status(400).json({ error: true, message: 'All fields are required' });
    }
  
    // Ketentuan Passwordnya
    if (password.length < 8) {
      return res.status(400).json({ error: true, message: 'Password must be at least 8 characters long' });
    }
    if (!/[A-Z]/.test(password)) {
      return res.status(400).json({ error: true, message: 'Password must contain at least one uppercase letter' });
    }
    if (!/[0-9]/.test(password)) {
      return res.status(400).json({ error: true, message: 'Password must contain at least one number' });
    }
    if (!/[!@#$%^&.*_]/.test(password)) {
      return res.status(400).json({ error: true, message: 'At least one special character (! @ # $ % ^ & * . _)' });
    }
  
    const checkEmailQuery = 'SELECT * FROM users WHERE email = ?';
    pool.query(checkEmailQuery, [email], (error, emailResults) => {
      if (error) {
        console.error('Error checking email:', error);
        return res.status(500).json({ error: true, message:  'Server error' });
      }
  
      if (emailResults.length > 0) {
        return res.status(409).json({ error: true, message: 'Email is already taken' });
      }
  
      // Check if the username is already taken
      const checkUsernameQuery = 'SELECT * FROM users WHERE username = ?';
      pool.query(checkUsernameQuery, [username], (usernameError, usernameResults) => {
        if (usernameError) {
          console.error('Error checking username:', usernameError);
          return res.status(500).json({ error: true, message:  'Server error' });
        }
  
        if (usernameResults.length > 0) {
          return res.status(409).json({ error: true, message:  'Username is already taken' });
        }
  
        // Hash the password
        bcrypt.hash(password, 10, (hashError, hashedPassword) => {
          if (hashError) {
            console.error('Error hashing password:', hashError);
            return res.status(500).json({ error: true, message: 'Server error' });
          }
  
          // Save the account details to the database
          const insertUserQuery = 'INSERT INTO users (username, email, password) VALUES (?, ?, ?)';
          pool.query(insertUserQuery, [username, email, hashedPassword], (insertError, _) => {
            if (insertError) {
              console.error('Error registering user:', insertError);
              return res.status(500).json({ error: true, message :'Server error' });
            }
  
            res.json({ error: false, message: 'Account registered successfully' });
          });
        });
      });
    });  
  };
  
  const loginHandler = (req, res) => {
    const { usernameORemail, password } = req.body;
  
    // Validate if email and password are provided
    if (!usernameORemail || !password) {
      return res.status(400).json({ error: true, message : 'Email and password are required' });
    }
  
    // Check if the email or username exists in the database
    const checkUserQuery = 'SELECT * FROM users WHERE email = ? OR username = ?';
    pool.query(checkUserQuery, [usernameORemail, usernameORemail], (error, results) => {
      if (error) {
        console.error('Error checking email or username:', error);
        return res.status(500).json({ error: true, message: 'Server error' });
      }
  
      if (results.length === 0) {
        return res.status(401).json({ error: true, message:  'Invalid email or password' });
      }
  
      const user = results[0];
  
      bcrypt.compare(password, user.password, (compareError, isMatch) => {
        if (compareError) {
          console.error('Error comparing passwords:', compareError);
          return res.status(500).json({ error: true, message: 'Server error' });
        }
  
        if (!isMatch) {
          return res.status(401).json({ error: true, message: 'Invalid email or password' });
        }
  
        const token = jwt.sign({ userId: user.id }, secret);
  
        res.status(200).json({ error:false, message: 'Login Succeed', token });
      });
    });
  };
  
  module.exports = { registerHandler, loginHandler}
