<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Connexion - MiniShop" scope="request"/>
<%@ include file="/WEB-INF/jsp/common/header.jspf" %>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <!-- Login Card -->
            <div class="card shadow-lg border-0 rounded-custom">
                <div class="card-body p-5">
                    <!-- Logo/Title -->
                    <div class="text-center mb-4">
                        <i class="bi bi-shop display-3 text-primary mb-3"></i>
                        <h2 class="fw-bold">Connexion</h2>
                        <p class="text-muted">Accédez à votre compte MiniShop</p>
                    </div>

                    <!-- Error Message -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            ${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <!-- Login Form -->
                    <form method="post" action="${pageContext.request.contextPath}/login" novalidate>
                        <!-- Hidden redirect URL -->
                        <c:if test="${not empty redirectUrl}">
                            <input type="hidden" name="redirectUrl" value="${redirectUrl}">
                        </c:if>

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
                                   required
                                   autofocus>
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
                                       required>
                                <button class="btn btn-outline-secondary"
                                        type="button"
                                        id="togglePassword"
                                        title="Afficher/Masquer le mot de passe">
                                    <i class="bi bi-eye" id="toggleIcon"></i>
                                </button>
                            </div>
                            <div class="invalid-feedback">
                                Veuillez saisir votre mot de passe.
                            </div>
                        </div>

                        <!-- Remember Me -->
                        <div class="form-check mb-4">
                            <input type="checkbox"
                                   class="form-check-input"
                                   id="rememberMe"
                                   name="rememberMe">
                            <label class="form-check-label" for="rememberMe">
                                Se souvenir de moi
                            </label>
                        </div>

                        <!-- Submit Button -->
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-box-arrow-in-right me-2"></i>
                                Se connecter
                            </button>
                        </div>
                    </form>

                    <div class="text-center my-4">
                        <span class="text-muted">Accès au catalogue après connexion</span>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<!-- JavaScript for password toggle -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const togglePassword = document.getElementById('togglePassword');
    const password = document.getElementById('password');
    const toggleIcon = document.getElementById('toggleIcon');

    if (togglePassword) {
        togglePassword.addEventListener('click', function() {
            const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
            password.setAttribute('type', type);

            if (type === 'text') {
                toggleIcon.classList.remove('bi-eye');
                toggleIcon.classList.add('bi-eye-slash');
            } else {
                toggleIcon.classList.remove('bi-eye-slash');
                toggleIcon.classList.add('bi-eye');
            }
        });
    }

    // Form validation
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
