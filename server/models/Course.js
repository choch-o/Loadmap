var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var courseSchema = new Schema({
  name: String,
  professor: String,
  code: String,
  semester: String
});

var Course = mongoose.model('Course', courseSchema);

module.exports = Course;
