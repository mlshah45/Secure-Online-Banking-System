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

<title>Submit Payment</title>
</head>
<body>

<jsp:include page="navBarExt.jsp"></jsp:include>
	<form class="form-horizontal" action="/Sparkys-Bank/paymentSubmitted" method="post">
		<fieldset>
			<div style="width: 50%;">
				<div class="form-group">
					<label for="inputEmail" class="col-lg-2 control-label">Customer</label>
					<div class="col-lg-10">
						<select name="user">
							<c:forEach var="user" items="${users}">
								<option>${user}</option>

							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword" class="col-lg-2 control-label">Amount</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" name="amount"
							placeholder="Amount">

					</div>
				</div>

				<div class="form-group">
					<label for="inputPassword" class="col-lg-2 control-label">Encrypted
						Token</label>
					<div class="col-lg-10">
						<textarea rows="4" cols="10" class="form-control" name="token"></textarea>

					</div>
				</div>


			</div>


			<div class="form-group">
				<div class="col-lg-10 col-lg-offset-2">
					
					<button type="submit" class="btn btn-primary">Submit</button>
				</div>
			</div>
		</fieldset>
	</form>


</body>

</html>