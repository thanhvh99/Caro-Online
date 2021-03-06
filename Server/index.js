var express = require('express')
var app = express()
app.use(express.json())
const cors = require('cors')
app.use(cors())
require('dotenv').config()

require('./routes/route')(app)
const db = require('./configs/db.config')

db.sequelize.sync().then(() => {
    console.log("Sequelize is Running")
}).catch(err => {
    console.log(err.message)
});

var port = process.env.PORT
app.listen(port)