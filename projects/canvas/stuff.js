var b = document.getElementById("b");
var c = document.getElementById("c");
var ctx = c.getContext("2d");

var people = [];
var balls = [];


var ball = function(){
    //console.log("ball");
    x = 10 + Math.random() * 550;
    y = 10 + Math.random() * 550;
    r = 10 + Math.random() * 10;
    dx = 1.2 + Math.random();
    dy = 1.2 + Math.random();
    balls.push(ballstuff(x,y,r,dx,dy,ctx));
};

var ballstuff = function(x,y,r,dx,dy,ctx){
    return{
	x: x,
	y: y,
	r: r, 
	dx: dx,
	dy: dy,
 	draw: function(){
	    //console.log(x);
	    ctx.beginPath();
	    ctx.arc(this.x,this.y,10 + this.r,0,2*Math.PI);
	    ctx.fillStyle = "#ff0000";	
	    ctx.stroke();
	    ctx.fill();
	},
	move: function(){
	   //console.log(this.x);
	    this.x = this.x + this.dx;
	    //console.log(this.x);
	    this.y = this.y + this.dy;
	    if (this.x < 10 || this.x > 570){
		this.dx = this.dx * -1;
	    }
	    if (this.y < 10 || this.y > 570){
		this.dy = this.dy * -1;
	    }
	}
    };
};


var addPerson = function(x,y,w,h,dx,dy,ctx,pic){
    //console.log("add");
    return{
	x: x,
	y: y,
	w: w,
	h: h,
	ctx: ctx,
	dx: dx,
	dy: dy,
	pic: pic,
	draw: function(){
	    var person = new Image();
	    person.src = this.pic;
	    ctx.drawImage(person,this.x,this.y,this.w,this.h);
	},
	move: function(){
	    this.x = this.x + this.dx;
	    this.y = this.y + this.dy;
	    if (this.x < 10 || this.x > 515){
		this.dx = this.dx * -1;
	    }
	    if (this.y < 10 || this.y > 510){
		this.dy = this.dy * -1;
	    }
	},
	checkDeath: function(){
	    for (var i = 0; i < balls.length; i++){
		if (Math.abs(this.x - balls[i].x) < 20 && Math.abs(this.y - balls[i].y) < 25){
		    this.pic = "grave.png";
		    this.dx = 0;
		    this.dy = 0;
		}
	    }
	}
    };
};

var kill = function(){
    ctx.save();
    ctx.translate(person.width/2,person.height/2);
    ctx.rotate(1.5);
    ctx.drawImage(person,50,50,100,100);
    ctx.restore();
};

var update = function(){
    //console.log("update");
    ctx.fillStyle = "#ffffff";
    ctx.fillRect(0,0,600,600);
    for (var i = 0; i < people.length; i++){
	people[i].move();
	people[i].draw();
	people[i].checkDeath();
    }
    for (var i = 0; i < balls.length; i++){
	balls[i].move();
	balls[i].draw();
    }
    window.requestAnimationFrame(update);
};

var clicked = function(e){
    //console.log("click");
    var x = e.offsetX;
    var y = e.offsetY;
    var w = 25 + Math.random() * 25;
    var h = 25 + Math.random() * 25;
    var dx = 0.5 + Math.random();
    var dy = 0.5 + Math.random();
    people.push(addPerson(x,y,w,h,dx,dy,ctx,"stickperson.png"));
};

/*person.src = "stickperson.png";
person.onload = function(){
    //ctx.translate(110,1);
    //ctx.rotate(1.5);
    ctx.drawImage(person,10,10,100,100);
};
*/

//kill(person);
b.addEventListener("click", ball);
c.addEventListener("click", clicked);
document.getElementById("clear").addEventListener("click",function(){
    console.log("ok")
    people = [];
    balls = [];
    ctx.fillStyle = "#ffffff";
    ctx.fillRect(0,0,600,600);
});

window.requestAnimationFrame(update);
