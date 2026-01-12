<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Mon Panier - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-4">
    <div class="row mb-4">
        <div class="col-12">
            <h1 class="display-5 fw-bold">
                <i class="bi bi-cart text-primary me-2"></i>
                Mon Panier
            </h1>
            <p class="text-muted">Gérez les articles de votre panier</p>
        </div>
    </div>

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

    <c:choose>
        <c:when test="${empty cart or empty cart.items}">
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
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0">Articles (${cart.productCount})</h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th>Produit</th>
                                    <th class="text-center">Quantité</th>
                                    <th class="text-end">Total</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${cart.itemsList}">
                                    <tr>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/product?id=${item.product.id}"
                                               class="text-decoration-none text-dark">
                                                ${item.product.name}
                                            </a>
                                        </td>
                                        <td class="text-center">
                                            ${item.quantity}
                                        </td>
                                        <td class="text-end">
                                            <strong class="text-primary">
                                                <fmt:formatNumber value="${item.totalCents / 100.0}"
                                                                 type="number"
                                                                 minFractionDigits="2"
                                                                 maxFractionDigits="2"/> €
                                            </strong>
                                        </td>
                                        <td class="text-end">
                                            <form method="post"
                                                  action="${pageContext.request.contextPath}/app/cart/remove"
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

            <div class="d-flex justify-content-between align-items-center mt-4">
                <a href="${pageContext.request.contextPath}/products"
                   class="btn btn-outline-secondary">
                    Continuer mes achats
                </a>
                <div class="fs-5">
                    <strong>Total :</strong>
                    <span class="text-success">
                        <fmt:formatNumber value="${cart.totalCents / 100.0}"
                                         type="number"
                                         minFractionDigits="2"
                                         maxFractionDigits="2"/> €
                    </span>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
