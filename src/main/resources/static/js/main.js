$(function(){
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
        
        if (!url) {
            $("#urlInvaildAlert").text('Please Input Url you want to short url.');
            $("#urlInvaildAlert").addClass('show').css('z-index', 1).fadeOut('slow');
            urlElement.val("http://");
            return;
        }
        
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/short",
            data: {
                "url" : url
            },
            success: function(result) {
                // url input value init
                urlElement.val("http://");
                
//                const encodeUrl = $(location).attr('href') + result;
//                var alert = $('div').addClass('alert alert-success').prop('role', 'alert').text('test');
//                
//                console.log( $("div.url"))
//                
////                $("div.url").insertAfter(alert);
            }
        });
    });
    
});