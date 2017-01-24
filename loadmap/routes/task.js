var express = require('express');

var Task = require('../models/Task');
var Course = require('../models/Course');
var User = require('../models/User');
var router = express.Router();

router.get('/viewdata', function(req, res) {
  Task.find({}, function(err,results){
    if(err) throw err;

    res.writeHead(200, {'Content-Type':'application/json'});
    res.write(JSON.stringify(results));
    res.end();
  });
});

router.get('/:user_id', function(req, res) {
  Task.aggregate([
    { $match: {
      username: req.params.user_id
    }},
    {
      $group: {
        _id: {
          course: "$subject.code",
          tasktype: "$tasktype"
        },
        courseName: { $first: "$subject.name" },
        subtotalDuration: { $sum: "$duration" },
        tasks: { $addToSet: "$$ROOT" }
      }
    },
    { $group: {
      _id: "$_id.course",
      courseName: { $first: "$courseName" },
      totalDuration: { $sum: "$subtotalDuration" },
      tasksByType: { $addToSet: {
        tasktype: "$_id.tasktype",
        subtotalDuration: "$subtotalDuration",
        tasks: "$tasks"
      }}
    }}
  ], function (err, result) {
    if (err) {
      console.log(err);
      return;
    }
    console.log(result);
    res.send(result);
  });
});

router.post('/data', function(req, res) {
  console.log("[task/data] Got request");

  var newTask = new Task({
    username : req.body['username'],
    subject : req.body['subject'],
    tasktype : req.body['tasktype'],
    datetime : req.body['datetime'],
    taskstatus : req.body['taskstatus'],
    duration : req.body['duration']
  });
  newTask.save();

  res.writeHead(200, {'Content-Type':'application/json'});
  res.end();
});

module.exports = router;
