<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Gestion Produits - Admin" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-4">
    <div class="row mb-4">
        <div class="col-md-8">
            <h1 class="display-5 fw-bold">
                <i class="bi bi-shield-check text-danger me-2"></i>
                Gestion des Produits
            </h1>
            <p class="text-muted">Administration - ${productCount} produit${productCount > 1 ? 's' : ''}</p>
        </div>
        <div class="col-md-4 text-end">
            <a href="${pageContext.request.contextPath}/admin/products/create" class="btn btn-success btn-lg">
                <i class="bi bi-plus-circle me-2"></i>
                Nouveau produit
            </a>
        </div>
    </div>

    <!-- Tableau des produits -->
    <div class="card border-0 shadow-sm">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">
                <i class="bi bi-list-ul me-2"></i>
                Liste des produits
            </h5>
        </div>
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty products}">
                    <div class="alert alert-info m-3">
                        <i class="bi bi-info-circle me-2"></i>
                        Aucun produit dans la base de données.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th width="50">ID</th>
                                    <th>Nom</th>
                                    <th>Description</th>
                                    <th width="120" class="text-end">Prix</th>
                                    <th width="80" class="text-center">Stock</th>
                                    <th width="180" class="text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="product" items="${products}">
                                    <tr>
                                        <td class="text-muted">${product.id}</td>
                                        <td class="fw-bold">${product.name}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty product.description}">
                                                    <span class="text-muted fst-italic">Aucune description</span>
                                                </c:when>
                                                <c:when test="${product.description.length() > 60}">
                                                    ${product.description.substring(0, 60)}...
                                                </c:when>
                                                <c:otherwise>
                                                    ${product.description}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-end">
                                            <span class="badge bg-success">
                                                <fmt:formatNumber value="${product.priceCents / 100.0}"
                                                                 type="number"
                                                                 minFractionDigits="2"
                                                                 maxFractionDigits="2"/> €
                                            </span>
                                        </td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${product.stock <= 0}">
                                                    <span class="badge bg-danger">${product.stock}</span>
                                                </c:when>
                                                <c:when test="${product.stock < 10}">
                                                    <span class="badge bg-warning text-dark">${product.stock}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-success">${product.stock}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <div class="btn-group" role="group">
                                                <a href="${pageContext.request.contextPath}/product?id=${product.id}"
                                                   class="btn btn-sm btn-info"
                                                   title="Voir"
                                                   target="_blank">
                                                    <i class="bi bi-eye"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/admin/products/edit?id=${product.id}"
                                                   class="btn btn-sm btn-warning"
                                                   title="Modifier">
                                                    <i class="bi bi-pencil"></i>
                                                </a>
                                                <form method="post" 
                                                      action="${pageContext.request.contextPath}/admin/products/delete"
                                                      onsubmit="return confirm('Êtes-vous sûr de vouloir supprimer ce produit ?')"
                                                      style="display: inline;">
                                                    <input type="hidden" name="id" value="${product.id}">
                                                    <button type="submit" 
                                                            class="btn btn-sm btn-danger"
                                                            title="Supprimer">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Boutons de navigation -->
    <div class="mt-4">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left me-2"></i>
            Retour à l'accueil
        </a>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-primary ms-2">
            <i class="bi bi-grid me-2"></i>
            Voir le catalogue public
        </a>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>

