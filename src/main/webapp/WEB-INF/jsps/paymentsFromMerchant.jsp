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
		var transactionid=document.getElementById(id).childNodes[1].innerHTML;
		var merchant=document.getElementById(id).childNodes[3].innerHTML;
		var customertoken=document.getElementById("customerToken"+id).value;
		$.ajax({
			type: "POST",
			url:"/Sparkys-Bank/verifyMerchant",
			data:{id: transactionid, merchant:merchant,customertoken:customertoken},
			success: function(data){
				location.reload();
			}
		}
	);
	}
	
	
</script>
<title>Home</title>
</head>

<body>

	<jsp:include page="navBarExt.jsp"></jsp:include>
	<br>
	<br>
	<form>
		<table class="table table-striped table-hover ">
			<thead>
				<tr>
					<th>Transaction</th>
					<th>Merchant</th>
					<th>Amount</th>
					<th>Customer Token</th>
					<th>Verify Merchant</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${transactions}" var="transaction"
					varStatus="status">
					<tr class="active" id="${status.count}">
						<td class="xyz"><c:out value="${transaction.transactionid}"></c:out></td>
						<td><c:out value="${transaction.merchant}"></c:out></td>
						<td><c:out value="${transaction.amount}"></c:out></td>
						<td><input type="text" id="customerToken${status.count}" /></td>
						<td><a href="" onclick="authenticate(${status.count})">Approve</a></td>

					</tr>

				</c:forEach>
			</tbody>

		</table>
		<c:if test="${not empty error}">
			<div class="alert alert-dismissable alert-danger">
				Please add customer token.
			</div>
		</c:if>
		
		<c:if test="${not empty invalid}">
			<div class="alert alert-dismissable alert-danger">
			merchant invalid
			</div>
		</c:if>
	</form>
</body>
</html>