// VARIABLES
filterList = [];

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
	getOpportunities();
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

$(function () {
	$("i").click(function () {
		$("i,span").toggleClass("press", 1000);
	});
});

/*#######################################################################################################################################*/

function searchByWord() {
	var val = document.getElementById("search").value;

	$.getJSON(
		"http://localhost:8080/Perfume/search?word=" + val,
		function (data) {
			var table = document.getElementById("table");

			deleteAllRows(table);

			addCardsToTable(data, table);
		}
	);
}

function getOpportunities() {
	$.getJSON("http://localhost:8080/Main/getOpportunities", function (data) {
		var table = document.getElementById("table");

		deleteAllRows(table);

		addCardsToTable(data, table);
	});
}

function deleteAllRows(table) {
	table.innerHTML = "";
}

function addCardsToTable(data, table) {
	for (let i = 0; i < data.length; i++) {
		// Create a row every 3 time
		if (i % 3 == 0) {
			var row = table.insertRow(i / 3);
		}

		// Create 3 cell in each row
		var cell = row.insertCell();
		cell.style.padding = "10px";

		// Create div inside of every cell
		var card = document.createElement("div");
		card.className = "card";
		card.id = data[i].id;
		cell.appendChild(card);

		// Create a div for image to fit in
		var imageDiv = document.createElement("div");
		imageDiv.className = "image_div";
		card.appendChild(imageDiv);

		// Add image to the div
		var img = document.createElement("img");
		img.className = "image";
		img.src = data[i].image;
		img.id = data[i].id;
		imageDiv.appendChild(img);
		img.addEventListener("click", (event)=>{
			openProductPage(event.target.id);
		});

		// Create and add div for brand and name to card
		var brandAndName = document.createElement("div");
		brandAndName.className = "brand_name";
		card.appendChild(brandAndName);

		// Add brand and name to brandAndName div
		var brand = document.createElement("h1");
		var name = document.createElement("h2");
		brand.className = "brand";
		name.className = "name";
		brand.innerText = data[i].brand;
		name.innerText = data[i].name;
		brandAndName.appendChild(brand);
		brandAndName.appendChild(name);

		// Add 3 next-line
		var nextLine = document.createElement("br");
		card.appendChild(nextLine);
		card.appendChild(nextLine);
		card.appendChild(nextLine);

		// Add price
		var price = document.createElement("p");
		price.className = "price";
		price.innerText = "$" + data[i].price;
		card.appendChild(price);

		// Calculate and add discounted price
		var discountedPrice = document.createElement("p");
		discountedPrice.className = "discount_price";
		discountedPrice.innerText =
			"$" + data[i].price * (1 - data[i].discount);
		card.appendChild(discountedPrice);

		// Add information
		var info = document.createElement("p");
		info.className = "info";
		info.innerText = data[i].information;
		card.appendChild(info);

		// Add an 'Add to Cart' button
		var button = document.createElement("button");
		button.value = data[i].id;
		button.className = "button";
		button.innerText = "Add to Cart";
		button.addEventListener("click", (e) => {
			if (
				sessionStorage.getItem("CurrentUserID") == "null" ||
				sessionStorage.getItem("CurrentUserID") == undefined
			) {
				alert("You need to sign in first!");
			} else {
				addProductToUsersCart(
					sessionStorage.getItem("CurrentUserID"),
					e.target.value
				);
				changePublicityOfProduct(
					sessionStorage.getItem("CurrentUserID"),
					e.target.value,
					false
				);
			}
		});
		card.appendChild(button);

		// Add an 'Add to Favorites' button
		var buttonFav = document.createElement("button");
		buttonFav.value = data[i].id;
		buttonFav.className = "button_fav";
		buttonFav.innerText = "♥";
		buttonFav.onclick = "getProductId(buttonFav)";
		buttonFav.addEventListener("click", (e) => {
			if (
				sessionStorage.getItem("CurrentUserID") == "null" ||
				sessionStorage.getItem("CurrentUserID") == undefined
			) {
				alert("You need to sign in first!");
			} else {
				addProductToUsersFav(
					sessionStorage.getItem("CurrentUserID"),
					e.target.value
				);
			}
		});
		card.appendChild(buttonFav);
	}
}

function getSelectedBrands() {
	var selectedBrands = [];

	var brandList = document.getElementById("brand_list");

	var brandChecboxes = brandList.getElementsByClassName("checkbox");
	var brandLabels = brandList.getElementsByClassName("label");

	for (let i = 0; i < brandLabels.length; i++) {
		if (brandChecboxes[i].checked) {
			selectedBrands.push(brandLabels[i].innerHTML);
			console.log(brandLabels[i].innerHTML);
		}
	}

	return selectedBrands;
}

function isInFavorites(userID, productID) {
	if (userID == "null" || userID == undefined) {
		return false;
	}

	var request =
		"http://localhost:8080/User/isInFavorites?userId=" +
		userID +
		"&productId=" +
		productID;

	$.getJSON(request, function (data) {
		// boolean
		doesIt = data;
	});

	return doesIt;
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

function changePublicityOfProduct(userID, productID, isPublished) {
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

function openProductPage(productId) {
	location.replace("product.html?id=" + productId);
}
