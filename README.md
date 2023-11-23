
# upload file to api in java
## make sure to create folder `/image` 

when choose a file it's auto upload to this server



## Server 1 code : 

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

```

## Server 2 code : 

when choose a file and click button upload it's auto upload to this server support only image


```php

<?php

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $imageData = $_POST['file'];

    $imageData = str_replace('data:image/png;base64,', '', $imageData);
    $imageData = str_replace(' ', '+', $imageData);

   
    $decodedImageData = base64_decode($imageData);

    $fileName = uniqid('image_') . '.jpeg';

    $uploadPath = "image/";

      $filePath = $uploadPath . $fileName;

  
    if (file_put_contents($filePath, $decodedImageData)) {
        echo "success";
    } else {
        echo "fail";
    }

    echo 'Image saved successfully!';
} else {
    echo 'Invalid request method';
}

?>




