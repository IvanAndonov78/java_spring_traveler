
const LoginService = {

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
     
     deleteAllCookies() {
         // Does NOT work and should be repair!
         let allCookies = document.cookie.split(';');
         for (let i = 0; i < allCookies.length; i++) {
             document.cookie = allCookies[i] + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
         }
     },
     
     killCookie(name) {
         let newCookie = `${name}=${api.getCookie(name)};expires=Thu, 01 Jan 1970 00:00:00 GMT`;
         document.cookie = newCookie;
     },
     
     logout() {
     
         fetch('/logout', {
             method: 'GET',
             headers: {
                 'Content-Type': 'application/json'
             }
         })
         .then(function(res){
             return res.json();
         }).then(function(data) {
             if (data !== undefined && data !== null) {
                LoginService.killCookie("userId");
                LoginService.killCookie("roleId");
                LoginService.killCookie("accessToken");
                
             }
         }).then(function(){
            LoginService.redirect('http://localhost:8000/');
         }).catch(function(err) {
             console.log('Error', err);
         });
     },

    login(inputEmail, inputPassword, inputCsrfToken) {
                        
        const input = {
            email: LoginService.escapeInput(inputEmail),
            password: LoginService.escapeInput(inputPassword),
            csrfToken: inputCsrfToken
        };

        const url = '/login_ajax';
    
        // const fetchPromise = fetch(url, {
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(input),
        })
        .then(function(res){
            return res.json();
        }).then(function(data) {
            if (!data) {
                redirect('http://localhost:8000/');
            } else {
                LoginService.setCookie('roleId', parseInt(data.roleId), 30);
                LoginService.setCookie('accessToken', data.accessToken, 30);
            }
            return data;
        }).then(function(data){
            LoginService.redirect('http://localhost:8000/calculator_form');
        }).catch(function(err) {
            console.log('Error', err);
        });
        // return fetchPromise;
    },
    
    chronJobLogout() {
      const minutes = 30;
      const timeToReload = minutes * 60 * 1000;
      setInterval(function() {
        let roleId = LoginService.getCookie("roleId");
        let accessToken = LoginService.getCookie('temp_token');
        if (accessToken !== undefined && accessToken !== null) {
          LoginService.killCookie("roleId");
          LoginService.killCookie("accessToken");
          window.alert('Session expired!');
          window.location.href = 'http://localhost:8000/';
        }
      }, timeToReload);
    },
    
    isLoggedIn() {
        const accessToken = LoginService.getCookie('accessToken');
        if (accessToken !== undefined && accessToken !== null) {
            return true;
        }
        return false;
    },
    
    hasRole(roleId) {
        const storedRoleId = parseInt(LoginService.getCookie('roleId'));
        if (storedRoleId !== undefined && storedRoleId !== null && storedRoleId === roleId) {
            return true;
        }
        return false;
    }
        
};

export { LoginService };