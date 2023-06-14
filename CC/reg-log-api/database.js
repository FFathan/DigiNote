const mysql =  require('mysql')

const pool = mysql.createPool({
    host: "34.101.219.14",
    user: "root",
    password: "DigiNote69",
    database: "diginote-db",
  })

module.exports = pool