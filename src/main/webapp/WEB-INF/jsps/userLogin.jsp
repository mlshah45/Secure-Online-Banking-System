<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="<c:url value="/resources/css/theme.css"/>" rel="stylesheet">
<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>

<script type="text/javascript"
	src="<c:url value="/resources/js/bootstrap.js"/>"></script>
<!-- <script -->
<!-- 	src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> -->
<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script> -->

<title>Sparky's Bank Login Page</title>
</head>
<body onload="document.f.j_username.focus()">

	<div class="jumbotron">
		<h1>Sparky's Bank</h1>

	</div>

	<c:if test="${not empty error }">
		<div style="width: 30%;">
			<div class="alert alert-dismissable alert-danger">
				<strong>Your Login attempt was unsuccessful.</br>

				</strong>
			</div>

		</div>
	</c:if>
	<div class="panel panel-default">
		<div class="panel-body">
			<form:form action="j_spring_security_check" class="form-horizontal"
				method="post" name="f">
				<fieldset>
					<legend>Login</legend>


					<div class="form-group">
						<label for="inputEmail" class="col-lg-2 control-label">Username</label>
						<div class="col-lg-10">
							<input type="text" class="form-control" name="j_username"
								id="inputEmail" placeholder="Username">
						</div>
					</div>
					<div class="form-group">
						<label for="inputPassword" class="col-lg-2 control-label">Password</label>
						<div class="col-lg-10">
							<input type="password" class="form-control" name="j_password"
								id="inputPassword" placeholder="Password">
						</div>
					</div>
					<div class="form-group">
						<div class="col-lg-10 col-lg-offset-2">

							<button type="submit" class="btn btn-primary">Login</button>
						</div>
					</div>
				</fieldset>
			</form:form>
			<div class="col-lg-10 col-lg-offset-2">
				<a href="/Sparkys-Bank/forgotpassword"  class="btn btn-primary">Forgot Password</a>
			</div>
		</div>
	</div>


</body>
</html>
