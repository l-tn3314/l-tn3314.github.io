var App = new Marionette.Application();

App.addRegions({
    firstRegion: "#first-region",
    //secondRegion: "#second-region",
    //thirdRegion: "#third-region",
    fourthRegion: "#fourth-region",
});

App.on("start", function(){
    console.log("start");
    
    var staticview = new App.StaticView();
    App.fourthRegion.show(staticview);

    //var placeview = new App.PlaceView({model:p1});
    //App.secondRegion.show(placeview);

    //var placesview = new App.PlacesView({collection:c});
    //App.thirdRegion.show(placesview);

    var compview = new App.CompView({model:p1, collection:c});
    App.firstRegion.show(compview);
    
    Backbone.history.start();

});

App.StaticView = Marionette.ItemView.extend({
    template : "#static-template"
});

App.PlaceView = Marionette.ItemView.extend({
    template : "#place-template",
    tagName : "tr",
    events : {
	//filler....
	//"click #report" : function(){this.remove();}
	
	"click #up" : function(e) {
	    var r = this.model.get("rating");
	    r = parseInt(r);
	    r = r + 1;
	    this.model.set("rating",r);
	    this.render();
	},
	
	"click #down" : function(e) {
	    var r = this.model.get("rating");
	    r = parseInt(r);
	    r = r - 1;
	    this.model.set("rating",r);
	    this.render();
	},
    },
    modelEvents : {
	"change" : function() { this.render(); }
    }
});

App.PlacesView = Marionette.CollectionView.extend({
		childView : App.PlaceView
});

App.CompView = Marionette.CompositeView.extend({
    template : "#composite-template",
    childView : App.PlaceView,
    childViewContainer : "tbody",
    modelEvents : {
	"change" : function() { this.render(); }
	},
    events : {
	"click #add" : function(){
	    var n = $("#words").val();
	    if (n.length > 0){
		this.collection.add(new Place({phrase:n,rating:0}));
		this.collection.comparator = "phrase";
		this.collection.sort();
		$("#words").val("");
	    }
	}
    }
});

var myController = Marionette.Controller.extend({
    default : function(){
	var compview = new App.CompView({model:person,collection:c});
	App.firstRegion.show(compview);
    },
//    oneRoute : function(){
//	App.firstRegion.show(new App.PlaceView({model:p1}));
//    },
});

App.controller = new myController();

App.router = new Marionette.AppRouter({
    controller : App.controller,
    appRoutes : {
	"/" : "default",
	//one : "oneRoute",
    }
});

var Place = Backbone.Model.extend();
var Places = Backbone.Collection.extend({
    model:Place
});

var p1 = new Place({phrase:"Hello World", rating:8});
var p2 = new Place({phrase:"No Sleep", rating:10});
var c = new Places([p1,p2]);

App.start();
