// VARIABLES

// When the user scrolls the page, execute myFunction
window.onscroll = function () {
	myFunction();
};

// Get the navbar
var navbar = document.getElementById("navbar");

// Get the offset position of the navbar
var sticky = navbar.offsetTop;

// Add the sticky class to the navbar when you reach its scroll position. Remove "sticky" when you leave the scroll position
function myFunction() {
	if (window.pageYOffset >= sticky) {
		navbar.classList.add("sticky");
	} else {
		navbar.classList.remove("sticky");
	}
}

window.addEventListener("load", (event) => {
	loadProductInformations(getUrlParam());
	loadBottomBar(15);
});

const accountLink = document.getElementById("account_link");

accountLink.addEventListener("click", () => {
	if (
		sessionStorage.getItem("CurrentUserID") == "null" ||
		sessionStorage.getItem("CurrentUserID") == undefined
	) {
		location.replace("login_register.html");
	} else {
		location.replace("account.html");
	}
});

/*#######################################################################################################################################*/

async function loadProductInformations(productID) {
	request = "http://localhost:8080/Main/getById?id=" + productID;

	await $.getJSON(request, function (data) {
		console.log(data);
		document.getElementById("brand").innerText = data.brand;
		document.getElementById("name").innerText = data.name;
		document.getElementById("info").innerText = data.information;
		document.getElementById("old_price").innerText = data.price + "$";
		document.getElementById("image").src = data.image;
		document.getElementById("new_price").innerText =
			(data.price * (1 - data.discount)).toFixed(2) + "$";
	});
}

async function loadBottomBar(productCount) {
	request = "http://localhost:8080/Main/getRandom?count=" + productCount;

	await $.getJSON(request, function (data) {
		var bottomLine = document.getElementById("bottom_line");

		for (let i = 0; i < data.length; i++) {
			// Create cell (tr) for every product
			var cell = document.createElement("td");
			cell.className = "product_cell";
			bottomLine.appendChild(cell);

			// Add image to the div
			var img = document.createElement("img");
			img.className = "bar_image";
			img.src = data[i].image;
			img.id = data[i].id;
			cell.appendChild(img);

			img.addEventListener("click", (event)=>{
				openProductPage(event.target.id);
			});
		}
	});
}

function addProductToUsersFav(userID, productID) {
	var request =
		"http://localhost:8080/User/addProductToFavorites?userId=" +
		userID +
		"&productId=" +
		productID;

	$.getJSON(request, function (data) {
		if (data != null) {
			alert("product succesfully added to the users favorites");
		}
	});
}

function addProductToUsersCart(userID, productID) {
	var request =
		"http://localhost:8080/User/addProductToCart?userId=" +
		userID +
		"&productId=" +
		productID;

	$.getJSON(request, function (data) {
		if (data != null) {
			alert("product succesfully added to the users cart");
		}
	});
}

function removeProductFromUsersFav(userID, productID) {
	var request =
		"http://localhost:8080/User/removeProductFromFavorites?userId=" +
		userID +
		"&productId=" +
		productID;

	$.getJSON(request, function (data) {
		if (data != null) {
			alert("product succesfully removed from the users favorites");
		}
	});
}

function removeProductFromUsersCart(userID, productID) {
	var request =
		"http://localhost:8080/User/removeProductFromCart?userId=" +
		userID +
		"&productId=" +
		productID;

	$.getJSON(request, function (data) {
		if (data != null) {
			alert("product succesfully removed from the users cart");
		}
	});
}

function changePublicityOfProduct(productID, isPublished) {
	var request =
		"http://localhost:8080/Main/setPublished?productId=" +
		productID +
		"&isPublished=" +
		isPublished;

	$.getJSON(request, function (data) {
		if (data != null) {
			alert("product publicity succesfully changed!");
		}
	});
}

function getUrlParam() {
	// Get URL
	const queryString = window.location.search;

	// Then get URL params object
	const urlParams = new URLSearchParams(queryString);

	// Return the id parameter
	return urlParams.get("id");
}

function openProductPage(productId) {
	location.replace("product.html?id=" + productId);
}