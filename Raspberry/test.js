var i2c = require ('i2c');
var address = 0x18;
var device = new i2c(address, {device: '/dev/i2c-1'});
var devices = [device];
var getdatas;
var humi;
var temp;
var mois;
var askdata = 0;
var mysql = require('mysql');
var con = mysql.createConnection({
	host : "localhost",
	user : "root",
	password : "lumber5sq;",
	database : "Arduino1"
});

device.setAddress(0x8);

function handleTimeout(){
	setTimeout(function() { handleRead(); }, 1000 );
}

function handleRead(){
	var date = new Date();
	var min = date.getMinutes();
	var sec = date.getSeconds();

	if(min%2==0 && askdata==0){
		console.log("Je lis des données");
		devices[0].readBytes(null,17,function(err,res){
			getdatas = res.toString('ascii');
			console.log(getdatas);
			getdatas = getdatas.split(";");
			humi = getdatas[0];
			addInBDD('HU',humi);
			temp = getdatas[1];
			addInBDD('TE',temp);
			mois = getdatas[2];
			addInBDD('MO',mois);
			console.log("h=" + humi + ", t=" + temp + ", m=" + mois);
		});
		devices[0].writeByte(0x1,function(err) {});

		askdata = 1;
	}
	if((min%2)!=0){
		askdata = 0;
	}

	handleTimeout();
}


function getDateMin(){
	min = (min < 10 ? "0" : "") + min;
	return min;
}

function getDateSec(){
	sec = (sec < 10 ? "0" : "") + sec;
	return sec;
}

function addInBDD(id,value) {
	con.connect(function(err) {
  	console.log("Connected!" + id + ", " + value);
  	var sql = "INSERT INTO donnees (typecapteur, valeur) VALUES (''" + id + "''," + value +");";
		console.log(sql);
  	con.query(sql, function (err, result) {
    	console.log("1 record inserted");
  	});
	});
}
handleTimeout();
