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

<title>Change PII</title>
</head>
<body>
<jsp:include page="navBarExt.jsp"></jsp:include>

<form class="form-horizontal" action="/Sparkys-Bank/piiChangeRequest">
  <fieldset>
    <div class="form-group">
      <label for="inputPassword" class="col-lg-2 control-label">Identification No</label>
      <div class="col-lg-10">
        <input type="password" class="form-control" name="pii" placeholder="Identification no">
      </div>
    </div>
    <div class="form-group">
      <label for="textArea" class="col-lg-2 control-label">Encrypted Token</label>
      <div class="col-lg-10">
        <textarea class="form-control" rows="3" name="token"></textarea>
       </div>
    </div>
    
    <div class="form-group">
      <div class="col-lg-10 col-lg-offset-2">
        <button class="btn btn-default">Cancel</button>
        <button type="submit" class="btn btn-primary">Submit</button>
      </div>
    </div>
  </fieldset>
</form>
</body>
</html>