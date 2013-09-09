<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../../assets/ico/favicon.png">

    <title>Signin Template for Bootstrap</title>

    <h2>${msg}</h2>
    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" media="screen">

    <!-- Custom styles for this template -->
    <link href="<%=request.getContextPath()%>/resources/css/signin.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="<%=request.getContextPath() %>/resources/js/html5shiv.js"></script>
      <script src="<%=request.getContextPath() %>/resources/js/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="container">

      <form class="form-signin">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input type="text" class="form-control" placeholder="Email address" autofocus>
        <input type="password" class="form-control" placeholder="Password">
        <label class="checkbox">
          <input type="checkbox" value="remember-me"> Remember me
        </label>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        <span id="signinButton">
          <span
            class="g-signin"
            data-callback="signinCallback"
            data-clientid="950592796361.apps.googleusercontent.com"
            data-cookiepolicy="single_host_origin"
            data-requestvisibleactions="http://schemas.google.com/AddActivity"
            data-scope="https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar">
          </span>
        </span>

      </form>

      <p>Click below button to redirect the result to new page</p>
      <form method="GET" action="redirect">
      <table>
          <tr>
          <td>
          <input type="submit" value="Redirect Page"/>
          </td>
          </tr>
      </table>
      </form>

    </div> <!-- /container -->

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<%=request.getContextPath() %>/resources/js/bootstrap.min.js"></script>
        <script src="<%=request.getContextPath() %>/resources/js/signin.js"></script>
            <!-- Place this asynchronous JavaScript just before your </body> tag -->
            <script type="text/javascript">
              (function() {
               var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
               po.src = 'https://apis.google.com/js/client:plusone.js';
               var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
             })();
            </script>

  </body>
</html>