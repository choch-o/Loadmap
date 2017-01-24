var express = require('express');
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