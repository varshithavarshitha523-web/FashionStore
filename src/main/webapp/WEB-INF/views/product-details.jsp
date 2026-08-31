<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Details</title>

    <link rel="stylesheet"
          href="<c:url value='/assets/css/product-details.css'/>?v=20">
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

    <div class="details-card">

        <!-- MAIN IMAGE -->
        <div class="image-box">
            <c:choose>
                <c:when test="${not empty product.imageUrl}">
                    <img src="<c:url value='/image/${product.imageUrl}'/>"
                         alt="${product.productName}"
                         onerror="this.style.display='none'">
                </c:when>

                <c:otherwise>
                    <div class="no-image">No Image</div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- PRODUCT INFO -->
        <div class="info-box">

            <p class="brand">${product.brand}</p>

            <h1>${product.productName}</h1>

            <h2>₹ ${product.price}</h2>

            <h3>Description</h3>
            <p class="desc">${product.description}</p>

            <h3>Available Sizes</h3>

            <form action="<c:url value='/cart?action=add'/>" method="post">

                <div class="sizes">
                    <c:forEach var="v" items="${variants}" varStatus="status">
                        <label class="size-option">
                            <input type="radio"
                                   name="variantId"
                                   value="${v.variantId}"
                                   required
                                   <c:if test="${status.first}">checked</c:if>>
                            <span>${v.size}</span>
                        </label>
                    </c:forEach>
                </div>

                <p class="hint">Select a size and quantity to continue</p>

                <label class="qty-label">Quantity</label>
                <input class="qty-input"
                       type="number"
                       name="quantity"
                       value="1"
                       min="1"
                       required>

                <div class="actions">
                    <button type="submit" class="add-btn">
                        Add to Cart
                    </button>

                    <a href="products" class="back-btn">
                        Back to Products
                    </a>
                </div>

            </form>

        </div>

    </div>

    <!-- RELATED PRODUCTS -->
    <h2 class="related-title">Related Products</h2>

    <div class="related-grid">

        <c:forEach var="rp" items="${relatedProducts}">

            <div class="related-card">

                <div class="related-img">
                    <c:choose>
                        <c:when test="${not empty rp.imageUrl}">
                            <img src="<c:url value='/image/${rp.imageUrl}'/>"
                                 alt="${rp.productName}"
                                 onerror="this.style.display='none'">
                        </c:when>

                        <c:otherwise>
                            <div class="no-image">No Image</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="related-info">

                    <h4>${rp.productName}</h4>

                    <p>${rp.brand}</p>

                    <h3>₹ ${rp.price}</h3>

                    <a href="product-details?productId=${rp.productId}">
                        <button>View Details</button>
                    </a>

                </div>

            </div>

        </c:forEach>

    </div>

</div>

</body>
</html>