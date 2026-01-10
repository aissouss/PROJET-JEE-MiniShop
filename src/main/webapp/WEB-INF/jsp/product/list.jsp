<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Catalogue - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-4">
    <!-- Page Header -->
    <div class="row mb-4">
        <div class="col-lg-8">
            <h1 class="display-5 fw-bold">
                <i class="bi bi-grid text-primary me-2"></i>
                Catalogue Produits
            </h1>
            <p class="text-muted">${pageSubtitle}</p>
        </div>
        <div class="col-lg-4">
            <!-- Search Form -->
            <form method="get" action="${pageContext.request.contextPath}/products" class="d-flex">
                <input type="search"
                       name="search"
                       class="form-control"
                       placeholder="Rechercher un produit..."
                       value="${searchTerm}"
                       aria-label="Rechercher">
                <button class="btn btn-primary ms-2" type="submit">
                    <i class="bi bi-search"></i>
                </button>
            </form>
        </div>
    </div>

    <!-- Filters and Results Info -->
    <div class="row mb-4">
        <div class="col-md-12">
            <div class="d-flex justify-content-between align-items-center flex-wrap">
                <!-- Category Filter -->
                <div class="mb-2">
                    <strong>Catégories:</strong>
                    <a href="${pageContext.request.contextPath}/products"
                       class="btn btn-sm ${empty selectedCategory ? 'btn-primary' : 'btn-outline-primary'} ms-2">
                        Toutes
                    </a>
                    <c:forEach var="cat" items="${categories}">
                        <a href="${pageContext.request.contextPath}/products?category=${cat}"
                           class="btn btn-sm ${selectedCategory == cat ? 'btn-primary' : 'btn-outline-primary'} ms-1">
                            ${cat}
                        </a>
                    </c:forEach>
                </div>

                <!-- Results Count -->
                <div class="mb-2">
                    <span class="badge bg-secondary fs-6">
                        ${productCount} produit${productCount > 1 ? 's' : ''}
                    </span>
                </div>
            </div>
        </div>
    </div>

    <!-- Products Grid -->
    <c:choose>
        <c:when test="${empty products}">
            <!-- No Products Found -->
            <div class="row">
                <div class="col-12">
                    <div class="alert alert-info text-center py-5">
                        <i class="bi bi-inbox display-1 text-muted"></i>
                        <h4 class="mt-3">Aucun produit trouvé</h4>
                        <p class="text-muted">
                            <c:choose>
                                <c:when test="${not empty searchTerm}">
                                    Aucun produit ne correspond à votre recherche "${searchTerm}".
                                </c:when>
                                <c:otherwise>
                                    Il n'y a actuellement aucun produit disponible dans cette catégorie.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary mt-2">
                            <i class="bi bi-arrow-left me-2"></i>
                            Voir tous les produits
                        </a>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Product Cards -->
            <div class="row g-4">
                <c:forEach var="product" items="${products}">
                    <div class="col-sm-6 col-md-4 col-lg-3">
                        <div class="card h-100 product-card shadow-sm hover-card">
                            <!-- Product Image -->
                            <c:choose>
                                <c:when test="${not empty product.imageUrl}">
                                    <img src="${product.imageUrl}"
                                         class="card-img-top"
                                         alt="${product.name}"
                                         onerror="this.src='https://via.placeholder.com/300x250?text=${product.name}'">
                                </c:when>
                                <c:otherwise>
                                    <img src="https://via.placeholder.com/300x250?text=${product.name}"
                                         class="card-img-top"
                                         alt="${product.name}">
                                </c:otherwise>
                            </c:choose>

                            <div class="card-body d-flex flex-column">
                                <!-- Product Name -->
                                <h5 class="card-title">
                                    <a href="${pageContext.request.contextPath}/product?id=${product.id}"
                                       class="text-decoration-none text-dark">
                                        ${product.name}
                                    </a>
                                </h5>

                                <!-- Product Description (truncated) -->
                                <p class="card-text text-muted small flex-grow-1">
                                    <c:choose>
                                        <c:when test="${product.description.length() > 80}">
                                            ${product.description.substring(0, 80)}...
                                        </c:when>
                                        <c:otherwise>
                                            ${product.description}
                                        </c:otherwise>
                                    </c:choose>
                                </p>

                                <!-- Product Category -->
                                <c:if test="${not empty product.category}">
                                    <div class="mb-2">
                                        <span class="badge bg-info text-dark">
                                            ${product.category}
                                        </span>
                                    </div>
                                </c:if>

                                <!-- Product Price -->
                                <div class="mb-2">
                                    <span class="product-price text-success fw-bold fs-4">
                                        <fmt:formatNumber value="${product.priceCents / 100.0}"
                                                         type="number"
                                                         minFractionDigits="2"
                                                         maxFractionDigits="2"/> €
                                    </span>
                                </div>

                                <!-- Stock Status -->
                                <div class="mb-3">
                                    <c:choose>
                                        <c:when test="${product.stock > 0}">
                                            <small class="text-success">
                                                <i class="bi bi-check-circle"></i>
                                                ${product.stockStatus}
                                            </small>
                                        </c:when>
                                        <c:otherwise>
                                            <small class="text-danger out-of-stock">
                                                <i class="bi bi-x-circle"></i>
                                                ${product.stockStatus}
                                            </small>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Action Button -->
                                <a href="${pageContext.request.contextPath}/product?id=${product.id}"
                                   class="btn btn-primary w-100 mt-auto">
                                    <i class="bi bi-eye me-2"></i>
                                    Voir les détails
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Custom CSS for product cards -->
<style>
.product-card {
    transition: all 0.3s ease;
}

.product-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 25px rgba(0,0,0,0.15) !important;
}

.product-card .card-img-top {
    height: 250px;
    object-fit: cover;
    transition: transform 0.3s ease;
}

.product-card:hover .card-img-top {
    transform: scale(1.05);
}

.product-card .card-title a {
    transition: color 0.2s ease;
}

.product-card .card-title a:hover {
    color: var(--bs-primary) !important;
}
</style>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
