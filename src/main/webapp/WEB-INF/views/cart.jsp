<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.CartItem" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    List<CartItem> cartItems =
            (List<CartItem>) request.getAttribute("cartItems");

    double grandTotal = 0;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Cart</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/cart.css'/>?v=30">
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

    <div class="hero">
        <h1>Your Shopping Cart</h1>
        <p>Review your selected items before checkout.</p>
    </div>

    <c:if test="${param.success == 'updated'}">
        <div class="success-msg">Cart updated successfully.</div>
    </c:if>

    <div class="cart-layout">

        <div class="cart-left">

            <%
                if (cartItems != null && !cartItems.isEmpty()) {
                    for (CartItem item : cartItems) {
                        double total = item.getPrice() * item.getQuantity();
                        grandTotal += total;
            %>

            <div class="cart-card">

                <div class="cart-img">
                    <span><%= item.getProductName() %></span>
                </div>

                <div class="cart-info">
                    <small>Fashion Store</small>
                    <h2><%= item.getProductName() %></h2>
                    <p>Size: <%= item.getSize() %></p>
                    <h3>₹ <%= item.getPrice() %></h3>

                    <div class="cart-actions">

                        <form action="cart?action=update" method="post" class="qty-form">
                            <input type="hidden" name="variantId" value="<%= item.getVariantId() %>">

                            <label>Qty</label>

                            <input type="number"
                                   name="quantity"
                                   value="<%= item.getQuantity() %>"
                                   min="1">

                            <button type="submit" class="update-btn">
                                Update
                            </button>
                        </form>

                        <form action="cart?action=remove" method="post">
                            <input type="hidden" name="variantId" value="<%= item.getVariantId() %>">

                            <button type="submit" class="remove-btn">
                                Remove
                            </button>
                        </form>

                    </div>
                </div>

                <div class="subtotal">
                    <p>Subtotal</p>
                    <h2>₹ <%= total %></h2>
                </div>

            </div>

            <%
                    }
                } else {
            %>

            <div class="empty-cart">
                <h2>Your cart is empty</h2>
                <a href="products">Continue Shopping</a>
            </div>

            <%
                }
            %>

        </div>

        <div class="summary">
            <h2>Order Summary</h2>

            <div class="summary-row">
                <span>Total</span>
                <strong>₹ <%= grandTotal %></strong>
            </div>

            <a href="checkout" class="checkout-btn">
                Proceed to Checkout
            </a>

            <a href="products" class="continue-btn">
                Continue Shopping
            </a>
        </div>

    </div>

</div>

<footer>
    <h3>Fashion Store</h3>
    <p>Your one-stop destination for stylish fashion</p>
</footer>

</body>
</html>