<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="<c:url value="/resources/css/theme.css"/>" rel="stylesheet">
<script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/bootstrap.js"/>"></script>
<script>
function authenticate(id){
		var username=document.getElementById(id).childNodes[3].innerHTML;
		alert(username);
		$.ajax({
			type: "POST",
			url:"/Sparkys-Bank/verifyPIIRequest",
			data:{username:username},
			success: function(data){
				location.reload();
			}
		}
	);
	}
</script>
<title>Review Change PII</title>
</head>
<body>
	<jsp:include page="navBar.jsp"></jsp:include>
	<table class="table table-striped table-hover ">
		<thead>
			<tr>
				<th>Id</th>
				<th>Customer</th>
				<th></th>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${requests}" var="request"
				varStatus="status">
				<tr class="active" id="${status.count}">
						<td><c:out value="${status.count}"></c:out></td>
						<td><c:out value="${request.username}"></c:out></td>
						<td><a onclick="authenticate(${status.count})">Authenticate PII Request</a></td>
						
				</tr>

			</c:forEach>
		</tbody>

	</table>
	

</body>
</html>