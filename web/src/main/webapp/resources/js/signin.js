function signinCallback(authResult) {
  if (authResult['access_token']) {
    // Successfully authorized
    // Hide the sign-in button now that the user is authorized, for example:
    //document.getElementById('signinButton').setAttribute('style', 'display: none');
    //window.location.href="https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+authResult['access_token'];

    window.location.href="http://localhost:8080/wikishow/login?token="authResult['access_token'];
    //response.setAttribute('accessToken',authResult['access_token']);
  } else if (authResult['error']) {
    // There was an error.
    // Possible error codes:
    //   "access_denied" - User denied access to your app
    //   "immediate_failed" - Could not automatically log in the user
    // console.log('There was an error: ' + authResult['error']);
  }
}