var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var courseSchema = mongoose.model('Course').schema

var taskDataSchema = new Schema({
  username : {type:String, required : true},
  subject: courseSchema,
  tasktype : {type:String, required : true},
  datetime : {type: Number, required : true},
  taskstatus : {type:Number, default : 0},
  duration : {type: Number, required : true}
});

var Task = mongoose.model('task', taskDataSchema, 'task');

module.exports = Task;
