<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Accueil - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<!-- Hero Section -->
<section class="hero-section bg-light py-5">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-lg-6">
                <h1 class="display-4 fw-bold text-primary mb-3">
                    Bienvenue sur MiniShop
                </h1>
                <p class="lead text-muted mb-4">
                    Découvrez notre sélection de produits de qualité à des prix imbattables.
                    Profitez d'une expérience d'achat simple et sécurisée.
                </p>
                <div class="d-flex gap-3">
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary btn-lg">
                        <i class="bi bi-grid"></i> Voir les produits
                    </a>
                </div>
            </div>
            <div class="col-lg-6 text-center mt-4 mt-lg-0">
                <i class="bi bi-cart-check display-1 text-primary"></i>
            </div>
        </div>
    </div>
</section>

<!-- Features Section -->
<section class="features-section py-5">
    <div class="container">
        <h2 class="text-center mb-5 fw-bold">Pourquoi choisir MiniShop ?</h2>
        <div class="row g-4">
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm hover-card">
                    <div class="card-body text-center p-4">
                        <div class="feature-icon bg-primary bg-opacity-10 rounded-circle p-3 d-inline-block mb-3">
                            <i class="bi bi-truck fs-1 text-primary"></i>
                        </div>
                        <h5 class="card-title fw-bold">Livraison rapide</h5>
                        <p class="card-text text-muted">
                            Livraison gratuite pour toute commande supérieure à 50€
                        </p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm hover-card">
                    <div class="card-body text-center p-4">
                        <div class="feature-icon bg-success bg-opacity-10 rounded-circle p-3 d-inline-block mb-3">
                            <i class="bi bi-shield-check fs-1 text-success"></i>
                        </div>
                        <h5 class="card-title fw-bold">Paiement sécurisé</h5>
                        <p class="card-text text-muted">
                            Vos transactions sont protégées par un cryptage SSL
                        </p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card h-100 border-0 shadow-sm hover-card">
                    <div class="card-body text-center p-4">
                        <div class="feature-icon bg-warning bg-opacity-10 rounded-circle p-3 d-inline-block mb-3">
                            <i class="bi bi-headset fs-1 text-warning"></i>
                        </div>
                        <h5 class="card-title fw-bold">Support 24/7</h5>
                        <p class="card-text text-muted">
                            Notre équipe est disponible pour vous aider à tout moment
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- CTA Section -->
<section class="cta-section bg-primary text-white py-5">
    <div class="container text-center">
        <h2 class="mb-3 fw-bold">Prêt à commencer vos achats ?</h2>
        <p class="lead mb-4">
            Inscrivez-vous dès maintenant et profitez de nos offres exclusives !
        </p>
        <a href="${pageContext.request.contextPath}/products" class="btn btn-light btn-lg">
            <i class="bi bi-grid"></i> Explorer les produits
        </a>
    </div>
</section>

<!-- Stats Section -->
<section class="stats-section py-5 bg-light">
    <div class="container">
        <div class="row text-center g-4">
            <div class="col-md-3">
                <div class="stat-item">
                    <h3 class="display-4 fw-bold text-primary">1000+</h3>
                    <p class="text-muted">Produits disponibles</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-item">
                    <h3 class="display-4 fw-bold text-success">5000+</h3>
                    <p class="text-muted">Clients satisfaits</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-item">
                    <h3 class="display-4 fw-bold text-warning">24/7</h3>
                    <p class="text-muted">Support disponible</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-item">
                    <h3 class="display-4 fw-bold text-info">99%</h3>
                    <p class="text-muted">Satisfaction client</p>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/jsp/common/footer.jspf" %>
