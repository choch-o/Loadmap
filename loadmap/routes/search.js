var express = require('express');
var Task = require('../models/Task');
var Course = require('../models/Course');
var router = express.Router();

router.post('/', function(req, res) {
  console.log("Course search");
  console.log(req.body.coursename);
  Task.aggregate([
    { $match: {
      'subject.name' : req.body.coursename
    }},
    {
      $group: {
        _id: {
          tasktype: "$tasktype"
        },
        name: { $first: "$tasktype" },
        size: { $sum: "$duration" },
      }
    },
    {
      $project: {
        _id: 0,
        name: 1,
        size: 1
      }
    }

  ], function (err, result) {
    if (err) {
      console.log(err);
      return;
    }
    console.log("RESULT");
    console.log(result);

    res.send(result);
    res.end();
  });
});

module.exports = router;
