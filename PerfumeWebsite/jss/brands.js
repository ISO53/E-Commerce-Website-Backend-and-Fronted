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
	listBrands();
});

window.addEventListener("scroll", (event) => {
	horizontalSliderEffect();
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

function listBrands() {
	$.getJSON("http://localhost:8080/Main/getBrands", function (data) {
		var table = document.getElementById("table");

		deleteAllRows(table);

		addCardsToTable(data, table);
	});
}

function deleteAllRows(table) {
	table.innerHTML = "";
}

function addCardsToTable(data, table) {
	data.forEach((element) => {
		// Create row element
		var row = document.createElement("tr");
		table.appendChild(row);

		// Create cell in row
		var cell = row.insertCell();
		cell.className = "cell";

		// Create header for brand name
		var header = document.createElement("h1");
		header.className = "header1";
		header.innerText = element;
		cell.appendChild(header);
	});
}
