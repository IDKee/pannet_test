<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <#assign path="${springMacroRequestContext.getContextPath()}"/>
    <script href="${path}/js/jquery-2.1.4.min.js"></script>
</head>
<body>
${path}
<form id="from" action="#" method="post">
    <table>
        <tr>
            <td>账号：</td>
            <td><input type="text" id="username" name="username" value=""></td>
        </tr>
        <tr>
            <td>密码：</td>
            <td><input type="password" id="password" name="password" value=""></td>
        </tr>
        <tr>
            <td><button type="submit" id="submit">提交</button></td>
        </tr>
    </table>
</form>
</body>
<script>
    $("#submit").click(function () {
        $.ajax({
            //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "/login" ,//url
            data: $('#form').serialize(),
            success: function (result) {
                console.log(result);//打印服务端返回的数据(调试用)
            },
            error : function() {
                alert("异常！");
            }
        });
    })
</script>
</html>