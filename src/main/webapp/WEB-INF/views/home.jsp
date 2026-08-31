<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Fashion Store - Home</title>

    <link rel="stylesheet"
          href="<c:url value='/assets/css/home.css'/>?v=99999">
</head>

<body>

<!-- NAVBAR -->

<nav class="navbar">

    <h2>Fashion Store</h2>

    <!-- SEARCH -->

    <div class="search-box">

        <form action="products" method="get">

            <input type="text"
                   name="keyword"
                   placeholder="Search by product or category">

            <button type="submit">
                Search
            </button>

        </form>

    </div>

    <!-- NAV LINKS -->

    <div class="nav-links">

        <a href="home">Home</a>

        <a href="products">Products</a>

        <a href="cart">Cart</a>

        <a href="login">Login</a>

    </div>

</nav>

<!-- PAGE -->

<main class="page">

    <!-- HERO SECTION -->

    <section class="hero">

        <div class="hero-text">

            <span class="badge">
                New Season Collection
            </span>

            <h1>
                Discover Your Style<br>
                with Fashion Store
            </h1>

            <p>
                Explore the latest trends in fashion for men,
                women, kids, footwear and accessories.
            </p>

            <a href="products" class="shop-btn">

                Shop Now

            </a>

        </div>

        <!-- HERO IMAGE -->

        <div class="hero-image">

            <img src="<c:url value='/image/brand.jpg'/>"
                 alt="Fashion Banner">

        </div>

    </section>

    <!-- CATEGORY SECTION -->

    <section class="section">

        <h2>Shop by Category</h2>

        <div class="category-grid">

            <c:forEach var="c"
                       items="${categories}">

                <div class="category-card">

                    <h3>

                        ${c.categoryName}

                    </h3>

                    <p>

                        ${c.description}

                    </p>

                    <a href="products?categoryId=${c.categoryId}">

                        Explore

                    </a>

                </div>

            </c:forEach>

        </div>

    </section>

    <!-- LATEST PRODUCTS -->

    <section class="section">

        <h2>Latest Products</h2>

        <div class="product-grid">

            <c:forEach var="p"
                       items="${latestProducts}">

                <div class="product-card">

                    <!-- IMAGE -->

                    <div class="product-image">

                        <c:choose>

                            <c:when test="${not empty p.imageUrl}">

                                <img src="<c:url value='/image/${p.imageUrl}'/>"
                                     alt="${p.productName}">

                            </c:when>

                            <c:otherwise>

                                <div class="no-image">

                                    No Image

                                </div>

                            </c:otherwise>

                        </c:choose>

                    </div>

                    <!-- PRODUCT INFO -->

                    <div class="product-info">

                        <h3>

                            ${p.productName}

                        </h3>

                        <p>

                            ${p.brand}

                        </p>

                        <h4>

                            ₹ ${p.price}

                        </h4>

                        <!-- FIXED VIEW DETAILS -->

                        <a href="product-details?productId=${p.productId}">

                            <button>

                                View Details

                            </button>

                        </a>

                    </div>

                </div>

            </c:forEach>

        </div>

    </section>

</main>

<!-- FOOTER -->

<footer>

    <h3>Fashion Store</h3>

    <p>
        Your one-stop destination for stylish fashion
    </p>

</footer>

</body>
</html>