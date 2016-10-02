/**************************************

                SIDEBAR

**************************************/

// display sidebar at beginning
$('#sidebarContents').css('width','250px');
$('#lines').css('z-index', '0');

// opening sidebar
$('#lines').click(function() {
    $('#sidebarContents').css('width', '250px');
    $('#lines').css('z-index', '0');
});

// closing sidebar
$('#x').click(function() {
    $('#sidebarContents').css('width', '0');
    $('#lines').css('z-index', '3');
});

// change background when hovering over sidebar elements
$('#sidebarContents li').hover(function() {
    $(this).css('background-color', '#008B8B');
}, function() {
    $(this).css('background-color', 'rgb(50,50,50)');
});

// change background when hovering over top of sidebar
$('#top').hover(function() {
    $(this).css('background-color', 'rgb(100,100,100)');
}, function() {
    $(this).css('background-color', 'rgb(50,50,50)');
});

// set page to top (clicking top of sidebar)
$('#top').click(function() {
    $('html body').animate({scrollTop: 0}, 'fast');
});

// Generating a website: removes the sidebar option
$('#done').click(function() {
    $('#sideOptions').remove();
    var a = window.document.createElement('a');
    var f = new File([$('html').html()], 'temp.html');
    
    a.href = window.URL.createObjectURL(f);
    
    a.download = 'temp.html';

    // Append anchor to body.
    document.body.appendChild(a)
    a.click();

    // Remove anchor from body
    document.body.removeChild(a)
});

// Resetting a website: resets user's work
// $('#reset').click(function() {
//     $('#headerUser').empty();
//     $('#navbarUser').empty();
//     $('#bgUser').empty();
//     $('#pictxtUser').empty();
//     $('#listUser').empty();
//     $('#tableUser').empty();

//     $('body').css('background-image', 'none');
//     $('body').css('background-color', 'rgb(255,255,255)');
// });


/**************************************

                MODAL

**************************************/

// modals for sidebar options
$('.navList').click(function() {
    $('#' + $(this).prop('id') + 'Modal').modal({show:true, backdrop:'static', keyboard:false});
});

// process input for reset
// different messages
var messages =["Are you sure you want to reset?", "Are you <b>REALLY</b> sure you want to reset?", 
"Are you <b><i>100%</i></b> sure you want to reset?", "hi!", "Do you really want to get rid of all your hard work?",
"Do you think I will let you reset?", "Huh... you're really stubborn. Fine."];
var counter=0;
$('#reset').click(function() {
    counter=0;
    $('#words').html(messages[counter]);

});
$('#resetOK').click(function() {
    if(counter > 5) {
        $('#headerUser').empty();
        $('#navbarUser').empty();
        $('#bgUser').empty();
        $('#pictxtUser').empty();
        $('#listUser').empty();
        $('#tableUser').empty();

        $('body').css('background-image', 'none');
        $('body').css('background-color', 'rgb(255,255,255)');
        counter=0;
    }
    else {
        console.log(counter);
        $('#resetModal').modal("hide");
        setTimeout(function(){
        $('#words').html(messages[counter]);
        $('#resetModal').modal({show:true, backdrop:'static', keyboard:false});
        }, 400);
        counter+=1;
    }
});

// process input for header
$('#headerOK').click(function() {
    var headerName = $('#headerInput').val();
    var imageSrc = $('#pictureLink').val();

    var height = screen.height / 3;
    var html;

    // if image has invalid source, or if no image is entered
    // set image to a default
    if (imageSrc == '' || (imageSrc.match(/\.(jpeg|jpg|gif|png)$/) == null)) {
	// chicken image
	//imageSrc = 'http://www.brilliantstore.com/pimagesl/l_os-yj0013y-4.jpg';
	// gif
	imageSrc = 'https://media.tenor.co/images/87bc727f6e742842f05cfc656a4be118/raw';
    }
    html = '<div class="headerUserContent" style="position: relative; background: url(' + imageSrc + '); width: 100%; height: ' + height + '";>';
    html +=  '<p style="color: #7FFF00; font-size: 55px; position: absolute; top: 1em; right: 1em;">' + headerName.substring(0, headerName.length / 2) + '</p>' 
	+ '<p style="color: #FF00FF; font-size: 55px; position: absolute; bottom: 1em; left: 1em;">' + headerName.substring(headerName.length / 2) + '</p>'
    +'</div>';
    $('#headerUser').append(html);

    $('#headerInput').val('');
    $('#pictureLink').val('');
});

// process input for background
$('#bgOK').click(function() {
    var selection = $('#bgModal input[name="bgChoice"]:checked').val();
    if (selection === 'image') {
	var imageSrc = $('#radioImageText').val();
	if (imageSrc == '' || imageSrc.match(/\.(jpeg|jpg|gif|png)$/) == null) {
	    imageSrc = 'http://www.brilliantstore.com/pimagesl/l_os-yj0013y-4.jpg';
	}
	$('body').css('background-image', 'url(' + imageSrc + ')');
    } else {
	var colorOptions = $('#bgModal select option');
	var color = colorOptions[Math.floor(Math.random() * colorOptions.length)].value;
	$('body').css('background-color', color);
    }
});

// process input for navbar
$('#navbarOK').click(function() {
    // reset navbar
    $('#navbarUser').empty();
    
    var allOptions = $('#navbarModal input');
    var selectedOptionsJQuery = $('#navbarModal input:checked');
    var selectedOptions = [];
    for (var k = 0; k < selectedOptionsJQuery.length; k++) {
	selectedOptions.push(selectedOptionsJQuery[k]);
    }
    
    var notSelectedOptions = [];
    for (var i = 0; i < allOptions.length; i++) {
	// if option was not selected, add to not selected array
	if (selectedOptions.indexOf(allOptions[i]) === -1) {
	    notSelectedOptions.push(allOptions[i]);
	}
    }

    // navbar elements
    var listHtml = '';
    for (var j = 0; j < notSelectedOptions.length; j++) {
	listHtml += '<li><a href="#">' + notSelectedOptions[j].value + '</a></li>';
    }

    // html for navbar
    var html = '<nav class="navbar navbar-default navbar-fixed-top">'
	+ '<div class="container-fluid">'
	+ '<div class="navbar-header">'
	+ '<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">'
	+ '<span class="icon-bar"></span>'
	+ '<span class="icon-bar"></span>'
	+ '<span class="icon-bar"></span>'
	+ '</button>'
	+ '</div>'
	+ '<div class="navbar-collapse collapse" aria-expanded="false" style="height: 1px;">'
	+ '<ul class="nav navbar-nav barNav">'
	+ listHtml
	+ '</ul></div></div></nav>'
    $('#navbarUser').append(html);
});

// process input for image/text
$('#pictxtOK').click(function() {
    // default image
    var imageSrc = $('#linkPicText').val();
    if (imageSrc == '' || imageSrc.match(/\.(jpeg|jpg|gif|png)$/) == null) {
	imageSrc = 'http://www.clipartbest.com/cliparts/bcy/Eeo/bcyEeordi.jpeg';
    }

    // default text
    var text = $('#description').val();
    if (text == undefined || text.trim() == '') {
	text = '<b>WATER YOU DOING?</b>';
    } else {
	var splitStrings = text.split(' ');
	var shuffledStrings = randomize(splitStrings);
	text = '';
	for (var i = 0; i < shuffledStrings.length - 1; i++) {
	    if (shuffledStrings[i] != undefined && shuffledStrings[i].trim() != '') {
		text += shuffledStrings[i].trim() + " ";
	    }
	}
	text += shuffledStrings[shuffledStrings.length - 1] + ".";
    }

    var html = '<div class="picTextContent"><center>'
	+ '<img src="' + imageSrc + '" alt="Missing out..."'
	+ '<br><br><br><p style="font-size: 20;">' + text
	+ '</p><br><br></center></div>';
    $('#pictxtUser').append(html);

    $('#linkPicText').val('');
    $('#description').val('');
});

var names = ["jumbo", "grapes", "skippy", "subtle"];
// process input for list
$('#listOK').click(function() {
    var listName = $('#listName').val();
    var items = $('#items').val();

    var listString = ''; // name for the list 
    var itemsString = ''; // list of items
    
    if (listName == undefined || listName.trim() == '') {
	listString = 'Array';
    } else {
	var listNameStrings = listName.split(' ');
	for (var i = 0; i < listNameStrings.length; i++) {
	    if (i === listNameStrings.length / 2) {
		listString += names[Math.floor(Math.random() * names.length)] + " ";
	    }
	    if (listNameStrings[i] != undefined && listNameStrings[i].trim() != '') {
		listString += listNameStrings[i].trim() + " ";
	    }
	}
    }
    if (items == undefined || items.trim() == '') {
	itemsString = '<li>Vector</li>';
    } else {
	var listItems = items.split('\n');
	var shuffledItems = randomize(listItems);
	for (var j = 0; j < shuffledItems.length; j++) {
	    if (shuffledItems[j] != undefined && shuffledItems[j].trim() != '') {
		var fontSize = Math.floor(Math.random() * 100);
		itemsString += '<li style="font-size: ' + fontSize + 'px;">' + shuffledItems[j].trim() + '</li>';
	    }
	}
    }

    var html = '<center><h2><i>' + listString + '</i></h2><ul>' + itemsString + '</ul></center>';
    
    $('#listUser').append(html);
    
    
});

/**************************************

                OTHER

**************************************/
// set width for textarea
$('#list').click(function() {
    setTimeout(function() {
	$('textarea').css('width', $('#listModal .modal-body').width());
    }, 500);
});
$('#pictxt').click(function() {
    $('textarea').css('width', $('#pictxtModal .modal-body').width());
    setTimeout(function() {
	$('textarea').css('width', $('#pictxtModal .modal-body').width());
    }, 500);
});

// returns an array with the original elements shuffled
function randomize(arr) {
    var arrCopy = []; // copy of the given array
    for (var i = 0; i < arr.length; i++) {
	arrCopy[i] = arr[i];
    }
    // randomizes the order
    var arrTemp = [];
    while (arrCopy.length > 0) {
	var ind = Math.floor(Math.random() * arrCopy.length);
      	arrTemp = arrTemp.concat(arrCopy.splice(ind, 1));
    }
    return arrTemp;
}
