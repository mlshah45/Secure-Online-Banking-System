<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>External User Activity</title>
</head>
<body>
   <jsp:include page="internalHome.jsp"></jsp:include>
      <form:form class="form-horizontal" commandName="accessInfo" method="post" action="/Sparkys-Bank/UserAccountManagementActivity">
    <fieldset>
    <legend>Enter User Credentials to Review Transactions Details</legend>
    <div>
      <div class="col-lg-10">
           <form:input path="username" class="form-control" id="usernameid" placeholder="User Name" />
           <label style="color:red">${usernameerror}</label>
      </div>
    </div>
    <div class="form-group">
      <div class="col-lg-10 col-lg-offset-2">
        <button type="submit" class="btn btn-primary">Submit</button>
      </div>
    </div>
  </fieldset>
   </form:form>
   <div>
		<table class="table table-striped table-hover ">
			<thead>
				<tr>
					<th>Amount Involved</th>
					<th>Transaction Type</th>
				</tr>
			</thead>
			<c:if test="${not empty transList}">
			<tbody>
			<c:forEach items="${transList}" var="i">
				<tr>
					<td><c:out value="${i.amtInvolved}"/></td>
					<td><c:out value="${i.transactionTypes}"/></td>
				</tr>
			</c:forEach>
			</tbody>
			</c:if>
		</table>
	</div>
   
</body>
</html>