<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Mon Panier - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-4">
    <!-- Page Header -->
    <div class="row mb-4">
        <div class="col-12">
            <h1 class="display-5 fw-bold">
                <i class="bi bi-cart text-primary me-2"></i>
                Mon Panier
            </h1>
            <p class="text-muted">Gérez les articles de votre panier</p>
        </div>
    </div>

    <!-- Validation Messages -->
    <c:if test="${not empty validationMessages}">
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            <strong>Attention!</strong> Certains articles ont été ajustés:
            <ul class="mb-0 mt-2">
                <c:forEach var="message" items="${validationMessages}">
                    <li>${message}</li>
                </c:forEach>
            </ul>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Cart Content -->
    <c:choose>
        <c:when test="${empty cart or cart.empty}">
            <!-- Empty Cart -->
            <div class="row">
                <div class="col-12">
                    <div class="card border-0 shadow-sm text-center py-5">
                        <div class="card-body">
                            <i class="bi bi-cart-x display-1 text-muted mb-4"></i>
                            <h3 class="mb-3">Votre panier est vide</h3>
                            <p class="text-muted mb-4">
                                Vous n'avez pas encore ajouté de produits à votre panier.
                            </p>
                            <a href="${pageContext.request.contextPath}/products"
                               class="btn btn-primary btn-lg">
                                <i class="bi bi-grid me-2"></i>
                                Découvrir nos produits
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <!-- Cart with Items -->
            <div class="row">
                <!-- Cart Items -->
                <div class="col-lg-8 mb-4">
                    <div class="card border-0 shadow-sm">
                        <div class="card-header bg-white py-3">
                            <h5 class="mb-0">
                                <i class="bi bi-bag-check me-2"></i>
                                Articles (${cart.productCount} ${cart.productCount > 1 ? 'produits' : 'produit'})
                            </h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Produit</th>
                                            <th class="text-center">Prix unitaire</th>
                                            <th class="text-center">Quantité</th>
                                            <th class="text-end">Total</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${cart.itemsList}">
                                            <tr>
                                                <!-- Product Info -->
                                                <td>
                                                    <div class="d-flex align-items-center">
                                                        <c:choose>
                                                            <c:when test="${not empty item.product.imageUrl}">
                                                                <img src="${item.product.imageUrl}"
                                                                     alt="${item.product.name}"
                                                                     class="rounded me-3"
                                                                     style="width: 80px; height: 80px; object-fit: cover;"
                                                                     onerror="this.src='https://via.placeholder.com/80?text=${item.product.name}'">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img src="https://via.placeholder.com/80?text=${item.product.name}"
                                                                     alt="${item.product.name}"
                                                                     class="rounded me-3"
                                                                     style="width: 80px; height: 80px; object-fit: cover;">
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <div>
                                                            <h6 class="mb-1">
                                                                <a href="${pageContext.request.contextPath}/product?id=${item.product.id}"
                                                                   class="text-decoration-none text-dark">
                                                                    ${item.product.name}
                                                                </a>
                                                            </h6>
                                                            <c:if test="${not empty item.product.category}">
                                                                <small class="text-muted">
                                                                    <i class="bi bi-tag"></i>
                                                                    ${item.product.category}
                                                                </small>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </td>

                                                <!-- Unit Price -->
                                                <td class="text-center">
                                                    <span class="fw-bold text-success">
                                                        <fmt:formatNumber value="${item.product.priceCents / 100.0}"
                                                                         type="number"
                                                                         minFractionDigits="2"
                                                                         maxFractionDigits="2"/> €
                                                    </span>
                                                </td>

                                                <!-- Quantity -->
                                                <td class="text-center">
                                                    <span class="badge bg-secondary fs-6">
                                                        ${item.quantity}
                                                    </span>
                                                </td>

                                                <!-- Total Price -->
                                                <td class="text-end">
                                                    <strong class="fs-5 text-primary">
                                                        <fmt:formatNumber value="${item.totalCents / 100.0}"
                                                                         type="number"
                                                                         minFractionDigits="2"
                                                                         maxFractionDigits="2"/> €
                                                    </strong>
                                                </td>

                                                <!-- Remove Button -->
                                                <td class="text-end">
                                                    <form method="post"
                                                          action="${pageContext.request.contextPath}/app/cart/remove"
                                                          style="display: inline;"
                                                          onsubmit="return confirm('Êtes-vous sûr de vouloir retirer ce produit ?');">
                                                        <input type="hidden" name="productId" value="${item.product.id}">
                                                        <button type="submit"
                                                                class="btn btn-sm btn-outline-danger"
                                                                title="Retirer du panier">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <!-- Continue Shopping Button -->
                    <div class="mt-3">
                        <a href="${pageContext.request.contextPath}/products"
                           class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left me-2"></i>
                            Continuer mes achats
                        </a>
                    </div>
                </div>

                <!-- Cart Summary -->
                <div class="col-lg-4 mb-4">
                    <div class="card border-0 shadow-lg cart-total sticky-top" style="top: 20px;">
                        <div class="card-body p-4">
                            <h5 class="card-title mb-4">
                                <i class="bi bi-calculator me-2"></i>
                                Récapitulatif
                            </h5>

                            <!-- Summary Details -->
                            <div class="mb-3">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Articles (${cart.itemCount}):</span>
                                    <span class="fw-bold">
                                        <fmt:formatNumber value="${cart.totalCents / 100.0}"
                                                         type="number"
                                                         minFractionDigits="2"
                                                         maxFractionDigits="2"/> €
                                    </span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Livraison:</span>
                                    <c:choose>
                                        <c:when test="${cart.totalCents >= 5000}">
                                            <span class="text-success fw-bold">GRATUITE</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="fw-bold">4,90 €</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <hr>

                            <!-- Total -->
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h5 class="mb-0">Total:</h5>
                                <h3 class="mb-0 text-primary">
                                    <c:choose>
                                        <c:when test="${cart.totalCents >= 5000}">
                                            <fmt:formatNumber value="${cart.totalCents / 100.0}"
                                                             type="number"
                                                             minFractionDigits="2"
                                                             maxFractionDigits="2"/> €
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${(cart.totalCents + 490) / 100.0}"
                                                             type="number"
                                                             minFractionDigits="2"
                                                             maxFractionDigits="2"/> €
                                        </c:otherwise>
                                    </c:choose>
                                </h3>
                            </div>

                            <!-- Free Shipping Progress -->
                            <c:if test="${cart.totalCents < 5000}">
                                <div class="alert alert-info mb-3">
                                    <small>
                                        <i class="bi bi-truck me-1"></i>
                                        Plus que
                                        <strong>
                                            <fmt:formatNumber value="${(5000 - cart.totalCents) / 100.0}"
                                                             type="number"
                                                             minFractionDigits="2"
                                                             maxFractionDigits="2"/> €
                                        </strong>
                                        pour la livraison gratuite !
                                    </small>
                                </div>
                            </c:if>

                            <!-- Checkout Button -->
                            <div class="d-grid">
                                <a href="${pageContext.request.contextPath}/checkout"
                                   class="btn btn-primary btn-lg">
                                    <i class="bi bi-credit-card me-2"></i>
                                    Commander
                                </a>
                            </div>

                            <!-- Trust Badges -->
                            <div class="text-center mt-4 pt-3 border-top">
                                <small class="text-muted d-block mb-2">
                                    <i class="bi bi-shield-check text-success me-1"></i>
                                    Paiement 100% sécurisé
                                </small>
                                <small class="text-muted d-block">
                                    <i class="bi bi-arrow-repeat text-primary me-1"></i>
                                    Retours gratuits sous 30 jours
                                </small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Cart merge script -->
<script src="${pageContext.request.contextPath}/assets/js/cart-merge.js"></script>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
