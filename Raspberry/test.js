var i2c = require ('i2c');
var address = 0x18;
var device = new i2c(address, {device: '/dev/i2c-1'});
var devices = [device];
var getdatas;
var humi;
var temp = 0.00;
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

con.connect(function(err) {
	if(err) console.log("Erreur de connection");
	else	console.log("Connected!");
});

function handleTimeout(){
	setTimeout(function() { handleRead(); }, 1000 );
}

function handleRead(){
	var date = new Date();
	var min = date.getMinutes();
	var sec = date.getSeconds();

	if(min%2==0 && askdata==0){
		devices[0].readBytes(null,17,function(err,res){
			if(err) console.log("Erreur de lecture Arduino");
			else{
				getdatas = res.toString('ascii');
				getdatas = getdatas.split(";");
				humi = getdatas[0];
				addInBDD('HU',humi);
				temp = getdatas[1];
				addInBDD('TE',temp);
				mois = getdatas[2];
				addInBDD('MO',mois);
			}
		});
		devices[0].writeByte(0x1,function(err) {
			if(err) console.log(err);
		});

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
  var sql = "INSERT INTO donnees (typecapteur, valeur) VALUES ('" + id + "'," + value +");";
  con.query(sql, function (err, result) {
		if(err) console.log(err);
  });
}

handleTimeout();
