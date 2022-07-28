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

window.addEventListener("load", (event) => {});

/*#######################################################################################################################################*/

const signUpButton = document.getElementById("signUp");
const signInButton = document.getElementById("signIn");
const container = document.getElementById("container");

const register = document.getElementById("sign_up");
const login = document.getElementById("sign_in");

const accountLink = document.getElementById("account_link");

signUpButton.addEventListener("click", () => {
	// Panel movement
	container.classList.add("right-panel-active");
});

signInButton.addEventListener("click", () => {
	// Panel movement
	container.classList.remove("right-panel-active");
});

register.addEventListener("click", async () => {
	var name_value = document.getElementById("register_name").value;
	var mail_value = document.getElementById("register_mail").value;
	var password_value = document.getElementById("register_password").value;
	var phone_value = document.getElementById("register_phone").value;
	var location_value = document.getElementById("register_location").value;

	const data = {
		name_surname: name_value,
		location: location_value,
		password: password_value,
		mail: mail_value,
		phone_number: phone_value,
		favorites: [],
		shopping_cart: [],
	};

	console.log(data);

	//POST request with body equal on data in JSON format
	await fetch("http://localhost:8080/User/register", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(data),
	})
		.then((response) => response.json())
		//Then with the data from the response in JSON...
		.then((data) => {
			console.log("Success:", data);
			alert("You can sign in now!");
		})
		//Then with the error genereted...
		.catch((error) => {
			console.error("Error:", error);
			alert("Something went wrong!");
		});
});

login.addEventListener("click", async () => {
	var mail_value = document.getElementById("login_mail").value;
	var password_value = document.getElementById("login_password").value;

	// Create a request string
	let requestString = "http://localhost:8080/User/login?";

	// Add mail information to the http request
	requestString += "mail=" + mail_value;

	// Add password information to the http request
	requestString += "&password=" + password_value;

	// Call the http request
	await $.getJSON(requestString, function (data) {
		console.log("girdi");
		sessionStorage.setItem("CurrentUserID", data.id);
	})
		.done(function () {
			location.replace("perfume.html");
			console.log(sessionStorage.getItem("CurrentUserID"));
		})
		.fail(function (jqXHR, textStatus, errorThrown) {
			alert("Something went wrong");
			console.error(textStatus);
		})
		.always(function () {
			console.log("Request ended");
		});
});

accountLink.addEventListener("click", () => {
	
	if (sessionStorage.getItem("CurrentUserID") == "null" || sessionStorage.getItem("CurrentUserID") == undefined) {
		location.replace("login_register.html");
	} else {
		location.replace("account.html");
	}
});

/*#######################################################################################################################################*/
