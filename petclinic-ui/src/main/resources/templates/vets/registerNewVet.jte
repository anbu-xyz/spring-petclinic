@import java.util.List

@param String title
@param List<String> specializations

<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="shortcut icon" type="image/x-icon" href="/resources/images/favicon.png">

    <title>${title}</title>

    <link href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/resources/css/petclinic.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark" role="navigation">
    <div class="container-fluid">
        <a class="navbar-brand" href="/"><span></span></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#main-navbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="main-navbar" style="">
            <ul class="nav navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/" title="home page">
                        <span class="fa fa-home"></span>
                        <span>Home</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/owners/find" title="find owners">
                        <span class="fa fa-search"></span>
                        <span>Find owners</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="/vets.html" title="veterinarians">
                        <span class="fa fa-th-list"></span>
                        <span>Veterinarians</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/oups" title="trigger a RuntimeException to see how it is handled">
                        <span class="fa fa-exclamation-triangle"></span>
                        <span>Error</span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <h1>Vet Registration Form</h1>
    <form action="#" method="post">
        <div class="form-group">
            <label for="firstName">First Name:</label>
            <input type="text" id="firstName" name="firstName" required>
        </div>

        <div class="form-group">
            <label for="lastName">Last Name:</label>
            <input type="text" id="lastName" name="lastName" required>
        </div>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>

        <div class="form-group">
            <label for="phone">Phone Number:</label>
            <input type="tel" id="phone" name="phone" required>
        </div>

        <div class="form-group">
            <label for="licenseNumber">License Number:</label>
            <input type="text" id="licenseNumber" name="licenseNumber" required>
        </div>

        <div class="form-group">
            <label for="specialities">Specialization:</label>
            <select id="specialities" name="specialities" required>
                <option value="">Select a specialization</option>
                @for(String specialization : specializations)
                    <option value="${specialization.toLowerCase()}">${specialization} Practice</option>
                @endfor
            </select>
        </div>

        <div class="form-group">
            <button type="submit">Submit</button>
        </div>
    </form>
</div>
</body>
</html>
