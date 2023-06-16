const mysql =  require('mysql')

const pool = mysql.createPool({
    host: "your_host",
    user: "your_user",
    password: "your_password",
    database: "your_database",
  })

module.exports = pool
