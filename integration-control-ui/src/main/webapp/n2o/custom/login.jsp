<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="n2o" uri="http://n2oapp.net/framework/tld" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="mtr/favicon.ico">
    <title>Авторизация</title>
    <link href="mtr/login/mtrLogin.css" rel="stylesheet">
</head>

<body>
<div class="body-background"></div>

<div class="wrapper">
    <div class="logo_container" style="width: 350px;">
        <div class="logo">
            <img src='mtr/login/img/logo-light.png' class='logo_img'>
        </div>
        <div>
            <div class="logo_text_h1">ФЕДЕРАЛЬНЫЙ ФОНД</div>
            <div class="logo_text_h2">ОБЯЗАТЕЛЬНОГО МЕДИЦИНСКОГО СТРАХОВАНИЯ</div>
        </div>
    </div>
    <div class="form_h2">ГОСУДАРСТВЕННАЯ ИНФОРМАЦИОННАЯ СИСТЕМА ОБЯЗАТЕЛЬНОГО МЕДИЦИНСКОГО СТРАХОВАНИЯ</div>
    <div class="form_h1">Подсистема "Интеграционная шина ГИС ОМС"</div>
    <div class="form-wrapper">
        <div class="form">
            <div class="form__header">Вход</div>
            <form class="form__form" action="login" method='POST'>
                <c:if test="${param.error == 'true'}">
                    <div class="form__error-message"><i></i><span>Неверное имя пользователя или пароль</span></div><br/>
                </c:if>
                <div class="form__group">
                    <input id="username" placeholder="Имя пользователя"
                           name="username" value="" type="text"
                           autofocus class="form__input form__input-with-icon" required/>
                    <div class="form__icon" style="background: url('mtr/login/img/Username.svg') no-repeat; top: 14px"></div>
                </div>
                <div class="form__group">
                    <input class="form__input form__input-with-icon"
                           required type="password" id="password" name="password" placeholder="Пароль"/>
                    <div class="form__icon" style="background: url('mtr/login/img/lock_psw.svg') no-repeat"></div>
                </div>
                <div class="form__group">
                    <input name="login" id="kc-login" type="submit" class="form__submit" value="Войти">
                </div>
            </form>
        </div>
    </div>

    <div class="bottom-wrapper">
        <div class="bottom-block">
            <div class="footer">
                <div class="bottom-block__phone">
                    Служба технической поддержки: ${n2o:property('ffoms.support.phone')}
                </div>
                <div class="bottom-block__link">
                    ${n2o:property('ffoms.support.email')}
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>