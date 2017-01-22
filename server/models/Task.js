var mongoose = require('mongoose');
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

module.exports = Task;
