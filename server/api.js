var mongoose = require('mongoose');

var Course = require('./models/Course');
var User = require('./models/User');
exports.saveCourses = function(req, res) {
  User.findOne({username: req.body.username}, function(error, user) {
    if (user == null) {
      console.log(user);
      var newUser = new User(req.body);
      newUser.save();
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
  console.log(req.params);
  User.findOne({username: req.params.user_id}, function(error, user) {
    if (user == null) {
      res.send("User not registerd");
    } else {
      res.send(user);
    }
  });
}
