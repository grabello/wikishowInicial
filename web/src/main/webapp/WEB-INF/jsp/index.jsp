<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="../../assets/ico/favicon.png">

    <title>WikiShow</title>

    <h2>${msg}</h2>
    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/zocial.css" />

    <!-- Custom styles for this template -->
    <link href="<%=request.getContextPath()%>/resources/css/signin.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="<%=request.getContextPath() %>/resources/js/html5shiv.js"></script>
      <script src="<%=request.getContextPath() %>/resources/js/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="container" >

      <form class="form-signin" action="google-signin">
        <h2 class="form-signin-heading">Please sign in</h2>
        <a href="wikishow/facebook-signin" class="zocial facebook">Sign in with Facebook</a>
        <a href="wikishow/google-signin" class="zocial googleplus">Sign in with Google+</a>
      </form>


    </div> <!-- /container -->

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="//code.jquery.com/jquery.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<%=request.getContextPath() %>/resources/js/bootstrap.min.js"></script>

  </body>
</html>