function customizeActivePage() {
    var numPage = document.getElementById("num_page").getAttribute("data-num_page");
    var elementTop = document.getElementById("top-"+numPage);
    elementTop.classList.add("active");
    var elementBottom = document.getElementById("bottom-"+numPage);
    elementBottom.classList.add("active");
}

window.onload = customizeActivePage;

function activateEdit(userId) {
    var text = document.getElementById("review-text-"+userId);
    $(text).hide();
    var edit = document.getElementById("review-edit-"+userId);
    $(edit).show();
}

function deactivateEdit(userId) {
    var text = document.getElementById("review-text-"+userId);
    $(text).show();
    var edit = document.getElementById("review-edit-"+userId);
    $(edit).hide();
}

(function ($){
    $(function (){
        $('.btn-file').each(function (){
            var self = this;
            $('input[type=file]', this).change(function (){
                // remove existing file info
                $(self).next().remove();
                // get value
                var value = $(this).val();
                // get file name
                var fileName = value.substring(value.lastIndexOf('/')+1);
                // get file extension
                var fileExt = fileName.split('.').pop().toLowerCase();
                // append file info
                $('<span><i class="fa fa-file icon-' + fileExt + '"></i> ' + fileName + '</span>').insertAfter(self);
            });
        });
    });
})(jQuery);

function activateEditStatus() {
    var text = document.getElementById("status");
    $(text).hide();
    var edit = document.getElementById("edit-status");
    $(edit).show();
}

function deactivateEditStatus() {
    var text = document.getElementById("status");
    $(text).show();
    var edit = document.getElementById("edit-status");
    $(edit).hide();
}

$(function () {
    $('#datetimepicker1').datetimepicker();
});