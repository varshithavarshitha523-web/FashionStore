<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login | Fashion Store</title>

    <link rel="stylesheet"
          href="<c:url value='/assets/css/login.css'/>?v=99">
</head>

<body>

<!-- NAVBAR -->
<nav class="navbar">

    <h2>Fashion Store</h2>

    <form action="<c:url value='/products'/>"
          method="get"
          class="search-box">

        <input type="text"
               name="keyword"
               placeholder="Search for fashion products">

        <button type="submit">
            Search
        </button>

    </form>

    <div class="nav-links">

        <a href="<c:url value='/home'/>">Home</a>

        <a href="<c:url value='/products'/>">Products</a>

        <a href="<c:url value='/cart'/>">Cart</a>

        <a href="<c:url value='/login'/>">Login</a>

    </div>

</nav>

<!-- LOGIN -->
<main class="login-page">

    <div class="login-card">

        <h1>Welcome Back</h1>

        <p>
            Login to continue shopping at Fashion Store
        </p>

        <!-- SUCCESS -->
        <c:if test="${param.registered == 'true'}">

            <div class="success-box">
                Registration successful! Please login.
            </div>

        </c:if>

        <!-- ERROR -->
        <c:if test="${not empty errorMessage}">

            <div class="error-box">
                ${errorMessage}
            </div>

        </c:if>

        <!-- FORM -->
        <form action="<c:url value='/login'/>"
              method="post">

            <label>Email</label>

            <input type="email"
                   name="email"
                   value="${email}"
                   placeholder="Enter your email"
                   required>

            <label>Password</label>

            <input type="password"
                   name="password"
                   placeholder="Enter your password"
                   required>

            <button type="submit">
                Login
            </button>

        </form>

        <!-- REGISTER -->
        <div class="register-link">

            Don’t have an account?

            <a href="<c:url value='/register'/>">
                Register here
            </a>

        </div>

    </div>

</main>

<!-- FOOTER -->
<footer>

    <div>

        <h3>Fashion Store</h3>

        <p>
            Your one-stop destination for stylish fashion
        </p>

    </div>

    <div class="footer-links">

        <a href="#">Privacy Policy</a>

        <a href="#">Terms & Conditions</a>

    </div>

</footer>

</body>
</html>