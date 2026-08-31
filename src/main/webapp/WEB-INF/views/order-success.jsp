<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    List<Map<String, Object>> orderItems =
        (List<Map<String, Object>>) request.getAttribute("orderItems");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Successful</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/order.css'/>?v=50">
</head>

<body>

<nav class="navbar">
    <h2>Fashion Store</h2>

    <div class="nav-links">
        <a href="home">Home</a>
        <a href="products">Products</a>
        <a href="cart">Cart</a>
        <a href="login">Login</a>
    </div>
</nav>

<div class="page">

    <div class="success-card">

        <div class="success-icon">✓</div>

        <h1>Order Placed Successfully</h1>

        <p class="subtitle">
            Your order has been placed successfully and is now being processed.
        </p>

        <div class="details-box">

            <div class="detail-row">
                <span>Order ID</span>
                <strong>#${orderId}</strong>
            </div>

            <div class="detail-row">
                <span>Order Date</span>
                <strong>${orderDate}</strong>
            </div>

            <div class="detail-row">
                <span>Payment Method</span>
                <strong>${paymentMethod}</strong>
            </div>

            <div class="detail-row">
                <span>Status</span>
                <strong>${orderStatus}</strong>
            </div>

            <div class="detail-row">
                <span>Total Amount</span>
                <strong>₹ ${totalAmount}</strong>
            </div>

        </div>

        <div class="details-box">

            <h2>Delivery Details</h2>

            <p><strong>${deliveryName}</strong></p>
            <p>${deliveryPhone}</p>
            <p>${deliveryAddress}</p>

        </div>

        <div class="details-box">

            <h2>Ordered Items</h2>

            <%
                if (orderItems != null) {
                    for (Map<String,Object> item : orderItems) {
            %>

            <div class="product-row">

                <div>
                    <h3><%= item.get("productName") %></h3>
                    <p>Size: <%= item.get("size") %></p>
                    <p>Quantity: <%= item.get("quantity") %></p>
                </div>

                <div class="price">
                    ₹ <%= item.get("price") %>
                </div>

            </div>

            <%
                    }
                }
            %>

        </div>

        <a href="products" class="shop-btn">
            Continue Shopping
        </a>

    </div>

</div>

</body>
</html>