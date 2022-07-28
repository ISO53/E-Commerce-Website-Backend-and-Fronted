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
	listAll();
	listBrands();
});

const accountLink = document.getElementById("account_link");

accountLink.addEventListener("click", () => {
	
	if (sessionStorage.getItem("CurrentUserID") == "null" || sessionStorage.getItem("CurrentUserID") == undefined) {
		location.replace("login_register.html");
	} else {
		location.replace("account.html");
	}
});

/*#######################################################################################################################################*/

function searchByWord() {
	var val = document.getElementById("search").value;

	$.getJSON(
		"http://localhost:8080/Main/search?word=" + val,
		function (data) {
			var table = document.getElementById("table");

			deleteAllRows(table);

			addCardsToTable(data, table);
		}
	);
}

function listByFilters() {
	// Create a request string
	let requestString = "http://localhost:8080/Main/getByFilters?";

	// Add selected brands to the http request as parameters
	var selectedBrands = getSelectedBrands();
	requestString += "brands=";
	for (let i = 0; i < selectedBrands.length; i++) {
		if (i == selectedBrands.length - 1) {
			requestString += selectedBrands[i];
		} else {
			requestString += selectedBrands[i] + ",";
		}
	}

	// Add price range to the http request as parameter
	var priceRange = getPriceRange();
	requestString +=
		"&price=" +
		(isNaN(priceRange[0]) ? 0 : priceRange[0]) +
		"," +
		(isNaN(priceRange[1]) ? 99999999 : priceRange[1]);

	console.log(requestString);

	// Add sorting type to the http request as parameter
	let sortingType = document.querySelector('input[name="sort_by"]:checked').value;
	requestString += "&sort_by=" + sortingType;

	// Add boolean value for ascending or descending sorting to the http request as parameter
	let isAscending = document.getElementById("is_ascending").checked;
	requestString += "&is_ascending=" + isAscending;

	// Call the http request
	$.getJSON(requestString, function (data) {
		var table = document.getElementById("table");

		deleteAllRows(table);

		addCardsToTable(data, table);
	});
}

function listAll() {
	$.getJSON("http://localhost:8080/Main/getAll", function (data) {
		var table = document.getElementById("table");

		deleteAllRows(table);

		addCardsToTable(data, table);
	});
}

function listBrands() {
	$.getJSON("http://localhost:8080/Main/getBrands", function (data) {
		var list = document.getElementById("brand_list");

		list.innerHTML = '<li class="list" id="brand_list">Brand</li><br>';

		for (let i = 0; i < data.length; i++) {
			// Create a (anchor) for checkbox and label
			var anchor = document.createElement("a");
			list.appendChild(anchor);

			// Add next-line between checkboxes
			anchor.appendChild(document.createElement("br"));

			// Create and add checkbox to te list for every brand
			var checkbox = document.createElement("input");
			checkbox.setAttribute("type", "checkbox");
			checkbox.className = "checkbox";
			checkbox.disabled = false;
			checkbox.checked = true;
			anchor.appendChild(checkbox);

			// Create label for checkbox
			var label = document.createElement("label");
			label.setAttribute("for", "checkbox");
			label.className = "label";
			label.innerHTML = data[i];
			anchor.appendChild(label);

			// Add event listener to the checkbox
			checkbox.addEventListener("click", (event) => {
				//alert("asd");
			});
		}
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
				sessionStorage.getItem("CurrentUserID") == "null" || sessionStorage.getItem("CurrentUserID") == undefined
			) {
				alert("You need to sign in first!");
			} else {
				addProductToUsersCart(sessionStorage.getItem("CurrentUserID"), e.target.value);
				changePublicityOfProduct(sessionStorage.getItem("CurrentUserID"), e.target.value, false);
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
				sessionStorage.getItem("CurrentUserID") == "null" || sessionStorage.getItem("CurrentUserID") == undefined
			) {
				alert("You need to sign in first!");
			} else {
				addProductToUsersFav(sessionStorage.getItem("CurrentUserID"), e.target.value);
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

function getPriceRange() {
	var lowerPrice = document.getElementById("lower_price");
	var upperPrice = document.getElementById("upper_price");

	return [parseInt(lowerPrice.value), parseInt(upperPrice.value)];
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

	$.getJSON(request, function (data) { // boolean
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

function removeProductFromUsersFav(userID, productID) {
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

function changePublicityOfProduct(userID, productID, isPublished) {
	var request =
		"http://localhost:8080/Main/setPublished?productId=" +
		productID +
		"&isPublished=" +
		isPublished;

	$.getJSON(request, function (data) {
		if (data != null) {
			alert("product succesfully removed from the users cart");
		}
	});
}

function openProductPage(productId) {
	location.replace("product.html?id=" + productId);
}
