/**
 * String Json Type Check
 * @param {string} str 복사 원하는 Text
 */
function isJson (str) {
    if (typeof str != 'string') {
        return false;
    }
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
};

/**
 * Text Copy Clipboard
 * @param {string} text 복사 원하는 Text
 */
function copyToClipboard (text) {
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
function showAlert (divName, msgText, isWarning) {
    const fadeOutTime = 2700;
    const alertElement = $(`<div class='alert alert-${isWarning ? 'warning' : 'danger'} text-center' role='alert'>${msgText}</div>`);
    
    $(`#${divName}`).prepend(alertElement);
    alertElement.fadeOut(fadeOutTime, 'linear');
};

/**
 * get UrlList of Member
 * @param {Number} page Page Number
 * @param {Number} size Page Size
 */
function getMemberUrlList (page = 1, size = 5) {
    sessionStorage.setItem('memberPage', page);

    $.ajax({
        type: "GET",
        url: '/member/url',
        data: {
            "page" : page,
            "size" : size,
        }
    }).done(function (fragment) {
        $('#urlMemberList').replaceWith(fragment);
    });
};

$(document).ready(function() {
    //const href = $(location).attr('href'); // default : http://localhost:8080/
    const memberPage = Number(sessionStorage.getItem('memberPage'));

    if (!(isNaN(memberPage) || memberPage == 0)) {
        getMemberUrlList(memberPage);
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
            url: '/member/login',
            data: {
                "email" : emailElement.val(),
                "password" : passwordElement.val(),
            },
            success: function(xhr, textStatus, errorThrown) {
                location.reload(); // 새로고침
                sessionStorage.setItem('memberPage', '1');
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
            url: '/member/new',
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
            url: '/member/logout',
            success: function(xhr, textStatus, errorThrown) {
                sessionStorage.removeItem('memberPage');
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
            urlElement.val("");
            return;
        }

        // Already Display Short URL
        ulElement.children().each(function(){
            if ($(this).data('url') == url) {
                showAlert('alertDiv', 'Already you find this URL.', true);
                urlElement.val("");
                return;
            }
        });

        $.ajax({
            type: "POST",
            url: '/short',
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
                const href = $(location).attr('href');
                const encodeUrl = `/redirect/${result}`;
                const liElement = $(`<li class='list-group-item list-group-item-success' data-url=${url}>${url}</li>`);
                
                const linkBtnElement = $("<div></div>");
                // Url Link Tag Add
                linkBtnElement.append(`<a target="_blank" href=${encodeUrl}>${href}${encodeUrl.substring(1)}</a>`);
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
            urlElement.val("");
        });
    });
    
});