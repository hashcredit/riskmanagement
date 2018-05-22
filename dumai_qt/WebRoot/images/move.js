var dragswitch=0
var nsx
var nsy
var nstemp
var whichIt = null;
var dragapproved=false

function drag_dropns(name){
	temp=eval(name)
	temp.captureEvents(Event.MOUSEDOWN | Event.MOUSEUP)
	temp.onmousedown=gons
	temp.onmousemove=dragns
	temp.onmouseup=stopns
}

function gons(e){
	temp.captureEvents(Event.MOUSEMOVE)
	nsx=e.x
	nsy=e.y
}
function dragns(e){
	if (dragswitch==1){
		temp.moveBy(e.x-nsx,e.y-nsy)
		return false
	}
}

function stopns(){
	temp.releaseEvents(Event.MOUSEMOVE)
}

function drag_dropie(){
	if (dragapproved==true){
		temp.style.pixelLeft=tempx+event.clientX-iex
		temp.style.pixelTop=tempy+event.clientY-iey
		return false
	}
}

function initializedragie(name){
	whichIt=name
	temp=whichIt
	iex=event.clientX
	iey=event.clientY
	tempx=temp.style.pixelLeft
	tempy=temp.style.pixelTop
	dragapproved=true
	document.onmousemove=drag_dropie
}

if (document.all){
	document.onmouseup=new Function("dragapproved=false")
}