var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var taskDataSchema = new Schema({
  username : {type:String, required : true},
  subject : {type:String, required : true},
  tasktype : {type:String, required : true},
  datetime : {type: Number, required : true},
  taskstatus : {type:Number, default : 0},
  duration : {type: Number, required : true}
});

var Task = mongoose.model('task', taskDataSchema, 'task');

module.exports = Task;
