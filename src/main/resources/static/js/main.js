$(function(){
    const href = $(location).attr('href'); // default : http://localhost:8080/
    const fadeOutTime = 2700;

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
     * Alert Warning for request short URL
     * @param {string} text 경고메시지
     */
    const addWarningAlert = function (warningText) {
        const urlAlertElement = $(`<div class='alert alert-warning text-center' role='alert'>${warningText}</div>`);
        $("#alertDiv").prepend(urlAlertElement);
        urlAlertElement.fadeOut(fadeOutTime, 'linear');
    };

    /* Particles.js setting(background image) */
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
        const email = $('#sidebar-wrapper input[name="email"]').val();
        const password = $('#sidebar-wrapper input[name="password"]').val();

        $.ajax({
            type: "POST",
            url: href + 'member/login',
            data: {
                "email" : email,
                "password" : password,
            },
            success: function(xhr, textStatus, errorThrown) {
                console.log(xhr);
                console.log(textStatus);
                console.log(errorThrown);
                location.reload();
            },
            error: function(result) {
                const loginAlertElement = $(`<div class='alert alert-danger text-center' role='alert'>${result.responseJSON['message']}</div>`);
                $("#loginAlertDiv").prepend(loginAlertElement);
                loginAlertElement.fadeOut(4000, 'linear');
            }
        });
    });

    /**
     * Add Member Button Click Event
     */
     $("#registerBtn").click(function(event){
        event.preventDefault();
        const email = $('#sidebar-wrapper input[name="email"]').val();
        const password = $('#sidebar-wrapper input[name="password"]').val();

        $.ajax({
            type: "POST",
            url: href + 'member/new',
            data: {
                "email" : email,
                "password" : password,
            },
            success: function(xhr, textStatus, errorThrown) {
                console.log(xhr);
                console.log(textStatus);
                console.log(errorThrown);

            },
            error: function(result) {
                console.log(result.reponseJson);
            }
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
            addWarningAlert('Please Input Url you want to Short URL.');
            urlElement.val("http://");
            return;
        }

        // Already Display Short URL
        if (ulElement.children([`data-url=${url}`]).length > 0) {
            addWarningAlert('Already you find this URL.');
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
                const errElement = $(`<div class="alert alert-danger text-center" role="alert">${xhr.responseText}</div>`);
                $("#alertDiv").prepend(errElement);
                errElement.fadeOut(fadeOutTime, 'linear');
            }
        }).always(function(){ // success, error
            // URL input value init
            urlElement.val("http://");
        });
    });
    
});