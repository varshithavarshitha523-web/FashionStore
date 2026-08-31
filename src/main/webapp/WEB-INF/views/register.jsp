<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Register | Fashion Store</title>

<link rel="stylesheet"
      href="<c:url value='/assets/css/register.css'/>?v=10">

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

<!-- PAGE -->
<main class="register-page">

    <div class="register-card">

        <h1>Create Account</h1>

        <p class="subtitle">
            Join Fashion Store today
        </p>

        <!-- ERROR -->
        <c:if test="${not empty errorMessage}">
            <div class="error-box">
                ${errorMessage}
            </div>
        </c:if>

        <!-- FORM -->
        <form action="<c:url value='/register'/>"
              method="post">

            <div class="form-group">

                <label>Full Name</label>

                <input type="text"
                       name="fullName"
                       value="${fullName}"
                       required>

            </div>

            <div class="row">

                <div class="form-group">

                    <label>Email</label>

                    <input type="email"
                           name="email"
                           value="${email}"
                           required>

                </div>

                <div class="form-group">

                    <label>Phone</label>

                    <input type="text"
                           name="phone"
                           value="${phone}"
                           required>

                </div>

            </div>

            <div class="form-group">

                <label>Password</label>

                <input type="password"
                       name="password"
                       required>

            </div>

            <div class="form-group">

                <label>Address Line 1</label>

                <input type="text"
                       name="addressLine1"
                       value="${addressLine1}"
                       required>

            </div>

            <div class="form-group">

                <label>Address Line 2</label>

                <input type="text"
                       name="addressLine2"
                       value="${addressLine2}">

            </div>

            <div class="row">

                <div class="form-group">

                    <label>City</label>

                    <input type="text"
                           name="city"
                           value="${city}"
                           required>

                </div>

                <div class="form-group">

                    <label>State</label>

                    <input type="text"
                           name="state"
                           value="${state}"
                           required>

                </div>

            </div>

            <div class="row">

                <div class="form-group">

                    <label>Pincode</label>

                    <input type="text"
                           name="pincode"
                           value="${pincode}"
                           required>

                </div>

                <div class="form-group">

                    <label>Country</label>

                    <input type="text"
                           name="country"
                           value="${country != null ? country : 'India'}"
                           required>

                </div>

            </div>

            <button type="submit"
                    class="register-btn">

                Register

            </button>

        </form>

        <!-- FOOTER -->
        <div class="login-link">

            Already have an account?

            <a href="<c:url value='/login'/>">
                Login
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