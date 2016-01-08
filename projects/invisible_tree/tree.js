var mouseX, mouseY;
var tree = document.getElementById('tree');
var width, height;
var count, myEvent;

var findTree = function(e) {
    var x = width;
    var y = height;
    var xd = Math.abs(mouseX-x);
    var yd = Math.abs(mouseY-y);
    //console.log(mouseX);
    //console.log(x);
    //console.log(xd);
    //console.log(yd);
    if ((xd < 10) && (yd < 10)){
	document.body.style.background = '#FFFFFF';
	tree.style.visiblity = 'visible';
    }
    else if ((xd < 50) && (yd < 50)){
	changeColors('#FFE6E6','#E6F2E6');
    }
    else if ((xd < 100) && (yd < 100)){
	changeColors('#FFCCCC','#CCE6CC');
    }
    else if ((xd < 200) && (yd < 200)){
	changeColors('#FFB2B2','#B2D9B2');
    }
    else if ((xd < 300) && (yd < 300)){
	changeColors('#FF9999','#99CC99');
    }
    else if ((xd < 400) && (yd < 400)){
	changeColors('#FF8080','#80C080');
    }
    else if ((xd < 500) && (yd < 500)){
	changeColors('#FF6666','#66B366');
    }
    else if ((xd < 600) && (yd < 600)){
	changeColors('#FF4D4D','#4DA64D');
    }
    else if ((xd < 700) && (yd < 700)){
	changeColors('#FF3333','#339933');
    }
    else if ((xd < 800) && (yd < 800)){
	changeColors('#FF1919','#198D19');
    }
    else if ((xd < 900) && (yd < 900)){
	changeColors('#FF0000','#008000');
    }
    else
	changeColors('#E60000','#007300');
}   

function changeColors(r,g){
    setInterval(change(r,g),100);
}

function change(r,g){
    if (count == 1){
	color = r;
	count = 2;
    } else{
	color = g;
	count = 1;
    }
    document.body.style.background = color;
}


function start(){
    var w = window.innerWidth;
    var h = window.innerHeight;
    width = Math.floor((Math.random() * w) + 1);
    height = Math.floor((Math.random() * h) + 1);
    console.log(width);
    console.log(height);
    myevent = setInterval(findTree,150);
    tree.style.position = "absolute";
    tree.style.visibility = 'hidden';
    tree.style.left = width + 'px';
    tree.style.top = height + 'px';
    //console.log(tree.offsetleft);
    count = 1;
}

function stop(){
    window.clearTimeout(myevent);
}

function checktree(){
    var x = width;
    var y = height;
    var xd = Math.abs(mouseX-x);
    var yd = Math.abs(mouseY-y);
    if (xd < 35 && yd < 35){
	tree.style.visibility = 'visible';
	stop();
    }
}
    



document.getElementById("start").addEventListener('click',start);
document.getElementById("stop").addEventListener('click',stop);

window.addEventListener('mousemove', function(e) {
    mouseX = e.pageX;
    mouseY = e.pageY;
});

window.addEventListener('click',checktree);
    
