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
    { $group: {
      _id: "$subject",
      totalDuration: { $sum: "$duration" },
      tasks: { $push: "$$ROOT" }
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

  Task.find({username:req.body['username']}, function(err, result){
    console.log("          "+result)
    if(result.length>0){
      var newTask={};

      if(req.body['subject']) newTask.subject=req.body['subject'];
      if(req.body['tasktype']) newTask.tasktype=req.body['tasktype'];
      if(req.body['datetime']) newTask.datetime=req.body['datetime'];
      if(req.body['taskstatus']) newTask.taskstatus=req.body['taskstatus'];
      if(req.body['duration']) newTask.duration=req.body['duration'];

      Task.findOneAndUpdate({username:req.body['username']}, newTask, {}, function(err,results){
        if (err) throw err;
        console.log("DONE UPDATE TASK " + results);
      });
    } else {
      var newTask = {
        username : req.body['username'],
        subject : req.body['subject'],
        tasktype : req.body['tasktype'],
        datetime : req.body['datetime'],
        taskstatus : req.body['taskstatus'],
        duration : req.body['duration']
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
}


