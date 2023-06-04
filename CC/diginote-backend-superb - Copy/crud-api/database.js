const mysql =  require('mysql')

const pool = mysql.createPool({
    host: "localhost",
    user: "root",
    password: "",
    database: "digitin",
  })

module.exports = pool