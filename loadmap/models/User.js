var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var courseSchema = mongoose.model('Course').schema

var userSchema = new Schema({
  username: String,
  name: String,
  courses: [courseSchema]
});

var User = mongoose.model('User', userSchema);

module.exports = User;

