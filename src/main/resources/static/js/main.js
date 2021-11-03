$(function(){
    const href = $(location).attr('href'); // default : http://localhost:8080/
    const fadeOutTime = 2700;

    /**
     * String Json Type Check
     * @param {string} str 복사 원하는 Text
     */
    const isJson = function (str) {
        if (typeof str != 'string') {
            return false;
        }

        try {
            JSON.parse(str);
        } catch (e) {
            return false;
        }
        return true;
    }

    /**
     * Text Copy Clipboard
     * @param {string} text 복사 원하는 Text
     */
    const copyToClipboard = function (text) {
        var $temp = $("<input>");
        $("body").append($temp);
        $temp.val(text).select();
        document.execCommand("copy");
        $temp.remove();
    };

    /**
     * Add Div for Alert
     * @param {string}  divName   div for alert show 
     * @param {string}  msgText   message text
     * @param {boolean} isWarning true -> warning, false -> danger
     */
    const showAlert = function (divName, msgText, isWarning) {
        const alertElement = $(`<div class='alert alert-${isWarning ? 'warning' : 'danger'} text-center' role='alert'>${msgText}</div>`);
        $(`#${divName}`).prepend(alertElement);
        alertElement.fadeOut(fadeOutTime, 'linear');
    };

    if (isJson(sessionStorage.getItem('urlList'))) {
        const urlList = JSON.parse(sessionStorage.getItem('urlList'));
        const memberUrlElement = $('#urlMemberList tbody');

        $.each(urlList, function(idx, url) {
            let trElement = $('<tr></tr>');
            let urlStr = url['url'];

            if (urlStr.length > 20) {
                urlStr = urlStr.substring(0, 20) + '...';
            }

            let urlDate = $.format.date(url['createdDate'], "yyyy-MM-dd hh:mm:ss");

            trElement.append(`<th scope="row">${idx+1}</th>`);
            trElement.append(`<td>${urlStr}</td>`);
            trElement.append(`<td>${url.encodeId}</td>`);
            trElement.append(`<td>${urlDate}</td>`);

            memberUrlElement.append(trElement);
        });

        sessionStorage.removeItem('urlList');
    }
    

    /* Particles.js setting (background image) */
    Particles.init({
        selector: '.background',
        color: '#955B98',
        maxParticles: 220,
        connectParticles: true
    });

    /**
     * Login Button Click Event
     */
     $("#loginBtn").click(function(event){
        event.preventDefault();
        const emailElement = $('#sidebar-wrapper input[name="email"]');
        const passwordElement = $('#sidebar-wrapper input[name="password"]');

        $.ajax({
            type: "POST",
            url: href + 'member/login',
            data: {
                "email" : emailElement.val(),
                "password" : passwordElement.val(),
            },
            success: function(xhr, textStatus, errorThrown) {
                sessionStorage.setItem('urlList', JSON.stringify(xhr['urlList']));
                location.reload();
            },
            error: function(result) {
                showAlert('loginAlertDiv', result.responseJSON['message'], false);
            }
        }).always(function(){ // success, error
            // Login input (Email, Password) init
            emailElement.val('');
            passwordElement.val('');
        });
    });

    /**
     * Add Member Button Click Event
     */
     $("#registerBtn").click(function(event){
        event.preventDefault();
        const emailElement = $('#sidebar-wrapper input[name="email"]');
        const passwordElement = $('#sidebar-wrapper input[name="password"]');

        $.ajax({
            type: "POST",
            url: href + 'member/new',
            data: {
                "email" : emailElement.val(),
                "password" : passwordElement.val(),
            },
            success: function(xhr, textStatus, errorThrown) {
                showAlert('loginAlertDiv', `Register Succcess!!! ${xhr}`, true);
            },
            error: function(xhr, textStatus, errorThrown) {
                showAlert('loginAlertDiv', xhr['responseText'], false);
            }
        }).always(function(){ // success, error
            // Login input (Email, Password) init
            emailElement.val('');
            passwordElement.val('');
        });
    });

    /**
     * Add Member Button Click Event
     */
     $("#logoutBtn").click(function(event){
        event.preventDefault();

        $.ajax({
            type: "POST",
            url: href + 'member/logout',
            success: function(xhr, textStatus, errorThrown) {
                location.reload();
            },
            error: function(result) {
                console.log(result.reponseJson);
            }
        });
    });

    /**
     * Send Short Url user want
     */
    $("#sidebarToggle").click(function(event){
        event.preventDefault();
        document.body.classList.toggle('sb-sidenav-toggled');
        localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
    });
    
    /**
     * Send Short Url user want
     */
    $(".url button.customBtn").click(function() {
        const urlElement = $(".url input[name=url]");
        const ulElement = $("#alertDiv ul.list-group");
        const url = $(".url input[name=url]").val();
        
        // urlElement Validate Action
        if (!url) {
            showAlert('alertDiv', 'Please Input Url you want to Short URL.', true);
            urlElement.val("http://");
            return;
        }

        // Already Display Short URL
        if (ulElement.children([`data-url=${url}`]).length > 0) {
            showAlert('alertDiv', 'Already you find this URL.', true);
            urlElement.val("http://");
            return;
        }
        
        $.ajax({
            type: "POST",
            url: href + 'short',
            data: {
                "url" : url
            },
            success: function(result) {
                
                // Display URL Count Check (Max Display : 5)
                if ($('#alertDiv ul.list-group li').length == 5) {
                    // Ul Tag Last childern remove
                    ulElement.children().last().remove();
                }

                // ADD Tag for show short URL
                const encodeUrl = `${href}redirect/${result}`;
                const liElement = $(`<li class='list-group-item list-group-item-success' data-url=${url}>${url}</li>`);
                
                const linkBtnElement = $("<div></div>");
                // Url Link Tag Add
                linkBtnElement.append(`<a target="_blank" href=${encodeUrl}>${encodeUrl}</a>`);
                // Url Text Copy Button Add
                const copyBtnElement = $("<button type='button' class='btn btn-outline-primary'>COPY</button>");
                copyBtnElement.click(copyToClipboard(encodeUrl));

                linkBtnElement.append(copyBtnElement);
                liElement.append(linkBtnElement);

                ulElement.prepend(liElement);
            },
            error: function(xhr, textStatus, errorThrown) {
                showAlert('alertDiv', xhr['responseText'], false);
            }
        }).always(function(){ // success, error
            // URL input value init
            urlElement.val("http://");
        });
    });
    
});