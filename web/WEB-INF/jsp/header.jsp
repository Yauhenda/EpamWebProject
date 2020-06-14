<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: koksR4
  Date: 02.06.2020
  Time: 18:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .divT {
        height: 8%; /* Размеры */
        outline: 1px solid darkgrey; /* Чёрная рамка */
        border: 25px solid #fff; /* Белая рамка */
        border-radius: 20%; /* Радиус скругления */
        margin: auto;
    }
</style>


<div class="divT">

    <div style="display: inline">
        <a style="text-decoration: none" href="${pageContext.request.contextPath}/olener">
            <img src="${pageContext.servletContext.contextPath}/images/logo.jpg">
        </a>

    </div>

    <div style="display: inline; margin-left: 50px">

        <form action="${pageContext.request.contextPath}/catalog" method="post" style="display: inline">

            <input type="text" name="login" style="height: 40px" size="140" placeholder="Поиск в каталоге. Например,
&#34;сварочный аппарат&#34;">

            <button type="submit" style="background: red; color: white; height: 40px; width: 75px"> ПОИСК</button>

        </form>

    </div>

    <c:if test="${ empty sessionScope.customer}">
        <div style="display: inline; margin-left: 50px">
            <a href="/autorization">
                <button type="submit"
                        style="background: silver; border-radius: 20px; color: black; height: 40px; width: 75px">
                    ВОЙТИ
                </button>
            </a>

        </div>

    </c:if>


    <c:if test="${not empty sessionScope.customer}">
        <div style="display: inline; margin-left: 50px">
            <a href="/profile">
                <button type="submit"
                        style="background: silver; border-radius: 20px; color: black; height: 40px; width: 75px">
                        ${sessionScope.customer.login}
                </button>
            </a>

        </div>
    </c:if>


</div>





