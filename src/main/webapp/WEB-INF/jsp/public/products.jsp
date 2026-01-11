<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Catalogue - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-4">
    <div class="row mb-4">
        <div class="col-12">
            <h1 class="display-5 fw-bold">
                <i class="bi bi-grid text-primary me-2"></i>
                Catalogue Produits
            </h1>
            <p class="text-muted">${productCount} produit${productCount > 1 ? 's' : ''}</p>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty products}">
            <div class="alert alert-info text-center py-5">
                <i class="bi bi-inbox display-1 text-muted"></i>
                <h4 class="mt-3">Aucun produit trouvé</h4>
                <p class="text-muted">Le catalogue est vide pour le moment.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <c:forEach var="product" items="${products}">
                    <div class="col-sm-6 col-md-4 col-lg-3">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title">
                                    <a href="${pageContext.request.contextPath}/product?id=${product.id}"
                                       class="text-decoration-none text-dark">
                                        ${product.name}
                                    </a>
                                </h5>
                                <p class="card-text text-muted small flex-grow-1">
                                    <c:choose>
                                        <c:when test="${empty product.description}">
                                            Description non disponible.
                                        </c:when>
                                        <c:when test="${product.description.length() > 80}">
                                            ${product.description.substring(0, 80)}...
                                        </c:when>
                                        <c:otherwise>
                                            ${product.description}
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <div class="mb-2">
                                    <span class="text-success fw-bold">
                                        <fmt:formatNumber value="${product.priceCents / 100.0}"
                                                         type="number"
                                                         minFractionDigits="2"
                                                         maxFractionDigits="2"/> €
                                    </span>
                                </div>
                                <small class="text-muted mb-3">${product.stockStatus}</small>
                                <a href="${pageContext.request.contextPath}/product?id=${product.id}"
                                   class="btn btn-primary w-100 mt-auto">
                                    Voir le détail
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
