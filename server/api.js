var mongoose = require('mongoose');

var Course = require('./models/Course');
var User = require('./models/User');
var Task = require('./models/Task');

exports.saveCourses = function(req, res) {
  console.log("REQUESTTTTTT");
  User.findOne({username: req.body.username}, function(error, user) {
    if (user == null) {
      console.log(user);
      var newUser = new User(req.body);
      newUser.save();
      res.send(user);
    } else {
      user.courses = req.body.courses || user.courses;
      user.save(function (err, user) {
        if (err) {
          res.status(500).send(err);
        }
        // Temporarily return user data => 'load' statistics
        res.send(user);
      });
    }
  });
}

exports.getCourses = function(req, res) {
  console.log("REQUEST");
  User.findOne({username: req.params.user_id}, function(error, user) {
    if (user == null) {
      res.send("User not registerd");
    } else {
      res.send(user);
    }
  });
}

exports.getTasks = function(req, res) {
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
        courseName: { $first: "subject.name" },
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
  /*
  Task.find({username: req.params.user_id})
    .sort({datetime: 'ascending'})
    .aggregate([
      {
        $group: {
          _id: '$subject',
          totalDuration: { $sum: '$duration' },
          count: { $sum: 1 }
        }
      }
    ])
    .exec(function (error, tasks) {
      res.send(tasks);
    });
    */
}

exports.viewData = function(req, res) {
  Task.find({}, function(err,results){
    if(err) throw err;

    res.writeHead(200, {'Content-Type':'application/json'});
    res.write(JSON.stringify(results));
    res.end();
  });
}

exports.taskData = function(req, res) {
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
}


