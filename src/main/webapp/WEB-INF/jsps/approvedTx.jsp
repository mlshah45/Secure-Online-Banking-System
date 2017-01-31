<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>

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
	var customer=document.getElementById(id).childNodes[3].innerHTML;
	$.ajax({
		type: "POST",
		url:"/Sparkys-Bank/verifyCustomer",
		data:{id: transactionid, customer:customer},
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

	<table class="table table-striped table-hover ">
		<thead>
			<tr>
				<th>Transaction</th>
				<th>Customer</th>
				<th>Amount</th>
				<th></th>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${transactions}" var="transaction"
				varStatus="status">
				<tr class="active" id="${status.count}">
						<td class="xyz"><c:out value="${transaction.transactionid}"></c:out></td>
						<td><c:out value="${transaction.customer}"></c:out></td>
						<td><c:out value="${transaction.amount}"></c:out></td>
						<td><a onclick="authenticate(${status.count})">Submit</a></td>
						
				</tr>

			</c:forEach>
		</tbody>

	</table>

</body>
</html>