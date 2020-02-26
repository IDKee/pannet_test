<!DOCTYPE>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <div style="color: red">panjie123 </div>

    <div>${test.title!}</div>

<#list list as list>
    <div style="color: blue">${list.title!}</div>
</#list>
</body>
</html>