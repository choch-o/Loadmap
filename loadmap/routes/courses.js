var express = require('express');
var async = require('async');
var mongoose = require('mongoose');

var Course = require('../models/Course');
var User = require('../models/User');
var Task = require('../models/Task');

var Schema = mongoose.Schema;
var router = express.Router();

router.post('/', function(req, res) {
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

  var calls = [];

  var i;
  req.body.courses.forEach(function (course) {
    calls.push(function(callback) {
      Course.find({
        name: course.name,
        semester: course.semester
      }, function(error, result) {
        console.log(course);
        console.log("@@@@")
        console.log(result);
        console.log("##$");
        if (result.length == 0) {
          console.log("NULL");
          var newCourse = new Course({
            name: course.name,
            professor: course.professor,
            code: course.code,
            semester: course.semester
          });
          newCourse.save();
        } else {
          console.log("NOT NULL");
        }
      });
    });
  });

  async.parallel(calls, function(err, result) {
    if (err)
      return console.log(err);
    console.log(result);
  });
});

router.get('/:user_id', function(req, res) {
  console.log("REQUEST");
  User.findOne({username: req.params.user_id}, function(error, user) {
    if (user == null) {
      res.send("User not registerd");
    } else {
      res.send(user);
    }
  });
});


module.exports = router;
