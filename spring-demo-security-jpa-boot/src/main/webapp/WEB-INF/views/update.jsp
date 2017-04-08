<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title>Spring Security + JPA</title>
	
	<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	
</head>
<body>
<div class="container">
	<div class="page-header">
	  <h1>Update Page <small>with Bootstrap</small></h1>
	</div>
	<div class="container-fluid">
		<form data-toggle="validator" role="form" action="/users/<sec:authentication property="principal.id"/>" method="post">
		<input type="hidden" name="id" value="<sec:authentication property="principal.id"/>"/>
		<sec:csrfInput />
		  <div class="form-group">
		    <label for="email">이메일</label>
		    <input type="email" name="email" class="form-control" placeholder="Email" value="<sec:authentication property="principal.email"/>" data-error="올바른 이메일 형식이 아닙니다" required readonly>
		  	<div class="help-block with-errors"></div>
		  </div>
		  <div class="form-group">
		    <label for="nickname">닉네임</label>
		    <input type="text" name="nickname" class="form-control" placeholder="Nickname" data-error="반드시 입력해주세요" value="<sec:authentication property="principal.nickname"/>" required>
		    <div class="help-block with-errors"></div>
		  </div>
		  <div class="form-group">
		    <label for="password">비밀번호 확인</label>
		    <input type="password" name="password" class="form-control" placeholder="Password" pattern="[A-Za-z]{1}[A-Za-z0-9]{7,19}$" data-error="영문으로 시작하여 8자 이상 20이하의 영문, 숫자 조합이 가능합니다"  required>
		    <div class="help-block with-errors"></div>
		  </div>
		  <button type="submit" class="btn btn-default">수정하기</button>
		  <a class="btn btn-default" href="/">메인으로</a>
		</form>
	</div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!--[if lt IE 9]>
   <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
   <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>
</body>
</html>