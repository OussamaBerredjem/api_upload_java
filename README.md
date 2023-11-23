# api_upload_java
# upload file to api in java

'when choose a file it's auto upload to this server'



# Server 1 code : 

This code allows you to upload an file to the `upload` directory.

```php
<?php

$file_path = "upload/"; //replace it with your folder directory

$file_path = $file_path . basename( $_FILES['file']['name']);
if(move_uploaded_file($_FILES['file']['tmp_name'], $file_path)) {
    echo "success";
} else{
    echo "fail";
}

?>

'''php

##when choose a file and click button upload it's auto upload to this server support only image
# Server 2 code : 

This code allows you to upload an file to the `upload` directory.

```php
<?php

$file_path = "upload/"; //replace it with your folder directory

$file_path = $file_path . basename( $_FILES['file']['name']);
if(move_uploaded_file($_FILES['file']['tmp_name'], $file_path)) {
    echo "success";
} else{
    echo "fail";
}

?>


