@import gg.jte.Content
@import uk.anbu.spring.sample.petclinic.ui.MenuBarItem

@param Content content
@param String activeMenu

<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="shortcut icon" type="image/x-icon" href="/resources/images/favicon.png">

    <title>PetClinic :: a Spring Framework demonstration</title>

    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <link href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/resources/css/petclinic.css" />
</head>

<body>
<nav class="navbar navbar-expand-lg navbar-dark" role="navigation">
    <div class="container-fluid">
        <a class="navbar-brand" href="/"><span></span></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#main-navbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="main-navbar" style>
            <ul class="nav navbar-nav me-auto">
                @for(var currentMenu : MenuBarItem.MENU_ITEMS)
                    <li class="nav-item">
                        <a class="${currentMenu.active().equals(activeMenu) ? "nav-link active" : "nav-link"}" href="${currentMenu.link()}" title="${currentMenu.title()}">
                            <span class="fa fa-${currentMenu.glyph()}"></span>
                            <span>${currentMenu.text()}</span>
                        </a>
                    </li>
                @endfor
            </ul>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <div class="container xd-container">
        ${content}
        <br />
        <br />
        <div class="container">
            <div class="row">
                <div class="col-12 text-center">
                    <img src="/resources/images/spring-logo.svg" alt="VMware Tanzu Logo" class="logo">
                </div>
            </div>
        </div>
    </div>
</div>

<script src="/webjars/bootstrap/5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
