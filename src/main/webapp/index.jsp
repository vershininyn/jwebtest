<!DOCTYPE html>
<html lang="en">
<head>
    <title>File Upload</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
        table, td, th {
            border-collapse: collapse;
            text-align: justify;
            border: 1px solid black;
        }
    </style>
</head>

<script type="text/javascript">
        var url="http://localhost:8080/jtest/";
        var pbar= undefined;
        var userFilesTable= undefined;
        var filesChooser= undefined;
        var filesIsLoaded= false;

        document.addEventListener("DOMContentLoaded", function() {
            pbar= document.getElementById("progress");
            userFilesTable= document.getElementById("userFilesTable").getElementsByTagName("tbody")[0];
            filesChooser= document.getElementById("file");

            updateUserFileslist();

            document.getElementById("upload").addEventListener("click", function() {
                var files= Array.from(filesChooser.files);
                var filesCount= files.length;

                if (filesCount <= 0) {
                    alert("Please, choose some files to upload");
                    return;
                }

                pbar.max= filesCount;
                uploadFile(files);
            });
        });

        function updateUserFileslist() {
            if (filesIsLoaded) {return;}

           var uploadHttp= new XMLHttpRequest();

           uploadHttp.onreadystatechange= function() {
               if(this.readyState == 4 && this.status == 200) {
                    var resp= JSON.parse(this.responseText);

                    for(var key in resp) {
                        addRowToTable(key,resp[key].toString());
                    }

                    filesIsLoaded= true;
               }
            }

            uploadHttp.open("GET", url+"executeAction?action=getfileslist", true);
            uploadHttp.send();
        }

        function uploadFile(fileArray) {
           var file= fileArray.shift();

           if (!file) {
              alert("All files is uploaded successfully!!!");

              filesChooser.type= '';
              filesChooser.type= 'file';
              pbar.value= 0;

              return;
           }

           var uploadHttp= new XMLHttpRequest();

           uploadHttp.onreadystatechange= function() {
               if(this.readyState == 4 && this.status == 200) {
                    var resp= JSON.parse(this.responseText);

                    addRowToTable(resp.fileid,file.name);

                    pbar.value++;
                    uploadFile(fileArray);
               }
            }

            var fileWrapper= new FormData();
            fileWrapper.append("file", new File([file],file.name,{type: "multipart/form-data"}));

            uploadHttp.open("POST", url+"uploadFile", true);
            uploadHttp.send(fileWrapper);
        }

        function addRowToTable(id,filename) {
            var row= userFilesTable.insertRow(userFilesTable.rows.length);

            row.insertCell(0).appendChild(document.createTextNode(filename));

            var link= document.createElement("a");
            link.href= url+"executeAction?action=downloadfile&id="+id;
            link.appendChild(document.createTextNode(filename));

            row.insertCell(1).appendChild(link);
        }
</script>
<body>
    <input id="file" multiple="true" type="file" name="file">
    <br></br>
    <input id="upload" type="button" value="upload" name="upload"/>
    <progress id="progress" value="0" max="0"></progress>
    <br></br>
    <table id="userFilesTable" width:"25%">
        <thead>
            <tr>
                <th>Filename</th>
                <th>Link</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</body>
</html>