<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Spring Demo Security</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
</head>
<body>
    <div class="container p-5">
        <div class="jumbotron">
            <h1 class="display-4">Hello, world!</h1>
            <p class="lead">This project is example using spring-security-oauth2 modules.</p>
            <hr class="my-4">
            <p>You can check how to set configuration for oauth2 authentication system.</p>
            <#if !(authentication?exists) || (authentication.principal == "anonymousUser")>
                <a class="btn btn-primary btn-lg" href="/login" role="button">Login</a>
            <#else>
                <a class="btn btn-primary btn-lg" href="/logout" role="button">Logout</a>
            </#if>
        </div>
    </div>

    <#if authentication?exists && !(authentication.principal == "anonymousUser")>
        <div class="container px-5">
            <p>${authentication.principal}</p>
            <p>You can authorize to client <code>/oauth/authorize?client_id=e0113ca5-486f-43e9-9615-674b2232caed&response_type=code</code></p>
        </div>
    </#if>

    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
</body>
</html>