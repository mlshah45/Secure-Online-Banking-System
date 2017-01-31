<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Employee Profile</title>
</head>
<body>
   <jsp:include page="hello.jsp"></jsp:include>
   <div>
<form:form commandName="accessInfo" action="transactionReviewRequest" method="post">
		<div class="panel panel-default">
			<div class="panel-heading">Click request transaction review button to initiate review of recent transactions</div>
			<label style="color:red">${Message}</label>
		</div>
	   <input type="submit" value="Request Transactions Review" />
</form:form>
  </div>
</body>
</html>