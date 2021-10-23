$(function(){
    const href = $(location).attr('href'); // default : http://localhost:8080/

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

    /* Particles.js setting(background image) */
    Particles.init({
        selector: '.background',
        color: '#955B98',
        maxParticles: 220,
        connectParticles: true
    });
    
    /**
     * Send Short Url user want
     */
    $(".url button.customBtn").click(function() {
        const urlElement = $(".url input[name=url]");
        const url = $(".url input[name=url]").val();
        
        // urlElement Validate Action
        if (!url) {
            const urlAlertElement = $("<div class='alert alert-warning text-center' role='alert'>Please Input Url you want to short url.</div>");
            $("#alertDiv").prepend(urlAlertElement);
            urlAlertElement.fadeOut(2800, 'linear');
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
                const encodeUrl = `${href}redirect/${result}`;
                const resultElement = $(`<li class='list-group-item list-group-item-success'>${url}</li>`);
                // Url Link Tag Add
                resultElement.append(`<a target="_blank" href=${encodeUrl}>${encodeUrl}</a>`);
                // Url Text Copy Button Add
                const copyBtnElement = $("<button type='button' class='btn btn-outline-primary'>COPY</button>");
                copyBtnElement.click(copyToClipboard(encodeUrl));
                resultElement.append(copyBtnElement);

                $("#alertDiv .list-group").append(resultElement);
            },
            error: function(xhr, textStatus, errorThrown) {
                const errElement = $(`<div class="alert alert-danger text-center" role="alert">${xhr.responseText}</div>`);
                $("#alertDiv").prepend(errElement);
                errElement.fadeOut(2500, 'linear');
            }
        }).always(function(){ // success, error
            // URL input value init
            urlElement.val("http://");
        });
    });
    
});