var express = require('express');
var http = require('http');
var https = require('https');
var bodyParser = require('body-parser');

var app = new express;
app.use(bodyParser.json());

var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost/loadmap');

var Schema = mongoose.Schema;

var taskDataSchema = new Schema({
  username : {type:String, required : true},
  subject : {type:String, required : true},
  tasktype : {type:String, required : true},
  date : {type:String, required : true},
  time : {type:String, required : true},
  taskstatus : {type:Number, default : 0}
});
var Task = mongoose.model('task', taskDataSchema, 'task');

var api = require('./api');

app.get('/task/viewdata', function(req,res){
  Task.find({}, function(err,results){
    if(err) throw err;

    res.writeHead(200, {'Content-Type':'application/json'});
    res.write(JSON.stringify(results));
    res.end();
  });
});

// app.get('/courses', api.getCourses);
app.get('/courses/:user_id', api.getCourses);
app.post('/courses', api.saveCourses);
app.post('/task/data', function(req,res){
  console.log("[task/data] Got request");

  Task.find({username:req.body['username']}, function(err, result){
    console.log("          "+result)
    if(result.length>0){
      var newTask={};

      if(req.body['subject']) newTask.subject=req.body['subject'];
      if(req.body['tasktype']) newTask.tasktype=req.body['tasktype'];
      if(req.body['date']) newTask.date=req.body['date'];
      if(req.body['time']) newTask.time=req.body['time'];
      if(req.body['taskstatus']) newTask.taskstatus=req.body['taskstatus'];

      Task.findOneAndUpdate({username:req.body['username']}, newTask, {}, function(err,results){
        if (err) throw err;
        console.log("DONE UPDATE TASK " + results);
      });
    } else {
      var newTask = {
        username : req.body['username'],
        subject : req.body['subject'],
        tasktype : req.body['tasktype'],
        date : req.body['date'],
        time : req.body['time'],
        taskstatus : req.body['taskstatus']
      };

      Task.findOneAndUpdate({username : req.body['username']}, newTask, {upsert: true, new: true}, function (err, results){
        if(err) throw err;
        console.log("DONE ENROLL NEW TASK " + results);
      });
    }
  });
  res.writeHead(200, {'Content-Type':'application/json'});
  //	res.write(JSON,stringify({result: 'OK'}));
  res.end();
});

app.listen (3000, function() {console.log("Listening on port #3000")});
