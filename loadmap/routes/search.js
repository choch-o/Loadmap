var express = require('express');
var router = express.Router();

router.post('/', function(req, res) {
  console.log("Course search");
  console.log(req.body);
});

module.exports = router;
