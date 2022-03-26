const queryString = window.location.search;
//console.log(queryString);
const urlParams = new URLSearchParams(queryString);
const s = urlParams.get('s');
var msg = "Hello "
if (s != null) {
   msg = s;
}

const {HelloRequest, RepeatHelloRequest,
       HelloReply} = require('./helloworld_pb.js');
const {GreeterClient} = require('./helloworld_grpc_web_pb.js');



 var client = new GreeterClient('rpc',
                                null, null);

  // simple unary call
  var request = new HelloRequest();
  request.setName(msg);

  client.sayHello(request, {}, (err, response) => {
    if (err) {
      console.log(`Unexpected error for sayHello: code = ${err.code}` +
                  `, message = "${err.message}"`);
    } else {
      console.log(response.getMessage());
    }
  });



