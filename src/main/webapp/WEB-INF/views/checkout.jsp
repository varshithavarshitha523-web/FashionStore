<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.CartItem" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    List<CartItem> cartItems =
            (List<CartItem>) request.getAttribute("cartItems");

    Double total =
            (Double) request.getAttribute("total");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>

    <link rel="stylesheet"
          href="<c:url value='/assets/css/checkout.css'/>?v=40">
</head>

<body>

<!-- NAVBAR -->
<nav class="navbar">

    <h2>Fashion Store</h2>

    <div class="nav-links">
        <a href="home">Home</a>
        <a href="products">Products</a>
        <a href="cart">Cart</a>
        <a href="login">Login</a>
    </div>

</nav>

<!-- PAGE -->
<div class="container">

    <!-- HERO -->
    <div class="hero">

        <h1>Checkout</h1>

        <p>
            Review your order and confirm your delivery details
        </p>

    </div>

    <!-- LAYOUT -->
    <div class="checkout-layout">

        <!-- LEFT -->
        <div class="left">

            <div class="box">

                <h2>Delivery Details</h2>

                <div class="form-group">
                    <label>Full Name</label>
                    <input type="text"
                           value="${user.fullName}"
                           readonly>
                </div>

                <div class="form-group">
                    <label>Phone</label>
                    <input type="text"
                           value="${user.phone}"
                           readonly>
                </div>

                <div class="form-group">
                    <label>Address Line 1</label>
                    <input type="text"
                           value="${user.addressLine1}"
                           readonly>
                </div>

                <div class="form-group">
                    <label>Address Line 2</label>
                    <input type="text"
                           value="${user.addressLine2}"
                           readonly>
                </div>

                <div class="row">

                    <div class="form-group">
                        <label>City</label>
                        <input type="text"
                               value="${user.city}"
                               readonly>
                    </div>

                    <div class="form-group">
                        <label>State</label>
                        <input type="text"
                               value="${user.state}"
                               readonly>
                    </div>

                </div>

                <div class="row">

                    <div class="form-group">
                        <label>Pincode</label>
                        <input type="text"
                               value="${user.pinCode}"
                               readonly>
                    </div>

                    <div class="form-group">
                        <label>Country</label>
                        <input type="text"
                               value="${user.country}"
                               readonly>
                    </div>

                </div>

                <h3 class="payment-title">
                    Payment Method
                </h3>

                <div class="payment-box">
                    Cash On Delivery
                </div>

            </div>

        </div>

        <!-- RIGHT -->
        <div class="right">

            <div class="summary-box">

                <h2>Order Summary</h2>

                <%
                    if(cartItems != null){

                        for(CartItem item : cartItems){
                %>

                <div class="summary-item">

                    <div>

                        <h3>
                            <%= item.getProductName() %>
                        </h3>

                        <p>
                            Size:
                            <%= item.getSize() %>
                            |
                            Qty:
                            <%= item.getQuantity() %>
                        </p>

                    </div>

                    <div class="price">
                        ₹ <%= item.getPrice()
                                * item.getQuantity() %>
                    </div>

                </div>

                <%
                        }
                    }
                %>

                <div class="total-row">

                    <span>Total Amount</span>

                    <strong>
                        ₹ <%= total %>
                    </strong>

                </div>

                <form action="place-order" method="post">

                    <button type="submit"
                            class="place-btn">

                        Place Order

                    </button>

                </form>

                <a href="cart"
                   class="back-btn">

                    Back to Cart

                </a>

            </div>

        </div>

    </div>

</div>

</body>
</html>