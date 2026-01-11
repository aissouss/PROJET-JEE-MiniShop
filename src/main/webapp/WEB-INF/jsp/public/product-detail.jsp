<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="${product.name} - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-4">
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/home">Accueil</a>
            </li>
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/products">Produits</a>
            </li>
            <li class="breadcrumb-item active" aria-current="page">${product.name}</li>
        </ol>
    </nav>

    <div class="row g-4">
        <div class="col-md-12">
            <h1 class="display-5 fw-bold mb-3">${product.name}</h1>
            <p class="text-muted mb-4">
                <c:choose>
                    <c:when test="${empty product.description}">
                        Description non disponible.
                    </c:when>
                    <c:otherwise>
                        ${product.description}
                    </c:otherwise>
                </c:choose>
            </p>

            <div class="mb-3">
                <span class="display-6 fw-bold text-success">
                    <fmt:formatNumber value="${product.priceCents / 100.0}"
                                     type="number"
                                     minFractionDigits="2"
                                     maxFractionDigits="2"/> €
                </span>
            </div>

            <div class="mb-4">
                <span class="badge bg-secondary">${product.stockStatus}</span>
            </div>

            <div class="d-grid gap-2">
                <c:choose>
                    <c:when test="${product.stock > 0}">
                        <c:choose>
                            <c:when test="${sessionScope.AUTH_USER != null}">
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
                                        <button type="submit" class="btn btn-primary">
                                            <i class="bi bi-cart-plus me-2"></i>
                                            Ajouter au panier
                                        </button>
                                    </div>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <div class="input-group mb-3">
                                    <span class="input-group-text">Quantité</span>
                                    <input type="number"
                                           id="guestQuantity"
                                           class="form-control"
                                           value="1"
                                           min="1"
                                           max="${product.stock}">
                                    <button type="button"
                                            class="btn btn-primary"
                                            onclick="addToGuestCartFromPage()">
                                        <i class="bi bi-cart-plus me-2"></i>
                                        Ajouter au panier
                                    </button>
                                </div>
                                <small class="text-muted">
                                    Connectez-vous pour finaliser votre commande.
                                </small>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-secondary" disabled>
                            Produit indisponible
                        </button>
                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/products"
                   class="btn btn-outline-secondary">
                    Retour au catalogue
                </a>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/cart-local.js"></script>
<script>
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
                'Produit ajouté à votre panier invité !',
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
