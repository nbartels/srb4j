#set($dollar = '$')
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="text-danger">
    <c:out value="${dollar}{it.err.nonFieldUserError}"/>
</div>