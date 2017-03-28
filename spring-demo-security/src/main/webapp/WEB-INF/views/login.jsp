<!-- 
	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#taglibs
 -->
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
	<!-- https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#csrf-include-csrf-token -->
	<!--
		<meta id="_csrf" name="_csrf" content="${_csrf.token}" />
		<meta id="_csrf_header" name="_csrf_header" content="${_csrf.headerName}" />
	 -->
	<sec:csrfMetaTags />
	<title>Spring Security</title>
	
	<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	
</head>
<body>
<div class="container">
	<div class="page-header">
	  <h1>Login Page <small>with Bootstrap</small></h1>
	</div>
	<div class="container-fluid">
		<form method="post" action="/login">
		<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> --%>
		  <sec:csrfInput />
		  <div class="form-group">
		    <label for="email">이메일</label>
		    <input type="text" class="form-control" name="email" placeholder="Email" required>
		  </div>
		  <div class="form-group">
		    <label for="password">비밀번호</label>
		    <input type="password" class="form-control" name="password" placeholder="Password" required>
		  </div>
		  <div class="checkbox">
		    <label>
		      <input type="checkbox" name="remember-me" > Remember me
		    </label>
		  </div>
		  <div class="text-center">
		  	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
		  		<span class="text-danger"><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/></span>
		  		<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session"/>
		  	</c:if>
		  </div>
		  <hr>
		  <button type="submit" class="btn btn-default">로그인</button>
		  <!-- <a class="btn btn-default" href="/register">회원가입</a> -->
		</form>
	</div>
	<div class="container">
		
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