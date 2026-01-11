<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur serveur - MiniShop</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-md-8 text-center">
                <div class="card shadow-lg border-0">
                    <div class="card-body p-5">
                        <i class="bi bi-bug display-1 text-danger mb-4"></i>
                        <h1 class="display-4 fw-bold text-dark mb-3">500</h1>
                        <h2 class="mb-4">Erreur interne du serveur</h2>
                        <p class="lead text-muted mb-4">
                            Désolé, une erreur s'est produite sur notre serveur.
                            Veuillez réessayer plus tard ou contacter le support.
                        </p>
                        <div class="d-flex justify-content-center gap-3">
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary btn-lg">
                                <i class="bi bi-house me-2"></i>Accueil
                            </a>
                            <a href="javascript:history.back()" class="btn btn-outline-secondary btn-lg">
                                <i class="bi bi-arrow-left me-2"></i>Retour
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
