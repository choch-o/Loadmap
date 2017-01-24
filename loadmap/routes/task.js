var express = require('express');

var Task = require('../models/Task');
var Course = require('../models/Course');
var User = require('../models/User');
var router = express.Router();

router.get('/viewdata', function(req, res) {
  Task.find({}, function(err,results){
    if(err) throw err;

    res.writeHead(200, {'Content-Type':'application/json'});
    res.write(JSON.stringify(results));
    res.end();
  });
});

router.get('/:user_id', function(req, res) {
  Task.aggregate([
    { $match: {
      username: req.params.user_id
    }},
    {
      $group: {
        _id: {
          course: "$subject.code",
          tasktype: "$tasktype"
        },
        courseName: { $first: "$subject.name" },
        subtotalDuration: { $sum: "$duration" },
        tasks: { $addToSet: "$$ROOT" }
      }
    },
    { $group: {
      _id: "$_id.course",
      courseName: { $first: "$courseName" },
      totalDuration: { $sum: "$subtotalDuration" },
      tasksByType: { $addToSet: {
        tasktype: "$_id.tasktype",
        subtotalDuration: "$subtotalDuration",
        tasks: "$tasks"
      }}
    }}
  ], function (err, result) {
    if (err) {
      console.log(err);
      return;
    }
    console.log(result);
    res.send(result);
  });
});

router.get('/:user_id/timeSort', function(req, res){
	var dataWeek1, dataWeek2, dataWeek3, dataWeek4, dataWeek5, dataWeek6, dataWeek7, dataWeek8;q
	if ((req.body.datetime > 1483196400000-1) && (req.body.datetime <= 1483801200001)) {
		Task.aggregate([
			{ $match: {
			  username: req.params.user_id      
			  datetime: { $gt: 1483196399999, $lt: 1483801200001}
			}},
			{
			  $group: {
				_id: {
				  course: "$subject.code",
				  tasktype: "$tasktype",
				},
				period: 1,
				courseName: { $first: "$subject.name" },
				subtotalDuration: { $sum: "$duration" },
				tasks: { $addToSet: "$$ROOT" }
			  }
			},
			{ $group: {
			  _id: "$_id.course",
			  courseName: { $first: "$courseName" },
			  totalDuration: { $sum: "$subtotalDuration" },
			  tasksByType: { $addToSet: {
				tasktype: "$_id.tasktype",
				subtotalDuration: "$subtotalDuration",
				tasks: "$tasks"
			  }}
			}}
		  ], function (err, result) {
				if (err) {
				  console.log(err);
				  return;
				}
				console.log(result);
				dataWeek1 = result;
		});
	}
	else if ((req.body.datetime > 1483801200000) && (req.body.datetime <= 1484406000000)) {
        Task.aggregate([
            { $match: {
              username: req.params.user_id
              datetime: { $gt: 1483801200000, $lt: 1484406000000 }
            }},
            {
              $group: {
                _id: {
                  course: "$subject.code",
                  tasktype: "$tasktype",
                },
                period: 2,
                courseName: { $first: "$subject.name" },
                subtotalDuration: { $sum: "$duration" },
                tasks: { $addToSet: "$$ROOT" }
              }
            },
            { $group: {
              _id: "$_id.course",
              courseName: { $first: "$courseName" },
              totalDuration: { $sum: "$subtotalDuration" },
              tasksByType: { $addToSet: {
                tasktype: "$_id.tasktype",
                subtotalDuration: "$subtotalDuration",
                tasks: "$tasks"
              }}
            }}
          ], function (err, result) {
                if (err) {
                  console.log(err);
                  return;
                }
                console.log(result);
                dataWeek2 = result;
        });
    }
    else if ((req.body.datetime > 1484406000000) && (req.body.datetime <= 1485010800000)) {
        Task.aggregate([
            { $match: {
              username: req.params.user_id
              datetime: { $gt: 1484406000000, $lt: 1485010800000 }
            }},
            {
              $group: {
                _id: {
                  course: "$subject.code",
                  tasktype: "$tasktype",
                },
                period: 3,
                courseName: { $first: "$subject.name" },
                subtotalDuration: { $sum: "$duration" },
                tasks: { $addToSet: "$$ROOT" }
              }
            },
            { $group: {
              _id: "$_id.course",
              courseName: { $first: "$courseName" },
              totalDuration: { $sum: "$subtotalDuration" },
              tasksByType: { $addToSet: {
                tasktype: "$_id.tasktype",
                subtotalDuration: "$subtotalDuration",
                tasks: "$tasks"
              }}
            }}
          ], function (err, result) {
                if (err) {
                  console.log(err);
                  return;
                }
                console.log(result);
                dataWeek3 = result;
        });
    }
    else if ((req.body.datetime > 1485010800000) && (req.body.datetime <= 1485615600000)) {
        Task.aggregate([
            { $match: {
              username: req.params.user_id
              datetime: { $gt: 1485010800000, $lt: 1485615600000 }
            }},
            {
              $group: {
                _id: {
                  course: "$subject.code",
                  tasktype: "$tasktype",
                },
                period: 4,
                courseName: { $first: "$subject.name" },
                subtotalDuration: { $sum: "$duration" },
                tasks: { $addToSet: "$$ROOT" }
              }
            },
            { $group: {
              _id: "$_id.course",
              courseName: { $first: "$courseName" },
              totalDuration: { $sum: "$subtotalDuration" },
              tasksByType: { $addToSet: {
                tasktype: "$_id.tasktype",
                subtotalDuration: "$subtotalDuration",
                tasks: "$tasks"
              }}
            }}
          ], function (err, result) {
                if (err) {
                  console.log(err);
                  return;
                }
                console.log(result);
                dataWeek4 = result;
        });
    }
    else if ((req.body.datetime > 1485615600000) && (req.body.datetime <= 1486220400000)) {
        Task.aggregate([
            { $match: {
              username: req.params.user_id
              datetime: { $gt: 1485615600000, $lt: 1486220400000 }
            }},
            {
              $group: {
                _id: {
                  course: "$subject.code",
                  tasktype: "$tasktype",
                },
                period: 5,
                courseName: { $first: "$subject.name" },
                subtotalDuration: { $sum: "$duration" },
                tasks: { $addToSet: "$$ROOT" }
              }
            },
            { $group: {
              _id: "$_id.course",
              courseName: { $first: "$courseName" },
              totalDuration: { $sum: "$subtotalDuration" },
              tasksByType: { $addToSet: {
                tasktype: "$_id.tasktype",
                subtotalDuration: "$subtotalDuration",
                tasks: "$tasks"
              }}
            }}
          ], function (err, result) {
                if (err) {
                  console.log(err);
                  return;
                }
                console.log(result);
                dataWeek5 = result;
        });
    }
    else if ((req.body.datetime > 1486220400000) && (req.body.datetime <= 1486825200000)) {
        Task.aggregate([
            { $match: {
              username: req.params.user_id
              datetime: { $gt: 1486220400000, $lt: 1486825200000 }
            }},
            {
              $group: {
                _id: {
                  course: "$subject.code",
                  tasktype: "$tasktype",
                },
                period: 6,
                courseName: { $first: "$subject.name" },
                subtotalDuration: { $sum: "$duration" },
                tasks: { $addToSet: "$$ROOT" }
              }
            },
            { $group: {
              _id: "$_id.course",
              courseName: { $first: "$courseName" },
              totalDuration: { $sum: "$subtotalDuration" },
              tasksByType: { $addToSet: {
                tasktype: "$_id.tasktype",
                subtotalDuration: "$subtotalDuration",
                tasks: "$tasks"
              }}
            }}
          ], function (err, result) {
                if (err) {
                  console.log(err);
                  return;
                }
                console.log(result);
                dataWeek6 = result;
        });
    }
    else if ((req.body.datetime > 1486825200000) && (req.body.datetime <= 1487430000000)) {
        Task.aggregate([
            { $match: {
              username: req.params.user_id
              datetime: { $gt: 1486825200000, $lt: 1487430000000 }
            }},
            {
              $group: {
                _id: {
                  course: "$subject.code",
                  tasktype: "$tasktype",
                },
                period: 7,
                courseName: { $first: "$subject.name" },
                subtotalDuration: { $sum: "$duration" },
                tasks: { $addToSet: "$$ROOT" }
              }
            },
            { $group: {
              _id: "$_id.course",
              courseName: { $first: "$courseName" },
              totalDuration: { $sum: "$subtotalDuration" },
              tasksByType: { $addToSet: {
                tasktype: "$_id.tasktype",
                subtotalDuration: "$subtotalDuration",
                tasks: "$tasks"
              }}
            }}
          ], function (err, result) {
                if (err) {
                  console.log(err);
                  return;
                }
                console.log(result);
                dataWeek7 = result;
        });
    }
    else if ((req.body.datetime > 1487430000000) && (req.body.datetime <= 1488034800000)) {
        Task.aggregate([
            { $match: {
              username: req.params.user_id
              datetime: { $gt: 1487430000000, $lt: 1488034800000 }
            }},
            {
              $group: {
                _id: {
                  course: "$subject.code",
                  tasktype: "$tasktype",
                },
                period: 8,
                courseName: { $first: "$subject.name" },
                subtotalDuration: { $sum: "$duration" },
                tasks: { $addToSet: "$$ROOT" }
              }
            },
            { $group: {
              _id: "$_id.course",
              courseName: { $first: "$courseName" },
              totalDuration: { $sum: "$subtotalDuration" },
              tasksByType: { $addToSet: {
                tasktype: "$_id.tasktype",
                subtotalDuration: "$subtotalDuration",
                tasks: "$tasks"
              }}
            }}
          ], function (err, result) {
                if (err) {
                  console.log(err);
                  return;
                }
                console.log(result);
                dataWeek8 = result;
        });
    }
}
	result = new Array(dataWeek1, dataWeek2, dataWeek3, dataWeek4, dataWeek5, dataWeek6, dataWeek7, dataWeek8);
   	res.send(result);
});


router.post('/data', function(req, res) {
  console.log("[task/data] Got request");

  var newTask = new Task({
    username : req.body['username'],
    subject : req.body['subject'],
    tasktype : req.body['tasktype'],
    datetime : req.body['datetime'],
    taskstatus : req.body['taskstatus'],
    duration : req.body['duration']
  });
  newTask.save();

  res.writeHead(200, {'Content-Type':'application/json'});
  res.end();
});

module.exports = router;
