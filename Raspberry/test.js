//Variables de connection au device Arduino
var i2c = require ('i2c');
var address = 0x18;
var device = new i2c(address, {device: '/dev/i2c-1'});
var devices = [device];

//Variables de récupération des données de l'Arduino 1
var humi;
var temp = 0.00;
var mois;
var askdata = 0;

//Variables de connection à la base de données SQL
var mysql = require('mysql');
var con = mysql.createConnection({
	host : "localhost",
	user : "root",
	password : "lumber5sq;",
	database : "Arduino1"
});

//Set du numéro de device par la commande sudo i2cdetect -y 1
device.setAddress(0x8);

//Connection à la base SQL avec collecte erreur
con.connect(function(err) {
	if(err) console.log("Erreur de connection");
	else	console.log("Connected!");
});

//Fonction de timeout des requêtes de la fonction handleread (lecture Arduino)
function handleTimeout(){
	setTimeout(function() { handleRead(); }, 1000 );
}

//Fonction de lecture des données Arduino toutes les 2 minutes
function handleRead(){
	var date = new Date();
	var min = date.getMinutes();
	var sec = date.getSeconds();

	if(min%2==0 && askdata==0){

		//On lit les données du Arduino (humidité,température,moisture)
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

		//Ecriture d'un bit de donnée 1.
		devices[0].writeByte(0x1,function(err) {
			if(err) console.log(err);
		});

		if(temp > 30 && mois > 750){
			devices[0].writeByte(0x2,function(err){
				if(err) console.log(err);
			}
		}

		askdata = 1;
	}
	if((min%2)!=0){
		askdata = 0;
	}
	//Appel de la fonction de gestion timeout qui boucle sur handleRead
	handleTimeout();
}

//Fonction de recupération de la valeur des minutes de la date actuelle
function getDateMin(){
	min = (min < 10 ? "0" : "") + min;
	return min;
}

//Fonction de récupération de la valeur des secondes de la date actuelle
function getDateSec(){
	sec = (sec < 10 ? "0" : "") + sec;
	return sec;
}

//Fonction d'ajout des datas récupérées dans la base de données mysql
function addInBDD(id,value) {
  var sql = "INSERT INTO donnees (typecapteur, valeur) VALUES ('" + id + "'," + value +");";
  con.query(sql, function (err, result) {
		if(err) console.log(err);
  });
}

//Appel de la fonction de gestion timeout qui boucle sur handleRead
handleTimeout();
