var express = require('express');
var http = require('http');
var https = require('https');
var bodyParser = require('body-parser');

var app = new express;
app.use(bodyParser.json());

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var taskDataSchema = new Schema({
	randomkey : {type:Number, required : true},
	subject : {type:String, required : true},
	tasktype : {type:String, required : true},
	date : {type:String, required : true},
	time : {type:String, required : true},
	taskstatus : {type:Number, default : 0}
});
var Task = mongoose.model('task', taskDataSchema, 'task');


app.get('/task/viewdata', function(req,res){
	Task.find({}, function(err,results){
		if(err) throw err;

		res.writeHead(200, {'Content-Type':'application/json'});
		res.write(JSON.stringify(results));
		res.end();
	});
});


app.post('/task/data', function(req,res){
	console.log("[task/data] Got request");

	Task.find({randomkey:req.body['randomkey']}, function(err, result){
		console.log("          "+result)
		if(result.length>0){
		var newTask={};

		if(req.body['subject']) newTask.subject=req.body['subject'];
		if(req.body['tasktype']) newTask.tasktype=req.body['tasktype'];
		if(req.body['date']) newTask.date=req.body['date'];
		if(req.body['time']) newTask.subject=req.body['time'];
		if(req.body['taskstatus']) newTask.taskstatus=req.body['taskstatus'];

		Task.findOneAndUpdate({randomkey:req.body['randomkey']}, newTask, {}, function(err,results){
			if (err) throw err;
			console.log("DONE UPDATE TASK " + results);
		});
	} else {
		var newTask = {
			randomkey : req.body['randomkey'],
			tasktype : req.body['tasktype'],
			date : req.body['date'],
			time : req.body['time'],
			taskstatus : req.body['taskstatus']
		};

		User.findOneAndUpdate({randomkey : req.body['randomkey']}, newTask, {upsert: true, new: true}, function (err, results){
		if(err) throw err;
		console.log("DONE ENROLL NEW TASK " + results);
		});
	}
});
	res.writeHead(200, {'Content-Type':'application/json'});
	res.write(JSON,stringify({result: 'OK'}));
	res.end();
});

app.listen (3000, function() {console.log("Listening on port #3000")});
