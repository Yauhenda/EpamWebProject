<%--
  Created by IntelliJ IDEA.
  User: koksR4
  Date: 08.06.2020
  Time: 22:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fun" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>OLener</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<jsp:include page="secondHeader.jsp"></jsp:include>

<c:forEach var="product" items="${requestScope.products}">

    <table style="display: inline">
        <tr>
            <th>
                <a style="text-decoration: none"
                   href="${pageContext.request.contextPath}/product-info?id=${product.id}">
                    <img width="auto" height="auto" src="${product.img}"> </a>
            </th>
            <th>
                <table style="width: 250px">
                    <tr style="border-color: white">
                        <th style="border-color: white; font-size: x-large">
                            <a style="text-decoration: none"
                               href="${pageContext.request.contextPath}/product-info?id=${product.id}">
                                    ${product.brand.name} ${product.model} </a>
                        </th>
                    </tr>
                    <tr>
                        <th width="700px" style="border-color: white; font-size: x-large">
                                ${product.price} р.
                        </th>
                    </tr>
                </table>
            </th>
        </tr>
    </table>
</c:forEach>

<form action="/catalog" method="get">
    <button type="submit" name="TEST" value="add_product"
            style="background: green; color: white">
        TESTT!!!
    </button>
</form>

</body>
</html>