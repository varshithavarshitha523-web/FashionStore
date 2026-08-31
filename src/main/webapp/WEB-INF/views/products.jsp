<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Products</title>

    <link rel="stylesheet"
          href="<c:url value='/assets/css/products.css'/>?v=700">
</head>

<body>

<div class="navbar">
    <h2>Fashion Store</h2>

    <div class="nav-links">
        <a href="home">Home</a>
        <a href="products">Products</a>
        <a href="cart">Cart</a>
        <a href="login">Login</a>
    </div>
</div>

<div class="hero">
    <h1>Explore Our Fashion Collection</h1>
    <p>Browse products and discover styles that match your taste</p>
</div>

<div class="back-container">
    <a href="home" class="back-btn">← Back to Home</a>
</div>

<div class="layout">

    <aside class="filter-box">
        <h3>Filter Products</h3>

        <form action="products" method="get">

            <label>Category</label>
            <select name="categoryId">
                <option value="">All Categories</option>
                <option value="1" ${categoryId == '1' ? 'selected' : ''}>Men</option>
                <option value="2" ${categoryId == '2' ? 'selected' : ''}>Women</option>
                <option value="3" ${categoryId == '3' ? 'selected' : ''}>Kids</option>
                <option value="4" ${categoryId == '4' ? 'selected' : ''}>Accessories</option>
                <option value="5" ${categoryId == '5' ? 'selected' : ''}>Footwear</option>
                <option value="6" ${categoryId == '6' ? 'selected' : ''}>Sports</option>
            </select>

            <label>Min Price</label>
            <input type="number"
                   name="minPrice"
                   placeholder="Min Price"
                   value="${minPrice}">

            <label>Max Price</label>
            <input type="number"
                   name="maxPrice"
                   placeholder="Max Price"
                   value="${maxPrice}">

            <button type="submit">Apply Filter</button>

            <a href="products" class="clear-btn">Clear Filter</a>

        </form>
    </aside>

    <main class="products-area">

        <div class="products-header">
            <h2>
                <c:choose>
                    <c:when test="${not empty keyword}">
                        Search Results for "${keyword}"
                    </c:when>
                    <c:otherwise>
                        Products
                    </c:otherwise>
                </c:choose>
            </h2>

            <span>${products.size()} item(s) found</span>
        </div>

        <div class="product-grid">

            <c:choose>
                <c:when test="${empty products}">
                    <div class="empty-box">
                        <h3>No Products Found</h3>
                    </div>
                </c:when>

                <c:otherwise>
                    <c:forEach var="p" items="${products}">
                        <div class="product-card">

                            <div class="product-image">
                                <c:choose>
                                    <c:when test="${not empty p.imageUrl}">
                                        <img src="<c:url value='/image/${p.imageUrl}'/>"
                                             alt="${p.productName}">
                                    </c:when>

                                    <c:otherwise>
                                        <div class="no-image">No Image</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="product-info">
                                <h3>${p.productName}</h3>
                                <p>${p.brand}</p>
                                <h4>₹ ${p.price}</h4>

                                <a href="product-details?productId=${p.productId}">
                                    <button>View Details</button>
                                </a>
                            </div>

                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </div>

    </main>

</div>

</body>
</html>