// VARIABLES
MONTHS = [
	"January",
	"February",
	"March",
	"April",
	"May",
	"June",
	"July",
	"August",
	"September",
	"October",
	"November",
	"December",
];

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
	loadAccountPage();
});

const logOut = document.getElementById("button_log_out");

const accountLink = document.getElementById("account_link");

logOut.addEventListener("click", () => {
	sessionStorage.setItem("CurrentUserID", "null");
	location.replace("login_register.html");
});

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

function getShoppingCart(cart) {
	cartList = [];

	for (let i = 0; i < cart.length; i++) {
		$.getJSON(
			"http://localhost:8080/Main/getById?id=" + cart[i],
			function (data) {
				cartList.push(data);
			}
		);
	}
	return cartList;
}

function loadAccountPage() {
	var userId = sessionStorage.getItem("CurrentUserID");

	$.getJSON("http://localhost:8080/User/getById?id=" + userId).then(function (
		data
	) {
		console.log(data.favorites);

		loadUserInformations(data);

		loadFavorites(data.favorites);

		loadShoppingCart(data.shopping_cart);
	});
}

function loadUserInformations(data) {
	var nameSurname = document.getElementById("username");
	var id = document.getElementById("ID");
	var mail = document.getElementById("mail");
	var phone = document.getElementById("phone");
	var location = document.getElementById("location");
	var password = document.getElementById("password");

	nameSurname.innerText = data.name_surname;
	id.innerText = data.id;
	mail.innerText = data.mail;
	phone.innerText = data.phone_number;
	location.innerText = data.location;
	password.innerText = "******";
}

async function loadFavorites(favorites) {
	for (let i = 0; i < favorites.length; i++) {
		await $.getJSON(
			"http://localhost:8080/Main/getById?id=" + favorites[i],
			function (data) {
				var table = document.getElementById("favorites");

				var row = table.insertRow(i);
				row.className = "row";

				// Add image to the first cell
				var cell1 = row.insertCell();
				cell1.className = "cell";
				var img = document.createElement("img");
				img.className = "cellIn";
				img.id = "image";
				img.src = data.image;
				cell1.appendChild(img);

				// Add brand to the second cell
				var cell2 = row.insertCell();
				cell2.className = "cell";
				var brand = document.createElement("h1");
				brand.className = "cellIn";
				brand.innerText = data.brand;
				cell2.appendChild(brand);

				// Add name to the thirth cell
				var cell3 = row.insertCell();
				cell3.className = "cell";
				var name = document.createElement("h1");
				name.className = "cellIn";
				name.innerText = data.name;
				cell3.appendChild(name);

				// Add date to the fourth cell
				var cell4 = row.insertCell();
				cell4.className = "cell";
				var date = document.createElement("h1");
				date.className = "cellIn";
				date.innerText = unixToDate(data.date);
				cell4.appendChild(date);

				// Add info to the fifth cell
				var cell5 = row.insertCell();
				cell5.className = "cell";
				var info = document.createElement("h1");
				info.className = "cellIn";
				info.innerText = data.information;
				cell5.appendChild(info);

				// Add price to the sixth cell
				var cell6 = row.insertCell();
				cell6.className = "cell";
				var price = document.createElement("h1");
				price.className = "cellIn";
				price.innerText =
					(data.price * (1 - data.discount)).toFixed(2) + "$";
				cell6.appendChild(price);

				// Add discount to the seventh cell
				var cell7 = row.insertCell();
				cell7.className = "cell";
				var discount = document.createElement("h1");
				discount.className = "cellIn";
				discount.innerText = "%" + data.discount * 100 + " OFF!";
				cell7.appendChild(discount);

				// Add remove button to the eighth cell
				var cell8 = row.insertCell();
				//cell8.className = "cell";
				cell8.id = "button_cell";
				var button = document.createElement("button");
				button.className = "button";
				button.id = "remove_button";
				button.value = data.id;
				button.innerText = "X";
				cell8.appendChild(button);
				button.addEventListener("click", (event) => {
					removeProductFromUsersFav(sessionStorage.getItem("CurrentUserID"), event.target.value);
					changePublicityOfProduct(sessionStorage.getItem("CurrentUserID"), event.target.value, true);
					document.location.reload();
				});
			}
		);
	}
}

async function loadShoppingCart(shoppingCart) {
	for (let i = 0; i < shoppingCart.length; i++) {
		await $.getJSON(
			"http://localhost:8080/Main/getById?id=" + shoppingCart[i],
			function (data) {
				var table = document.getElementById("shopping_cart");

				var row = table.insertRow(i);
				row.className = "row";

				// Add image to the first cell
				var cell1 = row.insertCell();
				cell1.className = "cell";
				var img = document.createElement("img");
				img.className = "cellIn";
				img.id = "image";
				img.src = data.image;
				cell1.appendChild(img);

				// Add brand to the second cell
				var cell2 = row.insertCell();
				cell2.className = "cell";
				var brand = document.createElement("h1");
				brand.className = "cellIn";
				brand.innerText = data.brand;
				cell2.appendChild(brand);

				// Add name to the thirth cell
				var cell3 = row.insertCell();
				cell3.className = "cell";
				var name = document.createElement("h1");
				name.className = "cellIn";
				name.innerText = data.name;
				cell3.appendChild(name);

				// Add date to the fourth cell
				var cell4 = row.insertCell();
				cell4.className = "cell";
				var date = document.createElement("h1");
				date.className = "cellIn";
				date.innerText = unixToDate(data.date);
				cell4.appendChild(date);

				// Add info to the fifth cell
				var cell5 = row.insertCell();
				cell5.className = "cell";
				var info = document.createElement("h1");
				info.className = "cellIn";
				info.innerText = data.information;
				cell5.appendChild(info);

				// Add price to the sixth cell
				var cell6 = row.insertCell();
				cell6.className = "cell";
				var price = document.createElement("h1");
				price.className = "cellIn";
				price.innerText =
					(data.price * (1 - data.discount)).toFixed(2) + "$";
				cell6.appendChild(price);

				// Add discount to the seventh cell
				var cell7 = row.insertCell();
				cell7.className = "cell";
				var discount = document.createElement("h1");
				discount.className = "cellIn";
				discount.innerText = "%" + data.discount * 100 + " OFF!";
				cell7.appendChild(discount);

				// Add remove button to the eighth cell
				var cell8 = row.insertCell();
				//cell8.className = "cell";
				cell8.id = "button_cell";
				var button = document.createElement("button");
				button.className = "button";
				button.id = "remove_button";
				button.value = data.id;
				button.innerText = "X";
				cell8.appendChild(button);
				button.addEventListener("click", (event) => {
					removeProductFromCart(sessionStorage.getItem("CurrentUserID"), event.target.value);
					changePublicityOfProduct(sessionStorage.getItem("CurrentUserID"), event.target.value, true);
					document.location.reload();
				});
			}
		);
	}
}

function unixToDate(unix) {
	var date = new Date(unix * 1000);
	return (
		date.getDay() + " " + MONTHS[date.getMonth()] + " " + date.getFullYear()
	);
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

function removeProductFromCart(userID, productID) {
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
			alert("product publicity succesfully changed!");
		}
	});
}