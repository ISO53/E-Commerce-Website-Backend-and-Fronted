const accountLink = document.getElementById("account_link");

accountLink.addEventListener("click", () => {
	
	if (sessionStorage.getItem("CurrentUserID") == "null" || sessionStorage.getItem("CurrentUserID") == undefined) {
		location.replace("login_register.html");
	} else {
		location.replace("account.html");
	}
});