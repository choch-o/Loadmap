var express = require('express');
var http = require('http');
var https = require('https');
var bodyParser = require('body-parser');

var app = new express;
app.use(bodyParser.json());

var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/loadmap');

var Schema = mongoose.Schema;


var api = require('./api');

app.get('/task/viewdata', api.viewData);
app.get('/tasks/:user_id', api.getTasks);
app.get('/courses/:user_id', api.getCourses);
app.post('/courses', api.saveCourses);
app.post('/task/data', api.taskData);

app.listen (3000, function() {console.log("Listening on port #3000")});
