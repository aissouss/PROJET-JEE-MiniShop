<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="${product.name} - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/home">Accueil</a>
            </li>
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/products">Produits</a>
            </li>
            <c:if test="${not empty product.category}">
                <li class="breadcrumb-item">
                    <a href="${pageContext.request.contextPath}/products?category=${product.category}">
                        ${product.category}
                    </a>
                </li>
            </c:if>
            <li class="breadcrumb-item active" aria-current="page">${product.name}</li>
        </ol>
    </nav>

    <!-- Product Details -->
    <div class="row g-4">
        <!-- Product Image -->
        <div class="col-md-6">
            <div class="card border-0 shadow-lg">
                <c:choose>
                    <c:when test="${not empty product.imageUrl}">
                        <img src="${product.imageUrl}"
                             class="card-img-top rounded"
                             alt="${product.name}"
                             style="max-height: 500px; object-fit: cover;"
                             onerror="this.src='https://via.placeholder.com/500x500?text=${product.name}'">
                    </c:when>
                    <c:otherwise>
                        <img src="https://via.placeholder.com/500x500?text=${product.name}"
                             class="card-img-top rounded"
                             alt="${product.name}"
                             style="max-height: 500px; object-fit: cover;">
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Product Information -->
        <div class="col-md-6">
            <div class="product-details">
                <!-- Product Name -->
                <h1 class="display-5 fw-bold mb-3">${product.name}</h1>

                <!-- Category Badge -->
                <c:if test="${not empty product.category}">
                    <div class="mb-3">
                        <a href="${pageContext.request.contextPath}/products?category=${product.category}"
                           class="badge bg-info text-dark fs-6 text-decoration-none">
                            <i class="bi bi-tag me-1"></i>
                            ${product.category}
                        </a>
                    </div>
                </c:if>

                <!-- Product Price -->
                <div class="mb-4">
                    <span class="product-price display-4 fw-bold text-success">
                        <fmt:formatNumber value="${product.priceCents / 100.0}"
                                         type="number"
                                         minFractionDigits="2"
                                         maxFractionDigits="2"/> €
                    </span>
                </div>

                <!-- Stock Status -->
                <div class="mb-4">
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <div class="alert alert-success" role="alert">
                                <i class="bi bi-check-circle-fill me-2"></i>
                                <strong>${product.stockStatus}</strong>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-danger" role="alert">
                                <i class="bi bi-x-circle-fill me-2"></i>
                                <strong>${product.stockStatus}</strong>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Product Description -->
                <div class="mb-4">
                    <h5 class="fw-bold mb-3">Description</h5>
                    <p class="lead text-muted">${product.description}</p>
                </div>

                <!-- Product Features -->
                <div class="mb-4">
                    <h5 class="fw-bold mb-3">Informations produit</h5>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item d-flex justify-content-between">
                            <span><i class="bi bi-tag-fill text-primary me-2"></i>Référence:</span>
                            <strong>#${product.id}</strong>
                        </li>
                        <li class="list-group-item d-flex justify-content-between">
                            <span><i class="bi bi-box-seam text-primary me-2"></i>Stock disponible:</span>
                            <strong>
                                <c:choose>
                                    <c:when test="${product.stock > 0}">
                                        ${product.stock} unité${product.stock > 1 ? 's' : ''}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-danger">Rupture de stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </strong>
                        </li>
                        <c:if test="${not empty product.category}">
                            <li class="list-group-item d-flex justify-content-between">
                                <span><i class="bi bi-grid-fill text-primary me-2"></i>Catégorie:</span>
                                <strong>${product.category}</strong>
                            </li>
                        </c:if>
                    </ul>
                </div>

                <!-- Action Buttons -->
                <div class="d-grid gap-2">
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <c:choose>
                                <c:when test="${sessionScope.AUTH_USER != null}">
                                    <!-- Add to Cart form -->
                                    <form method="post" action="${pageContext.request.contextPath}/app/cart/add">
                                        <input type="hidden" name="productId" value="${product.id}">
                                        <div class="input-group mb-3">
                                            <span class="input-group-text">Quantité</span>
                                            <input type="number"
                                                   name="quantity"
                                                   class="form-control"
                                                   value="1"
                                                   min="1"
                                                   max="${product.stock}"
                                                   required>
                                            <button type="submit" class="btn btn-primary btn-lg">
                                                <i class="bi bi-cart-plus me-2"></i>
                                                Ajouter au panier
                                            </button>
                                        </div>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <!-- Guest cart - Add to localStorage -->
                                    <div class="guest-cart-section">
                                        <div class="input-group mb-3">
                                            <span class="input-group-text">Quantité</span>
                                            <input type="number"
                                                   id="guestQuantity"
                                                   class="form-control"
                                                   value="1"
                                                   min="1"
                                                   max="${product.stock}">
                                            <button type="button"
                                                    class="btn btn-primary btn-lg"
                                                    onclick="addToGuestCartFromPage()">
                                                <i class="bi bi-cart-plus me-2"></i>
                                                Ajouter au panier
                                            </button>
                                        </div>
                                        <small class="text-muted">
                                            <i class="bi bi-info-circle me-1"></i>
                                            Article ajouté à votre panier invité.
                                            <a href="${pageContext.request.contextPath}/login?redirect=/product?id=${product.id}">
                                                Connectez-vous
                                            </a>
                                            pour finaliser votre commande.
                                        </small>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-secondary btn-lg" disabled>
                                <i class="bi bi-x-circle me-2"></i>
                                Produit indisponible
                            </button>
                        </c:otherwise>
                    </c:choose>

                    <a href="${pageContext.request.contextPath}/products"
                       class="btn btn-outline-secondary">
                        <i class="bi bi-arrow-left me-2"></i>
                        Retour au catalogue
                    </a>
                </div>

                <!-- Trust Badges -->
                <div class="row mt-4 pt-4 border-top">
                    <div class="col-4 text-center">
                        <i class="bi bi-shield-check display-6 text-success"></i>
                        <p class="small mt-2 mb-0">Paiement sécurisé</p>
                    </div>
                    <div class="col-4 text-center">
                        <i class="bi bi-truck display-6 text-primary"></i>
                        <p class="small mt-2 mb-0">Livraison rapide</p>
                    </div>
                    <div class="col-4 text-center">
                        <i class="bi bi-arrow-repeat display-6 text-info"></i>
                        <p class="small mt-2 mb-0">Retours faciles</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Additional Information -->
    <div class="row mt-5">
        <div class="col-12">
            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <ul class="nav nav-tabs" id="productTabs" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="description-tab" data-bs-toggle="tab"
                                    data-bs-target="#description" type="button" role="tab">
                                Description détaillée
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="shipping-tab" data-bs-toggle="tab"
                                    data-bs-target="#shipping" type="button" role="tab">
                                Livraison & Retours
                            </button>
                        </li>
                    </ul>
                    <div class="tab-content p-4" id="productTabsContent">
                        <div class="tab-pane fade show active" id="description" role="tabpanel">
                            <h5 class="mb-3">À propos de ce produit</h5>
                            <p>${product.description}</p>
                            <p class="text-muted">
                                Référence produit: <strong>#${product.id}</strong>
                            </p>
                        </div>
                        <div class="tab-pane fade" id="shipping" role="tabpanel">
                            <h5 class="mb-3">Informations de livraison</h5>
                            <ul>
                                <li>Livraison gratuite pour les commandes de plus de 50€</li>
                                <li>Délai de livraison standard: 3-5 jours ouvrables</li>
                                <li>Retours acceptés sous 30 jours</li>
                                <li>Politique de retour simple et sans frais</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Guest cart scripts -->
<script src="${pageContext.request.contextPath}/assets/js/cart-local.js"></script>
<script>
// Add to guest cart function
function addToGuestCartFromPage() {
    const quantityInput = document.getElementById('guestQuantity');
    const quantity = parseInt(quantityInput?.value || 1);
    const productId = ${product.id};
    const productName = "${product.name}";
    const price = ${product.priceCents};

    if (quantity > 0 && window.MiniShopGuestCart) {
        const success = window.MiniShopGuestCart.add(productId, quantity, productName, price);

        if (success) {
            window.MiniShopGuestCart.showNotification(
                'Produit ajouté à votre panier invité ! Connectez-vous pour finaliser votre commande.',
                'success'
            );
        } else {
            window.MiniShopGuestCart.showNotification(
                'Erreur lors de l\'ajout au panier',
                'danger'
            );
        }
    }
}
</script>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
