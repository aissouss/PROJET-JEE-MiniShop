<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Inscription - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <!-- Registration Card -->
            <div class="card shadow-lg border-0 rounded-custom">
                <div class="card-body p-5">
                    <!-- Logo/Title -->
                    <div class="text-center mb-4">
                        <i class="bi bi-person-plus display-3 text-primary mb-3"></i>
                        <h2 class="fw-bold">Créer un compte</h2>
                        <p class="text-muted">Rejoignez MiniShop dès aujourd'hui</p>
                    </div>

                    <!-- Error Messages -->
                    <c:if test="${not empty errors}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <strong>Erreurs :</strong>
                            <ul class="mb-0 mt-2">
                                <c:forEach var="error" items="${errors}">
                                    <li>${error}</li>
                                </c:forEach>
                            </ul>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- Registration Form -->
                    <form method="post" action="${pageContext.request.contextPath}/register" novalidate>
                        <!-- Full Name Field -->
                        <div class="mb-3">
                            <label for="fullName" class="form-label">
                                <i class="bi bi-person me-1"></i>
                                Nom complet
                            </label>
                            <input type="text"
                                   class="form-control form-control-lg"
                                   id="fullName"
                                   name="fullName"
                                   placeholder="Jean Dupont"
                                   value="${fullName}"
                                   minlength="2"
                                   maxlength="100"
                                   required
                                   autofocus>
                            <div class="invalid-feedback">
                                Veuillez saisir votre nom complet (minimum 2 caractères).
                            </div>
                        </div>

                        <!-- Email Field -->
                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <i class="bi bi-envelope me-1"></i>
                                Adresse email
                            </label>
                            <input type="email"
                                   class="form-control form-control-lg"
                                   id="email"
                                   name="email"
                                   placeholder="votreemail@example.com"
                                   value="${email}"
                                   required>
                            <div class="invalid-feedback">
                                Veuillez saisir une adresse email valide.
                            </div>
                        </div>

                        <!-- Password Field -->
                        <div class="mb-3">
                            <label for="password" class="form-label">
                                <i class="bi bi-lock me-1"></i>
                                Mot de passe
                            </label>
                            <div class="input-group">
                                <input type="password"
                                       class="form-control form-control-lg"
                                       id="password"
                                       name="password"
                                       placeholder="••••••••"
                                       minlength="6"
                                       maxlength="100"
                                       required>
                                <button class="btn btn-outline-secondary"
                                        type="button"
                                        id="togglePassword"
                                        title="Afficher/Masquer le mot de passe">
                                    <i class="bi bi-eye" id="toggleIcon"></i>
                                </button>
                            </div>
                            <div class="form-text">
                                <i class="bi bi-info-circle me-1"></i>
                                Minimum 6 caractères
                            </div>
                            <div class="invalid-feedback">
                                Le mot de passe doit contenir au moins 6 caractères.
                            </div>
                        </div>

                        <!-- Confirm Password Field -->
                        <div class="mb-4">
                            <label for="confirmPassword" class="form-label">
                                <i class="bi bi-lock-fill me-1"></i>
                                Confirmer le mot de passe
                            </label>
                            <div class="input-group">
                                <input type="password"
                                       class="form-control form-control-lg"
                                       id="confirmPassword"
                                       name="confirmPassword"
                                       placeholder="••••••••"
                                       minlength="6"
                                       maxlength="100"
                                       required>
                                <button class="btn btn-outline-secondary"
                                        type="button"
                                        id="toggleConfirmPassword"
                                        title="Afficher/Masquer le mot de passe">
                                    <i class="bi bi-eye" id="toggleConfirmIcon"></i>
                                </button>
                            </div>
                            <div class="invalid-feedback" id="confirmPasswordFeedback">
                                Veuillez confirmer votre mot de passe.
                            </div>
                        </div>

                        <!-- Terms and Conditions -->
                        <div class="form-check mb-4">
                            <input type="checkbox"
                                   class="form-check-input"
                                   id="terms"
                                   required>
                            <label class="form-check-label" for="terms">
                                J'accepte les
                                <a href="#" class="text-decoration-none">conditions d'utilisation</a>
                                et la
                                <a href="#" class="text-decoration-none">politique de confidentialité</a>
                            </label>
                            <div class="invalid-feedback">
                                Vous devez accepter les conditions d'utilisation.
                            </div>
                        </div>

                        <!-- Submit Button -->
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-person-check me-2"></i>
                                Créer mon compte
                            </button>
                        </div>
                    </form>

                    <!-- Divider -->
                    <div class="text-center my-4">
                        <span class="text-muted">ou</span>
                    </div>

                    <!-- Login Link -->
                    <div class="text-center">
                        <p class="mb-0">
                            Vous avez déjà un compte ?
                            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-bold">
                                Se connecter
                            </a>
                        </p>
                    </div>
                </div>
            </div>

            <!-- Benefits Section -->
            <div class="card mt-3 bg-light border-0">
                <div class="card-body">
                    <h6 class="card-title">
                        <i class="bi bi-star-fill text-warning me-2"></i>
                        Avantages membre
                    </h6>
                    <ul class="small mb-0">
                        <li>Accès à des offres exclusives</li>
                        <li>Suivi de vos commandes en temps réel</li>
                        <li>Historique d'achat et recommandations personnalisées</li>
                        <li>Gestion simplifiée de vos adresses de livraison</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript for password toggle and validation -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    // Toggle password visibility
    function setupPasswordToggle(toggleButtonId, passwordFieldId, iconId) {
        const toggleButton = document.getElementById(toggleButtonId);
        const passwordField = document.getElementById(passwordFieldId);
        const icon = document.getElementById(iconId);

        if (toggleButton && passwordField && icon) {
            toggleButton.addEventListener('click', function() {
                const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordField.setAttribute('type', type);

                if (type === 'text') {
                    icon.classList.remove('bi-eye');
                    icon.classList.add('bi-eye-slash');
                } else {
                    icon.classList.remove('bi-eye-slash');
                    icon.classList.add('bi-eye');
                }
            });
        }
    }

    setupPasswordToggle('togglePassword', 'password', 'toggleIcon');
    setupPasswordToggle('toggleConfirmPassword', 'confirmPassword', 'toggleConfirmIcon');

    // Password match validation
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const confirmPasswordFeedback = document.getElementById('confirmPasswordFeedback');

    function validatePasswordMatch() {
        if (confirmPassword.value && password.value !== confirmPassword.value) {
            confirmPassword.setCustomValidity('Les mots de passe ne correspondent pas');
            confirmPasswordFeedback.textContent = 'Les mots de passe ne correspondent pas';
        } else {
            confirmPassword.setCustomValidity('');
            confirmPasswordFeedback.textContent = 'Veuillez confirmer votre mot de passe.';
        }
    }

    if (password && confirmPassword) {
        password.addEventListener('input', validatePasswordMatch);
        confirmPassword.addEventListener('input', validatePasswordMatch);
    }

    // Form validation
    const form = document.querySelector('form');
    if (form) {
        form.addEventListener('submit', function(event) {
            validatePasswordMatch();

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
