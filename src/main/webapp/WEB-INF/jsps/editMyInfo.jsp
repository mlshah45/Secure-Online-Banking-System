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
   <h4>Please note that only the Address information is editable</h4>
   <label style="color:red">${msg}</label>
<form:form commandName="accessInfo" action="requestEdit" method="post">
		<div class="panel panel-default">
			<div class="panel-heading">First Name</div>
			<form:input path="firstname" readonly="true"/>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">Middle Name</div>
            <form:input path="middlename" readonly="true"/>
   		</div>
		<div class="panel panel-default">
			<div class="panel-heading">Last Name</div>
			<form:input path="lastname" readonly="true"/>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">User Name</div>
			<form:input path="username" readonly="true"/>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">Modified Email ID</div>
			<form:input path="email" readonly="true"/>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading">Address</div>
			<form:input path="address" />
			<label style="color:red">${addresserror}</label>
	   </div>
	   <input type="submit" value="Update Details" />
  </form:form>
  </div>
</body>
</html>