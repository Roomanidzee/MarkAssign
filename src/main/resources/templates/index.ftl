<!DOCTYPE html>
<html lang="ru">

<head>

    <meta charset="utf-8">

    <title>Title</title>
    <meta name="description" content="">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- Chrome, Firefox OS and Opera -->
    <meta name="theme-color" content="#000">
    <!-- Windows Phone -->
    <meta name="msapplication-navbutton-color" content="#000">
    <!-- iOS Safari -->
    <meta name="apple-mobile-web-app-status-bar-style" content="#000">

    <link rel="stylesheet" href="css/main.min.css">
    <link href="https://use.fontawesome.com/releases/v5.0.8/css/all.css" rel="stylesheet">
</head>

<body>
<div class="content">
    <header>
        <div class="header-container">
            <h1 class="logo top-logo">UNISERVICE</h1>
            <!--<nav>
                <ul>
                    <li><a href="#">Заголовок 1</a></li>
                    <li><a href="#">Заголовок 2</a></li>
                    <li><a href="#">Заголовок 3</a></li>
                    <li><a href="#">Заголовок 4</a></li>
                </ul>
            </nav>&ndash;&gt;-->
        </div>
    </header>

    <div class="container">
        <div class="panel col-lg-6 col-lg-offset-3 centered">
            <div class="login-words">
                <h1>Добро пожаловать на UniService!</h1>
                <h3>Навигация по внеучебной жизни университета</h3>
            </div>

            <form name='form-login' action="/login" method="POST">
                <div class="input-box">
                    <input type="text" name="login" placeholder="Логин" autofocus required>
                    <span><i class="far fa-user-circle"></i></span>
                </div>
                <div class="input-box">
                    <input type="password" name="password" placeholder="Пароль" required>
                    <span><i class="fas fa-lock"></i></span>
                </div>
                <div class="input-box">
                    <label class="remember-me"><input name="" value="remember" type="checkbox" /> &nbsp;Запомнить меня</label>
                    <input type="submit" value="Войти">
                </div>
            </form>
        </div>
    </div>
</div>

<footer>
    <div class="footer-container">
        <div class="footer-text">2018, UniService Team
            <div class="social-media">
                <a href="https://vk.com/itis_kpfu"><i class="fab fa-vk fa-lg"></i></a>
            </div>
        </div>
    </div>
</footer>

<script src="js/scripts.min.js"></script>

</body>
</html>