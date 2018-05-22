function mobileDesensitize(mobile){
	if(mobile){
		return mobile.substring(0,3) + "****" + mobile.substring(mobile.length-4);
	}
	else return mobile;
}
function amountDesensitize(amount,unit){
	if(amount){
		unit = unit||1000;
		amount = parseInt(amount);
		if(amount.toString()!="NaN"){
			return amount<unit?unit+"以下":(parseInt(amount/unit)*unit)+"+";
		}
		else return amount;
		
	}
	else return amount;
}
function companyDesensitize(name){
	if(name){
		return name.substring(0,3)+"***" + name.substring(6);
		
	}
	else return name;
}
function companyCodeDesensitize(code){
	if(code){
		return code.substring(0,4)+"***" + code.substring(7);
		
	}
	else return code;
}