(function() {
	var app = angular.module('productManager', ['ui.bootstrap']);


	app.controller('ProductController', function(ProductService) {
		
		this.alerts = [];
		
		this.getProduct = function() {
			var dis = this;
			ProductService.getProduct(this.id).then(function(data) {
				 if (data.msg != undefined) {
					  dis.alerts[0] = data;
				  } else {
					  dis.value = data;
				  }
			});
		}
		
		this.closeAlert = function() {
			this.alerts = [];
		}
		
		this.getProductIds = function(id) {
			this.alerts = [];
			if (id.length >= 2) {
				var producTds = ProductService.getProductIds(id);
				return producTds;
			}
			return "";
		}
		
		this.update = function() {
			this.closeAlert();
			var dis = this;
			ProductService.update(this.value).then(function(data) {
				dis.alerts[0] = data;
			});
			return "";
		}
	});

	
	app.service('ProductService', function($http) {
		this.getProduct = function(id) {
			return $http.get('/getProducts/'+id).
			  then(function(response) {
				 return response.data;
			});
		};
		
		this.getProductIds = function(id) {
			return $http.get('/getProductIds/'+id).
			  then(function(response) {
			      return response.data;
			});
		}
		
		this.update = function(product) {
			return $http.post('/update/', product).
			  then(function(response) {
			      return response.data;
			});
		}
	});

	
	app.directive("productDescription", function() {
	      return {
	        restrict: 'E',
	        templateUrl: "assets/views/product.html"
	      };
	    });

	/*
	This directive allows us to pass a function in on an enter key to do what we want.
	*/
	app.directive('ngEnter', function () {
	    return function (scope, element, attrs) {
	        element.bind("keydown keypress", function (event) {
	            if(event.which === 13) {
	                scope.$apply(function (){
	                    scope.$eval(attrs.ngEnter);
	                });
	 
	                event.preventDefault();
	            }
	        });
	    };
	});
	
})();
