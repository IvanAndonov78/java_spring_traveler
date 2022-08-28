import { LoginService } from './LoginService.js';

const api = {

    escapeInput(input) {
        return input.toString()
             .replace(/&/g, "&amp;")
             .replace(/</g, "&lt;")
             .replace(/>/g, "&gt;")
             .replace(/"/g, "&quot;")
             .replace(/'/g, "&#039;")
             .replace(/^\s+|\s+$/g,''); // trim
     },

    redirect(url) {
      window.location.href = url
    },
        
    setCookie(cookieName, cookieVal, minutes) {
      const now = new Date();
      now.setTime(now.getTime() + (minutes * 60 * 1000));
      const expires = "expires="+ now.toUTCString();
      document.cookie = cookieName + "=" + cookieVal + ";" + expires + ";path=/";
    },
            
    getCookie(name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for(var i=0;i < ca.length;i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1,c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
        }
        return null;
    },
    
    killCookie(name) {
        let newCookie = `${name}=${api.getCookie(name)};expires=Thu, 01 Jan 1970 00:00:00 GMT`;
        document.cookie = newCookie;
    },
    
    logout() {
        api.killCookie("userId");
        api.killCookie("roleId");
        api.killCookie("accessToken");
        LoginService.logout();
    },
        
    setCurrencyValue(selectedObject) {

         fetch('/country_options_ext', {
             method: 'GET',
             headers: {
                 'Content-Type': 'application/json'
             }
         })
         .then(function(res){
             return res.json();
         }).then(function(data) {
             if (data !== undefined && data !== null) {
                const selectedCountry = selectedObject.value;
                const currencySelector = document.querySelector('#currency');
                for(let i = 0; i < data.length; i++) {
                    if (selectedCountry === data[i].countryName) {
                        currencySelector.value = data[i].currencyCode;
                    }
                }
             }
         }).catch(function(err) {
             console.log('Error', err);
         });
    },
    
    onSubmitLoginForm() {
        const loginForm = document.querySelector('#loginForm');
        const inputEmail = loginForm['email'].value;
        const inputPassword = loginForm['password'].value;
        const csrfToken = loginForm['csrfToken'].value;
        LoginService.login(inputEmail, inputPassword, csrfToken);
    },
    
    isLoggedIn() {
        return LoginService.isLoggedIn();
    },
    
    hasRole(roleId) {
        return LoginService.hasRole(roleId);
    },
    
    validateBudgetPerCountry(inputBudgetPerCountry) {
        const calcForm = document.querySelector('#calc-form');
        const totalBudget = calcForm['totalBudget'].value;
        const budgetPerCountry = inputBudgetPerCountry.value;
        if (budgetPerCountry < 0 ) {
            window.alert("The Budger per Country should be greater than 0 !");
            calcForm.reset();
        }
        if (budgetPerCountry >= totalBudget) {
            window.alert("The Budger per Country should be less than the Total Budget !");
            calcForm.reset();
        }
        return false;
    },
        
    onInit() {

        LoginService.chronJobLogout();
        
        const loginLink = document.querySelector('#loginLink');
        const logoutLink = document.querySelector('#logoutLink');
        const calcLink = document.querySelector('#calcLink');
        
        if (LoginService.isLoggedIn() && LoginService.hasRole(1)) { 
            if (loginLink !== undefined && loginLink !== null) {
                loginLink.style.display = "none";
            }
            if (logoutLink !== undefined && logoutLink !== null) {
                 logoutLink.style.display = "block";       
            }
            if (calcLink !== undefined && calcLink !== null) {
                  calcLink.style.display = "block";      
            }
        } else {
            if (loginLink !== undefined && loginLink !== null) {
                loginLink.style.display = "block";
            }
            if (logoutLink !== undefined && logoutLink !== null) {
                 logoutLink.style.display = "none";       
            }
            if (calcLink !== undefined && calcLink !== null) {
                  calcLink.style.display = "none";      
            }
        }
    }

};

export { api };
