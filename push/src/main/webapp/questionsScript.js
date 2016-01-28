'use strict'

var files;

// Add events
$('input[type=file]').on('change', prepareUpload);

// Grab the files and set them to our variable
function prepareUpload(event)
{
    files = event.target.files;
}


$('qForm').on('submit', uploadFiles);


function prepareData(){
    return $('qForm').serialize()
}

// Catch the form submit and upload the files
function uploadFiles(event)
{
    event.stopPropagation(); // Stop stuff happening
    event.preventDefault(); // Totally stop stuff happening

    $.ajax({
        url: '/push/questions',
        type: 'POST',
        data: prepareData(),
        cache: false,
        dataType: 'multipart/form-data',
        processData: false, // Don't process the files
        contentType: "multipart/form-data", // Set content type to false as jQuery will tell the server its a query string request
        success: function(data, textStatus, jqXHR)
        {
            if(typeof data.error === 'undefined')
            {
                // Success so call function to process the form
                submitForm(event, data);
            }
            else
            {
                // Handle errors here
                console.log('ERRORS: ' + data.error);
            }
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            // Handle errors here
            console.log('ERRORS: ' + textStatus);
            // STOP LOADING SPINNER
        }
    });
}
