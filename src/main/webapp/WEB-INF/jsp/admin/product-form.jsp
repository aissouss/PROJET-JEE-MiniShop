<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="isEdit" value="${mode == 'edit'}"/>
<c:set var="pageTitle" value="${isEdit ? 'Modifier' : 'Créer'} un produit - Admin" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <!-- En-tête -->
            <div class="mb-4">
                <h1 class="display-5 fw-bold">
                    <i class="bi bi-${isEdit ? 'pencil' : 'plus-circle'} text-primary me-2"></i>
                    ${isEdit ? 'Modifier' : 'Créer'} un produit
                </h1>
                <p class="text-muted">Administration</p>
            </div>

            <!-- Formulaire -->
            <div class="card border-0 shadow-lg">
                <div class="card-body p-4">
                    <!-- Message d'erreur -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            ${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form method="post" 
                          action="${pageContext.request.contextPath}/admin/products/${isEdit ? 'edit' : 'create'}"
                          novalidate>
                        
                        <!-- ID caché en mode édition -->
                        <c:if test="${isEdit}">
                            <input type="hidden" name="id" value="${product.id}">
                        </c:if>

                        <!-- Nom du produit -->
                        <div class="mb-3">
                            <label for="name" class="form-label">
                                <i class="bi bi-tag me-1"></i>
                                Nom du produit <span class="text-danger">*</span>
                            </label>
                            <input type="text"
                                   class="form-control form-control-lg"
                                   id="name"
                                   name="name"
                                   value="${isEdit ? product.name : ''}"
                                   placeholder="Ex: Laptop Dell XPS 13"
                                   required
                                   autofocus>
                            <div class="invalid-feedback">
                                Veuillez saisir un nom de produit.
                            </div>
                        </div>

                        <!-- Description -->
                        <div class="mb-3">
                            <label for="description" class="form-label">
                                <i class="bi bi-card-text me-1"></i>
                                Description
                            </label>
                            <textarea class="form-control"
                                      id="description"
                                      name="description"
                                      rows="4"
                                      placeholder="Description détaillée du produit...">${isEdit ? product.description : ''}</textarea>
                        </div>

                        <!-- Prix -->
                        <div class="mb-3">
                            <label for="price" class="form-label">
                                <i class="bi bi-currency-euro me-1"></i>
                                Prix (en euros) <span class="text-danger">*</span>
                            </label>
                            <div class="input-group input-group-lg">
                                <input type="number"
                                       class="form-control"
                                       id="price"
                                       name="price"
                                       value="${isEdit ? product.priceCents / 100.0 : ''}"
                                       step="0.01"
                                       min="0"
                                       placeholder="99.99"
                                       required>
                                <span class="input-group-text">€</span>
                            </div>
                            <div class="form-text">
                                Prix en euros (exemple: 99.99)
                            </div>
                            <div class="invalid-feedback">
                                Veuillez saisir un prix valide.
                            </div>
                        </div>

                        <!-- Stock -->
                        <div class="mb-4">
                            <label for="stock" class="form-label">
                                <i class="bi bi-box-seam me-1"></i>
                                Stock disponible
                            </label>
                            <input type="number"
                                   class="form-control form-control-lg"
                                   id="stock"
                                   name="stock"
                                   value="${isEdit ? product.stock : '0'}"
                                   min="0"
                                   placeholder="0">
                            <div class="form-text">
                                Nombre d'unités disponibles
                            </div>
                        </div>

                        <!-- Boutons -->
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-${isEdit ? 'warning' : 'success'} btn-lg">
                                <i class="bi bi-${isEdit ? 'pencil' : 'plus-circle'} me-2"></i>
                                ${isEdit ? 'Modifier' : 'Créer'} le produit
                            </button>
                            <a href="${pageContext.request.contextPath}/admin/products" 
                               class="btn btn-outline-secondary">
                                <i class="bi bi-x-circle me-2"></i>
                                Annuler
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Informations complémentaires en mode édition -->
            <c:if test="${isEdit}">
                <div class="alert alert-info mt-3">
                    <i class="bi bi-info-circle me-2"></i>
                    <strong>Produit ID:</strong> ${product.id}
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- JavaScript pour validation -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    }
});
</script>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>

