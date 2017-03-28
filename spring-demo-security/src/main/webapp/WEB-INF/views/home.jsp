<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title>Spring Security</title>
	
	<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	
</head>
<body>
<div class="container">
	<div class="page-header">
	  <h1>Index Page <small>with Bootstrap</small></h1>
	</div>
	<p>

	</p>
	<div class="container-fluid">
		<sec:authorize access="isAuthenticated()">
			
			<dl class="dl-horizontal">
			  <dt>Username</dt>
			  <dd><sec:authentication property="principal.username"/></dd>
			</dl>
			<dl class="dl-horizontal">
			  <dt>Password</dt>
			  <dd>[PROTECTED]</dd>
			</dl>
			<dl class="dl-horizontal">
			  <dt>Enabled</dt>
			  <dd><sec:authentication property="principal.enabled"/></dd>
			</dl>
			<dl class="dl-horizontal">
			  <dt>Authorities</dt>
			  <dd><sec:authentication property="principal.authorities"/></dd>
			</dl>
			<p>
			<sec:authorize access="hasRole('ADMIN') or hasRole('ADMIN_MASTER')">
				This content will only be visible to users who have the "ADMIN" or "ADMIN_MASTER" authority in their list of <tt>GrantedAuthority</tt>s.
			</sec:authorize>
			</p>
			<hr>
			<form action="/logout" method="post" id="logoutForm">
			<p>
				<sec:csrfInput />
				<button class="btn btn-default pull-right" type="submit">로그아웃</button>
			</p>
			</form>
		</sec:authorize>
		<sec:authorize access="isAnonymous()">
			<h4>register and login please</h4>
			<p><a class="btn btn-default" href="/login">로그인 페이지</a></p>	
			<p><a class="btn btn-default" href="/register">회원가입 페이지</a></p>
		</sec:authorize>
	</div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!--[if lt IE 9]>
   <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
   <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</body>
</html>